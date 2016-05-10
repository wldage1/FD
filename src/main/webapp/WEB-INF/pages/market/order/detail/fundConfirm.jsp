<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
<link  href="${base}/${skin}/css/tab.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
$(document).ready(function(){
    jQuery("#productList").jqGrid({
        url: '${base}/market/order/detail/fundConfirmGrid.json',
        datatype: 'json',
        colNames: [
                "<msg:message code='order.orderNumber'/>",
       	        "<msg:message code='order.contractNumber'/>",
       	        "<msg:message code='order.member.name'/>", 
       	  	    "<msg:message code='order.orgID.shortName'/>",
       	        "<msg:message code='order.clientName'/>",
       	        "<msg:message code='order.iDCard'/>",
       	        "<msg:message code='order.orderAmount'/>",
       	        "<msg:message code='order.amountDistribute'/>",
      	   		"<msg:message code='order.payDataTime'/>",
       	        "<msg:message code='order.payAmount'/>",
       	        "<msg:message code='order.proof.proofTime'/>",
       	        "<msg:message code='order.proof.amount'/>",
      	    	"<msg:message code='order.proof.files'/>",
      	    	"<msg:message code='order.proof.status'/>",
       	        "<msg:message code='order.operate'/>"
       	        ],
               colModel: [
                {name: 'order.orderNumber',index: 'orderNumber',width:'10%',align:'center',hidden: false,sortable: false}, 
                {name: 'order.contractNumber',index: 'order.contractNumber',width:'8%',align:'center',hidden: false,sortable: false},
                {name: 'order.member.name',index: 'order.member.name',width:'10%',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowObject){
                	return cellvalue+"("+rowObject.order.member.nickName+")";
                }},
                {name: 'order.organization.shortName',index: 'order.organization.shortName',width:'10%',align:'center',hidden: false,sortable: false},
       			{name: 'order.clientName',index: 'order.clientName',width:'7%',align:'center',hidden: false,sortable:false},
       			{name: 'order.iDCard',index: 'order.iDCard',width:'7%',align:'center',hidden: false,sortable:false},
       			{name: 'order.investAmount',index: 'order.investAmount',width:'10%',align:'center',hidden: false,sortable:false},
       			{name: 'order.share',index: 'order.share',width:'10%',align:'center',hidden: false,sortable:false},
       			{name: 'order.payTime',index: 'order.payTime',width:'10%',align:'center',hidden: false,sortable: false},
       			{name: 'order.payAmount',index: 'order.payAmount',width:'10%',align:'center',hidden: false,sortable: false},
       			{name: 'proofTime',index: 'proofTime',width:'6%',align:'center',hidden: false,sortable: false},
       			{name: 'proofAmount',index: 'proofAmount',width:'7%',align:'center',hidden: false,sortable: false},
       			{name: 'proofFileUrl',index: 'proofFileUrl',width:'6%',align:'center',hidden: false,sortable: false,formatter:fileChange},
       			{name: 'proofStatus',index: 'proofStatus',width:'6%',align:'center',hidden: false,sortable: false,formatter:statusChange},
       			{name: 'act',index:'act', width:'8%',align:'center',hidden:false,sortable:false,formatter:actChange}],
        mtype:"POST",
        postData:{c:"${market_order_detail_fundConfirm.code}",orderNumber:"${orderProof.orderNumber}"},
        rowNum:10,     
        page:"${orderProof.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pageredOne',
        height:	230,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	loadComplete:function(){
    		
    	},
        caption:'<msg:message code="order.proof.list"/>',
        toolbar: [true,"both"]
    });
    
  	//上传打款凭证 按钮
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
	.attr("id","${market_order_detail_fundConfirm_uploadProof.indexCode}")
	.attr("border","0").attr("class","${market_order_detail_fundConfirm_uploadProof.indexCode}")
	.append($("<img/>").attr("src","${base}/${skin}/${market_order_detail_fundConfirm_uploadProof.icon}")
  	.attr("width","18").attr("height","18").attr("border","0")
  	.attr("border","0").attr("align","absmiddle"))
  	.append("${market_order_detail_fundConfirm_uploadProof.name}");
	$("#t_productList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea)); 
    
	//上传打款凭证
    $("#${market_order_detail_fundConfirm_uploadProof.indexCode}").click(function(){
    	window.location.href='${base}${market_order_detail_fundConfirm_uploadProof.controller}?c=${market_order_detail_fundConfirm_uploadProof.code}&orderNumber=${orderProof.orderNumber}&productID=${orderProof.productID}&memberID=${orderProof.memberID}';
    });
	
  //返回按钮
	var $back = $("<a></a>").attr("href","javascript:void();")
	  .attr("id","back")
	  .append($("<img/>").attr("src","${base}/${skin}/images/icon/back.png")
	  .attr("width","18").attr("height","18").attr("border","0")
	  .attr("border","0").attr("align","absmiddle"))
	  .append("<msg:message code="button.back"/>");    
	$("#tb_productList").append("&nbsp;&nbsp;").append(
		$("<span></span>")
		.attr("class","jqgridContainer")
		.append($back)
	);
	$("#tb_productList #back").click(function(){
		window.location.href='${base}${market_order_detail.controller}?c=${market_order_detail.code}&back=1&id=${orderID}';
	});
	
    jQuery("#productList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    
    //查询按钮
    $("#select").click(function(){
    	jQuery("#productList").jqGrid("setGridParam",{postData:{productShortName:$("#productShortName").val(),memberName:$("#memberName").val(),proofStatus:$("#proofStatus").val()}}).trigger("reloadGrid");    
    });
    
  	//当前页iframe ID
	var iframeID = window.frameElement.id;
    /*******************   操作按钮的功能实现   start    *********************/
    //处理 按钮的连接实现
    $(".shortcut_${market_order_detail_fundConfirm_dispose.indexCode}").live("click",function(){
    	var url='${base}${market_order_detail_fundConfirm_dispose.controller}?c=${market_order_detail_fundConfirm_dispose.code}&id='+this.id+'&iframeID='+iframeID;
		k=top.$.layer({
			type: 2,
			title: "<msg:message code='proof.confirm'/>",
			fix: false,
			closeBtn: true,
			shadeClose: false,
			shade: [0.5,'#B0B1B2', true],
			closeBtn:[0,true],
			border : [5, 0.3, '#666', true],
			offset: ['10px',''],
			area: ['950px','550px'],
			iframe: {src: url},
			success: function(){
				//layer.msg('点击层外任意处，可关闭该iframe层', 2, 9);
			}
		});
    });
    /*******************   操作按钮的功能实现   end    *********************/
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

//处理状态 初始化
function statusChange(cellvalue, options, rowObject){
	var content="";
	<c:forEach items="${proofStatusMap}" var="it">
		if(cellvalue==${it.key}){
			return '${it.value}';
		}
	</c:forEach>
}

//列表操作功能展示
function actChange(cellvalue, options, rowObject){
	var id=rowObject.id;
	var status = rowObject.proofStatus;
	var content="";
	//处理  连接 
	<c:if test="${!empty market_order_detail_fundConfirm_dispose}">
    <security:authorize ifAnyGranted="${market_order_detail_fundConfirm_dispose.code}">
		if(status == 0){
			content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_order_detail_fundConfirm_dispose.indexCode}' title='${market_order_detail_fundConfirm_dispose.name}'>";
			content += "<img src='${base}/${skin}/${market_order_detail_fundConfirm_dispose.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${market_order_detail_fundConfirm_dispose.name}'>${market_order_detail_fundConfirm_dispose.name}</font>";
			content += "</a>";	
		}else{
			content += "<span style=\"color:#CFC09F;\"><img src=\"/FD/common/skin/images/icon/order processing.png\" weight=\"18\" height=\"18\" border=\"0\" align=\"absmiddle\">已处理</span>";
		}
    </security:authorize>
	</c:if>
	return content;
}

function closeWin(){
	top.layer.close(k);
}

function reloadGridTwo(){
	jQuery("#productList").jqGrid().trigger("reloadGrid");
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
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%">
					<input type="hidden" name="c" value="${orderProof.c}"/>
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td align="center">
							<form:form method="post" action="#" commandName="orderProof" name="f">
								<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
									<tr>
										<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.proof.status'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable" colspan="3">
											<form:select path="proofStatus">
												<form:option value=""><msg:message code='select.all'/></form:option>
												<c:forEach items="${proofStatusMap}" var="it">
													<form:option value="${it.key}">${it.value}</form:option>	
												</c:forEach>
											</form:select>
										</td>
										<td rowspan="2" width="20%" class="button_area" align="center">
											<input type="button" class="btn2" id="select" value="<msg:message code='button.search'/>">
										</td>
									</tr>	
								</table>
							</form:form>
							</td>
						</tr>
						<tr>
							<td>
								<table id="productList"><tr><td>&nbsp;</td></tr></table>
								<div id="pageredOne"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>