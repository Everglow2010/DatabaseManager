<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MainPage</title>
<meta name="Keywords" content="TreeSoft数据库管理系统">
<meta name="Description" content="TreeSoft数据库管理系统">

<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<%@ include file="/WEB-INF/views/include/codemirror.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<!--导入首页启动时需要的相应资源文件(首页相应功能的 js 库、css样式以及渲染首页界面的 js 文件)-->
<script src="${ctx}/static/plugins/easyui/common/index.js" type="text/javascript"></script>
<script src="${ctx}/static/plugins/easyui/common/indexSearch.js" type="text/javascript"></script>
<link href="${ctx}/static/plugins/easyui/common/index.css" rel="stylesheet" />
<script src="${ctx}/static/plugins/easyui/common/index-startup.js"></script>

<link type="text/css" rel="stylesheet" href="${ctx}/static/css/eclipse.css">  
<link type="text/css" rel="stylesheet" href="${ctx}/static/css/codemirror.css" />  
<link type="text/css" rel="stylesheet" href="${ctx}/static/css/show-hint.css" /> 
<link rel="icon" href="${ctx}/favicon.ico" mce_href="${ctx}/favicon.ico" type="image/x-icon">  
<link rel="shortcut icon" href="${ctx}/favicon.ico" mce_href="${ctx}/favicon.ico" type="image/x-icon">
<script type="text/javascript" src="${ctx}/static/js/codemirror.js"></script>  

<script type="text/javascript" src="${ctx}/static/js/sql.js"></script>  
<script type="text/javascript" src="${ctx}/static/js/show-hint.js"></script>  
<script type="text/javascript" src="${ctx}/static/js/sql-hint.js"></script>  
<style>  
        .CodeMirror {  
            border: 1px solid #cccccc; 
            height: 98%;
        }  
</style> 

