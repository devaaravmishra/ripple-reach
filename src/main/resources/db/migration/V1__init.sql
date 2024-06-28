CREATE TABLE app_user
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username      VARCHAR(255)                            NOT NULL,
    phone         VARCHAR(255)                            NOT NULL,
    university_id BIGINT,
    company_id    BIGINT,
    profession    VARCHAR(255),
    is_verified   BOOLEAN,
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    deleted_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_app_user PRIMARY KEY (id)
);

CREATE TABLE company
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name       VARCHAR(255)                            NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_company PRIMARY KEY (id)
);

CREATE TABLE refresh_token
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    token        VARCHAR(255),
    created_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_refreshtoken PRIMARY KEY (id)
);

CREATE TABLE university
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name       VARCHAR(255)                            NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_university PRIMARY KEY (id)
);

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_company UNIQUE (company_id);

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_phone UNIQUE (phone);

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_university UNIQUE (university_id);

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_username UNIQUE (username);

ALTER TABLE company
    ADD CONSTRAINT uc_company_name UNIQUE (name);

ALTER TABLE university
    ADD CONSTRAINT uc_university_name UNIQUE (name);

ALTER TABLE app_user
    ADD CONSTRAINT FK_APP_USER_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE app_user
    ADD CONSTRAINT FK_APP_USER_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES university (id);