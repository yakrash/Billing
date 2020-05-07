CREATE TABLE financial_transaction (
    id serial not null,
    src integer not null,
    dst integer not null,
    amount numeric not null
);