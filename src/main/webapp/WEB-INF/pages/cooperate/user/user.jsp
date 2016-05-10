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
	$("#userList").setGridWidth($(window).width()*0.999);　
});
var deptid = null;
var zNodes = ${json};
var setting = {
		check: {
			enable: false
		},
		view:{
			dblClickExpand:dblClickExpand,
			showLine:true
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			onClick:this.onClick
		}
	};
	
	function dblClickExpand(treeId,treeNode){
		return treeNode.level > 0;
	}
	
	function onClick(e,treeid,node){
	 	deptid = node.id;
	 	reload(deptid);
	}
	
	function reload(orgid){
		currcount = 0;
		jQuery("#userList").jqGrid("setGridParam",{postData:{c:"${user.c}",orgId:orgid}});		
		jQuery("#userList").trigger("reloadGrid");	
	}
	


$(document).ready(function(){
	
	$.fn.zTree.init($("#treeDemo"), setting,zNodes);
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	treeObj.expandAll(true);
	
	<c:if test="${!empty user.orgId}">
		var node = treeObj.getNodeByParam("id", ${user.orgId}, null);
		treeObj.selectNode(node);
	</c:if>

    jQuery("#userList").jqGrid({
        url: 'user/grid.json',
        datatype: 'json',
        colNames: ["<msg:message code='user.act'/>",
        		   "<msg:message code='user.name'/>", 
				   "<msg:message code='organization.name'/>",
				   "",
                   "<msg:message code='user.operate'/>"
                   ],
        colModel: [
				   {name: 'account',index:'u.account',width:'20%',align:'center',hidden: false,sortable:false},
				   {name: 'name',index: 'u.name',width:'30%',align:'center',hidden: false,sortable:false},
                   {name: 'orgName',index:'org.name',width:'20%',align:'center',hidden: true,sortable:false},
				   {name: 'type',index:'u.type',hidden: true},
                   {name:'act',index:'act', width:'30%',align:'center',sortable:false,sortable:false,formatter:function(cellvalue, options, rowdata){
                   		var id = rowdata.id;
    					var content = "";
						var space = "&nbsp;&nbsp;";
    					
						<c:if test="${!empty cooperate_user_bind}">
							<security:authorize ifAnyGranted="${cooperate_user_bind.code}">	
							content += "<a href='javascript:void(0);' id='" + id + "' ";
							if(rowdata.type != 0){
								content += "class='shortcut_${cooperate_user_bind.indexCode}' ";
							}else{
								content += "style='color:#999;' ";
							}
							content += " title='${cooperate_user_bind.name}' >";
							content += "<img src='${base}/${skin}/images/icon/auth.png' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_user_bind.name}";
							content += "</a>";
							</security:authorize>
						</c:if> 
						
						<c:if test="${!empty cooperate_user_cpwd}">
							<security:authorize ifAnyGranted="${cooperate_user_cpwd.code}">	
							content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_user_cpwd.indexCode}' ";
							content += " title='${cooperate_user_cpwd.name}' >";
							content += "<img src='${base}/${skin}/images/icon/password.png' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_user_cpwd.name}";
							content += "</a>";
							</security:authorize>
						</c:if>
						
						<c:if test="${!empty cooperate_user_modify}">
							<security:authorize ifAnyGranted="${cooperate_user_modify.code}">	
							content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_user_modify.indexCode}'";
							content += " title='${cooperate_user_modify.name}' >";
							content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_user_modify.name}";
							content += "</a>";
							</security:authorize>
						</c:if>  
						
						<c:if test="${!empty cooperate_user_delete}">
							<security:authorize ifAnyGranted="${cooperate_user_delete.code}">
							content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_user_delete.indexCode}'";
							content += " title='${cooperate_user_delete.name}' >";
							content += "<img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_user_delete.name}";
							content += "</a>";	
							</security:authorize>
						</c:if> 
						
		    		    return content;
						}
					}],
        mtype:"POST",
        postData:{c:"${user.c}"<c:if test="${!empty user.orgId}">,orgId:${user.orgId}</c:if>,name:"${user.name}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,     
        page:"${user.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:<msg:message code='jqgrid.height.250'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: true,
        jsonReader: {
        	repeatitems: false
        },
        caption:'<msg:message code="user.list"/>',
        toolbar: [true,"top"]
    });
	
	 $(".shortcut_${cooperate_user_delete.indexCode}").live("click",function(){
    	    	var rowid = $(this).attr("id");
    	    	var data = jQuery("#userList").jqGrid("getRowData",rowid);
    			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
					var rdata = "c=${cooperate_user_delete.code}&id="+rowid+"&prompt=name&name="+encodeURIComponent(encodeURIComponent(data.name))+"";
					$.ajax({
						url:'${base}${cooperate_user_delete.controller}.json',
						type:'post',
						timeout:'60000',
						dataType:'json',
						data: rdata, 
						async:false,
						cache:false,
						success:function(jsonData){
							if (jsonData.status == "success"){
    							//jQuery("#userList").jqGrid("delRowData",rowid);
    							$("#userList").trigger("reloadGrid"); 
								top.layer.close(layers);
    						}else{
								top.layer.msg("<msg:message code='info.prompt.delete.limit'/>");
							}
						}
					});
    			});
    });     		
    $(".shortcut_${cooperate_user_modify.indexCode}").live("click",function(){
    	var rowid = $(this).attr("id");
		window.location.href = "${base}${cooperate_user_modify.controller}?c=${cooperate_user_modify.code}&id="+ rowid;
    });   
    $(".shortcut_${cooperate_user_cpwd.indexCode}").live("click",function(){
    	var rowid = $(this).attr("id");
    	window.location.href = "${base}${cooperate_user_cpwd.controller}?c=${cooperate_user_cpwd.code}&id="+rowid;
    }); 
    $(".shortcut_${cooperate_user_bind.indexCode}").live("click",function(){
    	var rowid = $(this).attr("id");
    	window.location.href = "${base}${cooperate_user_bind.controller}?c=${cooperate_user_bind.code}&id="+rowid;
    });
	
	jQuery("#userList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    <c:if test="${!empty cooperate_user_create}">
    <security:authorize ifAnyGranted="${cooperate_user_create.code}">	 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${cooperate_user_create.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/images/icon/create.png")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${cooperate_user_create.name}");
	$("#t_userList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));  
    $("#${cooperate_user_create.indexCode}","#t_userList").click(function(){
    	if(deptid){
    		window.location.href = "${base}${cooperate_user_create.controller}?c=${cooperate_user_create.code}&orgId="+deptid+"";
    	}else{
    		top.layer.msg("<msg:message code='role.orgId.select'/>");
    	}
    });    
    </security:authorize>
    </c:if>
    
    <c:if test="${!empty cooperate_user_delete}">
    <security:authorize ifAnyGranted="${cooperate_user_delete.code}">	 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${cooperate_user_delete.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/images/icon/delete.png")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${cooperate_user_delete.name}");
	$("#t_userList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));  
    $("#${cooperate_user_delete.indexCode}","#t_userList").click(function(){
    	var ids = jQuery("#userList").jqGrid("getGridParam","selarrrow");
    	var names = "";
    	for(var i=0;i < ids.length;i++){
    		var id = ids[i];
    		var data = jQuery("#userList").jqGrid("getRowData",id);
    		names += data.name;
    		if(i!= ids.length-1)
    			names += ",";
    	}
		if (ids && ids !=''){
			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
				var url = "${base}${cooperate_user_delete.controller}.json?c=${cooperate_user_delete.code}&ids="+ids+"&name="+encodeURIComponent(encodeURIComponent(names))+"&prompt=name";
				$.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData,textStatus){
						if (textStatus == "success"){
							if (jsonData.status == "success"){
								//jQuery("#userList").jqGrid("delRowData",selrow);
								$("#userList").trigger("reloadGrid");
								top.layer.close(layers);
							}else{
								top.layer.msg("<msg:message code='info.prompt.delete.limit'/>");
							}
						}
					}
				});
			});
		}
		else{
    		top.layer.msg("<msg:message code='info.prompt.delete'/>");
   		}    	
    });    
    </security:authorize>
    </c:if> 
    
    $("#sbtn").click(function(){
    	var name = $("#name").val();
    	var url = "grid.json?c=1&name="+name;
    	jQuery("#bigset").jqGrid('setGridParam',{url:url,page:1}).trigger("reloadGrid");
    });
    
});
function submitForm(){
	$('#name').val($.trim($('#name').val()));
	jQuery("#userList").jqGrid("setGridParam",{postData:{c:"${user.c}",name:$('#name').val()}});		
	jQuery("#userList").trigger("reloadGrid");
}
</script>
</head>
<body class="skinMain">
<form:form method="post" action="user" commandName="user" name="f">
<input type="hidden" name="c" value="${user.c}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="308" valign="top">
			<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width:308px; ">
				<div class="ui-jqgrid-view" id="gview_dictionaryItemList" style="width:308px;">
					<div class="ui-jqgrid-view" id="gview_dictionaryItemList" style="width:308px;">
						<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
							<msg:message code="commission.list"/>
						</div>
						<div style="width:308px;height:100%; " class="ui-state-default ui-jqgrid-hdiv">
							<div class="ui-jqgrid-hbox">
								<ul id="treeDemo" class="ztree" style="margin-top:0px;width:300px;"></ul>  
							</div>
						</div>
					</div>
				</div>
			</div>		
		</td>
		<td width="80%">
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td align="center">
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
							<tr>
								<td align="right" width="10%" class="search_info">
									<msg:message code='user.name'/><msg:message code="system.common.sign.colon"/>
								</td>
								<td align="left" width="70%"  class="search_lable">
									<form:input path="name" htmlEscape="true" />
								</td>
								<td rowspan="2" width="20%" class="button_area">
									<input type=button class="btn2" id="select" value="<msg:message code='button.search'/>" onclick="submitForm();">
								</td>				
							</tr>		
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table id="userList"><tr><td>&nbsp;</td></tr></table>	
						<div id="pagered"></div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td></td>
	</tr>
</table>
</form:form>
</body>	
</html>