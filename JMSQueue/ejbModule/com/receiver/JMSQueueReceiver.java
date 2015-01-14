package com.receiver;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;

import com.pojo.Employee;

public class JMSQueueReceiver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName());  
		QueueConnectionFactory factory = (QueueConnectionFactory) HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);
		Queue queue = HornetQJMSClient.createQueue("testQueue");
		QueueConnection queueConnection = null;
		QueueSession session = null;
		try{

			queueConnection = factory.createQueueConnection("testuser", "jboss");
			queueConnection.start();
			session = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			QueueReceiver recv = session.createReceiver(queue);
			Message message = (Message) recv.receiveNoWait();
			
			while(message != null){
				if(message instanceof TextMessage){
					System.out.println("Queue Message@Receiver---------------->: " + ((TextMessage)message).getText());
				} else{
					System.out.println("Queue Message@Receiver---------------->: " + ((Employee)((ObjectMessage)message).getObject()).toString());
				}
				/*System.out.println("JMSCorrelation ID------------>: " + message.getJMSCorrelationID());
  	            System.out.println("Message ID ------------------>: " + message.getJMSMessageID());*/
				message = (Message) recv.receiveNoWait();
			}

			if(message == null){
				System.out.println("Queue is empty");
			}

		} catch (Exception exception) {
			// TODO: handle exception
			exception.printStackTrace();
		} finally {
			try {
				if(session != null){
					session.close();
				}
				if(queueConnection != null){
					queueConnection.close();
				}

			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

	}

	public static class ExListener implements MessageListener
	{
		@Override
		public void onMessage(Message msg)
		{
			TextMessage tm = (TextMessage) msg;
			try {
				System.out.println("onMessage, recv text="+tm.getText());
			} catch(Throwable t) {
				t.printStackTrace();
			}
		}
	}

}
