<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title></title>
	<script type="text/javascript">
		$(document).ready(function(){
			//验证
			var options = {
				event:'focus',
				ajax:{
					url:'${base}/activity/manage/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						//提交表单
						$("form#activity").submit();
					}
				}
			};
			$("form #btnok").valid(options);
		});
	</script>
</head>
<body class="skinMain">
	<form:form action="save" method="post" commandName="activity">
		<input type="hidden" name="turnCode" value="${activity_manage.code}" />
		<input type="hidden" name="coverParam" value="1" />
		<input name="c" type="hidden" value="${activity.c}">
		<input type="hidden" name="prompt" value="name"/>
		<input name="id" type="hidden" value="${activity.id}">
		<table width="100%" border="0" cellpadding="0" cellspacing="1" class="skinLayout">
			<tr>
				<td width="15%" align="right" class="skinLayout_info">
					活动类别<msg:message code="system.common.sign.colon"/>
				</td>
				<td width="85%" class="skinLayout_lable">
					<select name="type" id="type">
						<c:forEach items="${activityType}" var="item">
							<c:choose>
								<c:when test="${item.id == acticity.type}">
									<option value="${item.id}" selected="selected">${item.name}</option>
								</c:when>
								<c:otherwise>
									<option value="${item.id}">${item.name}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td width="15%" align="right" class="skinLayout_info">
					活动名称<msg:message code="system.common.sign.colon"/>
				</td>
				<td width="85%" class="skinLayout_lable">
					<form:input path="name" type="text" class="valid-control"/>
					<font color="red">&nbsp;*&nbsp;</font>
				</td>
			</tr>
			<tr>
				<td width="15%" align="right" class="skinLayout_info">
					开始时间<msg:message code="system.common.sign.colon"/>
				</td>
				<td width="85%" class="skinLayout_lable">
					<form:input path="startTime" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" class="Wdate valid-control"/>
					<font color="red">&nbsp;*&nbsp;</font>
				</td>
			</tr>
			<tr>
				<td width="15%" align="right" class="skinLayout_info">
					结束时间<msg:message code="system.common.sign.colon"/>
				</td>
				<td width="85%" class="skinLayout_lable">
					<form:input path="endTime" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" class="Wdate valid-control"/>
					<font color="red">&nbsp;*&nbsp;</font>
				</td>
			</tr>
			<tr>
				<td width="15%" align="right" class="skinLayout_info">
					模板名称<msg:message code="system.common.sign.colon"/>
				</td>
				<td width="85%" class="skinLayout_lable">
					<form:input path="templetName" type="text"/>
				</td>
			</tr>
			<tr>
				<td width="15%" align="right" class="skinLayout_info">
					网页地址<msg:message code="system.common.sign.colon"/>
				</td>
				<td width="85%" class="skinLayout_lable">
					<form:input path="url" type="text"/>
				</td>
			</tr>
			<tr>
				<td width="15%" align="right" class="skinLayout_info">
					活动描述<msg:message code="system.common.sign.colon"/>
				</td>
				<td width="85%" class="skinLayout_lable">
					<form:textarea path="description" cssStyle="width:500px;height:150px;border:1px solid #A6C9E2;font-size:14px;"/>
				</td>
			</tr>
			<tr>
				<td width="15%" align="right">&nbsp;</td>
				<td width="85%" colspan="2" align="left">
					<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" class="btn2" onclick="window.location.href='${base}${activity_manage.controller}?c=${activity_manage.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
				</td>
			</tr>
		</table>
	</form:form>
</body>
</html>