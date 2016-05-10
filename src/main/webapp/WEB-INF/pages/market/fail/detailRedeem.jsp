<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
</head>
<body class="skinMain">
<form:form method="post" id="redeemOrder" action="#" commandName="redeemOrder">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
				<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
					<tr>
						<td align="right" class="skinLayout_info" width="20%"><msg:message code="order.orderNumber"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable" width="30%">${redeemOrder.orderNumber}</td>
						<td align="right" class="skinLayout_info" width="20%"><msg:message code="order.contractNumber"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable" width="30%">${redeemOrder.contractNumber}</td>
					</tr>
					<tr>
						<td align="right" class="skinLayout_info"><msg:message code="order.orgID.name"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.organization.name}</td>
						<td align="right" class="skinLayout_info"><msg:message code="product.shortName"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.product.shortName}</td>
					</tr>
					<tr>
						<td align="right" class="skinLayout_info"><msg:message code="order.teamID"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.teamOrOrgName}</td>
						<td align="right" class="skinLayout_info"><msg:message code="member.name"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.member.name}（${redeemOrder.member.nickName}）</td>
					</tr>
					<tr>
						<td align="right" class="skinLayout_info"><msg:message code="order.clientTypeName"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">
							<c:forEach items="${clientType}" var="item">
								<c:if test="${item.key == redeemOrder.clientType}">
									${item.value}
								</c:if>
							</c:forEach>
						</td>
						<td align="right" class="skinLayout_info"><msg:message code="order.clientName"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.clientName}</td>
					</tr>
					<tr>
						<td align="right" class="skinLayout_info"><msg:message code="order.iDCardTypeName"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">
							<c:forEach items="${iDCardType}" var="item">
								<c:if test="${item.key == redeemOrder.iDCardType}">
									${item.value}
								</c:if>
							</c:forEach>
						</td>
						<td align="right" class="skinLayout_info"><msg:message code="order.iDCard"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.iDCard}</td>
					</tr>
					<tr>
						<td align="right" class="skinLayout_info"><msg:message code="redeemorder.shares"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.share}</td>
						<td align="right" class="skinLayout_info"><msg:message code="redeemorder.amount"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.amount}</td>
					</tr>
					<tr>
						<td align="right" class="skinLayout_info"><msg:message code="order.tradeStatus"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">
							<c:forEach items="${tradeStatus}" var="item">
								<c:if test="${item.key == redeemOrder.tradeStatus}">
									${item.value}
								</c:if>
							</c:forEach>
						</td>
						<td align="right" class="skinLayout_info"><msg:message code="order.documentStatus"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">
							<c:forEach items="${documentStatus}" var="item">
								<c:if test="${item.key == redeemOrder.documentStatus}">
									${item.value}
								</c:if>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<td align="right" class="skinLayout_info"><msg:message code="order.remark"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.remark}</td>
						<td align="right" class="skinLayout_info"><msg:message code="cancelorder.providername"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.providerName}</td>
					</tr>
					<tr>
						<td align="right" class="skinLayout_info"><msg:message code="redeemorder.openTime"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.openTime}</td>
						<td align="right" class="skinLayout_info"><msg:message code="redeemorder.tradeTime"/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" class="skinLayout_lable">${redeemOrder.tradeTime}</td>
					</tr>
					<c:if test="${redeemOrder.product.type==2}">
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.netValue"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="skinLayout_lable" colspan="3">${redeemOrder.netValue}</td>
						</tr>
					</c:if>
				</table>
				<div class="toolbar">
					<div class="left"></div>
						<div class="center">
							<input type="button" class="btn2" onclick="location.href='${base}${market_fail.controller}?c=${market_fail.code}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
						</div>
					<div class="right"></div>
				</div>
			</td>
		</tr>
	</table>
</form:form>
</html>