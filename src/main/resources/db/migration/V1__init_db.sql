CREATE TABLE `user`
(
    `id`         bigint      NOT NULL AUTO_INCREMENT,
    `created`    datetime     DEFAULT NULL,
    `first_name` varchar(60)  DEFAULT NULL,
    `last_name`  varchar(60)  DEFAULT NULL,
    `login`      varchar(20) NOT NULL,
    `password`   varchar(255) DEFAULT NULL,
    `role`       varchar(20) NOT NULL,
    `status`     varchar(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_login` (`login`)
);

CREATE TABLE `file`
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `description`   varchar(255) DEFAULT NULL,
    `file_location` varchar(255) DEFAULT NULL,
    `name`          varchar(30)  DEFAULT NULL,
    `status`        int          DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `event`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `action_type` int    DEFAULT NULL,
    `file_id`     bigint NOT NULL,
    `user_id`     bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_event_file`
        FOREIGN KEY (`file_id`)
            REFERENCES `file` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_event_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `user` (`id`)
            ON DELETE SET NULL
            ON UPDATE CASCADE
);