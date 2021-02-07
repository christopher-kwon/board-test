
package net.board.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.member.action.Action;
import net.member.action.ActionForward;


@WebServlet("*.bo")
public class BoardFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		/*
		 * 요청된 전체 URL 중에서 포트 번호 다음 부터 마지막 문자열까지 반환됩니다.
		 * 예) http://localhost:8088/Jsp/login.net인 경우
		 * "/Jsp/login.net" 반환됩니다.
		 */
		
		String RequestURI = request.getRequestURI();
		System.out.println("RequestURI = " + RequestURI);
		
		//getContextPath() : 컨텍스트 경로가 반환됩니다.
		//contextPath()는 "/Jsp"가 반환됩니다. 
		String contextPath = request.getContextPath();
		System.out.println("contextPath = " + contextPath);
		
		//RequestURI에서 컨텍스트 경로 길이 값의 인덱스 위치의 문자부터
		//마지막 위치 문자까지 추출합니다.
		//command는 "/login.net" 반환됩니다.
		String command = RequestURI.substring(contextPath.length());
		System.out.println("command = " + command);
		
		//초기화
		ActionForward forward = null;
		Action action = null;
		
		switch(command) {
		case "/BoardWrite.bo" :
			action = new BoardWriteAction();
			break;
			
		case "/BoardList.bo" :
			action = new BoardListAction();
			break;
			
		case "/BoardAddAction.bo" :
			action = new BoardAddAction();
			break;
			
		case "/BoardReplyView.bo" :
			action = new BoardReplyView();
			break;
			
		case "/BoardReplyAction.bo" :
			action = new BoardReplyAction();
			break;
			
		case "/BoardDeleteAction.bo" :
			action = new BoardDeleteAction();
			break;
			
		case "/BoardFileDown.bo" :
			action = new BoardFileDownAction();
			break;
			
		case "/BoardModifyView.bo" :
			action = new BoardModifyView();
			break;
			
		case "/BoardModifyAction.bo" :
			action = new BoardModifyAction();
			break;
			
		case "/BoardDetailAction.bo" :
			action = new BoardDetailAction();
			break;
		
		} //switch end
		
		forward = action.execute(request, response);
		
		if(forward != null) {
			if(forward.isRedirect()) { //리다이렉트 됩니다.
				response.sendRedirect(forward.getPath());
			} else { //포워딩 됩니다.
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}
	}
	
       

    // doProcess(request, response) 메서드를 구현하여 요청이 GET방식이든
    //POST 방식으로 전송되어 오든 같은 메서드에서 요청을 처리할 수 있도록 하였습니다. 
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
