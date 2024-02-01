package com.encore.ordering.order.controller;

import com.encore.ordering.order.service.OrderingService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderingController {
    private final OrderingService orderingService;

    public OrderingController(OrderingService orderingService) {
        this.orderingService = orderingService;
    }
}
