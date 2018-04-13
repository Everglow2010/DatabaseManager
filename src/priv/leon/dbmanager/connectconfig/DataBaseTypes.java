package priv.leon.dbmanager.connectconfig;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataBaseTypes {
	public static final List<String > dataBaseTypes = new ArrayList<String>();
	public static final Map<String, String> driverClassName=new LinkedHashMap<String, String>();
	
	static{
		dataBaseTypes.add("MySQL");
		dataBaseTypes.add("SQLite");
		dataBaseTypes.add("SQLsever");
		driverClassName.put("MySQL", "com.mysql.jdbc.Driver");
		driverClassName.put("SQLite", "org.sqlite.JDBC");
	}
}
