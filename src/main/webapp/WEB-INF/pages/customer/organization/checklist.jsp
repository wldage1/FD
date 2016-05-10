<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(document).ready(function(){
    jQuery("#checkList").jqGrid({
        treeGridModel: 'adjacency',
        url: '${base}/customer/organization/checklist/grid.json',
        datatype: 'json',
        colNames: [
                  '<msg:message code="memberOrganization.name"/>',
                   '<msg:message code="memberOrganization.checkType"/>',
                   '<msg:message code="memberOrganization.status"/>',
                   '<msg:message code="info.operate"/>'
                   ],
        colModel: [
                   {name: 'name',index: 'name',width:'10%',align:'center',hidden: false,sortable: false}, 
                   {name: 'type',index: 'type',width:'10%',align:'center',hidden: false,sortable: false,
                	formatter:function(cellvalue, options, rowObject){
						var content="";
                	   	<c:forEach var="item" items="${teamOrOrgApplicationType}">
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
                	   	<c:forEach var="item" items="${teamOrOrgApplicationStatus}">
							   if(cellvalue==${item.key}){
								   content="${item.value}";
							   }
						</c:forEach>
						return content;
					 }
                  }, 
                   {name:'act',index:'act', width:'50%',align:'center',sortable:false,
                	  formatter:function(cellvalue, options, rowdata){
	                   			var content = "";
							   	var id = rowdata.id;
				    			var space = "&nbsp;&nbsp;";
				    			var content = "";
				    			if (rowdata.type==1){
				    		    <c:if test="${!empty customer_organization_checklist_createcheck}">
				    		    <security:authorize ifAnyGranted="${customer_organization_checklist_createcheck.code}">
				    		    if (rowdata.status!=0){
				    		    	//如果状态为禁用或者注销或者待审核数为0，那么审核按钮不可用
				    		    		content += "<span style='color:#CFC09F;' title='${customer_organization_checklist_createcheck.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_createcheck.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${customer_organization_checklist_createcheck.name}' id='" + id + "' class='shortcut_${customer_organization_checklist_createcheck.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_createcheck.name}";
				   		    	    content += "</a>";
				    		    }
				    		    <c:if test="${!empty customer_organization_checklist_createdetail}">
				    		    <security:authorize ifAnyGranted="${customer_organization_checklist_createdetail.code}">
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_organization_checklist_createdetail.indexCode}'";
				        		    content += " title='${customer_organization_checklist_createdetail.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_createdetail.name}";
				        		    content += "</a>";	
				    		    </security:authorize>
				    		    </c:if>
				    		    </security:authorize>
				    		    </c:if>
				    			}
				    			if (rowdata.type==2){
				    		    <c:if test="${!empty customer_organization_checklist_changecheck}">
				    		    <security:authorize ifAnyGranted="${customer_organization_checklist_changecheck.code}">
				    		    if (rowdata.status!=0){
				    		    	//如果状态为禁用或者注销或者待审核数为0，那么审核按钮不可用
				    		    		content += "<span style='color:#CFC09F;' title='${customer_organization_checklist_changecheck.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_changecheck.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${customer_organization_checklist_changecheck.name}' id='" + id + "' class='shortcut_${customer_organization_checklist_changecheck.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_changecheck.name}";
				   		    	    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_organization_checklist_changedetail}">
				    		    <security:authorize ifAnyGranted="${customer_organization_checklist_changedetail.code}">
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_organization_checklist_changedetail.indexCode}'";
				        		    content += " title='${customer_organization_checklist_changedetail.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_changedetail.name}";
				        		    content += "</a>";	
				    		    </security:authorize>
				    		    </c:if>
				    			}
				    			if (rowdata.type==3){
				    		    <c:if test="${!empty customer_organization_checklist_annualcheck}">
				    		    <security:authorize ifAnyGranted="${customer_organization_checklist_annualcheck.code}">
				    		    if (rowdata.status!=0){
				    		    	//如果状态为禁用或者注销或者待审核数为0，那么审核按钮不可用
				    		    		content += "<span style='color:#CFC09F;' title='${customer_organization_checklist_annualcheck.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_annualcheck.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${customer_organization_checklist_annualcheck.name}' id='" + id + "' class='shortcut_${customer_organization_checklist_annualcheck.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_annualcheck.name}";
				   		    	    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_organization_checklist_annualdetail}">
				    		    <security:authorize ifAnyGranted="${customer_organization_checklist_annualdetail.code}">
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_organization_checklist_annualdetail.indexCode}'";
				        		    content += " title='${customer_organization_checklist_annualdetail.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_annualdetail.name}";
				        		    content += "</a>";	
				    		    </security:authorize>
				    		    </c:if>
				    			}
				    		    if (rowdata.type==4){
				    		    <c:if test="${!empty customer_organization_checklist_cancelcheck}">
				    		    <security:authorize ifAnyGranted="${customer_organization_checklist_cancelcheck.code}">
				    		    if (rowdata.status!=0){
				    		    	//如果状态为禁用或者注销或者待审核数为0，那么审核按钮不可用
				    		    		content += "<span style='color:#CFC09F;' title='${customer_organization_checklist_cancelcheck.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_cancelcheck.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${customer_organization_checklist_cancelcheck.name}' id='" + id + "' class='shortcut_${customer_organization_checklist_cancelcheck.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_cancelcheck.name}";
				   		    	    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty customer_organization_checklist_canceldetail}">
				    		    <security:authorize ifAnyGranted="${customer_organization_checklist_canceldetail.code}">
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${customer_organization_checklist_canceldetail.indexCode}'";
				        		    content += " title='${customer_organization_checklist_canceldetail.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_organization_checklist_canceldetail.name}";
				        		    content += "</a>";	
				    		    </security:authorize>
				    		    </c:if>
				    		    }
				    		    
				    		    return content;
	                   	}
                  }],
       	mtype: "POST",
		postData:{c:"${memberOrganization.c}",id:"${memberOrganization.id}"},
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
    	caption:'<msg:message code="memberOrganization.checklist"/>',
        toolbar: [false,"top"]
    });
     $(".shortcut_${customer_organization_checklist_createcheck.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_organization_checklist_createcheck.controller}?c=${customer_organization_checklist_createcheck.code}&id="+ rowid;
   	 });
      $(".shortcut_${customer_organization_checklist_changecheck.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_organization_checklist_changecheck.controller}?c=${customer_organization_checklist_changecheck.code}&id="+ rowid;
   	 }); 
     $(".shortcut_${customer_organization_checklist_annualcheck.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_organization_checklist_annualcheck.controller}?c=${customer_organization_checklist_annualcheck.code}&id="+ rowid;
   	 }); 
     $(".shortcut_${customer_organization_checklist_cancelcheck.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_organization_checklist_cancelcheck.controller}?c=${customer_organization_checklist_cancelcheck.code}&id="+ rowid;
   	 }); 
     //////////////////
     $(".shortcut_${customer_organization_checklist_createdetail.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_organization_checklist_createdetail.controller}?c=${customer_organization_checklist_createdetail.code}&id="+ rowid;
   	 }); 
     $(".shortcut_${customer_organization_checklist_changedetail.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_organization_checklist_changedetail.controller}?c=${customer_organization_checklist_changedetail.code}&id="+ rowid;
   	 }); 
     $(".shortcut_${customer_organization_checklist_annualdetail.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_organization_checklist_annualdetail.controller}?c=${customer_organization_checklist_annualdetail.code}&id="+ rowid;
   	 }); 
     $(".shortcut_${customer_organization_checklist_canceldetail.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_organization_checklist_canceldetail.controller}?c=${customer_organization_checklist_canceldetail.code}&id="+ rowid;
   	 }); 
	jQuery("#checkList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
});
</script>
</head>

<body class="skinMain">
<form:form method="post" action="team" commandName="memberOrganization">
<input type="hidden" name="c" value="${memberOrganization.c}" />
<input type="hidden" name="turnCode" value="${customer_organization.code}"/>
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