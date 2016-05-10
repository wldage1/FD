<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${base}/common/js/print/jquery.PrintArea.js" type="text/javascript"></script>
<link href="${base}/${skin}/css/tab.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#orgList").setGridWidth($(window).width()*0.999);　
});
var currTab = 1;
var flag=1;
var payTimeStart;
var payTimeEnd;
var payDataStart;
var payDataEnd;
var orgShortName;
var gridWidth;
$(document).ready(function(){
	jQuery("#orgList").jqGrid({
        url: 'organizationgrant/orgGrid.json',
        datatype: 'json',
        colNames: [
                "","","","",
                "机构简称",
                "开户行",
       	  		"账户名",
       	  		"卡号",
       	  		"居间费总金额（元）",
       	     	"<msg:message code='order.operate'/>"
       	        ],
		colModel: [
                {name: 'id',index: 'id',width:'0%',align:'center',hidden: true,sortable: false}, 
                {name: 'orgId',index: 'orgId',width:'0%',align:'center',hidden: true,sortable: false}, 
                {name: 'teamOrOrgId',index: 'teamOrOrgId',width:'0%',align:'center',hidden: true,sortable: false}, 
                {name: 'status',index: 'status',width:'0%',align:'center',hidden: true,sortable: false},  
                {name: 'title1',index: 'title1',width:'14%',align:'center',hidden: false,sortable: false}, 
                {name: 'title2',index: 'title2',width:'14%',align:'center',hidden: false,sortable: false}, 
                {name: 'title3',index: 'title3',width:'14%',align:'center',hidden: false,sortable: false}, 
                {name: 'title4',index: 'title4',width:'14%',align:'center',hidden: false,sortable: false}, 
                {name: 'payAmount',index: 'payAmount',width:'14%',align:'center',hidden: false,sortable: false}, 
       			{name: 'act',index:'act', width:'30%',align:'center',hidden: false,sortable:false,formatter:orgListAct}
       			],
        mtype:"POST",
        postData:{c:'303', statusStr: "(0)"},
        height:	'auto',
        autowidth: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete:function(){
        	gridWidth=jQuery("#orgList").jqGrid().getGridParam().width;
        },
        toolbar: [true,"top"],
	    subGrid : true,
        caption:'待发放居间费列表',
        subGridRowExpanded: function(subgrid_id, row_id) {
        	var data = jQuery("#orgList").jqGrid("getRowData",row_id);
        	var id = data.id;
        	var status = data.status;
	        var subgrid_table_id, pager_id;
			subgrid_table_id = subgrid_id+"_t";
			pager_id = "p_"+subgrid_table_id;
			$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
			jQuery("#"+subgrid_table_id).jqGrid({
		        url: 'organizationgrant/grid.json',
		        datatype: 'json',
		        colNames: ["",
		                "<msg:message code='order.orderNumber'/>",
		       	  		"<msg:message code='order.clientName'/>",
		       	  		"<msg:message code='order.commissionPercent'/>",
		       	  		"<msg:message code='order.payAmount'/>",
		       	        "<msg:message code='order.commission'/>","审核时间",
		       	     	"<msg:message code='order.operate'/>"
		       	        ],
		               colModel: [
		                {name: 'commissionID',index: 'commissionID',width:'10%',align:'center',hidden: true,sortable: false},
		                {name: 'orderNumber',index: 'orderNumber',width:'10%',align:'center',hidden: false,sortable: false}, 
		                {name: 'clientName',index: 'clientName',width:'6%',align:'center',hidden: false,sortable: false},
		                {name: 'commissionProportionStr',index: 'commissionProportionStr',width:'8%',align:'center',hidden: false,sortable: false},
		                {name: 'payAmount',index: 'payAmount',width:'8%',align:'center',hidden: false,sortable: false},
		                {name: 'commission',index: 'commission',width:'10%',align:'center',hidden: false,sortable: false},
		                {name: 'auditingTime',index: 'auditingTime',width:'6%',align:'center',hidden: false,sortable: false},
		       			{name: 'act',index:'act', width:'6%',align:'center',hidden: false,sortable:false,formatter:actChange}
		       			],
		        mtype:"POST",
		        postData:{c:'303', id:id, status:status},
		        height:	'auto',
		        autowidth: true,
		        jsonReader: {
		        	repeatitems: false
		        },
		    });
        }
	});
	
	<c:if test="${!empty commission_organizationgrant_export}">
	<security:authorize ifAnyGranted="${commission_organizationgrant_export.code}">
		var $ea = $("<a></a>").attr("href","javascript:void(0)")
		 .attr("id","${commission_organizationgrant_export.indexCode}")
		 .append($("<img/>").attr("src","../common/skin/${commission_auditing_export.icon}")
		 .attr("width","18").attr("height","18").attr("border","0")
		 .attr("border","0").attr("align","absmiddle"))
		 .append("${commission_organizationgrant_export.name}");
		$("#t_orgList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));
		$("#${commission_organizationgrant_export.indexCode}","#t_orgList").click(function(){
	    	payTimeStart=$("#payTimeStart").val();
	    	payTimeEnd=$("#payTimeEnd").val();
	    	payDataStart=$("#payDataStart").val();
	    	payDataEnd=$("#payDataEnd").val();
			if(flag==1){
				var orgShortName =$("#orgShortNameNew").val();
				window.location.href='${base}${commission_organizationgrant_export.controller}?c=303&status=0&payDataStart='+payDataStart+'&payDataEnd='+payDataEnd+'&orgShortName='+orgShortName
			}else if(flag==2){
				var orgShortName =encodeURIComponent($("#orgShortNameNew").val());
				window.location.href='${base}${commission_organizationgrant_export.controller}?c=303&status=1&payTimeStart='+payTimeStart+'&payTimeEnd='+payTimeEnd+'&orgShortName='+orgShortName
			}else{
				window.location.href="${base}${commission_organizationgrant_export.controller}?c=303&status=3&payDataStart=''&payDataEnd=''&orgShortName=''&payTimeEnd=''&payTimeStart=''"
			}
			
		});
	 </security:authorize>
	 </c:if> 
  	
  	//查看
    $(".shortcut_${commission_organizationgrant_detail.indexCode}").live("click",function(){
    	window.location.href='${base}${commission_organizationgrant_detail.controller}?c=${commission_organizationgrant_detail.code}&tab='+currTab+'&id='+this.id;
    });
    //查询按钮
    $("input[name='select']").click(function(){
    	payTimeStart=$("#payTimeStart").val();
    	payTimeEnd=$("#payTimeEnd").val();
    	payDataStart=$("#payDataStart").val();
    	payDataEnd=$("#payDataEnd").val();
    	orgShortName=encodeURIComponent($("#orgShortNameNew").val());
    	if(flag==1){
        	if(payDataStart>payDataEnd && payDataEnd!=''){
        		top.$.layer({
        			shade : [0.5 , '#000' , true],
        	  		offset : ['50px',''],
        	  	 	title : "<msg:message code='info.prompt'/>",
        		    dialog : {msg:"预计发放开始时间应小于预计发放结束时间！",type : 3}	
        		});
        	} 		
        	jQuery("#orgList").jqGrid("setGridParam",{postData:{c:"303", status: 0,payDataStart:payDataStart,payDataEnd:payDataEnd,orgShortName:orgShortName}}).setCaption('已发放居间费列表').trigger("reloadGrid");
    	}
    	if(flag==2){
    		orgShortName=encodeURIComponent($("#orgShortName").val());
        	if(payTimeStart>payTimeEnd && payTimeEnd!=''){
        		top.$.layer({
        			shade : [0.5 , '#000' , true],
        	  		offset : ['50px',''],
        	  	 	title : "<msg:message code='info.prompt'/>",
        		    dialog : {msg:"发放开始时间应小于发放结束时间！",type : 3}	
        		});
        	} 		
        	jQuery("#orgList").jqGrid("setGridParam",{postData:{c:"303", status:1,payTimeStart:payTimeStart,payTimeEnd:payTimeEnd,orgShortName:orgShortName}}).setCaption('已发放居间费列表').trigger("reloadGrid");
    	}
    });
});

//列表操作功能展示
function actChange(cellvalue, options, rowObject){
	var commissionID = rowObject.commissionID;
	var content="";
	<c:if test="${!empty commission_organizationgrant_detail}">
    <security:authorize ifAnyGranted="${commission_organizationgrant_detail.code}">
    	content += "<a href='#' id='" + commissionID + "' class='shortcut_${commission_organizationgrant_detail.indexCode}' title='${commission_organizationgrant_detail.name}' >";
    	content += "<img src='${base}/${skin}/${commission_organizationgrant_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_organizationgrant_detail.name}'>${commission_organizationgrant_detail.name}</font>";
    	content += "</a>";
    </security:authorize>
	</c:if>
	return content;
}

//设置主表格操作
function orgListAct(cellval, opts, rowObject){
	var id = rowObject.id;
	var content="";
	<c:if test="${!empty commission_organizationgrant_paySuccess}">
    <security:authorize ifAnyGranted="${commission_organizationgrant_paySuccess.code}">
	    content += "<a href='#' id='"+id+"' class='shortcut_${commission_organizationgrant_paySuccess.indexCode}' title='${commission_organizationgrant_paySuccess.name}' >";
		content += "<img src='${base}/${skin}/${commission_organizationgrant_paySuccess.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_organizationgrant_paySuccess.name}'>${commission_organizationgrant_paySuccess.name}</font>";
		content += "</a>";
    </security:authorize>
	</c:if>
			
	<c:if test="${!empty commission_organizationgrant_payFail}">
    <security:authorize ifAnyGranted="${commission_organizationgrant_payFail.code}">
	    content += "<a href='#'  id='"+id+"'  class='shortcut_${commission_organizationgrant_payFail.indexCode}' title='${commission_organizationgrant_payFail.name}' >";
		content += "<img src='${base}/${skin}/${commission_organizationgrant_payFail.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_organizationgrant_payFail.name}'>${commission_organizationgrant_payFail.name}</font>";
		content += "</a>";
    </security:authorize>
	</c:if>
		
	<c:if test="${!empty commission_organizationgrant_download}">
	<security:authorize ifAnyGranted="${commission_organizationgrant_download.code}">
		content += "<a href='#'  id='"+id+"' class='shortcut_${commission_organizationgrant_download.indexCode}' title='${commission_organizationgrant_download.name}' >";
		content += "<img src='${base}/${skin}/${commission_organizationgrant_download.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_organizationgrant_download.name}'>${commission_organizationgrant_download.name}</font>";
		content += "</a>";
	</security:authorize>
	</c:if>
		
	<c:if test="${!empty commission_organizationgrant_invoice}">
    <security:authorize ifAnyGranted="${commission_organizationgrant_invoice.code}">
	    content += "<a href='#'  id='"+id+"' class='shortcut_${commission_organizationgrant_invoice.indexCode}' title='${commission_organizationgrant_invoice.name}' >";
		content += "<img src='${base}/${skin}/${commission_organizationgrant_invoice.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_organizationgrant_invoice.name}'>${commission_organizationgrant_invoice.name}</font>";
		content += "</a>";
    </security:authorize>
	</c:if>
		
	return content;
}

function tradeTypeFormat(cellvalue, options, rowObject){
   var content="";
   	<c:forEach var="item" items="${tradeType}">
   	<c:set var="isDoing" value="0"/>
   	   <c:if test="${isDoing!='1'}">
		   if(cellvalue=="${item.key}"){
			   content="${item.value}";
			   <c:set var="isDoing" value="1"/> 
		   }
	   </c:if>
	</c:forEach>
	return content;    
}
$(".shortcut_${commission_organizationgrant_paySuccess.indexCode}").live("click",function(){
	var rowid = $(this).attr("id");
	var data = jQuery("#orgList").jqGrid("getRowData",rowid);
	var teamOrOrgId =data.teamOrOrgId;
	var teamOrOrgPayRecordId = data.id;
	var url = "${base}${commission_organizationgrant_paySuccess.controller}.json?c=${commission_organizationgrant_paySuccess.code}&id=" 
			+ teamOrOrgPayRecordId + "&status=1" + "&teamID=" + teamOrOrgId;
	var index = top.$.layer({
		shade : [0.5 , '#000' , true],
  	    area : ['auto','auto'],
  	  	offset : ['50px',''],
		dialog   : {
			msg  : "确定发放成功该机构的居间费吗？",
			btns : 2,
			type : 4,
			btn  : ["<msg:message code='button.confirm'/>", "<msg:message code='button.cancel'/>"],
			yes  : function(){
				$.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData,textStatus){
						if(textStatus == "success"){
							if(jsonData.mark == "1"){
								top.layer.close(index);
								top.$.layer({
									shade : [0.5 , '#000' , true],
							  		offset : ['50px',''],
							  	 	title : "<msg:message code='info.prompt'/>",
								    dialog : {msg:"该机构还有订单没处理，不能发放！",type : 3}	
								});
								return false;
							}
							if(jsonData.status == "success"){
								top.layer.close(index);
								top.$.layer({
									shade : [0.5 , '#000' , true],
							  		offset : ['50px',''],
							  	 	title : "<msg:message code='info.prompt'/>",
								    dialog : {msg:"操作成功！",type : 1}	
								});
								$("#orgList").trigger("reloadGrid");
							}else{
								top.layer.close(index);
								top.$.layer({
									shade : [0.5 , '#000' , true],
							  		offset : ['50px',''],
							  	 	title : "<msg:message code='info.prompt'/>",
								    dialog : {msg:"操作失败！",type : 3}	
								});
							}
						}
					}
				});
			},
			no   : function(){
				top.layer.close(index);
			}
		}
	});
});

