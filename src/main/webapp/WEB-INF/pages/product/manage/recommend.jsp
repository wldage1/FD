<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title></title>
<link rel="stylesheet" type="text/css" href="${base}/common/skin/css/productTab.css" />

<script type="text/javascript">
function formSubmit(){
	if($("#startTime").val()==""){
		top.layer.msg("<msg:message code='product.position.startTime.empty'/>");
		return false;
	}
	if($("#finishTime").val()==""){
		top.layer.msg("<msg:message code='product.position.finishTime.empty'/>");
		return false;
	}
	return true;
	
}
</script>

</head>
<body class="skinMain">
<form:form method="post" action="saveProductSeckill" id="productSeckill"  onsubmit="return formSubmit();">
	<input type="hidden" name="c" value="${product.c}"/>
	<input type="hidden" name="productId" value="${product.id}"/>
	<input type="hidden" name="id" value="${productSecKill.id}"/>
	<input type="hidden" name="prompt" value="productName"/>
	<input type="hidden" name="turnCode" value="${product_manage.code}"/>
	<input type="hidden" name="productName" value="${product.name}"/>
	<input type="hidden" name="coverParam" value="true" />

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
					<tr>
						<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.name"/><msg:message code="system.common.sign.colon"/></td>
						<td width="85%" class="skinLayout_lable">
							${product.name}
						</td>
					</tr>
					<!--<tr>
						<td width="15%" align="right" class="skinLayout_info"><msg:message code="productSeckill.sortNumber"/><msg:message code="system.common.sign.colon"/></td>
						<td width="85%" class="skinLayout_lable">
							<input name="sortNumber" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,\'\')" size="8" maxlength="20" />
						</td>
					</tr>-->
					<tr>
						<td width="15%" align="right" class="skinLayout_info"><msg:message code="productSeckill.startTime"/><msg:message code="system.common.sign.colon"/></td>
						<td width="85%" class="skinLayout_lable">
						    <input name="startTime" id="startTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'finishTime\')}'});" value="${productSecKill.startTime}" class="Wdate" readonly="readonly" /> 
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info"><msg:message code="productSeckill.finishTime"/><msg:message code="system.common.sign.colon"/></td>
						<td width="85%" class="skinLayout_lable">
							<input name="finishTime" id="finishTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'});" value="${productSecKill.finishTime}" class="Wdate" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info"><msg:message code="productSeckill.status"/><msg:message code="system.common.sign.colon"/></td>
						<td width="85%" class="skinLayout_lable">
							<input type="radio" name="status" value="1"  checked="checked" /><msg:message code="productSeckill.status.start"/>
							&nbsp;&nbsp;
							<input type="radio" name="status" value="0"  <c:if test="${productSecKill.status == 0}"> checked="checked"</c:if> /><msg:message code="productSeckill.status.stop"/>
						</td>
					</tr>
				</table>
		</td>
	</tr>
</table>
<div class="toolbar">
	<div class="left"></div>
	<div class="center">
		<input type="submit" class="btn2" id="btn_save" value="<msg:message code="button.save"/>" />
		<input type="button" class="btn2" onclick="history.go(-1);" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
	</div>
	<div class="right"></div>
</div>
</form:form>
</body>
</html>
