<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
	</head>
	<body class="skinMain">
		<input type="hidden" name="turnCode" value="${customer_member_memberclient.code}"/>
		<input type="hidden" name="coverParam" value="1"/>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" class="skinMain">
			<tr>
				<td width="100%">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="skinLayout">
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.name"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.name}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.memberName"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.memberName}（${client.memberNickName}）
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.type"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable" id="type">
								<c:forEach items="${clientType}" var="item">
									<c:if test="${client.type == item.key}">
										${item.value}
									</c:if>
								</c:forEach>
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.email"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.email}
							</td>
						</tr>
						<c:if test="${client.type == 1}">
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.sex"/><msg:message code="system.common.sign.colon"/></td>
								<td width="35%" class="skinLayout_lable" id="sex">
									<c:forEach items="${memberGender}" var="item">
										<c:if test="${client.sex == item.key}">
											${item.value}
										</c:if>
									</c:forEach>
								</td>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.birthday"/><msg:message code="system.common.sign.colon"/></td>
								<td width="35%" class="skinLayout_lable">
									${client.formatBirthday}
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.married"/><msg:message code="system.common.sign.colon"/></td>
								<td width="35%" class="skinLayout_lable" id="married">
									<c:forEach items="${clientMarried}" var="item">
										<c:if test="${client.married == item.key}">
											${item.value}
										</c:if>
									</c:forEach>
								</td>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.idCardType"/><msg:message code="system.common.sign.colon"/></td>
								<td width="35%" class="skinLayout_lable">
									<c:forEach items="${memberIDCardType}" var="item">
										<c:if test="${client.idCardType == item.key}">
											${item.value}
										</c:if>
									</c:forEach>
								</td>
							</tr>
						</c:if>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.idCard"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.idCard}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.idCardValid"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.formatIdCardValid}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.mobilePhone"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.mobilePhone}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.phone"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.phone}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.contactAddress"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.contactAddress}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.postCode"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.postCode}
							</td>
						</tr>
						
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.ventureTrend"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.ventureTrend}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.intentionProduct"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.intentionProduct}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.investmentSituation"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.investmentSituation}
							</td>
						</tr>
						<%-- <tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.belief"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.belief}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.nationality"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.nationality}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.province"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.province}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.city"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.city}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.hobby"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.hobby}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.sourceIndustry"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.sourceIndustry}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.occupation"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.occupation}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.education"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.education}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.memberPosition"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.memberPosition}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.workStatus"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.workStatus}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.company"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.company}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.companyIndustry"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.companyIndustry}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.income"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.income}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.incomeRangeID"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.incomeRangeID}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="client.homeAddress"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${client.homeAddress}
							</td>
						</tr> --%>
					</table>
				</td>
			</tr>
			<div class="toolbar">
				<div class="left"></div>
				<div class="center">
				<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
				</div>
				<div class="right"></div>
			</div>
		</table>
	</body>
</html>