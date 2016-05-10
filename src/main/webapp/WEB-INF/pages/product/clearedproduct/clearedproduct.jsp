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
		$("#productList").setGridWidth($(window).width()*0.999);　
	});
	var zTree = ""; 
	$(document).ready(function(){
		jQuery("#productList").jqGrid({
			url: 'clearedproduct/grid.json',
			datatype: 'json',
			colNames: ["","<msg:message code='product.shortName'/>","<msg:message code='product.code'/>","<msg:message code='product.type'/>","<msg:message code='product.provider' />","<msg:message code='product.clearedTime'/>","<msg:message code='product.operate'/>",""],
			colModel: [{name: 'id',index: 'id',width:'1%',align:'center',hidden: true,sortable: false},
						{name: 'shortName',index: 'shortName',width:'30%',align:'center',hidden: false,sortable: false}, 
						{name: 'code',index: 'code',width:'10%',align:'center',hidden: false,sortable: false},
						{name: 'type',index: 'type',width:'10%',align:'center',hidden: false,sortable:false,formatter:changeType},
						{name: 'providerShortName',index: 'providerShortName',width:'10%',align:'center',hidden: false,sortable:false},
						{name: 'createTime',index: 'createTime',width:'15%',align:'center',hidden: false,sortable:false},
						{name:'act',index:'act', width:'5%',align:'center',hidden:false,sortable:false,formatter:initAct},
						{name: 'type',index: 'type',hidden: true}],
			mtype:"POST",
			postData:{c:"${product.c}",name:"${product.name}",type:"${product.type}"},
			rowNum:10,     
			page:"${product.page}",
			rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
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
		jQuery("#productList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
		
		<!-- 列表按钮 -->
		//查看按钮 连接绑定事件
		$(".shortcut_${product_clearedproduct_cleareddetail.indexCode}").live("click",function(){
			var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
			window.location.href='${base}${product_clearedproduct_cleareddetail.controller}?c=${product_clearedproduct_cleareddetail.code}&id='+this.id+'&type='+rowdata.type;
		});
		<!--// 列表按钮 -->
		
		//查询按钮
		$("#select").click(function(){
			this.form.submit();
			//jQuery("#productList").jqGrid("setGridParam",{postData:{name:$("#name").val(),type:$("#type").val()}}).trigger("reloadGrid");    
		});
		
	});

	//初始化列表后的操作
	function initAct(cellvalue, options, rowObject){
		var id = rowObject.id;
		var rowdata = rowObject;
		var content = "";
		<!-- 查看 -->
		<c:if test="${!empty product_clearedproduct_cleareddetail}">
	    <security:authorize ifAnyGranted="${product_clearedproduct_cleareddetail.code}">
	    	content += "<a href='#' id='" + id + "' class='shortcut_${product_clearedproduct_cleareddetail.indexCode}' title='${product_clearedproduct_cleareddetail.name}'>";
	    	content += "<img src='${base}/${skin}/${product_clearedproduct_cleareddetail.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${product_clearedproduct_cleareddetail.name}'>${product_clearedproduct_cleareddetail.name}</font>";
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
							<form:form method="post" action="clearedproduct" commandName="product" name="f">
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
								<table id="productList"><tr><td>&nbsp;</td></tr></table>
								<div id="pagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>