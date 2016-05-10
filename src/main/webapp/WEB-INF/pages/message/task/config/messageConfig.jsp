<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
<c:if test="${messageTask.isConfigReceiver == 1}">
var treeParam = {};
<c:if test="${user.isCommission == 1}">
	treeParam['id'] = '${user.id}';
	treeParam['orgId'] = "${user.orgId}";
</c:if>
var setting = {
		async: {
			enable: true,
			url: "${base}/cooperate/user/roletree.json",
			otherParam: treeParam,
			dataType:"json",
			dataFilter: commissionFilter
		},
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onAsyncSuccess: zTreeOnAsyncSuccess
		}
	};
function commissionFilter(treeId, parentNode, childNodes){
	if (!childNodes) return null;
	if (childNodes.stree){
		var treeAll = childNodes.stree;
		var orgRoleTree = new Array();
		$(treeAll).each(function(){
			//设置为不选
			this.checked=false;
			//删除运营方的角色
			if(this.isCommission == 0){
				orgRoleTree.push(this);
				treeAll.splice($.inArray(this,treeAll),1);
			}
			//增加显示机构名称
			this.name = this.name + "(" + this.orgName + ")";
		});
		childNodes = treeAll;
	}
	//$.fn.zTree.init($("#roleTreeOrg"), setting2,orgRoleTree);
	//setOrgTreeSelected();
	return childNodes; 
}	
var setting2 = {
		async: {
			enable: true,
			url: "${base}/system/user/roletree.json",
			otherParam: {"id":"${user.id}","orgId":"${user.orgId}"},
			dataType:"json",
			dataFilter: commissionFilter2
		},
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onAsyncSuccess: setOrgTreeSelected
		}
	};
