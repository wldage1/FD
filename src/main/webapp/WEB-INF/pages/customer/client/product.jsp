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
		jQuery("#productList").jqGrid({
			url: 'product/grid.json',
			datatype: 'json',
			colNames: ["<msg:message code='client.contractNumber'/>",
			           "<msg:message code='client.productShortName'/>",
			           "<msg:message code='client.share'/>",
			           "<msg:message code='client.investAmount'/>"],
			colModel: [{name:'contractNumber', index:'c.contractNumber', align:'center', hidden:false, sortable:false},
			           {name:'product.shortName', index:'c.product.shortName', align:'center', hidden:false, sortable:false},
			           {name:'share', index:'c.share', align:'center', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   if(rowdata.product.type == 2){
									return rowdata.share + "<msg:message code='holdingProduct.share.share'/>";
			        		   }else{
									return rowdata.share/1000000 + "<msg:message code='holdingProduct.share.money'/>";
			        		   }
							}
			           },
			           {name:'investAmount', index:'c.investAmount', align:'center', hidden:false, sortable:false}],
			mtype: "POST",
			postData: {c:"${client.c}", memberId:"${client.memberId}", idCardType:"${client.idCardType}", idCard: "${client.idCard}"},
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
			caption: '${client.name}<msg:message code="holdingProduct.list"/>',
			toolbar: [true,"bottom"]
		});
		var $back = $("<a></a>").attr("href","javascript:void();")
		  .attr("id","back")
		  .append($("<img/>").attr("src","${base}/${skin}/images/icon/restore.png")
		  .attr("width","18").attr("height","18").attr("border","0")
		  .attr("border","0").attr("align","absmiddle"))
		  .append("<msg:message code="button.back"/>");    
		/*$("#t_productList").append("&nbsp;&nbsp;").append(
			$("<span></span>")
			.attr("class","jqgridContainer")
			.append($back)
		);
		$("#t_productList #back").click(function(){
			window.location.href="window.location.href='${base}${backLink}'";
		});
		*/
	});
</script>
	</head>
	<body class="skinMain">
	<input type="hidden" name="turnCode" value="${customer_client.code}"/>
			<input type="hidden" name="coverParam" value="1"/>
		<table width="100%" border="0" cellpadding="0" cellspacing="1" class="skinMain">
			<tr>
				<td >
					<table id="productList"><tr><td>&nbsp;</td></tr></table>
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