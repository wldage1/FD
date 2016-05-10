<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
$(document).ready(function() {
	var setting = {
		async: {
			enable: true,
			url:"stree.json",
			dataType:"json",
			autoParam:["id"],
			dataFilter: function(treeId, parentNode, childNodes){
				if (!childNodes) return null;
				if (childNodes.stree){
					childNodes = childNodes.stree;
				}
				return childNodes;			
			}
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeAsync: function(treeId, treeNode){
				return treeNode ? treeNode.level < 100 : true;
			},
			onClick: function(e, treeId, treeNode){
				var id = treeNode.id;
				var name = treeNode.name;
				var code = treeNode.code;
				var path = treeNode.path;
				var level = treeNode.olevel;
				var childNode = treeNode.childNode;
				$("#parentId").attr("value", id);
				$("#parentName").attr("value", name);
				$("#treePath").attr("value", path);
				$("#treeLevel").attr("value", level);
				$("#isChildNode").attr("value", childNode);
				$("#menuContent").hide();
			}
		}
	};	
	//初始化树形菜单
	$.fn.zTree.init($("#treeDemo"), setting);
	//点击输入框弹出菜单
	$("#parentName").click(function(){
		var cityObj = $("#parentName");
		var cityOffset = $("#parentName").offset();
		$("#menuContent").css({
			left : cityOffset.left + "px",
			top : cityOffset.top + cityObj.outerHeight() + "px"
		}).slideDown("fast");
	});
	//点击选择按钮弹出菜单
	$("#menuSelect").click(function(){
		var cityObj = $("#parentName");
		var cityOffset = $("#parentName").offset();
		$("#menuContent").css({
			left : cityOffset.left + "px",
			top : cityOffset.top + cityObj.outerHeight() + "px"
		}).slideDown("fast");		
	});
	//页面鼠标点击处理事件
	$("body").bind("click", function(event){
		var id = $(event.target).attr("id");
		//判断是否点击属性下拉列表上
		if (typeof id !="undefined" && id.indexOf("treeDemo") < 0){
			if (id != "menuSelect" && id != "menuContent" && id != "parentName") {
				$("#menuContent").hide();
			}
		}
	});
	
	var options = {
		event:'focus',
		ajax:{
			url:'${base}/system/organization/valid.json'
		},
		validStatus:function(status){
			if (status){
				$("form#organization").submit();
			}
		}
	};
	$("form #btnok").valid(options);
});
</script>
</head>
<body class="skinMain">
<div id="menuContent" class="menuContent" style="display: none; position: absolute;">
	<ul id="treeDemo" class="ztree" style="margin-top: 0; width: 240px;"></ul>
</div>
<form:form method="post" action="save" commandName="organization">
<input type="hidden" name="turnCode" value="${system_organization.code}" />
<input type="hidden" name="c" value="${organization.c}"/>
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="operate" value="1" />
<form:input path="parentId" type="hidden"/>
<form:input path="treeLevel" type="hidden" /> 
<form:input path="treePath" type="hidden" />  
<form:input path="isChildNode" type="hidden"/>  
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.parent"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="parentName" type="text" maxlength="20" readonly="true" class="valid-control"/> 
						<a id="menuSelect" href="#"><msg:message code="organization.select"/></a>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.code"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="code" maxlength="10" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
					<form:input path="name" maxlength="50" class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.phone"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="phone" maxlength="15" class="valid-control"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.fax"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="fax" maxlength="15" class="valid-control"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.postcode"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="postCode" maxlength="10" class="valid-control"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.address"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="address" maxlength="250" class="valid-control"/>
					</td>
				</tr>																														
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.description"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%" class="skinLayout_lable">
						<form:input path="description" maxlength="250"class="valid-control" />
					</td>
				</tr>	
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="window.location.href='${base}${system_organization.controller}?c=${system_organization.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tableMargin"></td>
	</tr>
	<tr>
		<td></td>
	</tr>
</table>
</form:form>	
</body>
</html>
