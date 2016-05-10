<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${base}/common/js/layer/layer.js"></script>
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#memberList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
	
    jQuery("#memberList").jqGrid({
        treeGridModel: 'adjacency',
        url: 'member/grid.json',
        datatype: 'json',
        colNames: [
                  '<msg:message code="member.name"/>',
                   '<msg:message code="member.teamID"/>', 
                   '<msg:message code="member.nickName"/>',
                   '<msg:message code="member.teamName"/>',
                   '<msg:message code="member.mobilePhone"/>',
                   '<msg:message code="member.iDCard"/>',
                   '<msg:message code="member.status"/>',
                   '<msg:message code="member.checkCount"/>',
                   '<msg:message code="member.clientCount"/>',
                   '<msg:message code="member.orderCount"/>',
                   '<msg:message code="member.registerTime"/>',
                   '<msg:message code="info.operate"/>'
                   ],
        colModel: [
                   {name: 'name',index: 'name',width:'9%',align:'left',hidden: false,sortable: false,
                	  formatter:function(cellvalue, options, rowObject){
                	   var content="&nbsp;";
                	   if (rowObject.name!=null&&rowObject.nickName!=null){
                	   	  content= rowObject.name+"("+rowObject.nickName+")";
                	   }
					   return content;    
					 }
                   }, 
                   {name: 'teamID',index: 'teamID',align:'center',hidden: true,sortable: false}, 
                   {name: 'nickName',index:'nickName',align:'center',hidden: true,sortable: false},
                   {name: 'teamName',index:'teamName',width:'9%',align:'center',hidden: false,sortable: false},
                   {name: 'mobilePhone',index:'mobilePhone',width:'12%',align:'center',hidden: false,sortable: false,
                	   formatter:function(cellvalue, options, rowObject){
                		   return strEncrypt(cellvalue, 4); 
                	   }
                   },
                   {name: 'iDCard',index:'iDCard',width:'12%',align:'center',hidden: false,sortable: false,
                	   formatter:function(cellvalue, options, rowObject){
                		   return strEncrypt(cellvalue, 4); 
                	   }
                   },
                   {name: 'status',index: 'status',width:'5%',align:'center',hidden: false,sortable: false,
                	  formatter:function(cellvalue, options, rowObject){
                	   var content="";
                	   	<c:forEach var="item" items="${memberStatus}">
                	   	<c:set var="isDoing" value="0"/>
                	   	   <c:if test="${isDoing!='1'}">
							   if(cellvalue==${item.key}){
								   content="${item.value}";
								   <c:set var="isDoing" value="1"/> 
							   }
						   </c:if>
						</c:forEach>
						return content;    
					 }
                  }, 
                  {name: 'checkCount',index: 'checkCount',width:'5%',align:'center',hidden: false,sortable: false,
                	   formatter:function(cellvalue, options, rowObject){
					   var content="";
					   if(rowObject.checkCount>0){
						   content+="<span style='color:red;' >待审</span>";
					   }
					   else
					   {
						  content+="<span style='color:red;' >已审核</span>";
					      //content+=rowObject.checkCount;
					   }
						return content; 
					 }
                   }, 
                   {name: 'clientCount',index: 'clientCount',width:'4%',align:'center',hidden: false,sortable: false},
                   {name: 'orderCount',index: 'orderCount',width:'4%',align:'center',hidden: false,sortable: false},
                   {name: 'registerTime',index: 'registerTime',width:'12%',align:'center',hidden: false,sortable: false},
                   {name:'act',index:'act', width:'31%',align:'center',sortable:false,
                	  formatter:function(cellvalue, options, rowdata){
	                   			var content = "";
							   	var id = rowdata.id;
							   	var type=rowdata.type;
							   	var teamID = rowdata.teamID;
				    			var space = "&nbsp;&nbsp;";
				    			var content = "";
				    		    <c:if test="${!empty customer_member_checklist}">
				    		    <security:authorize ifAnyGranted="${customer_member_checklist.code}">
				    		    if (rowdata.status==4){
				    		    	//如果状态为禁用或者注销或者待审核数为0，那么审核按钮不可用
				    		    	content += "<span style='color:#CFC09F;' title='${customer_member_checklist.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_checklist.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${customer_member_checklist.name}' id='" + id + "' class='shortcut_${customer_member_checklist.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_checklist.name}";
				   		    	    content += "</a>";
				    		    }
				   		    
				    		    </security:authorize>
				    		    </c:if>
				    		   	<c:if test="${!empty customer_member_memberclient}">
				    		    <security:authorize ifAnyGranted="${customer_member_memberclient.code}">
				    		    //如果状态为禁用或者注销，那么理财师客户按钮不可用
				    		    if (rowdata.status==4){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_member_memberclient.name}'><img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_memberclient.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_member_memberclient.indexCode}'";
				        		    content += " title='${customer_member_memberclient.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_memberclient.name}";
				        		    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		   	<c:if test="${!empty customer_member_suspendtask}">
				    		    <security:authorize ifAnyGranted="${customer_member_suspendtask.code}">
				    		    //如果状态为禁用或者注销或者创建中，那么暂停按钮不可用
				    		    if (rowdata.status==1||rowdata.status==4||rowdata.status==5){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_member_suspendtask.name}'><img src='${base}/${skin}/images/icon/recover-business.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_suspendtask.name}</span>"; 
				    		    //如果状态为暂停，那么显示为恢复按钮
				    		    }else if (rowdata.status == 3||rowdata.status == 0){
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_member_suspendtask.indexCode}'";
				        		    content += " title='<msg:message code='member.recoverytask'/>' >";
				        		    content += "<img src='${base}/${skin}/images/icon/recover-business.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='member.recoverytask'/>";
				        		    content += "</a>";
				    		    }
				    		    //如果状态为启动，那么显示为暂停按钮
				    		    else if (rowdata.status == 2){
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_member_suspendtask.indexCode}'";
				        		    content += " title='${customer_member_suspendtask.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/pause-business.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_suspendtask.name}";
				        		    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_member_detail}">
				    		    
				    		    <security:authorize ifAnyGranted="${customer_member_detail.code}">
				    		    //如果状态为禁用或者注销，那么详细按钮不可用
				    		    if (rowdata.status==4){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_member_detail.name}'><img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_detail.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_member_detail.indexCode}'";
				        		    content += " title='${customer_member_detail.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_detail.name}";
				        		    content += "</a>";
				    		    }	
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_member_resetpassword}">
				    		    <security:authorize ifAnyGranted="${customer_member_resetpassword.code}">
				    		    //如果状态为禁用或者注销，那么重置密码按钮不可用
				    		    if (rowdata.status==4){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_member_resetpassword.name}'><img src='${base}/${skin}/images/icon/password.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_resetpassword.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_member_resetpassword.indexCode}'";
				        		    content += " title='${customer_member_resetpassword.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/password.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_resetpassword.name}";
				        		    content += "</a>";
				    		    }	
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_member_delete}">
				    		    <security:authorize ifAnyGranted="${customer_member_delete.code}">
				    		    //如果状态为禁用或者注销，那么删除按钮不可用
				    		    if (rowdata.orderCount>0){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_member_delete.name}'><img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_delete.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_member_delete.indexCode}'";
				        		    content += " title='${customer_member_delete.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/delete.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_delete.name}";
				        		    content += "</a>";
				    		    }	
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_member_cancel}">
				    		    <security:authorize ifAnyGranted="${customer_member_cancel.code}">
				    		    //如果状态为禁用或者注销，那么注销按钮不可用
				    		    if (rowdata.status==4){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_member_cancel.name}'><img src='${base}/${skin}/images/icon/empty.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_cancel.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' id='" + id + "'  type='" + type + "' teamID='" + teamID + "' class='shortcut_${customer_member_cancel.indexCode}'";
				        		    content += " title='${customer_member_cancel.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/empty.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_cancel.name}";
				        		    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    return content;
	                   	}
                  }],
       	mtype: "POST",
		postData:{c:"${member.c}",name:"${member.name}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,   
        page:"${member.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:<msg:message code='jqgrid.height.250'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
        loadComplete:function(){
    	},
    	caption:'<msg:message code="member.list"/>',
        toolbar: [true,"top"]
    });
    
    //导出
    <c:if test="${!empty customer_member_export}">
	<security:authorize ifAnyGranted="${customer_member_export.code}">
		var $ea = $("<a></a>").attr("href","javascript:void(0)")
			 .attr("id","${customer_member_export.indexCode}")
			 .append($("<img/>").attr("src","../common/skin/${customer_member_export.icon}")
			 .attr("width","18").attr("height","18").attr("border","0")
			 .attr("border","0").attr("align","absmiddle"))
			 .append("${customer_member_export.name}");
		$("#t_memberList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea));
		$("#${customer_member_export.indexCode}","#t_memberList").click(function(){
			var name = $("#name").val();
			
	    	window.location.href='${base}${customer_member_export.controller}?c=${customer_member_export.code}&name=' + name ;
			
		});
	 </security:authorize>
	 </c:if> 
    
    //理财师暂停任务恢复任务的js操作
    $(".shortcut_${customer_member_suspendtask.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
	   	var title=$(this).attr("title");
	   	var data = jQuery("#memberList").jqGrid("getRowData",rowid);
	   	var url = "${base}/customer/member/suspendtask.json?prompt=name&id="+rowid+"&name="+encodeURIComponent(encodeURIComponent(data.name));
	   	if (title=='<msg:message code="member.recoverytask"/>'){
	   		url = "${base}/customer/member/recoverytask.json?prompt=name&id="+rowid+"&name="+encodeURIComponent(encodeURIComponent(data.name));
	   	}
	   	///
	   	var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['50px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.changemember.confirm'/>",
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
							if(jsonData.mark=="true"){
								top.layer.close(index);
								//window.parent.Boxy.alert("<msg:message code='team.changesuccess'/>", null, {title: "<msg:message code='info.prompt'/>"});
								$("#memberList").trigger("reloadGrid"); 
							}else if(jsonData.mark=="false")
							{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='member.changeerror'/>",type : 3}	
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
    //理财师重置密码的js操作
    $(".shortcut_${customer_member_resetpassword.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
	   	var data = jQuery("#memberList").jqGrid("getRowData",rowid);
		window.location.href = "${base}${customer_member_resetpassword.controller}?c=${customer_member_resetpassword.code}&id="+ rowid;
     });
    //理财师注销的js操作
    $(".shortcut_${customer_member_cancel.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
	   	var type = $(this).attr("type");
	   	var teamID =$(this).attr("teamID");
	   	if(teamID!="null"&&teamID!=""&&type==2){
			top.$.layer({
			shade : [0.5 , '#000' , true],
	  		offset : ['50px',''],
	  	 	title : "<msg:message code='info.prompt'/>",
		    dialog : {msg:"不能直接注销组织的管理者！",type : 3}	
			});
			return false;
		}
	   	if(teamID!="null"&&teamID!=""&&type==1){
			top.$.layer({
			shade : [0.5 , '#000' , true],
	  		offset : ['50px',''],
	  	 	title : "<msg:message code='info.prompt'/>",
		    dialog : {msg:"不能直接注销组织下的理财顾问！",type : 3}	
			});
			return false;
		}
	   	var url = "${base}/customer/member/cannel.json?prompt=name&id="+rowid;
	   	///
	   	var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['50px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.cannel.confirm'/>",
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
							if(jsonData.mark=="true"){
								top.layer.close(index);
								//window.parent.Boxy.alert("<msg:message code='team.changesuccess'/>", null, {title: "<msg:message code='info.prompt'/>"});
								$("#memberList").trigger("reloadGrid"); 
							}else if(jsonData.mark=="false")
							{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='member.cannelerror'/>",type : 3}	
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
     $(".shortcut_${customer_member_delete.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var data = jQuery("#memberList").jqGrid("getRowData",rowid);
			var url = "${base}${customer_member_delete.controller}.json?c=${customer_member_delete.code}&prompt=name&id="+ rowid;
			//
			var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['50px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.delete.confirm'/>",
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
						if(textStatus == "success"){
							if(jsonData.status == "success"){
								top.layer.close(index);
								$("#memberList").trigger("reloadGrid");
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
     <c:if test="${!empty customer_member_delete}">
		<security:authorize ifAnyGranted="${customer_member_delete.code}">
			var $content = $("<a></a>").attr("href","javascript:void(0)")
						   .attr("id","${customer_member_delete.indexCode}")
						   .append($("<img/>").attr("src","${base}/${skin}/images/icon/delete.png")
						   .attr("width","18").attr("height","18").attr("border",0)
						   .attr("border","0").attr("align","absmiddle"))
						   .append("${customer_member_delete.name}");
			$("#t_memberList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
			$("#${customer_member_delete.indexCode}","#t_memberList").click(function(){
				var selrow = jQuery("#memberList").jqGrid("getGridParam","selarrrow");
				if (selrow != null && selrow != ""){
					var url = "${base}${customer_member_delete.controller}.json?c=${customer_member_delete.code}&ids=" + selrow;
					var index = top.$.layer({
			  	    shade : [0.5 , '#000' , true],
			  	    area : ['auto','auto'],
			  	  	offset : ['50px',''],
			  	    dialog : {
			  	        msg:"<msg:message code='info.prompt.delete.confirm'/>",
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
								if(textStatus == "success"){
									if(jsonData.status == "success"){
										top.layer.close(index);
										$("#memberList").trigger("reloadGrid");
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
				} else{
					top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"<msg:message code='info.prompt.delete'/>",type : 3}	
					});
				}
			});
		</security:authorize>
	 </c:if>
     $(".shortcut_${customer_member_checklist.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_member_checklist.controller}?c=${customer_member_checklist.code}&id="+ rowid;
   	 });
      $(".shortcut_${customer_member_cancelcheck.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_member_cancelcheck.controller}?c=${customer_member_cancelcheck.code}&id="+ rowid;
   	 });
     $(".shortcut_${customer_member_memberclient.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_member_memberclient.controller}?c=${customer_member_memberclient.code}&id="+ rowid;
   	 });
     $(".shortcut_${customer_member_detail.indexCode}").live("click",function(){
    	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_member_detail.controller}?c=${customer_member_detail.code}&id="+ rowid;
   	 });
     
	jQuery("#memberList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
});


function strEncrypt(s, len){
	if(typeof(s) == "undefined" || s == null){
		return "" ;
	}else if(s.length > len){
		var str = "" ;
		for(var i = 0; i < s.length - len; i ++){
			str += "*" ;
		}
		return str + s.substr(s.length - len) ;
	}else if(s.length == 0){
		return "" ;
	}else {
		return s ;
	}
	
}
</script>
</head>

<body class="skinMain">
<form:form method="post" action="member" commandName="member">
<input type="hidden" name="c" value="${member.c}" />
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td align="center">
			<table cellpadding="0" cellspacing="1" border="0" width="100%"
				class="search">
				<tr>
					<td align="right" width="10%" class="search_info">
						<msg:message code='member.name'/>
					</td>
					<td align="left" width="30%" class="search_lable">
						<form:input path="name" htmlEscape="true"/>
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
			<table id="memberList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</form:form>
</body>
</html>