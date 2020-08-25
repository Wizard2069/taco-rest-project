package tacos.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tacos.Order;

// @Service
public class RabbitOrderMessagingServiceEx implements OrderMessagingService {

    private RabbitTemplate rabbitTemplate;

    // @Autowired
    public RabbitOrderMessagingServiceEx(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendOrder(Order order) {
        MessageConverter converter = rabbitTemplate.getMessageConverter();
        MessageProperties props = new MessageProperties();

        props.setHeader("X_ORDER_SOURCE", "WEB");

        Message message = converter.toMessage(order, props);

        rabbitTemplate.send("tacocloud.order.queue", message);
    }

}
