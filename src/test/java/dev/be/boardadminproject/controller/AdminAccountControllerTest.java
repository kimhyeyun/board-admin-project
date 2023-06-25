package dev.be.boardadminproject.controller;

import dev.be.boardadminproject.config.SecurityConfig;
import dev.be.boardadminproject.config.TestSecurityConfig;
import dev.be.boardadminproject.domain.constant.RoleType;
import dev.be.boardadminproject.dto.AdminAccountDto;
import dev.be.boardadminproject.service.AdminAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 회원 관리")
@Import(SecurityConfig.class)
@WebMvcTest(AdminAccountController.class)
class AdminAccountControllerTest {

    @Autowired MockMvc mvc;
    @MockBean private AdminAccountService adminAccountService;

    @BeforeTestMethod
    public void securitySetup() {
        given(adminAccountService.searchMember(anyString()))
                .willReturn(Optional.of(createAdminAccountDto()));
        given(adminAccountService.saveMember(anyString(), anyString(), anySet(), anyString(), anyString(), anyString()))
                .willReturn(createAdminAccountDto());
    }

    @WithMockUser(username = "tester",roles = "USER")
    @DisplayName("[view][GET] 어드민 회원 페이지 - 정상 호출")
    @Test
    void givenAuthorizedMember_whenRequestingAdminMembersView_thenReturnsAdminMembersView() throws Exception {
        mvc.perform(get("/management/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/members"));
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[data][GET] 어드민 회원 리스트 - 정상 호출")
    @Test
    void givenAuthorizedMember_whenRequestingAdminMembers_thenReturnsAdminMembers() throws Exception {
        given(adminAccountService.members()).willReturn(List.of());

        mvc.perform(get("/api/admin/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        then(adminAccountService).should().members();
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @DisplayName("[data][DELETE] 어드민 회원 삭제 - 정상 호출")
    @Test
    void givenAuthorizedMember_whenDeletingAdminMember_thenDeletesAdminMember() throws Exception {
        String username = "yun";

        willDoNothing().given(adminAccountService).deleteMember(username);

        mvc.perform(
                        delete("/api/admin/members/" + username)
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        then(adminAccountService).should().deleteMember(username);
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