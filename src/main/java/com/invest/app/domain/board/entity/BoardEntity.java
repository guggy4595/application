package com.invest.app.domain.board.entity;

import lombok.*;
import javax.persistence.*;
import com.invest.app.domain.BaseEntity;
import com.invest.app.domain.account.entity.AccountEntity;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boardTable")
public class BoardEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardNumber;

    @Column(name = "boardTitle")
    private String boardTitle;

    @Column(name = "boardContents")
    private String boardContents;

    @Column(name = "boardView")
    @ColumnDefault("0")
    private Integer boardView;

    @Column(name = "boardShowYn")
    @ColumnDefault("'Y'")
    private String boardShowYn;

    @ManyToOne
    @JoinColumn(name = "accountNumber")
    private AccountEntity accountEntity;
}
