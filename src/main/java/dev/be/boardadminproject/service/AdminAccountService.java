package dev.be.boardadminproject.service;

import dev.be.boardadminproject.domain.constant.RoleType;
import dev.be.boardadminproject.dto.AdminAccountDto;
import dev.be.boardadminproject.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AdminAccountService {

    private final AdminAccountRepository adminAccountRepository;

    public Optional<AdminAccountDto> searchMember(String username) {
        return Optional.empty();
    }

    public AdminAccountDto saveMember(String username, String password, Set<RoleType> roleTypes, String email, String nickname, String memo) {
        return null;
    }

    public List<AdminAccountDto> members() {
        return List.of();
    }

    public void deleteMember(String username) {

    }
}
