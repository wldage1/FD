<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
	//弹出层标识
	var k;
	$(document).ready(function(){
		//订单ID
		var id='${order.id}';
		var orderNumber='${order.orderNumber}';
		var orderTradeStatus='${order.tradeStatus}';
		
		//额度确认
		$("#button1").click(function(){
			//判断订单状态  只有1/2/12状态可以操作
			if(orderTradeStatus!='1' && orderTradeStatus!='2' && orderTradeStatus!='12'){
				top.$.layer({
					area : ['auto','auto'],
					dialog : {msg: '此项操作只有订单销售状态为 [预约提交][份额确认][额度重新分配]可以使用！' ,type : 8}	
				});
				return false;
			}
			k = top.$.layer({
				type: 1,
				title: '<msg:message code="order.set.share"/>',
				shadeClose: true, 
				closeBtn: [0,true],
				border : [5, 0.5, '#666', true],
				offset: ['50px',''],
				move: ['.xubox_title', true],
				area: ['520px','auto'],
				page: {
					html: 
					'<table width="520" border="0" cellspacing="0" cellpadding="0">'+
					'<tr>'+
					'<td width="100%" class="skinLayout_info" colspan="2">'+
						'<table width="99%" align="center" border="0" height="200" cellspacing="1" cellpadding="0" class="skinLayout">'+
						'<tr>'+
						'<td align="right" width="50%">'+
							'<msg:message code="order.orderNumber" /><msg:message code="system.common.sign.colon"/>'+
						'</td>'+
						'<td align="left" width="50%">${order.orderNumber}</td>'+
						'</tr><tr>'+
						'<td align="right" width="50%">'+
							'<msg:message code="order.product.shortName" /><msg:message code="system.common.sign.colon"/>'+
						'</td>'+
						'<td align="left" width="50%"><c:out value="${order.product.shortName}" /></td>'+
						'</tr>'+
						'<tr>'+
							'<td align="right" width="50%">'+
								'<msg:message code="order.investAmount" /><msg:message code="system.common.sign.colon"/>'+
							'</td>'+
							'<td align="left" width="50%">${order.investAmount}</td>'+
						'</tr>'+
						'<tr>'+
						'<td align="right" width="50%">'+
							'<msg:message code="order.amountDistribute" /><msg:message code="system.common.sign.colon"/>'+
						'</td>'+
						'<td align="left" width="50%">'+
						'${order.share}'+
						'</td>'+
						'</tr>'+
						<c:if test="${order.product.type==2}">
							'<tr>'+
								'<td align="right" width="50%">'+
									'<msg:message code="order.netValue" /><msg:message code="system.common.sign.colon"/>'+
								'</td>'+
								'<td align="left" width="50%">${order.netValue}</td>'+
							'</tr>'+
						</c:if>
						'<tr>'+
						'<td align="right" width="50%">'+
							'<msg:message code="order.amountDistribute.distribute" /><msg:message code="system.common.sign.colon"/>'+
						'</td>'+
						'<td align="left" width="50%">'+
						'<input type="text" id="share" value="${order.investAmount}" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" onafterpaste="this.value=this.value.replace(/\\D/g,\'\')"/>'+
						'</td>'+
						'</tr>'+
						'</table>'+
						'</td></tr>'+
						'<tr height="40" bgcolor="#CCDCFF">'+
						'<td align="right" width="50%"><input type="button" class="btn2" onclick="updatepayShare()" value="<msg:message code="order.true.button" />"/>&nbsp;&nbsp;</td>'+
						'<td width="50%" >&nbsp;&nbsp;<input type="button" class="btn2" id="closeSta" value="<msg:message code="order.fail.button" />"/></td></tr>'+
						'</tr>'+
						'</table>'
					}
				});
		});
		 //额度确认按钮绑定
		 window.parent.updatepayShare = function(){
			var share=$("#share",window.parent.document).val();
			 $.ajax({
					url: '${base}/market/order/checkMinOrder.json',
					type:'post',
					timeout:'60000',
					dataType:'json',
					data: {id:id}, 
					async:false,
					cache:false,
					success:function(jsonData){
						if(jsonData.zt=='success'){
							if(jsonData.isOrno=='yes'){
								$.ajax({
									url: '${base}/market/order/setAllStatus.json',
									type:'post',
									timeout:'60000',
									dataType:'json',
									data: {id:id,tradeStatus:2,share:share}, 
									async:false,
									cache:false,
									success:function(jsonData){
										top.layer.close(k);
										if(jsonData.zt=='success'){
											window.location.reload();
											top.$.layer({
												area : ['auto','auto'],
												dialog : {msg: '<msg:message code="order.set.share.success"/>' ,type : 9}	
											});
										}else{
											top.$.layer({
												area : ['auto','auto'],
												dialog : {msg: '<msg:message code="order.set.share.error"/>' ,type : 8}	
											});
										}
									}
								});
							}else{
								top.$.layer({
									area : ['auto','auto'],
									dialog : {msg: '小额投资人数已超过限制！' ,type : 8}	
								});
							}
						}else{
							
						}
					}
				});
		 }
		//打款凭证确定
		$("#button3").click(function(){
			//判断订单状态  只有2/3状态可以操作
			if(orderTradeStatus!='2'){
				top.$.layer({
					area : ['auto','auto'],
					dialog : {msg: '此项操作只有订单销售状态为 [份额确认]可以使用！' ,type : 8}	
				});
				return false;
			}
			window.location.href="${base}${market_order_detail_fundConfirm.controller}?c=${market_order_detail_fundConfirm.code}&orderNumber=${order.orderNumber}&productID=${order.productID}&memberID=${order.memberID}&orderID="+id;
		});
<%--	 
		//当前页iframeID
		var iframeID = window.frameElement.id;
				//上传单证
		$("#button2").click(function(){
			//判断订单状态  只有2/4状态可以操作
			if(orderTradeStatus!='2' && orderTradeStatus!='4'){
				top.$.layer({
					area : ['auto','auto'],
					dialog : {msg: '此项操作只有订单销售状态为 [份额确认][资金到账]可以使用！' ,type : 8}	
				});
				return false;
			}
			var url='${base}${market_order_detail_uploadDocument.controller}?c=${market_order_detail_uploadDocument.code}&id='+id+'&iframeID='+iframeID;;
			k=top.$.layer({
				type: 2,
				title: "<msg:message code='orderAttachment.upload' />",
				fix: false,
				closeBtn: true,
				shadeClose: false,
				shade: [0.5,'#B0B1B2', true],
				closeBtn:[0,true],
				border : [5, 0.3, '#666', true],
				offset: ['100px',''],
				area: ['560px','365px'],
				iframe: {src: url},
				success: function(){
					//layer.msg('点击层外任意处，可关闭该iframe层', 2, 9);
					///window.location.href="${base}${market_order_detail.controller}?c=${market_order_detail.code}&id="+id;
				}
			});
		});
		//额度配给
		$("#button7").click(function(){
			k = top.$.layer({
				type: 1,
				title: '<msg:message code="order.set.pushShare"/>',
				shadeClose: true, 
				closeBtn: [0,true],
				border : [5, 0.5, '#666', true],
				offset: ['50px',''],
				move: ['.xubox_title', true],
				area: ['520px','auto'],
				page: {
					html: 
					'<table width="100%" border="0" cellspacing="0" cellpadding="0">'+
					'<tr>'+
					'<td width="100%" class="skinLayout_info" colspan="2">'+
						'<table width="99%" align="center" border="0" height="200" cellspacing="1" cellpadding="0" class="skinLayout">'+
						'<tr>'+
						'<td align="right" width="50%">'+
							'<msg:message code="order.orderNumber" /><msg:message code="system.common.sign.colon"/>'+
						'</td>'+
						'<td align="left" width="50%">${order.orderNumber}</td>'+
						'</tr><tr>'+
						'<td align="right" width="50%">'+
							'<msg:message code="product.name" /><msg:message code="system.common.sign.colon"/>'+
						'</td>'+
						'<td align="left" width="50%">${order.product.name}</td>'+
						'</tr>'+
						'<tr>'+
							'<td align="right" width="50%">'+
								'<msg:message code="order.investAmount" /><msg:message code="system.common.sign.colon"/>'+
							'</td>'+
							'<td align="left" width="50%">${order.investAmount}</td>'+
						'</tr>'+
						'<tr>'+
						'<td align="right" width="50%">'+
							'<msg:message code="order.pushShare" /><msg:message code="system.common.sign.colon"/>'+
						'</td>'+
						'<td align="left" width="50%">'+
						'<input type="text" id="pushShare" value="${order.pushShare}" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" onafterpaste="this.value=this.value.replace(/\\D/g,\'\')"/>'+
						'</td>'+
						'</tr>'+
						'</table>'+
						'</td></tr>'+
						'<tr height="40" bgcolor="#CCDCFF">'+
						'<td align="right" width="50%"><input type="button" class="btn2" onclick="updatePushShare()" value="<msg:message code="order.true.button" />"/>&nbsp;&nbsp;</td>'+
						'<td width="50%" >&nbsp;&nbsp;<input type="button" class="btn2" id="closeSta" value="<msg:message code="order.fail.button" />"/></td></tr>'+
						'</tr>'+
						'</table>'
					}
				});
		});
		//额度配给确认按钮绑定
		 window.parent.updatePushShare = function(){
			var pushShare=$("#pushShare",window.parent.document).val();
			$.ajax({
				url: '${base}/market/order/updatePushShare.json',
				type:'post',
				timeout:'60000',
				dataType:'json',
				data: {id:id,pushStatus:1,pushShare:pushShare,share:pushShare,orderNumber:'${order.orderNumber}'}, 
				async:false,
				cache:false,
				success:function(jsonData){
					top.layer.close(k);
					if(jsonData.zt=='success'){
						$("#pushShare").val(pushShare);	
						$("#pushStatusName").html('<msg:message code="order.pushShare.already" />');
						top.$.layer({
							area : ['auto','auto'],
							dialog : {msg: '<msg:message code="order.set.pushShare.success"/>' ,type : 9}	
						});
					}else{
						top.$.layer({
							area : ['auto','auto'],
							dialog : {msg: '<msg:message code="order.set.pushShare.error"/>' ,type : 8}	
						});
					}
				}
			});
		 }
		//银行流水确认
		$("#button6").click(function(){
			var url="${base}${market_order_detail_bankStreamConfirm.controller}?c=${market_order_detail_bankStreamConfirm.code}&id="+id+"&iframeID="+iframeID;;
			k=top.$.layer({
				type: 2,
				title: "银行流水确认",
				fix: false,
				closeBtn: true,
				shadeClose: false,
				shade: [0.5,'#B0B1B2', true],
				closeBtn:[0,true],
				border : [5, 0.3, '#666', true],
				offset: ['10px',''],
				area: ['950px','550px'],
				iframe: {src: url},
				success: function(){
					//layer.msg('点击层外任意处，可关闭该iframe层', 2, 9);
				}
			});
		});
--%>
		//成功订单
		$("#button4").click(function(){
			//申购订单  成功设置时 需要添加合同编号验证
			if('${order.contractNumber}'==null || '${order.contractNumber}'==''){
				top.$.layer({
					area : ['auto','auto'],
					dialog : {msg: '此订单的合同编号不可以为空，请到[单证管理]模块进行处理！' ,type : 8}	
				});
				return false;
			}
			//判断订单状态  只有4状态可以操作
			if(orderTradeStatus!='4'){
				top.$.layer({
					area : ['auto','auto'],
					dialog : {msg: '此项操作只有订单销售状态为 [资金到账 ]可以使用！' ,type : 8}	
				});
				return false;
			}
			$.setTradeStatus(7);
		});
		//失败订单
		$("#button5").click(function(){
			$.setTradeStatus(13);
		});
		//通用设置交易状态
		$.setTradeStatus=function(v){
			var dates = {id:id,tradeStatus:v};
			var msg;
			var msgSuccess;
			var msgError;
			if(v==4){
				msg="<msg:message code='order.4.tradeStatus.isornot'/>";
				msgSuccess="<msg:message code='order.4.tradeStatus.success'/>";
				msgError="<msg:message code='order.4.tradeStatus.error'/>";
			}else if(v==7){
				//申购订单不用判断 产品是否成立  只有认购用  
				if('${order.tradeType}'==1){
					if('${order.product.sellStatus}'!=5 && '${order.product.sellStatus}'!=6){
						top.$.layer({
							area : ['auto','auto'],
							dialog : {msg: "产品尚未成立！" ,type : 8 }
						});
						return false;
					}
				}
				msg="<msg:message code='order.7.tradeStatus.isornot'/>";
				msgSuccess="<msg:message code='order.7.tradeStatus.success'/>";
				msgError="<msg:message code='order.7.tradeStatus.error'/>";
			}else if(v==13){
				if('${order.tradeType}'=='2' || '${order.tradeType}'=='3'){
					if('${order.tradeStatus}'=='11' && '${order.payProgress}'=='2'){
						dates.breakReason=2;	
					}
				}
				msg="<msg:message code='order.13.tradeStatus.isornot'/>";
				msgSuccess="<msg:message code='order.13.tradeStatus.success'/>";
				msgError="<msg:message code='order.13.tradeStatus.error'/>";
			}
			var k=$.layer({
			    shade : false,
			    area : ['auto','auto'],
			    dialog : {
			        msg: msg,
			        btns : 2, 
			        type : 4,
			        btn : ["<msg:message code='order.true.button'/>","<msg:message code='order.fail.button'/>"],
			        yes : function(){
			        	$.ajax({
	    					url: '${base}/market/order/setAllStatus.json',
	    					type:'post',
	    					timeout:'60000',
	    					dataType:'json',
	    					data: dates, 
	    					async:false,
	    					cache:false,
	    					success:function(jsonData){
	    						layer.close(k);
	    						if(jsonData.zt=='success'){
	    							top.$.layer({
	    								area : ['auto','auto'],
	    								dialog : {msg: msgSuccess ,type : 9}	
	    							});
	    							window.location.href="${base}${market_order.controller}?c=${market_order.code}";
	    						}else{
	    							top.$.layer({
	    								area : ['auto','auto'],
	    								dialog : {msg: msgError ,type : 8}	
	    							});
	    						}
	    					}
	    				});
			        },
			        no : function(){
			            layer.close(k);
			        }
			    }
			});
		}
		//关闭层
		$("#closeSta",window.parent.document).live("click",function(){
			top.layer.close(k);
		});
	});
	
	function closeWin(){
		top.layer.close(k);
	}
