<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript"> 
var testAndValidationRuleBoxy;
$(document).ready(function(){
	
	$("#btntest").click(function(){

	var rulecontent =$("#ruleContent").val();
	if (rulecontent==null||rulecontent=="")
	{
		return false;
	}
	var url = "${base}/config/validationrule/validationTesting";
	var html= "<table cellSpacing=1 cellPadding=0 border=0 style='padding:0;'>"
	         +"    <tr><td><iframe width='450px' height='35px' name='validationFrame' id='validationFrame' src='"+url+"' frameborder='0'  style='padding:0px;'></iframe></td></tr>"
	         +"</table>";
	if(	testAndValidationRuleBoxy != null){
		testAndValidationRuleBoxy.setContent(html);
		testAndValidationRuleBoxy.show();
	}else{
		testAndValidationRuleBoxy = new Boxy(html, { title:"验证规则测试", closeText:"关闭" }); 
		$(".boxy-wrapper").css("width","460px");
	}
	});  
	
	var options = {
			event:'focus',
			ajax:{
				url:'${base}/config/validationrule/valid.json'
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					$("form#validationRule").submit();
				}
			}
		};
	$("form #btnok").valid(options);
	
}); 

</script> 
</head>
<body class="skinMain">
<form:form method="post" action="save" commandName="validationRule">
<input type="hidden" name="turnCode" value="${config_validationrule.code}" />
<input type="hidden" name="coverParam" value="1"/>
<input type="hidden" name="id" value="${validationRule.id}"/>
<input type="hidden" name="c" value="${validationRule.c}"/>
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="operate" value="2" />
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="validationrule.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="name" maxlength='20' onchange="this.value=$.trim(this.value)" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;
					</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="validationrule.rulecontent"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%"   valign="middle" class="skinLayout_lable">
						<div style="float:left;"><form:textarea path="ruleContent" maxlength='100' cols="30" rows="4" onchange="this.value=$.trim(this.value)" cssStyle="width: 250px; height: 85px;" class="valid-control"/></div>
						<div style="height:85px; line-height:85px; vertical-align:middle">
						<font  color="red" >&nbsp;&nbsp;*&nbsp;
						</font>
						</div>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="validationrule.promptmessage"/><msg:message code="system.common.sign.colon"/>
					</td>
						<td width="70%" class="skinLayout_lable">
						<div style="float:left;"><form:textarea path="promptMessage" maxlength='100' rows="4" cols="30" onchange="this.value=$.trim(this.value)" cssStyle="width: 250px; height: 85px;" class="valid-control"/></div>
						<div style="height:85px; line-height:85px; vertical-align:middle">
						<font color="red" >&nbsp;&nbsp;*&nbsp;
						</font>
						</div>
						
					</td>
				</tr>	
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="" name="btntest" value="<msg:message code="button.test"/>" id="btntest"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="window.location.href='${base}${config_validationrule.controller}?c=${config_validationrule.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
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
</form:form>
</body>
</html>
