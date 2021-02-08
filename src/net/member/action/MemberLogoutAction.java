package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MemberLogoutAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		HttpSession session = request.getSession();
		session.invalidate();

		PrintWriter out = response.getWriter();

		out.print("<script>");
		out.println("alert('로그아웃 되었습니다.');");
		out.println("location.href='login.net';");

		out.println("</script>");
		out.close();
		return null;
	}

}
