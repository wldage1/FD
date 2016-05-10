<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#logList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
	
	$(window).resize(function(){ 　　
		$("#logList").setGridWidth($(window).width()*0.999);　
	});　　
	
    jQuery("#logList").jqGrid({
        treeGridModel: 'adjacency',
        url: 'log/grid.json',
        datatype: 'json',
        colNames: [
                   "<msg:message code='log.operatePeople'/>",
                   "<msg:message code='log.IP'/>", 
                   "<msg:message code='log.operateTime'/>",
                   "<msg:message code='log.conter'/>",
                   "帐号",
                   "<msg:message code='info.operate'/>"
                   ],
        colModel: [
                   {name: 'userName',index: 'userName',width:'15%',align:'center',hidden: false,sortable: false}, 
                   {name: 'accessIp',index: 'accessIp',width:'15%',align:'center',hidden: true,sortable: false}, 
                   {name: 'operationTime',index:'optTime',width:'15%',align:'center',hidden: false,sortable: false},
                   {name: 'content',index:'content',width:'40%',align:'center',hidden: false,sortable: false},
                   {name: 'userAccount',index: 'userAccount',width:'15%',align:'center',hidden: true,sortable: false}, 
                   {name:'act',index:'act', width:'15%',align:'center',sortable:false}
                   ],
		postData:{c:"${log.c}",content:"${log.content}"},
        rowNum:<msg:message code='jqgrid.row.num.15'/>,   
        page:"${log.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:<msg:message code='jqgrid.height.350'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: true,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	gridComplete: function(){
    		var ids = jQuery("#logList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			var content = "";
    		    <c:if test="${!empty system_log_delete}">
    		    <security:authorize ifAnyGranted="${system_log_delete.code}">
   		    	content += "<a href='javascript:void(0);' title='${system_log_delete.name}' id='" + id + "' class='shortcut_${system_log_delete.indexCode}'>";
   		    	content += "<img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${system_log_delete.name}";
   		    	content += "</a>";
    		    </security:authorize>
    		    </c:if>         	
    			jQuery("#logList").jqGrid('setRowData',ids[i],{act:"<div class='jqgridContainer'>" + content + "</div>"});
    		}	
    	},
    	loadComplete:function(){
    	    $(".shortcut_${system_log_delete.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
    				var url = "${base}${system_log_delete.controller}.json?c=${system_log_delete.code}&id="+rowid;
    				$.ajax({
    					url:url,
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					success:function(jsonData,textStatus){
    						if (textStatus == "success"){
    							if (jsonData.status == "success"){
    								//jQuery("#logList").jqGrid("delRowData",rowid);
    								$("#logList").trigger("reloadGrid");  
									top.layer.close(layers);										
    							}
    						}
    					}
    				});
    			});
    	    });    
    	},
        caption:'<msg:message code="log.list"/>',
        toolbar: [true,"top"]
    });
    
	jQuery("#logList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	/* jQuery("#logList").jqGrid('navGrid','#pagered',
			{edit:false,add:false,del:false},
			{},
			{},
			{},
			{multipleSearch:true, multipleGroup:true, showQuery: true});
	jQuery("#logList").jqGrid('navButtonAdd','#pagered',{
	    caption: "Columns",
	    title: "Reorder Columns",
	    onClickButton : function (){
	        jQuery("#logList").jqGrid('columnChooser');
	    }
	}); */
	
    <c:if test="${!empty system_log_delete}">
    <security:authorize ifAnyGranted="${system_log_delete.code}">	
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${system_log_delete.indexCode}")
			  .attr("class","${system_log_delete.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/images/icon/delete.png")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${system_log_delete.name}");
	$("#t_logList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));
    $(".${system_log_delete.indexCode}","#t_logList").click(function(){
		var page = jQuery('#logList').jqGrid('getGridParam','page');
		var reccount = jQuery('#logList').jqGrid('getGridParam','reccount');
		var selrow = jQuery('#logList').jqGrid('getGridParam','selarrrow');
		if (selrow != null && selrow != ''){
			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
				var url = "${base}${system_log_delete.controller}.json?c=${system_log_delete.code}&ids="+selrow;
				$.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData,textStatus){
						if (textStatus == "success"){
							if (jsonData.status == "success"){
								if(selrow.length == reccount){
									jQuery("#logList").setGridParam({page:page-1}).trigger("reloadGrid");
								}else{
									jQuery("#logList").setGridParam({page:page}).trigger("reloadGrid");
								}
								top.layer.close(layers);								
							}
						}
					}
				});
			});
		}
		else{
			top.layer.msg("<msg:message code='info.prompt.delete.select'/>");
   		}	
    });    
    </security:authorize>
    </c:if>
});
</script>
</head>

<body class="skinMain">
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td align="center">
			<form:form method="post" action="log" commandName="log" name="f" id="search_form">
			<input type="hidden" name="c" value="${log.c}"/>
				<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
					<tr>
						<td align="right" width="10%" class="search_info" height="25"><msg:message code='log.conter'/><msg:message code="system.common.sign.colon"/></td>
						<td align="left" width="30%"  class="search_lable" colspan="3">
							<form:input path="content"  htmlEscape="true"/>
						</td>
						<td rowspan="3" width="20%" class="button_area" align="center">
							<input type="submit" class="btn2" id="select" value="<msg:message code='button.search'/>">
						</td>
					</tr>	
				</table>
			</form:form>
		</td>
	</tr>
	<tr>
		<td>
			<table id="logList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</body>
</html>