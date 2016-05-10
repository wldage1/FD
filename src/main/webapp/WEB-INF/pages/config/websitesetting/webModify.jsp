<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script	type="text/javascript">
	function submitForm(){
		$("#value").val($("textarea").val());
		$("form#websiteSetting").submit();
	}
</script>
</head>
<body class="skinMain">
<form:form method="post" action="save" commandName="websiteSetting" name="f">
<form:input type="hidden" path="c" value="${c}" />
<form:input type="hidden" path="value" />
<input type="hidden" id="id" name="id" value="${website.id}" />
<input type="hidden" name="turnCode" value="${config_websitesetting.code}" />
<input type="hidden" name="coverParam" value="1" />
<form:input type="hidden" path="prompt" value="name" />
<form:input type="hidden" path="name" value="${website.name}" />
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code='websiteSetting.name'/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						${website.name}
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code='websiteSetting.value'/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<textarea  style="height:300px;width:300px;overflow-x:hidden;overflow-y:hidden" >${website.value }</textarea>
					</td>
				</tr>				
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.confirm"/>" id="btnok" onclick="submitForm()"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="window.location.href='${base}${config_websitesetting.controller}?c=${config_websitesetting.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
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
