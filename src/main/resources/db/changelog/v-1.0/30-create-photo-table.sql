
create table photo
(
    dtype           varchar(31)  not null,
    id              integer generated by default as identity primary key,
    file_path       varchar(255) not null,
    file_size       bigint not null,
    media_type      varchar(255) not null,
    users_id        integer constraint photoUsersId references users,
    ads_id          integer constraint photoAdsId references ads

);

GO