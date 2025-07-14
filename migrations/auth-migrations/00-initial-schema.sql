--liquibase formatted sql

--changeset create-schema:0
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username TEXT  UNIQUE,
    password TEXT
);

