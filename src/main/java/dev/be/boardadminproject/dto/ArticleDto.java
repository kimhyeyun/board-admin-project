package dev.be.boardadminproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ArticleDto (
        Long id,
        MemberDto member,
        String title,
        String content,
        Set<String> hashtags,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
){

}

