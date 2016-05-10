<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript">
    function saveTeamCheck(){
		var applicantFeedback;
		var checkId;
		var checkType;
        checkId=$("#promoteCheckId").val();
        checkType=$("#promoteCheckType").val();
        applicantFeedback = $("#applicantFeedback").val();
		var url = "${base}/customer/team/saveTeamCheck.json";
		$.ajax({
			url:url,
			data:{
	                id:$("#id").val(),
	                checkId: checkId,
	                rOrgId:$("#rOrgId").val(),
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
						    dialog : {msg:'<msg:message code="team.checksuccess"/>',type : 1,yes : function(){
					    	window.location.href='${base}${backLink}';
					    	top.layer.close(index);
					    }}	
						});
		        	    $("#promotebtnok").detach();
		        	    $("#promotebtnback").detach();
					     
					}else if(jsonData.mark=="false")
					{
						top.$.layer({
							shade : [0.5 , '#000' , true],
					  		offset: ['50px',''],
					  	 	title : '<msg:message code="info.prompt"/>',
						    dialog : {msg:'<msg:message code="team.checkerror"/>',type : 3}	
						});
					}
				}
			}
		});
	}
	function backTeamCheck(){
		var applicantFeedback;
		var checkId;
		var checkType;
        checkId=$("#promoteCheckId").val();
        checkType=$("#promoteCheckType").val();
        applicantFeedback = $("#applicantFeedback").val();
		var url = "${base}/customer/team/backTeamCheck.json";
		$.ajax({
			url:url,
			data:{
                 id:$("#id").val(),
                 orgId:$("#orgId").val(),
                 rOrgId:$("#rOrgId").val(),
                 applicationMember: $("#applicationMember").val(),
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
						    dialog : {msg:'<msg:message code="team.backsuccess"/>',type : 1,yes : function(){
					    	window.location.href='${base}${backLink}';
					    	top.layer.close(index);
					    }}	
						});
			        	$("#promotebtnok").detach();
			        	$("#promotebtnback").detach();
					    	
					}else if(jsonData.mark=="false")
					{
						top.$.layer({
							shade : [0.5 , '#000' , true],
					  		offset: ['50px',''],
					  	 	title : '<msg:message code="info.prompt"/>',
						    dialog : {msg:'<msg:message code="team.backerror"/>',type : 3}	
						});
					}
				}
			}
		});
	}
	
</script>
</head>
<body class="content-bg">

<table width="99%" align="center" border="0" cellpadding="0" cellspacing="0" >

	<tr>
		<td>
						<tr>
							<td width="100%">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
									<form:form method="post" action="" commandName="promoteOrganization">
									
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
										<img src="${ftpHttpUrl}${promoteOrganization.licenceImage}" height="40" width="100">	
											
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
								</form:form>
								<form:form method="post" action="" commandName="promoteTeam">
								<input type="hidden" name="turnCode" value="${customer_team_checklist.code}"/>
								<input type="hidden" name="coverParam" value="1"/>
								<input type="hidden" name="rOrgId" id="rOrgId" value="${promoteOrganization.checkId}"/>
								<input type="hidden" name="orgId" id="orgId" value="${promoteOrganization.id}"/>
								<input type="hidden" name="id" id="id" value="${promoteTeam.id}"/>
								<input type="hidden" name="applicationMember" id="applicationMember" value="${promoteTeam.applicationMember}"/>
								<input type="hidden" name="promoteCheckId" id="promoteCheckId" value="${promoteTeam.checkId}"/>
								<input type="hidden" name="promoteCheckType" id="promoteCheckType" value="${promoteTeam.checkType}"/>
							
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.name"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="name" maxlength='20' readonly='true'  class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.shortName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="shortName" maxlength='20' readonly='true'  class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicantFeedback"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%"   valign="middle" class="skinLayout_lable">
											<div style="float:left;"><form:textarea path="applicantFeedback" maxlength='100' cols="30" rows="4"  cssStyle="width: 250px; height: 85px;" class="valid-control"/></div>
											<div style="height:85px; line-height:85px; vertical-align:middle">
											</div>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											
										</td>
										<td width="35%"   valign="middle" class="skinLayout_lable">
											
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="team.applicationTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="35%" class="skinLayout_lable">
											<form:input path="applicationTime" maxlength='20' readonly='true'  class="valid-control"/>
										</td>
										<td width="15%" align="right" class="skinLayout_info">
											
										</td>
										<td width="35%" class="skinLayout_lable">
											
										
										</td>
									</tr>
									</form:form>
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
					
		</td>
	</tr>
		<div class="toolbar">
				<div class="left"></div>
				<div class="center">
				<input type="button" class="btn2" name="promotebtnok" onclick="saveTeamCheck()" value="通过" id="promotebtnok"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;						
				<input type="button" class="btn2" onclick="backTeamCheck()" name="promotebtnback" value="不通过" id="promotebtnback"/>
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


