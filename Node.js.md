## Node.js

### Node.js简介

![image-20210801202123814](typora-user-images\image-20210801202123814.png)

![image-20220731135837341](Node.js.assets/image-20220731135837341.png)

Node.js的优点

![image-20210731173408385](typora-user-images\image-20210731173408385.png)

![image-20210731173425768](typora-user-images\image-20210731173425768.png)

![image-20210731173631140](typora-user-images\image-20210731173631140.png)

![image-20210731174411762](typora-user-images\image-20210731174411762.png)

![image-20220730160055076](Node.js.assets/image-20220730160055076.png)

Node采用了与Web Workers相同的思路来解决单线程中大计算量的问题：child_process。

![image-20211113145753200](Node.js.assets/image-20211113145753200.png)

### 原生内置模块

#### fs文件系统模块

![image-20220731135534881](Node.js.assets/image-20220731135534881.png)



##### fs.readFile()

![image-20220731140106108](Node.js.assets/image-20220731140106108.png)

![image-20220731140808446](Node.js.assets/image-20220731140808446.png)

##### fs.writeFile()

![image-20220731141254521](Node.js.assets/image-20220731141254521.png)

#### path路径模块

![image-20220731165120705](Node.js.assets/image-20220731165120705.png)

##### path.join()

![image-20220731165512167](Node.js.assets/image-20220731165512167.png)

 	![image-20220731165534804](Node.js.assets/image-20220731165534804.png)

##### path.basename()

![image-20220801151229337](Node.js.assets/image-20220801151229337.png)

![image-20220801151319877](Node.js.assets/image-20220801151319877.png)

##### path.extname() 

![image-20220801151354817](Node.js.assets/image-20220801151354817.png)

#### http模块

![image-20220801154541919](Node.js.assets/image-20220801154541919.png)

![image-20220801154555114](Node.js.assets/image-20220801154555114.png)

### 模块化

#### require()

![image-20220801155028571](Node.js.assets/image-20220801155028571.png)

#### module对象

![image-20220801155248859](Node.js.assets/image-20220801155248859.png)

#### module.exports()

在自定义模块中，可以使用 module.exports 对象，将模块内的成员共享出去，供外界使用。
外界用 require() 方法导入自定义模块时，得到的就是 module.exports 所指向的对象。

#### exports对象

![image-20220801155802767](Node.js.assets/image-20220801155802767.png)

#### exports和module.exports的使用误区

#### ![image-20220801160231880](Node.js.assets/image-20220801160231880.png)模块化规范

![image-20220802122003909](Node.js.assets/image-20220802122003909.png)

#### 开发自己的包

![image-20220802141317115](Node.js.assets/image-20220802141317115.png)

![image-20220802141327747](Node.js.assets/image-20220802141327747.png)

##### 发布包

![image-20220802141407326](Node.js.assets/image-20220802141407326.png)

![image-20220802141415383](Node.js.assets/image-20220802141415383.png)

![image-20220802141422326](Node.js.assets/image-20220802141422326.png)

![image-20220802141428656](Node.js.assets/image-20220802141428656.png)

#### 模块加载机制

![image-20220802210925239](Node.js.assets/image-20220802210925239.png)

![image-20220802210941483](Node.js.assets/image-20220802210941483.png)

![image-20220802210950656](Node.js.assets/image-20220802210950656.png)

![image-20220802211000361](Node.js.assets/image-20220802211000361.png)

![image-20220802211011493](Node.js.assets/image-20220802211011493.png)

### Express

![image-20220803145520058](Node.js.assets/image-20220803145520058.png)

![image-20220803145613736](Node.js.assets/image-20220803145613736.png)

#### 基本使用

##### 基本的web服务器

![image-20220803153045647](Node.js.assets/image-20220803153045647.png)

##### 监听GET请求

![image-20220803153008736](Node.js.assets/image-20220803153008736.png)

##### 监听POST请求

![image-20220803153122217](Node.js.assets/image-20220803153122217.png)

