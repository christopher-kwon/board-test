package net.member.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.membet.db.Member;
import net.membet.db.MemberDAO;

public class MemberInfoAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ActionForward forward = new ActionForward();
		MemberDAO mdao = new MemberDAO();
		String id = request.getParameter("id");	
		
		Member m = mdao.member_info(id);



		if(m == null) {
			forward.setPath("error/error.jsp");
			forward.setRedirect(false); // �ּ� ������� jsp�������� ������ �����ݴϴ�.
			request.setAttribute("message", "���̵� �ش��ϴ� ȸ���� �����ϴ�,");
			return forward;
			
		}

		request.setAttribute("memberInfo", m);
		forward.setRedirect(false); // �ּ� ������� jsp�������� ������ �����ݴϴ�.
		forward.setPath("member/memberInfo.jsp");
		return forward;

	}

}
