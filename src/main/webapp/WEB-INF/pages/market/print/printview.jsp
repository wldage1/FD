<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>打印预览</title>
<style type="text/css">
    .ab{ width:150px;border:0 solid 1px; border-top:none; border-left:none; border-right:none;}
</style>
<script src="${base}/common/js/print/jquery-barcode.min.js" type="text/javascript"></script>
<link  href="${base}/${skin}/css/tab.css" rel="stylesheet" type="text/css" />
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	var settings = {
	        output:'css',
	        bgColor: '#FFFFFF',
	        color: '#000000',
	        barWidth: 2,
	        barHeight: 30
	      };
	<c:forEach items="${orders}" var="order"  varStatus="vs">
	$("#barcodeTarget_${vs.index}").html("").show().barcode('${order.orderNumber}', 'code39',settings);
	</c:forEach>
	
})

function showOrHide(){
	$("#toolbar").remove();
	window.print();
}
</script>
<div class="toolbar" id="toolbar">
	<div class="left"></div>
		<div class="center">
			<input type="button" id="print" value="打印"  class="btn2" onclick="showOrHide();" />
		</div>
	<div class="right"></div>
</div>
<c:forEach items="${orders}" var="order" varStatus="vs">
	<table align="center" id="printContent" height="850" style="page-break-after:always;">
		<tr style="height:200px;">
			<td><h1 align="center">理  财  产  品  投  资  合  同  书</h1></td>
		</tr>
		<tr>
			<td>
				<br/>
				<br/>
				<br/>
				<br/>
				<br/>
				<br/>
				<br/>
				<br/>
				<br/>
				<br/>
			</td>
		</tr>
		<tr>
			<td><h3 align="center">产 品 名 称 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" id="productShortName" value="${order.productShortName }"/> </h3></td>
		</tr>
		<tr>
			<td>
				<br/>
				<br/>
			</td>
		</tr>
		<tr>
			<td><h3 align="center">发 行 机 构 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" id="providerShortName" value="${order.providerShortName }"/> </h3></td>
		</tr>
		<tr>
			<td>
				<br/>
				<br/>
			</td>
		</tr>
		<tr>
			<td><h3 align="center">投 资 金 额 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" id="investAmount" value="${order.investAmount}   万元人民币"/> </h3></td>
		</tr>
		<tr>
			<td>
				<br/>
				<br/>
			</td>
		</tr>
		<tr>
			<td><h3 align="center">客 户 姓 名 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" /> </h3></td>
		</tr>
		<tr>
			<td>
				<br/>
				<br/>
			</td>
		</tr>
		<tr>
			<td><h3 align="center">合 同 编 号 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;"/> </h3></td>
		</tr>
		<tr>
			<td>
				<br/>
				<br/>
			</td>
		</tr>
		<tr>
			<td ><div id="barcodeTarget_${vs.index}" ></div></td>
		</tr>
	</table>
</c:forEach>
</body>
</html>