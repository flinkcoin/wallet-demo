CREATE SEQUENCE app_user_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE TABLE app_user (
	id integer NOT NULL,
	deleted boolean NOT NULL,
	failed_logins integer NOT NULL,
	full_name character varying(255) NOT NULL,
	last_login_at timestamp without time zone,
	locked_at timestamp without time zone,
	password character varying(255) NOT NULL,
	password_last_changed_at timestamp without time zone,
	phone_number character varying(255),
	username character varying(255) NOT NULL
);

ALTER TABLE app_user
	ADD CONSTRAINT app_user_pkey PRIMARY KEY (id);

