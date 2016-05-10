<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(document).ready(function(){
		$("#a_subproduct_${subProduct.id}").attr("class","sub-title-active");
		<!--  新订单额度分配列表 -->
	
		jQuery("#newordersList").jqGrid({
			datatype: "local",
			colNames:["","<msg:message code='order.orderNumber' />","<msg:message code='member.nickName' />","<msg:message code='member.name.nickname'/>","<msg:message code='member.level' />",
			     "<msg:message code='order.clientName' />","<msg:message code='order.orderAmount' />","<msg:message code='order.commissionPayTime' />","<msg:message code='order.orderTime' />","<msg:message code='order.share' />","<msg:message code='order.payAmount' />"
				,"<msg:message code='order.pushShare' />","<msg:message code='product.operate'/>"
				],
			colModel:[{name:'id',index:'id',hidden:true}
			,{name:'orderNumber',index:'orderNumber', width:'10%',align:'center'}
			,{name:'member.nickName',index:'member.nickName', width:'1%',align:'center',hidden:true,sortable: false}
			,{name:'member.name',index:'member.name', width:'10%',align:'center',sortable: false,formatter:function(cellvalue,options,rowObject){
				if(rowObject.member.name!=null){
					if(rowObject.member.nickName!=null){
						return rowObject.member.name+"("+rowObject.member.nickName+")";
					}else{
						return rowObject.member.name;
					}
				}else{
					return " <br>";
				}
			}}
			,{name:'member.level',index:'member.level', width:'10%',align:'center',sortable: false}
			,{name:'clientName',index:'clientName', width:'10%',align:'center',sortable: false}
			,{name:'investAmount',index:'investAmount', width:'10%',align:'center',sortable: false}
			,{name:'commissionPayTime',index:'commissionPayTime', width:'10%',align:'center',sortable: false}
			,{name:'orderTime',index:'orderTime', width:'10%',align:'center',sortable: false}
			,{name:'share',index:'share', width:'10%',align:'center',sortable: false}
			,{name:'payAmount',index:'payAmount', width:'10%',align:'center',sortable: false}
			,{name:'pushShare',index:'pushShare', width:'10%',align:'center',sortable: false,formatter:newOrderActor}
			,{name:'act',index:'act',width:'5%',align:'center',hidden: false,sortable: false,
				 formatter:function(cellvalue,options,rowdata){
					 var content = "";
					<c:if test="${!empty market_productlist_pushshare_comfirm}">
					<security:authorize ifAnyGranted="${market_productlist_pushshare_comfirm.code}">	
					content += "<a href='javascript:void(0);'  id="+rowdata.id+" class='shortcut_market_productlist_pushshare_comfirm'>";
					content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${market_productlist_pushshare_comfirm.name}";
					content += "</a>";
					</security:authorize>
					</c:if> 
					return content;
				 }}
			],
			mtype:"POST",
			postData:{c:"${c}",productID:"${productID}",subProductID:"${subProduct.id}"},
			page:"${order.page}",
			pager: "#newordersListPaged",
			rowNum:<msg:message code='jqgrid.row.num.10'/>,
			rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
			height:"auto",
			autoheight: true,
			autowidth: true,
			viewrecords: true,
			rownumbers: true,
			gridview: true,
			jsonReader: {
				repeatitems: false
			}
		});
		
		<!--  额度已分配列表 -->
		jQuery("#ordersList").jqGrid({
			datatype: "local",
			colNames:["","","<msg:message code='order.orderNumber' />","<msg:message code='member.nickName' />","<msg:message code='member.name.nickname'/>","<msg:message code='member.level' />"
			          	,"<msg:message code='order.clientName' />","<msg:message code='order.orderAmount' />","<msg:message code='order.commissionPayTime' />","<msg:message code='order.orderTime' />","<msg:message code='order.share' />",
						"<msg:message code='order.payAmount' />","<msg:message code='order.payTime' />","<msg:message code='order.pushTime' />"
						,"<msg:message code='order.pushShare' />","<msg:message code='order.pushStatus' />","<msg:message code='product.operate'/>"
						],
			colModel:[{name:'id',index:'id',hidden:true}
			    ,{name:'payProgress',index:'payProgress',hidden:true}
				,{name:'orderNumber',index:'orderNumber', width:'10%',align:'center',sortable: false}
				,{name:'member.nickName',index:'member.nickName', width:'1%',align:'center',sortable: false,hidden:true}
				,{name:'member.name',index:'member.name', width:'10%',align:'center',sortable: false,formatter:function(cellvalue,options,rowObject){
					if(rowObject.member.name!=null){
						if(rowObject.member.nickName!=null){
							return rowObject.member.name+"("+rowObject.member.nickName+")";
						}else{
							return rowObject.member.name;
						}
					}else{
						return " <br>";
					}
				}}
				,{name:'member.level',index:'member.level', width:'10%',align:'center',sortable: false}
				,{name:'clientName',index:'clientName', width:'10%',align:'center',sortable: false}
				,{name:'investAmount',index:'investAmount', width:'10%',align:'center',sortable: false}
				,{name:'commissionPayTime',index:'commissionPayTime', width:'10%',align:'center',sortable: false}
				,{name:'orderTime',index:'orderTime', width:'10%',align:'center',sortable: false}
				,{name:'share',index:'share', width:'10%',align:'center',sortable: false}
				,{name:'payAmount',index:'payAmount', width:'10%',align:'center',sortable: false,formatter:function(cellvalue,options,rowObject){
					if(rowObject.payAmount){
						return "<lable style='color:red'>"+rowObject.payAmount+"</lable>";
					}else{
						return " <br>";
					}
				}}
				,{name:'payTime',index:'payTime', width:'10%',align:'center',sortable: false}
				,{name:'pushTime',index:'pushTime', width:'10%',align:'center',sortable: false}
				,{name:'pushShare',index:'pushShare', width:'10%',align:'center',sortable: false,formatter:function(cellvalue,options,rowObject){
					if(rowObject.pushShare){
						return "<lable style='color:red'>"+rowObject.pushShare+"</lable>";
					}else{
						return " <br>";
					}
				}}
				,{name:'pushStatus',index:'pushStatus', width:'10%',align:'center',sortable: false,formatter:changePushStatus}
				,{name:'act',index:'act',width:'15%',align:'center',hidden: false,sortable: false,
					 formatter:function(cellvalue,options,rowdata){
						var content = "";
						if(rowdata.pushStatus == 3){
							<c:if test="${!empty market_productlist_pushshare_agree}">
							<security:authorize ifAnyGranted="${market_productlist_pushshare_agree.code}">	
							content += "<a href='javascript:void(0);'  id="+rowdata.id+" class='shortcut_market_productlist_pushshare_agree'>";
							content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${market_productlist_pushshare_agree.name}";
							content += "</a>";
							</security:authorize>
							</c:if>
							<c:if test="${!empty market_productlist_pushshare_refuse}">
							<security:authorize ifAnyGranted="${market_productlist_pushshare_refuse.code}">	
							content += "<a href='javascript:void(0);'  id="+rowdata.id+" class='shortcut_market_productlist_pushshare_refuse'>";
							content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${market_productlist_pushshare_refuse.name}";
							content += "</a>";
							</security:authorize>
							</c:if>
						}
							return content;
					 }}
				],
			mtype:"POST",
			postData:{c:"${c}",productID:"${productID}",subProductID:"${subProduct.id}"},
			page:"${order.page}",
			pager: "#ordersListPaged",
			rowNum:<msg:message code='jqgrid.row.num.10'/>,
			rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
			height:"auto",			
			autoheight: true,
			autowidth: true,
			viewrecords: true,
			rownumbers: true,
			gridview: true,
			jsonReader: {
				repeatitems: false
			}
		});
		
		 //确认按钮操作	
		 $(".shortcut_${market_productlist_pushshare_comfirm.indexCode}").live("click",function(){
				var id = this.id;
				var pushshare = $("#share_"+id).val();
				if(pushshare!=null && pushshare!=''){
					var k=top.$.layer({
						shade : [0.5 , '#000' , true],
						area : ['auto','auto'],
						offset : ['150px',''],
						dialog : {
							msg:'您确定要配给吗？',
							btns : 2, 
							type : 4,
							btn : ['确定','取消'],
							yes : function(){
								$.ajax({
									url: '${base}/market/pushshare/saveshare.json',
									type:'post',
									timeout:'60000',
									dataType:'json',
									data:{id:id,PushShare:pushshare},
									success:function(jsonData){
										if(jsonData.status=='success'){
											top.layer.close(k);
											window.location.href='${base}/market/productlist/pushshare?c=${market_productlist_pushshare.code}&id=${product.id}&subProductId=${subProduct.id}';
										}else{
											top.$.layer({
												shade : [0.5 , '#000' , true],
												area : ['auto','auto'],
												offset : ['150px',''],
												dialog : {msg:'配给异常',type : 8}	
											});
										}
									}
								});
							},
							no : function(){
								top.layer.close(k);
							}
						}
					});
				}else{
					top.layer.msg("<msg:message code='order.pushshare.notnull' />");
				}
		 });
	 
		 //同意按钮操作
		 $(".shortcut_${market_productlist_pushshare_agree.indexCode}").live("click",function(){
			 var id = this.id;
				var k=top.$.layer({
					shade : [0.5 , '#000' , true],
					area : ['auto','auto'],
					offset : ['150px',''],
					dialog : {
						msg:'您确定要同意吗？',
						btns : 2, 
						type : 4,
						btn : ['确定','取消'],
						yes : function(){
							$.ajax({
								url: '${base}/market/pushshare/comfirmshare.json',
								type:'post',
								timeout:'60000',
								dataType:'json',
								data:{id:id},
								success:function(jsonData){
									if(jsonData.status=='success'){
										top.layer.close(k);
										//刷新页面
										window.location.href='${base}/market/productlist/pushshare?c=${market_productlist_pushshare.code}&id=${product.id}&subProductId=${subProduct.id}';
										//$("#ordersList").trigger("reloadGrid"); 
									}else{
										top.$.layer({
											shade : [0.5 , '#000' , true],
											area : ['auto','auto'],
											offset : ['150px',''],
											dialog : {msg:'同意失败',type : 8}	
										});
									}
								}
							});
						},
						no : function(){
							top.layer.close(k);
						}
					}
				});
		 });
	 
		 //拒绝按钮操作
		 $(".shortcut_${market_productlist_pushshare_refuse.indexCode}").live("click",function(){
			 var id = this.id;
			 var payProgress = jQuery("#ordersList").jqGrid('getRowData',id).payProgress;
				var k=top.$.layer({
					shade : [0.5 , '#000' , true],
					area : ['auto','auto'],
					offset : ['150px',''],
					dialog : {
						msg:'您确定要拒绝吗？',
						btns : 2, 
						type : 4,
						btn : ['确定','取消'],
						yes : function(){
							$.ajax({
								url: '${base}/market/pushshare/refuse.json',
								type:'post',
								timeout:'60000',
								dataType:'json',
								data:{id:id,payProgress:payProgress},
								success:function(jsonData){
									if(jsonData.status=='success'){
										top.layer.close(k);
										window.location.href='${base}/market/productlist/pushshare?c=${market_productlist_pushshare.code}&id=${product.id}&subProductId=${subProduct.id}';
										//$("#ordersList").trigger("reloadGrid"); 
									}else{
										top.$.layer({
											shade : [0.5 , '#000' , true],
											area : ['auto','auto'],
											offset : ['150px',''],
											dialog : {msg:'拒绝失败',type : 8}	
										});
									}
								}
							});
						},
						no : function(){
							top.layer.close(k);
						}
					}
				});
		});
		
		// 替换小额投资人数
		if('${plot.maxLowAmountClientCount}' == -1){
			$("#maxLowAmountClientCount").text("不限");
		}
		
		// 重载列表
		reloadOrders("${base}/market/pushshare/unpushshare.json","newordersList");
		
	});
	
	function  changePushStatus(cellvalue,options,rowObject){
		var status = rowObject.pushStatus;
		if(status == 1){
			return '待确认';
		}else if(status == 2){
			return '已接受';
		}else if(status == 3){
			return '部分接受';
		}else if(status == 4){
			return '已拒绝';
		}
	}
	
	//额度分配列表操作功能
	function newOrderActor(cellvalue, options, rowObject){
		var id=rowObject.id;
		var redata = rowObject.investAmount;
		if(typeof(redata) == "undefined"){
			redata = "";
		}
		var orderForm =  "<input class='input_share' type='text' value='"+redata+"'  id='share_"+id+"' onkeyup=\"checkNumber("+id+",event,this,'edit');\"  onpropertychange=\"checkNumber("+id+",event,this,'edit');\" oninput=\"checkNumber("+id+",event,this,'edit');\"/>";
		return orderForm;
	}
	
	function changeColor(cellvalue,options,rowObject){
		return "<lable style='color:red'>"+rowObject.payAmount+"</lable>";
	}
	
	// 重新加载列表数据
	function reloadOrders(rurl,obj){
		var data = {};
		if('${product.isTotalShare}' == '1'){
			data = {c:"${c}",productID:"${product.id}",isTotalShare:1};
		}else{
			var subProductId;
			if('${subProduct.id}' != ''){
				subProductId = "${subProduct.id}";
			}else{
				subProductId = "${subProductList[0].id}";
			}
			data = {c:"${c}",subProductID:subProductId,isTotalShare:0};
		}
		jQuery("#"+obj).jqGrid("setGridParam",{url:rurl,datatype: "json",postData:data}).trigger("reloadGrid");
	}
	
	
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
	
	function tab(num){
		if(num == 1){
			$("#tab_1").addClass("selected");
			$("#div_1").css("visibility","visible");
			$("#tab_2").removeClass("selected");
			$("#div_2").css("visibility","hidden");
			reloadOrders("${base}/market/pushshare/unpushshare.json","newordersList");
		}else{
			$("#tab_2").addClass("selected");
			$("#div_2").css("visibility","visible");
			$("#tab_1").removeClass("selected");
			$("#div_1").css("visibility","hidden");
			reloadOrders("${base}/market/pushshare/pushshare.json","ordersList");
		}
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
	.sub-button ul{float:left;width:600px;height:15px;}
	.sub-button ul li{list-style: none;float: left;position: relative;margin-right:10px;background-color:#FF6600;padding:3px;text-align:center;}
	.sub-button a{color:#FFF;cursor: pointer;}
	
	.top-1{border:1px #ccc solid;width:100%}
	.top-1-info{color:#999;padding:0px 0px 5px 0px;}
	.top-1-des{font-size:1.0em;padding-botton:5px;color:#5DBDF1;font-size:1.0em;font-weight: bold;}
	.top-1-des lable{font-size:1.0em;padding-botton:5px;color:#000;font-weight: normal;}
	.box-dis{height:auto;padding:0px 5px 5px;}
	.box-scale{float:left;width:100%;height:16px;padding:0px;margin: 5px 0px;background:#FFFAE2;}
	.box-scale div{float:left;margin:0px;padding:0px;height:16px;float: left; display: block;color:#FFF;}
</style>
</head>
<body class="skinMain">
	<div class="overview">
		<div class="title">
			<msg:message code='product.shortName'/><msg:message code='system.common.sign.colon'/>${product.shortName}
		</div>
		<!-- 如果产品共享额度，则按主产品的最大最小募集额度，否则，按子产品各个分解 -->
		<c:if test="${!empty subProductList}">
			<div class="sub-title">
				<ul>
					<c:forEach var="subProduct" items="${subProductList}" >   
					<li id="a_subproduct_${subProduct.id}" class="sub-title-normal"><a href="${base}${market_productlist_pushshare.controller}?c=${market_productlist_pushshare.code}&id=${product.id}&subProductId=${subProduct.id}">${subProduct.type}</a></li>
					</c:forEach>
				</ul>
			</div>
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
					<div class="box-dis"> 
						<div class="top-1-info">额度配给情况</div>
						<div class="top-1-des">
							<lable>产品规模上限(万元):</lable>&nbsp;${plot.productScaleMax}&nbsp;
							<lable>到账金额(万元):</lable>&nbsp;${plot.orderTotalPayed}&nbsp;
							<lable>锁定到账金额(万元):</lable>&nbsp;${plot.orderTotalCorrectPayed}&nbsp;
							<lable>总配给额度(万元):</lable>&nbsp;${plot.orderTotalPushShare}&nbsp;
							<lable>配给接受额度(万元):</lable>&nbsp;${plot.orderTotalAcceptPushShare}&nbsp;
							<lable>小额订单投资人数限制(人):</lable>&nbsp;<span id="maxLowAmountClientCount">${plot.maxLowAmountClientCount}</span>&nbsp;
							<lable>到账订单数(份):</lable>&nbsp;${plot.orderTotalLimitPayedCount}&nbsp;
							<lable>锁定到账订单数(份):</lable>&nbsp;${plot.orderTotallimitCorrectCount}&nbsp;
						</div>
					</div>
				</td>
			</tr>
		</table>
	<ul id="tabnav">
		<li class="tab_1" ><a href="#" id="tab_1" onclick="tab(1)"  class="selected">未配给列表</a></li>
		<li class="tab_2" ><a href="#" id="tab_2" onclick="tab(2)" >已配给列表</a></li>
	</ul>
	<div id="div_1" class="tab-div-show">
		<table cellpadding="1" cellspacing="1" border="0" width="100%">
			<tr>
				<td width="100%" valign="top">
					<table id="newordersList"><tr><td>&nbsp;</td></tr></table>	
					<div id="newordersListPaged"></div>
				</td>
			</tr>
		</table>
	</div>
	<div id="div_2" class="tab-div-hide">
		<table cellpadding="1" cellspacing="1" border="0" width="100%">
			<tr>
				<td width="100%" valign="top">
					<table id="ordersList"><tr><td>&nbsp;</td></tr></table>	
					<div id="ordersListPaged"></div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
		<div class="left"></div>
			<div class="center">
				<input type="button" class="btn2" onclick="location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
			</div>
		<div class="right"></div>
	</div>
</body>
</html>