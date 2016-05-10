<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript">
function saveOrganizationCheck(){
		var applicantFeedback;
		var checkId;
		var checkType;
        checkId=$("#cancelCheckId").val();
        checkType=$("#cancelCheckType").val();
        applicantFeedback = $("#applicantFeedback").val();
		var url = "${base}/customer/organization/saveOrganizationCheck.json";
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
						    dialog : {msg:'<msg:message code="memberOrganization.checksuccess"/>',type : 1,yes : function(){
					    	window.location.href='${base}${backLink}';
					    	top.layer.close(index);
					    }}	
						});
		        	    $("#cancelbtnok").detach();
		        	    $("#cancelbtnback").detach();
					     
					}else if(jsonData.mark=="false")
					{
						top.$.layer({
							shade : [0.5 , '#000' , true],
					  		offset: ['50px',''],
					  	 	title : '<msg:message code="info.prompt"/>',
						    dialog : {msg:'<msg:message code="memberOrganization.checkerror"/>',type : 3}	
						});
					}
				}
			}
		});
	}
	function backOrganizationCheck(){
	var applicantFeedback;
	var checkId;
	var checkType;
	checkId=$("#cancelCheckId").val();
	checkType=$("#cancelCheckType").val();
	applicantFeedback = $("#applicantFeedback").val();
		var url = "${base}/customer/organization/backOrganizationCheck.json";
		$.ajax({
			url:url,
			data:{
                 id:$("#id").val(),
                 checkId: checkId,
                 checkType: checkType,
                 applicationMember: $("#applicationMember").val(),
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
						    dialog : {msg:'<msg:message code="memberOrganization.backsuccess"/>',type : 1,yes : function(){
					    	window.location.href='${base}${backLink}';
					    	top.layer.close(index);
					    }}	
						});
			        	$("#cancelbtnok").detach();
			        	$("#cancelbtnback").detach();
					    	
					}else if(jsonData.mark=="false")
					{
						top.$.layer({
							shade : [0.5 , '#000' , true],
					  		offset: ['50px',''],
					  	 	title : '<msg:message code="info.prompt"/>',
						    dialog : {msg:'<msg:message code="memberOrganization.backerror"/>',type : 3}	
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

</script>
</head>
<body class="content-bg">
<table width="99%" align="center" border="0" cellpadding="0" cellspacing="0" >

	<tr>
		<td>
					<form:form method="post" action="" commandName="cancelMemberOrganization">
					<input type="hidden" name="id" id="id" value="${cancelMemberOrganization.id}"/>
					<input type="hidden" name="checkId" id="cancelCheckId" value="${cancelMemberOrganization.checkId}"/>
					<input type="hidden" name="checkType" id="cancelCheckType" value="${cancelMemberOrganization.checkType}"/>
					<input type="hidden" name="applicationMember" id="applicationMember" value="${cancelMemberOrganization.applicationMember}"/>
					<input type="hidden" name="turnCode" value="${customer_organization_checklist.code}"/>
					<input type="hidden" name="coverParam" value="1"/>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
						<tr>
							<td width="100%">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.name"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="name" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.shortName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="shortName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.licenceCode"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceCode" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.licenceImage"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<img src="${ftpHttpUrl}${cancelMemberOrganization.licenceImage}" height="40" width="100" onclick="changeBig('${ftpHttpUrl}${cancelMemberOrganization.licenceImage}')">	
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.licenceExpireTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="licenceExpireTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.representative"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="representative" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										</td>
							
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.applicationMemberName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="applicationMemberName" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
											
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.applicationTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="applicationTime" maxlength='20' readonly='true' onchange="this.value=$.trim(this.value)" class="valid-control"/>
										
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.applicantFeedback"/><msg:message code="system.common.sign.colon"/>
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
				<input type="button" class="btn2" name="cancelbtnok" onclick="saveOrganizationCheck()" value="通过" id="cancelbtnok"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;						
				<input type="button" class="btn2" onclick="backOrganizationCheck()" name="cancelbtnback" value="不通过" id="cancelbtnback"/>
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


