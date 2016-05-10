<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#roleList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
	if('${role.orgId}' == ''){
		$('#orgId')[0].selectedIndex = 0;
	}else{
		$('#orgId').val('${role.orgId}');
	}
    jQuery("#roleList").jqGrid({
        treeGridModel: 'adjacency',
        ExpandColumn: 'name',
        ExpandColClick: true,
        url: 'role/grid.json',
        datatype: 'json',
        colNames: ["<msg:message code='role.name'/>",
        	"<msg:message code='role.description'/>",
        	"<msg:message code='organization.name'/>",
        	"<msg:message code='info.operate'/>"],
        colModel: [{name: 'name',index: 'role.name',width:'25%',align:'center',hidden: false,sortable: false}, 
                   {name: 'description',index:'role.description',width:'25%',align:'center',hidden: false,sortable: false},
				   {name: 'orgName',index:'org.name',width:'25%',align:'center',hidden: true,sortable: false},
                   {name:'act',index:'act', width:'25%',align:'center',sortable:false,sortable: false}],
        mtype:"POST",
        postData:{c:"${role.c}",orgId:$("#orgId").val()},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,
        page:"${role.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:<msg:message code='jqgrid.height.300'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: true,
        jsonReader: {
        	repeatitems: false
        },
    	gridComplete: function(){
    		var ids = jQuery("#roleList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			var content = "";
    			var space = "&nbsp;&nbsp;";
    			<c:if test="${!empty cooperate_role_modify}">
    		    <security:authorize ifAnyGranted="${cooperate_role_modify.code}">	
    			content += "<a href='javascript:void(0);' title='${cooperate_role_modify.name}' id='" + id + "' class='shortcut_${cooperate_role_modify.indexCode}' title='${cooperate_role_modify.name}'>";
    			content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_role_modify.name}";
    			content += "</a>";
    			content += space;
    		    </security:authorize>
    		    </c:if>
    			<c:if test="${!empty cooperate_role_funcauth}">
    		    <security:authorize ifAnyGranted="${cooperate_role_funcauth.code}">	
    			content += "<a href='javascript:void(0);' title='${cooperate_role_funcauth.name}' id='" + id + "' class='shortcut_${cooperate_role_funcauth.indexCode}' title='${cooperate_role_funcauth.name}'>";
    			content += "<img src='${base}/${skin}/images/icon/auth.png' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_role_funcauth.name}";
    			content += "</a>";
    			content += space;
    		    </security:authorize>
    		    </c:if>
				
    		    <c:if test="${!empty cooperate_role_delete}">
    		    <security:authorize ifAnyGranted="${cooperate_role_delete.code}">
   		    	content += "<a href='javascript:void(0);' title='${cooperate_role_delete.name}' id='" + id + "' class='shortcut_${cooperate_role_delete.indexCode}' title='${cooperate_role_delete.name}'>";
   		    	content += "<img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_role_delete.name}";
   		    	content += "</a>";
       			content += space;
    		    </security:authorize>
    		    </c:if>      	
    			jQuery("#roleList").jqGrid('setRowData',ids[i],{act:"<div class='jqgridContainer'>" + content + "</div>"});
    		}	
    	},
    	loadComplete:function(){
    	    $(".shortcut_${cooperate_role_delete.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    	    	var data = jQuery("#roleList").jqGrid("getRowData",rowid);
    			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
    				var url = "${base}${cooperate_role_delete.controller}.json?c=${cooperate_role_delete.code}&id="+rowid+"&prompt=name&name="+encodeURIComponent(encodeURIComponent(data.name));
    				$.ajax({
    					url:url,
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					success:function(jsonData,textStatus){
    						if (textStatus == "success"){
    							if (jsonData.status == "success"){
    								//jQuery("#roleList").jqGrid("delRowData",rowid);
    								$("#roleList").trigger("reloadGrid"); 
									top.layer.close(layers);
    							}else{
									top.layer.msg("<msg:message code='info.role.delete.limit'/>");
								}
    						}
    					}
    				});
    			});
    	    });     		
    	    $(".shortcut_${cooperate_role_modify.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			window.location.href = "${base}${cooperate_role_modify.controller}?c=${cooperate_role_modify.code}&id="+rowid;
    	    });    
    	    $(".shortcut_${cooperate_role_funcauth.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			window.location.href = "${base}${cooperate_role_funcauth.controller}?c=${cooperate_role_funcauth.code}&id="+rowid;
    	    }); 
    	    
    	},
        caption:'<msg:message code="role.list"/>',
        toolbar: [true,"top"]
    });
	
	jQuery("#roleList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	//JQgrid 添加导航工具
	
/* 	jQuery("#roleList").jqGrid('navGrid','#pagered',
			{edit:false,add:false,del:false},
			{},
			{},
			{},
			{multipleSearch:true, multipleGroup:true, showQuery: true});
	jQuery("#roleList").jqGrid('navButtonAdd','#pagered',{
	    caption: "Columns",
	    title: "Reorder Columns",
	    onClickButton : function (){
	        jQuery("#roleList").jqGrid('columnChooser');
	    }
	}); */
    
    <c:if test="${!empty cooperate_role_create}">
    <security:authorize ifAnyGranted="${cooperate_role_create.code}">	 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
   			  .attr("id","${cooperate_role_create.indexCode}")
   			  .append($("<img/>").attr("src","${base}/${skin}/images/icon/create.png")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${cooperate_role_create.name}");
    $("#t_roleList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));   
    $("#${cooperate_role_create.indexCode}","#t_roleList").click(function(){
    	window.location.href = "${base}${cooperate_role_create.controller}?c=${cooperate_role_create.code}&orgId="+$('#orgId').val()+"";
    });    
    </security:authorize>
    </c:if>
    
    <c:if test="${!empty cooperate_role_delete}">
    <security:authorize ifAnyGranted="${cooperate_role_delete.code}">	 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${cooperate_role_delete.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/images/icon/delete.png")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${cooperate_role_delete.name}");
	$("#t_roleList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));
    $("#${cooperate_role_delete.indexCode}","#t_roleList").click(function(){
    	var ids = jQuery("#roleList").jqGrid("getGridParam","selarrrow");
    	var names = "";
    	for(var i=0;i < ids.length;i++){
    		var id = ids[i];
    		var data = jQuery("#roleList").jqGrid("getRowData",id);
    		names += data.name;
    		if(i!= ids.length-1)
    			names += ",";
    	}
		if (ids && ids !=''){
			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
				var url = "${base}${cooperate_role_delete.controller}.json?c=${cooperate_role_delete.code}&ids="+ids+"&name="+encodeURIComponent(encodeURIComponent(names))+"&prompt=name";
				$.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData,textStatus){
						if (textStatus == "success"){
							if (jsonData.status == "success"){
								//jQuery("#roleList").jqGrid("delRowData",selrow);
								$("#roleList").trigger("reloadGrid");
								top.layer.close(layers);
							}else{
								top.layer.msg("<msg:message code='info.prompt.delete.limit'/>");
							}
						}
					}
				});
			});
		}else{
    		top.layer.msg("<msg:message code='info.prompt.delete'/>");
   		}    	
    });    
    </security:authorize>
    </c:if>  
    
	$("#orgId").change(function(){
		jQuery("#roleList").jqGrid("setGridParam",{postData:{c:"${role.c}",orgId:$('#orgId').val()}}).trigger("reloadGrid");
	});

});


</script>
</head>
<body class="skinMain">
<table cellpadding="2" cellspacing="2" border="0" width="100%">
	<tr>
		<td width="15%"  valign="top">
			<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_dictionaryList" dir="ltr" style="width: 208px; ">
				<div class="ui-jqgrid-view" id="gview_dictionaryList" style="width: 208px; ">
					<div class="ui-jqgrid-view" id="gview_dictionaryList" style="width: 208px; ">
					<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix"><msg:message code='commission.list'/></div>
					<div style="width: 208px; height:383px; background:#FFF;" class="ui-state-default ui-jqgrid-hdiv"><div class="ui-jqgrid-hbox" >
					<form:form commandName="role">
					<form:select path="orgId" id="orgId" items="${orgList}" itemValue="id" itemLabel="name" size="${orgListSize}" style="width:208px;border:0px;font-size:10pt;"/>
					</form:form>
					</div>
					</div>
					</div>
				</div>
			</div>
		</td>
		<td width="85%" valign="top">
			<table id="roleList"><tr><td>&nbsp;</td></tr></table>	
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</body>
</html>