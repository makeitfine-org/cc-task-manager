CREATE TABLE tasks (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(200)  NOT NULL,
    description VARCHAR(2000),
    status      VARCHAR(20)   NOT NULL DEFAULT 'TODO',
    priority    VARCHAR(10)   NOT NULL DEFAULT 'MEDIUM',
    created_at  TIMESTAMP     NOT NULL,
    updated_at  TIMESTAMP     NOT NULL
);

CREATE INDEX idx_tasks_status   ON tasks (status);
CREATE INDEX idx_tasks_priority ON tasks (priority);
