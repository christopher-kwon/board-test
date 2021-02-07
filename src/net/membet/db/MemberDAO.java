package net.membet.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {

	private DataSource ds;
	
	//�����ڿ��� JNDI ���ҽ��� �����Ͽ� Connection ��ü�� ���ɴϴ�.
	public MemberDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("DB���� ���� : " + e);
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
			System.out.println("��� ���̵� �ߺ� �����Դϴ�.");

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
					conn.close(); // 4�ܰ� : DB ������ ���´�.
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
		//int result = -1; //���̵� �������� �ʽ��ϴ�. 
		try {

			conn = ds.getConnection();
			String sql = "SELECT PASSWORD FROM member WHERE ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				if (rs.getString(1).equals(password)) {
					return 1; // �α��� ����
				} else {
					return 0; // ��й�ȣ ����ġ
				}
			}
			return -1; // �������� �ʴ� ���̵�
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
					conn.close(); // 4�ܰ� : DB ������ ���´�.
			} catch (Exception qe) {
				qe.printStackTrace();

			}

		}

		return -2; // ������ ���̽� ����
	}
	
	public int isId(String id) {
		return isId(id, null);
	}
	
	
}
