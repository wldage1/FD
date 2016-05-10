<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
$(document).ready(function(){
    jQuery("#messageTaskList").jqGrid({
        url: 'config/grid.json',
        datatype: 'json',
        colNames: [
                "<msg:message code='messageTask.name'/>",
       	        "<msg:message code='messageTask.code'/>",
       	        "",
       	     	"<msg:message code='messageTask.sendType'/>",
       	     	"<msg:message code='messageTask.status'/>",
       	        "<msg:message code='messageTask.operate'/>"
       	        ],
               colModel: [
                {name: 'name',index: 'name',width:'40%',align:'center',sortable: false},
                {name: 'code',index: 'code',width:'40%',align:'center',sortable: false}, 
                {name: 'type',index: 'type',width:'10%',align:'center',sortable: false,hidden: true,formatter: formatType},
                {name: 'sendType',index: 'sendType',width:'10%',align:'center',sortable: false,formatter: formatSendType},
                {name: 'status',index: 'status',width:'5%',align:'center',sortable: false,formatter: formatStatus},
       			{name: 'act',index:'act', width:'5%',align:'center',sortable:false,formatter: formatAct}
                ],
        mtype:"POST",
        postData:{c:"${messageTask.c}",code:"${messageTask.code}",name:"${messageTask.name}",isConfigReceiver:"${messageTask.isConfigReceiver }"},
        rowNum:10,     
        page:"${messageTask.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:	230,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
        caption:'<msg:message code="messageTask.list"/>'
    });
	
    function formatType(cellvalue, options, rowObject){
		return cellvalue == 1 ? '<msg:message code="messageTask.inlineTask"/>' : '<msg:message code="messageTask.customTask"/>';    
	}
    
    function formatSendType(cellvalue, options, rowObject){
		return cellvalue == 1 ? '<msg:message code="messageTask.immediatelySend"/>' : '<msg:message code="messageTask.timingSend"/>';    
	}
    
    function formatStatus(cellvalue, options, rowObject){
    	var content;
    	switch (cellvalue){
    		case 1:
    		content='<msg:message code="messageTask.forbidden"/>';
    		break;
    		case 2:
    		content='<msg:message code="messageTask.enabled"/>';
    		break;
    		case 3:
    		content='<msg:message code="messageTask.pause"/>';
    		break;
    	}
		return content;    
	}
    
    function formatAct(cellvalue, options, rowdata){
	   	var id = rowdata.id;
		var content = "";
	    <c:if test="${!empty message_task_config_messageConfig}">
	    <security:authorize ifAnyGranted="${message_task_config_messageConfig.code}">
	    	content = "<a href='javascript:void(0);' title='${message_task_config_messageConfig.name}' id='" + id + "' class='shortcut_${message_task_config_messageConfig.indexCode}'>";
    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${message_task_config_messageConfig.name}";
    		content += "</a>"; 
	    </security:authorize>
	    </c:if>  
	    return content;	     
   	}
    
     $(".shortcut_${message_task_config_messageConfig.indexCode}").live("click",function(){
     	var rowid = $(this).attr("id");
		window.location.href = "${base}${message_task_config_messageConfig.controller}?c=${message_task_config_messageConfig.code}&id="+ rowid;
     });

    jQuery("#messageTaskList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidd en");
    
});

</script>
	</head>
	<body class="skinMain">
	<ul id="tabnav">
		<li class="tab_1" ><a href="${base }${message_task.controller}?c=${message_task.code}"  >自定义任务</a></li>
		<li class="tab_2" ><a href="#" class="selected">内置任务</a></li>
	</ul>
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
		   <tr>
		      <td>
		      <form:form method="post" action="config"  commandName="messageTask" >
	            <form:hidden path="c"/>
		        <table width="100%" border="0"  cellspacing="1" cellpadding="0" class="search">
				<tr>
					<td width="15%" align="right" class="search_info"><msg:message code="messageTask.name"/>
						<msg:message code="system.common.sign.colon"/></td>
					<td width="25%" class="search_lable">
						<form:input path="name" />
					</td>
					<td width="15%" align="right" class="search_info">任务代码
						<msg:message code="system.common.sign.colon"/></td>
					<td width="25%" class="search_lable">
						<form:input path="code" />
					</td>
					<td  width="20%" class="button_area" align="center" rowspan="2">
						<input type="button" class="btn2" id="select" onclick="this.form.submit()" value="<msg:message code='button.search'/>">&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<tr style="background-color: #fff;">
					<td width="15%" align="right" class="search_info">是否配置接收人员
						<msg:message code="system.common.sign.colon"/></td>
					<td width="25%" class="search_lable">
						<form:select path="isConfigReceiver">
							<form:option value="">请选择</form:option>
							<form:option value="1">是</form:option>
							<form:option value="0">否</form:option>
					    </form:select>
					</td>
					<td width="15%" align="right"></td>
					<td width="25%">
					</td>
					<td  width="20%" class="button_area" align="center">
					</td>
				</tr>
				</table>
				</form:form>
		      </td>
		    </tr>
			<tr>
				<td width="100%" valign="top">
					
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td>
								<table id="messageTaskList"><tr><td>&nbsp;</td></tr></table>
								<div id="pagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
