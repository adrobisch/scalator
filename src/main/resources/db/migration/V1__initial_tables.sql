CREATE TABLE T_USERS
(
    LOGIN VARCHAR(255) PRIMARY KEY NOT NULL,
    PASSWORD_HASH VARCHAR(255) NOT NULL,
    DISPLAY_NAME VARCHAR(512),
    EMAIL VARCHAR(512),
    ABOUT VARCHAR(4000)
);

CREATE INDEX IDX_USERS_LOGIN ON T_USERS (LOGIN);

CREATE TABLE T_CREDENTIALS
(
    LOGIN VARCHAR(255) NOT NULL,
    TOKEN VARCHAR(64) NOT NULL,
    CREDENTIAL_TYPE VARCHAR(32) NOT NULL,
    VALID_UNTIL BIGINT NOT NULL,
    FOREIGN KEY (LOGIN) REFERENCES T_USERS(LOGIN)
);

CREATE INDEX IDX_CRED_LOGIN ON T_CREDENTIALS (LOGIN);