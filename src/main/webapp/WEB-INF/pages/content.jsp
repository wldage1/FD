<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link  href="${base}/${common}/${style}/css/skin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/${common}/js/jquery-1.6.min.js"></script>
<title></title>
<script type="text/javascript">
 $(document).ready(function(){
	 $("html").click(function(){
		$("#moreTabList",window.parent.document).hide();
		$("#moreTabBtn",window.parent.document).removeClass("onMore");
		$("#moreTabBtn",window.parent.document).removeClass("downMore");
	 });
	 var ajax = {
				//ajax请求url
				url:		'orglist.json',
				//ajax提交方式
				type:	    'post',
				//ajax请求超时时长
				timeout:	'60000',
				//ajax请求参数
				data:	Array(),
				//请求数据类型，包括json和xml两种
				dataType:	'json',
				//ajax执行前调用函数
				beforeSend:function(request){
				},
				//ajax请求完成后调用函数
				complete:function(request,textStatus){},
				//ajax请求成功调用函数
				success:function(jsonData,textStatus){
					//如果正常返回,设置GRID.data.columnData
					//为ajax请求放回的json格式的数据
					if (textStatus == "success"){
						alert(jsonData);
					}
				},
				//ajax请求错误后调用函数
				error:function(request,textStatus,error){}
		};	
	 
	 $.ajax(ajax);
 });	 
 </script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table border="1">
<tr>
<td>${content}</td>
</tr>
</table>
</body>
</html>