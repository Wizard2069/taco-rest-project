package tacos.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import tacos.Order;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

// @Service
public class JmsOrderMessagingServiceEx implements OrderMessagingService {

    private JmsTemplate jmsTemplate;

    private Destination orderQueue;

    // @Autowired
    public JmsOrderMessagingServiceEx(
            JmsTemplate jmsTemplate,
            Destination orderQueue
    ) {
        this.jmsTemplate = jmsTemplate;
        this.orderQueue = orderQueue;
    }

    @Override
    public void sendOrder(Order order) {
        jmsTemplate.send(
                // orderQueue,
                "tacocloud.order.queue",
                session -> session.createObjectMessage(order)
        );
    }

}
