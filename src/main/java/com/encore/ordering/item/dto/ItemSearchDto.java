package com.encore.ordering.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class ItemSearchDto {
    private String name;
    private String category;
}
