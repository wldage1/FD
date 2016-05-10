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
			 	scroll: true,
		        url: '${base}/market/order/detail/fundConfirm/getOrderAcountAffirmDetailGrid.json',
		        datatype: 'json',
		        colNames: [
		                "<msg:message code='order.orderNumber'/>",
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
		       			{name: 'order.clientName',index: 'order.clientName',width:'10%',align:'center',hidden: false,sortable:false},
		       			{name: 'proofTime',index: 'proofTime',width:'16%',align:'center',hidden: false,sortable: false},
		       			{name: 'proofAmount',index: 'proofAmount',width:'10%',align:'center',hidden: false,sortable: false},
		       			{name: 'affirmProofAmount',index: 'affirmProofAmount',width:'14%',align:'center',hidden: false,sortable: false},
		       			{name: 'affirmProofTime',index: 'affirmProofTime',width:'16%',align:'center',hidden: false,sortable: false},
		       			{name: 'type',index: 'type',width:'10%',align:'center',hidden: false,sortable: false,formatter:typeChange},
		       			{name: 'operateType',index: 'operateType',width:'10%',align:'center',hidden: false,sortable: false,formatter:operateTypeChange}
		 				],
		        mtype:"POST",
		        postData:{orderNumber:'${orderProof.order.orderNumber}',c:"${market_order_detail_fundConfirm.code}"},
		        rowNum:10,     
		        page:"${orderAcountAffirmDetail.page}",
		        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
		        pager: '#pageredOne',
		        height: 230,
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
		
	});
	
	//金额确认
	function comfirmyes(){
		var k=$.layer({
		    shade : false,
		    area : ['auto','auto'],
		    dialog : {
		        msg:'<msg:message code='handle.confirm'/>',
		        btns : 2, 
		        type : 4,
		        btn : ['<msg:message code='order.true.button'/>','<msg:message code='order.fail.button'/>'],
		        yes : function(){
		        	$.ajax({
    					url: '${base}${market_order_detail_fundConfirm_dispose_addto.controller}.json',
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					data: {type:1,operateType:1,proofID:${orderProof.id},orderNumber:'${orderProof.order.orderNumber}',proofAmount:${orderProof.proofAmount},proofTime:'${orderProof.proofTime}',affirmProofAmount:$("#affirmProofAmount").val(),affirmProofTime:$("#affirmProofTime").val(),serialNumber:$("#serialNumber").val(),comfirm:1,c:'${market_order_detail_fundConfirm_dispose_addto.code}'}, 
    					async:false,
    					cache:false,
    					success:function(jsonData){
    						layer.close(k);
    						if(jsonData.zt=='success'){
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code='set.success'/>',type : 9}
    							});
    							top.document.getElementById(iframeID).contentWindow.reloadGridTwo();
    							closeWin();
    						}else{
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code='set.fail'/>',type : 8}	
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
	
	// 拒绝
	function comfirmno(){
		var k=$.layer({
		    shade : false,
		    area : ['auto','auto'],
		    dialog : {
		        msg:'是否拒绝该凭证？',
		        btns : 2, 
		        type : 4,
		        btn : ['<msg:message code='order.true.button'/>','<msg:message code='order.fail.button'/>'],
		        yes : function(){
		        	$.ajax({
    					url: '${base}${market_order_detail_fundConfirm_dispose_refuse.controller}.json',
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					data: {proofID:${orderProof.id},comfirm:0,remark:$("#remark").val(),c:'${market_order_detail_fundConfirm_dispose_refuse.code}'}, 
    					async:false,
    					cache:false,
    					success:function(jsonData){
    						layer.close(k);
    						if(jsonData.zt=='success'){
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'凭证处理成功',type : 9}
    							});
    							top.document.getElementById(iframeID).contentWindow.reloadGridTwo();
    							closeWin();
    						}else{
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code='set.fail'/>',type : 8}	
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
	
	//复核
	$("#${market_order_detail_fundConfirm_dispose_recheck.indexCode}").live("click",function(){
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
    					url: '${base}${market_order_detail_fundConfirm_dispose_recheck.controller}.json',
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					data: {type:1,operateType:2,proofID:${orderProof.id},orderNumber:'${orderProof.order.orderNumber}',proofAmount:${orderProof.proofAmount},proofTime:'${orderProof.proofTime}',affirmProofAmount:$("#affirmProofAmount").val(),affirmProofTime:$("#affirmProofTime").val(),c:'${market_order_detail_fundConfirm_dispose_recheck.code}'}, 
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
	$("#${market_order_detail_fundConfirm_dispose_modifyTwo.indexCode}").live("click",function(){
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
    					url: '${base}${market_order_detail_fundConfirm_dispose_modifyTwo.controller}.json',
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					data: {type:1,operateType:3,proofID:${orderProof.id},orderNumber:'${orderProof.order.orderNumber}',proofAmount:${orderProof.proofAmount},proofTime:'${orderProof.proofTime}',affirmProofAmount:$("#affirmProofAmount").val(),affirmProofTime:$("#affirmProofTime").val(),c:'${market_order_detail_fundConfirm_dispose_modifyTwo.code}'}, 
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
    							top.document.getElementById(iframeID).contentWindow.reloadGridTwo();
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
	
	function changeBig(imageUrl){
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
				html: '<img width="950" class="ttee" height="500" src="'+imageUrl+'"/>'
			}
		});
	}
	
	function reComfirm(obj){
		var v = $(obj).val();
		if(v == 0){
			$("#reSelect").attr("disabled",false);
			$("#remark").attr("disabled",false);
			$("#remark").val($("#reSelect option[value='1']").text());
		}else{
			$("#reSelect").attr("disabled",true);
			$("#remark").attr("disabled",true);
		}
	}
	
	function reMark(obj){
		var v = $("#reSelect option:selected").text();
		$("#remark").val(v);
	}
	
	
	function btconfirm(){
		var flag = $("#comfirm_status").val();
		//console.log(flag);
		if(flag == 1){
			comfirmyes();
		}else{
			comfirmno();
		}
	}
</script>

</head>
<body class="skinMain">
<form id="orderAttachment" name="orderAttachment" method="post">
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="47%" class="skinLayout_info">
						<table border="0" cellspacing="1" cellpadding="0" width="100%" class="skinLayout">
							<tr>
								<td align="right" width="40%"><msg:message code='order.orderNumber' /><msg:message code='system.common.sign.colon'/></td>
								<td width="60%">${orderProof.order.orderNumber}</td>
							</tr>
							<tr>
								<td align="right"><msg:message code='order.contractNumber' /><msg:message code='system.common.sign.colon'/></td>
								<td>${orderProof.order.contractNumber}</td>
							</tr>
							<tr>
								<td align="right"><msg:message code='order.product.shortName' /><msg:message code='system.common.sign.colon'/></td>
								<td>${orderProof.order.product.shortName}</td>
							</tr>
							<tr>
								<td align="right"><msg:message code='order.amountDistribute.distribute' /><msg:message code='system.common.sign.colon'/></td>
								<td>${orderProof.order.share}</td>
							</tr>
							<tr>
								<td align="right"><msg:message code='order.payAmount' /><msg:message code='system.common.sign.colon'/></td>
								<td>${orderProof.order.payAmount}</td>
							</tr>
							<tr>
								<td align="right"><msg:message code='order.proof.proofTime' /><msg:message code='system.common.sign.colon'/></td>
								<td>${orderProof.proofTime}</td>
							</tr>
						</table>
					</td>
					<td width="53%" class="skinLayout_info" >
						<table border="0" cellspacing="1" cellpadding="0" width="100%" class="skinLayout">
							<tr>
								<td align="right" width="30%"><msg:message code='order.proof.amount' /><msg:message code='system.common.sign.colon'/></td>
								<td colspan="2" width="70%" >${orderProof.proofAmount}</td>
							</tr>
							<tr>
								<td align="right" ><msg:message code='order.proof.affirm.amount' /><msg:message code='system.common.sign.colon'/></td>
								<td colspan="2"><input type="text" id="affirmProofAmount"  value="${orderProof.proofAmount}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" /></td>
							</tr>
							<tr>
								<td align="right"><msg:message code='order.proof.affirm.proofTime' /><msg:message code='system.common.sign.colon'/></td>
								<td colspan="2"><input type="text" id="affirmProofTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate" readonly="readonly" value="${orderProof.proofTime}" /></td>
							</tr>
							<tr>
								<td align="right" ><msg:message code='orderProof.serialNumber'/><msg:message code='system.common.sign.colon'/></td>
								<td><input type="text" id="serialNumber" /></td>
								<td><font color="red" style="font-size: 12px;"><msg:message code='orderProof.more.proof'/></font></td>
							</tr>
							<tr>
								<td align="right" >确认状态：</td>
								<td colspan="2">
									<select id="comfirm_status" onchange="reComfirm(this);">
										<option value="1">接受</option>
										<option value="0">拒绝</option>
									</select>
									<select id="reSelect" disabled="true" onchange="reMark(this);">
										<option value="1">姓名不符</option>
										<option value="2">打款金额不符</option>
										<option value="3">打款凭证不清晰</option>
										<option value="4">打款凭证无付款账号</option>
										<option value="-1">其它</option>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right">拒绝原因：</td>
								<td colspan="2" >
									<input type="text" id="remark" name="remark" disabled="true"/> 	
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="skinLayout_info">
						<table border="0" cellspacing="1" cellpadding="0" width="100%" class="skinLayout">
							<tr>
								<td align="left" colspan="2"><msg:message code='order.proof.files' /></td>
							</tr>
							<tr height="100">
								<td align="center"><img style="cursor:pointer;" width="300" height="100" src="${ftpHttpUrl}${orderProof.proofFileUrl}" onclick="changeBig(this.src);"/></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="productList"><tr><td>&nbsp;</td></tr></table>
			<div id="pageredOne"></div>
		</td>
	</tr>
	<tr height="45"><td>&nbsp;</td></tr>
</table>
</form>
<div class="toolbar">
	<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" value="确认" onclick="btconfirm();"/>
			<input type="button" class="btn2" onclick="closeWin();" name="btnback" value="<msg:message code="order.fail.button"/>" id="btnback"/>
		</div>
	<div class="right"></div>
</div>
</body>
</html>
