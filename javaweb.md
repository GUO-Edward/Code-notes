## 静态web

![image-20210925160943245](typora-user-images\image-20210925160943245.png)

### 静态web存在的缺点

web页面无法动态更新，所有用户看到的都是同一个页面

无法和数据库交互（数据无法持久化，用户无法交互）

## 动态web

![image-20210925161342085](typora-user-images\image-20210925161342085.png)

![image-20210925161413571](typora-user-images\image-20210925161413571.png)

## XML

xml是可拓展的标记性语言

### xml主要作用

1.用来保护数据，而且这些数据具有自我描述性

2.它还可以作为项目或者模块的配置文件

3.还可以作为网络传输数据的格式

## Tomcat服务器

### 如何启动Tomcat

#### 第一种

找到Tomcat目录下的startup.bat文件 点击 启动Tomcat服务器

#### 第二种

1.打开命令行

2.cd到Tomcat的bin目录下

3.敲入启动命令：catalina run 

### 测试Tomcat服务器启动成功

打开浏览器，在浏览器地址中输入以下地址测试

1.http://localhost:8080

2.http://127.0.0.1:8080

3.http://真实ip:8080

出现猫 则说明启动成功

### 如何修改Tomcat的端口号

Tomcat 默认的端口号是 8080

找到Tomcat 目录下的conf目录，找到server.xml配置文件

## Servlet

### servlet简介

1、Servlet是JavaEE规范之一  规范就是接口

2、Servlet是JavaWeb三大组件之一 三大嘴贱饭别是：Serbvlet程序、Filter过滤器、Listener监听器

3、Servlet是运行在服务器上的一个java小程序，它可以接收客户端发送过来的请求，并响应数据给客户端

### 手动实现Servlet程序

1、编写一个类去实现Servlet接口

2、实现Service方法，处理请求，并响应数据

3、到web.xml中去配置Servlet程序的访问地址

![image-20211005162114091](typora-user-images\image-20211005162114091.png)

### Servlet的生命周期

1、执行Servlet构造器方法

2、执行init初始化方法

3、执行service方法

4、执行destory销毁方法

第1 2 步是在第一次访问的时候创建Servlet程序会调用

第 3 步每次访问都会调用

第 4 步 在web工程停止的时候调用

### 通过继承HttpServlet实现Servlet程序

一般在实际开发项目中，都是使用继承HttoServlet类的方法去实现Servlet程序

1、编写一个类去继承HttpServlet类

2、根据业务需要去重写doGet和doPost方法

3、到web.xml中去配置Servlet程序的访问地址

### 整个Servlet类的继承体系

![image-20211005181615570](typora-user-images\image-20211005181615570.png)

### 用eclipse创建自己的servlet程序

第一步，创建Web project。（File->New->Project..）在里面找到Web选项

## ServletConfig类

ServletConfig类是Servlet程序的配置信息类

Servlet程序和ServletConfig对象都是由Tomcat负责创建，我们负责使用

Servlet程序默认是第一次访问的时候创建的，ServletConfig是每个Servlet程序创建时，就创建一个对应的ServletConfig对象

### ServletConfig类的三大作用

1、可以获取Servlet程序的别名 servlet-name的值

2、获取初始化参数 init-param

3、获取ServletContext对象

## ServletContext类

### ServletContext类的介绍

1、ServletContext是一个接口，它表示Servlet上下文对象

2、一个web工程，只有一个ServletContext对象实例

3、ServletContext对象是一个域对象

域对象：是可以像Map一样存储数据的对象，叫域对象

这里的域指的是存取数据的操作范围

![image-20211005185359455](typora-user-images\image-20211005185359455.png)

### ServletContext类的四个作用

1、获取web.xml中配置的 (上下文参数)context-param

2、获取当前的工程路径，格式：/工程路径

3、获取工程部署后再服务器硬盘上的绝对路径

4、像Map一样存储数据

## Http协议

Http协议中的数据又叫报文

### 请求的Http协议格式

客户端给服务器发送数据叫请求

服务器给客户端传递数据叫响应

#### Get请求

1.请求行

​	(1)请求的方式    					GET

​	(2)请求的资源路径[+?+请求参数]

​	(3)请求的协议的版本号		http/1.1

2.请求头

​	key：value 组成  	不同的键值对 表示不同的含义

![image-20211007194443041](typora-user-images\image-20211007194443041.png)

#### Post请求

1.请求行

​	(1)请求的方式    					GET

​	(2)请求的资源路径[+?+请求参数]

​	(3)请求的协议的版本号		http/1.1

2.请求头

​	key：value 组成  	不同的键值对 表示不同的含义

​	**空行**

3.请求体 ====》就是发送给服务器的数据

##### post请求中文乱码问题

需要设置请求提的字符集为UTF-8来解决中文乱码问题

```
req.setCharacterEncoding("UTF-8")
需要设置在请求参数之前调用才有效
```

![image-20211007200302790](typora-user-images\image-20211007200302790.png)

### 响应的http协议格式

1.响应行

​	(1)响应的协议和版本号			

​	(2)响应状态码							

​	(3)响应状态描述符			

2.响应头

​	key：value  不同的响应头，有其不同的含义

3.响应体 ==》回传给客户端的数据

![image-20211007201752136](typora-user-images\image-20211007201752136.png)

### 常用的响应码说明

200 表示请求成功

302 表示请求重定向

404 表示请求服务器已经收到了，但是想要的数据不存在（请求地址错误）

500 表示服务器已经收到请求，但是服务器内部错误（代码错误）

### 如何查看Http协议

![image-20211011193537573](typora-user-images\image-20211011193537573.png)

### HttpServletRequest类

#### 介绍

每次只要有请求进入Tomcat服务器，Tomcat就会把请求过来的Http协议信息解析好封装到Request对象中

然后传递到service方法(doGet和doPost)中给我们使用。

我们可以通过HttpServletRequest对象，获取到所有的请求信息

#### 常用方法

1、getRequestURI()		获取请求的资源路径

2、getRequestURL()		获取请求的统一资源定位符（绝对路径）

3、getRemoteHost()		获取客户端的ip地址

4、getHeader()				获取请求头

5、getParameter() 		 获取请求的参数

6、getParameterValues() 	获取请求的参数（多个值得时候使用）

7、getMethod()				获取请求的方式Get或Post

8、setAttribute(key,value)	设置域数据

9、getAttribute(key)		获取域数据

10、getRequestDispatcher()	获取请求转发对象

#### base标签的作用

base标签设置页面相对路径工作时参照的地址   href属性就是参数的地址值

```
写在html文件中的head里面
<base href='http://localhost:8080/a/b/c.html(最后一个可省 但/不可省)'>
```

