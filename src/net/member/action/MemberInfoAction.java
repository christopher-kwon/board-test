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
			forward.setRedirect(false); // 주소 변경없이 jsp페이지의 내용을 보여줍니다.
			request.setAttribute("message", "아이디에 해당하는 회원이 없습니다,");
			return forward;
			
		}

		request.setAttribute("memberInfo", m);
		forward.setRedirect(false); // 주소 변경없이 jsp페이지의 내용을 보여줍니다.
		forward.setPath("member/memberInfo.jsp");
		return forward;

	}

}
