<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#orgList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
    jQuery("#orgList").jqGrid({
        treeGrid: true,
        treeGridModel: 'adjacency',
        ExpandColumn: 'name',
        ExpandColClick: true,
        url: 'organization/grid.json',
        datatype: 'json',
        colNames: [
                   "<msg:message code='organization.isChildNode'/>",
                   "<msg:message code='organization.name'/>",
                   "<msg:message code='organization.code'/>",
                   "<msg:message code='info.operate'/>"
                   ],
        colModel: [
                   {name: 'childNode',index: 'childNode',width:'1%',align:'left',hidden: true,sortable: false}, 
                   {name: 'name',index: 'name',width:'45%',align:'left',hidden: false,sortable: false}, 
                   {name: 'code',index:'code',width:'40%',align:'center',hidden: false,sortable: false},
                   {name:'act',index:'act', width:'15%',align:'center',sortable:false,sortable: false}
                   ],
        pager: false,
        height:<msg:message code='jqgrid.height.300'/>,
        autowidth: true,
        viewrecords: true,
        jsonReader: {
        	repeatitems: false
        },
        beforeProcessing:function(jsonData){
        	if (jsonData.rows){
       			var resultDate = $.pluginJQGT({
        			level:"treeLevel",
        			isLeaf:"isChildNode",
        			expanded:true,
        			loaded:true,
					root:${root}
       			},jsonData.rows);
       			$.extend(jsonData,{rows:resultDate});
        	}       
        	return jsonData;
        },
    	gridComplete: function(){
    		var ids = jQuery("#orgList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			var rowdata = jQuery("#orgList").jqGrid('getRowData',id);
    			var content = "";
    			<c:if test="${!empty system_organization_modify}">
    		    <security:authorize ifAnyGranted="${system_organization_modify.code}">	
				if (rowdata.level && rowdata.level != '1'){
    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${system_organization_modify.indexCode}'";
					content += " title='${system_organization_modify.name}' >";
					content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${system_organization_modify.name}";
					content += "</a>";
    		    }else{
					content += "<span style='color:#CFC09F;'><img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${system_organization_modify.name}</span>"; 
				}
    		    </security:authorize>
    		    </c:if>  
    		    <c:if test="${!empty system_organization_delete}">
    		    <security:authorize ifAnyGranted="${system_organization_delete.code}">
    		    var dis = "";
    		    if (rowdata.isLeaf && rowdata.isLeaf == 'true'){
    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${system_organization_delete.indexCode}'";
    		    	content += " title='${system_organization_delete.name}' >";
    		    	content += "<img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${system_organization_delete.name}";
    		    	content += "</a>";
    		    }
    		    else{
					content += "<span style='color:#CFC09F;'><img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${system_organization_delete.name}</span>";
    		    }
    		    </security:authorize>
    		    </c:if>      	
    			jQuery("#orgList").jqGrid('setRowData',ids[i],{act:"<div class='jqgridContainer'>" + content + "</div>"});
    		}	
    	},
    	loadComplete:function(){
		
    	    $(".shortcut_${system_organization_delete.indexCode}").click(function(){
				
    	    	var rowid = $(this).attr("id");
    	    	var data = jQuery("#orgList").jqGrid("getRowData",rowid);
    			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
    				var url = "${base}${system_organization_delete.controller}.json?c=${system_organization_delete.code}&id="+rowid+"&prompt=name&name="+encodeURIComponent(encodeURIComponent(data.name));
    				$.ajax({
    					url:url,
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					success:function(jsonData,textStatus){
    						if (textStatus == "success"){
    							if (jsonData.status == "success"){
    								//jQuery("#orgList").jqGrid("delRowData",rowid);
    								$("#orgList").trigger("reloadGrid");
									top.layer.close(layers);								
    							}else{
									top.layer.msg('<msg:message code='organization.hasuser'/>') ;
    							}
    						}
    					}
    				});
    			});
    	    });     		
    	    $(".shortcut_${system_organization_modify.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			window.location.href = "${base}${system_organization_modify.controller}?c=${system_organization_modify.code}&id="+rowid;
    	    });     		
    	},
        caption:'<msg:message code="organization.list"/>',
        toolbar: [true,"top"]
    });
	jQuery("#orgList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	
    <c:if test="${!empty system_organization_create}">
    <security:authorize ifAnyGranted="${system_organization_create.code}">	
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${system_organization_create.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/images/icon/create.png")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${system_organization_create.name}");
	$("#t_orgList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));    
    $("#${system_organization_create.indexCode}","#t_orgList").click(function(){
    	window.location.href = "${base}${system_organization_create.controller}?c=${system_organization_create.code}";
    });    
    </security:authorize>
    </c:if>
    jQuery("#orgList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
});
</script>
</head>
<body class="skinMain">
<table cellpadding="2" cellspacing="2" border="0" width="100%">
	<tr>
		<td>
			<table id="orgList"><tr><td>&nbsp;</td></tr></table>
		</td>
	</tr>
</table>
</body>
</html>