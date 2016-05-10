<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${base}/common/js/layer/layer.js"></script>
<title>理财顾问认证审核</title>
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
<form:form method="post" action="" commandName="cancelMember">
<input type="hidden" name="turnCode" value="${customer_member_checklist.code}"/>
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
											<msg:message code="member.name"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${cancelMember.name}
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.iDCard"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${cancelMember.iDCard}
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.birthday"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${cancelMember.birthday}
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.mobilePhone"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${cancelMember.mobilePhone}
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.cardImage"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<img src="${ftpHttpUrl}${cancelMember.cardImage}" height="40" width="250" onclick="changeBig('${ftpHttpUrl}${cancelMember.cardImage}')">
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.changedTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											${cancelMember.changedTime}
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.applicantFeedback"/><msg:message code="system.common.sign.colon"/>
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


