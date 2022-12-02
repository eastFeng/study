package com.dongfeng.study.basicstudy.spring.annotation;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * {@link ResponseBody}注解的学习
 *
 * @author eastFeng
 * @date 2022-11-23 0:01
 */
public class ResponseBodyStudy {

    /**
     * <p> {@link ResponseBody}注解的作用是
     * 将Controller的方法返回的对象，通过适当的转换器转换为指定的格式之后，写入到response对象的body区，
     * 通常用来返回JSON数据或者是XML数据，然后将数据返回客户端。
     * 需要注意的呢，在使用此注解之后不会再走视图处理器，而是直接将数据写入到输入流中，
     * 他的效果等同于通过response对象输出指定格式的数据。</p>
     *
     * <p>
     *     <li>如果一个方法上没有加入{@link ResponseBody}注解，则Spring会将方法的返回值封装为一个ModelAndView对象返回。</li>
     *     <li>如果一个方法上加入了{@link ResponseBody}注解时，不再做试图解析。
     *       当该方法的返回值是字符串时，则返回字符串至客户端；
     *       当该方法的返回值是一个对象时，则将对象转换为json串，返回到客户端。
     *       （对于自定义的Java类转换为JSON格式的数据，该类要是可序列化的）</li>
     *     <li>
     *     ResponseBody返回值的编码需注意，我们可以手动修改编码格式，如:
     * 	   RequestMapping(value="/getUsers",produces="text/html;charset=utf-8")
     *     </li>
     * </p>
     *
     * <p>
     *  作用范围：
     *  <ol>
     *      <li>标记在方法上。</li>
     *      <li>标记在类上，通过{@link RestController}注解实现，
     *      此时该类所有的方法都将会被添加{@link ResponseBody}注解</li>
     *  </ol>
     * </p>
     *
     *
     */
    public static void main(String[] args) {
    }
}
