drop table if exists ordertbl;
create table ordertbl
(
   order_id integer not null auto_increment primary key,
   symbol varchar(64) not null,
   side varchar(64) check (side in ('Buy', 'Sell')) not null,
   amount integer not null,
   price integer not null,
   source varchar(64),
   created_on timestamp with time zone,
   updated_on timestamp with time zone
);