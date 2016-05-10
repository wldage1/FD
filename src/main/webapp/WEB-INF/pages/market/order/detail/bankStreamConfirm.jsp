<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" type="text/css" href="${base}/common/skin/css/productTab.css" />
<script type="text/javascript">
var iframeID='${iframeID}';
	$(document).ready(function() {
		 jQuery("#productList").jqGrid({
		        url: '${base}/market/order/detail/fundConfirm/getOrderAcountAffirmDetailGrid.json',
		        datatype: 'json',
		        colNames: [
		                "<msg:message code='order.orderNumber'/>",
		       	       // "<msg:message code='order.contractNumber'/>",
		       	       // "<msg:message code='order.product.shortName'/>", 
		       	        "<msg:message code='order.clientName'/>",
		       	        "<msg:message code='order.proof.proofTime'/>",
		       	        "<msg:message code='order.proof.amount'/>",
		      	    	"<msg:message code='payAmount.confirm'/>",
		      	    	"<msg:message code='payTime.confirm'/>",
		      	    	"<msg:message code='operate.type'/>",
		      	    	"<msg:message code='operate.step'/>"
		       	        ],
		               colModel: [
		                {name: 'order.orderNumber',index: 'orderNumber',width:'14%',align:'center',hidden: false,sortable: false}, 
		                //{name: 'order.contractNumber',index: 'order.contractNumber',width:'10%',align:'center',hidden: false,sortable: false},
		               // {name: 'order.product.shortName',index: 'order.product.shortName',width:'10%',align:'center',hidden: false,sortable: false},
		       			{name: 'order.clientName',index: 'order.clientName',width:'10%',align:'center',hidden: false,sortable:false},
		       			{name: 'proofTime',index: 'proofTime',width:'16%',align:'center',hidden: false,sortable: false},
		       			{name: 'proofAmount',index: 'proofAmount',width:'10%',align:'center',hidden: false,sortable: false},
		       			{name: 'affirmProofAmount',index: 'affirmProofAmount',width:'14%',align:'center',hidden: false,sortable: false},
		       			{name: 'affirmProofTime',index: 'affirmProofTime',width:'16%',align:'center',hidden: false,sortable: false},
		       			{name: 'type',index: 'type',width:'10%',align:'center',hidden: false,sortable: false,formatter:typeChange},
		       			{name: 'operateType',index: 'operateType',width:'10%',align:'center',hidden: false,sortable: false,formatter:operateTypeChange}
		 				],
		        mtype:"POST",
		        postData:{c:"${market_order_fundConfirm.code}",orderNumber:'${order.orderNumber}'},
		        rowNum:1000,     
		       // page:"${order.page}",
		       // rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
		       // pager: '#pageredOne',
		        height:	'auto',
		        autowidth: true,
		        viewrecords: true,
		        multiselect: false,
		        jsonReader: {
		        	repeatitems: false
		        },
		    	loadComplete:function(){
		    		
		    	},
		        caption:'<msg:message code='pay.confirm.list'/>',
		        toolbar: [true,"top"]
		    });
		 
		 jQuery("#productListTwo").jqGrid({
		        url: '${base}/market/order/detail/fundConfirmGrid.json',
		        datatype: 'json',
		        colNames: [
		                "<msg:message code='order.orderNumber'/>",
		       	        "<msg:message code='order.contractNumber'/>",
		       	        "<msg:message code='order.member.name'/>", 
		       	  	    "<msg:message code='order.orgID.shortName'/>",
		       	        "<msg:message code='order.clientName'/>",
		       	        "<msg:message code='order.iDCard'/>",
		       	        "<msg:message code='order.orderAmount'/>",
		       	        "<msg:message code='order.amountDistribute'/>",
		      	   		"<msg:message code='order.payDataTime'/>",
		       	        "<msg:message code='order.payAmount'/>",
		       	        "<msg:message code='order.proof.proofTime'/>",
		       	        "<msg:message code='order.proof.amount'/>",
		      	    	"<msg:message code='order.proof.files'/>",
		      	    	"<msg:message code='order.proof.status'/>"
		       	        ],
		               colModel: [
		                {name: 'order.orderNumber',index: 'orderNumber',width:'10%',align:'center',hidden: false,sortable: false}, 
		                {name: 'order.contractNumber',index: 'order.contractNumber',width:'8%',align:'center',hidden: false,sortable: false},
		                {name: 'order.member.name',index: 'order.member.name',width:'10%',align:'center',hidden: false,sortable: false},
		                {name: 'order.organization.shortName',index: 'order.organization.shortName',width:'10%',align:'center',hidden: false,sortable: false},
		       			{name: 'order.clientName',index: 'order.clientName',width:'7%',align:'center',hidden: false,sortable:false},
		       			{name: 'order.iDCard',index: 'order.iDCard',width:'7%',align:'center',hidden: false,sortable:false},
		       			{name: 'order.investAmount',index: 'order.investAmount',width:'10%',align:'center',hidden: false,sortable:false},
		       			{name: 'order.share',index: 'order.share',width:'10%',align:'center',hidden: false,sortable:false},
		       			{name: 'order.payTime',index: 'order.payTime',width:'10%',align:'center',hidden: false,sortable: false},
		       			{name: 'order.payAmount',index: 'order.payAmount',width:'10%',align:'center',hidden: false,sortable: false},
		       			{name: 'proofTime',index: 'proofTime',width:'6%',align:'center',hidden: false,sortable: false},
		       			{name: 'proofAmount',index: 'proofAmount',width:'7%',align:'center',hidden: false,sortable: false},
		       			{name: 'proofFileUrl',index: 'proofFileUrl',width:'6%',align:'center',hidden: false,sortable: false,formatter:fileChange},
		       			{name: 'proofStatus',index: 'proofStatus',width:'6%',align:'center',hidden: false,sortable: false,formatter:statusChange}
		 				],
		        mtype:"POST",
		        postData:{c:"${market_order_detail_fundConfirm.code}",orderNumber:'${order.orderNumber}'},
		        rowNum:1000,     
		       // page:"${orderProof.page}",
		       // rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
		       // pager: '#pageredTwo',
		        height:	'auto',
		        autowidth: true,
		        viewrecords: true,
		        multiselect: false,
		        jsonReader: {
		        	repeatitems: false
		        },
		    	loadComplete:function(){
		    		
		    	},
		        caption:'<msg:message code="order.proof.list"/>',
		        toolbar: [true,"top"]
		    });
		
	});
	
	//金额确认
	$("#${market_order_detail_bankStreamConfirm_addto.indexCode}").live("click",function(){
		if($("#affirmProofAmount").val()==null || $("#affirmProofAmount").val()=='' || $("#affirmProofAmount").val()==0){
			top.$.layer({
				area : ['auto','auto'],
				dialog : {msg:'<msg:message code="payAmount.notNull"/>',type : 9}	
			});
			return false;
		}else if($("#affirmProofTime").val()==null || $("#affirmProofTime").val()==''){
			top.$.layer({
				area : ['auto','auto'],
				dialog : {msg:'<msg:message code="payTime.notNull"/>',type : 9}	
			});
			return false;
		}else{
			var k=$.layer({
			    shade : false,
			    area : ['auto','auto'],
			    dialog : {
			        msg:'<msg:message code="handle.confirm"/>',
			        btns : 2, 
			        type : 4,
			        btn : ['<msg:message code="order.true.button"/>','<msg:message code="order.fail.button"/>'],
			        yes : function(){
			        	$.ajax({
	    					url: '${base}${market_order_detail_bankStreamConfirm_addto.controller}.json',
	    					type:'post',
	    					timeout:'60000',
	    					dataType:'json',
	    					data: {type:0,operateType:1,orderNumber:'${order.orderNumber}',affirmProofAmount:$("#affirmProofAmount").val(),affirmProofTime:$("#affirmProofTime").val(),c:'${market_order_detail_bankStreamConfirm_addto.code}'}, 
	    					async:false,
	    					cache:false,
	    					success:function(jsonData){
	    						layer.close(k);
	    						if(jsonData.zt=='success'){
	    							closeWin();
	    							top.$.layer({
	    								area : ['auto','auto'],
	    								dialog : {msg:'<msg:message code="amount.set.success"/>',type : 9}	
	    							});
	    							top.document.getElementById(iframeID).contentWindow.location.reload(true);
	    						}else{
	    							top.$.layer({
	    								area : ['auto','auto'],
	    								dialog : {msg:'<msg:message code="amount.set.fail"/>',type : 8}	
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
	
	});
	
	//复核
	$("#${market_order_detail_bankStreamConfirm_recheck.indexCode}").live("click",function(){
		var k=$.layer({
		    shade : false,
		    area : ['auto','auto'],
		    dialog : {
		        msg:'<msg:message code='recheck.confirm'/>',
		        btns : 2, 
		        type : 4,
		        btn : ['<msg:message code='order.true.button'/>','<msg:message code='order.fail.button'/>'],
		        yes : function(){
		        	$.ajax({
    					url: '${base}${market_order_detail_bankStreamConfirm_recheck.controller}.json',
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					data: {type:0,operateType:2,orderNumber:'${order.orderNumber}',affirmProofAmount:$("#affirmProofAmount").val(),affirmProofTime:$("#affirmProofTime").val(),c:'${market_order_detail_bankStreamConfirm_recheck.code}'}, 
    					async:false,
    					cache:false,
    					success:function(jsonData){
    						layer.close(k);
    						if(jsonData.zt=='success'){
    							closeWin();
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code='recheck.success'/>',type : 9}	
    							});
    						}else{
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code='recheck.fail'/>',type : 8}	
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
	});
	
	//修改
	$("#${market_order_detail_bankStreamConfirm_modifyTwo.indexCode}").live("click",function(){
		if($("#affirmProofAmount").val()==null || $("#affirmProofAmount").val()=='' || $("#affirmProofAmount").val()==0){
			top.$.layer({
				area : ['auto','auto'],
				dialog : {msg:'<msg:message code='payAmount.notNull'/>',type : 9}	
			});
			return false;
		}
		var k=$.layer({
		    shade : false,
		    area : ['auto','auto'],
		    dialog : {
		        msg:'<msg:message code='modify.confirm'/>',
		        btns : 2, 
		        type : 4,
		        btn : ['<msg:message code='order.true.button'/>','<msg:message code='order.fail.button'/>'],
		        yes : function(){
		        	$.ajax({
    					url: '${base}${market_order_detail_bankStreamConfirm_modifyTwo.controller}.json',
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					data: {type:0,operateType:3,orderNumber:'${order.orderNumber}',affirmProofAmount:$("#affirmProofAmount").val(),affirmProofTime:$("#affirmProofTime").val(),c:'${market_order_detail_bankStreamConfirm_modifyTwo.code}'}, 
    					async:false,
    					cache:false,
    					success:function(jsonData){
    						layer.close(k);
    						if(jsonData.zt=='success'){
    							closeWin();
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code='modify.success'/>',type : 9}	
    							});
    							top.document.getElementById(iframeID).contentWindow.location.reload(true);
    						}else{
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code='modify.fail'/>',type : 8}	
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
	});
	
	//文件名初始化
	function fileChange(cellvalue, options, rowObject){
		var str="";
		if(cellvalue!=null && cellvalue!=''){
			str="<a href='javascript:void(0);' style='color:blue;' onclick='changeBig(\"${ftpHttpUrl}"+cellvalue+"\");'>";
			str += cellvalue.substring(cellvalue.lastIndexOf("/")+1,cellvalue.length);
			str += "</a>";
		}else{
			str="无";
		}
		return str;
	}
	
	//处理状态 初始化
	function statusChange(cellvalue, options, rowObject){
		var content="";
		<c:forEach items="${proofStatusMap}" var="it">
			if(cellvalue==${it.key}){
				return '${it.value}';
			}
		</c:forEach>
	}
	
	
	function typeChange(cellvalue, options, rowObject){
		var content="";
		<c:forEach items="${typeMap}" var="it">
			if(cellvalue==${it.key}){
				return '${it.value}';
			}
		</c:forEach>
	}
	
	function operateTypeChange(cellvalue, options, rowObject){
		var content="";
		<c:forEach items="${operateTypeMap}" var="it">
			if(cellvalue==${it.key}){
				return '${it.value}';
			}
		</c:forEach>
	}
	
	function closeWin(){
	 	top.document.getElementById(iframeID).contentWindow.closeWin();
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
				html: '<img width="950" class="ttee" height="500" src="'+ImageUrl+'""/>'
			}
		});
	}
</script>

</head>
<body class="skinMain">
<form id="orderAttachment" name="orderAttachment" method="post">
<table width="98%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="50%" class="skinLayout_info">
						<table border="0" cellspacing="1" cellpadding="0" width="100%" class="skinLayout">
							<tr>
								<td align="right" width="40%"><msg:message code='order.orderNumber' /><msg:message code='system.common.sign.colon'/></td>
								<td width="60%">${order.orderNumber}</td>
							</tr>
							<tr>
								<td align="right"><msg:message code='order.contractNumber' /><msg:message code='system.common.sign.colon'/></td>
								<td>${order.contractNumber}</td>
							</tr>
							<tr>
								<td align="right"><msg:message code='product.name' /><msg:message code='system.common.sign.colon'/></td>
								<td>${order.product.name}</td>
							</tr>
							<tr>
								<td align="right"><msg:message code='order.amountDistribute.distribute' /><msg:message code='system.common.sign.colon'/></td>
								<td>${order.share}</td>
							</tr>
							<tr>
								<td align="right"><msg:message code='order.payAmount' /><msg:message code='system.common.sign.colon'/></td>
								<td>${order.payAmount}</td>
							</tr>
							
						</table>
					</td>
					<td width="50%" class="skinLayout_info" >
						<table border="0" cellspacing="1" cellpadding="0" width="100%" class="skinLayout">
							<tr>
								<td align="right" width="40%"><msg:message code='order.affirm.amount' /><msg:message code='system.common.sign.colon'/></td>
								<td width="60%"><input type="text" id="affirmProofAmount"  onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" oninput="checkNumber(this);" onblur="checkNumber(this);" /></td>
							</tr>
							<tr>
								<td align="right"><msg:message code='order.proof.affirm.proofTime' /><msg:message code='system.common.sign.colon'/></td>
								<td><input type="text" id="affirmProofTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate" readonly="readonly" /></td>
							</tr>
							<tr><td colspan="2">&nbsp;</td></tr>
							<tr><td colspan="2">&nbsp;</td></tr>
							<tr>
								<td colspan="2" align="center">
									<c:if test="${!empty market_order_detail_bankStreamConfirm_addto}">
						    			<security:authorize ifAnyGranted="${market_order_detail_bankStreamConfirm_addto.code}">
						    				<input type="button" id="${market_order_detail_bankStreamConfirm_addto.indexCode}" value="${market_order_detail_bankStreamConfirm_addto.name}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						    			</security:authorize>
					    			</c:if>
					    			<c:if test="${!empty market_order_detail_bankStreamConfirm_recheck}">
						    			<security:authorize ifAnyGranted="${market_order_detail_bankStreamConfirm_recheck.code}">
						    				<input type="button" id="${market_order_detail_bankStreamConfirm_recheck.indexCode}" value="${market_order_detail_bankStreamConfirm_recheck.name}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						    			</security:authorize>
					    			</c:if>
					    			<c:if test="${!empty market_order_detail_bankStreamConfirm_modifyTwo}">
						    			<security:authorize ifAnyGranted="${market_order_detail_bankStreamConfirm_modifyTwo.code}">
						    				<input type="button" id="${market_order_detail_bankStreamConfirm_modifyTwo.indexCode}" value="${market_order_detail_bankStreamConfirm_modifyTwo.name}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						    			</security:authorize>
					    			</c:if>
					    			<c:if test="${!empty market_order_detail_bankStreamConfirm_cancel}">
						    			<security:authorize ifAnyGranted="${market_order_detail_bankStreamConfirm_cancel.code}">
						    				<input type="button" onclick="closeWin();" id="${market_order_detail_bankStreamConfirm_cancel.indexCode}" value="${market_order_detail_bankStreamConfirm_cancel.name}"/>
						    			</security:authorize>
					    			</c:if>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="productListTwo"><tr><td>&nbsp;</td></tr></table>
			<div id="pageredTwo"></div>
		</td>
	</tr>
	<tr>
		<td>
			<table id="productList"><tr><td>&nbsp;</td></tr></table>
			<div id="pageredOne"></div>
		</td>
	</tr>
	<tr height="40"><td>&nbsp;</td></tr>
</table>
</form>
<div class="toolbar">
	<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" onclick="closeWin();" name="btnback" value="<msg:message code="order.fail.button"/>" id="btnback"/>
		</div>
	<div class="right"></div>
</div>
</body>
</html>