</head>
<body>
	<!-- 容器遮罩 -->
    <div id="maskContainer">
        <div class="datagrid-mask" style="display: block;"></div>
        <div class="datagrid-mask-msg" style="display: block; left: 50%; margin-left: -52.5px;">
            正在加载...
        </div>
    </div>
    <div id="mainLayout" class="easyui-layout hidden" data-options="fit: true">
        <div id="northPanel" data-options="region: 'north', border: false" style="height: 80px; overflow: hidden;">
           
            <div id="topbar" class="top-bar">
            
                <div class="top-bar-left">
                    <h1 style="margin-left: 10px; margin-top: 10px;color: #fff">Everglow<span style="color: #3F4752"> 数据库管理系统 </span>  <span style="color: #fffl;font-size:12px;">初级版 for mySql v1.0</span> </h1>
                   <span> for mySql </span>
                </div>
                
                <div class="top-bar-right">
                    <div id="timerSpan"> 
                    
                     <div id="operator" style="padding:5px;height:auto">
                         <div style="padding-right:80px;height:auto">
                         
                           <div style="padding-right:20px; display:inline; cursor:pointer;">
                                 <img   src="${ctx}/static/images/btn_hd_support.gif" onclick="javascript:ShowConfigDialog()"  title="参数配置"/>
                          </div>  
                         
                           
                         <div style="padding-right:20px; display:inline;cursor:pointer;">
                             <img   src="${ctx}/static/images/btn_hd_help.gif" onclick="javascript:help()"  title="帮助"   />
                        </div>
                        <div style=" display:inline;cursor:pointer; ">
                             <img id="btnExit"   src="${ctx}/static/images/btn_hd_exit.gif" title="注销"   /> 
	       		         </div> 
	       		      </div> 
                      </div>
                    </div>
                </div>
            </div>
            <div id="toolbar" class="panel-header panel-header-noborder top-toolbar">
                <div id="infobar">
                    <span class="icon-hamburg-user" style="padding-left: 25px; background-position: left center;">
                      Everglow，您好
                    </span>
                </div>
               
                <div id="buttonbar">
                   <%--
                   <a id="mainTabs_toggleAll" class="easyui-linkbutton easyui-tooltip" title="展开/折叠面板使选项卡最大化" data-options="plain: true, iconCls: 'icon-standard-arrow-out'">最大化</a>
                   --%>
                   
                   <a href="javascript:void(0);"  id="btnFullScreen" class="easyui-linkbutton easyui-tooltip" title="全屏切换" data-options="plain: true, iconCls: 'icon-standard-arrow-inout'"  >全屏切换</a> 
                
                    <span>更换皮肤：</span>
                    <select id="themeSelector"></select>
					
                    <a id="btnShowNorth" class="easyui-linkbutton" data-options="plain: true, iconCls: 'layout-button-down'" style="display: none;"></a>
                </div>
            </div>
        </div>

        <div data-options="region: 'west', title: '数据库选择', iconCls: 'icon-standard-map', split: true, minWidth: 200, maxWidth: 400" style="width: 220px; padding: 1px;">
			  
			<div id="eastLayout" class="easyui-layout" data-options="fit: true">
			
                <div data-options="region: 'north', split: false, border: false" style="height: 34px;">
                    <select class="combobox-f combo-f" style="width:180px;margin:5px; " id="databaseSelect"  >   </select> 
                </div>
                
                <div   data-options="region: 'center', border: false, title: '数据库', iconCls: 'icon-hamburg-database', tools: [{ iconCls: 'icon-hamburg-refresh', handler: function () {  dg.treegrid('reload'); } }]">
                       <input id="pid" name="pid" />  
                </div>
            </div>
			  
        </div>

        <div data-options="region: 'center'">
            <div id="mainTabs_tools" class="tabs-tool">
                <table>
                    <tr>
                        <td><a id="mainTabs_jumpHome" class="easyui-linkbutton easyui-tooltip" title="跳转至主页选项卡" data-options="plain: true, iconCls: 'icon-hamburg-home'"></a></td>
                        <td><div class="datagrid-btn-separator"></div></td>
						<td><a id="mainTabs_toggleAll" class="easyui-linkbutton easyui-tooltip" title="展开/折叠面板使选项卡最大化" data-options="plain: true, iconCls: 'icon-standard-arrow-out'"></a></td>
                        <td><div class="datagrid-btn-separator"></div></td>
                        <td><a id="mainTabs_refTab" class="easyui-linkbutton easyui-tooltip" title="刷新当前选中的选项卡" data-options="plain: true, iconCls: 'icon-standard-arrow-refresh'"></a></td>
                        <td><div class="datagrid-btn-separator"></div></td>
                        <td><a id="mainTabs_closeTab" class="easyui-linkbutton easyui-tooltip" title="关闭当前选中的选项卡" data-options="plain: true, iconCls: 'icon-standard-application-form-delete'"></a></td>
                    </tr>
                </table>
            </div>

            <div id="mainTabs" class="easyui-tabs" data-options="fit: true, border: false, showOption: true, enableNewTabMenu: true, tools: '#mainTabs_tools', enableJumpTabMenu: true">
                <div id="homePanel" data-options="title: '运行及展示', iconCls: 'icon-hamburg-home'">   
           	 		<div id="eastLayout" class="easyui-layout" data-options="fit: true">      
                		<div data-options="region: 'center', border: false" style="height:50%;">
                    		 <div id="operator"  class="panel-header panel-header-noborder  " style="padding:5px;height:auto"  >
                            	<div>
	       		              	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-32-communication" plain="true" onclick="run();">执行</a>
	       		             	<span class="toolbar-item dialog-tool-separator"></span>
	        	             
	        	              	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="clearSQL()">清空</a>
	                         	<span class="toolbar-item dialog-tool-separator"></span>
	                         
	                         	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveSearchDialog()">SQL保存</a>
	                         	<span class="toolbar-item dialog-tool-separator"></span>
	                         	                         
	                         	<span class="l-btn-left l-btn-icon-left" style="margin-top: 4px"><span class="l-btn-text">更换样式:</span><span class="l-btn-icon icon-hamburg-drawings">&nbsp;</span></span>
										<select id="codeThemeSelector" onchange="selectTheme()"
											style="margin-top: 4px">
											<option selected>
												default
											</option>
										</select>                 
                            	</div> 
                       		</div>
                       		
		                	<div  style="width:100%;height:80%; " >	              
		                    	<input type="hidden" id="searchHistoryId">
		                    	<input type="hidden" id="searchHistoryName">
		                    	<textarea  id="sqltextarea" style="margin:10px; font-size:14px;font-family: '微软雅黑';width:97%;height:95%; "> </textarea>
	                    	</div>
                		</div>
                
                		<div  id="searchHistoryPanel" style="height:50%; "  data-options="region: 'south',split: true, collapsed: false,  minHeight: 280, maxHeight: 600, border: false, title: '运行结果', iconCls: 'icon-standard-application-view-icons'  ">
                    
                     		<div id="searchTabs" class="easyui-tabs" data-options="fit: true, border: false, showOption: true, enableNewTabMenu: true, enableJumpTabMenu: true">
                        		<div id="searcHomePanel" data-options="title: '消息', iconCls: 'icon-hamburg-issue'">                          
                            	<textarea  id="executeMessage" style="margin:10px; font-size:14px;font-family: '微软雅黑';width:97%;height:95%; " readonly >   </textarea>                         
                        		</div>
                    		</div>                       
                		</div>        
            		</div>
                </div>
            </div>
        </div>
        
        <div data-options="region: 'south', title: '关于...', iconCls: 'icon-standard-information', collapsed: true, border: false" style="height: 70px;">
            <div style="color: #4e5766; padding: 6px 0px 0px 0px; margin: 0px auto; text-align: center; font-size:12px; font-family:微软雅黑;">
            </div>
        </div>
    </div>
    