</script>
</head>
<body class="skinMain">
<form:form method="post" id="order" action="#" commandName="order">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%" colspan="7">
					<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
						<tr>
							<td class="skinLayout_info" align="center">
								<c:if test="${!empty market_order_detail_amountDistribute}">
					  				<security:authorize ifAnyGranted="${market_order_detail_amountDistribute.code}">
					  					<input type="button" class="btn3" id="button1" value='${market_order_detail_amountDistribute.name}' />
				  					</security:authorize>
			  					</c:if>
		  					</td>
							<td class="skinLayout_info" align="center">
								<c:if test="${!empty market_order_detail_fundConfirm}">
					  				<security:authorize ifAnyGranted="${market_order_detail_fundConfirm.code}">
					  					<input type="button" class="btn3" id="button3" value="${market_order_detail_fundConfirm.name}" />
				  					</security:authorize>
			  					</c:if>
							</td>
<%--
							<td class="skinLayout_info" align="center">
								<c:if test="${!empty market_order_detail_uploadDocument}">
					  				<security:authorize ifAnyGranted="${market_order_detail_uploadDocument.code}">
					  					<input type="button" class="btn3" id="button2" value="${market_order_detail_uploadDocument.name}" />
				  					</security:authorize>
			  					</c:if>
							</td> 
							<td class="skinLayout_info" align="center">
								<c:if test="${!empty market_order_detail_pushShare}">
					  				<security:authorize ifAnyGranted="${market_order_detail_pushShare.code}">
					  					<input type="button" class="btn3" id="button7" value="${market_order_detail_pushShare.name}" />
				  					</security:authorize>
			  					</c:if>
 								<c:if test="${!empty market_order_detail_bankStreamConfirm}"> 
					  				<security:authorize ifAnyGranted="${market_order_detail_bankStreamConfirm.code}">
					  					<input type="button" class="btn3" id="button6" value="${market_order_detail_bankStreamConfirm.name}" />
				  					</security:authorize>
			  					</c:if>
							</td>
