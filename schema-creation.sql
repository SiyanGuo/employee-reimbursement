
drop table if exists reimb_status cascade;
drop table if exists reimb_type cascade;
drop table if exists user_role cascade;
drop table if exists reimbursement cascade;
drop table if exists users cascade;

-- reimbursement status table
create table reimb_status (
id serial primary key,
status varchar(10) not null
);

-- reimbursement types table
create table reimb_type (
id serial primary key,
type varchar(10) not null
);

-- user roles table
create table user_role (
id serial primary key,
role varchar(20) not null
);

--users table
create table users (
id serial primary key,
username varchar(50) not null unique,
password varchar(100) not null,
first_name varchar(100) not null,
last_name varchar(100) not null,
email varchar(150) not null,
user_role_id integer not null,

constraint fk_user_role foreign key(user_role_id) references user_role(id) 
);

-- reimbursement table
create table reimbursement (
id serial primary key,
amount decimal(6,2) not null check (amount > 0),
author_id integer not null,
description varchar(250) not null,
type_id integer not null,
submitted_at timestamptz not null,
status_id integer default 1,
resolved_at timestamptz,
resolver_id integer,
receipt varchar(250) not null,

constraint fk_author foreign key(author_id) references users(id),
constraint fk_resolver foreign key(resolver_id) references users(id),
constraint fk_status foreign key(status_id) references reimb_status(id),
constraint fk_type foreign key(type_id) references reimb_type(id)
);

insert into reimb_status (status)
values 
('PENDING'), ('APPROVED'), ('DENIED');

insert into reimb_type (type)
values 
('LODGING'), ('TRAVEL'), ('FOOD'), ('OTHER');

insert into user_role (role)
values ('EMPLOYEE'), ('FINANCE MANAGER');

insert into users (username, password, first_name, last_name, email, user_role_id)
values 
('employee111', '$2a$10$p9Kx8jxJoOGjNzTGZ5H6x.mddC9ycjxZovZiFnunZcIUAhMWXEPsi', 'Tiffany', 'Bell', 'tb@qmail.com', 1),
('manager111', '$2a$10$/NRFsdRtdh9L3BjpjEoDSu2rx1OQhD0yt2MH3d.0cRt4jHOmypxX2', 'Mark', 'Lee', 'ml@qmail.com', 2),
('user333', '$2a$10$/NRFsdRtdh9L3BjpjEoDSu2rx1OQhD0yt2MH3d.0cRt4jHOmypxX2', 'Hanna', 'Martinez', 'hm@qmail.com', 2),
('user444', '$2a$10$p9Kx8jxJoOGjNzTGZ5H6x.mddC9ycjxZovZiFnunZcIUAhMWXEPsi', 'Charles', 'Kaur', 'ck@qmail.com', 1),
('user555', '$2a$10$p9Kx8jxJoOGjNzTGZ5H6x.mddC9ycjxZovZiFnunZcIUAhMWXEPsi', 'Abby', 'Miller', 'am@qmail.com', 1),
('user666', '$2a$10$p9Kx8jxJoOGjNzTGZ5H6x.mddC9ycjxZovZiFnunZcIUAhMWXEPsi', 'Hanna', 'Martinez', 'hm@qmail.com', 1);


insert into reimbursement (amount, author_id, description, type_id, submitted_at, status_id, resolved_at, resolver_id, receipt)
values 
('520.00', 1, 'Onsite hotel', 1, '2021-11-11 10:08', 2, '2021-12-01 14:10', 2, 'https://storage.googleapis.com/employee_reimbursement/receipt1.jpeg'),
('225.00', 4, 'Onsite hotel', 1, '2021-10-27 15:08', 3, '2021-11-11 16:00', 3, 'https://storage.googleapis.com/employee_reimbursement/receipt1.jpeg'),
('1500.00', 5, 'Travel for business conference',2, '2021-09-01 11:18', 2, '2021-09-10 15:18', 2, 'https://storage.googleapis.com/employee_reimbursement/receipt2.jpeg'),
('90.00', 6, 'Office supplies', 4, '2021-09-09 12:23', 2, '2021-09-18 12:10', 3, 'https://storage.googleapis.com/employee_reimbursement/receipt3.png'),
('107.00', 1, 'Client luncheon', 3, '2021-10-12 14:15', 2, '2021-10-22 09:45', 2, 'https://storage.googleapis.com/employee_reimbursement/receipt2.jpeg'),
('1500.00', 1, 'Travel for business conference', 2, '2021-09-02 10:08', 3, '2021-09-10 14:10', 3, 'https://storage.googleapis.com/employee_reimbursement/receipt3.png'),
('62.00', 1, 'Office supplies', 4, '2021-07-23 11:30', 2, '2021-08-01 13:36', 2, 'https://storage.googleapis.com/employee_reimbursement/receipt4.jpeg'),
('218.00', 1, 'Sales meeting with hotel', 3, '2022-03-18 10:48', 1,  null, null, 'https://storage.googleapis.com/employee_reimbursement/receipt5.png'),
('32.00', 5, 'Office supplies', 4, '2022-03-19 15:27', 1, null, null, 'https://storage.googleapis.com/employee_reimbursement/receipt5.png'),
('400.00', 6, 'Team building activity', 4, '2022-03-18 16:33', 1, null, null, 'https://storage.googleapis.com/employee_reimbursement/receipt5.png'),
('165.00', 1, 'Professional development', 4, '2020-11-27 15:08', 3, '2020-12-11 16:00', 3, 'https://storage.googleapis.com/employee_reimbursement/receipt1.jpeg'),
('499.00', 5, 'Annual membership fee', 4, '2022-01-01 11:18', 2, '2022-01-10 15:18', 2, 'https://storage.googleapis.com/employee_reimbursement/receipt2.jpeg'),
('394.00', 6, 'Airport overnight hotel', 1, '2022-02-17 12:23', 3, '2022-02-28 12:10', 2, 'https://storage.googleapis.com/employee_reimbursement/receipt3.png'),
('222.00', 1, 'Team bonding event', 3, '2022-03-26 14:15', 1, null, null, 'https://storage.googleapis.com/employee_reimbursement/receipt2.jpeg'),
('399.00', 5, 'GCP Certificate', 4, '2022-03-18 10:48', 1,  null, null, 'https://storage.googleapis.com/employee_reimbursement/receipt5.png'),
('182.00', 4, 'Client luncheon', 3, '2022-03-29 15:27', 1, null, null, 'https://storage.googleapis.com/employee_reimbursement/receipt5.png'),
('1000.00', 6, 'Tradeshow expenses', 2, '2022-03-28 16:33', 1, null, null, 'https://storage.googleapis.com/employee_reimbursement/receipt5.png');


-- View: users table with user role
create view user_with_user_role as
select u.id as user_id, u.username, u.password, u.first_name, u.last_name, u.email, ur.role as user_role
from users u
left join user_role ur
on ur.id = u.user_role_id;

-- View: reimbursement table with full details
create view reimbursement_full as
select r.id, r.amount, r.author_id, r.description, rt.type, r.receipt, r.submitted_at, rs.status, r.resolved_at, r.resolver_id
from reimbursement r 
left join reimb_status rs 
on r.status_id =rs.id
left join reimb_type rt 
on r.type_id = rt.id
order by r.submitted_at desc;