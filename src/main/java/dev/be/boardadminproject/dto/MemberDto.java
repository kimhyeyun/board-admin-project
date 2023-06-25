package dev.be.boardadminproject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class MemberDto implements Serializable {
    @Builder
    public record Dto(
            String memberId,
            String email,
            String nickname,
            String memo,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {}

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

        public static ClientResponse of(List<Dto> members) {
            return new ClientResponse(
                    new Embedded(members),
                    new Page(members.size(), members.size(), 1, 0)
            );
        }

        public List<Dto> members() {
            return this.embedded().members();
        }

        public record Embedded(List<Dto> members) {}
        public record Page(
                int size,
                long totalElements,
                int totalPages,
                int number
        ){}
    }

    @Builder
    public record Response(
            String memberId,
            String email,
            String nickname,
            String memo,
            LocalDateTime createdAt,
            String createdBy
    ) {

        public static Response from(Dto dto) {
            return Response.builder()
                    .memberId(dto.memberId)
                    .email(dto.email)
                    .nickname(dto.nickname)
                    .memo(dto.memo)
                    .createdAt(dto.createdAt)
                    .createdBy(dto.createdBy)
                    .build();
        }

    }


}