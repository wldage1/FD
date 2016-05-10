<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#dictionaryList").setGridWidth($(window).width()*0.999);　
});
var count = 0;   //用于计数
var currcount=0; //一次只允许操作一条记录
var dictType;
var count_1 = 0; //tab_1 全局变量
var currcount_1 = 0;
//#tab_1
$(document).ready(function(){
	//字典类型验证提示
	var options = {
		event:'focus',
		ajax:{
			url:'${base}/config/dictionary/valid.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: $("form#dictionary").serialize(), 
			cache:false
		},
		poshytip:{
			alignX: 'right'
		}
	};
    var valid = new $.Valid(options);
    
    //数据字典验证提示
	var options2 = {
		event:'focus',
		ajax:{
			url:'${base}/config/dictionaryItem/valid.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: $("form#dictionaryItem").serialize(), 
			cache:false
		},
		poshytip:{
			alignX: 'right'
		}
	};
    var valid2 = new $.Valid(options2);
    	
    jQuery("#dictionaryList").jqGrid({
        url: 'dictionary/grid.json',
        datatype: 'json',
        colNames: ["<msg:message code='dictionary.name'/>", 
                   "<msg:message code='dictionary.code'/>",
                   "<msg:message code='info.operate'/>",
                   "<img id='addRow' src='${base}/${skin}/images/icon/Add.png' width='20' height='20' />"
                   ],
        colModel: [{name: 'name',index:'name',width:'33%',align:'center',sortable:false,editable:true},
                   {name: 'code',index:'code',width:'34%',align:'center',sortable:false,editable:true},
                   {name:'operate',index:'operate', width:'30%',align:'left',sortable:false},
                   {name:'space',index:'space',width:'3%',align:'left',sortable:false}
                   ],
        postData:{c:"${dictionary.c}"},
        mtype:"POST",
        height:<msg:message code='jqgrid.height.250'/>,
        pager: false,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete: function(){
    		var ids = jQuery("#dictionaryList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			count_1 = id;
    			var rowdata = jQuery("#dictionaryList").jqGrid('getRowData',id);
    			var content = "";
				
    			<c:if test="${!empty config_dictionary_modify}">
						<security:authorize ifAnyGranted="${config_dictionary_modify.code}">	
						content += "<a href='javascript:void(0);' id='shortcut_config_dictionary_modify_"+id+"' sid="+id+" class='shortcut_config_dictionary_modify'>";
						content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${config_dictionary_modify.name}";
						content += "</a>";

						content += "<a href='javascript:void(0);' id='shortcut_config_dictionary_save_"+id+"' sid="+id+" class='' style='color:#999;' method celcalue='' >";
						content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${config_dictionary_save.name}";
						content += "</a>";

						content += "<a href='javascript:void(0);' id='shortcut_config_dictionary_reset_"+id+"' sid="+id+" class='' style='color:#999;'>";
						content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${config_dictionary_reset.name}";
						content += "</a>";
						</security:authorize>
				</c:if> 
    				jQuery("#dictionaryList").jqGrid('setRowData',ids[i],{operate:"<div class='jqgridContainer'>" + content + "</div>"});
    		}	
    	},
        caption:'<msg:message code="dictionary.list"/>',
        toolbar: [true,"top"]
	});
	jQuery("#dictionaryList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	
	$('#addRow').click(function(){
		count_1++;
		if(currcount_1 == 0){
			currcount_1 = 1;
			jQuery("#dictionaryList").jqGrid('addRowData',count_1,"{}");
			var data = {
					name:"<input name='name' id='"+count_1+"_name' maxlength='20' class='valid-control' style='width:98%;'/>",
					code:"<input name='code' id='"+count_1+"_code' maxlength='20' class='valid-control' style='width:98%;'/>",
					operate:"<c:if test="${!empty config_dictionary_modify}"><security:authorize ifAnyGranted="${config_dictionary_modify.code}"><a href='javascript:void(0);'  id='shortcut_${config_dictionary_modify.indexCode}_"
					+count_1+"' sid="+count_1+"  class=''  style='color:#999;'><img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${config_dictionary_modify.name}</a><a href='javascript:void(0);'  id='shortcut_${config_dictionary_save.indexCode}_"
					+count_1+"' class='shortcut_${config_dictionary_save.indexCode}' style='color:#53056A;' sid="+count_1+"><img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${config_dictionary_save.name}</a><a href='javascript:void(0);'  id='shortcut_${config_dictionary_reset.indexCode}_"+
					count_1+"' class='' style='color:#999;'><img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${config_dictionary_reset.name}</a></security:authorize></c:if>",
					space:"<c:if test="${!empty config_dictionary_delete}"><security:authorize ifAnyGranted="${config_dictionary_delete.code}"><a href='javascript:void(0);' id='shortcut_${config_dictionary_delete.indexCode}_"
					+count_1+"' sid="+count_1+" class='shortcut_${config_dictionary_delete.indexCode}' method='add'><img src='${base}/${skin}/images/icon/Del.png' width='20' height='20' /></a></security:authorize></c:if>"
			};
			jQuery("#dictionaryList").jqGrid('setRowData',count_1,data);
		}else{
		 	top.layer.msg('<msg:message code="dictionaryItem.notsave"/>');
			count_1--;
		}
	});	

	$(".shortcut_${config_dictionary_delete.indexCode}").live("click",(function(){
		jQuery("#dictionaryList").jqGrid('delRowData',count_1);
		currcount_1 = 0;
	}))
		
	$(".shortcut_config_dictionary_modify").live("click",(function(){
		var rowid = $(this).attr("sid");
		if(currcount_1 == 0){
			currcount_1 = 1;
			jQuery("#dictionaryList").jqGrid('editRow',rowid);
			$(this).attr("class","").css("color","#999");
			$("#shortcut_config_dictionary_save_"+rowid).attr("class","shortcut_config_dictionary_save").css("color","#53056A");
			$("#shortcut_config_dictionary_reset_"+rowid).attr("class","shortcut_config_dictionary_reset").css("color","#53056A");
		}else{
			var index = $.layer({
		  	    shade : [0.5 , '#000' , true],
		  	    area : ['auto','auto'],
		  	  	offset : ['50px',''],
		  	    dialog : {
		  	        msg:"请先保存当前编辑的条目",
		  	        btns : 1, 
		  	        type : 4,
		  	        btn : ["确定"],
		  	        yes : function(){
		  	        	layer.close(index);
		  	        },
		  	        no : function(){
		  	        	layer.close(index);
		  	        }
		  	    }});
		}
	}));
		
		
	$(".shortcut_config_dictionary_reset").live("click",( function() {
		currcount_1 = 0;
		var rowid = $(this).attr("sid");
		jQuery("#dictionaryList").jqGrid('restoreRow',rowid);
		$(this).attr("class","").css("color","#999");	
		$("#shortcut_config_dictionary_save_"+rowid).attr("class","").css("color","#999");	
		$("#shortcut_config_dictionary_modify_"+rowid).attr("class","shortcut_config_dictionary_modify").css("color","#53056A");
	}));
		
	$(".shortcut_config_dictionary_save").live("click",(function() {
		var data="";
		var celcalue = "";
		var method = $(this).attr("method");
		var rowid = $(this).attr("sid");
		var clsn = $("#"+rowid+"_name").attr("class");
		if(method == ""){
			celcalue = $(this).attr("celcalue");
			data = "c=${dictionary.c}&name="+$.trim($("#"+rowid+"_name").val())+"&id="+rowid+"&code="+$.trim($("#"+rowid+"_code").val())+"";
		}else{
			celcalue = $.trim($("#"+rowid+"_code").val());
			data = "c=${dictionary.c}&name="+$.trim($("#"+rowid+"_name").val())+"&code="+$.trim($("#"+rowid+"_code").val())+"";
		}
	 var tempOpts = {
	  	   classNames:[clsn],
		   validStatus:function(status){
			   if (status){
					$.ajax({
						url:'dictionary/save.json',
						type:'post',
						timeout:'60000',
						dataType:'json',
						data: data, 
						success:function(jsonData){
							if (jsonData.status == "success"){
								currcount_1 = 0;
								jQuery("#dictionaryList").jqGrid('setRowData',rowid,{name:$.trim($("#"+rowid+"_name").val()),code:$.trim($("#"+rowid+"_code").val())});
								$("#"+rowid).attr("editable","0");
								$("#shortcut_config_dictionary_save_"+rowid).attr("class","").css("color","#999");
								$("#shortcut_config_dictionary_reset_"+rowid).attr("class","").css("color","#999");					
								$("#shortcut_config_dictionary_modify_"+rowid).attr("class","shortcut_config_dictionary_modify").css("color","#53056A");
								$("#dictionaryList").trigger("reloadGrid");					
								if(method != ""){
									$("#shortcut_config_dictionary_save_"+rowid).attr("method","");
									$("#shortcut_config_dictionary_save_"+rowid).attr("sid",jsonData.gid);
									$("#dictionaryList").trigger("reloadGrid");
								}
							}else if(jsonData.message != '' && jsonData.message != 'undefined' && jsonData.message != null){
								top.layer.msg(jsonData.message);
							}
						}
						
					});
				}
			}
		};
		$.extend(options,tempOpts);
		valid.updateSetting(options);
 		valid.reset();
  		valid.valid();
	}));
		
	//#tab_2
	// 初始字典类型
	<c:if test="${dictionaryListSize != 0}">
	<c:forEach var="item" items="${dictionaryList}" varStatus="pt">
		<c:if test="${pt.index == 0}">
			$('#dictionaryCode').val('${item.code}');
			dictType = '${item.code}';
		</c:if>
	</c:forEach>
	</c:if>
	

	
	jQuery("#dictionaryItemList").jqGrid({
		url: 'dictionaryitem/grid.json',
		datatype: 'json',
		colNames: ["<msg:message code='dictionaryItem.itemName'/>",
				   "<msg:message code='dictionaryItem.itemValue'/>",
				   "<msg:message code='info.operate'/>",
				   "<img id='addRow2' src='${base}/${skin}/images/icon/Add.png' width='20' height='20' />"
				   ],
		colModel: [
				   {name: 'itemName',index:'itemName',width:'33%',align:'center',sortable:false,editable:true},
				   {name: 'itemValue',index:'itemValue',width:'34%',align:'center',sortable:false},
				   {name: 'act',index:'act', width:'30%',align:'center',sortable:false,title:false},
				   {name: 'del',index:'del', width:'3%',align:'center',sortable:false,title:false}
				   ],
		mtype:"POST",
		postData:{c:"${dictionary.c}",dictionaryCode:dictType},
		rowNum:100,     
		rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
		pager: false,
		height:<msg:message code='jqgrid.height.250'/>,
		autowidth: true,
		viewrecords: true,
		jsonReader: {
        	repeatitems: false
        },
		gridComplete: function(){
			var ids = jQuery("#dictionaryItemList").jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var id = ids[i];
				count = id;
				var rowdata = jQuery("#dictionaryItemList").jqGrid('getRowData',id);
				var content = "";
					<c:if test="${!empty config_dictionary_itemModify}">
					<security:authorize ifAnyGranted="${config_dictionary_itemModify.code}">	
					content += "<a href='javascript:void(0);' id='shortcut_config_dictionary_itemModify_"+id+"' sid="+id+" class='shortcut_config_dictionary_itemModify'>";
					content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${config_dictionary_itemModify.name}";
					content += "</a>";

					content += "<a href='javascript:void(0);' id='shortcut_config_dictionary_itemSave_"+id+"' sid="+id+" class='' style='color:#999;' method celcalue='"+rowdata.itemValue+"' >";
					content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='dictionaryItem.save'/>";
					content += "</a>";

					content += "<a href='javascript:void(0);' id='shortcut_config_dictionary_itemReset_"+id+"' sid="+id+" class='' style='color:#999;'>";
					content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='dictionaryItem.reset'/>";
					content += "</a>";
					</security:authorize>
					</c:if> 
					jQuery("#dictionaryItemList").jqGrid('setRowData',ids[i],{act:"<div class='jqgridContainer'>" + content + "</div>"});
					<c:if test="${!empty config_dictionary_itemDelete}">
					<security:authorize ifAnyGranted="${config_dictionary_itemDelete.code}">
					jQuery("#dictionaryItemList").jqGrid('setRowData',ids[i],{del:"<a href='javascript:void(0);' id='shortcut_${config_dictionary_itemDelete.indexCode}_"+id+"' sid="+id+" class='shortcut_${config_dictionary_itemDelete.indexCode}' method><img src='${base}/${skin}/images/icon/Del.png' width='20' height='20' /></a>"});
					</security:authorize>
					</c:if>
			}	
		},
		caption:'<msg:message code="dictionaryItem.list"/>',
		toolbar: [true,"top"]
	});
	
	jQuery("#dictionaryItemList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	
	//增加字段行
	<c:if test="${!empty config_dictionary_itemCreate}">
	<security:authorize ifAnyGranted="${config_dictionary_itemCreate.code}">
	$('#addRow2').click(function(){
			if(dictType == null || dictType == ''){
						top.layer.msg("<msg:message code="dictionaryItem.select"/>");
						currcount == 0
						return false ;
		}
		count++;
		if(currcount == 0){
			currcount = 1;
			jQuery("#dictionaryItemList").jqGrid('addRowData',count,"{}");
			var data = {
					itemName:"<input name='itemName' id='"+count+"_itemName' class='valid-control' style='width:98%;'/>",
					itemValue:"<input name='itemValue' id='"+count+"_itemValue' class='valid-control' style='width:98%;'/>",
					act:"<c:if test="${!empty config_dictionary_itemModify}"><security:authorize ifAnyGranted="${config_dictionary_itemModify.code}"><div class='jqgridContainer'><a href='javascript:void(0);' id='shortcut_config_dictionary_itemModify_"+count+"' sid="+count+" class='' style='color:#999;'><img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${config_dictionary_itemModify.name}</a><a href='javascript:void(0);' id='shortcut_config_dictionary_itemSave_"+count+"'   sid="+count+" class='shortcut_config_dictionary_itemSave' method='add'><img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='dictionaryItem.save'/></a><a href='javascript:void(0);' id='shortcut_config_dictionary_itemReset_"+count+"'  sid="+count+" class='' style='color:#999;'><img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='dictionaryItem.reset'/></a></div></security:authorize></c:if>",
					del:"<c:if test="${!empty config_dictionary_itemDelete}"><security:authorize ifAnyGranted="${config_dictionary_itemDelete.code}"><a href='javascript:void(0);' id='shortcut_${config_dictionary_itemDelete.indexCode}_"+count+"' sid="+count+" class='shortcut_${config_dictionary_itemDelete.indexCode}' method='add'><img src='${base}/${skin}/images/icon/Del.png' width='20' height='20' /></a></security:authorize></c:if>"
			};
			jQuery("#dictionaryItemList").jqGrid('setRowData',count,data);
		}else{
			top.layer.msg("<msg:message code="dictionaryItem.notsave"/>");
			count--;
		}
		
	});	
	</security:authorize>
	</c:if>
	
	$(".shortcut_config_dictionary_itemModify").live("click",(function(){
		var rowid = $(this).attr("sid");
		if(currcount == 0){
			currcount = 1;
			jQuery("#dictionaryItemList").jqGrid('editRow',rowid);
			$(this).attr("class","").css("color","#999");
			$("#shortcut_config_dictionary_itemSave_"+rowid).attr("class","shortcut_config_dictionary_itemSave").css("color","#53056A");
			$("#shortcut_config_dictionary_itemReset_"+rowid).attr("class","shortcut_config_dictionary_itemReset").css("color","#53056A");
		}else{
			var index = $.layer({
		  	    shade : [0.5 , '#000' , true],
		  	    area : ['auto','auto'],
		  	  	offset : ['50px',''],
		  	    dialog : {
		  	        msg:"请先保存当前编辑的条目",
		  	        btns : 1, 
		  	        type : 4,
		  	        btn : ["确定"],
		  	        yes : function(){
		  	        	layer.close(index);
		  	        },
		  	        no : function(){
		  	        	layer.close(index);
		  	        }
		  	    }});
		}
	}));
			
	$(".shortcut_config_dictionary_itemSave").live("click",(function() {
		var data="";
		var celcalue = "";
		var method = $(this).attr("method");
		var rowid = $(this).attr("sid");
		var clsn2 = $("#"+rowid+"_itemName").attr("class");
		if(method == ""){
			celcalue = $(this).attr("celcalue");
			data = "c=${dictionary.c}&itemName="+$.trim($("#"+rowid+"_itemName").val())+"&id="+rowid+"&dictionaryCode="+$("#dictionaryCode").val()+"";
		}else {
			celcalue = $.trim($("#"+rowid+"_itemValue").val());
			data = "c=${dictionary.c}&itemName="+$.trim($("#"+rowid+"_itemName").val())+"&itemValue="+$.trim($("#"+rowid+"_itemValue").val())+"&dictionaryCode="+$("#dictionaryCode").val()+"";
		}
	var tempOpts2 = {
	   	   classNames:[clsn2],
		   validStatus:function(status){
			   if (status){
					$.ajax({
						url:'dictionaryitem/save.json',
						type:'post',
						timeout:'60000',
						dataType:'json',
						data: data, 
						success:function(jsonData){
							if (jsonData.status == "success"){
								currcount = 0;
								jQuery("#dictionaryItemList").jqGrid('setRowData',rowid,{itemName:$.trim($("#"+rowid+"_itemName").val()),itemValue:celcalue});
								$("#"+rowid).attr("editable","0");
								$("#shortcut_config_dictionary_itemSave_"+rowid).attr("class","").css("color","#999");
								$("#shortcut_config_dictionary_itemReset_"+rowid).attr("class","").css("color","#999");					
								$("#shortcut_config_dictionary_itemModify_"+rowid).attr("class","shortcut_config_dictionary_itemModify").css("color","#53056A");
								$("#dictionaryItemList").trigger("reloadGrid");	
								if(method != ""){
									$("#shortcut_config_dictionary_itemSave_"+rowid).attr("method","");
									$("#shortcut_config_dictionary_itemDelete_"+rowid).attr("method","");
									$("#shortcut_config_dictionary_itemSave_"+rowid).attr("sid",jsonData.gid);
									$("#dictionaryItemList").trigger("reloadGrid");	
								}
							}
						}
						});
				}
			}
		};
		$.extend(options2,tempOpts2);
		valid2.updateSetting(options2);
 		valid2.reset();
  		valid2.valid();
	}));
			
	$(".shortcut_config_dictionary_itemReset").live("click",( function() {
		currcount = 0;
		var rowid = $(this).attr("sid");
		jQuery("#dictionaryItemList").jqGrid('restoreRow',rowid);
		$(this).attr("class","").css("color","#999");	
		$("#shortcut_config_dictionary_itemSave_"+rowid).attr("class","").css("color","#999");	
		$("#shortcut_config_dictionary_itemModify_"+rowid).attr("class","shortcut_config_dictionary_itemModify").css("color","#53056A");
	}));
			
	$(".shortcut_${config_dictionary_itemDelete.indexCode}").live("click",(function(){
		var rowid = $(this).attr("sid");
		var method = $(this).attr("method");
		
		if(method != ""){
			currcount = 0;
			jQuery("#dictionaryItemList").jqGrid('delRowData',rowid); 
		}else{
			if(currcount == 0){
				var data = jQuery("#dictionaryItemList").jqGrid("getRowData",rowid);
			   	var index = top.$.layer({
			  	    shade : [0.5 , '#000' , true],
			  	    area : ['auto','auto'],
			  	  	offset : ['50px',''],
			  	    dialog : {
			  	        msg:"<msg:message code='receivermessage.delete'/>",
			  	        btns : 2, 
			  	        type : 4,
			  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
			  	        yes : function(){
			  	  		$.ajax({
							url:'dictionaryitem/delete.json?c=${config_dictionary_itemDelete.code}&ids='+rowid+'&prompt=itemName&itemName="+encodeURIComponent(encodeURIComponent(data.itemName))',
							type:'post',
							timeout:'60000',
							dataType:'json',
							success:function(jsonData,textStatus){
								top.layer.close(index);
								if (textStatus == "success"){
									if (jsonData.status == "success"){
										jQuery("#dictionaryItemList").jqGrid("delRowData",rowid);
									}
								}
							}
						});
			  	        },
			  	        no : function(){
			  	        	top.layer.close(index);
			  	        }
			  	    }
			  	});
			}else{
				top.layer.msg('<msg:message code="dictionaryItem.notsave"/>');
			}
		}
	})); 
});
function reload(){
	dictType = $('#dictionaryCode').val();
	jQuery("#dictionaryItemList").jqGrid("setGridParam",{postData:{c:"${dictionary.c}",dictionaryCode:$('#dictionaryCode').val()}});
	jQuery("#dictionaryItemList").trigger("reloadGrid");
	currcount = 0;
}

function tab(tag){
	for(var i = 1 ; i <= 2 ; i++){
		if(i == tag){
			$("#tab_"+i).addClass("selected");
			$("#div_"+i).css("visibility","visible");
		}else{
			$("#tab_"+i).removeClass("selected");
			$("#div_"+i).css("visibility","hidden");
		}
		if(tag=='2'){
			$.ajax({
				url:"dictionary/getDicList.json?c=${dictionary.c}",
				dataType:"json",
				type:"post",
				success:function(data){
					var diclist = data.dictionaryList;
					var size = data.dicsize;
					$("#dictionaryCode").attr("size",size);
					var option = "";
					if(diclist.length>0){
						for ( var i = 0; i < diclist.length; i++) {
							option += "<option value= "+diclist[i].code +">"+diclist[i].name+"</option>";
						}
						$("#dictionaryCode").text("");
						$("#dictionaryCode").append(option);
						if(diclist[0].code!=null){
							$("#dictionaryCode option:first").attr("selected","selected");
							$("#dictionaryCode").trigger("onchange");
						}
					}
				},
				error:function(){
				}
			});
		}
	}
}
</script>
</head >
<body class="skinMain">
<ul id="tabnav">
	<li class="tab_1" ><a href="#" id="tab_1" onclick="tab(1)" class="selected"><msg:message code="dictionaryItem.dictionary"/></a></li>
	<li class="tab_2" ><a href="#" id="tab_2" onclick="tab(2)" ><msg:message code="dictionaryitem.data" /></a></li>
</ul>
<div id="div_1" class="tab-div-show">
<form:form method="post" action="list" commandName="dictionary">
<input type="hidden" name="c" value="${dictionary.c}" />
<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
	<tr>
		<td>
			<table id="dictionaryList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</form:form>
</div>
<div id="div_2" class="tab-div-hide">
<form:form method="post" action="list" commandName="dictionaryItem">
<table cellpadding="0" cellspacing="1"  border="0" width="100%" class="skinMain">
	<tr>
		<td width="15%" valign="top">
			<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width: 208px; ">
				<div class="ui-jqgrid-view" id="gview_dictionaryItemList" style="width: 208px; ">
					<div class="ui-jqgrid-view" id="gview_dictionaryItemList" style="width: 208px; ">
						<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix"><msg:message code="dictionary.list"/></div>
						<div style="width: 208px;" class="ui-state-default ui-jqgrid-hdiv">
							<div class="ui-jqgrid-hbox">
								<select id="dictionaryCode" style="width:208px;border:0px;font-size:11pt;"  onchange="reload();">  
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
		</td>
		<td width="85%" valign="top">
			<table id="dictionaryItemList" ><tr><td>&nbsp;</td></tr></table>
		</td>
	</tr>
</table>
</form:form>
</div>	
</body>
</html>