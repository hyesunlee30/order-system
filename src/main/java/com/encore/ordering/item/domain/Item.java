package com.encore.ordering.item.domain;

import com.encore.ordering.common.BaseTimeEntity;
import com.encore.ordering.item.dto.ItemReqDto;
import com.encore.ordering.item.dto.ItemResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private int price;
    private int stockQuantity;
    private String imagePath;
    @Builder.Default
    private String delYn = "N";

    public void deleteItem() {
        this.delYn = "Y";
    }

    public void updateItem(String name, String category, int price, int stockQuantity, String imagePath) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imagePath = imagePath;
    }

    public void updateStockQuantity(int newQuantity) {
        this.stockQuantity = newQuantity;
    }

    public void setImagePath(String imagePath) {
            this.imagePath =imagePath;
    }

    public static ItemResDto toItemResDto(Item item) {
        return ItemResDto.
                builder()
                .id(item.id)
                .name(item.name)
                .price(item.price)
                .stockQuantity(item.stockQuantity)
                .imagePath(item.imagePath)
                .category(item.category)
                .build();
    }

    public void addStockQuantity(int count) {
        this.stockQuantity  = this.getStockQuantity() + count;
    }
}
