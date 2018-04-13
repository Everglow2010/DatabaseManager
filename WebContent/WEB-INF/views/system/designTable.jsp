<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

</head>
<body>

 <div id="tb3" style="padding:5px;height:auto">
                         <div>                         
	       		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addRowButton"  onclick="addRow2()"> 添加</a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="delButton"   onclick="deleteTableColumn()">删除</a>
	        	           <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="editRowButton" onclick="editRow2()">修改</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                      
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                         
                           <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true"  >&nbsp;</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                         
                           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="editRowButton" onclick="saveRow()">保存</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" id="editRowButton" onclick="cancelChange()">取消</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                      
                         </div> 
                       
  </div>
  
 <input type="hidden" id="databaseName" value="${databaseName}" >
 <input type="hidden" id="tableName"  value="${tableName}">
<table id="dg3"></table> 
 
 
<script type="text/javascript">

var dgg;
var tableName;
var databaseName;

$(function(){  
	databaseName = $("#databaseName").val();
	tableName = $("#tableName").val();
    initData();//初始化选中表格的字段信息数据
});


function initData(){
	
	var ISNULLABLE = new Array();
	ISNULLABLE.push( { "value": "YES", "text": "YES" }   );
	ISNULLABLE.push( { "value": "NO", "text": "NO" }   );
	
	var ISPRIMARY = new Array();
	ISPRIMARY.push( { "value": "", "text": "" }   );
	ISPRIMARY.push( { "value": "PRI", "text": "YES" }   );
	ISPRIMARY.push( { "value": "NO", "text": "NO" }   );
	
	var Address = new Array();
	//Address.push( { "value": "varchar", "text": "varchar" }  );
	//Address.push( { "value": "int", "text": "int" }   );
	//Address.push( { "value": "datetime", "text": "datetime" }   );
	 
	
	Address.push( { "value": "char", "text": "char" }   );
    Address.push( { "value": "binary", "text": "binary" }   );
    Address.push( { "value": "bigint", "text": "bigint" }   );
    Address.push( { "value": "bit", "text": "bit" }   );
    Address.push( { "value": "blob", "text": "blob" }   );
    Address.push( { "value": "date", "text": "date" }   );
    Address.push( { "value": "datetime", "text": "datetime" }   );
    Address.push( { "value": "double", "text": "double" }   );
    Address.push( { "value": "decimal", "text": "decimal" }   );  
    Address.push( { "value": "enum", "text": "enum" }   );
    Address.push( { "value": "float", "text": "float" }   );
    Address.push( { "value": "int", "text": "int" }   );
	Address.push( { "value": "integer", "text": "integer" }   );
	Address.push( { "value": "longtext", "text": "longtext" }   );
    Address.push( { "value": "longblob", "text": "longblob" }   );
    Address.push( { "value": "mediumint", "text": "mediumint" }   );
    Address.push( { "value": "mediumblob", "text": "mediumblob" }   );
    Address.push( { "value": "mediumtext", "text": "mediumtext" }   );
    Address.push( { "value": "numeric", "text": "numeric" }   );
    Address.push( { "value": "real", "text": "real" }   );
    Address.push( { "value": "set", "text": "set" }   );
    Address.push( { "value": "smallint", "text": "smallint" }   );
    Address.push( { "value": "tinytext", "text": "tinytext" }   );
    Address.push( { "value": "text", "text": "text" }   );
    Address.push( { "value": "tinyblob", "text": "tinyblob" }   );
    Address.push( { "value": "timestamp", "text": "timestamp" }   );
    Address.push( { "value": "time", "text": "time" }   );
    Address.push( { "value": "varchar", "text": "varchar" }   );
    Address.push( { "value": "varbinary", "text": "varbinary" }   );
    Address.push( { "value": "year", "text": "year" }   );
	
	dgg=$('#dg3').datagrid({     
	method: "get",
    url: '${ctx}/system/permission/i/designTableData/'+tableName+'/'+databaseName, //获取要展示的datagrid中的字段信息数据
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'treeSoftPrimaryKey',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 20,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
    columns:[[    
		{field:'treeSoftPrimaryKey',checkbox:true}, 		  
		{field:'column_name',title:'字段名',sortable:true,width:30 , editor: { type: 'text', options: { required: true }}  },
		{field:'data_type',title:'类型',sortable:true,width:30,editor: { type: 'combobox', options: { data: Address, valueField: "value", textField: "text" } }    },
      	{field:'character_maximum_length',title:'长度',sortable:true,width:30, editor:'numberbox'},
      	{field:'column_comment',title:'注释',width:50 , sortable:true, editor:'text'},
        {field:'is_nullable',title:'允许空值', width:10, editor:{  type:'checkbox',options:{ on: 'YES',off: '' } } , formatter:function(value,row,index){   
	       if (value == 'YES') {
              return '<input type="checkbox"  checked    onclick=changeNullAble(this,'+index +') >'; 
          }else{
        	  return '<input type="checkbox"    onclick=changeNullAble(this,'+index +') >'; 
          }
         }},   
      	{field:'column_key',title:'主键设置', width:10,editor:{  type:'checkbox',  options:{ on: 'PRI',off: '' } } , formatter:function( value,row,index ){   
	       if (value == 'PRI') {
              return '<input type="checkbox"   checked  onclick=changePrimaryKey(this,'+index +') >'; 
          }else{
        	  return '<input type="checkbox"   onclick=changePrimaryKey(this,'+index +') >'; 
          }
         }}
      	
    ]], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tb3',
    autoEditing: true,     //该属性启用双击行时自定开启该行的编辑状态
    singleEditing: true,
    onAfterEdit:function(rowIndex, rowData, changes){
        	//alert("edit");
    	  // submitUpdate(rowData);
    },
    onDblClickCell: function(index,field,value){
		  $(this).datagrid('beginEdit', index);
		  var ed = $(this).datagrid('getEditor', {index:index,field:field});
		  $(ed.target).focus();
	  } 
	}); 
	
	//$("#dg3").datagrid('beginEdit',2);
	
}

