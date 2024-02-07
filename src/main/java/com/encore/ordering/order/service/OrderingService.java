package com.encore.ordering.order.service;

import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.repository.ItemRepository;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.repository.MemberRepository;
import com.encore.ordering.order.domain.OrderStatus;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.OrderReqDto;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.order.repository.OrderingRepository;
import com.encore.ordering.order_item.domain.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OrderingService {

    private final OrderingRepository repository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public OrderingService(OrderingRepository repository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }

    public Ordering create(OrderReqDto orderReqDto) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("not found email"));

        Ordering ordering = Ordering.builder()
                .member(member)
                .build();

        List<OrderReqDto.OrderItemDto> orderItemDtos = orderReqDto.getOrderItemDtos();
        log.debug(orderItemDtos.toString());
        for (OrderReqDto.OrderItemDto req : orderReqDto.getOrderItemDtos()) {
            Item item = itemRepository.findById(req.getItemId()).orElseThrow(()->new EntityNotFoundException("not found item"));
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .quantity(req.getCount())
                    .ordering(ordering)
                    .build();
            ordering.getOrderItems().add(orderItem);
            if(item.getStockQuantity() - req.getCount() < 0) {
                throw new IllegalArgumentException("재고가 부족합니다.");
            }
            orderItem.getItem().updateStockQuantity(item.getStockQuantity() - req.getCount());

        }
        repository.save(ordering);
        return ordering;
    }

    public Ordering cancel(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Ordering ordering = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found order"));
        if (!ordering.getMember().getEmail().equals(email) && !authentication.getAuthorities().contains((new SimpleGrantedAuthority("ROLE_ADMIN")))) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
;
        if(ordering.getOrderStatus().equals(OrderStatus.CANCELED)) {
            throw new IllegalArgumentException("이미 취소된 주문입니다");
        }
        List<OrderItem> orderItems = ordering.getOrderItems();

        ordering.cancelOrder();
        for (OrderItem orderItem : orderItems) {
            orderItem.getItem().updateStockQuantity(
                    orderItem.getItem().getStockQuantity() + orderItem.getQuantity()
            );

            //orderItem.cancel();
        }
        return ordering;
    }

    public List<OrderResDto> findAll(){
        List<Ordering> orderings = repository.findAll();
        return orderings.stream().map(OrderResDto::toDto).collect(Collectors.toList());
    }
}
