package com.dk.jms;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author dk
 * @date 2016/4/26
 */
public class Chat implements MessageListener {
    private TopicSession pubSession;
    private TopicPublisher publisher;
    private TopicConnection connection;
    private String username;

    public Chat(String topicFactory,String topicName,String username) throws NamingException, JMSException {

        // 设置JNDI连接参数
//        Properties env = new Properties();
//        env.put(javax.naming.Context.SECURITY_PRINCIPAL, "system");
//        env.put(javax.naming.Context.SECURITY_CREDENTIALS, "manager");
//        env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
//        env.put(javax.naming.Context.PROVIDER_URL,"tcp://127.0.0.1:61616");

        //使用jndi.properties文件获取一个JNDI连接
        InitialContext cxt = new InitialContext();

        //查找一个JMS连接工厂并创建连接
        TopicConnectionFactory conFactory = (TopicConnectionFactory) cxt.lookup(topicFactory);
        TopicConnection connection = conFactory.createTopicConnection();

        //创建两个JMS会话对象
        TopicSession pubSession = connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
        TopicSession subSession = connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);

        //查找一个JMS主题
        Topic chatTopic = (Topic) cxt.lookup(topicName);

        //创建一个JMS发布者和订阅者，createSubscriber中附加的参数是一个消息选择器（null）和noLocal标记的一个真值，
        // 它表明这个发布者生产的消息不应被它自己所消费
        TopicPublisher publisher = pubSession.createPublisher(chatTopic);
        TopicSubscriber subscriber = subSession.createSubscriber(chatTopic,null,true);

        //设置一个JMS消息侦听器
        subscriber.setMessageListener(this);

        this.connection = connection;
        this.pubSession = pubSession;
        this.publisher = publisher;
        this.username = username;


        //启动JMS连接；允许传送消息
        connection.start();
    }

    /**
     * 接收来自TopicSubscriber的消息
     * @param message
     */
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            System.out.println(textMessage.getText());
        }catch (JMSException e){
            e.printStackTrace();
        }
    }

    /**
     * 使用发布者创建并发送消息
     * @param text
     * @throws JMSException
     */
    protected void writeMessage(String text) throws JMSException {
        TextMessage message = pubSession.createTextMessage();
        message.setText(username+": "+text);
        publisher.publish(message);
    }

    //关闭JMS连接
    public void close() throws JMSException {
        connection.close();
    }

    public static void main(String[] args )throws Exception {
//        if(args.length!=3){
//            System.out.println("error");
//        }
        Chat chat = new Chat("TopicCF","topic1","username");
        BufferedReader commandLine = new BufferedReader(new InputStreamReader(System.in));

        while (true){
            String s = commandLine.readLine();
            if(s.equalsIgnoreCase("exit")){
                chat.close();
                System.exit(0);
            }else{
                chat.writeMessage(s);
            }
        }
    }




}
