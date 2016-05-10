<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link  href="${base}/${skin}/css/tab.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#orderList").setGridWidth($(window).width()*0.999);　
});
var gridWidth;
//分组显示内容
var customInfo = new Array();
//需要核定订单的ID和提示信息内容
var vouchOrderID, vouchOrderNumber;
var index;
var tabmark="1";
//var productName;
//订单类型
var tradeType = "1";
$(document).ready(function(){
    $("#remark").attr("disabled", "disabled");
    
    //按照产品查询订单数据
    $("#productID").change(function(){
    	var productId = $("#productID").val();
    	if (tabmark=="1"){
	    	jQuery("#orderList").jqGrid("setGridParam",{
	    		postData:{c:"301", tradeTypeCollection: "1", productID: productId}
	    	}).trigger("reloadGrid");
    	}else if (tabmark=="3"){
    		jQuery("#orderList").jqGrid("setGridParam",{
	    		postData:{c:"301", tradeTypeCollection: "1,2,3", productID: productId}
	    	}).trigger("reloadGrid");
    	}
    	//清楚当前列表数据
    	//jQuery("#orderList").clearGridData();
    });
    
    jQuery("#orderList").jqGrid({
        url: 'adjust/holdlingPhase.json',
        datatype: 'json',
        colNames: ["", "", "", "",
                "产品简称",
                "子产品名称",
       	  		"存续阶段说明",
       	  		"生效日期",
       	  		"未核定",
       	  		"核定通过",
       	        "核定未通过",
       	        "拒发",
       	     	"订单数",
       	     	"到账金额(万元)",
       	    	"预计居间费(元)",
       	     	"<msg:message code='order.operate'/>"
       	        ],
		colModel: [
				{name: 'orderID',index: 'orderID',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'subProductID',index: 'subProductID',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'maxShareThreshold',index: 'maxShareThreshold',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'minShareThreshold',index: 'minShareThreshold',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'productName',index: 'productName',width:'10%',align:'center',hidden: false,sortable: false}, 
                {name: 'subProductName',index: 'subProductName',width:'10%',align:'center',hidden: false,sortable: false},
                {name: 'holdlingPhase',index: 'holdlingPhase',width:'6%',align:'center',hidden: false,sortable: false},
                {name: 'effectiveDate',index: 'effectiveDate',width:'8%',align:'center',hidden: false,sortable: false},
               	{name: 'notCheckOrderNumber',index: 'notCheckOrderNumber',width:'8%',align:'center',hidden: false,sortable: false},
               	{name: 'notAgreeOrderNumber',index: 'notAgreeOrderNumber',width:'8%',align:'center',hidden: false,sortable: false},
                {name: 'notDisagreeOrderNumber',index: 'notDisagreeOrderNumber',width:'10%',align:'center',hidden: false,sortable: false},
       			{name: 'refuseOrderNumber',index: 'refuseOrderNumber',width:'8%',align:'center',hidden: false,sortable:false},
       			{name: 'orderCount',index: 'orderCount',width:'8%',align:'center',hidden: false,sortable:false},
       			{name: 'payAmount',index: 'payAmount',width:'8%',align:'center',hidden: false,sortable: false},
       			{name: 'commission',index: 'commission',width:'8%',align:'center',sortable:false},
       			{name: 'act',index:'act', width:'15%',align:'center',hidden: false,sortable:false,formatter: formatterAct}
       			],
        mtype:"POST",
        postData:{c:'301', tradeTypeCollection: "1",productID : $("#productID").val()},
        height:	'auto',
        autowidth: true,
        multiselect: false,
        subGridOptions:{
        	expandOnLoad: true
	    },
        jsonReader: {
        	repeatitems: false
        },
        subGrid : true,
        caption:'认购订单居间费核算列表',
        subGridRowExpanded: function(subgrid_id, row_id) {
        	var data = jQuery("#orderList").jqGrid("getRowData",row_id);
        	var productId = $("#productID").val();
        	var subProductID = data.subProductID;
        	//var holdlingPhase = data.holdlingPhase;
        	var effectiveDate = data.effectiveDate;
        	var maxShareThreshold = data.maxShareThreshold;
        	var minShareThreshold = data.minShareThreshold;
	        var subgrid_table_id, pager_id;
			subgrid_table_id = subgrid_id+"_t";
			pager_id = "p_"+subgrid_table_id;
			$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
			jQuery("#"+subgrid_table_id).jqGrid({
				url:"adjust/grid.json",
				datatype: "json",
				colNames: [
				           '<msg:message code='order.orderNumber'/>',
	                       '<msg:message code='order.clientName'/>',
	                       '机构简称',
	                       '<msg:message code='order.commissionPercent'/>',
	                       '<msg:message code='order.payAmount'/>',
	                       '<msg:message code='order.commission'/>',
	                       '理财顾问',
	                       '<msg:message code='commission.adjust.status'/>',
	                       '<msg:message code='order.operate'/>'],
				colModel: [
					{name:"orderNumber",index:"orderNumber",width:80,align:"center",sortable:false},
					{name:"clientName",index:"clientName",width:60,align:"center",sortable:false},
					{name:"orgShortName",index:"orgShortName",width:60,align:"center",sortable:false},
					{name:"commissionProportionStr",index:"commissionProportionStr",width:70,align:"center",sortable:false},
					{name:"payAmount",index:"payAmount",width:70,align:"center",sortable:false},
					{name:"commission",index:"commission",width:70,align:"center",sortable:false},
					{name:"memberName",index:"memberName",width:60,align:"center",sortable:false},
					{name:"payStatus",index:"payStatus",width:60,align:"center",sortable:false, formatter: formatPayStatus},
					{name:"act",index:"act",width:70,align:"center",sortable:false,formatter: actChange},
				],
				postData:{tradeTypeCollection: tradeType, subProductID: subProductID, productID: productId,
					maxShareThreshold: maxShareThreshold, effectiveDate: effectiveDate, minShareThreshold: minShareThreshold},
			   	autowidth: true,
			    height: '100%',
			    multiselect: true,
			    //altRows:true,
			    //altclass: 'altRowsColour',
			    gridComplete:function(){
			    	var ids = jQuery("#"+subgrid_table_id).jqGrid('getDataIDs');
			    	var payAmountSum=0;
			    	var commissionSum=0;
			    	if(ids == ""){
			    		$("#"+row_id).next().remove();
			    		$("#"+row_id).remove();
			    	}else{
			    		for(var i=0;i < ids.length;i++){
			    			var id = ids[i];
			    			var rowdata = jQuery("#"+subgrid_table_id).jqGrid('getRowData',id);
			    			payAmountSum += parseFloat(rowdata.payAmount);
			    			commissionSum += parseFloat(rowdata.commission);
			    		}
			    		$("#orderList").jqGrid('setRowData', row_id, {payAmount: payAmountSum, commission: commissionSum});
			    	}
		        },
		        jsonReader: {
		        	repeatitems: false,
		        }
			});
        },
    });
	//$("#jqgh_orderList_commissionProportion").attr("title","年化发行成本率 × 佣金系数（个人/机构） × 时间系数 = 佣金比例 + 激励费用率 = 最终佣金比例");
    
    jQuery("#orderList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    $("#div_2").css("display","none");
    
  	//查看
    $(".shortcut_${commission_adjust_detail.indexCode}").live("click",function(){
    	window.location.href='${base}${commission_adjust_detail.controller}?c=${commission_adjust_detail.code}&id='+this.id;
    });
  	
  	//不发
    $(".shortcut_${commission_adjust_nonPayment.indexCode}").live("click",function(){
    	vouchOrderID = $(this).attr("id");
    	$("#remark2").val("");
		index = $.layer({
			type : 1,
			title : "拒绝发放居间费",
			shadeClose : true,
			border : [0, 0.5, '#666', true],
	  	    area : ['350px', 'auto'],
	  	  	//move: ['.xubox_title', true],
	  	  	offset : ['100px',''],
	       	page :{
	        	dom : '#nonPaymentDiv'
	        }
  	  	});
    });
  	
  	//核定
    $(".shortcut_${commission_adjust_vouch.indexCode}").live("click",function(){
    	vouchOrderID = $(this).attr("id");
    	$("#remark").val("");
		index = $.layer({
			type : 1,
			title : "订单核定",
			shadeClose : true,
			border : [0, 0.5, '#666', true],
	  	    area : ['350px', 'auto'],
	  	  	//move: ['.xubox_title', true],
	  	  	offset : ['100px',''],
	       	page :{
	        	dom : '#vouchDiv'
	        }
  	  	});
		$("input[name='adjustSelect'][value='1']").attr("checked",true);
    });
    
  	//核算
    $(".shortcut_${commission_adjust_adjust.indexCode}").live("click",function(){
    	//主表格行ID
    	var id = $(this).attr("id");
    	var pid = $(this).attr("pid");
    	//子表格ID
    	var subgrid_table_id = "orderList_" + id + "_t";
    	var productId = pid;
    	//以后要确定那个tab页的表格
    	var data = jQuery("#orderList").jqGrid("getRowData",id);
    	var subProductID = data.subProductID;
    	var holdlingPhase = data.holdlingPhase;
    	var effectiveDate = data.effectiveDate;
   		jQuery("#"+subgrid_table_id).jqGrid("setGridParam",
   		 		{postData:{tradeTypeCollection: tradeType, subProductID: subProductID, productID: productId,
							holdlingPhase: encodeURIComponent(holdlingPhase), effectiveDate: effectiveDate}}).trigger("reloadGrid");
    });
  	
  	//一键核定
    $(".shortcut_${commission_adjust_batchVouch.indexCode}").live("click",function(){
    	//主表格行ID
    	var id = $(this).attr("id");
    	//子表格ID
    	var subgrid_table_id = "orderList_" + id + "_t";
    	var selrow = jQuery("#"+subgrid_table_id).jqGrid("getGridParam","selarrrow");
    	if(selrow == 0){
    		top.$.layer({
    			shade : [0.5 , '#000' , true],
    	  		offset : ['50px',''],
    	  	 	title : "<msg:message code='info.prompt'/>",
    		    dialog : {msg:"请选择需要核定的订单！",type : 3}
    		});
    		return false;
    	}else{
    		for(var i=0; i<selrow.length; i++){
    			var id = selrow[i];
    			var data = jQuery("#"+subgrid_table_id).jqGrid("getRowData",id);
    			var payStatus = data.payStatus;
    			if(payStatus == "通过"){
    				top.$.layer({
    					shade : [0.5 , '#000' , true],
    			  		offset : ['50px',''],
    			  	 	title : "<msg:message code='info.prompt'/>",
    				    dialog : {msg:"核定通过的订单不能再次核定！",type : 3}
    				});
    				return false;
    			}
    		} 
    		if(selrow != ""){
    			var url = "${base}${commission_adjust_batchVouch.controller}.json?c=${commission_adjust_batchVouch.code}&payStatus=1&ids=" + selrow;
    			var index = top.$.layer({
    				shade : [0.5 , '#000' , true],
    		  	    area : ['auto','auto'],
    		  	  	offset : ['50px',''],
    				dialog   : {
    					msg  : "确定核定通过所选的订单么？",
    					btns : 2,
    					type : 4,
    					btn  : ["<msg:message code='button.confirm'/>", "<msg:message code='button.cancel'/>"],
    					yes  : function(){
    						$.ajax({
    							url:url,
    							type:'post',
    							timeout:'60000',
    							dataType:'json',
    							success:function(jsonData,textStatus){
    								if(textStatus == "success"){
    									if(jsonData.status == "success"){
    										top.layer.close(index);
    										top.$.layer({
    											shade : [0.5 , '#000' , true],
    									  		offset : ['50px',''],
    									  	 	title : "<msg:message code='info.prompt'/>",
    										    dialog : {msg:"操作成功！",type : 1}
    										});
    										$("#orderList").trigger("reloadGrid");
    									}else{
    										top.layer.close(index);
    										top.$.layer({
    											shade : [0.5 , '#000' , true],
    									  		offset : ['50px',''],
    									  	 	title : "<msg:message code='info.prompt'/>",
    										    dialog : {msg:"操作失败！",type : 3}
    										});
    									}
    								}
    							}
    						});
    					},
    					no   : function(){
    						top.layer.close(index);
    					}
    				}
    			});
    		}
    	}
    });
  	
});

//居间费比例
function formatProportion(cellval, opts, rowObject){
	return cellval + "%";
}

//设置居间费状态
function formatPayStatus(cellval, opts, rowObject){
	var payStatus;
	switch (cellval) {
	case 0:
		payStatus = "未核定";
		break;
	case 1:
		payStatus = "核定通过";
		break;
	case 2:
		payStatus = "未通过";
		break;
	case 6:
		payStatus = "拒付";
		break;
	}
	return payStatus;
}

//主表格操作功能
function formatterAct(cellvalue, options, rowObject){
	var id=rowObject.id;
	var pid= rowObject.productID;
	var content="";
	<c:if test="${!empty commission_adjust_adjust}">
    <security:authorize ifAnyGranted="${commission_adjust_adjust.code}">
    	content += "<a href='#' id='" + id + "' pid='" + pid + "'  class='shortcut_${commission_adjust_adjust.indexCode}' title='${commission_adjust_adjust.name}' >";
    	content += "<img src='${base}/${skin}/${commission_adjust_adjust.icon}' weight='18' height='18' border='0' align='absmiddle'/>";
    	content += "<font color='#0000FF' title='${commission_adjust_adjust.name}'>${commission_adjust_adjust.name}</font>";
    	content += "</a>";
    </security:authorize>
	</c:if>
	<c:if test="${!empty commission_adjust_batchVouch}">
    <security:authorize ifAnyGranted="${commission_adjust_batchVouch.code}">
    	content += "<a href='#' id='" + id + "' class='shortcut_${commission_adjust_batchVouch.indexCode}' title='${commission_adjust_batchVouch.name}' >";
    	content += "<img src='${base}/${skin}/${commission_adjust_batchVouch.icon}' weight='18' height='18' border='0' align='absmiddle'/>";
    	content += "<font color='#0000FF' title='${commission_adjust_batchVouch.name}'>${commission_adjust_batchVouch.name}</font>";
    	content += "</a>";
    </security:authorize>
	</c:if>
	return content;
}

//列表操作功能展示
function actChange(cellvalue, options, rowObject){
	var id=rowObject.id;
	//var orderID = rowObject.orderID;
	var payStatus = rowObject.payStatus;
	var content="";
	<c:if test="${!empty commission_adjust_detail}">
    <security:authorize ifAnyGranted="${commission_adjust_detail.code}">
    	content += "<a href='#' id='" + id + "' class='shortcut_${commission_adjust_detail.indexCode}' title='${commission_adjust_detail.name}' >";
    	content += "<img src='${base}/${skin}/${commission_adjust_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_adjust_detail.name}'>${commission_adjust_detail.name}</font>";
    	content += "</a>";
    </security:authorize>
	</c:if>
	
	if((payStatus == 6 && $("#tab_3").hasClass("selected")) || (payStatus == 0) || payStatus == 2){
		<c:if test="${!empty commission_adjust_vouch}">
	    <security:authorize ifAnyGranted="${commission_adjust_vouch.code}">
	    	content += "<a href='#' id='" + id + "' class='shortcut_${commission_adjust_vouch.indexCode}' title='${commission_adjust_vouch.name}' >";
	    	content += "<img src='${base}/${skin}/${commission_adjust_vouch.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_adjust_vouch.name}'>${commission_adjust_vouch.name}</font>";
	    	content += "</a>";
	    </security:authorize>
		</c:if>
		if(payStatus != 6){
			<c:if test="${!empty commission_adjust_nonPayment}">
		    <security:authorize ifAnyGranted="${commission_adjust_nonPayment.code}">
		    	content += "<a href='#' id='" + id + "' class='shortcut_${commission_adjust_nonPayment.indexCode}' title='${commission_adjust_nonPayment.name}' >";
		    	content += "<img src='${base}/${skin}/${commission_adjust_nonPayment.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_adjust_nonPayment.name}'>${commission_adjust_nonPayment.name}</font>";
		    	content += "</a>";
		    </security:authorize>
			</c:if>
		}
	}
	return content;
}

//TAB切换
function tab_order(tag){
	changeTab(tag);
	if(tag == 1){
		var productID = $("#productID").val();
		tradeType = "1";
		jQuery("#orderList").jqGrid("setGridParam",{postData:{c:"301", tradeTypeCollection: "1", productID: productID}})
		.setCaption('认购订单居间费列表').trigger("reloadGrid");
	}else if(tag == 2){
		tradeType = "2,3";
		jQuery("#orderList").jqGrid("setGridParam",{postData:{c:"301", tradeTypeCollection: "2,3", productID: null}})
		.setCaption('申购订单居间费列表').trigger("reloadGrid");
	}else if(tag == 3){
		var productID = $("#productID").val();
		tradeType = "1,2,3";
		jQuery("#orderList").jqGrid("setGridParam",{postData:{c:"301", tradeTypeCollection: "1,2,3", productID: productID}})
		.setCaption('拒发居间费列表').trigger("reloadGrid");
	}
}

function changeTab(tag){
	if(tag == 1){
		tabmark="1";
		$("#adjustTr").show();
		$("#tab_1").addClass("selected");
		//$("#div_1").css("display","block");
		//隐藏其他页面
		//$("#div_2").css("display","none");
		//去掉其他tab页选中状态
		$("#tab_2").removeClass("selected");
		$("#tab_3").removeClass("selected");
	}else if(tag == 2){
		//隐藏产品查询行
		$("#adjustTr").hide();
		$("#tab_2").addClass("selected");
		//$("#div_2").css("display","block");
		//隐藏其他页面
		//$("#div_1").css("display","none");
		//去掉其他tab页选中状态
		$("#tab_1").removeClass("selected");
		$("#tab_3").removeClass("selected");
	}else if(tag == 3){
		//隐藏产品查询行
		tabmark="3";
		$("#adjustTr").show();
		$("#tab_3").addClass("selected");
		//$("#div_2").css("display","block");
		//隐藏其他页面
		//$("#div_1").css("display","none");
		//去掉其他tab页选中状态
		$("#tab_1").removeClass("selected");
		$("#tab_2").removeClass("selected");
	}
}

//拒发居间费
function nonPayment(){
	var remarkContent = $("#remark2").val();
	if(remarkContent == ""){
		top.$.layer({
			shade : [0.5 , '#000' , true],
	  		offset : ['50px',''],
	  	 	title : "<msg:message code='info.prompt'/>",
		    dialog : {msg:"请输入备注信息。",type : 3}	
		});
		return false;
	}
	layer.close(index);
	var url = "${base}${commission_adjust_nonPayment.controller}.json?c=${commission_adjust_nonPayment.code}&id="+ vouchOrderID
			+ "&remark=" + remarkContent;
	$.ajax({
		url:url,
		type:'post',
		timeout:'60000',
		dataType:'json',
		success:function(jsonData,textStatus){
			if(textStatus == "success"){
				if(jsonData.status == "success"){
					top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"操作成功！",type : 1}	
					});
					if($("#div_2").css("display") == "none"){
						$("#orderList").trigger("reloadGrid");
					}else{
						$("#orderList2").trigger("reloadGrid");
					}
				}else{
					top.layer.close(index);
					top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"操作失败！",type : 3}	
					});
				}
			}
		}
	});
}

