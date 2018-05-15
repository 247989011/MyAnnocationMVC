# 作业三：将Mvc框架实现annocation注解方式 

手写一个mini版SpringMVC

## SpringMVC原理


## org.eclipse.jetty 插件

[jetty-maven-plugin 配置详情](https://www.eclipse.org/jetty/documentation/current/jetty-maven-plugin.html#configuring-your-webapp "")


## properties文件读取方法

```java
public class LoadProperties{
    //1、基于ClassLoder读取配置文件;该方式只能读取类路径下的配置文件，有局限但是如果配置文件在类路径下比较方便。
    void method1(){
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        //this是不能在static（静态）方法或者static块中使用的,如果方法是静态的用LoadProperties类名.class
        InputStream in = LoadProperties.class.getClassLoader().getResourceAsStream("config/config.properties");
        // 使用properties对象加载输入流
        properties.load(in);
        //获取key对应的value值
        properties.getProperty("key");
    }
    //2、基于 InputStream 读取配置文件;该方式的优点在于可以读取任意路径下的配置文件
    void method2(){
        Properties properties = new Properties();
        // 使用InPutStream流读取properties文件
        BufferedReader bufferedReader = new BufferedReader(new FileReader("E:/config.properties"));
        properties.load(bufferedReader);
        // 获取key对应的value值
        properties.getProperty("key");
    }
    //3、通过 java.util.ResourceBundle 类来读取，这种方式比使用 Properties 要方便一些
    void method3(){
        //1>通过 ResourceBundle.getBundle() 静态方法来获取（ResourceBundle是一个抽象类），这种方式来获取properties属性文件不需要加.properties后缀名，只需要文件名即可
        properties.getProperty("key");
        //config为属性文件名，放在包com.test.config下，如果是放在src下，直接用config即可  
        ResourceBundle resource = ResourceBundle.getBundle("com/test/config/config");
        String key = resource.getString("keyWord"); 
        //　2>从 InputStream 中读取，获取 InputStream 的方法和上面一样，不再赘述
        ResourceBundle resource = new PropertyResourceBundle(inStream);
    }
}


```
## Class.getResources()和classLoader.getResources()区别

### Class.getResources()
    Class.getResources(String path) path如果是以 / 开头，就从classpath中去找（classpath可以认为是eclipse的bin目录或者是target的classes目录），如果不以/开头，就以当前类
    的位置开始找，假如实在上一级目录，那么就是 ../application/xml. 获取application.xml文件的方式有
```java
<!--从当前类的位置去查找 -->
System.out.println(IndexService.class.getResource("application.xml"));

<!--从classpath下查找该文件 -->
System.out.println(IndexService.class.getResource("/main/test/com/jt3/web/application.xml"));

```
### classLoader.getResources()
获取当前类的加载器，从加载器的环境下进行加载，就是classpath目录下.

## javassist
Javassist是一个开源的分析、编辑和创建Java字节码的类库

Javassist是一个动态类库，可以用来检查、”动态”修改以及创建 Java类。其功能与jdk自带的反射功能类似，但比反射功能更强大。

### 重要的类
    ClassPool：javassist的类池，使用ClassPool 类可以跟踪和控制所操作的类,它的工作方式与 JVM 类装载器非常相似， 
    CtClass： CtClass提供了检查类数据（如字段和方法）以及在类中添加新字段、方法和构造函数、以及改变类、父类和接口的方法。不过，Javassist 并未提供删除类中字段、方法或者构造函数的任何方法。 
    CtField：用来访问域 
    CtMethod ：用来访问方法 
    CtConstructor：用来访问构造器
    
### 限制与局限性
    需要注意的是，在调用ctClass.toClass()时，会加载此类，如果此类在之前已经被加载过，则会报一个duplicate load的错误，表示不能重复加载一个类。所以，修改方法的实现必须在修改的类加载之前进行。
    不能访问块之外的局部变量。如果在一个方法的开始和结尾都增加了代码段，那么在方法的结尾块中无法访问方法开始中的代码段中的变量（不太完美的解决方法是将原方法改名，然后再增加与原方法同名的方法）。    