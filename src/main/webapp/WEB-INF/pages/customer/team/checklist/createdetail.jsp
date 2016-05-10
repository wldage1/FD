<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body class="content-bg">
<form:form method="post" action="" commandName="createTeam">
<input type="hidden" name="turnCode" value="${customer_team_checklist.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<table width="99%" align="center" border="0" cellpadding="0" cellspacing="0" >
	<tr>
		<td>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
						<tr>
							<td width="100%">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
											<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.name"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${createTeam.name}
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.shortName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${createTeam.shortName}
										</td>
									</tr>
									
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicationMemberName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${createTeam.applicationMemberName}
											
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicationTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${createTeam.applicationTime}
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicantFeedback"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%"   valign="middle" class="skinLayout_lable">
											<form:textarea path="applicantFeedback" readonly="true" rows="3" cols="25" cssStyle="border:0;background:transparent;overflow:auto;width: 250px; height: 85px;"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											
										</td>
										<td width="35%" class="skinLayout_lable">
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="tableMargin"></td>
						</tr>
						<tr>
							<td></td>
						</tr>
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
	<tr>
		<td></td>
	</tr>
</table>
</form:form>
</body>
</html>