//核定是否通过选择
function adjustClick(){
	var payStatus = $("input[name='adjustSelect']:checked").val();
	var msgContent;
	var remarkContent = $("#remark").val();
	if(remarkContent == "" && payStatus == 2){
		top.$.layer({
			shade : [0.5 , '#000' , true],
	  		offset : ['50px',''],
	  	 	title : "<msg:message code='info.prompt'/>",
		    dialog : {msg:"核定不通过需要输入备注信息。",type : 3}	
		});
		return false;
	}
	layer.close(index);
	var url = "${base}${commission_adjust_batchVouch.controller}.json?c=${commission_adjust_batchVouch.code}&id="
			+ vouchOrderID + "&payStatus=" + payStatus;
	$.ajax({
		url:url,
		type:'post',
		timeout:'60000',
		dataType:'json',
		success:function(jsonData,textStatus){
			if(textStatus == "success"){
				if(jsonData.status == "success"){
					top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"操作成功！",type : 1}	
					});
					if($("#div_2").css("display") == "none"){
						$("#orderList").trigger("reloadGrid");
					}else{
						$("#orderList2").trigger("reloadGrid");
					}
				}else{
					top.layer.close(index);
					top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"操作失败！",type : 3}	
					});
				}
			}
		}
	});
}

$("input[name='adjustSelect']").live("click", function (){
	var val = $(this).val();
	if(val == 1){
		$("#remark").attr("disabled", "disabled");
	}else{
		$("#remark").removeAttr("disabled");
	}
});

