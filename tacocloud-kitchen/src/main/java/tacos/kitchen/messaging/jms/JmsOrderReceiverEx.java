package tacos.kitchen.messaging.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import tacos.Order;
import tacos.kitchen.OrderReceiver;

import javax.jms.JMSException;
import javax.jms.Message;

// @Component("templateOrderReceiver")
public class JmsOrderReceiverEx implements OrderReceiver {

    private JmsTemplate jmsTemplate;

    private MessageConverter converter;

    // @Autowired
    public JmsOrderReceiverEx(JmsTemplate jmsTemplate, MessageConverter converter) {
        this.jmsTemplate = jmsTemplate;
        this.converter = converter;
    }

    @Override
    public Order receiveOrder() {
        Message message = jmsTemplate.receive("tacocloud.order.queue");

        Order order = null;

        try {
            order = (Order) converter.fromMessage(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return order;
    }

}
