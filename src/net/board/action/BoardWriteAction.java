package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.member.action.Action;
import net.member.action.ActionForward;

public class BoardWriteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(false); //주소 변경없이 jsp페이지의 내용을 보여줍니다.
		forward.setPath("board/참고/boardWrite.jsp");
		return forward;
		
	}

}
