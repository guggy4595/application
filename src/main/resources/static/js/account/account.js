let duplicateIdYn = false;

const accountService = {
    login: function(){
        if($("#acid").val() == ""){
            $("#valid").text("아이디를 입력해 주세요.")
            return false;
        } else if($("#acpw").val() == "") {
            $("#valid").text("비밀 번호를 입력해 주세요.")
            return false;
        }
        $("#valid").text("")
        accountService.idUseCheck();
    },
    idUseCheck : function(){
        $.ajax({
            type : 'POST',
            url: '/account/idUserYn' ,
            data : {accountId : $("#acid").val()},

            success: function( re ){
                if(re == 0000) {
                    $("#loginform").submit();
                } else if(re == 2) {
                    $("#valid").text("휴먼 계정입니다. 재인증 해주세주세요.")
                    //TODO : SMTP 이메일 인증 수단 추가
                } else if(re == 4){
                    $("#valid").text("존재하지 않는 회원 입니다.")
                }
            },
        });
    },

    signup: function(){
        const regex = /^[ㄱ-ㅎ|가-힣|a-z|A-Z|]+$/;
        const emailRegex = /^[a-z0-9]+@[a-z]+\.[a-z]{2,3}$/;
        const pwRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
        if(!duplicateIdYn) {
            alert("아이디 중복확인 필요");
            return false;
        }
        if($("#accountPassword").val() == "") {
            alert("비밀번호를 입력해주세요.");
            return false;
        }
        if(!pwRegex.test($("#accountPassword").val())) {
            alert("8~15자리 특수문자를 포함해 만들어 주세여.");
            return false;
        }
        if($("#checkPassword").val() == "") {
            alert("비밀번호를 확인해 주세요");
            return false;
        }
        if($("#accountPassword").val() !== $("#checkPassword").val()) {
            alert("비밀번호가 다릅니다.")
            return false;
        }
        if($("#accountName").val() == "") {
            alert("이름을 입력해주세요");
            return false;
        }
        if(!regex.test($("#accountName").val())) {
            alert("이름은 한글 또는 영어만 가능합니다.");
            return false;
        }
        if($("#accountEmail").val() == "") {
            alert("이메일을 입력해 주세요");
            return false;
        }
        if(!emailRegex.test($("#accountEmail").val())) {
            alert("이메일 형식이 올바르지 않습니다.");
            return false;
        }
        accountService.signUpGo();
    },
    signUpGo: function(){
        let form = $("#signupform")[0];
        let formdata = new FormData( form);
        $.ajax({
            type : 'POST',
            url: '/account/goSignup',
            data: formdata,
            contentType: false ,
            processData: false ,
            success: function( re ){
                if(re=="0000"){
                    location.href = "/account/login";
                } else if(re == "1"){
                    alert("내용을 입력해 주세요");
                } else if(re == "2"){
                    alert("아이디 형식이 잘못 되어있습니다.")
                } else if(re == "3"){
                    alert("비밀번호 형식이 잘못 되어있습니다.")
                } else if(re == "4"){
                    alert("이름 형식이 잘못 되어있습니다.")
                } else if(re == "5"){
                    alert("이메일 형식이 잘못 되어있습니다.")
                }
            },
        });
    },

    searchId:function(){
        if($("#accountId").val() == "") {
            alert("아이디를 입력해 주세요.")
            return false;
        }
        let idReg = /^[a-z]+[a-z0-9]{5,19}$/g;
        if(!idReg.test($("#accountId").val())) {
            alert("아이디는 영문자로 시작하는 6~20자 영문자 또는 숫자이어야 합니다.");
            return false;
        }
        if($("#idDuple").text() == "중복확인"){
            $.ajax({
                type : 'POST',
                url: '/account/searchId' ,
                data : {accountId : $("#accountId").val()},

                success: function( re ){
                    if(re == true) {
                        $("#accountId").attr("readonly", true);
                        $("#idDuple").text("변경")
                        duplicateIdYn = true;
                    } else {
                        alert("사용중인 아이디 입니다.")
                        $("#accountId").val("")
                    }
                },
                error: function(re){
                    alert("오류");
                    $("#accountId").val("")
                }
            });
        } else if($("#idDuple").text() == "변경"){
            $("#accountId").attr("readonly", false);
            $("#idDuple").text("중복확인");
            duplicateIdYn = false;
        }
    }
};