<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#categoryList").setGridWidth($(window).width()*0.999);　
});
var count = 0;   //用于计数
var currcount=0; //一次只允许操作一条记录
var dictType;
var count_1 = 0; //tab_1 全局变量
var currcount_1 = 0;
//#tab_1
$(document).ready(function(){
	var options = {
		event:'focus',
		ajax:{
			url:'${base}/extension/youku_category/valid.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: $("form#category").serialize(), 
			cache:false
		},
		poshytip:{
			alignX: 'right'
		}
	};
    var valid = new $.Valid(options);
    var select  = "0:否;1:是";
    jQuery("#categoryList").jqGrid({
        url: 'youku_category/grid.json',
        datatype: 'json',
        colNames: ["<msg:message code='category.name'/>", 
                   "<msg:message code='category.code'/>",
                   "<msg:message code='category.isRelate' />",
                   "<msg:message code='category.isPwdControl' />",
                   "<msg:message code='info.operate'/>",
                   "<img id='addRow' src='${base}/${skin}/images/icon/Add.png' width='20' height='20' />"
                   ],
        colModel: [{name: 'name',index:'name',width:'33%',align:'center',sortable:false,editable:true},
                   {name: 'code',index:'code',width:'34%',align:'center',sortable:false,editable:true},
                   {name: 'isRelate',index:'isRelate',width:'10%',align:'center',sortable:false,editable:true,edittype:'select',editoptions:{value:select},formatter:changeToC},
                   {name: 'isPwdControl',index:'isPwdControl',width:'10%',align:'center',sortable:false,formatter:changeToC,edittype:'select',editoptions:{value:select},editable:true},
                   {name:'operate',index:'operate', width:'30%',align:'left',sortable:false},
                   {name:'del',index:'del',width:'3%',align:'center',sortable:false}
                   ],
        postData:{c:"${category.c}"},
        mtype:"POST",
        height:<msg:message code='jqgrid.height.250'/>,
        pager: false,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete: function(){
    		var ids = jQuery("#categoryList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			count_1 = id;
    			var rowdata = jQuery("#categoryList").jqGrid('getRowData',id);
    			var content = "";
				
    			<c:if test="${!empty extension_youku_category_modify}">
						<security:authorize ifAnyGranted="${extension_youku_category_modify.code}">	
						content += "<a href='javascript:void(0);' style='color:#53056A' id='shortcut_extension_youku_category_modify_"+id+"' sid="+id+" class='shortcut_extension_youku_category_modify'>";
						content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${extension_youku_category_modify.name}";
						content += "</a>";

						content += "<a href='javascript:void(0);' id='shortcut_extension_youku_category_save_"+id+"' sid="+id+" class='' style='color:#999;' method celcalue='' >";
						content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${extension_youku_category_save.name}";
						content += "</a>";

						content += "<a href='javascript:void(0);' id='shortcut_extension_youku_category_reset_"+id+"' sid="+id+" class='' style='color:#999;'>";
						content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${extension_youku_category_reset.name}";
						content += "</a>";
						</security:authorize>
				</c:if> 
   				jQuery("#categoryList").jqGrid('setRowData',ids[i],{operate:"<div class='jqgridContainer'>" + content + "</div>"});
   				<c:if test="${!empty extension_youku_category_delete}">
				<security:authorize ifAnyGranted="${extension_youku_category_delete.code}">
				jQuery("#categoryList").jqGrid('setRowData',ids[i],{del:"<a href='javascript:void(0);' id='shortcut_${extension_youku_category_delete.indexCode}_"+id+"' sid="+id+" class='shortcut_${extension_youku_category_delete.indexCode}' method><img src='${base}/${skin}/images/icon/Del.png' width='20' height='20' /></a>"});
				</security:authorize>
				</c:if>
    		}	
    	},
        caption:'<msg:message code="video_category.list"/>',
        toolbar: [true,"top"]
	});
	jQuery("#categoryList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	
	$('#addRow').click(function(){
		jQuery("#categoryList").setColProp('isRelate',{formatter:""});
  		jQuery("#categoryList").setColProp('isPwdControl',{formatter:""});
		count_1++;
		if(currcount_1 == 0){
			currcount_1 = 1;
			jQuery("#categoryList").jqGrid('addRowData',count_1,"{}");
			var data = {
					name:"<input name='name' id='"+count_1+"_name' maxlength='20' class='valid-control' style='width:98%;'/>",
					code:"<input name='code' id='"+count_1+"_code' maxlength='20' class='valid-control' style='width:98%;'/>",
					isRelate:$("<select><select>").attr("id",count_1+"_isRelateTemp").append("<option value='0'>否</option>").append("<option value='1'>是</option>"),
					isPwdControl:$("<select><select>").attr("id",count_1+"_isPwdControlTemp").append("<option value='0'>否</option>").append("<option value='1'>是</option>"),
					operate:"<c:if test="${!empty extension_youku_category_modify}"><security:authorize ifAnyGranted="${extension_youku_category_modify.code}"><a href='javascript:void(0);'  id='shortcut_${extension_youku_category_modify.indexCode}_"
					+count_1+"' sid="+count_1+"  class=''  style='color:#999;'><img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${extension_youku_category_modify.name}</a><a href='javascript:void(0);'  id='shortcut_${extension_youku_category_save.indexCode}_"
					+count_1+"' class='shortcut_${extension_youku_category_save.indexCode}' style='color:#53056A;' sid="+count_1+"><img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${extension_youku_category_save.name}</a><a href='javascript:void(0);'  id='shortcut_${extension_youku_category_reset.indexCode}_"+
					count_1+"' class='' style='color:#999;'><img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${extension_youku_category_reset.name}</a></security:authorize></c:if>",
					del:"<c:if test="${!empty extension_youku_category_delete}"><security:authorize ifAnyGranted="${extension_youku_category_delete.code}"><a href='javascript:void(0);' id='shortcut_${extension_youku_category_delete.indexCode}_"
					+count_1+"' sid="+count_1+" class='shortcut_${extension_youku_category_delete.indexCode}' method='add'><img src='${base}/${skin}/images/icon/Del.png' width='20' height='20' /></a></security:authorize></c:if>"
			};
			jQuery("#categoryList").jqGrid('setRowData',count_1,data);
		}else{
		 	top.layer.msg('<msg:message code="dictionaryItem.notsave"/>');
			count_1--;
		}
	});	

	$(".shortcut_${extension_youku_category_delete.indexCode}").live("click",(function(){
		var rowid = $(this).attr("sid");
		var method = $(this).attr("method");
		if(method != ""){
			currcount_1 = 0;
			jQuery("#categoryList").jqGrid('delRowData',rowid); 
		}else{
			if(currcount_1 == 0){
				var data = jQuery("#categoryList").jqGrid("getRowData",rowid);
			   	var index = top.$.layer({
			  	    shade : [0.5 , '#000' , true],
			  	    area : ['auto','auto'],
			  	  	offset : ['50px',''],
			  	    dialog : {
			  	        msg:"<msg:message code='receivermessage.delete'/>",
			  	        btns : 2, 
			  	        type : 4,
			  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
			  	        yes : function(){
			  	  		$.ajax({
							url:'youku_category/delete.json?c=${extension_youku_category_delete.code}&id='+rowid,
							type:'post',
							timeout:'60000',
							dataType:'json',
							success:function(jsonData,textStatus){
								top.layer.close(index);
								if (textStatus == "success"){
									if (jsonData.status == "success"){
										jQuery("#categoryList").jqGrid("delRowData",rowid);
									}
								}
							}
						});
			  	        },
			  	        no : function(){
			  	        	top.layer.close(index);
			  	        }
			  	    }
			  	});
			}else{
				top.layer.msg('<msg:message code="dictionaryItem.notsave"/>');
			}
		}
	}))
		
	var tempIsRelate;
	var tempIsPwdControl;
	$(".shortcut_extension_youku_category_modify").live("click",(function(){
		var rowid = $(this).attr("sid");
		var $td = $("#"+rowid).find("td").eq(2);
		var $td1 = $("#"+rowid).find("td").eq(3);
		tempIsRelate = $td.html();
		tempIsPwdControl = $td1.html()
		if(currcount_1 == 0){
			currcount_1 = 1;
			jQuery("#categoryList").jqGrid('editRow',rowid);
			$(this).attr("class","").css("color","#999");
			$("#shortcut_extension_youku_category_save_"+rowid).attr("class","shortcut_extension_youku_category_save").css("color","#53056A");
			$("#shortcut_extension_youku_category_reset_"+rowid).attr("class","shortcut_extension_youku_category_reset").css("color","#53056A");
		}else{
			var index = $.layer({
		  	    shade : [0.5 , '#000' , true],
		  	    area : ['auto','auto'],
		  	  	offset : ['50px',''],
		  	    dialog : {
		  	        msg:"请先保存当前编辑的条目",
		  	        btns : 1, 
		  	        type : 4,
		  	        btn : ["确定"],
		  	        yes : function(){
		  	        	layer.close(index);
		  	        },
		  	        no : function(){
		  	        	layer.close(index);
		  	        }
		  	    }});
		}
	}));
		
		
	$(".shortcut_extension_youku_category_reset").live("click",( function() {
		currcount_1 = 0;
		var rowid = $(this).attr("sid");
		$("#"+rowid).find("td").eq(2).html(tempIsRelate);
		$("#"+rowid).find("td").eq(3).html(tempIsPwdControl);
		jQuery("#categoryList").jqGrid('restoreRow',rowid);
		$(this).attr("class","").css("color","#999");	
		$("#shortcut_extension_youku_category_save_"+rowid).attr("class","").css("color","#999");	
		$("#shortcut_extension_youku_category_modify_"+rowid).attr("class","shortcut_extension_youku_category_modify").css("color","#53056A");
	}));
		
	$(".shortcut_extension_youku_category_save").live("click",(function() {
		var data="";
		var celcalue = "";
		var method = $(this).attr("method");
		var rowid = $(this).attr("sid");
		var clsn = $("#"+rowid+"_name").attr("class");
		if(method == ""){
			celcalue = $(this).attr("celcalue");
			data = "c=${category.c}&name="+$.trim($("#"+rowid+"_name").val())+"&id="+rowid+"&code="+$.trim($("#"+rowid+"_code").val())+"&isRelate="+$("#"+rowid+"_isRelate").val()+"&isPwdControl="+$("#"+rowid+"_isPwdControl").val()+"";
		}else{
			celcalue = $.trim($("#"+rowid+"_code").val());
			data = "c=${category.c}&name="+$.trim($("#"+rowid+"_name").val())+"&code="+$.trim($("#"+rowid+"_code").val())+"&isRelate="+$("#"+rowid+"_isRelateTemp").val()+"&isPwdControl="+$("#"+rowid+"_isPwdControlTemp").val()+"";
		}
		 var tempOpts = {
			  	   classNames:[clsn],
				   validStatus:function(status){
					   if (status){
							$.ajax({
								url:'youku_category/save.json',
								type:'post',
								timeout:'60000',
								dataType:'json',
								data: data, 
								success:function(jsonData){
									if (jsonData.status == "success"){
										currcount_1 = 0;
										jQuery("#categoryList").jqGrid('setRowData',rowid,{name:$.trim($("#"+rowid+"_name").val()),code:$.trim($("#"+rowid+"_code").val())});
										$("#"+rowid).attr("editable","0");
										$("#shortcut_extension_youku_category_save_"+rowid).attr("class","").css("color","#999");
										$("#shortcut_extension_youku_category_reset_"+rowid).attr("class","").css("color","#999");					
										$("#shortcut_extension_youku_category_modify_"+rowid).attr("class","shortcut_extension_youku_category_modify").css("color","#53056A");
										$("#categoryList").trigger("reloadGrid");					
										if(method != ""){
											$("#shortcut_extension_youku_category_save_"+rowid).attr("method","");
											$("#shortcut_extension_youku_category_save_"+rowid).attr("sid",jsonData.gid);
											$("#categoryList").trigger("reloadGrid");
										}
									}else if(jsonData.message != '' && jsonData.message != 'undefined' && jsonData.message != null){
										top.layer.msg(jsonData.message);
									}
								}
							});
						}
					}
				};
				$.extend(options,tempOpts);
				valid.updateSetting(options);
		 		valid.reset();
		  		valid.valid();
		  		jQuery("#categoryList").setColProp('isRelate',{formatter:changeToC});
		  		jQuery("#categoryList").setColProp('isPwdControl',{formatter:changeToC});
	}));
		
})

function changeToC(data){
	if(data == 0){
		return "否";
	}else if(data == 1){
		return "是";
	}
}

</script>
</head >
<body class="skinMain">
<form:form method="post" action="list" commandName="category">
<input type="hidden" name="c" value="${category.c}" />
<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
	<tr>
		<td>
			<table id="categoryList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</form:form>
</body>
</html>