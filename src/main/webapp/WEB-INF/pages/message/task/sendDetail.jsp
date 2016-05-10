<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<script type="text/javascript">
$(document).ready(function(){
    jQuery("#sendDetailList").jqGrid({
        url: 'detailGrid.json',
        datatype: 'json',
        colNames: [
                "<msg:message code='member.name'/>",
       	        "昵称",
       	        "性别",
       	     	"发送方式",
       	     	"发送时间",
       	     	"发送内容",
       	     	"",
       	        "内容详细"
       	        ],
               colModel: [
                {name: 'memberName',index: 'memberName',width:'16%',align:'center',sortable: false},
                {name: 'nickName',index: 'nickName',width:'16%',align:'center',sortable: false}, 
                {name: 'gender',index: 'gender',width:'5%',align:'center',sortable: false,hidden: false,formatter:formatGender},
                {name: 'sendWay',index: 'sendWay',width:'5%',align:'center',sortable: false,formatter:formatSendWay},
                {name: 'sendTime',index: 'sendTime',width:'10%',align:'center',sortable:false},
                {name: 'content',index: 'content',width:'35%',align:'left',sortable: false,hidden: false,formatter:formatContent},
                {name: 'content',index: 'contentDetail',align:'center',sortable: false,hidden: true},
       			{name: 'act',index:'act', width:'6%',align:'center',sortable:false,formatter: formatAct}
                ],
        mtype:"POST",
        postData:{c:"${sendMessage.c}",memberName:"${sendMessage.memberName}",sendTime:"${sendMessage.sendTime}",messageTaskID:"${sendMessage.messageTaskID}"},
        rowNum:10,     
        page:"${sendMessage.page}",
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
        caption:'消息详细',
        toolbar: [true,"top"]
    });
	function formatContent(cellvalue){
		var content = cellvalue;
		if(cellvalue.length >= 30){
			content = cellvalue.substring(0,29) + '...';
		}
		return content;
	}
    
    function formatSendWay(cellvalue, options, rowObject){
		var way = '';
		if(cellvalue == 1){
			way = '站内'
		}else if(cellvalue ==2){
			way='短信'
		}else if(cellvalue ==3){
			way = '邮件';
		}else if(cellvalue == 4){
			way = 'IM';
		}
    	return way;
	}
    function formatGender(cellvalue){
    	var gender = '';
    	if(cellvalue ==1){
    		gender = '男	';
    	}else if(cellvalue == 2){
    		gender = '女';
    	}else if(cellvalue == 3){
    		gender = '保密';
    	}
    	return gender;
    }
    
    function formatAct(cellvalue, options, rowdata){
	   	var id = rowdata.id;
		var content = "";
	    	content += "<a href='javascript:void(0);' onclic title='${message_task_sendDetail.name}' id='" + id + "' class='shortcut_${message_task_sendDetail.indexCode}'>";
    		content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>内容详细";
    		content += "</a>"; 
	    return content;	     
   	}
    
     $(".shortcut_${message_task_sendDetail.indexCode}").live("click",function(){
      	var rowid = $(this).attr("id");
      	var obj = jQuery('#sendDetailList').jqGrid('getRowData',rowid);
      	var contentDeatail = '<div class="skinLayout_info" style="width:600px;" id="showContentDetail"><div style="padding:10px;white-space: pre">' 
      	 +  obj.content + '</div></div>';
      	$('#showContentDetail').html(contentDeatail);
      	var index = top.$.layer({
      		shade : [0.5 , '#000' , true],
      		type : 1,
      		area : ['600px','auto'],
      		title : "消息详情",
      		border : [1],
      		page : {html : contentDeatail},
      		close : function(index){
      			top.layer.close(index);
      		}
      	});
      });
    jQuery("#sendDetailList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
});

</script>
	<form:form method="post" action="sendDetail" id="sendMessage" commandName="sendMessage" >
	<form:hidden path="c"/>
	<form:hidden path="messageTaskID"/>
		<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
		    <tr>
		      <td>
		        <table width="100%" border="0"  cellspacing="1" cellpadding="0" class="search">
				<tr>
					<td width="10%" align="right" class="search_info"><msg:message code="member.name"/>
						<msg:message code="system.common.sign.colon"/></td>
					<td width="30%" class="search_lable">
						<form:input path="memberName" />
					</td>
					<td  width="20%" class="button_area" align="center">
						<input type="button" class="btn2" id="select" onclick="this.form.submit()" value="<msg:message code='button.search'/>">&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				</table>
		      </td>
		    </tr>
			<tr>
				<td width="100%" valign="top">
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td>
								<table id="sendDetailList"><tr><td>&nbsp;</td></tr></table>
								<div id="pagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form:form>
    <div style="height: 43px;"></div>
		<div class="toolbar">
		<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" onclick="window.location.href='${base}${message_task.controller}?c=${message_task.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
		</div>
		<div class="right"></div>
	</div>
