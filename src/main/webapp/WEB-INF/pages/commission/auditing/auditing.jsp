<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link  href="${base}/${skin}/css/tab.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#orderList").setGridWidth($(window).width()*0.999);　
});
//订单交易类型
var tradeType = 1;
//需要核定订单的ID和提示信息内容
var vouchOrderID, vouchOrderNumber;
var index;
var payStatus=1,flag=1; //payStatus:审核通过不通过 flag:个人或机构
var payAmount,issuanceCostRate,timeCoefficient,serviceChargeCoefficient,incentiveFeeRate,commissionProportion,commiss;
$(document).ready(function(){
	$("#remark").attr("disabled", "disabled");
    jQuery("#orderList").jqGrid({
        url: 'auditing/grid.json',
        datatype: 'json',
        colNames: ["","","", "", "", "", "",
                "<msg:message code='order.orderNumber'/>",
       	        "理财顾问","机构简称","产品名称","子产品名称",
       	  		"<msg:message code='order.clientName'/>",
       	  		"<msg:message code='order.commissionPercent'/>",
       	  		"<msg:message code='order.payAmount'/>",
       	        "<msg:message code='order.commission'/>",
       	        "核定时间","审核状态","","","","","",
       	     	"<msg:message code='order.operate'/>"
       	        ],
               colModel: [
				{name: 'memberID',index: 'memberID',width:'1%',align:'center',hidden: true,sortable: false},
				{name: 'orgID',index: 'orgID',width:'1%',align:'center',hidden: true,sortable: false},
				{name: 'teamID',index: 'teamID',width:'1%',align:'center',hidden: true,sortable: false},         
				{name: 'orgContractID',index: 'orgContractID',width:'1%',align:'center',hidden: true,sortable: false},
				{name: 'agreementTemplateID',index: 'agreementTemplateID',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'id',index: 'id',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'commissionProportion',index: 'commissionProportion',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'orderHistory.orderNumber',index: 'orderNumber',width:'10%',align:'center',hidden: false,sortable: false}, 
                {name: 'memberName',index: 'memberName',width:'10%',align:'center',hidden: false,sortable: false},
                {name: 'orgShortName',index: 'orgShortName',width:'8%',align:'center',hidden: true,sortable: false},
                {name: 'productName',index: 'productName',width:'10%',align:'center',hidden: false,sortable: false},
                {name: 'subProductName',index: 'subProductName',width:'10%',align:'center',hidden: false,sortable: false},
                {name: 'clientName',index: 'clientName',width:'6%',align:'center',hidden: false,sortable: false},
                {name: 'commissionProportionStr',index: 'commissionProportionStr',width:'8%',align:'center',hidden: false,sortable: false},
                {name: 'payAmount',index: 'payAmount',width:'5%',align:'center',hidden: false,sortable: false},
       			{name: 'commission',index: 'commission',width:'8%',align:'center',hidden: false,sortable:false},
       			{name: 'checkRatifyTime',index: 'checkRatifyTime',width:'8%',align:'center',hidden: false,sortable: false},
       			{name: 'payStatus',index: 'payStatus',width:'8%',align:'center',sortable:false,formatter: formatPayStatus},
       			{name: 'payAmount',index: 'payAmount',width:'5%',align:'center',hidden: true,sortable: false},
       			{name: 'timeCoefficient',index: 'timeCoefficient',width:'8%',align:'center',hidden: true,sortable: false},
       			{name: 'issuanceCostRate',index: 'issuanceCostRate',width:'8%',align:'center',hidden: true,sortable: false},
       			{name: 'serviceChargeCoefficient',index: 'serviceChargeCoefficient',width:'8%',align:'center',hidden: true,sortable: false},
       			{name: 'incentiveFeeRate',index: 'incentiveFeeRate',width:'8%',align:'center',hidden: true,sortable: false},
       			{name: 'act',index:'act', width:'20%',align:'center',hidden: false,sortable:false,formatter:actChange}
       			],
        mtype:"POST",
        postData:{c:'302',payStatus:1,flag:1,month:'',shoudPayDate:''},
        height:	'auto',
        autowidth: true,
        multiselect:true,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete:function(){
        	gridWidth=jQuery("#orderList").jqGrid().getGridParam().width;
        },
        toolbar: [true,"top"]
    });
    
	<c:if test="${!empty commission_auditing_batchAuditing}">
	<security:authorize ifAnyGranted="${commission_auditing_batchAuditing.code}">
		var $ea = $("<a></a>").attr("href","javascript:void(0)")
		 .attr("id","${commission_auditing_batchAuditing.indexCode}")
		 .append($("<img/>").attr("src","../common/skin/${commission_auditing_batchAuditing.icon}")
		 .attr("width","18").attr("height","18").attr("border","0")
		 .attr("border","0").attr("align","absmiddle"))
		 .append("${commission_auditing_batchAuditing.name}");
		$("#t_orderList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));
		$("#${commission_auditing_batchAuditing.indexCode}","#t_orderList").click(function(){
				var selrow = jQuery("#orderList").jqGrid("getGridParam","selarrrow");
				if (selrow != null && selrow != ""){
					for(var i=0; i<selrow.length; i++){
						var data = jQuery("#orderList").jqGrid("getRowData",selrow[i]);
						var status = data.payStatus;
						if(status != "未审核"){
							top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"只能审核未审核的订单！",type : 3}
							});
							return false;
						}
					}
				var url = "${base}${commission_auditing_auditing.controller}.json?c=${commission_auditing_batchAuditing.code}&flag="+flag+"&payStatus=4&ids=" + selrow+"&status="+payStatus;
				var index = top.$.layer({
			  	    shade  : [0.5 , '#000' , true],
			  	    area   : ['auto','auto'],
			  	  	offset : ['50px',''],
			  	    dialog : {
			  	    msg    : "确定审核所选择的订单么？",
			  	    btns   : 2, 
			  	    type   : 4,
			  	    btn    : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
			  	    yes    : function(){
						$.ajax({
							url:url,
							type:'post',
							timeout:'60000',
							dataType:'json',
							success:function(jsonData,textStatus){
								if(jsonData.status == "success"){
									top.layer.close(index);
									top.$.layer({
										shade : [0.5 , '#000' , true],
								  		offset : ['50px',''],
								  	 	title : "<msg:message code='info.prompt'/>",
									    dialog : {msg:"操作成功！",type : 1}
									});
									
									$("#orderList").trigger("reloadGrid");
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
						});
			  	    },
			  	    no : function(){
			  	        	top.layer.close(index);
			  	        }
	  	    		}
				});
			} else{
				top.$.layer({
					shade  : [0.5 , '#000' , true],
			  		offset : ['50px',''],
			  	 	title  : "<msg:message code='info.prompt'/>",
				    dialog : {msg:"请选择需要审核的订单！",type : 3}	
				});
			}
	 });
	 </security:authorize>
	 </c:if>
    
	<c:if test="${!empty commission_auditing_export}">
	<security:authorize ifAnyGranted="${commission_auditing_export.code}">
		var $ea = $("<a></a>").attr("href","javascript:void(0)")
		 .attr("id","${commission_auditing_export.indexCode}")
		 .append($("<img/>").attr("src","../common/skin/${commission_auditing_export.icon}")
		 .attr("width","18").attr("height","18").attr("border","0")
		 .attr("border","0").attr("align","absmiddle"))
		 .append("${commission_auditing_export.name}");
		$("#t_orderList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));
		$("#${commission_auditing_export.indexCode}","#t_orderList").click(function(){
			window.location.href='${base}/commission/auditing/export?payStatus='+payStatus+'&flag='+flag+'&month='+$("#month").val()+'&shoudPayDate='+shoudPayDate;
		});
	 </security:authorize>
	 </c:if> 
		 
    jQuery("#orderList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
  	//查看
    $(".shortcut_${commission_auditing_detail.indexCode}").live("click",function(){
    	window.location.href='${base}${commission_auditing_detail.controller}?c=${commission_auditing_detail.code}&id='+this.id;
    });
  	
  	//查看合同
    $(".shortcut_${commission_auditing_viewerContract.indexCode}").live("click",function(){
    	var rowid = $(this).attr("id");
		var data = jQuery("#orderList").jqGrid("getRowData",rowid);
		var flag;
		var agreementTemplateID = data.agreementTemplateID;
		var orgContractID = data.orgContractID;
		var orgName = data.orgShortName;
		var orgID = data.orgID;
		var teamID = data.teamID;
		var id = data.id;
		var memberID = data.memberID;
		var url = "${base}${commission_auditing_viewerContract.controller}.json?c=${commission_auditing_viewerContract.code}";
		//查看居间公司合同
		if(orgName != ""){
			flag = 1;
			if(orgContractID != ""){
				url += "&orgContractID=" + orgContractID+ "&id=" + id;;
			}else{
				url += "&orgID=" + orgID + "&teamID=" + teamID + "&id=" + id;
			}
		//查看理财师证件复印件和网签协议
		}else{
			flag = 2;
			url += "&agreementTemplateID=" + agreementTemplateID + "&memberID=" + memberID;
		}
		url += "&flag=" + flag;
		//
		$.ajax({
			url:url,
			type:'post',
			timeout:'60000',
			dataType:'json',
			success:function(jsonData,textStatus){
				if(textStatus == "success"){
					if(jsonData.status == "success"){
						var data = jsonData.contract;
						if(flag == 1){
							if(data == null){
								 top.$.layer({
									shade : [0.5 , '#000' , true],
							  		offset : ['50px',''],
							  	 	title : "<msg:message code='info.prompt'/>",
								    dialog : {msg:"请在合同管理中上传合同！",type : 3}	
								});
								return false;
							}else{
								window.location.href = data.contractCreateTime + "/" + data.fileUrl;
							}
						}else{
							$("#image").attr("src", data.contractCreateTime + data.contractStopTime);
							$("#title").text(data.title);
							$("#content").html(data.content);
							index = $.layer({
								type : 1,
								title : "查看合同",
								shadeClose : true,
								border : [0, 0.5, '#666', true],
						  	    area : ['350px', '300px'],
						  	  	move: ['.xubox_title', true],
						  	  	offset : ['100px',''],
						       	page :{
						        	dom : '#appendix'
						        }
					  	  	});

						}
					}else{
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
		
    	//window.location.href='${base}${commission_auditing_viewerContract.controller}?c=${commission_auditing_viewerContract.code}&id='+this.id;
    });
  	
  	//审核
    $(".shortcut_${commission_auditing_auditing.indexCode}").live("click",function(){
    	var rowid = $(this).attr("id");
    	var data = jQuery("#orderList").jqGrid("getRowData",rowid);
    	var orgName = data.orgShortName;
    	var orgContractID = data.orgContractID;
    	var orgID = data.orgID;
    	var teamID = data.teamID;
    	vouchOrderID = data.id;
    	var id = data.id;
    	if(orgName != ""){
    		var url = "${base}${commission_auditing_viewerContract.controller}.json?c=${commission_auditing_viewerContract.code}&flag="+1;
        	if(orgContractID != ""){
    			url += "&orgContractID=" + orgContractID;
    		}else{
    			url += "&orgID=" + orgID + "&teamID=" + teamID + "&id=" + id;
    		}
        	$.ajax({
    			url:url,
    			type:'post',
    			timeout:'60000',
    			dataType:'json',
    			success:function(jsonData,textStatus){
    				if(textStatus == "success"){
    					if(jsonData.status == "success"){
    						var dt = jsonData.contract;
    							if(dt == null){
    								 top.$.layer({
    									shade : [0.5 , '#000' , true],
    							  		offset : ['50px',''],
    							  	 	title : "<msg:message code='info.prompt'/>",
    								    dialog : {msg:"请在合同管理中上传合同！",type : 3}	
    								});
    								return false;
    							}else{
    								if(payStatus == 1){
    									index = $.layer({
    										type : 1,
    										title : "订单审核",
    										shadeClose : true,
    										border : [0, 0.5, '#666', true],
    								  	    area : ['350px', 'auto'],
    								  	  	move: ['.xubox_title', true],
    								  	  	offset : ['100px',''],
    								       	page :{
    								        	dom : '#auditingDiv'
    								        }
    							  	  	});
    						    	}else{
    						    		payAmount=data.payAmount;
    						    		issuanceCostRate=data.issuanceCostRate;
    						    		timeCoefficient=data.timeCoefficient;
    						    		serviceChargeCoefficient=data.serviceChargeCoefficient;
    						    		incentiveFeeRate=data.incentiveFeeRate;
    						    		commissionProportion=data.commissionProportion;
    						    		//commissionProportion=commissionProportion.substring(0,commissionProportion.length-1);
    						    		commiss=data.commission;
    						    		$("#payAmount").val(data.payAmount);
    						    		$("#issuanceCostRate").val(data.issuanceCostRate);
    						    		$("#timeCoefficient").val(data.timeCoefficient);
    						    		$("#serviceChargeCoefficient").val(data.serviceChargeCoefficient);
    						    		$("#incentiveFeeRate").val(data.incentiveFeeRate);
    						    		$("#commissionProportion").val(commissionProportion);
										commissionProportion = commissionProportion*0.01;
    						    		$("#commiss").val(data.commission);
    						    		index = $.layer({
    										type : 1,
    										title : "订单审核",
    										shadeClose : true,
    										border : [0, 0.5, '#666', true],
    								  	    area : ['350px', 'auto'],
    								  	  	move: ['.xubox_title', true],
    								  	  	offset : ['100px',''],
    								       	page :{
    								        	dom : '#orgAuditingDiv'
    								        }
    							  	  	});
    						    	}
    							}
    							$("input[name='adjustSelect'][value='4']").attr("checked",true);
    					}else{
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
    	}else{
			if(payStatus == 1){
				index = $.layer({
					type : 1,
					title : "订单审核",
					shadeClose : true,
					border : [0, 0.5, '#666', true],
			  	    area : ['350px', 'auto'],
			  	  	move: ['.xubox_title', true],
			  	  	offset : ['100px',''],
			       	page :{
			        	dom : '#auditingDiv'
			        }
		  	  	});
	    	}else{
	    		payAmount=data.payAmount;
	    		
	    		issuanceCostRate=data.issuanceCostRate;
	    		timeCoefficient=data.timeCoefficient;
	    		serviceChargeCoefficient=data.serviceChargeCoefficient;
	    		incentiveFeeRate=data.incentiveFeeRate;
	    		commissionProportion=data.commissionProportion;
	    		//commissionProportion=commissionProportion.substring(0,commissionProportion.length-1);
	    		commiss=data.commission;
	    		$("#payAmount").val(data.payAmount);
	    		$("#issuanceCostRate").val(data.issuanceCostRate);
	    		$("#timeCoefficient").val(data.timeCoefficient);
	    		$("#serviceChargeCoefficient").val(data.serviceChargeCoefficient);
	    		$("#incentiveFeeRate").val(data.incentiveFeeRate);
	    		$("#commissionProportion").val(commissionProportion);
				commissionProportion = commissionProportion*0.01;
	    		$("#commiss").val(data.commission);
	    		index = $.layer({
					type : 1,
					title : "订单审核",
					shadeClose : true,
					border : [0, 0.5, '#666', true],
			  	    area : ['350px', 'auto'],
			  	  	move: ['.xubox_title', true],
			  	  	offset : ['100px',''],
			       	page :{
			        	dom : '#orgAuditingDiv'
			        }
		  	  	});
	    	}
		}
    });
  	
    //查询按钮
    $("#select").click(function(){
    	var shoudPayDate;
    	var month=$("#month").val();
    	if(flag==1){
    		shoudPayDate=$("#shoudPayDate").val();
    		if(shoudPayDate!=''&&month==''){
    			top.$.layer({
					shade : [0.5 , '#000' , true],
			  		offset : ['50px',''],
			  	 	title : "请选择月份",
				    dialog : {msg:"请选择月份！",type : 3}	
				});
    		}
    	}else{
    		shoudPayDate=$("#orgShoudPayDate").val();
    		if(shoudPayDate!=''&&month==''){
    			top.$.layer({
					shade : [0.5 , '#000' , true],
			  		offset : ['50px',''],
			  	 	title : "请选择月份",
				    dialog : {msg:"请选择月份！",type : 3}	
				});
    		}
    	}
    	jQuery("#orderList").jqGrid("setGridParam",{datatype:'json',postData:{payStatus:payStatus,flag:flag,month:$("#month").val(),shoudPayDate:shoudPayDate}}).trigger("reloadGrid");
    });
});

/* //一键审核
$("#batchAuditing").live("click",function(){
	var selrow = jQuery("#orderList").jqGrid("getGridParam","selarrrow");
	if(selrow == 0){
		top.$.layer({
			shade : [0.5 , '#000' , true],
	  		offset : ['50px',''],
	  	 	title : "<msg:message code='info.prompt'/>",
		    dialog : {msg:"请选择需要审核的订单！",type : 3}	
		});
		return false;
	}
	if (selrow != null && selrow != ""){
		var url = "${base}${commission_auditing_auditing.controller}.json?c=${commission_auditing_auditing.code}&payStatus=4&ids=" + selrow;
		var index = top.$.layer({
			shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['50px',''],
			dialog   : {
				msg  : "确定审核通过所选的订单么？",
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
								if(jsonData.status == "success"){
									top.layer.close(index);
									top.$.layer({
										shade : [0.5 , '#000' , true],
								  		offset : ['50px',''],
								  	 	title : "<msg:message code='info.prompt'/>",
									    dialog : {msg:"操作成功！",type : 3}	
									});
									$("#orderList").trigger("reloadGrid");
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
	}
}); */

//设置居间费状态
function formatPayStatus(cellval, opts, rowObject){
	var status;
	switch (cellval) {
	case 1:
		status = "未审核";
		break;
	case 4:
		status = "通过";
		break;
	case 5:
		status = "未通过";
		break;
	}
	return status;
}
//设置导出状态
function formatExportStatus(cellval, opts, rowObject){
	var exportStatus; 
	switch (cellval) {
	case '0':
		exportStatus = "未导出";
		break;
	case '1':
		exportStatus = "已导出";
		break;
	}
	return exportStatus;
}
//列表操作功能展示
function actChange(cellvalue, options, rowObject){
	var id=rowObject.id;
	var status = rowObject.payStatus;
	var content="";
	<c:if test="${!empty commission_auditing_detail}">
    <security:authorize ifAnyGranted="${commission_auditing_detail.code}">
    	content += "<a href='#' id='" + id + "' class='shortcut_${commission_auditing_detail.indexCode}' title='${commission_auditing_detail.name}' >";
    	content += "<img src='${base}/${skin}/${commission_auditing_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_auditing_detail.name}'>${commission_auditing_detail.name}</font>";
    	content += "</a>";
    </security:authorize>
	</c:if>
	if(payStatus==1){
		<c:if test="${!empty commission_auditing_auditing}">
	    <security:authorize ifAnyGranted="${commission_auditing_auditing.code}">
	    	content += "<a href='#' id='" + id + "' class='shortcut_${commission_auditing_auditing.indexCode}' title='${commission_auditing_auditing.name}' >";
	    	content += "<img src='${base}/${skin}/${commission_auditing_auditing.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_auditing_auditing.name}'>${commission_auditing_auditing.name}</font>";
	    	content += "</a>";
	    </security:authorize>
		</c:if>
	}else{
		<c:if test="${!empty commission_auditing_auditing}">
	    <security:authorize ifAnyGranted="${commission_auditing_auditing.code}">
	    	content += "<a href='#' id='" + id + "' class='shortcut_${commission_auditing_auditing.indexCode}' title='${commission_auditing_auditing.name}' >";
	    	content += "<img src='${base}/${skin}/${commission_auditing_auditing.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_auditing_auditing.name}'>重新审核</font>";
	    	content += "</a>";
	    </security:authorize>
		</c:if>
	}
	
	<c:if test="${!empty commission_auditing_viewerContract}">
    <security:authorize ifAnyGranted="${commission_auditing_viewerContract.code}">
    	content += "<a href='#' id='" + id + "' class='shortcut_${commission_auditing_viewerContract.indexCode}' title='${commission_auditing_viewerContract.name}' >";
    	content += "<img src='${base}/${skin}/${commission_auditing_viewerContract.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_auditing_viewerContract.name}'>${commission_auditing_viewerContract.name}</font>";
    	content += "</a>";
    </security:authorize>
	</c:if>
	
	return content;
}
/* 
function exportExcel(){
	var sql=$("#sql").val();
	window.location.href='${base}/commission/auditing/export?sql='+sql+'&tradeTypeCollection='+$("#tradeTypeCollection").val();
} */

//查看身份证和网签协议
function detail(tag){ 
	changeTab2(tag);
}

function changeTab2(tag){
	if(tag == 1){
		
		$("#tab_3").addClass("selected");
		$("#tab_4").removeClass("selected");
		$("#div_4").css("display", "none");
		$("#div_3").css("display", "block");
	}else if(tag == 2){
		$("#tab_4").addClass("selected");
		$("#tab_3").removeClass("selected");
		$("#div_3").css("display", "none");
		$("#div_4").css("display", "block");
		
	}
}

//核定是否通过选择
function adjustClick(){
	var status = $("input[name='adjustSelect']:checked").val();
	var msgContent;
	var remarkContent = $("#remark").val();
	if(remarkContent == "" && status == 5){
		top.$.layer({
			shade : [0.5 , '#000' , true],
	  		offset : ['50px',''],
	  	 	title : "<msg:message code='info.prompt'/>",
		    dialog : {msg:"审核不通过需要输入备注信息。",type : 3}	
		});
		return false;
	}
	var url;
	if(payStatus==1){
		url= "${base}${commission_auditing_auditing.controller}.json?c=${commission_auditing_auditing.code}&id="
				+ vouchOrderID+"&flag="+flag + "&payStatus=" + status+"&status="+payStatus+"&auditingRemark="+remarkContent;
	}else{
		if($("#payAmount").val()==''){
			$("#error").text("到账金额为必填项");
			return false;
		}if($("#issuanceCostRate").val()==''){
			$("#error").text("年化发行成本率为必填项");
			return false;
		}if($("#timeCoefficient").val()==''){
			$("#error").text("时间系数为必填项");
			return false;
		}if($("#serviceChargeCoefficient").val()==''){
			$("#error").text("服务费系数为必填项");
			return false;
		}if($("#incentiveFeeRate").val()==''){
			$("#error").text("激励费用率为必填项");
			return false;
		}
		url = "${base}${commission_auditing_auditing.controller}.json?c=${commission_auditing_auditing.code}&id="
				+ vouchOrderID+"&flag="+flag + "&payStatus=4&status="+payStatus+"&issuanceCostRate="+issuanceCostRate*0.01
				+"&timeCoefficient="+timeCoefficient*0.01+"&serviceChargeCoefficient="+serviceChargeCoefficient*0.01+"&incentiveFeeRate="+incentiveFeeRate*0.01
				+"&commissionProportion="+commissionProportion*100+"&commission="+commiss+"&payAmount="+payAmount; 
	}
	layer.close(index);
	$.ajax({
		url:url,
		type:'post',
		timeout:'60000',
		dataType:'json',
		success:function(jsonData,textStatus){
			if(textStatus == "success"){
				if(jsonData.status == "success"){
					top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"操作成功！",type : 1}	
					});
					$("#orderList").trigger("reloadGrid");
				}else{
					//top.layer.close(index);
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
}

$("input[name='adjustSelect']").live("click", function (){
	var val = $(this).val();
	if(val == 1){
		$("#remark").attr("disabled", "disabled");
	}else{
		$("#remark").removeAttr("disabled");
	}
});

//TAB切换
function tab_order(tag){
	if(tag==1){
		$("#tab_1").addClass("selected");
		$("#tab_2").removeClass("selected");
		$("#${commission_auditing_batchAuditing.indexCode}").show();
		payStatus=1;
		if(flag==1){
    		shoudPayDate=$("#shoudPayDate").val();
    	}else{
    		shoudPayDate=$("#orgShoudPayDate").val();
    	}
		jQuery("#orderList").jqGrid("setGridParam",{datatype:'json',postData:{payStatus:payStatus,flag:flag,month:$("#month").val(),shoudPayDate:shoudPayDate}}).trigger("reloadGrid");
	}else if(tag==2){
		payStatus=5;
		if(flag==1){
    		shoudPayDate=$("#shoudPayDate").val();
    	}else{
    		shoudPayDate=$("#orgShoudPayDate").val();
    	}
		$("#tab_2").addClass("selected");
		$("#tab_1").removeClass("selected");
		$("#${commission_auditing_batchAuditing.indexCode}").hide();
		jQuery("#orderList").jqGrid("setGridParam",{datatype:'json',postData:{payStatus:payStatus,flag:flag,month:$("#month").val(),shoudPayDate:shoudPayDate}}).trigger("reloadGrid");
	}
}
//TAB切换
function tab_order_1(tag){
	if(tag==1){
		flag=1;
		$("#tab_1_1").addClass("selected");
		$("#tab_1_2").removeClass("selected");
		$("#orglist").hide();
		$("#orglist1").hide();
		$("#personlist").show();
		$("#personlist1").show();
		jQuery("#orderList").jqGrid("setGridParam",{datatype:'json',postData:{payStatus:payStatus,flag:flag,month:$("#month").val(),shoudPayDate:$("#shoudPayDate").val()},autowidth:true}).jqGrid('hideCol',"orgShortName").trigger("reloadGrid");
		jQuery("#orderList").setGridWidth(gridWidth);
	}else if(tag==2){
		flag=2;
		$("#tab_1_2").addClass("selected");
		$("#tab_1_1").removeClass("selected");
		$("#orglist").show();
		$("#orglist1").show();
		$("#personlist").hide();
		$("#personlist1").hide();
		jQuery("#orderList").jqGrid("setGridParam",{datatype:'json',postData:{payStatus:payStatus,flag:flag,month:$("#month").val(),shoudPayDate:$("#orgShoudPayDate").val()},autowidth:true}).jqGrid('showCol',"orgShortName").trigger("reloadGrid");
		jQuery("#orderList").setGridWidth(gridWidth);
		$("#adjustTr").css("display","none");
	}
}
function change(){
	var re = /^[0-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
	payAmount=$("#payAmount").val();//到账金额
	issuanceCostRate=$("#issuanceCostRate").val(); //年化发行成本率
	timeCoefficient=$("#timeCoefficient").val();    //时间系数
	serviceChargeCoefficient=$("#serviceChargeCoefficient").val(); //服务费系数
	incentiveFeeRate=$("#incentiveFeeRate").val();//激励费用率
	if(!re.test(payAmount)){
		$("#error").text('请输入数字');
		$("#payAmount").val('');
		return false;
	}if(!re.test(issuanceCostRate)){
		$("#error").text('请输入数字');
		$("#issuanceCostRate").val('');
		return false;
	}if(!re.test(timeCoefficient)){
		$("#error").text('请输入数字');
		$("#timeCoefficient").val('');
		return false;
	}if(!re.test(serviceChargeCoefficient)){
		$("#error").text('请输入数字');
		$("#serviceChargeCoefficient").val('');
		return false;
	}if(!re.test(incentiveFeeRate)){
		$("#error").text('请输入数字');
		$("#incentiveFeeRate").val('');
		return false;
	}
	//年化发行成本率 × 服务费系数  × 时间系数 + 激励费用率 = 最终佣金比例
	commissionProportion = 0;
	commissionProportion=issuanceCostRate*timeCoefficient*serviceChargeCoefficient*0.000001+incentiveFeeRate*0.01;
	//console.log(issuanceCostRate+"|"+timeCoefficient+"|"+serviceChargeCoefficient+"|"+incentiveFeeRate+"|"+commissionProportion);
	commiss=payAmount*commissionProportion*10000;
	$("#commissionProportion").val((commissionProportion*100).toFixed(6));
	$("#commiss").val(commiss.toFixed(2));
	
}
</script>

</head>
<body class="skinMain">
<input type="hidden" name="tradeTypeCollection" id="tradeTypeCollection" />
<input type="hidden" name="sql" id="sql" />
	<ul id="tabnav">
		<li class="tab_1" ><a href="#" id="tab_1" onclick="tab_order(1)" class="selected">未审核</a></li>
		<li class="tab_2" ><a href="#" id="tab_2" onclick="tab_order(2)">审核未通过</a></li>
	</ul>
	<ul id="tabnav">
		<li class="tab_1" ><a href="#" id="tab_1_1" onclick="tab_order_1(1)" class="selected">个人</a></li>
		<li class="tab_2" ><a href="#" id="tab_1_2" onclick="tab_order_1(2)">机构</a></li>
	</ul>
	<div id="div_1">
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%" valign="top">
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr id="adjustTr">
							<td align="center">
							<form:form method="post" action="commission" commandName="commission" name="f" onsubmit="return false;">
								<input type="hidden" name="c" value="${commission.c}"/>									
								<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
									<tr>
										<td align="right" width="10%" class="search_info" height="25">选择月份
										<msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="10%"  class="search_lable">
											<form:input path="month" readonly="readonly" onfocus="WdatePicker({dateFmt:'yyyy-MM'});" class="Wdate" />
										</td>
										<td align="right" width="10%" class="search_info" id="personlist">发放时间<msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable" id="personlist1">
											<form:select path="shoudPayDate">
												<form:option value="">全部</form:option>
												<c:forEach items="${shoudPayDateList}" var="it">
													<form:option value="${it.name}">${it.name}</form:option>
												</c:forEach>
											</form:select>										
										</td>
										<td align="right" width="10%" class="search_info" id="orglist" style="display:none">发放时间<msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable" id="orglist1" style="display:none">
											<form:select path="orgShoudPayDate">
												<form:option value="">全部</form:option>
												<c:forEach items="${orgShoudPayDateList}" var="it">
													<form:option value="${it.name}">${it.name}号</form:option>
												</c:forEach>
											</form:select>										
										</td>
										<td width="20%" class="button_area">
											<input type="button" class="btn2" id="select" value="<msg:message code='button.search' />"/>
										</td>
									</tr>
								</table>
							</form:form>
							</td>
						</tr>
						<tr>
							<td>
								<table id="orderList"><tr><td>&nbsp;</td></tr></table>
								<div id="pagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="appendix" style="display: none;width: 350px;">
		<ul id="tabnav">
			<li class="tab_1" ><a href="#" id="tab_3" onclick="detail(1)" class="selected">证件复印件</a></li>
			<li class="tab_2" ><a href="#" id="tab_4" onclick="detail(2)">网签协议</a></li>
		</ul>
		<div id="div_3" align="center" style="width: 350px;">
			<img src="" id="image" width="240" height="150">
		</div>
		<div id="div_4" style="display: none;height: 200px;overflow-y:scroll;">
			<div id=title align="center" style="width: 330px;"></div>
			<div id="content" style="width: 320px;word-break:break-all;margin-left:5px;text-indent: 2em;"></div>
		</div>
	</div>
	<div id="auditingDiv" style="width: 350px; display: none;">
		<table width="100%" border="0" cellspacing="3" cellpadding="0" class="skinMain" align="left">
			<tr>
				<td align="right">是否通过：</td>
				<td colspan='2'>
					<input type='radio' style='width:14px;height:14px;' name='adjustSelect' checked="checked" value='4'>&nbsp;通过
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type='radio' style='width:14px;height:14px;' name='adjustSelect' value='5'>&nbsp;不通过
				</td>
			</tr>
			<tr>
				<td valign='top' align="right">备注：</td>
				<td><textarea id="remark" style='resize: none;' rows='3' cols='30'></textarea></td>
			</tr>
			<tr>
				<td align='center' colspan='2'>
					<input type='button' onclick='adjustClick();' id='adjustSubmit' class='btn2' value='确定'>
				</td>
			</tr>
		</table>
	</div>
	<div id="orgAuditingDiv" style="width: 350px; display: none;">
		<table width="100%" border="0" cellspacing="3" cellpadding="0" class="skinMain" align="left">
			<tr>
				<td colspan="2" align="center" style="background-color:red;color:#FFF;">
					<font id="error"></font>
				</td>
			</tr>
			<tr>
				<td align="right">到帐金额(万元)：</td>
				<td>
					<input type='text' name="payAmount" id="payAmount" onchange="change()"/>
				</td>
			</tr>
			<tr>
				<td valign='top' align="right">年化发行成本率(%)：</td>
				<td>
					<input type='text' name="issuanceCostRate" id="issuanceCostRate" onchange="change()"/>
				</td>
			</tr>
			<tr>
				<td valign='top' align="right">时间系数(%)：</td>
				<td>
					<input type='text' name="timeCoefficient" id="timeCoefficient" onchange="change()"/>
				</td>
			</tr>
			<tr>
				<td valign='top' align="right">服务费系数(%)：</td>
				<td>
					<input type='text' name="serviceChargeCoefficient" id="serviceChargeCoefficient" onchange="change()"/>
				</td>
			</tr>
			<tr>
				<td valign='top' align="right">激励费用率(%)：</td>
				<td>
					<input type='text' name="incentiveFeeRate" id="incentiveFeeRate" onchange="change()"/>
				</td>
			</tr>
			<tr>
				<td valign='top' align="right">居间费比例(%)：</td>
				<td>
					<input type='text' name="commissionProportion" id="commissionProportion" readOnly/>
				</td>
			</tr>
			<tr>
				<td valign='top' align="right">居间费(元)：</td>
				<td>
					<input type='text' name="commiss" id="commiss" readOnly />
				</td>
			</tr>
			<tr>
				<td align='center' colspan='2'>
					<input type='button' onclick='adjustClick();' id='adjustSubmit' class='btn2' value='确定'>
				</td>
			</tr>
		</table>
	</div>
</body>
