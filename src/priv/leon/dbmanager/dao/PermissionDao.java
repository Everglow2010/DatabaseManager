package priv.leon.dbmanager.dao;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import priv.leon.dbmanager.domain.Page;
import priv.leon.dbmanager.domain.Config;
import priv.leon.dbmanager.connectconfig.Constants;
import priv.leon.dbmanager.util.DBUtil;
import priv.leon.dbmanager.util.DBUtil2;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Repository;

@Repository
public class PermissionDao {
	
	public List<Map<String, Object>> getAllDataBase() throws Exception {//获取初始时所有数据库名列表供左侧 菜单展示（已通）
		//String sql = " select * from  information_schema.schemata ";//获取所有数据库名
		DBUtil2 db = new DBUtil2(Constants.DATABASENAME);
		//List list = DBUtil2.queryForList(sql);
		List list = db.fetchAllDatabaseList();
		List list2 = new ArrayList();
		Map map = new HashMap();
		map.put("SCHEMA_NAME", Constants.DATABASENAME);
		list2.add(map);
		for (int i = 0; i < list.size(); ++i) {//保存所有数据库名以返回
			Map map2 = (Map) list.get(i);
			String schema_name = (String) map2.get("SCHEMA_NAME");

			if (!(schema_name.equals(Constants.DATABASENAME))) {
				list2.add(map2);
			}
		}

		return list2;
	}
	

	//获取所有表格基本信息
	public List<Map<String, Object>> getAllTables(String dbName) throws Exception {
		DBUtil2 db = new DBUtil2(dbName);

		String sql = " select table_name from information_schema.TABLES where table_schema='" + dbName
				+ "' and table_type='BASE TABLE' ";
		List list = DBUtil2.queryForList(sql);

		return list;
	}

	//获取所有视图基本信息
	public List<Map<String, Object>> getAllViews(String dbName) throws Exception {
		String sql = " select table_name   from information_schema.TABLES where table_schema='" + dbName
				+ "' and table_type='VIEW' ";

		DBUtil2 db = new DBUtil2(dbName);
		List list = DBUtil2.queryForList(sql);
		return list;
	}

	//获取所有函数基本信息
	public List<Map<String, Object>> getAllFuntion(String dbName) throws Exception {
		String sql = " select routine_name   from information_schema.ROUTINES where routine_schema='" + dbName + "' ";

		DBUtil2 db = new DBUtil2(dbName);
		List list = DBUtil2.queryForList(sql);
		return list;
	}

	//获取字段基本信息
	public List<Map<String, Object>> getTableColumns(String dbName, String tableName) throws Exception {
		String sql = "select * from  " + dbName + "." + tableName + " limit 1 ";
		DBUtil2 db = new DBUtil2(dbName);
		List list = DBUtil2.queryForColumnOnly(sql);

		return list;
	}

	public List<Map<String, Object>> getTableColumns3(String dbName, String tableName) throws Exception {
		String sql = " select column_name as treeSoftPrimaryKey, column_name,column_type , data_type ,character_maximum_length,is_nullable, column_key, column_comment  from information_schema.columns where   table_name='"
				+ tableName + "' and table_schema='" + dbName + "'  ";

		DBUtil2 db = new DBUtil2(dbName);
		List list = DBUtil2.queryForList(sql);

		return list;
	}

