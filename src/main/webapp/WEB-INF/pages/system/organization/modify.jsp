<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
$(document).ready(function() {
	//$.fn.zTree.init($("#treeDemo"), setting);
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
<form:form method="post" action="save" commandName="organization">
<input type="hidden" name="turnCode" value="${system_organization.code}" />
<input type="hidden" name="coverParam" value="1" />
<input type="hidden" name="id" value="${organization.id}"/>
<input type="hidden" name="c" value="${organization.c}"/>
<input type="hidden" name="oldParentId" value="${organization.parentId}" />
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="operate" value="2" />
<input name="parentId" type="hidden" id="parentId" value="${organization.parentId}"/>
<input name="parentName" type="hidden" value="${organization.parentName}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.parent"/><msg:message code="system.common.sign.colon"/>
					</td>
					 <td width="85%" class="skinLayout_lable">
						<input type="text" maxlength="20" value="${organization.parentName}" disabled="true" id="parentName" style="width: 200;" /> 
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.code"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="code" value="${organization.code}" maxlength="10" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info"><msg:message code="organization.name"/><msg:message code="system.common.sign.colon"/></td>
					<td width="85%" class="skinLayout_lable">
					<form:input path="name" value="${organization.name}" maxlength="50" class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.phone"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="phone" value="${organization.phone}" maxlength="15" class="valid-control"/>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.fax"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="fax" maxlength="15" value="${organization.fax}" class="valid-control"/>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.postcode"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="postCode" value="${organization.postCode}" maxlength="10" class="valid-control"/>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.address"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="address" value="${organization.address}" maxlength="250" class="valid-control"/>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>																														
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="organization.description"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%" class="skinLayout_lable">
						<form:input path="description" value="${organization.description}" maxlength="250" class="valid-control"/>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>	
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.confirm"/>" id="btnok"/>
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
