alter TABLE case_txn add COLUMN update_timestamp TIMESTAMP default current_timestamp;
alter TABLE case_txn add COLUMN description varchar(1000);

 alter TABLE case_info add COLUMN animal_name varchar(200) ;
 alter TABLE case_info add COLUMN animal_condition varchar(500);
 alter TABLE case_info add COLUMN contact_name varchar(500);
 alter TABLE case_info add COLUMN contact_number varchar(20);
 alter TABLE case_info add COLUMN location varchar(1000);
 alter TABLE case_info add COLUMN location_pincode varchar(10);

  create table bird_animal (
        bird_animal_id bigint  auto_increment primary key,
        bird_animal_name varchar(100)
  );

 insert into bird_animal (bird_animal_name) values ( "Crow"), ("Pigeon"), ("Dog"),("Cow");

alter TABLE case_info modify user_id_closed bigint(20) ;