<div id="dlgg"   ></div>  
<div id="addRow" ></div> 
<input type="hidden" id="currentTableName" >

<iframe id="exeframe" name="exeframe" style="display:none"> </iframe>
<form id="form1" method="post" target="exeframe" action="${ctx}/system/permission/i/exportExcel"   accept-charset="utf-8" onsubmit="document.charset='utf-8'" >
   <input type="hidden" id="sContent" name="sContent" value=""/>
</form>

<script type="text/javascript">

var dg;
var d;
var pwd;
var config;
var tableName;
var type;
var rowtype;
var colums ="";
var searchBG
var databaseName;
var add;
var primary_key;
var saveSearch;

var selectRowCount = 0;
var heightStr=300;  //新增 ，编辑 对话框的高度。
 var sqlArray = new Array();

var columnsTemp = new Array();
var index =0; 
var messTemp ="";

 //初始化左侧（主布局的西区域）菜单及东区域SQL帮助菜单
  $(function(){
	//initSqlStudyTree();	  
	$.ajax({
		type:'get',
		url:"${ctx}/system/permission/i/allDatabaseList",
		success: function(data){
			 $.each( data, function(index,value){
				 $("#databaseSelect").append("<option value='"+data[index].SCHEMA_NAME+"'>"+ data[index].SCHEMA_NAME +" </option>");
			 });
		     initDataBase();
		}
	});
  });

 //更改当前展示数据库
 $("#databaseSelect").change(function(){
	 databaseName =  $('#databaseSelect').val();
	 initDataBase(); 
	 //清空操作的提示信息
	 $("#executeMessage").val("");
	 executeMessage.setValue("");
	 
  })
  
  //左侧菜单 库表 展示
 function initDataBase(){
	 
	databaseName =  $('#databaseSelect').val();
	
	dg=$('#pid').treegrid({
	method: "GET",
    url:"${ctx}/system/permission/i/databaseList/"+databaseName,
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'id',
	treeField:'name',
	parentField : 'pid',
	iconCls: 'icon',
	animate:true, 
	rownumbers:false,
	singleSelect:true,
	striped:true,
    columns:[[    
        {field:'name',title:'&nbsp;&nbsp;详情',width:210}
    ]],
    enableHeaderClickMenu: false,
    enableHeaderContextMenu: false,
    enableRowContextMenu: true,
   
    dataPlain: false,
    onClickRow:function(rowData){
    	 tableName = rowData.name;
    	 type = rowData.type;
    	 
    	 columnsTemp.length = 0;
    	 $('#searchHistoryId').val('');
    	 $('#searchHistoryName').val('');
    	 
    	 var rootNode = $('#pid').treegrid('getRoot', rowData.id );
    	 var dbName = rootNode.name;
    	 
    	//表
    	if( type == 'table'){
    		selectRowCount = 0;
    	//	getTableColumns( tableName ,dbName );
    	$("#currentTableName").val(tableName );
    	
    		showSQL( tableName ,dbName );
    		clickTable( tableName );
    	}
    	
    	//视图
    	if( type =='view'){
    		selectRowCount = 0;
    		//getTableColumns( tableName ,dbName );
    		showViewSQL(  dbName, tableName )
    	}
    }
  });
 }
 
 // 整理成SQL ,  SQL提交到后台， 返回的DATA在列表中展示。
 //在命令编辑区，显示SQL
 function showSQL( tableName , dbName ){
	 var sql = "select * from "+tableName +";";
	 var str =  $('#sqltextarea').val();
	 $('#sqltextarea').val( str +'\n'+  sql);
	 
	  editor.setValue(  str +'\n'+  sql );
	 
	   //清空操作的提示信息
	 $("#executeMessage").html("");
	  executeMessage.setValue("");
 }
 
 //显示视图 的SQL
 function  showViewSQL(  dbName, tableName ){
	  $.ajax({
			type:'POST',
		   	contentType:'application/json;charset=utf-8',
            url:"${ctx}/system/permission/i/getViewSql",
            data: JSON.stringify({ 'databaseName':dbName ,'tableName':tableName } ),
            datatype: "json", 
            //成功返回之后调用的函数             
            success:function(data){
            	var status = data.status ;
            	if(status == 'success' ){
            	  //  d.datagrid('reload');
            	    selectRowCount = 0;
            	    //$('#sqltextarea').val( data.viewSql ); 
            	    
            	    var str =  $('#sqltextarea').val();
	                $('#sqltextarea').val( str +'\n'+  data.viewSql);
	                editor.setValue(  str +'\n'+  data.viewSql );
            	    
            	}else{
            	     parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	}
            }  
     });
	 
 }

