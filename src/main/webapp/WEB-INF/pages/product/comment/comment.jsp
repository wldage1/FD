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
	$(document).ready(function(){
		jQuery("#productList").jqGrid({
			url: 'comment/grid.json',
			datatype: 'json',
			colNames: ["<msg:message code='product.shortName'/>",
			           "<msg:message code='product.type'/>",
			           "<msg:message code='provider.shortName'/>",
			           "<msg:message code='product.goodCommentRatio'/>",
			           "<msg:message code='product.badCommentRatio'/>",
			           "<msg:message code='product.sellStatus'/>",
			           "<msg:message code='product.pushStatus'/>",
			           "<msg:message code='info.operate'/>"],
			colModel: [{name:'shortName', index:'p.shortName', align:'center', hidden:false, sortable:false},
			           {name:'type', index:'p.type', align:'center', hidden:false, sortable:false,
							formatter:function(cellvalue, options, rowdata){
			             	   var content="";
			             	   	<c:forEach var="item" items="${productType}">
									   if(cellvalue=='${item.key}'){
										   content="${item.value}";
									   }
								</c:forEach>
								return content;   
						   }
					   },
			           {name:'provider.shortName', index:'p.provider.shortName', align:'center', hidden:false, sortable:false},
			           {name:'goodCommentRatio', index:'p.goodCommentRatio', align:'center', hidden:false, sortable:false},
			           {name:'badCommentRatio', index:'p.badCommentRatio', align:'center', hidden:false, sortable:false},
			           {name:'sellStatus', index:'p.sellStatus', align:'center', hidden:true, sortable:false},
			           {name:'pushStatus', index:'p.pushStatus', align:'center', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   var pushStatus = rowdata.pushStatus;
			        		   var content = "<msg:message code='push.notyet'/>";
			        		   <c:forEach var="item" items="${productPushStatus}">
									if(cellvalue=='${item.key}'){
										content="${item.value}";
									}
							   </c:forEach>
			        		   return content;
			        	   }
			           },
			           {name:'act', index:'i.act', align:'center', sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   var id = rowdata.id;
			        		   var content = "";
			        		   <c:if test="${!empty product_comment_evaluate}">
			        		   <security:authorize ifAnyGranted="${product_comment_evaluate.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${product_comment_evaluate.indexCode}'";
			        		   		content += " title='${product_comment_evaluate.name}'>";
			        		   		content += "<img src='${base}/${skin}/${product_comment_evaluate.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_comment_evaluate.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   if((rowdata.sellStatus==5 || rowdata.sellStatus==6) && rowdata.pushStatus!=1){
				        		   <c:if test="${!empty product_comment_push}">
				        		   <security:authorize ifAnyGranted="${product_comment_push.code}">
				        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${product_comment_push.indexCode}'";
				        		   		content += " title='${product_comment_push.name}'>";
				        		   		content += "<img src='${base}/${skin}/${product_comment_push.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_comment_push.name}";
				        		   		content += "</a>";
				        		   </security:authorize>
				        		   </c:if>
			        		   }
			        		   return content;
			        	   }
			           }],
			mtype: "POST",
			postData: {c:"${productEvaluate.c}",productShortName:"${productEvaluate.productShortName}",productType:"${productEvaluate.productType}"},
			rowNum: 10,
			rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#pagered',
			height: '<msg:message code='jqgrid.height.250'/>',
			autowidth: true,
			viewrecords: true,
			rownumbers: true,
			jsonReader: {
				repeatitems: false
			},
			caption: '<msg:message code="productEvaluate.list"/>',
			toolbar: [true,"top"]
		});
		
		$(".shortcut_${product_comment_push.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var rowdata = jQuery("#productList").jqGrid('getRowData',rowid);
			var productShortName = rowdata.shortName;
			$.ajax({
				url: '${base}${product_comment_push.controller}.json',
				type:'post',
				timeout:'60000',
				dataType:'json',
				data: {productId:rowid, productShortName:productShortName},
				async:false,
				cache:false,
				success:function(jsonData){
					var msg;
					var type;
					if(jsonData.status == "success"){
						msg="<msg:message code='push.success'/>";
						type="1";
					}else if(jsonData.status == "error"){
						msg="<msg:message code='push.error'/>";
						type="3";
					}
					$.layer({
						shade  : false,
					    title  : false,
					    dialog : {msg:msg, type:type},
					    close  : function(index){
					    	layer.close(index);
					    }
					});
					$("#productList").trigger("reloadGrid");
				}
			});
		});
		
		$(".shortcut_${product_comment_evaluate.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var rowdata = jQuery("#productList").jqGrid('getRowData',rowid);
			var goodCommentRatio = escape(rowdata.goodCommentRatio);
			var badCommentRatio = escape(rowdata.badCommentRatio);
			window.location.href = "${base}${product_comment_evaluate.controller}?c=${product_comment_evaluate.code}&productId=" + rowid + "&goodCommentRatio=" + goodCommentRatio + "&badCommentRatio=" + badCommentRatio;
		});
	});
</script>
	</head>
	<body class="skinMain">
		<form:form action="comment" method="post" commandName="productEvaluate">
			<input type="hidden" name="c" value="${productEvaluate.c}"/>
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
				<tr>
					<td align="right" width="10%" class="search_info">
						<msg:message code="product.shortName"/>
					</td>
					<td align="left" width="30%" class="search_lable">
						<form:input path="productShortName" htmlEscape="true"/>
					</td>
					<td align="right" width="10%" class="search_info">
						<msg:message code="product.type"/>
					</td>
					<td align="left" width="30%" class="search_lable">
						<select name="productType" id="productType">
							<option value=""><msg:message code="productType.all"/></option>
							<c:forEach items="${productType}" var="item">
								<c:choose>
									<c:when test="${item.key == productEvaluate.productType}">
										<option value="${item.key}" selected="selected">${item.value}</option>
									</c:when>
									<c:otherwise>
										<option value="${item.key}">${item.value}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</td>
					<td width="20%" class="button_area">
						<input type="submit" class="btn2" id="select" value="<msg:message code='button.search' />"/>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td>
						<table id="productList"><tr><td>&nbsp;</td></tr></table>
						<div id="pagered"></div>
					</td>
				</tr>
			</table>
		</form:form>
	</body>
</html>