create table users.tb_follower (
    flw_id serial primary key,
    flw_user_id serial references users.tb_user(usr_id) not null,
    flw_user_followed_id serial references users.tb_user(usr_id) not null,
    flw_created_at timestamp not null default now()
);

create sequence users.tb_follower_id_seq
    minvalue 1
    increment 1
    cache 1
    start 1
    owned by users.tb_follower.flw_id;
