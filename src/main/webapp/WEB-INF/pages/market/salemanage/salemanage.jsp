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
		$("#salingProductList").setGridWidth($(window).width()*0.999);　
	});
	var zTree = "";
	
	$(document).ready(function(){
		jQuery("#salingProductList").jqGrid({
			url: 'salemanage/grid.json',
			datatype: 'json',
			colNames: ["","<msg:message code='product.shortName'/>","<msg:message code='product.provider' />","<msg:message code='product.code'/>",
			           "<msg:message code='product.type'/>","<msg:message code='product.sale.distribute.newOrderCount'/>","<msg:message code='product.operate'/>","","",""],
			colModel: [{name: 'id',index: 'id',hidden: true},
						{name: 'shortName',index: 'shortName',width:'25%',align:'center',hidden: false,sortable: false}, 
						{name: 'providerShortName',index: 'providerShortName',width:'15%',align:'center',hidden: false,sortable: false}, 
						{name: 'code',index: 'code',width:'10%',align:'center',hidden: false,sortable: false},
						{name: 'type',index: 'type',width:'10%',align:'center',hidden: false,sortable:false,formatter:changeType},
						{name: 'newOrderCount',index: 'newOrderCount',width:'5%',align:'center',hidden: false,sortable:false,formatter:newOrderCountFa},
						{name:'act',index:'act', width:'35%',align:'center',hidden:false,sortable:false,formatter:initAct},
						{name: 'type',index: 'type',hidden: true},
						{name:'isTotalShare',index:'isTotalShare',hidden:true},
						{name:'sellStatus',index:'sellStatus',hidden:true}
						],
			mtype:"POST",
			postData:{c:"${product.c}",name:"${product.name}",type:"${product.type}",code:"${product.code}"},
			rowNum:10,     
			page:"${product.page}",
			rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#pagered',
			height:'auto',
			autowidth: true,
			viewrecords: true,
			multiselect: false,
			rownumbers: true,
			jsonReader: {
				repeatitems: false
			},
			caption:'<msg:message code="product.list"/>'
		});
		jQuery("#salingProductList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
		
		<!-- 列表按钮 -->
		//查看详细
		$(".shortcut_${market_salemanage_detail.indexCode}").live("click",function(){
    			var rowdata = jQuery("#salingProductList").jqGrid('getRowData',this.id);
    			window.location.href='${base}${market_salemanage_detail.controller}?c=${market_salemanage_detail.code}&id='+this.id+'&type='+rowdata.type;
    	});
		
		// 设置销售参数
		$(".shortcut_${market_salemanage_saleconfig_config.indexCode}").live("click",function(){
			var rowdata = jQuery("#salingProductList").jqGrid('getRowData',this.id);
			window.location.href='${base}${market_salemanage_saleconfig_config.controller}?c=${market_salemanage_saleconfig_config.code}&id='+rowdata.id;
		});
		
		// 设置单证邮寄参数
		$(".shortcut_${market_salemanage_saleconfig_docconfig.indexCode}").live("click",function(){
			var rowdata = jQuery("#salingProductList").jqGrid('getRowData',this.id);
			window.location.href='${base}${market_salemanage_saleconfig_docconfig.controller}?c=${market_salemanage_saleconfig_docconfig.code}&productId='+rowdata.id;
		});
		
		// 产品拆分按钮
		$(".shortcut_${market_salemanage_saleconfig.indexCode}").live("click",function(){
			var rowdata = jQuery("#salingProductList").jqGrid('getRowData',this.id);
			window.location.href='${base}${market_salemanage_saleconfig.controller}?c=${market_salemanage_saleconfig.code}&id='+rowdata.id;
		});
		
		// 销售控制按钮
		$(".shortcut_${market_salemanage_manage.indexCode}").live("click",function(){
			var rowdata = jQuery("#salingProductList").jqGrid('getRowData',this.id);
			jQuery.ajax({
				url:"${base}/market/salemanage/saleconfig/countsubproduct.json",type: "post",contentType : 'application/x-www-form-urlencoded',
				data:{c:"${c}",productId:this.id},dataType : 'json',
				success:function(data){
					if(data.count > 0){
						window.location.href='${base}${market_salemanage_manage_overview.controller}?c=${market_salemanage_manage_overview.code}&id='+rowdata.id;
					}else{
						top.layer.msg("请先设置该产品的销售参数并拆分产品！");
					}
				}
			});
		});
		
		<!--// 列表按钮 -->
		
		//查询按钮
		$("#select").click(function(){
			// $('#search_form').submit();
			//this.form.submit();
			jQuery("#salingProductList").jqGrid("setGridParam",{postData:{name:$("#name").val(),type:$("#type").val(),code:$("#code").val()},page:"1"}).trigger("reloadGrid");    
		});
		
	});
	
	// 新预约订单提醒
	function newOrderCountFa(cellvalue, options, rowObject){
		if(cellvalue > 0){
			return "<font style='color:red;font-weight:bold;'>"+cellvalue+"</font>";
		}else{
			return 0;
		}
	}

	//初始化列表后的操作
	function initAct(cellvalue, options, rowObject){
		var id = rowObject.id;
		var rowdata = rowObject;
		var isTotalShare = rowObject.isTotalShare;
		var productType = rowObject.type;
		var sellSatus = rowObject.sellStatus;
		var content = "";
		<!-- 销售控制 -->
		
		<c:if test="${!empty market_salemanage_detail}">
	    <security:authorize ifAnyGranted="${market_salemanage_detail.code}">
	    	content += "<a href='#' id='" + id + "' class='shortcut_${market_salemanage_detail.indexCode}' title='${market_salemanage_detail.name}'>";
	    	content += "<img src='${base}/${skin}/${market_salemanage_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${market_salemanage_detail.name}'>${market_salemanage_detail.name}</font>";
	    	content += "</a>";
	    </security:authorize>
    	</c:if>
		
		// 销售参数设置
		<c:if test="${!empty market_salemanage_saleconfig_config}">
	    <security:authorize ifAnyGranted="${market_salemanage_saleconfig_config.code}">
	    	content += "<a href='#' id='" + id + "' class='shortcut_${market_salemanage_saleconfig_config.indexCode}' title='${market_salemanage_saleconfig_config.name}'>";
	    	content += "<img src='${base}/${skin}/${market_salemanage_saleconfig_config.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${market_salemanage_saleconfig_config.name}'>${market_salemanage_saleconfig_config.name}</font>";
	    	content += "</a>";
	    </security:authorize>
    	</c:if>
		
		// 销售单证邮寄设置
		<c:if test="${!empty market_salemanage_saleconfig_docconfig}">
	    <security:authorize ifAnyGranted="${market_salemanage_saleconfig_docconfig.code}">
	    	content += "<a href='#' id='" + id + "' class='shortcut_${market_salemanage_saleconfig_docconfig.indexCode}' title='${market_salemanage_saleconfig_docconfig.name}'>";
	    	content += "<img src='${base}/${skin}/${market_salemanage_saleconfig_docconfig.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${market_salemanage_saleconfig_docconfig.name}'>${market_salemanage_saleconfig_docconfig.name}</font>";
	    	content += "</a>";
	    </security:authorize>
    	</c:if>
		
		// 产品拆分
		<c:if test="${!empty market_salemanage_saleconfig}">
	    <security:authorize ifAnyGranted="${market_salemanage_saleconfig.code}">
			if(isTotalShare != null){
				content += "<a href='#' id='" + id + "' class='shortcut_${market_salemanage_saleconfig.indexCode}' title='${market_salemanage_saleconfig.name}'>";
				content += "<img src='${base}/${skin}/${market_salemanage_saleconfig.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${market_salemanage_saleconfig_split.name}'>${market_salemanage_saleconfig_split.name}</font>";
				content += "</a>";
			}else{
				content += "<span>";
				content += "<img src='${base}/${skin}/${market_salemanage_saleconfig.icon}' weight='18' height='18' border='0' align='absmiddle'/><font style='color:#999;' title='${market_salemanage_saleconfig_split.name}'>${market_salemanage_saleconfig_split.name}</font>";
				content += "</span>";
			}
	    </security:authorize>
    	</c:if>

		// 概览
		<c:if test="${!empty market_salemanage_manage}">
	    <security:authorize ifAnyGranted="${market_salemanage_manage.code}">
			if(isTotalShare == null){
				content += "<span>";
				content += "<img src='${base}/${skin}/${market_salemanage_manage.icon}' weight='18' height='18' border='0' align='absmiddle'/><font style='color:#999;'  title='${market_salemanage_manage.name}'>${market_salemanage_manage.name}</font>";
				content += "</span>";
			}else{
				content += "<a href='#' id='" + id + "' class='shortcut_${market_salemanage_manage.indexCode}' title='${market_salemanage_manage.name}'>";
				content += "<img src='${base}/${skin}/${market_salemanage_manage.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${market_salemanage_manage.name}'>${market_salemanage_manage.name}</font>";
				content += "</a>";
			}
	    </security:authorize>
    	</c:if>
   		return content;
	} 
	
	//产品类型中文转换
	function changeType(cellvalue, options, rowObject){
		<c:forEach items="${productTypeMap}" var="it">
			if(cellvalue==${it.key}){
				return '${it.value}';		
			}
		</c:forEach>
	}

</script>
	</head>
	<body class="skinMain">
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%">
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td align="center">
							<form:form method="post" action="salemanage" commandName="product" name="f" id="search_form">
							<input type="hidden" name="c" value="${product.c}"/>
								<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
									<tr>
										<td align="right" width="10%" class="search_info" height="25"><msg:message code='product.shortname'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<form:input path="name"  htmlEscape="true"/>
										</td>
										<td align="right" width="10%" class="search_info"><msg:message code='product.type'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<form:select path="type">
												<form:option value="">全部</form:option>
												<c:forEach items="${productTypeMap}" var="it">
													<form:option value="${it.key}">${it.value}</form:option>
												</c:forEach>
											</form:select>
										</td>
										<td rowspan="3" width="20%" class="button_area" align="center">
											<input type="button" class="btn2" id="select" value="<msg:message code='button.search'/>">
										</td>
									</tr>	
									<tr>
										<td align="right" width="10%" class="search_info" height="25"><msg:message code='product.code'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable" colspan="3">
											<form:input path="code"  htmlEscape="true"/>
										</td>
									</tr>	
								</table>
							</form:form>
							</td>
						</tr>
						<tr>
							<td>
								<table id="salingProductList"><tr><td>&nbsp;</td></tr></table>
								<div id="pagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>