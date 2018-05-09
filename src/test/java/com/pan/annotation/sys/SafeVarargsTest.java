package com.pan.annotation.sys;

import java.util.*;

/**
 * 大部分参数化类型，例如ArrayList<Number> 和 List<String>，都属于非具体化类型(non-reifiable types)。
 * 非具体化类型是指在运行时(runtime)并不完整的类型。在编译时，非具体化类型经过了一个名为「类型擦除」的过程，
 * 编译器删除了与类型参数相关的信息。这将保证Java运行库与那些诞生在Java泛型之前的应用程序之间的二进制兼容性。
 * 由于在编译时，类型擦除操作删除了来自参数化类型的相关信息，因此它们是不完整的。
 *
 * 一个参数化类型的变量引用一个非参数化类型的对象，将会导致堆污染。这种情况只会在程序执行了一些在编译时会
 * 出现未经检查的警告(unchecked warning)的操作时发生。不管是在编译时(符合编译时类型检查规则的约束)，还是在
 * 运行时，如果无法验证一个表示参数化类型的操作(例如：类型强转、方法调用)是否正确，就会产生一个未经检查的警告。
 */
public class SafeVarargsTest {

    /**
     * Add to list 2.
     *
     * @param <T>      the type parameter
     * @param listArg  the list arg
     * @param elements the elements
     */
    @SuppressWarnings({"unchecked", "varargs"})
    public static <T> void addToList2 (List<T> listArg, T... elements) {
        for (T x : elements) {
            listArg.add(x);
        }
    }

    /**
     * Add to list 3.
     *
     * @param <T>      the type parameter
     * @param listArg  the list arg
     * @param elements the elements
     */
    @SafeVarargs
    public static <T> void addToList3 (List<T> listArg, T... elements) {
        for (T x : elements) {
            listArg.add(x);
        }
    }
    public static void main(String[] args){
        List list = new ArrayList<String>();
        addToList2(list,"abc");
        addToList2(list,123);
        addToList3(list,"abc");
        addToList3(list,123);
        System.out.println(list);
    }
}

class SafeVarargsTest2{
    //此方法接受的是一个泛型为Integer类型的Set集合，假如我们将一个没有泛型的Set对象传给此方法时，则有可能造成堆污染
    public static void method(Set<Integer> obj){

    }

    public static void main(String[] args){
        Set set = new TreeSet();
        set.add("abc");
        /**
         * 没有注解@SaveVarargs 时提示：
         * Unchecked assignment: 'java.util.Set' to 'java.util.Set<java.lang.Integer>' less... (Ctrl+F1)
         * Signals places where an unchecked warning is issued by the compiler, for example:
         *
         *   void f(HashMap map) {
         *     map.put("key", "value");
         *   }
         *
         * Hint: Pass -Xlint:unchecked to javac to get more details.
         */
        method(set);
    }

}