package dev.be.boardadminproject.controller;

import dev.be.boardadminproject.config.SecurityConfig;
import dev.be.boardadminproject.config.TestSecurityConfig;
import dev.be.boardadminproject.dto.ArticleDto;
import dev.be.boardadminproject.dto.MemberDto;
import dev.be.boardadminproject.service.ArticleManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 - 게시글 관리")
@Import(TestSecurityConfig.class)
@WebMvcTest(ArticleManagementController.class)
class ArticleManagementControllerTest {

    @Autowired MockMvc mvc;
    @MockBean ArticleManagementService articleManagementService;

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[view][GET] 게시글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleManagementView_thenReturnsArticleManagementView() throws Exception {
        given(articleManagementService.getArticles()).willReturn(List.of());

        mvc.perform(get("/management/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/articles"))
                .andExpect(model().attribute("articles", List.of()));

        then(articleManagementService).should().getArticles();
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[data][GET] 게시글 1개 - 정상 호출")
    @Test
    void givenArticleId_whenRequestingArticle_thenReturnsArticle() throws Exception {
        Long articleId = 1L;
        ArticleDto.Dto articleDto = createArticleDto("title", "content");

        given(articleManagementService.getArticle(articleId)).willReturn(articleDto);

        mvc.perform(get("/management/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(articleId))
                .andExpect(jsonPath("$.title").value(articleDto.title()))
                .andExpect(jsonPath("$.content").value(articleDto.content()))
                .andExpect(jsonPath("$.member.nickname").value(articleDto.member().nickname()));

        then(articleManagementService).should().getArticle(articleId);
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @DisplayName("[view][POST] 게시글 삭제 - 정상 호출")
    @Test
    void givenArticleId_whenRequestingDeletion_thenRedirectsToArticleManagementView() throws Exception {
        Long articleId = 1L;

        willDoNothing().given(articleManagementService).deleteArticle(articleId);

        mvc.perform(
                        post("/management/articles/" + articleId)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/articles"))
                .andExpect(redirectedUrl("/management/articles"));

        then(articleManagementService).should().deleteArticle(articleId);
    }

    private ArticleDto.Dto createArticleDto(String title, String content) {
        return ArticleDto.Dto.builder()
                .id(1L)
                .member(createMemberDto())
                .title(title)
                .content(content)
                .hashtags(null)
                .createdAt(LocalDateTime.now())
                .createdBy("Yun")
                .modifiedAt(LocalDateTime.now())
                .modifiedBy("Yun")
                .build();
    }

    private MemberDto.Dto createMemberDto() {
        return MemberDto.Dto.builder()
                .memberId("yunTest")
                .email("yun-test@mail.com")
                .nickname("yun-test")
                .memo("test memo")
                .build();
    }
}