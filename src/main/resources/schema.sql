-- Extensión para UUID
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Tabla base
CREATE TABLE IF NOT EXISTS tasks (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
    );

CREATE INDEX IF NOT EXISTS idx_tasks_created_at ON tasks (created_at DESC);

