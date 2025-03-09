create table tb_follower (
    flw_id serial primary key,
    flw_user_id serial references tb_user(usr_id) not null,
    flw_user_followed_id serial references tb_user(usr_id) not null,
    flw_created_at timestamp not null default now()
);

create sequence tb_follower_id_seq
    minvalue 1
    increment 1
    cache 1
    start 1
    owned by tb_follower.flw_id;