//点击表格时在中间区域新增tab来展示表格数据
  function  clickTable( tableName ){
	 window.mainpage.mainTabs.addModule( tableName ,'${ctx}/system/permission/i/showTableData/'+tableName+'/'+databaseName,'icon-berlin-calendar');
 }

//刷新datagrid
  function refresh(){
	    d.datagrid('reload');
   }
   
 //主页编辑器定义------------------------------------------------------------------------------------
    var mime = 'text/x-mariadb';
    var editor = CodeMirror.fromTextArea(document.getElementById('sqltextarea'), {
    mode: mime,
    theme: "eclipse", //主题  
    lineNumbers: true,
    autofocus: true,
    extraKeys: {"Ctrl": "autocomplete"}, //输入s然后ctrl就可以弹出选择项 
    hintOptions: {tables: {
      users: {name: null, score: null, birthDate: null},
      countries: {name: null, population: null, size: null}
     }
    }
  });
 
  editor.on('change', function() {  
        //  editor.showHint(); //满足自动触发自动联想功能  
      $('#sqltextarea').val( editor.getValue() );  
   });  
   
   var executeMessage = CodeMirror.fromTextArea(document.getElementById('executeMessage'), {
    mode: "text/x-mariadb",
    theme: "eclipse", //主题 
    
    lineNumbers: true,
    autofocus: false,
    readOnly :true
  });
  
  //修改编辑器风格
  function selectTheme( theme ) {
    editor.setOption("theme", theme);
    executeMessage.setOption("theme", theme);
  }
 
  function selectTheme() {
    var input = document.getElementById("codeThemeSelector");
    var theme = input.options[input.selectedIndex].textContent;
    editor.setOption("theme", theme);
    executeMessage.setOption("theme", theme);
  }
  
 //maintab的执行按钮触发函数，执行sqltextarea中的sql语句 
 //判断 执行的SQL是什么 类型，有没返回 结果集。
 function run(){	 
     var sql = $.trim( $('#sqltextarea').val() );
     var selectSql = editor.getSelection();
     if (selectSql != '') {
         sql = selectSql;
     }
	 
	 var sqlArray = new Array();
	 
	 messTemp ="";
	 
	 if(sql==""||sql==null ){
		 parent.$.messager.show({ title : "提示", msg: "亲，请输入SQL语句！" });
		 return;
	 }
	 
	 var arry = new Array();
	 
	 var tempSql2 = sql.replace(' ','');
	 if( tempSql2.indexOf("createfunction")==0 || tempSql2.indexOf("createprocedure")== 0 ){
		 arry.push( sql  );
		 
	 }else{
		 arry = sql.split(";");//以换行符为分隔符将内容分割成数组
	 }	 
	 
	 var tempStr;
	 for (var i = 0; i < arry.length; i++) {  
		 tempStr = $.trim( arry[i] );
       
        //判断注释的行 
        if( tempStr.indexOf("--")==0  ){
        	continue;
        }
        if( tempStr.indexOf("/*")==0  ){
        	continue;
        }
        if( tempStr == "" || tempStr == null ){
        	continue;
        }
        sqlArray.push( tempStr );
     }   
	 
	 //支持多行SQL执行，每行用分号 结束 
	 var tempSql;
	 var selectCount=1;
	 window.mainpage.searchTabs.closeAllTabs(); 
	 executeSQLArray( sqlArray , index );
 }
 
   //执行多行SQL时,需要顺序动态生成多个结果TAB页面
   //1 将SQL分组
   //2 AJAX请求一行SQL数据,
   //3 动态生成TAB,动态生成datagrid,将数据赋给datagrid.
   //4 AJAX执行成功后,递归调用方法自己,执行下一名SQL
   //5 动态生成TAB,动态生成datagrid,将数据赋给datagrid.
   //6 直到处理完 全部的SQL.
 var executeSQLArray = function( sqlArray , index ){
	 if(index >= sqlArray.length) {	
		parent.$.messager.show({ title : "提示",msg: "执行完成！", position: "bottomRight" });
		$("#searchTabs").tabs("select", 0 ); //TAB切换到第一项
		executeMessage.setValue( messTemp ); //显示执行后的信息
        return;
     }
	   
	 var sql = sqlArray[index] ;
	   
	 $.ajax({
		type:'post',
		timeout:3600000 ,
		url : "${ctx}/system/permission/i/executeSqlTest",
		data:{'sql':sql,'dbName':databaseName},//传回语句和数据库做参数
		success: function(data){
			//var messTemp = executeMessage.getValue() ;
			var status = data.status ;
            if(status == 'success' ){
            	//写执行的结果,时间 信息
            	messTemp = messTemp + '【SQL】 '+sql+ '\n 影响 '+data.totalCount +' 行， \n 运行时间：'+data.time+'毫秒。\n\n' ;
                
            	if( data.rows != null){
            	 //处理返回结果，并显示数据表格
            	   showResultTabGrid(data,sql , index );
            	}
            }else{
            	messTemp = messTemp + '【SQL】 '+sql+  "\n 信息："+data.mess  +' \n\n';
            }
			//递归调用 自己 .
			executeSQLArray( sqlArray , index+1 );
		}
	 });
 }
    

 //处理返回结果，并显示数据表格  
