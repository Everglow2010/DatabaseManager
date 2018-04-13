package priv.leon.dbmanager.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
import priv.leon.dbmanager.connectconfig.DataBaseTypes;

public class DBUtil2 {
	//Constants constants = new Constants();
	private static String driver = null;
	private static String databaseName = null;
	private static String url = null;
	private static String userName = null;
	private static String passwrod = null;
	private static String port = null;
	private static String ip = null;

	//根据现有配置信息生成连接数据库的url
	public String buildUrl(){
		StringBuilder url = new StringBuilder();
		switch (Constants.DATABASETYPE) {
		case "MySQL": url.append("jdbc:mysql://");
			break;
		case "SQLsever": url.append("jdbc:sqlite://");
			break;
		default:
			break;
		}
		url.append(ip);
		url.append(':');
		url.append(port);
		url.append('/');
		url.append(databaseName);
		System.out.println("in build url: " + url.toString());
		return url.toString();
	}
	
	public DBUtil2(String dbName) {
		driver = Constants.DRIVER;
		ip = Constants.IP;
		port = Constants.PORT;
		userName = Constants.USERNAME;
		passwrod = Constants.PASSWROD;
		databaseName = dbName;
		url = buildUrl() + "?characterEncoding=utf8&tinyInt1isBit=false&useSSL=true";
		//System.out.println("DBUtil2 URL: " + url);
	}

	//根据当前连接参数获取对当前数据库的连接
	public static final synchronized Connection getConnection() {
		try {
			Class.forName(driver);
			//System.out.println("in get connection: " + url + userName + passwrod);
			Connection conn = DriverManager.getConnection(url, userName, passwrod);

			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//测试连接
	public static boolean testConnection(String databaseType2, String databaseName2, String ip2, String port2, String user2, String pass2) {
		try {
			
			StringBuilder url2 = new StringBuilder();
			switch (databaseType2) {
			case "MySQL": url2.append("jdbc:mysql://");
				break;
			case "SQLite": url2.append("jdbc:sqlite://");
				break;
			default:
				break;
			}
			url2.append(ip2);
			url2.append(':');
			url2.append(port2);
			url2.append('/');
			url2.append(databaseName2);
			url2.append("?characterEncoding=utf8&tinyInt1isBit=false&useSSL=true");
			String driver2 = DataBaseTypes.driverClassName.get(databaseType2);
			
			System.out.println(url2.toString());
			
			Class.forName(driver2);
			Connection conn = DriverManager.getConnection(url2.toString(), user2, pass2);

			return (conn != null);
		} catch (Exception e) {
		}

		return false;
	}
	
	
	public static boolean testConn() {
		boolean bl = false;
		Connection conn = getConnection();
		if (conn != null)
			bl = true;
		try {
			conn.close();
		} catch (Exception localException) {
		}
		return bl;
	}
	
	//执行更新语句，返回影响行数
	public static int setupdateData(String sql) throws Exception {
		System.out.println("setupdateData执行:"+sql);
		Connection conn = getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			return stmt.executeUpdate(sql);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new Exception(e.getMessage());
			}
		}
	}

	//利用JDBC无差别获取数据库产品下所有数据库名
	public static  List<Map<String, Object>> fetchAllDatabaseList() throws Exception {
		Connection conn = null;
		ResultSet rs = null;
		DatabaseMetaData dmd = null;
		ResultSetMetaData rsmd = null;
		int columnCount = -1;
		String[] fields = (String[]) null;
		List times = new ArrayList();
		List rows = new ArrayList();
		Map row = null;
		
		conn = getConnection();
		dmd = conn.getMetaData();
		rs = dmd.getCatalogs();
		rsmd = rs.getMetaData();
		
		columnCount = rsmd.getColumnCount();//获取结果集字段数
		fields = new String[columnCount];
		
		while (rs.next()) {
			row = new HashMap();
			for (int i = 0; i < columnCount; ++i) {//将每一行存成一个map<string,object>
				Object value = rs.getObject("TABLE_CAT");
				row.put("SCHEMA_NAME", value);
			}
			rows.add(row);
		}
		
		return rows;
	}
	
	//执行指定SQL语句，获取的数据包装成List<Map<String, Object>>,每个map为一行数据，key为字段名，值为该字段值
	public static List<Map<String, Object>> queryForList(String sql) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		int columnCount = -1;
		String[] fields = (String[]) null;
		List times = new ArrayList();
		List rows = new ArrayList();
		Map row = null;

		conn = getConnection();
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		rsmd = rs.getMetaData();

