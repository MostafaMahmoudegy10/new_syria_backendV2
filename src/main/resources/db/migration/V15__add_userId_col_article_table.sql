alter table Article add column user_id uuid;

alter table Article add constraint fk_article_user
    foreign key (user_id) references users(id) on delete cascade;