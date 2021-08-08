alter table `notification` add column `comment_id` bigint(20);

alter table notification
    add constraint FKgmcypgrcb3oo4ujbbk7cyaro2
    foreign key (comment_id)
    references comment(id);
