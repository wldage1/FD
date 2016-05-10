<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript">
	function changeBig(ImageUrl){
		var i = top.$.layer({
			type: 1,
			title: false,
			closeBtn: [0,true],
			shadeClose: true,
			border : [5, 0.5, '#666', true],
			offset: ['20px','50%'],
			move: ['.ttee', true],
			moveOut:true, 
			area: ['auto','auto'],
			page: {
				html: '<img width="400" class="ttee" height="300" src="'+ImageUrl+'""/>'
			}
		});
	}
	
</script>
</head>
<body class="content-bg">
<form:form method="post" action="" commandName="annualMemberOrganization">
<input type="hidden" name="turnCode" value="${customer_organization_checklist.code}"/>
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
											<msg:message code="memberOrganization.name"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${annualMemberOrganization.name}
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.shortName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${annualMemberOrganization.shortName}
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.licenceCode"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${annualMemberOrganization.licenceCode}
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.licenceImage"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<img src="${ftpHttpUrl}${annualMemberOrganization.licenceImage}" height="40" width="100" onclick="changeBig('${ftpHttpUrl}${annualMemberOrganization.licenceImage}')">	
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.licenceExpireTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${annualMemberOrganization.licenceExpireTime}
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.representative"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${annualMemberOrganization.representative}
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.checkLicenceCode"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${annualMemberOrganization.checkLicenceCode}	
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.checkLicenceImage"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<img src="${ftpHttpUrl}${annualMemberOrganization.checkLicenceImage}" height="40" width="100" onclick="changeBig('${ftpHttpUrl}${annualMemberOrganization.checkLicenceImage}')">
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.checkLicenceExpireTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${annualMemberOrganization.checkLicenceExpireTime}
											
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.applicationTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${annualMemberOrganization.applicationTime}
										
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.applicantFeedback"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" valign="middle" class="skinLayout_lable">
											<form:textarea path="applicantFeedback" readonly="true" rows="3" cols="25" cssStyle="border:0;background:transparent;overflow:auto;width: 250px; height: 85px;"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											
										</td>
										<td width="35%" valign="middle" class="skinLayout_lable">
											
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


