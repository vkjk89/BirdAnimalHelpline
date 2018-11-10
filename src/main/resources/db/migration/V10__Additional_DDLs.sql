alter TABLE case_info add COLUMN type_animal VARCHAR(30);
alter TABLE case_info add COLUMN current_user_id bigint;
alter TABLE case_info add COLUMN is_active boolean;
alter TABLE case_txn add COLUMN is_ack boolean default false ;

ALTER TABLE case_info
ADD CONSTRAINT fk_case_txn_4
FOREIGN KEY (current_user_id) REFERENCES user(user_id);