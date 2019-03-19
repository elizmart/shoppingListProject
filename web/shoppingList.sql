
drop table liza.shoppingList;

create table liza.shoppingList (
  id int not null auto_increment,
  product varchar(50),
  amount int,
  status varchar(20),
  parentListId int,
  primary key (id),
  constraint FK_ParentList FOREIGN KEY (parentListId) references liza.listcollection(id)

);

create table liza.listcollection(
  id int not null auto_increment,
  name varchar(50),
  user_id int,
  primary key (id),
  constraint FK_AuthoringUser FOREIGN KEY (user_id) references liza.user(id)
);
insert into liza.shoppingList (id, product, amount, status, parentListId)values
(1,  'milk', 2, 'active', 1),
(2, 'eggs', 10, 'active', 2);

insert into liza.listcollection(id, name, user_id)values
(1, 'toys', 2),
(2, 'phones',3);

insert into liza.user (id, username, email, password) values (1, 'Liza', '123@mail.ru', '123');

delete from liza.shoppingList where parentListId = 128;

delete from liza.listcollection where id = 128;

delete from liza.shoppingList where id>5;

delete from liza.listcollection where id != 28 AND id !=1;

select * from liza.shoppingList order by id desc;

select * from liza.listcollection order by id desc;

select product from liza.shoppingList;

select username from liza.user;

select * from liza.shoppingList;

select * from liza.user;

delete from liza.shoppingList where id>150;

drop table liza.shoppingList;

drop table liza.listcollection;


