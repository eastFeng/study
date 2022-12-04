package com.dongfeng.study.basicstudy.spring.ioc;

import org.springframework.beans.factory.FactoryBean;

/**
 * {@link FactoryBean}接口：这个接口我们日常开发中使用的不多，更多的是第三方框架接入Spring的时候会使用。
 * <p> 不过由于这个接口跟Spring IoC容器中承载主要逻辑的BeanFactory（容器接口之一）长的比较像，
 * 所以面试的时候面试官偶尔也会问问这两种有什么区别。这两者区别可大了去了，因为他们基本没啥关联，完全是两个东西。
 *
 * <p> 当我们向Spring注册一个{@link FactoryBean}接口实现类的对象时，
 * 通过beanName获取到的将是{@link FactoryBean#getObject()}方法返回的类型的实例对象。
 *
 *
 * @author eastFeng
 * @date 2022-12-04 14:46
 */
public class BookFactoryBean implements FactoryBean<Book> {

    @Override
    public Book getObject() throws Exception {
        Book book = new Book();
        book.setName("毛泽东选集 第一卷");
        book.setAuthor("毛泽东");
        book.setBriefIntroduction("毛主席万岁");
        return book;
    }


    @Override
    public Class<?> getObjectType() {
        return Book.class;
    }

    /**
     * {@link BookFactoryBean#getObject()}方法返回的bean实例是否是单例的，
     * 如果是单例，那么{@link BookFactoryBean#getObject()}方法将只会被调用一次。
     * <p> 默认单例模式
     */
    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
