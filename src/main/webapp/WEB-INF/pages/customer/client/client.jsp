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
	$("#clientList").setGridWidth($(window).width()*0.999);　
});
	$(document).ready(function(){
		jQuery("#clientList").jqGrid({
			url: 'client/grid.json',
			datatype: 'json',
			colNames: ["<msg:message code='client.name'/>",
			           "<msg:message code='client.memberID'/>",
			           "<msg:message code='client.memberName'/>",
			           "<msg:message code='client.type'/>",
			           "<msg:message code='client.idCardType'/>",
			           "<msg:message code='client.idCardType'/>",
			           "<msg:message code='client.idCard'/>",
			           "<msg:message code='client.status'/>",
			           "<msg:message code='info.operate'/>"],
			colModel: [{name:'name', index:'c.name', align:'center', hidden:false, sortable:false},
			           {name:'memberId', index:'c.memberId', align:'center', hidden:true, sortable:false},
			           {name:'memberName', index:'c.memberName', align:'center', hidden:false, sortable:false,
							formatter:function(cellvalue, options, rowdata){
							   var content="&nbsp;";
		                	   if (rowdata.memberName!=null&&rowdata.memberNickName!=null){
		                	   	  content= rowdata.memberName+"("+rowdata.memberNickName+")";
		                	   }
							   return content;
							}
			           },
			           {name:'type', index:'c.type', align:'center', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
		                	   var content="";
		                	   	<c:forEach var="item" items="${clientType}">
									   if(cellvalue==${item.key}){
										   content="${item.value}";
									   }
								</c:forEach>
								return content;   
						   }
			           },
			           {name:'idCardType', index:'c.idCardType', align:'center', hidden:true, sortable:false},
			           {name:'idCardTypeName', index:'c.idCardTypeName', align:'center', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
		                	   var content="";
		                	   	<c:forEach var="item" items="${idCardTypeMap}">
									   if(rowdata.idCardType==${item.key}){
										   content="${item.value}";
									   }
								</c:forEach>
								return content;   
						   }
			           },
			           {name:'idCard', index:'c.idCard', align:'center', hidden:false, sortable:false},
			           {name:'status', index:'c.status', align:'center', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
		                	   var content="";
		                	   	<c:forEach var="item" items="${clientStatus}">
									   if(cellvalue==${item.key}){
										   content="${item.value}";
									   }
								</c:forEach>
								return content;   
						   }
			           },
			           {name:'act', index:'c.act', align:'center', sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   var id = rowdata.id;
			        		   var content = "";
			        		   <c:if test="${!empty customer_client_detail}">
			        		   <security:authorize ifAnyGranted="${customer_client_detail.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_client_detail.indexCode}'";
			        		   		content += " title='${customer_client_detail.name}'>";
			        		   		content += "<img src='${base}/${skin}/${customer_client_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${customer_client_detail.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty customer_client_product}">
			        		   <security:authorize ifAnyGranted="${customer_client_product.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_client_product.indexCode}'";
			        		   		content += " title='${customer_client_product.name}'>";
			        		   		content += "<img src='${base}/${skin}/${customer_client_product.icon}' weight='18' height='18' border='0' align='absmiddle'/>${customer_client_product.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   return content;
			        	   }   
			        	}],
			mtype: "POST",
			postData: {c:"${client.c}", memberName:"${client.memberName}", name: "${client.name}"},
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
			caption: '<msg:message code="client.list"/>'
		});
		
		$(".shortcut_${customer_client_detail.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			window.location.href = "${base}${customer_client_detail.controller}?c=${customer_client_detail.code}&id=" + rowid;
		});
		
		$(".shortcut_${customer_client_product.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var rowdata = jQuery("#clientList").jqGrid('getRowData',rowid);
			window.location.href = "${base}${customer_client_product.controller}?c=${customer_client_product.code}&name=" + rowdata.name + "&memberId=" + rowdata.memberId + "&idCardType=" + rowdata.idCardType + "&idCard=" + rowdata.idCard;
		});
		
	});
</script>
	</head>
	<body class="skinMain">
		<form:form action="client" method="post" commandName="client">
			<input type="hidden" name="c" value="${client.c}" />
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="skinMain">
				<tr>
					<td align="center">
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
							<td align="right" width="10%" class="search_info">
								<msg:message code="client.memberName"/>
							</td>
							<td align="left" width="30%" class="search_lable">
								<form:input path="memberName" htmlEscape="true"/>
							</td>
							<td align="right" width="10%" class="search_info">
								<msg:message code="client.name"/>
							</td>
							<td align="left" width="30%" class="search_lable">
								<form:input path="name" htmlEscape="true"/>
							</td>
							<td width="20%" class="button_area">
								<input type="submit" class="btn2" id="select" value="<msg:message code='button.search' />"/>
							</td>
						</table>
					</td>
				</tr>
				<tr>
					<td >
						<table id="clientList"><tr><td>&nbsp;</td></tr></table>
						<div id="pagered"></div>
					</td>
				</tr>
			</table>
		</form:form>
	</body>
</html>