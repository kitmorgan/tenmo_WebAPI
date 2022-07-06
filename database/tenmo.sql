BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transfer;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, transfer_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance decimal(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

CREATE TABLE transfer (
	transfer_id serial,
	toAccount_id int NOT NULL,
	fromAccount_id  int NOT NULL,
	status varchar NOT NULL,
	CONSTRAINT PK_transfer PRIMARY KEY (transfer_id),
	CONSTRAINT FK_toAccount FOREIGN KEY (toAccount_id) REFERENCES account (account_id),
	CONSTRAINT FK_fromAccount FOREIGN KEY (fromAccount_id) REFERENCES account (account_id),
	CONSTRAINT CHK_statusValues CHECK (status IN ('ACCEPTED', 'REJECTED', 'PENDING')),
	CONSTRAINT CHK_differentAccounts CHECK (toAccount_id != fromAccount_id)
);

INSERT INTO tenmo_user (username, password_hash) values ('patrickmorris', '$2a$10$xX/UuWqKOWI1XJ8f4uCU/uXCt8H2vW7rt/hKol9OggwmcGtbxN06S');
INSERT INTO tenmo_user (username, password_hash) values ('kitmorgan', '$2a$10$yo42jI1ZzM8WPJP4R7sQKOQ08jXPJ1PXd2TTbwPmn9eTNrw.M3vWm');
INSERT INTO tenmo_user (username, password_hash) values ('user3', '$2a$10$ADG3yPwo0a8S7crm4qKqW.PxMHqYPwGJOJWLE6UvBHnyaGFhxDhzS');

INSERT INTO account (user_id, balance) values (1001, 1000);
INSERT INTO account (user_id, balance) values (1002, 1000);
INSERT INTO account (user_id, balance) values (1003, 1000);

COMMIT;
