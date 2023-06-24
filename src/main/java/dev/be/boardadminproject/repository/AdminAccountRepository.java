package dev.be.boardadminproject.repository;

import dev.be.boardadminproject.domain.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAccountRepository extends JpaRepository<AdminAccount, String > {
}
