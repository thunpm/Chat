drop database if exists chat;

create database chat;

use chat;

create table User (
	Id varchar(10) primary key,
    Username varchar(50) not null,
    Password varchar(50) not null,
    Name nvarchar(50) not null
);

create table FriendUser (
	IdUser varchar(10) not null,
    IdFriend varchar(10) not null,
    
    primary key (IdUser, IdFriend)
);

insert User(Id, Username, Password, Name)
values
	('US01', 'thu', '123', 'Thu'),
    ('US02', 'duong', '123', 'Duong'),
    ('US03', 'ngan', '123', 'Ngan'),
    ('US04', 'cam', '123', 'Cam');
    
insert FriendUser(IdUser, IdFriend)
values
	('US01', 'US02'),
    ('US01', 'US03'),
    ('US01', 'US04'),
    ('US02', 'US01'),
    ('US02', 'US03'),
    ('US03', 'US01'),
    ('US03', 'US02'),
    ('US04', 'US01');

