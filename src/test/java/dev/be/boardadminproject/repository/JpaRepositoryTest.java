package dev.be.boardadminproject.repository;

import dev.be.boardadminproject.domain.Member;
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

    @Autowired MemberRepository memberRepository;

    @DisplayName("회원 정보 select 테스트")
    @Test
    void givenMembers_whenSelecting_thenWorksFine() {
        List<Member> members = memberRepository.findAll();

        assertThat(members)
                .isNotNull()
                .hasSize(4);
    }

    @DisplayName("회원 정보 insert 테스트")
    @Test
    void givenMember_whenInserting_thenWorksFine() {
        long previousCount = memberRepository.count();
        Member member = Member.builder()
                .memberId("test")
                .password("pw")
                .roleTypes(Set.of(RoleType.DEVELOPER))
                .build();

        memberRepository.save(member);

        assertThat(memberRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("회원 정보 update 테스트")
    @Test
    void givenMemberAndRoleType_whenUpdating_thenWorksFine() {
        Member member = memberRepository.getReferenceById("yun");
        member.addRoleType(RoleType.MANAGER);
        member.addRoleTypes(List.of(RoleType.USER, RoleType.USER));
        member.removeRoleType(RoleType.ADMIN);

        Member updatedMember = memberRepository.saveAndFlush(member);

        assertThat(updatedMember)
                .hasFieldOrPropertyWithValue("memberId", "yun")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.MANAGER, RoleType.USER));
    }

    @DisplayName("회원 정보 delete 테스트")
    @Test
    void givenMember_whenDeleting_thenWorksFine() {
        long previousCount = memberRepository.count();
        Member member = memberRepository.getReferenceById("yun");

        memberRepository.delete(member);

        assertThat(memberRepository.count()).isEqualTo(previousCount - 1);
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