//发放失败
$(".shortcut_${commission_organizationgrant_payFail.indexCode}").live("click",function(){
	var rowid = $(this).attr("id");
	var data = jQuery("#orgList").jqGrid("getRowData",rowid);
	var teamOrOrgId =data.teamOrOrgId;
	var teamOrOrgPayRecordId = data.id;
	var url = "${base}${commission_organizationgrant_paySuccess.controller}.json?c=${commission_organizationgrant_paySuccess.code}&id="
		+ teamOrOrgPayRecordId + "&status=3" + "&teamID=" + teamOrOrgId;
	var index = top.$.layer({
		shade : [0.5 , '#000' , true],
  	    area : ['auto','auto'],
  	  	offset : ['50px',''],
		dialog   : {
			msg  : "确定发放失败该机构的居间费吗？",
			btns : 2,
			type : 4,
			btn  : ["<msg:message code='button.confirm'/>", "<msg:message code='button.cancel'/>"],
			yes  : function(){
				$.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData,textStatus){
						if(textStatus == "success"){
							if(jsonData.mark == "1"){
								top.layer.close(index);
								top.$.layer({
									shade : [0.5 , '#000' , true],
							  		offset : ['50px',''],
							  	 	title : "<msg:message code='info.prompt'/>",
								    dialog : {msg:"该机构还有订单没处理，不能发放！",type : 3}	
								});
								return false;
							}
							if(jsonData.status == "success"){
								top.layer.close(index);
								top.$.layer({
									shade : [0.5 , '#000' , true],
							  		offset : ['50px',''],
							  	 	title : "<msg:message code='info.prompt'/>",
								    dialog : {msg:"操作成功！",type : 1}	
								});
								$("#orgList").trigger("reloadGrid");
							}else{
								top.layer.close(index);
								top.$.layer({
									shade : [0.5 , '#000' , true],
							  		offset : ['50px',''],
							  	 	title : "<msg:message code='info.prompt'/>",
								    dialog : {msg:"操作失败！",type : 3}	
								});
							}
						}
					}
				});
			},
			no   : function(){
				top.layer.close(index);
			}
		}
	});
});

