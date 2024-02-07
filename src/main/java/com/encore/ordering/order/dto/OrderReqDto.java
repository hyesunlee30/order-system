package com.encore.ordering.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderReqDto {
    private List<OrderItemDto> orderItemDtos;
    @Data
    public static class OrderItemDto{
        private Long itemId;
        private int count;
    }
}
