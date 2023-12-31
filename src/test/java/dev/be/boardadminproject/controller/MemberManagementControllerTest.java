package dev.be.boardadminproject.controller;

import dev.be.boardadminproject.config.GlobalControllerConfig;
import dev.be.boardadminproject.config.TestSecurityConfig;
import dev.be.boardadminproject.dto.MemberDto;
import dev.be.boardadminproject.service.MemberManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 - 어드민 회원")
@Import({TestSecurityConfig.class, GlobalControllerConfig.class})
@WebMvcTest(MemberManagementController.class)
class MemberManagementControllerTest {

    @Autowired MockMvc mvc;
    @MockBean MemberManagementService memberManagementService;


    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[view][GET] 어드민 회원 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingAdminMembersView_thenReturnsAdminMembersView() throws Exception {
        given(memberManagementService.getMembers()).willReturn(List.of());

        mvc.perform(get("/management/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/members"))
                .andExpect(model().attribute("members", List.of()));

        then(memberManagementService).should().getMembers();
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[data][GET] 회원 1개 - 정상 호출")
    @Test
    void givenMemberId_whenRequestingMember_thenReturnsMember() throws Exception {
        String memberId = "yun";
        MemberDto.Dto memberDto = createMemberDto(memberId, "Yun");

        given(memberManagementService.getMember(memberId)).willReturn(memberDto);

        mvc.perform(get("/management/members/" + memberId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.nickname").value(memberDto.nickname()));

        then(memberManagementService).should().getMember(memberId);
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @DisplayName("[view][POST] 회원 삭제 - 정상 호출")
    @Test
    void givenMemberId_whenRequestingDeletion_thenRedirectsToMemberManagementView() throws Exception {
        String memberId = "yun";

        willDoNothing().given(memberManagementService).deleteMember(memberId);

        mvc.perform(
                        post("/management/members/" + memberId)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/members"))
                .andExpect(redirectedUrl("/management/members"));

        then(memberManagementService).should().deleteMember(memberId);

    }

    private MemberDto.Dto createMemberDto(String memberId, String nickname) {
        return MemberDto.Dto.builder()
                .memberId(memberId)
                .email("yun-test@mail.com")
                .nickname(nickname)
                .memo("test memo")
                .build();
    }
}