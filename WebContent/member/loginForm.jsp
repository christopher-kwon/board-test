<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Session</title>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link href="css/login.css" type="text/css" rel="stylesheet">
<script src="http://code.jquery.com/jquery-latest.js"></script>

<script>
	$(function() {
		$(".join").click(function() {
			location.href = "join.net";
		});

		var id = '${id}';
		if (id) { //id가 값이 있으면
			$("#id").val(id);
			$("#remember").prop('checked', true);
		}

	});
</script>

</head>

<body>


	<form name="loginform" action="loginProcess.net" method="post">
		<h1>로그인</h1>
		<hr>
		<b>아이디</b> <input type="text" class="form-control" id="id"
			placeholder="Enter id" name="id" required> <b>Password</b> <input
			type="password" placeholder="Enter passsword" name="pass" required>
		<input type="checkbox" id="remember" name="remember" value="store">
		<span>remember</span>
		<div class="clearfix">
			<button type="submit" class="submitbtn">로그인</button>
			<button type="button" class="join">회원가입</button>
		</div>

	</form>

</body>
</html>