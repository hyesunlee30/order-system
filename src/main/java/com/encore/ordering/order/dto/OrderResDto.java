package com.encore.ordering.order.dto;

import com.encore.ordering.order.domain.OrderStatus;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order_item.domain.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class OrderResDto {
    private Long id;
    private String email;
    private String orderStatus;
    private List<OrderResItemDto> orderItems;
    private LocalDateTime createdTime;


    @Data
    public static class OrderResItemDto{
        private Long id;
        private String itemName;
        private int count;
    }


    public static OrderResDto toDto(Ordering ordering) {
        List<OrderResItemDto> orderResItemDtos = new ArrayList<>();
        for(OrderItem orderItem : ordering.getOrderItems()) {
            OrderResDto.OrderResItemDto dto = new OrderResDto.OrderResItemDto();
            dto.setId(orderItem.getId());
            dto.setItemName(orderItem.getItem().getName());
            dto.setCount(orderItem.getQuantity());
            orderResItemDtos.add(dto);
        }

        return OrderResDto.builder()
                .id(ordering.getId())
                .email(ordering.getMember().getEmail())
                .orderStatus(ordering.getOrderStatus().toString())
                .orderItems(orderResItemDtos)
                .createdTime(ordering.getCreatedTime())
                .build();
    }
}
