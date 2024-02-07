package com.encore.ordering.order.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.dto.ItemReqDto;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.OrderReqDto;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.order.service.OrderingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderingController {
    private final OrderingService orderingService;

    public OrderingController(OrderingService orderingService) {
        this.orderingService = orderingService;
    }

    @PostMapping("/order/create")
    public ResponseEntity<CommonResponse> orderCreate(@RequestBody OrderReqDto orderReqDto) {
        Ordering ordering = orderingService.create(orderReqDto);
        return new ResponseEntity<>(
                new CommonResponse(HttpStatus.CREATED,"order successfully create", ordering.getId())
                , HttpStatus.CREATED);
    }

    //@PreAuthorize("hasRole('ADMIN') or #email == authentication.principal.username")
    @DeleteMapping("/order/{id}/cancel")
    public ResponseEntity<CommonResponse> orderDelete(@PathVariable Long id) {
        Ordering ordering = orderingService.cancel(id);
        return new ResponseEntity<>(
                new CommonResponse(HttpStatus.OK,"order successfully cancel", ordering.getId())
                , HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public ResponseEntity<CommonResponse> orders() {
        List<OrderResDto> orderResDtos =  orderingService.findAll();
        return new ResponseEntity<>(
                new CommonResponse(HttpStatus.OK,"order successfully cancel", orderResDtos)
                , HttpStatus.OK);
    }

}
