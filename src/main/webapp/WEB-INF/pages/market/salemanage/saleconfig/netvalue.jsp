<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
	var isEdit = false; //  是否处于编辑状态
	var selrow = -1; // 当前页面记录条数
	var lastsel; // 最后编辑的行
	var rowicon = "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>";
$(document).ready(function() {

	$(window).resize(function(){ 　　
		$("#productNetValueList").setGridWidth($(window).width()*0.999);　
	});

	/* 页面初始化*/

	//加载收益率维护列表
	jQuery("#productNetValueList").jqGrid({
		url:"${base}/market/salemanage/saleconfig/netvalue/grid.json",
    	datatype: "json",
        colNames: ["",
				   "<msg:message code='subProduct.netValue.openDay'/><font color=\"red\">&nbsp;*&nbsp;</font>", 
                   "<msg:message code='subProduct.netValue.netValue'/><font color=\"red\">&nbsp;*&nbsp;</font>",
                   "<img id='addRow' src='${base}/${skin}/images/icon/Add.png' width='20' height='20' onclick='addRowData();' style='cursor:pointer;'/>"
                   ],
        colModel: [{name: 'id',index:'id',hidden: true},
                   {name: 'openDay',index:'openDay',width:'45%',align:'left',hidden: false,sortable:false,editable:true},
                   {name: 'netValue',index:'netValue',width:'40%',align:'left',hidden: false,sortable:false,editable:true},
                   {name: 'operate',index:'operate',width:'15%',align:'center',hidden: false,sortable:false}
                   ],
		mtype:"POST",
		postData:{c:"${c}",subProductId:${subProduct.id}},
		rowNum:13,     
		page:"${subProduct.page}",
		pager: '#pagered',
		height:<msg:message code='jqgrid.height.300'/>,
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		jsonReader: {
			repeatitems: false
		},
		gridComplete: function(){ 
			var ids = jQuery("#productNetValueList").jqGrid('getDataIDs'); 
			for(var i=0;i < ids.length;i++){ 
				var cl = ids[i];
				var rowdata = jQuery("#productNetValueList").jqGrid('getRowData',cl);
				be = "<a href='javascript:void(0);' id='shortcut_modify_"+cl+"' sid="+cl+" ro="+rowdata.openDay+" rn="+rowdata.netValue+" class='shortcut_modify'>"+rowicon+"修改</a>"; 
				se = "<a href='javascript:void(0);' id='shortcut_save_"+cl+"' sid="+cl+"  ro="+rowdata.openDay+" rn="+rowdata.netValue+" class='' style='color:#999;'>"+rowicon+"保存</a>"; 
				ce = "<a href='javascript:void(0);' id='shortcut_reset_"+cl+"' sid="+cl+"  ro="+rowdata.openDay+" rn="+rowdata.netValue+" class='' style='color:#999;'>"+rowicon+"取消</a>"; 
				de = "<a href='javascript:void(0);' id='shortcut_remove_"+cl+"' sid="+cl+"  ro="+rowdata.openDay+" rn="+rowdata.netValue+" class='shortcut_remove' style='color:#53056A;'>"+rowicon+"删除</a>"; 
				jQuery("#productNetValueList").jqGrid('setRowData',ids[i],{operate:be+se+ce+de});
			} 
		},
        caption:'<msg:message code="subProduct.netValue.list"/>'
    });
	
	// 行操作事件
		
		// 修改事件
		$(".shortcut_modify").live("click",function(){
			if(isEdit == true){
				top.layer.msg("<msg:message code="dictionaryItem.notsave"/>");
				return;
			}
			isEdit = true;
			var rowid = $(this).attr("sid");
			var ro = $(this).attr("ro");
			var rn = $(this).attr("rn");
			
			// 准备行数据
			var openDayHTML = '<input class="Wdate" id="'+rowid+'_openDay" submitName="openDay" role="textbox" style="width: 98%;" onclick="WdatePicker();" readOnly="readonly" value="'+ro+'"/>';
			var netValueHTML = '<input class="editable" id="'+rowid+'_netValue" submitName="netValue" role="textbox" style="width: 98%;" value="'+rn+'" onkeyup="checkNumber(event,this,\'edit\');" onpropertychange="checkNumber(event,this,\'edit\');" onfocusout="checkNumber(event,this,\'paste\');" oninput="checkNumber(event,this,\'edit\');"/>';
			// 编辑行
			jQuery("#productNetValueList").jqGrid('editRow',rowid);
			jQuery("#productNetValueList").jqGrid('setRowData',rowid,{openDay:openDayHTML,netValue:netValueHTML});
			//$("#"+rowid+"_openDay").attr({"class":"Wdate","onclick":"WdatePicker();","readonly":"readonly"});
			//$("#"+rowid+"_netValue").attr({"onkeyup":"checkNumber(event,this,'edit');","onfocusout":"checkNumber(event,this,'paste');","onpropertychange":"checkNumber(event,this,'edit');","oninput":"checkNumber(event,this,'edit');"});
			// 设置按钮
			$(this).attr("class","").css("color","#999");
			$("#shortcut_save_"+rowid).attr("class","shortcut_save").css("color","#53056A");
			$("#shortcut_reset_"+rowid).attr("class","shortcut_reset").css("color","#53056A");
			$("#shortcut_remove_"+rowid).attr("class","").css("color","#999");
			lastsel = rowid;
		});
		
		// 取消事件
		$(".shortcut_reset").live("click",function(){
			var rowid = $(this).attr("sid");
			// 编辑行
			jQuery("#productNetValueList").jqGrid('restoreRow',rowid);
			// 设置按钮
			$(this).attr("class","").css("color","#999");
			$("#shortcut_modify_"+rowid).attr("class","shortcut_modify").css("color","#53056A");
			$("#shortcut_save_"+rowid).attr("class","shortcut_save").css("color","#999");
			$("#shortcut_remove_"+rowid).attr("class","shortcut_remove").css("color","#53056A");
			isEdit = false;
		});
		
		// 保存事件
		$(".shortcut_save").live("click",function(){
			var rowid = $(this).attr("sid");
			var openDay = $("#"+rowid+"_openDay").val();
			var netValue = $("#"+rowid+"_netValue").val();
			
			if(openDay == '' || netValue == ''){
				return false;
			}
			
			if(!chnum(netValue)){
				return false;
			}
			
			// 设置按钮
			$(this).attr("class","").css("color","#999");
			$("#shortcut_modify_"+rowid).attr("class","shortcut_modify").css("color","#53056A");
			$("#shortcut_reset_"+rowid).attr("class","shortcut_save").css("color","#999");
			$("#shortcut_remove_"+rowid).attr("class","shortcut_remove").css("color","#53056A");
			
			var data="c=${product_product_netvalue.code}&subProductId=${subProduct.id}&openDay="+openDay+"&netValue="+netValue+"";
			if(rowid >0){
				data+="&id="+rowid;
			}
			// 保存行数据
			jQuery.ajax({
				url:"${base}/market/salemanage/saleconfig/netvalue/save.json",
				type: "post",
				contentType : 'application/x-www-form-urlencoded',
				data:data, 
				dataType : 'json',
				success:function(data){
					if(data.status == 'success'){
						isEdit = false;
						var page = jQuery('#productNetValueList').jqGrid('getGridParam','page');
						jQuery("#productNetValueList").setGridParam({page:page,c:"${c}",subProductId:${subProduct.id}}).trigger("reloadGrid");
						top.$.layer({
							area : ['auto','auto'],
							dialog : {msg:'<msg:message code='info.operate.success'/>',type : 9}	
						});
					}else{
						top.layer.msg("error");
					}
				}
			});
		});
		
		// 删除事件
		$(".shortcut_remove").live("click",function(){
			// 删除行，并且清除文件
			var rowid = $(this).attr("sid");
			if(rowid < 0){
				jQuery("#productNetValueList").jqGrid('delRowData',rowid); 
				isEdit = false;
			}else{
				if(isEdit == true){
					top.layer.msg("<msg:message code="dictionaryItem.notsave"/>");
					return;
				}
				var data="c=${product.c}&id="+rowid+"";
				var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
					jQuery.ajax({
						url:"${base}/market/salemanage/saleconfig/netvalue/delete.json",
						type: "post",
						contentType : 'application/x-www-form-urlencoded',
						data:data, 
						dataType : 'json',
						success:function(jsonData){
							if (jsonData.status == "success"){
								isEdit = false;
								var page = jQuery('#productNetValueList').jqGrid('getGridParam','page');
								jQuery("#productNetValueList").setGridParam({page:page,c:"${c}",subProductId:${subProduct.id}}).trigger("reloadGrid");
								top.layer.close(layers);							
							}
						}
					});
				});
			}
		});
		//-- 行操作事件

});
	
	// 行数据
	function addRowData(){
		if(isEdit == true){
			top.layer.msg("<msg:message code="dictionaryItem.notsave"/>");
			return;
		}
		isEdit = true;
		// 还原当前编辑行
		jQuery("#productNetValueList").jqGrid('restoreRow',lastsel);
		var data={id:selrow};
		jQuery("#productNetValueList").jqGrid('addRowData',selrow,data);
		// 插入首行
		var current = $("tr[id='"+selrow+"']");
		var prev = $(".jqgfirstrow");
		current.insertAfter(prev);
		
		// 准备行数据
		var openDayHTML = '<input class="Wdate" id="'+selrow+'_openDay" submitName="openDay" role="textbox" style="width: 98%;" onclick="WdatePicker();" readOnly="readonly"/>';
		var netValueHTML = '<input class="editable" id="'+selrow+'_netValue" submitName="netValue" role="textbox" style="width: 98%;" onkeyup="checkNumber(event,this,\'edit\');" onpropertychange="checkNumber(event,this,\'edit\');" onfocusout="checkNumber(event,this,\'paste\');" oninput="checkNumber(event,this,\'edit\');"/>';
		
		jQuery('#productNetValueList').editRow(selrow);
		jQuery("#productNetValueList").jqGrid('setRowData',selrow,{openDay:openDayHTML,netValue:netValueHTML});
		//$("#"+selrow+"_openDay").attr({"class":"Wdate","onclick":"WdatePicker();","readonly":"readonly"});
		//$("#"+selrow+"_netValue").attr({"onkeyup":"checkNumber(event,this,'edit');","onfocusout":"checkNumber(event,this,'paste');","onpropertychange":"checkNumber(event,this,'edit');","oninput":"checkNumber(event,this,'edit');"});
		// 设置按钮
		$("#shortcut_modify_"+selrow).attr("class","shortcut_modify").css("color","#999");
		$("#shortcut_save_"+selrow).attr("class","shortcut_save").css("color","#53056A");
		$("#shortcut_reset_"+selrow).attr("class","").css("color","#999");
		$("#shortcut_remove_"+selrow).attr("class","shortcut_remove").css("color","#53056A");
		
		selrow--;
	 };
	 
	// 数字
	function checkNumber(evt,object,flag){
		var key = window.event?event.keyCode:evt.which;
		//var re = /^-?[1-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
		var re = /^-?[0-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
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
	
	function chnum(object){
		var re = /^\d+(\.\d+)?$/;
		if(!re.test(object)){
			layer.msg("请输入有效的净值,净值为数字类型");
			return false;
		}else{
			return true;
		}
	}

</script>
</head>
<body class="skinMain">
	<table  width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain" align="left">
		<tr>
			<td width="100%" valign="top" >
				<form id="productNetValue" name="productNetValue">
					<input type="hidden" name="id" value="${subProduct.id}"/>
					<table id="productNetValueList" width="100%">
						<tr><td>&nbsp;</td></tr>
					</table>
					<div id="pagered"></div>
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
