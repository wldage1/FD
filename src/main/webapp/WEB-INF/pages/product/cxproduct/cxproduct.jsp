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
			url: 'cxproduct/grid.json',
			datatype: 'json',
			colNames: ["","","","<msg:message code='product.shortName'/>","<msg:message code='product.code'/>","<msg:message code='product.type'/>","<msg:message code='product.provider' />","<msg:message code='product.operate'/>",""],
			colModel: [{name: 'id',index: 'id',width:'1%',align:'center',hidden: true,sortable: false},
						{name: 'sellStatus',index: 'sellStatus',width:'1%',align:'center',hidden: true,sortable: false},
						{name: 'raiseFinishTime',index: 'raiseFinishTime',width:'1%',align:'center',hidden: true,sortable: false},
						{name: 'shortName',index: 'shortName',width:'30%',align:'center',hidden: false,sortable: false}, 
						{name: 'code',index: 'code',width:'10%',align:'center',hidden: false,sortable: false},
						{name: 'type',index: 'type',width:'10%',align:'center',hidden: false,sortable:false,formatter:changeType},
						{name: 'providerShortName',index: 'providerShortName',width:'20%',align:'center',hidden: false,sortable:false},
						{name:'act',index:'act', width:'20%',align:'center',hidden:false,sortable:false,formatter:initAct},
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
		$(".shortcut_${product_cxproduct_cxdetail.indexCode}").live("click",function(){
			var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
			window.location.href='${base}${product_cxproduct_cxdetail.controller}?c=${product_cxproduct_cxdetail.code}&id='+this.id+'&type='+rowdata.type;
		});
		//产品上传按钮 连接绑定事件
		$(".shortcut_${product_manage_attachment.indexCode}").live("click",function(){
			var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
			window.location.href='${base}${product_manage_attachment.controller}?c=${product_manage_attachment.code}&id='+this.id;
		});
		//产品净值 连接绑定事件
		$(".shortcut_${market_salemanage_saleconfig_natvalue.indexCode}").live("click",function(){
			var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
			window.location.href='${base}${market_salemanage_saleconfig_natvalue.controller}?c=${market_salemanage_saleconfig_natvalue.code}&productId='+this.id;
		});
		<!--// 列表按钮 -->
		
		//查询按钮
		$("#select").click(function(){
			 $('#search_form').submit();
			//jQuery("#productList").jqGrid("setGridParam",{postData:{name:$("#name").val(),type:$("#type").val()}}).trigger("reloadGrid");    
		});
		
		//产品清算定事件
		$(".shortcut_${product_cxproduct_clearproduct.indexCode}").live("click",function(){
			var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
			//封帐日
			var raiseFinishTime =new Date(rowdata.raiseFinishTime.replace("-", "/")); 
			//当前时间
			var now = new Date();
			//产品成立并且到封帐日才能被清算
			if(raiseFinishTime <= now && rowdata.sellStatus == 5){
				var k=$.layer({
					shade : false,
					area : ['auto','auto'],
					dialog : {
						msg:'是否要清算"'+rowdata.shortName+'"',
						btns : 2, 
						type : 4,
	    			    btn : ['确定','取消'],
	    			    yes : function(){
						$.ajax({
							url: '${base}/product/manage/clearproduct.json',
							type:'post',
							timeout:'60000',
							dataType:'json',
							data: {id:rowdata.id,prompt:name,name:rowdata.shortName,c:'${product_cxproduct_clearproduct.code}'}, 
							async:false,
							cache:false,
							success:function(jsonData){
								layer.close(k);
								if(jsonData.status=='success'){
									top.$.layer({
										area : ['auto','auto'],
										dialog : {msg:''+rowdata.shortName+'清算成功',type : 9}	
									});
									jQuery("#productList").jqGrid().trigger("reloadGrid");
								}else{
									top.$.layer({
										area : ['auto','auto'],
										dialog : {msg:'产品清算失败',type : 8}	
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
			}else{
				top.$.layer({
					area : ['auto','auto'],
					dialog : {msg:'产品销售未截止不能被清算哦',type : 8}	
				});
			}
		});
		
	});

	//初始化列表后的操作
	function initAct(cellvalue, options, rowObject){
		var id = rowObject.id;
		var rowdata = rowObject;
		var content = "";
		<!-- 查看 -->
		<c:if test="${!empty product_cxproduct_cxdetail}">
	    <security:authorize ifAnyGranted="${product_cxproduct_cxdetail.code}">
	    	content += "<a href='#' id='" + id + "' class='shortcut_${product_cxproduct_cxdetail.indexCode}' title='${product_cxproduct_cxdetail.name}'>";
	    	content += "<img src='${base}/${skin}/${product_cxproduct_cxdetail.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${product_cxproduct_cxdetail.name}'>${product_cxproduct_cxdetail.name}</font>";
	    	content += "</a>";
	    </security:authorize>
    	</c:if>
		
		<!-- 资料上传 -->
		<c:if test="${!empty product_manage_attachment}">
	    <security:authorize ifAnyGranted="${product_manage_attachment.code}">
	    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_attachment.indexCode}' title='${product_manage_attachment.name}'>";
	    	content += "<img src='${base}/${skin}/${product_manage_attachment.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${product_manage_attachment.name}'>${product_manage_attachment.name}</font>";
	    	content += "</a>";
	    </security:authorize>
    	</c:if>
		
		<!-- 净值 -->
		
		<c:if test="${!empty market_salemanage_saleconfig_natvalue}">
	    <security:authorize ifAnyGranted="${market_salemanage_saleconfig_natvalue.code}">
			if(rowdata.type == 2){
				content += "<a href='#' id='" + id + "' class='shortcut_${market_salemanage_saleconfig_natvalue.indexCode}' title='${market_salemanage_saleconfig_natvalue.name}'>";
				content += "<img src='${base}/${skin}/${market_salemanage_saleconfig_natvalue.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${market_salemanage_saleconfig_natvalue.name}'>${market_salemanage_saleconfig_natvalue.name}</font>";
				content += "</a>";
			}else{
				content += "<img src='${base}/${skin}/${market_salemanage_saleconfig_natvalue.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#999999' title='${market_salemanage_saleconfig_natvalue.name}'>${market_salemanage_saleconfig_natvalue.name}</font>";
			}
	    </security:authorize>
    	</c:if>
		
		<!-- 清算  -->
	    <c:if test="${!empty product_cxproduct_clearproduct}">
   		   <security:authorize ifAnyGranted="${product_cxproduct_clearproduct.code}">
   		    content += "<a href='#' id='" + id + "' class='shortcut_${product_cxproduct_clearproduct.indexCode}' title='${product_cxproduct_clearproduct.name}'>";
   		    content += "<img src='${base}/${skin}/${product_cxproduct_clearproduct.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${product_cxproduct_clearproduct.name}'>${product_cxproduct_clearproduct.name}</font>";
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
							<form:form method="post" action="cxproduct" commandName="product" name="f" id="search_form">
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