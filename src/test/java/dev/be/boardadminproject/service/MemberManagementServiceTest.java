package dev.be.boardadminproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.be.boardadminproject.dto.MemberDto;
import dev.be.boardadminproject.dto.properties.ProjectProperties;
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

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@DisplayName("비즈니스 로직 - 회원 관리")
class MemberManagementServiceTest {

    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {

        @Autowired MemberManagementService sut;

        @DisplayName("회원 API를 호출하면, 회원 정보를 가져온다.")
        @Test
        void givenNothing_whenCallingMemberAPI_thenReturnsMemberList() {
            List<MemberDto.Dto> result = sut.getMembers();

            System.out.println(result.stream().findFirst());

            assertThat(result).isNotNull();
        }
    }

    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(MemberManagementService.class)
    @Nested
    class RestTemplateTest {

        @Autowired MemberManagementService sut;
        @Autowired ProjectProperties properties;
        @Autowired MockRestServiceServer server;
        @Autowired ObjectMapper mapper;


        @DisplayName("회원 목록 API를 호출하면, 회원들을 가져온다.")
        @Test
        void givenNothing_whenCallingMembersAPI_thenReturnsMemberList() throws JsonProcessingException {
            MemberDto.Dto expectedMember = createMemberDto("yun", "Yun");
            MemberDto.ClientResponse expectedResponse = MemberDto.ClientResponse.of(List.of(expectedMember));

            server.expect(requestTo(properties.board().url() + "/api/members?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));

            List<MemberDto.Dto> result = sut.getMembers();

            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("memberId", expectedMember.memberId())
                    .hasFieldOrPropertyWithValue("nickname", expectedMember.nickname());

            server.verify();
        }

        @DisplayName("회원 ID와 함께 회원 API를 호출하면, 회원을 가져온다.")
        @Test
        void givenMemberId_whenCallingMemberAPI_thenReturnsMember() throws JsonProcessingException {
            String memberId = "yun";
            MemberDto.Dto expectedMember = createMemberDto(memberId, "Yun");

            server.expect(requestTo(properties.board().url() +"/api/members/"+memberId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedMember),
                            MediaType.APPLICATION_JSON
                    ));

            MemberDto.Dto result = sut.getMember(memberId);

            assertThat(result)
                    .hasFieldOrPropertyWithValue("memberId", expectedMember.memberId())
                    .hasFieldOrPropertyWithValue("nickname", expectedMember.nickname());

            server.verify();
        }

        @DisplayName("회원 ID와 함께 회원 삭제 API를 호출하면, 회원을 삭제한다.")
        @Test
        void givenMemberId_whenCallingDeleteMemberAPI_thenDeletesMember() {
            String memberId = "yun";
            server.expect(requestTo(properties.board().url() + "/api/members/" + memberId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            sut.deleteMember(memberId);

            server.verify();
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



}