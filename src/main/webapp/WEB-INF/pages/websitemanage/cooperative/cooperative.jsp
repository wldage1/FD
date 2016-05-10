<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#partnersList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
    jQuery("#partnersList").jqGrid({
        treeGridModel: 'adjacency',
        url: 'cooperative/grid.json',
        datatype: 'json',
        colNames: [
                   "<msg:message code='partners.name'/>",
                   "<msg:message code='partners.url'/>", 
                   "<msg:message code='info.operate'/>"
                   ],
        colModel: [
                   {name: 'name',index: 'name',width:'15%',align:'center',hidden: false,sortable: false}, 
                   {name: 'url',index: 'url',width:'15%',align:'center',hidden: true,sortable: false}, 
                   {name:'act',index:'act', width:'15%',align:'center',sortable:false,formatter:function(cellvalue,options,rowdata){
                    	var id=rowdata.id;
						var content="";
						//修改 
						<c:if test="${!empty websitemanage_cooperative_modify}">
					    <security:authorize ifAnyGranted="${websitemanage_cooperative_modify.code}">
					    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${websitemanage_cooperative_modify.indexCode}' title='${websitemanage_cooperative_modify.name}'>";
					    	content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${websitemanage_cooperative_modify.name}";
					    	content += "</a>";
					    </security:authorize>
						</c:if>
						//删除
						<c:if test="${!empty websitemanage_cooperative_delete}">
		    		    <security:authorize ifAnyGranted="${websitemanage_cooperative_delete.code}">
		   		    	content += "<a href='javascript:void(0);' title='${websitemanage_cooperative_delete.name}' id='" + id + "' class='shortcut_${websitemanage_cooperative_delete.indexCode}'>";
		   		    	content += "<img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${websitemanage_cooperative_delete.name}";
		   		    	content += "</a>";
		    		    </security:authorize>
		    		    </c:if>   
						return content;
                   }}
                   ],
		postData:{c:"${links.c}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,   
        page:"${partners.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:'<msg:message code='jqgrid.height.250'/>',
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	loadComplete:function(){
    	    $(".shortcut_${websitemanage_cooperative_delete.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
    				var url = "${base}${websitemanage_cooperative_delete.controller}.json?c=${websitemanage_cooperative_delete.code}&id="+rowid;
    				$.ajax({
    					url:url,
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					success:function(jsonData,textStatus){
    						if (textStatus == "success"){
    							if (jsonData.status == "success"){
    								$("#partnersList").trigger("reloadGrid");  
									top.layer.close(layers);										
    							}
    						}
    					}
    				});
    			});
    	    });
    	    
    	    $(".shortcut_${websitemanage_cooperative_modify.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			window.location.href = "${base}${websitemanage_cooperative_modify.controller}?c=${websitemanage_cooperative_modify.code}&id="+ rowid;
    	    }); 
    	},
        caption:'合作伙伴',
        toolbar: [true,"top"]
    });
    
    <c:if test="${!empty websitemanage_cooperative_create}">
    <security:authorize ifAnyGranted="${websitemanage_cooperative_create.code}">	 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${websitemanage_cooperative_create.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/images/icon/create.png")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${websitemanage_cooperative_create.name}");
	$("#t_partnersList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));  
    $("#${websitemanage_cooperative_create.indexCode}","#t_partnersList").click(function(){
    	window.location.href = "${base}${websitemanage_cooperative_create.controller}?c=${websitemanage_cooperative_create.code}";
    });    
    </security:authorize>
    </c:if>
    
	jQuery("#partnersList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
});
</script>
</head>

<body class="skinMain">
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td>
			<table id="partnersList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</body>
</html>