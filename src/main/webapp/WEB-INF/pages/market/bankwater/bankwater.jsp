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
$(window).resize(function(){ 　　
	$("#productList").setGridWidth($(window).width()*0.999);　
});
//layer弹出层标签
var k;
var gridWidth;
var providerId = "";
var matchingStatus;
$(document).ready(function(){
	/**********************************  认购产品-全部 STAR  *************************************/          
    jQuery("#productList").jqGrid({
        url: 'bankwater/rgAllGrid.json',
        datatype: 'json',
        colNames: ["<msg:message code='client.name'/>",
                   "<msg:message code='pay.amount'/>",
                   "<msg:message code='pay.time'/>",
                   "<msg:message code='product.shortName'/>",
                   "<msg:message code='order.provider.name'/>",
                   "<msg:message code='match.status'/>"],
        colModel: [
	                {name: 'name',index: 'name',width:'15%',align:'center',hidden: false,sortable: false},
	                {name: 'payAmount',index: 'payAmount',width:'15%',align:'center',hidden: false,sortable: false},
	                {name: 'payTime',index: 'payTime',width:'20%',align:'center',hidden: false,sortable: false},
	                {name: 'productShortName',index: 'productShortName',width:'20%',align:'center',hidden: false,sortable: false},
	       			{name: 'providerShortName',index: 'providerShortName',width:'20%',align:'center',hidden: false,sortable:false},
	       			{name: 'matchingStatus',index: 'matchingStatus',width:'10%',align:'center',hidden: false,sortable:false,formatter:function(cellvalue, options, rowObject){
       					if(cellvalue==0){return "<msg:message code='match.notyet'/>";}else{return "<msg:message code='match.already'/>";}
       				}}
       			   ],
        mtype:"POST",
        postData:{c:"${market_bankwater.code}",dataType:"1"},
        rowNum:10,     
        page:"${payConfirmFromProvider.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pageredOne',
        height:	230,
        autowidth: true,
        viewrecords: true,
        rownumbers: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete:function(){
        	gridWidth=jQuery("#productList").jqGrid().getGridParam().width;
        },
        caption:'<msg:message code='buy.product'/>'+'—<msg:message code='select.all'/>',
        toolbar: [true,"top"]
    });
  	//导入数据 按钮
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
	.attr("id","${market_bankwater_bankwaterimportor.indexCode}")
	.attr("border","0").attr("class","${market_bankwater_bankwaterimportor.indexCode}")
	.append($("<img/>").attr("src","${base}/${skin}/${market_bankwater_bankwaterimportor.icon}")
  	.attr("width","18").attr("height","18").attr("border","0")
  	.attr("border","0").attr("align","absmiddle"))
  	.append("${market_bankwater_bankwaterimportor.name}");
  	
  	//匹配 按钮
    var $pp = $("<a></a>").attr("href","javascript:void(0)")
	.attr("id","${market_bankwater_mappingbankwater.indexCode}")
	.attr("border","0").attr("class","${market_bankwater_mappingbankwater.indexCode}")
	.append($("<img/>").attr("src","${base}/${skin}/${market_bankwater_mappingbankwater.icon}")
  	.attr("width","18").attr("height","18").attr("border","0")
  	.attr("border","0").attr("align","absmiddle"))
  	.append("${market_bankwater_mappingbankwater.name}");
	$("#t_productList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea).append("&nbsp;").append($pp));
	$("#t_productList").append("<span id='toolBarDiv'></span>");
	
	//当前页iframeID
	var iframeID = window.frameElement.id;
	
	//导入数据按钮 事件
    $("#${market_bankwater_bankwaterimportor.indexCode}").click(function(){
    	var dataType = $("#dataType").val();
    	var productId = $("#productID_one").val();
    	var url='${base}${market_bankwater_bankwaterimportor.controller}?c=${market_bankwater_bankwaterimportor.code}&iframeID='+iframeID+'&dataType='+dataType+'&productId='+productId+'&providerId='+providerId;
		k=top.$.layer({
			type: 2,
			title: "<msg:message code='bankwater.import'/>",
			fix: false,
			closeBtn: true,
			shadeClose: false,
			shade: [0.5,'#B0B1B2', true],
			closeBtn:[0,true],
			border : [5, 0.3, '#666', true],
			offset: ['100px',''],
			area: ['560px','310px'],
			iframe: {src: url},
			success: function(){
			}
		});
    });
	
	//匹配按钮 事件
	$("#${market_bankwater_mappingbankwater.indexCode}").click(function(){
		var productID=$("#productID_one option:selected").val();
		var dataType=$("#dataType").val();
		$("#productID_one_show").html("");
		if(!productID){
			$("#productID_one_show").html("&nbsp;&nbsp;&nbsp;<msg:message code='product.select' />");
			return false;
		}
		var url='${base}${market_bankwater_mappingbankwater.controller}?c=${market_bankwater_mappingbankwater.code}&iframeID='+iframeID+'&productId='+productID;
		k=top.$.layer({
		 	type: 2,
			title: "<msg:message code='bankwaterandproof.mapping' />",
			fix: false,
			closeBtn: true,
			shadeClose: false,
			shade: [0.5,'#B0B1B2', true],
			closeBtn:[0,true],
			border : [5, 0.3, '#666', true],
			offset: ['100px',''],
			area: ['1150px','450px'],
			iframe: {src: url},
			success: function(){
			} 
		});
	});
	
    jQuery("#productList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    //查询按钮
    $("#select").click(function(){
    	jQuery("#productList").jqGrid("setGridParam",{page:0,postData:{productId:$("#productID_one").val(),dataType:$("#dataType").val()}}).trigger("reloadGrid");    
    });
    
    /**********************************  认购产品-全部 END  *************************************/  
    
    /**********************************  认购产品-银行流水匹配和未匹配  STAR  *************************************/          
    jQuery("#productList_hasOrNotDispose").jqGrid({
        url: 'bankwater/rgAllGrid.json',
        datatype: 'local',
        colNames: [
                   "<msg:message code='order.orderNumber'/>",
                   "<msg:message code='client.name'/>",
                   "<msg:message code='pay.amount'/>",
                   "<msg:message code='pay.time'/>",
                   "<msg:message code='product.shortName'/>",
                   "<msg:message code='order.provider.name'/>",
                   "<msg:message code='match.status'/>",
                   "<msg:message code='info.operate'/>"
                   ],
        colModel: [ 
                    {name: 'orderNumber',index: 'orderNumber',width:'15%',align:'center',hidden: false,sortable: false},
	                {name: 'name',index: 'name',width:'15%',align:'center',hidden: false,sortable: false},
	                {name: 'payAmount',index: 'payAmount',width:'15%',align:'center',hidden: false,sortable: false},
	                {name: 'payTime',index: 'payTime',width:'15%',align:'center',hidden: false,sortable: false},
	                {name: 'productShortName',index: 'productShortName',width:'15%',align:'center',hidden: false,sortable: false},
	       			{name: 'providerShortName',index: 'providerShortName',width:'15%',align:'center',hidden: false,sortable:false},
	       			{name: 'matchingStatus',index: 'matchingStatus',width:'15%',align:'center',hidden: false,sortable:false,formatter:function(cellvalue, options, rowObject){
       					if(cellvalue==0){
       						return "<msg:message code='match.notyet'/>";
       					}else{
       						return "<msg:message code='match.already'/>";
       					}
       				}},
   				   {name: 'act',index:'act', width:'5%',align:'left',hidden:false,sortable:false,formatter:function(cellvalue, options, rowObject){
	       		    	var id=rowObject.id;
	       		    	var content="";
	       		    	//流水处理  连接 
	       		    	<c:if test="${!empty market_bankwater_disposebankwater}">
		       		        <security:authorize ifAnyGranted="${market_bankwater_disposebankwater.code}">
		       		        	if(rowObject.matchingStatus==0){
		       		        		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_bankwater_disposebankwater.indexCode}' title='${market_bankwater_disposebankwater.name}'>";
			       		        	content += "<img src='${base}/${skin}/${market_bankwater_disposebankwater.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_bankwater_disposebankwater.name}";
			       		        	content += "</a>";
		       		        	}else{
		       		        		content += "<span style='color:#CFC09F;'><img src='${base}/${skin}/${market_bankwater_disposebankwater.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_bankwater_disposebankwater.name}</span>";	
		       		        	}
		       		        </security:authorize>
	       		    	</c:if>
	       		    	return content;
	       		    }}
	       		],
        mtype:"POST",
        postData:{"c":${market_bankwater.code},"dataType":1},
        rowNum:10,     
        page:"${order.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pageredOne_hasOrNotDispose',
        height:	230,
        autowidth: true,
        rownumbers: true,
        viewrecords: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
    	loadComplete:function(){
    	},
        caption:'<msg:message code='buy.product'/>—<msg:message code='select.all'/>',
        toolbar: [true,"top"]
    });
    
  	//导出EXCEL按钮
    var $eaexport = $("<a></a>").attr("href","javascript:void(0)")
	.attr("id","${market_bankwater_bankwaterexport.indexCode}")
	.attr("border","0").attr("class","${market_bankwater_bankwaterexport.indexCode}")
	.append($("<img/>").attr("src","${base}/${skin}/${market_bankwater_bankwaterexport.icon}")
  	.attr("width","18").attr("height","18").attr("border","0")
  	.attr("border","0").attr("align","absmiddle"))
  	.append("${market_bankwater_bankwaterexport.name}");
	$("#t_productList_hasOrNotDispose").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($eaexport)); 
	
	//导出到EXCEL
    $("#${market_bankwater_bankwaterexport.indexCode}").click(function(){
    	$("#productID_two_show").html("");
    	var productId = $("#productID_two").val();
    	if(productId == ""){
    		$("#productID_two_show").html("&nbsp;&nbsp;&nbsp;<msg:message code='productId.notnull'/>");
    	}else{
	    	window.location.href='${base}/market/bankwater/export?productId='+productId+'&matchingStatus='+matchingStatus;
    	}
    });
  	//当前页iframe ID
	var iframeID = window.frameElement.id;
    //处理 按钮的连接实现
    $(".shortcut_${market_bankwater_disposebankwater.indexCode}").live("click",function(){
    	var url='${base}${market_bankwater_disposebankwater.controller}?c=${market_bankwater_disposebankwater.code}&id='+this.id+'&iframeID='+iframeID;
    	k=top.$.layer({
			type: 2,
			title: "<msg:message code='bankwater.match'/>",
			fix: false,
			closeBtn: true,
			shadeClose: false,
			shade: [0.5,'#B0B1B2', true],
			closeBtn:[0,true],
			border : [5, 0.3, '#666', true],
			offset: ['10px',''],
			area: ['940px','514px'],
			iframe: {src: url},
			success: function(){
			}
		});
    });
	
    jQuery("#productList_hasOrNotDispose").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    //查询按钮
    $("#select_hasOrNotDispose").click(function(){
    	jQuery("#productList_hasOrNotDispose").jqGrid("setGridParam",{page:0,postData:{productId:$("#productID_two").val()}}).trigger("reloadGrid");    
    });
    
    //展示提示信息
    $.showPrompt=function(v){
    	var optionText=$("#productID_one option:selected").text();
    	$.ajax({
			url:'${base}/market/bankwater/getProductOrderProof.json',
			type:'POST',
			timeout:'60000',
			data:{"productId":v},
	   		dataType:'json',
	   		success:function(datas){
	   			if(datas.status){
	   				$("#toolBarDiv").empty()
	   				.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	   				.append("<msg:message code='product.shortName'/><msg:message code='system.common.sign.colon'/><font color='red'>"+optionText+"</font>").append("&nbsp;&nbsp;")
	   				.append("<msg:message code='order.count'/><msg:message code='system.common.sign.colon'/><font color='red'>"+datas.orderCount+"</font>").append("&nbsp;&nbsp;")
	   				.append("<msg:message code='payProof.count'/><msg:message code='system.common.sign.colon'/><font color='red'>"+datas.orderProofCount+"</font>").append("&nbsp;&nbsp;")
	   				.append("<msg:message code='bankwater.count'/><msg:message code='system.common.sign.colon'/><font color='red'>"+datas.bankWaterCount+"</font>").append("&nbsp;&nbsp;")
	   				.append("<msg:message code='bankwater.matched.count'/><msg:message code='system.common.sign.colon'/><font color='red'>"+datas.bankWaterHasDisposeCount+"</font>").append("&nbsp;&nbsp;")
	   				.append("<msg:message code='bankwater.notMatch.count'/><msg:message code='system.common.sign.colon'/><font color='red'>"+datas.bankWaterHasNotDisposeCount+"</font>").append("&nbsp;&nbsp;")
	   				.append("<msg:message code='payProof.notMatch.count'/><msg:message code='system.common.sign.colon'/><font color='red'>"+datas.orderProofHasNotDisposeCount+"</font>");
	   			}else{
	   				//数据加载有误
	   			}
	   		}
		});
    }
    /**********************************  认购产品-银行流水匹配和未匹配 END  *************************************/ 
    
    /**********************************  认购产品-全部 STAR  *************************************/          
    jQuery("#subScribeList").jqGrid({
        url: '#',
        datatype: 'local',
        colNames: ["",
                   "<msg:message code='order.orderNumber'/>",
                   "<msg:message code='order.contractNumber'/>",
                   "<msg:message code='order.product.shortName'/>",
                   "<msg:message code='order.client.name'/>",
                   "<msg:message code='order.tradeStatus'/>",
                   "<msg:message code='order.investAmount'/>",
                   "<msg:message code='order.share'/>",
                   "<msg:message code='order.payAmount'/>",
                   "<msg:message code='bankwater.hasornoproof'/>",
                   "<msg:message code='order.affirm.amount'/>",
                   "<msg:message code='info.operate'/>"],
        colModel: [
					{name: 'id',index: 'id',width:'1%',align:'center',hidden: true,sortable: false},
	                {name: 'orderNumber',index: 'orderNumber',width:'10%',align:'center',hidden: false,sortable: false},
	                {name: 'contractNumber',index: 'contractNumber',width:'10%',align:'center',hidden: false,sortable: false},
	                {name: 'productShortName',index: 'productShortName',width:'10%',align:'center',hidden: false,sortable: false},
	                {name: 'clientName',index: 'clientName',width:'10%',align:'center',hidden: false,sortable: false},
	       			{name: 'tradeStatus',index: 'tradeStatus',width:'10%',align:'center',hidden: false,sortable:false,formatter:function(cellvalue, options, rowObject){
	       				<c:forEach items="${tradeStatusMap}" var="it" >
							if(cellvalue==${it.key}){
	       						return '${it.value}';
	       					}	       					
	       				</c:forEach>
	       			}},
	       			{name: 'investAmount',index: 'investAmount',width:'8%',align:'center',hidden: false,sortable:false},
	       			{name: 'share',index: 'share',width:'8%',align:'center',hidden: false,sortable:false},
	       			{name: 'payAmount',index: 'payAmount',width:'8%',align:'center',hidden: false,sortable:false},
	       			{name: 'proofCount',index: 'proofCount',width:'8%',align:'center',hidden: false,sortable:false,formatter:function(cellvalue, options, rowObject){
	       				if(cellvalue=='0'){
	       					return "无";
	       				}else{
	       					return "有";
	       				}
	       			}},
	       			{name: 'affirmAmount',index: 'affirmAmount',width:'10%',align:'center',hidden: false,sortable:false,formatter:changeAffirmAmount},
	       			{name: 'act',index: 'act',width:'6%',align:'center',hidden: false,sortable:false,formatter:function(cellvalue, options, rowObject){
	       				var id=rowObject.id;
	       				var content="";
	       				<c:if test="${!empty market_bankwater_affirmbankwater}">
		       		        <security:authorize ifAnyGranted="${market_bankwater_affirmbankwater.code}">
	       		        		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${market_bankwater_affirmbankwater.indexCode}' title='${market_bankwater_affirmbankwater.name}'>";
		       		        	content += "<img src='${base}/${skin}/${market_bankwater_affirmbankwater.icon}' weight='18' height='18' border='0' align='absmiddle'/>${market_bankwater_affirmbankwater.name}";
		       		        	content += "</a>";
		       		        </security:authorize>
       		    		</c:if>
       		    		return content;
       				}}
       			   ],
        mtype:"POST",
        postData:{c:"${market_bankwater.code}",dataType:"2"},
        rowNum:10,     
        page:"${payConfirmFromProvider.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pageredOne_subScribe',
        height:	230,
        autowidth: true,
        viewrecords: true,
        rownumbers: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete:function(){
        	//gridWidth=jQuery("#subScribeList").jqGrid().getGridParam().width;
        },
        caption:'<msg:message code='buy.product'/>'+'—<msg:message code='select.all'/>',
        toolbar: [true,"top"]
    });
	
    jQuery("#subScribeList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    //查询按钮
    $("#subScribe_select").click(function(){
    	jQuery("#subScribeList").jqGrid("setGridParam",{page:0,postData:{productId:$("#subScribe_productID_one").val()}}).trigger("reloadGrid");    
    });
    
  	//导出申购 EXCEL按钮
  	<c:if test="${!empty market_bankwater_exportsubscibeexcel}">
     	<security:authorize ifAnyGranted="${market_bankwater_exportsubscibeexcel.code}">
	     	var $subscribeExcel = $("<a></a>").attr("href","javascript:void(0)")
	    	.attr("id","${market_bankwater_exportsubscibeexcel.indexCode}")
	    	.attr("border","0").attr("class","${market_bankwater_exportsubscibeexcel.indexCode}")
	    	.append($("<img/>").attr("src","${base}/${skin}/${market_bankwater_exportsubscibeexcel.icon}")
	      	.attr("width","18").attr("height","18").attr("border","0")
	      	.attr("border","0").attr("align","absmiddle"))
	      	.append("${market_bankwater_exportsubscibeexcel.name}");
	    	$("#t_subScribeList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($subscribeExcel)); 
     	</security:authorize>
	</c:if>
	
	//导出申购Excel事件
	$("#${market_bankwater_exportsubscibeexcel.indexCode}").click(function(){
		$("#subScribe_productID_one_show").html("");
    	var productId = $("#subScribe_productID_one").val();
    	if(productId == null || productId == ""){
    		$("#subScribe_productID_one_show").html("&nbsp;&nbsp;&nbsp;<msg:message code='productId.notnull'/>");
    	}else{
	    	window.location.href='${base}${market_bankwater_exportsubscibeexcel.controller}?productId='+productId;
    	}
	});
    
    //匹配按钮点击事件
    $(".shortcut_${market_bankwater_affirmbankwater.indexCode}").live("click",function(){
    	var orderID=jQuery("#subScribeList").getRowData(this.id).id;
    	var tempID="payAmount_"+this.id;
    	var tempValue=$("#"+tempID).val();
    	if(tempValue==null || tempValue==0){
    		$("#"+tempID).poshytip('show');
    		$("#"+tempID).focus();
    		return false;
    	}
    	var k=$.layer({
		    shade : false,
		    area : ['auto','auto'],
		    dialog : {
		        msg:'匹配完成之后，订单将会变为成功订单，是否确认操作？',
		        btns : 2, 
		        type : 4,
		        btn : ['<msg:message code='order.true.button'/>','<msg:message code='order.fail.button'/>'],
		        yes : function(){
		        	$.ajax({
    					url: '${base}${market_bankwater_affirmbankwater.controller}.json',
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					data: {id:orderID,c:'${market_bankwater_affirmbankwater.code}',payAmount:tempValue}, 
    					async:false,
    					cache:false,
    					success:function(jsonData){
    						layer.close(k);
    						if(jsonData.zt=='success'){
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'订单匹配成功！',type : 9}	
    							});
    							jQuery("#subScribeList").jqGrid().trigger("reloadGrid");
    						}else{
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'订单匹配失败！',type : 8}	
    							});
    						}
    					}
    				});
		        },
		        no : function(){
		            layer.close(k);
		        }
		    }
		});
    	
    });
    
    /**********************************  申购产品-全部 END  *************************************/  
    
    //产品变更事件
    $("#productID_one").change(function(){
    	if(this.value!=null && this.value!=''){
    		$.showPrompt(this.value);
    	}else{
    		$("#toolBarDiv").empty();
    	}
    });
    
    //获取供应商ID
    getProviderId();
    
  	//点击页面其他地方起泡提示隐藏
	$(document).bind("click",function(event) {
		var target = $(event.target);
		var targetID=target.attr("id");
		var targerVal=target.attr("value");
		if (typeof(targetID)!='undefined' && targetID.indexOf("payAmount_")!=-1){
			if(targerVal!=null && targerVal!='' && targerVal!=0){
				$("#"+targetID).poshytip('hide');		
			}
		}
	});	
  	
	var SubmitSelect = function(evt){
	    evt = window.event || evt;
	    if(evt.keyCode==13){//如果取到的键值是回车
	    	if($("#all_order").css("visibility")=='visible'){
    			$("#select").click();	
	    	}else if($("#subScribe_order").css("visibility")=='visible'){
	    		$("#subScribe_select").click();	
	    	}else if($("#hasOrNotDispose_order").css("visibility")=='visible'){
	    		$("#select_hasOrNotDispose").click();	
	    	}
	     }
	}
	window.document.onkeydown=SubmitSelect;
});

