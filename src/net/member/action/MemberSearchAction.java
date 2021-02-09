package net.member.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.membet.db.Member;
import net.membet.db.MemberDAO;

public class MemberSearchAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ActionForward forward = new ActionForward();
		MemberDAO mdao = new MemberDAO();

		// �α��� ������ �Ķ���� page�� �����. �׷��� �ʱⰪ�� �ʿ��մϴ�.
		int page = 1; // ������ page
		int limit = 3; // �� �������� ������ �Խ��� ����� ��
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}

		System.out.println("�Ѿ�� ������ = " + page);

		List<Member> list = null;
		int listcount = 0;
		int index = -1; // search_fiel�� �������� �ʴ� ������ �ʱ�ȭ

		String search_word = "";
		// �޴�-������-ȸ������ Ŭ���� ���(member_list.net)
		// �Ǵ� �޴� ������ Ȯ������ Ŭ�� �� Ȩ������ Ŭ���� ���
		// (member_list.net?page=2&search_field=-1&search_word=)
		if (request.getParameter("search_word") == null || request.getParameter("search_word").equals("")) {
			// �� ����Ʈ ���� �޾� �ɴϴ�.
			listcount = mdao.getListCount();
			list = mdao.getList(page, limit);
		} else { // �˻��� Ŭ���� ���
			index = Integer.parseInt(request.getParameter("search_field"));
			String[] search_field = new String[] { "id", "name", "age", "gender" };
			search_word = request.getParameter("search_word");
			listcount = mdao.getListCount(search_field[index], search_word);
			list = mdao.getList(search_field[index], search_word, page, limit);
		}

		int maxpage = (listcount + limit - 1) / limit;
		System.out.println("�� �������� = " + maxpage);

		int startpage = ((page - 1) / 10) * 10 + 1;
		System.out.println("���� �������� ������ ���� ������ �� = " + startpage);

		// endpage : ���� ������ �׷쿡�� ������ ������ ������ ��([10], [20], [30] ��...)
		int endpage = startpage + 10 - 1;
		System.out.println("���� �������� ������ ������ ������ �� = " + endpage);

		/*
		 * ������ �׷��� ������ ������ ���� �ִ� ������ ���Դϴ�. ���� ������ ������ �׷��� [21]~[30]�� ���
		 * ����������(startpage=21)�� ������������(endpage=30)������ �ִ� ������(maxpage)�� 25��� [21]~[25]������
		 * ǥ�õǵ��� �մϴ�.
		 */

		if (endpage > maxpage)
			endpage = maxpage;

		request.setAttribute("page", page); // ���� ������ ��
		request.setAttribute("maxpage", maxpage); // �ִ� ������ ��

		// ���� �������� ǥ���� ù ������ ��
		request.setAttribute("startpage", startpage);

		// ���� �������� ǥ���� �� ������ ��
		request.setAttribute("endpage", endpage);

		request.setAttribute("listcount", listcount); // �� ���� ��

		// �ش� �������� �� ����� ���� �ִ� ����Ʈ
		request.setAttribute("limit", limit); // ��ȣ �κ�

		request.setAttribute("totallist", list);
		request.setAttribute("search_field", index);
		request.setAttribute("serarch_word", search_word);
		request.setAttribute("listcount", listcount);
		forward.setRedirect(false); // �ּ� ������� jsp�������� ������ �����ݴϴ�.
		forward.setPath("member/memberList.jsp");
		return forward;

	}

}
