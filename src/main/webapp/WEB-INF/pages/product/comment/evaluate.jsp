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
		$("#gcr").text(unescape("${productEvaluate.goodCommentRatio}"));
		$("#bcr").text(unescape("${productEvaluate.badCommentRatio}"));
		jQuery("#evaluateList").jqGrid({
			url: 'evaluate/grid.json',
			datatype: 'json',
			colNames: ["<msg:message code='product.shortName'/>",
			           "<msg:message code='product.evaluate'/>",
			           "<msg:message code='member.name'/>",
			           "<msg:message code='evaluate.description'/>"],
			colModel: [{name:'productShortName', index:'p.productShortName', align:'center', hidden:false, sortable:false},
			           {name:'evaluate', index:'p.evaluate', align:'center', hidden:false, sortable:false,
							formatter:function(cellvalue, options, rowdata){
			             	   var content="";
			             	   	<c:forEach var="item" items="${productEvaluateQuota}">
									   if(cellvalue=='${item.key}'){
										   content="${item.value}";
									   }
								</c:forEach>
								return content;   
						   }
					   },
			           {name:'memberName', index:'p.memberName', align:'center', hidden:false, sortable:false,
							formatter:function(cellvalue, options, rowdata){
								return rowdata.memberName + "（" + rowdata.memberNickName + "）";
						   }
			           },
			           {name:'description', index:'p.description', align:'center', hidden:false, sortable:false}],
			mtype: "POST",
			postData: {c:"${productEvaluate.c}",productId:"${productEvaluate.productId}"},
			rowNum: 10,
			rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#pagered',
			height: '<msg:message code='jqgrid.height.350'/>',
			autowidth: true,
			viewrecords: true,
			jsonReader: {
				repeatitems: false
			},
			caption: '<msg:message code="productEvaluateDetail.list"/>',
			toolbar: [true,"bottom"]
		});
	});
</script>
	</head>
	<body class="skinMain">
		<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
			<tr height="25">
				<td align="right" width="8%" class="search_info">
					<msg:message code="product.goodCommentRatio"/>
				</td>
				<td id="gcr" align="left" width="12%" class="search_lable"></td>
				<td align="right" width="8%" class="search_info">
					<msg:message code="product.badCommentRatio"/>
				</td>
				<td id="bcr" align="left" width="12%" class="search_lable"></td>
				<td width="60%" class="search_lable"></td>
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td>
					<table id="evaluateList"><tr><td>&nbsp;</td></tr></table>
					<div id="pagered"></div>
				</td>
			</tr>
		</table>
		<div class="toolbar">
		<div class="left"></div>
			<div class="center">
				<input type="button" class="btn2" onclick="history.go(-1);" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
			</div>
		<div class="right"></div>
	</div>
	</body>
</html>