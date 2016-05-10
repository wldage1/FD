<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><msg:message code="system.name"/></title>
<link  href="${base}/${skin}/css/index.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/common/js/jquery-1.8.min.js"></script>
<script type="text/javascript">
 $(document).ready(function(){
	 if (window != top.window){
		 top.location.href = "${base}";
	 }
 	var $captchaImage = $("#captchaImage");
 	var $captchaInput = $("#j_captcha");
 	//设置验证码输入框为空
 	$captchaInput.val("");
	// 刷新验证码
	$captchaImage.click( function() {
		var timestamp = (new Date()).valueOf();
		var imageSrc = $captchaImage.attr("src");
		if(imageSrc.indexOf("?") >= 0) {
			imageSrc = imageSrc.substring(0, imageSrc.indexOf("?"));
		}
		imageSrc = imageSrc + "?timestamp=" + timestamp;
		$captchaImage.attr("src", imageSrc);
		//设置验证码输入框为空
		$captchaInput.val("");
	});	
 });
</script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form action="<%=request.getContextPath()%>/j_spring_security_check" method="post">
<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
    <tr>
        <td class="header">
        </td>
    </tr>
    <tr>
        <td align="center" valign="top">
            <table width="574" height="497" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="center_01"></td>
                    <td class="center_00" align="center">
               	   	    <table width="514" height="497" border="0" align="center" cellpadding="0" cellspacing="0"">
                            <tr>
                                <td class="center_02">&nbsp;</td> 
               		  		</tr>
                            <tr  style="background-color:#e2f0f9;">
                            	<td class="center_03" align="center" valign="top" height="179">
                                    <table width="293" border="0" align="center" cellpadding="0" cellspacing="4">
                                        <tr>
                                           <td height="15"  colspan="2">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td width="100" height="32" align="right"><msg:message code="user.account"/></td>
                                            <td align="left" class="login_td">
                                            	<input type="text" name="j_username" id="j_username" class="input_01" maxlength="25"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td height="32" align="right"><msg:message code="user.password"/></td>
                                            <td height="32" align="left" class="login_td">
                                            	<input type="password" name="j_password" id="j_password" class="input_02" maxlength="20"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td height="32" align="right" ><msg:message code="user.captcha"/></td>
                                            <td align="center" class="login_td">
                                                <table border="0" height="32"  align="center" width="100%" cellpadding="0" cellspacing="0">
                                                    <tr>
                                                        <td height="32" valign="middle" align="left">
                                                        	<input type="text" name="j_captcha" id="j_captcha" class="input_03" maxlength="6"/>
                                                        </td>
                                                        <td height="32" align="left" valign="middle">
                                                       		<img class="captchaImage" id="captchaImage" src="<%=request.getContextPath()%>/captcha.jpg" align="absmiddle" width="86" height="23" />
                                                        </td>
                                                    </tr>
                                                </table>                                            
                                            </td>
                                        </tr>
                                        <tr>
                                          <td height="32" align="left">&nbsp;</td>
                                            <td height="32" align="left">
                                            	<input type="submit" value="" class="button"/>
                                            </td>
                                        </tr>
                          	  		</table>
                           	  </td> 
                            </tr>
                            <tr>
                                <td class="center_04" align="center"><msg:message code="system.coypright"></msg:message></td> 
                            </tr>
                        </table>                    
                    </td>
                    <td class="center_05">&nbsp;</td>
                </tr>
            </table>        
		</td>
 	</tr>
</table>
</form>
</body>
</html>