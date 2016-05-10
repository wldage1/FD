<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
<link  href="${base}/${skin}/css/tab.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#productList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
    jQuery("#productList").jqGrid({
        url: '${base}/market/succeed/grid.json',
        datatype: 'json',
        colNames: ["",
                "<msg:message code='order.orderNumber'/>",
       	        "<msg:message code='order.product.shortName'/>",
       	        "<msg:message code='product.org.name'/>", 
       	        "<msg:message code='order.clientName'/>",
       	        "<msg:message code='order.investAmount'/>",
       	        "<msg:message code='member.name'/>",
       	        "<msg:message code='order.tradeType'/>",
       	        "<msg:message code='order.docStatus'/>",
       	        "<msg:message code='redeemorder.subproduct.type'/>",
       	        "<msg:message code='order.operate'/>"
       	        ],
               colModel: [
                {name: 'id',index: 'id',align:'center',hidden: true,sortable: false},
                {name: 'orderNumber',index: 'orderNumber',align:'center',hidden: false,sortable: false}, 
                {name: 'product.shortName',index: 'product.shortName',align:'center',hidden: false,sortable: false},
                {name: 'provider.shortName',index: 'provider.shortName',align:'center',hidden: false,sortable: false},
       			{name: 'clientName',index: 'clientName',align:'center',hidden: false,sortable:false},
       			{name: 'investAmount',index: 'investAmount',align:'center',hidden: false,sortable:false},
       			{name: 'member.name',index: 'member.name',align:'center',hidden: false,sortable: false,
					formatter:function(cellvalue, options, rowdata){
						return rowdata.member.name + "（" + rowdata.member.nickName + "）";
				   }
	            },
       			{name: 'tradeType',index: 'tradeType',align:'center',hidden: false,sortable: false,formatter:changeZhn},
       			{name: 'docStatusName',index: 'docStatusName',align:'center',hidden: false,sortable: false},
       			{name: 'subProductType',index: 'subProductType',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowObject){
       				if(cellvalue == '' || cellvalue == null){
						return rowObject.product.shortName;
					}else{
						return cellvalue;
					}
       			}},
       			{name: 'act',index:'act',align:'left',hidden:false,sortable:false,formatter:actChange}],
        mtype:"POST",
        postData:{c:"${order.c}",tradeType:1,tradeStatus:7,productName:"${order.productName}",memberName:"${order.memberName}",documentStatus:"${order.documentStatus}"},
        rowNum:10,     
        page:"${order.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pageredOne',
        height:	230,
        autowidth: true,
        viewrecords: true,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	loadComplete:function(){
    		
    	},
        caption:'<msg:message code="order.tradeType.1.list"/>',
        toolbar: [true,"top"]
    });
    
    jQuery("#productList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    
    //查询按钮
    $("#select").click(function(){
    	jQuery("#productList").jqGrid("setGridParam",{page:0,postData:{productName:$("#productName").val(),memberName:$("#memberName").val(),docStatus:$("#docStatus").val()}}).trigger("reloadGrid");    
    });
    
    
    /*******************   操作按钮的功能实现   start    *********************/
    //查看 按钮的连接实现
    $(".shortcut_${market_succeed_detail.indexCode}").live("click",function(){
    	window.location.href='${base}${market_succeed_detail.controller}?c=${market_succeed_detail.code}&id='+this.id;
    });
    
    //初始化改变中文 交易类型
    function changeZhn(cellvalue, options, rowObject){
    	var content="";
    	<c:forEach items="${tradeTypeMap}" var="it">
	    	if(cellvalue==${it.key}){
	    		return '${it.value}';
	    	}
    	</c:forEach>
    }
    
    /*******************   操作按钮的功能实现   end    *********************/
    
    /*****************   赎回订单  START**********************/
    //赎回订单列表
    jQuery("#productList3").jqGrid({
        url: '#',
        datatype: 'local',
        colNames: ["",
            "<msg:message code='order.contractNumber'/>",
	        "<msg:message code='order.product.shortName'/>",
	        "<msg:message code='product.org.name'/>",
	        "<msg:message code='order.clientName'/>",
	        "<msg:message code='order.member.name'/>",
	        "<msg:message code='redeemorder.subproduct.type' />",
	        "<msg:message code='redeemorder.shares'/>",
	        "<msg:message code='redeemorder.amount'/>",
	        "<msg:message code='redeemorder.openTime'/>",
	        "<msg:message code='order.documentStatus'/>",
	        "<msg:message code='redeemorder.subproduct.type'/>",
	        "<msg:message code='order.operate'/>"
	        ],
        colModel: [
            {name: 'id',index: 'id',align:'center',hidden: true,sortable: false},
            {name: 'contractNumber',index: 'contractNumber',align:'center',hidden: false,sortable: false}, 
            {name: 'productName',index: 'productName',align:'center',hidden: false,sortable: false},
            {name: 'providerName',index: 'providerName',align:'center',hidden: false,sortable: false},
			{name: 'clientName',index: 'clientName',align:'center',hidden: false,sortable:false},
			{name: 'member.name',index: 'member.name',align:'center',hidden: false,sortable:false,formatter:function(cellvalue, options, rowObject){
				return cellvalue+"("+rowObject.member.nickName+")";
   			}},
			{name: 'subProductType',index:'subProductType',align:'center',sortable:false},
			{name: 'share',index: 'share',align:'center',hidden: false,sortable:false},
			{name: 'amount',index: 'amount',align:'center',hidden: false,sortable:false},
			{name: 'openTime',index: 'openTime',align:'center',hidden: false,sortable: false},
			{name: 'documentStatus',index: 'documentStatus',align:'center',hidden: false,sortable: false,
				formatter:function(cellvalue,options,rowdata){
					var content="";
             	   	<c:forEach var="item" items="${docStatusMap}">
						   if(cellvalue=='${item.key}'){
							   content="${item.value}";
						   }
					</c:forEach>
					return content;
    			}
			},
			{name: 'subProductType',index: 'subProductType',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowObject){
       				if(cellvalue == '' || cellvalue == null){
						return rowObject.productName;
					}else{
						return cellvalue;
					}
       			}},
			{name:'act',index:'act',align:'left',hidden:false,sortable:false,
				 formatter:function(cellvalue,options,rowdata){
					 var id=rowdata.id;
						var content="";
						//查看 连接 
						<c:if test="${!empty market_succeed_redeemDetail}">
					    <security:authorize ifAnyGranted="${market_succeed_redeemDetail.code}">
					    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_succeed_redeemDetail.indexCode}' title='${market_succeed_redeemDetail.name}'>";
					    	content += "<img src='${base}/${skin}/${market_succeed_redeemDetail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_succeed_redeemDetail.name}";
					    	content += "</a>";
					    </security:authorize>
						</c:if>
						return content;
	    			}   	
			}],
        mtype:"POST",
        postData:{c:"${order.c}",productName:"${order.productName}",memberName:"${order.memberName}",documentStatus:"${order.documentStatus}"},
        rowNum:10,     
        page:"${order.page}",
        rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height: 230,
        autowidth: true,
        viewrecords: true,
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
    	jQuery("#productList3").jqGrid("setGridParam",{page:0,postData:{productName:$("#productName3").val(),memberName:$("#memberName3").val(),documentStatus:$("#documentStatus3").val()}}).trigger("reloadGrid");    
    });
    
    $(".shortcut_${market_succeed_redeemDetail.indexCode}").live("click",function(){
    	window.location.href='${base}${market_succeed_redeemDetail.controller}?c=${market_succeed_redeemDetail.code}&id='+this.id;
    });
    /*****************   赎回订单  end**********************/
    
    var SubmitSelect = function(evt){
	    evt = window.event || evt;
	    if(evt.keyCode==13){//如果取到的键值是回车
	    	if($("#div_1").css("visibility")=='visible'){
    			$("#select").click();	
	    	}else if($("#div_3").css("visibility")=='visible'){
	    		$("#selectRedeemOrder").click();	
	    	}
	     }
	}
	window.document.onkeydown=SubmitSelect;
});

