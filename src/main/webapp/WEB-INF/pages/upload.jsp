<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/tlds/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@ taglib prefix="form" uri="/WEB-INF/tlds/springframework-form.tld" %>
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
 <title></title>
<head>   
<link  href="${base}/common/skin/css/skin.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${base}/common/skin/css/uploadify.css">
<script type="text/javascript" src="${base}/common/js/jquery-1.6.min.js"></script>
<script language="javascript" src="${base}/common/js/uploadify/jquery.uploadify-3.1.min.js" type="text/javascript"></script>
<script type="text/javascript">   
$(document).ready(function() {   
	var fileTypeDesc = "${fileTypeDesc}";
	if (!fileTypeDesc){
		fileTypeDesc = "txt、jpg、gif、swf文件";
	}
	var fileTypeExts = "${fileTypeExts}";
	if (!fileTypeExts){
		fileTypeExts = "*.txt;*.jpg;*.gif;*.swf;";
	}
	var queueSizeLimit = "${queueSizeLimit}";
	if (!queueSizeLimit){
		queueSizeLimit = 2;
	}
	var uploadLimit = "${uploadLimit}";
	if (!uploadLimit){
		uploadLimit = 2;
	}	
	var inputIds = "${inputIds}";
	var imgIds = "${imgIds}";
    $("#uploadify").uploadify({   
        swf            : '${base}/common/flash/uploadify/uploadify.swf',//指定uploadify.swf路径   
        uploader       : '${base}${controllerName}',//后台处理的请求的servlet,同时传递参数,由于servlet只能接收一个参数，所以将两个参数合并成一个。   
        buttonClass    : 'btn2',
        fileDataName   : 'uploadify',   
        queueID        : 'fileQueue',//与下面的id对应   
        queueSizeLimit : queueSizeLimit,//当允许多文件生成时，设置选择文件的个数   
        fileTypeDesc   : fileTypeDesc,               
        fileTypeExts   : fileTypeExts, //控制可上传文件的扩展名，启用本项时需同时声明fileDesc   
        auto           : false,//是否自动上传，即选择了文件即刻上传。   
        multi          : true, //是否允许同时上传多文件，默认false   
        uploadLimit    : uploadLimit,//多文件上传时，同时上传文件数目限制 
        buttonText     : '<msg:message code='info.prompt.select'/>',   
        width          : 60,
        height         : 24,
        onUploadSuccess: function (file, response) {
        	var arr,id = file.id;
        	if (id){
        		arr = id.split("_");
        	}
        	var index = arr[arr.length-1];
        	if (inputIds){
        		var arrInputIds = inputIds.split(",");
        		var eid = arrInputIds[index];
        		if (eid){
        			$(window.parent.window.frames["${iframeId}"].document).find("#"+eid).val(response);
        		}
        	}
        	if (imgIds){
        		var arrImgIds = imgIds(",");
        		var eid = arrImgIds[index];
        		if (eid){
        			$(window.parent.window.frames["${iframeId}"].document).find("#"+eid).attr("src","${base}/"+response);
        		}
        	}        	
        }   
    });
    
	$("#startUpload").click(function(){
		$("#uploadify").uploadify("upload");
	});  
	$("#cancelUpload").click(function(){
		$("#uploadify").uploadify("cancel");
	});  	
	$("#uploadfileDesc").text(fileTypeExts);
	$("#uploadfileSize").text(uploadLimit);
});   
  
</script>  
</head>
<body bottommargin="0" leftmargin="0" topmargin="0" rightmargin="0" >
<form name="actionForm" method="post" enctype="multipart/form-data" id="actionForm">
<table border="0" width="100%" height="100%" style="background:#e1eeff;" cellpadding="1">
	<tr>
		<td height="100%" width="100%" style="background:#ffffff;">
			<table border="0" width="100%" height="100%">
				<tr>
					<td height="100%" width="100%">
						<div id="fileQueue" ></div>   
					</td>
				</tr>
				<tr>
					<td height="100%" width="100%">
						<msg:message code='info.prompt.uploadfile.desc'/><span id="uploadfileDesc"></span>
					</td>	
				</tr>	
				<tr>
					<td height="100%" width="100%">
						<msg:message code='info.prompt.uploadfile.size'/><span id="uploadfileSize"></span>
					</td>	
				</tr>									
				<tr>
					<td height="100%" width="100%">
						<table border="0">
							<tr>
								<td>
									<input type="file" name="uploadify" id="uploadify" />   
								</td>
								<td valign="top">
									<input type="button" class="btn2" name="startUpload" id="startUpload" value="开始上传"/>
								</td>
								<td valign="top">
									<input type="button" class="btn2" name="cancelUpload" id="cancelUpload" value="取消上传" />
								</td>				
							</tr>
						</table>
					</td>	
				</tr>	
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
