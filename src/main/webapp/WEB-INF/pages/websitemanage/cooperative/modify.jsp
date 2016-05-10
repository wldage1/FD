<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(document).ready(function(){
	
    var options = {
			event:'focus',
			ajax:{
				url:'${base}/websitemanage/cooperative/valid.json'
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					//提交表单
					$("form#partners").submit();
				}
			}
		};
	$("form #btnok").valid(options);
	
	//存储路径
	$("#uploadify").uploadify({
   		'swf'            : '${base}/common/flash/uploadify/uploadify.swf',//指定uploadify.swf路径   
   	//后台处理的请求的servlet,同时传递参数,由于servlet只能接收一个参数，所以将两个参数合并成一个。   
      	'uploader'       : '${base}/websitemanage/cooperative/uploadfile;jsessionid=<%=session.getId()%>',
       'buttonClass'    : 'btn2',
       'queueID'        : 'fileQueue',
       // 'percentage''speed''all'//队列中显示文件上传进度的方式：all-上传速度+百分比，percentage-百分比，speed-上传速度
       'progressData'	 : 'all',
     	//上传成功后的文件，是否在队列中自动删除
       'removeCompleted' : true,
       'auto'           : false,//是否自动上传，即选择了文件即刻上传。  
       'multi'          : false,//是否允许同时上传多文件，默认false
       'fileTypeDesc'   : "jpg文件",               
       'fileTypeExts'   : "*.jpg;*.png;*.bmp;*.gif;", //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
       'buttonText'     : "<msg:message code='info.prompt.select'/>",   
		'width'          : 60,
		'height'         : 24,
		'queueSizeLimit' : 1,   //队列中允许的最大上传数目
	//	'uploadLimit'    : 1,    //用于同时上传的数目
		'onInit'      	 : function() {
			var logoUrl = $("#logo").val();
			if(logoUrl == "" || logoUrl == null){
				
			}else{
				$("#fileDel").html("<img height='100' src='${ftpUrl}${partners.logo}'>&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' class='delMongoFile'><msg:message code='button.delete'/></a>");
			}
		},
		onUploadSuccess : function (file,data,response) {
			if(response == true){
		        var fileName = file.name;
		        $("#picName").val(fileName);
		        $("#fileDel").html('');
		        var showpath = "<div id='showLogo' name='" + fileName + "' ><span>";
		        showpath +=  "<img src='${ftpUrl}" + data + "' width='100px'></span>";
		        showpath += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		        showpath += "<a href='#' class='delMongoFile'><msg:message code='button.delete'/></a>";
		        $(showpath).appendTo($("#fileDel"));
		        $("#logo").val(data);
	         }else{
	              alert("存在不安因素导致上传失败!")
	         }
	     }
   });
	
    $(".delMongoFile").live("click",function(){
	   	$("#fileDel").html('');
	    $("#showLogo").remove();
	    $("#logo").val("");
	});
	
});
</script>
</head>

<body class="skinMain">
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td>
			<form:form method="post" action="save" commandName="partners">
				<input type="hidden" name="c" value="${partners.c}"/>
				<input type="hidden" name="id" value="${partners.id}"/>
				<input type="hidden" name="prompt" value="name" />
				<input type="hidden" name="turnCode" value="${websitemanage_cooperative.code}" />
				<input type="hidden" name="coverParam" value="1" />
				<input type="hidden" name="logo" id="logo" value="${partners.logo}" />
				<input type="hidden" name="picName" id="picName" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
					<tr>
						<td width="15%" align="right" class="skinLayout_info"><msg:message code="partners.name"/><msg:message code="system.common.sign.colon"/></td>
						<td width="85%" class="skinLayout_lable">
							<input name="name" type="text" id="name" style="width: 200;" value="${partners.name}"  class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info"><msg:message code="partners.url"/><msg:message code="system.common.sign.colon"/></td>
						<td width="85%" class="skinLayout_lable">
							<form:input path="url" id="url" maxlength="200" value="${partners.url}" class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info"><msg:message code="partners.logo"/><msg:message code="system.common.sign.colon"/></td>
						<td width="85%" class="skinLayout_lable">
						<div id="fileQueue"></div>
						<input type="file" name="file_Path" id="uploadify"/>
						<p>
							<div id="fileOperateInfo">
								<a href="javascript:$('#uploadify').uploadify('cancel', '*')">
								<msg:message code='uploadify.cancelUpload'/></a> | 
								<a href="javascript:$('#uploadify').uploadify('upload', '*')">
								<msg:message code='uploadify.startUpload'/></a>
							</div>
						</p>
						<div id="fileDel" ></div>
					</td>		
					</tr>
					<tr>
						<td width="15%" align="right">&nbsp;</td>
						<td width="85%" colspan="2" align="left">
							<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
						</td>
					</tr>
				</table>
			</form:form>
		</td>
	</tr>
</table>
</body>
</html>