		columnCount = rsmd.getColumnCount();//获取结果集字段数
		fields = new String[columnCount];
		for (int i = 0; i < columnCount; ++i) {//获取所有字段名以用作表头
			fields[i] = rsmd.getColumnLabel(i + 1);
			if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {//对时间记录单独处理
				times.add(fields[i]);
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		while (rs.next()) {
			row = new HashMap();
			for (int i = 0; i < columnCount; ++i) {//将每一行存成一个map<string,object>
				Object value = (times.contains(fields[i])) ? rs.getTimestamp(fields[i]) : rs.getObject(fields[i]);
				if ((times.contains(fields[i])) && (value != null)) {
					value = sdf.format(value);//时间记录处理成为String
				}
				row.put(fields[i], value);
			}
			rows.add(row);
		}
		try {
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}

		return rows;
	}
	
	
	//获取指定sql查询结果集中字段的控制信息
	public static List<Map<String, Object>> queryForListWithType(String sql) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List rows = new ArrayList();
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			rs.next();
			for (int i = 1; i < columnCount + 1; ++i) {
				Map<String, Object> map = new HashMap<String, Object>();

				map.put("column_name", rsmd.getColumnName(i));//字段名

				map.put("column_value", rs.getObject(rsmd.getColumnName(i)));//字段值

				map.put("data_type", rsmd.getColumnTypeName(i));//字段类型

				map.put("precision", Integer.valueOf(rsmd.getPrecision(i)));//字段长度或精度

				map.put("isAutoIncrement", Boolean.valueOf(rsmd.isAutoIncrement(i)));//字段是否是自动增加生成的

				map.put("is_nullable", Integer.valueOf(rsmd.isNullable(i)));//是否可空

				map.put("isReadOnly", Boolean.valueOf(rsmd.isReadOnly(i)));//是否是只读的

				rows.add(map);
			}
		} catch (Exception e) {
			System.out.println("queryForListWithType  " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException localSQLException1) {
			}
		}
		return rows;
	}
	
	//只查询指定sql语句获取的结果集的字段信息
	public static List<Map<String, Object>> queryForColumnOnly(String sql) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List rows = new ArrayList();
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsme = rs.getMetaData();
			int columnCount = rsme.getColumnCount();

			for (int i = 1; i < columnCount + 1; ++i) {
				Map map = new HashMap();

				map.put("column_name", rsme.getColumnName(i));

				map.put("data_type", rsme.getColumnTypeName(i));

				map.put("precision", Integer.valueOf(rsme.getPrecision(i)));

				map.put("isAutoIncrement", Boolean.valueOf(rsme.isAutoIncrement(i)));

				map.put("is_nullable", Integer.valueOf(rsme.isNullable(i)));

				map.put("isReadOnly", Boolean.valueOf(rsme.isReadOnly(i)));

				rows.add(map);
			}
		} catch (Exception e) {
			System.out.println("queryForColumnOnly  " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException localSQLException1) {
			}
		}
		return rows;
	}

	
	//只获取查询结果集的字段名
	public static List<Map<String, Object>> executeSqlForColumns(String sql) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		int maxSize = -1;
		List rows = new ArrayList();

		conn = getConnection();
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		rsmd = rs.getMetaData();
		maxSize = rsmd.getColumnCount();

		for (int i = 0; i < maxSize; ++i) {
			Map map = new HashMap();

			map.put("column_name", rsmd.getColumnLabel(i + 1));

			rows.add(map);
		}

		rs.close();
		pstmt.close();
		conn.close();

		return rows;
	}

	//查询以获取结果集记录总行数（？？？？有问题）
	public static int executeQueryForCount(String sql) {
		int rowCount = 0;
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Long count = (Long) rs.getObject("count(*)");
				rowCount = count.intValue();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException localSQLException1) {
			}
		}
		return rowCount;
	}
	
	//利用移动游标获取结果集总行数
	public static int executeQueryForCount2(String sql) {
		int rowCount = 0;
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			rs.last();
			rowCount = rs.getRow();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException localSQLException1) {
			}
		}
		return rowCount;
	}

	
	public static boolean executeQuery(String sql) {
		boolean bl = false;
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())  bl = true;
		} catch (Exception localException) {
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException localSQLException1) {
			}
		}
		return bl;
	}


	//获取指定表格的主键信息
	public String getPrimaryKeys(String databaseName, String tableName) {
		Connection conn = null;

		List rows2 = new ArrayList();
		try {
			conn = getConnection();
			DatabaseMetaData metadata = conn.getMetaData();

			ResultSet rs2 = metadata.getPrimaryKeys(databaseName, null, tableName);
			if (rs2.next()) System.out.println("主键名称: " + rs2.getString(4));
			return rs2.getString(4);
		} catch (Exception e) {
			System.out.println("getPrimaryKeys  " + e.getMessage());
		} finally {
			try {
				conn.close();
			} catch (SQLException localSQLException2) {
			}
		}
		try {
			conn.close();
		} catch (SQLException localSQLException3) {
		}
		return "";
	}

	//获取指定数据库中指定表格的主键列表
	public List<String> getPrimaryKeyss(String databaseName, String tableName) {
		Connection conn = null;

		List rows = new ArrayList();
		try {
			conn = getConnection();
			DatabaseMetaData metadata = conn.getMetaData();

			ResultSet rs = metadata.getPrimaryKeys(databaseName, null, tableName);
			while (rs.next()) {
				System.out.println("主键名称2: " + rs.getString(4));
				rows.add(rs.getString(4));
			}
		} catch (Exception e) {
			System.out.println("getPrimaryKeyss  " + e.getMessage());
		} finally {
			try {
				conn.close();
			} catch (SQLException localSQLException1) {
			}
		}
		return rows;
	}
}