<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>连接参数配置</title>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/plugins/easyui/jquery-easyui-1.5.2/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/plugins/easyui/jquery-easyui-1.5.2/themes/icon.css">
	<script type="text/javascript" src="${ctx}/static/plugins/easyui/jquery-easyui-1.5.2/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/static/plugins/easyui/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
	<link rel="icon" href="${ctx}/favicon.ico" mce_href="${ctx}/favicon.ico" type="image/x-icon">  
    <link rel="shortcut icon" href="${ctx}/favicon.ico" mce_href="${ctx}/favicon.ico" type="image/x-icon">
</head>
<body>

<div class="easyui-layout" data-options="fit:'true'">
<div data-options="region:'center',title:'Connect Parameters Config',iconCls:'icon-edit',border:'true'">
	<sf:form modelAttribute="connectParameters" action="toconnect" method="post">
		<table class="formTable" >
			<tr>
				<td>数据库类型：</td>
				<td><sf:select id="dbType" path="dbType" items="${dbTypes}"/></td>
			</tr>
			 
			<tr>
				<td>默认数据库：</td>
				<td><sf:input id="defaultDataBase" path="defaultDataBase"/></td>
			</tr>
			 
			<tr>
				<td>IP地址：</td>
				<td><sf:input id="host" path="host"/></td>
			</tr>
			<tr>
				<td>端口：</td>
				<td><sf:input id="port" path="port"/></td>
			</tr>
			
			<tr>
				<td>用户名：</td>
				<td><sf:input id="userName" path="userName"/></td>
			</tr>
			
			<tr>
				<td>密   码：</td>
				<td><sf:password id="userPassword" path="userPassword"/></td>
			</tr>
			 
			 <tr>
				<td> </td>
				<td> <span id="mess2">  </span> </td>
			</tr>		 
		</table>
		<input type="submit" value="连接"/><br/>
	</sf:form>
</div>
</div>
</body>
</html>