//确认打款金额
function changeAffirmAmount(cellvalue, options, rowObject){
	var id=rowObject.id;
	var redata = rowObject.payAmount;
	if(redata==null || typeof(redata) == "undefined"){
		redata = "";
	}
	var content ='';
	var a=jQuery("#subScribeList").getGridParam("url");
	if(a.indexOf("getNotMappOrderGrid")!=-1){
		content =  '<input class="input_payAmount" title="'+redata+'" style="width:90px;" type="text" value="'+redata+'"  id="payAmount_'+id+'" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" onafterpaste="this.value=this.value.replace(/\\D/g,\'\')" />';
		$("#payAmount_"+id).poshytip({
    		content: "确认金额不能为空或0！",
    		className: 'tip-violet',
    		showOn: 'focus',
    		alignTo: 'target',
    		alignX: 'left',
    		alignY: 'center',
    		offsetX: 5,
    		liveEvents: true,
    		showTimeout: 1
    	});
	}else{
		content=cellvalue;
	}
	return content;
}

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

//TAB切换
function tab_order(tag){
	changeTab(tag);
	if(tag==1){
		$("#dataType").val(1);
		tab_order_Two(1);
	}else if(tag==2){
		$("#dataType").val(2);
		tab_order_Two(1);
	}
}

function changeTab(tag){
	if(tag == 1){
		$("#tab_1").addClass("selected");
		$("#tab_2").removeClass("selected");
	}else if(tag == 2){
		$("#tab_2").addClass("selected");
		$("#tab_1").removeClass("selected");
	}
}

