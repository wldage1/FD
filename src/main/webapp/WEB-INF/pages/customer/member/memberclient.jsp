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
		jQuery("#clientList").jqGrid({
			url: '${base}/customer/client/grid.json',
			datatype: 'json',
			colNames: ["<msg:message code='client.name'/>",
			           "<msg:message code='client.memberName'/>",
			           "<msg:message code='client.type'/>",
			           "<msg:message code='client.status'/>",
			           "<msg:message code='info.operate'/>"],
			colModel: [{name:'name', index:'c.name', align:'center', hidden:false, sortable:false},
			           {name:'memberName', index:'c.memberName', align:'center', hidden:false, sortable:false,
	                	  formatter:function(cellvalue, options, rowObject){
	                	   var content="&nbsp;";
	                	   if (rowObject.memberName!=null&&rowObject.memberNickName!=null){
	                	   	  content= rowObject.memberName+"("+rowObject.memberNickName+")";
	                	   }
						   return content;    
						  }
					},
			           {name:'type', index:'c.type', align:'center', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowObject){
						   var content="";
	                	   	<c:forEach var="item" items="${clientType}">
								   if(cellvalue==${item.key}){
									   content="${item.value}";
								   } 
							</c:forEach>
							return content; 
					 }
			        },
			           {name:'status', index:'c.status', align:'center', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowObject){
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
			        		   <c:if test="${!empty customer_member_memberclient_clientdetail}">
			        		   <security:authorize ifAnyGranted="${customer_member_memberclient_clientdetail.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_member_memberclient_clientdetail.indexCode}'";
			        		   		content += " title='${customer_member_memberclient_clientdetail.name}'>";
			        		   		content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_memberclient_clientdetail.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   return content;
			        	   }   
			        	}],
			mtype: "POST",
			postData: {c:"${member.c}", memberId:"${member.id}"},
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
		
		$(".shortcut_${customer_member_memberclient_clientdetail.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			window.location.href = "${base}${customer_member_memberclient_clientdetail.controller}?c=${customer_member_memberclient_clientdetail.code}&id=" + rowid;
		});
		
	});
</script>
	</head>
	<body class="skinMain">
			<input type="hidden" name="c" value="${member.c}" />
			<input type="hidden" name="turnCode" value="${customer_member.code}"/>
			<input type="hidden" name="coverParam" value="1"/>
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="skinMain">
				<tr>
					<td >
						<table id="clientList"><tr><td>&nbsp;</td></tr></table>
						<div id="pagered"></div>
					</td>
				</tr>
				<div class="toolbar">
				<div class="left"></div>
					<div class="center">
					<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
					</div>
					<div class="right"></div>
				</div>
				<tr>
					<td></td>
				</tr>
			</table>
	</body>
</html>