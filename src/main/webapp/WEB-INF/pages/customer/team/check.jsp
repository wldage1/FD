<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${base}/common/js/layer/layer.js"></script>
<link href="${base}/${skin}/css/layer.css" rel="stylesheet" type="text/css"/>
<title>机构审核</title>
<link href="${base}/${skin}/css/seatcenter/index.css" rel="stylesheet" type="text/css"/>
<link href="${base}/${skin}/css/seatcenter/jquery.ui.custom.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${base}/${skin}/css/seatcenter/ui.jqgrid.css" rel="stylesheet" type="text/css" media="screen"/>

<script type="text/javascript" src="${base}/common/js/jqgrid/jquery.jqGrid.src.js" ></script>
<script type="text/javascript" src="${base}/common/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/common/js/jqgrid/jquery.ui.min.js"></script>
<script type="text/javascript" src="${base}/common/js/jqgrid/grid.locale-cn.js"></script>
<script type="text/javascript">
  function saveTeamCheck(i){
		var applicantFeedback;
		var checkId;
		var checkType;
		switch (i)
	       {
	          case 1:
	              checkId=$("#createCheckId").val();
	              checkType=$("#createCheckType").val();
	              applicantFeedback = $("#div_createcheck #applicantFeedback").val();
	              break;
	          case 2:
	              checkId=$("#changeCheckId").val();
	              checkType=$("#changeCheckType").val();
	              applicantFeedback = $("#div_changecheck #applicantFeedback").val();
	              break;
	          case 3:
	              checkId=$("#annualCheckId").val();
	              checkType=$("#annualCheckType").val();
	              applicantFeedback = $("#div_annualcheck #applicantFeedback").val();
	              break;
	          case 4:
	              checkId=$("#cancelCheckId").val();
	              checkType=$("#cancelCheckType").val();
	              applicantFeedback = $("#div_cancelcheck #applicantFeedback").val();
	              break;
	      }
		var url = "${base}/customer/team/saveTeamCheck.json";
		$.ajax({
			url:url,
			data:{
	                id:$("#id").val(),
	                checkId: checkId,
	                checkType: checkType,
	                applicationMember: $("#applicationMember").val(),
	                memberID: $("#memberID").val(),
	                checkLicenceCode: $("#checkLicenceCode").val(),
	                checkLicenceImage: $("#checkLicenceImage").val(),
	                checkLicenceExpireTime: $("#checkLicenceExpireTime").val(),
	                applicantFeedback: applicantFeedback,
	           },//参数
			type:'post',
			timeout:'60000',
			dataType:'json',
			success:function(jsonData,textStatus){
				if (textStatus == "success"){
					if(jsonData.mark=="true"){
						$.layer({
							shade : [0.5 , '#000' , true],
					  		offset: ['50px',''],
					  	 	title : '<msg:message code="info.prompt"/>',
						    dialog : {msg:'<msg:message code="team.checksuccess"/>',type : 3}	
						});
						switch (i)
					       {
					          case 1:
					        	  $("#createbtnok").detach();
					        	  $("#createbtnback").detach();
					              break;
					          case 2:
					              $("#changebtnok").detach();
					        	  $("#changebtnback").detach();
					              break;
					          case 3:
					              $("#annualbtnok").detach();
					        	  $("#annualbtnback").detach();
					              break;
					          case 4:
					              $("#cancelbtnok").detach();
					        	  $("#cancelbtnback").detach();
					              break;
					      }
					}else if(jsonData.mark=="false")
					{
						$.layer({
							shade : [0.5 , '#000' , true],
					  		offset : ['50px',''],
					  	 	title : '<msg:message code="info.prompt"/>',
						    dialog : {msg:'<msg:message code="team.checkerror"/>',type : 3}	
						});
					}
				}
			}
		});
	}
	function backTeamCheck(i){
	var applicantFeedback;
	var checkId;
	var checkType
	switch (i)
	       {
	          case 1:
	              checkId=$("#createCheckId").val();
	              checkType=$("#createCheckType").val();
	              applicantFeedback = $("#div_createcheck #applicantFeedback").val();
	              break;
	          case 2:
	              checkId=$("#changeCheckId").val();
	              checkType=$("#changeCheckType").val();
	              applicantFeedback = $("#div_changecheck #applicantFeedback").val();
	              break;
	          case 3:
	              checkId=$("#annualCheckId").val();
	              checkType=$("#annualCheckType").val();
	              applicantFeedback = $("#div_annualcheck #applicantFeedback").val();
	              break;
	          case 4:
	              checkId=$("#cancelCheckId").val();
	              checkType=$("#cancelCheckType").val();
	              applicantFeedback = $("#div_cancelcheck #applicantFeedback").val();
	              break;
	      }
		var url = "${base}/customer/team/backTeamCheck.json";
		$.ajax({
			url:url,
			data:{
                 id:$("#id").val(),
                 checkId: checkId,
                 checkType: checkType,
                 applicantFeedback: applicantFeedback,
            },//参数
			type:'post',
			timeout:'60000',
			dataType:'json',
			success:function(jsonData,textStatus){
				if (textStatus == "success"){
					if(jsonData.mark=="true"){
						$.layer({
							shade : [0.5 , '#000' , true],
					  		offset : ['50px',''],
					  	 	title : "<msg:message code='info.prompt'/>",
						    dialog : {msg:"<msg:message code='team.backsuccess'/>",type : 3}	
						});
						switch (i)
					       {
					          case 1:
					        	  $("#createbtnok").detach();
					        	  $("#createbtnback").detach();
					              break;
					          case 2:
					              $("#changebtnok").detach();
					        	  $("#changebtnback").detach();
					              break;
					          case 3:
					              $("#annualbtnok").detach();
					        	  $("#annualbtnback").detach();
					              break;
					          case 4:
					              $("#cancelbtnok").detach();
					        	  $("#cancelbtnback").detach();
					              break;
					      }
					}else if(jsonData.mark=="false")
					{
					   $.layer({
							shade : [0.5 , '#000' , true],
					  		offset : ['50px',''],
					  	 	title : "<msg:message code='info.prompt'/>",
						    dialog : {msg:"<msg:message code='team.backerror'/>",type : 3}	
					   });
					}
				}
			}
		});
	}
	
