package dev.be.boardadminproject.dto;

import dev.be.boardadminproject.domain.Member;
import dev.be.boardadminproject.domain.constant.RoleType;
import lombok.*;
import java.util.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private String memberId;
    private String password;
    private Set<RoleType> roleTypes;
    private String email;
    private String nickname;
    private String memo;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;


    public static MemberDto from(Member entity) {

        return MemberDto.builder()
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

    public Member toEntity() {
        return Member.builder()
                .memberId(memberId)
                .password(password)
                .roleTypes(roleTypes)
                .email(email)
                .nickname(nickname)
                .memo(memo)
                .build();
    }

}
