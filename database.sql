CREATE TABLE public.user_vt (
    id serial NOT NULL,
    email varchar NULL,
    "password" varchar NULL,
    username varchar NULL,
    "admin" boolean NULL,
    "banned" boolean default false,
    "notificationbyemail" boolean default true,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE public.content(
	id varchar NOT NULL,
	title varchar NULL,
	duration int4 NULL,
	n_episode int4 NULL,
	link varchar NULL,
	CONSTRAINT content_pk PRIMARY KEY (id)
);

CREATE TABLE public.notification (
	id serial NOT NULL,
	title varchar NULL,
	description varchar NULL,
	CONSTRAINT notification_pk PRIMARY KEY (id)
);

CREATE TABLE public.list (
	id serial NOT NULL,
	id_user serial NOT NULL,
	CONSTRAINT list_pk PRIMARY KEY (id),
	CONSTRAINT list_user_fk FOREIGN KEY (id_user) REFERENCES public.user_vt(id)
);

CREATE TABLE public.review (
	id serial NOT NULL,
	vote int4 NULL,
	user_comment varchar NULL,
	id_user serial NOT NULL,
	id_content varchar NULL,
	CONSTRAINT review_pk PRIMARY KEY (id),
	CONSTRAINT review_content_fk FOREIGN KEY (id_content) REFERENCES public.content(id),
	CONSTRAINT review_user_fk FOREIGN KEY (id_user) REFERENCES public.user_vt(id)
);

CREATE TABLE public.contains (
	id_list serial NOT NULL,
	id_content varchar NOT NULL,
	state varchar NULL,
	CONSTRAINT contains_pk PRIMARY KEY (id_list, id_content),
	CONSTRAINT contains_content_fk FOREIGN KEY (id_content) REFERENCES public.content(id),
	CONSTRAINT contains_list_fk FOREIGN KEY (id_list) REFERENCES public.list(id)
);

CREATE TABLE public.receive(
	id_user serial NOT NULL,
	id_notification serial NOT NULL,
	CONSTRAINT receive_pk PRIMARY KEY (id_user, id_notification),
	CONSTRAINT receive_notification_fk FOREIGN KEY (id_notification) REFERENCES public.notification(id),
	CONSTRAINT receive_user_fk FOREIGN KEY (id_user) REFERENCES public.user_vt(id)
);


ALTER TABLE review ALTER COLUMN id_user DROP DEFAULT;
ALTER TABLE list ALTER COLUMN id_user DROP DEFAULT;
ALTER TABLE receive ALTER COLUMN id_user DROP DEFAULT;
