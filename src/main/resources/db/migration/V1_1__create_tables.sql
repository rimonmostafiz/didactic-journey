CREATE TABLE ROLE(
    ID                  BIGINT          PRIMARY KEY AUTO_INCREMENT,
    NAME                VARCHAR(64)     NOT NULL UNIQUE,
    CREATE_TIME         TIMESTAMP(6)    NOT NULL,
    CREATED_BY          VARCHAR(64)     NOT NULL,
    EDIT_TIME           TIMESTAMP(6),
    EDITED_BY           VARCHAR(64),
    INTERNAL_VERSION    BIGINT          DEFAULT 1
)ENGINE=InnoDB;

CREATE TABLE USER(
    ID                  BIGINT          PRIMARY KEY AUTO_INCREMENT,
    USERNAME            VARCHAR(64)     NOT NULL UNIQUE,
    PASSWORD            VARCHAR(64)     NOT NULL,
    EMAIL               VARCHAR(64)     NOT NULL UNIQUE,
    FIRST_NAME          VARCHAR(128),
    LAST_NAME           VARCHAR(128),
    STATUS              VARCHAR(64)     DEFAULT 'ACTIVE',
    CREATE_TIME         TIMESTAMP(6)    NOT NULL,
    CREATED_BY          VARCHAR(64)     NOT NULL,
    EDIT_TIME           TIMESTAMP(6),
    EDITED_BY           VARCHAR(64),
    INTERNAL_VERSION    BIGINT          DEFAULT 1
)ENGINE=InnoDB;

CREATE TABLE ACTIVITY_USER(
    ACTIVITY_ID         BIGINT          PRIMARY KEY AUTO_INCREMENT,
    USER_ID             BIGINT,
    USERNAME            VARCHAR(64),
    PASSWORD            VARCHAR(64),
    EMAIL               VARCHAR(64),
    FIRST_NAME          VARCHAR(128),
    LAST_NAME           VARCHAR(128),
    STATUS              VARCHAR(64),
    CREATE_TIME         TIMESTAMP(6),
    CREATED_BY          VARCHAR(64),
    EDIT_TIME           TIMESTAMP(6),
    EDITED_BY           VARCHAR(64),
    ACTIVITY_USER       VARCHAR(64)     NOT NULL,
    ACTIVITY_ACTION     INT             NOT NULL,
    ACTIVITY_TIME       TIMESTAMP(6)    NOT NULL,
    INTERNAL_VERSION    BIGINT          DEFAULT 1
)ENGINE=InnoDB;

CREATE TABLE USER_ROLES(
    ID                  BIGINT          PRIMARY KEY AUTO_INCREMENT,
    USER_ID             BIGINT          NOT NULL,
    ROLE_ID             BIGINT          NOT NULL,
    ROLE_NAME           VARCHAR(64)     NOT NULL,
    CREATE_TIME         TIMESTAMP(6)    NOT NULL,
    CREATED_BY          VARCHAR(64)     NOT NULL,
    EDIT_TIME           TIMESTAMP(6),
    EDITED_BY           VARCHAR(64),
    INTERNAL_VERSION    BIGINT          DEFAULT 1,
    CONSTRAINT FK_USER_ROLES_USER FOREIGN KEY (USER_ID) REFERENCES USER(ID),
    CONSTRAINT FK_USER_ROLES_ROLE FOREIGN KEY (ROLE_ID) REFERENCES ROLE(ID),
    UNIQUE KEY USER_ROLE_UNIQUE (USER_ID, ROLE_ID)
)ENGINE=InnoDB;

CREATE TABLE PROJECT(
    ID                  BIGINT          PRIMARY KEY AUTO_INCREMENT,
    NAME                VARCHAR(64)     NOT NULL,
    DESCRIPTION         VARCHAR(128),
    STATUS              VARCHAR(64)     DEFAULT 'ACTIVE',
    ASSIGNED_USER       BIGINT          NOT NULL,
    CREATE_TIME         TIMESTAMP(6)    NOT NULL,
    CREATED_BY          VARCHAR(64)     NOT NULL,
    EDIT_TIME           TIMESTAMP(6),
    EDITED_BY           VARCHAR(64),
    INTERNAL_VERSION    BIGINT          DEFAULT 1,
    CONSTRAINT FK_PROJECT_USER FOREIGN KEY (ASSIGNED_USER) REFERENCES USER(ID)
)ENGINE=InnoDB;

CREATE TABLE ACTIVITY_PROJECT(
    ACTIVITY_ID         BIGINT          PRIMARY KEY AUTO_INCREMENT,
    PROJECT_ID          BIGINT,
    NAME                VARCHAR(64),
    DESCRIPTION         VARCHAR(128),
    STATUS              VARCHAR(64),
    ASSIGNED_USER       BIGINT,
    CREATE_TIME         TIMESTAMP(6),
    CREATED_BY          VARCHAR(64),
    EDIT_TIME           TIMESTAMP(6),
    EDITED_BY           VARCHAR(64),
    ACTIVITY_USER       VARCHAR(64)     NOT NULL,
    ACTIVITY_ACTION     INT             NOT NULL,
    ACTIVITY_TIME       TIMESTAMP(6)    NOT NULL,
    INTERNAL_VERSION    BIGINT          DEFAULT 1
)ENGINE=InnoDB;

CREATE TABLE TASK(
    ID                  BIGINT          PRIMARY KEY AUTO_INCREMENT,
    DESCRIPTION         VARCHAR(512)    NOT NULL,
    STATUS              VARCHAR(64)     DEFAULT 'OPEN',
    PROJECT_ID          BIGINT          NOT NULL,
    ASSIGNED_USER       BIGINT          NOT NULL,
    DUE_DATE            DATE,
    CREATE_TIME         TIMESTAMP(6)    NOT NULL,
    CREATED_BY          VARCHAR(64)     NOT NULL,
    EDIT_TIME           TIMESTAMP(6),
    EDITED_BY           VARCHAR(64),
    INTERNAL_VERSION    BIGINT          DEFAULT 1,
    CONSTRAINT FK_TASK_PROJECT FOREIGN KEY (PROJECT_ID) REFERENCES PROJECT(ID),
    CONSTRAINT FK_TASK_USER FOREIGN KEY (ASSIGNED_USER) REFERENCES USER(ID)
)ENGINE=InnoDB;

CREATE TABLE ACTIVITY_TASK(
    ACTIVITY_ID         BIGINT          PRIMARY KEY AUTO_INCREMENT,
    TASK_ID             BIGINT,
    DESCRIPTION         VARCHAR(512),
    STATUS              VARCHAR(64),
    PROJECT_ID          BIGINT,
    ASSIGNED_USER       BIGINT,
    DUE_DATE            DATE,
    CREATE_TIME         TIMESTAMP(6),
    CREATED_BY          VARCHAR(64),
    EDIT_TIME           TIMESTAMP(6),
    EDITED_BY           VARCHAR(64),
    ACTIVITY_USER       VARCHAR(64)     NOT NULL,
    ACTIVITY_ACTION     INT             NOT NULL,
    ACTIVITY_TIME       TIMESTAMP(6)    NOT NULL,
    INTERNAL_VERSION    BIGINT          DEFAULT 1
)ENGINE=InnoDB;