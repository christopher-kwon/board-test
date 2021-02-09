<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<jsp:include page="../board/참고/header.jsp" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<title>Insert title here</title>
<style>
tr>td:nth-child(odd) {
	font-weight: bold
}

td {
	text-align: center
}

.container {
	width: 50%
}
</style>
</head>
<body>

	<div class="continer">
		<table class="table table-bordered">

			<tr>
				<th>아이디</th>
				<th>${memberInfo.id}</th>
			</tr>

			<tr>
				<th>비밀번호</th>
				<th>${memberInfo.password}</th>
			</tr>

			<tr>
				<th>이름</th>
				<th>${memberInfo.name}</th>
			</tr>

			<tr>
				<th>나이</th>
				<th>${memberInfo.age}</th>
			</tr>

			<tr>
				<th>성별</th>
				<th>${memberInfo.gender}</th>
			</tr>

			<tr>
				<th>이메일 주소</th>
				<th>${memberInfo.email}</th>
			</tr>

			<tr>
				<td colspan=2><a href="memberList.net">리스트로 돌아가기</a></td>
			</tr>


		</table>
	</div>


</body>
</html>