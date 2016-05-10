<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">

$(document).ready(function() {
	
	/* 页面初始化
	 * 信托产品配置页面 1:加载列表－> 创建受益类型 －> 配置收益率*/
	 
	 // 1、加载受益权类型列表
	 jQuery("#subProductList").jqGrid({
        treeGridModel: 'adjacency',
        ExpandColClick: true,
        url: 'saleconfig/saleconfigjson.json',
        datatype: 'json',
        colNames: [ "<msg:message code='product.name'/>",
		            "<msg:message code='product.beginningShare'/>",
					"<msg:message code='product.incrementalShare'/>",
					"","",
					"<msg:message code='info.operate'/>"],
        colModel: [{name: 'type',index: 'subProduct.type',width:'55%',align:'left',hidden: false,sortable: false,formatter:function(cellvalue, options, rowdata){
						if(cellvalue == '' || cellvalue == null){
							showTitle = "${product.name}";
						}else{
							showTitle = "${product.name}["+cellvalue+"]";
						}
    					return showTitle;	
						}
				   }, 
				   {name: 'beginningShare',index: 'beginningShare',width:'10%',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowdata){
						return "${product.beginningShare}";
						}
				   }, 
				   {name: 'incrementalShare',index: 'incrementalShare',width:'10%',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowdata){
						return "${product.incrementalShare}";
						}
				   }, 
				   {name: 'id',index: 'id',hidden: true},
				   {name: 'id',index: 'profitType',hidden: true},
                   {name:'act',index:'act', width:'25%',align:'center',sortable:false,sortable: false,formatter:function(cellvalue, options, rowdata){
                   		var id = rowdata.id;
    					var content = "";
						var space = "&nbsp;&nbsp;";
    					
						// 修改
						<c:if test="${!empty product_manage_saleconfig_split}">
						<security:authorize ifAnyGranted="${product_manage_saleconfig_split.code}">	
						content += "<a href='javascript:void(0);' title='${product_manage_saleconfig_split.name}' id='" + id + "' class='shortcut_${product_manage_saleconfig_split.indexCode}'>";
						content += "<img src='${base}/${skin}/${product_manage_saleconfig_split.icon}' weight='18' height='18' border='0' align='absmiddle'/><msg:message code="product.operate.modify"/>";
						content += "</a>";
						content += space;
						</security:authorize>
						</c:if>
						
						// 收益
						<c:if test="${!empty product_manage_saleconfig_profit}">
						<security:authorize ifAnyGranted="${product_manage_saleconfig_profit.code}">
						content += "<a href='javascript:void(0);' title='${product_manage_saleconfig_profit.name}' id='" + id + "'";
						if(rowdata.profitType == 2){
						content += "class='shortcut_${product_manage_saleconfig_profit.indexCode}'";
						}else{
						content += "style='color:#ccc;'";
						}
						content += "title='${product_manage_saleconfig_profit.name}'>";
						content += "<img src='${base}/${skin}/${product_manage_saleconfig_profit.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_saleconfig_profit.name}";
						content += "</a>";
						content += space;
						</security:authorize>
						</c:if>
						
						// 净值
						<c:if test="${product.type == 2}">
						<c:if test="${!empty product_manage_saleconfig_netvalue}">
						<security:authorize ifAnyGranted="${product_manage_saleconfig_netvalue.code}">
						content += "<a href='javascript:void(0);' title='${product_manage_saleconfig_netvalue.name}' id='" + id + "' class='shortcut_${product_manage_saleconfig_netvalue.indexCode}' >";
						content += "<img src='${base}/${skin}/${product_manage_saleconfig_netvalue.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_saleconfig_netvalue.name}";
						content += "</a>";
						content += space;
						</security:authorize>
						</c:if>
						</c:if>
						
						// 居间费比例
						<c:if test="${!empty product_manage_saleconfig_commissionratio}">
						<security:authorize ifAnyGranted="${product_manage_saleconfig_commissionratio.code}">	
						content += "<a href='javascript:void(0);' title='${product_manage_saleconfig_commissionratio.name}' id='" + id + "' class='shortcut_${product_manage_saleconfig_commissionratio.indexCode}'>";
						content += "<img src='${base}/${skin}/${product_manage_saleconfig_commissionratio.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_saleconfig_commissionratio.name}";
						content += "</a>";
						content += space;
						</security:authorize>
						</c:if>
						
						// 删除
						<c:if test="${!empty product_manage_saleconfig_delete}">
						<security:authorize ifAnyGranted="${product_manage_saleconfig_delete.code}">
						content += "<a href='javascript:void(0);' title='${product_manage_saleconfig_delete.name}' id='" + id + "' class='shortcut_${product_manage_saleconfig_delete.indexCode}' >";
						content += "<img src='${base}/${skin}/${product_manage_saleconfig_delete.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_saleconfig_delete.name}";
						content += "</a>";
						content += space;
						</security:authorize>
						</c:if>
						
		    		    return content;
						}
				  }],
        mtype:"POST",
        postData:{c:"${c}",productId:"${product.id}",id:"${product.id}"},/*id 参数为成功业跳转时产品ID参数*/
        height:<msg:message code='jqgrid.height.300'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
		rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
        caption:'${product_manage_saleconfig_trust.name}',
        toolbar: [true,"top"]
    });
	
	$(".shortcut_${product_manage_saleconfig_split.indexCode}").live("click",function(){
    	var rowid = $(this).attr("id");
    	window.location.href = "${base}${product_manage_saleconfig_split.controller}?c=${product_manage_saleconfig_split.code}&productId=${product.id}&id="+rowid+"";
    });
	
	$(".shortcut_${product_manage_saleconfig_profit.indexCode}").live("click",function(){
	  	var rowid = $(this).attr("id");
 		window.location.href = "${base}${product_manage_saleconfig_profit.controller}?c=${product_manage_saleconfig_profit.code}&id="+rowid+"&productId=${product.id}";
	});
	
	$(".shortcut_${product_manage_saleconfig_commissionratio.indexCode}").live("click",function(){
	  	var rowid = $(this).attr("id");
 		window.location.href = "${base}${product_manage_saleconfig_commissionratio.controller}?c=${product_manage_saleconfig_commissionratio.code}&id="+rowid+"&productId=${product.id}";
	});
	
	$(".shortcut_${product_manage_saleconfig_netvalue.indexCode}").live("click",function(){
	  	var rowid = $(this).attr("id");
 		window.location.href = "${base}${product_manage_saleconfig_netvalue.controller}?c=${product_manage_saleconfig_netvalue.code}&id="+rowid+"&productId=${product.id}";
	});

	<c:if test="${!empty product.isTotalShare}">
	<c:if test="${!empty product_manage_saleconfig_split}">
		<security:authorize ifAnyGranted="${product_manage_saleconfig_split.code}">
		var $ea = $("<a></a>").attr("href","javascript:void(0)")
				  .attr("id","${product_manage_saleconfig_split.indexCode}")
				  .append($("<img/>").attr("src","${base}/${skin}/images/icon/create.png")
				  .attr("width","18").attr("height","18").attr("border","0")
				  .attr("border","0").attr("align","absmiddle"))
				  .append("${product_manage_saleconfig_split.name}");
		$("#t_subProductList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea)); 
		$("#${product_manage_saleconfig_split.indexCode}","#t_subProductList").click(function(){
			window.location.href = "${base}${product_manage_saleconfig_split.controller}?c=${product_manage_saleconfig_split.code}&productId=${product.id}";
		});    
		</security:authorize>
    </c:if>
	</c:if>
	
	
	<c:if test="${!empty product_manage_saleconfig_delete}">
		<security:authorize ifAnyGranted="${product_manage_saleconfig_delete.code}">
		$(".shortcut_${product_manage_saleconfig_delete.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
				var url = "${base}${product_manage_saleconfig_delete.controller}.json?c=${product_manage_saleconfig_delete.code}&id="+rowid;
				$.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData,textStatus){
						if (textStatus == "success"){
							if (jsonData.status == "success"){
								//jQuery("#subProductList").jqGrid("delRowData",rowid);
								$("#subProductList").trigger("reloadGrid");  
								top.layer.close(layers);										
							}
						}
					}
				});
			});
		}); 
		</security:authorize>
    </c:if>
});

</script>
</head>
<body class="skinMain">

	<table id="subProduct_table" width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain" align="left">
		<tr>
			<td valign="top" >
				<form id="subProductList_form">
					<table id="subProductList"><tr><td>&nbsp;</td></tr></table>
				</form>
			</td>
		</tr>
	</table>
	<div class="toolbar">
		<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" onclick="window.location.href='${base}${market_salemanage.controller}?c=${market_salemanage.code}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
		</div>
		<div class="right"></div>
	</div>
</body>
</html>
