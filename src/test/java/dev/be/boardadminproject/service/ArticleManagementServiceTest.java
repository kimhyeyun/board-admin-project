package dev.be.boardadminproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.be.boardadminproject.domain.constant.RoleType;
import dev.be.boardadminproject.dto.ArticleDto;
import dev.be.boardadminproject.dto.MemberDto;
import dev.be.boardadminproject.dto.properties.ProjectProperties;
import dev.be.boardadminproject.dto.response.ArticleClientResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@DisplayName("비즈니스 로직 - 게시글 관리")
class ArticleManagementServiceTest {

    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {

        @Autowired ArticleManagementService sut;

        @DisplayName("게시글 ID와 함께 게시글 API를 호출하면, 게시글을 가져온다.")
        @Test
        void given_when_then() {
            List<ArticleDto> result = sut.getArticles();

            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();
        }
    }


    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleManagementService.class)
    @Nested
    class RestTemplateTest {

        @Autowired ArticleManagementService sut;

        @Autowired ProjectProperties properties;
        @Autowired MockRestServiceServer server;
        @Autowired ObjectMapper mapper;

        @DisplayName("게시글 목록 API를 호출하면, 게시글들을 가져온다.")
        @Test
        void givenNothing_whenCallingArticlesApi_thenReturnsArticleList() throws JsonProcessingException {
            ArticleDto expectedArticle = createArticleDto("제목", "글");
            ArticleClientResponse expectedResponse = ArticleClientResponse.of(List.of(expectedArticle));

            server.expect(requestTo(properties.board().url() + "/api/articles?size=10000"))
                    .andRespond(MockRestResponseCreators.withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));

            List<ArticleDto> result = sut.getArticles();

            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedArticle.id())
                    .hasFieldOrPropertyWithValue("title", expectedArticle.title())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.content())
                    .hasFieldOrPropertyWithValue("member.nickname", expectedArticle.member().getNickname());

            server.verify();
        }

        @DisplayName("게시글 API를 호출하면, 게시글을 가져온다.")
        @Test
        void givenNothing_whenCallingArticleAPI_thenReturnsArticle() throws JsonProcessingException {
            Long articleId = 1L;
            ArticleDto expectedArticle = createArticleDto("게시글", "글");

            server.expect(requestTo(properties.board().url() + "/api/aritlces/"+articleId) )
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedArticle),
                            MediaType.APPLICATION_JSON
                    ));

            ArticleDto result = sut.getArticle(articleId);

            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedArticle.id())
                    .hasFieldOrPropertyWithValue("title", expectedArticle.title())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.content())
                    .hasFieldOrPropertyWithValue("member.nickname", expectedArticle.member().getNickname());

            server.verify();

        }

        @DisplayName("게시글 ID와 함께 게시글 삭제 API를 호출하면, 게시글을 삭제한다.")
        @Test
        void givenArticleId_whenCallingDeleteArticleAPI_thenDeletesArticle() {
            Long articleId = 1L;

            server
                    .expect(requestTo(properties.board().url() + "/api/articles/" + articleId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            sut.deleteArticle(articleId);

            server.verify();
        }

        private ArticleDto createArticleDto(String title, String content) {
            return ArticleDto.builder()
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

        private MemberDto createMemberDto() {
            return MemberDto.builder()
                    .memberId("yunTest")
                    .password("pw")
                    .roleTypes(Set.of(RoleType.ADMIN))
                    .email("yun-test@mail.com")
                    .nickname("yun-test")
                    .memo("test memo")
                    .build();
        }
    }

}