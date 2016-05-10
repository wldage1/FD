<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
<script type="text/javascript">
	$(window).resize(function(){ 　　
		$("#docAdvanceList").setGridWidth($(window).width()*0.999);　
	});
	$(window).resize(function(){ 　　
		$("#docList").setGridWidth($(window).width()*0.999);　
	});
	
	//弹出层标识
	var k;
	//合同类型：1-预用印，2-非预用印
	var type = 1;
	//1:在途订单表 2:历史订单表
	var tableType = 1;
	var url = '${base}/market/docmanage/grid.json';
	//当前页iframeID
	var iframeID = window.frameElement.id;
	
	$(window).resize(function(){ 　　
		$("#docAdvanceList").setGridWidth($(window).width()*0.999);
		$("#docList").setGridWidth($(window).width()*0.999);
	});
	
	$(document).ready(function(){
		//预用印单证查询
		jQuery("#docAdvanceList").jqGrid({
			url: url,
			datatype: 'json',
			colNames: ["<msg:message code='doc.contractNumber'/>",
			           "",
			           "",
			           "<msg:message code='doc.productShortName'/>",
			           "<msg:message code='doc.providerShortName'/>",
			           "<msg:message code='doc.orderNumber'/>",
			           "<msg:message code='doc.clientName'/>",
			           "<msg:message code='doc.investAmount'/>",
			           "",
			           "<msg:message code='doc.member'/>",
			           "<msg:message code='doc.docStatus'/>",
			           "<msg:message code='doc.isPlatformSentUsing'/>",
			           "<msg:message code='doc.isNetReceivedUsing'/>",
			           "<msg:message code='doc.isNetSentSignUsing'/>",
			           "<msg:message code='doc.isPlatformReceivedSignUsing'/>"],
			colModel: [{name:'contractNumber', index:'d.contractNumber', align:'center', hidden:false, width:'8%', sortable:false},
					   {name:'productId', index:'d.productId', align:'center', hidden:true, sortable:false},
					   {name:'contractPrefix', index:'d.contractPrefix', align:'center', hidden:true, sortable:false},
					   {name:'productShortName', index:'d.productShortName', align:'center', hidden:false, width:'8%', sortable:false},
					   {name:'providerShortName', index:'d.providerShortName', align:'center', hidden:true, sortable:false},
					   {name:'orderNumber', index:'d.orderNumber', align:'center', hidden:false, width:'8%', sortable:false},
					   {name:'clientName', index:'d.clientName', align:'center', hidden:false, width:'8%', sortable:false},
					   {name:'investAmount', index:'d.investAmount', align:'center', hidden:true, sortable:false},
					   {name:'memberId', index:'d.memberId', align:'center', hidden:true, sortable:false},
					   {name:'memberName', index:'d.memberName', align:'center', hidden:false, width:'8%', sortable:false,formatter:function(cellvalue, options, rowObject){
		       				return cellvalue+"("+rowObject.memberNickName+")";
		       			}},
					   {name:'docStatus', index:'d.docStatus', align:'center', hidden:true, sortable:false},
					   {name:'isPlatformSentUsing', index:'d.isPlatformSentUsing', align:'center', hidden:false, width:'15%', sortable:false,formatter:function(cellvalue, options, rowObject){
		       				if(cellvalue==null||cellvalue==0){
								return "<msg:message code='doc.status.no'/>&nbsp;&nbsp;<input type='button' value='<msg:message code='doc.seal.send'/>' onclick='send("+rowObject.id+","+'"platformSentUsingTime"'+")'>";
		       				}else if(cellvalue==1){
		       					if(rowObject.docStatus!=1){
									return "<msg:message code='doc.status.yes'/>&nbsp;&nbsp;<input type='button' id='"+rowObject.id+"' class='printContent' value='<msg:message code='doc.cover.print'/>' />";
		       					}else{
		       						return "<msg:message code='doc.status.yes'/>";
		       					}
		       				}
		       			}},
					   {name:'isNetReceivedUsing', index:'d.isNetReceivedUsing', align:'center', hidden:false, width:'15%', sortable:false,formatter:function(cellvalue, options, rowObject){
		       				if(cellvalue==null||cellvalue==0){
								return "<msg:message code='doc.status.no'/>";
		       				}else if(cellvalue==1){
								return "<msg:message code='doc.status.yes'/>";
		       				}
		       			}},
					   {name:'isNetSentSignUsing', index:'d.isNetSentSignUsing', align:'center', hidden:false, width:'15%', sortable:false,formatter:function(cellvalue, options, rowObject){
		       				if(cellvalue==null||cellvalue==0){
								return "<msg:message code='doc.status.no'/>";
		       				}else if(cellvalue==1){
								return "<msg:message code='doc.status.yes'/>";
		       				}
		       			}},
					   {name:'isPlatformReceivedSignUsing', index:'d.isPlatformReceivedSignUsing', align:'center', hidden:false, width:'15%', sortable:false,formatter:function(cellvalue, options, rowObject){
		       				if(cellvalue==null||cellvalue==0){
		       					if(rowObject.isNetSentSignUsing==1){
									return "<msg:message code='doc.status.no'/>&nbsp;&nbsp;<input type='button' value='<msg:message code='doc.sign.seal.receive'/>' onclick='receive("+rowObject.id+","+'"platformReceivedSignUsingTime"'+")'>";
		       					}else{
									return "<msg:message code='doc.status.no'/>";
		       					}
		       				}else if(cellvalue==1){
		       					if(rowObject.docStatus==1){
									return "<msg:message code='doc.status.yes'/>&nbsp;&nbsp;<input type='button' value='<msg:message code='doc.att.upload'/>' onclick='upload("+rowObject.id+")'";
		       					}else{
		       						return "<msg:message code='doc.status.yes'/>";
		       					}
		       				}
		       			}}],
			mtype: "POST",
			postData: {c:"${document.c}",type:1,docStatus:0},
			rowNum: 10,
			rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#docPagered',
			height: '250',
			autowidth: true,
			viewrecords: true,
			rownumbers: true,
			jsonReader: {
				repeatitems: false
			},
			caption: "<msg:message code='doc.advance.list'/>"
		});
		//预用印单证查询按钮
		$("#selectAdvance").click(function(){
			jQuery("#docAdvanceList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:1,docStatus:$("#docStatusAdvance").val(),productShortName:$("#productShortNameAdvance").val(),clientName:$("#clientNameAdvance").val(),orderNumber:$("#orderNumberAdvance").val(),contractNumber:$("#contractNumberAdvance").val()}}).trigger("reloadGrid");
	    });
		//非预用印单证查询
		jQuery("#docList").jqGrid({
			url: url,
			datatype: 'json',
			colNames: ["<msg:message code='doc.contractNumber'/>",
			           "",
			           "<msg:message code='doc.productShortName'/>",
			           "<msg:message code='doc.providerShortName'/>",
			           "<msg:message code='doc.orderNumber'/>",
			           "<msg:message code='doc.clientName'/>",
			           "<msg:message code='doc.investAmount'/>",
			           "",
			           "<msg:message code='doc.member'/>",
			           "<msg:message code='doc.docStatus'/>",
			           "<msg:message code='doc.isPlatformSentNotusing'/>",
			           "<msg:message code='doc.isNetReceivedNotusing'/>",
			           "<msg:message code='doc.isNetSentSignNotusing'/>",
			           "<msg:message code='doc.isPlatformReceivedNotusing'/>",
			           "<msg:message code='doc.isPlatformSentSignUsing'/>",
			           "<msg:message code='doc.isNetreceivedSignUsing'/>",
			           ""],
			colModel: [{name:'contractNumber', index:'d.contractNumber', align:'center', hidden:false, width:'6%', sortable:false},
			           {name:'productId', index:'d.productId', align:'center', hidden:true, sortable:false},
					   {name:'productShortName', index:'d.productShortName', align:'center', hidden:false, width:'6%', sortable:false},
					   {name:'providerShortName', index:'d.providerShortName', align:'center', hidden:true, sortable:false},
					   {name:'orderNumber', index:'d.orderNumber', align:'center', hidden:false, width:'6%', sortable:false},
					   {name:'clientName', index:'d.clientName', align:'center', hidden:false, width:'5%', sortable:false},
					   {name:'investAmount', index:'d.investAmount', align:'center', hidden:true, sortable:false},
					   {name:'memberId', index:'d.memberId', align:'center', hidden:true, sortable:false},
					   {name:'memberName', index:'d.memberName', align:'center', hidden:false, width:'5%', sortable:false,formatter:function(cellvalue, options, rowObject){
		       				return cellvalue+"("+rowObject.memberNickName+")";
		       			}},
		       		   {name:'docStatus', index:'d.docStatus', align:'center', hidden:true, sortable:false},
					   {name:'isPlatformSentNotusing', index:'d.isPlatformSentNotusing', align:'center', hidden:false, width:'12%', sortable:false,formatter:function(cellvalue, options, rowObject){
						   if(cellvalue==null||cellvalue==0){
								return "<msg:message code='doc.status.no'/>&nbsp;&nbsp;<input type='button' value='<msg:message code='doc.send'/>' onclick='send("+rowObject.id+","+'"platformSentNotusingTime"'+")'>";
		       				}else if(cellvalue==1){
		       					if(rowObject.docStatus!=1){
									return "<msg:message code='doc.status.yes'/>&nbsp;&nbsp;<input type='button' id='"+rowObject.id+"' value='<msg:message code='doc.cover.print'/>' class='printContent'/>";
		       					}else{
		       						return "<msg:message code='doc.status.yes'/>";
		       					}
		       				}
		       			}},
					   {name:'isNetReceivedNotusing', index:'d.isNetReceivedNotusing', align:'center', hidden:false, width:'12%', sortable:false,formatter:function(cellvalue, options, rowObject){
		       				if(cellvalue==null||cellvalue==0){
								return "<msg:message code='doc.status.no'/>";
		       				}else if(cellvalue==1){
								return "<msg:message code='doc.status.yes'/>";
		       				}
		       			}},
					   {name:'isNetSentSignNotusing', index:'d.isNetSentSignNotusing', align:'center', hidden:false, width:'12%', sortable:false,formatter:function(cellvalue, options, rowObject){
		       				if(cellvalue==null||cellvalue==0){
								return "<msg:message code='doc.status.no'/>";
		       				}else if(cellvalue==1){
								return "<msg:message code='doc.status.yes'/>";
		       				}
		       			}},
					   {name:'isPlatformReceivedNotusing', index:'d.isPlatformReceivedNotusing', align:'center', hidden:false, width:'12%', sortable:false,formatter:function(cellvalue, options, rowObject){
		       				if(cellvalue==null||cellvalue==0){
		       					if(rowObject.isNetSentSignNotusing==1){
									return "<msg:message code='doc.status.no'/>&nbsp;&nbsp;<input type='button' value='<msg:message code='doc.sign.receive'/>' onclick='receive("+rowObject.id+","+'"platformReceivedNotusingTime"'+")'>";
		       					}else{
									return "<msg:message code='doc.status.no'/>";
		       					}
		       				}else if(cellvalue==1){
								return "<msg:message code='doc.status.yes'/>";
		       				}
		       			}},
					   {name:'isPlatformSentSignUsing', index:'d.isPlatformSentSignUsing', align:'center', hidden:false, width:'12%', sortable:false,formatter:function(cellvalue, options, rowObject){
						   if(cellvalue==null||cellvalue==0){
		       					if(rowObject.isPlatformReceivedNotusing==1){
									return "<msg:message code='doc.status.no'/>&nbsp;&nbsp;<input type='button' value='<msg:message code='doc.sign.seal.send'/>' onclick='sendSign("+rowObject.id+","+'"platformSentSignUsingTime"'+")'>";
		       					}else{
									return "<msg:message code='doc.status.no'/>";
		       					}
		       				}else if(cellvalue==1){
								return "<msg:message code='doc.status.yes'/>";
		       				}
		       			}},
					   {name:'isNetreceivedSignUsing', index:'d.isNetreceivedSignUsing', align:'center', hidden:false, width:'12%', sortable:false,formatter:function(cellvalue, options, rowObject){
		       				if(cellvalue==null||cellvalue==0){
								return "<msg:message code='doc.status.no'/>";
		       				}else if(cellvalue==1){
		       					if(rowObject.docStatus==1){
									return "<msg:message code='doc.status.yes'/>&nbsp;&nbsp;<input type='button' value='<msg:message code='doc.att.upload'/>' onclick='upload("+rowObject.id+")'";
		       					}else{
		       						return "<msg:message code='doc.status.yes'/>";
		       					}
		       				}
		       			}},
		       			{name:'contractPrefix', index:'d.contractPrefix', align:'center', hidden:true, sortable:false}],
			mtype: "POST",
			postData: {c:"${document.c}",type:2,docStatus:0},
			rowNum: 10,
			rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#docAdvancePagered',
			height: '250',
			autowidth: true,
			viewrecords: true,
			rownumbers: true,
			jsonReader: {
				repeatitems: false
			},
			caption: "<msg:message code='doc.list'/>"
		});
		//非预用印单证查询按钮
		$("#select").click(function(){
			jQuery("#docList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:2,docStatus:$("#docStatus").val(),productShortName:$("#productShortName").val(),clientName:$("#clientName").val(),orderNumber:$("#orderNumber").val(),contractNumber:$("#contractNumber").val()}}).trigger("reloadGrid");
	    });
		
	   $(".printContent").live("click",function(){
	    	var rowid = $(this).attr("id");
	    	var data;
	    	if(type == 1){
	    		data = jQuery("#docAdvanceList").jqGrid("getRowData",rowid);
	    	}else{
	    		data = jQuery("#docList").jqGrid("getRowData",rowid);
	    	}
	    	window.open('${base}/market/docmanage/printview?c=${market_docmanage_printview.code}&orderNumber='+data.orderNumber+'&tableType='+tableType);
	    });
	});
	
	//重载jqGrid
	function reloadList(contractType){
		if(contractType==1){
			jQuery("#docAdvanceList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:1,docStatus:$("#docStatusAdvance").val(),productShortName:$("#productShortNameAdvance").val(),clientName:$("#clientNameAdvance").val(),orderNumber:$("#orderNumberAdvance").val(),contractNumber:$("#contractNumberAdvance").val()}}).trigger("reloadGrid");
		}else if(contractType==2){
			jQuery("#docList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:2,docStatus:$("#docStatus").val(),productShortName:$("#productShortName").val(),clientName:$("#clientName").val(),orderNumber:$("#orderNumber").val(),contractNumber:$("#contractNumber").val()}}).trigger("reloadGrid");
		}
	}
	
	//寄出用印单证
	function send(rowid,name){
		var rowdata;
		if(type==1){
			rowdata = jQuery("#docAdvanceList").jqGrid('getRowData',rowid);
		}else if(type==2){
			rowdata = jQuery("#docList").jqGrid('getRowData',rowid);
		}
		var orderNumber = rowdata.orderNumber;
		var contractPrefix = rowdata.contractPrefix;
		var contractSuffix = "";
		if(rowdata.contractNumber!=''){
			contractSuffix = rowdata.contractNumber.substring(contractPrefix.length);
		}
		k = $.layer({
			type : 1,
			title : "<msg:message code='doc.seal.send'/>",
			border : [0],
			area : ['350px','200px'],
			page : {
				html :  "<table height='165px' width='350px' bgcolor='#EAFBFF'>"+
							"<tr>"+
								"<td align='right' width='35%'>"+
									"<msg:message code='doc.orderNumber'/><msg:message code='system.common.sign.colon'/>"+
									"<input type='hidden' id='sendTimeName' value='"+name+"'>"+
								"</td>"+
								"<td>"+
									orderNumber+
								"</td>"+
							"</tr>"+
							"<tr>"+
								"<td align='right'>"+
									"<msg:message code='doc.contractNumber'/><msg:message code='system.common.sign.colon'/>"+
								"</td>"+
								"<td>"+
									"<input type='text' id='contractPrefix' size='10' disabled='disabled' value='" + contractPrefix + "'> - <input type='text' id='contractSuffix' size='12' value='" + contractSuffix + "'>"+
								"</td>"+
							"</tr>"+
							"<tr>"+
								"<td align='right'>"+
									"寄出时间<msg:message code='system.common.sign.colon'/>"+
								"</td>"+
								"<td>"+
									"<input type='text' id='sendTime' onfocus='WdatePicker()' class='Wdate'>"+
								"</td>"+
							"</tr>"+
							"<tr bgcolor='#CCDFED' height='40px'>"+
								"<td colspan='2' align='center'>"+
									"<input type='button' class='btn2' value='<msg:message code='button.confirm'/>' onclick='sendDoc("+rowid+")'>"+
									"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
									"<input type='button' class='btn2' value='<msg:message code='button.cancel'/>' onclick='closeLayer()'>"+
								"</td>"+
							"</tr>"+
						"</table>"
			}
		});
	}
	
	//确认寄出用印单证
	window.sendDoc = function(rowid){
		var name = $("#sendTimeName").val();
		var contractPrefix = $("#contractPrefix").val();
		var contractSuffix = $("#contractSuffix").val();
		var contractNumber = contractPrefix.concat(contractSuffix);
		var time = $("#sendTime").val();
		if(contractSuffix==''){
			layer.msg("<msg:message code='doc.contractNumber.notNull'/>",1);
			return false;
		}
		
		//合同编号验证状态：0-重复，1-通过
		var status = 0;
		$.ajax({
			url: '${base}/market/docmanage/checkContractNum.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: {id:rowid,contractNumber:contractNumber},
			async:false,
			success:function(jsonData){
				if(jsonData.status == 'success'){
					status = 1;
				}
			}
		});
		if(status==0){
			layer.msg("<msg:message code='doc.contractNumber.already'/>",1);
			return false;
		}
		if(time == null || time == ""){
			layer.msg("寄出时间不能为空!",1);
			return false;
		}
		var rowdata;
		if(type==1){
			rowdata = jQuery("#docAdvanceList").jqGrid('getRowData',rowid);
		}else if(type==2){
			rowdata = jQuery("#docList").jqGrid('getRowData',rowid);
		}
		var orderNumber = rowdata.orderNumber;
		var memberId = rowdata.memberId;
		var sendUrl = '${base}/market/docmanage/send.json';
		if(url=='${base}/market/docmanage/gridSuccess.json'){
			sendUrl = '${base}/market/docmanage/sendSuccess.json';
		}
		var data = "memberId=" + memberId + "&orderId=" + rowid + "&orderNumber=" + orderNumber + "&contractNumber=" + contractNumber + "&isPlatformSentUsing=1&"+name+"="+"'"+time+"'";
		if(type==2){
			data = "memberId=" + memberId + "&orderId=" + rowid + "&orderNumber=" + orderNumber + "&contractNumber=" + contractNumber + "&isPlatformSentNotusing=1&"+name+"="+"'"+time+"'";
		}
		$.ajax({
			url: sendUrl,
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: data,
			success:function(jsonData){
				layer.close(k);
				if(jsonData.status=='success'){
					$.layer({
						area : ['auto','auto'],
						dialog : {msg: "<msg:message code='doc.send.success'/>" ,type : 9}
					});
					if(type==1){
						jQuery("#docAdvanceList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:1,docStatus:$("#docStatusAdvance").val(),productShortName:$("#productShortNameAdvance").val(),clientName:$("#clientNameAdvance").val(),orderNumber:$("#orderNumberAdvance").val(),contractNumber:$("#contractNumberAdvance").val()}}).trigger("reloadGrid");
					}else if(type=2){
						jQuery("#docList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:2,docStatus:$("#docStatus").val(),productShortName:$("#productShortName").val(),clientName:$("#clientName").val(),orderNumber:$("#orderNumber").val(),contractNumber:$("#contractNumber").val()}}).trigger("reloadGrid");
					}
				}else{
					$.layer({
						area : ['auto','auto'],
						dialog : {msg: "<msg:message code='doc.send.fail'/>" ,type : 8}	
					});
				}
			}
		});
	}
	
	//关闭弹出层
	window.closeLayer = function(){
		layer.close(k);
	}
	
	//收到单证
	function receive(rowid,name){
		k = $.layer({
			type : 1,
			title : "平台收到签字用印单证",
			border : [0],
			area : ['350px','200px'],
			page : {
				html :  "<table height='165px' width='350px' bgcolor='#EAFBFF'>"+
							"<tr>"+
								"<td align='right'>"+
									"收到时间<msg:message code='system.common.sign.colon'/>"+
								"</td>"+
								"<td>"+
									"<input type='text' id='receiveTime' onfocus='WdatePicker()' class='Wdate'>"+
									"<input type='hidden' id='receiveTimeName' value='"+name+"'>"+
								"</td>"+
							"</tr>"+
							"<tr bgcolor='#CCDFED' height='40px'>"+
								"<td colspan='2' align='center'>"+
									"<input type='button' class='btn2' value='<msg:message code='button.confirm'/>' onclick='receiveDoc("+rowid+")'>"+
									"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
									"<input type='button' class='btn2' value='<msg:message code='button.cancel'/>' onclick='closeLayer()'>"+
								"</td>"+
							"</tr>"+
						"</table>"
			}
		});
	}
	
	function receiveDoc(rowid){
		var receiveTime = $("#receiveTime").val();
		var rowdata;
		var name = $("#receiveTimeName").val();
		if(receiveTime == null || receiveTime == ""){
			layer.msg("收到时间不能为空!",1);
			return false;
		}
		if(type==1){
			rowdata = jQuery("#docAdvanceList").jqGrid('getRowData',rowid);
		}else if(type==2){
			rowdata = jQuery("#docList").jqGrid('getRowData',rowid);
		}
		var orderNumber = rowdata.orderNumber;
		var productId = rowdata.productId;
		var data = "productId=" + productId + "&orderNumber=" + orderNumber + "&isPlatformReceivedNotusing=1&"+name+"="+"'"+receiveTime+"'";
		var receiveUrl = '${base}/market/docmanage/receiveOrSend.json';
		if(type==1){
			data = "productId=" + productId + "&orderId=" + rowid + "&orderNumber=" + orderNumber + "&isPlatformReceivedSignUsing=1&"+name+"="+"'"+receiveTime+"'";
			receiveUrl = '${base}/market/docmanage/receiveAdvance.json';
			if(url=='${base}/market/docmanage/gridSuccess.json'){
				receiveUrl = '${base}/market/docmanage/receiveSuccessAdvance.json';
			}
		}
		$.ajax({
			url:receiveUrl,
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: data,
			success:function(jsonData){
				layer.close(k);
				if(jsonData.status == "success"){
					$.layer({
						area : ['auto','auto'],
						dialog : {msg: "<msg:message code='doc.receive.success'/>" ,type : 9}	
					});
					if(type==1){
						jQuery("#docAdvanceList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:1,docStatus:$("#docStatusAdvance").val(),productShortName:$("#productShortNameAdvance").val(),clientName:$("#clientNameAdvance").val(),orderNumber:$("#orderNumberAdvance").val(),contractNumber:$("#contractNumberAdvance").val()}}).trigger("reloadGrid");
					}else if(type=2){
						jQuery("#docList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:2,docStatus:$("#docStatus").val(),productShortName:$("#productShortName").val(),clientName:$("#clientName").val(),orderNumber:$("#orderNumber").val(),contractNumber:$("#contractNumber").val()}}).trigger("reloadGrid");
					}
				}else if(jsonData.status == "error"){
					$.layer({
						area : ['auto','auto'],
						dialog : {msg: "<msg:message code='doc.receive.fail'/>" ,type : 8}	
					});
				}
			}
		});
	}
	
	//寄出签字用印单证
	function sendSign(rowid,name){
		k = $.layer({
			type : 1,
			title : "平台收到签字用印单证",
			border : [0],
			area : ['350px','200px'],
			page : {
				html :  "<table height='165px' width='350px' bgcolor='#EAFBFF'>"+
							"<tr>"+
								"<td align='right'>"+
									"寄出时间<msg:message code='system.common.sign.colon'/>"+
								"</td>"+
								"<td>"+
									"<input type='text' id='signSendTime' onfocus='WdatePicker()' class='Wdate'>"+
									"<input type='hidden' id='signSendTimeName' value='"+name+"'>"+
								"</td>"+
							"</tr>"+
							"<tr bgcolor='#CCDFED' height='40px'>"+
								"<td colspan='2' align='center'>"+
									"<input type='button' class='btn2' value='<msg:message code='button.confirm'/>' onclick='signSend("+rowid+")'>"+
									"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
									"<input type='button' class='btn2' value='<msg:message code='button.cancel'/>' onclick='closeLayer()'>"+
								"</td>"+
							"</tr>"+
						"</table>"
			}
		});
	}
	
	function signSend(rowid){
		var rowdata = jQuery("#docList").jqGrid('getRowData',rowid);
		var orderNumber = rowdata.orderNumber;
		var memberId = rowdata.memberId;
		var signSendTime = $("#signSendTime").val();
		var name = $("#receiveTimeName").val();
		$.ajax({
			url:'${base}/market/docmanage/receiveOrSend.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: {memberId:memberId,orderNumber:orderNumber,isPlatformSentSignUsing:1,signSendTime:"'"+signSendTime+"'"},
			success:function(jsonData){
				layer.close(k);
				if(jsonData.status == "success"){
					$.layer({
						area : ['auto','auto'],
						dialog : {msg: "<msg:message code='doc.send.success'/>" ,type : 9}	
					});
					jQuery("#docList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:2,docStatus:$("#docStatus").val(),productShortName:$("#productShortName").val(),clientName:$("#clientName").val(),orderNumber:$("#orderNumber").val(),contractNumber:$("#contractNumber").val()}}).trigger("reloadGrid");
				}else if(jsonData.status == "error"){
					$.layer({
						area : ['auto','auto'],
						dialog : {msg: "<msg:message code='doc.send.fail'/>" ,type : 8}	
					});
				}
			}
		});
	}
	
	//上传单证附件
	function upload(rowid){
		var rowdata;
		if(type==1){
			rowdata = jQuery("#docAdvanceList").jqGrid('getRowData',rowid);
		}else if(type==2){
			rowdata = jQuery("#docList").jqGrid('getRowData',rowid);
		}
		var orderNumber = rowdata.orderNumber;
		k = $.layer({
		    type : 2,
		    title : "<msg:message code='doc.att.upload.title'/>",
		    iframe : {src : '${base}${market_docmanage_uploaddoc.controller}?c=${market_docmanage_uploaddoc.code}&iframeID='+iframeID+'&orderNumber='+orderNumber},
		    maxmin: true,
		    area : ['560px','335px'],
		    offset : ['100px','']
		});
	}
	
	function closeWin(){
		layer.close(k);
	}
	
	//导航标签切换
	function tabOrder(tag){
		if(tag == 1){
			tableType = 1;
			$("#tab_1_order").addClass("selected");
			$("#tab_2_order").removeClass("selected");
			url = '${base}/market/docmanage/grid.json';
		}else if(tag == 2){
			tableType = 2;
			$("#tab_2_order").addClass("selected");
			$("#tab_1_order").removeClass("selected");
			url = '${base}/market/docmanage/gridSuccess.json';
		}
		if(type==1){
			jQuery("#docAdvanceList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:1,docStatus:$("#docStatusAdvance").val(),productShortName:$("#productShortNameAdvance").val(),clientName:$("#clientNameAdvance").val(),orderNumber:$("#orderNumberAdvance").val(),contractNumber:$("#contractNumberAdvance").val()}}).trigger("reloadGrid");
		}else if(type=2){
			jQuery("#docList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:2,docStatus:$("#docStatus").val(),productShortName:$("#productShortName").val(),clientName:$("#clientName").val(),orderNumber:$("#orderNumber").val(),contractNumber:$("#contractNumber").val()}}).trigger("reloadGrid");
		}
	}
	function tab(tag){
		if(tag == 1){
			type = 1;
			$("#tab_1").addClass("selected");
			$("#tab_2").removeClass("selected");
			$("#div_doc_advance").css("visibility","visible");
			$("#div_doc").css("visibility","hidden");
			jQuery("#docAdvanceList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:1,docStatus:$("#docStatusAdvance").val(),productShortName:$("#productShortNameAdvance").val(),clientName:$("#clientNameAdvance").val(),orderNumber:$("#orderNumberAdvance").val(),contractNumber:$("#contractNumberAdvance").val()}}).trigger("reloadGrid");
		}else if(tag == 2){
			type = 2;
			$("#tab_2").addClass("selected");
			$("#tab_1").removeClass("selected");
			$("#div_doc").css("visibility","visible");
			$("#div_doc_advance").css("visibility","hidden");
			jQuery("#docList").jqGrid("setGridParam",{url:url,postData:{c:"${document.c}",type:2,docStatus:$("#docStatus").val(),productShortName:$("#productShortName").val(),clientName:$("#clientName").val(),orderNumber:$("#orderNumber").val(),contractNumber:$("#contractNumber").val()}}).trigger("reloadGrid");
		}
	}
</script>
	</head>
	<body class="skinMain">
		<ul id="tabnav">
			<li class="tab1"><a href="javascript:void(0);" id="tab_1_order" onclick="tabOrder(1)" class="selected"><msg:message code='doc.order'/></a></li>
			<li class="tab2"><a href="javascript:void(0);" id="tab_2_order" onclick="tabOrder(2)"><msg:message code='doc.order.success'/></a></li>
		</ul>
		<ul id="tabnav">
			<li class="tab1"><a href="javascript:void(0);" id="tab_1" onclick="tab(1)" class="selected"><msg:message code='doc.advance.yes'/></a></li>
			<li class="tab2"><a href="javascript:void(0);" id="tab_2" onclick="tab(2)"><msg:message code='doc.advance.no'/></a></li>
		</ul>
		<div id="div_doc_advance" class="tab-div-show" style="overflow: auto;">
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
				<tr>
					<td width="100%">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td>
									<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
										<tr>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='doc.productShortName'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable">
												<input id="productShortNameAdvance" maxlength="20"/>
											</td>
											<td align="right" width="10%" class="search_info"><msg:message code='doc.clientName'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable">
												<input id="clientNameAdvance" maxlength="20"/>
											</td>
											<td align="right" width="10%" class="search_info"><msg:message code='doc.docStatus'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable">
												<select id="docStatusAdvance" onchange="reloadList(1)">
													<option value=""><msg:message code='doc.docStatus.all'/></option>
													<option value="0" selected="selected"><msg:message code='doc.docStatus.notyet'/></option>
													<option value="1"><msg:message code='doc.docStatus.already'/></option>
												</select>
											</td>
											<td rowspan="2" width="10%" class="button_area" align="center">
												<input type="button" class="btn2" id="selectAdvance" value="<msg:message code='button.search'/>">
											</td>
										</tr>
										<tr>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='doc.orderNumber'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="20%"  class="search_lable">
												<input id="orderNumberAdvance" maxlength="20"/>
											</td>
											<td align="right" width="10%" class="search_info"><msg:message code='doc.contractNumber'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="50%" colspan="3" class="search_lable">
												<input id="contractNumberAdvance" maxlength="20"/>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table id="docAdvanceList"><tr><td>&nbsp;</td></tr></table>
									<div id="docAdvancePagered"></div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<div id="div_doc" class="tab-div-hide" style="overflow: auto;">
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
				<tr>
					<td width="100%">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td>
									<form:form method="post" action="#" commandName="document" name="f">
										<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
											<tr>
												<td align="right" width="10%" class="search_info" height="25"><msg:message code='doc.productShortName'/><msg:message code="system.common.sign.colon"/></td>
												<td align="left" width="20%"  class="search_lable">
													<input id="productShortName" maxlength="20"/>
												</td>
												<td align="right" width="10%" class="search_info"><msg:message code='doc.clientName'/><msg:message code="system.common.sign.colon"/></td>
												<td align="left" width="20%"  class="search_lable">
													<input id="clientName" maxlength="20"/>
												</td>
												<td align="right" width="10%" class="search_info"><msg:message code='doc.docStatus'/><msg:message code="system.common.sign.colon"/></td>
												<td align="left" width="20%"  class="search_lable">
													<select id="docStatus" onchange="reloadList(2)">
														<option value=""><msg:message code='doc.docStatus.all'/></option>
														<option value="0" selected="selected"><msg:message code='doc.docStatus.notyet'/></option>
														<option value="1"><msg:message code='doc.docStatus.already'/></option>
													</select>
												</td>
												<td rowspan="2" width="10%" class="button_area" align="center">
													<input type="button" class="btn2" id="select" value="<msg:message code='button.search'/>">
												</td>
											</tr>
											<tr>
												<td align="right" width="10%" class="search_info" height="25"><msg:message code='doc.orderNumber'/><msg:message code="system.common.sign.colon"/></td>
												<td align="left" width="20%"  class="search_lable">
													<input id="orderNumber" maxlength="20"/>
												</td>
												<td align="right" width="10%" class="search_info"><msg:message code='doc.contractNumber'/><msg:message code="system.common.sign.colon"/></td>
												<td align="left" width="50%" colspan="3" class="search_lable">
													<input id="contractNumber" maxlength="20"/>
												</td>
											</tr>
										</table>
									</form:form>
								</td>
							</tr>
							<tr>
								<td>
									<table id="docList"><tr><td>&nbsp;</td></tr></table>
									<div id="docPagered"></div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>