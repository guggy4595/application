let current_cno     = 1;
let current_page    = 0;
let current_key     = "";
let current_keyword = "";

function board_save(){
    let form = $("#saveform")[0];
    let formdata = new FormData( form );
    $.ajax({
        url : "/board/save" ,
        data : formdata ,
        type : "POST",
        processData : false ,
        contentType : false ,
        success : function( re ){
            location.href = "/board/main";
        }
    });
}

function board_list(page ,key ,keyword) {
    this.current_page = page;

    if(key != undefined) {
        this.current_key = key;
    }
    if(keyword != undefined) {
        this.current_keyword = keyword;
    }
    $.ajax({
        type : "POST",
        url : "/board/boardList" ,
        data : {key:this.current_key,keyword:this.current_keyword,page:this.current_page} ,
        success : function(result){
            console.log(result);

            let pagehtml = "";
            let html = '<tr> <th width="10%">번호</th> <th width="50%">제목</th> <th width="10%">작성일</th> <th width="10%">조회수</th><th width="10%">작성자</th></tr>';

            if(result.data.length == 0 ){
                html += '<tr><td colspan="5">내역이 없습니다..</td></tr>';
            } else {
                for( let i = 0 ; i<result.data.length ; i++ ){
                    html +=
                        '<tr>'+
                                '<td>'+result.data[i].boardNumber+'</td> '+
                                '<td><a href="/board/view/'+result.data[i].boardNumber+'">'+result.data[i].boardTitle+'<a></td> '+
                                '<td>'+result.data[i].boardData+'</td>'+
                                '<td>'+result.data[i].boardView+'</td>'+
                                '<td>'+result.data[i].accountId+'</td>'+
                         '</tr>';
                }
            }
            if( page == 0 ){
                pagehtml += '<li class="page-item"><button class="page-link" onclick="board_list('+(page)+')"> 이전 </button></li>';
            }else{
                pagehtml += '<li class="page-item"><button class="page-link" onclick="board_list('+(page-1)+')"> 이전 </button></li>';
            }

            if( page == result.totalpages -1 ){
                 pagehtml += '<li class="page-item"><button class="page-link" onclick="board_list('+(page)+')"> 다음 </button></li>';
            }else{
                 pagehtml += '<li class="page-item"><button class="page-link" onclick="board_list('+(page+1)+')"> 다음 </button></li>';
            }
            $("#boardtable").html(html);
            $("#pagebtnbox").html(pagehtml);
        }
    });
}

function board_read(gubun){
    $.ajax({
        type : "POST",
        url: '/board/getboard',
        success : function(board){
            if(gubun == 0){
                let html =
                   ' <div>게시물번호 : '+board.boardNumber+'</div>'+
                    '<div>게시물제목 : '+board.boardTitle+' </div>'+
                    '<div>게시물내용 : '+board.boardContents+' </div>'+
                    '<div>게시물작성일: '+board.boardData+' </div>'+
                    '<div>게시물작성자: '+board.accountId+' </div>'+
                    '<div>게시물조회수: '+board.boardView+' </div>';
                    if(board.showYn == "Y") {
                        html += '<button onclick="board_delete('+board.boardNumber+')"> 삭제 </button>'
                            +'<button onclick="board_update('+board.boardNumber+')"> 수정 </button>';
                    }

                $("#boarddiv").html(html);
            } else if(gubun == 1){
                $("#boardTitle").val(board.boardTitle);
                $("#boardContents").val(board.boardContents);
            }

        },
        error : function(error){
            console.log(error);
        }
    });
}

function board_update(gubun){
    if(gubun == 'update') {
        let form = $("#updateform")[0];
        let formdata = new FormData( form );
        $.ajax({
            url : "/board/goUpdate" ,
            data : formdata ,
            method : "PUT",
            processData : false ,
            contentType : false ,
            success : function( re ){
                if(re == 0000){
                    location.href = "/board/view/"+gubun;
                } else if(re == 9999) {
                    alert("잘못된 접근");
                }
            }
        });
    } else {
        location.href = "/board/update/"+gubun;
    }

}

function board_delete(num){
    if (confirm("지움?")) {
        $.ajax({
             url : "/board/delete" ,
             headers: {'board_number': num},
             type : "POST",
             success : function( re ){
                if(re == 9999){
                    alert("잘못된 접근");
                } else {
                    location.href = "/board/main";
                }
             }
         });
    }
}