--%>
							<td class="skinLayout_info" align="center">
								<c:if test="${!empty market_order_detail_orderSuccess}">
					  				<security:authorize ifAnyGranted="${market_order_detail_orderSuccess.code}">
					  					<input type="button" class="btn3" id="button4" value="${market_order_detail_orderSuccess.name}" />
				  					</security:authorize>
			  					</c:if>
							</td>
							<td class="skinLayout_info" align="center">
								<c:if test="${!empty market_order_detail_orderfail}">
					  				<security:authorize ifAnyGranted="${market_order_detail_orderfail.code}">
					  					<input type="button" class="btn3" id="button5" value="${market_order_detail_orderfail.name}" />
				  					</security:authorize>
			  					</c:if>
							</td>
						</tr>
					</table>
					<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
							<tr>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="order.orderNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%">${order.orderNumber}</td>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="order.orgID.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%">${order.organization.name}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.investAmount"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${order.investAmount}</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.payAmount"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"  class=""  width="300px">${order.payAmount}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.product.shortName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="">${order.product.shortName}</td>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="product.org.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%">${order.provider.shortName}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.member.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${order.member.name}(${order.member.nickName})</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.teamID"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${order.team.name}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.clientName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${order.clientName}</td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.clientTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >
									<c:forEach items="${clientTypeMap}" var="it">
										<c:if test="${order.clientType==it.key}">${it.value}</c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.iDCardTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >
									<c:forEach items="${iDCardTypeMap}" var="it">
										<c:if test="${order.iDCardType==it.key}">${it.value}</c:if>
									</c:forEach>
								</td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.iDCard"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${order.iDCard}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.tradeType"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >
									<c:forEach items="${tradeTypeMap}" var="it">
										<c:if test="${order.tradeType==it.key}">${it.value}</c:if>
									</c:forEach>
								</td>
								<td align="right" class="skinLayout_info">产品期限
								<c:choose>
									<c:when test="${order.product.deadlineDataType == 2}">(日)</c:when>
									<c:otherwise>(月)</c:otherwise>
								</c:choose>
								<msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">
									<c:choose>
										<c:when test="${order.deadline == -1}"><msg:message code="order.deadline.unlimited"/></c:when>
										<c:otherwise>${order.deadline}</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.profitRatio"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${order.profitRatio}</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.orderTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${order.orderTime}</td>
 							</tr>
