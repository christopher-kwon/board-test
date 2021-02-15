package net.membet.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {

	private DataSource ds;

	// 생성자에서 JNDI 리소스를 참조하여 Connection 개체를 얻어옵니다.
	public MemberDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("DB연결 실패 : " + e);
			return;
		}
	}

	public int insert(Member member) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;

		try {
			conn = ds.getConnection();
			System.out.println("getConnection : insert()");
			String sql = "insert into member" + "(id, password, name, age, gender, email)" + "values(?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getName());
			pstmt.setInt(4, member.getAge());
			pstmt.setString(5, member.getGender());
			pstmt.setString(6, member.getEmail());

			result = pstmt.executeUpdate();

		} catch (SQLIntegrityConstraintViolationException e) {
			result = -1;
			System.out.println("멤버 아이디 중복 에러입니다.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close(); // 4단계 : DB 연결을 끊는다.
				} catch (Exception qe) {
					qe.printStackTrace();
				}

		}

		return result;
	}

	public int isId(String id, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// int result = -1; //아이디가 존재하지 않습니다.
		try {

			conn = ds.getConnection();
			String sql = "SELECT PASSWORD FROM member WHERE ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				if (rs.getString(1).equals(password)) {
					return 1; // 로그인 성공
				} else {
					return 0; // 비밀번호 불일치
				}
			}
			return -1; // 존재하지 않는 아이디
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();

			} catch (Exception xe) {
				xe.printStackTrace();

			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception se) {
				se.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close(); // 4단계 : DB 연결을 끊는다.
			} catch (Exception qe) {
				qe.printStackTrace();

			}

		}

		return -2; // 데이터 베이스 오류
	}

	public int isId(String id) {
		return isId(id, null);
	}

	public Member member_info(String id) {
		Member m = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = ds.getConnection();

			String sql = "select * from member where id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				m = new Member();
				m.setId(rs.getString(1));
				m.setPassword(rs.getString(2));
				m.setName(rs.getString(3));
				m.setAge(rs.getInt(4));
				m.setGender(rs.getString(5));
				m.setEmail(rs.getString(6));
				m.setMemberfile(rs.getString(7));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();

			} catch (Exception xe) {
				xe.printStackTrace();

			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception se) {
				se.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close(); // 4단계 : DB 연결을 끊는다.
			} catch (Exception qe) {
				qe.printStackTrace();

			}

		}

		return m;
	}

	public int update(Member m) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;

		try {
			conn = ds.getConnection();
			String sql = "update member set name = ?, age = ?, gender = ?, email = ?, memberfile = ? where id = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getName());
			pstmt.setInt(2, m.getAge());
			pstmt.setString(3, m.getGender());
			pstmt.setString(4, m.getEmail());
			pstmt.setString(5, m.getMemberfile());
			pstmt.setString(6, m.getId());


			result = pstmt.executeUpdate();

		} catch (SQLIntegrityConstraintViolationException e) {
			result = -1;
			System.out.println("정보 수정에 실패하였습니다. ");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close(); // 4단계 : DB 연결을 끊는다.
				} catch (Exception qe) {
					qe.printStackTrace();
				}

		}

		return result;
	}
	
	public List<Member> getList(int page, int limit) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Member> list = new ArrayList<Member>();

		try {

			conn = ds.getConnection();

			String select_sql = "SELECT * from (select b.*, rownum rnum from (select * from member where id != 'admin' order by id) b) where rnum >= ? and rnum <=?";

			pstmt = conn.prepareStatement(select_sql);
			// 한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지, 4페이지...
			int startrow = (page - 1) * limit + 1;
			// 읽기 시작할 row 번호(1, 11, 21, 31 ...)
			int endrow = startrow + limit - 1;

			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();

			while (rs.next()) { // 더 이상 읽을 데이터가 없을 때까지 반복
				Member m = new Member();

				m.setId(rs.getString(1));
				m.setPassword(rs.getString(2));
				m.setName(rs.getString(3));
				m.setAge(rs.getInt(4));
				m.setGender(rs.getString(5));
				m.setEmail(rs.getString(6));
				list.add(m);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();

			} catch (Exception xe) {
				xe.printStackTrace();

			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception se) {
				se.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close(); // 4단계 : DB 연결을 끊는다.
			} catch (Exception qe) {
				qe.printStackTrace();

			}

		}

		return list;
	}
	
	public List<Member> getList(String field, String value, int page, int limit) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Member> list = new ArrayList<Member>();

		try {

			conn = ds.getConnection();

			String sql = "select * from (select b.*, rownum rnum from (select * from member where id != "
					+ "'admin' and " + field + " like ? order by id) b) where rnum >= ? and rnum <=?";

			System.out.println(sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+value+"%");

			
			int startrow = (page - 1) * limit + 1;
			// 읽기 시작할 row 번호(1, 11, 21, 31 ...)
			int endrow = startrow + limit - 1;

			pstmt.setInt(2, startrow);
			pstmt.setInt(3, endrow);

			rs = pstmt.executeQuery();

			while (rs.next()) { // 더 이상 읽을 데이터가 없을 때까지 반복
				Member m = new Member();

				m.setId(rs.getString(1));
				m.setPassword(rs.getString(2));
				m.setName(rs.getString(3));
				m.setAge(rs.getInt(4));
				m.setGender(rs.getString(5));
				m.setEmail(rs.getString(6));
				list.add(m);
			}
			System.out.println(list.size());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();

			} catch (Exception xe) {
				xe.printStackTrace();

			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception se) {
				se.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close(); // 4단계 : DB 연결을 끊는다.
			} catch (Exception qe) {
				qe.printStackTrace();

			}

		}

		return list;
	}
	

	public int getListCount(String field, String value) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;
		try {

			conn = ds.getConnection();

			String sql = "select count(*) from member where id != 'admin' and " + field + " like ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+value+"%");
			
			rs = pstmt.executeQuery();

			if (rs.next()) { // 더 이상 읽을 데이터가 없을 때까지 반복
				x = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();

			} catch (Exception xe) {
				xe.printStackTrace();

			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception se) {
				se.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close(); // 4단계 : DB 연결을 끊는다.
			} catch (Exception qe) {
				qe.printStackTrace();

			}

		}

		return x;
	}

	public int getListCount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("select count(*) from member where id != 'admin'");
			rs = pstmt.executeQuery();

			if (rs.next()) {
				x = rs.getInt(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("getListCount() 에러 : " + ex);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close(); // 4단계 : DB 연결을 끊는다.
				} catch (Exception qe) {
					qe.printStackTrace();
				}

		}
		return x;
	}

	public int delete(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;

		try {
			conn = ds.getConnection();
			String sql = "delete from member where id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			result = pstmt.executeUpdate();

		} catch (SQLIntegrityConstraintViolationException e) {
			result = -1;
			System.out.println("삭제에 실패하였습니다. ");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close(); // 4단계 : DB 연결을 끊는다.
				} catch (Exception qe) {
					qe.printStackTrace();
				}

		}

		return result;
	}

}
