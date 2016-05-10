<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${base}/common/js/layer/layer.js"></script>
<link href="${base}/${skin}/css/layer.css" rel="stylesheet" type="text/css"/>
<title></title>
<script type="text/javascript">
var iframeID = window.frameElement.id;
var parentIframeID='${iframeID}';
$(document).ready(function(){
    jQuery("#historyList").jqGrid({
        treeGridModel: 'adjacency',
        url: '${base}/market/order/historyGrid.json',
        datatype: 'json',
        colNames: [
                   '',
                  '<msg:message code="order.clientName"/>',
                   '<msg:message code="product.name" />',
                   '<msg:message code="order.contractNumber"/>', 
                   '<msg:message code="info.operate"/>'
                   ],
        colModel: [{name: 'id',index: 'id',width:'10%',align:'center',hidden: true,sortable: false},
                   {name: 'clientName',index: 'clientName',width:'20%',align:'center',hidden: false,sortable: false}, 
                   {name: 'product.name',index: 'product.name',width:'20%',align:'center',hidden: false,sortable: false}, 
                   {name: 'contractNumber',index: 'contractNumber',width:'10%',align:'center',hidden: false,sortable: false}, 
                   {name:'act',index:'act', width:'10%',align:'left',sortable:false,
                	   formatter:function(cellvalue, options, rowObject){
                    	   var content="<a href='javascript:void(0);' onclick=\"selectHistory('"+rowObject.clientName+"','"+rowObject.contractNumber+"');\" class='memberSelect'><font color='#0000FF' title='${product_manage_detail.name}'><msg:message code='select.button'/></fond></a>";
    						return content;    
   					   }
                  	}
                  ],
       	mtype: "POST",
		postData:{c:"${order.c}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,   
        page:"${order.page}",
        rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:	200,
        autowidth: true,
        viewrecords: true,
   //     multiselect: true,
        rownumbers: false,
        jsonReader: {
        	repeatitems: false
        },
    	caption:'<msg:message code="order.history.list"/>',
        toolbar: [true,"top"]
    });
	jQuery("#historyList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	
});

function selectList(){
	jQuery("#historyList").jqGrid("setGridParam",{postData:{c:"${order.c}",clientName:$('#clientName').val(),productName:$("#productName").val()}});
	$("#historyList").trigger("reloadGrid"); 
}

function selectHistory(cName,cNum){
    top.document.getElementById(parentIframeID).contentWindow.getHistory(cName,cNum);
    top.document.getElementById(parentIframeID).contentWindow.closeTopLayer();
}
</script>
</head>

<body class="skinMain">
<form:form method="post" action="historyGrid" commandName="order">
<input type="hidden" name="c" value="${order.c}" />
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td align="center">
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
				<tr>
					<td align="right" width="20%" class="search_info">
						<msg:message code='order.clientName'/><msg:message code="system.common.sign.colon"/>
					</td>
					<td align="left" width="20%" class="search_lable">
						<input id="clientName"/>
					</td>
					<td align="right" width="20%" class="search_info">
						<msg:message code="product.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td align="left" width="20%" class="search_lable">
						<input id="productName"/>
					</td>
					<td rowspan="2" width="20%" class="button_area">
						<input type="button" onclick="selectList()" class="btn2" id="select" value="<msg:message code='button.search'/>">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="historyList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</form:form>
</body>
</html>