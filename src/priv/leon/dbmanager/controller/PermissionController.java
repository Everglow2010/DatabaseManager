package priv.leon.dbmanager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import priv.leon.dbmanager.connectconfig.Constants;
import priv.leon.dbmanager.connectconfig.DataBaseTypes;
import priv.leon.dbmanager.domain.Config;
import priv.leon.dbmanager.domain.Page;
import priv.leon.dbmanager.service.PermissionService;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "system/permission" })
public class PermissionController extends BaseController{
	
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping({ "i/allDatabaseList" })//获取所有数据库列表以供左侧树形菜单展示（已通）
	@ResponseBody
	public List<Map<String, Object>> allDatabaseList() throws Exception {
		List listDb = new ArrayList();

		listDb = this.permissionService.getAllDataBase();

		return listDb;
	}
	
	//获取主界面左侧展示数据库列表，主要是封装成前端treegrid能够加载的树形的JSON字符串
	@RequestMapping(value = { "i/databaseList/{databaseName}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public List<Map<String, Object>> databaseList(@PathVariable("databaseName") String databaseName) throws Exception {

		List listAll = new ArrayList();

		List listTable = new ArrayList();//表的列表
		List listView = new ArrayList();//视图列表
		List listFunction = new ArrayList();//函数列表		
		List listTableColumn = new ArrayList();//表字段列表
		
		
		String dbName = databaseName;
		Map tempMap = new HashMap();
		String column_name = "";
		String data_type = "";
		Object precision = "";
		
		Map tempObject = new HashMap();
		Map tempObjectTable2 = new HashMap();

		int id = 0;//treenode字段，标识树节点
		int pid = 0;//parentField字段，标识父节点
		int cpid = 0;//生成Json数据时暂存父节点

		//第一级，代表数据库的树根节点
		tempObject.put("id", Integer.valueOf(++id));
		tempObject.put("name", dbName);
		tempObject.put("type", "db");
		tempObject.put("icon", "icon-hamburg-database");

		listAll.add(tempObject);//数据库对象

		pid = id;//数据库父节点的id
		
		//第二级，代表表格的树节点
		Map tempObject2 = new HashMap();
		tempObject2.put("id", Integer.valueOf(++id));
		tempObject2.put("pid", Integer.valueOf(pid));
		tempObject2.put("name", "表");
		tempObject2.put("icon", "icon-berlin-billing");
		tempObject2.put("type", "direct");

		listAll.add(tempObject2);
		
		//第二级，代表视图的树节点
		Map tempObject3 = new HashMap();
		tempObject3.put("id", Integer.valueOf(++id));
		tempObject3.put("pid", Integer.valueOf(pid));
		tempObject3.put("name", "视图");
		tempObject3.put("icon", "icon-berlin-address");
		tempObject3.put("type", "direct");

		listAll.add(tempObject3);

		//第二级，代表函数的树节点
		Map tempObject4 = new HashMap();
		tempObject4.put("id", Integer.valueOf(++id));
		tempObject4.put("pid", Integer.valueOf(pid));
		tempObject4.put("name", "函数");
		tempObject4.put("icon", "icon-berlin-address");
		tempObject4.put("type", "direct");

		listAll.add(tempObject4);

		//第三级，循环添加代表表的树节点
		listTable = this.permissionService.getAllTables(dbName);
		for (int y = 0; y < listTable.size(); ++y) {
			tempObject = (Map) listTable.get(y);

			String table_name = (String) tempObject.get("table_name");
			Map tempObjectTable = new HashMap();
			tempObjectTable.put("id", Integer.valueOf(++id));
			tempObjectTable.put("pid", Integer.valueOf(pid + 1));
			tempObjectTable.put("name", table_name);
			tempObjectTable.put("icon", "icon-berlin-calendar");
			tempObjectTable.put("type", "table");
			tempObjectTable.put("state", "closed");

			cpid = id;

			listAll.add(tempObjectTable);

			//第四级，循环添加代表表格字段的树节点
			listTableColumn = this.permissionService.getTableColumns3(databaseName, table_name);
			for (int z = 0; z < listTableColumn.size(); ++z) {
				tempMap = (Map) listTableColumn.get(z);
				column_name = (String) tempMap.get("column_name");

				data_type = ((String) tempMap.get("column_type")).toLowerCase();

				tempObjectTable2 = new HashMap();
				tempObjectTable2.put("id", Integer.valueOf(++id));
				tempObjectTable2.put("pid", Integer.valueOf(cpid));

				tempObjectTable2.put("name", "<b>" + column_name + "</b>" + "  " + data_type);

				tempObjectTable2.put("icon", "icon-berlin-project");
				tempObjectTable2.put("type", "column");
				listAll.add(tempObjectTable2);
			}

		}
		
		//第三级，循环添加代表视图的树节点
		listView = this.permissionService.getAllViews(dbName);
		for (int y = 0; y < listView.size(); ++y) {
			tempObject = (Map) listView.get(y);

			Map tempObjectView = new HashMap();
			tempObjectView.put("id", Integer.valueOf(++id));
			tempObjectView.put("pid", Integer.valueOf(pid + 2));
			tempObjectView.put("name", tempObject.get("table_name"));
			tempObjectView.put("icon", "icon-berlin-library");
			tempObjectView.put("type", "view");

			listAll.add(tempObjectView);
		}
		
		//第三级，循环添加代表表的树节点
		listFunction = this.permissionService.getAllFuntion(dbName);
		for (int y = 0; y < listFunction.size(); ++y) {
			tempObject = (Map) listFunction.get(y);

			Map tempObjectFunction = new HashMap();
			tempObjectFunction.put("id", Integer.valueOf(++id));
			tempObjectFunction.put("pid", Integer.valueOf(pid + 3));
			tempObjectFunction.put("name", tempObject.get("routine_name"));
			tempObjectFunction.put("icon", "icon-berlin-settings");
			tempObjectFunction.put("type", "function");

			listAll.add(tempObjectFunction);
		}

		return listAll;
	}
	
	//展示表格数据
	@RequestMapping(value = { "i/showTableData/{tableName}/{databaseName}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public String showTableData(@PathVariable("tableName") String tableName,
			@PathVariable("databaseName") String databaseName, HttpServletRequest request) {
		System.out.println(databaseName+" "+tableName);
		request.setAttribute("databaseName", databaseName);
		request.setAttribute("tableName", tableName);
		return "system/showTableData";
	}
	
	//showtabledata页面获取指定表格具体数据
	@RequestMapping(value = { "i/table/{tableName}/{dbName}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> getData(@PathVariable("tableName") String tableName,
			@PathVariable("dbName") String dbName, HttpServletRequest request) throws Exception {
		Page page = getPage(request);

		page = this.permissionService.getData(page, tableName, dbName);
		
		//返回EasyUI通用数据
		return getEasyUIData(page);
	}

	//保存前端showtabledata.jsp中传来的更新数据，包括新增行（inserted）和修改行（updated）
	@RequestMapping(value = { "i/saveData" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> saveData(HttpServletRequest request) {
		Map mapResult = new HashMap();

		String databaseName = request.getParameter("databaseName");
		String tableName = request.getParameter("tableName");
		String inserted = request.getParameter("inserted");
		String updated = request.getParameter("updated");
		String primary_key = request.getParameter("primary_key");

		String mess = "";
		String status = "";
		try {
			if (inserted != null) {
				//解析新增行数据交给dao层处理
				JSONArray insertArray = JSONArray.parseArray(inserted);
				for (int i = 0; i < insertArray.size(); ++i) {
					Map map1 = (Map) insertArray.get(i);
					Map maps = new HashMap();
					for (String key : (Set<String>)map1.keySet()) {
						maps.put(key, map1.get(key));
					}
					maps.remove("treeSoftPrimaryKey");
					
					//保存新增数据行
					this.permissionService.saveRows(maps, databaseName, tableName);
				}

			}

			List condition = new ArrayList();

			if (updated != null) {
				//解析更新数据交给dao层处理
				JSONArray updateArray = JSONArray.parseArray(updated);
				for (int i = 0; i < updateArray.size(); ++i) {
					Map map1 = (Map) updateArray.get(i);

					Map map2 = (Map) map1.get("oldData");
					Map map3 = (Map) map1.get("changesData");

					String setStr = " set ";
					String whereStr = " where 1=1 ";

					if (map2.size() <= 0) {
						break;
					}

					if (map3.size() <= 0) {
						break;
					}

					if ((primary_key == null) || (primary_key.equals(""))) {
						for (String key : (Set<String>)map3.keySet()) {
							if (map3.get(key) == null)
								setStr = setStr + key + " = null , ";
							else {
								setStr = setStr + key + " = '" + map3.get(key) + "',";
							}
						}

						for (String key : (Set<String>)map2.keySet()) {
							if (map2.get(key) != null)
								whereStr = whereStr + " and " + key + " = '" + map2.get(key) + "' ";
						}
					} else {
						String[] primaryKeys = primary_key.split(",");

						for (String key : (Set<String>)map3.keySet()) {
							if (map3.get(key) == null)
								setStr = setStr + key + " = null , ";
							else {
								setStr = setStr + key + " = '" + map3.get(key) + "',";
							}
						}

						for (String primaryKey : primaryKeys) {
							whereStr = whereStr + " and " + primaryKey + " = '" + map2.get(primaryKey) + "' ";
						}

					}

					setStr = setStr.substring(0, setStr.length() - 1);

					condition.add(setStr + whereStr);
				}
				this.permissionService.updateRowsNew(databaseName, tableName, condition);
			}

			mess = "保存成功！";
			status = "success";
		} catch (Exception e) {
			mess = "保存出错！" + e.getMessage();
			status = "fail";
		}

		mapResult.put("mess", mess);
		mapResult.put("status", status);
		return mapResult;
	}
	
	//删除前端showtabledata.jsp中传来的需要删除的行
	@RequestMapping(value = { "i/deleteRows" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> deleteRows(@RequestBody IdsDto tem) {
		String databaseName = tem.getDatabaseName();
		String tableName = tem.getTableName();
		String primary_key = tem.getPrimary_key();

		String checkedItems = tem.getCheckedItems();

		List condition = new ArrayList();

		if (checkedItems != null) {
			JSONArray deleteArray = JSONArray.parseArray(checkedItems);

			for (int i = 0; i < deleteArray.size(); ++i) {
				Map map1 = (Map) deleteArray.get(i);
				String whereStr = "";

				if ((primary_key == null) || (primary_key.equals(""))) {
					for (String key : (Set<String>)map1.keySet()) {
						if (map1.get(key) != null) {
							whereStr = whereStr + " and " + key + " = '" + map1.get(key) + "' ";
						}
					}
				} else {
					String[] primaryKeys = primary_key.split(",");
					for (String primaryKey : primaryKeys) {
						whereStr = whereStr + " and " + primaryKey + " = '" + map1.get(primaryKey) + "' ";
					}
				}
				condition.add(whereStr);
			}
		}
		int i = 0;
		String mess = "";
		String status = "";
		try {
			//删除相应行
			this.permissionService.deleteRowsNew(databaseName, tableName, primary_key, condition);
			mess = "删除成功";
			status = "success";
		} catch (Exception e) {
			mess = e.getMessage();
			status = "fail";
		}

		Map map = new HashMap();

		map.put("totalCount", Integer.valueOf(i));

		map.put("mess", mess);
		map.put("status", status);

		return map;
	}
	
	//点击执行按钮后处理执行前端sqltextarea中输入的sql语句组
	@RequestMapping(value = { "i/executeSqlTest" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> executeSqlTest(HttpServletRequest request) throws Exception {
		Map map = new HashMap();

		String sql = request.getParameter("sql");
		String dbName = request.getParameter("dbName");

		//语句以select开始即为查询语句，可能有结果
		if ((sql.indexOf("select") == 0) || (sql.indexOf("SELECT") == 0))
			map = executeSqlHaveRes(sql, dbName, request);
		else {
			map = executeSqlNotRes(sql, dbName);
		}
		return map;
	}
	
	//执行有返回结果的sql语句（SELECT）
	public Map<String, Object> executeSqlHaveRes(String sql, String dbName, HttpServletRequest request) {
		Map map = new HashMap();
		Page page = getPage(request);

		String mess = "";
		String status = "";
		Date b1 = new Date();
		try {
			page = this.permissionService.executeSql(page, sql, dbName);
			mess = "执行成功！";
			status = "success";
		} catch (Exception e) {
			System.out.println("444 " + e.getMessage());
			mess = e.getMessage();
			status = "fail";
		}

		Date b2 = new Date();

		long y = b2.getTime() - b1.getTime();
		map.put("rows", page.getResult());
		map.put("total", Long.valueOf(page.getTotalCount()));
		map.put("columns", page.getColumns());
		map.put("primaryKey", page.getPrimaryKey());

		map.put("totalCount", Long.valueOf(page.getTotalCount()));
		map.put("time", Long.valueOf(y));
		map.put("mess", mess);
		map.put("status", status);

		return map;
	}
    
	//执行没有返回结果的sql语句
	public Map<String, Object> executeSqlNotRes(String sql, String dbName) {
		String mess = "";
		String status = "";

		Date b1 = new Date();
		int i = 0;
		try {
			i = this.permissionService.executeSqlNotRes(sql, dbName);
			mess = "执行成功！";
			status = "success";
		} catch (Exception e) {
			mess = e.getMessage();
			status = "fail";
		}

		Date b2 = new Date();

		long y = b2.getTime() - b1.getTime();

		Map map = new HashMap();

		map.put("totalCount", Integer.valueOf(i));
		map.put("time", Long.valueOf(y));
		map.put("mess", mess);
		map.put("status", status);

		return map;
	}
	
/*----------------------表格设计模块---------------------------------------------------------*/
	//处理查看表格时点击设计表格按钮的请求
	@RequestMapping(value = { "i/designTable/{tableName}/{databaseName}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public String designTable(@PathVariable("tableName") String tableName,
			@PathVariable("databaseName") String databaseName, HttpServletRequest request) throws Exception {
		request.setAttribute("databaseName", databaseName);
		request.setAttribute("tableName", tableName);

		return "system/designTable";
	}

	//获取设计表格时的初始化数据（表格各字段的信息）
	@RequestMapping(value = { "i/designTableData/{tableName}/{databaseName}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> designTableData(@PathVariable("tableName") String tableName,
			@PathVariable("databaseName") String databaseName, HttpServletRequest request) throws Exception {
		Map map = new HashMap();

		//获取指定表格字段信息
		List listAllColumn = this.permissionService.getTableColumns3(databaseName, tableName);

		map.put("rows", listAllColumn);
		map.put("total", Integer.valueOf(listAllColumn.size()));

		return map;
	}
	
	//表格设计的更新入库
	@RequestMapping(value = { "i/designTableUpdate" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> designTableUpdate(HttpServletRequest request) {
		Map mapResult = new HashMap();
		String mess = "";
		String status = "";

		String databaseName = request.getParameter("databaseName");
		String tableName = request.getParameter("tableName");
		String inserted = request.getParameter("inserted");//新增行（表格的新增字段）的列表
		String updated = request.getParameter("updated");//更新行（表格被更新字段）的列表
		try {
			if (inserted != null) {
				JSONArray insertArray = JSONArray.parseArray(inserted);
				for (int i = 0; i < insertArray.size(); ++i) {
					Map map1 = (Map) insertArray.get(i);
					Map maps = new HashMap();
					for (String key : (Set<String>)map1.keySet()) {
						maps.put(key, map1.get(key));
					}
					maps.remove("treeSoftPrimaryKey");
					this.permissionService.saveDesginColumn(maps, databaseName, tableName);
				}
			}

			if (updated != null) {
				this.permissionService.updateTableColumn(updated, databaseName, tableName);
			}

			mess = "保存成功！";
			status = "success";
		} catch (Exception e) {
			mess = "保存出错！" + e.getMessage();
			status = "fail";
		}

		mapResult.put("mess", mess);
		mapResult.put("status", status);
		return mapResult;
	}
	
	//删除设计表格时选中的字段
	@RequestMapping(value = { "i/deleteTableColumn" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> deleteTableColumn(@RequestBody IdsDto tem) {
		String databaseName = tem.getDatabaseName();
		String tableName = tem.getTableName();
		String[] ids = tem.getIds();

		int i = 0;
		String mess = "";
		String status = "";
		try {
			this.permissionService.deleteTableColumn(databaseName, tableName, ids);
			mess = "删除成功";
			status = "success";
		} catch (Exception e) {
			mess = e.getMessage();
			status = "fail";
		}

		Map map = new HashMap();

		map.put("totalCount", Integer.valueOf(i));
		map.put("mess", mess);
		map.put("status", status);

		return map;
	}

	//处理设计表格时置某字段是否可空
	@RequestMapping(value = { "i/designTableSetNull" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> designTableSetNull(@RequestBody IdsDto tem) {
		String mess = "";
		String status = "";

		String databaseName = tem.getDatabaseName();
		String tableName = tem.getTableName();
		String column_name = tem.getColumn_name();
		String is_nullable = tem.getIs_nullable();
		try {
			this.permissionService.updateTableNullAble(databaseName, tableName, column_name, is_nullable);
			mess = "保存成功";
			status = "success";
		} catch (Exception e) {
			mess = e.getMessage();
			status = "fail";
		}

		Map map = new HashMap();

		map.put("mess", mess);
		map.put("status", status);

		return map;
	}
	
	//处理设计表格时设置某字段为主键
	@RequestMapping(value = { "i/designTableSetPimary" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> designTableSetPimary(@RequestBody IdsDto tem) {
		String mess = "";
		String status = "";

		String databaseName = tem.getDatabaseName();
		String tableName = tem.getTableName();
		String column_name = tem.getColumn_name();
		String column_key = tem.getColumn_key();
		try {
			this.permissionService.savePrimaryKey(databaseName, tableName, column_name, column_key);
			mess = "保存成功";
			status = "success";
		} catch (Exception e) {
			mess = e.getMessage();
			status = "fail";
		}

		Map map = new HashMap();

		map.put("mess", mess);
		map.put("status", status);

		return map;
	}	

/*----------------------表格设计模块---------------------------------------------------------*/

	
	
	//处理帮助按钮请求
	@RequestMapping(value = { "i/help" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String help(HttpServletRequest request) {
		return "system/help";
	}
	
	//处理更新连接参数配置按钮请求,返回连接参数更新设置填写表单
	@RequestMapping(value = { "i/config" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String config(Model model) {
		Config config = new Config();
		//预填的连接参数
		config.setIp(Constants.IP);
		config.setUserName(Constants.USERNAME);
		config.setDatabaseName(Constants.DATABASENAME);
		config.setPort(Constants.PORT);
		config.setPasswrod(Constants.PASSWROD);

		model.addAttribute("config", config);
		return "system/configForm";
	}
	
	//处理连接参数更新页面中确认提交更新参数请求
	@RequestMapping(value = { "i/configUpdate" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public String configUpdate(@ModelAttribute @RequestBody Config config, HttpServletRequest request, HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		session.setAttribute("newOrUpdate", "update");
		String dbtype = config.getDatabaseType();
		String userName = config.getUserName();
		String userPassword = config.getPasswrod();
		String ip = config.getIp();
		String port = config.getPort();
		String dbName = config.getDatabaseName();
		String driver = DataBaseTypes.driverClassName.get(dbtype);
		config.setDriver(driver);
/*		Constants.DATABASETYPE = dbtype;
		Constants.DATABASENAME = dbName;
		Constants.IP = ip;
		Constants.PORT = port;
		Constants.USERNAME = userName;
		Constants.PASSWROD = userPassword;
		Constants.DRIVER = driver;*/
		
		System.out.println("更新前全局参数为：" + Constants.DATABASETYPE + "  " + Constants.DATABASENAME + "  " + Constants.IP + "  " + Constants.PORT
				 + "  " + Constants.USERNAME + "  " + Constants.PASSWROD + "  " + Constants.DRIVER);
		
		if(this.permissionService.configUpdate(config)) {
			System.out.println("！！！更新后全局参数为：" + Constants.DATABASETYPE + "  " + Constants.DATABASENAME + "  " + Constants.IP + "  " + Constants.PORT
					 + "  " + Constants.USERNAME + "  " + Constants.PASSWROD + "  " + Constants.DRIVER);			
			return "success";
		}
		else return "fail";
	}
	
	//处理连接参数更新页面中测试连接的请求
	@RequestMapping(value = { "i/testConn" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> testConn(@RequestBody Config config) {
		Map mapResult = new HashMap();

		String databaseType = config.getDatabaseType();
		String databaseName = config.getDatabaseName();
		String ip = config.getIp();
		String port = config.getPort();
		String user = config.getUserName();
		String pass = config.getPasswrod();
		
		if (databaseType==null) {
			System.out.println("类型空");
		}else System.out.println(databaseType);
		
		String mess = "";
		String status = "";

		boolean bl = this.permissionService.testConn(databaseType, databaseName, ip, port, user, pass);

		if (bl) {
			mess = "连接成功！";
			status = "success";
		} else {
			status = "fail";
		}

		mapResult.put("mess", mess);
		mapResult.put("status", status);

		return mapResult;
	}
	
}
