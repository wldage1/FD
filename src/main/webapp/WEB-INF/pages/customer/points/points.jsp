<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#pointsList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
	
    jQuery("#pointsList").jqGrid({
        url: 'points/grid.json',
        datatype: 'json',
        colNames: ["<msg:message code='points.name'/>",
        		   "<msg:message code='points.nickName'/>", 
				   "<msg:message code='points.code'/>",
                   "<msg:message code='points.points'/>",
                   "<msg:message code='points.discription'/>"
                   ],
        colModel: [
				   {name: 'name',index:'name',width:'20%',align:'center',hidden: false,sortable:false},
				   {name: 'nickName',index: 'nickName',width:'20%',align:'center',hidden: false,sortable:false},
                   {name: 'code',index:'code',width:'20%',align:'center',hidden: false,sortable:false},
				   {name: 'points',index: 'points',width:'10%',align:'center',hidden: false,sortable:false},
                   {name: 'discription',index:'discription',width:'30%',align:'center',hidden: false,sortable:false}],
        mtype:"POST",
        postData:{c:"${points.c}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,     
        page:"${points.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:<msg:message code='jqgrid.height.250'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        caption:'<msg:message code="points.list"/>',
        toolbar: [true,"top"]
    });
	jQuery("#pointsList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
});
function submitForm(){
	$('#code').val($.trim($('#code').val()));
	$('#name').val($.trim($('#name').val()));
	jQuery("#pointsList").jqGrid("setGridParam",{postData:{c:"${points.c}",name:$('#name').val()}});		
	jQuery("#pointsList").trigger("reloadGrid");
}
</script>
</head>
<body class="skinMain">
<form:form method="post" action="points" commandName="points" name="f">
<input type="hidden" name="c" value="${points.c}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="80%">
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td align="center">
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
							<tr>
								<td align="right" width="10%" class="search_info">
									<msg:message code='points.name'/><msg:message code="system.common.sign.colon"/>
								</td>
								<td align="left" width="70%"  class="search_lable">
									<form:input path="name" htmlEscape="true" />
								</td>
								<td rowspan="2" width="20%" class="button_area">
									<input type=button class="btn2" id="select" value="<msg:message code='button.search'/>" onclick="submitForm();">
								</td>				
							</tr>		
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table id="pointsList"><tr><td>&nbsp;</td></tr></table>	
						<div id="pagered"></div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td></td>
	</tr>
</table>
</form:form>
</body>	
</html>