	//获取一页数据
	public Page<Map<String, Object>> getData(Page<Map<String, Object>> page, String tableName, String dbName)
			throws Exception {
		int pageNo = page.getPageNo();
		int pageSize = page.getPageSize();
		int limitFrom = (pageNo - 1) * pageSize;
		String orderBy = page.getOrderBy();
		String order = page.getOrder();

		DBUtil2 db = new DBUtil2(dbName);

		List<Map<String, Object>> list3 = getPrimaryKeyss(dbName, tableName);
		String tem = "";
		for (Map map : list3) {
			tem = tem + map.get("column_name") + ",";
		}
		String primaryKey = "";

		if (!(tem.equals(""))) {
			primaryKey = tem.substring(0, tem.length() - 1);
		}

		String sql = "select count(*) from  " + dbName + "." + tableName;

		String sql2 = "";
		if ((orderBy == null) || (orderBy.equals("")))
			//获取指定数量（pagesize）的查询结果
			sql2 = "select  *  from  " + dbName + "." + tableName + "  LIMIT " + limitFrom + "," + pageSize;
		else {
			sql2 = "select  *  from  " + dbName + "." + tableName + " order by " + orderBy + " " + order + "  LIMIT "
					+ limitFrom + "," + pageSize;
		}

		List list = DBUtil2.queryForList(sql2);

		int rowCount = DBUtil2.executeQueryForCount(sql);

		List<Map<String, Object>> columns = getTableColumns(dbName, tableName);
		List tempList = new ArrayList();

		Map map1 = new HashMap();
		map1.put("field", "treeSoftPrimaryKey");
		map1.put("checkbox", Boolean.valueOf(true));
		tempList.add(map1);

		for (Map map : columns) {
			Map map2 = new HashMap();
			map2.put("field", map.get("column_name"));
			map2.put("title", map.get("column_name"));
			map2.put("sortable", Boolean.valueOf(true));
			map2.put("editor", "text");

			if (map.get("data_type").equals("DATETIME")) {
				map2.put("editor", "datebox");
			} else if ((map.get("data_type").equals("INT")) || (map.get("data_type").equals("SMALLINT"))
					|| (map.get("data_type").equals("TINYINT"))) {
				map2.put("editor", "numberbox");
			} else if (map.get("data_type").equals("DOUBLE")) {
				map2.put("editor", "numberbox");
			} else {
				map2.put("editor", "text");
			}
			tempList.add(map2);
		}

		ObjectMapper mapper = new ObjectMapper();
		String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";

		page.setTotalCount(rowCount);
		page.setResult(list);
		page.setColumns(jsonfromList);
		page.setPrimaryKey(primaryKey);

		return page;
	}

	public Page<Map<String, Object>> executeSql(Page<Map<String, Object>> page, String sql, String dbName)
			throws Exception {
		int pageNo = page.getPageNo();
		int pageSize = page.getPageSize();
		int limitFrom = (pageNo - 1) * pageSize;
		String sql2 = sql + "  LIMIT " + limitFrom + "," + pageSize;
		DBUtil2 db = new DBUtil2(dbName);
		List list = DBUtil2.queryForList(sql2);

		int rowCount = DBUtil2.executeQueryForCount2(sql);

		List<Map<String, Object>> columns = executeSqlForColumns(sql, dbName);
		List tempList = new ArrayList();

		for (Map map : columns) {
			Map map2 = new HashMap();
			map2.put("field", map.get("column_name"));
			map2.put("title", map.get("column_name"));
			map2.put("sortable", Boolean.valueOf(true));
			tempList.add(map2);
		}
		ObjectMapper mapper = new ObjectMapper();
		String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";

		page.setTotalCount(rowCount);
		page.setResult(list);
		page.setColumns(jsonfromList);

		return page;
	}

	public List<Map<String, Object>> executeSqlForColumns(String sql, String dbName) throws Exception {
		String sql2 = sql + "  limit 1 ";
		DBUtil2 db = new DBUtil2(dbName);
		List list = DBUtil2.executeSqlForColumns(sql2);
		return list;
	}

	public String buildUrl(Config config){
		StringBuilder url = new StringBuilder();
		switch (config.getDatabaseType()) {
		case "MySQL": url.append("jdbc:mysql://");
			break;
		case "SQLite": url.append("jdbc:sqlite://");
			break;
		default:
			break;
		}
		url.append(config.getIp());
		url.append(':');
		url.append(config.getPort());
		url.append('/');
		url.append(config.getDatabaseName());
		return url.toString();
	}
	
	//更新连接参数存在参数类中
	public boolean configUpdate(Config config) {
		System.out.println("in configUpdate:" + config.getIp());
		Constants.DATABASENAME = config.getDatabaseName();
		Constants.DATABASETYPE = config.getDatabaseType();
		Constants.DRIVER = config.getDriver();
		Constants.IP = config.getIp();
		Constants.PORT = config.getPort();
		Constants.USERNAME = config.getUserName();
		Constants.PASSWROD = config.getPasswrod();
		Constants.URL = this.buildUrl(config);
		System.out.println("ConfigUpdate "+Constants.URL);
		return true;
	}

