alter table `user` add column `created_date` datetime not null;
alter table `user` add column `modified_date` datetime not null;
alter table `user` add column `bio` varchar(255) DEFAULT NULL;

alter table `user`
    add constraint UK_d2ia11oqhsynodbsi46m80vfc unique (nick_name);

