<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#categoryList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
    jQuery("#categoryList").jqGrid({
    	treeGrid: true,
        treeGridModel: 'adjacency',
        ExpandColumn: 'name',
        ExpandColClick: true,
        url: 'capitalconfig/tree.json',
        datatype: 'json',
        colNames: ["<msg:message code='category.isChildNode'/>",
                   "<msg:message code='category.type'/>", 
                   "<msg:message code='category.code'/>", 
                   "<msg:message code='category.sortNum'/>",
                   "<msg:message code='category.operate'/>"
                   ],
        colModel: [{name: 'isChildNode',index: 'isChildNode',width:'1%',align:'left',hidden: true,sortable: false}, 
				   {name: 'name',index: 'name',width:'40%',align:'left',hidden: false,sortable:false}, 
				   {name: 'code',index: 'code',width:'29%',align:'center',hidden: false,sortable:false}, 
				   {name:'sortNum', index:'sortNum',width:'10%', align:'center', hidden:false, sortable:false,editable:true,edittype:'text',editrules:{edithidden:true,required:true,number:true,minValue:0,maxValue:100000}},
                   {name:'act',index:'act', width:'30%',align:'center',sortable:false,sortable:false}],
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
        			loaded:true
       			},jsonData.rows);
       			$.extend(jsonData,{rows:resultDate});
        	}       
        	return jsonData;
        },
    	gridComplete: function(){
    		var ids = jQuery("#categoryList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			var rowdata = jQuery("#categoryList").jqGrid('getRowData',id);
    			var content = "";
    			<c:if test="${!empty product_capitalconfig_modify}">
    		    <security:authorize ifAnyGranted="${product_capitalconfig_modify.code}">	
    			content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${product_capitalconfig_modify.indexCode}'";
    			content += " title='${product_capitalconfig_modify.name}' >";
    			content += "<img src='${base}/${skin}/${product_capitalconfig_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_capitalconfig_modify.name}";
    			content += "</a>";
    		    </security:authorize>
    		    </c:if>  
				<c:if test="${!empty product_capitalconfig_attribute}">
				<security:authorize ifAnyGranted="${product_capitalconfig_attribute.code}">
    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${product_capitalconfig_attribute.indexCode}'";
        		    content += " title='${product_capitalconfig_attribute.name}' >";
        		    content += "<img src='${base}/${skin}/${product_capitalconfig_attribute.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_capitalconfig_attribute.name}";
        		    content += "</a>";
    		    </security:authorize>
				</c:if>
    		    <c:if test="${!empty product_capitalconfig_delete}">
    		    <security:authorize ifAnyGranted="${product_capitalconfig_delete.code}">
    		    if (rowdata.isChildNode == '0'){
    		    	content += "<span style='color:#CFC09F;' title='${product_capitalconfig_delete.name}'><img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${product_capitalconfig_delete.name}</span>"; 
    		    }
    		    else{
    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${product_capitalconfig_delete.indexCode}'";
        		    content += " title='${product_capitalconfig_delete.name}' >";
        		    content += "<img src='${base}/${skin}/${product_capitalconfig_delete.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_capitalconfig_delete.name}";
        		    content += "</a>";
    		    }
    		    </security:authorize>
    		    </c:if>
    			jQuery("#categoryList").jqGrid('setRowData',ids[i],{act:"<div class='jqgridContainer'>" + content + "</div>"});
    		}	
    	},
    	loadComplete:function(){
    	    $(".shortcut_${product_capitalconfig_delete.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    	    	var data = jQuery("#categoryList").jqGrid("getRowData",rowid);
    			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
    				var url = "${base}${product_capitalconfig_delete.controller}.json?c=${product_capitalconfig_delete.code}&id="+rowid+"&prompt=name&name="+encodeURIComponent(encodeURIComponent(data.name));
    				$.ajax({
    					url:url,
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					success:function(jsonData,textStatus){
    						if (textStatus == "success"){
    							if (jsonData.status == "success"){
    								$("#categoryList").trigger("reloadGrid");
									top.layer.close(layers);	
    							}else{
									top.layer.msg("<msg:message code='category.hasuser'/>");
								}
    						}
    					}
    				});
    			});
    	    });     		
    	    $(".shortcut_${product_capitalconfig_modify.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			window.location.href = "${base}${product_capitalconfig_modify.controller}?c=${product_capitalconfig_modify.code}&id="+ rowid;
    	    }); 
			
			$(".shortcut_${product_capitalconfig_attribute.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			window.location.href = "${base}${product_capitalconfig_attribute.controller}?c=${product_capitalconfig_attribute.code}&id="+ rowid;
    	    });			
    	},
    	cellEdit: true, 
        cellurl: "${base}/product/capitalconfig/saveSortNum?c=${code}",
        cellsubmit: 'remote', 
        caption:'<msg:message code="category.list"/>',
        toolbar: [true,"top"]
    });
    
    <c:if test="${!empty product_capitalconfig_create}">
    <security:authorize ifAnyGranted="${product_capitalconfig_create.code}">	 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${product_capitalconfig_create.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/${product_capitalconfig_create.icon}")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${product_capitalconfig_create.name}");
	$("#t_categoryList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));  
    $("#${product_capitalconfig_create.indexCode}","#t_categoryList").click(function(){
    	window.location.href = "${base}${product_capitalconfig_create.controller}?c=${product_capitalconfig_create.code}";
    });    
    </security:authorize>
    </c:if>
    jQuery("#categoryList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
});

</script>
	</head>
	<body class="skinMain">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td>
					<table id="categoryList"><tr><td>&nbsp;</td></tr></table>	
				</td>
			</tr>
		</table>
	</body>
</html>