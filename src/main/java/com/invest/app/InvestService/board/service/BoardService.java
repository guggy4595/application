package com.invest.app.InvestService.board.service;


import com.invest.app.InvestService.account.repository.AccountRepository;
import com.invest.app.InvestService.account.service.AccountService;
import com.invest.app.InvestService.board.repository.BoardRepository;
import com.invest.app.domain.account.entity.AccountEntity;
import com.invest.app.domain.board.dto.BoardDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.invest.app.domain.board.entity.BoardEntity;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    private HttpServletRequest request;

    @Transactional
    public boolean savetoBoard(BoardDto dto) {
        if(accountService.isLogin()) {
            AccountEntity accountEntity = accountRepository.findByaccountId(accountService.getUserId());
            BoardEntity boardEntity =  boardRepository.save(dto.toentity());
            boardEntity.setAccountEntity(accountEntity);
            boardEntity.setBoardShowYn("Y");
            boardEntity.setBoardView(0);
            return true;
        } else {
            return false;
        }
    }

    public JSONObject getboardlist(String key , String keyword , int page  ){

        JSONObject object = new JSONObject();
        Page<BoardEntity> boardEntities = null;
        Pageable pageable = PageRequest.of(page,3, Sort.by( Sort.Direction.DESC, "board_number"));

        if(StringUtils.equals(key,"boardTitle")){
            boardEntities = boardRepository.findByboardTitle(keyword,pageable);
        }else if(StringUtils.equals(key,"boardContents")){
            boardEntities = boardRepository.findByboardContents(keyword,pageable);
        }else if(StringUtils.equals(key,"accountId")){
            AccountEntity accountEntity = accountRepository.findByaccountId(keyword);
            if(accountEntity != null) {
                boardEntities = boardRepository.findByAccountNumber(accountEntity,pageable);
            } else {return object;}
        }else{
            boardEntities = boardRepository.findByBoard(pageable);
        }

        int btncount = 5;                                   //버튼개수
        int startbtn  = ( page / btncount ) * btncount + 1; //(현재페이지 / 표시할버튼수 ) * 표시할버튼수 +1
        int endhtn = startbtn + btncount -1;                //끝 번호버튼의 번호 [  시작버튼 + 표시할버튼수-1 ]
        if( endhtn > boardEntities.getTotalPages()){
            endhtn = boardEntities.getTotalPages();         // 만약에 끝번호가 마지막페이지보다 크면 끝번호는 마지막페이지 번호로 사용
        }

        JSONArray jsonArray = new JSONArray();
        for( BoardEntity entity : boardEntities ){

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("boardNumber"    , entity.getBoardNumber());
            jsonObject.put("boardTitle"     , entity.getBoardTitle());
            jsonObject.put("boardView"      , entity.getBoardView());
            jsonObject.put("boardData"      , entity.getCreatedate());
            jsonObject.put("accountId"      , entity.getAccountEntity().getAccountId());
            jsonArray.put(jsonObject);
        }
        object.put("startbtn"   , startbtn );                       //시작 버튼
        object.put("endhtn"     , endhtn );                         //끝 버튼
        object.put("totalpages" , boardEntities.getTotalPages() );  //전체 페이지 수
        object.put("data"       , jsonArray );                      //리스트를 추가

        return object;
    }

    @Transactional
    public JSONObject getboard(long board_number){

        String ip = request.getRemoteAddr();

        Optional<BoardEntity> optional =  boardRepository.findById(board_number);
        BoardEntity entity = optional.get();

        Object com =  request.getSession().getAttribute(ip+board_number);
        if( com == null  ){
            request.getSession().setAttribute(ip+board_number, 1 );
            request.getSession().setMaxInactiveInterval( 60*60*24  );
            entity.setBoardView(entity.getBoardView()+1);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("boardNumber"    , entity.getBoardNumber());
        jsonObject.put("boardTitle"     , entity.getBoardTitle());
        jsonObject.put("boardContents"  , entity.getBoardContents());
        jsonObject.put("boardView"      , entity.getBoardView());
        jsonObject.put("boardData"      , entity.getCreatedate());
        jsonObject.put("accountId"      , entity.getAccountEntity().getAccountId());

        if(accountService.isLogin()) {
            if(StringUtils.equals(accountService.getUserId(),entity.getAccountEntity().getAccountId())) {
                jsonObject.put("showYn","Y");
            }
        }
        return jsonObject;
    }

    @Transactional
    public int update(BoardDto boardDto){
        Optional<BoardEntity> optionalBoard
                =  boardRepository.findById(boardDto.getBoardNumber());
        BoardEntity boardEntity =  optionalBoard.get();

        if(accountService.isLogin()) {
            if(!StringUtils.equals(accountService.getUserId(),boardEntity.getAccountEntity().getAccountId())) {
                return 9999;
            }
        }

        boardEntity.setBoardTitle(boardDto.getBoardTitle());
        boardEntity.setBoardContents(boardDto.getBoardTitle());
        return 0000;
    }
    @Transactional
    public int delete( long board_number ){
        Optional<BoardEntity> optional =  boardRepository.findById(board_number);
        BoardEntity entity = optional.get();

        if(accountService.isLogin()) {
            if(!StringUtils.equals(accountService.getUserId(),entity.getAccountEntity().getAccountId())) {
                return 9999;
            }
        }
        BoardEntity boardEntity = boardRepository.findById(board_number).get();
        boardRepository.delete(boardEntity);
        return 0000;
    }
}
