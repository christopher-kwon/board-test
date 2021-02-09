package net.member.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.membet.db.Member;
import net.membet.db.MemberDAO;

public class MemberSearchAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ActionForward forward = new ActionForward();
		MemberDAO mdao = new MemberDAO();

		// 로그인 성공시 파라미터 page가 없어요. 그래서 초기값이 필요합니다.
		int page = 1; // 보여줄 page
		int limit = 3; // 한 페이지에 보여줄 게시판 목록의 수
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}

		System.out.println("넘어온 페이지 = " + page);

		List<Member> list = null;
		int listcount = 0;
		int index = -1; // search_fiel에 존재하지 않는 값으로 초기화

		String search_word = "";
		// 메뉴-관리자-회원정보 클릭한 경우(member_list.net)
		// 또는 메뉴 관리자 확인정보 클릭 후 홈페이지 클릭한 경우
		// (member_list.net?page=2&search_field=-1&search_word=)
		if (request.getParameter("search_word") == null || request.getParameter("search_word").equals("")) {
			// 총 리스트 수를 받아 옵니다.
			listcount = mdao.getListCount();
			list = mdao.getList(page, limit);
		} else { // 검색을 클릭한 경우
			index = Integer.parseInt(request.getParameter("search_field"));
			String[] search_field = new String[] { "id", "name", "age", "gender" };
			search_word = request.getParameter("search_word");
			listcount = mdao.getListCount(search_field[index], search_word);
			list = mdao.getList(search_field[index], search_word, page, limit);
		}

		int maxpage = (listcount + limit - 1) / limit;
		System.out.println("총 페이지수 = " + maxpage);

		int startpage = ((page - 1) / 10) * 10 + 1;
		System.out.println("현재 페이지에 보여줄 시작 페이지 수 = " + startpage);

		// endpage : 현재 페이지 그룹에서 보여줄 마지막 페이지 수([10], [20], [30] 등...)
		int endpage = startpage + 10 - 1;
		System.out.println("현재 페이지에 보여줄 마지막 페이지 수 = " + endpage);

		/*
		 * 마지막 그룹의 마지막 페이지 값은 최대 페이지 값입니다. 예로 마지막 페이지 그룹이 [21]~[30]인 경우
		 * 시작페이지(startpage=21)와 마지막페이지(endpage=30)이지만 최대 페이지(maxpage)가 25라면 [21]~[25]까지만
		 * 표시되도록 합니다.
		 */

		if (endpage > maxpage)
			endpage = maxpage;

		request.setAttribute("page", page); // 현재 페이지 수
		request.setAttribute("maxpage", maxpage); // 최대 페이지 수

		// 현재 페이지에 표시할 첫 페이지 수
		request.setAttribute("startpage", startpage);

		// 현재 페이지에 표시할 끝 페이지 수
		request.setAttribute("endpage", endpage);

		request.setAttribute("listcount", listcount); // 총 글의 수

		// 해당 페이지의 글 목록을 갖고 있는 리스트
		request.setAttribute("limit", limit); // 번호 부분

		request.setAttribute("totallist", list);
		request.setAttribute("search_field", index);
		request.setAttribute("serarch_word", search_word);
		request.setAttribute("listcount", listcount);
		forward.setRedirect(false); // 주소 변경없이 jsp페이지의 내용을 보여줍니다.
		forward.setPath("member/memberList.jsp");
		return forward;

	}

}
