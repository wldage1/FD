<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#validationrulelist").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
    jQuery("#validationrulelist").jqGrid({
        
        url: 'validationrule/grid.json',
        datatype: 'json',
        colNames: ["<msg:message code='validationrule.name'/>","<msg:message code='validationrule.promptmessage'/>","<msg:message code='info.operate'/>"],
        colModel: [{name: 'name',index: 'name',width:'45%',align:'center',hidden: false,sortable: false}, 
        		   {name: 'promptMessage',index: 'promptMessage',width:'35%',align:'center',hidden: false,sortable: false},
                   {name:'act',index:'act', width:'15%',align:'center',sortable:false}],
        mtype:"POST",
        postData:{name:"${validationRule.name}",c:"${validationRule.c}"},
        rowNum:10,  
        page:"${validationRule.page}",   
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:'auto',
        autowidth: true,
        viewrecords: true,
        multiselect: true,
        jsonReader: {
        	repeatitems: false
        },
    	gridComplete: function(){
    		var ids = jQuery("#validationrulelist").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];	
    			var content = "";  
    		    <c:if test="${!empty config_validationrule_modify}">
    		    <security:authorize ifAnyGranted="${config_validationrule_modify.code}">	
    			content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${config_validationrule_modify.indexCode}'";
    			content += " title='${config_validationrule_modify.name}' >";
    			content += "<img src='${base}/${skin}/${config_validationrule_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${config_validationrule_modify.name}";
    			content += "</a>";
    		    </security:authorize>
    		    </c:if> 
    		    <c:if test="${!empty config_validationrule_delete}">
    		    <security:authorize ifAnyGranted="${config_validationrule_delete.code}">
   		    	content += "<a href='javascript:void(0);' title='${config_validationrule_delete.name}' id='" + id + "' class='shortcut_${config_validationrule_delete.indexCode}'>";
   		    	content += "<img src='${base}/${skin}/${config_validationrule_delete.icon}' weight='18' height='18' border='0' align='absmiddle'/>${config_validationrule_delete.name}";
   		    	content += "</a>";
    		    </security:authorize>
    		    </c:if>         	
    			jQuery("#validationrulelist").jqGrid('setRowData',ids[i],{act:"<div class='jqgridContainer'>" + content + "</div>"});
    		}	
    	},
    	loadComplete:function(){
    	    $(".shortcut_${config_validationrule_delete.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    	    	var data = jQuery("#validationrulelist").jqGrid("getRowData",rowid);
    		   	var index = top.$.layer({
			  	    shade : [0.5 , '#000' , true],
			  	    area : ['auto','auto'],
			  	  	offset : ['50px',''],
			  	    dialog : {
			  	        msg:"<msg:message code='receivermessage.delete'/>",
			  	        btns : 2, 
			  	        type : 4,
			  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
			  	        yes : function(){
			  	  		var url = "${base}${config_validationrule_delete.controller}.json?c=${config_validationrule_delete.code}&id="+rowid+"&prompt=name&name="+encodeURIComponent(encodeURIComponent(data.name));
	    				$.ajax({
	    					url:url,
	    					type:'post',
	    					timeout:'60000',
	    					dataType:'json',
	    					success:function(jsonData,textStatus){
	    						top.layer.close(index);
	    						if (textStatus == "success"){
	    							if (jsonData.status == "success"){
	    								//jQuery("#validationlist").jqGrid("delRowData",rowid);
	    								$("#validationrulelist").trigger("reloadGrid");    
	    							}
	    						}
	    					}
	    				});
			  	        },
			  	        no : function(){
			  	        	top.layer.close(index);
			  	        }
			  	    }
			  	});
    	    	
    	    }); 
    	    $(".shortcut_${config_validationrule_modify.indexCode}").click(function(){
    	    	var rowid = $(this).attr("id");
    			window.location.href = "${base}${config_validationrule_modify.controller}?c=${config_validationrule_modify.code}&id="+ rowid;
    	    });     
    	},
        caption:'<msg:message code="validationrule.list"/>',
        toolbar: [true,"top"]
    });
    <c:if test="${!empty config_validationrule_create}">
    <security:authorize ifAnyGranted="${config_validationrule_create.code}"> 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${config_validationrule_create.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/${config_validationrule_create.icon}")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${config_validationrule_create.name}");
	$("#t_validationrulelist").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));  
    $("#${config_validationrule_create.indexCode}","#t_validationrulelist").click(function(){
    	window.location.href = "${base}${config_validationrule_create.controller}?c=${config_validationrule_create.code}&mark=1";
    });    
    </security:authorize>
    </c:if>
        <c:if test="${!empty config_validationrule_delete}">
    <security:authorize ifAnyGranted="${config_validationrule_delete.code}">	
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${config_validationrule_delete.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/${config_validationrule_delete.icon}")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${config_validationrule_delete.name}");
	$("#t_validationrulelist").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));  
    $("#${config_validationrule_delete.indexCode}","#t_validationrulelist").click(function(){
    	var ids = jQuery("#validationrulelist").jqGrid("getGridParam","selarrrow");
    	var names = "";
    	for(var i=0;i < ids.length;i++){
    		var id = ids[i];
    		var data = jQuery("#validationrulelist").jqGrid("getRowData",id);
    		names += data.name;
    		if(i!= ids.length-1)
    			names += ",";
    	}
		if (ids != null&&ids!=""){
		   	var index = top.$.layer({
		  	    shade : [0.5 , '#000' , true],
		  	    area : ['auto','auto'],
		  	  	offset : ['50px',''],
		  	    dialog : {
		  	        msg:"<msg:message code='receivermessage.delete'/>",
		  	        btns : 2, 
		  	        type : 4,
		  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
		  	        yes : function(){
		  	        	var url = "${base}${config_validationrule_delete.controller}.json?c=${config_validationrule_delete.code}&ids="+ids+"&name="+encodeURIComponent(encodeURIComponent(names))+"&prompt=name";
						$.ajax({
							url:url,
							type:'post',
							timeout:'60000',
							dataType:'json',
							success:function(jsonData,textStatus){
								top.layer.close(index);
								if (textStatus == "success"){
									if (jsonData.status == "success"){
										//jQuery("#validationlist").jqGrid("delRowData",selrow);
										$("#validationrulelist").trigger("reloadGrid");    
									}
								}
							}
						});
		  	        },
		  	        no : function(){
		  	        	top.layer.close(index);
		  	        }
		  	    }
		  	});
		}
		else{
			top.layer.msg('<msg:message code='info.prompt.delete'/>');
   		}    	
    });    
    </security:authorize>
    </c:if>
});
</script>
</head>
<body class="skinMain">
<form:form method="post" action="validationrule" commandName="validationRule" name="f">
<input type="hidden" name="c" value="${validationRule.c}" />
<table cellpadding="0" cellspacing="1" border="0" width="100%" >
	<tr>
		<td align="center">
			<table cellpadding="0" cellspacing="1" border="0" width="100%"
				class="search">
				<tr>
					<td align="right" width="10%" class="search_info">
						<msg:message code='validationrule.name' />
					</td>
					<td align="left" width="60%" class="search_lable">
						<form:input path="name" htmlEscape="true" />
					</td>

					<td rowspan="2" width="20%" class="button_area">
						<input type=submit class="btn2" id="select"
							value="<msg:message code='button.search'/>">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="validationrulelist">
				<tr>
					<td></td>
				</tr>
			</table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</form:form>
</body>
</html>