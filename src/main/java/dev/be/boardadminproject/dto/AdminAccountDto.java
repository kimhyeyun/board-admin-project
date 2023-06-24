package dev.be.boardadminproject.dto;

import dev.be.boardadminproject.domain.AdminAccount;
import dev.be.boardadminproject.domain.constant.RoleType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record AdminAccountDto(
        String memberId,
        String password,
        Set<RoleType> roleTypes,
        String email,
        String nickname,
        String memo,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static AdminAccountDto from(AdminAccount entity) {
        return AdminAccountDto.builder()
                .memberId(entity.getMemberId())
                .password(entity.getPassword())
                .roleTypes(entity.getRoleTypes())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .memo(entity.getMemo())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }

    public AdminAccount toEntity() {
        return AdminAccount.builder()
                .memberId(memberId)
                .password(password)
                .roleTypes(roleTypes)
                .email(email)
                .nickname(nickname)
                .memo(memo)
                .build();
    }
}
