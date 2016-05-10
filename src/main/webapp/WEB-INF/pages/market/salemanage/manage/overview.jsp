<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
	<script type="text/javascript" src="${base}/common/js/jqplot/excanvas.min.js"></script>
	<script type="text/javascript" src="${base}/common/js/jqplot/jquery.jqplot.min.js"></script>
	<script type="text/javascript" src="${base}/common/js/jqplot/plugins/jqplot.barRenderer.min.js"></script>
	<script type="text/javascript" src="${base}/common/js/jqplot/plugins/jqplot.pointLabels.min.js"></script>
	<script type="text/javascript" src="${base}/common/js/jqplot/plugins/jqplot.pieRenderer.min.js"></script>
	<script type="text/javascript" src="${base}/common/js/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
	<script type="text/javascript" src="${base}/common/js/jqplot/plugins/jqplot.dateAxisRenderer.min.js"></script>
	<script type="text/javascript" src="${base}/common/js/jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
	<script type="text/javascript" src="${base}/common/js/jqplot/plugins/jqplot.canvasTextRenderer.min.js"></script>
	<link rel="stylesheet" type="text/css" href="${base}/${skin}/css/jquery.jqplot.min.css" />
	<script type="text/javascript">
	var isTotalShare = '${product.isTotalShare}';
	var curr_btn = 0;
	var labels;
	var line;
	var seriesColors;
	var legends;
	var return_status = false;// 请求状态
	var currStatus = 0;
	var this_layers = 0;
	
	/* -- 产品销售规模 -- */
	var product_scale_max = '${plot.productScaleMax}';// 产品最大募集规模
	product_scale_max = parseFloat(product_scale_max);
	var product_scale_min = '${plot.productScaleMin}';// 产品最小募集规模
	product_scale_min = parseFloat(product_scale_min);
	
	/* -- 到账金额 -- */
	var order_total_payed = ${plot.orderTotalPayed};
	var order_total_correct_payed = ${plot.orderTotalCorrectPayed};
	/* -- 订单总额 -- */
	var order_total = ${plot.orderTotal};
	var order_total_shared = ${plot.orderTotalShared};
	var order_total_unshared = order_total-order_total_shared;
	/* -- 订单总数 -- */
	var order_total_count = ${plot.orderTotalCount};
	var order_total_shared_count = ${plot.orderTotalSharedCount};
	var order_total_payed_count = ${plot.orderTotalPayedCount};
	/* -- 小额订单总额 -- */
	var order_total_limit = ${plot.orderTotalLimit};
	var order_total_limit_payed_shared = ${plot.orderTotalLimitPayedShared};
	/* -- 单证归集情况 -- */
	var order_total_correct_payed_count = ${plot.orderTotalCorrectPayedCount};
	var order_total_correct_collect_count = ${plot.orderTotalCorrectCollectCount};
	/* -- 每日到账情况 -- */
	var order_day_amount = ${plot.orderDaysAmountListJSON};

	
	// -- 图表配置 START
	var seriesDefaults_bar = {
			renderer:$.jqplot.BarRenderer,
            shadowAngle: 135,
            rendererOptions: {
				barDirection: 'horizontal',
				highlightMouseDown: true   
			},
            pointLabels: {show: true, formatString: '%.2f'}
		}
	var seriesDefaults_bar_d = {
			renderer:$.jqplot.BarRenderer,
            shadowAngle: 135,
            rendererOptions: {
				barDirection: 'horizontal',
				highlightMouseDown: true   
			},
            pointLabels: {show: true, formatString: '%d'}
		}
	var seriesDefaults_point_f = { 
			showMarker:true,
			pointLabels: { show:true ,formatString: '%.2f'} 
		}
	var seriesDefaults_point_d = { 
			showMarker:true,
			pointLabels: { show:true ,formatString: '%d'} 
		}
	var legend =  {
		show: true,
		placement: 'outside',
		labels: labels,
		location: 'e'
	}
	var axes_bar = {
		yaxis: {
            renderer: $.jqplot.CategoryAxisRenderer
        }
	}
	var axes_bar_d = {
		yaxis: {
            renderer: $.jqplot.CategoryAxisRenderer
        },
		xaxis:{
			tickOptions:{formatString:'%d'}
		}
	}
	
	var axes_point = {
		xaxis:{
			renderer:$.jqplot.DateAxisRenderer,
			//min:getPreday('${product.raiseStartTime}'),
			min:'${product.raiseStartTime}',
			tickInterval:'1 day',
			tickRenderer: $.jqplot.CanvasAxisTickRenderer,
            tickOptions: {
				formatString:'%m-%#d',
                angle: -30
            }
		}
	}
	// -- 图表配置 END
	
	$(document).ready(function(){

		$("#tab_${c}").attr("class","selected");
		currStatus = getCurrStatus();
		// 初始化子产品选中状态
		$("#a_subproduct_${subProduct.id}").attr("class","sub-title-active");

		// 初始化产品状态
		curr_btn = currStatus;
		setProductStatus();
		
		if("${subProduct.isFavorites}" != "1"){
			$("#favor").html("<a>开启收藏</a>");
		}else{
			$("#favor").html("<a>关闭收藏</a>");
		}
		if("${subProduct.isOrder}" != "1"){
			$("#preorder").html("<a>开启预约</a>");
		}else{
			$("#preorder").html("<a>关闭预约</a>");
		}
		if("${subProduct.isRemittance}" == "1" && "${subProduct.isOrder}" == "1"){
			$("#privateorder").html("<a>关闭预约停止打款</a>");
		}else{
			$("#privateorder").html("<a>开启预约开启打款</a>");
		}
		
		// 设置是否显示图表
		var isGraph = true;
		// 获取主产品销售状态
		var t_saleStatus = "${product.sellStatus}";
		// 获取产品类型
		var t_productType = "${product.type}";
		// 如果是私募产品，成立后不再统计
		if((t_productType == "2" || t_productType == "5") && (t_saleStatus == "5" || t_saleStatus == "6")){
			isGraph  = false;
		}else{
			$(".box-chart").css("display","block");
		}
		
		if(isGraph){
		/*初始图表数据
		*/
		var sineRenderer_amount = function() {
			var list = new Array(3);
			list[0] = new Array();
			list[1] = new Array();
			list[2] = new Array();
			if(order_day_amount.length != 0){
				$.each(order_day_amount, function(i, item) {
					var a = new Array(item.orderTime, item.payAmount);
					var b = new Array(item.orderTime, item.investAmount);
					var c = new Array(item.orderTime, item.share);
					list[0][i] = a;
					list[1][i] = b;
					list[2][i] = c;
				});
			}else{
				list[0][0] = new Array('${product.raiseStartTime}',0);
				list[1][0] = new Array('${product.raiseStartTime}',0);
				list[2][0] = new Array('${product.raiseStartTime}',0);
			}
			return list;
		};
		
		var sineRenderer_count = function() {
			var list = new Array(2);
			list[0] = new Array();
			list[1] = new Array();
			if(order_day_amount.length != 0){
				var new_order_amount = 0;
				var order_payed_amount = 0;
				$.each(order_day_amount, function(i, item) {
					new_order_amount+=item.investAmount;
					order_payed_amount+=item.payAmount;
					var a = new Array(item.orderTime, new_order_amount);
					var b = new Array(item.orderTime, order_payed_amount);
					list[0][i] = a;
					list[1][i] = b;
				});
			}else{
				list[0][0] = new Array('${product.raiseStartTime}',0);
				list[1][0] = new Array('${product.raiseStartTime}',0);
			}
			return list;
		};
	
		
		// 每日到账情况
		//var line1=[[['2008-09-15 4:00PM',2], ['2008-09-12 4:00PM',7]],[['2008-08-12 4:00PM',1], ['2008-09-12 4:00PM',3]]];
		legends = $.extend({},legend,{labels:['新到账金额','新预约金额','新配额金额']});
		seriesColors = ['#FF6600', '#EAA228','#4BB2C5'];
		$.jqplot('graph_product_payed', [], {
			//animate: true,
			dataRenderer: sineRenderer_amount,
			seriesDefaults: seriesDefaults_point_f,
			axes:axes_point,
			legend: legends,
			seriesColors:seriesColors
		});
		
		
		// 逐日预约情况
		//line = [[14, 32, 41, 44, 40, 47, 53, 67],[11, 31, 14, 24, 50, 17, 23, 17]];
		legends = $.extend({},legend,{labels:['预约金额累计','到账金额累计']});
		seriesColors = ['#FF6600', '#4BB2C5'];
		$.jqplot('graph_product_order', [], {
			dataRenderer: sineRenderer_count,
			seriesDefaults: seriesDefaults_point_f,
			axes:axes_point,
			legend: legends,
			seriesColors:seriesColors
		});
	
		
		// 产品销售规模情况
		//line = [['',product_scale_min/10000], ['产品销售规模上限',product_scale_max/10000]];
		//[[[2,1], [6,2], [7,3]], [[7,1], [5,2],[3,3]], [[14,1], [9,2], [9,3]]
		var product_scale = 0;
		if(product_scale_max != -1){
			product_scale = product_scale_max-product_scale_min;
		}
		line = [[[product_scale_min,1]], [[product_scale,1]]];
		legends = $.extend({},legend,{labels:['产品销售规模下限','产品销售规模上限']});
		$.jqplot('chart_product_scale', line, {
			//animate: true,
            stackSeries: true,
            seriesDefaults:seriesDefaults_bar,
            legend: legends,
            axes: axes_bar
        });
		
		
		// 产品到账情况
		line = [[[order_total_correct_payed,1]], [[order_total_payed - order_total_correct_payed,1]]];
		legends = $.extend({},legend,{labels:['锁定到账','非合法到账']});
           
		$.jqplot('chart_product_payed', line, {
			//animate: true,
			stackSeries: true,
            seriesDefaults: seriesDefaults_bar,
			legend: legends,
            axes:axes_bar
        });
		
		// 订单总额
		line = [[[order_total_shared,1]], [[order_total-order_total_shared,1]]];
		legends = $.extend({},legend,{labels:['已配额','未配额']});
           
		$.jqplot('chart_product_neworder', line, {
			//animate: true,
			stackSeries: true,
            seriesDefaults: seriesDefaults_bar,
			legend: legends,
            axes:axes_bar
        });
		
		// 订单总数
		line = [[[order_total_shared_count,1]], [[order_total_count - order_total_shared_count,1]]];
		legends = $.extend({},legend,{labels:['已配额','未配额']});
           
		$.jqplot('chart_product_orders', line, {
			//animate: true,
			stackSeries: true,
            seriesDefaults: seriesDefaults_bar_d,
			legend: legends,
            axes:axes_bar_d
        });
		
		// 小额订单总额
		if(order_total_limit != -1){
			line = [[[order_total_limit_payed_shared,1]], [[order_total_limit - order_total_limit_payed_shared,1]]];
			legends = $.extend({},legend,{labels:['配额已到账','配额未到账']});
		
			$.jqplot('chart_product_limit', line, {
				//animate: true,
				stackSeries: true,
				seriesDefaults: seriesDefaults_bar,
				legend: legends,
				axes:axes_bar_d
			});
		}
		
		// 单证归集情况
		line = [[[order_total_correct_collect_count,1]], [[order_total_correct_payed_count - order_total_correct_collect_count,1]]];
		legends = $.extend({},legend,{labels:['已归集','未归集']});
    
		$.jqplot('chart_product_collection', line, {
			//animate: true,
			stackSeries: true,
            seriesDefaults: seriesDefaults_bar_d,
			legend: legends,
            axes:axes_bar_d
        });
		
		// 格式化数字添加千位符号
		//给数字类型加上千分符 
		$(".mt").each(function(){
			var value=$(this).text();
			var revalue = numsplitor(value);
			$(this).text(revalue);
		}); 
		
		}
		
		// 状态按钮变更
		// 打款
		$("#_2").click(function(){
		if(curr_btn == 1 || curr_btn == 3){
			var tdata = {c:"${c}",isRemittance:"1",subProductId:"${subProduct.id}",productId:"${product.id}",isTotalShare:isTotalShare};
			var layers = top.layer.confirm("您确认要执行<span style='color:red'>"+$(this).text()+"</span>操作吗？",  function(){
				top.layer.close(layers);
				layers =  top.layer.load(0,2);
				var flag = false;
				jQuery.ajax({
					url:"overview/check_docconfig.json",type: "post",contentType : 'application/x-www-form-urlencoded',
					data:{c:"${c}",productId:"${product.id}"},dataType : 'json',
					success:function(data){
						if(data.status == '1'){
							jQuery.ajax({
								url:"overview/alterstatus.json",type: "post",contentType : 'application/x-www-form-urlencoded',
								data:tdata,dataType : 'json',
								success:function(data){
									if(data.status == 'success'){
										curr_btn = 2;
										setProductStatus();
										top.layer.close(layers);
									}
								}
							});
						}else{
							top.layer.close(layers);
							top.layer.msg("请先配置该产品的合同送寄参数!");
						}
					}
				});
			});
		}
		});
		
		// 停止打款
		$("#_3").click(function(){
		if(curr_btn == 2){
			var tdata = {c:"${c}",isRemittance:"0",subProductId:"${subProduct.id}",productId:"${product.id}",isTotalShare:isTotalShare};
			var layers = top.layer.confirm("您确认要执行<span style='color:red'>"+$(this).text()+"</span>操作吗？",  function(){
				top.layer.close(layers);
				layers =  top.layer.load(0,2);
				jQuery.ajax({
					url:"overview/alterstatus.json",type: "post",contentType : 'application/x-www-form-urlencoded',
					data:tdata,dataType : 'json',
					success:function(data){
						if(data.status == 'success'){
							curr_btn = 3;
							setProductStatus();
							top.layer.close(layers);
						}
					}
				});
			});
		}
		});
		// 产品封账
		$("#_4").click(function(){
		if(curr_btn == 3){
			var tdata = {c:"${c}",sellStatus:"9",productId:"${product.id}"};
			var layers = top.layer.confirm("您确认要执行<span style='color:red'>"+$(this).text()+"</span>操作吗？",  function(){
				top.layer.close(layers);
				layers =  top.layer.load(0,2);
				jQuery.ajax({
					url:"overview/alterstatus.json",type: "post",contentType : 'application/x-www-form-urlencoded',
					data:tdata,dataType : 'json',
					success:function(data){
						if(data.status == 'success'){
							curr_btn = 4;
							setProductStatus();
							top.layer.close(layers);
						}
					}
				});
			});
		}
		});
		// 已成立
		$("#_5").click(function(){
			if(curr_btn == 4){
				// 检查成立条件
				jQuery.ajax({
					url:"overview/checkproductfundcondition.json",type: "get",contentType : 'application/x-www-form-urlencoded',
					data:{c:"${c}",productId:"${product.id}"},dataType : 'json',
					success:function(data){
						if(data.status == 'success'){
							var deadlineType = '${product.deadlineType}';
							var deadlineType_s = "";
							if(deadlineType == 1){
								deadlineType_s = '<li><lable>产品止期：</lable><input type="text" name="stopDate" id="la_stopDate" onclick="WdatePicker();maxDeadline();" class="Wdate" readonly="readonly"/></li>'
							}
							this_layers = $.layer({
								type: 1,
								title: '产品成立',
								shadeClose: true, 
								closeBtn: [0,true],
								border : [5, 0.5, '#666', true],
								offset: ['100px',''],
								move: ['.xubox_title', true],
								area: ['240px','auto'],
								page: {
								html: '<form id="found_form">'
									 +'<input type="hidden" name="c" value="${c}"/>'
									 +'<input type="hidden" name="sellStatus" value="5"/>'
									 +'<input type="hidden" name="productId" value="${product.id}"/>'
									 +'<div style="padding:11px 11px 0px 11px;line-height:2em;"><font style="color:red;">注：成立产品之前请删除与该产品关联的培训视频。</font></div>'
									 +'<div style="padding:11px;line-height:2em;">'
									 +'<ul>'
									 +'<li><lable>成立日期：</lable><input type="text" name="foundDate" id="la_foundDate" onclick="WdatePicker()" class="Wdate" readonly="readonly"/></li>'
									 +deadlineType_s
									 +'<li><center><a id="found" style="padding:2px 5px;border: 1px #FF6600 solid;background-color:#FF6600;color:#FFF;cursor: pointer;">确认</a></center></li>'
									 +'</ul>'
									 +'</div>'
									 +'</form>'
								}
							});
						}else{
							top.layer.msg("请检查该产品下未分配合同编号的订单!");
						}
					}
				});
			
			}
		});
		
		$("#found").live("click",function(){
			$(this).css("background-color","#CCC");
			$(this).attr("id","");
			var form = $("#found_form").serializeArray();
			var foundDate = form[1].value;
			if(foundDate != ''){
				layer.close(this_layers);
				layers =  top.layer.load(0,2);
				jQuery.ajax({
					url:"overview/alterstatus.json",type: "post",contentType : 'application/x-www-form-urlencoded',
					data:form,dataType : 'json',
					success:function(data){
						if(data.status == 'success'){
							curr_btn = 5;
							setProductStatus();
							top.layer.closeAll();
						}
					}
				});
			}
		});
		
		// 发行失败
		$("#_6").click(function(){
			if(curr_btn == 4){
				var tdata = {c:"${c}",sellStatus:"4",productId:"${product.id}"};
				var layers = top.layer.confirm("您确认要执行<span style='color:red'>"+$(this).text()+"</span>操作吗？",  function(){
					top.layer.close(layers);
					layers =  top.layer.load(0,2);
					jQuery.ajax({
						url:"overview/alterstatus.json",type: "post",contentType : 'application/x-www-form-urlencoded',
						data:tdata,dataType : 'json',
						success:function(data){
							if(data.status == 'success'){
								curr_btn = 6;
								setProductStatus();
								top.layer.close(layers);
							}
						}
					});
				});
			}
		});
		// 取消投放
		$("#_7").click(function(){
			if(curr_btn < 2 ){
				var tdata = {c:"${c}",sellStatus:"8",productId:"${product.id}"};
				var layers = top.layer.confirm("您确认要执行<span style='color:red'>"+$(this).text()+"</span>操作吗？",  function(){
					top.layer.close(layers);
					layers =  top.layer.load(0,2);
					jQuery.ajax({
						url:"overview/alterstatus.json",type: "post",contentType : 'application/x-www-form-urlencoded',
						data:tdata,dataType : 'json',
						success:function(data){
							if(data.status == 'success'){
								curr_btn = 7;
								setProductStatus();
								top.layer.close(layers);
							}
						}
					});
				});
			}
		});
		
		$("#favor").click(function(){
			var text = "<a>关闭收藏</a>";
			var t = 1;
			if(curr_btn != 6  && curr_btn != 7){
			if($("#favor").text() == '关闭收藏'){
				text = "<a>开启收藏</a>";
				t = 0;
			}
			var tdata = {c:"${c}",isFavorites:t,subProductId:"${subProduct.id}",productId:"${product.id}",isTotalShare:1};
			var layers = top.layer.confirm("您确认要执行<span style='color:red'>"+$(this).text()+"</span>操作吗？",  function(){
				top.layer.close(layers);
				layers =  top.layer.load(0,2);
				jQuery.ajax({
					url:"overview/alterstatus.json",type: "post",contentType : 'application/x-www-form-urlencoded',
					data:tdata,dataType : 'json',
					success:function(data){
						if(data.status == 'success'){
							$("#favor").html(text);
							top.layer.close(layers);
						}
					}
				});
			});
			}
		});
		
		// 预约
		$("#preorder").click(function(){
			var text = "<a>关闭预约</a>";
			var j = 1;
			if(curr_btn == 0 || curr_btn ==1 || curr_btn == 2 || curr_btn == 3){
				if($("#preorder").text() == '关闭预约'){
					text = "<a>开启预约</a>";
					j = 0;
				}
				var tdata = {c:"${c}",isOrder:j,isTotalShare:0,productId:"${product.id}",subProductId:"${subProduct.id}"};
				var layers = top.layer.confirm("您确认要执行<span style='color:red'>"+$(this).text()+"</span>操作吗？",  function(){
					top.layer.close(layers);
					layers =  top.layer.load(0,2);
					jQuery.ajax({
						url:"overview/alterstatus.json",type: "post",contentType : 'application/x-www-form-urlencoded',
						data:tdata,dataType : 'json',
						success:function(data){
							if(data.status == 'success'){
								if(curr_btn == 0){
									curr_btn = 1;
								}
								$("#preorder").html(text);
								setProductStatus();
								top.layer.close(layers);
							}
						}
					});
				});
			}
		});
		
		// 阳光私募产品开放日开启预约开启打款
		$("#privateorder").click(function(){
			var text = "<a>关闭预约停止打款</a>";
			var j = 1;
			if(curr_btn == 5){
				if($("#privateorder").text() == '关闭预约停止打款'){
					text = "<a>开启预约开启打款</a>";
					j = 0;
				}
				var tdata = {c:"${c}",isOrderAndRemittance:j,isTotalShare:isTotalShare,productId:"${product.id}",subProductId:"${subProduct.id}"};
				var layers = top.layer.confirm("您确认要执行<span style='color:red'>"+$(this).text()+"</span>操作吗？",  function(){
					top.layer.close(layers);
					layers =  top.layer.load(0,2);
					jQuery.ajax({
						url:"overview/alterorderandremittancestatus.json",type: "post",contentType : 'application/x-www-form-urlencoded',
						data:tdata,dataType : 'json',
						success:function(data){
							if(data.status == 'success'){
								$("#privateorder").html(text);
								top.layer.close(layers);
							}
						}
					});
				});
			}
		});
		
		// TIPS
		var tips = "产品募集规模";
		if(isTotalShare == 1){
			tips = "产品共享募集规模";
		}
		$("#c_isTotal").text(tips);
		
	});
	
	// 计算产品止期
	function maxDeadline(){
		var deadlineType = '${product.deadlineType}';
		if(deadlineType == 1){
			var maxDeadline = '${product.maxDeadline}';
			var d1,s = '';
			var remindTime=$("#la_foundDate").val();
			var str = remindTime.toString();
			str = str.replace(/-/g,"/");
			var d1 = new Date(str);
			d3 = new Date(d1.getFullYear(),d1.getMonth()+parseInt(maxDeadline),d1.getDate(),d1.getHours(),d1.getMinutes());
			s += d3.getFullYear()+'-';
			s += (d3.getMonth()+1)+'-';
			s += d3.getDate()+' ';
			$("#la_stopDate").val(s);
		}
	}
	
	// 验证产品成立日期的合法性
	function validFoundDate(){
		var foundDate = $("input[name='foundDate']");
		var raiseFinishTime = "${product.raiseFinishTime}";
		if($.trim(foundDate) != "" && $.trim(raiseFinishTime) != ""){
			return dateCompare(foundDate,raiseFinishTime);
		}
	}
	
	// 时间比较
	function dateCompare(a, b) {
		var arr = a.split("-");
		var starttime = new Date(arr[0], arr[1], arr[2]);
		var starttimes = starttime.getTime();

		var arrs = b.split("-");
		var lktime = new Date(arrs[0], arrs[1], arrs[2]);
		var lktimes = lktime.getTime();

		if (starttimes > lktimes) {
			return false;
		}else{
			return true;
		}
	}
	
	// 设置产品状态
	function setProductStatus(){
		if(curr_btn == 0){
			$("li[id^='_']").attr("class","stop");
			$("#_7").attr("class","normal");
		}else if(curr_btn == 1){
			$("li[id^='_']").attr("class","stop");
			$("#_3").attr("class","stop");
			$("#_2").attr("class","normal");
			$("#_7").attr("class","normal");
		}else if(curr_btn == 2){
			$("#_3").attr("class","normal");
			$("#_4").attr("class","stop");
			$("#_2").attr("class","active");
			$("#_7").attr("class","stop");
		}else if(curr_btn == 3){
			$("#_2").attr("class","normal");
			$("#_3").attr("class","active");
			$("#_4").attr("class","normal");
			$("#_7").attr("class","stop");
		}else if(curr_btn == 4){
			$("li[id^='_']").attr("class","stop");
			$("#_4").attr("class","active");
			$("#_5").attr("class","normal");
			$("#_6").attr("class","normal");
			$("#_7").attr("class","stop");
			$("#preorder").attr("class","stop");
		}else if(curr_btn == 5){
			$("li[id^='_']").attr("class","stop");
			$("#_5").attr("class","active");
			$("#preorder").attr("class","stop");
			$("#_7").attr("class","stop");
			// 是否是私募产品
			if("2" == "${product.type}" || "5" == "${product.type}"){
				$("#preorder").hide();
				$("#privateorder").show();
			}
		}else if(curr_btn == 6){
			$("li[id^='_']").attr("class","stop");
			$("#_6").attr("class","active");
			$("#preorder").attr("class","stop");
			$("#_7").attr("class","stop");
		}else{
			$("li[id^='_']").attr("class","stop");
			$("#_7").attr("class","active");
			$("#preorder").attr("class","stop");
		}
		$("#favor").attr("class","other");
	}
	
	// 数字添加千位符
	function numsplitor(s){
        if(/[^0-9\.]/.test(s) || $.trim(s) == ""){ 
			return "";
		}
        s=s.replace(/^(\d*)$/,"$1.");
        s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
        s=s.replace(".",",");
		var re=/(\d)(\d{3},)/;
        while(re.test(s)){
			s=s.replace(re,"$1,$2");
		}
		s=s.replace(/,(\d\d)$/,".$1");
		s=s.replace(/^\./,"0.");
		return s;
    }
	
	// 前一天日期推算
	function getPreday(remindTime){
		var d1,d2,d3,s= '';
		//var remindTime="2013-1-1"
		var  str=remindTime.toString();
		str =  str.replace(/-/g,"/");
		var d1 = new Date(str);
		var d3 = new Date(d1.getFullYear(),d1.getMonth(),d1.getDate()-1,d1.getHours(),d1.getMinutes());
		s += d3.getFullYear()+'/';
		s += (d3.getMonth()+1)+'/';
		s += d3.getDate()+' ';
		return s;
	}
	
	// 计算当前产品状态
	function getCurrStatus(){
		var currStatus = 0;
		// 获取主产品销售状态
		var saleStatus = "${product.sellStatus}";
		// 获取子产品打款状态
		var isRemittance = "${subProduct.isRemittance}";
		// 获取子产品预约状态
		var isOrder = "${subProduct.isOrder}";
		// 获取子产品收藏状态
		var isFavorites = "${subProduct.isFavorites}";

		// 产品销售状态为'计划投放'
		if (saleStatus == 1) {
			if (isOrder == 1) {
				currStatus = 1;
			} else {
				currStatus = 0;
			}
		}
		// 产品销售状态为'在售'
		else if (saleStatus == 2 ) {
			if (isRemittance && isRemittance == "1") {
				currStatus = 2;
			} else if (isRemittance && isRemittance == "0"){
				currStatus = 3;
			}else{
				if (isOrder == 1) {
					currStatus = 1;
				} else {
					currStatus = 0;
				}
			}
		}
		// 产品销售状态为'产品封账'
		else if (saleStatus ==  9) {
			currStatus = 4;
		}
		// 产品销售状态为'已成立或已成立且在售'
		else if (saleStatus == 5 || saleStatus == 6) {
			currStatus = 5;
		}
		// 产品销售状态为 '发行失败'
		else if (saleStatus == 4) {
			currStatus = 6;
		}
		// 产品销售状态为取消投放
		else if (saleStatus == 8) {
			currStatus = 7;
		}
		return currStatus;
	}
	</script>
	<style>
		.overview{width:100%;margin: 0 auto 10px;padding:0px 5px;float:left}
		.title{font-family:'黑体';font-size:1.2em;width:100%;}
		.sub-title{float:left;width:100%;padding:8px 0px 3px 0px;}
		.sub-title ul li{list-style:none;float: left;position: relative;margin-right:10px;}
		.sub-title ul li a{padding:3px;text-align:center;color:#FFF;}
		.sub-title-active a{background-color:#3879D9;}
		.sub-title-normal a{background-color:#999;}
		
		.sub-button{float:left;width:100%;padding:8px 0px;}
		.sub-button ul{float:left;width:700px;height:15px;}
		.sub-button ul li{list-style: none;float: left;position: relative;margin-right:10px;}
		.sub-button ul li a{padding:3px;text-align:center;color:#FFF;background-color:#CCC;}
		.sub-button .active a{background-color:#FF6600;}
		.sub-button .normal a{background-color:#17BDB8;cursor: pointer;}
		.sub-button .stop a{background-color:#CCC;}
		.sub-button .other a{background-color:#000;cursor: pointer;}
		.sub-button .normal a:hover,.other a:hover{background-color:#FF6600;}

		.box-title{font-family:'Trebuchet ';font-size:1.2em;color:#333;margin:0 5px;width:80%;padding:0px 5px;float:left;width:100%}
		.box-title lable{color:#999;}
		.box-title span{color:green;font-weight:normal}
		.box-chart{width:1000px;border-bottom:1px #CCC dashed;margin-bottom:5px;float:left;padding:5px 0px;display:none}
		
		.box-chart-2{float:left;width:80%; height:65px;}
		
		.box-graph{float:left;width:900px; height:260px;}
		
		.jqplot-image-button {margin-bottom: 15px;margin-top: 15px;}
		div.jqplot-image-container {position: relative;z-index: 11; margin: auto;display: none;background-color: #ffffff;border: 1px solid #999;display: inline-block;margin-top: 25px;}
		div.jqplot-image-container-header {font-size: 1.0em;font-weight: bold;padding: 5px 15px;background-color: #eee;}
		div.jqplot-image-container-content {padding: 15px;background-color: #ffffff;}
		.jqplot-table-legend-label {white-space: nowrap;}
		div.syntaxhighlighter {margin-top: 4em !important;margin-bottom: 3em !important;}
		
	</style>
	</head>
	<body>
		<%@ include file="tab.jsp" %>
		<div class="overview">
			<div class="title">
				<msg:message code='product.shortName'/><msg:message code="system.common.sign.colon"/>${product.shortName}<span id="title_tip"></span>
			</div>
			<!-- 如果产品共享额度，则按主产品的最大最小募集额度，否则，按子产品各个分解 -->

			<c:if test="${!empty subProductList}">
			
				<div class="sub-title">
					<ul>
						<c:forEach var="subProduct" items="${subProductList}" >
						<c:if test="${!empty subProduct.type}">   
						<li id="a_subproduct_${subProduct.id}" class="sub-title-normal"><a href="${base}${market_salemanage_manage_overview.controller}?c=${market_salemanage_manage_overview.code}&id=${product.id}&subProductId=${subProduct.id}">${subProduct.type}</a></li>
						</c:if>
						</c:forEach>
					</ul>
				</div>
			</c:if>
			
			
			<div class="sub-button">
				<ul>
				<c:if test="${!empty market_salemanage_manage_overview_openpay}">
    			<security:authorize ifAnyGranted="${market_salemanage_manage_overview_openpay.code}">
					<li id="_2" class="stop"><a>开启打款</a></li>
				</security:authorize>
				</c:if>
				<c:if test="${!empty market_salemanage_manage_overview_closepay}">
    			<security:authorize ifAnyGranted="${market_salemanage_manage_overview_closepay.code}">
					<li id="_3" class="stop"><a>停止打款</a></li>
				</security:authorize>
				</c:if>
				<c:if test="${!empty market_salemanage_manage_overview_stoppay}">
    			<security:authorize ifAnyGranted="${market_salemanage_manage_overview_stoppay.code}">
					<li id="_4" class="stop"><a>产品封账</a></li>
				</security:authorize>
				</c:if>
				<c:if test="${!empty market_salemanage_manage_overview_success}">
    			<security:authorize ifAnyGranted="${market_salemanage_manage_overview_success.code}">
					<li id="_5" class="stop"><a>产品成立</a></li>
				</security:authorize>
				</c:if>
				<c:if test="${!empty market_salemanage_manage_overview_failed}">
    			<security:authorize ifAnyGranted="${market_salemanage_overview_failed.code}">
					<li id="_6" class="stop"><a>发行失败</a></li>
				</security:authorize>
				</c:if>
				<c:if test="${!empty market_salemanage_manage_overview_cancel}">
    			<security:authorize ifAnyGranted="${market_salemanage_manage_overview_cancel.code}">
					<li id="_7" class="normal"><a>取消投放</a></li>
				</security:authorize>
				</c:if>
				<c:if test="${!empty market_salemanage_manage_overview_closecollect}">
    			<security:authorize ifAnyGranted="${market_salemanage_manage_overview_closecollect.code}">
					<li id="favor" class="other"><a>开启收藏</a></li>
				</security:authorize>
				</c:if>
				<c:if test="${!empty market_salemanage_manage_overview_openorder}">
    			<security:authorize ifAnyGranted="${market_salemanage_manage_overview_openorder.code}">
					<li id="preorder" class="other"><a>开启预约</a></li>
				</security:authorize>
				</c:if>
				<c:if test="${!empty market_salemanage_manage_overview_privateorder}">
    			<security:authorize ifAnyGranted="${market_salemanage_manage_overview_privateorder.code}">
					<li id="privateorder" class="other" style="display:none"><a>开启预约开启打款</a></li>
				</security:authorize>
				</c:if>
				</ul>
			</div>
		</div>
		<div class="box-chart">
			<div class="box-title"><font id="c_isTotal">产品募集规模</font>(万元)[ <lable>产品销售规模上限:</lable><c:if test="${plot.productScaleMax == -1}">不限</c:if><span class="mt"><c:if test="${plot.productScaleMax != -1}">${plot.productScaleMax}</c:if></span> &nbsp;&nbsp;<lable>产品销售规模下限:<span class="mt">${plot.productScaleMin}</span></lable>]</div>
			<div id="chart_product_scale" class="box-chart-2"></div>	
		</div>
		<div class="box-chart">
			<div class="box-title">到账情况(万元)[ <lable>到账金额:</lable><span class="mt">${plot.orderTotalPayed}</span>&nbsp;&nbsp;<lable>锁定到账:</lable><span class="mt">${plot.orderTotalCorrectPayed}</span>&nbsp;&nbsp;<lable>非合法到账:</lable><span class="mt">${plot.orderTotalPayed - plot.orderTotalCorrectPayed}</span>]</div>
			<div id="chart_product_payed" class="box-chart-2"></div>
		</div>
		<div class="box-chart">
			<div class="box-title">订单总额(万元)[<lable>订单总金额:</lable><span class="mt">${plot.orderTotal}</span>&nbsp;&nbsp;<lable>已配额:</lable><span class="mt">${plot.orderTotalShared}</span>&nbsp;&nbsp;<lable>未配额:</lable><span class="mt">${plot.orderTotal - plot.orderTotalShared}</span>]</div>
			<div id="chart_product_neworder" class="box-chart-2"></div>
		</div>
		<div class="box-chart">
			<div class="box-title">订单总数(份)[<lable>订单总份数:</lable><span>${plot.orderTotalCount}</span>&nbsp;&nbsp;<lable>已到账:</lable><span>${plot.orderTotalPayedCount}</span>&nbsp;&nbsp;<lable>已配额:</lable><span>${plot.orderTotalSharedCount}</span>&nbsp;&nbsp;<lable>未配额:</lable><span>${plot.orderTotalCount - plot.orderTotalSharedCount}</span>]</div>
			<div id="chart_product_orders" class="box-chart-2"></div>
		</div>
		<c:if test="${plot.orderTotalLimit != -1}">
		<div class="box-chart">
			<div class="box-title">小额订单总额(万元)[<lable>小额订单总金额:</lable><span class="mt">${plot.orderTotalLimit}</span>&nbsp;&nbsp;<lable>配额已到账:</lable><span class="mt">${plot.orderTotalLimitPayedShared}</span>&nbsp;&nbsp;<lable>配额未到账:</lable><span class="mt">${plot.orderTotalLimitUnPayedShared}</span>]</div>
			<div id="chart_product_limit" class="box-chart-2"></div>
		</div>
		</c:if>
		<div class="box-chart">
			<div class="box-title">锁定到账订单归集总数(份)[<lable>锁定到账订单归集数:</lable><span>${plot.orderTotalCorrectPayedCount}</span>&nbsp;&nbsp;<lable>已归集:</lable><span>${plot.orderTotalCorrectCollectCount}</span>&nbsp;&nbsp;<lable>未归集:</lable><span>${plot.orderTotalCorrectPayedCount-plot.orderTotalCorrectCollectCount}</span>]</div>
			<div id="chart_product_collection" class="box-chart-2"></div>
		</div>
		<div class="box-chart">
			<div class="box-title">每日预约、到账情况</div>
			<div id="graph_product_payed" class="box-graph"></div>
		</div>
		<div class="box-chart">
			<div class="box-title">每日预约、到账累计</div>
			<div id="graph_product_order" class="box-graph"></div>
		</div>
		<div style="float:left;width:100%;height:36px;"></div>
		<div class="toolbar">
			<div class="left"></div>
			<div class="center">
				<input type="button" class="btn2" onclick="history.go(-1);" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
			</div>
			<div class="right"></div>
		</div>
	</body>
</html>