ALTER TABLE app_user
    ADD avatar VARCHAR(255);

ALTER TABLE app_user
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE app_user
    ALTER COLUMN updated_at SET NOT NULL;