##### 把内容响应给客户端

![image-20220803153255757](Node.js.assets/image-20220803153255757.png)

##### 获取 URL 中携带的查询参数

![image-20220803153518974](Node.js.assets/image-20220803153518974.png)

##### 获取 URL 中的动态参数

![image-20220803153546894](Node.js.assets/image-20220803153546894.png)

#### 托管静态资源

##### express.static()

![image-20220803155624280](Node.js.assets/image-20220803155624280.png)

##### 托管多个静态资源目录

![image-20220803160016642](Node.js.assets/image-20220803160016642.png)

##### 挂载路径前缀

![image-20220803160221706](Node.js.assets/image-20220803160221706.png)

#### nodemon

##### 作用

![image-20220803160422994](Node.js.assets/image-20220803160422994.png)



#### 路由

##### 模块化路由

![image-20220803231343737](Node.js.assets/image-20220803231343737.png)

![image-20220804145753869](Node.js.assets/image-20220804145753869.png)

![image-20220804145758189](Node.js.assets/image-20220804145758189.png)

#### 中间件

![image-20220804145901956](Node.js.assets/image-20220804145901956.png)

##### next 函数的作用

![image-20220804145945555](Node.js.assets/image-20220804145945555.png)

##### 定义中间件函数

![image-20220804150717684](Node.js.assets/image-20220804150717684.png)

##### 全局生效的中间件

![image-20220804150734341](Node.js.assets/image-20220804150734341.png)

##### 定义全局中间件的简化形式

![image-20220804150752584](Node.js.assets/image-20220804150752584.png)

##### 中间件的作用

![image-20220804150825629](Node.js.assets/image-20220804150825629.png)





##### 定义多个全局中间件

![image-20220804151807014](Node.js.assets/image-20220804151807014.png)

##### 局部生效的中间件

![image-20220804152555867](Node.js.assets/image-20220804152555867.png)

##### 定义多个局部中间件

![image-20220804152816235](Node.js.assets/image-20220804152816235.png)

##### 了解中间件的5个使用注意事项

![image-20220804153252310](Node.js.assets/image-20220804153252310.png)

##### 中间件的分类

![image-20220804153509386](Node.js.assets/image-20220804153509386.png)

###### 应用级别的中间件

![image-20220804153538846](Node.js.assets/image-20220804153538846.png)

######  路由级别的中间件

![image-20220804153557792](Node.js.assets/image-20220804153557792.png)

###### 错误级别的中间件

![image-20220804153630834](Node.js.assets/image-20220804153630834.png)

![image-20220804153643267](Node.js.assets/image-20220804153643267.png)

###### Express内置的中间件

![image-20220804153702202](Node.js.assets/image-20220804153702202.png)

###### 第三方的中间件

![image-20220804153719438](Node.js.assets/image-20220804153719438.png)

##### 自定义中间件

![image-20220805152136375](Node.js.assets/image-20220805152136375.png)

![image-20220805152147577](Node.js.assets/image-20220805152147577.png)

![image-20220805152159206](Node.js.assets/image-20220805152159206.png)

![image-20220805152210346](Node.js.assets/image-20220805152210346.png)

![image-20220805152218593](Node.js.assets/image-20220805152218593.png)

![image-20220805152226613](Node.js.assets/image-20220805152226613.png)

![image-20220805152235475](Node.js.assets/image-20220805152235475.png)



#### 接口

##### 编写 GET 接口

![image-20220807001014124](Node.js.assets/image-20220807001014124.png)

![image-20220807000847397](Node.js.assets/image-20220807000847397.png)

##### 编写 POST 接口

![image-20220807000917889](Node.js.assets/image-20220807000917889.png)

#### 跨域

##### 使用 cors 中间件解决跨域问题

![image-20220807001430262](Node.js.assets/image-20220807001430262.png)

##### cors相关的三个响应头

###### Access-Control-Allow-Origin

![image-20220807005105181](Node.js.assets/image-20220807005105181.png)