//TAB_Two切换
function tab_order_Two(tag){
	changeTab_Two(tag);
	$("#productID_one").empty().append("<option value=''>--<msg:message code='info.select'/>--</option>");
	$("#productID_two").empty().append("<option value=''>--<msg:message code='info.select'/>--</option>");
	$("#subScribe_productID_one").empty().append("<option value=''>--<msg:message code='info.select'/>--</option>");
	if($("#dataType").val()==1){
		var offerpayListJson=${offerpayListJson};
		$.each(offerpayListJson,function(index,item){
			$("#productID_one").append("<option value="+item.id+">"+item.shortName+"</option>");
			$("#productID_two").append("<option value="+item.id+">"+item.shortName+"</option>");
		});
		if(tag==1){
			jQuery("#productList").jqGrid("setGridParam",{page:0,postData:{dataType:$("#dataType").val()}}).setCaption('<msg:message code='buy.product'/>—<msg:message code='select.all'/>').trigger("reloadGrid");
			$("#toolBarDiv").empty();
		}else if(tag==2){
			matchingStatus = 0;
			jQuery("#productList_hasOrNotDispose").jqGrid("setGridParam",{page:0,datatype:'json',postData:{dataType:$("#dataType").val(),matchingStatus:0},autowidth:true}).jqGrid('hideCol',"orderNumber").jqGrid("showCol","act").setCaption('<msg:message code='buy.product'/>—<msg:message code='record.notMatch'/>').trigger("reloadGrid");
			jQuery("#productList_hasOrNotDispose").setGridWidth(gridWidth);
		}else{
			matchingStatus = 1;
			jQuery("#productList_hasOrNotDispose").jqGrid("setGridParam",{page:0,datatype:'json',postData:{dataType:$("#dataType").val(),matchingStatus:1},autowidth:true}).jqGrid('hideCol',"act").jqGrid("showCol","orderNumber").setCaption('<msg:message code='buy.product'/>—<msg:message code='record.matched'/>').trigger("reloadGrid");
			jQuery("#productList_hasOrNotDispose").setGridWidth(gridWidth);
		}	
	}else{
		var subpayListJson=${subpayListJson};
		$.each(subpayListJson,function(index,item){
			$("#subScribe_productID_one").append("<option value="+item.id+">"+item.shortName+"</option>");
		});
		if(tag==1 || tag==2){
			jQuery("#subScribeList").jqGrid("setGridParam",{page:0,url:'bankwater/getNotMappOrderGrid.json',datatype:'json'}).jqGrid("showCol","act").setCaption('<msg:message code='product.buy.add'/>—<msg:message code='record.notMatch'/>').trigger("reloadGrid");
			jQuery("#subScribeList").setGridWidth(gridWidth);
			jQuery("#t_subScribeList").hide();
		}else{
			jQuery("#subScribeList").jqGrid("setGridParam",{page:0,url:'bankwater/getHasMappOrderGrid.json',datatype:'json',toolbar:true}).jqGrid('hideCol',"act").setCaption('<msg:message code='product.buy.add'/>—<msg:message code='record.matched'/>').trigger("reloadGrid");
			jQuery("#subScribeList").setGridWidth(gridWidth);
			jQuery("#t_subScribeList").show();
		}
	}
}

