DROP TABLE IF EXISTS hits;

CREATE TABLE IF NOT EXISTS hits (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app varchar,
    uri varchar,
    ip varchar,
    timestamp timestamp
);
