package dev.be.boardadminproject.repository;

import dev.be.boardadminproject.domain.AdminAccount;
import dev.be.boardadminproject.domain.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
public class JpaRepositoryTest {

    @Autowired
    AdminAccountRepository adminAccountRepository;

    @DisplayName("회원 정보 select 테스트")
    @Test
    void givenMembers_whenSelecting_thenWorksFine() {
        List<AdminAccount> adminAccounts = adminAccountRepository.findAll();

        assertThat(adminAccounts)
                .isNotNull()
                .hasSize(4);
    }

    @DisplayName("회원 정보 insert 테스트")
    @Test
    void givenMember_whenInserting_thenWorksFine() {
        long previousCount = adminAccountRepository.count();
        AdminAccount adminAccount = AdminAccount.builder()
                .memberId("test")
                .password("pw")
                .roleTypes(Set.of(RoleType.DEVELOPER))
                .build();

        adminAccountRepository.save(adminAccount);

        assertThat(adminAccountRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("회원 정보 update 테스트")
    @Test
    void givenMemberAndRoleType_whenUpdating_thenWorksFine() {
        AdminAccount adminAccount = adminAccountRepository.getReferenceById("yun");
        adminAccount.addRoleType(RoleType.MANAGER);
        adminAccount.addRoleTypes(List.of(RoleType.USER, RoleType.USER));
        adminAccount.removeRoleType(RoleType.ADMIN);

        AdminAccount updatedAdminAccount = adminAccountRepository.saveAndFlush(adminAccount);

        assertThat(updatedAdminAccount)
                .hasFieldOrPropertyWithValue("memberId", "yun")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.MANAGER, RoleType.USER));
    }

    @DisplayName("회원 정보 delete 테스트")
    @Test
    void givenMember_whenDeleting_thenWorksFine() {
        long previousCount = adminAccountRepository.count();
        AdminAccount adminAccount = adminAccountRepository.getReferenceById("yun");

        adminAccountRepository.delete(adminAccount);

        assertThat(adminAccountRepository.count()).isEqualTo(previousCount - 1);
    }



    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig {

        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("yun");
        }
    }
}
