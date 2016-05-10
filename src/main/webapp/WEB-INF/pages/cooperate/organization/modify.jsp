<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<title></title>
<script type="text/javascript">
$(document).ready(function() {
	//$.fn.zTree.init($("#treeDemo"), setting);
	var options = {
			event:'focus',
			ajax:{
				url:'${base}/cooperate/organization/valid.json'
			},
			validStatus:function(status){
				if (status){
					$("form#commission").submit();
				}
			}
		};
	$("form #btnok").valid(options);
	$("form #btnok").click(function(){
		tab(1);
	});
	// 富文本
	var editor = "";
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="description"]',{
			uploadJson : '${base}/provider/product/uploadImage',
			fileManagerJson : '${base}/provider/product/uploadImage',
			allowFileManager: true,
			afterBlur: function(){this.sync();}
		});
	});	
});
</script>
</head>
<body class="skinMain">
<form:form method="post" action="save" commandName="commission">
<input type="hidden" name="turnCode" value="${cooperate_organization.code}" />
<input type="hidden" name="coverParam" value="1" />
<input type="hidden" name="id" value="${commission.id}"/>
<input type="hidden" name="c" value="${commission.c}"/>
<input type="hidden" name="oldParentId" value="${commission.parentId}" />
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="operate" value="2" />
<input name="parentId" type="hidden" id="parentId" value="${commission.parentId}"/>
<input name="parentName" type="hidden" value="${commission.parentName}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
		<div id="div_1" >
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.parent"/><msg:message code="system.common.sign.colon"/>
					</td>
					 <td width="85%" class="skinLayout_lable">
						<input type="text" maxlength="20" value="${commission.parentName}" disabled="true" id="parentName" style="width: 200;" /> 
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.code"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="code" value="${commission.code}" maxlength="10" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info"><msg:message code="commission.name"/><msg:message code="system.common.sign.colon"/></td>
					<td width="85%" class="skinLayout_lable">
					<form:input path="name" value="${commission.name}" maxlength="50" class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.shortName"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
					<form:input path="shortName" value="${commission.shortName}" maxlength="50" class="valid-control"/><font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.bankName"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
					<form:input path="bankName" value="${commission.bankName}" maxlength="50" class="valid-control"/><font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.accountHolder"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
					<form:input path="accountHolder" value="${commission.accountHolder}"  maxlength="50" class="valid-control"/><font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.account"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
					<form:input path="account" value="${commission.account}" maxlength="50" class="valid-control"/><font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.phone"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="phone" value="${commission.phone}" maxlength="15" class="valid-control"/>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.fax"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="fax" maxlength="15" value="${commission.fax}" class="valid-control"/>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.postcode"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="postCode" value="${commission.postCode}" maxlength="10" class="valid-control"/>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.address"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="address" value="${commission.address}" maxlength="250" class="valid-control"/>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>																														
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.description"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%" class="skinLayout_lable">
						<textarea id="description" name="description" rows="4" style="width: 90%">${commission.description}</textarea>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>	
			</table>
		</div>
		</td>
	</tr>
	<tr>
		<td class="tableMargin"></td>
	</tr>
	<tr>
		<td></td>
	</tr>
	<div class="toolbar">
		<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" class="btn2" onclick="window.location.href='${base}${cooperate_organization.controller}?c=${cooperate_organization.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
		</div>
		<div class="right"></div>
	</div>
</table>
</form:form>	
</body>
</html>
