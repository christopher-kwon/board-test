package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

public class BoardReplyAction implements Action {

	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ActionForward forward = null;
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();
		int result = 0;

		// �Ķ���ͷ� �Ѿ�� ������ boarrdata ��ü�� �����մϴ�.
		boarddata.setBoard_name(request.getParameter("board_name"));
		boarddata.setBoard_pass(request.getParameter("board_pass"));
		boarddata.setBoard_subject(request.getParameter("board_subject"));
		boarddata.setBoard_content(request.getParameter("board_content"));
		boarddata.setBoard_re_ref(Integer.parseInt(request.getParameter("board_re_ref")));
		boarddata.setBoard_re_lev(Integer.parseInt(request.getParameter("board_re_lev")));
		boarddata.setBoard_re_seq(Integer.parseInt(request.getParameter("board_re_seq")));

		// �亯�� DB�� �����ϱ� ���� boarddata ��ü�� �Ķ���ͷ� �����ϰ� DAO�� �޼��� boardReply�� ȣ���մϴ�.
		result = boarddao.boardReply(boarddata);

		// �亯 ���忡 ������ ���
		if (result == 0) {
			System.out.println("���� ���� ����.");
			forward = new ActionForward();
			forward.setRedirect(false);
			request.setAttribute("message", "���� ���� �����Դϴ�.");
			forward.setPath("error/error.jsp");
			return forward;
		}

		// �亯 ������ ����� �� ���
		System.out.println("���� �Ϸ�.");
		forward = new ActionForward();
		forward.setRedirect(true);
		// �亯 �� ������ Ȯ���ϱ� ���� �� ���� ���� �������� ��η� �����մϴ�.
		forward.setPath("BoardDetailAction.bo?num=" + result);
		
		return forward;

	}

}
