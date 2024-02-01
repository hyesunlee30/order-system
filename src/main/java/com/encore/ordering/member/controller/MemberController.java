package com.encore.ordering.member.controller;

import com.encore.ordering.common.ResponseDTO;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.domain.Role;
import com.encore.ordering.member.dto.LoginRequest;
import com.encore.ordering.member.dto.MemberResponse;
import com.encore.ordering.member.dto.MemberSaveRequest;
import com.encore.ordering.member.service.MemberService;
import com.encore.ordering.securities.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ResponseDTO> save(@Valid @RequestBody MemberSaveRequest request) {
        System.out.println(request.getEmail());
        Member member =service.save(request);
        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.CREATED,"member successfully created.",member.getId()),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/doLogin")
    public ResponseEntity<ResponseDTO> signIn(@Valid @RequestBody LoginRequest request) {
        Member member =service.login(request);
        //json web token
        //토큰 생성 로직
        String jwt = jwtTokenProvider.createdToken(member.getEmail(), member.getRole().name());

        Map<String, Object> memberInfo = new HashMap<>();
        memberInfo.put("id", member.getId());
        memberInfo.put("token", jwt);
        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK,"member successfully logind.",memberInfo),
                HttpStatus.OK
        );
    }

    @GetMapping("/members")
    public List<MemberResponse> members() {
        return service.findAll();
    }

    @GetMapping("/member/myInfo")
    public MemberResponse findMyInfo() {
        return service.findMyInfo();
    }

    //@GetMapping("/member/{id}/orders")

    //@GetMapping("/member/myorders")

}
