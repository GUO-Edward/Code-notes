#### MySQL

进入服务界面:右击此电脑 点击管理 点击服务和应用程序 之后点击服务即可;

#### MySQL的语法规范

1.不区分大小写,但建议关键字大些,表名 列名小写;
2.每条命令最好用分号结尾;
3.每条命令根据需要,可以进行缩进 或换行;

#### MySQL服务端的登陆和退出

在管理员窗口输入 net stop + 需要停止的服务名称
net start + 需要开始的服务名称;

从管理员窗口进入mysql系统：mysql -h localhost -P 3306 -u root -p或-p直接加密码
          (如果本机为3306) 直接输入mysql -u root -p或-p直接加密码
单行注释:#注释文字
	-- + 空格 +注释文字;
多行注释:/* 注释文字　*/

#### MySQL常见命令

1. 展示数据库：show databases; 
2. 打开数据库：use + 数据库名;
3. 显示数据库中的表：show tables; 
4. 从当前库中查看下一个库(并没有出去当前库)：show tables form + 数据库名; 
5. 查看当前所在库：select database(); 
6. 查看当前版本：select version(); 或在管理员窗口输入 mysql --version或-V
7. 创建表：create tables 表名( 列名 列类型,列名 列类型 ) 
    create table reded(id int,name varchar(20));
9. desc 表名; 查看表结构;
10. 查看表中数据：select * form 表名; 
11. 插入数据：insert into 666(id,name) values(2,'john'); 
12. 更新数据：update 666 set name='lilei' where id=1;
13. 删除数据：delete form 666 where id=1;
	      truncate table 表名; 
	      drop table 表名; (删除表结构)
14. ``用来区分表中的属性与查询的名字是否重复

起别名:
15. select last_name 姓 from employees;
16. 去重: 案例:查询员工表中涉及到的所有部门的编号
     select distinct department_id from employees;
17. concat的用法  案例:查询员工名和姓连接为一个字段,并显示为 姓名
     select concat(last_name,first_name) as 姓名 form employees;

#### 条件查询

语法：select 查询列表 form 表名 where 筛选条件;

##### 分类:  一. 按条件表达式筛选

​	条件运算符:> < = != <> >= <=
​          案例: 查询工资>12000的员工信息
​	select * from employees where sallary>12000;

#####    二. 按逻辑表达式筛选

   	逻辑运算符:
   	&& || !
   	and or not 
案例1：查询工资在10000到20000之间的员工名,工资以及奖金

```mysql
select last_name,salary,commission_pct from employees where salary >= 10000 and salary <= 20000; 
```

案例2：查询部门编号不是在90到110之间,或者工资高于150000的员工信息

```mysql
select * from employees where not(department_id>=90 and department_id<=110) or salary>15000;
```

#####    三. 模糊查询

   		like 特点 1.一般和通配符搭配使用 %任意多个字符 _任意单个字符
   	between and_
   	in  判断某字段的值是否属于in列表中的某一项(里面不能使用通配符)
   	is null / is not null  '=' 不能用来判断null值

​	<=> 安全等于：既可以判断null值 又可以判断普通的数值

案例1：查询员工名中包含字符a的员工信息

```mysql
	select * from employees where last_name like '%a%';(%代表任意字符)
```

案例2：查询员工名中第二个字符为_的员工名

```mysql
select * from employees where Last_name like '_\_%' = '_$_%' escape '$';('\'代表转义)
```

案例3：查询员工的工种编号是 IT_PROG AD_VP AD_PRES中的一个员工和工种编号

```mysql
select last_name,job_id from employees where job_id in('IT_PROG','AD_VP','AD_PRES')
```

#### 排序查询

select 查询列表 from 表 (where 筛选条件) order by 排序列表 【asc(升序)|desc(降序)】(不写默认升序)

​	案例：查询员工信息,要求工资从高到低排序

```mysql
select * from employees order by salary desc;
```

​	order by子句中可以支持单个字段,多个字段,表达式,函数,别名
​		一般放在最后面,limit子句除外

​	group by子句将表中的数据分成若干组

​	SQL 中增加 HAVING 子句原因是，WHERE 关键字无法与合计函数一起使用 HAVING短语作用于组 从中选择满足条件的组。 

函数执行时都需和select搭配

#### 单行函数

##### 1.字符函数

