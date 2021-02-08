package net.board.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

public class BoardModifyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ActionForward forward = null;
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();
		String realFolder = "";

		// WebContent �Ʒ��� �� ���� �����ϼ���.
		String saveFolder = "boardupload";

		int fileSize = 5 * 1024 * 1024; // ���ε��� ������ �ִ� �������Դϴ� .5MB

		// ���� ���� ��θ� �����մϴ�.
		ServletContext sc = request.getServletContext();
		realFolder = sc.getRealPath(saveFolder);
		System.out.println("realFolder = " + realFolder);
		boolean result = false;

		try {
			MultipartRequest multi = new MultipartRequest(request, realFolder, fileSize, "utf-8",
					new DefaultFileRenamePolicy());
			int num = Integer.parseInt(multi.getParameter("board_num"));
			String pass = multi.getParameter("board_pass");

			// �۾��� Ȯ���ϱ� ���� ����� ��й�ȣ�� �Է��� ��й�ȣ�� ���մϴ�.
			boolean usercheck = boarddao.isBoardWriter(num, pass);

			// ��й�ȣ�� �ٸ� ���
			if (usercheck == false) {
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('��й�ȣ�� �ٸ��ϴ�.');");
				out.println("history.back();");
				out.println("</script>");
				out.close();
				return null;
			}

			// ��й�ȣ�� ��ġ�ϴ� ��� ���� ������ �����մϴ�.
			// BoardBean ��ü�� �� ��� ������ �Է� ���� �������� �����մϴ�.
			boarddata.setBoard_num(num);
			boarddata.setBoard_subject(multi.getParameter("board_subject"));
			boarddata.setBoard_content(multi.getParameter("board_content"));

			String check = multi.getParameter("check");
			System.out.println("check = " + check);
			if (check != null) {// ���� ���� �״�� ����ϴ� ����Դϴ�.
				boarddata.setBoard_file(check);
			} else {
				// ���ε�� ������ �ý��� �� ���ε�� ���� ���ϸ��� ��� �ɴϴ�.
				String filename = multi.getFilesystemName("board_file");
				boarddata.setBoard_file(filename);
			}

			// DAO���� ���� �޼��� ȣ���Ͽ� �����մϴ�.
			result = boarddao.boardModify(boarddata);

			// ������ ������ ���
			if (result == false) {
				System.out.println("�Խ��� ���� ����");
				forward = new ActionForward();
				forward.setRedirect(false);
				request.setAttribute("message", "�Խ��� ������ ���� �ʾҽ��ϴ�.");
				forward.setPath("error/error.jsp");
				return forward;
			}

			// ���� ������ ���
			System.out.println("�Խ��� ���� �Ϸ�");
			forward = new ActionForward();
			forward.setRedirect(true);
			// ������ �� ������ Ȯ���ϱ� ���� �� ���� ���� �������� ��η� �����մϴ�.
			forward.setPath("BoardDetailAction.bo?num=" + boarddata.getBoard_num());
			return forward;

		} catch (IOException e) {
			e.printStackTrace();
			forward = new ActionForward();
			forward.setPath("error/error.jsp");
			request.setAttribute("message", "�Խ��� ���ε� �� �����Դϴ�.");
			forward.setRedirect(false);
			return forward;

		}

	}
}
