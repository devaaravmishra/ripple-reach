ALTER TABLE post
    ADD total_comments BIGINT DEFAULT 0;

UPDATE post
SET total_comments = 0
WHERE total_comments IS NULL;

ALTER TABLE post
    ALTER COLUMN total_comments SET NOT NULL;