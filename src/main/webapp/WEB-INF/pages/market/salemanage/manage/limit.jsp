<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
	
	<script type="text/javascript">
	$(window).resize(function(){ 　　
		$("#newordersList").setGridWidth($(window).width()*0.999);　
		$("#payedordersList").setGridWidth($(window).width()*0.999);　
		$("#sharedordersList").setGridWidth($(window).width()*0.999);　
	});
	
	
	<c:if test="${plot.maxLowAmountClientCount != -1}">
	var rowCount = 0;
	// 挂起任务
	var mission = 0;
	// 浮动层
	var layers = 0;
	// 是否需要计算额度进度条
	var isCaAmount = false;
	var isCaLimit = false;
	// 每一份金额占比
	var per_amount_percent = 0;
	// 每一份数量占比
	var per_count_percent = 0;
	
	// 预分配额度
	var totle_preshare = 0.0;
	// 预分配剩余额度
	var rest_share = 0.0;
	// 小额订单预分配份数
	var totle_limit_preshare = 0.0;
	// 小额订单预分配剩余份数
	var rest_limit_share = 0.0;
	
	// 已到账
	var order_total_payed = ${plot.orderTotalPayed};
	// 产品销售规模
	var product_scale_max = 1;
	// 配额未到账
	var order_total_unpayed_shared = ${plot.orderTotalUnPayedShared};
	// 小额已到账订单数量
	var order_totle_amount_limit = ${plot.lowAmountThreshold};
	var order_total_limit = ${plot.maxLowAmountClientCount};
	var order_total_limit_payed_count = ${plot.orderTotalLimitPayedCount};
	// 小额订单已配额未到账
	var order_total_limit_unpayed_shared_count = ${plot.orderTotalLimitUnPayedSharedCount};
	
	$(document).ready(function(){
		
		// 选中TAB
		$("#tab_${c}").attr("class","selected");
		
		// 产品销售规模
		product_scale_max = ${plot.productScaleMax}  > 0 ? ${plot.productScaleMax}:1;
		// 每一份金额占比
		per_amount_percent = 1/product_scale_max;
		// 每一份数量占比
		per_count_percent = 1/order_total_limit;
		
		// 初始进度条 
		caProcessLine(order_total_payed,order_total_unpayed_shared,totle_preshare);
		caLimitProcessLine(order_total_limit_payed_count,order_total_limit_unpayed_shared_count,totle_limit_preshare);
	
		<!--  额度分配列表 -->
		jQuery("#newordersList").jqGrid({
			datatype: "local",
			height: 255,
			width: 600,
			colNames:["","<msg:message code="product.sale.distribute.orderNum"/>"
				,"<msg:message code="product.sale.distribute.rmCoName"/>"
				,"<msg:message code="product.sale.distribute.teamName"/>"
				,"<msg:message code="product.sale.distribute.clientName"/>"
				,"<msg:message code="product.sale.distribute.orderTime"/>"
				,"<msg:message code="product.sale.distribute.commissionPayTime"/>"
				,"<msg:message code="product.sale.distribute.payTime"/>"
				,"<msg:message code="product.sale.distribute.shareTime"/>"
				,"<msg:message code="product.sale.distribute.investAmount"/>"
				,"<msg:message code="product.sale.distribute.payAmount"/>"
				,"<msg:message code="product.sale.distribute.orderCount"/>",""
				,"<msg:message code="product.sale.distribute.largeOrderDetail"/>"
				,"<msg:message code="product.sale.distribute.preShare"/>"	
				,""],
			colModel:[{name:'id',index:'id',hidden:true}
			,{name:'orderNumber',index:'orderNumber', width:'6%',align:'center',sortable: true}
			,{name:'rmName',index:'rmName', width:'6%',align:'center',sortable: false,formatter:rmNameFormatter}
			,{name:'teamShortName',index:'teamShortName', width:'6%',align:'center',sortable: false}
			,{name:'clientName',index:'clientName', width:'6%',align:'center',sortable: false}
			,{name:'orderTime',index:'orderTime', width:'6%',align:'center',sortable: false}
			,{name:'commissionPayTime',index:'commissionPayTime', width:'6%',align:'center',sortable: false}
			,{name:'payTime',index:'payTime', width:'6%',align:'center',sortable: false}
			,{name:'shareTime',index:'shareTime', width:'6%',align:'center',sortable: false}
			,{name:'investAmount',index:'investAmount', width:'6%',align:'center',sortable: false}
			,{name:'payAmount',index:'payAmount', width:'6%',align:'center',sortable: false}
			,{name:'largePayedCount',index:'largePayedCount', width:'6%',align:'center',sortable: false,formatter:orderCountActor}
			,{name:'smallSharedCount',index:'smallSharedCount',hidden:true}
			,{name:'largeOrderDetail',index:'largeOrderDetail', width:'6%',align:'center',sortable: false}
			,{name:'share',index:'share', width:'6%',align:'center',sortable: false,formatter:newOrderActor}
			,{name:'share',index:'share',hidden:true}
			],
			mtype:"POST",
			postData:{c:"${c}",productId:"${product.id}",operate:"1"},
			loadonce: true,
			rowNum:15,
			rowTotal: 2000,
			rowList : [20,30,50],
			scroll:1,			
			pager: '#newordersListPaged',
			height:130,
			autowidth: true,
			viewrecords: true,
			multiselect: false,
			rownumbers: true,
			gridview: true,
			jsonReader: {
				repeatitems: false
			},
			caption:'新预约订单'
		});
		
		<!--  配额未到账列表 -->
		jQuery("#payedordersList").jqGrid({
			datatype: "local",
			height: 255,
			width: 600,
			colNames:["","<msg:message code="product.sale.distribute.orderNum"/>"
				,"<msg:message code="product.sale.distribute.rmCoName"/>"
				,"<msg:message code="product.sale.distribute.teamName"/>"
				,"<msg:message code="product.sale.distribute.clientName"/>"
				,"<msg:message code="product.sale.distribute.orderTime"/>"
				,"<msg:message code="product.sale.distribute.commissionPayTime"/>"
				,"<msg:message code="product.sale.distribute.payTime"/>"
				,"<msg:message code="product.sale.distribute.shareTime"/>"
				,"<msg:message code="product.sale.distribute.investAmount"/>"
				,"<msg:message code="product.sale.distribute.payAmount"/>"
				,"<msg:message code="product.sale.distribute.shared"/>",""],
			colModel:[{name:'id',index:'id',hidden:true}
			,{name:'orderNumber',index:'orderNumber', width:'6%',align:'center',sortable: true,formatter:reSharedOrderActor}
			,{name:'rmName',index:'rmName', width:'6%',align:'center',sortable: false,formatter:rmNameFormatter}
			,{name:'teamShortName',index:'teamShortName', width:'6%',align:'center',sortable: false}
			,{name:'clientName',index:'clientName', width:'6%',align:'center',sortable: false}
			,{name:'orderTime',index:'orderTime', width:'6%',align:'center',sortable: false}
			,{name:'commissionPayTime',index:'commissionPayTime', width:'6%',align:'center',sortable: false}
			,{name:'payTime',index:'payTime', width:'6%',align:'center',sortable: false}
			,{name:'shareTime',index:'shareTime', width:'6%',align:'center',sortable: false}
			,{name:'investAmount',index:'investAmount', width:'6%',align:'center',sortable: false}
			,{name:'payAmount',index:'payAmount', width:'6%',align:'center',sortable: false}
			,{name:'share',index:'share', width:'6%',align:'center',sortable: false,formatter:payedOrderActor}
			,{name:'share',index:'share',hidden:true}
			],
			mtype:"POST",
			postData:{c:"${c}",productId:"${product.id}",operate:"1"},
			loadonce: true,
			rowNum:15,
			rowTotal: 2000,
			rowList : [20,30,50],
			scroll:1,			
			pager: '#payedordersListPaged',
			height:130,
			autowidth: true,
			viewrecords: true,
			multiselect: false,
			rownumbers: true,
			gridview: true,
			jsonReader: {
				repeatitems: false
			},
			caption:'已到账订单'
		});
		
		<!--  配额未到账列表 -->
		jQuery("#sharedordersList").jqGrid({
			datatype: "local",
			height: 255,
			width: 600,
			colNames:["","<msg:message code="product.sale.distribute.orderNum"/>"
				,"<msg:message code="product.sale.distribute.rmCoName"/>"
				,"<msg:message code="product.sale.distribute.teamName"/>"
				,"<msg:message code="product.sale.distribute.clientName"/>"
				,"<msg:message code="product.sale.distribute.orderTime"/>"
				,"<msg:message code="product.sale.distribute.commissionPayTime"/>"
				,"<msg:message code="product.sale.distribute.payTime"/>"
				,"<msg:message code="product.sale.distribute.shareTime"/>"
				,"<msg:message code="product.sale.distribute.investAmount"/>"
				,"<msg:message code="product.sale.distribute.payAmount"/>"
				,"<msg:message code="product.sale.distribute.shared"/>",""],
			colModel:[{name:'id',index:'id',hidden:true}
			,{name:'orderNumber',index:'orderNumber', width:'6%',align:'center',sortable: false,formatter:reSharedOrderActor}
			,{name:'rmName',index:'rmName', width:'6%',align:'center',sortable: false,formatter:rmNameFormatter}
			,{name:'teamShortName',index:'teamShortName', width:'6%',align:'center',sortable: false}
			,{name:'clientName',index:'clientName', width:'6%',align:'center',sortable: false}
			,{name:'orderTime',index:'orderTime', width:'6%',align:'center',sortable: false}
			,{name:'commissionPayTime',index:'commissionPayTime', width:'6%',align:'center',sortable: false,formatter:timeFormatter}
			,{name:'payTime',index:'payTime', width:'6%',align:'center',sortable: false}
			,{name:'shareTime',index:'shareTime', width:'6%',align:'center',sortable: false}
			,{name:'investAmount',index:'investAmount', width:'6%',align:'center',sortable: false}
			,{name:'payAmount',index:'payAmount', width:'6%',align:'center',sortable: false,formatter:payAmountFormatter,unformat:payAmountUnFormatter}
			,{name:'share',index:'share', width:'6%',align:'center',sortable: false,formatter:sharedOrderActor}
			,{name:'share',index:'share',hidden:true}
			],
			mtype:"POST",
			postData:{c:"${c}",productId:"${product.id}",operate:"1"},
			loadonce: true,
			rowNum:15,
			rowTotal: 2000,
			rowList : [20,30,50],
			scroll:1,			
			pager: '#sharedordersListPaged',
			height:130,
			autowidth: true,
			viewrecords: true,
			multiselect: false,
			rownumbers: true,
			gridview: true,
			jsonReader: {
				repeatitems: false
			},
			caption:'已配额订单'
		});
		
		<!-- 保存分配结果 -->
		$("#btn_save").click(function(){
			var limitc;
			if(order_total_limit != -1){
				limitc = order_total_limit - order_total_limit_payed_count - order_total_limit_unpayed_shared_count - totle_limit_preshare;
				if(limitc < 0){
					top.layer.msg("小额投资人数已超过限制，请重新分配。");
					return false;
				}
			}
		
			layers = top.layer.confirm("<msg:message code='product.sale.distribute.confirm'/>",  function(){
				top.layer.close(layers);
				layers = top.layer.load(0,2);
				jQuery.ajax({
					url:"distribute/distributeall.json",
					type: "post",
					contentType : 'application/x-www-form-urlencoded',
					data:$("#distribute_limit_orders").serializeArray(), 
					dataType : 'json',
					success:function(data){
						if(data.status == 'success'){
							top.$.layer({
								area : ['auto','auto'],
								dialog : {msg:'<msg:message code='info.operate.success'/>',type : 9}	
							});
							top.layer.close(layers);
							window.location.href='${base}${market_salemanage_manage_limit.controller}?c=${market_salemanage_manage_limit.code}&id=${product.id}&subProductId=${subProduct.id}';
						}else{
							top.layer.msg("error");
						}
					}
				});
			});
		});
		<!--//  额度分配列表 -->
		
		<!-- 到账订单挂起 -->
		$("#payedordersList .input_share").live("click",function(){
			var rowid = $(this).attr("sid");
			var elementId = $(this).attr("id");
			if(rowid == ''){
				top.layer.msg("error!");
				return false;
			}
			var rowdata = jQuery("#payedordersList").jqGrid('getRowData',rowid);
			//页面层
    		layers = $.layer({
    			type: 1,
    			title: '确认修改的额度和退款金额',
    			shadeClose: true, 
    			closeBtn: [0,true],
    			border : [5, 0.5, '#666', true],
    			offset: ['100px',''],
    			move: ['.xubox_title', true],
    			area: ['240px','auto'],
    			page: {
				html: '<form id="refund_form">'
				     +'<input type="hidden" name="id" value="'+rowid+'"/>'
					 +'<input type="hidden" name="orderNumber" value="'+rowdata.orderNumber+'"/>'
					 +'<input type="hidden" name="share" value="'+rowdata.share+'"/>'
					 +'<input type="hidden" name="payAmount" value="'+rowdata.payAmount+'"/>'
					 +'<div style="padding:11px;line-height:2em;">'
					 +'<ul>'
					 +'	<li><lable>订单编号：<font style="color:blue;font-weight:bold;">'+rowdata.orderNumber+'</font></lable></li>'
					 +'	<li><lable>到账金额：<font style="color:red;font-weight:bold;">'+rowdata.payAmount+'万元</font></lable></li>'
					 +'	<li><lable>分配额度：<font style="color:red;font-weight:bold;">'+rowdata.share+'万元</font></lable></lable><input type="hidden" name="reShare" value="'+$("#"+elementId).val()+'"/></li>'
					 +'	<li><lable>退款金额：</lable><input type="text" name="reFund" value="'+rowdata.payAmount+'"/></li>'
					 +'	<li><center><a id="limit_refund" style="padding:2px 5px;border: 1px #FF6600 solid;background-color:#FF6600;color:#FFF;cursor: pointer;">确认</a></center></li>'
					 +'</ul>'
					 +'</div></form>'
				}
    		});
		});
		
		<!-- 已配额订单挂起 -->
		$("#sharedordersList .input_share").live("click",function(){
			var rowid = $(this).attr("sid");
			var elementId = $(this).attr("id");
			if(rowid == ''){
				top.layer.msg("error!");
				return false;
			}
			var rowdata = jQuery("#sharedordersList").jqGrid('getRowData',rowid);
			//页面层
    		layers = $.layer({
    			type: 1,
    			title: '确认修改的额度',
    			shadeClose: true, 
    			closeBtn: [0,true],
    			border : [5, 0.5, '#666', true],
    			offset: ['100px',''],
    			move: ['.xubox_title', true],
    			area: ['240px','auto'],
    			page: {
				html: '<form id="refund_form">'
				     +'<input type="hidden" name="id" value="'+rowid+'"/>'
					 +'<input type="hidden" name="orderNumber" value="'+rowdata.orderNumber+'"/>'
					 +'<input type="hidden" name="share" value="'+rowdata.share+'"/>'
					 +'<input type="hidden" name="payAmount" value="'+rowdata.payAmount+'"/>'
					 +'<div style="padding:11px;line-height:2em;">'
					 +'<ul>'
					 +'	<li><lable>订单编号：<font style="color:blue;font-weight:bold;">'+rowdata.orderNumber+'</font></lable></li>'
					 +'	<li><lable>分配额度：</lable><input type="text" name="reShare" value="'+$("#"+elementId).val()+'"/></li>'
					 +'	<li><center><a id="limit_refund" style="padding:2px 5px;border: 1px #FF6600 solid;background-color:#FF6600;color:#FFF;cursor: pointer;">确认</a></center></li>'
					 +'</ul>'
					 +'</div></form>'
				}
    		});
		});
		
		//  操作挂起
		$("#limit_refund").live("click",function(){
			// 设置挂起任务参数
			var form = $("#refund_form").serializeArray();
			var id = form[0].value;
			var orderNumber = form[1].value;
			var share = form[2].value;
			var payAmount = form[3].value;
			var reShare = form[4].value;
			var t_rowCount = $("#span_"+id).attr("rowCount");
			var tr="";
			// 之前保存的挂起值
			var mission_up = $("#mission_cancel_"+id+"");
			var fund = mission_up.attr("refund");
			var un = mission_up.attr("reun");
			var lastshare = mission_up.attr("reshare");
			if(form.length > 5){
				var reFund = form[5].value;
				// 挂起前先还原之前状态
				if(mission_up.length > 0){
					recoverPayedShare(fund,un,lastshare,reShare);
				}
				// 更改分配进度
				var reun = alterPayedShare(payAmount,reFund,share,reShare);
				// 填充挂起列表数据
				$("#span_"+id).html("<input type='hidden' name='saleManageList["+t_rowCount+"].orderId' value='"+id+"'/><input type='hidden' name='saleManageList["+t_rowCount+"].processType' value='1'/><input type='hidden' name='saleManageList["+t_rowCount+"].reFund' value='"+reFund+"'/>");
				tr='<tr id="tr_'+id+'"><td>'+orderNumber+'</td><td>'+share+'</td><td>'+reShare+'</td><td>'+reFund+'</td><td align="center"><div class="mission-cancel" id="mission_cancel_'+id+'" sid='+id+' act=1 refund='+reFund+' reun='+reun+' reshare='+reShare+' revalue='+share+'>X</div></td></tr>';
			}else{
				// 挂起前先还原之前状态
				if(mission_up.length > 0){
					recoverSharedShare(un,lastshare,reShare);
				}
				var reun = alterSharedShare(share,reShare);
				$("#span_"+id).html("<input type='hidden' name='saleManageList["+t_rowCount+"].orderId' value='"+id+"'/><input type='hidden' name='saleManageList["+t_rowCount+"].processType' value='2'/>");
				tr='<tr id="tr_'+id+'"><td>'+orderNumber+'</td><td>'+share+'</td><td>'+reShare+'</td><td>--</td><td align="center"><div class="mission-cancel" id="mission_cancel_'+id+'" sid='+id+' act=2 reun='+reun+' reshare='+reShare+' revalue='+share+'>X</div></td></tr>';
			}
			$("#share_"+id).val(reShare);
			// 加入挂起列表
			$("#tr_"+id).remove();
			$("#mission_list").append(tr);
			var rowdata = $("DIV").data(""+id+"");
			if(typeof(rowdata) == 'undefined'){
				mission = mission+1;
				$("#standby_mission").text("["+mission+"]");	
				$("DIV").data(""+id+"","");
				layer.tips('有新挂起的任务',$("#standby_mission"),3);
			}
			layer.close(layers);
		});
		
		$(".mission-cancel").live("click",function(){
			var rowid = $(this).attr("sid");
			var act = $(this).attr("act");
			var fund = $(this).attr("refund");
			var un = $(this).attr("reun");
			var lastshare = $(this).attr("reshare");
			// 还原值
			var revalue = $(this).attr("revalue");
			$("#share_"+rowid).val(revalue);
			// 删除挂起任务
			$("#tr_"+rowid).remove();
			$("#tr_"+rowid).remove();
			mission = mission-1;
			$("#standby_mission").text("["+mission+"]");
			$("DIV").removeData(""+rowid+"");
			// 删除表单域
			$("#span_"+rowid).html('');
			// 更改分配进度
			if(act == '1'){
				recoverPayedShare(fund,un,lastshare,revalue);
			}else if(act =='2'){
				recoverSharedShare(un,lastshare,revalue);
			}
		});
		
		// 默认加载额度分配列表数据
		<!--// 初始化方法 -->
		reloadOrders(1);
		
		/**
		// 测试数据
		var mydata = [{id:"6",orderNumber:"203030020","member.nickName":"李明","member.name":"李明",payAmount:300.00,share:300.00}];
		jQuery("#payedordersList").jqGrid('addRowData',6,mydata[0]);
		mydata = [{id:"7",orderNumber:"203030021","member.nickName":"王小","member.name":"王小",share:"200.00"}];
		jQuery("#sharedordersList").jqGrid('addRowData',7,mydata[0]);
		mydata = [{id:"8",orderNumber:"203030022","member.nickName":"张三","member.name":"张三",share:""}];
		jQuery("#newordersList").jqGrid('addRowData',8,mydata[0]);
		*/
	});
	
	<!--  额度分配列表 -->
	
	//已到账列表操作功能
	function payedOrderActor(cellvalue, options, rowObject){
		var id=rowObject.id;
		var share = rowObject.share;
		if(share  == null || share == ''){
			share = "";
		}
		redata = share;
		var orderForm =  "<span id='span_"+id+"' rowCount="+rowCount+"></span><input readonly class='input_share' type='text' name='saleManageList["+rowCount+"].share' sid='"+id+"' id='share_"+id+"' value='"+redata+"' />";
		rowCount++;
		return orderForm;
	}
	
	//已配额列表操作功能
	function sharedOrderActor(cellvalue, options, rowObject){
		var id=rowObject.id;
		var share = rowObject.share;
		if(share  == null || share == ''){
			share = "";
		}
		redata = share;
		var orderForm =  "<span id='span_"+id+"' rowCount="+rowCount+"></span><input readonly class='input_share' type='text' name='saleManageList["+rowCount+"].share' sid='"+id+"' id='share_"+id+"' value='"+redata+"' />";
		rowCount++;
		return orderForm;
	}
	
	//已配额列表操作功能
	function reSharedOrderActor(cellvalue, options, rowObject){
		var id = rowObject.id;
		var status = rowObject.tradeStatus;
		if(status == 12){
			return "<font style='background-color:#4B7588;color:#FFF'>"+cellvalue+"</font>";
		}else{
			return cellvalue;
		}
	}
	
	//额度分配列表操作功能
	function newOrderActor(cellvalue, options, rowObject){
		var id=rowObject.id;
		var share = rowObject.share;
		if(share  == null || share == ''){
			share = "";
		}
		redata = share;
		var orderForm =  "<span id='span_"+id+"' rowCount="+rowCount+"></span><input type='hidden' name='saleManageList["+rowCount+"].orderId' value='"+id+"'/><input class='input_share' type='text' name='saleManageList["+rowCount+"].share' sid='"+id+"' id='share_"+id+"' value='"+redata+"' onchange='recordeShare("+id+",this.value);' onkeyup=\"checkNumber("+id+",event,this,'edit');\"  onpropertychange=\"checkNumber("+id+",event,this,'edit');\" oninput=\"checkNumber("+id+",event,this,'edit');\"/>";
		rowCount++;
		return orderForm;
	}
	
	// 到账大额已配置小额
	function orderCountActor(cellvalue, options, rowObject){
		var smallSharedCount=rowObject.smallSharedCount;
		if(cellvalue == null || typeof(cellvalue) == "undefined"){
			cellvalue = 0;
		}
		if(smallSharedCount == null || typeof(smallSharedCount) == "undefined"){
			smallSharedCount = 0;
		}
		return cellvalue+"/"+smallSharedCount;
	}
	
	//理财顾问姓名格式化
	function rmNameFormatter(cellvalue, options, rowObject){
		return cellvalue+"("+rowObject.rmShortName+")";
	}
	
	// 重新加载列表数据
	function reloadOrders(operate){
		rowCount = 0;
		var data = {};
		var limit = "${plot.lowAmountThreshold}" == "-1.0E-6" ? null:"${plot.lowAmountThreshold}";
		if('${product.isTotalShare}' == '1'){
			data = {c:"${c}",productId:"${product.id}",amountLimit:limit,operate:operate,isTotalShare:1};
		}else{
			var subProductId;
			if('${subProduct.id}' != ''){
				subProductId = "${subProduct.id}";
			}else{
				subProductId = "${subProductList[0].id}";
			}
			$("#a_subproduct_"+subProductId).attr("class","sub-title-active");
			data = {c:"${c}",subProductId:subProductId,amountLimit:limit,operate:operate,isTotalShare:0};
		}
		jQuery("#payedordersList").jqGrid("setGridParam",{url:'distribute/payedordersgrid.json',datatype: "json",postData:data}).trigger("reloadGrid");
		jQuery("#sharedordersList").jqGrid("setGridParam",{url:'distribute/sharedordersgrid.json',datatype: "json",postData:data}).trigger("reloadGrid");
		jQuery("#newordersList").jqGrid("setGridParam",{url:'distribute/limitnewordersgrid.json',datatype: "json",postData:data}).trigger("reloadGrid");
	}
	
	// 到账金额比较
	function payAmountFormatter(cellvalue, options, rowObject){
		var investAmount = rowObject.investAmount; //预约金额   
        if (investAmount != null && investAmount != "" && cellvalue != null && cellvalue != "") {  
            var be = investAmount - cellvalue;
			if(be < 0){
				return "<font style='background-color:green;color:#FFF'>"+cellvalue+"</font>";
			}else if(be > 0){
				return "<font style='background-color:red;;color:#FFF'>"+cellvalue+"</font>";
			}else{
				return cellvalue;
			}
        }else{
			return "&nbsp;";
		}
	}
	
	function payAmountUnFormatter( cellvalue, options, cell){    
		return cellvalue;    
	} 
	
	// 预计打款时间提醒
	function timeFormatter(cellvalue, options, rowObject){
		var commissionPayTime = rowObject.commissionPayTime; //获取起始时间  
        var currTime = new Date(); //获取当前时间  
        if (commissionPayTime != null && commissionPayTime != "") {  
            var cosj = new Date(commissionPayTime.replace("-", "/"));  
            var time = currTime.getDate() - cosj.getTime();
			var day = parseInt(time / (1000 * 60 * 60 * 24));
			if(day > 0){
				return "<font style='background-color:red;color:#FFF'>"+cellvalue+"</font>";
			}else if((-4 < day) && (day <= 0)){
				return "<font style='background-color:#FFFF33;'>"+cellvalue+"</font>";
			}else{
				return cellvalue;
			}
        }else{
			return "&nbsp;";
		}
	}
	
	// 记录当前列表表单值
	function recordeShare(rowid,share){
		preshare(rowid,share);
		if(share != '' && share != 0){
			$("body").data(""+rowid+"", ""+share+"");
		}else{
			$("body").removeData(""+rowid+"");
		}
	}
	
	
	// 已到账订单比例算法
	function alterPayedShare(payamount,refund,share,reshare){
		var re = /^[0-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
		if(re.test(reshare)){
			var p = payamount - refund;
			var s = reshare;
			var m = 0;
			var n = 0;
			if(share - payamount > 0){
				m = share - payamount;
			}
			// 新到账金额
			if(refund > 0){
				order_total_payed = order_total_payed - refund;
			}
			// 新配额未到账
			if(share - reshare != 0){
				if(s - p > 0){
					n = s - p - m;
					order_total_unpayed_shared = order_total_unpayed_shared -(-n);
				}
			}
			caProcessLine(order_total_payed,order_total_unpayed_shared,totle_preshare);
			// 新配额如果为0撤消配额,小额订单数量减1
			if(reshare == 0){
				order_total_limit_payed_count = order_total_limit_payed_count-1;
				caLimitProcessLine(order_total_limit_payed_count,order_total_limit_unpayed_shared_count,totle_limit_preshare);
			}
			return n;
		}
	}
	
	// 取消到账挂起任务
	function recoverPayedShare(fund,un,lastshare,thisshare){
		// 还原到账金额
		order_total_payed = order_total_payed -(-fund);
		// 还原未到账金额
		order_total_unpayed_shared = order_total_unpayed_shared - un;
		caProcessLine(order_total_payed,order_total_unpayed_shared,totle_preshare);
		if(lastshare == 0){
			order_total_limit_payed_count = order_total_limit_payed_count + 1;
			caLimitProcessLine(order_total_limit_payed_count,order_total_limit_unpayed_shared_count,totle_limit_preshare);
		}
	}
	
	// 已配额订单比例算法
	function alterSharedShare(share,reshare){
		var re = /^[0-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
		if(re.test(reshare)){
			var m = share - reshare;
			// 新配额未到账
			if(m != 0){
				order_total_unpayed_shared = order_total_unpayed_shared - m;
				caProcessLine(order_total_payed,order_total_unpayed_shared,totle_preshare);
			}
			// 新配额如果为0撤消配额,小额订单数量减1
			if(reshare == 0){
				order_total_limit_unpayed_shared_count = order_total_limit_unpayed_shared_count-1;
				caLimitProcessLine(order_total_limit_payed_count,order_total_limit_unpayed_shared_count,totle_limit_preshare);
			}
			return m;
		}
	}
	
	// 取消到账挂起任务
	function recoverSharedShare(un,lastshare,thisshare){
		// 还原未到账金额
		order_total_unpayed_shared = order_total_unpayed_shared -(-un);
		if(lastshare == 0){
			order_total_limit_unpayed_shared_count = order_total_limit_unpayed_shared_count + 1;
			caLimitProcessLine(order_total_limit_payed_count,order_total_limit_unpayed_shared_count,totle_limit_preshare);
		}
		caProcessLine(order_total_payed,order_total_unpayed_shared,totle_preshare);
	}
	
	// 未配额订单比例算法act = 1 act = 2 
	function preshare(rowid,share){
		var re = /^[0-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
		if(re.test(share)){
			var rowdata = jQuery("#newordersList").jqGrid('getRowData',rowid);
			var investAmount = rowdata.investAmount;
			
			var orivalue = $("body").data(""+rowid+"");
			if(typeof(orivalue) != 'undefined' && !isNaN(orivalue.length)){
				totle_preshare = totle_preshare - orivalue;
				if(investAmount < ${plot.lowAmountThreshold}){
					if(share == 0 || share == ''){
						totle_limit_preshare = totle_limit_preshare-1;
					}
				}
			}else{
				if(investAmount < ${plot.lowAmountThreshold} && share != 0){
					totle_limit_preshare++;
				}
			}
			totle_preshare = totle_preshare-(-share);
			caProcessLine(order_total_payed,order_total_unpayed_shared,totle_preshare);
			
			if(investAmount < ${plot.lowAmountThreshold}){
				caLimitProcessLine(order_total_limit_payed_count,order_total_limit_unpayed_shared_count,totle_limit_preshare);
			}
		}
	}

	
	// 计算额度进度条
	function caProcessLine(a1,a2,a3){
		var a4 = product_scale_max - a1 - a2 - a3;
		var p1 = a1*per_amount_percent;
		var p2 = a2*per_amount_percent;
		var p3 = a3*per_amount_percent;
		var p4 = 1 - p1 - p2 - p3 - 0.0005;
		//console.log(p1+"|"+p2+"|"+p3+"|"+p4);
		if(p1 >= 1){
			p1 = 1-0.0005;
			p2 = 0;p3 = 0;p4 = 0;
		}else if(p1+p2 >= 1){
			p2 = 1-p1-0.0005;
			p3 = 0;p4 = 0;
		}else if(p1+p2+p3 >= 1){
			p3 = 1-p1-p2-0.0005;
			p4 = 0;
		}
		$(".strip-1").animate({ width:p1*100+'%'},'fast');
		$(".strip-2").animate({ width:p2*100+'%'},'fast');
		$(".strip-3").animate({ width:p3*100+'%'},'fast');
		$(".strip-4").animate({ width:p4*100+'%'},'fast');
		$("#strip_1_val").html("&nbsp;&nbsp;"+a1+"&nbsp;&nbsp;");
		$("#strip_2_val").html("&nbsp;&nbsp;"+a2+"&nbsp;&nbsp;");
		$("#strip_3_val").html("&nbsp;&nbsp;"+a3+"&nbsp;&nbsp;");
		$("#strip_4_val").html("&nbsp;&nbsp;"+a4+"&nbsp;&nbsp;");
	}
	
	// 计算小额订单进度条
	function caLimitProcessLine(a1,a2,a3){
		var a4 = order_total_limit - a1 - a2 - a3;
		var p1 = a1*per_count_percent;
		var p2 = a2*per_count_percent;
		var p3 = a3*per_count_percent;
		var p4 = 1 - p1 - p2 - p3 - 0.0005;
		//console.log(p1+"|"+p2+"|"+p3+"|"+p4);
		if(p1 >= 1){
			p1 = 1-0.0005;
			p2 = 0;p3 = 0;p4 = 0;
		}else if(p1+p2 >= 1){
			p2 = 1-p1-0.0005;
			p3 = 0;p4 = 0;
		}else if(p1+p2+p3 >= 1){
			p3 = 1-p1-p2-0.0005;
			p4 = 0;
		}
		$(".limit-strip-1").animate({ width:p1*100+'%'},'fast');
		$(".limit-strip-2").animate({ width:p2*100+'%'},'fast');
		$(".limit-strip-3").animate({ width:p3*100+'%'},'fast');
		$(".limit-strip-4").animate({ width:p4*100+'%'},'fast');
		$("#limit_strip_1_val").html("&nbsp;&nbsp;"+a1+"&nbsp;&nbsp;");
		$("#limit_strip_2_val").html("&nbsp;&nbsp;"+a2+"&nbsp;&nbsp;");
		$("#limit_strip_3_val").html("&nbsp;&nbsp;"+a3+"&nbsp;&nbsp;");
		$("#limit_strip_4_val").html("&nbsp;&nbsp;"+a4+"&nbsp;&nbsp;");
	}
	
	<!--//  额度分配列表 -->
	
	// 数字验证
	function checkNumber(rowid,evt,object,flag){
		var key = window.event?event.keyCode:evt.which;
		var re = /^[0-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
		if(key != 8){
			var value = $(object).val();
			if(!re.test(value)){
				if("paste" == flag){
					$(object).val("");
				}else{
					if(value != null && value != ""){
						$(object).val(value.substring(0,value.length-1));
					}
				}
			}
		}
		
	}
	
	// 显示挂起任务
	function showMission(){
		//页面层
		layers = $.layer({
			type: 1,
			title: '挂起任务',
			shadeClose: true, 
			closeBtn: [0,true],
    		border : [5, 0.5, '#666', true],
    		offset: ['100px',''],
    		move: ['.xubox_title', true],
    		area: ['500px','auto'],
    		page: {
			html: $("#mission_list_box").html()
			}
    	});
	}
	</c:if>
	<c:if test="${plot.maxLowAmountClientCount == -1}">
	$(document).ready(function(){
		// 选中TAB
		$("#tab_${c}").attr("class","selected");
	});
	</c:if>
	</script>
	
	<style>
		.overview{width:100%;padding:0px 5px 10px;}
		.title{font-family:'黑体';font-size:1.2em;width:100%;}
		.sub-title{float:left;width:100%;padding:8px 0px 3px 0px;}
		.sub-title ul li{list-style:none;float: left;position: relative;margin-right:10px;}
		.sub-title ul li a{padding:3px;text-align:center;color:#FFF;}
		.sub-title-active a{background-color:#3879D9;}
		.sub-title-normal a{background-color:#999;}
		
		.sub-button{float:left;width:100%;padding:8px 0px;}
		.sub-button ul{float:left;width:600px;height:15px;}
		.sub-button ul li{list-style: none;float: left;position: relative;margin-right:10px;background-color:#FF6600;padding:3px;text-align:center;}
		.sub-button a{color:#FFF;cursor: pointer;}
	
		.top-1{border:1px #ccc solid;width:100%}
		.top-1-info{color:#999;}
		.top-1-des{;font-size:1.0em;color:#5DBDF1;font-size:1.0em;font-weight: bold;}
		.top-1-des lable{;font-size:1.0em;color:#000;font-weight: normal;}
		.box-dis{height:auto;padding:5px;}
		.box-scale{width:100%;height:16px;padding:0px;margin: 5px 0px;background:#FFFAE2;}
		.box-scale div{margin:0px;padding:0px;height:16px;float: left; display: block;color:#FFF;}
		.strip-1,.limit-strip-1{background-image: url(${base}/${skin}/images/bg/plan.gif);background-position: 0px -367px; background-repeat: repeat no-repeat;}
		.strip-2,.limit-strip-2{background-image: url(${base}/${skin}/images/bg/plan.gif);background-position: 0px -420px; background-repeat: repeat no-repeat;}
		.strip-3,.limit-strip-3{background-image: url(${base}/${skin}/images/bg/plan.gif);background-position: 0px -395px; background-repeat: repeat no-repeat;}
		.strip-4,.limit-strip-4{background-image: url(${base}/${skin}/images/bg/plan.gif);background-position: 0px -339px; background-repeat: repeat no-repeat;}
		
		.standby-mission{border:1px #ccc solid;width:100%;padding:5px 0px 5px 5px;font-weight:bold;}
		.standby-mission a{color:red;}

	</style>
	
	</head>
	<body class="skinMain">
		<%@ include file="tab.jsp" %>
		<div class="overview">
			<div class="title">
				<msg:message code='product.shortName'/><msg:message code="system.common.sign.colon"/>${product.shortName}
			</div>
			<!-- 如果产品共享额度，则按主产品的最大最小募集额度，否则，按子产品各个分解 -->
			<c:if test="${product.isTotalShare == 0}">
			<c:if test="${!empty subProductList}">
				<div class="sub-title">
					<ul>
						<c:forEach var="subProduct" items="${subProductList}" > 
						<c:if test="${!empty subProduct.type}">						
						<li id="a_subproduct_${subProduct.id}" class="sub-title-normal"><a  href="${base}${market_salemanage_manage_limit.controller}?c=${market_salemanage_manage_limit.code}&id=${product.id}&subProductId=${subProduct.id}">${subProduct.type}</a></li>
						</c:if>
						</c:forEach>
					</ul>
				</div>
			</c:if>
			</c:if>
		</div>
		<c:if test="${plot.maxLowAmountClientCount != -1}">
			<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td width="100%" class="top-1">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td>
									<table>
										<tr>
											<td>
													
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<div class="box-dis" <c:if test="${plot.productScaleMax == -1}">style="display:none;"</c:if>>
							<div class="top-1-info">额度分配情况(万元)</div>
							<div class="box-scale">
								<div class="strip-1"></div>
								<div class="strip-2"></div>
								<div class="strip-3"></div>
								<div class="strip-4"></div>
							</div>
							<!-- 
							<div class="top-1-des">
								<lable>图示</lable>&nbsp;&nbsp;
								<lable>产品销售规模:</lable>&nbsp;${plot.productScaleMax}&nbsp;
								<lable><img src="${base}/common/skin/images/chart/green.png" width="10" height="10" align="absmiddle">&nbsp;已到账:</lable><span id="strip_1_val" style="color:#59D70A">&nbsp;&nbsp;${plot.orderTotalPayed}&nbsp;&nbsp;</span>
								<lable><img src="${base}/common/skin/images/chart/yellow.png" width="10" height="10" align="absmiddle">&nbsp;已配额未到账:</lable><span id="strip_2_val" style="color:#FECB08">&nbsp;&nbsp;${plot.orderTotalUnPayedShared}&nbsp;&nbsp;</span>
								<lable><img src="${base}/common/skin/images/chart/purple.png" width="10" height="10" align="absmiddle">&nbsp;预分配:</lable><span id="strip_3_val" style="color:#AD05BD">&nbsp;&nbsp;&nbsp;&nbsp;</span>
								<lable><img src="${base}/common/skin/images/chart/blue.png" width="10" height="10" align="absmiddle">&nbsp;剩余:</lable><span id="strip_4_val"  style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;</span>
							</div>
							 -->
						<table style="border-collapse:collapse;" border="1">
							<tr>
								<th width="100">产品销售规模</th>
								<th width="80">已到账</th>
								<th width="100">已配额未到账</th>
								<th width="80">预分配</th>
								<th width="80">剩余</th>
							</tr>
							<tr >
								<th>${plot.productScaleMax}</th>
								<th><img src="${base}/common/skin/images/chart/green.png" width="10" height="10" >&nbsp;${plot.orderTotalPayed}</th>
								<th><img src="${base}/common/skin/images/chart/yellow.png" width="10" height="10" >&nbsp;${plot.orderTotalUnPayedShared}</th>
								<th><img src="${base}/common/skin/images/chart/purple.png" width="10" height="10" >&nbsp;<span id="strip_3_val" ></span></th>
								<th><img src="${base}/common/skin/images/chart/blue.png" width="10" height="10" >&nbsp;<span id="strip_4_val" ></span></th>
							</tr>
						</table>
						</div>
						<div class="box-dis" id="limit_box"> 
							<div class="top-1-info">小额订单分配情况(份)</div>
							<div class="box-scale">
								<div class="limit-strip-1"></div>
								<div class="limit-strip-2"></div>
								<div class="limit-strip-3"></div>
								<div class="limit-strip-4"></div>
							</div>
							<!-- 
							<div class="top-1-des">
								<lable>图示</lable>&nbsp;&nbsp;
								<lable>小额投资人数限制(小额订单额度阀值${plot.lowAmountThreshold}万元):</lable>&nbsp;${plot.maxLowAmountClientCount}&nbsp;
								<lable><img src="${base}/common/skin/images/chart/green.png" width="10" height="10" align="absmiddle">&nbsp;已到账:</lable><span id="strip_1_val" style="color:#59D70A">&nbsp;&nbsp;${plot.orderTotalLimitPayedCount}&nbsp;&nbsp;</span>
								<lable><img src="${base}/common/skin/images/chart/yellow.png" width="10" height="10" align="absmiddle">&nbsp;已配额未到账:</lable><span id="strip_2_val" style="color:#FECB08">&nbsp;&nbsp; ${plot.orderTotalLimitUnPayedSharedCount}&nbsp;&nbsp;</span>
								<lable><img src="${base}/common/skin/images/chart/purple.png" width="10" height="10" align="absmiddle">&nbsp;预分配:</lable><span id="limit_strip_3_val" style="color:#AD05BD">&nbsp;&nbsp;&nbsp;&nbsp;</span>
								<lable><img src="${base}/common/skin/images/chart/blue.png" width="10" height="10" align="absmiddle">&nbsp;剩余:</lable><span id="limit_strip_4_val"  style="color:red">&nbsp;&nbsp;${plot.productScaleMax}&nbsp;&nbsp;</span>
							</div>
							-->
						<table style="border-collapse:collapse;" border="1">
							<tr>
								<th width="260">小额投资人数限制(小额订单额度阀值${plot.lowAmountThreshold}万元)</th>
								<th width="80">已到账</th>
								<th width="80">已配额未到账</th>
								<th width="80">预分配</th>
								<th width="80">剩余</th>
							</tr>
							<tr >
								<th>${plot.maxLowAmountClientCount}</th>
								<th><img src="${base}/common/skin/images/chart/green.png" width="10" height="10" >&nbsp;${plot.orderTotalLimitPayedCount}</th>
								<th><img src="${base}/common/skin/images/chart/yellow.png" width="10" height="10" >&nbsp;${plot.orderTotalLimitUnPayedSharedCount}</th>
								<th><img src="${base}/common/skin/images/chart/purple.png" width="10" height="10" >&nbsp;<span id="limit_strip_3_val"></span></th>
								<th><img src="${base}/common/skin/images/chart/blue.png" width="10" height="10" >&nbsp;<span id="limit_strip_4_val" ></span></th>
							</tr>
						</table>
						</div>
					</td>
				</tr>
				<tr ><td width="100%" class="standby-mission">当前挂起操作:<a id="standby_mission" href="javascript:showMission();">[0]</a>项<span id="standby_mission_tips"></span></td></tr>
			</table>
			<form name="distribute_limit_orders" id="distribute_limit_orders">
				<input type="hidden" name="c" value="${c}"/>
				<input type="hidden" name="prompt" value="productName"/>
				<input type="hidden" name="productName" value="${product.name}"/>
				<input type="hidden" name="productId" value="${product.id}"/>
				<table id="newordersList"><tr><td>&nbsp;</td></tr></table>
				<div id="newordersListPaged"></div>
				<table id="sharedordersList"><tr><td>&nbsp;</td></tr></table>
				<div id="sharedordersListPaged"></div>
				<table id="payedordersList"><tr><td>&nbsp;</td></tr></table>
				<div id="payedordersListPaged"></div>
				
			</form>
			
			<!-- 挂起任务 -->
			<div style="display:none" id="mission_list_box">
				<style>
				.mission-list div{width:15px;height:15px;background:#999;color:#FFF;cursor: pointer;}
				.mission-list td{border-bottom:1px #CCC dashed;padding-bottom:5px;}
				</style>
				<table class="mission-list" align="left" width="490" cellspacing="5" cellpadding="0" id="mission_list">
					<tr align="left">
						<th width="30%">订单编号</th><th width="21%">已分配额度</th><th  width="21%">修改后的额度</th><th  width="21%">退款金额</th><th width="7%">操作</th>
					</tr>
				</table>
			</div>
		</c:if>
		<c:if test="${plot.maxLowAmountClientCount == -1}">
			<span style='padding:0px 5px;color:#999;'>当前产品不限制小额订单<span>
		</c:if>
		
		<div style="float:left;width:100%;height:36px;"></div>
		<div class="toolbar">
			<div class="left"></div>
			<div class="center">
				<c:if test="${plot.maxLowAmountClientCount != -1}"><input type="button" class="btn2" id="btn_save" value="确认分配" /></c:if>
				<input type="button" class="btn2" onclick="window.location.href='${base}${market_salemanage.controller}?c=${market_salemanage.code}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
			</div>
			<div class="right"></div>
		</div>

		
	</body>
</html>