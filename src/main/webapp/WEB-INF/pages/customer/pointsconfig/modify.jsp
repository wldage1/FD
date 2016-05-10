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
				url:'${base}/customer/pointsconfig/valid.json'
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					//提交表单
					$("form#pointsconfig").submit();
				}
			}
		};
	$("form #btn_save").valid(options);
})


</script>
</head>
<body class="skinMain">
<form:form method="post" action="save" commandName="pointsconfig">
<form:hidden path="c"/>
<input type="hidden" name="prompt" value="code" />
<input type="hidden" name="id" value="${pointsconfig.id}"/>
<input type="hidden" name="coverParam" value="1" />
<input type="hidden" name="operate" value="2" />
<input type="hidden" name="turnCode" value="${customer_pointsconfig.code}" />
<input type="hidden" name="head" value="" id="sqlid" />
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="config.code"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="code" maxlength="30" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="config.points"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="points" maxlength="20" class="valid-control" />
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>	
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="config.description"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:textarea path="description" cssStyle="width:250px;height:160px;border:1px solid #A6C9E2;font-size:14px;"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right">&nbsp;${errors}</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btn_save" value="<msg:message code="button.save"/>" id="btn_save" />
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
</body>
</html>