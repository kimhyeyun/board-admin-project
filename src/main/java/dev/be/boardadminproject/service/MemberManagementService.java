package dev.be.boardadminproject.service;

import dev.be.boardadminproject.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberManagementService {

    public List<MemberDto.Dto> getMembers() {
        return List.of();
    }

    public MemberDto.Dto getMember(String memberId) {
        return null;
    }

    public void deleteMember(String memberId) {

    }
}
