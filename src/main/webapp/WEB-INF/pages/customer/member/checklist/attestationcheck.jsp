<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${base}/common/js/layer/layer.js"></script>
<title>理财顾问审核</title>
<script type="text/javascript">
  function saveMemberCheck(i){
	  
		var applicantFeedback;
		var checkId;
		var checkType;
        checkId=$("#createCheckId").val();
        checkType=$("#createCheckType").val();
        applicantFeedback = $("#applicantFeedback").val();
		var url = "${base}/customer/member/saveMemberCheck.json";
		$.ajax({
			url:url,
			data:{
	                id:$("#id").val(),
	                checkId: checkId,
	                checkType: checkType,
	                applicantFeedback: applicantFeedback
	           },//参数
			type:'post',
			timeout:'60000',
			dataType:'json',
			success:function(jsonData,textStatus){
				if (textStatus == "success"){
					if(jsonData.mark=="true"){
						var index =top.$.layer({
							shade : [0.5 , '#000' , true],
					  		offset: ['50px',''],
					  	 	title : '<msg:message code="info.prompt"/>',
					  	 	close : function(index){
				  	 		top.layer.close(index);
					    	window.location.href='${base}${backLink}';	
					    	},
						    dialog : {msg:'<msg:message code="member.checksuccess"/>',type : 1,yes : function(){
					    	window.location.href='${base}${backLink}';
					    	top.layer.close(index);
					    }}	
						});
		        	    $("#createbtnok").detach();
		        	    $("#createbtnback").detach();
					     
					}else if(jsonData.mark=="false")
					{
						top.$.layer({
							shade : [0.5 , '#000' , true],
					  		offset: ['50px',''],
					  	 	title : '<msg:message code="info.prompt"/>',
						    dialog : {msg:'<msg:message code="member.checkerror"/>',type : 3}	
						});
					}
				}
			}
		});
	}
	function backMemberCheck(i){
	var applicantFeedback;
	var checkId;
	var checkType;
	checkId=$("#createCheckId").val();
	checkType=$("#createCheckType").val();
	applicantFeedback = $("#applicantFeedback").val();
		var url = "${base}/customer/member/backMemberCheck.json";
		$.ajax({
			url:url,
			data:{
                 id:$("#id").val(),
                 checkId: checkId,
                 checkType: checkType,
                 applicantFeedback: applicantFeedback
            },//参数
			type:'post',
			timeout:'60000',
			dataType:'json',
			success:function(jsonData,textStatus){
				if (textStatus == "success"){
					if(jsonData.mark=="true"){
						var index =top.$.layer({
							shade : [0.5 , '#000' , true],
					  		offset: ['50px',''],
					  	 	title : '<msg:message code="info.prompt"/>',
					  	 	close : function(index){
				  	 		top.layer.close(index);
					    	window.location.href='${base}${backLink}';	
					    	},
						    dialog : {msg:'<msg:message code="member.backsuccess"/>',type : 1,yes : function(){
					    	window.location.href='${base}${backLink}';
					    	top.layer.close(index);
					    }}	
						});
			        	$("#createbtnok").detach();
			        	$("#createbtnback").detach();
					    	
					}else if(jsonData.mark=="false")
					{
						top.$.layer({
							shade : [0.5 , '#000' , true],
					  		offset: ['50px',''],
					  	 	title : '<msg:message code="info.prompt"/>',
						    dialog : {msg:'<msg:message code="member.backerror"/>',type : 3}	
						});
					}
				}
			}
		});
	}
	function changeBig(ImageUrl){
		var i = top.$.layer({
			type: 1,
			title: false,
			closeBtn: [0,true],
			shadeClose: true,
			border : [5, 0.5, '#666', true],
			offset: ['20px','50%'],
			move: ['.ttee', true],
			moveOut:true, 
			area: ['auto','auto'],
			page: {
				html: '<img width="400" class="ttee" height="300" src="'+ImageUrl+'""/>'
			}
		});
	}
	$(document).ready(function(){
		<c:forEach var="item" items="${memberGender}">
			<c:if test="${item.key==createMember.gender}">
			 	$("#gender").val("${item.value}");
			</c:if>
		</c:forEach>
	})
	
</script>
</head>
<body class="content-bg">
<table width="99%" align="center" border="0" cellpadding="0" cellspacing="0" >
	<tr>
		<td>
					<form:form method="post" action="saveCreateMember" commandName="createMember">
					<input type="hidden" name="turnCode" value="${customer_member_checklist.code}"/>
					<input type="hidden" name="coverParam" value="1"/>
					<input type="hidden" name="id" id="id" value="${createMember.id}"/>
					<input type="hidden" name="checkId" id="createCheckId" value="${createMember.checkId}"/>
					<input type="hidden" name="checkType" id="createCheckType" value="${createMember.checkType}"/>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
						<tr>
							<td width="100%">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.name"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="name" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.nickName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="nickName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.birthday"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="birthday" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.iDCard"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="iDCard" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control" value="***************${fn:substring(createMember.iDCard, 14,20)}"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.gender"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<input id="gender" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.mobilePhone"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="mobilePhone" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control" value="********${fn:substring(createMember.mobilePhone, 7,15)}"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.province"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="provinceName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.city"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="cityName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.cardImage"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
										<img src="${ftpHttpUrl}${createMember.cardImage}" height="40" width="250" onclick="changeBig('${ftpHttpUrl}${createMember.cardImage}')">
											
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.changedTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="changedTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.bank"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
										<form:input path="bank" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
											
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.card"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="card" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="member.applicantFeedback"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%"   valign="middle" class="skinLayout_lable">
											<div style="float:left;"><form:textarea path="applicantFeedback" maxlength='100' cols="30" rows="4" onchange="this.value=$.trim(this.value)" cssStyle="width: 250px; height: 85px;" class="valid-control"/></div>
											<div style="height:85px; line-height:85px; vertical-align:middle">
											</div>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											
										</td>
										<td width="35%"   valign="middle" class="skinLayout_lable">
										
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
		</td>
	</tr>
		<div class="toolbar">
				<div class="left"></div>
				<div class="center">
				<input type="button" class="btn2" name="btnok" onclick="saveMemberCheck(1)" value="通过" id="createbtnok"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" class="btn2" onclick="backMemberCheck(1)" name="createbtnback" value="不通过" id="createbtnback"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
				</div>
				<div class="right"></div>
		</div>
	<tr>
		<td></td>
	</tr>
</table>
</body>
</html>


