<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<jsp:include page="header.jsp" />
<link rel="stylesheet" href="css/view.css">
<script src="js/view.js"></script>
<title>MVC 게시판 - view</title>
<style>
tr:nth-child(1) {
	text-align: center
}

td:nth-child(1) {
	width: 20%
}

a {
	color: white
}

tr:nth-child(5)>td:nth-child(2)>a {
	color: black
}

tbody tr:last-child {
	text-align: center
}

.btn-primary {
	background-color: #4f97e5
}

#myModal {
	display: none
}

tr>td:nth-child(1) {
	font-weitght: bold;
	font-size: 1em
}
tbody
>
tr
:nth-child(2)
>
td
:nth-child(1)
</style>
</head>
<body>
	<input type="hidden" id="loginid" value="${id}" name="loginid">
	<div class="container">
		<table class="table">
			<tr>
				<th colspan="2">MVC 게시판 - view 페이지</th>
			</tr>
			<tr>
				<td><div>글쓴이</div></td>
				<td><div>${boarddata.board_name }</div></td>
			</tr>

			<tr>
				<td><div>제목</div></td>
				<td><c:out value="${boarddata.board_subject}" /></td>
			</tr>

			<tr>
				<td><div>내용</div></td>
				<td style="padding-right: 0px"><textarea class="form-control"
						rows="5" readOnly>${boarddata.board_content }</textarea></td>
			</tr>

			<c:if test="${boarddata.board_re_lev==0}">
				<%-- 원문글인 경우에만 첨부파일을 추가할 수 있습니다. --%>
				<tr>
					<td><div>첨부파일</div></td>
					<c:if test="${!empty boarddata.board_file }">
						<%--파일첨부한 경우 --%>
						<td><img src="image/down.png" width="10px"> <a
							href="BoardFileDown.bo?filename=${boarddata.board_file }">${boarddata.board_file }</a></td>
					</c:if>
					<c:if test="${empty boarddata.board_file }">
						<%-- 파일 첨부하지 않은 경우 --%>
						<td></td>
					</c:if>
				</tr>
			</c:if>

			<tr>
				<td colspan="2" class="center"><a
					href="BoardReplyView.bo?num=${boarddata.board_num }"><button
							class="btn btn-primary">답변</button></a> <c:if
						test="${boarddata.board_name==id  || id=='admin'}">
						<a href="BoardModifyView.bo?num=${boarddata.board_num }">
							<button class="btn btn-info">수정</button>
						</a>
						<%--href의 주소를 #으로 설정합니다. --%>
						<a href="#">
							<button class="btn btn-danger" data-toggle="modal"
								data-target="#myModal">삭제</button>
						</a>
					</c:if> <a href="BoardList.bo">
						<button class="btn btn-secondary">목록</button>
				</a></td>
			</tr>

		</table>

		<%--modal 시작 --%>
		<div class="modal" id="myModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<%--Modal Body --%>
					<div class="modal-body">
						<form name="deleteForm" action="BoardDeleteAction.bo"
							method="post">
							<%--http://localhost:8088/Board/BoardDetailAction.bo?num=22
주소를 보면 num을 파라미터로 넘기고 있습니다. 이 값을 가져와서 ${param.num}를 사용 또는
${boarddata.board_num}--%>
							<input type="hidden" name="num" value="${param.num }"
								id="board_re_ref">
							<div class="form-group">
								<label for="pwd">비밀번호</label> <input type="password"
									class="form-control" placeholder="Enter password"
									name="board_pass" id="board_pass">

							</div>
							<button type="submit" class="btn btn-primary">전송</button>
							<button type="button" class="btn btn-danger" data-dismiss="modal">취소</button>

						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="CommentBox">
			<div class="comment_option">
				<h3 class="comment_title">
					댓글 <sup id="count"></sup>
				</h3>
				<div class="comment_tab">
					<ul class="comment_tab_list">
					</ul>
				</div>
			</div>
			<!-- comment option end-->
			<ul class="comment_list">
			</ul>
			<div class="CommentWriter">
				<div class="comment_inbox">

					<b class="comment_inbox_name">${id}</b> <span
						class="comment_inbox_count">0/200</span>
					<textarea placeholder="댓글을 남겨보세요" rows="1"
						class="comment_inbox_text" maxLength="200"></textarea>

				</div>
				<div class="register_box">
					<div class="button btn_cancel">취소</div>
					<div class="button btn_register">등록</div>
				</div>
			</div>
			<!--CommentWriter end-->
		</div>
		<!-- CommentBox end-->
	</div>
</body>
</html>