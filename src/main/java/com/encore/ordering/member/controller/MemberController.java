package com.encore.ordering.member.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.LoginRequest;
import com.encore.ordering.member.dto.MemberResponse;
import com.encore.ordering.member.dto.MemberSaveRequest;
import com.encore.ordering.member.service.MemberService;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.securities.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MemberController {
    private final MemberService service;

    private final JwtTokenProvider jwtTokenProvider;
    public MemberController(MemberService service, JwtTokenProvider jwtTokenProvider) {
        this.service = service;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/member/new")
    public ResponseEntity<CommonResponse> save(@Valid @RequestBody MemberSaveRequest request) {
        System.out.println(request.getEmail());
        Member member =service.save(request);
        return new ResponseEntity<>(
                new CommonResponse(HttpStatus.CREATED,"member successfully created.",member.getId()),
                HttpStatus.CREATED
        );
    }


    @PostMapping("/doLogin")
    public ResponseEntity<CommonResponse> signIn(@Valid @RequestBody LoginRequest request) {
        Member member =service.login(request);
        //json web token
        //토큰 생성 로직
        String jwt = jwtTokenProvider.createdToken(member.getEmail(), member.getRole().name());

        Map<String, Object> memberInfo = new HashMap<>();
        memberInfo.put("id", member.getId());
        memberInfo.put("token", jwt);
        return new ResponseEntity<>(
                new CommonResponse(HttpStatus.OK,"member successfully logind.",memberInfo),
                HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members")
    public List<MemberResponse> members() {
        return service.findAll();
    }

    @GetMapping("/member/myInfo")
    public MemberResponse findMyInfo() {
        return service.findMyInfo();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/{id}/orders")
    public ResponseEntity<CommonResponse> orders(@PathVariable Long id) {

        List<OrderResDto> orderResDtos = service.memberOrders(id);
        return new ResponseEntity<>(
                new CommonResponse(HttpStatus.OK,"order successfully cancel", orderResDtos)
                , HttpStatus.OK);
    }

    @GetMapping("/member/myorders")
    public ResponseEntity<CommonResponse> findByOrders() {
        List<OrderResDto> orderResDtos = service.finMyOrders();
        return new ResponseEntity<>(
                new CommonResponse(HttpStatus.OK,"order successfully cancel", orderResDtos)
                , HttpStatus.OK);
    }

}
