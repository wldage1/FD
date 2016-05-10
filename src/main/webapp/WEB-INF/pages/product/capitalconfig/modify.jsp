<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
$(document).ready(function(){
	var options = {
			event:'focus',
			ajax:{
				url:'${base}/product/capitalconfig/valid.json'
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					//提交表单
					$("form#productCategory").submit();
				}
			}
		};
	$("form #btnok").valid(options);
})
</script>
</head>
<body class="skinMain">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
				<form:form method="post" action="save" commandName="productCategory">
					<input type="hidden" name="id" value="${productCategory.id}"/>
					<input type="hidden" name="c" value="${productCategory.c}"/>
					<input type="hidden" name="parentId" value="${productCategory.parentId}"/>
					<input type="hidden" name="prompt" value="name" />
					<input type="hidden" name="turnCode" value="${product_capitalconfig.code}" />
					<input type="hidden" name="coverParam" value="1" />
					<input type="hidden" name="operate" value="2" />
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="category.parent"/><msg:message code="system.common.sign.colon"/></td>
						 	<td width="85%" class="skinLayout_lable">
						 		<c:if test="${productCategory.parentId == 0}">
						 			<input name="parentName" value="<msg:message code='category.tree.root'/>" readonly="true" disabled="true" style="width: 200;" /> 
						 		</c:if>
						 		<c:if test="${productCategory.parentId != 0}">
									<input name="parentName" value="${productCategory.parentName}" readonly="true" disabled="true" style="width: 200;" /> 
						 		</c:if>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="category.name"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="name" value="${productCategory.name}" maxlength="20" class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="category.code"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="code" maxlength="20" value="${productCategory.code}" class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right">&nbsp;</td>
							<td width="85%" colspan="2" align="left">
								<input type="button" class="btn2" name="btnok" value="<msg:message code="button.confirm"/>" id="btnok"/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
							</td>
						</tr>
					</table>
				</form:form>
			</td>
		</tr>
	</table>
</body>
</html>
