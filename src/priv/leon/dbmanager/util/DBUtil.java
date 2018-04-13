package priv.leon.dbmanager.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import priv.leon.dbmanager.connectconfig.Constants;

public class DBUtil {

	final String dburl = "jdbc:mysql://127.0.0.1:3306/everglow_users?characterEncoding=utf8&tinyInt1isBit=false&useSSL=true";

	
	public boolean do_update(String sql) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(dburl,"root","root");
			Statement stat = conn.createStatement();
			stat.executeUpdate(sql);

			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	//返回对everglow用户数据库的查询结果
	public List<Map<String, Object>> executeQuery(String sql) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List rslist = new ArrayList();
		StringBuffer sqlPage = new StringBuffer(sql + " ");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dburl,"root","root");
			pstmt = conn.prepareStatement(sqlPage.toString());
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();
			while (rs.next()) {
				Map row = new HashMap(numberOfColumns);
				for (int i = 1; i <= numberOfColumns; ++i) {
					Object o = rs.getObject(i);
					if (("Date".equalsIgnoreCase(rsmd.getColumnTypeName(i))) && (o != null))
						row.put(rsmd.getColumnName(i), formatter.format(o));
					else {
						row.put(rsmd.getColumnName(i), (o == null) ? "" : o);
					}
				}
				rslist.add(row);
			}
		} catch (Exception e) {
			//try {
				//rs.close();
				//pstmt.close();
				//conn.close();
			//} catch (SQLException localSQLException) {
			//}
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException localSQLException2) {
			}
		}
		return rslist;
	}

	public Object setinsertData(String sql) throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection(dburl);

		Statement stmt = null;
		String flagOper = "0";
		try {
			stmt = conn.createStatement();
			return Integer.valueOf(stmt.executeUpdate(sql));
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			}
		}
	}
}