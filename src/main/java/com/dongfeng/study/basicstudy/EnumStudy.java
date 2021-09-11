package com.dongfeng.study.basicstudy;

import java.util.Arrays;

/**
 * <b> Java枚举学习 </b>
 * @author eastFeng
 * @date 2021-04-23 0:12
 */
public class EnumStudy {
    public static void main(String[] args) {
        // 枚举类型学习
        enumTest();
    }

    public static void enumTest(){
        /*
         * 枚举类型
         * 有时候，变量的取值只在一个有限的集合内。例如：销售的奶茶只有小、中、大和超大这四种尺寸。
         * 当然，可以将这些尺寸分别编码为1、2、3、4或S、M、L、X。但这样存在着一定的隐患。在变量中很可能保存的是一个错误的值（如0或m）。
         * 针对这种情况，可以自定义枚举类型。枚举类型包括有限个命名的值。
         * 【枚举是一种特殊的数据，它的取值是有限的，是可以枚举出来的】，比如一年有四季、一周有七天。
         * 虽然使用类也可以处理这种数据，但枚举类型更为简洁、安全和方便。
         *
         * 枚举使用enum这个关键字来定义。枚举TeaSize不能被继承。
         * 所有的枚举类型都是Enum类的子类。它们继承了这个类的许多方法。 其中最有用的一个是toString()，这个方法能够返回枚举常量名。
         *
         * Java是从Java 5才开始支持枚举的，在此之前，一般是在类中定义静态整型变量来实现类似功能。
         * 枚举的好处体现在以下几方面：
         * 1. 定义枚举的语法更为简洁。
         * 2. 枚举更为安全。一个枚举类型的变量，它的值要么为null，要么为枚举值之一，不可能为其他值，
         *    但使用变量，它的值就没有办法强制，值可能就是无效的。
         * 3. 枚举类型自带很多便利方法（如values、valueOf、toString等），易于使用。
         *
         * 枚举是怎么实现的呢？
         * 1. 枚举类型实际上会被Java编译器转换为一个对应的类，这个类继承了Enum类。
         *    Enum类有name和ordinal两个实例变量，在构造方法中需要传递，
         *    name()、toString()、ordinal()、compareTo()、equals()方法都是由Enum类根据其实例变量name和ordinal实现的。
         *    values和valueOf方法是编译器给每个枚举类型自动添加的。
         * 2. 枚举类型本质上也是类，但由于编译器自动做了很多事情，因此它的使用更为简洁、安全和方便。
         */

        // 获取该枚举名称
        String largeName = TeaSize.LARGE.name();
        String mediumName = TeaSize.MEDIUM.toString();

        // 获取该枚举下标
        // 枚举类型都有一个ordinal方法，返回声明中枚举常量的位置，位置从0开始计数。
        int ordinal = TeaSize.LARGE.ordinal();

        // 每个枚举类型都有一个静态的values方法，它将返回一个包含全部枚举值的数组，顺序与声明时的顺序一致。
        TeaSize[] teaSizes = TeaSize.values();
        Arrays.stream(teaSizes).forEach(t -> System.out.println(t.name() + " index:"+t.ordinal()));

        // 枚举类型都实现了Comparable接口，都可以通过方法compareTo与其他枚举值进行比较。比较其实就是比较ordinal的大小。
        // 如果枚举常量出现在other之前，则返回一个负值；如果this==other，则返回0；否则，返回正值。
        int cmp = TeaSize.LARGE.compareTo(TeaSize.EXTRA_LARGE);
    }

    /**
     * 枚举类型
     * <p> 有时候，变量的取值只在一个有限的集合内。例如：销售的奶茶只有小、中、大和超大这四种尺寸。
     * 当然，可以将这些尺寸分别编码为1、2、3、4或S、M、L、X。但这样存在着一定的隐患。在变量中很可能保存的是一个错误的值（如0或m）。
     * 针对这种情况，可以自定义枚举类型。枚举类型包括有限个命名的值。
     *
     * <p> 枚举是一种特殊的数据，它的取值是有限的，是可以枚举出来的，比如一年有四季、一周有七天。虽然使用类也可以处理这种数据，
     * 但枚举类型更为简洁、安全和方便。
     *
     * <p> 枚举使用enum这个关键字来定义。枚举TeaSize不能被继承。
     *
     * <p> 所有的枚举类型都是{@link Enum}类的子类。它们继承了这个类的许多方法。
     * 其中最有用的一个是{@link #toString()}，这个方法能够返回枚举常量名。
     *
     * <p> Java是从Java 5才开始支持枚举的，在此之前，一般是在类中定义静态整型变量来实现类似功能。
     *
     * <p> 枚举的好处体现在以下几方面：
     * <lo>
     *     <li> 定义枚举的语法更为简洁。
     *     <li> 枚举更为安全。一个枚举类型的变量，它的值要么为null，要么为枚举值之一，不可能为其他值，
     *          但使用整型变量，它的值就没有办法强制，值可能就是无效的。
     *     <li> 枚举类型自带很多便利方法（如values、valueOf、toString等），易于使用。
     * </lo>
     *
     * <p> 枚举是怎么实现的呢？
     * <p> 枚举类型实际上会被Java编译器转换为一个对应的类，这个类继承了{@link Enum}类。{@link Enum}类有name和ordinal两个实例变量，
     * 在构造方法中需要传递，name()、toString()、ordinal()、compareTo()、equals()方法都是由Enum类根据其实例变量name和ordinal实现的。
     * values和valueOf方法是编译器给每个枚举类型自动添加的。
     *
     * <p> <b>枚举类型本质上也是类，但由于编译器自动做了很多事情，因此它的使用更为简洁、安全和方便。</b>
     *
     */
    public static enum TeaSize{
        SMALL,
        MEDIUM,
        LARGE,
        EXTRA_LARGE;
        /*
         * 实际上，这个声明定义的类型是一个类，它刚好有4个实例，在此尽量不要构造新对象。
         * 因此，在比较两个枚举类型的值时，永远不需要调用equals，而直接使用“==”就可以了。
         */

        public static void onChosen(TeaSize teaSize){
            /*
             * 枚举还可以用于switch语句。
             * 在switch语句内部，枚举值不能带枚举类型前缀，例如，直接使用SMALL，不能使用TeaSize.SMALL。
             */
            switch (teaSize){
                case SMALL:
                    System.out.println("chosen SMALL");
                    break;
                case MEDIUM:
                    System.out.println("chosen medium");
                    break;
                case LARGE:
                    System.out.println("chosen LARGE");
                    break;
                case EXTRA_LARGE:
                    System.out.println("chosen EXTRA_LARGE");
                    break;
                default:
                    System.out.println("null");
            }
        }
    }
}
