<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" type="text/css" href="${base}/common/skin/css/productTab.css" />
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<script type="text/javascript">
	var iframeID='${iframeID}';
	$(document).ready(function() {
		 jQuery("#productListTwo").jqGrid({
			 	scroll: true,
		        url: '${base}/market/bankwater/orderProofGrid.json',
		        datatype: 'json',
		        colNames: ["",
						"<msg:message code='order.clientName'/>",
						"<msg:message code='order.iDCard'/>",
						"<msg:message code='order.proof.amount'/>",
						"<msg:message code='order.proof.proofTime'/>",
						"<msg:message code='order.proof.serialNumber'/>",
		                "<msg:message code='order.orderNumber'/>",
		       	        "<msg:message code='order.member.name'/>", 
		      	    	"<msg:message code='order.proof.files'/>"
		       	        ],
		               colModel: [
						{name: 'id',index: 'id',width:'1%',align:'center',hidden: true,sortable:false},
						{name: 'order.clientName',index: 'order.clientName',width:'10%',align:'center',hidden: false,sortable:false},
						{name: 'order.iDCard',index: 'order.iDCard',width:'15%',align:'center',hidden: false,sortable:false},
						{name: 'affirmProofAmount',index: 'affirmProofAmount',width:'10%',align:'center',hidden: false,sortable: false},
						{name: 'affirmProofTime',index: 'affirmProofTime',width:'15%',align:'center',hidden: false,sortable: false},
						{name: 'serialNumber',index: 'serialNumber',width:'15%',align:'center',hidden: false,sortable: false}, 
		                {name: 'order.orderNumber',index: 'orderNumber',width:'15%',align:'center',hidden: false,sortable: false}, 
		                {name: 'order.member.name',index: 'order.member.name',width:'10%',align:'center',hidden: false,sortable: false},
		       			{name: 'proofFileUrl',index: 'proofFileUrl',width:'10%',align:'center',hidden: false,sortable: false,formatter:fileChange}
		 				],
		        mtype:"POST",
		        postData:{"productID":${payConfirmFromProvider.productId}},
		        rowNum:10,     
		        page:"${orderProof.page}",
		        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
		        pager: '#pageredTwo',
		        height:	'auto',
		        height: 210,
		       	width: 600,
		        autowidth: true,
		        viewrecords: true,
		        multiselect: true,
		        jsonReader: {
		        	repeatitems: false
		        },
		        gridComplete:function(){
		        	$("#cb_productListTwo").attr("disabled","disabled");
		        },
		        onSelectRow:function(rowid,status){
		        	var ids=jQuery("#productListTwo").getGridParam("selarrrow");
		        	if(ids.length!=0){
		        		$.each(ids,function(index,item){
			        		if(item!=rowid){
			        			jQuery("#productListTwo").setSelection(item);
			        		}
			        	});		        		
		        	}
		        },
		        caption:'<msg:message code="order.proof.list"/>',
		        toolbar: [true,"top"]
		    });
		 //匹配按钮事件
		 $("#mappingID").click(function(){
			 var rowID=jQuery("#productListTwo").getGridParam("selarrrow");
			if(rowID.length==0 || rowID.length!=1){
				jQuery("#t_productListTwo").empty().append("&nbsp;&nbsp;<font color='red'><msg:message code='please.chose.prooffile'/></font>");
				return false;
			}
			 var proofID=jQuery("#productListTwo").getRowData(rowID).id;
			 $.ajax({
					url: '${base}/market/bankwater/mappingbankwaterbymanual.json',
					type:'post',
					timeout:'60000',
					dataType:'json',
					data: {"id":${payConfirmFromProvider.id},"orderAcountAffirmdetailID":proofID}, 
					async:false,
					cache:false,
					success:function(jsonData){
						if(jsonData.isTrue){
							top.document.getElementById(iframeID).contentWindow.reloadGridTwo("productList_hasOrNotDispose");
						}else{
							top.$.layer({
								area : ['auto','auto'],
								dialog : {msg: "<msg:message code="match.fail"/>" ,type : 8}	
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
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="100%" class="skinLayout_info"><font><b><msg:message code='bankwater.info'/></b></font>
						<table border="0" cellspacing="1" cellpadding="0" width="100%" class="skinLayout">
							<tr>
								<td align="right" width="20%"><msg:message code='order.clientName' /><msg:message code='system.common.sign.colon'/></td>
								<td width="30%">${payConfirmFromProvider.name}</td>
								<td align="right" width="20%"><msg:message code='payAmount.number' /><msg:message code='system.common.sign.colon'/></td>
								<td width="30%">${payConfirmFromProvider.payAmount}</td>
							</tr>
							<tr>
								<td align="right"><msg:message code='product.shortName' /><msg:message code='system.common.sign.colon'/></td>
								<td>${payConfirmFromProvider.productShortName}</td>
								<td align="right"><msg:message code='order.proof.proofTime' /><msg:message code='system.common.sign.colon'/></td>
								<td>${payConfirmFromProvider.payTime}</td>
							</tr>
							<tr>
								<td align="right"><msg:message code='order.provider.name'/><msg:message code='system.common.sign.colon'/></td>
								<td colspan="3">${payConfirmFromProvider.providerShortName}</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="productListTwo"><tr><td>&nbsp;</td></tr></table>
			<div id="pageredTwo"></div>
		</td>
	</tr>
	<tr height="40"><td>&nbsp;</td></tr>
</table>
</form>
<div class="toolbar">
	<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" value="匹配" id="mappingID"/>
			<input type="button" class="btn2" onclick="closeWin();" name="btnback" value="<msg:message code="order.fail.button"/>" id="btnback"/>
		</div>
	<div class="right"></div>
</div>
</body>
</html>