//新增一行字段信息
	function addRow2(){
		$('#dg3').datagrid('insertRow',{
	  	index: 0,	// 索引从0开始
	  	row: { }
    	});
		$('#dg3').datagrid("beginEdit",0 );
   }

 //编辑 一行数据
   function editRow2(){
	    
	   var checkedItems = $('#dg3').datagrid('getChecked');
	    
	   if(checkedItems.length == 0 ){
	    	parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		    return;
	   }
	    
	   $.each(checkedItems, function(index, item){
               var  index = $('#dg3').datagrid("getRowIndex", item );
               $('#dg3').datagrid("beginEdit",index );
      }); 
	 
   }

 //保存修改,同时保存新增行,修改行.
   function saveRow(){  
	   endEdit()
	   
	   var inserted = $('#dg3').datagrid('getChanges', 'inserted');
       //获取输入更新 
       var updated =  $('#dg3').datagrid('getChanges', 'updated');
	   var effectRow = new Object();
	   
	   effectRow["databaseName"] = databaseName;
	   effectRow["tableName"] = tableName;
	  // effectRow["primary_key"] = primary_key;
	    
       if (inserted.length) {//有新增行
    	  // alert( JSON.stringify(inserted) );
           effectRow["inserted"] = JSON.stringify(inserted);
        }
         
       if (updated.length) {//有更新行
    	 //   alert( JSON.stringify(updated) );
          effectRow["updated"] = JSON.stringify(updated);
       }
      
       if ( !inserted.length && !updated.length  ) {//没有更新行也没有新增行
    	   parent.$.messager.show({ title : "提示",msg: "没有变更内容！", position: "bottomRight" });
    	   return;
       }
       
       $.post("${ctx}/system/permission/i/designTableUpdate", effectRow, function(rsp) {
                            if(rsp.status =="success"){
                            	$('#dg3').datagrid('acceptChanges');
                            	parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
                                //$.messager.alert("提示", "保存成功！");
                               // $dg.datagrid('acceptChanges');
                               // endEdit();
                               // cancelChange();
                            }else{
                            	 $.messager.alert("提示", rsp.mess );
                            }
                        }, "JSON").error(function() {
                            $.messager.alert("提示", "提交错误了！");
       });
       
   }

	//刷新 
   function refresh(){
	    $('#dg3').datagrid('reload');
	    $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
   }
 

   //取消修改
   function  cancelChange(){
	 endEdit();
	 refresh();
   }

   //关闭编辑
   function endEdit(){
	     var rows = $('#dg3').datagrid('getRows');
         for ( var i = 0; i < rows.length; i++) {
            $('#dg3').datagrid('endEdit', i);
         }
     }

   //删除表字段
   function deleteTableColumn(){
	 
	 //表的主键字段
	//  var temp =  $('#dg3').datagrid('options') ;
	 
	  var checkedItems = $('#dg3').datagrid('getChecked');
	  var length = checkedItems.length;
	  
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	 
	  var ids = [];//要删除的行主键记录数组
      $.each( checkedItems, function(index, item){
    	//  alert( item["treeSoftPrimaryKey"] );
    	  ids.push( item["treeSoftPrimaryKey"] );
     }); 
	 
	 $.easyui.messager.confirm("操作提醒", "您确定要删除"+length+"行数据？", function (c) {
                if (c) {
                	
                   $.ajax({//发出删除选中行请求
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/permission/i/deleteTableColumn",
                    data: JSON.stringify( { 'databaseName':databaseName, 'tableName':tableName, 'ids':ids } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	    	   $('#dg3').datagrid('reload');
            	    	   $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg: "删除成功！", position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }
            	     }  
                   });
                }
            });
   }

	//设置是否可空
   function changeNullAble( obj, index ){
	   var row = $('#dg3').datagrid('getData').rows[index];
	   var column_name = row.treeSoftPrimaryKey;
	   var is_nullable = obj.checked ;
	   
	   $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/permission/i/designTableSetNull",
                    data: JSON.stringify( { 'databaseName':databaseName, 'tableName':tableName, 'column_name':column_name, 'is_nullable':is_nullable } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	    	 //  $('#dg3').datagrid('reload');
            	    	   $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }
            	     }  
         });
	   
   }
   
   //设置主键
   function changePrimaryKey( obj, index ){
	   var row = $('#dg3').datagrid('getData').rows[index];
	   var column_name = row.treeSoftPrimaryKey;
	   var column_key = obj.checked ;
	   
	   $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/permission/i/designTableSetPimary",
                    data: JSON.stringify( { 'databaseName':databaseName, 'tableName':tableName, 'column_name':column_name, 'column_key':column_key } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	    	 //  $('#dg3').datagrid('reload');
            	    	   $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }
            	     }  
         });
	   
   }
      
</script>

</body>
</html>