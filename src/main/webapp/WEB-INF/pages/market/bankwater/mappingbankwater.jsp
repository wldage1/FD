<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" type="text/css" href="${base}/common/skin/css/productTab.css" />
<style type="text/css">
.altclassTwo{
	color: blue;
	background-color: gray;
	background-image: none;
}
</style>
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<script type="text/javascript">
	var iframeID='${iframeID}';
	//流水列表
	$(document).ready(function() {
		 jQuery("#productListTwo").jqGrid({
			 	scroll: true,
		        url: '${base}/market/bankwater/mappingbankwaterGrid.json',
		        datatype: 'json',
		        colNames: [
		                "<msg:message code='order.product.shortName'/>",
						"<msg:message code='order.clientName'/>",
						"<msg:message code='bankwater.payamount'/>",
						"<msg:message code='bankwater.paytime'/>"
		       	        ],
		               colModel: [
						{name: 'productShortName',index: 'productShortName',width:'10%',align:'center',hidden: false,sortable:false},
						{name: 'name',index: 'name',width:'15%',align:'center',hidden: false,sortable:false},
						{name: 'payAmount',index: 'payAmount',width:'10%',align:'center',hidden: false,sortable: false},
						{name: 'payTime',index: 'payTime',width:'15%',align:'center',hidden: false,sortable: false}
		 				],
		        mtype:"POST",
		        postData:{"productId":${payConfirmFromProvider.productId}},
		        rowNum:15,     
		        page:"${payConfirmFromProvider.page}",
		        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
		        pager: '#pageredTwo',
		        height:	'auto',
		        height: 260,
		       	width: 570,
		        viewrecords: true,
		        multiselect: false,
		        altRows:true,
		        altclass:'altclassTwo',
		        jsonReader: {
		        	repeatitems: false
		        },
		        caption:'<msg:message code="bankwater.list"/>',
		        toolbar: [true,"top"]
		    });
		 //凭证列表
		 jQuery("#productListOne").jqGrid({
			 	scroll: true,
		        url: '${base}/market/bankwater/mappingbankwaterGrid.json',
		        datatype: 'json',
		        colNames: [
						"<msg:message code='order.product.shortName'/>",
						"<msg:message code='order.clientName'/>",
						"<msg:message code='order.proof.amount'/>",
						"<msg:message code='order.proof.time'/>",
						"<msg:message code='order.proof.files'/>"
		       	        ],
		               colModel: [
						{name: 'productShortName',index: 'productShortName',width:'10%',align:'center',hidden: false,sortable:false},
						{name: 'name',index: 'name',width:'15%',align:'center',hidden: false,sortable:false},
						{name: 'payAmount',index: 'payAmount',width:'10%',align:'center',hidden: false,sortable: false},
						{name: 'proofTime',index: 'proofTime',width:'15%',align:'center',hidden: false,sortable: false},
						{name: 'proofFileUrl',index: 'proofFileUrl',width:'15%',align:'center',hidden: false,sortable: false,formatter:fileChange}
		 				],
		        mtype:"POST",
		        postData:{"productId":${payConfirmFromProvider.productId}},
		        rowNum:15,     
		        page:"${payConfirmFromProvider.page}",
		        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
		        pager: '#pageredOne',
		        height:	'auto',
		        height: 260,
		       	width: 570,
		        viewrecords: true,
		        multiselect: false,
		        altRows:true,
		        altclass:'altclassTwo',
		        jsonReader: {
		        	repeatitems: false
		        },
		        caption:'<msg:message code="order.proof.list"/>',
		        toolbar: [true,"top"]
		    });
		 
		 
		 //匹配按钮事件
		 $("#mappingID").click(function(){
			var rowID=jQuery("#productListTwo").getDataIDs();
			if(rowID.length==0){
				top.$.layer({
					area : ['auto','auto'],
					dialog : {msg: "<msg:message code='bankwater.hasnt.data'/>" ,type : 8}	
				});
				return false;
			}
			 $.ajax({
					url: '${base}/market/bankwater/mappingbankwaterUp.json',
					type:'post',
					timeout:'60000',
					dataType:'json',
					data: {"productId":${payConfirmFromProvider.productId}}, 
					async:false,
					cache:false,
					success:function(jsonData){
						if(jsonData.isTrue){
							top.document.getElementById(iframeID).contentWindow.reloadGridTwo("productList");
							top.$.layer({
								area : ['auto','auto'],
								dialog : {msg: "<msg:message code='match.success' />" ,type : 9}	
							});
							jQuery("#productList").jqGrid().trigger("reloadGrid");
						}else{
							top.document.getElementById(iframeID).contentWindow.reloadGridTwo("productList");
							top.$.layer({
								area : ['auto','auto'],
								dialog : {msg: "<msg:message code='match.fail' />" ,type : 8}	
							});
						}
					}
				});
		 });
	});
	
	
	//文件名初始化
	function fileChange(cellvalue, options, rowObject){
		var str="";
		if(cellvalue!=null && cellvalue!=''){
			str="<a href='javascript:void(0);' style='color:blue;' onclick='changeBig(\"${ftpHttpUrl}"+cellvalue+"\");'>";
			str += cellvalue.substring(cellvalue.lastIndexOf("/")+1,cellvalue.length);
			str += "</a>";
		}else{
			str="无";
		}
		return str;
	}
	
	function closeWin(){
	 	top.document.getElementById(iframeID).contentWindow.closeWin();
	}
	
	function changeBig(ImageUrl){
		var i = top.$.layer({
			type: 1,
			title: false,
			closeBtn: [0,true],
			shadeClose: true,
			border : [5, 0.5, '#666', true],
			offset: ['20px','50%'],
			move: ['.ttee', true],
			moveOut:true, 
			area: ['auto','auto'],
			page: {
				html: '<img width="950" class="ttee" height="500" src="'+ImageUrl+'""/>'
			}
		});
	}
</script>

</head>
<body class="skinMain">
<form id="orderAttachment" name="orderAttachment" method="post">
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td>
			<table id="productListTwo"><tr><td>&nbsp;</td></tr></table>
			<div id="pageredTwo"></div>
		</td>
		<td>
			<table id="productListOne"><tr><td>&nbsp;</td></tr></table>
			<div id="pageredOne"></div>
		</td>
	</tr>
	<tr height="40"><td>&nbsp;</td></tr>
</table>
</form>
<div class="toolbar">
	<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" value="<msg:message code='bankwater.mappiing' />" id="mappingID"/>
			<input type="button" class="btn2" onclick="closeWin();" name="btnback" value="<msg:message code="order.fail.button"/>" id="btnback"/>
		</div>
	<div class="right"></div>
</div>
</body>
</html>
