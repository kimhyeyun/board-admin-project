package dev.be.boardadminproject.controller;

import dev.be.boardadminproject.config.SecurityConfig;
import dev.be.boardadminproject.dto.ArticleCommentDto;
import dev.be.boardadminproject.dto.MemberDto;
import dev.be.boardadminproject.service.ArticleCommentManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 - 댓글 관리")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleCommentManagementController.class)
class ArticleCommentManagementControllerTest {

    @Autowired MockMvc mvc;
    @MockBean private ArticleCommentManagementService articleCommentManagementService;

    @DisplayName("[view][GET] 댓글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleCommentManagementView_thenReturnsArticleCommentManagementView() throws Exception {
        given(articleCommentManagementService.getArticleComments()).willReturn(List.of());

        mvc.perform(get("/management/article-comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/article-comments"))
                .andExpect(model().attribute("comments", List.of()));

        then(articleCommentManagementService).should().getArticleComments();
    }

    @DisplayName("[data][GET] 댓글 1개 - 정상 호출")
    @Test
    void givenCommentId_whenRequeestingArticleComment_thenReturnsArticleComment() throws Exception {
        Long articleCommentId = 1L;
        ArticleCommentDto.Dto articleCommentDto = createArticleCommentDto("comment");

        given(articleCommentManagementService.getArticleComment(articleCommentId)).willReturn(articleCommentDto);

        mvc.perform(get("/management/article-comments/" + articleCommentId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(articleCommentId))
                .andExpect(jsonPath("$.content").value(articleCommentDto.content()))
                .andExpect(jsonPath("$.member.nickname").value(articleCommentDto.member().nickname()));

        then(articleCommentManagementService).should().getArticleComment(articleCommentId);
    }

    @DisplayName("[view][POST] 댓글 삭제 - 정상 호출")
    @Test
    void givenCommentId_whenRequestingDeletion_thenRedirectsToArticleCommentManagementView() throws Exception {
        Long articleCommentId = 1L;

        willDoNothing().given(articleCommentManagementService).deleteArticleComment(articleCommentId);

        mvc.perform(
                        post("/management/article-comments/" + articleCommentId)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/article-comments"))
                .andExpect(redirectedUrl("/management/article-comments"));

        then(articleCommentManagementService).should().deleteArticleComment(articleCommentId);
    }

    private ArticleCommentDto.Dto createArticleCommentDto(String content) {
        return ArticleCommentDto.Dto.builder()
                .articleId(1L)
                .id(1L)
                .member(createMemberDto())
                .parentCommentId(null)
                .content(content)
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