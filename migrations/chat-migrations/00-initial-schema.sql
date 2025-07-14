--liquibase formatted sql

--changeset create-schema:0
CREATE TABLE chatrooms (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    isPrivate BOOLEAN,
    owner BIGINT
);

CREATE TABLE userChatroom (
    id BIGSERIAL PRIMARY KEY,
    userId BIGINT,
    chatId BIGINT REFERENCES chatrooms (id) ON DELETE CASCADE
);
