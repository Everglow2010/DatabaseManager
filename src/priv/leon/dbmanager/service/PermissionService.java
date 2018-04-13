/**
 * 
 */
package priv.leon.dbmanager.service;

/**
 * @author Everglow
 *
 */

import com.alibaba.fastjson.JSONArray;
import priv.leon.dbmanager.domain.Page;
import priv.leon.dbmanager.dao.PermissionDao;
import priv.leon.dbmanager.domain.Config;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PermissionService {

	@Autowired
	private PermissionDao permissionDao;

	public List<Map<String, Object>> getAllDataBase() throws Exception {
		return this.permissionDao.getAllDataBase();//初始获取所有数据库列表以供左侧树形菜单展示（已通）
	}

	public List<Map<String, Object>> getAllTables(String dbName) throws Exception {
		return this.permissionDao.getAllTables(dbName);
	}

	public List<Map<String, Object>> getAllViews(String dbName) throws Exception {
		return this.permissionDao.getAllViews(dbName);
	}

	public List<Map<String, Object>> getAllFuntion(String dbName) throws Exception {
		return this.permissionDao.getAllFuntion(dbName);
	}

	public List<Map<String, Object>> getTableColumns(String dbName, String tableName) throws Exception {
		return this.permissionDao.getTableColumns(dbName, tableName);
	}

	public Page<Map<String, Object>> getData(Page<Map<String, Object>> page, String tableName, String dbName)
			throws Exception {
		return this.permissionDao.getData(page, tableName, dbName);
	}

	public Page<Map<String, Object>> executeSql(Page<Map<String, Object>> page, String sql, String dbName)
			throws Exception {
		return this.permissionDao.executeSql(page, sql, dbName);
	}

	public List<Map<String, Object>> executeSqlForColumns(String sql, String dbName) throws Exception {
		return this.permissionDao.executeSqlForColumns(sql, dbName);
	}
	
	public boolean configUpdate(Config config) {
		return this.permissionDao.configUpdate(config);
	}

	public String changePassUpdate(String userId, String oldPass, String newPass) {
		List list = this.permissionDao.selectUserById(userId);
		String oldPass2 = "";

		if (list.size() > 0) {
			Map map = (Map) list.get(0);
			oldPass2 = (String) map.get("password");
		}
		if (!(oldPass.equals(oldPass2))) {
			return "旧密码不符！";
		}

		this.permissionDao.updateUserPass(userId, newPass);
		return "success";
	}

	public int executeSqlNotRes(String sql, String dbName) throws Exception {
		return this.permissionDao.executeSqlNotRes(sql, dbName);
	}

	public int deleteRowsNew(String databaseName, String tableName, String primary_key, List<String> condition)
			throws Exception {
		return this.permissionDao.deleteRowsNew(databaseName, tableName, primary_key, condition);
	}

	public int saveRows(Map map, String databaseName, String tableName) throws Exception {
		return this.permissionDao.saveRows(map, databaseName, tableName);
	}

	public List<Map<String, Object>> getOneRowById(String databaseName, String tableName, String id, String idValues) {
		return this.permissionDao.getOneRowById(databaseName, tableName, id, idValues);
	}

	public int updateRows(Map map, String databaseName, String tableName, String id, String idValues) throws Exception {
		return this.permissionDao.updateRows(map, databaseName, tableName, id, idValues);
	}

	public int updateRowsNew(String databaseName, String tableName, List<String> strList) throws Exception {
		String sql = "";
		for (String str1 : strList) {
			if ((str1 == null) || ("".equals(str1))) {
				throw new Exception("数据不完整,保存失败!");
			}
			sql = " update  " + databaseName + "." + tableName + str1;

			this.permissionDao.executeSqlNotRes(sql, databaseName);
		}
		return 0;
	}

	public String getViewSql(String databaseName, String tableName) throws Exception {
		return this.permissionDao.getViewSql(databaseName, tableName);
	}

	public List<Map<String, Object>> getTableColumns2(String databaseName, String tableName) throws Exception {
		return this.permissionDao.getTableColumns2(databaseName, tableName);
	}

	public List<Map<String, Object>> getTableColumns3(String databaseName, String tableName) throws Exception {
		return this.permissionDao.getTableColumns3(databaseName, tableName);
	}

	public String getPrimaryKeys(String databaseName, String tableName) {
		return this.permissionDao.getPrimaryKeys(databaseName, tableName);
	}

	public boolean testConn(String databaseType, String databaseName, String ip, String port, String user, String pass) {
		return this.permissionDao.testConn(databaseType,databaseName, ip, port, user, pass);
	}

	public List<Map<String, Object>> selectSqlStudy() {
		return this.permissionDao.selectSqlStudy();
	}

	public int saveDesginColumn(Map map, String databaseName, String tableName) throws Exception {
		return this.permissionDao.saveDesginColumn(map, databaseName, tableName);
	}

	public int deleteTableColumn(String databaseName, String tableName, String[] ids) throws Exception {
		return this.permissionDao.deleteTableColumn(databaseName, tableName, ids);
	}

	@Transactional
	public int updateTableColumn(String updated, String databaseName, String tableName) throws Exception {
		if (updated != null) {
			JSONArray updateArray = JSONArray.parseArray(updated);
			for (int i = 0; i < updateArray.size(); ++i) {
				Map map1 = (Map) updateArray.get(i);
				Map maps = new HashMap();
				for (Object key : map1.keySet()) {
					maps.put(key, map1.get(key));
				}

				String idValues = (String) maps.get("treeSoftPrimaryKey");

				this.permissionDao.updateTableColumn(maps, databaseName, tableName, "column_name", idValues);
			}
		}
		return 0;
	}

	public int savePrimaryKey(String databaseName, String tableName, String column_name, String column_key)
			throws Exception {
		return this.permissionDao.savePrimaryKey(databaseName, tableName, column_name, column_key);
	}

	public int updateTableNullAble(String databaseName, String tableName, String column_name, String is_nullable)
			throws Exception {
		return this.permissionDao.updateTableNullAble(databaseName, tableName, column_name, is_nullable);
	}

	public int upDownColumn(String databaseName, String tableName, String column_name, String column_name2)
			throws Exception {
		return this.permissionDao.upDownColumn(databaseName, tableName, column_name, column_name2);
	}
}