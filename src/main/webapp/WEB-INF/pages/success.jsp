<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld"%>
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<title>成功提示</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link  href="${base}/${skin}/css/skin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/common/js/jquery-1.6.min.js"></script>
<script type="text/javascript">
 $(document).ready(function(){
	if('${directer}' != ''){
		$("#tr_back").attr("style", "display:none");
		$("#tr_close").attr("style", "display:block");
	}
 });
</script>
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
						                    <img align="absmiddle" src="${base}/${skin}/images/icon/success.png">
						                    <c:if test="${empty promptMessage}">
							                     <c:if test="${!empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}">
							                    	&nbsp;${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}
							                     </c:if>
						                    </c:if>	
						                    
						                     <c:if test="${!empty promptMessage}">
						                    &nbsp;${fn:replace(promptMessage, "<", "&lt;")}
						                    </c:if>	
				                            </td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td align="center" height="20"></td>
							</tr>				
				            <tr id="tr_back"> 
				                <td align="center" valign="bottom">
				                 <input type="button" class="btn3" onclick="window.location.href='${base}${backLink}'" value="<msg:message code="button.back"/>">
								 &nbsp;&nbsp;&nbsp;&nbsp; 
								 <input type="button" class="btn3" onclick="window.location.href='${base}${turnLink}'" value="<msg:message code="button.confirm"/>">
				                </td>
				            </tr>
							
							<tr id="tr_close" style="display:none"> 
				                <td align="center" valign="bottom">
				                  <input type="button" class="btn3" onclick="window.location.href='${base}${backLink}'" value="<msg:message code="button.back"/>">
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
