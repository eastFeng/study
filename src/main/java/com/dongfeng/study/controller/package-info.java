/**
 * @author eastFeng
 * @date 2021-04-22 17:49
 */
package com.dongfeng.study.controller;

/*
 *
 * @ResponseBody的作用其实是将java对象转为json格式的数据。
 * @responseBody注解的作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区，
 * 通常用来返回JSON数据或者是XML数据。
 * 注意：在使用此注解之后不会再走视图处理器，而是直接将数据写入到输入流中，他的效果等同于通过response对象输出指定格式的数据。
 *
 * @ResponseBody是作用在方法上的，@ResponseBody 表示该方法的返回结果直接写入 HTTP response body 中，一般在异步获取数据时使用【也就是AJAX】。
 *
 * 注意：在使用 @RequestMapping后，返回值通常解析为跳转路径，但是加上 @ResponseBody 后返回结果不会被解析为跳转路径，
 * 而是直接写入 HTTP response body 中。
 * 比如异步获取 json 数据，加上 @ResponseBody 后，会直接返回 json 数据。
 * @RequestBody 将 HTTP 请求正文插入方法中，使用适合的 HttpMessageConverter 将请求体写入某个对象。
 *
 * @RequestBody 注解则是将 HTTP 请求正文插入方法中，使用适合的 HttpMessageConverter 将请求体写入某个对象。
 * @RequestBody 作用：
 * 1) 该注解用于读取Request请求的body部分数据，使用系统默认配置的HttpMessageConverter进行解析，然后把相应的数据绑定到要返回的对象上；
 * 2) 再把HttpMessageConverter返回的对象数据绑定到 controller中方法的参数上。
 *
 * 简而言之，@RequestBody是作用在形参列表上，用于将前台发送过来固定格式的数据【xml格式 或者 json等】封装为对应的 JavaBean 对象，
 * 封装时使用到的一个对象是系统默认配置的 HttpMessageConverter进行解析，然后封装到形参上。
 *
 * 在Spring MVC 中使用 @RequestMapping 来映射请求，也就是通过它来指定控制器可以处理哪些URL请求。
 * 通过查看@RequestMapping注解源码：
 * 1. @Target({ElementType.TYPE, ElementType.METHOD})  // @RequestMapping 可以在方法和类上声明使用
 * 2. 可以看到注解中的属性除了 name() 返回的字符串，其它的方法均返回数组，也就是可以定义多个属性值，
 *    例如 value() 和 path() 都可以同时定义多个字符串值来接收多个URL请求。
 *
 *
 *
 */