​	length('参数') 获取参数值得字节个数
​	concat(参数,参数) 拼接字符串
​	upper('john') 变大写
​	lower('JOHN') 变小写
​	substr,substring('abcde',3) 123; 截取从制定索引处后面所有字符
​	substr,substring('abcde',1,3) 123; 截取从制定索引处指定字符长度的字符
​	案例：姓名中首字符大写,其他字符小写然后用_拼接,显示出来
​		select concat(upper(substr(last_name,1,1)),'_',lower(substr(last_name,2))) output from employee;
​	trim( '213' ) 不显示前后的空格
​	lpad('123',10,'*') 用指定的字符实现左填充至指定长度
​	rpad('123',10,'*') 用指定的字符实现右填充至指定长度
​	replace('123','3','1') 将'3'替换成'1'
​	计数
​	COUNT（[ DISTINCT | ALL ] *）
​	COUNT（[ DISTINCT | ALL ] <列名>）
​	计算总和
​	SUM（[ DISTINCT | ALL ] <列名>）	
​	计算平均值
​	AVG（[ DISTINCT | ALL ] <列名>）
​	最大最小值
 	 MAX（[ DISTINCT | ALL ] <列名>）
​	 MIN（[ DISTINCT | ALL ] <列名>）

##### 2.数学函数

​	round(值) 四舍五入
​	ceil 向上取整
​	floor 向下取整
​	truncats 截断
​	mod  取余
3.日期函数：
​	now() 返回当前日期 + 时间
​	curdate() 返回当前系统日期,不包含时间
​	curtime() 返回当前时间,不包括日期

#### 分组函数 

​	 (用作统计使用,又称为聚合函数或统计函数或组函数)
​	sum 求和
​	avg 求平均值
​	max 最大值
​	min 最小值
​	count 计算个数 
   1.sum、avg 一般用于处理数值型
​     max、min、count 可以处理任何类型
   2.以上分组函数都忽略null值
   3.可以和distinct搭配去实现去重的运算
   4.count详细介绍 count(*) 统计行数

#### 连接查询

又称多表查询
	笛卡尔乘积现象：表1 有m行 ,表2 有n行 结果=m*n行
	按功能分类

##### 		内连接

​			等值连接
​			非等值连接
​			自连接
​		select 查询列表 form 表1 别名 inner join 表2 别名 on 连接条件;

##### 		外连接

​			左外连接
​		select 查询列表 form 表1 别名 left join 表2 别名 on 连接条件;
​			右外连接
​		select 查询列表 form 表1 别名 right join 表2 别名 on 连接条件;
​			全外连接
​		select 查询列表 form 表1 别名 full join 表2 别名 on 连接条件;
​		交叉连接

相关子查询：exists() 结果：1 或 0;

##### 分页查询

​	查询前五条员工信息
​	select * from employee limit 0,5;

#### 表的修改

1.修改列名

```mysql
alter table student change column name sname varchar(10);
```

2.修改列的类型或约束

```mysql
alter table student modify column id double;
```

3.添加新列

```mysql
alter table student add column address char(10);
```

4.删除列

```mysql
alter table student drop column id;
```

5.修改表名

```mysql
alter table student rename column to s_student;
```

复制表的结构

```mysql
create table student1 like student;
```

复制表的结构+数据

```mysql
create table student2 select *from student;
```

#### 六大约束

1.not null	保证该字段不能为空

2.default	用于保证该字段有默认值

3.primary key	用于保证该字段具有唯一性



```mysql
删除主键
alter table student drop primary key;
```

4.unique	用于保证该字段具有唯一性,可为空

5.check	检查约束[mysql中不支持 但不报错]

6.foreign key	用来限制两个表的关系,用于保证该字段的值必须来自主表的关联列的值,该关联列一般为主键或unique

六大约束都可以充当列级约束 但外键无效果

除了非空,默认,其他约束都可以充当表级约束

```mysql
添加列级约束:
create table stuinfo(
		id int primary key,#主键
    	stuName varchar(20) not null,#非空
    	gender char(2) check(gender='男' or gender='女'),#检查
    	seat int unique,#默认约束
    	majorID int foreign key
    	references major(id)[引用来自主表的id]#外键
)
```

```mysql
添加表级约束:
语法：在各个字段的最下面添加
[constraint 约束名](可省) 约束类型(字段名);
```