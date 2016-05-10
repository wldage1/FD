<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(document).ready(function(){
    jQuery("#attributeGroupList").jqGrid({
        url: 'attribute/grid.json',
        datatype: 'json',
        colNames: ["<msg:message code='attributegroup.name'/>", 
                   "<msg:message code='attributegroup.code'/>",
                   "<msg:message code='attributegroup.operate'/>"],
        colModel: [{name: 'name',indexCode:'name',width:'35%',align:'center',sortable:false},
                   {name: 'code',indexCode:'code',width:'35%',align:'center',sortable:false},
                   {name:'operate',indexCode:'operate', width:'30%',align:'center',sortable:false}],
        postData:{c:"${productAttributeGroup.c}"},
        mtype:"POST",
        height:'auto',
        rowNum:10,
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        page:"${ProductAttributeGroup.page}",
        pager: '#pagered',
        autowidth: true,
        rownumbers: true,
        viewrecords: true,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete: function(){
    		var ids = jQuery("#attributeGroupList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			var rowdata = jQuery("#attributeGroupList").jqGrid('getRowData',id);
    			var content = "";
    		    
    			<c:if test="${!empty product_attribute_modify}">
    		    <security:authorize ifAnyGranted="${product_attribute_modify.code}">	
    			content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${product_attribute_modify.indexCode}' title='${product_attribute_modify.name}'>";
    			content += "<img  src='${base}/${skin}/${product_attribute_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_attribute_modify.name}";
    			content += "</a>";
    		    </security:authorize>
    		    </c:if> 
    		    <c:if test="${!empty product_attribute_preview}">
    		    <security:authorize ifAnyGranted="${product_attribute_preview.code}">	
    			content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${product_attribute_preview.indexCode}' title='${product_attribute_preview.name}'>";
    			content += "<img src='${base}/${skin}/${product_attribute_preview.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_attribute_preview.name}";
    			content += "</a>";
    		    </security:authorize>
    		    </c:if> 
    		    <c:if test="${!empty product_attribute_delete}">
    		    <security:authorize ifAnyGranted="${product_attribute_delete.code}">	
    			content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${product_attribute_delete.indexCode}' title='${product_attribute_delete.name}'>";
    			content += "<img src='${base}/${skin}/${product_attribute_delete.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_attribute_delete.name}";
    			content += "</a>";
    		    </security:authorize>
    		    </c:if> 
    		    <c:if test="${!empty product_attribute_copy}">
    		    <security:authorize ifAnyGranted="${product_attribute_copy.code}">	
    			content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${product_attribute_copy.indexCode}' title='${product_attribute_copy.name}'>";
    			content += "<img src='${base}/${skin}/${product_attribute_copy.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_attribute_copy.name}";
    			content += "</a>";
    		    </security:authorize>
    		    </c:if> 
    			jQuery("#attributeGroupList").jqGrid('setRowData',ids[i],{operate:content});
    		}	
    	},
    	loadComplete:function(){
    		$(".shortcut_${product_attribute_modify.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			window.location.href = "${base}${product_attribute_modify.controller}?c=${product_attribute_modify.code}&id="+rowid;
    	    });
    		$(".shortcut_${product_attribute_preview.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			window.location.href = "${base}${product_attribute_preview.controller}?c=${product_attribute_preview.code}&id="+rowid;
    	    });
    		$(".shortcut_${product_attribute_type.indexCode}").click(function(){
    			var rowid = $(this).attr("id");
    			showMenu(rowid);
    			return false;
    		});
    		$(".shortcut_${product_attribute_delete.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    	    	var data = jQuery("#attributeGroupList").jqGrid("getRowData",rowid);
    			window.parent.Boxy.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
    				var url = "${base}${product_attribute_delete.controller}.json?c=${product_attribute_delete.code}&id="+rowid+"&prompt=name&name="+encodeURIComponent(encodeURIComponent(data.name));
    				$.ajax({
    					url:url,
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					success:function(jsonData,textStatus){
    						if (textStatus == "success"){
    							if (jsonData.status == "success"){
    								if(jsonData.productCount!='undefinded'&&jsonData.productCount>0){
    									alert("<msg:message code='attribute.exist.product'/>");
    								}
    								else{
    									$("#attributeGroupList").trigger("reloadGrid");    
    								}
    							}else{
    								window.parent.Boxy.alert("<msg:message code='attribute.hasuser'/>",function(){return false;} ,{title: "<msg:message code='info.prompt'/>"});
    							}
    						}
    					}
    				});
    			}, {title: "<msg:message code='info.prompt'/>"});
    	    }); 
    		$(".shortcut_${product_attribute_copy.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			window.location.href = "${base}${product_attribute_copy.controller}?c=${product_attribute_copy.code}&id="+rowid;
    	    });
    	},
        caption:'<msg:message code="attributegroup.list"/>',
        toolbar: [true,"top"]
    });
    <c:if test="${!empty product_attribute_create}">
    <security:authorize ifAnyGranted="${product_attribute_create.code}">	
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${product_attribute_create.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/${product_attribute_create.icon}")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${product_attribute_create.name}");
	$("#t_attributeGroupList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));    
    $("#${product_attribute_create.indexCode}","#t_attributeGroupList").click(function(){
    	window.location.href = "${base}${product_attribute_create.controller}?c=${product_attribute_create.code}";
    });    
    </security:authorize>
    </c:if>
    
    jQuery("#attributeGroupList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
});


</script>
</head>
<body class="skinMain">
<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
	<tr>
		<td>
			<table id="attributeGroupList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</body>
</html>