function showResultTabGrid( data,sql ,index ) {  
	// alert( data.columns );
	
    if (data.rows.length == 0) {  
       // $.messager.alert("结果", "没有数据!", "info", null);  
    }  
		   
    //动态 创建TAB页面
     $("#searchTabs").tabs('add', {
        title: '结果'+ (index + 1) ,
        closable: true,
        tools: [{
            iconCls: 'icon-berlin-calendar',
            handler: function() {
              //  alert('refresh');
            }
        }]
    });
    
    var tableTempId = "selectDg"+index;
    
    var currentTabPanel = $("#searchTabs").tabs('getSelected');
    var dynamicTable = $('<table id='+ tableTempId +'></table>');
    //这里一定要先添加到currentTabPanel中，因为dynamicTable.datagrid()函数需要调用到parent函数
    currentTabPanel.html(dynamicTable);
     
         
    var options = {  
        url:"${ctx}/system/permission/i/executeSqlTest",
        method: "POST",
        queryParams: { 'sql':sql , 'dbName': databaseName },
        rownumbers: true ,
        fit : true,
	    fitColumns : true,
	    border : false,
	    striped:true,
	    pagination:true,
	    pageNumber:1,
	    pageSize : 20,
	    pageList : [ 10, 20, 30, 40, 50 ],
	    singleSelect:false,
        checkOnSelect:true,
        selectOnCheck:true 
       // toolbar:'#tb2',    
    };  
    options.columns = eval(data.columns);//把返回的数组字符串转为对象，并赋于datagrid的column属性  
   // options.idField = data.primaryKey ;
   // primary_key = data.primaryKey ;
   //  var dataGrid = $("#selectDg0");  
     var dataGrid=$('#'+tableTempId );
   // var dataGrid = document.getElementById( tableTempId );
    
    dataGrid.datagrid(options);//根据配置选项，生成datagrid  
    dataGrid.datagrid("loadData", data.rows); //载入本地json格式的数据  
    
  }
  
  //左上工具栏点击函数
  //帮助按钮
  function help(){
	  $("#addRow").dialog({   
	    title: "帮助",    
	    width: 500,    
	    height: 600,  
	    href:"${ctx}/system/permission/i/help" ,
	    maximizable:true,
	    modal:true,
	    buttons:[ 	    	 
		 {
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
			    $("#addRow").panel('close');
			}
		}]
	});
  }
  
  //更新连接参数配置界面
  function ShowConfigDialog(){
	 config= $("#dlgg").dialog({   
	    title: '参数配置',    
	    width: 380,    
	    height: 260,    
	    href:'${ctx}/system/permission/i/config',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'测试',
			iconCls:'icon-search',
			handler:function(){
	    	   testConn();
	    	  // alert("测试");
				//$("#mainform").submit(); 
			}
		   },
	    	{
			text:'确认',
			iconCls:'icon-edit',
			handler:function(){
				$("#mainform").submit();
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					config.panel('close');
				}
		}]
	});
	 
 }
</script>

</body>
</html>