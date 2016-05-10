<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<title>失败提示</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link  href="${base}/common/skin/css/skin.css" rel="stylesheet" type="text/css" />
<link  href="${base}/common/skin/css/main.css" rel="stylesheet" type="text/css" />
</head>
<body class="skinMain" bottommargin="0" leftmargin="0" rightmargin="0" topmargin="0">
<table align="center" height="100%" width="100%" border="0" cellpadding="0" cellspacing="0" class="tablemain">
	<tr>
		<td align="center" valign="top">
			<table align="center" border="0" cellpadding="0" cellspacing="0" class="tablemain">
				<tr>
					<td align="center" height="150">&nbsp;</td>
				</tr>
				<tr>
					<td align="center">
						<table border="0" cellpadding="0" cellspacing="0" >
							<tr>
								<td >
									<table width="100%" border="0" cellpadding="0" cellspacing="1" >
										<tr>
											<td align="center">
						                    <img align="absmiddle" src="${base}/common/skin/images/icon/error.png">
						                     	403！
				                            </td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td align="center" height="20"></td>
							</tr>				
				            <tr > 
				                <td align="center" valign="bottom">
				                <input type="button" class="btn2" onclick="javascript:window.history.back()" value="<msg:message code="button.back"/>">
				            	</td>
				            </tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>
