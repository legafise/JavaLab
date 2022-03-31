package com.epam.esm.controller;

import com.epam.esm.entity.Order;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final String ORDERED_CERTIFICATE_INFO = "Ordered certificate(id = %d) info";
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public EntityModel<Order> readOrder(@PathVariable long id) {
        Order readOrder = orderService.findOrderById(id);
        EntityModel<Order> orderEntityModel = EntityModel.of(readOrder);
        if (!Objects.requireNonNull(orderEntityModel.getContent()).getCertificate().isDeleted()) {
            WebMvcLinkBuilder linkToOrderedCertificate = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                    .readCertificateById(Objects.requireNonNull(orderEntityModel.getContent()).getCertificate().getId()));
            orderEntityModel.add(linkToOrderedCertificate.withRel(String.format(ORDERED_CERTIFICATE_INFO,
                    orderEntityModel.getContent().getCertificate().getId())));
        }

        return orderEntityModel;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public EntityModel<Order> createOrder(@RequestParam long userId, @RequestParam long certificateId) {
        Order createdOrder = orderService.createOrder(userId, certificateId);
        EntityModel<Order> orderModel = EntityModel.of(createdOrder);
        WebMvcLinkBuilder linkToCustomer = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .readUserById(userId));
        WebMvcLinkBuilder linkToAllCustomerOrders = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .readAllUserOrders(userId));
        orderModel.add(linkToCustomer.withRel("customer"));
        orderModel.add(linkToAllCustomerOrders.withRel("all-customer's-orders"));
        return orderModel;
    }
}
