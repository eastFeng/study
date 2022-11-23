package com.dongfeng.study.config.configuration;

import com.dongfeng.study.bean.base.Constants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> {@link Configuration}: 指示一个类声明 一个或者多个{@link Bean}方法。
 * <p> {@link Configuration}用于定义配置类，定义的配置类可以替换xml文件，一般和{@link Bean}注解联合使用。
 *
 * @author eastFeng
 * @date 2021-09-21 23:12
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 声明Topic类型的Exchange(交换器), 比较通用，其他模式都可以实现
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(Constants.RABBIT_TOPIC_EXCHANGE_NAME);
    }

    /**
     * 声明消息队列
     * @return Queue
     */
    @Bean
    public Queue queue(){
        return new Queue(Constants.RABBIT_QUEUE_NAME);
    }

    // 声明消息队列2
//    @Bean
//    public Queue queue2(){
//        return new Queue(Constants.RABBIT_QUEUE2_NAME);
//    }

    /**
     * Binding（绑定）：通过路由键将消息队列（Queue）和交换机（Exchange）绑定起来
     * @return Binding
     */
    @Bean
    public Binding binding(){
        return BindingBuilder.
                bind(queue())
                .to(topicExchange())
                .with(Constants.RABBIT_ROUTING_KEY);
    }
}
