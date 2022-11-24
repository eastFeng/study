package com.dongfeng.study.basicstudy.spring;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * {@link org.springframework.web.bind.annotation.RequestMapping}注解学习
 *
 * @author eastFeng
 * @date 2022-11-23 0:41
 */
public class RequestMappingAnnotation {

    /*
     * 我们在实际的开发当中，一个控制器中不一定只有一个方法，而这些方法都是用来处理请求的，那么怎样才能将请求与处理方法一一对应呢，
     * 当然是通过RequestMapping注解来处理这些映射请求，也就是通过它来指定控制器可以处理哪些URL请求。
     *
     * @RequestMapping注解是一个用来处理请求地址映射的注解，可用于映射一个请求或一个方法，可以用在类或方法上。
     *
     * 标注在方法上 ：表示在类的父路径下追加方法上注解中的地址将会访问到该方法。
     * 标注在类上 ：表示类中的所有响应请求的方法都是以该地址作为父路径。
     */

    /**
     * <p>{@link org.springframework.web.bind.annotation.RequestMapping}是SpringWeb应用程序中最常被用到的注解之一。
     * 这个注解会将HTTP请求映射到MVC和REST控制器的处理方法上。</p>
     *
     * <p> @RequestMapping注解可以用在类定义处和方法定义处，就是来映射服务器访问的路径。
     * <li>标注在类上：规定初步的请求映射，相对于web应用的根目录；</li>
     * <li>标注在方法上：进一步细分请求映射，相对于类定义处的URL。
     * 如果类定义处没有使用该注解，则方法标记的URL相对于根目录而言；</li>
     * </p>
     *
     * <p>
     *  RequestMapping注解有六个属性，分成三类进行说明：
     *  <ol>
     *      <li>
     *      {@link RequestMapping#value()}：指定请求的实际地址，指定的地址可以是URI Template模式，该属性必须设值。
     *      {@link RequestMapping#method()}：指定HTTP请求的method类型，GET、POST、PUT、DELETE等。
     *      </li>
     *      <li>
     *      {@link RequestMapping#consumes()}：指定处理请求的提交内容类型（Content-Type），例如ext/plain,application/json,text/html等。
     *      {@link RequestMapping#produces()}：指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回。
     *      </li>
     *      <li>
     *      {@link RequestMapping#params()}：指定request中必须包含某些参数值时，才让该方法处理。
     *      {@link RequestMapping#headers()}：指定request中必须包含某些指定的header值，才能让该方法处理请求
     *      </li>
     *  </ol>
     * </p>
     *
     */
    public static void main(String[] args) {
    }

    // RequestMapping注解中的属性用法举例

    /**
     * {@link RequestMapping#value()}和{@link RequestMapping#path()}：指定请求的实际地址，指定的地址可以是URI Template模式。
     * <p>指定请求的url地址。value属性和path属性的作用相同，可以互相替换。
     * value是默认属性，所以@RequestMapping(value=“/example”)和@RequestMapping(“/example”)是等价的。</p>
     *
     * <p>该属性必须设值。</p>
     * <p> value属性是通过当前请求的请求地址来匹配请求。 </p>
     * <p>从源码中可以看到value属性是一个字符串类型的数组，因此说明可以将多个请求映射到一个方法上，
     * 只需要给value来指定一个包含多个路径的数组。
     * 这时的请求映射所映射的请求的请求路径为选择value数组中的任意一个都可以。</p>
     */
    @RequestMapping("/test1")
    public void test1(){}
    @RequestMapping(value = {"/aaa","/bbb","/ccc"})
    public void test11(){}

    /**
     * {@link RequestMapping#method()}：指定HTTP请求的method类型，GET、POST、PUT、DELETE等。
     * <p>method属性是通过当前请求的请求方式来匹配请求。</p>
     * <p>浏览器向服务器发送请求，请求方式有很多GET、HEAD、POST、PUT、PATCH、DELETE、OPTIONS、TRACE。
     * 可以使用 method 属性来约束请求方式。</p>
     *
     * <p>例如，该映射方法中明确要求请求方式为get，所以post方式不被允许</p>
     */
    @RequestMapping(value = "/test2"
            ,method = RequestMethod.GET) // 只支持GET请求
    public void test2(){}
    @RequestMapping(value = "/test21"
            ,method = {RequestMethod.GET,RequestMethod.POST}) // 支持GET和POST请求
    public void test21(){}


    /**
     * {@link RequestMapping#params()}属性是通过当前请求的请求参数来匹配请求。
     *
     * <p>
     * params属性是一个字符串类型的数组，可以通过下面四种表达是来设置匹配关系:
     * <li>“paramA”：要求请求映射的请求必须为包含paramA的请求参数。</li>
     * <li>“!paramB”：要求请求映射的请求是不能包含paramB的请求参数。</li>
     * <li>“paramC=value”：要求请求映射的请求必须包含paramC的请求参数，且paramC参数的值必须为value。</li>
     * <li>“paramD!=value”： 要求请求映射的请求是必须包含paramD的请求参数，其值不能为value。</li>
     * </p>
     */
    @RequestMapping(value = "/test3",params = {"name=A","age=6"})
    public void test3(){}

    /**
     * {@link RequestMapping#headers()}属性是通过当前请求的请求头信息来匹配请求；
     *
     * <p>
     * headers属性是一个字符串类型的数组，可以通过下面四种表达是来设置匹配关系
     * <li>“headerA”：要求请求映射的请求必须为包含headerA的请求头信息。</li>
     * <li>“!headerB”：要求请求映射的请求必须为不包含headerB的请求头信息</li>
     * <li>“headerC=value”：要求请求映射的请求必须为包含headerC的请求头信息，并且headerC的值必须为value</li>
     * <li>“headerD!=value”：要求请求映射的请求必须为包含headerD的请求头信息，并且headerD的值必须不是value</li>
     * </p>
     *
     */
    @RequestMapping(value = "/test4",
            method = RequestMethod.HEAD,
            headers = {"headerA=AAAAAAAA"})
    public void test4(){}

    /**
     * {@link RequestMapping#consumes()}属性 ：
     * 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html。
     */
    @RequestMapping(value = "/test5",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE) // 只接收json类型
    public void test5(){}

    /**
     * {@link RequestMapping#produces()}属性:
     * 指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回。
     */
    @RequestMapping(value = "/test6",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void test6(){}


    // RequestMapping注解配合其他注解
    /*
     * 教程：
     * 1. https://blog.csdn.net/qq15035899256/article/details/126041210?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-126041210-blog-125427414.pc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-126041210-blog-125427414.pc_relevant_multi_platform_whitelistv3&utm_relevant_index=2
     * 2. https://blog.csdn.net/qq_40749830/article/details/115701610
     *
     *
     */
}
