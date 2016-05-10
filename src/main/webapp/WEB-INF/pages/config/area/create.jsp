<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
var setting = {
	async: {
		enable: true,
		url:"stree.json",
		dataType:"json",
		autoParam:["id"],
		dataFilter: filter
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeAsync: beforeAsync,
		//beforeClick: beforeClick,
		onClick: onClick
	}
};

function filter(treeId, parentNode, childNodes) {
	if (!childNodes) return null;
	if (childNodes.stree){
		childNodes = childNodes.stree;
	}
	return childNodes;
}
function beforeAsync(treeId, treeNode) {
	return treeNode ? treeNode.level < 100 : true;
}

function onClick(e, treeId, treeNode) {
	var id = treeNode.id;
	var name = treeNode.name;
	var code = treeNode.code;
	var path = treeNode.path;
	var level = treeNode.olevel;
	var childNode = treeNode.childNode;
	alert("id:"+id)
	alert("name:"+name)
	alert("code:"+code)
	alert("path:"+path)
	alert("childNode:"+childNode)
	alert("id:"+id)
	$("#parentId").attr("value", id);
	$("#parentName").attr("value", name);
	$("#treePath").attr("value", path);
	$("#treeLevel").attr("value", level);
	$("#isChildNode").attr("value", childNode);
	hideMenu();
}

function showMenu() {
	var cityObj = $("#parentName");
	var cityOffset = $("#parentName").offset();
	$("#menuContent").css({
		left : cityOffset.left + "px",
		top : cityOffset.top + cityObj.outerHeight() + "px"
	}).slideDown("fast");

	$("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
	$("#menuContent").hide();
	$("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
			event.target).parents("#menuContent").length > 0)) {
		hideMenu();
	}
}
$(document).ready(function() {
	//设置上级地区
	var parentName = "${area.name}";
	if(parentName)
		$("#parentName").val(parentName);
	else{
		$("#parentName").val("顶级地区");
		$("#parentId").val("0");
	}
	
	//$.fn.zTree.init($("#treeDemo"), setting);
	var options = {
			event:'focus',
			ajax:{
				url:'${base}/config/area/valid.json'
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					//提交表单
					$("form#area").submit();
				}
			}
		};
	$("form #btnok").valid(options);
});
</script>
</head>
<body class="skinMain">
<div id="menuContent" class="menuContent" style="display: none; position: absolute;">
	<ul id="treeDemo" class="ztree" style="margin-top: 0; width: 160px;"></ul>
</div>
<form:form method="post" action="save" commandName="area">
<input type="hidden" name="c" value="${c}"/>
<input type="hidden" name="prompt" value="name" />
<form:input path="parentId" type="hidden" value="${area.id}" />
<form:input path="treeLevel" type="hidden" value="${area.treeLevel}" /> 
<form:input path="treePath" type="hidden" value="${area.treePath}" />  
<form:input path="isChildNode" type="hidden" value="${area.isChildNode}" />  
<input type="hidden" name="turnCode" value="${config_area.code}"/>
<input type="hidden" name="coverParam" value="1"/>

<input type="hidden" name="operate" value="1" />
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="area.parent"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="parentName" type="text" readonly="true"  class="valid-control"/> 
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="area.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="name" maxlength="20" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="area.code"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="code" maxlength="6" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="area.pinyin"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="pinyin" maxlength="20" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					    <input type="button" class="btn2" onclick="window.location.href='${base}${config_area.controller}?c=${config_area.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form:form>
</body>
</html>
