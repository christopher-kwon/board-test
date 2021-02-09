package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.membet.db.MemberDAO;

public class MemberDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		String id = request.getParameter("id");
		MemberDAO mdao = new MemberDAO();
		int result = mdao.delete(id);

		PrintWriter out = response.getWriter();

		out.print("<script>");

		if (result == 1) {
			out.println("alert('���� �Ǿ����ϴ�.');");
			out.println("location.href='memberList.net';");

		} else {
			out.println("alert('������ �����߽��ϴ�.');");
			out.println("history.back();");
		}
		out.println("</script>");
		out.close();
		return null;

	}

}
