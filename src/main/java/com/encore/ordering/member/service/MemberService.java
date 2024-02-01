package com.encore.ordering.member.service;

import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.LoginRequest;
import com.encore.ordering.member.dto.MemberResponse;
import com.encore.ordering.member.dto.MemberSaveRequest;
import com.encore.ordering.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member save(MemberSaveRequest request) throws IllegalArgumentException{
        if (repository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 가입되어 있는 이메일입니다.");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));


        return repository.save(Member.toEntity(request));
    }



    public Member login(LoginRequest request) {
        Member member = repository.findByEmail(request.getEmail()).orElseThrow(
                ()->new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        return member;
    }

    public MemberResponse findMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = repository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return MemberResponse.toMemberResponse(member);
    }
    public List<MemberResponse> findAll(){
        List<Member> members= repository.findAll();
                return members.stream().map(m->MemberResponse.toMemberResponse(m)).collect(Collectors.toList());

    }
}
