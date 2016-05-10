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
	});
	
	// 当前排序
	var curr_sort=0;
	// 行变更
	var rowCount = 0;
	// 是否需要计算额度进度条
	var isCaAmount = false;
	var isCaLimit = false;
	// 每一份金额占比
	var per_amount_percent;
	// 每一份数量占比
	var per_count_percent;
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

		/* -----------------------  初始进度条 END-------------------------- */
	
		<!--  额度分配列表 -->
		jQuery("#newordersList").jqGrid({
			datatype: "local",
			colNames:["","<msg:message code="product.sale.distribute.orderNum"/>"
				,"<msg:message code="product.sale.distribute.clientName"/>"
				,"<msg:message code="product.sale.distribute.iDCard"/>"
				,"<msg:message code="product.sale.distribute.rmCoName"/>"
				,"<msg:message code="product.sale.distribute.teamName"/>"
				,"<msg:message code="product.sale.distribute.orderTime"/>"
				,"<msg:message code="product.sale.distribute.commissionPayTime"/>"
				,"<msg:message code="product.sale.distribute.investAmount"/>"
				,"<msg:message code="product.sale.distribute.preShare"/>"
				,""],
			colModel:[{name:'id',index:'id',hidden:true}
			,{name:'orderNumber',index:'orderNumber', width:'15%',align:'center',sortable: true,frozen : true,formatter:function(cellvalue, options, rowObject){
					// 判断当前列表有无数据
					$("#btn_save").attr("disabled",false);
					return "<a href='${base}/market/order/detail?c=${market_order_detail.code}&id="+rowObject.id+"'>"+cellvalue+"</a>"
				}
			}
			,{name:'clientName',index:'clientName', width:'10%',align:'center',sortable: false}
			,{name:'iDCard',index:'iDCard', width:'15%',align:'center',sortable: false,cellattr: function(rowid, cv, rawObject, cm, rdata) {
				if(curr_sort == 0){
					var orivalue = $("DIV").data(cv);
					if(typeof(orivalue) != 'undefined'){
						var firstrowid = orivalue.firstrowid;
						var linec = orivalue.linec+1;
						$("DIV").data(cv,{firstrowid:firstrowid,linec:linec});
						return "id='iDCard_"+rowid+"' style='display:none'";
					}else{
						$("DIV").data(cv,{firstrowid:rowid,linec:1});
						return "id='iDCard_"+rowid+"' rowspan=1";
					}
				}
                }
			}
			,{name:'rmName',index:'rmName', width:'10%',align:'center',sortable: false,formatter:rmNameFormatter}
			,{name:'teamShortName',index:'teamShortName', width:'8%',align:'center',sortable: false}
			,{name:'orderTime',index:'orderTime', width:'10%',align:'center',sortable: false}
			,{name:'commissionPayTime',index:'commissionPayTime', width:'10%',align:'center',sortable: false}
			,{name:'investAmount',index:'investAmount', width:'10%',align:'center',sortable: false}
			,{name:'share',index:'share', width:'12%',align:'center',sortable: false,formatter:newOrderActor}
			,{name:'rmShortName',index:'rmShortName', hidden:true}
			],
			mtype:"POST",
			postData:{c:"${c}",productId:"${product.id}",operate:"1"},
			loadonce: true,
			rowNum:15,
			rowTotal: 1000,
			rowList : [20,30,50],
			scroll:1,			
			pager: '#newordersListPaged',
			autoheight: true,
			autowidth: true,
			viewrecords: true,
			multiselect: true,
			rownumbers: true,
			gridview: true,
			jsonReader: {
				repeatitems: false
			},
			beforeSelectRow: function (rowid, e) {
				var $myGrid = $(this),
				i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
				cm = $myGrid.jqGrid('getGridParam', 'colModel');
				return (cm[i].name === 'cb');
			},
			gridComplete: function() {
				$.each( $('DIV').data(),function(k, v) {
					//console.log("key:"+k+"-id:"+v.firstrowid+"-v:"+v.linec);
					$("#iDCard_"+v.firstrowid).attr("rowspan",v.linec);
				});
			}
		});
		$("#newordersList").setGridHeight($(window).height() - 300); 
		
 		jQuery("#newordersList").jqGrid('navGrid','#newordersListPaged',{del:false,add:false,edit:false},{},{},{},{multipleSearch:false});
		
		jQuery("#newordersList").jqGrid('navButtonAdd','#newordersListPaged',{
			caption: "订单排重",
			title: "相同证件号订单聚集",
			onClickButton : function (){
				$("DIV").removeData();
				reloadOrders(0);
			}
		});
		jQuery("#newordersList").jqGrid('navButtonAdd','#newordersListPaged',{
			caption: "排序一",
			title: "预约金额+预计打款时间",
			onClickButton : function (){
				reloadOrders(1);
			}
		});
		jQuery("#newordersList").jqGrid('navButtonAdd','#newordersListPaged',{
			caption: "排序二",
			title: "预计打款时间+预约金额",
			onClickButton : function (){
				reloadOrders(2);
			}
		});
		jQuery("#newordersList").jqGrid('navButtonAdd','#newordersListPaged',{
			caption: "排序三",
			title: "预约金额+预约时间",
			onClickButton : function (){
				reloadOrders(3);
			}
		});
		jQuery("#newordersList").jqGrid('navButtonAdd','#newordersListPaged',{
			caption: "排序四",
			title: "预约时间+RM信用值",
			onClickButton : function (){
				reloadOrders(4);
			}
		}); 
		
		// 单选事件
		$(".cbox").live("click",function (){
			var id = $(this).parent().parent().attr("id");
			var rowdata = $("#newordersList").getRowData(id);
			var intv = rowdata.investAmount;
			var checkboxs = $("#jqg_newordersList_"+id).attr("checked");
			if(checkboxs == 'checked'){
				$("#share_"+id).val(intv);
				recordeShare(id,intv,true);
			}else{
				$("#share_"+id).val('');
				recordeShare(id,0,true);
			}
		});
		
		// 全选事件
		$("#cb_newordersList").click(function(){
			var checkboxs = $("#cb_newordersList").attr("checked");
			var rowids = jQuery("#newordersList").jqGrid('getDataIDs'); 
			$.each(rowids, function (n, id) {
				var rowdata = $("#newordersList").getRowData(id);
				var intv = rowdata.investAmount;
				if(checkboxs == 'checked'){
					$("#share_"+id).val(intv);
					recordeShare(id,intv,false);
				}else{
					$("#share_"+id).val('');
					recordeShare(id,0,false);
				}
			});
			caProcessLine(order_total_payed,order_total_unpayed_shared,totle_preshare);
			caLimitProcessLine(order_total_limit_payed_count,order_total_limit_unpayed_shared_count,totle_limit_preshare);
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
		
			var layers = top.layer.confirm("<msg:message code='product.sale.distribute.confirm'/>",  function(){
				top.layer.close(layers);
				layers =  top.layer.load(0,2);
				jQuery.ajax({
					url:"distribute/distributeall.json",
					type: "post",
					contentType : 'application/x-www-form-urlencoded',
					data:$("#distribute_new_orders").serializeArray(), 
					dataType : 'json',
					success:function(data){
						if(data.status == 'success'){
							top.$.layer({
								area : ['auto','auto'],
								dialog : {msg:'<msg:message code='info.operate.success'/>',type : 9}	
							});
							top.layer.close(layers);
							window.location.href='${base}${market_salemanage_manage_neworder.controller}?c=${market_salemanage_manage_neworder.code}&id=${product.id}&subProductId=${subProduct.id}';
						}else{
							top.layer.msg("error");
						}
					}
				});
			});
		});
		<!--//  额度分配列表 -->
		
		// 默认加载额度分配列表数据
		<!--// 初始化方法 -->
		reloadOrders(1);
		
	});
	
	//理财顾问姓名格式化
	function rmNameFormatter(cellvalue, options, rowObject){
		return cellvalue+"("+rowObject.rmShortName+")";
	}
	
	//额度分配列表操作功能
	function newOrderActor(cellvalue, options, rowObject){
		var id=rowObject.id;
		var redata = $("body").data(""+id+"");
		if(typeof(redata) == "undefined"){
			redata = "";
		}
		var orderForm =  "<input type='hidden' name='saleManageList["+rowCount+"].orderId' value='"+id+"'/><input class='input_share' type='text' name='saleManageList["+rowCount+"].share' id='share_"+id+"' value='"+redata+"' onchange='recordeShare("+id+",this.value,true);'  onkeyup=\"checkNumber("+id+",event,this,'edit');\"  onpropertychange=\"checkNumber("+id+",event,this,'edit');\" oninput=\"checkNumber("+id+",event,this,'edit');\"/>";
		rowCount++;
		return orderForm;
	}
	
	// 重新加载列表数据
	function reloadOrders(operate){
		if(operate == 0){
			$("#cb_newordersList").attr("disabled",true);
		}else{
			$("#cb_newordersList").attr("disabled",false);
		}
		curr_sort = operate;
		rowCount = 0;
		var data = {};
		if('${product.isTotalShare}' == '1'){
			data = {c:"${c}",productId:"${product.id}",operate:operate,isTotalShare:1};
		}else{
			var subProductId;
			if('${subProduct.id}' != ''){
				subProductId = "${subProduct.id}";
			}else{
				subProductId = "${subProductList[0].id}";
			}
			$("#a_subproduct_"+subProductId).attr("class","sub-title-active");
			data = {c:"${c}",subProductId:subProductId,operate:operate,isTotalShare:0};
		}
		jQuery("#newordersList").jqGrid("setGridParam",{url:'distribute/newordersgrid.json',datatype: "json",postData:data}).trigger("reloadGrid");
	}
	
	// 记录当前列表表单值
	function recordeShare(rowid,share,isgraph){
		preshare(rowid,share,isgraph);
		if(share != '' && share != 0){
			$("body").data(""+rowid+"", ""+share+"");
		}else{
			$("body").removeData(""+rowid+"");
		}
	}
		
	// 未配额订单比例算法act = 1 act = 2 
	function preshare(rowid,share,isgraph){
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
			if(isgraph){
				caProcessLine(order_total_payed,order_total_unpayed_shared,totle_preshare);
				
				if(investAmount < ${plot.lowAmountThreshold}){
					caLimitProcessLine(order_total_limit_payed_count,order_total_limit_unpayed_shared_count,totle_limit_preshare);
				}
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
			p1 = 1 - 0.0005;
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
	
	// 自动分配
	function autoAllPreshare(id){
		var rowdata = $("#newordersList").getRowData(id);
		var checkboxs = $("#jqg_newordersList_"+id).attr("checked");
		if(checkboxs == 'checked'){
			$("#share_"+id).val(rowdata.investAmount);
		}else{
			$("#share_"+id).val('');
		}
	}

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
		.top-1-des{font-size:1.0em;color:#5DBDF1;font-size:1.0em;font-weight: bold;}
		.top-1-des lable{font-size:1.0em;color:#000;font-weight: normal;}
		.box-dis{height:auto;padding:5px;}
		.box-scale{float:left;width:100%;height:16px;padding:0px;margin: 5px 0px;background:#FFFAE2;}
		.box-scale div{float:left;margin:0px;padding:0px;height:16px;float: left; display: block;color:#FFF;}
		.strip-1,.limit-strip-1{background-image: url(${base}/${skin}/images/bg/plan.gif);background-position: 0px -367px; background-repeat: repeat no-repeat;}
		.strip-2,.limit-strip-2{background-image: url(${base}/${skin}/images/bg/plan.gif);background-position: 0px -420px; background-repeat: repeat no-repeat;}
		.strip-3,.limit-strip-3{background-image: url(${base}/${skin}/images/bg/plan.gif);background-position: 0px -395px; background-repeat: repeat no-repeat;}
		.strip-4,.limit-strip-4{background-image: url(${base}/${skin}/images/bg/plan.gif);background-position: 0px -339px; background-repeat: repeat no-repeat;}
	
		#newordersList a:hover{color:#FF6600;}
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
						<li id="a_subproduct_${subProduct.id}" class="sub-title-normal"><a  href="${base}${market_salemanage_manage_neworder.controller}?c=${market_salemanage_manage_neworder.code}&id=${product.id}&subProductId=${subProduct.id}">${subProduct.type}</a></li>
						</c:if>
						</c:forEach>
					</ul>
				</div>
			</c:if>
			</c:if>
		</div>
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
					<div class="box-dis" <c:if test="${plot.productScaleMax < 0}">style="display:none;"</c:if>>
						<div class="top-1-info">额度分配情况(万元)</div>
						<div class="box-scale">
							<div class="strip-1"></div>
							<div class="strip-2"></div>
							<div class="strip-3"></div>
							<div class="strip-4"></div>
						</div>
					   <!-- <div class="top-1-des">
							<lable>图示</lable>&nbsp;&nbsp;
							<lable>产品销售规模:</lable>&nbsp;${plot.productScaleMax}&nbsp;
							<lable><img src="${base}/common/skin/images/chart/green.png" width="10" height="10" align="absmiddle">&nbsp;已到账:</lable><span id="strip_1_val" style="color:#59D70A">&nbsp;&nbsp;${plot.orderTotalPayed}&nbsp;&nbsp;</span>
							<lable><img src="${base}/common/skin/images/chart/yellow.png" width="10" height="10" align="absmiddle">&nbsp;已配额未到账:</lable><span id="strip_2_val" style="color:#FECB08">&nbsp;&nbsp;${plot.orderTotalUnPayedShared}&nbsp;&nbsp;</span>
							<lable><img src="${base}/common/skin/images/chart/purple.png" width="10" height="10" align="absmiddle">&nbsp;预分配:</lable><span id="strip_3_val" style="color:#AD05BD">&nbsp;&nbsp;&nbsp;&nbsp;</span>
							<lable><img src="${base}/common/skin/images/chart/blue.png" width="10" height="10" align="absmiddle">&nbsp;剩余:</lable><span id="strip_4_val"  style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;</span>
						</div>
						 --> 
						<table style="border-collapse:collapse;" border="1" bordercolor="black">
							<tr >
								<th width="100">产品销售规模</th>
								<th width="80">已到账</th>
								<th width="100">已配额未到账</th>
								<th width="80">预分配</th>
								<th width="80">剩余</th>
							</tr>
							<tr>
								<th>${plot.productScaleMax}</th>
								<th><img src="${base}/common/skin/images/chart/green.png" width="10" height="10" >&nbsp;${plot.orderTotalPayed}</th>
								<th><img src="${base}/common/skin/images/chart/yellow.png" width="10" height="10" >&nbsp;${plot.orderTotalUnPayedShared}</th>
								<th><img src="${base}/common/skin/images/chart/purple.png" width="10" height="10" >&nbsp;<span id="strip_3_val" ></span></th>
								<th><img src="${base}/common/skin/images/chart/blue.png" width="10" height="10" >&nbsp;<span id="strip_4_val" ></span></th>
							</tr>
						</table>
					</div>
					<div class="box-dis" id="limit_box" <c:if test="${plot.maxLowAmountClientCount < 0}">style="display:none;"</c:if>> 
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
							<lable><img src="${base}/common/skin/images/chart/yellow.png" width="10" height="10" align="absmiddle">&nbsp;已配额未到账:</lable><span id="strip_2_val" style="color:#FECB08">&nbsp;&nbsp;${plot.orderTotalLimitUnPayedSharedCount}&nbsp;&nbsp;</span>
							<lable><img src="${base}/common/skin/images/chart/purple.png" width="10" height="10" align="absmiddle">&nbsp;预分配:</lable><span id="limit_strip_3_val" style="color:#AD05BD">&nbsp;&nbsp;&nbsp;&nbsp;</span>
							<lable><img src="${base}/common/skin/images/chart/blue.png" width="10" height="10" align="absmiddle">&nbsp;剩余:</lable><span id="limit_strip_4_val"  style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;</span>
						</div>
						 -->
						 <table style="border-collapse:collapse;" border="1">
							<tr>
								<th width="260">小额投资人数限制(小额订单额度阀值${plot.lowAmountThreshold}万元)</th>
								<th width="80">已到账</th>
								<th width="100">已配额未到账</th>
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
		</table>
		<form name="distribute_new_orders" id="distribute_new_orders">
			<input type="hidden" name="c" value="${c}"/>
			<input type="hidden" name="prompt" value="productName"/>
			<input type="hidden" name="productName" value="${product.name}"/>
			<input type="hidden" name="productId" value="${product.id}"/>
			<table id="newordersList"><tr><td>&nbsp;</td></tr></table>
			<div id="newordersListPaged"></div>
		</form>
		<div style="float:left;width:100%;height:36px;"></div>
		<div class="toolbar">
			<div class="left"></div>
			<div class="center">
				<input type="button" class="btn2" id="btn_save" value="确认分配" disabled="disabled"/>
				<input type="button" class="btn2" onclick="window.location.href='${base}${market_salemanage.controller}?c=${market_salemanage.code}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
			</div>
			<div class="right"></div>
		</div>
	</body>
</html>