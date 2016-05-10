<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
</head>
<body class="skinMain">
	<form:form method="post" id="order" action="#" commandName="order">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%">
					<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
						<tr>
							<td align="right" class="skinLayout_info" width="20%"><msg:message code="order.orderNumber"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="" width="30%">${order.orderNumber}</td>
							<td align="right" class="skinLayout_info" width="20%"><msg:message code="order.orgID.name"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="" width="30%">${order.organization.name}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.investAmount"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.investAmount}</td>
							<td align="right" class="skinLayout_info"><msg:message code="order.payAmount"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  class="">${order.payAmount}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="product.shortName"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.product.shortName}</td>
							<td align="right" class="skinLayout_info"><msg:message code="product.org.name"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.provider.shortName}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="member.name"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.member.name}（${order.member.nickName}）</td>
							<td align="right" class="skinLayout_info"><msg:message code="order.teamID"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.team.name}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.clientName"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.clientName}</td>
							<td align="right" class="skinLayout_info"><msg:message code="order.clientTypeName"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">
								<c:forEach items="${clientType}" var="item">
									<c:if test="${item.key == order.clientType}">
										${item.value}
									</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.iDCardTypeName"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">
								<c:forEach items="${iDCardType}" var="item">
									<c:if test="${item.key == order.iDCardType}">
										${item.value}
									</c:if>
								</c:forEach>
							</td>
							<td align="right" class="skinLayout_info"><msg:message code="order.iDCard"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.iDCard}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.tradeType"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">
								<c:forEach items="${tradeType}" var="item">
									<c:if test="${item.key == order.tradeType}">
										${item.value}
									</c:if>
								</c:forEach>
							</td>
							<td align="right" class="skinLayout_info">产品期限
							<c:choose>
									<c:when test="${order.product.deadlineDataType == 2}">(日)</c:when>
									<c:otherwise>(月)</c:otherwise>
								</c:choose>
							<msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.deadline}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.profitRatio"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.profitRatio}</td>
							<td align="right" class="skinLayout_info"><msg:message code="order.orderTime"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.orderTime}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.commissionPayTime"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.commissionPayTime}</td>
							<td align="right" class="skinLayout_info"><msg:message code="order.contractNumber"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.contractNumber}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.share"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  class="">${order.share}</td>
							<td align="right" class="skinLayout_info"><msg:message code="order.payTime"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.payTime}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.payProgress"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">
								<c:forEach items="${payProgress}" var="item">
									<c:if test="${item.key == order.payProgress}">
										${item.value}
									</c:if>
								</c:forEach>
							</td>
							<td align="right" class="skinLayout_info"><msg:message code="order.tradeStatus"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">
								<c:forEach items="${tradeStatus}" var="item">
									<c:if test="${item.key == order.tradeStatus}">
										${item.value}
									</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.docStatus"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">
								<c:forEach items="${docStatus}" var="item">
									<c:if test="${item.key == order.docStatus}">
										${item.value}
									</c:if>
								</c:forEach>
							</td>
							<td align="right" class="skinLayout_info"><msg:message code="order.pushStatus"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="" colspan="3">
								<c:forEach items="${pushStatus}" var="item">
									<c:if test="${item.key == order.pushStatus}">
										${item.value}
									</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.pushShare"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.pushShare}</td>
							<td align="right" class="skinLayout_info"><msg:message code="order.pushTime"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="">${order.pushTime}</td>
						</tr>
						<c:if test="${order.product.type==2}">
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.netValue"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" colspan="3">${order.netValue}</td>
							</tr>
						</c:if>
						<tr>
							<td align="right" class="skinLayout_info"><msg:message code="order.remark"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="" colspan="3">${order.remark}</td>
						</tr>
						<%-- <tr>
							<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.payMoneyStopTime"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="" width="100px">${order.payMoneyStopTime}</td>
							<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.assignedShare"/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" class="" width="100px">${order.assignedShare}</td>
						</tr> --%>
					</table>
					<div class="toolbar">
						<div class="left"></div>
							<div class="center">
								<input type="button" class="btn2" onclick="location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
							</div>
						<div class="right"></div>
					</div>
				</td>
			</tr>
		</table>
	</form:form>
</html>