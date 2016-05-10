<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<title></title>

</head>
<body class="skinMain">
<form:form  method="post" action="save" commandName="contract" >
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<input type="hidden" name="turnCode" value="${commission_contract.code}"/>
			<input type="hidden" name="c" value="${contract.c}"/>
			<input type="hidden" name="coverParam" value="1"/>
			<input type="hidden" name="prompt" value="title" />
			<input type="hidden" name="id" value="${contract.id}"/>
			<input type="hidden" name="operate" value="1" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="contract.title"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							${contract.title}
						</td>
					</tr>
					<tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="contract.content"/><msg:message code="system.common.sign.colon"/>
						</td>
						<td height="40%" width="70%" style="padding-left:3px;padding-right:6px;" >
							<div style="float:left;height:400px;width:700px;OVERFLOW-Y: auto; OVERFLOW-X:hidden;">
							${contract.content}
						</td>
					</tr>
					
				</table>
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		 <div class="toolbar">
		<div class="left"></div>
			<div class="center">
			<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
			</div>
			<div class="right"></div>
		</div>
		<tr>
			<td></td>
		</tr>
	</table>
	</form:form>
</body>
</html>