$(document).ready(function(){
	//如果机构创建申请实体为空的话，删除机构创建审核的tab
	if (${empty createTeam}){
		$("#createcheck").detach();
		$("#div_createcheck").detach();
	}
	//如果注销实体为空的话，删除机构注销的tab
	if (${empty changeTeam}){
		$("#changecheck").detach();
		$("#div_changecheck").detach();
	}
	//如果注销实体为空的话，删除机构注销的tab
	if (${empty annualTeam}){
		$("#annualcheck").detach();
		$("#div_annualcheck").detach();
	}
	//如果机构注销申请实体为空的话，删除机构注销审核的tab
	if (${empty cancelTeam}){
		$("#cancelcheck").detach();
		$("#div_cancelcheck").detach();
	}
	$("#tab div").click(function(){
		//清除所有tab样式，并设置为不选中
		$("#tab div").each(function(){
			$(this).removeClass().addClass("tab-not-select");
		});
		//隐藏所有内容div
		$(".tab-content-show").each(function(){
			$(this).removeClass().addClass("tab-content-hide");
		});
		//设置选中tab为选中样式状态
		$(this).removeClass().addClass("tab-select");
		//设置tab对应的内容为选中状态
		var tabContentId = $(this).find("a").attr("id");
		$("#div_"+tabContentId).removeClass().addClass("tab-content-show");
	});
	//默认第一个div tab选中
	$("#tab div:first").click();
});

