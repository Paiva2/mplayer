create table tb_role (
    rl_id serial primary key,
    rl_name varchar(20) not null unique,
    rl_created_at timestamp not null default now()
);

create sequence tb_role_id_seq
    minvalue 1
    increment 1
    cache 1
    start 1
    owned by tb_role.rl_id;

insert into tb_role (rl_name) values ('USER'), ('ADMIN');

create table tb_user_role (
    url_user_id serial references tb_user(usr_id),
    url_role_id serial references tb_role(rl_id),
    url_created_at timestamp not null default now(),
    url_updated_at timestamp not null default now()
);

CREATE FUNCTION update_updated_at_tb_user_role()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.url_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_task_updated_on
    BEFORE UPDATE ON tb_user_role
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_user_role();