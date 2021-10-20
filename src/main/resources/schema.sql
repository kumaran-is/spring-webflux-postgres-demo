DROP TABLE IF EXISTS public.customer cascade;

DROP SEQUENCE IF EXISTS public.customer_sequence;

CREATE SEQUENCE public.customer_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.customer_sequence
    OWNER TO kumaraniyyasamysrinivasan;
    
CREATE TABLE IF NOT EXISTS public.customer
(
    id bigint NOT NULL DEFAULT nextval('customer_sequence'::regclass),
    dob date,
    email character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT customer_pkey PRIMARY KEY (id)
);

ALTER TABLE public.customer
    OWNER to kumaraniyyasamysrinivasan;