package com.hui.springcloud.order.api;

import com.hui.springcloud.common.entity.order.Order;
import com.hui.springcloud.order.client.StreamSender;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <b><code>TestController</code></b>
 * <p/>
 * Description:
 * <p/>
 * <b>Creation Time:</b> 2018/12/2 16:36.
 *
 * @author HuWeihui
 */
@RestController
@EnableBinding(StreamSender.class)
public class TestController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StreamSender streamSender;

    @Value("${env}")
    private String env;

    @GetMapping("/test_env")
    private ResponseEntity test(){
        return ResponseEntity.ok(env);
    }

    /**
     * 通过MQ发送String消息
     *
     * @author HuWeihui
     * @since hui_project v1
     */
    @GetMapping("/test_send")
    public void sendMsg(){
        amqpTemplate.convertAndSend("huiQueue",new Date()+ "send msg  to receiver");
    }


    /**
     * 通过MQ发送消息到ServiceA.
     *
     * @author HuWeihui
     * @since hui_project v1
     */
    @GetMapping("/test_sendA")
    public void sendMsgToServiceA(){
        amqpTemplate.convertAndSend("huiExchange","serviceA",new Date()+ "send msg  to receiver");
    }

    /**
     * 通过MQ发送消息到ServiceB.
     *
     * @author HuWeihui
     * @since hui_project v1
     */
    @GetMapping("/test_sendB")
    public void sendMsgToServiceB(){
        amqpTemplate.convertAndSend("huiExchange","serviceB",new Date()+ "send msg  to receiver");
    }

    /**
     * 通过CloudStream 发送文本消息.
     *
     * @author HuWeihui
     * @since hui_project v1
     */
    @GetMapping("/test_stream")
    public void sendStreamMsg(){
        String msg = new Date()+ " send  msg  to receiver";
        streamSender.output().send(MessageBuilder.withPayload(msg).build());
    }

    /**
     * 通过CloudStream发送序列化后的消息.
     *
     * @author HuWeihui
     * @since hui_project v1
     */
    @GetMapping("/test_stream_obj")
    public void sendStreamObj(){
        Order order = new Order();
        order.setOrderId("order-1");
        order.setProductId("product-1");
        order.setOrderName("大订单");
        streamSender.outputObj().send(MessageBuilder.withPayload(order).build());
    }

    /**
     * 通过CloudStream发送对象JSON（项目实施）.
     *
     * @author HuWeihui
     * @since hui_project v1
     */
    @GetMapping("/test_stream_obj2")
    public void sendStreamObj2(){
        Order order = new Order();
        order.setOrderId("order-1");
        order.setProductId("product-1");
        order.setOrderName("大订单");
        streamSender.output().send(MessageBuilder.withPayload(order).build());
    }
}