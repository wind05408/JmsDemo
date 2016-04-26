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

        //查找
        TopicConnectionFactory conFactory = (TopicConnectionFactory) cxt.lookup(topicFactory);
        TopicConnection connection = conFactory.createTopicConnection();

        TopicSession pubSession = connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
        TopicSession subSession = connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);

        Topic chatTopic = (Topic) cxt.lookup(topicName);

        TopicPublisher publisher = pubSession.createPublisher(chatTopic);
        TopicSubscriber subscriber = subSession.createSubscriber(chatTopic,null,true);

        subscriber.setMessageListener(this);

        this.connection = connection;
        this.pubSession = pubSession;
        this.publisher = publisher;
        this.username = username;

        connection.start();
    }

    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            System.out.println(textMessage.getText());
        }catch (JMSException e){
            e.printStackTrace();
        }
    }
    protected void writeMessage(String text) throws JMSException {
        TextMessage message = pubSession.createTextMessage();
        message.setText(username+": "+text);
        publisher.publish(message);
    }

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
