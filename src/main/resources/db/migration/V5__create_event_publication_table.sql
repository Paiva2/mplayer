create table public.event_publication (
    id UUID primary key,
    event_type varchar(255),
    listener_id varchar,
    serialized_event varchar,
    completion_date timestamp(6) with time zone,
    publication_date timestamp(6) with time zone
)