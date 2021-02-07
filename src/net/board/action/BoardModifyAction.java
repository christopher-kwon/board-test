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
		
		// �Ķ���ͷ� �Ѿ�� ������ boarrdata ��ü�� �����մϴ�.
		boarddata.setBoard_num(Integer.parseInt(request.getParameter("board_num")));
		boarddata.setBoard_name(request.getParameter("board_name"));
		boarddata.setBoard_pass(request.getParameter("board_pass"));
		boarddata.setBoard_subject(request.getParameter("board_subject"));
		boarddata.setBoard_content(request.getParameter("board_content"));

		result = boarddao.boardModify(boarddata);

		// �� ������ ������ ���
		if (result == 0) {
			System.out.println("�� ���� ����.");
			forward = new ActionForward();
			forward.setRedirect(false);
			request.setAttribute("message", "�� ���� �����Դϴ�.");
			forward.setPath("error/error.jsp");
			return forward;
		}

		// �� ������ ����� �� ���
		System.out.println("���� �Ϸ�.");
		forward = new ActionForward();
		forward.setRedirect(true);
		// ������ �� ������ Ȯ���ϱ� ���� �� ���� ���� �������� ��η� �����մϴ�.
		forward.setPath("BoardDetailAction.bo?num=" + Integer.parseInt(request.getParameter("board_num")));

		return forward;

	}
}
