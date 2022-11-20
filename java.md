#### 常见的DOS命令

 -exit 退出窗口
 -cls 清屏
 -DOS里面内容的赋值
    任意位置点击鼠标右键,选择标记,选中复制的内容,在任意位置点击鼠标右键,粘贴即可;
 -dir 列出当前目录下所有的子文件/子目录
 -md 创建目录
 -rd 删除文件
 -cd命令
   *cd命令表示:change directory【改变目录】
   *如何使用cd命令?
    cd目录的路径
   *绝对路径:表示该路径从某个磁盘的盘符下作为出发点的路径
   *相对路径:表示改路径从当前所在的路径下作为出发点的路径
 -cd..回到上级目录
 -cd\直接回到根目录
 -如何切换盘符:
  c:回车
  d:回车
  e:回车
  f:回车
 -del 删除文件

swin+R输入cmd(打开DOS窗口)

java区分大小写 windows不区分大小写

####  Java语言特性

【开放源代码 免费 纯面向对象 跨平台】

 (JKD = JRE + 开发工具集(javac等))

 (JRE = JVM + Java SE标准类库)

######  *简单性	

  相对而言,java不再支持多继承,c++是支持多继承的,多继承比较复杂
  c++中有指针,java中屏蔽了指针的概念.
  所以相对来说简单;

  java语言底层是C++实现的,不是c语言.

######  *面向对象

  java是纯面向对象的。更符合人的思维方式。更易理解。

######  *健壮性

 和自动垃圾回收机制有关，自动垃圾回收机制简称GC机制。
  java语言运行过程中产生的垃圾是自动回收的，不需要程序员关心。

######  *可移植性

  java程序可以在windows操作系统上运行，
  不做任何修改。同样的java程序可以直接放到linux操作系统上运行。
  或者叫做跨平台。

#### 文档注释(java特有)

/**  */
注释内容可以被JDK提供的工具javadoc所解析,生成一套以网页文件形式体现的说明文档

```java
javadoc -d 名字 -author -version 文件名.java
```

#### APT文档：(字典)

API(应用程序编程接口) 是java提供的基本编程接口 习惯上将语言提供的类库都称为API;

在一个java源文件中可以声明多个class 但是只能最多有一个类声明为public的
而且要求声明为public的类的类名必须与源文件名相同;
public static void main(String[] args(参数 可变))

编译以后,会生成一个或多个字节码文件,字节码文件的文件名与java源文件中的类名相同
但是只能运行包含main的类;

应用程序 = 算法 + 数据结构
char类型占用两个字节;
声明long型变量时 值最后必须以'l'或'L'结尾;
声明float型变量时 值最后必须以'f'或'F'结尾;
整数变量默认为int 小数默认为double;

当容量小的数据类型的变量与容量大的数据类型的变量做运算时,结果自动提升为容量大的数据类型;
byte,char,short-->int-->long-->float-->double
特别的:当byte,char,short(包括同种类型)三种类型的变量做运算时,结果为int型

String可以和8种基本数据类型变量做运算,且运算只能是连接运算：+
运算的结果仍然是String类型;

二进制以0b或0B开头;
八进制以0开头;
十六进制以0x或0X开头;

比较运算符的结果为boolean类型;

##### 区分&与&&

相同点：&与&&的运算结果相同;当符号左边是true时,二者都会执行符号右边的运算;
不同点；当符号左边是false时,&继续执行右边的运算,&&则不会;

##### 区分|与||

相同点：|与||的运算结果相同;当符号左边是false时,二者都会执行符号右边的运算;
不同点；当符号左边是true时,|继续执行右边的运算,||则不会;

##### 位运算符

1.位运算符操作的都是整型的数据
2.<< ：在一定范围内,每向左移1位,相当于 *2;

：在一定范围内,每向左移1位,相当于 /2;
(被移位的二进制最高位是0,右移后,空缺补0;最高位是1,空缺位补1)

###### 经典面试题

```java
最高效方式的计算 2 * 8 ？2 << 3 或8 << 1;
```

3.&二进制位进行&运算,只有1&1时结果是1,否则是0;
4.|二进制位进行|运算,只有0|0时结果是1,否则是0;
5.^相同二进制进行^运算,结果是0;1^1=0,0^0=0;
m=(m^n)^n,n=(m^n)^m;
num1=num1^num2;
num2=num1^num2;
num1=num1^num2;
^不同二进制进行^运算结果是1,1^0=1,0^1=1;
6.~正负数取反,各二进制码按补码各位取反;

##### Scanner类

从键盘中获取不同类型的变量,需要使用Scanner类
1.开头引用import java.util.Scanner;
2.Scanner的实例化:Scanner scan = new Scanner(Steam.in);
3.调用Scanner类的相关方法,用来获取指定类型的变量;
int num = scan.nextInt();
(Scanner中没有char型的获取,只能获取一个字符串)
{
	String gender = scan.next();
	char genderChar = gender.charAt(0);

	gender.equals("123");//与123进行匹配,返回一个boolean值
}

##### 随机数

如何获取一个随机数：[10,99]
int value = (默认值为double)(int)(Math.random() *90 +10);-
  ([0.0,1.0)-->(0.0,90.0)-->[10,99])
公式；[a,b] ：(int)(Math.random()*(b- a + 1) + a);

