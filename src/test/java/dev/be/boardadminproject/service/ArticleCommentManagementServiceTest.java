package dev.be.boardadminproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.be.boardadminproject.dto.ArticleCommentDto;
import dev.be.boardadminproject.dto.MemberDto;
import dev.be.boardadminproject.dto.properties.ProjectProperties;
import org.junit.jupiter.api.Disabled;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@DisplayName("비즈니스 로직 - 댓글 관리")
class ArticleCommentManagementServiceTest {

    @Disabled("실제 API 호출 결과 관찰용이므로 평상시엔 비활성화")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {

        @Autowired ArticleCommentManagementService sut;

        @DisplayName("댓글 API를 호출하면, 댓글을 가져온다.")
        @Test
        void givenNothing_whenCallingCommentApi_thenReturnsCommentsList() {
            List<ArticleCommentDto.Dto> result = sut.getArticleComments();

            System.out.println(result.stream().findFirst());

            assertThat(result).isNotNull();
        }
    }

    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleCommentManagementService.class)
    @Nested
    class RestTemplateTest {

        @Autowired ArticleCommentManagementService sut;
        @Autowired ProjectProperties properties;
        @Autowired MockRestServiceServer server;
        @Autowired ObjectMapper mapper;


        @DisplayName("댓글 목록 API를 호출하면, 댓글들을 가져온다.")
        @Test
        void givenNothing_whenCallingCommentsAPI_thenReturnsCommentsList() throws JsonProcessingException {
            ArticleCommentDto.Dto expectedComment = createArticleCommentDto("댓글");
            ArticleCommentDto.ClientResponse expectedResponse = ArticleCommentDto.ClientResponse.of(List.of(expectedComment));

            server.expect(requestTo(properties.board().url() + "/api/articleComments?size=10000"))
                    .andRespond(MockRestResponseCreators.withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));

            List<ArticleCommentDto.Dto> result = sut.getArticleComments();

            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedComment.id())
                    .hasFieldOrPropertyWithValue("content", expectedComment.content())
                    .hasFieldOrPropertyWithValue("member.nickname", expectedComment.member().nickname());

            server.verify();
        }

        @DisplayName("댓글 ID와 함께 댓글 API를 호출하면, 댓글을 가져온다.")
        @Test
        void givenCommentId_whenCallingCommentAPI_thenReturnsComment() throws JsonProcessingException {
            Long articleCommentId = 1L;
            ArticleCommentDto.Dto expectedComment = createArticleCommentDto("댓글");

            server.expect(requestTo(properties.board().url() + "/api/articleComments/" + articleCommentId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedComment),
                            MediaType.APPLICATION_JSON
                    ));

            ArticleCommentDto.Dto result = sut.getArticleComment(articleCommentId);

            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedComment.id())
                    .hasFieldOrPropertyWithValue("content", expectedComment.content())
                    .hasFieldOrPropertyWithValue("member.nickname", expectedComment.member().nickname());

            server.verify();
        }

        @DisplayName("댓글 ID와 함께 댓글 삭제 API를 호출하면, 댓글을 삭제한다.")
        @Test
        void givenCommentId_whenCallingDeleteCommentAPI_thenDeletesComment() {
            Long articleCommentId = 1L;

            server.expect(requestTo(properties.board().url()+"/api/articleComments/"+articleCommentId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            sut.deleteArticleComment(articleCommentId);

            server.verify();
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

}