<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<jsp:include page="../board/참고/header.jsp" />
<style>
.gray {
	color: gray
}

body>div>table>thead>tr:nth-child(2)>th:nth-child(1) {
	width: 8%
}

body>div>table>thead>tr:nth-child(2)>th:nth-child(2) {
	width: 50%
}

body>div>table>thead>tr:nth-child(2)>th:nth-child(3) {
	width: 14%
}

body>div>table>thead>tr:nth-child(2)>th:nth-child(4) {
	width: 17%
}

body>div>table>thead>tr:nth-child(2)>th:nth-child(5) {
	width: 11%
}
</style>
<script src="js/memberlist.js"></script>

<title>MVC 게시판</title>
</head>
<body>
	<div class="container">
		<form action="memberList.net" method="post">
			<div class="input-group">
				<select id="viewcount" name="search_field">
					<option value="0" selected>아이디</option>
					<option value="1">이름</option>
					<option value="2">나이</option>
					<option value="3">성별</option>
				</select> <input name="search_word" type="text" class="form-control"
					placeholder="아이디 입력하세요" value="${search_word}">
				<button class="btn btn-primary" type="submit">검색</button>
			</div>
		</form>

		<%-- 회원이 있는 경우 --%>
		<c:if test="${listcount > 0}">

			<table class="table table-striped">
				<caption style="font-weight: bold">회원 목록</caption>
				<thead>
					<tr>
						<th colspan="2">MVC 게시판 - 회원 정보 list</th>
						<th><font size=3>회원 수 : ${listcount }</font></th>
					</tr>

					<tr>
						<th><div>아이디</div></th>
						<th><div>이름</div></th>
						<th><div>삭제</div></th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="m" items="${totallist}">
						<tr>
							<td><a href="memberInfo.net?id=${m.id }">${m.id}</a></td>
							<td>${m.name}</td>
							<td><a href="memberDelete.net?id=${m.id}">삭제</a></td>
						</tr>
					</c:forEach>

				</tbody>
			</table>

			<div>
				<ul class="pagination justify-content-center">
					<c:if test="${page <= 1}">
						<li class="page-item"><a class="page-link current" href="#">이전&nbsp;</a>
						</li>
					</c:if>

					<c:if test="${page > 1}">
						<li class="page-item"><a
							href="memberList.net?page=${page-1}&search_field=${search_field}&search_word=${search_word}"
							class="page-link">이전</a> &nbsp;</li>
					</c:if>


					<c:forEach var="a" begin="${startpage}" end="${endpage }">
						<c:if test="${a == page }">
							<li class="page-item"><a class="page-link current" href="#">${a}</a>
							</li>
						</c:if>

						<c:if test="${a != page }">
							<li class="page-item"><a
								href="memberList.net?page=${a}&search_field=${search_field}&search_word=${search_word}"
								class="page-link">${a}</a></li>
						</c:if>
					</c:forEach>


					<c:if test="${page >= maxpage }">
						<li class="page-item"><a class="page-link current" href="#">&nbsp;다음</a>
						</li>
					</c:if>

					<c:if test="${page < maxpage }">
						<li class="page-item"><a
							href="memberList.net?page=${page+1}&search_field=${search_field}&search_word=${search_word}"
							class="page-link">&nbsp;다음</a></li>
					</c:if>
				</ul>
			</div>
		</c:if>
		<%-- <c:if test="${listcount > 0}"> end --%>
	</div>

	<%-- 회원이 없는 경우 --%>
	<c:if test="${listcount == 0 && empty search_word}">
		<h1>회원이 없습니다.</h1>
	</c:if>
	
		<c:if test="${listcount == 0 && !empty search_word}">
		<h1>검색 결과가 없습니다..</h1>
	</c:if>
	
	
</body>
</html>