###### Access-Control-Allow-Headers

![image-20220807005330902](Node.js.assets/image-20220807005330902.png)

###### Access-Control-Allow-Methods

![image-20220807005405158](Node.js.assets/image-20220807005405158.png)

##### CORS请求的分类

###### 简单请求

![image-20220807011126986](Node.js.assets/image-20220807011126986.png)

###### 预检请求

![image-20220807011147868](Node.js.assets/image-20220807011147868.png)

###### 简单请求和预检请求的区别

![image-20220807020138083](Node.js.assets/image-20220807020138083.png)

#### 操作mysql

![image-20220809162459107](Node.js.assets/image-20220809162459107.png)

##### 配置 mysql 模块

![image-20220809162529529](Node.js.assets/image-20220809162529529.png)

##### 测试 mysql 模块能否正常工作

![image-20220809162546320](Node.js.assets/image-20220809162546320.png)

##### 示例

![image-20220809162756707](Node.js.assets/image-20220809162756707.png)

![image-20220809162805061](Node.js.assets/image-20220809162805061.png)

![image-20220809162813728](Node.js.assets/image-20220809162813728.png)

![image-20220809162825262](Node.js.assets/image-20220809162825262.png)

![image-20220809162841575](Node.js.assets/image-20220809162841575.png)

![image-20220809162855005](Node.js.assets/image-20220809162855005.png)

![image-20220809162909525](Node.js.assets/image-20220809162909525.png)

#### 身份认证

![image-20220807220219261](Node.js.assets/image-20220807220219261.png)

##### Session 认证机制

###### HTTP 协议的无状态性

![image-20220807220326140](Node.js.assets/image-20220807220326140.png)

###### Cookie

![image-20220808135358913](Node.js.assets/image-20220808135358913.png)

![image-20220808135629946](Node.js.assets/image-20220808135629946.png)

![image-20220808150826183](Node.js.assets/image-20220808150826183.png)

![image-20220808152503048](Node.js.assets/image-20220808152503048.png)

###### Session的工作原理

![image-20220808152653294](Node.js.assets/image-20220808152653294.png)

###### Session的局限性

![image-20220809164300047](Node.js.assets/image-20220809164300047.png)

##### 在 Express 中使用 Session 认证

###### 配置 express-session 中间件

![image-20220808153634765](Node.js.assets/image-20220808153634765.png)

###### 向 session 中存数据

![image-20220808162007211](Node.js.assets/image-20220808162007211.png)

###### 从 session 中取数据

![image-20220808162044376](Node.js.assets/image-20220808162044376.png)

###### 清空 session

![image-20220808162443489](Node.js.assets/image-20220808162443489.png)

##### JWT 认证机制                                  

###### JWT 的工作原理

![image-20220809164352906](Node.js.assets/image-20220809164352906.png)

###### JWT 的组成部分

![image-20220809175439267](Node.js.assets/image-20220809175439267.png)

![image-20220809175936243](Node.js.assets/image-20220809175936243.png)

##### 在 Express 中使用 JWT

###### JWT 的使用方式

![image-20220809180111353](Node.js.assets/image-20220809180111353.png)

###### 安装 JWT 相关的包

![image-20220809180257908](Node.js.assets/image-20220809180257908.png)

###### 导入 JWT 相关的包

![image-20220811142756564](Node.js.assets/image-20220811142756564.png)

###### 定义 secret 密钥

![image-20220811143353750](Node.js.assets/image-20220811143353750.png)

###### 在登录成功后生成 JWT 字符串

![image-20220811143557573](Node.js.assets/image-20220811143557573.png)

###### 将 JWT 字符串还原为 JSON 对象

![image-20220811143921000](Node.js.assets/image-20220811143921000.png)

###### 使用 req.user 获取用户信息

![image-20220811144743729](Node.js.assets/image-20220811144743729.png)

###### 捕获解析 JWT 失败后产生的错误

![image-20220811145242827](Node.js.assets/image-20220811145242827.png)

