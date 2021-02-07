
package net.member.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("*.net")
public class MemberFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		/*
		 * ��û�� ��ü URL �߿��� ��Ʈ ��ȣ ���� ���� ������ ���ڿ����� ��ȯ�˴ϴ�.
		 * ��) http://localhost:8088/Jsp/login.net�� ���
		 * "/Jsp/login.net" ��ȯ�˴ϴ�.
		 */
		
		String RequestURI = request.getRequestURI();
		System.out.println("RequestURI = " + RequestURI);
		
		//getContextPath() : ���ؽ�Ʈ ��ΰ� ��ȯ�˴ϴ�.
		//contextPath()�� "/Jsp"�� ��ȯ�˴ϴ�. 
		String contextPath = request.getContextPath();
		System.out.println("contextPath = " + contextPath);
		
		//RequestURI���� ���ؽ�Ʈ ��� ���� ���� �ε��� ��ġ�� ���ں���
		//������ ��ġ ���ڱ��� �����մϴ�.
		//command�� "/login.net" ��ȯ�˴ϴ�.
		String command = RequestURI.substring(contextPath.length());
		System.out.println("command = " + command);
		
		//�ʱ�ȭ
		ActionForward forward = null;
		Action action = null;
		
		switch(command) {
		case "/join.net" :
			action = new MemberJoinAction();
			break;
			
		case "/idcheck.net" :
			action = new MemberIdCheckAction();
			break;
			
		case "/login.net" :
			action = new MemberLoginAction();
			break;
			
		case "/joinProcess.net" :
			action = new MemberJoinProcessAction();
			break;
			
		case "/loginProcess.net" :
			action = new MemberLoginProcessAction();
			break;
			
		case "/logout.net" :
			action = new MemberLogoutAction();
			break;
			
		case "/memberUpdate.net" :
			action = new MemberUpdateAction();
			break;
			
		case "/updateProcess.net" :
			action = new MemberUpdateProcessAction();
			break;
			
		case "/memberList.net" :
			action = new MemberSearchAction();
			break;
			
		case "/memberInfo.net" :
			action = new MemberInfoAction();
			break;
			
		case "/memberDelete.net" :
			action = new MemberDeleteAction();
			break;
			
		} //switch end
		forward = action.execute(request, response);
		
		if(forward != null) {
			if(forward.isRedirect()) { //�����̷�Ʈ �˴ϴ�.
				response.sendRedirect(forward.getPath());
			} else { //������ �˴ϴ�.
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}
	}
	
       

    // doProcess(request, response) �޼��带 �����Ͽ� ��û�� GET����̵�
    //POST ������� ���۵Ǿ� ���� ���� �޼��忡�� ��û�� ó���� �� �ֵ��� �Ͽ����ϴ�. 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doProcess(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		doProcess(request, response);
	}

}