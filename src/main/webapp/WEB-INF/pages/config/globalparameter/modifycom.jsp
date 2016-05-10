<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<title></title>

<script type="text/javascript">

$(document).ready(function() {
	//验证
	var options = {
			event:'focus',
			ajax:{
				url:'${base}/config/globalparameter/valid.json'
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					//提交表单
					$("form#globalparameter").submit();
				}
			}
		};
	$("form #btnok").valid(options);
	///////////////
	$("#dictionaryCode").change(function(){
		$("#codeItem option[value!='']").remove(); 
		      if ($("#dictionaryCode").val()==""){
		      return false;
	    }
		var selectfield="";
		
		 $.ajax({
			url:'${base}/config/globalparameter/getCodeItem.json',
			data:{
	                dictionaryCode: $("#dictionaryCode").val()
	        },//参数 
			type:'post',
			timeout:'60000',
			dataType:'json',
	        success:function(jsonData,textStatus){
				if (textStatus == "success"){
					$.each(jsonData.listDictionaryItem, function(i, item) {
					   selectfield += "<option value='"+item.itemValue+"'>"+item.itemName+"</option>"; 
					});
				}
				$("#codeItem").append(selectfield);
			}
		 });
		
	})
});
</script>
</head>
<body class="skinMain">
<form:form  method="post" action="saveCom" commandName="globalparameter" >
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<input type="hidden" name="turnCode" value="${config_globalparameter.code}"/>
			<input type="hidden" name="c" value="${globalparameter.c}"/>
			<input type="hidden" name="coverParam" value="1"/>
			<input type="hidden" name="prompt" value="name" />
			<input type="hidden" name="orgID" value="${globalparameter.orgID}"/>
			<input type="hidden" name="id" value="${globalparameter.id}"/>
			<input type="hidden" name="operate" value="1" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="globalparameter.company"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<form:input path="orgName" maxlength="20" disabled="true"/>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="globalparameter.code"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<form:input path="code" cssClass="valid-control" maxlength="50" readonly="true"/>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="globalparameter.name"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<form:input path="name" cssClass="valid-control" maxlength="50"/>
							<font color="red">&nbsp;*&nbsp;</font>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="globalparameter.value"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<form:input path="value" cssClass="valid-control" maxlength="100"/>
							<font color="red">&nbsp;*&nbsp;</font>
						</td>
					</tr>
					
				</table>
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		 <div class="toolbar">
		<div class="left"></div>
			<div class="center">
			<input type="button" class="btn2" name="btnok" 
			value="<msg:message code="button.save"/>" id="btnok"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
