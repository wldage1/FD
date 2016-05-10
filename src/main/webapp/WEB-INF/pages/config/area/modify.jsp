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
				url:'${base}/config/area/valid.json'
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					//提交表单
					$("form#area").submit();
				}
			}
		};
	$("form #btnok").valid(options);
});
</script>
</head>
<body class="skinMain">
<form:form method="post" action="save" commandName="area">
<input type="hidden" name="id" value="${area.id}"/>
<input type="hidden" name="c" value="${area.c}"/>
<input type="hidden" name="oldParentId" value="${area.parentId}" />
<input type="hidden" name="prompt" value="name" />
<input name="parentId" type="hidden" id="parentId" value="${area.parentId}"/>
<input type="hidden" name="operate" value="2" />
<input type="hidden" name="turnCode" value="${config_area.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="area.parent"/><msg:message code="system.common.sign.colon"/>
					</td>
				 	<td width="85%" class="skinLayout_lable">
						<c:if test="${area.parentName=='dingjidiqu'}">
						<input  name="parentName" type="hidden" maxlength="20"  value="<msg:message code="area.tree.root"/>"  readonly  id="parentName" style="width: 200;" /> 
						<input  type="text" maxlength="20" value="<msg:message code="area.tree.root"/>"   disabled="true"  readonly id="parentName" style="width:200;" /> 
						</c:if>
						<c:if test="${area.parentName!='dingjidiqu'}">
						<input name="parentName" type="hidden" maxlength="20"  value="${area.parentName}" readonly id="parentName" style="width:200;" /> 
						<input  type="text" maxlength="20"  value="${area.parentName}"  disabled="true" readonly id="parentName" style="width:200;" /> 
						</c:if>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="area.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="name" maxlength="20" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="area.code"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="code" maxlength="6" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="area.pinyin"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="pinyin" maxlength="20" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.confirm"/>" id="btnok"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="window.location.href='${base}${config_area.controller}?c=${config_area.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form:form>
</body>
</html>
