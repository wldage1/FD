<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.promotion/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	$(window).resize(function(){
		$("#promotionList").setGridWidth($(window).width()*0.999);
	});
	var currcount=0; //一次只允许操作一条记录
	var currPosition; //  记录当前所在的位置选项
	var modifyPositionOrderValue;//记录点击排序时修改的值；
	var rowicon = "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>";
	$(document).ready(function(){
		$('#positionId')[0].selectedIndex = 0;
		currPosition = $('#positionId').val();
		jQuery("#promotionList").jqGrid({
			url: 'promotion/grid.json',
			datatype: 'json',
			colNames: [	"",
			           	"",
						"",
						"<msg:message code='product.shortName'/>",
						"<msg:message code='provider.shortName'/>",
						"<msg:message code='product.sellStatus'/>",
						"<msg:message code='product.sortNumber'/>",
						"<msg:message code='info.operate'/>"],
			colModel: [{name: 'sellStatus',index: 'sellStatus',hidden: true},
			           {name: 'id',index: 'id',hidden: true},
					   {name: 'positionMappingId',index: 'positionMappingId',hidden: true}, 			
					   {name: 'productName',index: 'productName',width:'20%',align:'left',hidden: false,sortable: false}, 
					   {name: 'providerShortName',index: 'providerShortName',width:'20%',align:'left',hidden: false,sortable: false}, 
					   {name: 'sellStatus',index: 'sellStatus',width:'20%',align:'left',hidden: false,sortable: false,formatter:function(cellvalue, options, rowdata){
					    	<c:forEach items="${sellStatusList}" var="it">
						    	if(cellvalue==${it.key}){
						    		return '${it.value}';
						    	}
					    	</c:forEach>
					   }},
					   {name: 'positionOrder',index:'positionOrder',width:'20%',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowdata){
							//--
								var positionOrder = rowdata.positionOrder;
								if(positionOrder != null){
									return "<div id='positionOrder_"+rowdata.id+"'><input type='text' id='positionOrder_input_"+rowdata.id+"' disabled='disabled' value="+rowdata.positionOrder+" onkeyup=\"checkNumber(event,this,'edit');\" onfocusout=\"checkNumber(event,this,'paste');\" onpropertychange=\"checkNumber(event,this,'edit');\" oninput=\"checkNumber(event,this,'edit');\"  style='width:97%;'/><div>";
								}else{
									return "<div id='positionOrder_"+rowdata.id+"'>--<div>";
								}
							//--
							}
					   },
					   {name:'act',index:'act', width:'20%',align:'center',sortable:false,sortable: false,formatter:function(cellvalue, options, rowdata){
							//--
								var id = rowdata.id;
								var positionMappingId = rowdata.positionMappingId;
								var content = "";
								var space = "&nbsp;&nbsp;";
								
								var first = "推荐";
								var third = "取消";
								var remove = "";
								var rstyle= "style='color:#999;'";
								
								if(positionMappingId != null){
									first = "排序";
									third = "撤销";
									remove = "shortcut_remove";
									rstyle = "";
								}
								if(rowdata.sellStatus==5){
									// 推荐
									content += "<a href='javascript:void(0);' style='color:#999;' title='"+first+"'>"+rowicon;
									content +=  first;
									content += "</a>";
									
									// 保存
									content += "<a href='javascript:void(0);' style='color:#999;' title='保存'>"+rowicon;
									content += "保存";
									content += "</a>";
									
									// 取消
									content += "<a href='javascript:void(0);' id='shortcut_reset_"+id+"' sid="+id+" "; 
									content += "class='"+remove+"' title='"+third+"'";
									content += ""+rstyle+">"+rowicon;
									content +=  third;
									content += "</a>";
								}else {
									// 推荐
									content += "<a href='javascript:void(0);' id='shortcut_promotion_"+id+"' sid="+id+" class='shortcut_promotion' title='"+first+"'>"+rowicon;
									content +=  first;
									content += "</a>";
									
									// 保存
									content += "<a href='javascript:void(0);' id='shortcut_save_"+id+"' sid="+id+" class='' style='color:#999;' title='保存'>"+rowicon;
									content += "保存";
									content += "</a>";
									
									// 取消
									content += "<a href='javascript:void(0);' id='shortcut_reset_"+id+"' sid="+id+" "; 
									content += "class='"+remove+"' title='"+third+"'";
									content += ""+rstyle+">"+rowicon;
									content +=  third;
									content += "</a>";
								}
								// 撤销推荐
								return content;
							//--
							}
					   }
					   ],
			mtype:"POST",
			postData:{c:"${promotion.c}",positionId:$("#positionId").val()},
			rowNum:<msg:message code='jqgrid.row.num.20'/>,
			page:"${promotion.page}",
			rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#pagered',
			scroll:true,
			height:<msg:message code='jqgrid.height.300'/>,
			autowidth: true,
			viewrecords: true,
			multiselect: false,
			jsonReader: {
				repeatitems: false
			},
			caption:'<msg:message code="product.position.info"/>'
		});
		
		$(".shortcut_promotion").live("click",(function(){
			var rowid = $(this).attr("sid");
			var rowdata = jQuery("#promotionList").jqGrid('getRowData',rowid);
			var positionMappingId = rowdata.positionMappingId;
				if(currcount == 0){
					currcount = 1;
					jQuery("#promotionList").jqGrid('editRow',rowid);
					$(this).attr("class","").css("color","#999");
					$("#shortcut_save_"+rowid).attr("class","shortcut_save").css("color","#53056A");
					$("#shortcut_reset_"+rowid).attr("class","shortcut_reset").css("color","#53056A");
					$("#shortcut_reset_"+rowid).html(rowicon+"取消");
					$("#shortcut_reset_"+rowid).attr("title","取消")
					// 释放排序控件
					if(positionMappingId == ''){
						modifyPositionOrderValue = $("#positionOrder_"+rowid).text();
						$("#positionOrder_"+rowid).html("<input type='text' id='positionOrder_input_"+rowid+"' onkeyup=\"checkNumber(event,this,'edit');\" onfocusout=\"checkNumber(event,this,'paste');\" onpropertychange=\"checkNumber(event,this,'edit');\" oninput=\"checkNumber(event,this,'edit');\"  style='width:97%;'/>");
					}else{
						modifyPositionOrderValue = $("#positionOrder_input_"+rowid+"").val();
						$("#positionOrder_input_"+rowid+"").attr("disabled",false);
					}
					
				}else{
					top.layer.msg("<msg:message code="dictionaryItem.notsave"/>");
				}
			})
		);
		
		$(".shortcut_reset").live("click",( function() {
			currcount = 0;
			var rowid = $(this).attr("sid");
			var rowdata = jQuery("#promotionList").jqGrid('getRowData',rowid);
				jQuery("#promotionList").jqGrid('restoreRow',rowid);
				$(this).attr("class","").css("color","#999");	
				$("#shortcut_save_"+rowid).attr("class","").css("color","#999");	
				$("#shortcut_promotion_"+rowid).attr("class","shortcut_promotion").css("color","#53056A");
				if(rowdata.positionMappingId == ''){
					$("#positionOrder_"+rowid).html(modifyPositionOrderValue);
				}else{
					$("#positionOrder_input_"+rowid+"").val(modifyPositionOrderValue);
					$("#positionOrder_input_"+rowid+"").attr("disabled",true);
					$("#shortcut_reset_"+rowid).attr("class","shortcut_remove").css("color","#53056A");
					$("#shortcut_reset_"+rowid).html(rowicon+"撤消")
					$("#shortcut_reset_"+rowid).attr("title","撤消")
				}
			})
		);
		
		$(".shortcut_save").live("click",(function() {
			var rowid = $(this).attr("sid");
			var rowdata = jQuery("#promotionList").jqGrid('getRowData',rowid);
			var positionOrderValue=$("#positionOrder_input_"+rowid).val();
			if(positionOrderValue == null || positionOrderValue == ''){
				top.layer.msg("<msg:message code='positionOrder.value.empty'/>");
				return;
			}
			var data="c=${product_promotion.code}&productId="+rowid+"&positionId="+currPosition+"&positionOrder="+$("#positionOrder_input_"+rowid).val()+"&id="+rowdata.positionMappingId+"";
			jQuery.ajax({
				url:"${base}/product/promotion/updateorder.json",
				type: "post",
				contentType : 'application/x-www-form-urlencoded',
				data:data, 
				dataType : 'json',
				success:function(jsonData){
					if (jsonData.status == "success"){
						currcount = 0;
						jQuery("#promotionList").jqGrid("setGridParam",{postData:{positionId:currPosition}}).trigger("reloadGrid"); 					
					}
				}
			});
		}));
		
		$(".shortcut_remove").live("click",(function() {
			var rowid = $(this).attr("sid");
			var rowdata = jQuery("#promotionList").jqGrid('getRowData',rowid);
			var data="c=${product_promotion.code}&id="+rowdata.positionMappingId+"";
			var layers = top.layer.confirm("<msg:message code='product.position.remove.confirm'/>",  function(){
				jQuery.ajax({
					url:"${base}/product/promotion/deletemapping.json",
					type: "post",
					contentType : 'application/x-www-form-urlencoded',
					data:data, 
					dataType : 'json',
					success:function(jsonData){
						if (jsonData.status == "success"){
							currcount = 0;
							jQuery("#promotionList").jqGrid("setGridParam",{postData:{positionId:currPosition}}).trigger("reloadGrid");
							top.layer.close(layers);							
						}
					}
				});
			});
		}));
		
		jQuery("#promotionList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");

	});
	function changePosition(obj){
		if(currcount == 0){
			currPosition = $(obj).val();
			jQuery("#promotionList").jqGrid("setGridParam",{postData:{positionId:$("#positionId").val()}}).trigger("reloadGrid"); 
		}else{
			$(obj).val(currPosition);
			top.layer.msg("<msg:message code="dictionaryItem.notsave"/>");
		}
	};
	
	// 数字
	function checkNumber(evt,object,flag){
		var key = window.event?event.keyCode:evt.which;
		var re = /^[0-9]*[1-9][0-9]*$/;
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

</script>
<style>
	.order_disalter{border:0px;}
</style>
</head>
<body class="skinMain">
<table cellpadding="2" cellspacing="2" border="0" width="100%">
	<tr>
		<td width="15%"  valign="top">
			<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_dictionaryList" dir="ltr" style="width: 208px; ">
				<div class="ui-jqgrid-view" id="gview_dictionaryList" style="width: 208px; ">
					<div class="ui-jqgrid-view" id="gview_dictionaryList" style="width: 208px; ">
					<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix"><msg:message code='product.position.select'/></div>
					<div style="width: 208px; height:383px; background:#FFF;" class="ui-state-default ui-jqgrid-hdiv"><div class="ui-jqgrid-hbox" >
					<form:form commandName="promotion">
					<form:select path="positionId" id="positionId" items="${positionList}" itemValue="id" itemLabel="name" onChange="changePosition(this);" size="${positionListSize}" style="width:208px;border:0px;font-size:10pt;"/>
					</form:form>
					</div>
					</div>
					</div>
				</div>
			</div>
		</td>
		<td width="85%" valign="top">
			<table id="promotionList"><tr><td>&nbsp;</td></tr></table>	
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</body>
</html>