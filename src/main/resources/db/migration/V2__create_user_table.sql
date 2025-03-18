create table users.tb_user (
    usr_id serial primary key,
    usr_first_name varchar(100) not null,
    usr_last_name varchar(100) not null,
    usr_password varchar(255) not null,
    usr_email varchar(255) unique not null,
    usr_profile_picture varchar(500) default null,
    usr_enabled boolean not null default true,
    usr_created_at timestamp not null default now(),
    usr_updated_at timestamp not null default now()
);

create sequence users.tb_user_id_seq
    minvalue 1
    increment 1
    cache 1
    start 1
    owned by users.tb_user.usr_id;

CREATE FUNCTION update_updated_at_tb_user()
    RETURNS TRIGGER AS $$
        BEGIN
            NEW.usr_updated_at = now();
            RETURN NEW;
        END;
    $$ language 'plpgsql';

CREATE TRIGGER update_users_task_updated_on
    BEFORE UPDATE ON users.tb_user
    FOR EACH ROW
    EXECUTE PROCEDURE update_updated_at_tb_user();