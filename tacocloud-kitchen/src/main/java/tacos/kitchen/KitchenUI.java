package tacos.kitchen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tacos.Order;

@Slf4j
@Profile({"jms-listener", "rabbitmq-listener", "kafka-listener"})
@Controller
public class KitchenUI {

    private Order order;

    public void displayOrder(Order order) {
        // display some UI here
        log.info("RECEIVED ORDER: " + order);
        this.order = order;
    }

    @GetMapping
    public String showOrder(Model model) {
        if (order != null) {
            model.addAttribute("order", order);

            return "receiveOrder";
        }

        return "noOrder";
    }

}
