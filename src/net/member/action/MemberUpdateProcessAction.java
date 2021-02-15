package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.membet.db.Member;
import net.membet.db.MemberDAO;

public class MemberUpdateProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String realFolder = "";

		// WebContent �Ʒ��� �� ���� �����ϼ���.
		String saveFolder = "memberupload";

		int fileSize = 5 * 1024 * 1024; // ���ε��� ������ �ִ� ������ �Դϴ�. 5MB

		// ���� ���� ��θ� �����մϴ�.
		ServletContext sc = request.getServletContext();
		realFolder = sc.getRealPath(saveFolder);

		System.out.println("realFolder = " + realFolder);

		try {

			MultipartRequest multi = null;
			multi = new MultipartRequest(request, realFolder, fileSize, "utf-8", new DefaultFileRenamePolicy());

			String id = multi.getParameter("id");
			// String pass = request.getParameter("pass");
			String name = multi.getParameter("name");
			int age = Integer.parseInt(multi.getParameter("age"));
			String gender = multi.getParameter("gender");
			String email = multi.getParameter("email");
			String filename = multi.getFilesystemName("memberfile");

			Member m = new Member();

			m.setId(id);
			// m.setPassword(pass);
			m.setName(name);
			m.setAge(age);
			m.setEmail(email);
			m.setGender(gender);
			m.setMemberfile(filename);

			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();

			MemberDAO mdao = new MemberDAO();

			int result = mdao.update(m);
			out.print("<script>");

			if (result == 1) {
				out.println("alert('������ �����Ǿ����ϴ�.');");
				out.println("location.href='BoardList.bo';");
			} else if (result == -1) {
				out.println("alert('���������� �����Ͽ����ϴ�.');");
				out.println("history.back()");
			}

			out.println("</script>");
			out.close();
			return null;
		} catch (IOException ex) {
			ActionForward forward = new ActionForward();
			forward.setPath("error/error.jsp");
			request.setAttribute("message", "������ ���� ���ε� �����Դϴ�.");
			forward.setRedirect(false);
			return forward;
		}

	}
}