function commissionFilter2(treeId, parentNode, childNodes){
	if (!childNodes) return null;
	if (childNodes.stree){
		var treeAll = childNodes.stree;
		var orgRoleTree = new Array();
		$(treeAll).each(function(){
			//设置为不选
			this.checked=false;
			//删除运居间公司的角色
			if(this.isCommission == 1){
				orgRoleTree.push(this);
				treeAll.splice($.inArray(this,treeAll),1);
			}
			//增加显示机构名称
			this.name = this.name + "(" + this.orgName + ")";
		});
		childNodes = treeAll;
	}
	return childNodes; 
}	
//初始化角色树
function zTreeOnAsyncSuccess(){
	var configRoleIDs = '${messageTask.receiverRoles}';
	if(configRoleIDs != '' ){
		var roleIdArray = configRoleIDs.split(',');
		var zTree = $.fn.zTree.getZTreeObj("roleTree");
		var sval = zTree.getNodes();
		if (sval || sval.length > 0){
			for(var i=0; i<sval.length; i++){
				if(jQuery.inArray( sval[i].id.toString(), roleIdArray ) != -1){
					zTree.checkNode(sval[i], true, true);
				}
			} 
		}
	}
}
//设置运营机构树选中状态
function setOrgTreeSelected(){
    var configRoleIDs = '${messageTask.receiverRoles}';
	if(configRoleIDs != '' ){
	var roleIdArray = configRoleIDs.split(',');
	var zTreeOrg = $.fn.zTree.getZTreeObj("roleTreeOrg");
	var sval2 = zTreeOrg.getNodes();
	if (sval2 || sval2.length > 0){
		for(var i=0; i<sval2.length; i++){
			if(jQuery.inArray( sval2[i].id.toString(), roleIdArray ) != -1){
				zTreeOrg.checkNode(sval2[i], true, true);
			}
		} 
	}
  }	 
}
//设置角色提交数据
function setBindValue(){
	var roles = "";
	var rTree = $.fn.zTree.getZTreeObj("roleTree");
	var sval = rTree.getCheckedNodes();
	<c:if test="${user.isCommission == 0}">
	var rTreeOrg = $.fn.zTree.getZTreeObj("roleTreeOrg");
	sval = $.merge(sval, rTreeOrg.getCheckedNodes());
	</c:if>
	if (!sval || sval.length <= 0){
		
	}else{	
		for(var i=0; i<sval.length; i++){
			if (i ==0){ 
				roles = sval[i].id;
			}else{ 
				roles += "," + sval[i].id;
			}
		} 
	}
	$("#receiverRoles").val(roles);
}
//设置用户提交数据
function setReceiverUserIDs(){
	var  userarray = [];
	//选中的供应商
	$("input[name='provider_users_checkbox']:checked").each(function(){
		var ruersIdObj = {};
		ruersIdObj.receiverType= 2;
		ruersIdObj.receiverID= this.value;
		userarray.push(ruersIdObj);
	});
	//选中的iag
	$("input[name='iag_users_checkbox']:checked").each(function(){
		var ruersIdObj = {};
		ruersIdObj.receiverType= 1;
		ruersIdObj.receiverID= this.value;
		userarray.push(ruersIdObj);
	});
	//选中的居间公司用户
	$("input[name='org_users_checkbox']:checked").each(function(){
		var ruersIdObj = {};
		ruersIdObj.receiverType= 4;
		ruersIdObj.receiverID= this.value;
		userarray.push(ruersIdObj);
	});
	if(userarray.length >0 ){
		$("#receiverUserIDsJSON").val($.toJSON(userarray));
	}else{
		$("#receiverUserIDsJSON").val("");
	}
	
}
function checkOrUncheckAll(el,checkboxName){
	if($(el).attr('checked')){
		$('input[name='+ checkboxName +']').attr("checked","true");
	}else{
		$('input[name='+ checkboxName +']').removeAttr("checked");
	}
	
}
</c:if>
$(document).ready(function(){
	<c:if test="${messageTask.isConfigReceiver == 1}">
	$.fn.zTree.init($("#roleTree"), setting);
	var zTree = $.fn.zTree.getZTreeObj("roleTree");
	zTree.setting.check.chkboxType = {"Y":"ps", "N":"s"};
	<c:if test="${user.isCommission == 0}">	
	$.fn.zTree.init($("#roleTreeOrg"), setting2);
	var zTree2 = $.fn.zTree.getZTreeObj("roleTreeOrg");
	zTree2.setting.check.chkboxType = {"Y":"ps", "N":"s"};
	</c:if>
	</c:if>
	
	//隐藏所以模板标签
	$("#system").hide();
	$("#note").hide();
	$("#email").hide();
	$("#im").hide();
	//模板类型多选回显
	var type = "${messageTask.templateType}".toString().split(",");
	var types = $("input:checkbox[name=templateType]");
	for(var i=0;i < types.length;i++){
		for(var j=0;j<type.length;j++){
			if(types[i].value == type[j]){
				types[i].checked = true;
				break;
			}
		}
	}
	//根据模板类型显示模板标签
	$("input[name=templateType]:checked").each(function(){
		templateTypeToggle(this);
	});
	
	//模板标签点击事件
    $(".templateContent").click(function(){
    	var id = this.id;
    	$("#"+id).addClass("templateTab");
    	$("div[id!="+id+"]").removeClass("templateTab");
    	//toggleClass
    	var content,title,status;
    	if(id == "system"){
    		//选择模板状态
    		status = $("#systemTemplateStatus").val();
    		//显示模板内容
    		content=$("#systemTemplateContent").val();
    		//更改模板标题
    		title="<msg:message code='messageTemplate.systemContent'/><msg:message code='system.common.sign.colon'/>"
    	}else if(id == "note"){
    		status = $("#noteTemplateStatus").val();;
    		content=$("#noteTemplateContent").val();
    		title="<msg:message code='messageTemplate.noteContent'/><msg:message code='system.common.sign.colon'/>"
    	}else if(id == "email"){
    		status = $("#emailTemplateStatus").val();;
    		content=$("#emailTemplateContent").val();
    		title="<msg:message code='messageTemplate.emailContent'/><msg:message code='system.common.sign.colon'/>"
    	}else{
    		status = $("#imTemplateStatus").val();;
    		content=$("#imTemplateContent").val();
    		title="<msg:message code='messageTemplate.imContent'/><msg:message code='system.common.sign.colon'/>"
    	}
    	$("input:radio[name=templateStatus]").each(function(){
			if(status == this.value){
				this.checked = true;
			}
		});
    	$("#templateTitle").html(title);
		$("#templateContent").val(content);
    });
 	//模拟点击第一个模板标签
 	var tab = $("input[name=templateType]:checked");
 	if(typeof(tab[0]) != "undefined"){
 		switch (parseInt(tab[0].value)){
 		case 1:
 			$("#system").click();
 			break;
 		case 2:
 			$("#note").click();
 			break;
 		case 3:
 			$("#email").click();
 			break;
 		case 4:
 			$("#im").click();
 			break;
 		}
 	}
	
    //模板类型多选框单击事件
    $("input:checkbox[name=templateType]").click(function(){
    	templateTypeToggle(this);
    	switch (parseInt(this.value)){
    	case 1:
    		$("#system").click();
    		break;
    	case 2:
    		$("#note").click();
    		break;
    	case 3:
    		$("#email").click();
    		break;
    	case 4:
    		$("#im").click();
    		break;
    	}
    });
    
  	//验证
	var options = {
			event:'focus',
			ajax:{
				url:'${base}/message/config/valid.json'
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					<c:if test="${messageTask.isConfigReceiver == 1}">
					//设置接收者角色
					setBindValue();
					//设置供应商和后台用户接收者id
					setReceiverUserIDs();
					</c:if>
					//提交表单
					$("form#messageTask").submit();
				}
			}
		};
	$("form #btnok").valid(options);
    
});