	public List<Map<String, Object>> selectUserById(String userId) {
		DBUtil db = new DBUtil();
		String sql = " select * from  treesoft_users where id='" + userId + "' ";
		List list = db.executeQuery(sql);
		return list;
	}

	public boolean updateUserPass(String userId, String newPass) {
		DBUtil db = new DBUtil();
		String sql = " update treesoft_users  set password='" + newPass + "'  where id='" + userId + "'";
		boolean bl = db.do_update(sql);

		return bl;
	}

	public int executeSqlNotRes(String sql, String dbName) throws Exception {
		Map map = new HashMap();
		DBUtil2 db = new DBUtil2(dbName);
		int i = DBUtil2.setupdateData(sql);
		return i;
	}

	//删除数据库中某些数据行
	public int deleteRowsNew(String databaseName, String tableName, String primary_key, List<String> condition)
			throws Exception {
		DBUtil2 db = new DBUtil2(databaseName);
		int y = 0;
		for (int i = 0; i < condition.size(); ++i) {
			String whereStr = (String) condition.get(i);

			String sql = " delete from  " + databaseName + "." + tableName + " where  1=1 " + whereStr;

			y += DBUtil2.setupdateData(sql);
		}
		return y;
	}

	public int saveRows(Map<String, String> map, String databaseName, String tableName) throws Exception {
		DBUtil2 db = new DBUtil2(databaseName);
		String sql = " insert into " + databaseName + "." + tableName;
		int y = 0;
		String colums = " ";
		String values = " ";
		for (Map.Entry entry : map.entrySet()) {
			colums = colums + ((String) entry.getKey()) + ",";

			String str = (String) entry.getValue();
			if (str.equals(""))
				values = values + " null ,";
			else {
				values = values + "'" + ((String) entry.getValue()) + "',";
			}
		}

		colums = colums.substring(0, colums.length() - 1);
		values = values.substring(0, values.length() - 1);
		sql = sql + " (" + colums + ") values (" + values + ")";
		y = DBUtil2.setupdateData(sql);
		return y;
	}

	public List<Map<String, Object>> getOneRowById(String databaseName, String tableName, String id, String idValues) {
		DBUtil2 db = new DBUtil2(databaseName);
		String sql2 = " select * from   " + databaseName + "." + tableName + " where " + id + "='" + idValues + "' ";

		List list = DBUtil2.queryForListWithType(sql2);

		return list;
	}

	public int updateRows(Map<String, Object> map, String databaseName, String tableName, String id, String idValues)
			throws Exception {
		if ((id == null) || ("".equals(id))) {
			throw new Exception("数据不完整,保存失败!");
		}

		if ((idValues == null) || ("".equals(idValues))) {
			throw new Exception("数据不完整,保存失败!");
		}

		DBUtil2 db = new DBUtil2(databaseName);
		String sql = " update  " + databaseName + "." + tableName;

		int y = 0;
		String ss = " set  ";

		for (Map.Entry entry : map.entrySet()) {
			String str = (String) entry.getValue();
			if (str.equals("")) {
				ss = ss + ((String) entry.getKey()) + "= null ,";
			} else if (entry.getValue() instanceof String)
				ss = ss + ((String) entry.getKey()) + "= '" + entry.getValue() + "',";
			else {
				ss = ss + ((String) entry.getKey()) + "= " + entry.getValue() + ",";
			}

		}

		ss = ss.substring(0, ss.length() - 1);
		sql = sql + ss + " where " + id + "='" + idValues + "'";

		y = DBUtil2.setupdateData(sql);
		return y;
	}

	public String getViewSql(String databaseName, String tableName) throws Exception {
		String sql = " select  view_definition  from  information_schema.VIEWS  where  table_name='" + tableName
				+ "' and table_schema='" + databaseName + "'  ";
		String str = "";
		DBUtil2 db = new DBUtil2(databaseName);

		List list = DBUtil2.queryForList(sql);

		if (list.size() == 1) {
			Map map = (Map) list.get(0);
			str = (String) map.get("view_definition");
		}

		return str;
	}

