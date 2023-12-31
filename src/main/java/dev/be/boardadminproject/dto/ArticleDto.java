package dev.be.boardadminproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class ArticleDto implements Serializable {

    @Builder
    public record Dto(
            Long id,
            MemberDto.Dto member,
            String title,
            String content,
            Set<String> hashtags,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {

    }

    public record ClientResponse (
            @JsonProperty("_embedded") Embedded embedded,
            @JsonProperty("page") Page page
    ){

        public static ClientResponse empty() {
            return new ClientResponse(
                    new Embedded(List.of()),
                    new Page(1, 0, 1, 0)
            );
        }

        public static ClientResponse of(List<Dto> articles) {
            return new ClientResponse(
                    new ClientResponse.Embedded(articles),
                    new ClientResponse.Page(articles.size(), articles.size(), 1, 0)
            );
        }

        public List<Dto> articles() {
            return this.embedded().articles();
        }

        public record Embedded(List<Dto> articles) {}
        public record Page(
                int size,
                long totalElements,
                int totalPages,
                int number
        ){}
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Response(
            Long id,
            MemberDto.Dto member,
            String title,
            String content,
            LocalDateTime createdAt
    ) {

        public static Response withContent(Dto dto) {
            return Response.builder()
                    .id(dto.id)
                    .member(dto.member)
                    .title(dto.title)
                    .content(dto.content)
                    .createdAt(dto.createdAt)
                    .build();
        }

        public static Response withoutContent(Dto dto) {
            return Response.builder()
                    .id(dto.id)
                    .member(dto.member)
                    .title(dto.title)
                    .content(null)
                    .createdAt(dto.createdAt)
                    .build();
        }
    }

}