$(".shortcut_${commission_organizationgrant_download.indexCode}").live("click",function(){
	var rowid = $(this).attr("id");
	var data = jQuery("#orgList").jqGrid("getRowData",rowid);
	var orgId =data.orgId;
	var url = "${base}${commission_organizationgrant_download.controller}.json?c=${commission_organizationgrant_download.code}&teamOrOrgID="+ orgId;
	$.ajax({
		url:url,
		type:'post',
		timeout:'60000',
		dataType:'json',
		success:function(jsonData){
			if(jsonData.status == "have"){
				$("#teamOrOrgID").val(orgId);
				$("#c").val("${commission_organizationgrant_download.code}");
				$("#formDown").submit();
			}else if(jsonData.status == "nohave"){
				top.$.layer({
					shade : [0.5 , '#000' , true],
			  		offset : ['50px',''],
			  	 	title : "<msg:message code='info.prompt'/>",
				    dialog : {msg:"没有需要下载的文件！",type : 3}	
				});
			}else{
				top.$.layer({
					shade : [0.5 , '#000' , true],
			  		offset : ['50px',''],
			  	 	title : "<msg:message code='info.prompt'/>",
				    dialog : {msg:"操作失败！",type : 3}	
				});
			}
		}
	});
});
$(".shortcut_${commission_organizationgrant_invoice.indexCode}").live("click",function(){
	var rowid = $(this).attr("id");
	var data = jQuery("#orgList").jqGrid("getRowData",rowid);
	var teamOrOrgId =data.teamOrOrgId;
	var url = "${base}${commission_organizationgrant_invoice.controller}.json?c=${commission_organizationgrant_invoice.code}&teamID="
		+ teamOrOrgId;
	$.ajax({
		url:url,
		type:'post',
		timeout:'60000',
		dataType:'json',
		success:function(jsonData,textStatus){
			if(textStatus == "success"){
				if(jsonData.mark == "1"){
					top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"该机构还有订单没处理，不能发票通知！",type : 3}	
					});
					return false;
				}
				if(jsonData.status == "success"){
					top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"发票通知成功！",type : 1}	
					});
				}else{
					top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"发票通知失败！",type : 3}	
					});
				}
			}
		}
	});
});

