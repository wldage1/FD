<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
	
</script>
</head>
<body class="skinMain">
<form:form method="post" id="order" action="#" commandName="commission">
<input type="hidden" name="turnCode" value="${commission_adjust_detail.code}"/>
<input type="hidden" name="coverParam" value="1"/>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
					<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
							<tr>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="order.orderNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%">${commission.orderHistory.orderNumber}</td>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="order.orgID.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%">${commission.orgShortName}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.investAmount"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${commission.orderHistory.investAmount }</td>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.payAmount"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"  class=""  width="300px">${commission.orderHistory.payAmount }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="product.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="">${commission.productName}</td>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="org.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%">${commission.proShortName}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="member.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${commission.memberName }</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.teamID"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${commission.teamName }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.clientName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${commission.clientName}</td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.clientTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >
									<c:forEach items="${clientType}" var="item">
										<c:if test="${item.key == commission.clientType}">
											${item.value}
										</c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.iDCardTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >
									<c:forEach items="${iDCardTypeMap}" var="item">
										<c:if test="${item.key == commission.iDCardType}">
											${item.value}
										</c:if>
									</c:forEach>
								</td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.iDCard"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${commission.orderHistory.iDCard}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.tradeType"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >
									<c:forEach items="${tradeType}" var="item">
										<c:if test="${item.key == commission.tradeType}">
											${item.value}
										</c:if>
									</c:forEach>
								</td>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.deadline"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${commission.orderHistory.deadline}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.profitRatio"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${commission.orderHistory.profitRatio}</td>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.commissionPercent"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"  class="" width="100px">${commission.commissionProportion}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.expectCommission"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"  class=""  width="300px">${commission.commission}</td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.pushTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${commission.orderHistory.pushTime}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.salesTax"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="">${commission.salesTax}</td>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.constructionTax"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${commission.constructionTax}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  width="100px"><msg:message code="order.educationalSurtax"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${commission.educationalSurtax}</td>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.localEducationalCost"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${commission.localEducationalCost}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.personalIncomeTax"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${commission.personalIncomeTax}</td>
								<td align="right" class="skinLayout_info"  width="100px"><msg:message code="order.orderTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${commission.orderHistory.orderTime}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.commissionPayTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${commission.orderHistory.commissionPayTime}</td>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.payMoneyStopTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${commission.orderHistory.payMoneyStopTime}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.share"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"  class="" width="100px">${commission.orderHistory.share }</td>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.payTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class=""  width="300px">${commission.orderHistory.payTime }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.contractNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${commission.orderHistory.contractNumber }</td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.pushStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="">
									<c:forEach items="${pushStatus}" var="item">
										<c:if test="${item.key == commission.orderHistory.pushStatus}">
											${item.value}
										</c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.payProgress"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">
									<c:forEach items="${payProgress}" var="item">
										<c:if test="${item.key == commission.orderHistory.payProgress}">
											${item.value}
										</c:if>
									</c:forEach>
								</td>
								<td align="right" class="skinLayout_info"  width="100px"><msg:message code="order.tradeStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">
									<c:forEach items="${tradeStatus}" var="item">
										<c:if test="${item.key == commission.orderHistory.tradeStatus}">
											${item.value}
										</c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  width="100px"><msg:message code="order.payStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">
									<c:forEach items="${payStatusMap}" var="item">
										<c:if test="${item.key == commission.payStatus}">
											${item.value}
										</c:if>
									</c:forEach>
								</td>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.documentStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">
									<c:forEach items="${documentStatus}" var="item">
										<c:if test="${item.key == commission.orderHistory.documentStatus}">
											${item.value}
										</c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.pushShare"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${commission.orderHistory.pushShare}</td>
								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.remark"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" colspan="3" width="100px">${commission.orderHistory.remark}</td>
							</tr>
							<c:if test="${order.productType==2}">
								<tr>
									<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.netValue"/><msg:message code="system.common.sign.colon"/></td>
									<td align="left" class="" colspan="3">${commission.orderHistory.netValue}</td>
								</tr>
							</c:if>
							<tr>
								
							</tr>
							<tr ><td height="30">&nbsp;</td></tr>
						</table>
						<div class="toolbar">
							<div class="left"></div>
								<div class="center">
									<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
								</div>
							<div class="right"></div>
						</div>
					</td>
				</tr>
			</table>
	</form:form>
</html>