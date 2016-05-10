<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<script type="text/javascript">
$(document).ready(function(){
	$("#btnok").click(function(){
		var rulecontent =parent.$("#ruleContent").val();
		var testrulecontent =$("#testrulecontent").val();
		if (testrulecontent!=""&&rulecontent!="")
		{
			var r=testrulecontent.match(rulecontent);
			if (r!=null) {
            window.parent.Boxy.alert("<msg:message code='testvalidationrule.pass'/>", null, {title: "<msg:message code='info.prompt'/>"});
            parent.testAndValidationRuleBoxy.hide();
            }
            else
            {
            window.parent.Boxy.alert("<msg:message code='testvalidationrule.failpass'/>", null, {title: "<msg:message code='info.prompt'/>"});
            }
			
        }
	});
});

</script>
</head>
<body width="100%">
	<table border="0" width="100%" cellspacing="1" cellpadding="0" class="skinLayout" id="tooltestTable">
	<tr>
	<td><msg:message code="validationrule.testrulecontent"/><msg:message code="system.common.sign.colon"/></td>
	<td><input id="testrulecontent" type="text" maxlength='100' style="width:250px"/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="btn2" name="btnok" value="<msg:message code="button.validation"/>" id="btnok"/></td>
	</tr>
	</table>
</body>
</html>