package dev.be.boardadminproject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ArticleCommentDto implements Serializable {

    @Builder
    public record Dto(
            Long id,
            Long articleId,
            MemberDto.Dto member,
            Long parentCommentId,
            String content,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) { }

    public record ClientResponse(
            @JsonProperty("_embedded") Embedded embedded,
            @JsonProperty("page") Page page
    ) {

        public static ClientResponse empty() {
            return new ClientResponse(
                    new Embedded(List.of()),
                    new Page(1, 0, 1, 0)
            );
        }

        public static ClientResponse of(List<Dto> articleComments) {
            return new ClientResponse(
                    new Embedded(articleComments),
                    new Page(articleComments.size(), articleComments.size(), 1, 0)
            );
        }

        public record Embedded(List<Dto> articleComments) { }

        public record Page(
                int size,
                long totalElements,
                int totalPages,
                int number
        ) { }

    }

}
