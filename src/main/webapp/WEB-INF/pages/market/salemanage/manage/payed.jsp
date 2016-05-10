<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	$(window).resize(function(){ 　　
		$("#payedordersList").setGridWidth($(window).width()*0.999);　
	});

	var lastsel;
	var layers;
	$(document).ready(function(){
	
		// 选中TAB
		$("#tab_${c}").attr("class","selected");
		
		<!--  已到账订单列表 -->
		jQuery("#payedordersList").jqGrid({
			datatype: "local",
			colNames:["","<msg:message code="product.sale.distribute.orderNum"/>"
				,"<msg:message code="product.sale.distribute.rmCoName"/>"
				,"<msg:message code="product.sale.distribute.teamName"/>"
				,"<msg:message code="product.sale.distribute.clientName"/>"
				,"<msg:message code="product.sale.distribute.orderTime"/>"
				,"<msg:message code="product.sale.distribute.commissionPayTime"/>"
				,"<msg:message code="product.sale.distribute.payTime"/>"
				,"<msg:message code="product.sale.distribute.investAmount"/>"
				,"<msg:message code="product.sale.distribute.payAmount"/>"
				,"<msg:message code="product.sale.distribute.shared"/>"
				,"<msg:message code="info.operate"/>","",""
				],
			colModel:[{name:'id',index:'id',hidden:true}
			,{name:'orderNumber',index:'orderNumber', width:'14%',align:'center',sortable: true,formatter:reSharedOrderActor}
			,{name:'rmName',index:'rmName', width:'9%',align:'center',sortable: false,formatter:rmNameFormatter}
			,{name:'teamShortName',index:'teamShortName', width:'9%',align:'center',sortable: false}
			,{name:'clientName',index:'clientName', width:'9%',align:'center',sortable: false}
			,{name:'orderTime',index:'orderTime', width:'9%',align:'center',sortable: false}
			,{name:'commissionPayTime',index:'commissionPayTime', width:'9%',align:'center',sortable: false}
			,{name:'payTime',index:'payTime', width:'9%',align:'center',sortable: false}
			,{name:'investAmount',index:'investAmount', width:'9%',align:'center',sortable: false}
			,{name:'payAmount',index:'payAmount', width:'9%',align:'center',sortable: false,formatter:payAmountFormatter}
			,{name:'share',index:'share', width:'9%',align:'center',sortable: false,editable:true}
			,{name:'act',index:'act', width:'4%',align:'center',sortable: false,formatter:payedOrderActor}
			,{name:'payAmount',index:'payAmount',hidden:true}
			,{name:'rmShortName',index:'rmShortName',hidden:true}
			],
			mtype:"POST",
			postData:{c:"${c}",productId:"${product.id}",operate:"1"},
			loadonce: true,
			rowNum:15,
			rowTotal: 2000,
			rowList : [20,30,50],
			scroll:1,			
			pager: '#payedordersListPaged',
			autowidth: true,
			autoheight: true,
			viewrecords: true,
			multiselect: false,
			rownumbers: true,
			gridview: true,
			jsonReader: {
				repeatitems: false,
				userdata: "userdata"
			},
			gridComplete: function(){
				// 显示总配额度
				var total_payed = $("#payedordersList").jqGrid("getGridParam", "userData").totalpayed;
				if(total_payed != 0){
					$("#total_payed").text(total_payed);
				}else{
					$("#total_payed").text('');
				}
			},
			onSelectRow: function(id){
				/**
				if(id && id!==lastsel){
					jQuery('#payedordersList').jqGrid('restoreRow',lastsel);
					jQuery('#payedordersList').jqGrid('editRow',id,true);
					$("#"+id+"_share").attr({"onkeyup":"checkNumber(event,this,'edit');","onfocusout":"checkNumber(event,this,'paste');","onpropertychange":"checkNumber(event,this,'edit');","oninput":"checkNumber(event,this,'edit');"});
					buttonControl(lastsel,id);
					lastsel=id;
				}*/
			}
		});
		
		$("#payedordersList").setGridHeight($(window).height() - 270);
 		
		jQuery("#payedordersList").jqGrid('navGrid','#payedordersListPaged',{del:false,add:false,edit:false},{},{},{},{multipleSearch:false});
		
		jQuery("#payedordersList").jqGrid('navButtonAdd','#payedordersListPaged',{
			caption: "排序一",
			title: "预约金额+预计打款时间",
			onClickButton : function (){
				reloadOrders(1);
			}
		});
		jQuery("#payedordersList").jqGrid('navButtonAdd','#payedordersListPaged',{
			caption: "排序二",
			title: "预计打款时间+预约金额",
			onClickButton : function (){
				reloadOrders(2);
			}
		});
		jQuery("#payedordersList").jqGrid('navButtonAdd','#payedordersListPaged',{
			caption: "排序三",
			title: "预约金额+预约时间",
			onClickButton : function (){
				reloadOrders(3);
			}
		});
		jQuery("#payedordersList").jqGrid('navButtonAdd','#payedordersListPaged',{
			caption: "排序四",
			title: "预约时间+RM信用值",
			onClickButton : function (){
				reloadOrders(4);
			}
		}); 
		
		<!-- 保存已配额修改结果 -->
		$(".opt-save-active").live("click",function(){
			var rowid = $(this).attr("sid");
			var reshare = $("#"+rowid+"_share").val();
			if(rowid == '' || reshare == '' || reshare == 0){
				top.layer.msg("请输入有效的分配份额!");
				return false;
			}
			var layers = top.layer.confirm("<msg:message code='product.sale.distribute.confirm'/>",  function(){
				top.layer.close(layers);
				layers =  top.layer.load(0,2);
				jQuery.ajax({
					url:"distribute/updatedistribute.json",
					type: "post",
					contentType : 'application/x-www-form-urlencoded',
					data:{c:"${c}",orderId:rowid,share:reshare,processType:1,prompt:"productName",productName:"${product.name}"}, 
					dataType : 'json',
					success:function(data){
						if(data.status == 'success'){
							top.$.layer({
								area : ['auto','auto'],
								dialog : {msg:'<msg:message code='info.operate.success'/>',type : 9}	
							});
							reloadOrders(1);
							top.layer.close(layers);	
						}else{
							top.layer.msg("error");
						}
					}
				});
			});
		});
		
		<!-- 退款 -->
		$(".opt-refund-active").live("click",function(){
			var rowid = $(this).attr("sid");
			if(rowid == ''){
				top.layer.msg("error!");
				return false;
			}
			var rowdata = jQuery("#payedordersList").jqGrid('getRowData',rowid);
			//页面层例二
    		layers = $.layer({
    			type: 1,
    			title: '退款确认',
    			shadeClose: true, 
    			closeBtn: [0,true],
    			border : [5, 0.5, '#666', true],
    			offset: ['100px',''],
    			move: ['.xubox_title', true],
    			area: ['240px','auto'],
    			page: {
				html: '<form id="refund_form"><input type="hidden" name="orderId" value="'+rowid+'"/><div style="padding:11px;line-height:2em;">'
					 +'<ul>'
					 +'	<li><lable>订单编号：<font style="color:blue;font-weight:bold;">'+rowdata.orderNumber+'</font></lable></li>'
					 +'	<li><lable>到账金额：<font style="color:red;font-weight:bold;">'+rowdata.payAmount+'万元</font></lable></li>'
					 +'	<li><lable>退款金额：</lable><input type="text" name="reFund" value="'+rowdata.payAmount+'"/></li>'
					 +'	<li><center><a id="refund" sid="'+rowid+'" style="padding:2px 5px;border: 1px #FF6600 solid;background-color:#FF6600;color:#FFF;cursor: pointer;">提交退款申请</a></center></li>'
					 +'</ul>'
					 +'</div></form>'
				}
    		});
		});
		
		//  退款确认
		$("#refund").live("click",function(){
			var rowid = $(this).attr("sid");
			if(rowid == ''){
				top.layer.msg("error!");
				return false;
			}
			var form = $("#refund_form").serializeArray();
			layer.close(layers);
			layers =  top.layer.load(0,2);
			jQuery.ajax({
					url:"distribute/fundnotice.json?c=${c}",
					type: "post",
					contentType : 'application/x-www-form-urlencoded',
					data:form, 
					dataType : 'json',
					success:function(data){
						if(data.status == 'success'){
							top.$.layer({
								area : ['auto','auto'],
								dialog : {msg:'<msg:message code='info.operate.success'/>',type : 9}	
							});
							//jQuery("#payedordersList").jqGrid("delRowData",rowid);
							top.layer.close(layers);	
						}else{
							top.layer.msg("error");
						}
					}
			});
		});
		
		<!--//  已到账订单列表 -->
		
		// 默认加载额度分配列表数据
		reloadOrders(1);
		<!--// 初始化方法 -->
		
		// 查询方法
		$("#search").click(function(){
			var value = $.trim($("#s_orderNumber").val());
			var data = {c:"${c}",productId:"${product.id}",operate:1,orderNumber:null};
			if(value != ''){
				data = {c:"${c}",productId:"${product.id}",operate:1,orderNumber:value};
			}
			jQuery("#payedordersList").jqGrid("setGridParam",{url:'distribute/payedordersgrid.json',datatype: "json",postData:data}).trigger("reloadGrid");
		});
		
	});

	// 到账金额比较
	function payAmountFormatter(cellvalue, options, rowObject){
		var investAmount = rowObject.investAmount; //预约金额   
        if (investAmount != null && investAmount != "" && cellvalue != null && cellvalue != "") {  
            var be = investAmount - cellvalue;
			if(be < 0){
				return "<font style='background-color:green;color:#FFF'>"+cellvalue+"</font>";
			}else if(be > 0){
				return "<font style='background-color:red;color:#FFF'>"+cellvalue+"</font>";
			}else{
				return cellvalue;
			}
        }else{
			return "&nbsp;";
		}
	}

	//理财顾问姓名格式化
	function rmNameFormatter(cellvalue, options, rowObject){
		return cellvalue+"("+rowObject.rmShortName+")";
	}
	
	// 重新加载列表数据
	function reloadOrders(operate){
		jQuery("#payedordersList").jqGrid("setGridParam",{url:'distribute/payedordersgrid.json',datatype: "json",postData:{c:"${c}",productId:"${product.id}",operate:operate,isTotalShare:1}}).trigger("reloadGrid");
	}
	
	function buttonControl(ilastsel,cursel){
		$("#a_confirm_"+cursel).attr("class","opt-save-active");
		$("#a_refund_"+cursel).attr("class","opt-refund-normal");
		
		$("#a_confirm_"+ilastsel).attr("class","opt-save-normal");
		$("#a_refund_"+ilastsel).attr("class","opt-refund-active");

	}
	
	<!--  已到账列表 -->
	
	//额度分配列表操作功能
	function payedOrderActor(cellvalue, options, rowObject){
		var id = rowObject.id;
		var rowdata = rowObject;
		var content = "";
		
		<!-- 确认份额 -->
	    //content += "<a id='a_confirm_" + id + "' sid='"+id+"' class='opt-save-normal' title='确认' style='color:#FFF;cursor: pointer;'>确认</a>";
		//content += "&nbsp;&nbsp;";
		content += "<a id='a_refund_" + id + "' sid='"+id+"' class='opt-refund-active' title='退款' style='color:#FFF;cursor: pointer;'>退款</a>";
		
   		return content;
	}
	
	function reSharedOrderActor(cellvalue, options, rowObject){
		var id = rowObject.id;
		var status = rowObject.tradeStatus;
		if(status == 12){
			return "<font style='background-color:#4B7588;color:#FFF'>"+cellvalue+"</font>";
		}else{
			return cellvalue;
		}
	}

	<!--//  已到账列表 -->
	
	// 数字验证
	function checkNumber(evt,object,flag){
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
	</script>
<style>
	.top-1 {padding: 5px;border: 1px #ccc solid;width: 100%}
	.top-1-info {color: #ccc;}
	.top-1-graph {padding: 5px 0px;}
	.opt-save-normal,.opt-refund-normal{padding: 0px 5px;border: 1px #ccc solid;background-color: #ccc;}
	.opt-save-active,.opt-refund-active{padding: 0px 5px;border: 1px #FF6600 solid;background-color: #FF6600;}
	.overview{width:100%;margin: 0 auto 10px;padding:0px 5px;float:left}
	.title{font-family:'黑体';font-size:1.2em;width:100%;}
	.overview ul {float: left;width: 600px;height: 15px;}
	.overview ul li {list-style: none;float: left;position: relative;margin-right: 10px;background-color: #FF6600;padding: 3px;text-align: center;}
	.status a {color: #FFF;cursor: pointer;}
</style>
</head>
<body class="skinMain">
		<%@ include file="tab.jsp" %>
		<div class="overview">
			<div class="title">
				<msg:message code='product.shortName'/><msg:message code="system.common.sign.colon"/>${product.shortName}
			</div>
		</div>
		<table width="100%" border="0" cellspacing="1" cellpadding="0"
			class="skinMain">
			<tr>
				<td width="100%" class="top-1">
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td>
								<table width="100%">
									<tr>
										<td class="skinLayout_lable" width="304">
											<lable> 订单号:</lable>&nbsp;&nbsp;<input type="text" name="orderNunmber" id="s_orderNumber"/>&nbsp;&nbsp;
										</td>
										<td width="65">
											<input type="button" class="btn2" id="search" value="查询"/>
										</td>
										<td align="right" style="font-weight:bold;"><span>当前已到账金额：</span><span id="total_payed" style="color:red;"></span>万元</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<form name="distribute_payed_orders" id="distribute_payed_orders">
			<table id="payedordersList"><tr><td>&nbsp;</td></tr></table>
			<div id="payedordersListPaged"></div>
			<div style="padding:5px 5px;">注释（<font style='background-color:#4B7588;color:#FFF'>[订单号]</font>：额度重新分配的订单  
				&nbsp;&nbsp;<font style='background-color:green;color:#FFF;'>[到账金额]</font>：到账金额大于预约金额
				&nbsp;&nbsp;<font style='background-color:red;color:#FFF;'>[到账金额]</font>：到账金额小于预约金额）</div>
		</form>
		<div style="float:left;width:100%;height:36px;"></div>
		<div class="toolbar">
			<div class="left"></div>
			<div class="center">
				<input type="button" class="btn2" onclick="window.location.href='${base}${market_salemanage.controller}?c=${market_salemanage.code}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
			</div>
			<div class="right"></div>
		</div>
</body>
</html>