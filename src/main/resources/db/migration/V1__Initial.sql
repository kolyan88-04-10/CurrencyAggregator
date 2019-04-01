create table banks (
    id bigint not null AUTO_INCREMENT,
    name varchar(255) not null,
    primary key (id)) engine=MyISAM;
create table currency (
    id bigint not null AUTO_INCREMENT,
    name varchar(255) not null,
    primary key (id)) engine=MyISAM;
create table currency_rates (
    bank_Id bigint not null,
    currency_Id bigint not null,
    purchase_rate DECIMAL(10,2) not null,
    sale_rate DECIMAL(10,2) not null) engine=MyISAM;

alter table currency_rates
    add constraint bank_currency_fk
    foreign key (bank_Id) references bank (id);
alter table currency_rates
    add constraint currency_bank_fk
    foreign key (currency_Id) references currency (id);
