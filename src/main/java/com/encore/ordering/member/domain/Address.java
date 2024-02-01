package com.encore.ordering.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable // Embeddable 이 객체를 어딘가의 객체를 중간에 삽입될 수 있다.
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
