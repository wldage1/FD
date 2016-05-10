<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script	type="text/javascript">
$(window).resize(function(){ 　　
	$("#globalparameterList").setGridWidth($(window).width()*0.999);　
});
$(window).resize(function(){ 　　
	$("#globalparameterListCom").setGridWidth($(window).width()*0.999);　
});
var count = 0;
var currcount = 0;
var deptid = null;
var zNodesCom = ${comJson};
//////////////////////////////////////////////////////////////////////////////////////////////////
var settingCom = {
		check: {
			enable: false
		},
		view:{
			dblClickExpand:dblClickExpandCom,
			showLine:true
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			onClick:this.onClick
		}
	};
   function dblClickExpandCom(treeId,treeNode){
		return treeNode.level > 0;
	}
	
	function onClick(e,treeid,node){
		
		if (node.level>0){
		 	deptid = node.id;
		 	$("#comId").val(deptid);
		 	reloadCom(deptid);
	 	}else{
	 		$("#comId").val("");
	 	}
	}
	function reloadCom(orgid){
		currcount = 0;
		jQuery("#globalparameterListCom").jqGrid("setGridParam",{postData:{c:"${globalparameter.c}",orgID:orgid}});		
		jQuery("#globalparameterListCom").trigger("reloadGrid");	
	}
$(document).ready(function(){
	///////////////
	$.fn.zTree.init($("#treeDemo1"), settingCom,zNodesCom);
	var treeObjCom = $.fn.zTree.getZTreeObj("treeDemo1");
	treeObjCom.expandAll(true);
	});

function RepNumber(obj) {
    var reg = /^[\d]+$/g;
     if (!reg.test(obj.value)) {
         var txt = obj.value;
         txt.replace(/[^0-9]+/, function (char, index, val) {//匹配第一次非数字字符
            obj.value = val.replace(/\D/g, "");//将非数字字符替换成""
            var rtextRange = null;
            if (obj.setSelectionRange) {
                obj.setSelectionRange(index, index);
            } else {//支持ie
                rtextRange = obj.createTextRange();
                rtextRange.moveStart('character', index);
                rtextRange.collapse(true);
                rtextRange.select();
            }
        })
     }
 }

var parameterName = "${parameterName}";
<c:forEach var="parameterName" items="${parameterName}" varStatus='st'>
	parameterName += "<option value=\"${parameterName.key}\" >${parameterName.value}</option>";
</c:forEach>

