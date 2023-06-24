package dev.be.boardadminproject.dto;

import dev.be.boardadminproject.domain.AdminAccount;
import dev.be.boardadminproject.domain.constant.RoleType;
import lombok.*;
import java.util.*;

import java.time.LocalDateTime;

@Builder
public record MemberDto(
        String memberId,
        Set<RoleType> roleTypes,
        String email,
        String nickname,
        String memo,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

}

