package com.dongfeng.study;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;
import sun.dc.DuctusRenderingEngine;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eastFeng
 * @date 2020-12-17 21:45
 */
public class RabbitMqTest {
    public static void main(String[] args) {
        //RabbitMQ 是遵 AMQP 协议的， 换句话说 RabbitMQ 就是 AMQP协议的 Erlang 的实现

        //交换器类型：
        //1. fanout: 会把所有发送到该交换器的消息路由到所有与该交换器绑定的队列中。
        //2. direct: 会把消息路由到那些BindingKey 和 RoutingKey完全匹配的队列中。
        //3. topic: 是将消息路由到 BindingKey RoutingKey 相匹配的队列中。
        //4. headers: 不依赖于路由键的匹配规则来路由消息，而是根据发送的消息内容中的 headers 属性进行匹配，这种类型的交换器性能很差，也不实用，基本不用。
    }

    @Test
    public void publish(){
        ConnectionFactory factory = getFactory();
        try {
            //无论是生产者还是消费者，都需要和 RabbitMQ Broker 建立连接，这个连接就是一条TCP连接
            //创建连接
            Connection connection = factory.newConnection();

            //Channel是建立在Connection之上的虚拟连接，RabbitMQ 处理的每条 AMQP 指令都是通过信道完成的。
            //创建AMQP信道 (Channel) ,, Channel可以用来发送或者接受消息
            Channel channel = connection.createChannel();

            //Connection可以用来创建多个Channel实例，但是Channel实例不能在线程间共享，应用程序应该为每一个线程开辟一个Channel。
            //多线程间共享Channel实例是非线程安全的。

            //检测是否已处于开启状态, 不推荐在生产环境中使用isOpen方法
            boolean connectionIsOpen = connection.isOpen();
            boolean channelIsOpen = channel.isOpen();

            String message = "Hello World!";
            //将消息发送到Broker
            channel.basicPublish("EXCHANGE_NAME", "ROUTING_KEY", true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

            //声明交换器 : 创建一个持久化的、非自动删除的、绑定类型为direct的交换器
            channel.exchangeDeclare("exchangeName", BuiltinExchangeType.DIRECT, true);
            //声明队列 : 创建一个非持久化的、排他的、自动删除的队列（此队列的名称由RabbitMQ自动生成）
            //这个队列具备如下特性：只对当前应用中同一个Connection层面可用，同一个Connection的不同Channel可公用，并且也会在应用连接断开时自动删除。
            String queueName = channel.queueDeclare().getQueue();
            //用路由键（routingKey）将队列和交换器绑定起来
            channel.queueBind(queueName, "exchangeName", "routingKey");

            //创建一个应用中共享队列                 durable   exclusive  autoDelete  arguments
            channel.queueDeclare("queueName", true, false, false, null);  //持久化的，非排他的，非自动删除的

            //删除交换器
            channel.exchangeDelete("exchangeName");
            //删除队列
            channel.queueDelete("queueName");
            //清空队列中的内容
            channel.queuePurge("queueName");

            //将已经被绑定的队列和交换器进行解绑
            channel.queueUnbind("queueName", "exchangeName", "routingKey");


            channel.exchangeDeclare("source_exchange_name", BuiltinExchangeType.DIRECT, false, true, null);
            channel.exchangeDeclare("destination_exchange_name", BuiltinExchangeType.FANOUT, false, true, null);
            //将交换器与交换器进行绑定
            //绑定之后，消息从source交换器转发到destination交换器，某种程度上来说destination交换器可以看作一个队列。
            channel.exchangeBind("destination_exchange_name", "source_exchange_name", "exRoutingKey");
            channel.queueDeclare("destination_queue", false, false, true, null);
            //将destination_queue队列与destination_exchange_name交换器进行绑定
            channel.queueBind("destination_queue", "destination_exchange_name", "");
            //生产者发送消息值交换器source中，交换器source根据路由键找到匹配的另一个交换器destination，
            //并把消息转发到destination中，进而存储在destination绑定的队列queue中。
            channel.basicPublish("source_exchange_name", "exRoutingKey", null, "exToExDemo".getBytes());

            //关闭资源
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 推送消息
     */
    public void basicPush(Channel channel){
        try {
            //发送消息到Broker  :  basicPublish
            channel.basicPublish(
                    "exchangeName",
                    "rotingKey",
                    new AMQP.BasicProperties().builder()
                            .contentType("text/plain")  //content type
                            .deliveryMode(2)   //投递模式设为2，即消息会被持久化（即存入磁盘）在服务器中
                            .priority(1)  //优先级设为1
                            .userId("hidden")
                            .build(),
                    "hello".getBytes());

            //也可以发送一条带headers的消息
            Map<String, Object> headers = new HashMap<>();
            headers.put("location", "here");
            headers.put("time", "today");
            channel.basicPublish("exchangeName",
                    "rotingKey",
                    new AMQP.BasicProperties().builder()
                            .headers(headers)
                            .build(),
                    "hh".getBytes());

            //也可以发送一条带有过期时间(expiration)的消息
            channel.basicPublish("exchangeName",
                    "routingKey",
                    new AMQP.BasicProperties().builder()
                            .expiration("60000")
                            .build(),
                    "www".getBytes());


            // public void basicPublish(
            // String exchange,         交换器的名称
            // String routingKey,       路由键，交换器根据路由键将消息存储到相应的队列之中
            // boolean mandatory,       当mandatory参数设为true时，交换器无法根据自身的类型和路由键的类型找到一个符合条件的队列，那么RabbitMQ会调用Basic.Return
            //                          命令将消息返回给生产者。当mandatory参数设置为false时，出现上述情形，则消息直接被丢弃。
            // boolean immediate,       当immediate参数设为true时，如果交换器在将消息路由到队列时发现队列上并不存在任何消费者，那么这条消息将不会存入队列中。
            //                          当与路由键匹配的所有队列都没有消费者时，该消息会通过Basic.Return返回至生产者。RabbitMQ3.0版本开始去掉了去immediate参数的支持。
            // BasicProperties props,   消息的基本属性，其包含14个属性成员
            // byte[] body              消息体，真正需要发送的消息
            // ) throws IOException
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mandatory(Channel channel){
        try {

            //mandatory, 生产者如何获取到没有被正确路由到合适队列的消息呢？
            //生产者可以通过调用channel.addReturnListener来添加ReturnListener监听器实现
            channel.basicPublish("Exchange_Name",
                    "",
                    true,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    "mandatory test".getBytes());
            //添加ReturnListener监听器
            channel.addReturnListener(new ReturnListener() {
                @Override
                public void handleReturn(int replyCode,
                                         String replyText,
                                         String exchange,
                                         String routingKey,
                                         AMQP.BasicProperties properties,
                                         byte[] body) throws IOException {
                    String message = new String(body);
                    System.out.println("Basic.Return返回的结果是: "+message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 备份交换器
     */
    public void alternateExchange(Channel channel){
        try {
            //备份交换器: Alternate Exchange，简称AE。
            //声明了两个交换器 normalExchange 和 myAe，分别绑定了 normalQueue 和 unroutedQueue 这两个队列，同时将 myAe 设置为 normalExchange 的备份交换器。
            Map<String, Object> args = new HashMap<>();
            //myAe 设置为备份交换器
            args.put("alternate-exchange", "myAe");
            channel.exchangeDeclare("normalExchange", "direct", true, false, args);
            channel.exchangeDeclare("myAe", "fanout", true, false, null);
            channel.queueDeclare("normalQueue", true, false, false, null);
            channel.queueBind("normalQueue", "normalExchange", "normalKey");
            channel.queueDeclare("unroutedQueue", true, false, false, null);
            channel.queueBind("unroutedQueue", "myAe", "");

            //1. 如果设置的备份交换器不存在，客户端和RabbitMQ服务端都不会有异常，此时消息会丢失。
            //2. 如果备份交换器没有绑定任何队列，客户端和RabbitMQ服务器都不会有异常，此时消息会丢失。
            //3. 如果备份交换器没有任何匹配的队列，客户端和RabbitMQ服务器都不会有异常，此时消息会丢失。
            //4. 如果备份交换器和 mandatory 参数一起使用，那么 mandatory 参数无效。
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 过期时间
     * TTL，Time to Live 的简称，即过期时间。RabbitMQ可以对消息和队列设置TTL.
     */
    public void ttl(Channel channel){
        try {
            //1. 设置消息的 TTL。
            //方法一：通过队列属性设置，队列中所有消息都有相同的过期时间。
            //方法二：对消息本身进行单独设置，每条消息的TTL可以不同。
            //如果两种方法一起使用，则消息的 TTL 以两者之间较小的那个数值为准。
            //消息在队列中的生存时间一旦超过设置的 TTL 值时，就会变成 “死信”（Dead Message）。

            // 方法一： 通过设置队列属性设置消息 TTL
            Map<String, Object> args = new HashMap<>();
            // x-message-ttl 参数，单位是毫秒
            args.put("x-message-ttl", 6000);
            channel.queueDeclare("queueName", false, false, true, args);

            // 方法二： 推送时每条消息设置 TTL
            channel.basicPublish("exchangeName",
                    "routingKey",
                    new AMQP.BasicProperties.Builder()
                            .deliveryMode(2) //持久化消息
                            .expiration("6000") //设置 TTL = 6000ms
                            .build(),
                    "ttlTestMessage".getBytes());

            //对于第一种方法（设置队列TTL属性的方法），一旦消息过期，就会从队列中抹去，
            //而第二种方法（对消息本身进行单独设置），即使消息过期，也不会马上从队列中抹去，因为每条消息是否过期是在即将投递到消费者之前判定的。
            //为什么这种方法的处理方式不同呢？
            //因为第一种方法中，队列中已过期的消息肯定在队列头部，RabbitMQ只要定期从队列头部开始扫描是否有过期的消息即可。
            //而第二种方法中，每条消息的过期时间不同，如果要删除所有过期消息势必要扫描整个队列。所以不如等到此消息即将被消费时再判定是否过期，如果过期再进行删除即可。


            //2. 设置队列的 TTL
            // RabbitMQ会确保在过期时间到达后将队列删除，但是不保障删除的动作有多及时。在RabbitMQ重启后，持久化的队列的过期时间会被重新计算。
            Map<String, Object> queueArgs = new HashMap<>();
            //设置 TTL = 30分钟
            queueArgs.put("x-expires", 1800000);
            channel.queueDeclare("myQueue", false, false, false, queueArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 死信队列
     * <p> DLX，全称为 Dead-Letter-Exchange，可以称之为死信交换器，也有人称之为死信邮箱。
     * 当消息在一个队列中变成死信（dead message）之后，它能被重新发送到另一个交换器中，这个交换器就是 DLX，
     * 绑定 DLX 的队列就称为死信队列。
     *
     * <p> 消息变成死信一般有以下几种情况：
     * <ol>
     *     <li> 消息被拒绝（Basic.Reject/Basic.Nack），并且设置 require 参数为false；
     *     <li> 消息过期；
     *     <li> 队列达到最大长度；
     * </ol>
     */
    public void dlx(Channel channel){
        // DLX 也是一个正常的交换器，和一般的交换器没有区别，它能在任何的队列上被指定，实际上就是设置某个队列的属性。
        // 当这个队列中存在死信时，RabbitMQ 就会自动地把这个消息重新发布到设置的 DLX 上去，进而被路由到另一个队列，即死信队列。

        try {
            // 创建 DLX: dlx_exchange
            channel.exchangeDeclare("dlx_exchange", "direct", true);

            Map<String, Object> args = new HashMap<>();
            args.put("x-dead-letter-exchange", "dlx_exchange");
            //为队列 myQueue 添加 DLX
            channel.queueDeclare("myQueue", false, false, false, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 优先级队列 : 具有高优先级的队列具有高的优先权，优先级高的消息具备优先被消费的特权。
     */
    public void queuePriority(Channel channel){
        try {
            Map<String, Object> args = new HashMap<>();
            //配置队列的最大优先级为 10
            args.put("x-max-priority", 10);
            channel.queueDeclare("queue.priority", true, false, false, args);

            channel.basicPublish("exchange_priority",
                    "rk_priority",
                    new AMQP.BasicProperties().builder()
                            .priority(5)   //设置当前消息优先级为 5
                            .build(),
                    "message".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * RPC : Remote Procedure Call, 远程过程调用。
     * 它是一种通过网络从远程计算机上请求服务，而不需要了解底层网络的技术。
     * RPC的主要功能是让构建分布式计算更容易，在提供强大的远程调用能力时不损失本地调用的语义简洁性。
     */
    public void rpc(Channel channel){
        try {
            String callbackQueueName = channel.queueDeclare().getQueue();
            channel.basicPublish("",
                    "rpc_queue",
                    new AMQP.BasicProperties().builder()
                            .replyTo(callbackQueueName)  //设置一个回调队列
                            .correlationId("hh")
                            .build(),
                    "message".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 持久化
     */
    public void durable(){}


    /**
     * 声明交换器  exchangeDeclare 有多个重载方法
     */
    public void exchangeDeclare(){

        // 声明创建交换器方法
        // public DeclareOk exchangeDeclare(
        // String exchange,   交换器的名称
        // String type,       交换器的类型（fanout、direct、topic、headers）
        // boolean durable,   设置是否持久化（持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。）
        // boolean autoDelete,设置是否自动删除 （自动删除的前提是至少有一个队列或者交换器与这个交换器绑定，之后所有与这个交换器绑
        //                    定的队列或者交换器都与此解绑。注意不能错误地把这个参数理解为：当与此交换器连接的客户端都断开时，RabbitMQ会自动删除本交换器）
        // boolean internal,  设置是否是内置的。（内置的交换器，客户端程序无法直接发送消息到这个交换器中，只能通过交换器路由到交换器这种方式。）
        // Map<String, Object> arguments  其他一些结构化参数
        // ) throws IOException

        // 删除交换器方法
        // public AMQP.Exchange.DeleteOk exchangeDelete(String exchange  交换器的名称
        // ) throws IOException
    }

    /**
     * 声明队列  queueDeclare有多个重载方法
     *
     * 生产者和消费者都能够使用queueDeclare来声明一个队列，但是如果消费者在同一个信道（Channel）上订阅了另一个队列，就无法再声明队列了。
     * 必须先取消订阅，然后将信道置为 “传输” 模式，之后才能声明队列。
     */
    public void queueDeclare(){
        // 声明创建队列的方法
        // public com.rabbitmq.client.AMQP.Queue.DeclareOk queueDeclare(
        // String queue,                   队列的名称
        // boolean durable,                设置是否持久化（持久化的队列会存盘，在服务器重启的时候可以保证不丢失相关信息）
        // boolean exclusive,              设置是否排他（排他队列仅对首次声明它的连接可见，并在连接断开时自动删除。排他连接是基于连接(Connection)可见的，
        //                                 同一个连接的不同信道(Channel)是可以同时访问同一连接创建的排他队列；“首次”是指如果一个连接已经声明了一个排他队列，
        //                                 其他连接是不允许建立同名的排他队列的，这个与普通队列不同；即使该队列是持久化的，一旦连接关闭或者客户端退出，
        //                                 该排他队列都会被自动删除，这种队列适用于一个客户端同时发送和读取消息的应用场景。）
        // boolean autoDelete,             设置是否自动删除（自动删除的前提是：至少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开时，才会自动删除。
        //                                 不能把这个参数错误地理解为：”当连接到次队列的所有客户端都断开时，这个队列自动删除“，因为生产者客户端创建这个队列，
        //                                 或者没有消费者客户端与这个队列连接时，都不会自动删除这个队列。）
        // Map<String, Object> arguments   设置队列的其他一些参数，如x-message-ttl,x-expires,x-max-length,x-max-length-bytes等等。
        // ) throws IOException

        // 删除队列
        // public com.rabbitmq.client.AMQP.Queue.DeleteOk queueDelete(String queue  队列的名称
        // ) throws IOException

        // 清空队列中的内容
        // public PurgeOk queuePurge(String queue 队列的名称
        // ) throws IOException
    }

    /**
     * 队列和交换器绑定 queueBind
     */
    public void queueBind(){
        // 将队列和交换器绑定
        // public com.rabbitmq.client.AMQP.Queue.BindOk queueBind(
        // String queue,                  队列名称
        // String exchange,               交换器名称
        // String routingKey,             用来绑定队列和交换器的路由键
        // Map<String, Object> arguments  定义绑定的一些参数
        // ) throws IOException

        // 将已经被绑定的队列和交换器进行解绑
        // public com.rabbitmq.client.AMQP.Queue.UnbindOk queueUnbind(
        // String queue,                   队列名称
        // String exchange,                交换器名称
        // String routingKey,              路由键
        // Map<String, Object> arguments   一些参数
        // ) throws IOException
    }

    /**
     * 交换器和交换器绑定 exchangeBind
     */
    public void exchangeBind(){
        // 将交换器与交换器绑定
        // public BindOk exchangeBind(
        // String destination,            destination交换器名称
        // String source,                 source交换器名称
        // String routingKey,             路由键
        // Map<String, Object> arguments  绑定的一些参数
        // ) throws IOException
    }


    /**
     * RabbitMQ的消费模式分为两种：推(Push)模式和拉(Pull)模式。
     * 推模式采用Basic.Consume进行消费，拉模式采用Basic.get进行消费
     */
    @Test
    public void consume(){
        ConnectionFactory factory = getFactory();
        try {
            //无论是生产者还是消费者，都需要和 RabbitMQ Broker 建立连接，这个连接就是一条TCP连接
            //创建连接
            Connection connection = factory.newConnection();
            //Channel是建立在Connection之上的虚拟连接，RabbitMQ 处理的每条 AMQP 指令都是通过信道完成的。
            //创建AMQP信道 (Channel) ,, Channel可以用来发送或者接受消息
            Channel channel = connection.createChannel();

            //------------------------------------------推模式 basicConsumer-------------------------------------------//

            //与Broker建立连接
            DefaultConsumer consumer = new DefaultConsumer(channel);
            //消费消息
            channel.basicConsume("queue Name", consumer);

            //autoAck设为false，接收消息之后进行显示ack操作，对于消费者来说这个设置是非常必要的，可以防止消息不必要的丢失。
            boolean autoAck = false;
            channel.basicQos(64);
            channel.basicConsume("queueName",
                    autoAck,
                    "myConsumerTag",
                    new DefaultConsumer(channel){
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope,
                                                   AMQP.BasicProperties properties,
                                                   byte[] body) throws IOException {
                            String routingKey = envelope.getRoutingKey();
                            String contentType = properties.getContentType();
                            long deliveryTag = envelope.getDeliveryTag();
                            //
                            channel.basicAck(deliveryTag, false);
                        }
                    });

            //public String basicConsume(
            // String queue,                   队列的名称
            // boolean autoAck,                设置是否自动确认。建议设为false，即不自动确认
            // String consumerTag,             消费者标签，用来区分多个消费者
            // boolean noLocal,                设置为true则表示不能将同一个Connection中生产者发送的消息传送给这个Connection中的消费者。
            // boolean exclusive,              设置是否排他
            // Map<String, Object> arguments,  设置消费者的其他参数
            // Consumer callback               设置消费者的回调函数。用来处理RabbitMQ推送过来的消息。比如DefaultConsumer
            // ) throws IOException
            // 和生产者一样，消费者客户端同样需要考虑线程安全的问题。最常用的做法是一个Channel对应一个消费者。


            //------------------------------------------拉模式 basicGet------------------------------------------------//
            //basicGet没有其他重载方法
            GetResponse response = channel.basicGet("queue_name", false);
            System.out.println(new String(response.getBody()));
            channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * ACK
     * <p>消息确认机制(message acknowledgement) : 保证消息从队列可靠地到达消费者
     *
     * <p> 消费者在订阅消息的时候，可以指定autoAck参数，当autoAck等于false的时候，
     * RabbitMQ会等待消费者显示地回复确认消息后才从内存（或者磁盘）中移除消息（实际上是先打上删除标记，之后再删除）。
     * 当autoAck等于true是，RabbitMQ会自动把发出去的消息置为确认，然后从内存（或者磁盘）中删除，而不管消费者是否真正的消费了这些消息。
     *
     * <p> 采用消息确认机制后，只要设置autoAck参数为false，消费者就可以有足够的时间处理消息（任务），
     * 不用担心处理消息过程中消费者进程挂掉后消息丢失的问题，因为RabbitMQ会一直等待持有消息直到消费者显示调用Basic.Ack命令为止。
     *
     *
     */



    private ConnectionFactory getFactory(){
        // amqp://admin:admin@10.200.5.117:5672/
        ConnectionFactory factory = new ConnectionFactory();
        //用户名
        factory.setUsername("admin");
        //密码
        factory.setPassword("admin");
        //
        factory.setVirtualHost("");
        //IP
        factory.setHost("10.200.5.117");
        //端口
        factory.setPort(5672);
        return factory;
    }

    private ConnectionFactory getFactoryByUri() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        // URI格式 : amqp://userName:password@ipAddress:portNumber/virtualHost
        // amqp://admin:admin@10.200.5.117:5672/
        ConnectionFactory factory = new ConnectionFactory();
        String uri = "amqp://admin:admin@10.200.5.117:5672/";
        factory.setUri(uri);
        return factory;
    }
}
