package tacos.kitchen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.Order;

@Slf4j
@Profile({"jms-template", "rabbitmq-template"})
@Controller
@RequestMapping("/orders")
public class OrderReceiverController {

    private OrderReceiver orderReceiver;

    @Autowired
    public OrderReceiverController(OrderReceiver orderReceiver) {
        this.orderReceiver = orderReceiver;
    }

    @GetMapping("/receive")
    public String receiveOrder(Model model) {
        Order order = orderReceiver.receiveOrder();

        if (order != null) {
            model.addAttribute("order", order);

            return "receiveOrder";
        }

        return "noOrder";
    }

}
