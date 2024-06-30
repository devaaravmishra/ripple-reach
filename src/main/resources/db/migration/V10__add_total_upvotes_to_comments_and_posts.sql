ALTER TABLE post
    ADD COLUMN total_upvotes BIGINT DEFAULT 0;

UPDATE post
SET total_upvotes = 0
WHERE total_upvotes IS NULL;

ALTER TABLE post
    ALTER COLUMN total_upvotes SET NOT NULL;

ALTER TABLE comment
    ADD COLUMN total_upvotes BIGINT DEFAULT 0;

UPDATE comment
SET total_upvotes = 0
WHERE total_upvotes IS NULL;

ALTER TABLE comment
    ALTER COLUMN total_upvotes SET NOT NULL;
