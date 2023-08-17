package com.invest.app.InvestService.account.repository;

import org.springframework.stereotype.Repository;
import com.invest.app.domain.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity , Long> {

    AccountEntity findByaccountId(String id);
}
