package com.dk.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.ConnectionMetaData;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Enumeration;

/**
 * @author dk
 * @date 2016/5/3
 * JMS MetaData
 */
public class MetaData {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerApp.class);
    public static void main(String[] args) throws Exception {
        Context cxt = new InitialContext();
        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) cxt.lookup("TopicCF");
        QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
        ConnectionMetaData metaData = queueConnection.getMetaData();
        LOGGER.info("JMS Version:"+metaData.getJMSMajorVersion()+"."+metaData.getJMSMinorVersion()+"  "+metaData.getJMSVersion());
        LOGGER.info("JMS Provider:"+metaData.getJMSProviderName());
        Enumeration enumeration = metaData.getJMSXPropertyNames();
        while (enumeration.hasMoreElements()){
            LOGGER.info(" "+ enumeration.nextElement());
        }
    }
}
