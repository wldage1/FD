<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
//弹出层标记
var k;
$(window).resize(function(){ 　　
	$("#organizationList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
    jQuery("#organizationList").jqGrid({
        treeGridModel: 'adjacency',
        url: 'organization/grid.json',
        datatype: 'json',
        colNames: [
                   "<msg:message code='memberOrganization.name'/>",
                   "<msg:message code='memberOrganization.shortName'/>", 
                   "<msg:message code='memberOrganization.memberName.temp'/>",
                   "<msg:message code='memberOrganization.memberPhone'/>",
                   "<msg:message code='memberOrganization.memberEmail'/>",
                   "<msg:message code='memberOrganization.memberCount'/>",
                   "<msg:message code='memberOrganization.channelManager'/>",
                   "<msg:message code='memberOrganization.status'/>",
                   "<msg:message code='memberOrganization.checkStatus'/>",
                   "<msg:message code='info.operate'/>"
                   ],
        colModel: [
                   {name: 'name',index: 'name',width:'10%',align:'center',hidden: false,sortable: false}, 
                   {name: 'shortName',index: 'shortName',width:'8%',align:'center',hidden: false,sortable: false}, 
                   {name: 'memberName',index:'memberName',width:'8%',align:'center',hidden: false,sortable: false},
                   {name: 'memberPhone',index:'memberPhone',width:'8%',align:'center',hidden: false,sortable: false},
                   {name: 'memberEmail',index: 'memberEmail',width:'8%',align:'center',hidden: false,sortable: false}, 
                   {name: 'memberCount',index: 'memberCount',width:'5%',align:'center',hidden: false,sortable: false}, 
                   {name: 'channelManager',index: 'channelManager',width:'8%',align:'center',hidden: false,sortable: false}, 
                   {name: 'status',index: 'status',width:'5%',align:'center',hidden: false,sortable: false,
                	  formatter:function(cellvalue, options, rowObject){
						var content="";
                	   	<c:forEach var="item" items="${teamOrOrgStatus}">
							   if(cellvalue==${item.key}){
								   content="${item.value}";
							   }
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
                   {name:'act',index:'act', width:'35%',align:'center',sortable:false,
                	  formatter:function(cellvalue, options, rowdata){
	                   			var content = "";
							   	var id = rowdata.id;
				    			var space = "&nbsp;&nbsp;";
				    			var content = "";
				    		    <c:if test="${!empty customer_organization_checklist}">
				    		    <security:authorize ifAnyGranted="${customer_organization_checklist.code}">
				    		    //状态为注销或者待审核数为0，那么审核按钮不可编辑
				    		    if (rowdata.status==3){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_organization_checklist.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${customer_organization_checklist.name}' id='" + id + "' class='shortcut_${customer_organization_checklist.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist.name}";
				   		    		content += "</a>";
				    		    }
				    		   
				    		    </security:authorize>
				    		    </c:if>  
				    		   	<c:if test="${!empty customer_organization_organizationmember}">
				    		    <security:authorize ifAnyGranted="${customer_organization_organizationmember.code}">
				    		    //状态为注销 那么机构人员按钮不可编辑
				    		    if (rowdata.status==3){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_organization_organizationmember.name}'><img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_organizationmember.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_organization_organizationmember.indexCode}'";
				        		    content += " title='${customer_organization_organizationmember.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_organizationmember.name}";
				        		    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		   	<c:if test="${!empty customer_organization_channelmanager}">
				    		    <security:authorize ifAnyGranted="${customer_organization_channelmanager.code}">
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_organization_channelmanager.indexCode}'";
				        		    content += " title='${customer_organization_channelmanager.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_channelmanager.name}";
				        		    content += "</a>";
				    		    </security:authorize>
				    		    </c:if>
				    		   	<c:if test="${!empty customer_organization_suspendtask}">
				    		    <security:authorize ifAnyGranted="${customer_organization_suspendtask.code}">
				    		    //状态为注消，那么暂停任务按钮不可编辑
				    		    if (rowdata.status == 3||rowdata.status == 0||rowdata.status == 4){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_organization_suspendtask.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_suspendtask.name}</span>"; 
				    		    //状态为暂停的话，那么按钮名称为恢复业务	
				    		    }else if (rowdata.status == 2){
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_organization_suspendtask.indexCode}'";
				        		    content += " title='<msg:message code='memberOrganization.recoverytask'/>' >";
				        		    content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='memberOrganization.recoverytask'/>";
				        		    content += "</a>";
				    		    }
				    		     //状态为启用的话，那么按钮名称为暂停业务
				    		    else if (rowdata.status == 1){
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_organization_suspendtask.indexCode}'";
				        		    content += " title='${customer_organization_suspendtask.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_suspendtask.name}";
				        		    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_organization_update}">
				    		    <security:authorize ifAnyGranted="${customer_organization_update.code}">
				    		    //状态为注消或者创建中，那么修改按钮不可编辑
				    		    if (rowdata.status == 3||rowdata.status == 0){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_organization_update.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_update.name}</span>"; 
				    		    }else {
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_organization_update.indexCode}'";
				        		    content += " title='${customer_organization_update.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_update.name}";
				        		    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_organization_cancel}">
				    		    <security:authorize ifAnyGranted="${customer_organization_cancel.code}">
				    		    //状态为创建 那么注销按钮不可编辑
				    		    if (rowdata.status==0||rowdata.status==3){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_organization_cancel.name}'><img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_cancel.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_organization_cancel.indexCode}'";
				        		    content += " title='${customer_organization_cancel.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_cancel.name}";
				        		    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if> 
				    		    return content;
				    		     
	                   	}
                  }],
       	mtype: "POST",
		postData:{c:"${memberOrganization.c}",name:"${memberOrganization.name}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,   
        page:"${memberOrganization.page}",
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
    	caption:'<msg:message code="memberOrganization.list"/>',
        toolbar: [true,"top"]
    });
    <c:if test="${!empty customer_organization_exportorg}">
    <security:authorize ifAnyGranted="${customer_organization_exportorg.code}">
    //导出展示
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
	.attr("id","${customer_organization_exportorg.indexCode}")
	.attr("border","0").attr("class","${customer_organization_exportorg.indexCode}")
	.append($("<img/>").attr("src","${base}/${skin}/${customer_organization_exportorg.icon}")
  	.attr("width","18").attr("height","18").attr("border","0")
  	.attr("border","0").attr("align","absmiddle"))
  	.append("${customer_organization_exportorg.name}");
	$("#t_organizationList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea)); 
	
	//导出事件
	$(".${customer_organization_exportorg.indexCode}").live("click",function(){
		window.location.href='${base}/customer/organization/export?name='+$("#name").val()+'';
	});
	 </security:authorize>
    </c:if> 
    
    //理财师机构设置渠道经理的js操作
    $(".shortcut_${customer_organization_channelmanager.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
	   	var data = jQuery("#organizationList").jqGrid("getRowData",rowid);
	   	var channelManager = data.channelManager;
	   	var shortName = data.shortName;
	   	k = top.$.layer({
			type : 1,
			title : "<msg:message code='channelManager.set'/>",
			border : [0],
			area : ['350px','200px'],
			page : {
				html :  "<table height='165px' width='350px' bgcolor='#EAFBFF'>"+
							"<tr>"+
								"<td align='right' width='40%'>"+
									"<msg:message code='memberOrganization.shortName'/><msg:message code='system.common.sign.colon'/>"+
								"</td>"+
								"<td>"+
									shortName+
								"</td>"+
							"</tr>"+
							"<tr>"+
								"<td align='right'>"+
									"<msg:message code='memberOrganization.channelManager'/><msg:message code='system.common.sign.colon'/>"+
								"</td>"+
								"<td>"+
									"<input type='text' id='channelManager' value='" + channelManager + "'>" +
								"</td>"+
							"</tr>"+
							"<tr bgcolor='#CCDFED' height='40px'>"+
								"<td colspan='2' align='center'>"+
									"<input type='button' class='btn2' value='<msg:message code='button.confirm'/>' onclick='channel("+rowid+")'>"+
									"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
									"<input type='button' class='btn2' value='<msg:message code='button.cancel'/>' onclick='closeLayer()'>"+
								"</td>"+
							"</tr>"+
						"</table>"
			}
		});
     });
	//设置渠道经理确认提交
	window.parent.channel = function(rowid){
		var channelManager = $("#channelManager",window.parent.document).val();
		if(channelManager==''){
			top.layer.msg("<msg:message code='channelManager.notnull'/>",1);
			return false;
		}
		$.ajax({
			url: "${base}${customer_organization_channelmanager.controller}.json?c=${customer_organization_channelmanager.code}",
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: {id:rowid,channelManager:channelManager},
			success:function(jsonData){
				top.layer.close(k);
				if(jsonData.status=='success'){
					$("#organizationList").trigger("reloadGrid");
					top.$.layer({
						area : ['auto','auto'],
						dialog : {msg: "<msg:message code='channelManager.set.success'/>" ,type : 9}
					});
				}else{
					top.$.layer({
						area : ['auto','auto'],
						dialog : {msg: "<msg:message code='channelManager.set.fail'/>" ,type : 8}	
					});
				}
			}
		});
	}
	//关闭弹出层
	window.parent.closeLayer = function(){
		top.layer.close(k);
	}
    //理财师机构暂停任务,恢复任务的js操作
    $(".shortcut_${customer_organization_suspendtask.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
	   	var title=$(this).attr("title");
	   	var data = jQuery("#organizationList").jqGrid("getRowData",rowid);
	   	var url = "${base}/customer/organization/suspendtask.json?prompt=name&id="+rowid+"&name="+encodeURIComponent(encodeURIComponent(data.name));
	   	if (title=='<msg:message code="memberOrganization.recoverytask"/>'){
	   		url = "${base}/customer/organization/recoverytask.json?prompt=name&id="+rowid+"&name="+encodeURIComponent(encodeURIComponent(data.name));
	   	}
	   	//////////////////////
	   	var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['50px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.changememberOrganization.confirm'/>",
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
								//window.parent.Boxy.alert("<msg:message code='memberOrganization.changesuccess'/>", null, {title: "<msg:message code='info.prompt'/>"});
								$("#organizationList").trigger("reloadGrid"); 
							}else if(jsonData.mark=="false")
							{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='memberOrganization.changeerror'/>",type : 3}	
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
    //机构注销的js操作
    $(".shortcut_${customer_organization_cancel.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
	   	var url = "${base}/customer/organization/cancel.json?prompt=name&id="+rowid;
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
								//window.parent.Boxy.alert("<msg:message code='memberOrganization.changesuccess'/>", null, {title: "<msg:message code='info.prompt'/>"});
								$("#organizationList").trigger("reloadGrid"); 
							}else if(jsonData.mark=="false")
							{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='memberOrganization.cannelerror'/>",type : 3}	
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
     //机构审核列表的跳转
     $(".shortcut_${customer_organization_checklist.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_organization_checklist.controller}?c=${customer_organization_checklist.code}&id="+ rowid;
   	 });
     //机构信息修改的跳转
     $(".shortcut_${customer_organization_update.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_organization_update.controller}?c=${customer_organization_update.code}&id="+ rowid;
   	 });
     //机构人员的跳转
     $(".shortcut_${customer_organization_organizationmember.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_organization_organizationmember.controller}?c=${customer_organization_organizationmember.code}&id="+ rowid;
   	 });
	jQuery("#organizationList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
});

</script>
</head>

<body class="skinMain">
<form:form method="post" action="organization" commandName="memberOrganization">
<input type="hidden" name="c" value="${memberOrganization.c}" />
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td align="center">
			<table cellpadding="0" cellspacing="1" border="0" width="100%"
				class="search">
				<tr>
					<td align="right" width="10%" class="search_info">
						<msg:message code='memberOrganization.name'/>
					</td>
					<td align="left" width="30%" class="search_lable">
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
			<table id="organizationList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</form:form>
</body>
</html>