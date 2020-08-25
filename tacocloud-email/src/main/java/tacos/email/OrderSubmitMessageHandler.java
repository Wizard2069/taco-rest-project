package tacos.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrderSubmitMessageHandler implements GenericHandler<Order> {

    private RestTemplate restTemplate;

    private ApiProperties apiProps;

    @Autowired
    public OrderSubmitMessageHandler(RestTemplate restTemplate, ApiProperties apiProps) {
        this.restTemplate = restTemplate;
        this.apiProps = apiProps;
    }

    @Override
    public Object handle(Order order, MessageHeaders messageHeaders) {
        restTemplate.postForObject(apiProps.getUrl(), order, String.class);

        return null;
    }

}
