/* */
create table player.tb_collection (
    col_id int primary key,
    col_title varchar(100) not null,
    col_artist varchar(100) not null,
    col_image_url varchar(255) default null,
    col_created_at timestamp not null default now(),
    col_updated_at timestamp not null default now(),
    col_external_user_id varchar(255) not null
);

create sequence player.tb_collection_id_seq
    minvalue 1
    increment 1
    cache 1
    start 1
    owned by player.tb_collection.col_id;

create index search_ext_user_id_collection on player.tb_collection (col_external_user_id);

CREATE FUNCTION player.update_updated_at_tb_collections()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.col_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_collections_task_updated_on
    BEFORE UPDATE ON player.tb_collection
    FOR EACH ROW
EXECUTE PROCEDURE player.update_updated_at_tb_collections();
/* */

create table player.tb_music (
    mus_id int primary key,
    mus_title varchar(100) not null default 'unknown',
    mus_artist varchar(100) not null default 'unknown',
    mus_genre varchar(100) not null default 'unknown',
    mus_release_year varchar(10) not null default 'unknown',
    mus_cover_url varchar(500) default null,
    mus_composer varchar(200) not null default 'unknown',
    mus_duration_seconds bigint not null,
    mus_file_type varchar(20) not null,
    mus_cover_content_type varchar(20) default null,
    mus_created_at timestamp not null default now(),
    mus_repository_url varchar(255) default null,
    mus_external_user_id varchar(255) not null,
    mus_external_id varchar(255) not null unique,
    mus_collection_id int references player.tb_collection (col_id)
);

create sequence player.tb_music_id_seq
    minvalue 1
    increment 1
    cache 1
    start 1
    owned by player.tb_music.mus_id;

create index search_ext_user_id_music on player.tb_music (mus_external_user_id);

/* */
create table player.tb_lyric (
    lyr_id int primary key,
    lyr_lyric varchar not null,
    lyr_created_at timestamp not null default now(),
    lyr_updated_at timestamp not null default now(),
    lyr_music_id int references player.tb_music (mus_id) not null
);

create sequence player.tb_lyric_id_seq
    minvalue 1
    increment 1
    cache 1
    start 1
    owned by player.tb_lyric.lyr_id;

CREATE FUNCTION player.update_updated_at_tb_lyric()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.lyr_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_lyrics_task_updated_on
    BEFORE UPDATE ON player.tb_lyric
    FOR EACH ROW
EXECUTE PROCEDURE player.update_updated_at_tb_lyric();
/* */

/* */
create table player.tb_queue (
    que_id int primary key,
    que_external_user_id varchar(255) not null,
    que_created_at timestamp not null default now(),
    que_updated_at timestamp not null default now()
);

create index search_ext_user_id_queue on player.tb_queue (que_external_user_id);

create sequence player.tb_queue_id_seq
    minvalue 1
    increment 1
    cache 1
    start 1
    owned by player.tb_queue.que_id;

CREATE FUNCTION player.update_updated_at_tb_queue()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.que_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_queues_task_updated_on
    BEFORE UPDATE ON player.tb_queue
    FOR EACH ROW
EXECUTE PROCEDURE player.update_updated_at_tb_queue();
/* */

/* */
create table player.tb_music_queue (
    muq_position int not null,
    muq_created_at timestamp not null default now(),
    muq_queue_id int not null references player.tb_queue (que_id),
    muq_music_id int not null references player.tb_music (mus_id),

    unique (muq_position, muq_queue_id, muq_music_id),
    constraint music_queue_position_queue_music_pk primary key (muq_position, muq_queue_id, muq_music_id)
);
/* */

/* */
create table player.tb_playlist (
    ply_id int primary key,
    ply_name varchar(200) not null,
    ply_cover_url varchar(255) default null,
    ply_created_at timestamp not null default now(),
    ply_updated_at timestamp not null default now(),
    ply_external_user_id varchar(255) not null
);

create sequence player.tb_playlist_id_seq
    minvalue 1
    increment 1
    cache 1
    start 1
    owned by player.tb_playlist.ply_id;

create index search_ext_user_id_playlist on player.tb_playlist (ply_external_user_id);

CREATE FUNCTION player.update_updated_at_tb_playlist()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.ply_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_playlists_task_updated_on
    BEFORE UPDATE ON player.tb_playlist
    FOR EACH ROW
EXECUTE PROCEDURE player.update_updated_at_tb_playlist();
/* */

/* */
create table player.tb_playlist_music (
    plm_position integer not null,
    plm_created_at timestamp not null default now(),
    plm_updated_at timestamp not null default now(),
    plm_playlist_id int references player.tb_playlist (ply_id),
    plm_music_id int references player.tb_music (mus_id),

    unique(plm_position, plm_playlist_id, plm_music_id),
    constraint playlist_music_position_playlist_music_pk primary key (plm_position, plm_playlist_id, plm_music_id)
);