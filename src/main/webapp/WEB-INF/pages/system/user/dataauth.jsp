<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="JavaScript">
var setting = {
	check: {
		enable: true
	},
	async: {
		enable: true,
		type: "post",
		url: "orgtree.json",
		otherParam: {"id":"${user.id}","orgId":"${user.orgId}"},
		dataFilter: filter
	},	
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "pId",
			rootPId:null
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
	zTree.setting.check.chkboxType = {"Y":"s", "N":"s"};
	$("#btnok").click(function(){
		var org = "";
		var sval = zTree.getCheckedNodes();
		if (!sval || sval.length <= 0){
			//window.parent.Boxy.alert("<msg:message code='info.prompt.auth'/>", null, {title: "<msg:message code='info.prompt'/>"});
		}
		else{
			
			for(var i=0; i<sval.length; i++){
				if (i ==0){
					org = sval[i].id;
				}
				else{
					org += "," + sval[i].id;
				}
			}
			
		}
		$("#org").val(org);
		$("form").submit();
	});
});
</script>
</head>
<body class="skinMain">
<form:form method="post" action="savesourceauth" commandName="user">
<form:hidden path="id"/>
<form:hidden path="c"/>
<form:hidden path="name"/>
<input type="hidden" name="turnCode" value="${system_user.code}" />
<input type="hidden" name="coverParam" value="1" />
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="org" id="org"/>
<input type="hidden" name="type" value="1"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%">${user.name}</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.belong.org"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%">${orgName}</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.select.manage.org"/><msg:message code="system.common.sign.colon"/>
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