如果想要结束指定标识的一层循环结构,则在指定循环前加一标签 然后在break后加标签名;

#### 十大内部排序方法

选择排序:直接选择排序,堆排序;
交换排序:冒泡排序,快速排序;
插入排序:直接插入排序,折半插入排序,shell排序;
归并排序;
桶式排序;
基数排序;

数组的静态初始化:
int[] ids;
ids = new int[]{1001};
动态初始化:
int[] ids;
ids = new int[5];
获取数组的长度: ids.length;

#### Arrays工具类的使用

需引用import java.util.Arrays;
1.boolean equals(int[] a,int[] b):判断两个数组是否相同;
boolen isEquals = Arrays.equals(arr1,arr2);

2.String toString(int[] a):输出数组信息;
System.out.println(Arrays.toString(arr1));

3.void fill(int[] a,int val):将指定值填充到数组之中;
(数组中的值全变为val)

4.void sort(int[] a):对数组进行排序;
Arrays.sort(arr1);

5.int binarySearch(int[] a,int key):对排序的数组进行二分法检索指定的值;
int index = Arrays.binarySearch(arr3,211);(返回该数的位置,如果index为负数,则未找到该数;

修饰类只能用缺省或pulic;

#### stastic 

静态资源会随着类的加载而加载,随着类的消亡而消亡,只加载一次;
又称为共享资源;
静态只能调用静态 非静态的可以调用静态和非静态;
	开发中,如何确定一个属性是否要声明为static？
		>属性是可以被多个对象所共享的,不会随着对象的不同而不同的

	开发中,如何确定一个属性是否要声明为static？
		>操作静态属性的方法,通常设置为static
		>工具类中的方法,习惯上声明为static 比如Math,Arrays,Collections
#### 代码块的作用

用来初始化类,对象
静态代码块: static{}
局部代码块:
构造代码块:在构造对象之前先执行构造代码块的内容
同步代码块:

#### extends 继承

子类继承父类关键字

final修饰类时,该类是最终类,不可被继承
final修饰变量时,该变量为最终变量,只能被赋值一次
final修饰成员方法时,该方法为最终方法,该方法不能被重写

同一个包中的其他类,不可以调用类中私有的属性 方法
在不同包中的子类中,不能调用父类中声明为private和default(缺省)的属性 方法
不同包的普通类(非子类) 要使用其他包声明的类,不可以调用声明为private 缺省 protected 

#### super关键字 (通常省略)

理解为：父类的。。。 super可以用来调用：属性 方法 构造器

1.可以在子类的方法或构造器中,通过使用"super.属性"或"super.方法"的方式,显式的调用父类中声明的属性或方法

2.当子类和父类中定义了同名的属性时,我们要想在子类中调用父类中声明的属性,则必须显式的使用"super.属性"的方式

3.当子类重写了父类中的方法以后,我们要想在子类中调用父类中声明的方法,则必须显式的使用"super.方法"的方式

4.可以在子类的构造器中显式的使用"super(形参列表)"的方式,调用父类中声明的指定的构造器

5.在类的构造器中,针对"this(形参列表)"或super(形参列表)只能二选一 当二者都没写时 默认调用super

#### 多态性

一个事物的多种形态

父类的引用指向子类的对象 父类 = new 子类 (编译看左边 运行看右边)

当调用子父类同名同参数的方法时,实际执行的是子类重写父类的方法

那么如何调用子类持有的属性和方法？
向下转型:使用强制转换符
a instanceof A 判断对象a是否是类A的实例

#### 包装类与基本数据类型

##### 基本数据类型-->包装类

调用包装类的构造器

```java
int num = 10;
Integer in = new Integer(num);
System.out.println(in.toString());
```

##### 包装类-->基本数据类型

调用包装类的xxxValue()

```java
Integer in1 = new Integer(12);
int i1= in1.intValue();
```



##### 自动装箱与自动拆箱 

直接转化,省略以上步骤

##### 基本数据类型,包装类-->String类型

调用String重载的valueOf(Xxx xxx)

```java
方法一:
int num = 10;
String str1 = num + "";
```

```java
方法二:
float f = 12.3f;
String str2 = String.valueOf(f1);
```

##### String类型-->基本数据类型,包装类

调用包装类的parseXxx()

```java
String str1 = "123";
int num2 = Integer.parseInt(str1);
```

#### abstract

abstract修饰类:抽象类

​	>此类不能实例化

​	>抽象类中一定有构造器,便于子类实例化时调用(涉及,子类对象实例化的全过程)

​	>开发中,都会提供抽象类的子类,让子类对象实例化,完成相关操作

abstract修饰方法:抽象方法
	>抽象方法只有方法的声明,没有方法体

​	>包含抽象方法的类,一定是一个抽象类,反之抽象类中可以没有抽象方法

​	>若子类重写了父类中的所有抽象方法,此子类方可实例化

​	 若子类没有重写父类中的所有的抽象方法,则此子类也是一个抽象类,需要使用abstract来修饰

#### 接口的使用

1.接口用interface来定义

2.java中 接口和类是并列的两个结构

3.如何定义接口:定义接口中的成员

4.接口中不能定义构造器！意味着接口不可以实例化

5.接口通过让类去实现(implements)的方法来使用

  如果实现类覆盖了接口中的所有抽象方法,则此实现类就可以实例化
  如果实现类没有覆盖接口中的所有抽象方法,则此实现类仍为抽象类
6.java类可以实现多个接口-->弥补了java单继承的局限性
	格式:class AA extends BB implements CC,DD,EE

7.接口与接口之间可以继承,而且可以多继承

#### 内部类

1.java中允许将一个类A声明在另一个类B中,则类A就是内部类,类B称为外部类

2.内部类的分类:成员内部类(静态,非静态) vs 局部内部类(方法内,代码块内,构造器内)

3.成员内部类：
	一方面,作为外部类的成员；
		>调用外部类的结构
		>可以被static修饰
		>可以被4种不同的权限修饰

   另一方面,作为一个类：
	   >类内可以定义属性,方法,构造器等
	   >可以被final修饰,表示此类不能被继承。言外之意,不使用final,   就可以被继承
	   >可以被abstract修饰

#### 异常处理

异常处理:抓抛模型

过程一:"抛" 程序正常执行的过程中,一旦出现异常,就会在异常代码处生成一个对应异常的对象,并将此对象抛出

​	一旦抛出对象以后,其后的代码不再执行
​	可以手动生成一个异常对象,并抛出

##### 如何自定义异常类？

1.继承现有的异常结构,RuntimeException,Exception
2.提供全局变量:serialVersionUID
3.提供重载的构造器

过程二:"抓" 可以理解为异常的处理方式: 1.try-catch-finally 2.throws

##### try-catch-finally

```
try{
	//可能出现异常的代码
}catch(异常类型1 变量名1){
	//处理异常的方式1
}catch(异常类型2 变量名2){
	//异常处理的方式
}finally{
	//一定会执行的代码
}
```

##### try使用说明

1.使用try将可能出现异常代码包装起来,在执行过程中,一旦出现异常,就会生成一个对应异常类的对象,根据此对象的类型,
  去catch中进行匹配

2.一旦try中的异常对象匹配到一个catch时,就会进入catch中进行异常的处理,一旦处理完成,就跳出当前的try-catch结构
(在没有写finally的情况下),继续执行其后的代码

3.catch中的异常类型如果没有子父类关系,则谁声明在上,谁声明在下无所谓

 catch中的异常类型如果满足子父类关系,则要求子类一定声明在父类的上面。否则,报错

4.常用的异常对象处理的方式：

1.String getMessage()   

2.printStackTrace()

5.在try结构中声明的变量,在出了try结构后,就不能再被调用(所以应在try外边进行定义且赋初值 在try中调用)

6.try-catch-finally结构可以嵌套

##### finally使用说明

1.finally是可选的

2.finally中声明的是一定会被执行的代码。即使catch中又出现异常了,try中有return语句,catch中有return语句等情况

3.像数据库连接,输入输出流,网络编程Socket等资源,JVM是不能自动的回收的,我们需要自己手动的进行资源的释放,此时的
资源释放,就需要声明在finally中

##### throws使用说明

throws + 异常类型
1."throws + 异常类型"写在方法的声明处。指明此方法执行时,可能会抛出的异常类型

   一旦当方法体执行时,出现异常,仍会在异常代码处生成一个异常类的对象。此对象满足throwsff的异常类型时,就会被抛出。

 异常代码后续的代码,就不再执行

##### 总结

​	 1.try-catch-finally是真正的将异常给处理掉了

​     2.throws的方式只是将异常抛给了方法的调用者,并没有真正将异常处理掉

     3.     子类重写的方法抛出的异常类型不大于父类被重写的方法抛出的异常类型

开发中如何选择使用try-catch-finally还是throws:

1.如果父类中被重写的方法没有throws方式处理异常,则子类重写的方法也不能使用throws,意味着如果子类重写的方法中有异常,必须使用try-catch-finally方式处理

  2.执行的方法中,先后又调用了另外的几个方法,这几个方法是递进关系执行的,我们建议这几个方法使用throws的方法进行处理
    执行的方法A可以考虑使用try-catch-finally的方法进行处理

  

#### java的常用类

##### String:字符串类

​	Stirng是最特殊的类,String类的值都是常量,值一旦创建不能更改
​	boolean equalsIgnoreCase(String str) 不区分大小写是否相等
​	boolean contains(String str) 是否完全包含
​	boolean startsWith(String str) 是否是以某个字符串开头的
​	boolean endsWith(String str) 是否是以某个字符结尾的
​	int indexOf(String str) 得到某个字符串所在值
​	String substring(int start) 截取
​	String substring(int start , int end) 截取 包前不包后
​	String replace(String old , String new) 替换
​	

	StringBuffer:字符串缓冲区  可以看成是String的工具类
	append(String) 往字符串中添加字符串
	delete(int start , int end) 删除字符串 包前不包后
	replace(String,String) 替代

##### 时间类

​	SimpleDateFormat的使用:SimpleDateFormat对日期Date类的格式化和解析
​	SimpleDateFormat sdf = new SimpleDateFormat(pattern:"yyyy-MM-dd hh:mm:ss");
​	String asd  = sdf.format(date);

	获取时间类LocalDate,LocalTime,LocalDateTime
#### java的比较器

一. 说明：java中的对象,正常情况下,只能进行比较：== 或 != ,不能使用 > 或 < ,但是在开发场景中,我们需要对多个对象进行
	排序,言外之意,就需要比较对象的大小,如何实现？使用两个接口中的任何一个：Comparable 或 Comparator

##### Comparable接口的使用：自然排序

​	使用举例:
​	1.像String,包装类等实现了Comparable接口,重写了compareTo()方法,给出了比较两个对象大小的方式

​	2.像String,包装类重写comparaTo()方法以后,进行了从小到大的排序

​	3.重写comparaTo(obj)的规则：
​	    如果当前对象this大于形参对象obj,则返回正整数
​	    如果当前对象this小于形参对象obj,则返回负整数
​	    如果当前对象this等于形参对象obj,则返回零

​	4.对于自定义类来说,如果需要排序,我们可以让自定义类实现Comparable接口,重写comparaTo()方法

​	  在comparaTo(obj)方法中指明如何排序

##### Comparator接口的使用：定制排序

	1. 重写compara(Object o1,Object o2)方法,比较o1,o2的大小
	   如果方法返回正整数,则表示o1大于o2
	   如果返回零,表示相等
	   返回负数,表示o1小于o2

java的集合:是java的一个数据容器  只能存放引用类型
	数组只能是固定长度   数据类型是固定的
	集合可以存放不定长度  数据类型不一定
	改:集合可以存放不定长度 ,固定数据类型
	jdk1.6：提出了泛型的概念 
	Collection<泛型> 单列集合
	向Collection接口的实现类的对象中添加数据obj时,要求obj所在类要重写equals()
		有序:存入顺序和取出顺序一致

##### 		子接口

###### list:有序可重复集合 

###### 			list实现类

​			ArrayList(主要实现类) 线程不安全,效率高;  底层使用						Object[]存储

​			LinkedList 对于频繁的插入,删除操作,使用此类效率比				ArrayList高;  底层使用双向链表存储

​			Vector(最早) 线程安全,效率低;  底层使用Object[]存储

###### 面试题

Arraylist LinkedList Vector三者的异同？
		同：三个类都是实现了List接口,存储数据的特点相同：存储有序的,可重复的数据
		不同：见上
	

###### List接口中的常用操作

​	void add(int index, Object ele)：在index位置插入ele元素

​	addAll(int index,Collection eles)：从index位置开始将eles中所有元素添加进来

​	Object get(int index)：获取指定index位置的元素

​	int indexOf(Object obj)：返回obj在集合中首次出现的位置 如果不存在,返回-1

​	int LastIndexOf(Object obj)：返回obj在集合中末次出现的位置 如果不存在,返回-1

​	Object remove(int index)：移除指定index位置的元素,并返回此元素

​	Object set(int index,Object ele)：设置指定index位置的元素为ele

​	List subLIst(int fromIndex,int toIndex)：返回从fromIndex到toIndex位置的子集合

​	List sublist = list.subLixt(2,4);
​	

##### set:无序不可重复集合

###### 	set实现类

​		HashSet：作为set接口的主要实现类：线程不安全的,可以存储null值
​		linkedHashSet：作为HashSet的子类：遍历其内部数据时,可以按照添加时的哈希值遍历

​		TreeSet：可以按照添加对象的指定属性,进行排序

两种排序方式：自然排序(实现Comparable接口) 和 定制排序(Comparator)

自然排序中,比较两个对象是否相同的标准为：comparaTo()返回0,不再是equals()

定制排序中,比较两个对象是否相同的标准为：compara()返回0,不再是equals()

1.set接口中没有额外定义新的方法,使用的都是Collection中声明过的方法

2.要求：像Set中添加的数据,其所在的类一定要重写hashCode()和equals()

  要求：重写的hashCode()和equals()尽可能保持一致性：相等的对象必须具有相等的散列码

以HashSet为例说明：
1.无序性：不等于随机性,存储的数据在底层数组中并非按照数组索引的顺序添加,而是按照数据的哈希值

2.不可重复性：保证添加的元素按照equals()判断时,不能返回true,即：相同的元素只能添加一个基本操作：

##### 集合基本操作

增：add(Object obj)

删：remove(Object obj) 

removeAll(Collection coll1)：从当前集合中移除coll1中的所有元素

改：set(int index,Object ele)

查：get(int index)

插：add(int index,Object ele)

长度：size()

遍历：1.Iterator迭代器方式  2.增强for循环 3.普通的循环 

是否包含：contains(Object obj)

containsAll(Collection coll1)：判断coll1中的元素是否都存在于当前集合中

retainAll(Collection coll1)：获取当前集合和coll1集合的交集,并返回给当前集合

hashCode()：返回当前对象的哈希值

```
集合-->数组：toArray();
	Object[] arr = coll.toArray();
	for(int i = 0;i < arr.length;i++)
	{System.out.println(arr[i])}
```

##### 使用迭代器Iterator接口

   集合元素的遍历操作,使用迭代器Iterator接口  (主要遍历Collection) 

   1. 内部方法 hasNext() next() 
   2. 集合对象每次调用iterator()方法都得到一个全新的迭代器对象,默认游标都在集合的第一个元素之前

   	Iterator iterator = coll.iterator();
   	while(iterator.hasNext()){System.out.println(iterator.next())}
   测试Iterator中的remove()
   如果还未调用next()或在上一次调用next方法之后已经调用了remove方法,再调用remove都会报IllegalStateException

```
foreach(增强for循环) 用于遍历集合,数组
   for(集合元素的类型 局部变量 ：集合对象) 内部仍然调用了迭代器
   for(Object obj：coll){System.out.println(obj)}
   for(数组元素的类型 局部变量 ：数组对象)
   for(int i : arr){System.out.println(i)}
```

##### Map 双列集合 

用来储存一对(key - value)一对的数据

###### Map实现类

​		HashMap：作为Map的主要实现类：线程不安全,效率高 可以存储null的key和value

​		LinkedHashMap：保证在遍历map元素时,可以按照添加的顺序实现遍历

​			原因:在原有的HasMap底层结构基础上,添加了一对指针,指向前一个和后一个元素

​	TreeMap：保证按照添加的key-value对进行排序,实现排序遍历。此时考虑自然或定制排序  底层使用红黑树

​		向TreeMap中添加key-value,要求key必须是由同一个类创建的对象

​	Hashtable：作为古老的实现类：线程安全,效率低 不能存储null的key和value

​		Properties：常用来处理配置文件 key和value都是String类型

###### Map结构的理解

​	Map中的key：无序的,不可重复的,使用Set存储所有的key -->key所在的类(Hashmap)要重写equals和hashCode
​	Map中的value：无序的,可重复的,使用Collection存储所有的value -->value所在的类要重写equals
​	一个键值对：key-value构成了一个Entry对象
​	Map中的entry：无序的,不可重复的,使用Set存储所有的entry

###### Map中的常用方法

Object put(Object key,Object value)：将指定的key-value添加到(或修改)当前map对象中

void putAll(Map m)：将m中所有key-value对存放到当前map中

Object remove(Object key)：移除指定key的key-value对,并返回value

void clear()：清空当前map中的所有数据

Object get(Object key)：获取指定key对应的value

boolean containsKey(Object value)：是否包含指定的value

int size()：返回map中key-value对的个数

boolean isEmpty()：判断当前map是否为空

boolean equals(Object obj)：判断当前map和参数对象obj是否相等

###### 元视图操作的方法

Set keySet()：返回所有key构成的Set集合

Collection values()：返回所有value构成的Collection集合

Set entrySet()：返回所有key-value对构成的Set集合

Set<Map.Entry<Integer,String>> s =m.entrySet();

Collections：操作Collection,Map的工具类

reverse(List)：反转List中元素的排序

sort(List)：根据元素的自然顺序对指定的List集合元素进行升序排序

swap(List,int,int)：将指定的List集合中的i处元素和j处元素进行交换

##### 泛型：标签

泛型的使用：
在集合中使用泛型：
1.在实例化集合类时,可以指明具体的泛型类型

2.指明完以后,在集合类或接口中凡是定义类或接口时,内部结构(方法,构造器,属性等)都指定为实例化的泛型类型

​	add(E e)-->实例化后：add(Integer e)

3.泛型的类型必须是类,不能是基本数据类型

4.如果实例化,没有指明泛型的类型。则默认类型为java.long.Object

如何自定义泛型结构：泛型类,泛型接口;泛型方法

##### 关于自定义泛型类,泛型接口

```
public class Order<T>{
	String orderName;
	int orderId;
	T orderT;
  public Order(String ordername,int orderId,T orderT){
	this.ordername = orderName;
	this.orderId = orderId;
	this.orderT = orderT;
  }
  public T getOrderT(){
	return orderT;
  }
  public void setOrderT(T orderT){
	this.orderT = orderT;
  }
}
```

##### 文件

创建：
     creatNewFile() 创建文件
     mkdir() 创建单层文件夹
     mkdirs() 创建多层文件夹
判断：
     isDirectroy() 判断是否为文件夹
     isFile() 判断是否为文件
删除：
     delete() 删除指定File 不可逆操作
     	      文件夹只能删除空文件夹
查看文件夹：
	listFiles() 返回一个抽象路径名数组

##### IO流原理及流的分类

Java程序中,对于数据的输入/输出操作以"流(stream)"的方式进行

按操作数据单位不同分为：字节流(8 bit), 字符流(16 bit)

按数据流的流向不同分为：输入流, 输出流

按流的角色不同分为：节点流,处理流

以InputStream结尾为字节输入流

以OutputStream结尾为字节输出流

以Reader结尾为字符输入流

以Writer结尾为字符输出流

###### 流的体系结构

| 抽象基类     | 节点流(或文件流) | 缓冲流(处理流的一种) |
| ------------ | ---------------- | -------------------- |
| InputStream  | FileInputStream  | BufferedInputStream  |
| OutputStream | FileOutputStream | BufferedOutputStream |
| Reader       | FileReader       | BufferedReaderStream |
| Writer       | FileWriter       | BufferedWriter       |

为了保证流资源一定可以执行关闭操作,需要使用try-catch-finally处理

输入操作：read()：返回读入的一个字符。如果达到文件末尾,返回 -1

read(char [] cbuf)：返回每次读入cbuf数组中的字符的个数 如果达到文件末尾,返回-1

输出操作,对应的File可以不存在。

​	文件如果不存在,在输出的过程中,会自动创建此文件

​	如果存在：

​		如果流使用的构造器是：FileWriter(file,true)对原有文件进行追加
​		如果使用的是：FileWriter(file,false) / FileWriter(file)对原有文件进行覆盖

使用字节流FileInputStream处理文本文件,可能出现乱码

###### 结论

1.对于文本文件(.txt,.java,.c,.cpp),使用字符流处理

2.对于非文本文件(.jpg,.mp3.mp4,.doc,.ppt,.avi,...),使用字节流处理
先关闭外层的流,再关闭内层的流

(外层流关闭的同时,内层流也会自动的进行关闭。关于内层流的关闭,可以省略)

缓冲流的作用：提高读写速度(内部提供了一个缓冲区)
处理流,就是"套接"在已有流的基础上

##### 多线程的创建

###### 	方式一：继承于Thread类

1.创建一个继承于Thread类的子类

2.重写Thread类的run() --> 将此线程执行的操作声明在run()中

3.创建Thread类的子类的对象

4.通过此对象调用start()

问题一：我们不能直接调用run()的方法启动线程
问题二：再启动一个线程,不可以让已经start()的线程去执行 会报IllegalThread

###### 方式二：实现Runnable接口

​	1.创建一个实现了Runnnable接口的类

​	2.实现类去实现Runnable中的抽象方法：run()

​	3.创建实现类的对象

​	4.将此对象作为参数传递到Thread类的构造器中,创建Thread类的对象

​	5.通过Thread类的对象调用start()

###### 方式三：实现Callable接口

​	1.创建一个实现Callable的实现类
​		class NumThrea implements Callable{}

​	2.实现call()方法,将此线程需要执行的操作声明在call()中

​	3.创建Callable接口实现类的对象
​		NumThread numThread = newNumThread();

​	4.将此Callable接口实现类的对象传递到FutureTask构造器中,创		建FutureTask的对象
​		FutureTask futureTask = new FutureTask(numThread)

​	5.将FutureTask的对象作为参数传递到Thread类的构造器中,创建		Thread对象,并调用start()
​		new Thread(futureTask).start()

```java

import java.util.concurrent.Callable;

public class Threadthird implements Callable{

	@Override
	public Object call() throws Exception {
		int sum = 0;
		for (int i = 0; i <= 100; i++) {
			if(i%2==0) {
				System.out.println(i);
				sum+=i;
		}
		}
		return sum;
	}	
}

```

```java
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThirdThread{
	public static void main(String[] args) {
		Threadthird threadthird = new Threadthird();
		
		FutureTask futuretask= new FutureTask(threadthird);
		
		new Thread(futuretask).start();
		try {
			//get()返回值即为FutureTask构造器参数Callable实现类重写的call()的返回值 如果不需要返回值则不需要调用get()方法
			Object sum = futuretask.get();
			System.out.println(sum);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
```



###### Runnable与Callable对比

	与使用Runnable相比,Callable功能更强大些
	(1)相比run()方法,call()方法可以有返回值 
	(2)call()方法可以抛出异常
	(3)支持泛型的返回值
	(4)需要借助FutureTask类,比如获取返回结果

###### 方式四：使用线程池

思路：提前创建好多个线程,放入线程池中,使用时直接获取 使用完放回池中,可以避免频繁创建销毁、实现重复利用

###### 饿汉式与懒汉式

1.饿汉式在类创建的同时就实例化一个静态对象出来，不管之后会不会使用这个单例，都会占据一定的内存，但是相应的，
在第一次调用时速度也会更快，因为其资源已经初始化完成。

2.懒汉式顾名思义，会延迟加载，在第一次使用该单例的时候才会实例化对象出来，第一次调用时要做初始化，
如果要做的工作比较多，性能上会有些延迟，之后就和饿汉式一样了。

###### 测试Thread中的常用方法

1. start()：启动当前线程,调用当前的run()
2. run()：通常需要重写Thread类中的此方法,将创建的线程需要执行的操作声明在此方法中
3. currentThread()：静态方法,返回执行当前代码的线程
4. getName()：获取当前线程的名字
5. setName()：设置当前线程的名字
6. yield()：释放当前cpu的执行权
7. join()：在线程a中调用线程b的join(),此时线程a就进入阻塞状态,直到线程b完全执行完以后,线程a才结束阻塞状态
8. stop()：已过时。当执行此方法时,强制结束当前进程
9. sleep(long milltime)：让当前线程"睡眠"指定的milltime毫秒,在指定的milltime毫秒时间内,当前的线程是阻塞状态
10. isAlive()：判断当前线程是否存活

###### 线程的优先级

1.
MAX_PRIORITY：10
MIN_PRIORITY：1
NORM_PRIORITY：5
2.如何获取和设置当前线程的优先级：
	getPriority()：获取线程的优先级
	setPriority(int p)：设置当前线程的优先级

说明：高优先级的线程要抢占低优先级线程cpu的执行权,但是只是从概率上讲,高优先级的线程高概率的情况下被执行。
并不意味着只有当高优先级线程执行完以后,低优先级的线程才执行。

出现的问题：当某个线程在尚未完成操作过程时,其他线程参与进来,就会出现线程安全问题
解决：当一个线程a在操作的时候,其他线程不能参与进来,直到线程a操作完成时,其他线程才可以进行操作
	即使线程a出现阻塞,其他线程也不能进行操作

##### 在java中,通过同步机制,来解决线程安全问题

###### 	方式一：同步代码块

​		synchronized(同步监视器){需要被同步的代码}

​		说明 1.操作共享数据的代码,即为需要被同步的代码

​		     	2.同步监视器,俗称：锁  任何一个类的对象,都可以充当锁
​	     			要求：多个线程必须要共用同一把锁

同步代码块处理继承Thread类的线程安全问题：
	创建一个obj类 将其定义为static
	慎用this充当同步监视器,考虑使用当前类充当同步监视器 (类名.class) 保证锁唯一

###### 方式二：同步方法

​	如果操作共享数据的代码完整的声明在一个方法中,我们不妨将此方法声明为同步的

同步方法处理继承Thread类的线程安全问题：
由于同步方法中的同步监视器为this 所以应将方法定义为static 此时同步监视器为当前的类 

###### 方式三：Lock锁

​	1.实例化ReentrantLock                        
​	2.调用lock()
​	3.调用unlock()

###### 面试题：synchronized 与 lock 的异同？  

​	相同：二者都可以解决线程安全问题
​	不同：synchronized机制在执行完相应的同步代码后,自动的释放同步监视器
​	      lock需要手动的去启动同步(lock()),手动结束同步(unlock())

###### 线程的死锁问题

​	出现死锁后,不会出现异常,不会出现提示,只是所有的线程都处于阻塞状态 无法继续

线程通讯的例子：使用两个线程打印1-100 交替打印
	涉及到的三个方法：
	wait()：一旦执行此方法,当前线程就进入阻塞状态,并释放同步监视器
	notify()：一旦执行此方法,就会唤醒被wait的线程 如果有多个线程被wait,就唤醒优先级高的
	notifyAll()：一旦执行此方法,就会唤醒所有被wait的线程

注意：这三个方法必须使用在同步代码块或同步方法中

###### 面试题：sleep() 和 wait() 的异同？

1.相同：一旦执行此方法,都会使得当前的线程进入阻塞状态

2.不同： (1) 两个方法声明的位置不同：Thread类中声明sleep() Object类中声明wait()

​	 (2) 调用的要求不同：sleep()可以在任何需要的场景下使用 wait()必须在同步代码块或方法中

​	 (3) 如果两个方法都使用在同步代码或方法中,sleep()不会释放锁 wait()会释放锁

##### 网络编程

网络通信的三要素

​		Ip：终端在互联网上的唯一标识

​			internet版本协议4
​				以4段，0-255之间的数字来标识
​				192.168.0.128

​			internet版本协议6
​				用16进制的数字 0-f

​		端口号：每一个进程需要端口号

###### 获取本机的ip地址和用户名

```java
import java.net.InetAddress;

public class Iptest {
	public static void main(String[] args) throws Exception {
		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress();
		String name = addr.getHostName();
		System.out.println(ip);
		System.out.println(name);
	}
}

```

​		传输协议

###### 		UDP 

​		不安全的传输协议，不需要建立连接，数据不能超过64kb
​		把数据放在数据包中，需要IP和端口号  接收不到就丢包

​		UDP的传输：
​			1，创建udp的Socket-DatagramSocket
​			2，需要知道ip和端口号
​			3，Datagrampacket这是UDP传输时的数据报包，包上需要					IP和port
​			4，发送

```java
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

//客户端  只管发消息
public class UDPClient {
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket();
		Scanner sc = new Scanner(System.in);
		//发送消息
		while(true){
			System.out.println("请输入要发送的消息：");
			String str = sc.next();
			byte[] b = str.getBytes();
			//把数据放放到数据报包上
			DatagramPacket dp = new DatagramPacket(b, 0, b.length, InetAddress.getByName("127.0.0.1"), 8888);
			//发送
			ds.send(dp);
			if(str.equals("886")){
				break;
			}
		}
		
		ds.close();
	}
}

```

```java
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

//接收UDP
public class UDPServer {
	public static void main(String[] args) throws Exception {
		//创建一个端口号为8888 的udp端点   接收消息
		DatagramSocket ds = new DatagramSocket(8888);   
		byte[] b = new byte[1024];
		DatagramPacket dp = new DatagramPacket(b, 0, b.length);
		while(true){
		ds.receive(dp);
		String s = new String(dp.getData(),0,dp.getLength());
		//我们可以从接收的包上得到是谁，通过那个端口给我们发送的消息
		String ip = dp.getAddress().getHostAddress();
		int port = dp.getPort();
		System.out.println(ip+":"+port+"发送了消息："+s);
			if(s.equals("886")){
				break;
			}
		}
		ds.close();
	}
}

```



###### 		TCP

​		需要建立连接，经历3次握手，传输稳定，数据量大

​		TCP需要建立稳定的连接：用IO流来传输数据
​			Scoket:发送数据的
​			ServerSocket：接收数据的

```java
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
	public static void main(String[] args) throws Exception {
		//要往这个ip的这个端口发送消息
		Socket s = new Socket("127.0.0.1",10989);
		OutputStream out = s.getOutputStream();
		ServerSocket ss = new ServerSocket(64248);
		Socket s1 = ss.accept();
		InputStream in = s1.getInputStream();
		byte[] b = new byte[1024];
		int len =0;
		Scanner sc = new Scanner(System.in);
		//建立输出流通道，才能发消息
		while(true){
			System.out.println("给127.0.0.1的"+10989+"端口号发送消息：");
			String str = sc.next();
			out.write(str.getBytes());
			if(str.equals("886")){
				break;
			}
			//写一个接收端
			len = in.read(b);
			String str1 = new String(b,0,len);
			String ip = s1.getInetAddress().getHostAddress();
			int port = s1.getPort();
			System.out.println("接收到"+ip+"从"+port+"端口发来的数据："+str1);
			
		}
		s.close();
		ss.close();
		s1.close();
		
	}
}
```

​                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            

```java
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPServer {
	public static void main(String[] args) throws Exception {
		//使用10989端口的应用
		ServerSocket ss = new ServerSocket(10989);
		Socket  s = ss.accept();
		//读取端点中的内容  通过输入流
		InputStream in = s.getInputStream();
		Scanner sc = new Scanner(System.in);
		//这个端口得接收数据 有ServerSocket创建属于该服务端口的Socket
		Socket s1   = new Socket("127.0.0.1",64248);
		OutputStream out = s1.getOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		String str1 ="";
		while(true){
			//接收消息
			len = in.read(b);
			String str = new String(b,0,len);
			String ip = s.getInetAddress().getHostAddress();
			int port  = s.getPort();
			System.out.println(ip+"通过"+port+"端口发送了信息："+str);
			if(str1.equals("886")){
				break;
			}
			//接收以后回复
			System.out.println("给127.0.0.1的"+64248+"端口号恢复消息：");
			String str2 = sc.next();
			out.write(str2.getBytes());
			
			
		}
		s1.close();
		s.close();
		ss.close();
		
	}
}
```

##### JDBC

​		java databases connection  java数据库链接技术
​		提供统一的标准，可以链接任意数据库
​		java提供接口
​		数据库厂商提供实现类
​		mysql的jar包：第三方提供的 封装了可供使用的类
​		mysql5.7
​		mysql8.0
​		jar包的版本：
​			mysql-connector-java-5.1-bin.jar
​				Driver：com.mysql.jdbc.Driver 
​				url:jdbc:mysql://127.0.0.1:3306/nylg002?useUnicode=true&characterEncoding=utf8

​			mysql-connector-java-8.0.15.jar
​			Driver:com.mysql.cj.jdbc.Driver 
​			url = "jdbc:mysql://127.0.0.1:3306/nylg002?useSSL=false&characterEncoding=utf8";

​	java使用JDBC链接mysql的过程
​		1，导入jar包
​		2，读取驱动类
​		3， 由驱动管理器创建Connection
​		4，有连接创建statement执行，执行sql语句
​		5，PreparedStatement：预编译对象  sql语句需要交给他，他先读取sql
​			所有的值，不是直接拼接给sql的，而是把值交给预编译对象，
​			由预编译对象完成sql的拼装
​			预编译对象再创建时就需要sql，但这个sql仅仅是个空架子
​			预编译对象中，sql语句的变量用？占位符先占位
​			由预编译对象拼装，把变量交个预编译对象，按照占位符的顺序



```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Demo1 {
	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String url ="jdbc:mysql://localhost:3306/nylg002?useUnicode=true&characterEncoding=utf8";
		Connection conn = DriverManager.getConnection(url, "root", "root");
		//创建执行对象  用于传递sql
		Statement sta = conn.createStatement();
		//java 不编译和解析sql  直接以字符串的形式传递
		String name ="王婆";
		String sex="女";
		String card="88889898";
		String pwd="333";
		int age = 60;
		//String sql="insert into userinfo(uname,usex,ucard,upwd,uage) values('"+name+"','"+sex+"','"+card+"','"+pwd+"',"+age+")";
		String sql ="update userinfo set upwd ='"+pwd+"'";
		int num = sta.executeUpdate(sql);//执行更新语句
		if(num>0){
			System.out.println("操作成功");
		}
		sta.close();
		conn.close();
	}
}

```

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.nylg.utils.DButil;

public class Demo3 {
	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		try {
			conn = DButil.getConn();
			String sql = "select *  from userinfo where ucard= ? and upwd= ?";
			psta = conn.prepareStatement(sql);
			Scanner sc = new Scanner(System.in);
			System.out.println("请输入账号：");
			String card = sc.next();
			System.out.println("请输入密码：");
			String pwd = sc.next();
			psta.setString(1, card);
			psta.setString(2, pwd);
			System.out.println(psta);
			rs = psta.executeQuery();
			while(rs.next()){
				int uid = rs.getInt("uid");
				String uname = rs.getString("uname");
				String usex = rs.getString("usex");
				int uage =  rs.getInt("uage");
				System.out.println(uid+"--"+uname+"--"+usex+"--"+uage);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DButil.closeAll(rs, psta, conn);
		}
	
	}
}


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DButil {
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Connection  getConn(){
		try {
			
			String url="jdbc:mysql:///nylg002?useUnicode=true&characterEncoding=utf8";
			Connection conn = DriverManager.getConnection(url, "root", "root");
			return conn;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	public static void closeAll(ResultSet rs,PreparedStatement psta,Connection conn){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(psta!=null){
			try {
				psta.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
	}
}

```



