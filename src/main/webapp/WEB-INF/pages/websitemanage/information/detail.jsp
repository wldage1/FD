<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
	</head>
	<body class="skinMain">
		<table width="100%" border="0" cellpadding="1" cellspacing="1" class="skinMain">
			<tr>
				<td width="100%">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="skinLayout">
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="information.type"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								<c:forEach items="${informationType}" var="item">
									<c:if test="${item.id == information.type}">
										${item.name}
									</c:if>
								</c:forEach>
							</td>
							<td rowspan="5" width="15%" align="right" class="skinLayout_info"><msg:message code="information.titleImage"/><msg:message code="system.common.sign.colon"/></td>
							<td rowspan="5" width="35%" class="skinLayout_lable" height="92">
								&nbsp;&nbsp;<c:if test="${information.titleImage!=null}"><img src="${ftpHttpUrl}${information.titleImage}" height="120"></c:if>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="information.title"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${information.title}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="information.shorterTitle"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${information.shorterTitle}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="information.shortestTitle"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${information.shortestTitle}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="information.keyword"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${information.keyword}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="information.author"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${information.author}
							</td>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="information.source"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${information.source}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="information.summary"/><msg:message code="system.common.sign.colon"/></td>
							<td colspan="3" width="85%" class="skinLayout_lable">
								<textarea readonly="readonly" style="background:url(../images/main/bg.jpg) repeat;width:500px;height:160px;border:1px solid #A6C9E2;font-size:14px;border:none;">${information.summary}</textarea>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="information.content"/><msg:message code="system.common.sign.colon"/></td>
							<td colspan="3" width="85%" class="skinLayout_lable">
								${information.content}
							</td>
						</tr>
					</table>
					<div class="toolbar">
						<div class="left"></div>
							<div class="center">
								<input type="button" class="btn2" onclick="window.location.href='Javascript:history.go(-1);void(0);'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
							</div>
						<div class="right"></div>
					</div>
				</td>
			</tr>
		</table>
	</body>
</html>