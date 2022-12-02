package com.dongfeng.study.basicstudy.java.otherbasic;

import java.lang.annotation.*;









/**
 * <p> Java注解（Annotation）又称Java标注，是JDK5.0引入的一种注释机制。
 * Java 语言中的类、方法、变量、参数和包等都可以被标注。
 * Java注解可以通过反射获取标注内容。在编译器生成类文件时，注解可以被嵌入到字节码中。
 * Java虚拟机可以保留注解内容，在运行时可以获取到注解内容。当然它也支持自定义Java注解。
 *
 * <p> <b>1. 每一个注解（Annotation）都与一个RetentionPolicy关联，并且与1~n个ElementType关联 </b>
 * <ul>
 *     <li>{@link RetentionPolicy}: 保留政策（注解的作用域范围）。</li>
 *     <li> {@link ElementType}: 指定可以声明该注解的目标类型。</li>
 * </ul>
 * 可以理解为：每1个Annotation（注解），都会有唯一的RetentionPolicy属性，可以有若干个 ElementType 属性。
 *
 * <p>2. 使用{@link Documented}注解修饰该Annotation（注解），则表示该Annotation（注解）可以出现在javadoc中。
 *
 * <p>3. 使用{@link Target}指定可以声明该Annotation（注解）的目标类型：
 * 定义Annotation时，{@link Target}可有可无。
 * 若有{@link Target}，则该Annotation只能用于它所指定的地方；
 * 若没有{@link Target}，则该Annotation可以用于任何地方。
 *
 * <p>3. 使用{@link Retention}来指定Annotation的保留政策：
 * 定义Annotation时，@Retention可有可无。
 * 若没有@Retention，则默认是{@link RetentionPolicy#CLASS}。
 * {@link RetentionPolicy#RUNTIME}: 注解将由编译器记录在类文件中，并由VM在运行时保留，因此它们可能被反射地读取。
 *
 * <p>4. 使用@interface定义注解时，意味着它实现了java.lang.annotation.Annotation接口，即该注解就是一个Annotation。
 * <p>【注意】定义Annotation时，@interface是必须的。
 * <p>【注意】它和我们通常的implemented实现接口的方法不同。Annotation接口的实现细节都由编译器完成。
 * 通过@interface定义注解后，该注解不能继承其他的注解或接口。
 *
 * @author eastFeng
 * @date 2020/8/7 - 10:16
 */
@Documented
@Target({ElementType.METHOD}) // 该注解可以在方法上声明使用
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationStudy {

    // 定义一个String数组类型的属性value，默认值是unknown
    String[] value() default "unknown";
}