	public List<Map<String, Object>> getTableColumns2(String databaseName, String tableName) throws Exception {
		String sql = "select * from  " + databaseName + "." + tableName + " limit 1";
		DBUtil2 db = new DBUtil2(databaseName);

		List list = DBUtil2.queryForColumnOnly(sql);

		return list;
	}

	public String getPrimaryKeys(String databaseName, String tableName) {
		DBUtil2 db = new DBUtil2(databaseName);
		return db.getPrimaryKeys(databaseName, tableName);
	}

	public List<Map<String, Object>> getPrimaryKeyss(String databaseName, String tableName) throws Exception {
		String sql = " select   column_name  from information_schema.columns where   table_name='" + tableName
				+ "' and table_schema='" + databaseName + "' and column_key='PRI' ";

		DBUtil2 db = new DBUtil2(databaseName);
		List list = DBUtil2.queryForList(sql);

		return list;
	}

	public boolean testConn(String databaseType, String databaseName, String ip, String port, String user, String pass) {
		return DBUtil2.testConnection(databaseType, databaseName, ip, port, user, pass);
	}

	public List<Map<String, Object>> selectSqlStudy() {
		DBUtil db = new DBUtil();
		String sql = " select id, title, content, pid,icon  from  treesoft_study   ";
		List list = db.executeQuery(sql);
		return list;
	}

	//保存表格设计时新增的字段信息行
	public int saveDesginColumn(Map<String, String> map, String databaseName, String tableName) throws Exception {
		DBUtil2 db = new DBUtil2(databaseName);

		String sql = " alter table " + databaseName + "." + tableName + " add column ";

		sql = sql + ((String) map.get("column_name")) + "  ";
		sql = sql + ((String) map.get("data_type"));

		if ((map.get("character_maximum_length") != null) && (!(((String) map.get("character_maximum_length")).equals("")))) {
			sql = sql + " (" + ((String) map.get("character_maximum_length")) + ") ";
		}

		if ((map.get("column_comment") != null) && (!(((String) map.get("column_comment")).equals("")))) {
			sql = sql + " comment '" + ((String) map.get("column_comment")) + "'";
		}

		int y = 0;

		y = DBUtil2.setupdateData(sql);

		return y;
	}

	public int deleteTableColumn(String databaseName, String tableName, String[] ids) throws Exception {
		DBUtil2 db = new DBUtil2(databaseName);
		int y = 0;
		for (int i = 0; i < ids.length; ++i) {
			String sql = " alter table   " + databaseName + "." + tableName + " drop column  " + ids[i];

			y += DBUtil2.setupdateData(sql);
		}
		return y;
	}
	
	//更新表字段
	public int updateTableColumn(Map<String, Object> map, String databaseName, String tableName, String columnName,
			String idValues) throws Exception {
		if ((columnName == null) || ("".equals(columnName))) {
			throw new Exception("数据不完整,保存失败!");
		}

		if ((idValues == null) || ("".equals(idValues))) {
			throw new Exception("数据不完整,保存失败!");
		}

		DBUtil2 db = new DBUtil2(databaseName);

		String old_field_name = (String) map.get("treeSoftPrimaryKey");
		String column_name = (String) map.get("column_name");
		String data_type = (String) map.get("data_type");
		String character_maximum_length = (String) map.get("character_maximum_length");
		String column_comment = (String) map.get("column_comment");

		if (!(old_field_name.endsWith(column_name))) {
			String sql = " alter table  " + databaseName + "." + tableName + " change ";
			sql = sql + old_field_name + " " + column_name;

			int i = DBUtil2.setupdateData(sql);
		}

		String sql2 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " "
				+ data_type;

		if ((character_maximum_length != null) && (!(character_maximum_length.equals("")))) {
			sql2 = sql2 + " (" + character_maximum_length + ")";
		}

		if ((column_comment != null) && (!(column_comment.equals("")))) {
			sql2 = sql2 + " comment '" + column_comment + "'";
		}

		int y = DBUtil2.setupdateData(sql2);

		return y;
	}

