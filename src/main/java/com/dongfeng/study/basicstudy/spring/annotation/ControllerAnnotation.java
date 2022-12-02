package com.dongfeng.study.basicstudy.spring.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * {@link Controller}注解学习
 *
 * <p> {@link Controller}注解是{@link Component}注解的派生注解。
 * 请先学习{@link Component}注解，在{@link ComponentStudy}中。
 *
 * @author eastFeng
 * @date 2022-11-22 21:42
 */
public class ControllerAnnotation {

    /**
     * 1. {@link Controller}注解继承了Spring的{@link Component}注解，
     * 会把对应的类声明为Spring对应的Bean，并且可以被Web组件管理。
     *
     * <p>2. {@link Controller}注解标注的类，该类代表控制器类（控制层/表现层）。
     * 这里控制层里面的每个方法，都可以去调用{@link Service}注解标识的类（业务逻辑层），
     * {@link Service}注解标注的类中的方法可以继续调用{@link Repository}注解标注的接口实现类（Dao层/持久层）。
     *
     * <p>3. MVC设计模式：M(model)指模型，V(view)指视图层，C(controller)指控制层。
     * <ul>
     *     <li>M代表模型，一般指service和DAO；</li>
     *     <li>view代表视图，一般指页面eg：jsp，html ftl等；</li>
     *     <li>C代表控制器，比如SpringMVC中的Controller或Struts2中的Action。</li>
     * </ul>
     * Controller是Spring中的一个特殊组件，这个组件会被Spring识别为可以接受并处理网页请求的组件。
     * Spring中提供了基于注解的Controller定义方式：{@link Controller}和{@link RestController}注解。
     *
     * <p>4. {@link Controller}注解用于标记在一个类上，
     * 使用它标记的类就是一个SpringMVC的Controller类，分发处理器会扫描使用该注解的类的方法，
     * 并检测方法是否使用了{@link RequestMapping}注解。
     * {@link Controller}注解只是定义了一个控制器类，
     * 而使用{@link RequestMapping}注解的方法才是处理请求的处理器。
     *
     * <p>
     * 注意：
     * <ul>
     *     <li>用{@link Controller}定义一个控制器类。</li>
     *     <li>用{@link RequestMapping}给出外界访问方法的路径，或者说触发路径 ，触发条件。</li>
     *     <li>用{@link ResponseBody}标记Controller类中的方法。把方法return的结果变成JSON对象返回。
     *     （如果没有这个注解，这个方法只能返回要跳转的路径即跳转的html/JSP页面。有这个注解，可以不跳转页面，只返回JSON数据）</li>
     * </ul>
     * </p>
     *
     */
    public static void main(String[] args) {
    }
}
