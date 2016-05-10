<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" type="text/css" href="${base}/common/skin/css/productTab.css" />
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		// 上传产品资料
		$("#uploadify_productdoc").uploadify({
			'swf'            : '${base}/common/flash/uploadify/uploadify.swf',//指定uploadify.swf路径   
			'uploader'       : '${base}/market/order/redeemDetail/uploadfile;jsessionid=<%=session.getId()%>',//后台处理的请求的servlet,同时传递参数,由于servlet只能接收一个参数，所以将两个参数合并成一个。;jsessionid=<%=session.getId()%> (302的时候可以加上)  
			'buttonClass'    : 'btn2',
			'queueID'        : 'fileQueue_productdoc',
			'formData'       : {orderNumber:$("#orderNumber").val()},
			//'sizeLimit'		 : 86400 ,     //这里是文件最大值 byte
			//queueSizeLimit   : 50,   //允许同时上传文件的个数 
			'auto'           : false,//是否允许同时上传多文件，默认false   
			'multi'          : false,//是否自动上传，即选择了文件即刻上传。  
			'fileTypeDesc'   : "<msg:message code='orderAttachment.type' />",               
			'fileTypeExts'   : "*.rar;*.zip", //控制可上传文件的扩展名，启用本项时需同时声明fileDesc           		
			'buttonText'     : "<msg:message code='info.prompt.select'/>",   
			'width'          : 60,
			'height'         : 24,
			onUploadSuccess: function (file,data,response) {
				 if(response==true){
					 var fileName = file.name;
					 $("#fileName").val(fileName);
					 $("#fileDel").text(fileName);
				 }else{
					 top.layer.msg("<msg:message code='uploadify.fileFail'/>");
				 }
			}
		});
		$("#startUpload").click(function(){
			$('#uploadify_productdoc').uploadify('upload', '*');
		});
		$("#cancelUpload").click(function(){
			$('#uploadify_productdoc').uploadify('cancel', '*');
		});
	});
	var iframeID='${iframeID}';
	// 保存
	function save(){
		var fname =  $("#fileName").val();
		if (!fname){
		var index=top.$.layer({
    			area : ['auto','auto'],
    			dialog : {msg:'<msg:message code='orderAttachment.file.tip'/>',type : 9}	
    		});
		}
		else{
			jQuery.ajax({
					url: "${base}/market/order/saveRedeemOrderAttachment.json",
				   type: "post",
			contentType: 'application/x-www-form-urlencoded',
				   data: $("#redeemOrderAttachment").serialize(), 
			  dataType : 'json',
			    success: function(data){
					if(data.status == 'success'){
						top.document.getElementById(iframeID).contentWindow.location.reload(true);
					}else{
						top.layer.msg("error");
					}
					closeWin();
				}
			});
		}
	}
	
	function closeWin(){
	 	top.document.getElementById(iframeID).contentWindow.closeWin();
	}
</script>

</head>
<body class="skinMain">
<form id="redeemOrderAttachment" name="redeemOrderAttachment" method="post">
<input type="hidden" name="fileName" id="fileName">
<input type="hidden" name="orderNumber" id="orderNumber" value="${redeemOrder.orderNumber }" />
<input type="hidden" name="id" id="id" value="${redeemOrder.id}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="100%" class="skinLayout_info">
						<table border="0" cellspacing="1" cellpadding="0" width="100%" class="skinLayout">
							<tr>
								<td align="right">
									<msg:message code='orderAttachment.title' />
									<msg:message code='system.common.sign.colon'/>
								</td>
								<td>
									<input type='text' name="title" id='title'/>
								</td>
							</tr>
							<tr>
								<td align="right">
									<msg:message code='orderAttachment.fileName' />
									<msg:message code='system.common.sign.colon'/>
								</td>
								<td id="fileDel">
								</td>
							</tr>
							<tr>
								<td rowspan="2" align="right">
									<msg:message code='orderAttachment.upload' />
									<msg:message code='system.common.sign.colon'/>
								</td>
								<td>
									<table border="0" width="350px" height="120px">
										<tr>
											<td>
												<div id="fileQueue_productdoc" ></div>
											</td>
										</tr>
									</table>
									
								</td>
							</tr>
							<tr>
								<td>
									<table border="0">
										<tr>
											<td>
												<input type="file" name="uploadify_productdoc" id="uploadify_productdoc" />   
											</td>
											<td valign="top">
												<input type="button" class="btn2" name="startUpload" id="startUpload" value="<msg:message code='uploadify.startUpload'/>"/>
											</td>
											<td valign="top">
												<input type="button" class="btn2" name="cancelUpload" id="cancelUpload" value="<msg:message code='uploadify.cancelUpload'/>" />
											</td>				
										</tr>
									</table>								
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr height="35">
					<td width="100%"style="border:0px;"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
<div class="toolbar">
	<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" id="btnok" onclick="save();" value="<msg:message code="button.save"/>" />
			<input type="button" class="btn2" onclick="closeWin();" name="btnback" value="<msg:message code="order.fail.button"/>" id="btnback"/>
		</div>
	<div class="right"></div>
</div>
</body>
</html>
