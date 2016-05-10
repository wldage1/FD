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
	$("#configList").setGridWidth($(window).width()*0.999);　
});
function reload(orgid){
	currcount = 0;
	jQuery("#configList").jqGrid("setGridParam",{postData:{c:"${config.c}"}});		
	jQuery("#configList").trigger("reloadGrid");	
}
$(document).ready(function(){
	
    jQuery("#configList").jqGrid({
        url: 'pointsconfig/grid.json',
        datatype: 'json',
        colNames: ["<msg:message code='config.code'/>",
        		   "<msg:message code='config.points'/>", 
				   "<msg:message code='config.description'/>",
                   "<msg:message code='config.operate'/>"
                   ],
        colModel: [
				   {name: 'code',index:'code',width:'20%',align:'center',hidden: false,sortable:false},
				   {name: 'points',index: 'points',width:'30%',align:'center',hidden: false,sortable:false},
                   {name: 'description',index:'description',width:'30%',align:'center',hidden: false,sortable:false},
                   {name:'act',index:'act', width:'20%',align:'center',sortable:false,sortable:false,formatter:function(cellvalue, options, rowdata){
                   		var id = rowdata.id;
    					var content = "";
						var space = "&nbsp;&nbsp;";
    				
						<c:if test="${!empty customer_pointsconfig_modify}">
							<security:authorize ifAnyGranted="${customer_pointsconfig_modify.code}">	
							content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_pointsconfig_modify.indexCode}'";
							content += " title='${customer_config_modify.name}' >";
							content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_pointsconfig_modify.name}";
							content += "</a>";
							</security:authorize>
						</c:if>  
						
						<c:if test="${!empty customer_pointsconfig_delete}">
							<security:authorize ifAnyGranted="${customer_pointsconfig_delete.code}">
							content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_pointsconfig_delete.indexCode}'";
							content += " title='${customer_pointsconfig_delete.name}' >";
							content += "<img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_pointsconfig_delete.name}";
							content += "</a>";	
							</security:authorize>
						</c:if> 
						
		    		    return content;
						}
					}],
        mtype:"POST",
        postData:{c:"${config.c}",code:"${config.code}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,     
        page:"${config.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:<msg:message code='jqgrid.height.250'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: true,
        jsonReader: {
        	repeatitems: false
        },
        caption:'<msg:message code="config.list"/>',
        toolbar: [true,"top"]
    });
	
	 $(".shortcut_${customer_pointsconfig_delete.indexCode}").live("click",function(){
	    var rowid = $(this).attr("id");
	    var data = jQuery("#configList").jqGrid("getRowData",rowid);
		var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
			var rdata = "c=${customer_pointsconfig_delete.code}&id="+rowid+"&prompt=name";
			$.ajax({
				url:'${base}${customer_pointsconfig_delete.controller}.json',
				type:'post',
				timeout:'60000',
				dataType:'json',
				data: rdata, 
				async:false,
				cache:false,
				success:function(jsonData){
					if (jsonData.status == "success"){
	 					$("#configList").trigger("reloadGrid"); 
						top.layer.close(layers);								
	 				}else{
						top.layer.msg("<msg:message code='info.prompt.delete.limit'/>");
					}
				}
			});
 		});
    });     		
    $(".shortcut_${customer_pointsconfig_modify.indexCode}").live("click",function(){
    	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_pointsconfig_modify.controller}?c=${customer_pointsconfig_modify.code}&id="+ rowid;
    });   
	jQuery("#configList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    
    <c:if test="${!empty customer_pointsconfig_create}">
    <security:authorize ifAnyGranted="${customer_pointsconfig_create.code}">	 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
   			  .attr("id","${customer_pointsconfig_create.indexCode}")
   			  .append($("<img/>").attr("src","${base}/${skin}/images/icon/create.png")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${customer_pointsconfig_create.name}");
    $("#t_configList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));   
    $("#${customer_pointsconfig_create.indexCode}","#t_configList").click(function(){
    	window.location.href = "${base}${customer_pointsconfig_create.controller}?c=${customer_pointsconfig_create.code}";
    });    
    </security:authorize>
    </c:if>
    <c:if test="${!empty customer_pointsconfig_delete}">
    <security:authorize ifAnyGranted="${customer_pointsconfig_delete.code}">	 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${customer_pointsconfig_delete.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/images/icon/delete.png")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${customer_pointsconfig_delete.name}");
	$("#t_configList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));  
	
    $("#${customer_pointsconfig_delete.indexCode}","#t_configList").click(function(){
    	var ids = jQuery("#configList").jqGrid("getGridParam","selarrrow");    	
		if (ids && ids !=''){
			var layers = top.layer.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
				var url = "${base}${customer_pointsconfig_delete.controller}.json?c=${customer_pointsconfig_delete.code}&ids="+ids+"&prompt=name";
				$.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData,textStatus){
						if (textStatus == "success"){
							if (jsonData.status == "success"){
								$("#configList").trigger("reloadGrid");
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
    
});
function submitForm(){
	$('#code').val($.trim($('#code').val()));
	jQuery("#configList").jqGrid("setGridParam",{postData:{c:"${config.c}",code:$('#code').val()}});		
	jQuery("#configList").trigger("reloadGrid");
}
</script>
</head>
<body class="skinMain">
<form:form method="post" action="config" commandName="config" name="f">
<input type="hidden" name="c" value="${config.c}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="80%">
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td align="center">
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
							<tr>
								<td align="right" width="10%" class="search_info">
									<msg:message code='config.code'/><msg:message code="system.common.sign.colon"/>
								</td>
								<td align="left" width="70%"  class="search_lable">
									<form:input path="code" htmlEscape="true" />
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
						<table id="configList"><tr><td>&nbsp;</td></tr></table>	
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