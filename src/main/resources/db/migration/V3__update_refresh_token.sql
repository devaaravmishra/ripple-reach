ALTER TABLE refresh_token
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE refresh_token
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE refresh_token
DROP
COLUMN created_date;