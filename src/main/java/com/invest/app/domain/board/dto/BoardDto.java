package com.invest.app.domain.board.dto;

import lombok.*;
import com.invest.app.domain.board.entity.BoardEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private long boardNumber;
    private String boardTitle;
    private String boardContents;
    private Integer boardView;
    private String boardShowYn;

    public BoardEntity toentity(){
        return BoardEntity.builder()
                .boardNumber( this.boardNumber )
                .boardTitle( this.boardTitle)
                .boardContents( this.boardContents)
                .boardView( this.boardView)
                .boardShowYn( this.boardShowYn)
                .build();
    }
}
