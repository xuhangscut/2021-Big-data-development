--insert into data_shop.order_summary values('2','广东','化妆品','女',5,200);
--insert into data_shop.order_summary values('2','广东','文具','男',10,100);
--insert into data_shop.order_summary values('3','北京','文具','男',10,100);
--insert into data_shop.order_summary values('3','南京','家电','男',1,4000);

--按月查询总体销售额
select data_month,sum(order_total_price)
from data_shop.order_summary
group by data_month;

--insert into data_shop.user_summary values('3',50000,4000);
--insert into data_shop.user_summary values('4',54000,3000);

--按月查看新增注册人数
select data_month,curmonth_user_new_num
from data_shop.user_summary
order by data_month;

--insert into data_shop.city_summary values('2','广东',15,300);
--insert into data_shop.city_summary values('3','北京',10,100);
--insert into data_shop.city_summary values('3','南京',1,4000);

--按月查看城市的销售额
select data_month, city_name, sum(order_total_price)
from data_shop.city_summary
group by data_month, city_name;

--insert into data_shop.product_sort_summary values('2','化妆品',5,200);
--insert into data_shop.product_sort_summary values('2','文具',10,100);
--insert into data_shop.product_sort_summary values('3','文具',10,100);
--insert into data_shop.product_sort_summary values('3','家电',1,4000);
--
--insert into data_shop.gender_summary values('2','女',5,200);
--insert into data_shop.gender_summary values('2','男',10,100);
--insert into data_shop.gender_summary values('3','男',10,100);
--insert into data_shop.gender_summary values('3','男',1,4000);

--按月查看城市、商品类别的销售额
select data_month, city_name, product_sort_name, sum(order_total_price)
from data_shop.product_sort_summary natural join data_shop.city_summary
group by data_month, city_name, product_sort_name;

--按月查看性别、商品类别的销售额
select data_month, gender, product_sort_name, sum(order_total_price)
from data_shop.product_sort_summary natural join data_shop.gender_summary
group by data_month, gender, product_sort_name;


