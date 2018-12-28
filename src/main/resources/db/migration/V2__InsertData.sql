INSERT INTO authority (authority_id,authority_name) VALUES 
(1,'ADMIN')
,(5,'Volunteer')
,(6,'Doctor')
,(7,'Driver')
,(8,'Fosterer')
,(9,'Receptionist')
;

INSERT INTO user (user_id,user_name,password,email,enabled,update_timestamp,mobile) VALUES 
(1,'admin','$2a$10$4X2gT.zKpcqAu/6khoIJQ.N/amE5G2qg.8OtcD5sley90BTu6uX96','abc@abc.com',1,now(),1111111111)
;

INSERT INTO user_authority (user_authority_id,user_id,authority_id) VALUES 
(1,1,1)
;

INSERT INTO user_info (user_info_id,user_id,first_name,last_name,address,gender,dob,create_date,last_login_date,role,security_id,security_ans) VALUES
(1,1,'AdminFirst','AdminLast',NULL,'M',NULL,now(),now(),"ADMIN",NULL,NULL)
;

INSERT INTO user_image (user_id,old_image,image) VALUES
(1,NULL,NULL)
;

insert into pincode_landmark(pincode, landmark)  values (400004,"KHETWADI"),(400004,"CP TANK");
insert into security_q(security_q_id, security_q_text) values (1,"What is your favourite Sport?"),(2,"Which floor do you live on?"),(3,"What is your Place of Birth?");


insert into bird_animal (bird_animal_name) values ( "Crow"), ("Pigeon"), ("Dog"),("Cow");
