<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
	var iframeID='${iframeID}';
	$(document).ready(function() {
		// 上传单证附件
		$("#uploadDoc").uploadify({
			'swf'            : '${base}/common/flash/uploadify/uploadify.swf',//指定uploadify.swf路径
			'uploader'       : '${base}/market/docmanage/uploadDocAtt;jsessionid=<%=session.getId()%>',
			'buttonClass'    : 'btn2',
			'queueID'        : 'fileQueue_doc',
			'formData'       : {orderNumber:'${document.orderNumber}'},
			'auto'           : false,//是否允许同时上传多文件，默认false
			'multi'          : false,//是否自动上传，即选择了文件即刻上传。
			'fileTypeDesc'   : "<msg:message code='uploadify.fileTypeDesc'/>",
			'fileTypeExts'   : "*.rar;*.zip", //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
			'buttonText'     : "<msg:message code='uploadify.select'/>",
			'fileSizeLimit'  : 10485760,
			'width'          : 60,
			'height'         : 24,
			'queueSizeLimit' : 1,
			'onSelectError'  : function(file, errorCode, errorMsg) {
				var settings = this.settings;
				if ($.inArray('onSelectError', settings.overrideEvents) < 0) {
					if(errorCode=SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
						if (settings.queueSizeLimit > errorMsg) {
							 top.layer.msg("<msg:message code='doc.att.upload.onlyone'/>",1);
						}
					}
				}
	        },
	        'onSelect' : function(file) {
				var size = file.size;
				if(size >= 10485760){
					top.layer.msg("<msg:message code='doc.att.upload.underTen'/>",1);
					$('#uploadDoc').uploadify('cancelUpload', '*');
				}
			},
			onUploadSuccess: function (file,data,response) {
				 if(response==true){
					 $("#attFileUrl").val(data);
					 $("#attFileName").val(file.name);
					 $("#fileDel").html(file.name);
				 }else{
					 top.layer.msg("<msg:message code='uploadify.fileFail'/>");
				 }
			}
		});
		$("#startUpload").click(function(){
			$('#uploadDoc').uploadify('upload', '*');
		});
		$("#cancelUpload").click(function(){
			$('#uploadDoc').uploadify('cancel', '*');
		});
		
	});
	// 保存
	function save(){
		var title = $("#attTitle").val();
		if(title==''){
			top.layer.msg("<msg:message code='doc.att.title.notNull'/>",1);
			return false;
		}
		//附件标题验证状态：0-重复，1-通过
		var status = 0;
		$.ajax({
			url: '${base}/market/docmanage/checkAttTitle.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: {attTitle:title},
			async:false,
			success:function(jsonData){
				if(jsonData.status == 'success'){
					status = 1;
				}
			}
		});
		if(status==0){
			top.layer.msg("<msg:message code='doc.att.title.already'/>",1);
			return false;
		}
		var fileUrl = $("#attFileUrl").val();
		if(fileUrl==''){
			top.layer.msg("<msg:message code='doc.att.upload.please'/>",1);
			return false;
		}
		jQuery.ajax({
				url: "${base}/market/docmanage/saveDocAtt.json",
			   type: "post",
		contentType: 'application/x-www-form-urlencoded',
			   data: $("#docAtt").serialize(), 
		  dataType : 'json',
		    success: function(data){
				if(data.status == 'success'){
					top.layer.msg("<msg:message code='doc.att.save.success'/>",1,1);
				}else{
					top.layer.msg("<msg:message code='doc.att.save.fail'/>",1);
				}
				closeWin();
			}
		});
	}
	function closeWin(){
	 	top.document.getElementById(iframeID).contentWindow.closeWin();
	}
</script>
</head>
<body class="skinMain">
<form id="docAtt" name="docAtt" method="post">
<input type="hidden" name="attFileName" id="attFileName" />
<input type="hidden" name="attFileUrl" id="attFileUrl" />
<input type="hidden" name="orderNumber" value="${document.orderNumber}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="100%" class="skinLayout_info">
						<table border="0" cellspacing="1" cellpadding="0" width="100%" class="skinLayout">
							<tr>
								<td align="right">
									<msg:message code='doc.orderNumber'/><msg:message code="system.common.sign.colon"/>
								</td>
								<td>
									${document.orderNumber}
								</td>
							</tr>
							<tr>
								<td align="right">
									<msg:message code='doc.att.title'/><msg:message code="system.common.sign.colon"/>
								</td>
								<td>
									<input type="text" name="attTitle" id="attTitle"/>
								</td>
							</tr>
							<tr>
								<td align="right">
									<msg:message code='doc.att.name'/><msg:message code="system.common.sign.colon"/>
								</td>
								<td id="fileDel">
								</td>
							</tr>
							<tr>
								<td rowspan="2" align="right">
									<msg:message code='doc.att.upload.name'/><msg:message code="system.common.sign.colon"/>
								</td>
								<td>
									<table border="0" width="350px" height="120px">
										<tr>
											<td>
												<div id="fileQueue_doc" ></div>
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
												<input type="file" name="uploadDoc" id="uploadDoc" />   
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
<div class="toolbar">
	<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" id="btnok" onclick="save()" value="<msg:message code="button.save"/>" />
			<input type="button" class="btn2" onclick="closeWin();" name="btnback" value="<msg:message code='button.cancel'/>" id="btnback"/>
		</div>
	<div class="right"></div>
</div>
</form>
</body>
</html>