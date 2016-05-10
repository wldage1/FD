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
	return treeNode ? treeNode.level < 1 : true;
}

function onClick(e, treeId, treeNode) {
	var id = treeNode.id;
	var name = treeNode.name;
	var path = treeNode.path;
	var level = treeNode.olevel;
	var childNode = treeNode.childNode;
	$("#parentId").attr("value", id);
	document.getElementById("parentName").value = name;
	$("#treePath").attr("value", path);
	$("#treeLevel").attr("value", level);
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
	$.fn.zTree.init($("#treeDemo"), setting);
	var options = {
			event:'focus',
			ajax:{
				url:'${base}/product/capitalconfig/valid.json'
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					//提交表单
					$("form#productCategory").submit();
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
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
				<form:form method="post" action="save" commandName="productCategory">
					<input type="hidden" name="c" value="${productCategory.c}"/>
					<input type="hidden" name="prompt" value="name" />
					<input type="hidden" name="turnCode" value="${product_capitalconfig.code}" />
					<input type="hidden" name="operate" value="1" />
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="category.parent"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<input name="parentId" type="hidden" id="parentId"/>
								<input name="treeLevel" type="hidden" id="treeLevel"/> 
								<input name="treePath" type="hidden" id="treePath"/>  
								<input name="parentName" type="text" maxlength="20" value="<msg:message code='category.tree.root'/>" readonly id="parentName" style="width: 200;"  class="valid-control"/> 
								<!--<a id="menuBtn" href="#" onclick="showMenu(); return false;"><msg:message code="category.select"/></a>-->
								<font color="red">&nbsp;*&nbsp;</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="category.name"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="name" maxlength="20"  class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="category.code"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="code" maxlength="20"  class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right">&nbsp;</td>
							<td width="85%" colspan="2" align="left">
								<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
							</td>
						</tr>
					</table>
				</form:form>
			</td>
		</tr>
	</table>
</body>
</html>
