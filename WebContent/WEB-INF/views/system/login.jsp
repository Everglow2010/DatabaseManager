<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>Everglow数据库管理系统</title>
	<meta name="Keywords" content="Everglow数据库管理系统">
    <meta name="Description" content="Everglow数据库管理系统">
	<script src="${ctx}/static/plugins/easyui/jquery/jquery-1.11.1.min.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/bglogin.css" />
	<link rel="icon" href="${ctx}/favicon.ico" type="image/x-icon">  
    <link rel="shortcut icon" href="${ctx}/favicon.ico" type="image/x-icon">
	<script>
	var captcha;
	function refreshCaptcha(){  
	   //document.getElementById("img_captcha").src="${ctx}/static/images/kaptcha.jpg?t=" + Math.random();  
		document.getElementById("img_captcha").src="${ctx}/static/images/securityCode.jpg?t=" + Math.random();  
	}  
	</script>
</head>
<body>
	<img style="position:absolute; left:0px; top:0px; width:100%; height:auto; z-Index:-1; border:1px solid blue" src="${ctx}/static/images/welcome-bg.jpg"/>
	<div>
	<form id="loginForm" action="${ctx}/everglow/loginVaildate" method="post">
		<div class="login_top">
			<div class="login_title">
				&nbsp; &nbsp;Everglow数据库管理系统
			</div>
		</div>
		<div style="float:left;width:100%;">
			<div class="login_main">
				<div class="login_main_top"></div>
				<div class="login_main_errortip">&nbsp; ${message} </div>
				<div class="login_main_ln">
					<input type="text" id="username" name="username"   />
				</div>
				<div class="login_main_pw">
					<input type="password" id="password" name="password"   />
				</div>
				<div class="login_main_yzm"    >
					<div >
					<input type="text" id="captcha" name="captcha"/> 
					<img alt="验证码" src="${ctx}/static/images/securityCode.jpg" title="点击更换" id="img_captcha" onclick="javascript:refreshCaptcha();" style="height:45px;width:85px;float:right;margin-top:3px; margin-right:98px;"/>
					</div>
				</div>
				<div class="login_main_remb">
					<input id="rm" name="rememberMe" type="hidden"/>
				</div>
				
				
				<div class="login_main_submit">
				    <input type="submit"  value="" />	
				</div>
				
			</div>
		</div>
	</form>
  </div>
	 
</body>
</html>
