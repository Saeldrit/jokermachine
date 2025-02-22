CREATE SCHEMA IF NOT EXISTS chat;

CREATE TABLE IF NOT EXISTS chat.chats
(
    id        BIGINT PRIMARY KEY,
    chat_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS chat.persons
(
    id         BIGINT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    login      VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS chat.chat_users
(
    chat_id   BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    FOREIGN KEY (chat_id) REFERENCES chat.chats (id) ON DELETE CASCADE,
    FOREIGN KEY (person_id) REFERENCES chat.persons (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS chat.messages
(
    id           SERIAL PRIMARY KEY,
    chat_id      BIGINT NOT NULL,
    person_id    BIGINT NOT NULL,
    message_text TEXT   NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_id) REFERENCES chat.chats (id) ON DELETE CASCADE,
    FOREIGN KEY (person_id) REFERENCES chat.persons (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS chat.old_fires
(
    chat_id   BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    content   TEXT,
    PRIMARY KEY (chat_id, person_id),
    FOREIGN KEY (chat_id) REFERENCES chat.chats (id) ON DELETE CASCADE,
    FOREIGN KEY (person_id) REFERENCES chat.persons (id) ON DELETE CASCADE
);

CREATE INDEX idx_chat_users_chat ON chat.chat_users (chat_id);
CREATE INDEX idx_messages_chat ON chat.messages (chat_id);
CREATE INDEX idx_messages_user ON chat.messages (person_id);