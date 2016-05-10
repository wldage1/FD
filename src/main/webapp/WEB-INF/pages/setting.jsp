<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<title><msg:message code="system.name"></msg:message></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
$(document).ready(function(){
	$(document).click(function() {
		var dis = $(window.parent.document).find("#startMenu").css("display");
		if (dis == 'block'){
			$(window.parent.document).find("#startMenu").hide(600);
		}
	});	
	$(".setting-background-button").click(function(){
		var id = $(this).attr("id");
		var url = "${base}/setskin.json?id="+id;
    	var conf = confirm("确定要使用此皮肤吗？");
    	if (conf){
    		$.ajax({
    			url:url,
    			type:'post',
    			timeout:'60000',
    			dataType:'json',
    			success:function(jsonData,textStatus){
    				if (textStatus == "success"){
   						var path = jsonData.picturePath;
   						$(window.parent.document).find(".main-div").css("background-image","url("+path+")");
    				}
    			}
    		});
    	}		
	});

});
</script>
</head>
<body>
<table width="100%" height="100%" border="0" cellspacing="1" cellpadding="1" class="skinWelcome" >
	<tr>
		<td width="100%" height="40%" id="operate" align="center" valign="top">
			<table border="0" cellspacing="2" cellpadding="2" class="skinNav">
			<c:set var="starttr" value="<tr>"></c:set>
			<c:set var="endtr" value="</tr>"></c:set>
			<c:set var="linecount" value="4"></c:set>
			<c:set var="subindex" value="0"></c:set>
			<c:forEach var="bglist" items="${backgroundList}" varStatus="vs">
        		<c:if test="${subindex%linecount == 0}">
        			${starttr}
        		</c:if>
        		<c:set var="subindex" value="${subindex+1}"></c:set>
        		<td height="70" align="center">
	        		<div title="${bglist.name}" id="${subindex}">
	        		<p>${bglist.name}</p>
	        		<p><img title="${bglist.name}" border="0" src="${base}/${bglist.picturePath}" width="250" height="150"/></p>
	        		<p><input value="使用该背景" class="setting-background-button" id="${bglist.id}" type="button"/></p>
     				</div>
    			</td>
    			<c:if test="${(subindex%linecount == 0)}">
    				${endtr}
        		</c:if>				
			</c:forEach>
			<c:if test="${subindex%linecount > 0}">
        		${endtr}
        	</c:if>
			</table>
		</td>
	</tr>
</table> 
</body>
</html>
