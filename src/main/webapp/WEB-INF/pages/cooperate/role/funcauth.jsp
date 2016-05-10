<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
		url: "authtree.json",
		otherParam: ["id", "${role.id}"],
		dataFilter: function (treeId, parentNode, responseData) {
	        return responseData.treeJsonData;
	   }
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
$(document).ready(function(){
	$.fn.zTree.init($("#treeDemo"), setting,[]);
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.setting.check.chkboxType = {"Y":"ps", "N":"s"};
	$("#btnok").click(function(){
		var auth = "";
		var sval = zTree.getCheckedNodes();
		if (!sval || sval.length <= 0){
			//window.parent.Boxy.alert("<msg:message code='info.prompt.auth'/>", null, {title: "<msg:message code='info.prompt'/>"});
		}else{
			for(var i=0; i<sval.length; i++){
				if (i ==0){
					auth = sval[i].id;
				}
				else{
					auth += "," + sval[i].id;
				}
			}
			//$("#auth").val(auth);
			//$("form").submit();
		}
		$("#auth").val(auth);
		$("form").submit();
	});
});
</script>
</head>
<body class="skinMain">
<form:form method="post" action="saveauth" commandName="role">
<input type="hidden" name="turnCode" value="${cooperate_role.code}" />
<input type="hidden" name="coverParam" value="1" />
<input type="hidden" name="id" value="${role.id}"/>
<input type="hidden" name="c" value="${role.c}"/>
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="auth" id="auth"/>
<input type="hidden" name="operate" value="auth"/>
<input type="hidden" name="name" value="${role.name}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="role.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%">${role.name}</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.info"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%">${orgName}</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="role.select.auth"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%">
						<div>
							<ul id="treeDemo" class="ztree" style="margin-top:0px;width:90%;"></ul>
						</div>
					</td>
				</tr>	
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.confirm"/>" id="btnok"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="window.location.href='${base}${cooperate_role.controller}?c=${cooperate_role.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
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