<!--						<tr>	
								<td align="right" class="skinLayout_info"><msg:message code="order.payStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${order.payStatusName}</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.commissionPercent"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"  class="" width="100px">rrrr</td> 
 							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.expectCommission"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"  class=""  width="300px">rrrr</td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.commission"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class=""  width="300px">rrrr</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.salesTax"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="">rrrr</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.constructionTax"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">rrrr</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.educationalSurtax"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">rrrr</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.localEducationalCost"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">rrrr</td>
							</tr> 
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.personalIncomeTax"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">rrrr</td>
							</tr>-->
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.contractNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${order.contractNumber}</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.commissionPayTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px">${order.commissionPayTime}</td>
<%-- 								<td align="right" class="skinLayout_info" width="100px"><msg:message code="order.payMoneyStopTime"/><msg:message code="system.common.sign.colon"/></td> --%>
<%-- 								<td align="left" class="" width="100px">${order.payMoneyStopTime}</td> --%>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info">
									<c:if test="${order.product.type==2}">
										<msg:message code="order.share"/>
									</c:if>
									<c:if test="${order.product.type!=2}">
										<msg:message code="order.amountDistribute"/>
									</c:if>
									<msg:message code="system.common.sign.colon"/></td>
								<td align="left"  class="" width="100px" id="orderShare">${order.share}</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.payTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class=""  width="300px">${order.payTime}</td>
							</tr>
