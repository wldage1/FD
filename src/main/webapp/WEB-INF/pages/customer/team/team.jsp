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
	$("#teamList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
    jQuery("#teamList").jqGrid({
        treeGridModel: 'adjacency',
        url: 'team/grid.json',
        datatype: 'json',
        colNames: [
                   "<msg:message code='team.name'/>",
                   "<msg:message code='team.shortName'/>",
                   "<msg:message code='team.memberCount'/>",
                   "<msg:message code='team.status'/>",
                   "<msg:message code='team.checkStatus'/>",
                   "<msg:message code='info.operate'/>"
                   ],
        colModel: [
                   {name: 'name',index: 'name',width:'10%',align:'center',hidden: false,sortable: false}, 
                   {name: 'shortName',index: 'shortName',width:'10%',align:'center',hidden: true,sortable: false},  
                   {name: 'memberCount',index: 'memberCount',width:'10%',align:'center',hidden: false,sortable: false}, 
                   {name: 'status',index: 'status',width:'10%',align:'center',hidden: false,sortable: false,
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
                  {name: 'checkCount',index: 'checkCount',width:'10%',align:'center',hidden: false,sortable: false,
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
                   {name:'act',index:'act', width:'30%',align:'center',sortable:false,
                	  formatter:function(cellvalue, options, rowdata){
	                   			var content = "";
							   	var id = rowdata.id;
				    			var space = "&nbsp;&nbsp;";
				    			var content = "";
				    		    <c:if test="${!empty customer_team_checklist}">
				    		    <security:authorize ifAnyGranted="${customer_team_checklist.code}">
				    		    //状态为注销或者待审核数为0，那么审核按钮不可编辑
				    		    if (rowdata.status==3){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_team_checklist.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_team_checklist.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${customer_team_checklist.name}' id='" + id + "' class='shortcut_${customer_team_checklist.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_team_checklist.name}";
				   		    		content += "</a>";
				    		    }
				    		   
				    		    </security:authorize>
				    		    </c:if>  
				    		   	<c:if test="${!empty customer_team_teammember}">
				    		    <security:authorize ifAnyGranted="${customer_team_teammember.code}">
				    		    //状态为注销 那么机构人员按钮不可编辑
				    		    if (rowdata.status==3){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_team_teammember.name}'><img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_team_teammember.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_team_teammember.indexCode}'";
				        		    content += " title='${customer_team_teammember.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_team_teammember.name}";
				        		    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		   	<c:if test="${!empty customer_team_suspendtask}">
				    		    <security:authorize ifAnyGranted="${customer_team_suspendtask.code}">
				    		    //状态为注消，那么暂停任务按钮不可编辑
				    		    if (rowdata.status == 3||rowdata.status == 0||rowdata.status == 4){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_team_suspendtask.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_team_suspendtask.name}</span>"; 
				    		    //状态为暂停的话，那么按钮名称为恢复业务	
				    		    }else if (rowdata.status == 2){
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_team_suspendtask.indexCode}'";
				        		    content += " title='<msg:message code='team.recoverytask'/>' >";
				        		    content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='team.recoverytask'/>";
				        		    content += "</a>";
				    		    }
				    		     //状态为启用的话，那么按钮名称为暂停业务
				    		    else if (rowdata.status == 1){
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_team_suspendtask.indexCode}'";
				        		    content += " title='${customer_team_suspendtask.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_team_suspendtask.name}";
				        		    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_team_cancel}">
				    		    <security:authorize ifAnyGranted="${customer_team_cancel.code}">
				    		    //状态为创建 那么注销按钮不可编辑
				    		    if (rowdata.status==0||rowdata.status==3){
				    		    	content += "<span style='color:#CFC09F;' title='${customer_team_cancel.name}'><img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_team_cancel.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_team_cancel.indexCode}'";
				        		    content += " title='${customer_team_cancel.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_team_cancel.name}";
				        		    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if> 
				    		    return content;
				    		     
	                   	}
                  }],
       	mtype: "POST",
		postData:{c:"${team.c}",name:"${team.name}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,   
        page:"${team.page}",
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
    	caption:'<msg:message code="team.list"/>',
        toolbar: [false,"top"]
    });
    //理财师机构暂停任务,恢复任务的js操作
    $(".shortcut_${customer_team_suspendtask.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
	   	var title=$(this).attr("title");
	   	var data = jQuery("#teamList").jqGrid("getRowData",rowid);
	   	var url = "${base}/customer/team/suspendtask.json?prompt=name&id="+rowid+"&name="+encodeURIComponent(encodeURIComponent(data.name));
	   	if (title=='<msg:message code="team.recoverytask"/>'){
	   		url = "${base}/customer/team/recoverytask.json?prompt=name&id="+rowid+"&name="+encodeURIComponent(encodeURIComponent(data.name));
	   	}
	   	//////////////////////
	   	var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['50px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.changeteam.confirm'/>",
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
								$("#teamList").trigger("reloadGrid"); 
							}else if(jsonData.mark=="false")
							{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='team.changeerror'/>",type : 3}	
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
    $(".shortcut_${customer_team_cancel.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
	   	var url = "${base}/customer/team/cancel.json?prompt=name&id="+rowid;
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
								$("#teamList").trigger("reloadGrid"); 
							}else if(jsonData.mark=="false")
							{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='team.cannelerror'/>",type : 3}	
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
     $(".shortcut_${customer_team_checklist.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_team_checklist.controller}?c=${customer_team_checklist.code}&id="+ rowid;
   	 });
     //机构人员的跳转
     $(".shortcut_${customer_team_teammember.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_team_teammember.controller}?c=${customer_team_teammember.code}&id="+ rowid;
   	 });
	jQuery("#teamList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
});

</script>
</head>

<body class="skinMain">
<form:form method="post" action="team" commandName="team">
<input type="hidden" name="c" value="${team.c}" />
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td align="center">
			<table cellpadding="0" cellspacing="1" border="0" width="100%"
				class="search">
				<tr>
					<td align="right" width="10%" class="search_info">
						<msg:message code='team.name'/>
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
			<table id="teamList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</form:form>
</body>
</html>