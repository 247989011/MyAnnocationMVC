package com.pan.mvcframework.servlet;

import com.pan.mvcframework.annotation.*;
import com.pan.mvcframework.util.LocalVariableTableParameterNameDiscoverer;
import com.pan.mvcframework.util.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyDisptcherServlet extends HttpServlet {

    private Properties p = new Properties();
    private List<String> classNames = new ArrayList<String>();

    private Map<String, Object> ioc = new HashMap<String, Object>();

    //    private Map<String, Method> handlerMapping = new HashMap<String, Method>();
    private List<Handler> handlerMapping = new ArrayList<Handler>();

    @Override

    public void init(ServletConfig config) throws ServletException {
        System.out.println("初始化配置文件");
        //1.加载配置文件application.properties 代替xml
        String path = config.getInitParameter("contextConfigLocation");
        doLoadConfig(path);
        //2.扫描所有相关的类,拿到基础包路径，递归扫描
        doScanner(p.getProperty("scanPackage"));

        //3.把扫描到的所有的类实例化，放到IOC容器之中(自己实现一个容器，说白了就是一个Map)
        doInstance();

        //4.检查注入，只要加了@PanAutowired注解的字段，不管它是私有的还是公有的，还是受保护的都要给它强制赋值。
        doAutoWired();

        //5.获取用户的请求，根据所请求的URL去找到其对应的Method，通过反射机制去调用
        //HandlerMapping 把这样一个关系放到HandlerMapping中区，说白了就是一个Map
        iniHandlerMapping();

        //6.等待请求，把反射调用的结果通过response写出到浏览器之中


        System.out.println(path);
        System.out.println("初始化配置文件完成!!!!");
    }

    private void doLoadConfig(String path) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
        try {
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void doScanner(String scanPackage) {
        //java.lang.ClassLoader.getResource() 方法找到具有给定名称的资源。资源是一些数据(图像，音频，文本等)，可以通过类代码中的方式，是独立的代码的位置的访问。资源的名称是“/” - 标识资源分离路径名。
        //1.获取指定扫描包路径
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());//递归扫描
            } else {
                classNames.add(scanPackage + "." + file.getName().replace(".class", ""));
            }
        }
    }

    private void doInstance() {

        if (classNames.isEmpty()) {
            return;
        }
        try {
            for (String className : classNames) {

                Class<?> clazz = Class.forName(className);

                //不是所有的类都要初始化的，
                if (clazz.isAnnotationPresent(PanController.class)) {
                    //<bean id="" name="" class="" >3
                    String beanName = StringUtil.lowerFirstLetter(clazz.getSimpleName());
                    ioc.put(beanName, clazz.newInstance());

                } else if (clazz.isAnnotationPresent(PanService.class)) {
                    //1.如果依赖注入带有参数，则用参数名匹配注入，
                    //2.默认首字母小写(发生在不是接口的情况)
                    //3.如果注入的类型是接口，我要自动找到其实现类的实例并注入
                    PanService service = clazz.getAnnotation(PanService.class);
                    String beanName = service.value();
                    if ("".equals(beanName.trim())) {
                        beanName = StringUtil.lowerFirstLetter(clazz.getSimpleName());
                        ioc.put(beanName, clazz.newInstance());
                        Class<?>[] interfaces = clazz.getInterfaces();
                        for (Class<?> i : interfaces) {
                            ioc.put(StringUtil.lowerFirstLetter(i.getSimpleName()), clazz.newInstance());
                        }
                    } else {
                        ioc.put(beanName, clazz.newInstance());
                    }

                } else {
                    continue;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void doAutoWired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {

            Field[] fields = entry.getValue().getClass().getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(PanAutowired.class)) {
                    PanAutowired autowired = field.getAnnotation(PanAutowired.class);
                    String beanName = autowired.value().trim();
                    //通过接口注入
                    if ("".equals(beanName)) {
                        beanName = StringUtil.lowerFirstLetter(field.getType().getSimpleName());
                    }

                    field.setAccessible(true);
                    try {
                        field.set(entry.getValue(), ioc.get(beanName));
                        field.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }


                }

            }


        }
    }

    private void iniHandlerMapping() {

        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //Spring非常具有技术含量的地方

            //把所有的@RequestMapping扫描出来，读取它的值，并且放入到handlerMapping之中去

            Class<?> clazz = entry.getValue().getClass();
            //只跟PanController有关
            if (!clazz.isAnnotationPresent(PanController.class)) {
                continue;
            }
            String baseUrl = "";//这个是controller上的@PanRequestMapping中的值
            if (clazz.isAnnotationPresent(PanRequestMapping.class)) {
                PanRequestMapping requestMapping = clazz.getAnnotation(PanRequestMapping.class);
                baseUrl  = requestMapping.value();
            }
            Method[] methods = clazz.getMethods();//这里的方法肯定是外部可以访问到的方法.
            for (Method method : methods) {
                if (!method.isAnnotationPresent(PanRequestMapping.class)) {
                    continue;
                }
                PanRequestMapping requestMapping = method.getAnnotation(PanRequestMapping.class);
                String mappingUrl = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+","/");
//                handlerMapping.put(mappingUrl,method);
                Pattern pattern = Pattern.compile(mappingUrl);
                handlerMapping.add(new Handler(pattern, entry.getValue(), method));
                System.out.println("Mapping:" + mappingUrl +",Method:" + method);
            }



        }

    }

    /**
     * Handler记录Controller中的RequestMapping和Method的对应关系
     */
    private class  Handler{
        protected Object controller; //保存方法对应的实例
        protected Method method;     //保存映射的方法
        protected Pattern pattern;      //记得Spring的url是支持正则的
        protected Map<String,Integer> paramIndexMapping;        //参数顺序

        protected Handler(Pattern pattern, Object controller, Method method) {
            this.controller =controller;
            this.method = method;
            this.pattern =pattern;

            paramIndexMapping = new HashMap<String, Integer>();
            putParamIndexMapping(controller.getClass(),method);
        }

        private void putParamIndexMapping(Class<?> clazz,Method method) {
            //提取方法中加了注解的参数
            /*
            在Java 8之前的版本，代码编译为class文件后，方法参数的类型是固定的，但参数名称却丢失了，这和动态语言严重依赖参数名称形成了鲜明对比。现在，Java 8开始在class文件中保留参数名，给反射带来了极大的便利。jdk8增加了类Parameter
            如果编译级别低于1.8，得到的参数名称是无意义的arg0、arg1……
            遗憾的是，保留参数名这一选项由编译开关javac -parameters打开，默认是关闭的。
            注意此功能必须把代码编译成1.8版本的class才行。
            idea设置保留参数名：

在 preferences-》Java Compiler->设置模块字节码版本1.8，Javac Options中的 Additional command line parameters: -parameters
             */
//            Parameter parameters[] = method.getParameters();
//            for (int i = 0; i < parameters.length; i++) {
//                Parameter parameter = parameters[i];
//                System.out.println(parameter);
//                if (parameter.isAnnotationPresent(PanRequestParam.class)) {
//                    PanRequestParam requestParam = parameter.getAnnotation(PanRequestParam.class);
//                    String paramName = requestParam.value();
//                    if (!"".equals(paramName.trim())) {
//                        paramIndexMapping.put(paramName, i);
//                    } else {
//                        paramIndexMapping.put(LocalVariableTableParameterNameDiscoverer.getParameterNames(clazz,method.getName())[i], i);
//                    }
//                }
//            }
            Annotation[][] pa = method.getParameterAnnotations();
            for (int i = 0; i < pa.length; i++) {
                for (Annotation a : pa[i]) {
                    if (a instanceof PanRequestParam) {
                        String paramName = ((PanRequestParam) a).value();
                        if (!"".equals(paramName.trim())) {
                            paramIndexMapping.put(paramName, i);
                        }else{
                            //暂时没有实现注解没有参数时，默认以参数名保存
                            //paramIndexMapping.put(method.getParameters()[i].getName(), i);
                        }
                    }
                }
            }
            //提取方法中的request和response参数
            Class<?> [] paramsTypes = method.getParameterTypes();
            for (int i = 0; i < paramsTypes.length; i++) {
                Class<?> type = paramsTypes[i];
                if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                    paramIndexMapping.put(type.getName(), i);
                }
            }

        }

    }

    private Handler getHandler(HttpServletRequest request) throws Exception {
        if (handlerMapping.isEmpty()) {
            return null;
        }
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (Handler handler : handlerMapping) {
            try {
                Matcher matcher = handler.pattern.matcher(url);
                if (!matcher.matches()) {
                    continue;
                }
                return handler;
            } catch (Exception e) {
                throw e;
            }
        }
        return  null;
    }