//模板类型显示隐藏切换
function templateTypeToggle(obj){
	switch (parseInt(obj.value)){
	case 1:
		$("#system").toggle();
		break;
	case 2:
		$("#note").toggle();
		break;
	case 3:
		$("#email").toggle();
		break;
	case 4:
		$("#im").toggle();
		break;
	}
}

//保存改变后的模板内容
function textChange(){
	var id = $(".templateTab").attr("id");
	var content = $("#templateContent").val();
	if(id == "system"){
		$("#systemTemplateContent").val(content);
	}else if(id == "note"){
		$("#noteTemplateContent").val(content);
	}else if(id == "email"){
		$("#emailTemplateContent").val(content);
	}else{
		$("#imTemplateContent").val(content);
	}
}

//保存改变后的模板状态
function statusChange(){
	var id = $(".templateTab").attr("id");
	var status = $("input[name=templateStatus]:checked").val();
	if(id == "system"){
		$("#systemTemplateStatus").val(status);
	}else if(id == "note"){
		$("#noteTemplateStatus").val(status);
	}else if(id == "email"){
		$("#emailTemplateStatus").val(status);
	}else{
		$("#imTemplateStatus").val(status);
	}
}


</script>

<style type="text/css">

input[type="radio"] {
	width  : 14px;
	height : 14px;
	vertical-align:text-bottom; margin-bottom:-1px; margin-bottom:-2px\9;
}
input[type="checkbox"] {
	width  : 14px;
	height : 14px;
	vertical-align:text-bottom; margin-bottom:-1px; margin-bottom:-2px\9;
}
</style>

