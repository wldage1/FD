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
<link href="${base}/${skin}/css/boxy.css" rel="stylesheet" type="text/css"/>
<link  href="${base}/${skin}/css/main.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/common/js/jquery-1.6.min.js"></script>
<script type="text/javascript" src="${base}/common/js/jquery-auto-screen.js"></script>
<script type="text/javascript" src="${base}/common/js/jquery-main-tag.js"></script>
<script type="text/javascript" src="${base}/common/js/jquery-menu-tag.js"></script>
<script type="text/javascript" src="${base}/common/js/boxy/jquery.boxy.js"></script>
</head>
<body class="main-body">
<table id="windowTable" align="center" width="100%" border="0" cellspacing="0" cellpadding="0" >
 <tr class="topNav" valign="top">
	<td valign="bottom">
		<table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" style="border-spacing:0px;">
			<tr>
				<td id="windowTop">
				<table align="center" border="0" cellspacing="0" cellpadding="0" height="100%" width="100%">
					<tr>
						<td width="30%" align="center"></td>
						<td width="40%" align="center"></td>
						<td width="30%" align="right">
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td id="windowTab">
				 <table align="center" border="0" cellspacing="0" cellpadding="0" width="100%">
					  <tr>
						 <td width="210" align="center"></td>
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
						 <td width="210" align="center">
							<msg:message code="user.welcome"/>${SPRING_SECURITY_LAST_USERNAME}!
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
	<tr valign="top">
	  <td id="windowMain">
	    <table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%" class="main">
		    <tr>
			    <td width="70" align="center" class="main-left-menu-td" valign="top">
			      <table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">
			          <tr>
			            <td height="100%" valign="top">
							<div id="menu"></div>
							<div id="menu_tb_tr" class="menu-bg-line"></div>
							<div id="menu_tb" class="menu-bg-tb"></div>
			            </td>
			          </tr>
			      </table>     	
				</td>
			    <td align="center" valign="top" id="content" class="rightContent" height="100%">
			  	  <iframe id="mainFrameWelcome" width="100%" height="100%" name="mainFrameWelcome" src="welcome" frameborder="0" scrolling="auto"></iframe>
			    </td>
			</tr>
		</table>	  
	  </td>
  </tr>  
</table>
</body>
</html>