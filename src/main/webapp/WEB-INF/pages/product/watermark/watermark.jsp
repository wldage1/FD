<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
var status = 0;
$(document).ready(function(){
	
	jQuery("#watermarkList").jqGrid({
        url: 'watermark/grid.json',
        datatype: 'json',
        colNames: ["<msg:message code='org.shortName'/>",
                   "<msg:message code='org.watermark'/>",
                   "<msg:message code='info.operate'/>"],
       	colModel: [{name: 'shortName',index:'shortName',align:'center',sortable:false,width:'25%'},
                   {name: 'watermark',index:'watermark',align:'center',sortable:false,editable:true,width:'70%'},
                   {name:'operate',index:'operate',align:'left',sortable:false,width:'15%'}],
        postData:{c:"${organization.c}"},
        mtype:"POST",
        height:"<msg:message code='jqgrid.height.100'/>",
        pager: false,
        autowidth: true,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete: function(){
    		var ids = jQuery("#watermarkList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			var content = "";
				
    			<c:if test="${!empty product_watermark_modify}">
				<security:authorize ifAnyGranted="${product_watermark_modify.code}">	
					content += "<a href='javascript:void(0);' id='shortcut_${product_watermark_modify.indexCode}_"+id+"' sid="+id+" class='shortcut_${product_watermark_modify.indexCode}'>";
					content += "<img src='${base}/${skin}/${product_watermark_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_watermark_modify.name}";
					content += "</a>";
				</security:authorize>
				</c:if> 
				
				<c:if test="${!empty product_watermark_save}">	
				<security:authorize ifAnyGranted="${product_watermark_save.code}">	
					content += "<a href='javascript:void(0);' id='shortcut_${product_watermark_save.indexCode}_"+id+"' sid="+id+" style='color:#999;'>";
					content += "<img src='${base}/${skin}/${product_watermark_save.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_watermark_save.name}";
					content += "</a>";
				</security:authorize>
				</c:if>
				
				<c:if test="${!empty product_watermark_reset}">	
				<security:authorize ifAnyGranted="${product_watermark_reset.code}">	
						content += "<a href='javascript:void(0);' id='shortcut_${product_watermark_reset.indexCode}_"+id+"' sid="+id+" style='color:#999;'>";
						content += "<img src='${base}/${skin}/${product_watermark_reset.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_watermark_reset.name}";
						content += "</a>";
				</security:authorize>
				</c:if>
				
    			jQuery("#watermarkList").jqGrid('setRowData',ids[i],{operate:"<div class='jqgridContainer'>" + content + "</div>"});
    		}
    	},
        caption:"<msg:message code='org.watermark.list'/>"
	});
	
	$(".shortcut_${product_watermark_modify.indexCode}").live("click",(function(){
		var rowid = $(this).attr("sid");
		if(status == 0){
			status = 1;
			jQuery("#watermarkList").jqGrid('editRow',rowid);
			$("#"+rowid+"_watermark").attr("maxlength","20");
			$(this).attr("class","").css("color","#999");
			$("#shortcut_${product_watermark_save.indexCode}_"+rowid).attr("class","shortcut_${product_watermark_save.indexCode}").css("color","#53056A");
			$("#shortcut_${product_watermark_reset.indexCode}_"+rowid).attr("class","shortcut_${product_watermark_reset.indexCode}").css("color","#53056A");
		}else{
			top.$.layer({
				shade : [0.5 , '#000' , true],
		  		offset : ['50px',''],
		  	 	title : "<msg:message code='info.prompt'/>",
			    dialog : {msg:"<msg:message code='editing.save'/>", type:3}
			});
		}
	}));
		
	$(".shortcut_${product_watermark_reset.indexCode}").live("click",( function() {
		status = 0;
		var rowid = $(this).attr("sid");
		jQuery("#watermarkList").jqGrid('restoreRow',rowid);
		$(this).attr("class","").css("color","#999");	
		$("#shortcut_${product_watermark_save.indexCode}_"+rowid).attr("class","").css("color","#999");	
		$("#shortcut_${product_watermark_modify.indexCode}_"+rowid).attr("class","shortcut_${product_watermark_modify.indexCode}").css("color","#53056A");
	}));
		
	$(".shortcut_${product_watermark_save.indexCode}").live("click",(function() {
		var rowid = $(this).attr("sid");
		var rowdata = jQuery("#watermarkList").jqGrid('getRowData',rowid);
		var watermark = $("#"+rowid+"_watermark").val();
		$.ajax({
			url:'watermark/save.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: {c:"${organization.c}",id:rowid,watermark:watermark},
			success:function(jsonData){
				if (jsonData.status == "success"){
					status = 0;
					$("#watermarkList").trigger("reloadGrid");
				}else{
					top.$.layer({
						area : ['auto','auto'],
						dialog : {msg:"<msg:message code='save.fail'/>", type:8}
					});
				}
			}
		});
	}));
});
</script>
</head>
<body class="skinMain">
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td>
			<table id="watermarkList"><tr><td>&nbsp;</td></tr></table>
		</td>
	</tr>
</table>
</body>
</html>