$(document).ready(function(){
	jQuery("#globalparameterList").jqGrid({
		    url: 'globalparameter/grid.json',
	        datatype: 'json',
	        colNames: ["<msg:message code='globalparameter.code'/>",
	        		   "<msg:message code='globalparameter.name'/>",
	                   "<msg:message code='globalparameter.value'/>",
	                   "<msg:message code='info.operate'/>",
	                   ],
	        colModel: [
	        		   {name:'code',index:'code',width:'15%',align:'center',hidden: false,sortable:false},
	                   {name:'name',index:'name',width:'15%',align:'center',hidden: false,sortable:false},
					   {name: 'value',index:'value',width:'15%',align:'center',hidden: false,sortable:false,editable:true},
	                   {name:'act',index:'act', width:'15%',align:'center',sortable:false},
	                   ],
	        mtype:"POST",
	        postData:{c:"${globalparameter.c}"},
	        rowNum:100,     
	        pager: false,
	        height:'auto',
	        autowidth: true,
	        viewrecords: true,
	        multiselect: false,
	        jsonReader: {
        		repeatitems: false
        	},
	        gridComplete: function(){
	    		var ids = jQuery("#globalparameterList").jqGrid('getDataIDs');
	    		for(var i=0;i < ids.length;i++){
	    			var id = ids[i];
	    			count = id;
	    			var rowdata = jQuery("#globalparameterList").jqGrid('getRowData',id);
	    			var content = "";
	    			<c:if test="${!empty config_globalparameter_modify}">
	    		    <security:authorize ifAnyGranted="${config_globalparameter_modify.code}">	
	    			content += "<a href='javascript:void(0);'  id='shortcut_config_globalparameter_modify_" +id+ "'sid='"+id+"' style='color:#53056A' class='shortcut_${config_globalparameter_modify.indexCode}' ";
	    			content += " title='${config_globalparameter_modify.name}'>";
	    			content += "<img src='${base}/common/skin/images/icon/password.png' weight='18' height='18' border='0' align='absmiddle'/>${config_globalparameter_modify.name}";
	    			content += "</a>";
	    		    </security:authorize>
	    		    </c:if>
	    			<c:if test="${!empty config_globalparameter_save}">
	    		    <security:authorize ifAnyGranted="${config_globalparameter_save.code}">	
	    			content += "<a href='javascript:void(0);'  id='shortcut_config_globalparameter_save_" +id+ "'sid='"+id+"' style='color:#999;' class='' ";
	    			content += " title='${config_globalparameter_save.name}' ;\"  >";
	    			content += "<img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle' method/>${config_globalparameter_save.name}";
	    			content += "</a>";
	    		    </security:authorize>
	    		    </c:if>  
	    			<c:if test="${!empty config_globalparameter_cancel}">
	    		    <security:authorize ifAnyGranted="${config_globalparameter_cancel.code}">	
	    			content += "<a href='javascript:void(0);' id='shortcut_config_globalparameter_cancel_" + id + "' sid='"+id+"' style='color:#999;' class=''";
	    			content += " title='${config_globalparameter_cancel.name}' >";
	    			content += "<img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${config_globalparameter_cancel.name}";
	    			content += "</a>";
	    		    </security:authorize>
	    		    </c:if>
	    		    jQuery("#globalparameterList").jqGrid('setRowData',ids[i],{act:"<div class='jqgridContainer'>" + content + "</div>"});
	    		    <c:if test="${!empty config_globalparameter_delete}">
					<security:authorize ifAnyGranted="${config_globalparameter_delete.code}">
					jQuery("#globalparameterList").jqGrid('setRowData',ids[i],{del:"<a href='javascript:void(0);' id='shortcut_${config_globalparameter_delete.indexCode}_"+id+"' sid="+id+" class='shortcut_${config_globalparameter_delete.indexCode}' method><img src='${base}/${skin}/images/icon/Del.png' width='20' height='20' /></a>"});
					</security:authorize>
					</c:if>
	    		}	
	    	},        
	    	caption:'<msg:message code="globalparameter.list"/>',
	        toolbar: [false,"top"]
	});
	

	//修改按钮
    $(".shortcut_config_globalparameter_modify").live("click",(function(){
		if(currcount == 0){
        	var rowid = $(this).attr("sid");
        	oldId = rowid;
			currcount = 1;
			jQuery("#globalparameterList").jqGrid('editRow',rowid);
			$(this).attr("class","").css("color","#999");
			$("#shortcut_config_globalparameter_save_"+rowid).attr("class","shortcut_config_globalparameter_save").css("color","#53056A");
			$("#shortcut_config_globalparameter_cancel_"+rowid).attr("class","shortcut_config_globalparameter_cancel").css("color","#53056A");
		}else{
			window.parent.Boxy.alert("<msg:message code="globalparameter.notsave"/>", null, {title: "<msg:message code='info.prompt'/>"});
		}
    }));
    
	//重置按钮
    $(".shortcut_config_globalparameter_cancel").live("click",( function() {
		currcount = 0;
		var rowid = $(this).attr("sid");
		jQuery("#globalparameterList").jqGrid('restoreRow',rowid);
		$(this).attr("class","").css("color","#999");	
		$("#shortcut_config_globalparameter_save_"+rowid).attr("class","shortcut_config_globalparameter_save").css("color","#999");	
		$("#shortcut_config_globalparameter_modify_"+rowid).attr("class","shortcut_config_globalparameter_modify").css("color","#53056A");
	}));
	
	//保存按钮
    $(".shortcut_config_globalparameter_save").live("click",(function(){
		var data="";
		var celcalue = "";
		var method = $(this).attr("method");
		var rowid = $(this).attr("sid");
		var regvalue = /^[A-Za-z0-9]{1,10}$/;
		if($("#"+rowid+"_value").val() == null || $("#"+rowid+"_value").val() == ''){
			top.layer.msg('参数值不能为空');
			return false;
		}
		//判断是否为修改保存或增加保存
		if($("#"+rowid+"_parameterName").find("option:selected").text()==null || $("#"+rowid+"_parameterName").find("option:selected").text()==''){
			data = "c=${globalparameter.c}&value="+$("#"+rowid+"_value").val()+"&id="+rowid+"";
		}else{
			data = "c=${globalparameter.c}&code="+$("#"+rowid+"_parameterName").get(0).selectedIndex+"&orgId="+deptid+"&name="+$("#"+rowid+"_parameterName").find("option:selected").text()+"&value="+$("#"+rowid+"_value").val()+"";
		}
		$.ajax({
    		url:'globalparameter/save.json',
    		type:'post',
    		timeout:'60000',
    		dataType:'json',
			data: data, 
    		success:function(jsonData){
    			if (jsonData.status == "success"){
					currcount = 0;
					jQuery("#globalparameterList").jqGrid('setRowData',rowid,{parameterName:$("#parameterName_"+rowid).text(),value:$("#"+rowid+"_value").val()});
					$("#"+rowid).attr("editable","0");
					$("#shortcut_config_globalparameter_save_"+rowid).attr("class","").css("color","#999");
					$("#shortcut_config_globalparameter_cancel_"+rowid).attr("class","").css("color","#999");
					$("#shortcut_config_globalparameter_modify_"+rowid).attr("class","shortcut_config_globalparameter_modify").css("color","#53056A");
					if(method != ""){
						$("#shortcut_config_globalparameter_save_"+rowid).attr("method","");
						$("#shortcut_config_globalparameter_delete_"+rowid).attr("method","");
						$("#shortcut_config_globalparameter_save_"+rowid).attr("sid",jsonData.gid);
					}
					jQuery("#globalparameterList").trigger("reloadGrid");
    			}else{
					window.parent.Boxy.alert(jsonData.message, null, {title: "<msg:message code='info.prompt'/>"});
				}
    			
    		}
    	});
	}));
    ///////////////////////////////居间公司全局参数配置///////////////////////////////////////////////////
    jQuery("#globalparameterListCom").jqGrid({
		    url: 'globalparameterCom/grid.json',
	        datatype: 'json',
	        colNames: ["<msg:message code='globalparameter.orgName'/>",
	        		   "<msg:message code='globalparameter.code'/>",
	        		   "<msg:message code='globalparameter.name'/>",
	                   "<msg:message code='globalparameter.value'/>",
	                   "<msg:message code='info.operate'/>",
	                   ],
	        colModel: [
	        		   {name:'orgName',index:'orgName',width:'15%',align:'center',hidden: false,sortable:false},
	        		   {name:'code',index:'code',width:'15%',align:'center',hidden: false,sortable:false},
	                   {name:'name',index:'name',width:'15%',align:'center',hidden: false,sortable:false},
					   {name: 'value',index:'value',width:'15%',align:'center',hidden: false,sortable:false,editable:true},
	                   {name:'act',index:'act', width:'15%',align:'center',sortable:false,
                	   formatter:function(cellvalue, options, rowdata){
		     				var content = "";
							var id = rowdata.id;
			    			<c:if test="${!empty config_globalparameter_modifycom}">
			    		    <security:authorize ifAnyGranted="${config_globalparameter_modifycom.code}">	
			    			content += "<a href='javascript:void(0);'  class='shortcut_${config_globalparameter_modifycom.indexCode}' id='"+id+"'";
			    			content += " title='${config_globalparameter_modifycom.name}'>";
			    			content += "<img src='${base}/common/skin/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${config_globalparameter_modifycom.name}";
			    			content += "</a>";
			    		    </security:authorize>
			    		    </c:if>
			    		    return content;
	                   	}
                  	   }],
	        mtype:"POST",
	        postData:{c:"${globalparameter.c}"},
	        rowNum:100,     
	        pager: false,
	        height:'auto',
	        autowidth: true,
	        viewrecords: true,
	        multiselect: false,
	        jsonReader: {
        		repeatitems: false
        	},       
	    	caption:'<msg:message code="globalparameter.list"/>',
	        toolbar: [true,"top"]
	});
    $(".shortcut_${config_globalparameter_modifycom.indexCode}").live("click",function(){
		var rowid = $(this).attr("id");
		window.location.href = "${base}${config_globalparameter_modifycom.controller}?c=${config_globalparameter_modifycom.code}&id="+ rowid;
	});
    <c:if test="${!empty config_globalparameter_createcom}">
		<security:authorize ifAnyGranted="${config_globalparameter_createcom.code}">
			var $content = $("<a></a>").attr("href","javascript:void(0)")
						   .attr("id","${config_globalparameter_createcom.indexCode}")
						   .append($("<img/>").attr("src","${base}/${skin}/images/icon/create.png")
						   .attr("width","18").attr("height","18").attr("border",0)
						   .attr("border","0").attr("align","absmiddle"))
						   .append("${config_globalparameter_createcom.name}");
			
			$("#t_globalparameterListCom").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
			$("#${config_globalparameter_createcom.indexCode}","#t_globalparameterListCom").click(function(){
				if ($('#comId').val()==""){
					top.layer.msg("<msg:message code='globalparameter.chooseCom'/>");
					return false;
				}
			window.location.href = "${base}${config_globalparameter_createcom.controller}?c=${config_globalparameter_createcom.code}&orgID="+$('#comId').val();
			});
		</security:authorize>
	</c:if>
})
function tab(tag){
	for(var i = 1 ; i <= 2 ; i++){
		if(i == tag){
			$("#tab_"+i).addClass("selected");
			$("#div_"+i).css("visibility","visible");
		}else{
			$("#tab_"+i).removeClass("selected");
			$("#div_"+i).css("visibility","hidden");
		}
	}
}
</script>
</head>
<body class="skinMain">
<ul id="tabnav">
	<li class="tab_1" ><a href="#" id="tab_1" onclick="tab(1)" class="selected"><msg:message code='globalparameter.compara'/></a></li>
	<li class="tab_2" ><a href="#" id="tab_2" onclick="tab(2)"><msg:message code='globalparameter.syspara'/></a></li>
