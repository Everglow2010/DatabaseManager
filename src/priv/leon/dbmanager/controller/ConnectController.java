package priv.leon.dbmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import priv.leon.dbmanager.connectconfig.ConnectParameters;
import priv.leon.dbmanager.connectconfig.Constants;
import priv.leon.dbmanager.connectconfig.DataBaseTypes;


import  static org.springframework.web.bind.annotation.RequestMethod.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class ConnectController {

	
	@RequestMapping(value="/connectpara_input",method=GET)
	public String connectparaInput(HttpServletRequest request, HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		session.setAttribute("newOrUpdate", "new");
		String dataBaseName = "mysql";
		String defaultDataBase = "mysql";
		//String driverClassName = "com.mysql.jdbc.Driver";
		String host = "localhost";
		String port = "3306";
		String userName = "root";
		String userPassword = "root";
		ConnectParameters connectParameters = new ConnectParameters();
		
		connectParameters.setDataBaseName(dataBaseName);
		connectParameters.setDefaultDataBase(defaultDataBase);
		connectParameters.setHost(host);
		connectParameters.setPort(port);
		connectParameters.setUserName(userName);
		connectParameters.setUserPassword(userPassword);
		model.addAttribute("dbTypes", DataBaseTypes.dataBaseTypes);
		model.addAttribute("connectParameters", connectParameters);
		return "system/ConnectForm";
	}
	


	@RequestMapping(value="/toconnect",method=POST)
	public String toConnect(@ModelAttribute("cps") ConnectParameters cps, HttpServletRequest request, HttpServletResponse response, Model model){
		HttpSession session = request.getSession();
		String newOrUpdate = (String)session.getAttribute("newOrUpdate");
		//将用户输入的数据库连接参数存入connectparameters.properties文件中
		Properties prop = new Properties();
		prop.setProperty("dbType", cps.getDbType());
		prop.setProperty("host", cps.getHost());
		prop.setProperty("port", cps.getPort());
		prop.setProperty("defaultDataBase", cps.getDefaultDataBase());
		prop.setProperty("userName", cps.getUserName());
		prop.setProperty("userPassword", cps.getUserPassword());
		
		String filepath = getClass().getResource("/").getPath()+"connectParameters.properties";
		System.out.println(filepath);
		File file = new File(filepath);
		
		try {
			FileOutputStream oFile = new FileOutputStream(file);
			//System.out.println("write to file");
			prop.store(oFile, "ConnectParameters");
			oFile.close();
			//System.out.println("file closed");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		//ConnectParameters cps = new ConnectParameters();
		//Properties prop = new Properties();
		try {
			if ("new".equals(newOrUpdate)) {
				System.out.println("新建立连接，参数为：");
				cps.setDriverClassName(DataBaseTypes.driverClassName.get(cps.getDbType()));
				Constants.DATABASETYPE = cps.getDbType();
				Constants.DATABASENAME = cps.getDefaultDataBase();
				Constants.DRIVER = cps.getDriverClassName();
				Constants.IP = cps.getHost();
				Constants.PORT = cps.getPort();
				Constants.USERNAME = cps.getUserName();
				Constants.PASSWROD = cps.getUserPassword();

				System.out.println(Constants.DATABASETYPE);
				System.out.println(Constants.DRIVER);
				System.out.println(Constants.DATABASENAME);
				System.out.println(Constants.IP);
				System.out.println(Constants.PORT);
				System.out.println(Constants.USERNAME);
				System.out.println(Constants.PASSWROD);
			}else {
				System.out.println("更新参数触发刷新重载页面,重新获取连接，参数为： ");
				System.out.println(Constants.DATABASETYPE);
				System.out.println(Constants.DRIVER);
				System.out.println(Constants.DATABASENAME);
				System.out.println(Constants.IP);
				System.out.println(Constants.PORT);
				System.out.println(Constants.USERNAME);
				System.out.println(Constants.PASSWROD);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Connection conn = connectService.connectToDataBase(cps);
		
		//List<String> dataBasesList = fetchService.fetchAllDataBases(conn);
		
		//List<Table> tablesList = fetchService.fetchTables(conn);
		/*for (Table table : tablesList) {
			System.out.println(table.getTableCatlog()+" "+table.getTableSchema()+" "+table.getTableName()+" "+table.getTableType());
			System.out.println(table.getFieldCount());
			System.out.println(table.getRowCount());
		}
		
		
		for (String dbname : dataBasesList) {
			System.out.println(dbname);
		}*/
		
		//model.addAttribute("dataBasesList", dataBasesList);
		//connectService.closeConnection(conn);
		return "system/MainPage";
	}
}





