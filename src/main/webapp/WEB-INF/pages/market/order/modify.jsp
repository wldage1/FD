<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
	$(document).ready(function(){
		
		//计算 预期收益率
		$("#investAmount").change(function(){
			if($("#investAmount").val()!=null && $("#investAmount").val()!=''){
				$.ajax({
					url:'${base}/market/order/countExpectProfitRatio.json?c=${order.c}&subProductID=${order.subProductID}&investAmount=' + $("#investAmount").val(),
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData){
						$("#profitRatio").val(jsonData.profitRatio);
					}
				});	
			}
		});
		
		//根据投资金额计算预期收益率
		$("#investAmount").change(function(){
			//判断产品是否为空
			if(this.value){
				countExpectProfitRatio();
			}
		});
		
		//到账金额  绑定到打款状态
		$("#payAmount").change(function(){
			makePayProgress();
		});
		
		//已分配份额  绑定到打款状态
		$("#share").change(function(){
			makePayProgress();
		});
		
		function makePayProgress(){
			var payAmount = (Number)($("#payAmount").val());
			var share = (Number)($("#share").val());
			if(payAmount == 0){
				$("#payProgress").val(0);
				//$("#payProgressName").text("<msg:message code='payProgress.notyet'/>");
			}else if(payAmount < share){
				$("#payProgress").val(1);
				//$("#payProgressName").text("<msg:message code='payProgress.part'/>");
			}else if(payAmount == share){
				$("#payProgress").val(2);
				//$("#payProgressName").text("<msg:message code='payProgress.all'/>");
			}else if(payAmount > share){
				$("#payProgress").val(3);
				//$("#payProgressName").text("<msg:message code='payProgress.over'/>");
			}
		}
		
		//验证
		var options = {
				event:'focus',
				ajax:{
					url:'${base}/market/order/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						//提交表单
						if('${order.tradeType}'=='2' || '${order.tradeType}'=='3'){
							if('${order.tradeStatus}'=='11' && '${order.payProgress}'=='2'){
								if( $("#tradeStatus").val()=='9'){
									$.layer({
									    shade: [0],
									    area: ['auto','auto'],
									    dialog: {
									        msg: '是否将订单至为失败订单？',
									        btns: 2,                    
									        type: 4,
									        btn: ['是','否'],
									        yes: function(){
									        	$("#tradeStatus").val(13);
									        	$("#order").append("<input type='hidden' name='breakReason' value='2'/>");
									        	$("form#order").submit();
									        }, 
									        no: function(){
									        	$("#order").append("<input type='hidden' name='breakReason' value='2'/>");
									        	$("form#order").submit();
									        }
									    }
									});
								}else if($("#tradeStatus").val()=='13'){
									$("#order").append("<input type='hidden' name='breakReason' value='2'/>");
						        	$("form#order").submit();
								}else{
									$("form#order").submit();
								}
							}else{
								$("form#order").submit();
							}				
						}else{
							$("form#order").submit();
						}
					}
				}
			};
		$("form #btn_save").valid(options);
		
		var contractNumber = $("#contractNumber").val();
		var contractPrefix = $("#proContractPrefix").val();
		$("#contractPrefix").val(contractPrefix);
		if(contractNumber!=''){
			$("#contractSuffix").val(contractNumber.substring(contractPrefix.length));
		}
	});
	
	//计算 预期收益率
	function countExpectProfitRatio(){
		$.ajax({
			url:'${base}/market/order/countExpectProfitRatio.json?c=${order.c}&subProductID=' + ${order.subProductID} + '&investAmount=' + $("#investAmount").val(),
			type:'post',
			timeout:'60000',
			dataType:'json',
			success:function(jsonData){
				$("#profitRatio").val(jsonData.profitRatio);
			}
		});	
	}
	
	function contractNum(){
		if($("#contractSuffix").val()==""){
			$("#contractNumber").val("");
		}else{
			$("#contractNumber").val(($("#contractPrefix").val()).concat($("#contractSuffix").val()));
		}
	}
