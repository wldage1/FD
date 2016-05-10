﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#productList").setGridWidth($(window).width()*0.999);　
});
//layer弹出层标签
var k;
$(document).ready(function(){
	/**********************************  认购订单-全部 STAR  *************************************/          
    jQuery("#productList").jqGrid({
        url: '${base}/market/order/grid.json',
        datatype: 'json',
        colNames: ["","",
                "<msg:message code='order.orderNumber'/>",
       	        "<msg:message code='order.product.shortName'/>",
       	     	"<msg:message code='subProduct.type'/>",
       	        "<msg:message code='product.org.name'/>", 
       	  	    "<msg:message code='order.orgID.name'/>",
       	        "<msg:message code='order.clientName'/>",
       	        "<msg:message code='order.investAmount'/>",
       	        "<msg:message code='order.member.name'/>",
       	        "<msg:message code='order.tradeType'/>",
       	        "<msg:message code='order.tradeStatus'/>",
       	        "<msg:message code='order.docStatus'/>",
       	        "确认打款状态",
       	        "<msg:message code='order.operate'/>"
       	        ],
               colModel: [
                {name: 'id',index: 'id',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'share',index: 'share',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'orderNumber',index: 'orderNumber',width:'13%',align:'center',hidden: false,sortable: false}, 
                {name: 'productShortName',index: 'productShortName',width:'8%',align:'center',hidden: false,sortable: false},
                {name: 'subProductType',index: 'subProductType',width:'8%',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowObject){
       				if(cellvalue == '' || cellvalue == null){
						return rowObject.productShortName;
					}else{
						return cellvalue;
					}
       			}},
                {name: 'providerShortName',index: 'providerShortName',width:'10%',align:'center',hidden: false,sortable: false},
                {name: 'orgShortName',index: 'orgShortName',width:'10%',align:'center',hidden: false,sortable: false},
       			{name: 'clientName',index: 'clientName',width:'7%',align:'center',hidden: false,sortable:false},
       			{name: 'investAmount',index: 'investAmount',width:'12%',align:'center',hidden: false,sortable:false},
       			{name: 'memberName',index: 'memberName',width:'10%',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowObject){
       				return cellvalue+"("+rowObject.memberNickName+")";
       			}},
       			{name: 'tradeType',index: 'tradeType',width:'6%',align:'center',hidden: false,sortable: false,formatter:changeZhn},
       			{name: 'tradeStatusName',index: 'tradeStatusName',width:'8%',align:'center',hidden: false,sortable: false},
       			{name: 'docStatusName',index: 'docStatusName',width:'6%',align:'center',hidden: false,sortable: false},
       			{name: 'confirmPay',index: 'confirmPay',width:'6%',align:'center',hidden: false,sortable: false,formatter:function(cellvalue,options,rowObject){
       				if(rowObject.confirmPay == 0){
       					return "未确认打款";
       				}else if(rowObject.confirmPay == 1){
       					return "理财师确认打款";
       				}else if(rowObject.confirmPay == 2){
       					return "平台方确认收到";
       				}
       			}},
       			{name: 'act',index:'act', width:'17%',align:'left',hidden:false,sortable:false,formatter:actChange}],
        mtype:"POST",
        postData:{c:"${market_order.code}",tradeType:1},
        rowNum:10,     
        page:"${order.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pageredOne',
        height:	230,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	loadComplete:function(){
    	},
        caption:'<msg:message code="order.tradeType.1.list"/>'+'—<msg:message code="select.all"/>',
        toolbar: [true,"top"]
    });
  	//补录订单 按钮
  	<c:if test="${!empty market_order_create}">
        <security:authorize ifAnyGranted="${market_order_create.code}">
		    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			.attr("id","${market_order_create.indexCode}")
			.attr("border","0").attr("class","${market_order_create.indexCode}")
			.append($("<img/>").attr("src","${base}/${skin}/${market_order_create.icon}")
		  	.attr("width","18").attr("height","18").attr("border","0")
		  	.attr("border","0").attr("align","absmiddle"))
		  	.append("${market_order_create.name}");
			$("#t_productList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea)); 
			
			//补录订单
		    $("#${market_order_create.indexCode}").click(function(){
		    	window.location.href='${base}${market_order_create.controller}?c=${market_order_create.code}&tradeType='+$("#tradeType").val();
		    });
		</security:authorize>
	</c:if>
    
	
    jQuery("#productList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    //查询按钮
    $("#select").click(function(){
    	jQuery("#productList").jqGrid("setGridParam",{page:0,postData:{productName:$("#productName").val(),memberName:$("#memberName").val(),clientName:$("#clientName").val(),tradeStatus:$("#tradeStatus").val(),docStatus:$("#docStatus").val(),share:$("#share").val(),orderNumber:$("#orderNumber").val()}}).trigger("reloadGrid");    
    });
    
    
    //查看 按钮的连接实现
    $(".shortcut_${market_order_detail.indexCode}").live("click",function(){
    	window.location.href='${base}${market_order_detail.controller}?c=${market_order_detail.code}&id='+this.id;
    });
    //打款确认
    $(".shortcut_${market_order_confirmPay.indexCode}").live("click",function(){
    	var id=this.id;
    	var data = jQuery("#productList").jqGrid("getRowData",id);
    	k = $.layer({
			type: 1,
			title: '打款确认收到',
			shadeClose: true, 
			closeBtn: [0,true],
			border : [5, 0.5, '#666', true],
			offset: ['50px',''],
			move: ['.xubox_title', true],
			area: ['520px','auto'],
			page: {
				html: 
				'<form id="confirmForm">'+
				'<input type="hidden" name="c" value="${market_order_confirmPay.code}" />'+
				'<input type="hidden" name="id" value="'+id+'" />'+
				'<table width="520" border="0" cellspacing="0" cellpadding="0">'+
				'<tr>'+
				'<td width="100%" class="skinLayout_info" colspan="2">'+
					'<table width="99%" align="center" border="0" height="200" cellspacing="1" cellpadding="0" class="skinLayout">'+
					'<tr>'+
					'<td align="right" width="50%">'+
						'<msg:message code="order.orderNumber" /><msg:message code="system.common.sign.colon"/>'+
					'</td>'+
					'<td align="left" width="50%">'+data.orderNumber+'</td>'+
					'</tr><tr>'+
					'<td align="right" width="50%">'+
						'<msg:message code="order.investAmount" /><msg:message code="system.common.sign.colon"/>'+
					'</td>'+
					'<td align="left" width="50%">'+data.investAmount+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td align="right" width="50%">'+
							'<msg:message code="order.share" /><msg:message code="system.common.sign.colon"/>'+
						'</td>'+
						'<td align="left" width="50%">'+data.share+'</td>'+
					'</tr>'+
					'<tr>'+
					'<td align="right" width="50%">'+
						'<msg:message code="order.payAmount" /><msg:message code="system.common.sign.colon"/>'+
					'</td>'+
					'<td align="left" width="50%">'+
					'<input type="text" name="payAmount" id="payAmount" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" onafterpaste="this.value=this.value.replace(/\\D/g,\'\')"/>'+
					'<span id="payAmount_tip" style="color:red;">&nbsp;&nbsp;*</span>'+
					'</td>'+
					'</tr>'+
					'<tr>'+
					'<td align="right" width="50%">'+
						'<msg:message code="order.payTime" /><msg:message code="system.common.sign.colon"/>'+
					'</td>'+
					'<td align="left" width="50%">'+
					'<input type="text" name="payTime" id="payTime" onfocus="WdatePicker({dateFmt:\'yyyy-MM-dd\'});" class="Wdate" readonly="readonly"/>'+
					'<span id="payTime_tip" style="color:red;">&nbsp;&nbsp;*</span>'+
					'</td>'+
					'</tr>'+
					'</table>'+
					'</td></tr>'+
					'<tr height="40" bgcolor="#CCDCFF">'+
					'<td align="right" width="50%"><input type="button" class="btn2" onclick="confirmRecive()" value="<msg:message code="order.true.button" />"/>&nbsp;&nbsp;</td>'+
					'<td width="50%" >&nbsp;&nbsp;<input type="button" class="btn2" onclick="closeWinConfirm()" value="<msg:message code="order.fail.button" />"/></td></tr>'+
					'</tr>'+
					'</table>'+
					'</form>'
				}
			});
    });
    
    //修改 按钮的连接实现
    $(".shortcut_${market_order_modify.indexCode}").live("click",function(){
    	window.location.href='${base}${market_order_modify.controller}?c=${market_order_modify.code}&id='+this.id;
    });
    //撤销 按钮的连接实现
    $(".shortcut_${market_order_repeal.indexCode}").live("click",function(){
    	var id=this.id;
    	var data = jQuery("#productList").jqGrid("getRowData",id);
    	var orderNumber = data.orderNumber;
    	k=$.layer({
		    shade : false,
		    area : ['auto','auto'],
		    dialog : {
		        msg:'<msg:message code='order.repeal.isornot'/>',
		        btns : 2, 
		        type : 4,
		        btn : ['<msg:message code='order.true.button'/>','<msg:message code='order.fail.button'/>'],
		        yes : function(){
		        	$.ajax({
    					url: '${base}${market_order_repeal.controller}.json',
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					data: {id:id,c:'${market_order_repeal.code}',orderNumber:orderNumber}, 
    					async:false,
    					cache:false,
    					success:function(jsonData){
    						layer.close(k);
    						if(jsonData.zt=='success'){
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code='order.repeal.success'/>',type : 9}	
    							});
    							jQuery("#productList").jqGrid().trigger("reloadGrid");
    						}else{
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code='order.repeal.error'/>',type : 8}	
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
    
  	//列表操作功能展示
    function actChange(cellvalue, options, rowObject){
    	var id=rowObject.id;
    	var content="";
    	//查看 连接 
    	<c:if test="${!empty market_order_detail}">
        <security:authorize ifAnyGranted="${market_order_detail.code}">
        	if(rowObject.tradeStatus==1){
				if(rowObject.proofCount!=null && rowObject.proofCount!=''){
					content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_order_detail.indexCode}' title='${market_order_detail.name}'>";
		        	content += "<img src='${base}/${skin}/${market_order_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='red' title='额度未分配,但已打款'>${market_order_detail.name}</font>";
		        	content += "</a>";
				}else{
					content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_order_detail.indexCode}' title='${market_order_detail.name}'>";
		        	content += "<img src='${base}/${skin}/${market_order_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_order_detail.name}";
		        	content += "</a>";
				}
			}else{
				content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_order_detail.indexCode}' title='${market_order_detail.name}'>";
	        	content += "<img src='${base}/${skin}/${market_order_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_order_detail.name}";
	        	content += "</a>";
			}
        </security:authorize>
    	</c:if>
    	<c:if test="${!empty market_order_confirmPay}">
        <security:authorize ifAnyGranted="${market_order_confirmPay.code}">
        	if(rowObject.confirmPay == 1){
	        	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_order_confirmPay.indexCode}' title='${market_order_confirmPay.name}'>";
	        	content += "<img src='${base}/${skin}/${market_order_confirmPay.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_order_confirmPay.name}";
	        	content += "</a>";
        	}
        </security:authorize>
    	</c:if>
    	//修改  连接 
    	<c:if test="${!empty market_order_modify}">
        <security:authorize ifAnyGranted="${market_order_modify.code}">
        	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_order_modify.indexCode}' title='${market_order_modify.name}'>";
        	content += "<img src='${base}/${skin}/${market_order_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_order_modify.name}";
        	content += "</a>";
        </security:authorize>
    	</c:if>
    	//撤销  连接 
    	<c:if test="${!empty market_order_repeal}">
        <security:authorize ifAnyGranted="${market_order_repeal.code}">
        	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_order_repeal.indexCode}' title='${market_order_repeal.name}'>";
        	content += "<img src='${base}/${skin}/${market_order_repeal.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_order_repeal.name}";
        	content += "</a>";
        </security:authorize>
    	</c:if>
    	return content;
    }
    
    //初始化改变中文 交易类型
    function changeZhn(cellvalue, options, rowObject){
    	var content="";
    	<c:forEach items="${tradeTypeMap}" var="it">
	    	if(cellvalue==${it.key}){
	    		return '${it.value}';
	    	}
    	</c:forEach>
    }
    /**********************************  认购订单-全部 END  *************************************/  
    
    /**********************************  认购订单-打款凭证处理或未处理  STAR  *************************************/          
    jQuery("#productList_hasOrNotDispose").jqGrid({
        url: '${base}/market/order/hasOrNotDisposeGrid.json',
        datatype: 'local',
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
          	        "<msg:message code='order.operate'/>"
          	        ],
                  colModel: [
                   {name: 'orderProof.orderNumber',index: 'orderProof.orderNumber',width:'10%',align:'center',hidden: false,sortable: false}, 
                   {name: 'contractNumber',index: 'contractNumber',width:'8%',align:'center',hidden: false,sortable: false},
                   {name: 'memberName',index: 'memberName',width:'10%',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowObject){
                	   return cellvalue+"("+rowObject.memberNickName+")";
                   }},
                   {name: 'orgShortName',index: 'orgShortName',width:'10%',align:'center',hidden: false,sortable: false},
	      		   {name: 'clientName',index: 'clientName',width:'7%',align:'center',hidden: false,sortable:false},
      			   {name: 'iDCard',index: 'iDCard',width:'7%',align:'center',hidden: false,sortable:false},
      			   {name: 'investAmount',index: 'investAmount',width:'10%',align:'center',hidden: false,sortable:false},
      			   {name: 'share',index: 'share',width:'10%',align:'center',hidden: false,sortable:false},
      			   {name: 'payTime',index: 'payTime',width:'10%',align:'center',hidden: false,sortable: false},
      			   {name: 'payAmount',index: 'payAmount',width:'10%',align:'center',hidden: false,sortable: false},
      			   {name: 'orderProof.proofTime',index: 'order.orderProof.proofTime',width:'9%',align:'center',hidden: false,sortable: false},
      			   {name: 'orderProof.proofAmount',index: 'order.orderProof.proofAmount',width:'7%',align:'center',hidden: false,sortable: false},
      			   {name: 'orderProof.proofFileUrl',index: 'order.orderProof.proofFileUrl',width:'6%',align:'center',hidden: false,sortable: false,formatter:fileChange},
   				   {name: 'act',index:'act', width:'7%',align:'left',hidden:false,sortable:false,formatter:function(cellvalue, options, rowObject){
	       		    	var id=rowObject.id;
	       		    	var content="";
	       		    	//凭证处理  连接 
	       		    	<c:if test="${!empty market_order_detail_fundConfirm_dispose}">
		       		        <security:authorize ifAnyGranted="${market_order_detail_fundConfirm_dispose.code}">
		       		        	if(rowObject.orderProof.proofStatus==0){
		       		        		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_order_detail_fundConfirm_dispose.indexCode}' title='${market_order_detail_fundConfirm_dispose.name}'>";
			       		        	content += "<img src='${base}/${skin}/${market_order_detail_fundConfirm_dispose.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_order_detail_fundConfirm_dispose.name}";
			       		        	content += "</a>";
		       		        	}else{
		       		        		content += "<span style='color:#CFC09F;'><img src='${base}/${skin}/${market_order_detail_fundConfirm_dispose.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_order_detail_fundConfirm_dispose.name}</span>";	
		       		        	}
		       		        </security:authorize>
	       		    	</c:if>
	       		    	return content;
	       		    }}],
        mtype:"POST",
        postData:{c:"${market_order_detail_fundConfirm_dispose.code}",tradeType:1},
        rowNum:10,     
        page:"${order.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pageredOne_hasOrNotDispose',
        height:	230,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	loadComplete:function(){
    	},
        caption:'<msg:message code="order.tradeType.1.list"/>'+'—<msg:message code="select.all"/>',
        toolbar: [true,"top"]
    });
    
  	//当前页iframe ID
	var iframeID = window.frameElement.id;
    //处理 按钮的连接实现
    $(".shortcut_${market_order_detail_fundConfirm_dispose.indexCode}").live("click",function(){
    	var url='${base}${market_order_detail_fundConfirm_dispose.controller}?c=${market_order_detail_fundConfirm_dispose.code}&id='+this.id+'&iframeID='+iframeID;
		k=top.$.layer({
			type: 2,
			title: "<msg:message code='proof.confirm'/>",
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
	
    jQuery("#productList_hasOrNotDispose").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    //查询按钮
    $("#select_hasOrNotDispose").click(function(){
    	jQuery("#productList_hasOrNotDispose").jqGrid("setGridParam",{page:0,postData:{productName:$("#productName_hasOrNotDispose").val(),memberName:$("#memberName_hasOrNotDispose").val(),clientName:$("#clientName_hasOrNotDispose").val(),tradeStatus:$("#tradeStatus_hasOrNotDispose").val(),docStatus:$("#docStatus_hasOrNotDispose").val(),share:$("#share_hasOrNotDispose").val(),orderNumber:$("#orderNmberu_hasOrNotDispose").val()}}).trigger("reloadGrid");    
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
    /**********************************  认购订单-全部 END  *************************************/  
    
    /**********************************  赎回订单  START    *************************************/
    //赎回订单列表
    jQuery("#productList3").jqGrid({
        url: '${base}/market/order/redeemordergrid.json',
        datatype: 'local',
        colNames: ["",
            "<msg:message code='order.orderNumber'/>",
            "<msg:message code='order.contractNumber'/>",
	        "<msg:message code='order.product.shortName'/>",
	        "<msg:message code='product.org.name'/>",
	        "<msg:message code='order.clientName'/>",
	        "<msg:message code='order.member.name'/>",
	        "<msg:message code='redeemorder.subproduct.type' />",
	        "<msg:message code='redeemorder.shares'/>",
	        "<msg:message code='redeemorder.amount'/>",
	        "<msg:message code='redeemorder.openTime'/>",
	        "<msg:message code='order.tradeStatus'/>",
	        "<msg:message code='order.documentStatus'/>",
	        "<msg:message code='order.operate'/>"
	        ],
        colModel: [
            {name: 'id',index: 'id',width:'1%',align:'center',hidden: true,sortable: false},
            {name: 'orderNumber',index: 'orderNumber',width:'1%',align:'center',hidden: true,sortable: false},
            {name: 'contractNumber',index: 'contractNumber',width:'10%',align:'center',hidden: false,sortable: false}, 
            {name: 'productName',index: 'productName',width:'10%',align:'center',hidden: false,sortable: false},
            {name: 'providerName',index: 'providerName',width:'10%',align:'center',hidden: false,sortable: false},
			{name: 'clientName',index: 'clientName',width:'8%',align:'center',hidden: false,sortable:false},
			{name: 'member.name',index: 'member.name',width:'8%',align:'center',hidden: false,sortable:false,formatter:function(cellvalue, options, rowObject){
				return cellvalue+"("+rowObject.member.nickName+")";
   			}},
			{name: 'subProductType',index: 'subProductType',width:'8%',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowObject){
       				if(cellvalue == '' || cellvalue == null){
						return rowObject.productName;
					}else{
						return cellvalue;
					}
       			}},
			{name: 'share',index: 'share',width:'10%',align:'center',hidden: false,sortable:false},
			{name: 'amount',index: 'amount',width:'10%',align:'center',hidden: false,sortable:false},
			{name: 'openTime',index: 'openTime',width:'12%',align:'center',hidden: false,sortable: false},
			{name: 'tradeStatusName',index: 'tradeStatusName',width:'7%',align:'center',hidden: false,sortable: false},
			{name: 'documentStatusName',index: 'documentStatusName',width:'7%',align:'center',hidden: false,sortable: false},
			{name:'act',index:'act', width:'15%',align:'left',hidden:false,sortable:false,
				 formatter:function(cellvalue,options,rowdata){
					 var id=rowdata.id;
						var content="";
						//查看 连接 
						<c:if test="${!empty market_order_redeemDetail}">
					    <security:authorize ifAnyGranted="${market_order_redeemDetail.code}">
					    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_order_redeemDetail.indexCode}' title='${market_order_redeemDetail.name}'>";
					    	content += "<img src='${base}/${skin}/${market_order_redeemDetail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_order_redeemDetail.name}";
					    	content += "</a>";
					    </security:authorize>
						</c:if>
						//修改  连接 
						<c:if test="${!empty market_order_redeemModify}">
					    <security:authorize ifAnyGranted="${market_order_redeemModify.code}">
					    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_order_redeemModify.indexCode}' title='${market_order_redeemModify.name}'>";
					    	content += "<img src='${base}/${skin}/${market_order_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_order_redeemModify.name}";
					    	content += "</a>";
					    </security:authorize>
						</c:if>
						//撤销  连接 							
						<c:if test="${!empty market_order_redeemRepeal}">
					    <security:authorize ifAnyGranted="${market_order_redeemRepeal.code}">
					    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_order_redeemRepeal.indexCode}' title='${market_order_redeemRepeal.name}'>";
					    	content += "<img src='${base}/${skin}/${market_order_redeemRepeal.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_order_redeemRepeal.name}";
					    	content += "</a>";
					    </security:authorize>
						</c:if>
						return content;
	    			}   	
			}],
        mtype:"POST",
        postData:{c:"${order.c}"},
        rowNum:10,     
        page:"${order.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height: 282,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	loadComplete:function(){
    		
    	},
        caption:'<msg:message code="redeemorder.list"/>',
        toolbar: [true,"top"]
    });
    jQuery("#productList3").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    $("#selectRedeemOrder").click(function(){
    	jQuery("#productList3").jqGrid("setGridParam",{page:0,postData:{productName:$("#productName3").val(),memberName:$("#memberName3").val(),clientName:$("#clientName3").val(),tradeStatus:$("#tradeStatus3").val(),documentStatus:$("#documentStatus3").val()}}).trigger("reloadGrid");    
    });
    
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
	.attr("id","${market_order_redeemCreate.indexCode}")
	.attr("border","0").attr("class","${market_order_redeemCreate.indexCode}")
	.append($("<img/>").attr("src","${base}/${skin}/${market_order_redeemCreate.icon}")
  	.attr("width","18").attr("height","18").attr("border","0")
  	.attr("border","0").attr("align","absmiddle"))
  	.append("${market_order_create.name}");
	$("#t_productList3").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea)); 
    
	//补录订单
    $("#${market_order_redeemCreate.indexCode}").click(function(){
    	window.location.href='${base}${market_order_redeemCreate.controller}?c=${market_order_redeemCreate.code}';
    });
    
    $(".shortcut_${market_order_redeemDetail.indexCode}").live("click",function(){
    	window.location.href='${base}${market_order_redeemDetail.controller}?c=${market_order_redeemDetail.code}&id='+this.id;
    });
    //修改 按钮的连接实现
    $(".shortcut_${market_order_redeemModify.indexCode}").live("click",function(){
    	window.location.href='${base}${market_order_redeemModify.controller}?c=${market_order_redeemModify.code}&id='+this.id;
    });
    //撤销 按钮的连接实现
    $(".shortcut_${market_order_redeemRepeal.indexCode}").live("click",function(){
    	var id=this.id;
    	var data = jQuery("#productList3").jqGrid("getRowData",id);
    	var orderNumber = data.orderNumber;
    	var k=$.layer({
		    shade : false,
		    area : ['auto','auto'],
		    dialog : {
		        msg:'<msg:message code='order.repeal.isornot'/>',
		        btns : 2, 
		        type : 4,
		        btn : ['<msg:message code='order.true.button'/>','<msg:message code='order.fail.button'/>'],
		        yes : function(){
		        	$.ajax({
    					url: '${base}${market_order_redeemRepeal.controller}.json?id='+id+'&orderNumber='+orderNumber,
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    				//	data: {id:id,released:1,c:'${market_order_redeemRepeal.code}'}, 
    				//	async:false,
    				//	cache:false,
    					success:function(jsonData){
    						if(jsonData.status=='success'){
    						layer.close(k);
    						$("#productList3").trigger("reloadGrid"); 
    						}else{
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code='order.repeal.error'/>',type : 8}	
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
    
    /*****************   赎回订单  end**********************/
    
    //导出excel
    $("#export").click(function(){
    	$("#order").append("<input type='hidden' name='tradeType' value='"+$('#tradeType').val()+"'/>");
    	$("form#order").submit();
    });
    
    
	var SubmitSelect = function(evt){
		    evt = window.event || evt;
		    if(evt.keyCode==13){//如果取到的键值是回车
		    	if($("#div_1").css("visibility")=='visible'){
		    		if($("#all_order").css("visibility")=='visible'){
		    			$("#select").click();	
			    	}else if($("#hasOrNotDispose_order").css("visibility")=='visible'){
			    		$("#select_hasOrNotDispose").click();	
			    	}
		    	}else if($("#div_3").css("visibility")=='visible'){
		    		$("#selectRedeemOrder").click();	
		    	}
		     }
		}
	window.document.onkeydown=SubmitSelect;
});
//关闭层
function closeWinConfirm(){
	layer.close(k);
}

//打款确认保存
function confirmRecive(){
	var data=$("#confirmForm").serialize();
	$("#payAmount_tip").html("&nbsp;&nbsp;*");
	$("#payTime_tip").html("&nbsp;&nbsp;*");
	if(!$("#payAmount").val()){
		$("#payAmount_tip").html("金额不能为空！");
		return false;
	}else if(!$("#payTime").val()){
		$("#payTime_tip").html("日期不能为空！");
		return false;
	}else{
		$.ajax({
			url: '${base}${market_order_confirmPay.controller}.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: data, 
			async:false,
			cache:false,
			success:function(jsonData){
				layer.close(k);
				if(jsonData.zt=='success'){
					top.$.layer({
						area : ['auto','auto'],
						dialog : {msg:'平台方确认打款成功',type : 9}	
					});
					jQuery("#productList").jqGrid().trigger("reloadGrid");
				}else{
					top.$.layer({
						area : ['auto','auto'],
						dialog : {msg:'平台方确认打款失败',type : 8}	
					});
				}
			}
		});
	}
}

//TAB切换
function tab_order(tag){
	changeTab(tag);
	if(tag==1){
		$("#tradeType").val(1);
		tab_order_Two(1);
	}else if(tag==2){
		$("#tradeType").val(2);
		tab_order_Two(1);
	}else{
		$("#tradeType").val(2);
		jQuery("#productList3").jqGrid("setGridParam",{page:0,datatype:'json'}).trigger("reloadGrid");
	}
}

function changeTab(tag){
	if(tag == 1){
		$("#tab_1").addClass("selected");
		$("#div_1").css("visibility","visible");
		$("#div_3").css("visibility","hidden");
		$("#tab_2").removeClass("selected");
		$("#tab_3").removeClass("selected");
	}else if(tag == 2){
		$("#tab_2").addClass("selected");
		$("#div_1").css("visibility","visible");
		$("#tab_1").removeClass("selected");
		$("#tab_3").removeClass("selected");
		$("#div_3").css("visibility","hidden");
	}else{
		$("#all_order").css("visibility","hidden");
		$("#hasOrNotDispose_order").css("visibility","hidden");
		$("#tab_3").addClass("selected");
		$("#div_3").css("visibility","visible");
		$("#div_1").css("visibility","hidden");
		$("#tab_2").removeClass("selected");
		$("#tab_1").removeClass("selected");
	}
}

//TAB_Two切换
function tab_order_Two(tag){
	changeTab_Two(tag);
	if($("#tradeType").val()==1){
		if(tag==1){
			jQuery("#productList").jqGrid("setGridParam",{page:0,postData:{tradeType:$("#tradeType").val()}}).setCaption('<msg:message code="order.tradeType.1.list"/>'+'—全部').trigger("reloadGrid");
		}else if(tag==2){
			jQuery("#productList_hasOrNotDispose").jqGrid("setGridParam",{page:0,datatype:'json',postData:{tradeType:$("#tradeType").val(),proofStatus:1}}).setCaption('<msg:message code="order.tradeType.1.list"/>'+'—已处理打款凭证').trigger("reloadGrid");
		}else{
			jQuery("#productList_hasOrNotDispose").jqGrid("setGridParam",{page:0,datatype:'json',postData:{tradeType:$("#tradeType").val(),proofStatus:0}}).setCaption('<msg:message code="order.tradeType.1.list"/>'+'—未处理打款凭证').trigger("reloadGrid");
		}	
	}else{
		if(tag==1){
			jQuery("#productList").jqGrid("setGridParam",{page:0,postData:{tradeType:$("#tradeType").val()}}).setCaption('<msg:message code="order.tradeType.2.list"/>'+'—全部').trigger("reloadGrid");
		}else if(tag==2){
			jQuery("#productList_hasOrNotDispose").jqGrid("setGridParam",{page:0,datatype:'json',postData:{tradeType:$("#tradeType").val(),proofStatus:1}}).setCaption('<msg:message code="order.tradeType.2.list"/>'+'—已处理打款凭证').trigger("reloadGrid");
		}else{
			jQuery("#productList_hasOrNotDispose").jqGrid("setGridParam",{page:0,datatype:'json',postData:{tradeType:$("#tradeType").val(),proofStatus:0}}).setCaption('<msg:message code="order.tradeType.2.list"/>'+'—未处理打款凭证').trigger("reloadGrid");
		}
	}
			
}

function changeTab_Two(tag){
	document.getElementById("order").reset();
	document.getElementById("order_hasOrNotDispose").reset();
	if(tag == 1){
		$("#tab_1_1").addClass("selected");
		$("#all_order").css("visibility","visible");
		$("#hasOrNotDispose_order").css("visibility","hidden");
		$("#tab_1_2").removeClass("selected");
		$("#tab_1_3").removeClass("selected");
	}else if(tag == 2){
		$("#tab_1_2").addClass("selected");
		$("#all_order").css("visibility","hidden");
		$("#tab_1_1").removeClass("selected");
		$("#tab_1_3").removeClass("selected");
		$("#hasOrNotDispose_order").css("visibility","visible");
	}else{
		$("#tab_1_3").addClass("selected");
		$("#hasOrNotDispose_order").css("visibility","visible");
		$("#all_order").css("visibility","hidden");
		$("#tab_1_2").removeClass("selected");
		$("#tab_1_1").removeClass("selected");
	}
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
function closeWin(){
	top.layer.close(k);
}
function reloadGridTwo(){
	jQuery("#productList_hasOrNotDispose").jqGrid().trigger("reloadGrid");
}
</script>
	</head>
	<body class="skinMain">
	<ul id="tabnav">
		<li class="tab_1" ><a href="javascript:void(0);" id="tab_1" onclick="tab_order(1)" class="selected"><msg:message code="order.tradeType.1"/></a></li>
		<li class="tab_2" ><a href="javascript:void(0);" id="tab_2" onclick="tab_order(2)"><msg:message code="order.tradeType.2"/></a></li>
		<li class="tab_3" ><a href="javascript:void(0);" id="tab_3" onclick="tab_order(3)"><msg:message code="redeemOrder.order"/></a></li>
	</ul>
	<input type="hidden" id="tradeType" value="1"/>
	<div id="div_1"  class="tab-div-show">
		<ul id="tabnav">
			<li class="tab_1_1" ><a href="javascript:void(0);" id="tab_1_1" onclick="tab_order_Two(1)" class="selected"><msg:message code="select.all"/></a></li>
			<li class="tab_1_3" ><a href="javascript:void(0);" id="tab_1_3" onclick="tab_order_Two(3)"><msg:message code="payProof.handle.notyet"/></a></li>
			<li class="tab_1_2" ><a href="javascript:void(0);" id="tab_1_2" onclick="tab_order_Two(2)"><msg:message code="payProof.handle.already"/></a></li>
		</ul>
		<div id="all_order" class="tab-div-show">
			<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td width="100%">
						<input type="hidden" name="c" value="${order.c}"/>
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td align="center">
								<form:form method="post" action="${base}/market/order/export" commandName="order" id="order" name="f">
									<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
										<tr>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.product.shortName'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable">
												<input id="productName" name="productName" maxlength="20"/>
											</td>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.tradeStatus'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable">
												<form:select path="tradeStatus">
													<form:option value=""><msg:message code="select.all"/></form:option>
													<c:forEach items="${tradeStatusMap}" var="it">
														<form:option value="${it.key}">${it.value}</form:option>	
													</c:forEach>
												</form:select>
											</td>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.docStatus'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable">
												<form:select path="docStatus">
													<form:option value=""><msg:message code="select.all"/></form:option>
													<c:forEach items="${docStatusMap}" var="it">
														<form:option value="${it.key}">${it.value}</form:option>
													</c:forEach>
												</form:select>
											</td>
											<td rowspan="3" width="10%" class="button_area" align="center">
												<input type="button" class="btn2" id="select" value="<msg:message code='button.search'/>">
												<h6 style="height: 5px;">&nbsp;</h6>
												<c:if test="${!empty market_order_export}">
					    							<security:authorize ifAnyGranted="${market_order_export.code}">
														<input type="button" class="btn2" id="export" value="<msg:message code='order.export'/>">
													</security:authorize>
												</c:if>
											</td>
										</tr>	
										<tr>
											<td align="right" width="10%" class="search_info"><msg:message code='order.member.name'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable"><input id="memberName" name="memberName" maxlength="20"/></td>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.clientName'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable"><input id="clientName" name="clientName" maxlength="20"/></td>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.amountDistribute.distribute'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable"><input id="share" name="share" maxlength="20"/></td>
										</tr>	
										<tr>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.orderNumber'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="30%"  class="search_lable" colspan="5"><input id="orderNumber" name="orderNumber" maxlength="20"/></td>
										</tr>
									</table>
								</form:form>
								</td>
							</tr>
							<tr>
								<td>
									<table id="productList"><tr><td>&nbsp;</td></tr></table>
									<div id="pageredOne"></div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<div id="hasOrNotDispose_order" class="tab-div-hide">
			<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td width="100%">
						<input type="hidden" name="c" value="${order.c}"/>
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td align="center">
								<form:form method="post" action="#" commandName="order" id="order_hasOrNotDispose" name="f">
									<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
										<tr>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.product.shortName'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable"><input id="productName_hasOrNotDispose" maxlength="20"/></td>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.tradeStatus'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable">
												<form:select path="tradeStatus" id="tradeStatus_hasOrNotDispose">
													<form:option value=""><msg:message code="select.all"/></form:option>
													<c:forEach items="${tradeStatusMap}" var="it">
														<form:option value="${it.key}">${it.value}</form:option>	
													</c:forEach>
												</form:select>
											</td>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.docStatus'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable">
												<form:select path="docStatus" id="docStatus_hasOrNotDispose">
													<form:option value=""><msg:message code="select.all"/></form:option>
													<c:forEach items="${docStatusMap}" var="it">
														<form:option value="${it.key}">${it.value}</form:option>
													</c:forEach>
												</form:select>
											</td>
											<td rowspan="3" width="20%" class="button_area" align="center"><input type="button" class="btn2" id="select_hasOrNotDispose" value="<msg:message code='button.search'/>"></td>
										</tr>	
										<tr>
											<td align="right" width="10%" class="search_info"><msg:message code='order.member.name'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable"><input id="memberName_hasOrNotDispose" maxlength="20"/></td>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.clientName'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable" ><input id="clientName_hasOrNotDispose" maxlength="20"/></td>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.amountDistribute.distribute'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable"><input id="share_hasOrNotDispose" maxlength="20"/></td>
										</tr>	
										<tr>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.orderNumber'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="30%"  class="search_lable" colspan="5"><input id="orderNmberu_hasOrNotDispose" maxlength="20"/></td>
										</tr>
									</table>
								</form:form>
								</td>
							</tr>
							<tr>
								<td>
									<table id="productList_hasOrNotDispose"><tr><td>&nbsp;</td></tr></table>
									<div id="pageredOne_hasOrNotDispose"></div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div id="div_3" class="tab-div-hide">
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%">
					<input type="hidden" name="c" value="${product.c}"/>
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td align="center">
								<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
									<tr>
										<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.product.shortName'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<input id="productName3" maxlength="20"/>
										</td>
										<td align="right" width="10%" class="search_info"><msg:message code='order.member.name'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<input id="memberName3" maxlength="20"/>
										</td>
										<td rowspan="3" width="20%" class="button_area" align="center">
											<input type="button" class="btn2" id="selectRedeemOrder" value="<msg:message code='button.search'/>">
										</td>
									</tr>	
									<tr>
										<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.tradeStatus'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<select id="tradeStatus3">
												<option value=""><msg:message code="select.all"/></option>
												<c:forEach items="${tradeStatusRedeemMap}" var="it">
													<option value="${it.key}">${it.value}</option>	
												</c:forEach>
											</select>
										</td>
										<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.clientName'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable" >
											<input id="clientName3" maxlength="20"/>
										</td>
									</tr>
									<tr>
										<td align="right" width="10%" class="search_info"><msg:message code='order.documentStatus'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable" colspan="3">
											<select id="documentStatus3">
												<option value=""><msg:message code="select.all"/></option>
												<c:forEach items="${documentStatusMap }" var="it">
													<option value="${it.key}">${it.value}</option>
												</c:forEach>
											</select>
										</td>
									</tr>	
								</table>
							</td>
						</tr>
						<tr>
							<td >
								<table id="productList3"><tr><td>&nbsp;</td></tr></table>
								<div id="pagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>