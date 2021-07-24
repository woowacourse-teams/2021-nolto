CREATE TABLE `feed` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `created_date` datetime NOT NULL,
                        `modified_date` datetime DEFAULT NULL,
                        `content` varchar(255) NOT NULL,
                        `deployed_url` varchar(255) DEFAULT NULL,
                        `is_sos` bit(1) NOT NULL,
                        `step` varchar(255) NOT NULL,
                        `storage_url` varchar(255) DEFAULT NULL,
                        `thumbnail_url` varchar(255) DEFAULT NULL,
                        `title` varchar(255) NOT NULL,
                        `views` int(11) NOT NULL,
                        `author_id` bigint(20) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `tech` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `name` varchar(255) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `image_url` varchar(255) NOT NULL,
                        `nick_name` varchar(255) NOT NULL,
                        `social_id` varchar(255) NOT NULL,
                        `social_type` varchar(255) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `feed_tech` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `feed_id` bigint(20) NOT NULL,
                             `tech_id` bigint(20) NOT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `likes` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `feed_id` bigint(20) NOT NULL,
                         `user_id` bigint(20) NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table tech
    add constraint UK_61sbcb7t9ej3nihlu4wtynsio unique (name);

alter table feed
    add constraint fk_feed_to_author
        foreign key (author_id)
            references user(id);

alter table feed_tech
    add constraint FKp6xlnba2m64cda0x0m80km6x8
        foreign key (feed_id)
            references feed(id);

alter table feed_tech
    add constraint FK9p853vtexskwvqgxc7bv45hw9
        foreign key (tech_id)
            references tech(id);

alter table likes
    add constraint fk_likes_to_feed
        foreign key (feed_id)
            references feed(id);

alter table likes
    add constraint fk_likes_to_user
        foreign key (user_id)
            references user(id);