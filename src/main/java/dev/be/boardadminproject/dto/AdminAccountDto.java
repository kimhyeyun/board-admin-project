package dev.be.boardadminproject.dto;

import dev.be.boardadminproject.domain.AdminAccount;
import dev.be.boardadminproject.domain.constant.RoleType;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminAccountDto implements Serializable {

    @Builder
    public record Dto(
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

        public static AdminAccountDto.Dto from(AdminAccount entity) {
            return AdminAccountDto.Dto.builder()
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

    @Builder
    public record Response(
            String memberId,
            String roleTypes,
            String email,
            String nickname,
            String memo,
            LocalDateTime createdAt,
            String createdBy
    ) {

        public static Response from(Dto dto) {
            return Response.builder()
                    .memberId(dto.memberId)
                    .roleTypes(dto.roleTypes.stream()
                            .map(RoleType::getDescription)
                            .collect(Collectors.joining(", ")))
                    .email(dto.email)
                    .nickname(dto.nickname)
                    .memo(dto.memo)
                    .createdAt(dto.createdAt)
                    .createdBy(dto.createdBy)
                    .build();
        }

    }

}
