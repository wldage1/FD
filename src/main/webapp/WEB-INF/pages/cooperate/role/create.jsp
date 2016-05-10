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
			url:"${base}/cooperate/organization/secendleveltree.json",
			dataType:"json",
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
		},
		view: {
			fontCss: setFontCss
		}
	};
	
	function setFontCss(treeId, treeNode) {
		return treeNode.nocheck == true ? {color:"gray"} : {};
	};

	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		if (childNodes.stree){
			childNodes = childNodes.stree;
		}
		return childNodes;
	}
	function beforeAsync(treeId, treeNode) {
		return treeNode ? treeNode.level < 5 : true;
	}

	function onClick(e, treeId, treeNode) {
		var nocheck = treeNode.nocheck;
		if(nocheck == false){
			var id = treeNode.id;
			var name = treeNode.name;
			$("#orgId").attr("value", id);
			$("#orgName").attr("value", name);
			hideMenu();
		}
	}

	function showMenu() {
		var cityObj = $("#orgName");
		var cityOffset = $("#orgName").offset();
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
					url:'${base}/cooperate/role/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						//提交表单
						$("form#role").submit();
					}
				}
			};
		$("form #btnok").valid(options);
	});

	function saveRole(form){
		var name = $('#name').val();
		$('#name').val($.trim(name));
		document.f.submit();
   }	
</script>
</head>
<body class="skinMain">
<div id="menuContent" class="menuContent" style="display: none; position: absolute;">
	<ul id="treeDemo" class="ztree" style="margin-top: 0; width: 240px;"></ul>
</div>
<form:form method="post" action="save" commandName="role" name="f">
<input type="hidden" name="c" value="${role.c}"/>
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="operate" value="1" />
<input type="hidden" name="turnCode" value="${cooperate_role.code}" />
<input type="hidden" name="coverParam" value="1" />
<form:input type="hidden" path="orgId" value="${role.orgId}" />
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
		 		<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.info" /><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						${orgName}
					</td>
				</tr>
		 
				<tr>
					<td width="15%" align="right"  class="skinLayout_info">
						<msg:message code="role.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="name" maxlength="20" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="role.description"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%" class="skinLayout_lable">
						<form:input path="description"  maxlength="250"/>
					</td>
				</tr>	
				<tr>
					<td width="15%" align="right">&nbsp;${errors}</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok"  value="<msg:message code="button.save"/>" id="btnok" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<c:if test="${empty errors}"><input type="button" class="btn2" onclick="window.location.href='${base}${cooperate_role.controller}?c=${cooperate_role.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/></c:if>
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
