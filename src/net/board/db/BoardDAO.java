package net.board.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {

	private DataSource ds;

	// �����ڿ��� JNDI ���ҽ��� �����Ͽ� Connection ��ü�� ���ɴϴ�.
	public BoardDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("DB���� ���� : " + e);
			return;
		}
	}

	public boolean boardInsert(BoardBean board) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			String max_sql = "(select nvl(max(board_num), 0)+1 from board)";

			String SQL = "INSERT INTO board VALUES(" + max_sql + ", ?, ?, ?, ?, ?, " + max_sql + ", ?, ?, ?, SYSDATE)";

			conn = ds.getConnection();
			pstmt = conn.prepareStatement(SQL);

			pstmt.setString(1, board.getBoard_name());
			pstmt.setString(2, board.getBoard_pass());
			pstmt.setString(3, board.getBoard_subject());
			pstmt.setString(4, board.getBoard_content());
			pstmt.setString(5, board.getBoard_file());

			// ������ ��� BOARd_RE_LEV, BOARD_RE_SEQ �ʵ� ���� 0�Դϴ�.
			pstmt.setInt(6, 0); // BOARd_RE_LEV
			pstmt.setInt(7, 0); // BOARd_RE_SEQ
			pstmt.setInt(8, 0); // BOARd_READCOUNT �ʵ�

			result = pstmt.executeUpdate();

			if (result == 1) {
				System.out.println("������ ������ ��� �Ϸ�Ǿ����ϴ�.");
				return true;
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
		return false;
	}

	public int getListCount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("select count(*) from board");
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

	public List<BoardBean> getBoardList(int page, int limit) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// page : ������
		// limit : ������ �� ����� ��
		// board_re_ref desc, board_re_seq asc�� ���� ������ ����
		// �������� �´� rnum�� ���� ��ŭ �������� �������Դϴ�.

		String board_list_sql = "select * from " + " (select rownum rnum, board_num, board_name,"
				+ " board_subject, board_content, board_file," + " board_re_ref, board_re_lev, board_re_seq,"
				+ " board_readcount, board_date" + " from (select * from board " + " order by board_re_ref desc,"
				+ " board_re_seq asc)" + " )" + " where rnum >= ? and rnum <= ?";

		List<BoardBean> list = new ArrayList<BoardBean>();
		// �� �������� 10���� ����� ��� 1������, 2������, 3������, 4������...
		int startrow = (page - 1) * limit + 1; // �б� ������ row ��ȣ(1 11 21 31 ...
		int endrow = startrow + limit - 1; // ���� ������ row ��ȣ(10 20 30 40 ...
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(board_list_sql);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();

			// DB���� ������ �����͸� VO ��ü�� ����ϴ�.
			while (rs.next()) {
				BoardBean board = new BoardBean();
				board.setBoard_num(rs.getInt("board_num"));
				board.setBoard_name(rs.getString("board_name"));
				board.setBoard_subject(rs.getString("board_subject"));
				board.setBoard_content(rs.getString("board_content"));
				board.setBoard_file(rs.getString("board_file"));
				board.setBoard_re_ref(rs.getInt("board_re_ref"));
				board.setBoard_re_lev(rs.getInt("board_re_lev"));
				board.setBoard_re_seq(rs.getInt("board_re_seq"));
				board.setBoard_readcount(rs.getInt("board_readcount"));
				board.setBoard_date(rs.getString("board_date"));
				list.add(board);
			}

		}

		catch (Exception ex) {
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

		return list;
	}

	public BoardBean getDetail(int num) {
		BoardBean board = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String SQL = "SELECT * FROM board WHERE board_num = ?";
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				board = new BoardBean();
				board.setBoard_num(rs.getInt("board_num"));
				board.setBoard_name(rs.getString("board_name"));
				board.setBoard_subject(rs.getString("board_subject"));
				board.setBoard_content(rs.getString("board_content"));
				board.setBoard_file(rs.getString("board_file"));
				board.setBoard_re_ref(rs.getInt("board_re_ref"));
				board.setBoard_re_lev(rs.getInt("board_re_lev"));
				board.setBoard_re_seq(rs.getInt("board_re_seq"));
				board.setBoard_readcount(rs.getInt("board_readcount"));
				board.setBoard_date(rs.getString("board_date"));

			}

		} catch (Exception e) {
			System.out.println("getDetail() ���� : " + e);
			e.printStackTrace();
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
		return board;
	}

	public void setReadCountUpdate(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "update board set board_readcount = board_readcount + 1 where board_num = ?";

		try {

			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();

		} catch (Exception ex) {
			System.out.println("setReadCountUpdate() ���� : " + ex);
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

	}

	public int boardReply(BoardBean board) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int num = 0;

		// board ���̺��� �۹�ȣ�� ���ϱ� ���� board_num �ʵ��� �ִ밪�� ���ؿɴϴ�.
		String board_max_sql = "select max(board_num)+1 from board";

		/*
		 * �亯�� �� ���� �� �׷� ��ȣ�Դϴ�. �亯�� �ް� �Ǹ� �亯 ���� �� ��ȣ�� ���� ���ñ� ��ȣ�� ���� ó���Ǹ鼭 ���� �׷쿡 ���ϰ�
		 * �˴ϴ�. �� ��Ͽ��� ������ �� �ϳ��� �׷����� ������ ��µ˴ϴ�.
		 */

		int re_ref = board.getBoard_re_ref();

		/*
		 * ����� ���̸� �ǹ��մϴ�. ������ ���� ����� ��µ� �� �� �� �鿩���� ó���� �ǰ� ��ۿ� ���� ����� �鿩���Ⱑ �� �� ó���ǰ�
		 * �մϴ�. ������ ��쿡�� �� ���� 0�̰� ������ ����� 1, ����� ����� 2�� �˴ϴ�.
		 */

		int re_lev = board.getBoard_re_lev();

		// ���� ���� �� �߿��� �ش� ���� ��µǴ� �����Դϴ�.
		int re_seq = board.getBoard_re_seq();

		try {
			conn = ds.getConnection();

			// Ʈ������� �̿��ϱ� ���ؼ� setAutoCommit�� false�� �����մϴ�.
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(board_max_sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				num = rs.getInt(1);
			}
			pstmt.close();

			// BOARD_RE_REF, board_re_seq ���� Ȯ���Ͽ� ���� �ۿ� �ٸ� ����� ������
			// �ٸ� ��۵��� board_re_seq ���� 1�� ������ŵ�ϴ�.
			// ���� ���� �ٸ� ��ۺ��� �տ� ��µǰ� �ϱ� ���ؼ��Դϴ�.

			String sql = "update board set board_re_seq = board_re_seq + 1 where board_re_ref = ? and board_re_seq > ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, re_ref);
			pstmt.setInt(2, re_seq);
			pstmt.executeUpdate();
			pstmt.close();

			// ����� �亯 ���� board_re_lev, board_re_seq ���� ���� �ۺ��� 1�� ������ŵ�ϴ�.
			re_seq = re_seq + 1;
			re_lev = re_lev + 1;

			sql = "insert into board (board_num, board_name, board_pass, board_subject, board_content, board_file, board_re_ref, board_re_lev, board_re_seq, board_readcount) values("
					+ num + ", ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoard_name());
			pstmt.setString(2, board.getBoard_pass());
			pstmt.setString(3, board.getBoard_subject());
			pstmt.setString(4, board.getBoard_content());
			pstmt.setString(5, ""); // �亯���� ������ ���ε� ���� �ʽ��ϴ�.
			pstmt.setInt(6, re_ref);
			pstmt.setInt(7, re_lev);
			pstmt.setInt(8, re_seq);
			pstmt.setInt(9, 0); // board_readcount(��ȸ��)�� 0

			if (pstmt.executeUpdate() == 1) {
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
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		}

		return num;

	}

	public boolean boardModify(BoardBean board) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "update board set board_subject = ?, board_content = ?, board_file = ? where board_num = ?";

		try {
			conn = ds.getConnection();

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoard_subject());
			pstmt.setString(2, board.getBoard_content());
			pstmt.setString(3, board.getBoard_file());
			pstmt.setInt(4, board.getBoard_num());
			int result = pstmt.executeUpdate();

			if (result == 1) {
				System.out.println("���� ������Ʈ");
				return true;
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

		return true;

	}

	public boolean isBoardWriter(int num, String pass) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean result = false;
		String sql = "SELECT board_pass FROM board WHERE board_num = ?";

		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals(pass)) {
					result = true; // �۹�ȣ�� ��й�ȣ ��ġ
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("isBoardWriter() ���� : " + e);
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
					conn.close(); // 4�ܰ� : DB ������ ���´�.
			} catch (Exception qe) {
				qe.printStackTrace();

			}

		}
		return result; // ��й�ȣ ����ġ

	}

	public boolean boardDelete(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		String select_sql = "select board_re_ref, board_re_lev, board_re_seq from board where board_num = ?";

		String board_delete_sql = "delete from board where board_re_ref = ? and board_re_lev >= ? "
				+ "and board_re_seq >= ? and board_re_seq <= (nvl((select min(board_re_seq)-1 from board "
				+ "where board_re_ref = ? and board_re_lev = ? and board_re_seq > ?), (select max(board_re_seq) "
				+ "from board where board_re_ref = ?)))";

		boolean result_check = false;

		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(select_sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				pstmt2 = conn.prepareStatement(board_delete_sql);
				pstmt2.setInt(1, rs.getInt("board_re_ref"));
				pstmt2.setInt(2, rs.getInt("board_re_lev"));
				pstmt2.setInt(3, rs.getInt("board_re_seq"));
				pstmt2.setInt(4, rs.getInt("board_re_ref"));
				pstmt2.setInt(5, rs.getInt("board_re_lev"));
				pstmt2.setInt(6, rs.getInt("board_re_seq"));
				pstmt2.setInt(7, rs.getInt("board_re_ref"));

				int count = pstmt2.executeUpdate();

				if (count >= 1)
					result_check = true; // ���� �ȵ� ��쿡�� false�� ��ȯ�մϴ�.
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("boardDelete() ���� : " + ex);

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

		return result_check;

	}

}
