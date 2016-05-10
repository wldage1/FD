<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(document).ready(function(){
    jQuery("#memberList").jqGrid({
        treeGridModel: 'adjacency',
        url: '${base}/customer/teammember/grid.json',
        datatype: 'json',
        colNames: [
                   '<msg:message code="member.name"/>',
                   '<msg:message code="member.teamID"/>', 
                   '<msg:message code="member.nickName"/>',
                   '<msg:message code="member.teamName"/>',
                   '<msg:message code="member.type"/>',
                   '<msg:message code="member.status"/>',
                   '<msg:message code="info.operate"/>'
                   ],
        colModel: [
                   {name: 'name',index: 'name',width:'10%',align:'center',hidden: false,sortable: false,
                	  formatter:function(cellvalue, options, rowObject){
                	   var content="&nbsp;";
                	   if (rowObject.name!=null&&rowObject.nickName!=null){
                	   	  content= rowObject.name+"("+rowObject.nickName+")";
                	   }
					   return content;    
					 }
                   }, 
                   {name: 'teamID',index: 'teamID',width:'10%',align:'center',hidden: true,sortable: false}, 
                   {name: 'nickName',index:'nickName',width:'10%',align:'center',hidden: true,sortable: false},
                   {name: 'teamName',index:'teamName',width:'10%',align:'center',hidden: false,sortable: false},
                   {name: 'type',index: 'type',width:'10%',align:'center',hidden: false,sortable: false,
                	formatter:function(cellvalue, options, rowObject){
						var content="";
                	   	<c:forEach var="item" items="${memberType}">
							   if(cellvalue==${item.key}){
								   content="${item.value}";
							   }
						</c:forEach>
						return content;
					 }
                   }, 
                   {name: 'status',index: 'status',width:'10%',align:'center',hidden: false,sortable: false,
                	formatter:function(cellvalue, options, rowObject){
						var content="";
                	   	<c:forEach var="item" items="${memberStatus}">
							   if(cellvalue==${item.key}){
								   content="${item.value}";
							   }
						</c:forEach>
						return content;
					 }
                   }, 
                   {name:'act',index:'act', width:'30%',align:'center',sortable:false,
                	  formatter:function(cellvalue, options, rowdata){
	                   			var content = "";
							   	var id = rowdata.id;
				    			var space = "&nbsp;&nbsp;";
				    			var content = "";
				    		    <c:if test="${!empty customer_team_teammember_memberdetail}">
				    		    <security:authorize ifAnyGranted="${customer_team_teammember_memberdetail.code}">
				   		    	content += "<a href='javascript:void(0);' title='${customer_team_teammember_memberdetail.name}' id='" + id + "' class='shortcut_${customer_team_teammember_memberdetail.indexCode}'>";
				   		    	content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_team_teammember_memberdetail.name}";
				   		    	content += "</a>";
				    		    </security:authorize>
				    		    </c:if>  
				    		    return content;
	                   	}
                  }],
		postData:{c:"${team.c}",id:"${team.id}"},
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
    	caption:'<msg:message code="member.list"/>',
        toolbar: [false,"top"]
    });
    //理财师详细的跳转
     $(".shortcut_${customer_team_teammember_memberdetail.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
		window.location.href = "${base}${customer_team_teammember_memberdetail.controller}?c=${customer_team_teammember_memberdetail.code}&id="+ rowid;
   	 });
	jQuery("#memberList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
});
</script>
</head>

<body class="skinMain">
<input type="hidden" name="c" value="${team.c}" />
<input type="hidden" name="turnCode" value="${customer_team.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td>
			<table id="memberList"><tr><td>&nbsp;</td></tr></table>
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
</body>
</html>