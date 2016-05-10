<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		<script type="text/javascript">
		$(document).ready(function(){
			<c:if test="${!empty customer_team_account}">
		    <security:authorize ifAnyGranted="${customer_team_account.code}">
			$("#accountDetail").click(function(){
				$.ajax({
   					url: '${base}${customer_team_account.controller}.json',
   					type:'post',
   					timeout:'60000',
   					dataType:'json',
   					data: {id:${member.id}, c:'${customer_team_account.code}'}, 
   					async:false,
   					cache:false,
   					success:function(jsonData){
   						if(jsonData.account){
   							top.$.layer({
   								area : ['auto','auto'],
   								dialog : {msg:'<msg:message code="member.account"/>' + jsonData.account,type : 9}	
   							});
   						}
   					}
   				});
			});
			</security:authorize>
		    </c:if>
		    
		    <c:if test="${!empty customer_team_idcard}">
		    <security:authorize ifAnyGranted="${customer_team_idcard.code}">
			$("#idCardDetail").click(function(){
				$.ajax({
   					url: '${base}${customer_team_idcard.controller}.json',
   					type:'post',
   					timeout:'60000',
   					dataType:'json',
   					data: {id:${member.id}, c:'${customer_team_idcard.code}'}, 
   					async:false,
   					cache:false,
   					success:function(jsonData){
   						if(jsonData.idcard){
   							top.$.layer({
   								area : ['auto','auto'],
   								dialog : {msg:'<msg:message code="member.iDCard"/>' + jsonData.idcard,type : 9}	
   							});
   						}
   					}
   				});
			});
			</security:authorize>
		    </c:if>
		    
			<c:if test="${!empty customer_team_mobilephone}">
		    <security:authorize ifAnyGranted="${customer_team_mobilephone.code}">
			$("#mobilePhoneDetail").click(function(){
				$.ajax({
   					url: '${base}${customer_team_mobilephone.controller}.json',
   					type:'post',
   					timeout:'60000',
   					dataType:'json',
   					data: {id:${member.id}, c:'${customer_team_mobilephone.code}'}, 
   					async:false,
   					cache:false,
   					success:function(jsonData){
   						if(jsonData.mobilePhone){
   							top.$.layer({
   								area : ['auto','auto'],
   								dialog : {msg:'<msg:message code="member.mobilePhone"/>' + jsonData.mobilePhone,type : 9}	
   							});
   						}
   					}
   				});
			});
			</security:authorize>
		    </c:if>
		    
		    <c:if test="${!empty customer_team_homephone}">
		    <security:authorize ifAnyGranted="${customer_team_homephone.code}">
			$("#homePhoneDetail").click(function(){
				$.ajax({
   					url: '${base}${customer_team_homephone.controller}.json',
   					type:'post',
   					timeout:'60000',
   					dataType:'json',
   					data: {id:${member.id}, c:'${customer_team_homephone.code}'}, 
   					async:false,
   					cache:false,
   					success:function(jsonData){
   						if(jsonData.homePhone){
   							top.$.layer({
   								area : ['auto','auto'],
   								dialog : {msg:'<msg:message code="member.homePhone"/>' + jsonData.homePhone,type : 9}	
   							});
   						}
   					}
   				});
			});
			</security:authorize>
		    </c:if>
		});
	</script>
	</head>
	<body class="skinMain">
	<input type="hidden" name="turnCode" value="${customer_team_teammember.code}"/>
	<input type="hidden" name="coverParam" value="1"/>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" class="skinMain">
			<tr>
				<td width="100%">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="skinLayout">
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.name"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.name}
							</td>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.account"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
							<c:if test="${fn:length(member.account) > 0}">
								*****${fn:substring(member.account, fn:indexOf(member.account, "@"), fn:length(member.account))}
								<c:if test="${!empty customer_team_account}">
				    		    <security:authorize ifAnyGranted="${customer_team_account.code}">
				    		    	<a href="javascript:void(0);" id="accountDetail"><msg:message code="button.view"/></a>
				    		    </security:authorize>
				    		    </c:if>
							</c:if>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.nickName"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.nickName}
							</td>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.registerTime"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.registerTime}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.type"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
							<c:forEach var="item" items="${memberType}">
							<c:if test="${item.key==member.type}">
							  ${item.value}
							</c:if>
							</c:forEach>
							</td>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.status"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
							<c:forEach var="item" items="${memberStatus}">
								<c:if test="${item.key==member.status}">
								  ${item.value}
								</c:if>
							</c:forEach>
							</td>
						</tr>
						
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.iDCard"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
							<c:if test="${fn:length(member.iDCard) > 0}">
								***************${fn:substring(member.iDCard, 14,20)}
								<c:if test="${!empty customer_team_idcard}">
				    		    <security:authorize ifAnyGranted="${customer_team_idcard.code}">
				    		    	<a href="javascript:void(0);" id="idCardDetail"><msg:message code="button.view"/></a>
				    		    </security:authorize>
				    		    </c:if>
							</c:if>
							</td>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.teamID"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.teamName}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.province"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.provinceName}
							</td>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.city"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.cityName}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.gender"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
							<c:forEach var="item" items="${memberGender}">
								<c:if test="${item.key==member.gender}">
								  ${item.value}
								</c:if>
							</c:forEach>
							</td>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.birthday"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.birthdayFormat}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.mobilePhone"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
							<c:if test="${fn:length(member.mobilePhone) > 0}">
								********${fn:substring(member.mobilePhone, 7,15)}
								<c:if test="${!empty customer_team_mobilephone}">
				    		    <security:authorize ifAnyGranted="${customer_team_mobilephone.code}">
				    		    	<a href="javascript:void(0);" id="mobilePhoneDetail"><msg:message code="button.view"/></a>
				    		    </security:authorize>
				    		    </c:if>
				    		</c:if>
							</td>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.homePhone"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
							<c:if test="${fn:length(member.homePhone) > 0}">
								********${fn:substring(member.homePhone, 7,20)}
								<c:if test="${!empty customer_team_homephone}">
				    		    <security:authorize ifAnyGranted="${customer_team_homephone.code}">
				    		    	<a href="javascript:void(0);" id="homePhoneDetail"><msg:message code="button.view"/></a>
				    		    </security:authorize>
				    		    </c:if>
				    		</c:if>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.email"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.email}
							</td>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.qQ"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.qQ}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.weiXin"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.weiXin}
							</td>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.weiBo"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.weiBo}
							</td>
						</tr><tr>
							<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="member.address"/><msg:message code="system.common.sign.colon"/></td>
							<td width="35%" class="skinLayout_lable">
								${member.address}
							</td>
							<td width="15%" align="right" class="skinLayout_info">
							</td>
							<td width="35%" class="skinLayout_lable">
								
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<div class="toolbar">
				<div class="left"></div>
				<div class="center">
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