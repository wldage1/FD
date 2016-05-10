<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
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
					//var auth = $("input[name=authorization]:checked");
					//if(auth.length == 0){
					//	top.layer.msg('<msg:message code="provideruser.inputauthorization"/>', 1);
					//	return false;
					//}
					$("form#providerUser").submit();
				}
			}
		};
	$("form #btnok").valid(options);
});

//当用户角色为超级管理员时，别的角色不可选
function checkCheckBox(){
	var auth = $("input:checkbox[name=authorization]");
	if(auth[0].checked){
		auth.attr("disabled","disabled");
		auth[0].disabled = false;
	}
	else
		auth.removeAttr("disabled");
}
function imposeMaxLength(Object, MaxLen)
{
  return (Object.value.length <MaxLen);
}
</script>

<style type="text/css">

input[type="radio"] {
	width  : 14px;
	height : 14px;
	vertical-align:text-bottom; margin-bottom:2px; margin-bottom:-2px\9;
}
input[type="checkbox"] {
	width  : 14px;
	height : 14px;
	vertical-align:text-bottom; margin-bottom:2px; margin-bottom:-2px\9;
}
</style>
</head>
<body class="skinMain">
<form:form  method="post" action="saveuser" commandName="providerUser" >
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			
			<input type="hidden" name="turnCode" value="${cooperate_provider_provideruser.code}"/>
			<input type="hidden" name="c" value="${providerUser.c}"/>
			<input type="hidden" name="coverParam" value="1"/>
			<input type="hidden" name="prompt" value="name" />
			<input type="hidden" name="providersID" value="${providerUser.providersID}"/>
			<input type="hidden" name="operate" value="1" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.account"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<form:input path="account" cssClass="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.password"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<form:password path="password" maxlength="18" cssClass="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="providerUser.confirmPwd"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<form:password path="confirmPwd" maxlength="18" cssClass="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
						</td> 
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.authorization"/><msg:message code="system.common.sign.colon"/></td>
						<td width="85%" class="skinLayout_lable">
							<form:checkbox path="authorization" value="1" cssClass="valid-control" onChange="checkCheckBox()"/><msg:message code="provideruser.administrator"/>&nbsp;&nbsp;
							<form:checkbox path="authorization" value="2" cssClass="valid-control"/><msg:message code="provideruser.usermanage"/>&nbsp;&nbsp;
							<form:checkbox path="authorization" value="3" cssClass="valid-control"/><msg:message code="provideruser.productmanage"/>
							<form:checkbox path="authorization" value="4" cssClass="valid-control"/><msg:message code="provideruser.messagemanage"/>
						<font color="red">&nbsp;*&nbsp;</font>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.name"/><msg:message code="system.common.sign.colon"/></td>
						<td width="85%" class="skinLayout_lable">
							<form:input path="name" cssClass="valid-control"/>
							<font color="red">&nbsp;*&nbsp;</font>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.gender"/><msg:message code="system.common.sign.colon"/></td>
						<td width="85%" class="skinLayout_lable">
								<form:select path="gender">
									<form:options items="${providerUserGender}" />
								</form:select>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.mobilePhone"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<form:input path="mobilePhone" maxlength="15" />
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.description"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<form:textarea path="description" maxlength="200" rows="3" cols="28" onkeypress="return imposeMaxLength(this, 100)" cssStyle="width: 250px; height: 79px;"/><font color="red">&nbsp;&nbsp;<form:errors path="description" /></font>
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
			<input type="button" class="btn2" name="btnok" 
			value="<msg:message code="button.save"/>" id="btnok"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