//列表操作功能展示
function actChange(cellvalue, options, rowObject){
	var id=rowObject.id;
	var content="";
	//查看 连接 
	<c:if test="${!empty market_succeed_detail}">
    <security:authorize ifAnyGranted="${market_succeed_detail.code}">
    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_succeed_detail.indexCode}' title='${market_succeed_detail.name}'>";
    	content += "<img src='${base}/${skin}/${market_succeed_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_succeed_detail.name}";
    	content += "</a>";
    </security:authorize>
	</c:if>
	return content;
}

//TAB切换
function tab_order(tag){
	changeTab(tag);
	if(tag==1){
		jQuery("#productList").jqGrid("setGridParam",{page:0,postData:{tradeType:1}}).setCaption('<msg:message code="order.tradeType.1.list"/>').trigger("reloadGrid");
	}else if(tag==2){
		jQuery("#productList").jqGrid("setGridParam",{page:0,postData:{tradeType:2}}).setCaption('<msg:message code="order.tradeType.2.list"/>').trigger("reloadGrid");
	}else{
		jQuery("#productList3").jqGrid("setGridParam",{page:0,datatype:'json',url:'succeed/redeemordergrid.json'}).trigger("reloadGrid");
	}
}

function changeTab(tag){
	//重置查询条件
	document.getElementById('order').reset();
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
		$("#tab_3").addClass("selected");
		$("#div_3").css("visibility","visible");
		$("#div_1").css("visibility","hidden");
		$("#tab_2").removeClass("selected");
		$("#tab_1").removeClass("selected");
	}
}
</script>
	</head>
	<body class="skinMain">
	<ul id="tabnav">
		<li class="tab_1" ><a href="javascript:void(0);" id="tab_1" onclick="tab_order(1)" class="selected"><msg:message code="order.tradeType.1"/></a></li>
		<li class="tab_2" ><a href="javascript:void(0);" id="tab_2" onclick="tab_order(2)"><msg:message code="order.tradeType.2"/></a></li>
		<li class="tab_3" ><a href="javascript:void(0);" id="tab_3" onclick="tab_order(3)"><msg:message code="redeemOrder.order"/></a></li>
	</ul>
	<div id="div_1"  class="tab-div-show">
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%">
					<input type="hidden" name="c" value="${order.c}"/>
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td align="center">
							<form:form method="post" action="#" commandName="order" name="f">
								<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
									<tr>
										<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.product.shortName'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<input id="productName" maxlength="20" value="${order.productName}"/>
										</td>
										<td align="right" width="10%" class="search_info"><msg:message code='member.name'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<input id="memberName" maxlength="20"  value="${order.memberName}"/>
										</td>
										<td rowspan="2" width="20%" class="button_area" align="center">
											<input type="button" class="btn2" id="select" value="<msg:message code='button.search'/>">
										</td>
									</tr>	
									<tr>
										<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.docStatus'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable" colspan="3">
											<form:select path="docStatus">
												<form:option value="">全部</form:option>
												<c:forEach items="${docStatusMap }" var="it">
													<form:option value="${it.key}">${it.value}</form:option>
												</c:forEach>
											</form:select>
										</td>
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
											<input id="productName3" maxlength="20" value="${order.productName}"/>
										</td>
										<td align="right" width="10%" class="search_info"><msg:message code='member.name'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<input id="memberName3" maxlength="20" value="${order.memberName}"/>
										</td>
										<td rowspan="2" width="20%" class="button_area" align="center">
											<input type="button" class="btn2" id="selectRedeemOrder" value="<msg:message code='button.search'/>">
										</td>
									</tr>	
									<tr>
										<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.docStatus'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable" colspan="3">
											<select id="documentStatus3">
												<option value="">全部</option>
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