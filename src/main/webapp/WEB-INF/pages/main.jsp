<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%@ taglib uri="/WEB-INF/tlds/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@ taglib prefix="decorator" uri="/WEB-INF/tlds/sitemesh-decorator.tld" %>
<%@ taglib prefix="page" uri="/WEB-INF/tlds/sitemesh-page.tld" %>
<%@ taglib prefix="security" uri="/WEB-INF/tlds/springframework-security.tld" %>
<%@ taglib prefix="spring" uri="/WEB-INF/tlds/springframework-spring.tld" %>
<%@ taglib prefix="form" uri="/WEB-INF/tlds/springframework-form.tld" %>
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><msg:message code="system.name"></msg:message></title>
<link  href="${base}/${skin}/css/skin.css" rel="stylesheet" type="text/css" />
<link href="${base}/${skin}/css/layer.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${base}/common/js/jquery-1.8.min.js"></script>
<script type="text/javascript" src="${base}/common/js/jquery-auto-screen.js"></script>
<script type="text/javascript" src="${base}/common/js/jquery-main-tag.js"></script>
<script type="text/javascript" src="${base}/common/js/jquery-menu-tag.js"></script>
<script src="${base}/common/js/layer/layer.min.js" type="text/javascript"></script>

</head>
<body onload="menuTab();">
<table id="windowTable" align="center" width="100%" border="0" cellspacing="0" cellpadding="0" >
 <tr class="topNav" valign="top">
	<td valign="bottom">
		<table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" style="border-spacing:0px;">
			<tr>
				<td id="windowTop">
				<%-- <table align="center" border="0" cellspacing="0" cellpadding="0" height="100%" width="100%">
					<tr>
						<td width="30%"  align="center">	<img alt="logo" src="${base}/common/skin/images/main/logo.png"> </td>
						<td width="40%" align="center"></td>
						<td width="30%" align="right">
						</td>
					</tr>
				</table> --%>
				<img alt="logo" src="${base}/common/skin/images/main/logo.png" style="height: 40px;">
				</td>
			</tr>
			<tr>
				<td id="windowTab">
				 <table align="center" border="0" cellspacing="0" cellpadding="0" width="100%">
					  <tr>
						 <td width="300" align="center"></td>
						 <td colspan="2">
							<div class="mainNav">
								<ul id="showTabContainer">
								</ul>
								<div class="more">
									<div class="navMore" id="moreTabBtn"></div>
									<div class="moreList" id="moreTabList">
										<ul id="hideTabContainer">
										</ul>
									</div>
								</div>
							</div> 
						 </td>
						 <td width="300" align="center">
							${SPRING_SECURITY_LAST_USERNAME}<msg:message code="user.welcome"/>
						    <security:authorize ifAnyGranted="501">
							<a id="my_sys_message_tip" href="#">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
							<span id="my_sys_message_new_arrived_count"></span>&nbsp;&nbsp;&nbsp;
							</security:authorize>
							<a href="logout"><msg:message code="user.exit"/></a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						 </td>
					  </tr>		
				  </table>
				</td>
			</tr> 
		</table>
	</td>
	</tr>
	<tr>
	  <td id="windowMain" valign="top" style="position: relative;">
	  <div class="menu_button_bg" id="show_or_hide_menu"><div class="show"></div></div>
	    <table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%" class="main">
		    <tr>
			    <td width="168" align="center" valign="top" id="fd_menu_container" >
			      <table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%" >
			          <tr>
			            <td height="30" align="center" id="systemName" >&nbsp;</td>
			          </tr>
					  <tr>
						<td width="168" align="center" class="main-left-menu-td" valign="top">
							<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">
								<tr>
									<td height="100%" valign="top" >
										<div id="menu"></div>
										<div id="menu_tb_tr" class="menu-bg-line"></div>
										<div id="menu_tb" class="menu-bg-tb"></div>
									</td>
								</tr>
							</table>
						</td>
					 </tr>
				</table>     	
				</td>
			    <td align="center" valign="top" id="content" class="rightContent" height="98%">
			  	  <iframe id="mainFrameWelcome" width="100%" height="100%" name="mainFrameWelcome" src="#" frameborder="0" scrolling="auto"></iframe>
			    </td>
			</tr>
		    <%-- <tr><td></td>
				<td colspan="1">
					<div id="windowBottom"><msg:message code="system.coypright" /></div>
				</td>
			</tr> --%>
		</table>	  
	  </td>
  </tr>    
</table>
<script>
	var isFirstLogin='${isFirstLogin}';
	if(isFirstLogin){
		$("#mainFrameWelcome").attr("src","content?c=805");
	}else{
		$("#mainFrameWelcome").attr("src","welcome");
	}
</script>
</body>
</html>