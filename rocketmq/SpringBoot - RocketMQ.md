## SpringBoot RocketMQ

### rocketmq-starter

[官方文档](https://github.com/apache/rocketmq-spring/wiki/)

#### 1.依赖

```xml
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
    <version>2.2.2</version>
</dependency>
```

#### 2.yml配置

```yaml
rocketmq:
  name-server: 192.168.110.140:9876
  producer:
    group: my-producer
```

#### 3.生产者

```java
package com.example.demo.starter;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final Logger log = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送消息
     *
     * @param topic
     * @param message
     */
    public void sendMessage(String topic, String message) {
        rocketMQTemplate.convertAndSend(topic, message);
    }

    /**
     * 发送事务消息
     *
     * @param topic
     * @param message
     * @throws InterruptedException
     */
    public void sendMessageInTranslation(String topic, String message) throws InterruptedException {
        String[] tags = {"TagA", "TagB", "TagC", "TagD", "TagE"};
        int length = tags.length;
        for (int i = 0; i < 10; i++) {
            Message<String> msg = MessageBuilder.withPayload(message).build();
            String destination = topic + ":" + tags[i % length];
            TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(destination, msg, destination);
            log.info("{}", sendResult);
            Thread.sleep(10);
        }
    }

    /**
     * 同步发送延时消息
     * 级别：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h（1-18个级别）
     * @param topic
     * @param message
     * @param timeout 发送超时时间（毫秒）
     * @param delayLevel 延迟级别
     */
    public void syncSecond(String topic, String message, long timeout, int delayLevel) {
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(message).build(), timeout, delayLevel);
    }

    /**
     * 异步发送延时消息
     * 级别：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h（1-18个级别）
     * @param topic
     * @param message
     * @param timeout 发送超时时间
     * @param delayLevel 延迟级别（毫秒）
     */
    public void asyncSecond(String topic, String message, SendCallback sendCallback, long timeout, int delayLevel) {
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(message).build(), sendCallback, timeout, delayLevel);
    }
}
```

#### 4.消费者

```java
package com.example.demo.starter;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 参考：https://www.cnblogs.com/qlqwjy/p/16175864.html
 * 简单消费者（无法控制ack）
 * RocketMQMessageListener
 * 消息选择器：selectorType
 * 过滤规则：selectorExpression
 * 消费模式（并发消费/顺序消费）：consumeMode 默认就是并发。ConsumeMode.ORDERLY和MessageModel.BROADCASTING不能一起设置
 * 消费模型（集群和广播）：messageModel-默认为集群模式
 * 指定事务模板 @RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate")
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "${rocketmq.producer.group}", topic = "my-topic", selectorExpression = "*")
public class Consumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("消费消息: {}", message);
    }
}
```

#### 5.测试

```java
package com.example.demo.controller;

import com.example.demo.starter.Producer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MessageController {

    private static String topic = "my-topic";

    @Resource
    private Producer producer;

    @GetMapping("/send")
    public Object send(String msg) {
        producer.sendMessage(topic, msg);
        return "success";
    }

    @GetMapping("/send1")
    public Object send1(String msg) {
        try {
            producer.sendMessageInTranslation(topic, msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
```



### 传统版

##### 1.依赖

```xml
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-client</artifactId>
    <version>4.5.2</version>
</dependency>
```

##### 2.生产者

```java
package com.example.demo.recketmq.test;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.time.LocalDateTime;

@Slf4j
public class Producer {

    /**
     *  1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    public enum ExpiredEnum {

        /**
         * 消息延迟时间，一共18个级别
         */
        ONE(1, "1秒"),
        TWO(2, "5秒"),
        THREE(3, "10秒"),
        FOUR(4, "30秒"),
        FIVE(5, "1分钟"),
        SEX(6, "2分钟"),
        SEVEN(7, "3分钟"),
        EIGHT(8, "4分钟"),
        NINE(9, "5分钟"),
        TEN(10, "6分钟"),
        ELEVEN(11, "7分钟"),
        TWELVE(12, "8分钟"),
        THIRTEEN(13, "9分钟"),
        FOURTEEN(14, "10分钟"),
        FIFTEEN(15, "20分钟"),
        SIXTEEN(16, "30分钟"),
        SEVENTEEN(17, "1小时"),
        EIGHTEEN(18, "2小时"),
        ;
        private int value;
        private String text;

        ExpiredEnum(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }

    }

    public static void main(String[] args) throws MQClientException {

        DefaultMQProducer producer = new DefaultMQProducer("shop-order-a");
        producer.setNamesrvAddr("192.168.110.140:9876");

        producer.start();

        log.info("send time: {}", LocalDateTime.now());

        Student student = null;
        for (int i = 12; i <= 20; i++) {
            try {
                student = Student.builder().id((long) i).height(160 + i).age(18).score((double) i).stuName("张三" + i).build();
                Message msg = new Message("shop-order",
                        "tag",
                        "OrderID188",
                        JSON.toJSONString(student).getBytes(RemotingHelper.DEFAULT_CHARSET));

                // 消息延迟级别分别为1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h，共18个级别
                // 设置为5表示1分钟后过期
                //msg.setDelayTimeLevel(ExpiredEnum.FIVE.value);

                SendResult sendResult = producer.send(msg);
                log.info("{}", sendResult);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //测试时可以注释，线上环境必须打开
        producer.shutdown();
    }
}
```

##### 3.消费者

```java
package com.example.demo.recketmq.test;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class PushConsumer {

    public static void main(String[] args) throws Exception {
        // 消费者组名称
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("order-a-2");
        // *-所有tag  xxx-指定tag xxx
        consumer.subscribe("shop-order", "tag");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //wrong time format 2017_0422_221800
        //consumer.setConsumeTimestamp("20181109221800");
        consumer.setNamesrvAddr("192.168.110.140:9876");
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                //for (MessageExt msg : msgs) {
                //    log.info("{} Receive New Messages: {}, {}, {}, {}", Thread.currentThread().getName(), msg.getMsgId(), msg.getKeys(), msg.getTags(), new String(msg.getBody()));
                //}
                try {
                    List<Student> studentList = msgs.stream().map(Message::getBody).map(item -> JSON.parseObject(new String(item, UTF_8), Student.class)).collect(Collectors.toList());
                    log.info("信息：{}", studentList);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });
        consumer.start();
        log.info("Consumer Started.");
    }

}
```

