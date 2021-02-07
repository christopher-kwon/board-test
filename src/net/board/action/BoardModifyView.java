package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

public class BoardModifyView implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ActionForward forward = new ActionForward();
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();

		// �Ķ���ͷ� ���޹��� ������ �� ��ȣ�� num������ �����մϴ�.
		int num = Integer.parseInt(request.getParameter("num"));

		// �� ������ �ҷ��ͼ� board ��ü�� �����մϴ�.
		boarddata = boarddao.getDetail(num);

		// �� ���� �ҷ����� ������ ����Դϴ�.
		if (boarddata == null) {
			System.out.println("(����)�󼼺��� ����");
			forward = new ActionForward();
			forward.setRedirect(false);
			request.setAttribute("message", "�Խ��� ���� �� ���� �����Դϴ�.");
			forward.setPath("error/error.jsp");
			return forward;

		}

		System.out.println("(����)�󼼺��� ����");

		//���� �� �������� �̵��� �� ���� �� ������ �����ֱ� ������ boarddata ��ü�� 
		//request ��ü�� �����մϴ�.
		request.setAttribute("boarddata", boarddata);
		forward.setRedirect(false);

		// �� ���� ���� �������� �̵��ϱ� ���� ��θ� �����մϴ�.
		forward.setPath("board/����/boardModify.jsp");
		return forward;

	}
}
