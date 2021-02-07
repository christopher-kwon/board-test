package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

public class BoardReplyView implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ActionForward forward = new ActionForward();
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();

		// �Ķ���ͷ� ���޹��� �亯�� �� ��ȣ�� num������ �����մϴ�.
		int num = Integer.parseInt(request.getParameter("num"));

		// �۹�ȣ num�� �ش��ϴ� ������ �����ͼ� boardata ��ü�� �����մϴ�.
		boarddata = boarddao.getDetail(num);

		// �� ������ ���� ���
		if (boarddata == null) {
			System.out.println("���� �������� �ʽ��ϴ�.");
			forward.setRedirect(false);
			request.setAttribute("message", "���� �������� �ʽ��ϴ�.");
			forward.setPath("error/error.jsp");
			return forward;
		}

		System.out.println("�亯 ������ �̵� �Ϸ�");
		// �亯 �� �������� �̵��� �� ������ �� ������ �����ֱ� ����
		// boarddata ��ü�� request ��ü�� �����մϴ�.
		request.setAttribute("boarddata", boarddata);

		forward.setRedirect(false);

		// �� �亯 ������ ��� �����մϴ�.
		forward.setPath("board/����/boardReply.jsp");
		return forward;

	}

}