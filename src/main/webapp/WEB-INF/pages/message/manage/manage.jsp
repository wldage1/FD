<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
$(document).ready(function(){
    jQuery("#receiverMessageList").jqGrid({
        url: 'manage/grid.json',
        datatype: 'json',
        colNames: [
                "<msg:message code='receivermessage.content'/>",
                "发送者",
                "接收者",
                "发送时间",
       	        "<msg:message code='receivermessage.receiveTime'/>",
       	        "<msg:message code='receivermessage.isReaded'/>",
       	        "<msg:message code='receivermessage.operate'/>"
       	        ],
               colModel: [
                {name: 'title',index: 'title',width:'40%',align:'left',hidden: false,sortable: false},
                {name: 'sendUserName',index: 'title',width:'10%',align:'center',hidden: false,sortable: false},
                {name: 'receiverName',index: 'title',width:'10%',align:'center',hidden: false,sortable: false},
                {name: 'createTime',index: 'createTime',width:'15%',align:'center',hidden: false,sortable: false}, 
                {name: 'readTime',index: 'readTime',width:'15%',align:'center',hidden: false,sortable: false}, 
                {name: 'isReaded',index: 'isReaded',width:'5%',align:'center',hidden: false,sortable: false,formatter:formatIsReaded},
       			{name: 'act',index:'act', width:'5%',align:'center',hidden:false,sortable:false,formatter:formatAct}
                ],
        mtype:"POST",
        postData:{c:"${receiverMessage.c}"},
        rowNum:10,     
        page:"${receiverMessage.page}",
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
        caption:'<msg:message code="receivermessage.list"/>'
    });
	
    function formatIsReaded(cellvalue, options, rowObject){
		return cellvalue == 0 ? '<msg:message code="receivermessage.noReaded"/>' : '<msg:message code="receivermessage.readed"/>';    
	 }
    
    function formatAct(cellvalue, options, rowdata){
	   	var id = rowdata.id;
		var content = "";
	    <c:if test="${!empty message_manage_delete}">
	    <security:authorize ifAnyGranted="${message_manage_delete.code}">
	    	content = "<a href='javascript:void(0);' title='${message_manage_delete.name}' id='" + id + "' class='shortcut_${message_manage_delete.indexCode}'>";
    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${message_manage_delete.name}";
    		content += "</a>"; 
	    </security:authorize>
	    </c:if>  
	    return content;	     
   	}
    
     $(".shortcut_${message_manage_delete.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
	   	var data = jQuery("#receiverMessageList").jqGrid("getRowData",rowid);
	   	var productID =data.productID;
	   	var url = "${base}/message/manage/delete.json?id="+rowid;
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
		  	        $.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData,textStatus){
						if (textStatus == "success"){
							if(jsonData.status == "success"){
								top.layer.close(index);
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='provideruser.operationsuccess'/>",type : 1}	
								});
								$("#receiverMessageList").trigger("reloadGrid"); 
							}else if(jsonData.status=="error")
							{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='provideruser.operationerror'/>",type : 3}	
								});
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

    jQuery("#receiverMessageList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidd en");
    
});
	//通用关闭
	function closeTopLayer(){
			top.layer.close(i);
	}
</script>
	</head>
	<body class="skinMain">
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%" valign="top">
					
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td>
								<table id="receiverMessageList"><tr><td>&nbsp;</td></tr></table>
								<div id="pagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
