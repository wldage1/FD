<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
function onChange(max){
	if($("#reply").val().length>max){
		$("#reply").val($("#reply").val().substring(0,max));
	}
}

function submmit(){
	$("#reply").val($("#anwser").val());
	$('form#question').submit();
}
</script>
<title></title>
</head>
<body class="skinMain">
<form:form method="post" action="update" commandName="question">
<input type="hidden" id="c" name="c" value="${c}" />
<form:input type="hidden" path="id" value="${question.id}" />
<form:input type="hidden" path="reply" />
<input type="hidden" name="turnCode" value="${product_question.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<input type="hidden" name="prompt" value="question" />
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr width="100%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout" >
			<tr >
				<td width="15%" align="right" class="skinLayout_info">
					<msg:message code='product.shortName' /><msg:message code="system.common.sign.colon"/>
				</td>
				<td>
					${question.productName}
				</td>
			</tr>
			<tr>
				<td  width="15%" align="right" class="skinLayout_info">
					<msg:message code='question.askTime' /><msg:message code="system.common.sign.colon"/>
				</td>
				<td>
					${question.askTime}
				</td>
			</tr>
			<tr>
				<td width="15%" align="right" class="skinLayout_info">
					<msg:message code="question.question"/><msg:message code="system.common.sign.colon"/>
				</td>
				<td width="85%" class="skinLayout_lable">
					<textarea style="height:150px;width:35%;border:0px;resize:none;" readonly="readonly">${question.question}</textarea>
				</td>
			</tr>
			<tr>
				<td width="15%" align="right" class="skinLayout_info">
					<msg:message code="question.reply"/><msg:message code="system.common.sign.colon"/>
				</td>
				<td width="85%" class="skinlayout_lable" >
					<textarea id="anwser" onkeydown="onChange(100);" onkeyup="onChange(100);" style="height:150px;width:35%;" >${question.reply}</textarea>
				</td>
			</tr>
			<tr>
				<td width="15%" align="right">&nbsp;</td>
				<td width="85%" colspan="2" align="left">
					<input type="button" class="btn2" name="btnok" value="保存" id="btnok" onclick="submmit()"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" class="btn2" onclick="window.location.href='${base}${product_question.controller}?c=${product_question.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
				</td>
			</tr>
		</table>
	</tr>
</table>
</form:form>
</body>
</html>