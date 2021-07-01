--insert into data_shop.order_summary values('2','�㶫','��ױƷ','Ů',5,200);
--insert into data_shop.order_summary values('2','�㶫','�ľ�','��',10,100);
--insert into data_shop.order_summary values('3','����','�ľ�','��',10,100);
--insert into data_shop.order_summary values('3','�Ͼ�','�ҵ�','��',1,4000);

--���²�ѯ�������۶�
select data_month,sum(order_total_price)
from data_shop.order_summary
group by data_month;

--insert into data_shop.user_summary values('3',50000,4000);
--insert into data_shop.user_summary values('4',54000,3000);

--���²鿴����ע������
select data_month,curmonth_user_new_num
from data_shop.user_summary
order by data_month;

--insert into data_shop.city_summary values('2','�㶫',15,300);
--insert into data_shop.city_summary values('3','����',10,100);
--insert into data_shop.city_summary values('3','�Ͼ�',1,4000);

--���²鿴���е����۶�
select data_month, city_name, sum(order_total_price)
from data_shop.city_summary
group by data_month, city_name;

--insert into data_shop.product_sort_summary values('2','��ױƷ',5,200);
--insert into data_shop.product_sort_summary values('2','�ľ�',10,100);
--insert into data_shop.product_sort_summary values('3','�ľ�',10,100);
--insert into data_shop.product_sort_summary values('3','�ҵ�',1,4000);
--
--insert into data_shop.gender_summary values('2','Ů',5,200);
--insert into data_shop.gender_summary values('2','��',10,100);
--insert into data_shop.gender_summary values('3','��',10,100);
--insert into data_shop.gender_summary values('3','��',1,4000);

--���²鿴���С���Ʒ�������۶�
select data_month, city_name, product_sort_name, sum(order_total_price)
from data_shop.product_sort_summary natural join data_shop.city_summary
group by data_month, city_name, product_sort_name;

--���²鿴�Ա���Ʒ�������۶�
select data_month, gender, product_sort_name, sum(order_total_price)
from data_shop.product_sort_summary natural join data_shop.gender_summary
group by data_month, gender, product_sort_name;


