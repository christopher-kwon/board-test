package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

public class BoardDetailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();

		// �� ��ȣ �Ķ���� ���� num������ �����մϴ�.
		int num = Integer.parseInt(request.getParameter("num"));

		// ������ Ȯ���� ���� ��ȸ���� ������ŵ�ϴ�.
		boarddao.setReadCountUpdate(num);

		// ���� ������ DAO���� ���� �� ���� ����� boarddata ��ü�� �����մϴ�.
		boarddata = boarddao.getDetail(num);

		// boarddata=null; //error�׽�Ʈ�� ���� �� ����
		// DAO���� ���� ������ ���� ������ ��� null�� ��ȯ�մϴ�.
		if (boarddata == null) {
			System.out.println("�󼼺��� ����");
			ActionForward forward = new ActionForward();
			forward.setRedirect(false);
			request.setAttribute("message", "�����͸� ���� ���߽��ϴ�.");
			forward.setPath("error/error.jsp");
			return forward;

		}

		System.out.println("�󼼺��� ����");

		// boarddata ��ü�� request ��ü�� �����մϴ�.
		request.setAttribute("boarddata", boarddata);

		ActionForward forward = new ActionForward();
		forward.setRedirect(false);

		// �� ���� ���� �������� �̵��ϱ� ���� ��θ� �����մϴ�.
		forward.setPath("board/����/boardView.jsp");
		return forward;

	}
}
