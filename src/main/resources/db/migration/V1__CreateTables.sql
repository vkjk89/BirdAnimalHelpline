CREATE TABLE authority (
  authority_id int(11) NOT NULL AUTO_INCREMENT,
  authority_name varchar(30) NOT NULL,
  PRIMARY KEY (authority_id)
) ;

CREATE TABLE user (
  user_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_name varchar(256) NOT NULL,
  password varchar(256) NOT NULL,
  email varchar(256) DEFAULT NULL,
  enabled tinyint(1) NOT NULL DEFAULT '0',
  update_timestamp timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  mobile bigint(20) DEFAULT NULL,
  PRIMARY KEY (user_id),
  UNIQUE KEY user_name (user_name),
  UNIQUE KEY mobile (mobile)
) ;


CREATE TABLE case_info (
  case_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id_opened bigint(20) NOT NULL,
  creation_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_modification_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  close_date timestamp NULL DEFAULT NULL,
  description varchar(1000) DEFAULT NULL,
  user_id_closed bigint(20) NOT NULL,
  close_remark varchar(1000) DEFAULT NULL,
  PRIMARY KEY (case_id),
  KEY fk_case_1 (user_id_opened),
  KEY fk_case_2 (user_id_closed),
  CONSTRAINT fk_case_1 FOREIGN KEY (user_id_opened) REFERENCES user (user_id),
  CONSTRAINT fk_case_2 FOREIGN KEY (user_id_closed) REFERENCES user (user_id)
) ;

CREATE TABLE case_txn (
  case_txn_id bigint(20) NOT NULL AUTO_INCREMENT,
  case_id bigint(20) DEFAULT NULL,
  from_user_id bigint(20) DEFAULT NULL,
  to_user_id bigint(20) DEFAULT NULL,
  status varchar(100) DEFAULT NULL,
  amount double DEFAULT NULL,
  transfer_date timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (case_txn_id),
  KEY fk_case_txn_1 (case_id),
  KEY fk_case_txn_2 (from_user_id),
  KEY fk_case_txn_3 (to_user_id),
  CONSTRAINT fk_case_txn_1 FOREIGN KEY (case_id) REFERENCES case_info (case_id),
  CONSTRAINT fk_case_txn_2 FOREIGN KEY (from_user_id) REFERENCES user (user_id),
  CONSTRAINT fk_case_txn_3 FOREIGN KEY (to_user_id) REFERENCES user (user_id)
) ;

CREATE TABLE security_q (
  security_q_id bigint(20) NOT NULL AUTO_INCREMENT,
  security_q_text varchar(1000) DEFAULT NULL,
  PRIMARY KEY (security_q_id)
) ;



CREATE TABLE user_authority (
  user_authority_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) NOT NULL,
  authority_id int(11) NOT NULL,
  PRIMARY KEY (user_authority_id),
  KEY fk_user_authority_1 (user_id),
  KEY fk_user_authority_2 (authority_id),
  CONSTRAINT fk_user_authority_1 FOREIGN KEY (user_id) REFERENCES user (user_id),
  CONSTRAINT fk_user_authority_2 FOREIGN KEY (authority_id) REFERENCES authority (authority_id)
) ;

CREATE TABLE user_info (
  user_info_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) NOT NULL,
  first_name varchar(100) DEFAULT NULL,
  last_name varchar(100) DEFAULT NULL,
  address varchar(1000) DEFAULT NULL,
  gender varchar(1) DEFAULT NULL,
  dob varchar(50) DEFAULT NULL,
  create_date timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  last_login_date timestamp NULL DEFAULT NULL,
  role varchar(30) DEFAULT NULL,
  image mediumblob,
  security_id int(11) DEFAULT NULL,
  security_ans varchar(256) DEFAULT NULL,
  PRIMARY KEY (user_info_id),
  UNIQUE KEY user_id (user_id),
  CONSTRAINT fk_user_info_1 FOREIGN KEY (user_id) REFERENCES user (user_id)
) ;