</script>
</head>
<body class="content-bg">
<table width="99%" align="center" border="0" cellpadding="0" cellspacing="0" >
	<tr>
		<td>
			<div id="tab" class="tab">
				<div class="tab-not-select" id="createcheck"><a id="createcheck" href="javascript:;"><msg:message code='team.createcheck'/></a></div>
				<div class="tab-not-select" id="changecheck"><a id="changecheck" href="javascript:;"><msg:message code='team.changecheck'/></a></div>
				<div class="tab-not-select" id="annualcheck"><a id="annualcheck" href="javascript:;"><msg:message code='team.annualcheck'/></a></div>
				<div class="tab-not-select" id="cancelcheck"><a id="cancelcheck" href="javascript:;"><msg:message code='team.cancelcheck'/></a></div>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div id="tab_content" class="tab-content">
				<div id="div_createcheck" class="tab-content-show" style="overflow-y:scroll ;">
				<c:if test="${!empty createTeam}">
					<form:form method="post" action="saveCreateTeam" commandName="createTeam">
					<input type="hidden" name="id" id="id" value="${createTeam.id}"/>
					<input type="hidden" name="checkId" id="createCheckId" value="${createTeam.checkId}"/>
					<input type="hidden" name="checkType" id="createCheckType" value="${createTeam.checkType}"/>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
						<tr>
							<td width="100%">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.name"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="name" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.shortName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="shortName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceCode"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceCode" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceImage"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceImage" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceExpireTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceExpireTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.representative"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="representative" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicationMemberName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="applicationMemberName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
											
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicationTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="applicationTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicantFeedback"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%"   valign="middle" class="skinLayout_lable">
											<div style="float:left;"><form:textarea path="applicantFeedback" maxlength='100' cols="30" rows="4" onchange="this.value=$.trim(this.value)" cssStyle="width: 250px; height: 85px;" class="valid-control"/></div>
											<div style="height:85px; line-height:85px; vertical-align:middle">
											</div>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right">&nbsp;</td>
										<td width="85%" colspan="3" align="left">
											<input type="button" class="btn2" name="btnok" onclick="saveTeamCheck(1)" value="审核通过" id="createbtnok"/>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="button" class="btn2" onclick="backTeamCheck(1)" name="createbtnback" value="退回" id="createbtnback"/>
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
                    </c:if>
				</div>
				<div id="div_changecheck" class="tab-content-hide">
					<c:if test="${!empty changeTeam}">
					<form:form method="post" action="saveChangeTeam" commandName="changeTeam">
					<input type="hidden" name="id" id="id" value="${changeTeam.id}"/>
					<input type="hidden" name="checkId" id="changeCheckId" value="${changeTeam.checkId}"/>
					<input type="hidden" name="checkType" id="changeCheckType" value="${changeTeam.checkType}"/>
					<input type="hidden" name="memberID" id="memberID" value="${changeTeam.memberID}"/>
					<input type="hidden" name="applicationMember" id="applicationMember" value="${changeTeam.applicationMember}"/>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
						<tr>
							<td width="100%">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.name"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="name" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.shortName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="shortName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceCode"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceCode" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceImage"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceImage" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceExpireTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceExpireTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.representative"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="representative" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicationMemberName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="applicationMemberName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
											
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicationTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="applicationTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										
										</td>
										
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.memberName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="memberName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>	
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											
										</td>
										<td width="35%" class="skinLayout_lable">
											
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicantFeedback"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%"   valign="middle" class="skinLayout_lable">
											<div style="float:left;"><form:textarea path="applicantFeedback" maxlength='100' cols="30" rows="4" onchange="this.value=$.trim(this.value)" cssStyle="width: 250px; height: 85px;" class="valid-control"/></div>
											<div style="height:85px; line-height:85px; vertical-align:middle">
											</div>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right">&nbsp;</td>
										<td width="85%" colspan="3" align="left">
											<input type="button" class="btn2" name="changebtnok" onclick="saveTeamCheck(2)" value="审核通过" id="changebtnok"/>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="button" class="btn2" onclick="backTeamCheck(2)" name="changebtnback" value="退回" id="changebtnback"/>
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
					</c:if>	
				</div>
				<div id="div_annualcheck" class="tab-content-hide">
				<c:if test="${!empty annualTeam}">
					<form:form method="post" action="saveAnnualTeam" commandName="annualTeam">
					<input type="hidden" name="id" id="id" value="${annualTeam.id}"/>
					<input type="hidden" name="checkId" id="annualCheckId" value="${annualTeam.checkId}"/>
					<input type="hidden" name="checkType" id="annualCheckType" value="${annualTeam.checkType}"/>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
						<tr>
							<td width="100%">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.name"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="name" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.shortName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="shortName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceCode"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceCode" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceImage"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceImage" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceExpireTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceExpireTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.representative"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="representative" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.checkLicenceCode"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="checkLicenceCode" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>	
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.checkLicenceImage"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="checkLicenceImage" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>	
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.checkLicenceExpireTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="checkLicenceExpireTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
											
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicationTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="applicationTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicantFeedback"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%"   valign="middle" class="skinLayout_lable">
											<div style="float:left;"><form:textarea path="applicantFeedback" maxlength='100' cols="30" rows="4" onchange="this.value=$.trim(this.value)" cssStyle="width: 250px; height: 85px;" class="valid-control"/></div>
											<div style="height:85px; line-height:85px; vertical-align:middle">
											</div>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right">&nbsp;</td>
										<td width="85%" colspan="3" align="left">
											<input type="button" class="btn2" name="annualbtnok" onclick="saveTeamCheck(3)" value="审核通过" id="annualbtnok"/>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="button" class="btn2" onclick="backTeamCheck(3)" name="annualbtnback" value="退回" id="annualbtnback"/>
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
					</c:if>
				</div>
				<div id="div_cancelcheck" class="tab-content-hide">
				<c:if test="${!empty cancelTeam}">
					<form:form method="post" action="saveCancelTeam" commandName="cancelTeam">
					<input type="hidden" name="id" id="id" value="${cancelTeam.id}"/>
					<input type="hidden" name="checkId" id="cancelCheckId" value="${cancelTeam.checkId}"/>
					<input type="hidden" name="checkType" id="cancelCheckType" value="${cancelTeam.checkType}"/>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
						<tr>
							<td width="100%">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.name"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="name" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.shortName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="shortName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceCode"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceCode" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceImage"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceImage" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.licenceExpireTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceExpireTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.representative"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="representative" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
							
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicationMemberName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="applicationMemberName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
											
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicationTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="applicationTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicantFeedback"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%"   valign="middle" class="skinLayout_lable">
											<div style="float:left;"><form:textarea path="applicantFeedback" maxlength='100' cols="30" rows="4" onchange="this.value=$.trim(this.value)" cssStyle="width: 250px; height: 85px;" class="valid-control"/></div>
											<div style="height:85px; line-height:85px; vertical-align:middle">
											</div>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right">&nbsp;</td>
										<td width="85%" colspan="3" align="left">
											<input type="button" class="btn2" name="cancelbtnok" onclick="saveTeamCheck(4)" value="审核通过" id="cancelbtnok"/>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="button" class="btn2" onclick="backTeamCheck(4)" name="cancelbtnback" value="退回" id="cancelbtnback"/>
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
					</c:if>
				</div>
			</div>
		</td>
	</tr>
		<div class="toolbar">
				<div class="left"></div>
				<div class="center">
				<input type="button" class="btn2" onclick="window.location.href='${base}${customer_team.controller}?c=${customer_team.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
				</div>
				<div class="right"></div>
		</div>
	<tr>
		<td></td>
	</tr>
</table>
</body>
</html>


