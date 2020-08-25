package tacos.kitchen.messaging.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tacos.Order;

/*@Profile("rabbit-template")
@Component("templateOrderReceiver")*/
public class RabbitOrderReceiverEx {

    private RabbitTemplate rabbitTemplate;

    private MessageConverter converter;

    // @Autowired
    public RabbitOrderReceiverEx(
            RabbitTemplate rabbitTemplate,
            MessageConverter converter
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.converter = converter;
    }

    public Order receiveOrder() {
        Message message = rabbitTemplate.receive("tacocloud.order.queue");

        return message != null
                ? (Order) converter.fromMessage(message)
                : null;
    }

}
