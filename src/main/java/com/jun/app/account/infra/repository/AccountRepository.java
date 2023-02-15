package com.jun.app.account.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.jun.app.modules.account.domain.entity.Account;

@Transactional(readOnly = true) //
public interface AccountRepository extends JpaRepository<Account, Long> { 

    boolean existsByEmail(String email); // (3)

    boolean existsByNickname(String nickname); // (4)

    Account findByEmail(String email);

	Account findByNickname(String username);
}