//TAB切换
function tab_order(tag){
	currTab = tag;
	payTimeStart=$("#payTimeStart").val();
	payTimeEnd=$("#payTimeEnd").val();
	payDataStart=$("#payDataStart").val();
	payDataEnd=$("#payDataEnd").val();
	changeTab(tag);
	if(tag==1){
		orgShortName=encodeURIComponent($("#orgShortNameNew").val());
		jQuery("#orgList").jqGrid("setGridParam",{postData:{c:"303", statusStr: "(0)",payDataStart:payDataStart,payDataEnd:payDataEnd,orgShortName:orgShortName},autowidth:true}).jqGrid('showCol',"act").setCaption('待发放居间费列表').trigger("reloadGrid");
		jQuery("#orgList").setGridWidth(gridWidth);
	}else if(tag==2){
		orgShortName=encodeURIComponent($("#orgShortName").val());
		jQuery("#orgList").jqGrid("setGridParam",{postData:{c:"303", statusStr: "(1,2)",payTimeStart:payTimeStart,payTimeEnd:payTimeEnd,orgShortName:orgShortName},autowidth:true}).jqGrid('hideCol',"act").setCaption('已发放居间费列表').trigger("reloadGrid");
		jQuery("#orgList").setGridWidth(gridWidth);
	}else if(tag==4){
		jQuery("#orgList").jqGrid("setGridParam",{postData:{c:"303", statusStr: "(3)"},autowidth:true}).jqGrid('showCol',"act").setCaption('居间费发放失败列表').trigger("reloadGrid");
		jQuery("#orgList").setGridWidth(gridWidth);
	}
}

