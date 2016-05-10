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
<body onload = "BodyOnLoad();">
<script type="text/javascript">
$(document).ready(function(){
	BodyOnLoad();
	var settings = {
	        output:'css',
	        bgColor: '#FFFFFF',
	        color: '#000000',
	        barWidth: 2,
	        barHeight: 30
	      };
	$("#barcodeTarget").html("").show().barcode('${document.orderNumber}', 'code39',settings);
})

function showOrHide(){
	$("#toolbar").remove();
	window.print();
}
function BodyOnLoad() 
{ 
var textarea= document.getElementById('textarea'); 
textarea.style.posHeight=textarea.scrollHeight; 
var textarea1= document.getElementById('textarea1'); 
textarea1.style.posHeight=textarea1.scrollHeight; 
} 
</script>
<div class="toolbar" id="toolbar">
	<div class="left"></div>
		<div class="center">
			<input type="button" id="print" value="打印"  class="btn2" onclick="showOrHide();" />
		</div>
	<div class="right"></div>
</div>
	<table align="center" id="printContent" height="850" style="page-break-after:always;" border="0">
	<tr style="height:200px;">
		<td colspan="3"><h1 align="center">理  财  产  品  投  资  合  同  书</h1></td>
	</tr>
	<tr>
		<td colspan="3">
			<br/>
			<br/>
			<br/>
			<br/>
		</td>
	</tr>
	<tr>
	<td>
		<table border="0">
			<tr>
				<td><h3 align="left">收 件 人 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" value="${document.name }"/> </h3></td>
			</tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>
			<tr>
				<td><h3 align="left">收 件 人 地 址 :  <textarea  id="textarea" disabled="disabled" style='width:150px;border:2px solid rgb(237,237,237); border-top:none;border-left:none; border-right:none;overflow-y:visible;overflow:hidden; resize:none;'>${document.memAddress }</textarea> </h3></td>
			</tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>
			<tr>
				<td><h3 align="left">手 机 号 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" value="${document.phone }"/> </h3></td>
			</tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>
			<tr>
				<td><h3 align="left">固 定 号 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" value="${document.telphone }"/> </h3></td>
			</tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>
			<tr>
				<td><h3 align="left">邮 政 编 码 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" value="${document.memPostcode }"/> </h3></td>
			</tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>
			<tr>
				<td><h3 align="left">理 财 师:  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" value="${document.memberName }"/> </h3></td>
			</tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>
			<tr>
				<td><h3 align="left">移 动 号 码 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" value="${document.mobilePhone }"/> </h3></td>
			</tr>
			<tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>
			<tr>
				<td><h3 align="left">投 资 人 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" value="${document.clientName }"/> </h3></td>
			</tr>
		<tr>
			<td>
				<br/>
				<br/>
			</td>
		</tr>
		<tr>
			<td><h3 align="left">合 同 编 号 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" value="${document.contractNumber }"/> </h3></td>
		</tr>
		<tr>
			<td>
				<br/>
				<br/>
			</td>
		</tr>
		<tr>
			<td><h3 align="left">产 品 名 称 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;"  value="${document.productShortName }"/> </h3></td>
		</tr>
		<tr>
			<td>
				<br/>
				<br/>
			</td>
		</tr>
		<tr>
			<td><h3 align="left">发 行 机 构 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px;border-top:none;border-left:none; border-right:none;" value="${document.providerShortName }"/> </h3></td>
		</tr>
		<tr>
			<td>
				<br/>
				<br/>
			</td>
		</tr>
		</table>
	</td>
	<td>
	<div style="height:600px; width:70px;float:left; border-right:1px #000 solid"></div>
	<div style="height:600px; width:70px;float:right; border-left:1px #000 solid"></div>
	</td>
	<td>
		<table>
			<tr>
				<td><h3 align="left">回 寄 联 系 人 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" value="${document.contact }"/> </h3></td>
			</tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>
			<tr>
				<td><h3 align="left">回 寄 邮 编 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 0px; border-top:none;border-left:none; border-right:none;"  value="${document.postcode }"/> </h3></td>
			</tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>
			<tr>
				<!-- <td><h3 align="left">回 寄 地 址 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;"  value="${document.address }"/> </h3></td>-->
			    <td><h3 align="left">回 寄 地 址 :  <textarea  id="textarea1" disabled="disabled" style='width:150px;border:2px solid rgb(237,237,237); border-top:none;border-left:none; border-right:none;overflow-y:visible;overflow:hidden; resize:none;'>${document.address }</textarea></h3></td>
			</tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>
			<tr>
				<td><h3 align="left">手 机 :  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" value="${document.cellphone }"/> </h3></td>
			</tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>
			<tr>
				<td><h3 align="left">固 定 电 话:  <input type="text" disabled="disabled" style="width:150px;border:0 solid 1px; border-top:none;border-left:none; border-right:none;" value="${document.telephone }"/> </h3></td>
			</tr>
			<tr>
				<td>
					<br/>
					<br/>
				</td>
			</tr>	
		</table>
	</td>
	</tr>
	<tr>
		<td colspan="3" align="center"><div id="barcodeTarget" ></div></td>
	</tr>
	</table>
</body>
</html>