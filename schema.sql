CREATE TABLE IF NOT EXISTS public.accounts
(
    account_id text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT accounts_pkey PRIMARY KEY (account_id)
    )

    TABLESPACE pg_default;


CREATE TABLE IF NOT EXISTS public.transactions
(
    transaction_id serial NOT NULL,
    step integer,
    type text COLLATE pg_catalog."default",
    amount numeric(15,2),
    orig_account_id text COLLATE pg_catalog."default",
    dest_account_id text COLLATE pg_catalog."default",
    oldbalanceorg numeric(15,2),
    newbalanceorg numeric(15,2),
    oldbalancedest numeric(15,2),
    newbalancedest numeric(15,2),
    isfraud boolean,
    isflaggedfraud boolean,
    CONSTRAINT transactions_pkey PRIMARY KEY (transaction_id),
    CONSTRAINT transactions_dest_account_id_fkey FOREIGN KEY (dest_account_id)
    REFERENCES public.accounts (account_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT transactions_orig_account_id_fkey FOREIGN KEY (orig_account_id)
    REFERENCES public.accounts (account_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.alerts
(
    alerts_id serial NOT NULL,
    transaction_id integer,
    cause text COLLATE pg_catalog."default",
    CONSTRAINT alerts_pkey PRIMARY KEY (alerts_id),
    CONSTRAINT alerts_transaction_id_fkey FOREIGN KEY (transaction_id)
    REFERENCES public.transactions (transaction_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;