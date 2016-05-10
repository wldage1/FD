<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${base}/common/js/layer/layer.js"></script>
<title></title>
<script type="text/javascript">
$(document).ready(function(){
    jQuery("#checkList").jqGrid({
        treeGridModel: 'adjacency',
        url: '${base}/customer/member/checklist/grid.json',
        datatype: 'json',
        colNames: [
                  '<msg:message code="member.name"/>',
                   '<msg:message code="member.type"/>',
                   '<msg:message code="member.status"/>',
                   '<msg:message code="info.operate"/>'
                   ],
        colModel: [
                   {name: 'name',index: 'name',width:'10%',align:'center',hidden: false,sortable: false,
                	  formatter:function(cellvalue, options, rowObject){
                	   var content="";
                	   content= rowObject.name+"("+rowObject.nickName+")";
					   return content;    
					 }
                   }, 
                   {name: 'type',index: 'type',width:'10%',align:'center',hidden: false,sortable: false,
                	formatter:function(cellvalue, options, rowObject){
						var content="";
                	   	<c:forEach var="item" items="${memberChangeApplicationType}">
							   if(cellvalue==${item.key}){
								   content="${item.value}";
							   }
						</c:forEach>
						return content; 
					 }
                   }, 
                   {name: 'status',index:'status',width:'10%',align:'center',hidden: false,sortable: false,
                	  formatter:function(cellvalue, options, rowObject){
						var content="";
                	   	<c:forEach var="item" items="${memberChangeApplicationStatus}">
							   if(cellvalue==${item.key}){
								   content="${item.value}";
							   }
						</c:forEach>
						return content; 
					 }
                  }, 
                   {name:'act',index:'act', width:'20%',align:'left',sortable:false,
                	  formatter:function(cellvalue, options, rowdata){
	                   			var content = "";
							   	var id = rowdata.id;
				    			var space = "&nbsp;&nbsp;";
				    			var content = "";
				    			if (rowdata.type==1){
				    		    <c:if test="${!empty customer_member_checklist_attestationcheck}">
				    		    <security:authorize ifAnyGranted="${customer_member_checklist_attestationcheck.code}">
				    		    if (rowdata.status!=0){
				    		    	//如果状态为禁用或者注销或者待审核数为0，那么审核按钮不可用
				    		    		content += "<span style='color:#CFC09F;' title='${customer_member_checklist_attestationcheck.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_checklist_attestationcheck.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${customer_member_checklist_attestationcheck.name}' id='" + id + "' class='shortcut_${customer_member_checklist_attestationcheck.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_checklist_attestationcheck.name}";
				   		    	    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_member_checklist_attestationdetail}">
				    		    <security:authorize ifAnyGranted="${customer_member_checklist_attestationdetail.code}">
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_member_checklist_attestationdetail.indexCode}'";
				        		    content += " title='${customer_member_checklist_attestationdetail.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_checklist_attestationdetail.name}";
				        		    content += "</a>";	
				    		    </security:authorize>
				    		    </c:if>
				    			}
				    		    if (rowdata.type==2){
				    		    <c:if test="${!empty customer_member_checklist_cancelcheck}">
				    		    <security:authorize ifAnyGranted="${customer_member_checklist_cancelcheck.code}">
				    		    if (rowdata.status!=0){
				    		    	//如果状态为禁用或者注销或者待审核数为0，那么审核按钮不可用
				    		    		content += "<span style='color:#CFC09F;' title='${customer_member_checklist_cancelcheck.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_checklist_cancelcheck.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${customer_member_checklist_cancelcheck.name}' id='" + id + "' class='shortcut_${customer_member_checklist_cancelcheck.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_checklist_cancelcheck.name}";
				   		    	    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_member_checklist_canceldetail}">
				    		    <security:authorize ifAnyGranted="${customer_member_checklist_canceldetail.code}">
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_member_checklist_canceldetail.indexCode}'";
				        		    content += " title='${customer_member_checklist_canceldetail.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_member_checklist_canceldetail.name}";
				        		    content += "</a>";	
				    		    </security:authorize>
				    		    </c:if>
				    		    }
				    		    
				    		    return content;
	                   	}
                  }],
       	mtype: "POST",
		postData:{c:"${member.c}",id:"${member.id}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,   
        page:"${member.page}",
        rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:<msg:message code='jqgrid.height.250'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	caption:'<msg:message code="member.checklist"/>',
        toolbar: [false,"top"]
    });
     $(".shortcut_${customer_member_checklist_attestationcheck.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_member_checklist_attestationcheck.controller}?c=${customer_member_checklist_attestationcheck.code}&id="+ rowid;
   	 });
     $(".shortcut_${customer_member_checklist_cancelcheck.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_member_checklist_cancelcheck.controller}?c=${customer_member_checklist_cancelcheck.code}&id="+ rowid;
   	 }); 
     $(".shortcut_${customer_member_checklist_attestationdetail.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_member_checklist_attestationdetail.controller}?c=${customer_member_checklist_attestationdetail.code}&id="+ rowid;
   	 }); 
     $(".shortcut_${customer_member_checklist_canceldetail.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_member_checklist_canceldetail.controller}?c=${customer_member_checklist_canceldetail.code}&id="+ rowid;
   	 }); 
	jQuery("#checkList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
});
</script>
</head>

<body class="skinMain">
<form:form method="post" action="member" commandName="member">
<input type="hidden" name="c" value="${member.c}" />
<input type="hidden" name="turnCode" value="${customer_member.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td>
			<table id="checkList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
	<div class="toolbar">
				<div class="left"></div>
				<div class="center">
				<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
				</div>
				<div class="right"></div>
	</div>
	<tr>
		<td></td>
	</tr>
</table>
</form:form>
</body>
</html>