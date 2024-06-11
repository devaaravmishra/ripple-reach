ALTER TABLE company
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE university
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE company
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE university
    ALTER COLUMN updated_at SET NOT NULL;