package dev.be.boardadminproject.config;

import dev.be.boardadminproject.domain.constant.RoleType;
import dev.be.boardadminproject.dto.AdminAccountDto;
import dev.be.boardadminproject.service.AdminAccountService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@Import(SecurityConfig.class)
@TestConfiguration
public class TestSecurityConfig {

    @MockBean public AdminAccountService adminAccountService;

    @BeforeTestMethod
    public void securitySetup() {
        given(adminAccountService.searchMember(anyString()))
                .willReturn(Optional.of(createAdminAccountDto()));

        given(adminAccountService.saveMember(anyString(), anyString(), anySet(), anyString(), anyString(), anyString()))
                .willReturn(createAdminAccountDto());
    }

    private AdminAccountDto.Dto createAdminAccountDto() {
        return AdminAccountDto.Dto.builder()
                .memberId("yunTest")
                .password("pw")
                .roleTypes(Set.of(RoleType.USER))
                .email("yun-test@mail.com")
                .nickname("yun-test")
                .memo("test memo")
                .build();
    }
}
