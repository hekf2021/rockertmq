package com;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class Consumer {

	public static void main(String[] args) throws InterruptedException, MQClientException {
		//设置消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ConsumerGroupName");
        //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //指定nameServer的地址
        consumer.setNamesrvAddr("10.150.7.208:9876");
        //指定订阅的topic及tag表达式
        consumer.subscribe("TopicTest", "*");
        
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
															ConsumeConcurrentlyContext context) {
				MessageExt messageExt = msgs.get(0);
				System.out.println(String.format("Custome message [%s],tagName[%s]", 
						new String(messageExt.getBody()),
						messageExt.getTags()));
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		//启动消费者实例
        consumer.start();
        System.out.println("Consumer Started.");
    }
}