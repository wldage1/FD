<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
<script type="text/javascript">
	$(window).resize(function(){ 　　
		$("#orderList").setGridWidth($(window).width()*0.999);　
	});
	$(document).ready(function(){
		//已取消的认购、申购订单查询
		jQuery("#orderList").jqGrid({
			url: '${base}/market/cancel/grid.json',
			datatype: 'json',
			colNames: ["<msg:message code='order.orderNumber'/>",
			           "<msg:message code='order.product.shortName'/>",
			           "<msg:message code='product.org.name'/>",
			           "<msg:message code='order.clientName'/>",
			           "<msg:message code='order.investAmount'/>",
			           "<msg:message code='member.name'/>",
			           "<msg:message code='redeemorder.subproduct.type'/>",
			           "<msg:message code='info.operate'/>"],
			colModel: [{name:'orderNumber', index:'s.orderNumber', align:'center', hidden:false, sortable:false},
			           {name:'productShortName', index:'productShortName', align:'center', hidden:false, sortable:false},
			           {name:'providerShortName', index:'providerShortName', align:'center', hidden:false, sortable:false},
			           {name:'clientName', index:'s.clientName', align:'center', hidden:false, sortable:false},
			           {name:'investAmount', index:'s.investAmount', align:'center', hidden:false, sortable:false},
			           {name:'memberName', index:'memberName', align:'center', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   return rowdata.memberName + "（" + rowdata.memberNickName + "）";
			        	   }
			           },
			           {name: 'subProductType',index: 'subProductType',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowObject){
							if(cellvalue == '' || cellvalue == null){
								return rowObject.productShortName;
							}else{
								return cellvalue;
							}
						}},
			           {name:'act', index:'p.act', align:'left', sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   var id = rowdata.id;
			        		   var content = "";
			        		   <c:if test="${!empty market_cancel_detail}">
			        		   <security:authorize ifAnyGranted="${market_cancel_detail.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_cancel_detail.indexCode}'";
			        		   		content += " title='${market_cancel_detail.name}'>";
			        		   		content += "<img src='${base}/${skin}/${market_cancel_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_cancel_detail.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty market_cancel_faildispose}">
			       	        	<security:authorize ifAnyGranted="${market_cancel_faildispose.code}">
			       	        		if($("#tradeType").val()=='2'){
			       	        			content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_cancel_faildispose.indexCode}'";
				        		   		content += " title='${market_cancel_faildispose.name}'>";
				        		   		content += "<img src='${base}/${skin}/${market_cancel_faildispose.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_cancel_faildispose.name}";
				        		   		content += "</a>";
			       	        		}
				       	     	</security:authorize>
			        		   </c:if>
			        		   return content;
			        	   }
			           }],
			mtype: "POST",
			postData: {c:"${order.c}", tradeType:1,pushStatus:"${order.pushStatus}",productName:"${order.productName}",memberName:"${order.memberName}"},
			rowNum: 10,
			rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#orderPagered',
			height: '230',
			autowidth: true,
			viewrecords: true,
			rownumbers: true,
			multiselect:true,
			jsonReader: {
				repeatitems: false
			},
			caption: '<msg:message code="subscribe.list"/>',
			toolbar:[true,"top"]
		});
		//添加注销按钮至 工具栏
		<c:if test="${!empty market_cancel_faildispose}">
	        <security:authorize ifAnyGranted="${market_cancel_faildispose.code}">
		        var $ea = $("<a></a>").attr("href","javascript:void(0)")
		    	.attr("id","${market_cancel_faildispose.indexCode}")
		    	.attr("border","0").attr("class","sc_${market_cancel_faildispose.indexCode}")
		    	.append($("<img/>").attr("src","${base}/${skin}/${market_cancel_faildispose.icon}")
		      	.attr("width","18").attr("height","18").attr("border","0")
		      	.attr("border","0").attr("align","absmiddle"))
		      	.append("${market_cancel_faildispose.name}");
		    	$("#t_orderList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea)); 
	        </security:authorize>
		</c:if>

		$(".sc_${market_cancel_faildispose.indexCode}").click(function(){
			var ids=$("#orderList").jqGrid().getGridParam('selarrrow');
			if(ids==null || ids=='' || !ids.length>0){
				top.$.layer({
					area : ['auto','auto'],
					dialog : {msg:'请选择需要注销的订单！',type : 8}
				});
				return false;
			}
			deleteArry(true,ids+"");
		});
		
		$(".shortcut_${market_cancel_faildispose.indexCode}").live('click',function(){
			if(this.id==null || this.id==''){
				top.$.layer({
					area : ['auto','auto'],
					dialog : {msg:'请选择需要注销的订单！',type : 8}	
				});
				return false;
			}
			deleteArry(false,this.id);
		});
		function deleteArry(t,str){
			if(t){
				var data={arrid:str,c:'${market_cancel_faildispose.code}'};
			}else{
				var data={id:str,c:'${market_cancel_faildispose.code}'};
			}
		var k=$.layer({
			    shade : false,
			    area : ['auto','auto'],
			    dialog : {
			        msg:'是否确认注销？',
			        btns : 2, 
			        type : 4,
			        btn : ['<msg:message code='order.true.button'/>','<msg:message code='order.fail.button'/>'],
			        yes : function(){
			        	$.ajax({
	    					url: '${base}${market_cancel_faildispose.controller}.json',
	    					type:'post',
	    					timeout:'60000',
	    					dataType:'json',
	    					data: data, 
	    					async:false,
	    					cache:false,
	    					success:function(jsonData){
	    						layer.close(k);
	    						if(jsonData.status=='success'){
	    							top.$.layer({
	    								area : ['auto','auto'],
	    								dialog : {msg:'订单数据注销成功！',type : 9}	
	    							});
	    							jQuery("#orderList").jqGrid().trigger("reloadGrid");
	    						}else{
	    							top.$.layer({
	    								area : ['auto','auto'],
	    								dialog : {msg:'订单数据注销失败！',type : 8}	
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
		
		
		//已取消的认购、申购订单查看详细
		$(".shortcut_${market_cancel_detail.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			window.location.href = "${base}${market_cancel_detail.controller}?c=${market_cancel_detail.code}&id=" + rowid;
		});
		//已取消的认购、申购订单查询按钮
		$("#select").click(function(){
	    	jQuery("#orderList").jqGrid("setGridParam",{page:0,postData:{c:"${order.c}",pushStatus:$("#pushStatus").val(),tradeType:$("#tradeType").val(),productName:$("#productName").val(),memberName:$("#memberName").val()}}).trigger("reloadGrid");    
	    });
		//已取消的赎回订单查询
		jQuery("#redeemList").jqGrid({
			url: '#',
			datatype: 'local',
			colNames: ["<msg:message code='order.orderNumber'/>",
			           "<msg:message code='order.product.shortName'/>",
			           "<msg:message code='product.org.name'/>",
			           "<msg:message code='order.clientName'/>",
			           "<msg:message code='member.name'/>",
			           "<msg:message code='redeemorder.subproduct.type'/>",
			           "<msg:message code='info.operate'/>"],
			colModel: [{name:'orderNumber', index:'s.orderNumber', align:'center',width:'15%', hidden:false, sortable:false},
			           {name:'productName', index:'s.productName', align:'center',width:'15%', hidden:false, sortable:false},
			           {name:'provider.shortName', index:'s.provider.shortName',width:'15%', align:'center', hidden:false, sortable:false},
			           {name:'clientName', index:'s.clientName', align:'center',width:'15%', hidden:false, sortable:false},
			           {name:'member.name', index:'s.member.name', align:'center',width:'15%', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   return rowdata.member.name + "（" + rowdata.member.nickName + "）";
			        	   }
			           },
			           {name: 'subProductType',index: 'subProductType',align:'center',width:'15%',hidden: false,sortable: false,formatter:function(cellvalue, options, rowObject){
							if(cellvalue == '' || cellvalue == null){
								return rowObject.productName;
							}else{
								return cellvalue;
							}
						}},
			           {name:'act', index:'p.act', align:'left',width:'5%', sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   var id = rowdata.id;
			        		   var content = "";
			        		   <c:if test="${!empty market_cancel_detailRedeem}">
			        		   <security:authorize ifAnyGranted="${market_cancel_detailRedeem.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_cancel_detailRedeem.indexCode}'";
			        		   		content += " title='${market_cancel_detailRedeem.name}'>";
			        		   		content += "<img src='${base}/${skin}/${market_cancel_detailRedeem.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_cancel_detailRedeem.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   return content;
			        	   }
			           }],
			mtype: "POST",
			postData: {c:"${order.c}",pushStatus:"${order.pushStatus}",productName:"${order.productName}",memberName:"${order.memberName}"},
			rowNum: 10,
			rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#redeemPagered',
			height: '230',
			autowidth: true,
			viewrecords: true,
			rownumbers: true,
			jsonReader: {
				repeatitems: false
			},
			caption: '<msg:message code="redeem.list"/>'
		});
		//已取消的赎回订单查看详细
		$(".shortcut_${market_cancel_detailRedeem.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var rowdata = jQuery("#redeemList").jqGrid('getRowData',rowid);
			window.location.href = "${base}${market_cancel_detailRedeem.controller}?c=${market_cancel_detailRedeem.code}&id=" + rowid;
		});
		//已取消的赎回订单查询按钮
		$("#selectRedeem").click(function(){
	    	jQuery("#redeemList").jqGrid("setGridParam",{postData:{page:0,c:"${order.c}",pushStatus:$("#pushStatus").val(),productName:$("#productName2").val(),memberName:$("#memberName2").val()}}).trigger("reloadGrid");    
	    });
		
		var SubmitSelect = function(evt){
		    evt = window.event || evt;
		    if(evt.keyCode==13){//如果取到的键值是回车
		    	if($("#div_order").css("visibility")=='visible'){
	    			$("#select").click();	
		    	}else if($("#div_redeem").css("visibility")=='visible'){
		    		$("#selectRedeem").click();	
		    	}
		     }
		}
		window.document.onkeydown=SubmitSelect;
		$("#${market_cancel_faildispose.indexCode}").css("display","none");
	});
	//导航标签切换
	function tab(tag){
		//重置查询条件
		document.getElementById('order').reset();
		if(tag == 1){
			$("#tab_1").addClass("selected");
			$("#tab_2").removeClass("selected");
			$("#tab_3").removeClass("selected");
			$("#div_order").css("visibility","visible");
			$("#div_redeem").css("visibility","hidden");
			$("#tradeType").val(1);
			jQuery("#orderList").jqGrid("setGridParam",{page:0,postData:{tradeType:1}}).setCaption('<msg:message code="subscribe.list"/>').trigger("reloadGrid");
			$("#${market_cancel_faildispose.indexCode}").css("display","none");
		}else if(tag == 2){
			$("#tab_2").addClass("selected");
			$("#tab_1").removeClass("selected");
			$("#tab_3").removeClass("selected");
			$("#div_order").css("visibility","visible");
			$("#div_redeem").css("visibility","hidden");
			$("#tradeType").val(2);
			jQuery("#orderList").jqGrid("setGridParam",{page:0,postData:{tradeType:2}}).setCaption('<msg:message code="superaddition.list"/>').trigger("reloadGrid");
			$("#${market_cancel_faildispose.indexCode}").css("display","");
		}else{
			$("#tab_3").addClass("selected");
			$("#tab_1").removeClass("selected");
			$("#tab_2").removeClass("selected");
			$("#div_redeem").css("visibility","visible");
			$("#div_order").css("visibility","hidden");
			jQuery("#redeemList").jqGrid("setGridParam",{page:0,datatype:'json',url:'cancel/grid.json'}).trigger("reloadGrid");
		}
	}
</script>
	</head>
	<body class="skinMain">
		<ul id="tabnav">
			<li class="tab1"><a href="javascript:void(0);" id="tab_1" onclick="tab(1)" class="selected"><msg:message code="order.tradeType.1"/></a></li>
			<li class="tab2"><a href="javascript:void(0);" id="tab_2" onclick="tab(2)"><msg:message code="order.tradeType.2"/></a></li>
			<li class="tab3"><a href="javascript:void(0);" id="tab_3" onclick="tab(3)"><msg:message code="redeemOrder.order"/></a></li>
		</ul>
		<input type="hidden" name="tradeType" id="tradeType" value="1">
		<div id="div_order" class="tab-div-show">
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
				<tr>
					<td width="100%">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td>
									<form:form method="post" action="#" commandName="order" name="f">
										<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
											<tr>
												<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.product.shortName'/><msg:message code="system.common.sign.colon"/></td>
												<td align="left" width="30%"  class="search_lable">
													<input id="productName" maxlength="20" value="${order.productName}"/>
												</td>
												<td align="right" width="10%" class="search_info"><msg:message code='member.name'/><msg:message code="system.common.sign.colon"/></td>
												<td align="left" width="30%"  class="search_lable">
													<input id="memberName" maxlength="20" value="${order.memberName}"/>
												</td>
												<td rowspan="2" width="20%" class="button_area" align="center">
													<input type="button" class="btn2" id="select" value="<msg:message code='button.search'/>">
												</td>
											</tr>
											<tr>
												<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.pushStatus'/><msg:message code="system.common.sign.colon"/></td>
												<td align="left" colspan="3" width="30%"  class="search_lable">
													<select id="pushStatus">
														<option value=""><msg:message code='select.all'/></option>
														<c:forEach items="${pushStatusMap}" var="item">
															<c:if test="${pushStatusMap != null}">
																<option value="${item.key}">${item.value}</option>
															</c:if>
														</c:forEach>
													</select>
												</td>
											</tr>
										</table>
									</form:form>
								</td>
							</tr>
							<tr>
								<td>
									<table id="orderList"><tr><td>&nbsp;</td></tr></table>
									<div id="orderPagered"></div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<div id="div_redeem" class="tab-div-hide">
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
				<tr>
					<td width="100%">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td>
									<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
										<tr>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.product.shortName'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="30%"  class="search_lable">
												<input id="productName2" maxlength="20" value="${order.productName}"/>
											</td>
											<td align="right" width="10%" class="search_info"><msg:message code='member.name'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="30%"  class="search_lable">
												<input id="memberName2" maxlength="20" value="${order.memberName}"/>
											</td>
											<td width="20%" class="button_area" align="center">
												<input type="button" class="btn2" id="selectRedeem" value="<msg:message code='button.search'/>" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table id="redeemList"><tr><td>&nbsp;</td></tr></table>
									<div id="redeemPagered"></div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>