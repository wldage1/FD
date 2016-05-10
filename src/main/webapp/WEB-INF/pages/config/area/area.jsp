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
	$("#areaList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){

	var i = 1;
	var content = "";
	var parentName = "";
	var parentId = "";
	<c:forEach items="${List}" var="m">
		var id = "${m.id}";
		
		if( i == 1 || (i-1) % 5 == 0){
	    	content += "<tr>";
	    }
		parentName = "${m.parentName}";
		parentId = "${m.parentId}"; 
		content +=  "<td><a href='javascript:void(0);' id='" + id + "' parentid='"+id+ "' parentName=${m.name}  class='shortcut_look'";
		content += " title='查看下级' >";
		content += "${m.name}";
		content += "</a>";
		<c:if test="${!empty config_area_modify}">
	    <security:authorize ifAnyGranted="${config_area_modify.code}">	
			content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${config_area_modify.indexCode}'";
			content += " title='${config_area_modify.name}' >";
			content += "&nbsp;&nbsp;[${config_area_modify.name}]&nbsp;&nbsp;";
			content += "</a>";
	    </security:authorize>
	    </c:if>  
	    <c:if test="${!empty config_area_delete}">
	    <security:authorize ifAnyGranted="${config_area_delete.code}">
	    	content += "<a href='javascript:void(0);' id='" + id + "' name=${m.name}  class='shortcut_${config_area_delete.indexCode}'";
	  		    content += " title='${config_area_delete.name}' >";
	  		    content += "&nbsp;[${config_area_delete.name}]";
	  		    content += "</a></td>";
	    </security:authorize>
	    </c:if>

	    i++;
	    if((i-1) % 5 == 0){
	    	content += "</tr>";
	    }
	</c:forEach>
	
	//当没有下级地区时，设置上级地区名称
	if(content == ""){
		content += "<tr><td colspan='5' style='text-align: center; color: red;'>";
		content += "无下级地区";
		content += "<a href='${base}${config_area_create.controller}?c=${config_area_create.code}&id=${area.parentId}' style='color: gray'>&nbsp;&nbsp;添加</a></td></tr>";
		parentName = "${parentName}";
	}
	$("#areaList").append(content);
	
	if(parentName == "dingjidiqu"||parentName==null||parentName==""){
		$("#parentAreaName").html("顶级地区");
	}else{
		$("#parentAreaName").html("上级地区 -  "+parentName);
	}
	 
	$(".shortcut_${config_area_delete.indexCode}").click(function(){
    	var rowid = $(this).attr("id");
    	var name  = $(this).attr("name");
			var url = "${base}${config_area_delete.controller}.json?c=${config_area_delete.code}&id="+rowid+"&prompt=name&name="+encodeURIComponent(encodeURIComponent(name));
/* 		window.parent.Boxy.confirm("<msg:message code='info.prompt.delete.confirm'/>",  function(){
			$.ajax({
				url:url,
				type:'post',
				timeout:'60000',
				dataType:'json',
				success:function(jsonData,textStatus){
					if (textStatus == "success"){
						if (jsonData.status == "success"){
							window.location.href = "${base}/config/area?c=${code}&parentId=" + ${area.parentId};
						}else{
							window.parent.Boxy.alert("<msg:message code='area.hasuser'/>", null, {title: "<msg:message code='info.prompt'/>"});
						}
					}
				}
			});
		}, {title: "<msg:message code='info.prompt'/>"});  */
			var index = top.$.layer({
				shade    : [0.1,'#eee', true],
				dialog   : {
					msg  : "<msg:message code='info.prompt.delete.confirm'/>",
					btns : 2,
					type : 4,
					btn  : ["<msg:message code='button.confirm'/>", "<msg:message code='button.cancel'/>"],
					yes  : function(){
						$.ajax({
							url:url,
							type:'post',
							timeout:'60000',
							dataType:'json',
							success:function(jsonData,textStatus){
								if(textStatus == "success"){
									if(jsonData.status == "success"){
										window.location.href = "${base}/config/area?c=${code}&parentId=" + ${area.parentId};
										top.layer.close(index);
									}else{
										top.layer.close(index);
										top.layer.alert("<msg:message code='area.hasuser'/>", 3);
									}
								}
							}
						});
					},
					no   : function(){
						top.layer.close(index);
					}
				}
			});
    });
	
    $(".shortcut_${config_area_modify.indexCode}").click(function(){
    	var rowid = $(this).attr("id");
		window.location.href = "${base}${config_area_modify.controller}?c=${config_area_modify.code}&id="+ rowid;
    });
    
     $(".shortcut_look").click(function(){
    	var parentid = $(this).attr("parentid");
    	var parentName = $(this).attr("parentName");
		window.location.href = "${base}/config/area?c=${code}&parentId="+ parentid+"&parentName="+encodeURIComponent(encodeURIComponent(parentName));
    });

    jQuery("#areaList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    <c:if test="${!empty config_area_create}">
    <security:authorize ifAnyGranted="${config_area_create.code}">	 
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
			  .attr("id","${config_area_create.indexCode}")
			  .append($("<img/>").attr("src","${base}/${skin}/${config_area_create.icon}")
			  .attr("width","18").attr("height","18").attr("border","0")
			  .attr("border","0").attr("align","absmiddle"))
			  .append("${config_area_create.name}");
	$("#bar").append("&nbsp;&nbsp;").append($("<span></span>").append($ea));
	
    $("#${config_area_create.indexCode}","#bar").click(function(){
    	window.location.href = "${base}${config_area_create.controller}?c=${config_area_create.code}&id="+ ${area.parentId};
    });    
    </security:authorize>
    </c:if>
    
    if(parentId != 0 || parentId == ""){
    	var content =  "<a href='javascript:void(0);'  class='shortcut_parentArea'";
		content += " title='查看上级' >&nbsp;&nbsp;&nbsp;&nbsp;<img width='18' height='18' border='0' align='absmiddle' src='${base}/${skin}/images/icon/lookup_superior.png'/>查看上级";
		content += "</a>";
		$("#bar").append("<span></span>").append(content);
		
		$(".shortcut_parentArea").click(function(){
			if(parentId == "")
				parentId = ${area.parentId};
			window.location.href = "${base}/config/area/parent?c=${code}&id=" + parentId;
	    });
    }
    
    //如果表格不足五列，则补齐
    if(i != 1){
    	var content = "";
    	for(var j=0; j<5-((i-1)%5); j++){
    		content += "<td>&nbsp;</td>";
    	}
    	$("#areaList tr").last().append(content);
    }
});

</script>

<style type="text/css">

.green {
	color: green;
}

table{
	border-collapse:collapse;
}

*{margin:0;padding:0;}

table.list {
	width: 100%;
}

table.list tr:hover {
	background-color: #CCCCCC;
}

table.list .selected {
	background-color: #fff8e4;
}

table.list th {
	height: 24px;
	line-height: 24px;
	color: #5b5b5b;
	font-weight: normal;
	text-align: left;
	white-space: nowrap;
	border-top: solid 1px #d8deea;
	border-right: solid 1px #c6c9ca;
	border-bottom: solid 1px #c6c9ca;
	background: 0px -60px #f2f4f6 no-repeat;
}

table.list th a, table.list th span {
	padding: 0px 8px;
}

table.list td {
	line-height: 35px;
	padding-left: 5px;
	border-bottom: 1px solid #CCCC99;
}

table.list td a {
	color: #336699;
}

table.list td a:hover {
	color: #ff6600;
}

</style>

</head>
<body class="skinMain">
<div class="bar" id ="bar">
		
</div>

<table width="100%" border="0" class="list" id ="areaList">
	<tr>
		<th id="parentAreaName" colspan="5" class="green" style="text-align: center;">
			
		</th>
	</tr>
	
</table>
</body>
</html>