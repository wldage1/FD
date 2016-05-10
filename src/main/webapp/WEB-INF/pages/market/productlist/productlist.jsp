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
			url: 'productlist/grid.json',
			datatype: 'json',
			colNames: ["","<msg:message code='product.shortName'/>","<msg:message code='product.code'/>","<msg:message code='product.type'/>","<msg:message code='product.operate'/>","","","",""],
			colModel: [{name: 'id',index: 'id',width:'1%',align:'center',hidden: true,sortable: false},
						{name: 'shortName',index: 'shortName',width:'50%',align:'center',hidden: false,sortable: false}, 
						{name: 'code',index: 'code',width:'20%',align:'center',hidden: false,sortable: false},
						{name: 'type',index: 'type',width:'10%',align:'center',hidden: false,sortable:false,formatter:changeType},
						{name:'act',index:'act', width:'20%',align:'center',hidden:false,sortable:false,formatter:initAct},
						{name: 'type',index: 'type',hidden: true},
						{name:'isRemittance',index:'isRemittance',hidden:true},
						{name:'isTotalShare',index:'isTotalShare',hidden:true},
						{name:'sellStatus',index:'sellStatus',hidden:true}
						],
			mtype:"POST",
			postData:{c:"${product.c}",name:"${product.name}",type:"${product.type}"},
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
		
		// 产品拆分按钮
		$(".shortcut_${market_productlist_pushshare.indexCode}").live("click",function(){
			var rowdata = jQuery("#salingProductList").jqGrid('getRowData',this.id);
			if(rowdata.sellStatus == 5 || rowdata.sellStatus == 6 || rowdata.sellStatus == 9){
				top.$.layer({
					shade : [0.5 , '#000' , true],
					area : ['auto','auto'],
					offset : ['150px',''],
					dialog : {msg:'产品已成立或封帐，不需要配给了！',type : 8}	
				});
			}else{
				if(rowdata.isRemittance == "0"){
					window.location.href='${base}${market_productlist_pushshare.controller}?c=${market_productlist_pushshare.code}&id='+rowdata.id;
				}else{
					top.$.layer({
						shade : [0.5 , '#000' , true],
						area : ['auto','auto'],
						offset : ['150px',''],
						dialog : {msg:'抱歉！打款未停止',type : 8}	
					});
				}
			}
		});
		
		
		<!--// 列表按钮 -->
		
		//查询按钮
		$("#select").click(function(){
			this.form.submit();
			//jQuery("#salingProductList").jqGrid("setGridParam",{postData:{name:$("#name").val(),type:$("#type").val()}}).trigger("reloadGrid");    
		});
		
	});

	//初始化列表后的操作
	function initAct(cellvalue, options, rowObject){
		var id = rowObject.id;
		var rowdata = rowObject;
		var isTotalShare = rowObject.isTotalShare;
		var content = "";
		<!-- 销售控制 -->
		
		// 销售参数设置
		<c:if test="${!empty market_productlist_pushshare}">
	    <security:authorize ifAnyGranted="${market_productlist_pushshare.code}">
	    	content += "<a href='#' id='" + id + "' class='shortcut_${market_productlist_pushshare.indexCode}' title='${market_productlist_pushshare.name}'>";
	    	content += "<img src='${base}/${skin}/${market_salemanage_saleconfig_config.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${market_productlist_pushshare.name}'>${market_productlist_pushshare.name}</font>";
	    	content += "</a>";
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
							<form:form method="post" action="productlist" commandName="product" name="f">
					<input type="hidden" name="c" value="${product.c}"/>
								<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
									<tr>
										<td align="right" width="10%" class="search_info" height="25"><msg:message code='product.shortName'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<form:input path="name" id="name" htmlEscape="true" />
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