package com.encore.ordering.member.domain;

import com.encore.ordering.common.BaseTimeEntity;
import com.encore.ordering.member.dto.MemberSaveRequest;
import com.encore.ordering.order.domain.Ordering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class Member extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Embedded // 장점은 널처리
    private Address address;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Ordering> orderings = new ArrayList<>();


    public static Member toEntity (MemberSaveRequest request) {

        Address address = Address.builder()
                .city(request.getCity())
                .street(request.getStreet())
                .zipcode(request.getZipcode())
                .build();

        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .address(address)
                .role(Role.USER)
                .build();

        return member;
    }


}
