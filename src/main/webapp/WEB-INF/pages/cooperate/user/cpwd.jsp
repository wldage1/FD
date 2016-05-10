<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
$(document).ready(function() {
   var options = {
		event:'focus',	
		ajax:{
			url:'${base}/cooperate/user/valid.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: $("form#user").serialize(), 
			async:false,
			cache:false
		},
		validStatus:function(status){
			//status 返回结果为true 代表验证全部通过
			if (status){
				$("form#user").submit();
			}
		}
	};
	$("form #btnok").valid(options);	
});
</script>
</head>
<body class="skinMain">
<form:form method="post" action="cmpwdSave" commandName="user">
<input type="hidden" name="turnCode" value="${cooperate_user.code}" />
<input type="hidden" name="coverParam" value="1" />
<input type="hidden" name="id" value="${user.id}"/>
<input type="hidden" name="c" value="${user.c}"/>
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="name" value="${user.name}" />
<input type="hidden" name="operate" value="3" />
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.newpwd"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%" class="skinLayout_lable">
						<form:password path="newPassword" maxlength="25" class="valid-control" />
						<font color="red">&nbsp;*&nbsp;<form:errors path="newPassword" /></font>
					</td>
				</tr>	
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.confirmpwd"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%" class="skinLayout_lable">
						<form:password path="confirmPwd" maxlength="25" class="valid-control" />
						<font color="red">&nbsp;*&nbsp;<form:errors path="confirmPwd" /></font>
					</td>
				</tr>	
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="location.href='${base}${backLink}'" name="btnback" onclick="window.location.href=''" value="<msg:message code="button.back"/>" id="btnback"/>
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
