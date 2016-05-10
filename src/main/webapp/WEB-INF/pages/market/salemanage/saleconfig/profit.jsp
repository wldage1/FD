<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
var layers = 0;
var st = "";
var dis = false;
$(document).ready(function() {
	$(window).resize(function(){ 　　
		$("#productProfitList").setGridWidth($(window).width()*0.999);　
	});

	
	$("BODY").data("valid_3","最小投资金额为一位以上的数字");
	$("BODY").data("valid_4","最大投资金额为数字");
	$("BODY").data("check_valid_4","最大投资金额不能小于最小投资金额");
	$("BODY").data("valid_5","生效日期不能为空");
	
	var productType = "${subProduct.productType}";
	if(productType != "2" && productType != "5"){
		st = "display:none";
		dis = true;
	}
	
	/* 页面初始化*/

	//加载收益率维护列表
	jQuery("#productProfitList").jqGrid({
    	datatype: "local",
        colNames: ["<msg:message code='subProduct.profit.minShareThreshold'/><font color=\"red\">&nbsp;*&nbsp;</font>(万元)", 
		           "<msg:message code='subProduct.profit.maxShareThreshold'/><font color=\"red\">&nbsp;&nbsp;</font>(万元)",
                   "<msg:message code='subProduct.profit.profitRatio'/><font color=\"red\">&nbsp;*&nbsp;</font>",
                   "<msg:message code='subProduct.profit.effectiveDate'/><font color=\"red\">&nbsp;*&nbsp;</font>",
                   "<msg:message code='info.operate'/>",""
                   ],
        colModel: [
                   {name: 'minShareThreshold',index:'minShareThreshold',width:'10%',align:'center',hidden: false,sortable:false},
				   {name: 'maxShareThreshold',index:'maxShareThreshold',width:'10%',align:'center',hidden: false,sortable:false},
                   {name: 'profitRatio',index:'profitRatio',width:'62%',align:'center',hidden: false,sortable:false,formatter:ratioAct},
                   {name: 'effectiveDate',index:'effectiveDate',width:'10%',align:'center',hidden: dis,sortable:false},
                   {name: 'operate',index:'operate',width:'8%',align:'left',hidden: false,sortable:false,formatter:initAct},
				   {name: 'profitRatio',index:'profitRatio',hidden: true},
                   ],
		mtype:"POST",
        height:'<msg:message code='jqgrid.height.250'/>',
        autowidth: true,
		jsonReader: {
			repeatitems: false
		},
        caption:'<msg:message code="subProduct.profit.list"/>',
		toolbar: [true,"top"]
    });
	
	// 创建
	$("#shortcut_add").live("click",function(){
		editPage('');
	});

	// 编辑
	$(".shortcut_modify").live("click",function(){
		var rowid = $(this).attr("id");
		var rowdata = jQuery("#productProfitList").jqGrid('getRowData',rowid);
		var profitRatio = rowdata.profitRatio;
		editPage(rowid);
		$("input[name='minShareThreshold']").val(rowdata.minShareThreshold);
		$("input[name='maxShareThreshold']").val(rowdata.maxShareThreshold);
		$("input[name='effectiveDate']").val(rowdata.effectiveDate);
		var tr_profitRatio =  new Array();
		tr_profitRatio = profitRatio.split("#");
		$.each(tr_profitRatio,function(i,v){
			if(i == 0){
				$("input[name='pre_profitRatio']").eq(0).val(v);
			}else{
				$(".tr-profitRatio").eq(i-1).after('<tr class="tr-profitRatio"><td></td><td><input type="text" name="pre_profitRatio" value="'+v+'"/><img id="delRow" src="${base}/${skin}/images/icon/reduce.png" width="20" height="20" align="absmiddle" style="cursor:pointer;"/></td></tr>');
			}
		});
    });
	
	// 增加行
	$("#addRow").live("click",function(){
		var tr_profitRatio = $("input[name='pre_profitRatio']");
		var lt = tr_profitRatio.length;
		$(".tr-profitRatio").eq(lt-1).after('<tr class="tr-profitRatio"><td></td><td><input type="text" name="pre_profitRatio" value=""/><img id="delRow" src="${base}/${skin}/images/icon/reduce.png" width="20" height="20" align="absmiddle" style="cursor:pointer;"/></td></tr>');
	});
	
	// 增加行
	$("#delRow").live("click",function(){
		$(this).parent().parent().remove();
	});
	
	// 删除
	$(".shortcut_delete").live("click",function(){
		var rowid = $(this).attr("id");
		if(rowid == ""){
			top.layer.msg("<msg:message code='info.prompt.delete'/>");
			return;
		}
		var data="c=${c}&id="+rowid+"";
		layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
			jQuery.ajax({
				url:"${base}/market/salemanage/saleconfig/profit/delete.json",
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
	$("#t_productProfitList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea)); 
		
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
	
	// 格式化
	function ratioAct(cellvalue, options, rowObject){
		return cellvalue.replaceAll("#","&nbsp;,&nbsp;");
	}

	// 重新加载列表数据
	function reload(){
		jQuery("#productProfitList").jqGrid("setGridParam",{url:'profitdata.json',datatype: "json",postData:{c:"${c}",subProductId:"${subProduct.id}"}}).trigger("reloadGrid");
	}
	
	// 数字
	function checkNumber(evt,object,flag){
		var key = window.event?event.keyCode:evt.which;
		var re = /^-?[1-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
		if(key != 8){
			var value = $(object).val();
			if(!re.test(value)){
				if("paste" == flag){
					$(object).val("");
				}else{
					if(value != null && value != ""){
						$(object).val(value.substring(0,value.length-1));
					}
				}
			}
		}
	}
	
	// 保存收益率
	function save_profit(){
		var status = false;
		var form = $("#edit_form").serializeArray();
		var profitRatio = "";
		$.each(form, function(i, item) {
			if(i == 3 || i == 4){
				var re = /^-?[0-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
				if(form[3].value == "" || !re.test(form[3].value)){
					$("#error").text($("BODY").data("valid_3"));
					status = true;
					return false;
				}else if(form[4].value != "" && !re.test(form[4].value)){
					$("#error").text($("BODY").data("valid_4"));
					status = true;
					return false;
				}else if(form[4].value != "" && (parseInt(form[3].value) > parseInt(form[4].value))){
					$("#error").text($("BODY").data("check_valid_4"));
					status = true;
					return false;
				}
			}else if(!dis && i == 5 && form[5].value == ""){
				$("#error").text($("BODY").data("valid_5"));
				status = true;
				return false;
			}
			if(item.name=="pre_profitRatio" && $.trim(item.value) != ""){
				profitRatio += ($.trim(item.value)+"#");
			}
			$("#error").text('');
		});
		
		if(status){
			$("#tr_error").css("background-color","red");
			return false;
		}else{
			$("#tr_error").css("background-color","white");
		}

		profitRatio = profitRatio.substring(0,profitRatio.length-1);
		if(profitRatio == ""){
			$("#tr_error").css("background-color","red");
			$("#error").text('收益率描述不能空');
			return false;
		}else{
			$("#profitRatio").val(profitRatio);
			$("#tr_error").css("background-color","white");
		}
		
		jQuery.ajax({
			url:"${base}/market/salemanage/saleconfig/profit/save.json",
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
			title: '收益率设置',
			//shadeClose: true, 
			closeBtn: [0,true],
			border : [0, 0.5, '#666', true],
			offset: ['100px',''],
			move: ['.xubox_title', true],
			area: ['300px','auto'],
			page: {
				html: '<form id="edit_form">'
				+'<input type="hidden" name="id" value="'+rowid+'"/>'
				+'<input type="hidden" name="subProductId" value="${subProduct.id}"/>'
				+'<input type="hidden" name="profitRatio" id="profitRatio"/>'
				+'<table width="100%" border="0" cellspacing="3" cellpadding="0" class="skinMain" align="left">'
				+'<tr><td colspan="2" align="center" id="tr_error" style="color:#FFF;"><font id="error"></font></td><tr>'
				+'<tr><td align="right">最小投资金额(万元)：</td><td><input type="text" name="minShareThreshold"/><font color=\"red\">&nbsp;*&nbsp;</font></td><tr>'
				+'<tr><td align="right">最大投资金额(万元)：</td><td><input type="text" name="maxShareThreshold"/><font color=\"red\">&nbsp;&nbsp;</font></td><tr>'
				+'<tr style="'+st+'"><td align="right">生效日期：</td><td><input type="text" name="effectiveDate" onclick="WdatePicker();" class="Wdate" readonly="readonly"/></td><tr>'
				+'<tr class="tr-profitRatio"><td align="right">收益率描述：</td><td sytle="border:1px solid #000;"><input type="text" name="pre_profitRatio" /><img id="addRow" src="${base}/${skin}/images/icon/Add.png" width="20" height="20" align="absmiddle" style="cursor:pointer;"/></td><tr>'
				+'<tr><td colspan="2"><center><input type="button" class="btn2" onclick="save_profit();" value="确认" id="btnback"/></center></td><tr>'
				+'</table></form>'
			}
    	});
	}
	

</script>
</head>
<body class="skinMain">
	<table  width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain" align="left">
		<tr>
			<td width="100%" valign="top" >
				<form id="productProfit" name="productProfit">
					<input type="hidden" name="id" value="${subProduct.id}"/>
					<table id="productProfitList" width="100%">
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