//==============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception,Detail:" + Arrays.toString(e.getStackTrace()));//抛出500错误异常
        }


    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        System.out.println(url);
//        if (! handlerMapping.containsKey(url)) {
//            resp.getWriter().write("404 Not Found");
//            return;
//        }
//        Method method = handlerMapping.get(url);

        Handler handler = getHandler(req);
        if (handler == null) {
            resp.getWriter().write("404 Not Found");
            return;
        }

        //获取方法的参数列表
        Class<?> [] paramTypes = handler.method.getParameterTypes();

        //保存所有需要自动赋值的参数值
        Object[] paramValues = new Object[paramTypes.length];
        //首先获得request的参数列表
        //获得自己定义的方法的参数列表
        Map<String,String[]> params = req.getParameterMap();
        for (Map.Entry<String, String[]> param : params.entrySet()) {
            System.out.println("paramkey:"+param.getKey()+",paramValue:"+param.getValue());
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]","").replaceAll(",\\s",",");

            //如果找到匹配的对象，则开始填充参数值
            if (!handler.paramIndexMapping.containsKey(param.getKey())) {
                continue;
            }
            int index = handler.paramIndexMapping.get(param.getKey());
            paramValues[index] = convert(paramTypes[index],value);

        }
        // 设置方法中的request 和 response 对象
        int reqIndex = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
        paramValues[reqIndex] = req ;
        int resIndex = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
        paramValues[resIndex] = resp ;
        System.out.println(paramValues);
        System.out.println(handler);
        //反射调用方法
        handler.method.invoke(handler.controller, paramValues);

        //第一个参数：表示方法所在的实例，
        //怎么办？
//        method.invoke();

//        System.out.println("成功获取到即将要调用的Method："+method);


    }


    private Object convert(Class<?> type, String value) {
        if (Integer.class == type) {
            return Integer.valueOf(value);
        }
        return  value;
    }
}
