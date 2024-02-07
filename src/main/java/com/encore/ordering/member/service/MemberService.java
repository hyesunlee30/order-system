package com.encore.ordering.member.service;

import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.LoginRequest;
import com.encore.ordering.member.dto.MemberResponse;
import com.encore.ordering.member.dto.MemberSaveRequest;
import com.encore.ordering.member.repository.MemberRepository;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.order.repository.OrderingRepository;
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
    private final OrderingRepository orderingRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository repository, OrderingRepository orderingRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.orderingRepository = orderingRepository;
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

    //memberOrders
    public List<OrderResDto> memberOrders(Long id){
        Member member = repository.findById(id).orElseThrow(()->new EntityNotFoundException("찾는 회원이 없습니다."));
        List<Ordering> orderings = orderingRepository.findAllByMember(member);
        return orderings.stream().map(OrderResDto::toDto).collect(Collectors.toList());
    }

    public List<OrderResDto> finMyOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = repository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("찾는 회원이 없습니다."));
        List<Ordering> orderings = orderingRepository.findAllByMember(member);
        return orderings.stream().map(OrderResDto::toDto).collect(Collectors.toList());
    }
}
