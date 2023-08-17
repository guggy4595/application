package com.invest.app.domain.account.entity;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.invest.app.domain.BaseEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import com.invest.app.utilty.constant.AccountRole;
import com.invest.app.domain.board.entity.BoardEntity;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Table(name="accountInfo")
public class AccountEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountNumber;

    @Column
    @NonNull
    private String accountId;

    @Column
    @NonNull
    private String accountName;

    @Column
    @NonNull
    private String accountEmail;

    @Column
    @NonNull
    private String accountPassword;

    @Column(length = 1)
    @ColumnDefault("'Y'")
    private String accountUseYn;

    @Enumerated(value = EnumType.STRING )
    private AccountRole accountRole;

    @Builder.Default    // 빌더 사용시 초기값 설정
    @OneToMany( mappedBy ="accountEntity" , cascade = CascadeType.ALL)  // 1:M
    List<BoardEntity> boardEntityList = new ArrayList<>();
}
