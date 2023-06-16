package dev.be.boardadminproject.domain;

import dev.be.boardadminproject.domain.constant.RoleType;
import dev.be.boardadminproject.domain.converter.RoleTypesConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity{

    @Id
    @Column(length = 50)
    private String memberId;

    @Column(nullable = false) private String password;

    @Convert(converter = RoleTypesConverter.class)
    @Column(nullable = false)
    private Set<RoleType> roleTypes = new LinkedHashSet<>();

    private String email;
    private String nickname;
    private String memo;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        return this.getMemberId() != null && this.getMemberId().equals(member.getMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getMemberId());
    }
}