</head>
<body class="skinMain">
	<form:form method="post" action="save" id="messageTask" commandName="messageTask" >
		<input type="hidden" name="id" id="id" value="${messageTask.id}" />
		<input type="hidden" name="c" value="${messageTask.c}" />
		
		<input type="hidden" name="prompt" value="name"/>
		<input type="hidden" name="name" value="${messageTask.name}"/>
		<input type="hidden" name="isConfigReceiver" value="${messageTask.isConfigReceiver}"/>
		<input type="hidden" name="turnCode" value="${message_task_config.code}"/>
		<input type="hidden" name="coverParam" value="1"/>
		<c:if test="${messageTask.isConfigReceiver == 1}">
		<input type="hidden" name="receiverRoles" id="receiverRoles" value="${messageTask.receiverRoles}"/>
		<form:hidden path="receiverUserIDsJSON" value="${messageTask.receiverUserIDsJSON }"/>
		</c:if>
		
	
		<input type="hidden" name="systemTemplateId" id="systemTemplateId" value="${messageTask.systemTemplateId}" />
		<input type="hidden" name="noteTemplateId" id="noteTemplateId" value="${messageTask.noteTemplateId}" />
		<input type="hidden" name="emailTemplateId" id="emailTemplateId" value="${messageTask.emailTemplateId}" />
		<input type="hidden" name="imTemplateId" id="imTemplateId" value="${messageTask.imTemplateId}" />
		<input type="hidden" name="systemTemplateCode" id="systemTemplateCode" value="${messageTask.systemTemplateCode}" />
		<input type="hidden" name="noteTemplateCode" id="noteTemplateCode" value="${messageTask.noteTemplateCode}" />
		<input type="hidden" name="emailTemplateCode" id="emailTemplateCode" value="${messageTask.emailTemplateCode}" />
		<input type="hidden" name="imTemplateCode" id="imTemplateCode" value="${messageTask.imTemplateCode}" />
	
		<input type="hidden" name="systemTemplateStatus" id="systemTemplateStatus" value="${messageTask.systemTemplateStatus}" />
		<input type="hidden" name="noteTemplateStatus" id="noteTemplateStatus" value="${messageTask.noteTemplateStatus}" />
		<input type="hidden" name="emailTemplateStatus" id="emailTemplateStatus" value="${messageTask.emailTemplateStatus}" />
		<input type="hidden" name="imTemplateStatus" id="imTemplateStatus" value="${messageTask.imTemplateStatus}" />
		
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
						<tr>
							<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info"><msg:message code="messageTask.name"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="38%" class="skinLayout_lable">
								${messageTask.name}
							</td>
							<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info"><msg:message code="messageTask.code"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="38%" class="skinLayout_lable">
								${messageTask.code}
							</td>
						</tr>
						<tr>
							<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info"><msg:message code="messageTask.sendType"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="38%" class="skinLayout_lable">
								<c:choose>
									<c:when test="${messageTask.sendType ==1}"><msg:message code="messageTask.immediatelySend"/></c:when>
									<c:when test="${messageTask.sendType ==2}"><msg:message code="messageTask.timingSend"/></c:when>
								</c:choose>
							</td>
							<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info"><msg:message code="messageTask.status"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="38%" class="skinLayout_lable">
								<form:radiobutton path="status" value="1" /><msg:message code="messageTask.forbidden"/>
								<form:radiobutton path="status" value="2" /><msg:message code="messageTask.enabled"/>
								<form:radiobutton path="status" value="3" /><msg:message code="messageTask.pause"/>
							</td>
						</tr>
						<tr>
							<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info"><msg:message code="messageTemplate.name"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="38%" class="skinLayout_lable">
								<form:input path="templateName"  maxlength="100" class="valid-control"/>
							</td>
							<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info"><msg:message code="messageTemplate.status"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="38%" class="skinLayout_lable">
								<form:radiobutton onblur="statusChange()" path="templateStatus" value="1" />
								<msg:message code="messageTemplate.valid"/>
								<form:radiobutton onblur="statusChange()" path="templateStatus" value="2" />
								<msg:message code="messageTemplate.invalid"/>
							</td>
						</tr>
				<c:if test="${messageTask.isConfigReceiver == 1}">
						<tr id="roletreetr">
							<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info">居间公司接收角色
								<msg:message code="system.common.sign.colon" /></td>
							<td width="38%" style="vertical-align: top;">
								<div>
									<ul id="roleTree" class="ztree"
										style="margin-top: 0px; width: 96%;"></ul>
								</div>
							</td>
							<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info">居间公司接收者
								<msg:message code="system.common.sign.colon" /></td>
							<td width="38%" >
								<div>
									<table id="select_org_users" cellpadding="0" cellspacing="0" style="border-collapse:collapse">
									<tr style="font-weight: bold;"><td><input type="checkbox" onclick="checkOrUncheckAll(this,'org_users_checkbox')"/></td><td style="white-space:nowrap;word-break:nowrap">姓名</td><td>居间公司</td></tr>
									<c:forEach items="${userList }" var="org">
									   <c:if test="${org.isCommission ==  1}">
										   <tr><td><input type="checkbox" value="${org.id }" 
										       <c:if test="${org.receiverID >0}">
										           checked
										        </c:if>
										      name="org_users_checkbox"/></td>
										   <td>${org.name }</td><td>${org.orgName}</td></tr>
										</c:if>
									</c:forEach>	
									</table>
								</div>
							</td>
						</tr>
					<c:if test="${user.isCommission == 0}">	
						<tr>
							<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info">运营方接收角色
								<msg:message code="system.common.sign.colon" /></td>
							<td width="38%" style="vertical-align: top;">
							    <div>
								  <ul id="roleTreeOrg" class="ztree"
										style="margin-top: 0px; width: 96%; "></ul>
								</div>		
							</td>
							<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info">运营方接收者
								<msg:message code="system.common.sign.colon" /></td>
							<td width="38%">
							   <div>
									<table id="select_iag_users" cellpadding="0" cellspacing="0" style="border-collapse:collapse;width: 100%;" >
									<tr style="font-weight: bold;"><td><input type="checkbox" onclick="checkOrUncheckAll(this,'iag_users_checkbox')"/></td><td style="white-space:nowrap;word-break:nowrap">姓名</td><td>机构</td></tr>
									<c:forEach items="${userList }" var="iag">
									   <c:if test="${iag.isCommission ==  0}">
										   <tr><td><input type="checkbox" value="${iag.id }" 
										      <c:if test="${iag.receiverID >0}">
										          checked
										      </c:if>
										    name="iag_users_checkbox"/></td>
										   <td>${iag.name }</td><td>${iag.orgName }</td></tr>
										</c:if>
									</c:forEach>	
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info">供应商接收者
								<msg:message code="system.common.sign.colon" /></td>
							<td width="38%">
								<div>
									<table id="select_provider_users" cellpadding="0" cellspacing="0" style="border-collapse:collapse">
									<tr style="font-weight: bold;"><td><input type="checkbox" onclick="checkOrUncheckAll(this,'provider_users_checkbox')"/></td><td style="white-space:nowrap;word-break:nowrap">姓名</td><td>账号</td></tr>
									<c:forEach items="${providerUserList }" var="providerUser">
										<tr><td><input type="checkbox" value="${providerUser.id }" 
										<c:if test="${providerUser.receiverID >0}">
										   checked
										</c:if>
										 name="provider_users_checkbox"/></td>
										<td>${providerUser.name }</td><td>${providerUser.account }</td></tr>
									</c:forEach>	
									</table>
								</div>
							</td>
								<td width="12%" style="word-break: keep-all;white-space:nowrap;" align="right" class="skinLayout_info">
								</td>
							<td width="38%" style="vertical-align: top;">
							</td>
						</tr>
						</c:if>
					</c:if>	
						<tr>
							<td width="10%" align="right" class="skinLayout_info" ><msg:message code="messageTemplate.type"/>
								<msg:message code="system.common.sign.colon"/></td>
							<td width="90%" class="skinLayout_lable" colspan="3">
								<form:checkbox path="templateType" value="1"/><msg:message code="messageTemplate.system"/>
								<form:checkbox path="templateType" value="2"/><msg:message code="messageTemplate.note"/>
								<form:checkbox path="templateType" value="3"/><msg:message code="messageTemplate.email"/>
								<form:checkbox path="templateType" value="4" class="valid-control"/><msg:message code="messageTemplate.IM"/>
							</td>
						</tr>
						<tr>
							<td width="10%" align="right" class="skinLayout_info" id="templateTitle"><msg:message code="messageTemplate.systemContent"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="90%" class="skinLayout_lable" colspan="3">
								<div style="width:710px;height:260px;">
									<div style="width:710px;height:230px;">
										<textarea id="templateContent" rows="5" cols="30"  onblur="textChange()" style="width: 710px; height: 230px;" ></textarea>
									</div>
									<div style="width:710px;height:25px;">
										<div id="system" style="border-right-width:0px;" class="templateContent">
											<msg:message code="messageTemplate.system"/>
											<form:hidden path="systemTemplateContent" value="${messageTask.systemTemplateContent}" cssClass="valid-control"/>
										</div>
										<div id="note" style="border-right-width:0px;" class="templateContent">
											<msg:message code="messageTemplate.note"/>
											<form:hidden path="noteTemplateContent" value="${messageTask.noteTemplateContent}" cssClass="valid-control"/>
										</div>
										<div id="email" style="border-right-width:0px;" class="templateContent">
											<msg:message code="messageTemplate.email"/>
											<form:hidden path="emailTemplateContent" value="${messageTask.emailTemplateContent}" cssClass="valid-control"/>
										</div>
										<div id="im" class="templateContent">
											<msg:message code="messageTemplate.IM"/>
											<form:hidden path="imTemplateContent" value="${messageTask.imTemplateContent}" cssClass="valid-control"/>
										</div>
									</div>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
    <div style="height: 43px;"></div>
		<div class="toolbar">
		<div class="left"></div>
		<div class="center">
		   <input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		   <input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
		</div>
		<div class="right"></div>
	</div>
	</form:form>
</body>
