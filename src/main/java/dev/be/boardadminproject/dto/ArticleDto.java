package dev.be.boardadminproject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.be.boardadminproject.dto.response.ArticleClientResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class ArticleDto implements Serializable {

    @Builder
    public record Dto(
            Long id,
            MemberDto member,
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
}

