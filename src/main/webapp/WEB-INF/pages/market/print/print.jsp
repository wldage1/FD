<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<style type="text/css">
    .ab{ width:150px;border:0 solid 1px; border-top:none; border-left:none; border-right:none;}
</style>
<script src="${base}/common/js/print/jquery.PrintArea.js" type="text/javascript"></script>
<script src="${base}/common/js/print/jquery-barcode.min.js" type="text/javascript"></script>
<link  href="${base}/${skin}/css/tab.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#orderList").setGridWidth($(window).width()*0.999);　
});
var settings = {
        output:'css',
        bgColor: '#FFFFFF',
        color: '#000000',
        barWidth: 2,
        barHeight: 30
      };
$(document).ready(function(){
    jQuery("#orderList").jqGrid({
        url: 'print/orderjson.json',
        datatype: 'json',
        colNames: ["",
                "<msg:message code='order.orderNumber'/>",
       	        "<msg:message code='order.product.shortName'/>",
       	        "<msg:message code='product.org.name'/>", 
       	  	    "<msg:message code='order.orgID.name'/>",
       	        "<msg:message code='order.clientName'/>",
       	        "<msg:message code='order.investAmount'/>",
       	        "<msg:message code='member.name.nickname'/>",
       	        "<msg:message code='member.nickName'/>",
       	        "<msg:message code='order.tradeType'/>",
       	        "<msg:message code='order.tradeStatus'/>",
       	        "<msg:message code='order.documentStatus'/>",
       	        "<msg:message code='order.operate'/>"
       	        ],
               colModel: [
                {name: 'id',index: 'id',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'orderNumber',index: 'orderNumber',width:'13%',align:'center',hidden: false,sortable: false}, 
                {name: 'productShortName',index: 'productShortName',width:'8%',align:'center',hidden: false,sortable: false},
                {name: 'providerShortName',index: 'providerShortName',width:'10%',align:'center',hidden: false,sortable: false},
                {name: 'orgShortName',index: 'orgShortName',width:'10%',align:'center',hidden: false,sortable: false},
       			{name: 'clientName',index: 'clientName',width:'7%',align:'center',hidden: false,sortable:false},
       			{name: 'investAmount',index: 'investAmount',width:'12%',align:'center',hidden: false,sortable:false},
       			{name: 'memberName',index: 'memberName',width:'10%',align:'center',hidden: false,sortable: false,formatter:changeMember},
       			{name: 'memberNickName',index: 'memberNickName',width:'1%',align:'center',hidden: false,sortable: false,hidden:true},
       			{name: 'tradeTypeName',index: 'tradeTypeName',width:'6%',align:'center',hidden: false,sortable: false},
       			{name: 'tradeStatusName',index: 'tradeStatusName',width:'8%',align:'center',hidden: false,sortable: false},
       			{name: 'documentStatusName',index: 'documentStatusName',width:'6%',align:'center',hidden: false,sortable: false},
       			{name: 'act',index:'act', width:'17%',align:'center',hidden:false,sortable:false,formatter:actChange}],
        mtype:"POST",
        postData:{c:"${market_print.code}"},
        rowNum: 10,
        height:	230,
        pager: '#orderPagered',
        autowidth: true,
        viewrecords: true,
        multiselect: true,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	loadComplete:function(){
			
    	},
        caption:'<msg:message code="order.tradeStatus.2.list"/>',
        toolbar: [true,"top"]
    });
    
    <c:if test="${!empty market_print_print}">
    <security:authorize ifAnyGranted="${market_print_print.code}">	 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${market_print_print.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/${market_print_printview.icon}")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${market_print_print.name}");
	$("#t_orderList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));  
	
	//批量打印
    $("#${market_print_print.indexCode}","#t_orderList").click(function(){
    	var ids = jQuery("#orderList").jqGrid("getGridParam","selarrrow");
		if (ids && ids !=''){
//			var layers = top.layer.confirm("<msg:message code='printcover.printall.confirm'/>",  function(){
//				for(var i=0;i < ids.length;i++){
//					var id = ids[i];
					window.open('${base}${market_print_printview.controller}?c=${market_print_printview.code}&ids='+ ids);
//				}
//			});
//			top.layer.close(layers);
		}
		else{
    		top.layer.msg("<msg:message code='info.prompt.print'/>");
   		}    	
    });    
    </security:authorize>
    </c:if> 
	
	
    jQuery("#orderList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    $(".shortcut_${market_print_printview.indexCode}").live("click",function(){
    	var rowid = $(this).attr("id");
    	window.open('${base}${market_print_printview.controller}?c=${market_print_printview.code}&id='+ rowid);
    });
    
    //查询按钮
    $("#select").click(function(){
    	jQuery("#orderList").jqGrid("setGridParam",{postData:{productName:$("#productName").val()}}).trigger("reloadGrid");    
    });
    
    //初始化改变中文 交易类型
    function changeZhn(cellvalue, options, rowObject){
    	var content="";
    	<c:forEach items="${tradeTypeMap}" var="it">
	    	if(cellvalue==${it.key}){
	    		return '${it.value}';
	    	}
    	</c:forEach>
    }
});

//列表操作功能展示
function actChange(cellvalue, options, rowObject){
	var id=rowObject.id;
	var content="";
	<c:if test="${!empty market_print_printview}">
    <security:authorize ifAnyGranted="${market_print_printview.code}">
    	content += "<a href='#' id='" + id + "' class='shortcut_${market_print_printview.indexCode}' title='${market_print_printview.name}'>";
    	content += "<img src='${base}/${skin}/${market_print_printview.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${market_print_printview.name}'>${market_print_printview.name}</font>";
    	content += "</a>";
    </security:authorize>
	</c:if>
	return content;
}

function changeDocument(cellvalue,options,rowObject){
	if(cellvalue == 0){
		return '未寄出';
	}else if(cellvalue == 1){
		return '已寄出';
	}else if(cellvalue == 2){
		return '已收到';
	}
}
      

function changeMember(cellvalue,options,rowObject){
	if(rowObject.memberName!=null){
		if(rowObject.memberNickName!=null){
			return rowObject.memberName+"("+rowObject.memberNickName+")";
		}else{
			return rowObject.memberName;
		}
	}else{
		return " <br>";
	}
}

</script>
	</head>
	<body class="skinMain">
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%">
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td align="center">
							<form:form method="post" action="#" commandName="order" name="f">
								<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
									<tr>
										<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.product.shortName'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<select id="productName">
												<option value="">全部</option>
												<c:forEach items="${productList}" var="it">
													<option value="${it.shortName}">${it.shortName}</option>	
												</c:forEach>
											</select>
										</td>
										<td width="20%" class="button_area" align="center">
											<input type="button" class="btn2" id="select" value="<msg:message code='button.search'/>">
										</td>
									</tr>	
								</table>
							</form:form>
							</td>
						</tr>
						<tr>
							<td>
								<table id="orderList"><tr><td>&nbsp;</td></tr></table>
								<div id="orderPagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>