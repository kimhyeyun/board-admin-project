package dev.be.boardadminproject.service;

import dev.be.boardadminproject.domain.AdminAccount;
import dev.be.boardadminproject.domain.constant.RoleType;
import dev.be.boardadminproject.dto.AdminAccountDto;
import dev.be.boardadminproject.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminAccountService {

    private final AdminAccountRepository adminAccountRepository;

    @Transactional(readOnly = true)
    public Optional<AdminAccountDto.Dto> searchMember(String username) {
        return adminAccountRepository.findById(username)
                .map(AdminAccountDto.Dto::from);
    }

    public AdminAccountDto.Dto saveMember(String username, String password, Set<RoleType> roleTypes, String email, String nickname, String memo) {
        return AdminAccountDto.Dto.from(
                adminAccountRepository.save(AdminAccount.builder()
                        .memberId(username)
                        .password(password)
                        .roleTypes(roleTypes)
                        .email(email)
                        .nickname(nickname)
                        .memo(memo)
                        .build()
                )
        );
    }

    @Transactional(readOnly = true)
    public List<AdminAccountDto.Dto> members() {
        return adminAccountRepository.findAll().stream()
                .map(AdminAccountDto.Dto::from)
                .toList();
    }

    public void deleteMember(String username) {
        adminAccountRepository.deleteById(username);

    }
}