</script>
</head>
<body class="skinMain">
<form:form method="post" id="order" action="saveorupdate" commandName="order">
<input type="hidden" name="turnCode" value="${market_order.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<form:hidden path="productID"/>
<form:hidden path="memberID"/>
<form:hidden path="providerID" />
<form:hidden path="subProductID" />
<form:hidden path="id"/>
<form:hidden path="c"/>
<form:hidden path="orgID" />
<form:hidden path="clientType"/>
<input type="hidden" name="prompt" value="orderNumber" />
<input type="hidden" name="orderNumber" value="${order.orderNumber}" />
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
					<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
							<tr>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="order.orderNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" colspan="3">${order.orderNumber}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.investAmount"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="investAmount" class="valid-control" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.payAmount"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"   ><form:input path="payAmount" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.product.shortName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"><input id="productNameTwo" value="${order.product.shortName}" disabled="disabled"/></td>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="product.org.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" width="30%"><input id="providerShortName" value="${order.provider.shortName}" disabled="disabled"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.member.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><input id="memberName" value="${order.member.name}" disabled="disabled"/></td>
								<td align="right" class="skinLayout_info"><msg:message code="order.teamID"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><input id="teamName" value="${order.team.name}" disabled="disabled"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.clientTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><c:forEach items="${clientTypeMap}" var="it"><c:if test="${order.clientType==it.key}">${it.value}</c:if></c:forEach></td>
								<c:if test="${order.clientType==1}">
									<td align="right" class="skinLayout_info"  ><msg:message code="order.iDCardTypeName"/><msg:message code="system.common.sign.colon"/></td>
									<td align="left" ><form:select path="iDCardType" cssStyle="width:150px;"><c:forEach items="${iDCardTypeMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
								</c:if>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.clientName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="clientName" class="valid-control"/></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.iDCard"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="iDCard" class="valid-control"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.tradeType"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:select path="tradeType" cssStyle="width:150px;"><c:forEach items="${tradeTypeMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.deadline"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><c:if test="${order.deadline==-1}"><msg:message code="order.deadline.unlimited"/></c:if><c:if test="${order.deadline!=-1}">${order.deadline}</c:if></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.profitRatio"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="profitRatio" id="profitRatio" disabled="disabled"/></td>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.orderTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="orderTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate valid-control" readonly="readonly"/><font color="red">&nbsp;&nbsp;*</font></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.share"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"  ><form:input path="share" class="valid-control" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.commissionPayTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="commissionPayTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate valid-control" readonly="readonly"/><font color="red">&nbsp;&nbsp;*</font></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.payProgress"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:select path="payProgress" cssStyle="width:150px;" class="valid-control"><c:forEach items="${payProgressMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select><font color="red">&nbsp;&nbsp;*</font></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.payTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="payTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate" readonly="readonly"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.contractNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left">
									<form:input path="proContractPrefix" class="valid-control" cssStyle="display:none;"/>
									<input id="contractPrefix" disabled="disabled"/>
									-
									<input id="contractSuffix" type="text" onchange="contractNum()">
									<form:input path="contractNumber" cssStyle="height: 0;width: 0;" class="valid-control"/>
								</td>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.tradeStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:select path="tradeStatus" cssStyle="width:150px;"><c:forEach items="${tradeStatusMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.pushStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:select path="pushStatus" cssStyle="width:150px;"><c:forEach items="${pushStatusMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.documentStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:select path="documentStatus" cssStyle="width:150px;"><c:forEach items="${documentStatusMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.pushShare"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"><form:input path="pushShare" id="pushShare"/></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.pushTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"><form:input path="pushTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate" readonly="readonly"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.docStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" <c:if test="${order.product.type!=2}">colspan="3" </c:if>><form:select path="docStatus"  cssStyle="width:150px;"><form:option value="0">未归集</form:option><form:option value="1">已归集</form:option></form:select></td>
								<c:if test="${order.product.type==2}">
									<td align="right" class="skinLayout_info" ><msg:message code="order.netValue"/><msg:message code="system.common.sign.colon"/></td>
									<td align="left" colspan="3" ><form:input path="netValue"/></td>
								</c:if>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.remark"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" colspan="3"><form:textarea path="remark"/></td>
							</tr>
							<tr height="40px"><td colspan="4">&nbsp;</td></tr>
						</table>
						<div class="toolbar">
							<div class="left"></div>
								<div class="center">
									<input type="button" class="btn2" id="btn_save" value="<msg:message code="button.save"/>" />
									<input type="button" class="btn2" onclick="location.href='${base}${market_order.controller}?c=${market_order.code}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
								</div>
							<div class="right"></div>
						</div>
					</td>
				</tr>
			</table>
	</form:form>
</html>