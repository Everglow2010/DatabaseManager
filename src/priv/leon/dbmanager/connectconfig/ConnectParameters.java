package priv.leon.dbmanager.connectconfig;

/**
 * @author Everglow
 *
 */
public class ConnectParameters {
	private String dbType;
	private String driverClassName;
	private String host;
	private String port;
	private String defaultDataBase;
	private String dataBaseName;
	private String userName;
	private String userPassword;	
	private String url;
	
		
	/**
	 * 
	 */
	public ConnectParameters() {		
	}
	
	

	/**
	 * @param dbType
	 * @param driverClassName
	 * @param host
	 * @param port
	 * @param defaultDataBase
	 * @param dataBaseName
	 * @param userName
	 * @param userPassword
	 * @param url
	 */
	public ConnectParameters(String dbType, String driverClassName, String host, String port, String defaultDataBase,
			String dataBaseName, String userName, String userPassword, String url) {
		this.dbType = dbType;
		this.driverClassName = driverClassName;
		this.host = host;
		this.port = port;
		this.defaultDataBase = defaultDataBase;
		this.dataBaseName = dataBaseName;
		this.userName = userName;
		this.userPassword = userPassword;
		this.url = url;
	}



	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getDefaultDataBase() {
		return defaultDataBase;
	}
	public void setDefaultDataBase(String defaultDataBase) {
		this.defaultDataBase = defaultDataBase;
	}
	/**
	 * @return the dataBaseName
	 */
	public String getDataBaseName() {
		return dataBaseName;
	}

	/**
	 * @param dataBaseName the dataBaseName to set
	 */
	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
