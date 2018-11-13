-- drop TABLE user_addr_info;
create table user_addr_info (
        user_addr_info_id bigint  auto_increment primary key,
        user_id bigint  not null,
        full_name varchar(100),
        address_line1 varchar(500),
        address_line2 varchar(500),
        pincode bigint,
        alternate_contact varchar(20),
        address_type varchar(1),
        nature_business varchar(100),
        last_update_timestamp TIMESTAMP default current_timestamp,
        constraint fk_user_addr_info_1
           foreign key(user_id) references user(user_id),
        constraint fk_user_addr_info_1 UNIQUE key (user_id,address_type)
 );

 -- drop TABLE pincode_landmark;
 create table pincode_landmark (
        pin_land_id bigint  auto_increment primary key,
        pincode bigint not NULL,
        landmark varchar(200) not null
 );

 -- DROP TABLE user_service_time_info;
 create table user_service_time_info (
        user_service_time_info_id bigint  auto_increment primary key,
        user_id bigint  not null,
        pin_land_id bigint not null,
        from_time int,
        to_time int,
        last_update_timestamp TIMESTAMP default current_timestamp,
        constraint fk_user_service_time_info_1
           foreign key(user_id) references user(user_id),
        constraint fk_user_service_time_info_2
           foreign key(pin_land_id) references pincode_landmark(pin_land_id)
 );