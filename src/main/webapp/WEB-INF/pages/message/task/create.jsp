<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">

$(document).ready(function(){
	$("#tr_frequency").hide();
	$("#tr_frequency_select").hide();
	$(":radio[name='sendType']").click(function(){
		if(this.value == 2){
			$("#tr_frequency").show();
			$("#tr_frequency_select").show();
		}else{
			$("#tr_frequency").hide();
			$("#tr_frequency_select").hide();
		}
	});
	
	//验证
	 var options = {
			event:'focus',
			ajax:{
				url:'${base}/message/task/valid.json',
				data: {'type':$("form#type").val(),'code':$("form#code").val()}
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					//提交表单
					$("form#messageTask").submit();
				}
			}
		};
	$("form #btnok").valid(options); 
});
//sendFrequency
function showSelectContent(){
	var value = $("select[name='sendFrequency'] option:selected").val();
	var content;
	switch (parseInt(value)){
	case 1:
		var timeSelect = packageSelect("quartzHour", 24);
		content = "每天" + timeSelect + "点发送消息。";
		break;
	case 2:
		var timeSelect = packageSelect("quartzHour", 24);
		var weekSelect = packageSelect("quartzWeek", 7);
		content = "每周" + weekSelect + "," + timeSelect + "点发送消息。";
		break;
	case 3:
		var timeSelect = packageSelect("quartzHour", 24);
		var daySelect = packageSelect("quartzDay", 31);
		content = "每月" + daySelect + "号" + timeSelect + "点发送消息。";
		break;
	case 4:
		var timeSelect = packageSelect("quartzHour", 24);
		var daySelect = packageSelect("quartzDay", 31);
		var monthSelect = packageSelect("quartzMonth", 12);
		content = "每年" + monthSelect + "月" + daySelect + "号" + timeSelect + "点发送消息。";
		break;
	}
	$("#frequencySelect").html(content);
}

function packageSelect(selectName, endNumber){
	var content;
	content = "<select style='width:40px;' name='";
	content += selectName;
	content += "'>"
	for(var i=1; i<=endNumber; i++){
		content += "<option value='";
		content += i;
		content += "'>";
		content += i;
		content += "</option>";
	}
	content += "</select>";
	return content;
}

</script>

<style type="text/css">

input[type="radio"] {
	width  : 14px;
	height : 14px;
	vertical-align:text-bottom; margin-bottom:-1px; margin-bottom:-2px\9;
}
input[type="checkbox"] {
	width  : 14px;
	height : 14px;
	vertical-align:text-bottom; margin-bottom:-1px; margin-bottom:-2px\9;
}
</style>

</head>
<body class="skinMain">
	<form:form method="post" action="save" id="messageTask" commandName="messageTask" >
		<input type="hidden" name="id" id="id" value="${messageTask.id}" />
		<input type="hidden" name="c" value="${messageTask.c}" />
		<input type="hidden" name="prompt" value="name"/>
		<input type="hidden" name="turnCode" value="${message_task.code}"/>
		<input type="hidden" name="coverParam" value="1"/>
		<input type="hidden" name="type" value="2"/>
		<input type="hidden" name="sendType" value="1"/>
		<input type="hidden" name="templateType" value="0"/>
		<input type="hidden" name="templateName" value="0"/>
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="messageTask.name"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="name" class="valid-control" maxlength="100"/>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="messageTask.code"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="code" class="valid-control" maxlength="50"/>
							</td>
						</tr>
					<%-- 	<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="messageTask.sendType"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable" >
								<form:radiobutton path="sendType" value="1" /><msg:message code="messageTask.immediatelySend"/>
								<form:radiobutton path="sendType" value="2" class="valid-control" />
								<msg:message code="messageTask.timingSend"/>
							</td>
						</tr> --%>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="messageTask.status"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:radiobutton path="status" value="1" /><msg:message code="messageTask.forbidden"/>
								<form:radiobutton path="status" value="2" /><msg:message code="messageTask.enabled"/>
								<form:radiobutton path="status" value="3" class="valid-control" />
								<msg:message code="messageTask.pause"/>
							</td>
						</tr>
						<tr id="tr_frequency">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="messageTask.sendFrequency"/>
							<msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="sendFrequency" onchange="showSelectContent()" class="valid-control" >
									<form:option value="0"><msg:message code="messageTask.select"/></form:option>
									<form:option value="1"><msg:message code="messageTask.daySend"/></form:option>
									<form:option value="2"><msg:message code="messageTask.weekSend"/></form:option>
									<form:option value="3"><msg:message code="messageTask.monthSend"/></form:option>
									<form:option value="4"><msg:message code="messageTask.yearSend"/></form:option>
								</form:select>
							</td>
						</tr>
						<tr id="tr_frequency_select">
							<td width="15%" align="right" class="skinLayout_info"> </td>
							<td width="85%" class="skinLayout_lable">
								<div id="frequencySelect"></div>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right">&nbsp;</td>
							<td width="85%" colspan="2" align="left">
								<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							    <input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form:form>
</body>