<!-- 							<tr> -->
<%-- 								<td align="right" class="skinLayout_info"><msg:message code="order.assignedShare"/><msg:message code="system.common.sign.colon"/></td> --%>
<%-- 								<td align="left" class="" width="100px">${order.assignedShare}</td> --%>
<!-- 							</tr> -->
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.payProgress"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px" id="payProgressName">
									<c:forEach items="${payProgressMap}" var="it">
										<c:if test="${order.payProgress==it.key}">${it.value}</c:if>
									</c:forEach>
								</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.tradeStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px" id="tradeStatusName">
									<c:forEach items="${tradeStatusMap}" var="it">
										<c:if test="${order.tradeStatus==it.key}">${it.value}</c:if>
									</c:forEach>
								</td>
							</tr>
							<c:if test="${order.tradeType == 1}">
					 		<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.pushShare"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><fmt:formatNumber  value="${order.pushShare}" type="number" /></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.pushTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${order.pushTime}</td>
							</tr>
							</c:if>
							<c:if test="${order.product.type==2}">
								<tr>
									<td align="right" class="skinLayout_info"><msg:message code="order.netValue"/><msg:message code="system.common.sign.colon"/></td>
									<td align="left" class="" colspan="1">${order.netValue}</td>
									<c:if test="${order.tradeType == 1}">
									<td align="right" class="skinLayout_info" ><msg:message code="order.pushStatus"/><msg:message code="system.common.sign.colon"/></td>
									<td align="left" >
										<c:forEach items="${pushStatusMap}" var="it">
											<c:if test="${order.pushStatus==it.key}">${it.value}</c:if>
										</c:forEach>
									</td>
									</c:if>
								</tr>
							</c:if>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.docStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="100px" id="docStatusName">
									<c:forEach items="${docStatusMap}" var="it">
										<c:if test="${order.docStatus==it.key}">${it.value}</c:if>
									</c:forEach>
								</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.remark"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" colspan="" width="100px">${order.remark}</td>
							</tr>
							<tr height="40px;"><td colspan="4">&nbsp;</td></tr>
						</table>
						<div class="toolbar">
							<div class="left"></div>
								<div class="center">
									<input type="button" class="btn2" onclick="location.href='${base}${market_order.controller}?c=${market_order.code}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
								</div>
							<div class="right"></div>
						</div>
					</td>
				</tr>
			</table>
	</form:form>
</html>