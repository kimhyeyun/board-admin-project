package dev.be.boardadminproject.service;

import dev.be.boardadminproject.domain.AdminAccount;
import dev.be.boardadminproject.domain.constant.RoleType;
import dev.be.boardadminproject.dto.AdminAccountDto;
import dev.be.boardadminproject.repository.AdminAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 어드민 회원")
@ExtendWith(MockitoExtension.class)
class AdminAccountServiceTest {

    @InjectMocks private AdminAccountService sut;
    @Mock private AdminAccountRepository adminAccountRepository;


    @DisplayName("존재하는 회원 ID를 검색하면, 회원 데이터를 Optional로 반환한다.")
    @Test
    void givenExistedMemberId_whenSearching_thenReturnsOptionalMemberData() {
        String username = "yun";

        given(adminAccountRepository.findById(username)).willReturn(Optional.of(createAdminAccount(username)));

        Optional<AdminAccountDto.Dto> result = sut.searchMember(username);

        assertThat(result).isPresent();

        then(adminAccountRepository).should().findById(username);
    }

    @DisplayName("존재하지 않는 회원 ID를 검색하면, 비어있는 Optional을 반환한다.")
    @Test
    void givenNonExistentMemberId_whenSearching_thenReturnsOptionalMemberData() {
        String username = "wrong-user";

        given(adminAccountRepository.findById(username)).willReturn(Optional.empty());

        Optional<AdminAccountDto.Dto> result = sut.searchMember(username);

        assertThat(result).isEmpty();
        then(adminAccountRepository).should().findById(username);
    }

    @DisplayName("회원 정보를 입력하면, 새로운 회원 정보를 저장하여 가입시키고 해당 회원 데이터를 리턴한다.")
    @Test
    void givenMemberParams_whenSaving_thenSavesAdminAccount() {
        AdminAccount adminAccount = createSignUpAdminAccount("yun", Set.of(RoleType.USER));

        given(adminAccountRepository.save(adminAccount)).willReturn(adminAccount);

        AdminAccountDto.Dto result = sut.saveMember(
                adminAccount.getMemberId(),
                adminAccount.getPassword(),
                adminAccount.getRoleTypes(),
                adminAccount.getEmail(),
                adminAccount.getNickname(),
                adminAccount.getMemo()
        );

        assertThat(result)
                .hasFieldOrPropertyWithValue("memberId", adminAccount.getMemberId())
                .hasFieldOrPropertyWithValue("password", adminAccount.getPassword())
                .hasFieldOrPropertyWithValue("roleTypes", adminAccount.getRoleTypes())
                .hasFieldOrPropertyWithValue("email", adminAccount.getEmail())
                .hasFieldOrPropertyWithValue("nickname", adminAccount.getNickname())
                .hasFieldOrPropertyWithValue("memo", adminAccount.getMemo())
                .hasFieldOrPropertyWithValue("createdBy", adminAccount.getMemberId())
                .hasFieldOrPropertyWithValue("modifiedBy", adminAccount.getModifiedBy());

        then(adminAccountRepository).should().save(adminAccount);
    }

    @DisplayName("전체 어드민 회원을 조회한다.")
    @Test
    void givenNothing_whenSelectingAdminAccounts_thenReturnsAllAdminAccounts() {
        given(adminAccountRepository.findAll()).willReturn(List.of());

        List<AdminAccountDto.Dto> result = sut.members();

        assertThat(result).hasSize(0);
        then(adminAccountRepository).should().findAll();
    }

    @DisplayName("회원 ID를 입력하면, 회원을 삭제한다.")
    @Test
    void givenMemberId_whenDeleting_thenDeletesAdminAccount() {
        String memberId = "yun";

        willDoNothing().given(adminAccountRepository).deleteById(memberId);

        sut.deleteMember(memberId);

        then(adminAccountRepository).should().deleteById(memberId);
    }

    private AdminAccount createSignUpAdminAccount(String username, Set<RoleType> roleTypes) {
        return createAdminAccount(username, roleTypes, username);
    }

    private AdminAccount createAdminAccount(String username) {
        return createAdminAccount(username, Set.of(RoleType.USER), null);
    }

    private AdminAccount createAdminAccount(String username, Set<RoleType> roleTypes, String createdBy) {
        return AdminAccount.builder()
                .memberId(username)
                .password("password")
                .roleTypes(roleTypes)
                .email("e@mail.com")
                .nickname("nickname")
                .memo("memo")
                .createdBy(createdBy)
                .build();
    }
}