create table `notification` (
       `id` bigint(20) NOT NULL AUTO_INCREMENT,
       `created_date` datetime NOT NULL,
       `modified_date` datetime DEFAULT NULL,
       `notification_type` varchar(255) NOT NULL,
        `feed_id` bigint(20) NOT NULL,
        `listener_id` bigint(20) NOT NULL,
        `publisher_id` bigint(20) NOT NULL,
        primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table notification
       add constraint FKitid6tgnc70scny6gcltfu91u
       foreign key (feed_id)
       references feed(id);

alter table notification
       add constraint FK4qv6svm9lautfkk662sum330a
       foreign key (listener_id)
       references user(id);

alter table notification
       add constraint FKfvmrsnrraov7iy9rpssvvr7nk
       foreign key (publisher_id)
       references user(id);