<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform" action="${ctx}/system/permission/i/configUpdate" method="post">
		<table class="formTable">
			<tr>
				<td>数据库类型：</td>
				<td>
				<select id="databaseType" name="databaseType" style="font-size:12px;" class="easyui-validatebox"   data-options="width: 150" >
				  <option value="MySQL" >MySql </option>
				 <!-- <option value="SQLite" >SQLite </option>
				  <option value="Oracle">Oracle</option>
				  <option value="MSSQL">MSSQL</option>
				  <option value="DB2" >DB2</option>
				  --> 
				</select>
				</td>
			</tr>
			 
			 <tr>
				<td>默认数据库：</td>
				<td><input id="databaseName"  name="databaseName" type="text" value="${config.databaseName }"   class="easyui-validatebox"   data-options="width: 150,required:'required'"/>
				</td>
			</tr>
			 
			<tr>
				<td>IP地址：</td>
				<td><input id="ip" name="ip" type="text" value="${config.ip }" class="easyui-validatebox" data-options="width: 150,required:'required',validType:'length[3,20]'"/></td>
			</tr>
			<tr>
				<td>端口：</td>
				<td><input  id="port" name="port" type="text" value="${config.port }" class="easyui-validatebox"   data-options="width: 150,required:'required'"    />   </td>
			</tr>
			
			<tr>
				<td>用户名：</td>
				<td><input id="userName"  name="userName" type="text" value="${config.userName }" class="easyui-validatebox"  data-options="width: 150,required:'required'" /></td>
			</tr>
			
			<tr>
				<td>密   码：</td>
				<td><input id="passwrod" name="passwrod" type="password" value="${config.passwrod }" class="easyui-validatebox"  data-options="width: 150,required:'required'"  /></td>
			</tr>
			 
			 <tr>
				<td> </td>
				<td> <span id="mess2">  </span> </td>
			</tr>
			 
		</table>
		
		 
	</form>
</div>

<script type="text/javascript">

//提交表单
$('#mainform').form({    
    onSubmit: function(){    
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){  
    	parent.$.messager.show({ title : "提示",msg: "操作成功！", position: "bottomRight" });
    	setTimeout(function () {
            parent.config.panel('close');
        }, 2000);
        location.reload();
    }    
});   


 

//测试连接
 function  testConn(){
 	var option = $("#databaseType option:selected").val();
	  $.ajax({
			type:'POST',
		   	contentType:'application/json;charset=utf-8',
            url:"${ctx}/system/permission/i/testConn",
            data: JSON.stringify({ 'databaseName':$("#databaseName").val(),'databaseType':option,'userName':$("#userName").val(),'passwrod':$("#passwrod").val(),'port':$("#port").val(),'ip':$("#ip").val() } ),
            datatype: "json", 
            //成功返回之后调用的函数             
            success:function(data){
            	var status = data.status ;
            	if(status == 'success' ){
            		
            		$("#mess2").html(data.mess );
            		//alert( data.mess );
            	}else{
            		$("#mess2").html("连接失败！" );
            	}
            }  
     });
 }

</script>
</body>
</html>