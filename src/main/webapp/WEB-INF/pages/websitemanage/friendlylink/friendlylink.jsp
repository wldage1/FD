<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#linksList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
    jQuery("#linksList").jqGrid({
        treeGridModel: 'adjacency',
        url: 'friendlylink/grid.json',
        datatype: 'json',
        colNames: [
                   "<msg:message code='links.name'/>",
                   "<msg:message code='links.url'/>", 
                   "<msg:message code='info.operate'/>"
                   ],
        colModel: [
                   {name: 'name',index: 'name',width:'15%',align:'center',hidden: false,sortable: false}, 
                   {name: 'url',index: 'url',width:'15%',align:'center',hidden: true,sortable: false}, 
                   {name:'act',index:'act', width:'15%',align:'center',sortable:false,formatter:function(cellvalue,options,rowdata){
                    	var id=rowdata.id;
						var content="";
						//修改 
						<c:if test="${!empty websitemanage_friendlylink_modify}">
					    <security:authorize ifAnyGranted="${websitemanage_friendlylink_modify.code}">
					    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${websitemanage_friendlylink_modify.indexCode}' title='${websitemanage_friendlylink_modify.name}'>";
					    	content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${websitemanage_friendlylink_modify.name}";
					    	content += "</a>";
					    </security:authorize>
						</c:if>
						//删除
						<c:if test="${!empty websitemanage_friendlylink_delete}">
		    		    <security:authorize ifAnyGranted="${websitemanage_friendlylink_delete.code}">
		   		    	content += "<a href='javascript:void(0);' title='${websitemanage_friendlylink_delete.name}' id='" + id + "' class='shortcut_${websitemanage_friendlylink_delete.indexCode}'>";
		   		    	content += "<img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${websitemanage_friendlylink_delete.name}";
		   		    	content += "</a>";
		    		    </security:authorize>
		    		    </c:if>   
						return content;
                   }}
                   ],
		postData:{c:"${links.c}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,   
        page:"${links.page}",
        rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:<msg:message code='jqgrid.height.250'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	loadComplete:function(){
    	    $(".shortcut_${websitemanage_friendlylink_delete.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    	    	var records = $("#linksList").getGridParam("records");
    	    	var page = $("#linksList").getGridParam("page");
    			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
    				var url = "${base}${websitemanage_friendlylink_delete.controller}.json?c=${websitemanage_friendlylink_delete.code}&id="+rowid;
    				$.ajax({
    					url:url,
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					success:function(jsonData,textStatus){
    						if (textStatus == "success"){
    							if (jsonData.status == "success"){
    								for(var i = 1; ;i++){
    									if(records = 10*i+1){
    										jQuery("#linksList").setGridParam({page:page-1}).trigger("reloadGrid");
    										break;
    									}
    								}
    							}
									top.layer.close(layers);										
    						}
    					}
    				});
    			});
    	    });
    	    
    	    $(".shortcut_${websitemanage_friendlylink_modify.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			window.location.href = "${base}${websitemanage_friendlylink_modify.controller}?c=${websitemanage_friendlylink_modify.code}&id="+ rowid;
    	    }); 
    	},
        caption:'<msg:message code="links.title"/>',
        toolbar: [true,"top"]
    });
    
    <c:if test="${!empty websitemanage_friendlylink_create}">
    <security:authorize ifAnyGranted="${websitemanage_friendlylink_create.code}">	 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${websitemanage_friendlylink_create.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/images/icon/create.png")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${websitemanage_friendlylink_create.name}");
	$("#t_linksList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));  
    $("#${websitemanage_friendlylink_create.indexCode}","#t_linksList").click(function(){
    	window.location.href = "${base}${websitemanage_friendlylink_create.controller}?c=${websitemanage_friendlylink_create.code}";
    });    
    </security:authorize>
    </c:if>
    
	jQuery("#linksList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
});
</script>
</head>

<body class="skinMain">
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td>
			<table id="linksList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</body>
</html>