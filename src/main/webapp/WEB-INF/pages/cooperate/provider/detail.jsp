<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
	</head>
	<body class="skinMain">
	<form:form action="save" method="post" commandName="provider">
		<table width="100%" border="0" cellpadding="1" cellspacing="1" class="skinMain">
			<tr>
				<td width="100%">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="skinLayout">
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="provider.name"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${provider.name}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="provider.short.name"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${provider.shortName}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
								<msg:message code="provider.createDate"/><msg:message code="system.common.sign.colon"/>
							</td>
							<td width="35%" class="skinLayout_lable">
								${provider.createDate}
							</td>
							<td width="15%" align="right" class="skinLayout_info">
								<msg:message code="provider.companyType"/><msg:message code="system.common.sign.colon"/>
							</td>
							<td width="35%" class="skinLayout_lable">
									<c:forEach items="${companyType}" var="item">
										<c:if test="${provider.companyType == item.key}">
											${item.value}
										</c:if>
									</c:forEach>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
								<msg:message code="provider.substantialShareholder"/><msg:message code="system.common.sign.colon"/>
							</td>
							<td width="35%" class="skinLayout_lable">
								<textarea style="height: 100px;width:400px;" readonly="readonly">${provider.substantialShareholder}</textarea>
							</td>
							<td width="15%" align="right" class="skinLayout_info">
								<msg:message code="provider.registeredCapital"/><msg:message code="system.common.sign.colon"/>
							</td>
							<td width="35%" class="skinLayout_lable">
								<textarea style="height: 100px;width:400px;" readonly="readonly">${provider.registeredCapital}</textarea>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
								<msg:message code="provider.latestAssetSize"/><msg:message code="system.common.sign.colon"/>
							</td>
							<td width="35%" class="skinLayout_lable">
								<textarea style="height: 100px;width:400px;" readonly="readonly">${provider.latestAssetSize}</textarea>
							</td>
							<td width="15%" align="right" class="skinLayout_info">
								<msg:message code="provider.companiesRegistryAddress"/><msg:message code="system.common.sign.colon"/>
							</td>
							<td width="35%" class="skinLayout_lable">
								<textarea style="height: 100px;width:400px;" readonly="readonly">${provider.companiesRegistryAddress}</textarea>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info" height="75"><msg:message code="provider.logo"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" colspan="3" class="skinLayout_lable">
								<img src="${ftpHttpUrl}${provider.logo}" height="60" width="100">
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
								<msg:message code="provider.awards"/><msg:message code="system.common.sign.colon"/>
							</td>
							<td width="35%" colspan="3" class="skinLayout_lable">
								${provider.awards}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
								<msg:message code="provider.discription"/><msg:message code="system.common.sign.colon"/>
							</td>
							<td width="85%" colspan="3" class="skinLayout_lable">
								${provider.discription}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right">&nbsp;</td>
							<td width="85%" colspan="3" align="left">
								<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</form:form>
	</body>
</html>