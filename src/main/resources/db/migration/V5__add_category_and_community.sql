CREATE TABLE category
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(500),
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    slug        VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE community
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(500),
    image_url   VARCHAR(255),
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    category_id BIGINT                                  NOT NULL,
    slug        VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_community PRIMARY KEY (id)
);

ALTER TABLE category
    ADD CONSTRAINT uc_category_name UNIQUE (name);

ALTER TABLE category
    ADD CONSTRAINT uc_category_slug UNIQUE (slug);

ALTER TABLE community
    ADD CONSTRAINT uc_community_name UNIQUE (name);

ALTER TABLE community
    ADD CONSTRAINT uc_community_slug UNIQUE (slug);

ALTER TABLE community
    ADD CONSTRAINT FK_COMMUNITY_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);