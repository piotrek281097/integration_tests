--this script initiates db for h2 db (used in test profile)
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (account_status, email, first_name) values ('NEW', 'brian@domain.com', 'Brian')
