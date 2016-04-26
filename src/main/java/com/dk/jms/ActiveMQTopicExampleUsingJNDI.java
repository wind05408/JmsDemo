package com.dk.jms;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;

public class ActiveMQTopicExampleUsingJNDI {
    
	public static void main(String[] args) throws Exception {
		
		(new Thread(new ActiveMQHelloWorldProducer())).start();
		(new Thread(new ActiveMQHelloWorldProducer())).start();
		(new Thread(new ActiveMQHelloWorldProducer())).start();
		(new Thread(new ActiveMQHelloWorldProducer())).start();
				
		Thread.sleep(1000);
		(new Thread(new HelloWorldConsumer())).start();
		(new Thread(new HelloWorldConsumer())).start();
		(new Thread(new HelloWorldConsumer())).start();
		 
	}

	public static class ActiveMQHelloWorldProducer implements Runnable {
		
		public void run() {
			try {
				
				// Create ConnectionFactory
				Context ctx = new InitialContext();
				TopicConnectionFactory activeMQConnectionFactory = (TopicConnectionFactory)ctx.lookup("ConnectionFactory");
				
				// Create Connection
				Connection connection = activeMQConnectionFactory.createConnection();
				connection.start();

				// Create Session
				Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
				
				// Create the destination (Topic or Queue)
				Destination destination = session.createQueue("JavaHonk");

				// Create MessageProducer from the Session to the Topic or
				// Queue
				MessageProducer producer = session.createProducer(destination);
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);

				// Create messages
				String text = "Java Honk ActiveMQ Hello world! From: "+ Thread.currentThread().getName() + " : "+ this.hashCode();
				TextMessage message = session.createTextMessage(text);

				// Tell the producer to send the message
				System.out.println("Sent message: " + message.hashCode()+ " : " + Thread.currentThread().getName());
				producer.send(message);

				// Clean up
				session.close();
				connection.close();
			} catch (Exception e) {
				System.out.println("Caught Exception: " + e);
				e.printStackTrace();
			}
		}
	}

	public static class HelloWorldConsumer implements Runnable,
			ExceptionListener {
		public void run() {
			try {

				// Create ConnectionFactory
				Context ctx = new InitialContext();
				TopicConnectionFactory activeMQConnectionFactory = (TopicConnectionFactory)ctx.lookup("ConnectionFactory");

				// Create a Connection
				Connection connection = activeMQConnectionFactory.createConnection();
				connection.start();

				connection.setExceptionListener(this);

				// Create a Session
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				// Create the destination (Topic or Queue)
				Destination destination = session.createQueue("JavaHonk");

				// Create a MessageConsumer from the Session to the Topic or
				// Queue
				MessageConsumer consumer = session.createConsumer(destination);

				// Wait for a message
				Message message = consumer.receive(1000);

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					System.out.println("Received: " + text);
				} else {
					System.out.println("Received: " + message);
				}

				consumer.close();
				session.close();
				connection.close();
			} catch (Exception e) {
				System.out.println("Caught exception: " + e);
				e.printStackTrace();
			}
		}

		public synchronized void onException(JMSException ex) {
			System.out.println("ActiveMQ JMS Exception occured.  Shutting down client.");
		}
	}
}