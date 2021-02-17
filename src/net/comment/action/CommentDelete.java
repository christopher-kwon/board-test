package net.comment.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.comment.db.CommentDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

public class CommentDelete implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		CommentDAO dao = new CommentDAO();


		int result = dao.commentsDelete(Integer.parseInt(request.getParameter("num")));
		System.out.println(result);
		// response.getWriter().append(Integer.toString(result));
		response.getWriter().print(result);
		return null;
	}

}
