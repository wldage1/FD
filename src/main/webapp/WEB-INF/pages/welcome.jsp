<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<title><msg:message code="system.name"></msg:message></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link  href="${base}/${skin}/css/skin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/common/js/jquery-1.8.min.js"></script>
<script type="text/javascript" src="${base}/common/js/global.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$(".maptr td").hover(function(){
		  $(this).removeClass("flow-chart-level-2").addClass("flow-chart-level-2-temp");
	  },function(){
		  $(this).removeClass("flow-chart-level-2-temp").addClass("flow-chart-level-2");
	});	
	$(".mapa").click(function(){
		var code = $(this).attr("id");
		var txt = $(this).text();
		window.parent.menuTab(code,txt);
	});	
	var bodyHeight = document.body.clientHeight;
	$("#content_td").attr("height",bodyHeight-60);
	
	$("#systemmap").click(function(){
		var dis = $("#content_map").css("display");
		if (dis == 'none'){
			$("#content_flow").fadeOut(500,function(){
				$("#content_map").fadeIn(1000);
			});
			
		}
	});	
	$("#bussinessflow").click(function(){
		var dis = $("#content_flow").css("display");
		if (dis == 'none'){
			$("#content_map").fadeOut(500,function(){
				$("#content_flow").fadeIn(1000);
			});
		}
	});		
});
</script>
</head>
<body class="skinMain">
<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="skinNav">
	<tr>
		<th width="2%"><img src="${base}/${skin}/images/nav/bg_07.gif" width="10" height="9" /></th>
		<th width="98%"><msg:message code="system.name"/></th>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" style="padding:5px;" > 
	<tr>
		<td valign="top" colspan="2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="welcomeSkin">
				<tr>
					<th><msg:message code="info.prompt.system.map"/></th>
				</tr>
				<c:forEach var="auth1" items="${authlist1}" varStatus="vsindex1">
				<security:authorize ifAnyGranted="${auth1.code}" >
				<tr>
					<td>
						
						<b style="color:#8486C6;"><img src="${base}/${skin}/${auth1.icon}" width="18" height="18" align="absmiddle">${auth1.name}</b>
						<c:forEach var="auth2" items="${authlist2}" varStatus="vsindex2">
							<c:if test="${auth1.code == auth2.parentCode}">
								&nbsp;
								<font color="#3165CE">
								<security:authorize ifAnyGranted="${auth2.code}" >
								    <img src="${base}/${skin}/${auth2.icon}" width="18" height="18" align="absmiddle">
									&nbsp;<a href="#" class="mapa" id="${auth2.code}">${auth2.name}</a>
								</security:authorize>
								<security:authorize ifNotGranted="${auth3.code}">
									&nbsp;${auth2.name}
								</security:authorize>
								</font>
							</c:if>
						</c:forEach>
					</td>
				</tr>
				</security:authorize>
				</c:forEach>
			</table>
		</td>
	</tr>
	<tr>
		<td valign="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="welcomeSkin">
				<tr>
					<th><msg:message code="info.prompt.system.info"/></th>
				</tr>
				<tr>
					<td><msg:message code="info.prompt.system.os.version"/>${osName} ${osVersion}</td>
				</tr>
				<tr>
					<td><msg:message code="info.prompt.system.browser.version"/>${bversion}</td>
				</tr>	
				<tr>
					<td><msg:message code="info.prompt.system.server.version"/>${serverInfo}</td>
				</tr>	
				<tr>
					<td><msg:message code="info.prompt.system.servlet.version"/>${servletVersion}</td>
				</tr>																					
				<tr>
					<td><msg:message code="info.prompt.system.vm.version"/>${vmVendor} ${runtimeName} ${runtimeVersion}</td>
				</tr>	
				<tr>
					<td><msg:message code="info.prompt.system.mtotal"/>${totalkb}KB</td>
				</tr>		
				<tr>
					<td><msg:message code="info.prompt.system.mfree"/>${freekb}KB</td>
				</tr>					
			</table>
		</td>
	</tr>
</table>
</body>
</html>
