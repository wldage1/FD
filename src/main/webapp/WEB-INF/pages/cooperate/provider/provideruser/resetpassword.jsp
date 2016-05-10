<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
$(document).ready(function() {
		//表单提交s
	var options = {
			event:'focus',
			ajax:{
				url:'${base}/cooperate/provider/provideruser/valid.json'
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					$("form#providerUser").submit();
				}
			}
		};
	$("form #btnok").valid(options);
});
	
</script>
</head>
<body class="skinMain">
<form:form method="post" action="updatePassword" commandName="providerUser">
<input type="hidden" name="id" id ="id" value="${providerUser.id}"/>
<input type="hidden" name="c" value="${providerUser.c}"/>
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="turnCode" value="${cooperate_provider_provideruser.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<input type="hidden" name="name" value="${providerUser.name}" />
<input type="hidden" name="operate" value="3" />
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="providerUser.newPassword"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%" class="skinLayout_lable">
						<form:password path="newPwd" maxlength="18" class="valid-control" />
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>	
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="providerUser.confirmPwd"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%" class="skinLayout_lable">
						<form:password path="confirmPwd" maxlength="18" class="valid-control" />
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>	
			</table>
		</td>
	</tr>
	<tr>
		<td class="tableMargin"></td>
	</tr>
	<div class="toolbar">
				<div class="left"></div>
					<div class="center">
					<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
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