function changeTab_Two(tag){
	var fatherTab=$("#dataType").val();
	if(fatherTab==1){
		$(".tab_1_1").show();
		$("#subScribe_order").css("visibility","hidden");
		if(tag == 1){
			$("#tab_1_1").addClass("selected");
			$("#tab_1_2").removeClass("selected");
			$("#tab_1_3").removeClass("selected");
			$("#all_order").css("visibility","visible");
			$("#hasOrNotDispose_order").css("visibility","hidden");
		}else if(tag == 2){
			$("#tab_1_2").addClass("selected");
			$("#tab_1_1").removeClass("selected");
			$("#tab_1_3").removeClass("selected");
			$("#all_order").css("visibility","hidden");
			$("#hasOrNotDispose_order").css("visibility","visible");
		}else{
			$("#tab_1_3").addClass("selected");
			$("#tab_1_2").removeClass("selected");
			$("#tab_1_1").removeClass("selected");
			$("#hasOrNotDispose_order").css("visibility","visible");
			$("#all_order").css("visibility","hidden");
		}
	}else{
		$(".tab_1_1").hide();
		if(tag == 1  || tag == 2){
			$("#tab_1_2").addClass("selected");
			$("#tab_1_3").removeClass("selected");
			$("#all_order").css("visibility","hidden");
			$("#hasOrNotDispose_order").css("visibility","hidden");
			$("#subScribe_order").css("visibility","visible");
		}else{
			$("#tab_1_3").addClass("selected");
			$("#hasOrNotDispose_order").css("visibility","hidden");
			$("#all_order").css("visibility","hidden");
			$("#tab_1_2").removeClass("selected");
			$("#subScribe_order").css("visibility","visible");
		}		
	}
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
function closeWin(){
	top.layer.close(k);
}
function reloadGridTwo(v){
	top.layer.close(k);
	jQuery("#"+v).jqGrid().trigger("reloadGrid");
	var prodID=$("#productID_one option:selected").val();
	if(prodID!=null && prodID!=''){
		$.showPrompt(prodID);		
	}
}
//获取供应商ID
function getProviderId(){
	var id = $("#productID_one").find("option:selected").attr("id");
	if(id != undefined){
		providerId = id;
	}
}
</script>
	</head>
	<body class="skinMain">
	<ul id="tabnav">
		<li class="tab_1" ><a href="javascript:void(0);" id="tab_1" onclick="tab_order(1)" class="selected"><msg:message code='buy.product'/></a></li>
		<li class="tab_2" ><a href="javascript:void(0);" id="tab_2" onclick="tab_order(2)"><msg:message code='product.buy.add'/></a></li>
	</ul>
	<input type="hidden" id="dataType" value="1" />
	<div id="div_1"  class="tab-div-show">
		<ul id="tabnav">
			<li class="tab_1_1" ><a href="javascript:void(0);" id="tab_1_1" onclick="tab_order_Two(1)" class="selected"><msg:message code='record.all'/></a></li>
			<li class="tab_1_2" ><a href="javascript:void(0);" id="tab_1_2" onclick="tab_order_Two(2)"><msg:message code='record.notMatch'/></a></li>
			<li class="tab_1_3" ><a href="javascript:void(0);" id="tab_1_3" onclick="tab_order_Two(3)"><msg:message code='record.matched'/></a></li>
		</ul>
		<div id="all_order" class="tab-div-show">
			<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td width="100%">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td align="center">
								<form:form method="post" action="#" commandName="payConfirmFromProvider" id="payConfirmFromProvider" name="f">
									<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
										<tr>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.product.shortName'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="30%"  class="search_lable">
												<select id="productID_one" onchange="getProviderId()">
													<option value="" id="" selected="selected">--<msg:message code='info.select'/>--</option>
													<c:forEach items="${offerpayList}" var="it" >
														<option value="${it.id}" id="${it.providerId}">${it.shortName}</option>
													</c:forEach>
												</select>
												<span id="productID_one_show" style="color: red;font-size: 12px;"></span>
											</td>
											<td rowspan="3" width="20%" class="button_area" align="center">
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
		</div>
		<div id="subScribe_order" class="tab-div-hide">
			<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td width="100%">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td align="center">
								<form:form method="post" action="#" commandName="payConfirmFromProvider" id="payConfirmFromProvider" name="f">
									<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
										<tr>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.product.shortName'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="30%"  class="search_lable">
												<select id="subScribe_productID_one" onchange="getProviderId()">
													<option value="" id="" selected="selected">--<msg:message code='info.select'/>--</option>
													<c:forEach items="${offerpayList}" var="it" >
														<option value="${it.id}" id="${it.providerId}">${it.shortName}</option>
													</c:forEach>
												</select>
												<span id="subScribe_productID_one_show" style="color: red;font-size: 12px;"></span>
											</td>
											<td rowspan="3" width="20%" class="button_area" align="center">
												<input type="button" class="btn2" id="subScribe_select" value="<msg:message code='button.search'/>">
											</td>
										</tr>	
									</table>
								</form:form>
								</td>
							</tr>
							<tr>
								<td>
									<table id="subScribeList"><tr><td>&nbsp;</td></tr></table>
									<div id="pageredOne_subScribe"></div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<div id="hasOrNotDispose_order" class="tab-div-hide">
			<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td width="100%">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td align="center">
								<form:form method="post" action="#" commandName="payConfirmFromProvider" id="order_hasOrNotDispose" name="f">
									<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
										<tr>
											<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.product.shortName'/><msg:message code="system.common.sign.colon"/></td>
											<td align="left" width="30%"  class="search_lable">
												<select id="productID_two">
													<option value="">--<msg:message code='info.select'/>--</option>
													<c:forEach items="${offerpayList}" var="it" >
														<option value="${it.id}">${it.shortName}</option>
													</c:forEach>
												</select>
												<span id="productID_two_show" style="color: red;font-size: 12px;"></span>
											</td>
											<td width="20%" class="button_area" align="center">
												<input type="button" class="btn2" id="select_hasOrNotDispose" value="<msg:message code='button.search'/>">
											</td>
										</tr>	
									</table>
								</form:form>
								</td>
							</tr>
							<tr>
								<td>
									<table id="productList_hasOrNotDispose"><tr><td>&nbsp;</td></tr></table>
									<div id="pageredOne_hasOrNotDispose"></div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>