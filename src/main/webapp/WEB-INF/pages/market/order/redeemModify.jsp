<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
$(document).ready(function(){
	if(window.ActiveXObject){
		$("#netValue").attr("onpropertychange","Amount()");
		$("#share").attr("onpropertychange","Amount()");
	}else{
		$("#netValue").attr("oninput","Amount()");
		$("#share").attr("oninput","Amount()");
	}
})

function Amount(){
	var share = $("#share").val();
	var netValue = $("#netValue").val();
	if(share!=null && netValue!=null){
		$("#amount").val(share * netValue);
	}
}
</script>
</head>
<body class="skinMain">
<form:form method="post" id="redeemOrder" action="saveredeem" commandName="redeemOrder">
<form:hidden path="id" value="${redeemOrder.id }"/>
<form:hidden path="c" />
<input type="hidden" name="turnCode" value="${market_order.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<input type="hidden" name="prompt" value="orderNumber" />
<input type="hidden" name="orderNumber" id="orderNumber" value="${redeemOrder.orderNumber }" />
<input type="hidden" name="holdingProductID" id="holdingProductID" value="${redeemOrder.holdingProductID }">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
					<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
							<tr>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="order.orderNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%">${redeemOrder.orderNumber}</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.contractNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="">${redeemOrder.contractNumber}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.product.shortName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="">${redeemOrder.product.shortName}</td>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="product.org.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%">${redeemOrder.providerName }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.member.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${redeemOrder.memberName}(${redeemOrder.member.nickName})</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.teamID"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${redeemOrder.team.name }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.clientTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:select path="clientType" cssStyle="width:150px;"><c:forEach items="${clientTypeMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.iDCardTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:select path="iDCardType" cssStyle="width:150px;"><c:forEach items="${iDCardTypeMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.clientName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${redeemOrder.clientName}</td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.iDCard"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:input path="iDCard"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.netValue"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class=""><form:input path="netValue" /></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.documentStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:select path="documentStatus" cssStyle="width:150px;"><c:forEach items="${documentStatusMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="redeemorder.shares"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class=""><form:input path="share" /></td>
								<td align="right" class="skinLayout_info" ><msg:message code="redeemorder.tradeTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:input path="tradeTime" maxlength="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate" readonly="readonly"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  ><msg:message code="redeemorder.amount"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:input path="amount" readonly="true"/></td>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.tradeStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:select path="tradeStatus" cssStyle="width:150px;"><c:forEach items="${tradeStatusMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="redeemorder.openTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%" colspan="3"><span id="code" name="code"><form:input path="openTime" maxlength="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate" readonly="readonly"/></span></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.remark"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" colspan="3"><form:textarea path="remark" style="height:100px;width:200px;overflow-x:hidden;overflow-y:hidden"/></td>
							</tr>
						</table>
						<div class="toolbar">
							<div class="left"></div>
								<div class="center">
									<input type="submit" class="btn2" onclick="" name="btnok" value="<msg:message code="button.save" />" id="btnok" />
									<input type="button" class="btn2" onclick="window.location.href='${base}${market_order.controller}?c=${market_order.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
								</div>
							<div class="right"></div>
						</div>
					</td>
				</tr>
			</table>
	</form:form>
</html>