package com.remoteClient;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;

import com.pojo.Employee;

public class RemoteJMSBrowser {
	/**
	 * 
	 * @param args
	 */
    public static void main(String[] args){
 
    	TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName());  
        ConnectionFactory factory = (ConnectionFactory) HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);
        Queue queue = HornetQJMSClient.createQueue("testQueue");
        Connection connection = null;
        Session session = null;
        
        try{
        	connection = factory.createConnection("testuser", "jboss");
        	session = connection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
     
            QueueBrowser browser = session.createBrowser(queue);
            
			Enumeration<Message> messageEnum = browser.getEnumeration();
            while (messageEnum.hasMoreElements()) {
            		
    	            Message message = messageEnum.nextElement();
    	            if(message instanceof TextMessage){
    	            	System.out.println("Queue Message@Browser---------------->: " + ((TextMessage)message).getText());
    	            } else{
    	            	System.out.println("Queue Message@Browser---------------->: " + ((Employee)((ObjectMessage)message).getObject()).toString());
    	            }
    	            /*System.out.println("JMSCorrelation ID------------>: " + message.getJMSCorrelationID());
    	            System.out.println("Message ID ------------------>: " + message.getJMSMessageID());*/
            }
            
            if(!messageEnum.hasMoreElements()){
            	System.out.println("-----------No elements are present in the Queue---------");
            }
            
        } catch (Exception exception) {
			// TODO: handle exception
        	exception.printStackTrace();
        	
		} finally{
			if(session != null){
				try {
					session.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(connection != null){
				 try {
					connection.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }
}
