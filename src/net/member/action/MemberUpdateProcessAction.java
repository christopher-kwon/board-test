package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.membet.db.Member;
import net.membet.db.MemberDAO;

public class MemberUpdateProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id = request.getParameter("id");
		//String pass = request.getParameter("pass");
		String name = request.getParameter("name");
		int age = Integer.parseInt(request.getParameter("age"));
		String gender = request.getParameter("gender");
		String email = request.getParameter("email");

		Member m = new Member();

		m.setId(id);
		//m.setPassword(pass);
		m.setName(name);
		m.setAge(age);
		m.setEmail(email);
		m.setGender(gender);

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		MemberDAO mdao = new MemberDAO();

		int result = mdao.update(m);
		out.print("<script>");
		if (result == 1) {
			out.println("alert('정보가 수정되었습니다.');");
			out.println("location.href='BoardList.bo';");
		} else if (result == -1) {
			out.println("alert('정보수정에 실패하였습니다.');");
			out.println("history.back()");
		}

		out.println("</script>");
		out.close();
		return null;

	}
}
