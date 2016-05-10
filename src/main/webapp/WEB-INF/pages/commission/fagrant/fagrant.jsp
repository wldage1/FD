<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${base}/common/js/print/jquery.PrintArea.js" type="text/javascript"></script>
<link  href="${base}/${skin}/css/tab.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#orderList").setGridWidth($(window).width()*0.999);　
});
var currTab = 1;
//分组显示内容
var customInfo = new Array();
var ids = new Array();
//不重复加入分组信息标志
var mark,flag=0;
var notFagranted = true;
//需要核定订单的ID和提示信息内容
var vouchOrderID, vouchOrderNumber;
var index;
$(document).ready(function(){
    jQuery("#orderList").jqGrid({
        url: 'fagrant/grid.json',
        datatype: 'json',
        colNames: ["", "", "", "",
       	  		"理财顾问",
       	  		"开户行",
       	  		"账户名",
       	  		"卡号",
       	  		"身份证号",
       	        "预计发放时间",
       	        "到账金额(万元)",
       	        "预计居间费",
       	        "营业税(元)",
       	        "城建税(元)",
       	        "教育费附加(元)",
       	        "地方教育费附加(元)",
       	     	"个人所得税(元)",
       	     	"居间费(元)",
       	     	"<msg:message code='order.operate'/>"
       	        ],
               colModel: [
                {name: 'id',index: 'id',width:'1%',align:'center',hidden: true},
                {name: 'memberID',index: 'memberID',width:'1%',align:'center',hidden: true},
                {name: 'agreementTemplateID',index: 'agreementTemplateID',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'commissionID',index: 'commissionID',width:'1%',align:'center',hidden: true,sortable: false},
                {name: 'memberName',index: 'memberName',width:'7%',align:'center',hidden: false,sortable: false},
                {name: 'bankName',index: 'bankName',width:'4%',align:'center',hidden: false,sortable: false},
                {name: 'accountName',index: 'accountName',width:'3%',align:'center',hidden: false,sortable: false},
                {name: 'accountID',index: 'accountID',width:'8%',align:'center',hidden: false,sortable: false},
                {name: 'iDCard',index: 'iDCard',width:'8%',align:'center',hidden: false,sortable: false},
                {name: 'payDate',index: 'payDate',width:'5%',align:'center',hidden: false,sortable: false},
                {name: 'payAmount',index: 'payAmount',width:'4%',align:'center',hidden: false,sortable: false},
                {name: 'amount',index: 'amount',width:'4%',align:'center',hidden: false,sortable: false},
       			{name: 'salesTax',index: 'salesTax',width:'4%',align:'center',hidden: false,sortable:false},
       			{name: 'constructionTax',index: 'constructionTax',width:'4%',align:'center',hidden: false,sortable:false},
       			{name: 'educationalSurtax',index: 'educationalSurtax',width:'4%',align:'center',hidden: false,sortable:false},
       			{name: 'localEducationalCost',index: 'localEducationalCost',width:'4%',align:'center',hidden: false,sortable:false},
       			{name: 'personalIncomeTax',index: 'personalIncomeTax',width:'4%',align:'center',hidden: false,sortable:false},
				{name: 'fagranted',index: 'fagranted',width:'4%',align:'center',hidden: true,sortable:false,formatter: setFagranted},
       			{name: 'act',index:'act', width:'15%',align:'center',hidden: false,sortable:false,formatter: setGroupContent}
       			],
        mtype:"POST",
        postData:{c:'303', statusStr: "(0)"},
        height:	'auto',
        autowidth: true,
        jsonReader: {
        	repeatitems: false
        },
        subGrid : true,
        subGridRowExpanded: function(subgrid_id, row_id) {
        	var data = jQuery("#orderList").jqGrid("getRowData",row_id);
        	var id = data.id;
	        var subgrid_table_id, pager_id;
			subgrid_table_id = subgrid_id+"_t";
			pager_id = "p_"+subgrid_table_id;
			$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
			jQuery("#"+subgrid_table_id).jqGrid({
				url:"fagrant/subGrid.json",
				datatype: "json",
				colNames: [
				           '<msg:message code='order.orderNumber'/>',
	                       '<msg:message code='order.clientName'/>',
	                       '<msg:message code='order.commissionPercent'/>',
	                       '<msg:message code='order.payAmount'/>',
	                       '<msg:message code='order.commission'/>',
	                       '审核时间',
	                       "发放时间",
	                       '<msg:message code='order.operate'/>'],
				colModel: [
					{name:"orderNumber",index:"orderNumber",width:80,align:"center",sortable:false},
					{name:"clientName",index:"clientName",width:60,align:"center",sortable:false},
					{name:"commissionProportionStr",index:"commissionProportionStr",width:70,align:"center",sortable:false},
					{name:"payAmount",index:"payAmount",width:70,align:"center",sortable:false},
					{name:"commission",index:"commission",width:70,align:"center",sortable:false},
					{name:"auditingTime",index:"auditingTime",width:60,align:"center",sortable:false},
					{name:"payTime",index:"payTime",width:60,align:"center",hidden: true,sortable:false},
					{name:"act",index:"act",width:70,align:"center",sortable:false,formatter: actChange},
				],
				postData:{id: id},
			   	autowidth: true,
			    height: '100%',
			    multiselect: false,
		        jsonReader: {
		        	repeatitems: false,
		        }
			});
        },
        caption:'待发放居间费列表',
        toolbar: [true,"top"]
    });
	$("#jqgh_orderList_commission.commissionProportion").attr("title","年化发行成本率 × 佣金系数  × 时间系数 + 激励费用率 = 最终佣金比例");
    
    jQuery("#orderList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
  	
    $("#search_table_2").hide();
    $("#search_table_3").hide();
    $("#div_2").css("display", "none");
  	
    <c:if test="${!empty commission_fagrant_export}">
	<security:authorize ifAnyGranted="${commission_fagrant_export.code}">
		var $ea = $("<a></a>").attr("href","javascript:void(0)")
		 .attr("id","${commission_fagrant_export.indexCode}")
		 .append($("<img/>").attr("src","../common/skin/${commission_auditing_export.icon}")
		 .attr("width","18").attr("height","18").attr("border","0")
		 .attr("border","0").attr("align","absmiddle"))
		 .append("${commission_fagrant_export.name}");
		$("#t_orderList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));
		$("#${commission_fagrant_export.indexCode}","#t_orderList").click(function(){
			window.location.href='${base}/commission/fagrant/export?c=303&status='+flag+'&memberName='+$("#memberName").val()
					+'&payDateStart='+$("#payDateStart").val()+'&payDateEnd='+$("#payDateEnd").val()+'&payTimeStart='+$("#payTimeStart").val()
					+'&payTimeEnd='+$("#payTimeEnd").val()+'&taxAssessmentTime='+$("#taxAssessmentTime").val();
		});
	 </security:authorize>
	 </c:if>
    
  	//查看
    $(".shortcut_${commission_fagrant_detail.indexCode}").live("click",function(){
    	window.location.href='${base}${commission_fagrant_detail.controller}?c=${commission_fagrant_detail.code}&tab='+currTab+'&id='+this.id;
    });
  	
	 //查询按钮
    $(".btn2").click(function(){
    	jQuery("#orderList").jqGrid("setGridParam",{postData:{memberName:$("#memberName").val()
    												, payDateStart:$("#payDateStart").val()
    												, payDateEnd:$("#payDateEnd").val()
    												, payTimeStart:$("#payTimeStart").val()
    												, payTimeEnd:$("#payTimeEnd").val()
    												, taxAssessmentTime:$("#taxAssessmentTime").val()}}).trigger("reloadGrid");    
    });
	tab_order('${tab}');
});

//列表操作功能展示
function actChange(cellvalue, options, rowObject){
	var commissionID = rowObject.commissionID;
	var content="";
	<c:if test="${!empty commission_fagrant_detail}">
    <security:authorize ifAnyGranted="${commission_fagrant_detail.code}">
    	content += "<a href='#' id='" + commissionID + "' class='shortcut_${commission_fagrant_detail.indexCode}' title='${commission_fagrant_detail.name}' >";
    	content += "<img src='${base}/${skin}/${commission_fagrant_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_fagrant_detail.name}'>${commission_fagrant_detail.name}</font>";
    	content += "</a>";
    </security:authorize>
	</c:if>
	return content;
}

// 设置实际发放居间费
function setFagranted(cellval, opts, rowObject){
	if(notFagranted == false){
		var amount = rowObject.amount;
		var salesTax = rowObject.salesTax;
		var constructionTax = rowObject.constructionTax;
		var educationalSurtax = rowObject.educationalSurtax;
		var localEducationalCost = rowObject.localEducationalCost;
		var personalIncomeTax = rowObject.personalIncomeTax;
		return amount-salesTax-constructionTax-educationalSurtax-localEducationalCost-personalIncomeTax;
	}
}

//设置分组头内容
function setGroupContent(cellval, opts, rowObject){
	var id = rowObject.id;
	var memberID = rowObject.memberID;
	var status = rowObject.status;
	var agreementTemplateID = rowObject.agreementTemplateID;
	var content="";
	if(status == 0){
		<c:if test="${!empty commission_fagrant_preparePay}">
	    <security:authorize ifAnyGranted="${commission_fagrant_preparePay.code}">
		    content += "<a href='#' id='" + id + "' memberID='" + memberID + "' class='shortcut_${commission_fagrant_preparePay.indexCode}' title='${commission_fagrant_preparePay.name}' >";
	    	content += "<img src='${base}/${skin}/${commission_fagrant_preparePay.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_fagrant_preparePay.name}'>${commission_fagrant_preparePay.name}</font>";
	    	content += "</a>";
	    </security:authorize>
		</c:if>
	}
	if(status == 3 || status == 4){
		<c:if test="${!empty commission_fagrant_paySuccess}">
	    <security:authorize ifAnyGranted="${commission_fagrant_paySuccess.code}">
		    content += "<a href='#' id='" + id + "' memberID='" + memberID + "' class='shortcut_${commission_fagrant_paySuccess.indexCode}' title='${commission_fagrant_paySuccess.name}' >";
	    	content += "<img src='${base}/${skin}/${commission_fagrant_paySuccess.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_fagrant_paySuccess.name}'>${commission_fagrant_paySuccess.name}</font>";
	    	content += "</a>";
	    </security:authorize>
		</c:if>
		
		<c:if test="${!empty commission_fagrant_payFail}">
	    <security:authorize ifAnyGranted="${commission_fagrant_payFail.code}">
			content += "<a href='#' id='" + id + "' memberID='" + memberID + "' class='shortcut_${commission_fagrant_payFail.indexCode}' title='${commission_fagrant_payFail.name}' >";
			content += "<img src='${base}/${skin}/${commission_fagrant_payFail.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_fagrant_payFail.name}'>${commission_fagrant_payFail.name}</font>";
			content += "</a>";
	    </security:authorize>
		</c:if>
	}
	
	<c:if test="${!empty commission_fagrant_certificate}">
    <security:authorize ifAnyGranted="${commission_fagrant_certificate.code}">
	    content += "<a href='#' memberID='" + memberID + "' class='shortcut_${commission_fagrant_certificate.indexCode}' title='${commission_fagrant_certificate.name}' >";
		content += "<img src='${base}/${skin}/${commission_fagrant_certificate.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_fagrant_certificate.name}'>${commission_fagrant_certificate.name}</font>";
		content += "</a>";
    </security:authorize>
	</c:if>
	
	<c:if test="${!empty commission_fagrant_agreement}">
    <security:authorize ifAnyGranted="${commission_fagrant_agreement.code}">
	    content += "<a href='#' agreementTemplateID='" + agreementTemplateID + "' class='shortcut_${commission_fagrant_agreement.indexCode}' title='${commission_fagrant_agreement.name}' >";
		content += "<img src='${base}/${skin}/${commission_fagrant_agreement.icon}' weight='18' height='18' border='0' align='absmiddle'/><font color='#0000FF' title='${commission_fagrant_agreement.name}'>${commission_fagrant_agreement.name}</font>";
		content += "</a>";
    </security:authorize>
	</c:if>
	
	return content;
}

//准备发放
$(".shortcut_commission_fagrant_preparePay").live("click",function(){
	var id = $(this).attr("id");
	var memberID = $(this).attr("memberID");
	var url = "${base}${commission_fagrant_paySuccess.controller}.json?c=${commission_fagrant_preparePay.code}&status=4&id="+id+"&memberID=" + memberID;
	var index = top.$.layer({
		shade : [0.5 , '#000' , true],
  	    area : ['auto','auto'],
  	  	offset : ['50px',''],
		dialog   : {
			msg  : "确定准备发放该理财顾问的居间费吗？",
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
								    dialog : {msg:"该理财顾问还有订单没有处理，不能发放！",type : 3}	
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
});

//发放成功
$(".shortcut_commission_fagrant_paySuccess").live("click",function(){
	var id = $(this).attr("id");
	var memberID = $(this).attr("memberID");
	var url = "${base}${commission_fagrant_paySuccess.controller}.json?c=${commission_fagrant_paySuccess.code}&status=1&id="+id+"&memberID=" + memberID;
	var index = top.$.layer({
		shade : [0.5 , '#000' , true],
  	    area : ['auto','auto'],
  	  	offset : ['50px',''],
		dialog   : {
			msg  : "确定发放成功该理财顾问的居间费吗？",
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
$(".shortcut_commission_fagrant_payFail").live("click",function(){
	var id = $(this).attr("id");
	var memberID = $(this).attr("memberID");
	var url = "${base}${commission_fagrant_paySuccess.controller}.json?c=${commission_fagrant_paySuccess.code}&status=3&id="+ id+"&memberID=" + memberID;
	var index = top.$.layer({
		shade : [0.5 , '#000' , true],
  	    area : ['auto','auto'],
  	  	offset : ['50px',''],
		dialog   : {
			msg  : "确定发放失败该理财顾问的居间费吗？",
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
					}
				});
			},
			no   : function(){
				top.layer.close(index);
			}
		}
	});
});

//查看身份证
$(".shortcut_commission_fagrant_certificate").live("click",function(){
	var memberID = $(this).attr("memberID");
	var url = "${base}${commission_fagrant_certificate.controller}.json?c=${commission_fagrant_certificate.code}&memberID=" + memberID;
	$.ajax({
		url:url,
		type:'post',
		timeout:'60000',
		dataType:'json',
		success:function(jsonData,textStatus){
			if(textStatus == "success"){
				if(jsonData.status == "success"){
					var data = jsonData.contract;
					$("#image").attr("src", data.contractCreateTime + data.contractStopTime);
					index = $.layer({
						type : 1,
						title : "查看证件",
						shadeClose : true,
						border : [0, 0.5, '#666', true],
				  	    area : ['350px', 'auto'],
				  	  	move: ['.xubox_title', true],
				  	  	offset : ['100px',''],
				       	page :{
				        	dom : '#showCertificate'
				        }
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
		}
	});
});

//查看网签协议
$(".shortcut_commission_fagrant_agreement").live("click",function(){
	var agreementTemplateID = $(this).attr("agreementTemplateID");
	var url = "${base}${commission_fagrant_agreement.controller}.json?c=${commission_fagrant_agreement.code}&agreementTemplateID="
			+ agreementTemplateID;
	$.ajax({
		url:url,
		type:'post',
		timeout:'60000',
		dataType:'json',
		success:function(jsonData,textStatus){
			if(textStatus == "success"){
				if(jsonData.status == "success"){
					var data = jsonData.contract;
					$("#title").text(data.title);
					$("#content").html(data.content);
					index = $.layer({
						type : 1,
						border : [0, 0.5, '#666', true],
				  	    area : ['350px', 'auto'],
				  	  	move: ['.xubox_title', true],
				  	  	offset : ['100px',''],
				       	page :{
				        	dom : '#showAgreement'
				        }
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
		}
	});
});

//TAB切换
function tab_order(tag){
	currTab = tag;
	clearSearchVal();
	changeTab(tag);
	notFagranted = true;
	if(tag==1){
		$("#t_orderList").show();
		jQuery("#orderList").jqGrid("setGridParam",{postData:{c:"303", statusStr: "(0)",mark:0}})
			.setCaption('待发放居间费列表').jqGrid('hideCol',"fagranted").jqGrid('showCol',"auditingTime").jqGrid('showCol',"payTime").trigger("reloadGrid");
		flag=0;
	}else if(tag==2){
		$("#t_orderList").hide();
		jQuery("#orderList").jqGrid("setGridParam",{postData:{c:"303", statusStr: "(4)",mark:0}})
			.setCaption('居间费发放中').jqGrid('hideCol',"fagranted").jqGrid('showCol',"auditingTime").jqGrid('showCol',"payTime").trigger("reloadGrid");
		flag=4;
	}else if(tag==3){
		notFagranted = false;
		$("#t_orderList").show();
		jQuery("#orderList").jqGrid("setGridParam",{postData:{c:"303", statusStr: "(1,2)",mark:0}})
		.setCaption('已发放居间费').jqGrid('showCol',"fagranted").jqGrid('hideCol',"auditingTime").jqGrid('showCol',"payTime").trigger("reloadGrid");
		flag=1;
	}else if(tag==4){
		$("#t_orderList").show();
		jQuery("#orderList").jqGrid("setGridParam",{postData:{c:"303", statusStr: "(3)",mark:0}})
		.setCaption('居间费发放失败').jqGrid('hideCol',"fagranted").jqGrid('showCol',"auditingTime").jqGrid('showCol',"payTime").trigger("reloadGrid");
		flag=3;
	}else if(tag==5){
		$("#t_orderList").show();
		jQuery("#orderList").jqGrid("setGridParam",{postData:{c:"303", statusStr: "(1,2)",mark:5}})
		.setCaption('居间费计税').jqGrid('hideCol',"fagranted").jqGrid('hideCol',"auditingTime").jqGrid('showCol',"payTime").trigger("reloadGrid");
		flag=1;
	}
}

function changeTab(tag){
	if(tag == 1){
		//隐藏查询条件
		$("#search_table_1").show();
		$("#search_table_2").hide();
		$("#search_table_3").hide();
		$("#tab_1").addClass("selected");
		//去掉其他tab页选中状态
		$("[id^='tab_'][id!='tab_1']").removeClass("selected");
	}else if(tag == 2){
		$("#search_table_1").show();
		$("#search_table_2").hide();
		$("#search_table_3").hide();
		$("#tab_2").addClass("selected");
		//去掉其他tab页选中状态
		$("[id^='tab_'][id!='tab_2']").removeClass("selected");
	}
	else if(tag == 3){
		$("#search_table_2").show();
		$("#search_table_1").hide();
		$("#search_table_3").hide();
		$("#tab_3").addClass("selected");
		//去掉其他tab页选中状态
		$("[id^='tab_'][id!='tab_3']").removeClass("selected");
	}else if(tag == 4){
		$("#search_table_1").show();
		$("#search_table_2").hide();
		$("#search_table_3").hide();
		$("#tab_4").addClass("selected");
		//去掉其他tab页选中状态
		$("[id^='tab_'][id!='tab_4']").removeClass("selected");
	}else if(tag == 5){
		$("#search_table_3").show();
		$("#search_table_2").hide();
		$("#search_table_1").hide();
		$("#tab_5").addClass("selected");
		//去掉其他tab页选中状态
		$("[id^='tab_'][id!='tab_5']").removeClass("selected");
	}
}

function printCertificate(){
	$("#certificateDiv").printArea({mode:"popup"});
}

function clearSearchVal(){
	$("#memberName").val("");
	$("#payDateStart").val("");
	$("#payDateEnd").val("");
	$("#payTimeStart").val("");
	$("#payTimeEnd").val("");
	$("#taxAssessmentTime").val("");
}

</script>

</head>
<body class="skinMain">
	<ul id="tabnav">
		<li class="tab_1" ><a href="#" id="tab_1" onclick="tab_order(1)" class="selected">待发放居间费</a></li>
		<li class="tab_2" ><a href="#" id="tab_2" onclick="tab_order(2)">居间费发放中</a></li>
		<li class="tab_3" ><a href="#" id="tab_3" onclick="tab_order(3)">已发放居间费</a></li>
		<li class="tab_4" ><a href="#" id="tab_4" onclick="tab_order(4)">居间费失败</a></li>
		<li class="tab_5" ><a href="#" id="tab_5" onclick="tab_order(5)">居间费计税</a></li>
	</ul>
	<div id="div_1">
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
		      <td>
		      <form:form method="post" action="#"  commandName="commission" >
		        <table width="100%" border="0"  cellspacing="1" cellpadding="0" class="search" id="search_table_1">
					<tr>
						<td width="10%" align="right" class="search_info">计划发放时间
							<msg:message code="system.common.sign.colon"/></td>
						<td align="left" width="30%"  class="search_lable" colspan="3">
							<input name="payDateStart" id="payDateStart" 
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'payDateEnd\')||\'2080-10-01\'}'})"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'payDateEnd\')||\'2080-10-01\'}'})"
								style="width:100px;" readonly="readonly" class="Wdate"/>
							--
							<input name="payDateEnd" id="payDateEnd"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'payDateStart\')}',maxDate:'2080-10-01'})"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'payDateStart\')}',maxDate:'2080-10-01'})"
								style="width:100px;" readonly="readonly" class="Wdate"/>
						</td>
						<td  width="20%" class="button_area" align="center" rowspan="2">
							<input type="button" class="btn2" id="select" value="<msg:message code='button.search'/>">&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</table>
				<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="search" id="search_table_2">
					<tr>
						<td width="10%" align="right" class="search_info">理财顾问姓名
							<msg:message code="system.common.sign.colon"/></td>
						<td width="30%" class="search_lable">
							<form:input path="memberName" />
						</td>
						<td width="10%" align="right" class="search_info">发放时间
							<msg:message code="system.common.sign.colon"/></td>
						<td align="left" width="30%"  class="search_lable" colspan="3">
							<input name="payTimeStart" id="payTimeStart" 
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'payTimeEnd\')||\'2080-10-01\'}'})"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'payTimeEnd\')||\'2080-10-01\'}'})"
								style="width:100px;" readonly="readonly" class="Wdate"/>
							--
							<input name="payTimeEnd" id="payTimeEnd"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'payTimeStart\')}',maxDate:'2080-10-01'})"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'payTimeStart\')}',maxDate:'2080-10-01'})"
								style="width:100px;" readonly="readonly" class="Wdate"/>
						</td>
						<td  width="20%" class="button_area" align="center" rowspan="2">
							<input type="button" class="btn2" id="select" value="<msg:message code='button.search'/>">&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</table>
				<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="search" id="search_table_3">
					<tr>
						<td width="10%" align="right" class="search_info">计税月份
							<msg:message code="system.common.sign.colon"/></td>
						<td align="left" width="30%"  class="search_lable" colspan="3">
							<input name="taxAssessmentTime" id="taxAssessmentTime" 
								onfocus="WdatePicker({dateFmt:'yyyy-MM',maxDate:'%y-{%M-1}'});"
								style="width:100px;" readonly="readonly" class="Wdate"/>
						</td>
						<td  width="20%" class="button_area" align="center" rowspan="2">
							<input type="button" class="btn2" id="select" value="<msg:message code='button.search'/>">&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</table>
				</form:form>
		      </td>
		    </tr>
			<tr>
				<td width="100%" valign="top">
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
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
	
	<div id="showCertificate" style="display:none;height: 170px;text-align:center;width: 350px;">
			<div id="certificateDiv" style="padding-top: 8px;align:center">
				<img src="" id="image" width="200" height="120">
			</div>
			<div align="center" style="margin-top: 8px;margin-bottom: 8px;"> 
				<input type='button' onclick='printCertificate();' id='adjustSubmit' class='btn2' value='打印'>
			</div>
	</div>
	<div id="showAgreement" style="display: none;height: 240px;OVERFLOW-Y: auto; OVERFLOW-X:hidden;">
		<div id="title" align="center" style="width: 330px;height: auto;"></div>
		<div id="content" style="width: 330px;word-break:break-all;margin-left:5px;text-indent: 2em;"></div>
	</div>
	</body>
