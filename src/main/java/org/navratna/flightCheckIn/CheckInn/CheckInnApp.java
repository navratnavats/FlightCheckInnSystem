package org.navratna.flightCheckIn.CheckInn;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.navratna.flightCheckIn.model.Passenger;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CheckInnApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");


        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext();

            JMSProducer producer = jmsContext.createProducer();

            Passenger passenger = new Passenger();
            passenger.setId(1);
            passenger.setFirstName("Navratna");
            passenger.setLastName("Vats");
            passenger.setEmail("nvats@somemail.com");
            passenger.setPhoneNumber(12345678L);

            producer.send(requestQueue,passenger);

            JMSConsumer consumer = jmsContext.createConsumer(replyQueue);
            MapMessage replyMessage = (MapMessage) consumer.receive(5000);

            if(replyMessage.getBoolean("confirmed")){
                System.out.println("Ticket Confirmed");
                System.out.println("Passenger Name is :- "+ replyMessage.getString("name"));
                System.out.println("Passenger Email id is :- "+ replyMessage.getString("email"));
                System.out.println("Mobile number is :- "+ replyMessage.getLong("phoneNumber"));
            }

            else {
                System.out.println("Ticket not Confirmed");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

