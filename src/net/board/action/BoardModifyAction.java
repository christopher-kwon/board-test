package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

public class BoardModifyAction implements Action {
		
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ActionForward forward = null;
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();
		int result = 0;
		
		// 파라미터로 넘어온 값들을 boarrdata 개체에 저장합니다.
		boarddata.setBoard_num(Integer.parseInt(request.getParameter("board_num")));
		boarddata.setBoard_name(request.getParameter("board_name"));
		boarddata.setBoard_pass(request.getParameter("board_pass"));
		boarddata.setBoard_subject(request.getParameter("board_subject"));
		boarddata.setBoard_content(request.getParameter("board_content"));

		result = boarddao.boardModify(boarddata);

		// 글 수정에 실패한 경우
		if (result == 0) {
			System.out.println("글 수정 실패.");
			forward = new ActionForward();
			forward.setRedirect(false);
			request.setAttribute("message", "글 수정 실패입니다.");
			forward.setPath("error/error.jsp");
			return forward;
		}

		// 글 수정이 제대로 된 경우
		System.out.println("수정 완료.");
		forward = new ActionForward();
		forward.setRedirect(true);
		// 수정한 글 내용을 확인하기 위해 글 내용 보기 페이지를 경로로 설정합니다.
		forward.setPath("BoardDetailAction.bo?num=" + Integer.parseInt(request.getParameter("board_num")));

		return forward;

	}
}
