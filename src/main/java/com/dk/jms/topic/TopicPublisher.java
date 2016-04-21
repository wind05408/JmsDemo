package com.dk.jms.topic;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息生产者为发布者（publisher）,消息消费者为订阅者（subscriber）。
	 发布一个主题消息，能够由多个订阅者所接收。
	 广播（broadcasting）消息。
	 传送模型是基于推送(push)的模型
 *
 * 发布者
 */
public class TopicPublisher {
	public static void main(String[] args) throws JMSException {
		         ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		         Connection connection = factory.createConnection();
		         connection.start();
		         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		         Topic topic = session.createTopic("myTopic.messages");
		         MessageProducer producer = session.createProducer(topic);
		         producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		         for(int i =0;i<10;i++){
		             TextMessage message = session.createTextMessage();
		             message.setText("message_" + System.currentTimeMillis());
		             producer.send(message);
		             System.out.println("Sent message: " + message.getText());
		             try {
		                 Thread.sleep(1000);
		             } catch (InterruptedException e) {
		                 e.printStackTrace();
		             }
		         }
		       session.close();
		       connection.stop();
		       connection.close();
		    }

}
