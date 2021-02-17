package net.comment.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.comment.db.Comment;
import net.comment.db.CommentDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

public class CommentAdd implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		CommentDAO dao = new CommentDAO();
		Comment co = new Comment();

		co.setComment_id(request.getParameter("id"));
		co.setComment_content(request.getParameter("content"));
		System.out.println("content = " + co.getComment_content());
		co.setComment_board_num(Integer.parseInt(request.getParameter("comment_board_num")));
		co.setComment_re_lev(Integer.parseInt(request.getParameter("comment_re_lev")));
		co.setComment_re_seq(Integer.parseInt(request.getParameter("comment_re_seq")));

		int ok = dao.commentsInsert(co);

		// response.getWriter().append(Integer.toString(result));
		response.getWriter().print(ok);
		return null;
	}

}
