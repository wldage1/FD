<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
	var k;
$(document).ready(function(){
	//订单ID
	var id= ${redeemOrder.id};
	//产品类型
	var productType=2;
	//订单销售状态
	var orderTradeStatus='${redeemOrder.tradeStatus}';
	//额度确认
	$("#button1").click(function(){
		//判断订单状态  只有1/2状态可以操作
		if(orderTradeStatus!='1' && orderTradeStatus!='2'){
			top.$.layer({
				area : ['auto','auto'],
				dialog : {msg: '此项操作只有订单销售状态为 [预约提交][份额确认]可以使用！' ,type : 8}	
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
				'<table width="518" border="0" cellspacing="0" cellpadding="0">'+
					'<tr>'+
						'<td colspan="2" class="skinLayout_info">'+
								'<table width="100%" border="0" height="200" cellspacing="1" cellpadding="0"  class="skinLayout">'+
								'<tr>'+
								'<td align="right" width="50%">'+
									'<msg:message code="order.orderNumber" /><msg:message code="system.common.sign.colon"/>'+
								'</td>'+
								'<td align="left" width="50%">${redeemOrder.orderNumber}</td>'+
								'</tr><tr>'+
								'<td align="right" width="50%">'+
									'<msg:message code="order.product.shortName" /><msg:message code="system.common.sign.colon"/>'+
								'</td>'+
								'<td align="left" width="50%"><c:out value="${redeemOrder.product.shortName}" /></td>'+
								'</tr>'+
								'<tr>'+
									'<td align="right" width="50%">'+
										'<msg:message code="redeemorder.shares" /><msg:message code="system.common.sign.colon"/>'+
									'</td>'+
									'<td align="left" width="50%">${redeemOrder.share}</td>'+
								'</tr>'+
								'<tr>'+
									'<td align="right" width="50%">'+
										'<msg:message code="order.netValue" /><msg:message code="system.common.sign.colon"/>'+
									'</td>'+
									'<td align="left" width="50%">${redeemOrder.netValue}</td>'+
								'</tr>'+
								'<tr>'+
									'<td align="right" width="50%">'+
										'<msg:message code="redeemorder.affirm.share" /><msg:message code="system.common.sign.colon"/>'+
									'</td>'+
									'<td align="left" width="50%">'+
									'<input type="text" id="share"  value="${redeemOrder.share}"/>'+
									'</td>'+
								'</tr>'+
							'</table>'+
						'</td>'+
					'</tr>'+
					'<tr height="40" bgcolor="#CCDCFF">'+
	    				'<td align="right" width="50%"><input type="button" class="btn2" id="updatepayShare" onclick="updatepayShare()" value="<msg:message code="order.true.button" />"/>&nbsp;&nbsp;</td>'+
	    				'<td width="50%" >&nbsp;&nbsp;<input type="button" class="btn2" id="closeSta" value="<msg:message code="order.fail.button" />"/></td>'+
					'</tr>'+
				'</table>'
				}
			});
	});
		//额度确认确认按钮绑定
		 window.parent.updatepayShare=function(){
			 var share=$("#share",window.parent.document).val();
			 $.ajax({
				url: '${base}/market/order/updateShare.json',
				type:'post',
				timeout:'60000',
				dataType:'json',
				data: {id:id,tradeStatus:2,share:share}, 
				async:false,
				cache:false,
				success:function(jsonData){
					top.layer.close(k);
					if(jsonData.status=='success'){
						$("#orderShare").html(share);	
						$("#amount").html(share * ${redeemOrder.netValue});
						$("#tradeStatusName").html('<msg:message code="order.limit.confirm.button" />');
						window.location.href="${base}${market_order_redeemDetail.controller}?c=${market_order_redeemDetail.code}&id="+id;
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
		}
	
	var iframeID = window.frameElement.id;
	//上传单证
	$("#button2").click(function(){
		//判断订单状态  只有2状态可以操作
		if(orderTradeStatus!='2'){
			top.$.layer({
				area : ['auto','auto'],
				dialog : {msg: '此项操作只有订单销售状态为 [份额确认]可以使用！' ,type : 8}	
			});
			return false;
		}
		var url='${base}${market_order_redeemDetail_redeemUploadDocument.controller}?c=${market_order_redeemDetail_redeemUploadDocument.code}&id='+id+'&iframeID='+iframeID;
		k=top.$.layer({
			type: 2,
			title: "<msg:message code='orderAttachment.upload' />",
			fix: false,
			closeBtn: true,
			shadeClose: true,
			shade: [0.5,'#B0B1B2', true],
			closeBtn:[0,true],
			border : [5, 0.3, '#666', true],
			offset: ['100px',''],
			area: ['560px','300px'],
			iframe: {src: url},
			success: function(){
				//layer.msg('点击层外任意处，可关闭该iframe层', 2, 9);
			}
		});
	});
	//失败订单
	$("#button3").click(function(){
		$.setTradeStatus(10);
	});
	//成功订单
	$("#button4").click(function(){
		//判断订单状态  只有3状态可以操作
		if(orderTradeStatus!='3'){
			top.$.layer({
				area : ['auto','auto'],
				dialog : {msg: '此项操作只有订单销售状态为 [合同归集]可以使用！' ,type : 8}	
			});
			return false;
		}
		$.setTradeStatus(9);
	});
	
	//通用设置交易状态
	$.setTradeStatus=function(v){
		var url;
		var msg;
		var msgSuccess;
		var msgError;
		if(v==9){
			url="${base}/market/order/redeemSuccess.json";
			msg="<msg:message code='order.7.tradeStatus.isornot'/>";
			msgSuccess="<msg:message code='order.7.tradeStatus.success'/>";
			msgError="<msg:message code='order.7.tradeStatus.error'/>";
		}else if(v==10){
		    url ="${base}/market/order/redeemFailed.json";
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
    					url: url,
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					data: {id:id,tradeStatus:v}, 
    					async:false,
    					cache:false,
    					success:function(jsonData){
    						layer.close(k);
    						if(jsonData.status=='success'){
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg: msgSuccess ,type : 9}	
    							});
    							//top.document.getElementById('content_1501').contentWindow.tab_order(3);
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
<form:form method="post" id="redeemOrder" action="#">
<input type="hidden" id="holdingProductID" name="holdingProductID" value="${redeemOrder.holdingProductID}" />
<input type="hidden" id="productID" name="productID" value="${redeemOrder.productID}" />
<input type="hidden" id="contractNumber" name="contractNumber" value="${redeemOrder.contractNumber}" />
<input type="hidden" id="orgID" name="orgID" value="${redeemOrder.orgID }" />
<input type="hidden" id="subProductID" name="subProductID" value="${redeemOrder.subProductID }" />
<input type="hidden" id="teamID" name="teamID" value="${redeemOrder.teamID }" />
<input type="hidden" id="memberID" name="memberID" value="${redeemOrder.memberID}" />
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
					<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
						<tr>
							<td class="skinLayout_info" align="center">
								<c:if test="${!empty market_order_redeemDetail_redeemAmountDistribute}">
					  				<security:authorize ifAnyGranted="${market_order_redeemDetail_redeemAmountDistribute.code}">
					  					<input type="button" class="btn3" id="button1" value='${market_order_redeemDetail_redeemAmountDistribute.name}' />
				  					</security:authorize>
			  					</c:if>
		  					</td>
							<td class="skinLayout_info" align="center">
								<c:if test="${!empty market_order_redeemDetail_redeemUploadDocument}">
					  				<security:authorize ifAnyGranted="${market_order_redeemDetail_redeemUploadDocument.code}">
					  					<input type="button" class="btn3" id="button2" value="${market_order_redeemDetail_redeemUploadDocument.name}" />
				  					</security:authorize>
			  					</c:if>
							</td>
							<td class="skinLayout_info" align="center">
								<c:if test="${!empty market_order_redeemDetail_redeemOrderSuccess}">
					  				<security:authorize ifAnyGranted="${market_order_redeemDetail_redeemOrderSuccess.code}">
					  					<input type="button" class="btn3" id="button4" value="${market_order_redeemDetail_redeemOrderSuccess.name}" />
				  					</security:authorize>
			  					</c:if>
							</td>
							<td class="skinLayout_info" align="center">
								<c:if test="${!empty market_order_redeemDetail_redeemorderfail}">
					  				<security:authorize ifAnyGranted="${market_order_redeemDetail_redeemorderfail.code}">
					  					<input type="button" class="btn3" id="button3" value="${market_order_redeemDetail_redeemorderfail.name}" />
				  					</security:authorize>
			  					</c:if>
							</td>
						</tr>
					</table>
					<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
							<tr>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="order.orderNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%">${redeemOrder.orderNumber}</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.contractNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="">${redeemOrder.contractNumber}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.product.shortName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="">${redeemOrder.product.shortName }</td>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="product.org.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%">${redeemOrder.providerName }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.member.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${redeemOrder.memberName}(${redeemOrder.member.nickName})</td>
								<td align="right" class="skinLayout_info"><msg:message code="order.teamID"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${redeemOrder.teamName }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.clientName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${redeemOrder.clientName}</td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.clientTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${redeemOrder.clientTypeName}</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.iDCardTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${redeemOrder.iDCardTypeName}</td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.iDCard"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${redeemOrder.iDCard }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.netValue"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="">${redeemOrder.netValue }</td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.documentStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${redeemOrder.documentStatusName }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="redeemorder.shares"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" id="orderShare">${redeemOrder.share }</td>
								<td align="right" class="skinLayout_info" ><msg:message code="redeemorder.tradeTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" >${redeemOrder.tradeTime }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  ><msg:message code="redeemorder.amount"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class=""  id="amount">${redeemOrder.amount }</td>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.tradeStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" id="tradeStatusName">${redeemOrder.tradeStatusName }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="redeemorder.openTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%" colspan="3">${redeemOrder.openTime }</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.remark"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" colspan="3">${redeemOrder.remark }</td>
							</tr>
						</table>
						<div class="toolbar">
							<div class="left"></div>
								<div class="center">
									<input type="button" class="btn2" onclick="history.go(-1);" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
								</div>
							<div class="right"></div>
						</div>
					</td>
				</tr>
			</table>
	</form:form>
</html>