function changeTab(tag){
	flag=tag
	if(tag == 1){
		$("#tab_1").addClass("selected");
		//去掉其他tab页选中状态
		$("[id^='tab_'][id!='tab_1']").removeClass("selected");
		$("#show").hide();
		$("#show1").show();
	}else if(tag == 2){
		$("#tab_2").addClass("selected");
		//去掉其他tab页选中状态
		$("[id^='tab_'][id!='tab_2']").removeClass("selected");
		$("#show").show();
		$("#show1").hide();
	}else if(tag == 4){
		$("#tab_4").addClass("selected");
		//去掉其他tab页选中状态
		$("[id^='tab_'][id!='tab_4']").removeClass("selected");
		$("#show").hide();
		$("#show1").hide();
	}
}

</script>

</head>
<body class="skinMain">
	<ul id="tabnav">
		<li class="tab_1" ><a href="#" id="tab_1" onclick="tab_order(1)" class="selected">待发放居间费</a></li>
		<li class="tab_2" ><a href="#" id="tab_2" onclick="tab_order(2)">已发放居间费</a></li>
		<li class="tab_4" ><a href="#" id="tab_4" onclick="tab_order(4)">居间费失败</a></li>
	</ul>
	<div>
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%" valign="top">
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr id="show" style="display: none;">
							<td align="center">
							<form:form method="post" action="commission" commandName="teamOrOrgPayDetail" name="f">
								<input type="hidden" name="c" value="${commission.c}"/>									
								<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
									<tr>
										<!--<td align="right" width="10%" class="search_info" height="25">发放时间<msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="20%"  class="search_lable">
											<form:input path="payTimeStart" readonly="readonly" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" class="Wdate" style="width:100px;" />
											--
											<form:input path="payTimeEnd" readonly="readonly" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" class="Wdate" style="width:100px;" />															
										</td>-->
										<td align="right" width="10%" class="search_info">机构简称<msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="10%"  class="search_lable">
											<form:input path="orgShortName"/>																				
										</td>
										<td width="20%" class="button_area">
											<input type="button" class="btn2" name="select" value="<msg:message code='button.search' />"/>
										</td>
									</tr>
								</table>
							</form:form>
							</td>
						</tr>
						<tr id="show1">
							<td align="center">
							<form:form method="post" action="organizationgrant/downloadfile" id="formDown" commandName="ontractc">
								<form:hidden path="teamOrOrgID"/>
								<form:hidden path="c"/>
							</form:form>
							<form:form method="post" action="commission" commandName="teamOrOrgPayDetail" name="f">
								<input type="hidden" name="c" value="${commission.c}"/>									
								<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
									<tr>
										<!--<td align="right" width="15%" class="search_info" height="25">预计发放时间<msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="20%"  class="search_lable">
											<form:input path="payDataStart" readonly="readonly" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" class="Wdate" style="width:100px;" />
											--
											<form:input path="payDataEnd" readonly="readonly" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" class="Wdate" style="width:100px;" />															
										</td>-->
										<td align="right" width="10%" class="search_info">机构简称<msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="10%"  class="search_lable">
											<form:input path="orgShortNameNew"/>																				
										</td>
										<td width="20%" class="button_area">
											<input type="button" class="btn2" name="select" value="<msg:message code='button.search' />"/>
										</td>
									</tr>
								</table>
							</form:form>
							</td>
						</tr>
						<tr>
							<td>
								<table id="orgList"><tr><td>&nbsp;</td></tr></table>
								<div id="pagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</body>
