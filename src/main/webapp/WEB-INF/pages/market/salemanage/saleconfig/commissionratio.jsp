<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
	

var layers = 0;
var st1 = "";
var st2 = "";
$(document).ready(function() {
	$(window).resize(function(){ 　　
		$("#commissionRatioList").setGridWidth($(window).width()*0.999);　
	});

	
	var productType = "${subProduct.productType}";
	var dis1 = false;
	var dis2 = false;
	if(productType == "2" || productType == "5"){
		dis2 = true;
		st2 = "display:none";
	}else{
		dis1 = true;
		st1 = "display:none";
	}
	
	/* 页面初始化*/

	//加载收益率维护列表
	jQuery("#commissionRatioList").jqGrid({
		
    	datatype: "local",
        colNames: ["","个人居间费比例(%)","机构居间费比例(%)",
                   "<msg:message code='subProduct.commissionRatio.minShareThreshold'/>(万元)<font color=\"red\">&nbsp;*&nbsp;</font>", 
		           "<msg:message code='subProduct.commissionRatio.maxShareThreshold'/>(万元)<font color=\"red\">&nbsp;&nbsp;</font>",
                   "<msg:message code='subProduct.commissionRatio.effectiveDate'/><font color=\"red\">&nbsp;&nbsp;</font>",
                   "<msg:message code='subProduct.commissionRatio.sort'/><font color=\"red\">&nbsp;&nbsp;</font>",
                   "<msg:message code='subProduct.commissionRatio.holdlingPhase'/><font color=\"red\">&nbsp;&nbsp;</font>",
                   "<msg:message code='subProduct.commissionRatio.holdingPhaseDays'/><font color=\"red\">&nbsp;&nbsp;</font>",
                   "<msg:message code='subProduct.commissionRatio.timeCoefficient'/><font color=\"red\">&nbsp;&nbsp;</font>",
                   "<msg:message code='subProduct.commissionRatio.issuanceCostRate'/><font color=\"red\">&nbsp;&nbsp;</font>",
                   "<msg:message code='subProduct.commissionRatio.serviceChargeCoefficient'/><font color=\"red\">&nbsp;&nbsp;</font>",
                   "<msg:message code='subProduct.commissionRatio.orgServiceChargeCoefficient'/><font color=\"red\">&nbsp;&nbsp;</font>",
                   "<msg:message code='subProduct.commissionRatio.incentiveFeeRate'/><font color=\"red\">&nbsp;&nbsp;</font>",
				   "<msg:message code='subProduct.commissionRatio.isAvailable'/><font color=\"red\">&nbsp;&nbsp;</font>",
                   "<msg:message code='info.operate'/>",
				   ""
                   ],
        colModel: [{name:'id',index:'id',hidden:true},
				   {name:'pratio',index:'pratio',width:'10%',align:'center',sortable:false,formatter:syl},
				   {name:'oratio',index:'oratio',width:'10%',align:'center',sortable:false,formatter:syl2},
                   {name: 'minShareThreshold',index:'minShareThreshold',width:'10%',align:'center',hidden: false,sortable:false},
				   {name: 'maxShareThreshold',index:'maxShareThreshold',width:'10%',align:'center',hidden: false,sortable:false},
                   {name: 'effectiveDate',index:'effectiveDate',width:'5%',align:'center',hidden: dis1,sortable:false},
                   {name: 'sort',index:'sort',width:'5%',align:'center',hidden: dis2,sortable:false},
                   {name: 'holdlingPhase',index:'holdlingPhase',width:'10%',align:'center',hidden: true,sortable:false},
                   {name: 'holdingPhaseDays',index:'holdingPhaseDays',width:'5%',align:'center',hidden: true,sortable:false},
                   {name: 'timeCoefficient',index:'timeCoefficient',width:'5%',align:'center',hidden: true,sortable:false},
                   {name: 'issuanceCostRate',index:'issuanceCostRate',width:'5%',align:'center',hidden: true,sortable:false},
                   {name: 'serviceChargeCoefficient',index:'serviceChargeCoefficient',width:'5%',align:'center',hidden: true,sortable:false},
                   {name: 'orgServiceChargeCoefficient',index:'orgServiceChargeCoefficient',width:'5%',align:'center',hidden: true,sortable:false},
                   {name: 'incentiveFeeRate',index:'incentiveFeeRate',width:'5%',align:'center',hidden: true,sortable:false},
				   {name: 'isAvailable',index:'isAvailable',width:'5%',align:'center',hidden: dis2,sortable:false,formatter:intIsAvailable},
                   {name: 'operate',index:'operate',width:'10%',align:'left',hidden: false,sortable:false,formatter:initAct},
				   {name: 'isAvailable',index:'isAvailable',hidden: true},
                   ],
		mtype:"POST",
        height:'<msg:message code='jqgrid.height.250'/>',
        autowidth: true,
		jsonReader: {
			repeatitems: false
		},
        caption:'<msg:message code="subProduct.commissionRatio.list"/>',
		toolbar: [true,"top"]
    });
	
	// 创建
	$("#shortcut_add").live("click",function(){
		editPage('');
	});

	// 编辑
	$(".shortcut_modify").live("click",function(){
		var rowid = $(this).attr("id");
		var rowdata = jQuery("#commissionRatioList").jqGrid('getRowData',rowid);
		editPage(rowid);
		$("input[name='minShareThreshold']").val(rowdata.minShareThreshold);
		$("input[name='maxShareThreshold']").val(rowdata.maxShareThreshold);
		$("input[name='effectiveDate']").val(rowdata.effectiveDate);
		$("#st_sort").val(rowdata.sort);
		$("input[name='holdlingPhase']").val(rowdata.holdlingPhase);
		$("input[name='holdingPhaseDays']").val(rowdata.holdingPhaseDays);
		$("input[name='timeCoefficient']").val(rowdata.timeCoefficient);
		$("input[name='issuanceCostRate']").val(rowdata.issuanceCostRate);
		$("input[name='serviceChargeCoefficient']").val(rowdata.serviceChargeCoefficient);
		$("input[name='orgServiceChargeCoefficient']").val(rowdata.orgServiceChargeCoefficient);
		$("input[name='incentiveFeeRate']").val(rowdata.incentiveFeeRate);
		$("#isAvailable").val(rowdata.isAvailable);
		ca();
    });
	
	// 删除
	$(".shortcut_delete").live("click",function(){
		var rowid = $(this).attr("id");
		if(rowid == ""){
			top.layer.msg("<msg:message code='info.prompt.delete'/>");
			return;
		}
		var data="c=${c}&id="+rowid;
		layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
			jQuery.ajax({
				url:"${base}/market/salemanage/saleconfig/commissionratio/delete.json",
				type: "post",
				contentType : 'application/x-www-form-urlencoded',
				data:data, 
				dataType : 'json',
				success:function(jsonData){
					if (jsonData.status == "success"){
						reload();
						top.layer.close(layers);							
					}
				}
			});
		});
    });
	
	var $ea = $("<a></a>").attr("href","javascript:void(0)")
		.attr("id","shortcut_add")
		.append($("<img/>").attr("src","${base}/${skin}/images/icon/create.png")
		.attr("width","18").attr("height","18").attr("border","0")
		.attr("border","0").attr("align","absmiddle"))
		.append("创建");
	$("#t_commissionRatioList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea)); 
		
	reload();
});

	//初始化列表后的操作
	function initAct(cellvalue, options, rowObject){
		var id = rowObject.id;
		var content = "";
		
		// 编辑
	    content += "<a href='#' id='" + id + "' class='shortcut_modify' title='编辑'>";
	    content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>编辑";
	    content += "</a>";
		
		// 删除
		content += "<a href='#' id='" + id + "' class='shortcut_delete' title='删除'>";
	    content += "<img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>删除";
	    content += "</a>";
		
   		return content;
	} 
	
	function intIsAvailable(cellvalue, options, rowObject){
		var content="不启用";
		if(cellvalue){
			content = "启用";
		}
   		return content;
	} 
	
	// 格式化
	function ratioAct(cellvalue, options, rowObject){
		return cellvalue.replaceAll("#","&nbsp;,&nbsp;");
	}

	// 重新加载列表数据
	function reload(){
		jQuery("#commissionRatioList").jqGrid("setGridParam",{url:'commissionratiodata.json',datatype: "json",postData:{c:"${c}",subProductId:"${subProduct.id}"}}).trigger("reloadGrid");
	}
	
	// 数字验证
	function checkNumber(evt,object,flag){
		var key = window.event?event.keyCode:evt.which;
		var re = /^[0-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
		if(key != 8){
			var value = $(object).val();
			if(!re.test(value)){
				if("paste" == flag){
					$(object).val("");
				}else{
					if(value != null && value != ""){
						$(object).val(value.substring(0,value.length-1));
						$("#tr_error").css("background-color","red");
						$("#error").text($(object).parent().prev().text()+"请输入有效数字");
					}
				}
			}
		}
	}
	
	// 保存收益率
	function save(){
		var status = false;
		var form = $("#edit_form").serializeArray();
		$.each(form, function(i, item) {
			if((i == 4 || i== 6 || i == 8 || i==7) && item.value == ""){
				var pret = $("input[name='"+item.name+"']").parent().prev().text();
				$("#error").text(pret+"为必填项");
				status = true;
				return false;
			}else if(form[5].value != "" && (parseInt(form[4].value) > parseInt(form[5].value))){
				$("#error").text("最大投资金额不能小于最小投资金额");
				status = true;
				return false;
			}
			$("#error").text('');
		});
		if(status){
			$("#tr_error").css("background-color","red");
			return false;
		}else{
			$("#tr_error").css("background-color","white");
		}
		jQuery.ajax({
			url:"${base}/market/salemanage/saleconfig/commissionratio/save.json",
			type: "post",
			contentType : 'application/x-www-form-urlencoded',
			data:$("#edit_form").serializeArray(), 
			dataType : 'json',
			success:function(data){
				if(data.status == 'success'){
					reload();
					layer.close(layers);
					$.layer({
						area: ['300px','auto'],
						border : [0, 0.5, '#666', true],
						dialog : {msg:'<msg:message code='info.operate.success'/>',type : 9}	
					});	
				}else{
					top.layer.msg("error");
				}
			}
		});
	}
	
	// 弹出编辑页面
	function editPage(rowid){
		//页面层
		layers = $.layer({
			type: 1,
			title: '居间费比例设置',
			//shadeClose: true, 
			closeBtn: [0,true],
			border : [0, 0.5, '#666', true],
			offset: ['100px',''],
			move: ['.xubox_title', true],
			area: ['330px','auto'],
			page: {
				html: '<form id="edit_form">'
				+'<input type="hidden" name="id" value="'+rowid+'"/>'
				+'<input type="hidden" name="subProductId" value="${subProduct.id}"/>'
				+'<input type="hidden" name="productId" value="${subProduct.productId}"/>'
				+'<input type="hidden" name="alterCloseType" value="0"/>'
				+'<table width="330px" border="0" cellspacing="3" cellpadding="0" class="skinMain" align="left">'
				+'<tr><td colspan="2" align="center" id="tr_error" style="color:#FFF;"><font id="error"></font></td></tr>'
				+'<tr><td align="right">最小投资金额(万元)：</td><td><input type="text" name="minShareThreshold" onkeyup="checkNumber(event,this,\'edit\');"  onpropertychange="checkNumber(event,this,\'edit\');" oninput="checkNumber(event,this,\'edit\');"  /><font color="red">&nbsp;*&nbsp;</font></td></tr>'
				+'<tr><td align="right">最大投资金额(万元)：</td><td><input type="text" name="maxShareThreshold" onkeyup="checkNumber(event,this,\'edit\');"  onpropertychange="checkNumber(event,this,\'edit\');" oninput="checkNumber(event,this,\'edit\');"  /><font color="red">&nbsp;&nbsp;</font></td></tr>'
				+'<tr><td align="right">年化发行成本率(%)：</td><td><input type="text" name="issuanceCostRate" onchange="ca();" onkeyup="checkNumber(event,this,\'edit\');"  onpropertychange="checkNumber(event,this,\'edit\');" oninput="checkNumber(event,this,\'edit\');"  /><font color="red">&nbsp;*&nbsp;</font></td></tr>'
				+'<tr><td align="right">时间系数(%)：</td><td><input type="text" value="100" name="timeCoefficient" onchange="ca();" onkeyup="checkNumber(event,this,\'edit\');"  onpropertychange="checkNumber(event,this,\'edit\');" oninput="checkNumber(event,this,\'edit\');"  /><font color="red">&nbsp;*&nbsp;</font></td></tr>'
				+'<tr><td align="right">个人服务费系数(%)：</td><td><input type="text" name="serviceChargeCoefficient" onchange="ca();" onkeyup="checkNumber(event,this,\'edit\');"  onpropertychange="checkNumber(event,this,\'edit\');" oninput="checkNumber(event,this,\'edit\');"  /><font color="red">&nbsp;*&nbsp;</font></td></tr>'
				+'<tr><td align="right">机构服务费系数(%)：</td><td><input type="text" name="orgServiceChargeCoefficient" onchange="ca();" onkeyup="checkNumber(event,this,\'edit\');"  onpropertychange="checkNumber(event,this,\'edit\');" oninput="checkNumber(event,this,\'edit\');"  /><font color="red">&nbsp;*&nbsp;</font></td></tr>'
				+'<tr><td align="right">激励费用率(%)：</td><td><input type="text" name="incentiveFeeRate" onchange="ca();" onkeyup="checkNumber(event,this,\'edit\');"  onpropertychange="checkNumber(event,this,\'edit\');" oninput="checkNumber(event,this,\'edit\');"  /></td></tr>'
				+'<tr style='+st1+'><td align="right">生效日期：</td><td><input type="text" name="effectiveDate" onclick="WdatePicker();" class="Wdate" readonly="readonly"/><font color="red">&nbsp;&nbsp;</font></td></tr>'
				+'<tr style='+st2+'><td align="right">发放阶段序号：</td><td><select id="st_sort" name="sort" style="width:153px;"><option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option></select></td></tr>'
				+'<tr style='+st2+'><td align="right">存续阶段说明：</td><td><input type="text" name="holdlingPhase" /></td></tr>'
				+'<tr style='+st2+'><td align="right">阶段提醒时间点(第N天)：</td><td><input type="text" name="holdingPhaseDays" onkeyup="checkNumber(event,this,\'edit\');"  onpropertychange="checkNumber(event,this,\'edit\');" oninput="checkNumber(event,this,\'edit\');"  /></td></tr>'
				+'<tr style='+st2+'><td align="right">启用状态：</td><td><select name="isAvailable" id="isAvailable" style="width:153px;"><option value="1">启用</option><option value="0">不启用</option></select></td></tr>'
				+'<tr><td align="right">个人预计居间费率(%)：</td><td><input type="text" id="fee1" disabled="disabled"/></td></tr>'
				+'<tr><td align="right">机构预计居间费率(%)：</td><td><input type="text" id="fee2" disabled="disabled"/></td></tr>'
				+'<tr><td colspan="2"></td><tr>'
				+'<tr><td colspan="2"><center><input type="button" class="btn2" onclick="save();" value="确认" id="btnback"/></center></td></tr>'
				+'</table></form>'
			}
		});
	}
	
	function ca(){
		var t1 = $("input[name='issuanceCostRate']").val();
		var t2 = $("input[name='serviceChargeCoefficient']").val();
		var t3 = $("input[name='orgServiceChargeCoefficient']").val();
		var t4 = $("input[name='timeCoefficient']").val();
		var t5 = $("input[name='incentiveFeeRate']").val();
		if(t1 == ""){t1 = 0;}else{t1 = t1*0.01}
		if(t2 == ""){t2 = 0;}else{t2 = t2*0.01}
		if(t3 == ""){t3 = 0;}else{t3 = t3*0.01}
		if(t4 == ""){t4 = 0;}else{t4 = t4*0.01}
		if(t5 == ""){t5 = 0;}else{t5 = t5*0.01}
		//console.log(t1+"|"+t2+"|"+t3+"|"+t4+"|"+t5+"|");
		var va = (t1*t2*t4+t5)*100;
		$("#fee1").val(va.toFixed(4));
		var va = (t1*(t2+t3)*t4+t5)*100;
		$("#fee2").val(va.toFixed(4));
	}
	
	// 个人居间费率
	function syl(cellvalue, options, rowObject){
		var issuanceCostRate = rowObject.issuanceCostRate;
		var serviceChargeCoefficient = rowObject.serviceChargeCoefficient;
		var timeCoefficient = rowObject.timeCoefficient;
		var incentiveFeeRate = rowObject.incentiveFeeRate;
		if(issuanceCostRate == ""){issuanceCostRate = 0;}else{issuanceCostRate = issuanceCostRate*0.01}
		if(serviceChargeCoefficient == ""){serviceChargeCoefficient = 0;}else{serviceChargeCoefficient = serviceChargeCoefficient*0.01}
		if(timeCoefficient == ""){timeCoefficient = 0;}else{timeCoefficient = timeCoefficient*0.01}
		if(incentiveFeeRate == ""){incentiveFeeRate = 0;}else{incentiveFeeRate = incentiveFeeRate*0.01}
		//console.log(t1+"|"+t2+"|"+t3+"|"+t4+"|"+t5+"|");
		//alert(issuanceCostRate+"---"+"---"+serviceChargeCoefficient+"---"+orgServiceChargeCoefficient)
		var va = (issuanceCostRate*serviceChargeCoefficient*timeCoefficient+incentiveFeeRate)*100;
		return va.toFixed(4);
	}
	
	// 机构居间费率
	function syl2(cellvalue, options, rowObject){
		var issuanceCostRate = rowObject.issuanceCostRate;
		var serviceChargeCoefficient = rowObject.serviceChargeCoefficient;
		var orgServiceChargeCoefficient = rowObject.orgServiceChargeCoefficient;
		var timeCoefficient = rowObject.timeCoefficient;
		var incentiveFeeRate = rowObject.incentiveFeeRate;
		if(issuanceCostRate == ""){issuanceCostRate = 0;}else{issuanceCostRate = issuanceCostRate*0.01}
		if(serviceChargeCoefficient == ""){serviceChargeCoefficient = 0;}else{serviceChargeCoefficient = serviceChargeCoefficient*0.01}
		if(orgServiceChargeCoefficient == ""){orgServiceChargeCoefficient = 0;}else{orgServiceChargeCoefficient = orgServiceChargeCoefficient*0.01}
		if(timeCoefficient == ""){timeCoefficient = 0;}else{timeCoefficient = timeCoefficient*0.01}
		if(incentiveFeeRate == ""){incentiveFeeRate = 0;}else{incentiveFeeRate = incentiveFeeRate*0.01}
		//console.log(t1+"|"+t2+"|"+t3+"|"+t4+"|"+t5+"|");
		//alert(issuanceCostRate+"---"+"---"+serviceChargeCoefficient+"---"+orgServiceChargeCoefficient)
		var coefficient = serviceChargeCoefficient-(-orgServiceChargeCoefficient);
		var va = (issuanceCostRate*coefficient*timeCoefficient+incentiveFeeRate)*100;
		return va.toFixed(4);
	}

</script>
</head>
<body class="skinMain">
	<table  width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain" align="left">
		<tr>
			<td width="100%" valign="top" >
				<form id="commissionRatio" name="commissionRatio">
					<input type="hidden" name="id" value="${subProduct.id}"/>
					<table id="commissionRatioList" width="100%">
						<tr><td>&nbsp;</td></tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
	<div class="toolbar">
		<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" onclick="window.location.href='${base}${market_salemanage_saleconfig.controller}?c=${market_salemanage_saleconfig.code}&id=${subProduct.productId}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
		</div>
		<div class="right"></div>
	</div>
</body>
</html>
