--liquibase formatted sql

--changeset create-schema:0
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    text TEXT,
    sentAt TIMESTAMP,
    userId BIGINT,
    chatId BIGINT
);
