package org.navratna.flightCheckIn.reservation;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ReservationSystemApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext =new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
         try{
             ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = activeMQConnectionFactory.createContext();

             JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
             consumer.setMessageListener(new ReservationListenerSystemApp());

             Thread.sleep(15000);
         }
         catch (Exception e){
             e.printStackTrace();
         }
    }
}
