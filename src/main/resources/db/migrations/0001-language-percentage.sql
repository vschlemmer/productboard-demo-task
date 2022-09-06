--liquibase formatted sql

--changeset vojtech.schlemmer:1
CREATE TABLE language_percentage
(
    id            BIGSERIAL PRIMARY KEY,
    language_name TEXT UNIQUE NOT NULL,
    percentage    DECIMAL(2, 1) NOT NULL
);
