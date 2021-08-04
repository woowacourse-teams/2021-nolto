CREATE TABLE `comment` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `created_date` datetime NOT NULL,
                        `modified_date` datetime NOT NULL,
                        `content` text NOT NULL,
                        `helper` bit(1) NOT NULL,
                        `author_id` bigint(20) NOT NULL,
                        `feed_id` bigint(20) NOT NULL,
                        `parent_id` bigint(20),
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `comment_like` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `comment_id` bigint(20) NOT NULL,
                        `user_id` bigint(20) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

    alter table comment
       add constraint FKh1gtv412u19wcbx22177xbkjp
       foreign key (author_id)
       references user(id);

    alter table comment
       add constraint FKmq57ocw5jrw8rd2lot1g8t0v2
       foreign key (feed_id)
       references feed(id);

    alter table comment
       add constraint FKde3rfu96lep00br5ov0mdieyt
       foreign key (parent_id)
       references comment(id);

    alter table comment_like
       add constraint fk_comment_likes_to_comment
       foreign key (comment_id)
       references comment(id);

    alter table comment_like
       add constraint fk_comment_likes_to_user
       foreign key (user_id)
       references user(id);