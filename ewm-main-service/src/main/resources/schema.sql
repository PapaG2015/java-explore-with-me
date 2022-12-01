DROP TABLE IF EXISTS compilations_events;
DROP TABLE IF EXISTS compilations;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;



CREATE TABLE IF NOT EXISTS users (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(255),
    email varchar(255),
    CONSTRAINT email_unique UNIQUE (email),
    CONSTRAINT users_name_unique UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS categories (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar,
    CONSTRAINT cat_name_unique UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events (
    annotation varchar,
    category_id bigint REFERENCES categories(id),
    confirmed_requests bigint,
    created_on timestamp,
    description varchar,
    event_date timestamp,
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    initiator_id bigint REFERENCES users(id),
    location_lat float4,
    location_lon float4,
    paid boolean,
    participant_limit int,
    published_on timestamp,
    request_moderation boolean,
    state varchar,
    title varchar
);

CREATE TABLE IF NOT EXISTS comments (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    comment varchar,
    dateOfPublic timestamp,
    event_id bigint REFERENCES events(id),
    user_id bigint REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS requests (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created timestamp,
    event_id bigint REFERENCES events(id),
    requester_id bigint REFERENCES users(id),
    status varchar
);

CREATE TABLE IF NOT EXISTS compilations (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    --Закреплена ли подборка на главной странице сайта
    pinned boolean,
    title varchar
);

CREATE TABLE IF NOT EXISTS compilations_events (
    events_id bigint REFERENCES events(id),
    compilation_id bigint REFERENCES compilations(id)
);

