<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="JavaScript">
var setting = {
		async: {
			enable: true,
			url: "roletree.json",
			otherParam: {"id":"${user.id}","orgId":"${user.orgId}"},
			dataType:"json",
			dataFilter: filter
		},
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};
function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		if (childNodes.stree){
			childNodes = childNodes.stree;
		}
		return childNodes;
	}
$(document).ready(function(){
	$.fn.zTree.init($("#treeDemo"), setting);
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.setting.check.chkboxType = {"Y":"ps", "N":"s"};
	$("#btnok").click(function(){
		var bind = "";
		var sval = zTree.getCheckedNodes();
		if (!sval || sval.length <= 0){
			//window.parent.Boxy.alert("<msg:message code='info.prompt.bind'/>", null, {title: "<msg:message code='info.prompt'/>"});
		}
		else{
			
			for(var i=0; i<sval.length; i++){
				if (i ==0){
					bind = sval[i].id;
				}
				else{
					bind += "," + sval[i].id;
				}
			}
			
		}
		$("#bind").val(bind);
		$("form").submit();
	});
});
</script>
</head>
<body class="skinMain">
<form:form method="post" action="saveBind" commandName="user">
<input type="hidden" name="turnCode" value="${system_user.code}" />
<input type="hidden" name="coverParam" value="1" />
<input type="hidden" name="id" value="${user.id}"/>
<input type="hidden" name="c" value="${user.c}"/>
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="bind" id="bind"/>
<input type="hidden" name="type" value="1"/>
<input type="hidden" name="operate" value="bind"/>
<input type="hidden" name="name" value="${user.name}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" >${user.name}</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.belong.org"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%">${orgName}</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.select.bind"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%">
						<div>
							<ul id="treeDemo" class="ztree" style="margin-top:0px;width:100%;"></ul>
						</div>
					</td>
				</tr>	
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.confirm"/>" id="btnok"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
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
