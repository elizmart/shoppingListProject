
drop table liza.shoppingList;

create table liza.shoppingList (
  id int,
  product varchar(50),
  amount int,
  status varchar(20),
  parentListId int,
  primary key (id),
  constraint FK_ParentList FOREIGN KEY (parentListId) references liza.listcollection(id)

);

insert into liza.shoppingList (id, product, amount, status)values
(1,  'milk', 2, 'active'),
(2, 'eggs', 10, 'active');

delete from liza.shoppingList where parentListId = 128;

delete from liza.listcollection where id = 128;

delete from liza.shoppingList where id>5;

delete from liza.listcollection where id != 28 AND id !=1;

select * from liza.shoppingList order by id desc;

select * from liza.listcollection order by id desc;

select product from liza.shoppingList;

select username from liza.user;

select * from liza.shoppingList;

