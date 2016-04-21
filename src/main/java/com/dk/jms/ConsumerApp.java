package com.dk.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;

/**
 * @author dk
 * @date 2016/4/21
 *
 * activemq消费者
 */
public class ConsumerApp implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerApp.class);
    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String SUBJECT = "test-activemq-queue";

    public static void main(String[] args) throws JMSException {
        //初始化ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

        //创建mq连接
        Connection conn = connectionFactory.createConnection();
        //启动连接
        conn.start();

        //创建会话
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //通过会话创建目标
        Destination dest = session.createQueue(SUBJECT);
        //Topic topic = session.createTopic("TOPIC");

        //创建mq消息的消费者
        MessageConsumer consumer = session.createConsumer(dest);

        //初始化MessageListener
        ConsumerApp me = new ConsumerApp();

        //给消费者设定监听对象
        consumer.setMessageListener(me);
    }

    public void onMessage(Message message) {
        TextMessage txtMessage = (TextMessage)message;
        try {
            LOGGER.info ("get message " + txtMessage.getText());
        } catch (JMSException e) {
            LOGGER.error("error {}", e);
        }
    }
}