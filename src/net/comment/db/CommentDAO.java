package net.comment.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CommentDAO {

	private DataSource ds;

	// �����ڿ��� JNDI ���ҽ��� �����Ͽ� Connection ��ü�� ���ɴϴ�.
	public CommentDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("DB���� ���� : " + e);
			return;
		}
	}

	public int getListCount(int BOARD_RE_REF) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("select count(*) from comm where comment_board_num = ?");
			pstmt.setInt(1, BOARD_RE_REF);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				x = rs.getInt(1);
			}
		} catch (Exception ex) {
			System.out.println("getListCount() ���� : " + ex);
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
					conn.close(); // 4�ܰ� : DB ������ ���´�.
				} catch (Exception qe) {
					qe.printStackTrace();
				}

		}
		return x;
	}
	
	
	public JsonArray getCommentList(int comment_board_num, int state) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sort = "asc";
		if(state==2) {
			sort="desc";
		}

		String sql = "select * from ( select b.*, rownum rnum from(select num, comm.id, content, reg_date, "
				+ "comment_re_lev, comment_re_seq, comment_re_ref, member.memberfile from comm join member "
				+ "on comm.id=member.id where comment_board_num = ? order by comment_re_ref " + sort + ", " 
				+ "comment_re_seq asc)b"
				+ ")";
		
		JsonArray array = new JsonArray();
		
		// �� �������� 10���� ����� ��� 1������, 2������, 3������, 4������...
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, comment_board_num);
			rs = pstmt.executeQuery();

			// DB���� ������ �����͸� VO ��ü�� ����ϴ�.
			while (rs.next()) {
				JsonObject object = new JsonObject();
				object.addProperty("num", rs.getInt(1));
				object.addProperty("id", rs.getString(2));
				object.addProperty("content", rs.getString(3));
				object.addProperty("reg_date", rs.getString(4));
				object.addProperty("comment_re_lev", rs.getInt(5));
				object.addProperty("comment_re_seq", rs.getInt(6));
				object.addProperty("comment_re_ref", rs.getInt(7));
				object.addProperty("memberfile", rs.getString(8));
				array.add(object);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("getBoardList() ���� : " + ex);
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
					conn.close(); // 4�ܰ� : DB ������ ���´�.
				} catch (Exception qe) {
					qe.printStackTrace();
				}

		}

		return array;
	}
	
	
	
	public int commentsInsert(Comment comm) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {

			String SQL = "INSERT INTO comm VALUES(comm_seq.nextval, ?, ?, SYSDATE, ?, ?, ?, comm_seq.nextval)";

			conn = ds.getConnection();
			pstmt = conn.prepareStatement(SQL);

			pstmt.setString(1, comm.getComment_id());
			pstmt.setString(2, comm.getComment_content());
			pstmt.setInt(3, comm.getComment_board_num());
			pstmt.setInt(4, comm.getComment_re_lev());
			pstmt.setInt(5, comm.getComment_re_seq());


			result = pstmt.executeUpdate();

			if (result == 1) {
				System.out.println("������ ������ ��� �Ϸ�Ǿ����ϴ�.");
			} else {
				System.out.println("������ ���� ����");
			}

		} catch (Exception e) {
			System.out.println("boardInsert() ���� : " + e);
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
					conn.close(); // 4�ܰ� : DB ������ ���´�.
				} catch (Exception qe) {
					qe.printStackTrace();
				}

		}
		
		return result;
	}
	
	public int commentsDelete(int num) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	ResultSet rs = null;
	String select_sql = "select comment_re_ref, comment_re_lev, comment_re_seq from comm where num = ?";

	String delete_sql = "delete from comm where comment_re_ref = ? and comment_re_lev >= ? "
			+ "and comment_re_seq >= ? and comment_re_seq <= (nvl((select min(comment_re_seq)-1 from comm "
			+ "where comment_re_ref = ? and comment_re_lev = ? and comment_re_seq > ?), (select max(comment_re_seq) "
			+ "from comm where comment_re_ref = ?)))";
	

	int count = 0;

	try {
		conn = ds.getConnection();
		pstmt = conn.prepareStatement(select_sql);
		pstmt.setInt(1, num);
		rs = pstmt.executeQuery();

		if (rs.next()) {
			pstmt2 = conn.prepareStatement(delete_sql);
			pstmt2.setInt(1, rs.getInt("comment_re_ref"));
			pstmt2.setInt(2, rs.getInt("comment_re_lev"));
			pstmt2.setInt(3, rs.getInt("comment_re_seq"));
			pstmt2.setInt(4, rs.getInt("comment_re_ref"));
			pstmt2.setInt(5, rs.getInt("comment_re_lev"));
			pstmt2.setInt(6, rs.getInt("comment_re_seq"));
			pstmt2.setInt(7, rs.getInt("comment_re_ref"));

			count = pstmt2.executeUpdate();


		}

	} catch (SQLException ex) {
		ex.printStackTrace();
		System.out.println("commentsDelete() ���� : " + ex);

	} finally {
		if (rs != null)
			try {
				rs.close();

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		if (pstmt != null)
			try {
				pstmt.close();

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
	}

	return count;

}

	public int commentsReply(Comment comm) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;

		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String update_sql = "update comm set comment_re_seq = comment_re_seq + 1 where comment_re_ref = ? and comment_re_seq > ?";
			
			pstmt = conn.prepareStatement(update_sql);
			pstmt.setInt(1, comm.getComment_re_ref());
			pstmt.setInt(2, comm.getComment_re_seq());
			pstmt.executeUpdate();
			pstmt.close();

			String sql = "insert into comm values(comm_seq.nextval, ?, ?, SYSDATE, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comm.getComment_id());
			pstmt.setString(2, comm.getComment_content());
			pstmt.setInt(3, comm.getComment_board_num());
			pstmt.setInt(4, comm.getComment_re_lev()+1);
			pstmt.setInt(5, comm.getComment_re_seq()+1);
			pstmt.setInt(6, comm.getComment_re_ref());
			result = pstmt.executeUpdate();

			if (result == 1) {
				System.out.println("reply ���� �Ϸ�");
				conn.commit(); // commit �մϴ�.
			} else {
				conn.rollback();
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("boardReply() ���� : " + ex);
			if (conn != null) {
				try {
					conn.rollback(); // rollback �մϴ�.
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} finally {

			if (pstmt != null)
				try {
					pstmt.close();

				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (conn != null)
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		}

		return result;

	}

	public int commentsUpdate(Comment co) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "update comm set content = ? where num = ?";

		try {
			conn = ds.getConnection();

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, co.getComment_content());
			pstmt.setInt(2, co.getComment_num());

			result = pstmt.executeUpdate();

			if (result == 1) {
				System.out.println("��� ���� �Ϸ�");
				return result;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("boardModify() ���� : " + ex);

		} finally {

			if (pstmt != null)
				try {
					pstmt.close();

				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		}

		return result;

	}

//	public boolean isBoardWriter(int num, String pass) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		boolean result = false;
//		String sql = "SELECT board_pass FROM board WHERE board_num = ?";
//
//		try {
//			conn = ds.getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, num);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				if (rs.getString(1).equals(pass)) {
//					result = true; // �۹�ȣ�� ��й�ȣ ��ġ
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("isBoardWriter() ���� : " + e);
//		} finally {
//			try {
//				if (rs != null)
//					rs.close();
//
//			} catch (Exception xe) {
//				xe.printStackTrace();
//
//			}
//			try {
//				if (pstmt != null)
//					pstmt.close();
//			} catch (Exception se) {
//				se.printStackTrace();
//			}
//			try {
//				if (conn != null)
//					conn.close(); // 4�ܰ� : DB ������ ���´�.
//			} catch (Exception qe) {
//				qe.printStackTrace();
//
//			}
//
//		}
//		return result; // ��й�ȣ ����ġ
//
//	}
//

}