</script>

<style type="text/css">
table.altrowstable {
	width : 100%;
	font-family: verdana,arial,sans-serif;
	font-size:12px;
	color:#333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
	/*background-color: #F0F6E4;*/
}
table.altrowstable th {
	border-width: 1px;
	/*padding: 6px;*/
	border-style: solid;
	border-color: #a9c6c9;
	text-align: center;
	background-color: #E4E4E4;
}
table.altrowstable td {
	border-width: 1px;
	/*padding: 6px;*/
	border-style: solid;
	border-color: #a9c6c9;
	text-align: center;
}
.oddrowcolor{
	/*background-color:#d4e3e5;*/
	background-color:#F2F2F2;
}
.evenrowcolor{
	background-color:#FFFFFF;
}

input[type="radio"] {
	width  : 14px;
	height : 14px;
	vertical-align:text-bottom; margin-bottom:-1px; margin-bottom:-2px\9;
}

</style>

</head>
<body class="skinMain">
	<ul id="tabnav">
		<li class="tab_1" ><a href="#" id="tab_1" onclick="tab_order(1)" class="selected">认购订单居间费</a></li>
		<li class="tab_2" ><a href="#" id="tab_2" onclick="tab_order(2)">申购订单居间费</a></li>
		<li class="tab_3" ><a href="#" id="tab_3" onclick="tab_order(3)">拒发居间费</a></li>
	</ul>
	<div id="div_1">
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%" valign="top">
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr id="adjustTr">
							<td align="center">
							<form:form method="post" action="commission" commandName="commission" name="f">
								<input type="hidden" name="c" value="${commission.c}"/>									
								<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
									<tr>
										<td align="right" width="13%" class="search_info" height="25"><msg:message code='product.name'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<form:select path="productID">
												<form:option value="0">请选择</form:option>
												<c:forEach items="${productList}" var="it">
													<form:option value="${it.id}">${it.shortName}</form:option>
												</c:forEach>
											</form:select>
										</td>
									</tr>
									<tr>
										<td colspan="3"><div id="holdlingPhase"></div></td>
									</tr>
								</table>
							</form:form>
							</td>
						</tr>
						<tr>
							<td>
								<table id="orderList"><tr><td>&nbsp;</td></tr></table>
								<div id="pagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div id="div_2" >
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%" valign="top">
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td>
								<table id="orderList2"><tr><td>&nbsp;</td></tr></table>
								<div id="pagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div id="nonPaymentDiv" style="display: none;width: 350px;">
		<table width="100%" border="0" cellspacing="3" cellpadding="0" class="skinMain" align="left">
			<tr>
				<td valign='top' align="right">备注：</td>
				<td class='search_lable'><textarea id="remark2" style='resize: none;' rows='3' cols='30'></textarea></td>
			</tr>
			<tr>
				<td align='center' colspan='2' >
					<input type='button' onclick='nonPayment();' id='nonPaymentSubmit' class='btn2' value='确定'>
				</td>
			</tr>
		</table>
	</div>
	<div id="vouchDiv" style="display: none;width: 350px;">
		<table width="100%" border="0" cellspacing="3" cellpadding="0" class="skinMain" align="left">
			<tr>
				<td align="right">是否通过：</td>
				<td colspan='2'>
					<input type='radio' style='width:14px;height:14px;' name='adjustSelect' checked="checked" value='1'>&nbsp;通过
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type='radio' style='width:14px;height:14px;' name='adjustSelect' value='2'>&nbsp;不通过
				</td>
			</tr>
			<tr>
				<td valign='top' align="right">备注：</td>
				<td class='search_lable'><textarea id="remark" style='resize: none;' rows='3' cols='30'></textarea></td>
			</tr>
			<tr>
				<td align='center' colspan='2' >
					<input type='button' onclick='adjustClick();' id='adjustSubmit' class='btn2' value='确定'>
				</td>
			</tr>
		</table>
	</div>
</body>