	public int dropPrimaryKey(String databaseName, String tableName) throws Exception {
		DBUtil2 db = new DBUtil2(databaseName);
		String sql4 = " alter table  " + databaseName + "." + tableName + " drop primary key ";

		DBUtil2.setupdateData(sql4);

		return 0;
	}

	public int savePrimaryKey2(String databaseName, String tableName, String primaryKeys) throws Exception {
		String sql4 = "";
		if ((primaryKeys != null) && (!(primaryKeys.equals("")))) {
			DBUtil2 db = new DBUtil2(databaseName);
			sql4 = " alter table  " + databaseName + "." + tableName + " add primary key (" + primaryKeys + ")";

			DBUtil2.setupdateData(sql4);
		}
		return 0;
	}

	public int savePrimaryKey(String databaseName, String tableName, String column_name, String isSetting)
			throws Exception {
		String sql4 = "";
		if ((column_name != null) && (!(column_name.equals("")))) {
			DBUtil2 db = new DBUtil2(databaseName);

			List list2 = selectTablePrimaryKey(databaseName, tableName);
			if (isSetting.equals("true"))
				list2.add(column_name);
			else {
				list2.remove(column_name);
			}

			String tem = list2.toString();
			String primaryKey = tem.substring(1, tem.length() - 1);

			if (primaryKey.equals(""))
				sql4 = " alter table  " + databaseName + "." + tableName + " drop primary key ";
			else if ((list2.size() == 1) && (isSetting.equals("true")))
				sql4 = " alter table  " + databaseName + "." + tableName + " add primary key (" + primaryKey + ")";
			else {
				sql4 = " alter table  " + databaseName + "." + tableName + " drop primary key, add primary key ("
						+ primaryKey + ")";
			}
			DBUtil2.setupdateData(sql4);
		}

		return 0;
	}

	public List<String> selectTablePrimaryKey(String databaseName, String tableName) throws Exception {
		String sql = " select column_name   from information_schema.columns where   table_name='" + tableName
				+ "' and table_schema='" + databaseName + "'  and column_key='PRI' ";

		DBUtil2 db = new DBUtil2(databaseName);
		List<Map<String, Object>> list = DBUtil2.queryForList(sql);

		List list2 = new ArrayList();

		for (Map map : list) {
			list2.add((String) map.get("column_name"));
		}

		return list2;
	}

	public String selectOneColumnType(String databaseName, String tableName, String column_name) throws Exception {
		String sql = " select   column_type  from information_schema.columns where   table_name='" + tableName
				+ "' and table_schema='" + databaseName + "' and column_name='" + column_name + "'";

		DBUtil2 db = new DBUtil2(databaseName);
		List list = DBUtil2.queryForList(sql);
		return ((String) ((Map) list.get(0)).get("column_type"));
	}

	public int updateTableNullAble(String databaseName, String tableName, String column_name, String is_nullable)
			throws Exception {
		String sql4 = "";
		if ((column_name != null) && (!(column_name.equals("")))) {
			DBUtil2 db = new DBUtil2(databaseName);
			String column_type = selectOneColumnType(databaseName, tableName, column_name);

			if (is_nullable.equals("true"))
				sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " "
						+ column_type + "  null ";
			else {
				sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " "
						+ column_type + " not null ";
			}
			DBUtil2.setupdateData(sql4);
		}
		return 0;
	}

	public int upDownColumn(String databaseName, String tableName, String column_name, String column_name2)
			throws Exception {
		String sql4 = "";
		if ((column_name != null) && (!(column_name.equals("")))) {
			DBUtil2 db = new DBUtil2(databaseName);
			String column_type = selectOneColumnType(databaseName, tableName, column_name);
			if ((column_name2 == null) || (column_name2.equals("")))
				sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " "
						+ column_type + " first ";
			else {
				sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " "
						+ column_type + " after " + column_name2;
			}

			DBUtil2.setupdateData(sql4);
		}
		return 0;
	}
}