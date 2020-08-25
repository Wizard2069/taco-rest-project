package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tacos.Order;
import tacos.data.OrderRepository;
import tacos.messaging.OrderMessagingService;

@RestController
@RequestMapping(
        path = "/orders",
        produces = "application/json"
)
@CrossOrigin(origins = "*")
public class OrderApiController {

    private OrderRepository orderRepo;

    private OrderMessagingService orderMessagingService;

    private EmailOrderService emailOrderService;

    @Autowired
    public OrderApiController(
            OrderRepository orderRepo,
            OrderMessagingService orderMessagingService,
            EmailOrderService emailOrderService
    ) {
        this.orderRepo = orderRepo;
        this.orderMessagingService = orderMessagingService;
        this.emailOrderService = emailOrderService;
    }

    @GetMapping(produces = "application/json")
    public Iterable<Order> allOrders() {
        return orderRepo.findAll();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Order postOrder(@RequestBody Order order) {
        orderMessagingService.sendOrder(order);
        return orderRepo.save(order);
    }

    @PostMapping(
            path = "/fromEmail",
            consumes = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Order postOrderFromEmail(@RequestBody EmailOrder emailOrder) {
        Order order = emailOrderService.convertEmailOrderToDomainOrder(emailOrder);
        orderMessagingService.sendOrder(order);

        return orderRepo.save(order);
    }

    @PutMapping(consumes = "application/json")
    public Order putOrder(@RequestBody Order order) {
        return orderRepo.save(order);
    }

    @PatchMapping(
            path = "/{orderId}",
            consumes = "application/json"
    )
    public Order patchOrder(
            @PathVariable Long orderId,
            @RequestBody Order patch
    ) {
        Order order = orderRepo.findById(orderId).get();

        if (patch.getDeliveryName() != null) {
            patch.setDeliveryName(order.getDeliveryName());
        }

        if (patch.getDeliveryStreet() != null) {
            patch.setDeliveryStreet(order.getDeliveryStreet());
        }

        if (patch.getDeliveryCity() != null) {
            patch.setDeliveryCity(order.getDeliveryCity());
        }

        if (patch.getDeliveryState() != null) {
            patch.setDeliveryState(order.getDeliveryState());
        }

        if (patch.getDeliveryZip() != null) {
            patch.setDeliveryZip(order.getDeliveryZip());
        }

        if (patch.getCcNumber() != null) {
            patch.setCcNumber(order.getCcNumber());
        }

        if (patch.getCcExpiration() != null) {
            patch.setCcExpiration(order.getCcExpiration());
        }

        if (patch.getCcCVV() != null) {
            patch.setCcCVV(order.getCcCVV());
        }

        return orderRepo.save(patch);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long orderId) {
        try {
            orderRepo.deleteById(orderId);
        } catch (EmptyResultDataAccessException ignored) {

        }
    }

}
