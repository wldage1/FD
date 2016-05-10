<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">

$(document).ready(function(){
//角色名称回显
	var auth = "${providerUser.authorization}".toString().split(",");
	var authname="";
	for(var i=0;i <auth.length;i++){
		<c:forEach var="item" items="${providerUserAuthorization}">
			if(${item.key}==auth[i]){
				authname+="${item.value}"+" ";
			}
		</c:forEach>
		
	}
	$("#content").html(authname)
});
</script>
</head>
<body class="skinMain">
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<form:form method="post" action="save" commandName="providerUser" >
			<input type="hidden" name="turnCode" value="${cooperate_provider_provideruser.code}"/>
			<input type="hidden" name="coverParam" value="1"/>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.name"/><msg:message code="system.common.sign.colon"/></td>
						<td width="35%" class="skinLayout_lable">
							${providerUser.name }
						</td>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.gender"/><msg:message code="system.common.sign.colon"/></td>
						<td width="35%" class="skinLayout_lable">
							<c:forEach var="item" items="${providerUserGender}">
							<c:if test="${item.key==providerUser.gender}">
							  ${item.value}
							</c:if>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.authorization"/><msg:message code="system.common.sign.colon"/></td>
						<td width="35%"  class="skinLayout_lable">
							<p id='content'></p>
						</td>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.account"/><msg:message code="system.common.sign.colon"/></td>
						<td width="35%" class="skinLayout_lable">
							${providerUser.account }	
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.mobilePhone"/><msg:message code="system.common.sign.colon"/></td>
						<td width="35%" class="skinLayout_lable">
							${providerUser.mobilePhone }
						</td>
						<td width="15%" align="right" class="skinLayout_info">
						</td>
						<td width="35%" class="skinLayout_lable">
							
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provideruser.description"/><msg:message code="system.common.sign.colon"/></td>
						<td width="35%" class="skinLayout_lable">
							<form:textarea path="description" readonly="true" rows="3" cols="25" cssStyle="border:0;background:transparent;overflow:auto;width: 250px; height: 79px;" />
						</td>
						<td width="15%" align="right" class="skinLayout_info">
						</td>
						<td width="35%" class="skinLayout_lable">
							
						</td>
					</tr>
				</table>
			</form:form>
			</td>
		</tr>
		<tr>
			<td></td>
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
</html>
