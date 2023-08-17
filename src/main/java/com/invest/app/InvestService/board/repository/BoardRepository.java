package com.invest.app.InvestService.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import com.invest.app.domain.board.entity.BoardEntity;
import org.springframework.data.repository.query.Param;
import com.invest.app.domain.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {


    @Query(value = "select * from board_table where board_show_yn = 'Y'" , nativeQuery = true )
    Page<BoardEntity> findByBoard(Pageable pageable);
    @Query(value = "select * from board_table where board_show_yn = 'Y' and board_title like %:keyword%" , nativeQuery = true )
    Page<BoardEntity> findByboardTitle(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select * from board_table where board_show_yn = 'Y' and board_contents like %:keyword%" , nativeQuery = true )
    Page<BoardEntity> findByboardContents(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select * from board_table where board_show_yn = 'Y' and account_number = :#accountEntity.accountNumber", nativeQuery = true  )
    Page<BoardEntity> findByAccountNumber(@Param("memberEntity") AccountEntity accountEntity, Pageable pageable);
}