</ul>
<input type="hidden" name="orgId" id="orgId"/>
<input type="hidden" name="comId" id="comId"/>
<input type="hidden" name="c" id="c" value="${globalparameter.c}" />
<div id="div_1" class="tab-div-show">
<table style="width:100%" border="0">
	<tr>
		<td width="308" valign="top">
			<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width:308px; ">
				<div class="ui-jqgrid-view" id="gview_dictionaryItemList" style="width:308px;">
					<div class="ui-jqgrid-view" id="gview_dictionaryItemList" style="width:308px;">
						<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
							<msg:message code="company.list"/>
						</div>
						<div style="width:308px;height:100%; " class="ui-state-default ui-jqgrid-hdiv">
							<div class="ui-jqgrid-hbox">
								<ul id="treeDemo1" class="ztree" style="margin-top:0px;width:300px;"></ul>  
							</div>
						</div>
					</div>
				</div>
			</div>		
		</td>
		<td valign="top" width="80%">
			<table id="globalparameterListCom"><tr><td>&nbsp;</td></tr></table>	
			<div id="pageredCom"></div>
		</td>
	</tr>
	<tr>
		<td class="tableMargin"></td>
	</tr>
</table>
</div>
<div id="div_2" class="tab-div-hide">
<table style="width:100%" border="0">
	<tr>
		<td valign="top" width="80%">
			<table id="globalparameterList"><tr><td>&nbsp;</td></tr></table>	
			<div id="pagered"></div>
		</td>
	</tr>
	<tr>
		<td class="tableMargin"></td>
	</tr>
</table>
</div>
</body>
</html>