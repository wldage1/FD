<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#receiverMessageList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
    jQuery("#receiverMessageList").jqGrid({
        url: 'mymessage/grid.json',
        datatype: 'json',
        colNames: [
                "<msg:message code='receivermessage.content'/>",
                "发送者",
                "发送时间",
       	        "阅读时间",
       	        "<msg:message code='receivermessage.isReaded'/>",
       	        "<msg:message code='receivermessage.operate'/>"
       	        ],
               colModel: [
                {name: 'title',index: 'title',width:'50%',align:'left',hidden: false,sortable: false,formatter:bindMessageClick},
                {name: 'sendUserName',index: 'sendUserName',width:'10%',align:'center',hidden: false,sortable: false},
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
        multiselect: true,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
        caption:'<msg:message code="receivermessage.list"/>',
        toolbar: [true,"top"]
    });
	function bindMessageClick(cellvalue, options, rowdata){
	 	var id = rowdata.id;
	 	var hrefBegin = cellvalue.indexOf("href=\"");
	 	var hrefBegin1 = cellvalue.indexOf("href='");
	 	var linkHref = "";
	 	var hrefEnd = "";
	 	if(hrefBegin >= 0){
	 		hrefEnd = cellvalue.indexOf("\"",hrefBegin + 6);
	 		linkHref = cellvalue.substring(hrefBegin + 6,hrefEnd);
	 	} else if (hrefBegin1 >= 0){
	 		hrefEnd = cellvalue.indexOf("'",hrefBegin1 + 6);
	 		linkHref = cellvalue.substring(hrefBegin1 + 6,hrefEnd);
	 	}
	 	var code = "";
	 	if(cellvalue.indexOf("c=") > 0){
	 		if(cellvalue.indexOf("&",cellvalue.indexOf("c=") + 2) >0){
	 			var end = cellvalue.indexOf("&",cellvalue.indexOf("c=") +2) ;
	 			code = cellvalue.substring(cellvalue.indexOf("c=") + 2,end);
	 		}else if(cellvalue.indexOf("'",cellvalue.indexOf("c=") + 2) >0){
	 			var end = cellvalue.indexOf("'",cellvalue.indexOf("c=") +2) ;
	 			code = cellvalue.substring(cellvalue.indexOf("c=") + 2,end);
	 		}else if(cellvalue.indexOf("\"",cellvalue.indexOf("c=") + 2) >0){
	 			var end = cellvalue.indexOf("\"",cellvalue.indexOf("c=") +2) ;
	 			code = cellvalue.substring(cellvalue.indexOf("c=") + 2,end);
	 		}
	 	if(code != "" && linkHref != ''){
	 		var menuCode = parseInt(code);
	 		var secondCode = "";
	 		if(!isNaN(menuCode)){
	 			if(menuCode.toString().length % 2 == 0){
	 				secondCode = menuCode.toString().substring(0,4);
	 			}else{
	 				secondCode = menuCode.toString().substring(0,3);
	 			}
	 			if(top.document.getElementById(secondCode) != 'undefined' && top.document.getElementById(secondCode) != '' 
	 					&& top.document.getElementById(secondCode) != null){
	 			    var menu = top.$('#' + secondCode);
	 				var temp = menu.html();
			 		menu.find("img").remove();
			 		var tabText = menu.html().trim();
			 		menu.html(temp);
			 		var isReadedFlag = "NO";
				    if(rowdata.isReaded == 1){
				    	isReadedFlag = "YES";
				    } 
			 		//修改链接的点击事件
				    cellvalue = cellvalue.replace(linkHref,"#");
				    cellvalue = "<span onclick=\"openNewTab(" + id +",'" + secondCode + "','"+ tabText +"','" + linkHref + "','"+ isReadedFlag +"');\" >"
				    + cellvalue + "</span>";
	 			}
	 		}
	 	}
	 }else{
		  if(rowdata.isReaded == 0){
		    	cellvalue = "<span onclick=\"updataMessage(" + id +");\" >" + cellvalue + "</span>";
		   }  
	 }
	    return cellvalue;
	}

	
    function formatIsReaded(cellvalue, options, rowObject){
		return cellvalue == 0 ? '<msg:message code="receivermessage.noReaded"/>' : '<msg:message code="receivermessage.readed"/>';    
	 }
    
    function formatAct(cellvalue, options, rowdata){
	   	var id = rowdata.id;
		var content = "";
	    <c:if test="${!empty message_mymessage_delete}">
	    <security:authorize ifAnyGranted="${message_mymessage_delete.code}">
	    	content = "<a href='javascript:void(0);' title='${message_mymessage_delete.name}' id='" + id + "' class='shortcut_${message_mymessage_delete.indexCode}'>";
    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${message_mymessage_delete.name}";
    		content += "</a>"; 
	    </security:authorize>
	    </c:if>  
	    return content;	     
   	}
    
     $(".shortcut_${message_mymessage_delete.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
	   	var data = jQuery("#receiverMessageList").jqGrid("getRowData",rowid);
	   	var productID =data.productID;
	   	var url = "${base}/message/mymessage/delete.json?id="+rowid;
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
							/* 	top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='provideruser.operationsuccess'/>",type : 1}	
								}); */
								if(data.isReaded.trim() == '未读'){
									top.readOneSysMessage(parseInt(rowid));
								}
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
     
	 <c:if test="${!empty message_mymessage_delete}">
	 <security:authorize ifAnyGranted="${message_mymessage_delete.code}">
		var $ea = $("<a></a>").attr("href","javascript:void(0)")
		 .attr("id","${message_mymessage_delete.indexCode}")
		 .append($("<img/>").attr("src","${base}/${skin}/${message_mymessage_delete.icon}")
		 .attr("width","18").attr("height","18").attr("border","0")
		 .attr("border","0").attr("align","absmiddle"))
		 .append("${message_mymessage_delete.name}");
		$("#t_receiverMessageList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));
		$("#${message_mymessage_delete.indexCode}","#t_receiverMessageList").click(function(){
			var selrow = jQuery("#receiverMessageList").jqGrid("getGridParam","selarrrow");
			if (selrow != null && selrow != ""){
				var rowdata;
				var prompt = "<msg:message code='info.prompt.delete.confirm'/>";
				var url = "${base}${message_mymessage_delete.controller}.json?c=${message_mymessage_delete.code}&ids=" + selrow;
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
										for(var i=0;i<selrow.length;i++){
											var gid = selrow[i];	   	
											var data = jQuery("#receiverMessageList").jqGrid("getRowData",gid);
											if(data.isReaded.trim() == '未读'){
												top.readOneSysMessage(parseInt(gid));
											}
										}
										$("#receiverMessageList").trigger("reloadGrid");
										top.layer.close(index);
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
     
    jQuery("#receiverMessageList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
});
	//通用关闭
	function closeTopLayer(){
			top.layer.close(i);
	}
	
	function updataMessage(id){
	     var url = "${base}/message/mymessage/update.json?id="+id;
	    $.ajax({
			url:url,
			type:'post',
			timeout:'60000',
			dataType:'json',
			success:function(jsonData,textStatus){
				if (textStatus == "success" ){
					$("#receiverMessageList").trigger("reloadGrid"); 
					top.readOneSysMessage(id);
				 }
			  }
	     }); 
	}
	function openNewTab(id,code,text,href,isReadedFlag){
		top.menuTab(code,text,href);
		if(isReadedFlag == 'NO'){
			updataMessage(id);
		}
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
