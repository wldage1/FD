<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#messageTaskList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
    jQuery("#messageTaskList").jqGrid({
        url: 'task/grid.json',
        datatype: 'json',
        colNames: [
                "<msg:message code='messageTask.name'/>",
       	        "<msg:message code='messageTask.code'/>",
       	        "<msg:message code='messageTask.type'/>",
       	     	"<msg:message code='messageTask.sendType'/>",
       	     	"<msg:message code='messageTask.status'/>",
       	        "<msg:message code='messageTask.operate'/>"
       	        ],
               colModel: [
                {name: 'name',index: 'name',width:'10%',align:'center',sortable: false},
                {name: 'code',index: 'code',width:'10%',align:'center',sortable: false}, 
                {name: 'type',index: 'type',width:'8%',align:'center',sortable: false,hidden: true,formatter: formatType},
                {name: 'sendType',index: 'sendType',width:'8%',align:'center',sortable: false,formatter: formatSendType},
                {name: 'status',index: 'status',width:'8%',align:'center',sortable: false,formatter: formatStatus},
       			{name: 'act',index:'act', width:'8%',align:'center',sortable:false,formatter: formatAct}
                ],
        mtype:"POST",
        postData:{c:"${messageTask.c}",name:"${messageTask.name}"},
        rowNum:10,     
        page:"${messageTask.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:	230,
        autowidth: true,
        viewrecords: true,
        multiselect: true,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
        caption:'<msg:message code="messageTask.list"/>',
        toolbar: [true,"top"]
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
	    <c:if test="${!empty message_task_modify}">
	    <security:authorize ifAnyGranted="${message_task_modify.code}">
	    	content += "<a href='javascript:void(0);' title='${message_task_modify.name}' id='" + id + "' class='shortcut_${message_task_modify.indexCode}'>";
    		content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${message_task_modify.name}";
    		content += "</a>"; 
	    </security:authorize>
	    </c:if>
	    <c:if test="${!empty message_task_delete}">
	    <security:authorize ifAnyGranted="${message_task_delete.code}">
	    	content += "<a href='javascript:void(0);' title='${message_task_delete.name}' id='" + id + "' class='shortcut_${message_task_delete.indexCode}'>";
    		content += "<img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${message_task_delete.name}";
    		content += "</a>"; 
	    </security:authorize>
	    </c:if>
	    <c:if test="${!empty message_task_sendDetail}">
	    <security:authorize ifAnyGranted="${message_task_sendDetail.code}">
	    	content += "<a href='javascript:void(0);' title='${message_task_sendDetail.name}' id='" + id + "' class='shortcut_${message_task_sendDetail.indexCode}'>";
    		content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${message_task_sendDetail.name}";
    		content += "</a>"; 
	    </security:authorize>
	    </c:if>
	    return content;	     
   	}
    
     $(".shortcut_${message_task_modify.indexCode}").live("click",function(){
      	var rowid = $(this).attr("id");
 		window.location.href = "${base}${message_task_modify.controller}?c=${message_task_modify.code}&id="+ rowid;
      });
     $(".shortcut_${message_task_delete.indexCode}").live("click",function(){
    		var rowid = $(this).attr("id");
			var rowdata = jQuery("#messageTaskList").jqGrid('getRowData',rowid);
			var prompt = "<msg:message code='info.prompt.delete.confirm'/>";
			var url = "${base}${message_task_delete.controller}.json?c=${message_task_delete.code}&id=" + rowid;
			var index = top.$.layer({
				shade  : [0.5 , '#000' , true],
				dialog   : {
					msg  : prompt,
					btns : 2,
					type : 4,
					btn  : ["<msg:message code='button.confirm'/>", "<msg:message code='button.cancel'/>"],
					yes  : function(){
						$.ajax({
							url:url,
							type:'post',
							timeout:'60000',
							dataType:'json',
							success:function(jsonData,textStatus){
								if(textStatus == "success"){
									if(jsonData.status == "success"){
										top.layer.close(index);
										if(jsonData.nodeleteids != ""){
											top.layer.msg("任务已经发送消息，不能删除!");
										}else{
										   $("#messageTaskList").trigger("reloadGrid");
										}
									}
								}
							}
						});
					},
					no   : function(){
						top.layer.close(index);
					}
				}
			});
      });
     $(".shortcut_${message_task_sendDetail.indexCode}").live("click",function(){
      	var rowid = $(this).attr("id");
 		window.location.href = "${base}${message_task_sendDetail.controller}?c=${message_task_sendDetail.code}&messageTaskID="+ rowid;
      });

     <c:if test="${!empty message_task_create}">
	 <security:authorize ifAnyGranted="${message_task_create.code}">
		var $ea = $("<a></a>").attr("href","javascript:void(0)")
		 .attr("id","${message_task_create.indexCode}")
		 .append($("<img/>").attr("src","${base}/${skin}/${message_task_create.icon}")
		 .attr("width","18").attr("height","18").attr("border","0")
		 .attr("border","0").attr("align","absmiddle"))
		 .append("${message_task_create.name}");
		$("#t_messageTaskList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));
		$("#${message_task_create.indexCode}","#t_messageTaskList").click(function(){
			window.location.href = "${base}${message_task_create.controller}?c=${message_task_create.code}";
		});
	 </security:authorize>
	 </c:if>
	 
	 <c:if test="${!empty message_task_delete}">
	 <security:authorize ifAnyGranted="${message_task_delete.code}">
		var $ea = $("<a></a>").attr("href","javascript:void(0)")
		 .attr("id","${message_task_delete.indexCode}")
		 .append($("<img/>").attr("src","${base}/${skin}/${message_task_delete.icon}")
		 .attr("width","18").attr("height","18").attr("border","0")
		 .attr("border","0").attr("align","absmiddle"))
		 .append("${message_task_delete.name}");
		$("#t_messageTaskList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));
		$("#${message_task_delete.indexCode}","#t_messageTaskList").click(function(){
			var selrow = jQuery("#messageTaskList").jqGrid("getGridParam","selarrrow");
			if (selrow != null && selrow != ""){
				var rowdata;
				var prompt = "<msg:message code='info.prompt.delete.confirm'/>";
				var url = "${base}${message_task_delete.controller}.json?c=${message_task_delete.code}&ids=" + selrow;
				var index = top.$.layer({
			  	    shade  : [0.5 , '#000' , true],
			  	    area   : ['auto','auto'],
			  	  	offset : ['50px',''],
			  	    dialog : {
			  	    msg    : prompt,
			  	    btns   : 2, 
			  	    type   : 4,
			  	    btn    : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
			  	    yes    : function(){
						$.ajax({
							url:url,
							type:'post',
							timeout:'60000',
							dataType:'json',
							success:function(jsonData,textStatus){
								if(textStatus == "success"){
									if(jsonData.status == "success"){
										top.layer.close(index);
										if(jsonData.nodeleteids != ""){
											var taskIds = jsonData.nodeleteids.split(",");
											var tips = "";
										    $(taskIds).each(function(){
												var rowdata = jQuery("#messageTaskList").jqGrid('getRowData',this);
                                                if(tips == ""){
                                                	tips = rowdata.name;
                                                }else{
                                                	tips += "," + rowdata.name;
                                                }
											});
											top.layer.msg( tips + "已经发送消息，不能删除!");
										}
										   $("#messageTaskList").trigger("reloadGrid");
									}
								}
							}
						});
			  	    },
			  	    no     : function(){
			  	        	top.layer.close(index);
			  	        }
	  	    		}
				});
			} else{
				top.$.layer({
					shade  : [0.5 , '#000' , true],
			  		offset : ['50px',''],
			  	 	title  : "<msg:message code='info.prompt'/>",
				    dialog : {msg:"<msg:message code='info.prompt.delete'/>",type : 3}	
				});
			}
		});
	 </security:authorize>
	 </c:if>
     
    jQuery("#messageTaskList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidd en");
    
});

</script>
	</head>
	<body class="skinMain">
	<ul id="tabnav">
		<li class="tab_1" ><a href="#"  class="selected">自定义任务</a></li>
		 <security:authorize ifAnyGranted="${message_task_config.code}">
		<li class="tab_2" ><a href="${base }${message_task_config.controller}?c=${message_task_config.code}" >内置任务</a></li>
		</security:authorize>
	</ul>
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
		      <td>
		      <form:form method="post" action="task"  commandName="messageTask" >
	            <form:hidden path="c"/>
		        <table width="100%" border="0"  cellspacing="1" cellpadding="0" class="search">
				<tr>
					<td width="10%" align="right" class="search_info"><msg:message code="messageTask.name"/>
						<msg:message code="system.common.sign.colon"/></td>
					<td width="30%" class="search_lable">
						<form:input path="name" />
					</td>
					<td  width="20%" class="button_area" align="center">
						<input type="button" class="btn2" id="select" onclick="this.form.submit()" value="<msg:message code='button.search'/>">&nbsp;&nbsp;&nbsp;&nbsp;
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
