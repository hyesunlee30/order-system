package com.encore.ordering.member.dto;

import com.encore.ordering.member.domain.Address;
import com.encore.ordering.member.domain.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.hibernate.cache.spi.access.CachedDomainDataAccess;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private String city;
    private String street;
    private String zipcode;
    private int orderCount;

    public static MemberResponse toMemberResponse(Member member) {
        MemberResponseBuilder builder = MemberResponse.builder();
        builder.name(member.getName());
        builder.email(member.getEmail());
        builder.id(member.getId());
        builder.orderCount(member.getOrderings().size());
        Address address = member.getAddress();

        if (address != null) {
            builder.city(address.getCity());
            builder.street(address.getStreet());
            builder.zipcode(address.getZipcode());
        }


        return builder.build();
    }
}


