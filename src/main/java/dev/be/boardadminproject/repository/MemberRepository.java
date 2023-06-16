package dev.be.boardadminproject.repository;

import dev.be.boardadminproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String > {
}
