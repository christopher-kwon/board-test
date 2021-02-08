<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="http://code.jquery.com/jquery-latest.js"></script>


<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="css/join.css">
<title>회원관리 시스템 회원수정 페이지  </title>
<style>
h3{
text-align:center; color:#1a92b9;}
</style>
</head>
<body>

<jsp:include page="../board/참고/header.jsp"/>

<form name="updateform" action="updateProcess.net" method="post">
<h1>회원 정보 수정</h1>
<hr>
<b>아이디</b>
<input type="text" name="id" value="${memberinfo.id }" maxLength="12" readOnly>
<span id="message"></span>

<b>비밀번호</b>
<input type="password" name="pass" value=${memberinfo.password } readOnly>

<b>이름</b>
<input type="text" name="name" value=${memberinfo.name }  maxLength="15" required>

<b>나이</b>
<input type="text" name="age" value=${memberinfo.age } maxLength="2" required>

<b>성별</b>
<div>

<input type="radio" name="gender" value="남" checked><span>남자</span>
<input type="radio" name="gender" value="여"><span>여자</span>

</div>

<b>이메일 주소</b>
<input type="text" name="email" value=${memberinfo.email } >
<span id="email_message"></span>
<div class="clearfix">
<button type="submit" class="submitbtn">정보수정</button>
<button type="button" class="cancelbtn">취소</button>
</div>

</form>
<script>
//성별 체크해주는 부분
$("input[value='${memberinfo.gender}']").prop('checked', true);

$(".cancelbtn").click(function() {
	history.back();
});

//처음 화면 로드시 보여줄 이메일은 이미 체크 완료된 것이므로 기본 checkemail=true입니다.
var checkemail = true;

$("input:eq(6)").on('keyup',
	function() {
		$("#email_message").empty();
		//[A-Za-z0-9_]와 동일한 것이 \w
		//+는 1회 이상 반복을 의미합니다. {1,} 와 동일합니다.
		//\w+는 [A-Za-z0-9_]를 1개이상 사용하라는 의미입니다.
		var pattern = /^\w+@\w+[.]\w{3}$/;
		var email = $("input:eq(6)").val();
		if(!pattern.test(email)){
			$("#email_message").css('color', 'red').html("이메일형식이 맞지 않습니다.");
			
			checkemail = false;
			
		} else {
			$("#email_message").css('color', 'green').html("이메일형식에 맞습니다.");
			
			checkemail = true;
		} 
		
		
	}) //email keyup 이벤트 처리 끝
	
	$('form').submit(function() {
		if(!$.isNumeric($("input[name='age']").val())) {
			alert("나이는 숫자를 입력하세요");
			$("input[name='age']").val('').focus();
			return false;
		}
		
		if(!checkemail){
			alert("email 형식을 확인하세요");
			$("input:eq(6)").focus();
			return false;
		}
		
	});
		
		
</script>

</body>
</html>