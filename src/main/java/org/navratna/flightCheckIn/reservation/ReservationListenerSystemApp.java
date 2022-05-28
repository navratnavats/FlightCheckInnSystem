package org.navratna.flightCheckIn.reservation;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.navratna.flightCheckIn.model.Passenger;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ReservationListenerSystemApp implements MessageListener {
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext();

            InitialContext initialContext = new InitialContext();
            Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
            MapMessage replyMessage = jmsContext.createMapMessage();
            Passenger passenger = (Passenger) objectMessage.getObject();

            String firstName = passenger.getFirstName();
            String lastName = passenger.getLastName();
            System.out.println("Passenger Name is :- "+ firstName +" "+ lastName);
            String email = passenger.getEmail();
            System.out.println("Passenger Email id is :- "+ email);
            long phoneNumber = passenger.getPhoneNumber();
            System.out.println("Mobile number is :- "+ phoneNumber);

            int digitsInPhoneNumber =(Long.toString(phoneNumber)).length();
            System.out.println("Digits in phone number is "+ digitsInPhoneNumber);
            if(firstName!=null && lastName!=null && digitsInPhoneNumber>9 && digitsInPhoneNumber<11){
                replyMessage.setBoolean("confirmed", true);
                replyMessage.setString("name", firstName+" "+lastName);
                replyMessage.setString("email", email);
                replyMessage.setLong("phoneNumber", phoneNumber);
            }
            else {
                replyMessage.setBoolean("confirmed", false);
            }

            JMSProducer producer = jmsContext.createProducer();
            producer.send(replyQueue,replyMessage);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
