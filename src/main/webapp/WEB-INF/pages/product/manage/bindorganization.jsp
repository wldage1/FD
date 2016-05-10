<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
	input,label {vertical-align:middle;margin-right:3px;} 
	.list-bg{margin-left:20px;background-color:#FFF;margin-bottom:8px;padding:3px;}
</style>
<script type="text/javascript">
$(document).ready(function() {
	var arr = ${orgIds};
	for(var i = 0;i<arr.length;i++){
		$("#checkbox_"+arr[i]).attr("checked","checked");
	}
	
	$(".ch_all").click(function(){
		var v = this.value;
		var checked = this.checked;
		$("input[class='type-"+v+"']").each(function(index,val){ 
			var ch = false;
			if(checked){
				ch = true;
			}
			$(this).attr("checked",ch);
		});
	});

})

function chk(){    
	  var obj=document.getElementsByName('orgID');  
	  var s='';    
	  for(var i=0; i<obj.length; i++){    
		    if(obj[i].checked){
		    	s+=obj[i].value+',';
		    }
	  }
	  $("#orgIDs").val(s);
	  $("form #productOrgMapping").submit();
  }    
</script>
</head>
<body class="skinMain">
<c:set var="ct" scope="page" value="0"/>
<form:form method="post" action="bindorganization/save" commandName="productOrgMapping">
<input type="hidden" name="turnCode" value="${product_manage.code}" />
<input type="hidden" name="coverParam" value="1" />
<input type="hidden" name="prompt" value="name" />
<form:input type="hidden" path="productID" value="${productID}"/>
<form:input type="hidden" path="c" value="${c}"/>
<form:input type="hidden" path="orgIDs"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						产品名称
					</td>
					<td width="85%">
						${name}
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
					指定销售机构
					</td>
					<td width="85%">
						<div style="height:25px;">嘉华财富销售机构&nbsp;&nbsp;全选<input type="checkbox" class="ch_all" value="3"/></div>
						<div class="list-bg">
							<c:forEach items="${orgList}" var="it" varStatus="state">
							<c:if test="${it.identity == 1}">
							<input type="hidden" name="mappingList[${ct}].type" value="3"/>
							<input type="checkbox" value="${it.id}" name="mappingList[${ct}].orgID" id="checkbox_${it.id}" class="type-3"/><label>${it.name}</label>
							<c:set var="ct" scope="page" value="${ct+1}"/>
							<c:if test="${state.count%4 == 0 }"><br></c:if>
							</c:if>
							
							</c:forEach>
						</div>
						<div style="height:25px;">非嘉华财富销售机构&nbsp;&nbsp;全选<input type="checkbox" class="ch_all" value="1"/></div>
						<div class="list-bg">
							<c:forEach items="${orgList}" var="it" varStatus="state">
							<c:if test="${it.identity == 2}">
							<input type="hidden" name="mappingList[${ct}].type" value="1"/>
							<input type="checkbox" value="${it.id}" name="mappingList[${ct}].orgID" id="checkbox_${it.id}" class="type-1" /><label>${it.name}</label>
							<c:set var="ct" scope="page" value="${ct+1}"/>
							<c:if test="${state.count%4 == 0 }"><br></c:if>
							</c:if>
							</c:forEach>
						</div>
						<div style="height:25px;">非机构理财师&nbsp;&nbsp;<input type="hidden" name="mappingList[${ct}].type" value="2"/><input type="checkbox" name="mappingList[${ct}].orgID" id="checkbox_null" value="-1"/></div>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="submit" class="btn2" name="btnok" value="保存" id="btnok" onclick="chk();"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="返回" id="btnback"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tableMargin"></td>
	</tr>
	<tr>
		<td></td>
	</tr>
</table>
</form:form>
</body>
</html>