package com.client;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;

import com.pojo.Employee;

public class JMSApplicationSender {
	 /**
	  * 
	  * @param args
	  */
    public static void main(String[] args) {
    	
        TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName());  
        ConnectionFactory factory = (ConnectionFactory) HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);
        Queue queue = HornetQJMSClient.createQueue("testQueue");
        Connection connection = null;
        
        try {
            connection = factory.createConnection("testuser", "jboss");
            Session session = connection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);
             
            //1. Sending TextMessage to the Queue 
            TextMessage message = session.createTextMessage();
            message.setText("Hello EJB3 MDB Queue!!!");
            producer.send(message);
            System.out.println("1. Sent TextMessage to the Queue");
             
            //2. Sending ObjectMessage to the Queue
            ObjectMessage objMsg = session.createObjectMessage();
             
            Employee employee = new Employee();
            employee.setId(2163);
            employee.setName("Chetan");
            employee.setDesignation("Software Engineer");
            employee.setSalary(7262);
            objMsg.setObject(employee);                     
            producer.send(objMsg);
            System.out.println("2. Sent ObjectMessage to the Queue");
             
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
            try {
				connection.close();
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }               
    }
}