package com.invest.app.InvestService.board.controller;

import com.invest.app.domain.board.dto.BoardDto;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import com.invest.app.InvestService.board.service.BoardService;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping(value = "/main")
    public String boardMain() {
        return "board/board.html";
    }

    @GetMapping(value = "/write")
    public String boardWrite() {
        return "board/write.html";
    }

    @GetMapping("/view/{board_number}")
    public String view(@PathVariable("board_number") long board_number ) {
        request.getSession().setAttribute("board_number",board_number);
        return "board/view";
    }

    @GetMapping(value = "/update/{board_number}")
    public String boardUpdate(@PathVariable("board_number") long board_number ) {
        request.getSession().setAttribute("board_number",board_number);
        return "board/update.html";
    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(BoardDto boardDto ){
        return boardService.savetoBoard( boardDto );
    }

    @PostMapping(value = "/boardList")
    @ResponseBody
    public void getboardlist(
            HttpServletResponse response ,
            @RequestParam(value = "key",    required = false) String key ,
            @RequestParam(value = "keyword",required = false) String keyword ,
            @RequestParam(value = "page",   required = false) int page){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(boardService.getboardlist(key ,keyword ,page));
        }catch( Exception e ){ log.debug("[{}]",e);}
    }

    @PostMapping(value = "/getboard")
    @ResponseBody
    public void getboard(HttpServletResponse response){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(boardService.getboard((long)request.getSession().getAttribute("board_number")));
        }catch(Exception e){
            log.info("[{}]",e);
        }
    }

    @PutMapping("/goUpdate")
    @ResponseBody
    public int update(BoardDto boardDto){
        boardDto.setBoardNumber((Long) request.getSession().getAttribute("board_number"));
        return boardService.update( boardDto );
    }
    @PostMapping(value = "/delete")
    @ResponseBody
    public int delete(@RequestHeader("board_number") long board_number){
        return boardService.delete(board_number);
    }
}
