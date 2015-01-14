package com.mdb;

import java.util.Date;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import com.pojo.Employee;

/**
 * Message-Driven Bean implementation class for: QueueListenerMDB
 *
 */
@MessageDriven(
		activationConfig = { 
				@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
				@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/test")
				})
public class QueueListenerMDB implements MessageListener {

    /**
     * Default constructor. 
     */
    public QueueListenerMDB() {
        // TODO Auto-generated constructor stub
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        // TODO Auto-generated method stub
    	try {
            if (message instanceof TextMessage) {
                System.out.println("Queue: I received a TextMessage at "
                                + new Date());
                TextMessage msg = (TextMessage) message;
                System.out.println("Message is : " + msg.getText());
            } else if (message instanceof ObjectMessage) {
                System.out.println("Queue: I received an ObjectMessage at "
                                + new Date());
                ObjectMessage msg = (ObjectMessage) message;
                Employee employee = (Employee) msg.getObject();
                System.out.println("Employee Details: "+ employee);
                
            } else {
                System.out.println("Not a valid message for this Queue MDB");
            }
 
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
