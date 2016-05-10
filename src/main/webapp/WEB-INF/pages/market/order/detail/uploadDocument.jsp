<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" type="text/css" href="${base}/common/skin/css/productTab.css" />
<script type="text/javascript">
	$(document).ready(function() {
		// 上传产品资料
		$("#uploadify_productdoc").uploadify({
			'swf'            : '${base}/common/flash/uploadify/uploadify.swf',//指定uploadify.swf路径   
			'uploader'       : '${base}/market/order/uploadDocument/uploadfile;jsessionid=<%=session.getId()%>',  
			'buttonClass'    : 'btn2',
			'queueID'        : 'fileQueue_productdoc',
			'formData'       : {orderNumber:'${order.orderNumber}'},
			'auto'           : false,//是否允许同时上传多文件，默认false   
			'multi'          : false,//是否自动上传，即选择了文件即刻上传。  
			'fileTypeDesc'   : "<msg:message code='orderAttachment.type' />",               
			'fileTypeExts'   : "*.rar;*.zip", //控制可上传文件的扩展名，启用本项时需同时声明fileDesc           		
			'buttonText'     : "<msg:message code='info.prompt.select'/>",   
			'fileSizeLimit'  : 10485760,
			'width'          : 60,
			'height'         : 24,
			'queueSizeLimit' : 1,
			'onSelectError'  : function(file, errorCode, errorMsg) {
				var settings = this.settings;
				if ($.inArray('onSelectError', settings.overrideEvents) < 0) {
					if(errorCode=SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
						if (settings.queueSizeLimit > errorMsg) {
							 top.layer.msg("<msg:message code='document.uploadOne'/>");
						}
					}
				}
	        },
	        'onSelect' : function(file) {
				var size = file.size;
				if(size >= 10485760){
					top.layer.msg("请上传10M以内的文件");
					$('#uploadify_productdoc').uploadify('cancelUpload', '*');
				}
			},
			onUploadSuccess: function (file,data,response) {
				 if(response==true){
					 $("#fileUrl").val(data);
					 $("#fileName").val(file.name);
					 $("#fileDel").html(file.name);
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
		
		//验证
		var options = {
				event:'focus',
				ajax:{
					url:'${base}/market/order/uploadDocument/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						//提交表单
						save();
					}
				}
			};
		$("form #btnok").valid(options);
	});
	var iframeID='${iframeID}';
	// 保存
	function save(){
		jQuery.ajax({
				url: "${base}/market/order/saveOrderAttachment.json",
			   type: "post",
		contentType: 'application/x-www-form-urlencoded',
			   data: $("#orderAttachment").serialize(), 
		  dataType : 'json',
		    success: function(data){
				if(data.zt == 'success'){
					top.document.getElementById(iframeID).contentWindow.location.reload(true);
				}else{
					top.layer.msg("error");
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
<form id="orderAttachment" name="orderAttachment" method="post">
<input type="hidden" name="fileName" id="fileName" />
<input type="hidden" name="fileUrl" id="fileUrl" />
<input type="hidden" name="orderNumber" value="${order.orderNumber}"/>
<input type="hidden" name="id" id="id" value="${order.id}"/>
<input type="hidden" name="productID" id="productID" value="${order.productID}"/>
<input type="hidden" name="subProductID" id="subProductID" value="${order.subProductID}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="100%" class="skinLayout_info">
						<table border="0" cellspacing="1" cellpadding="0" width="100%" class="skinLayout">
							<tr>
								<td align="right">
									<msg:message code='order.contractNumber' />
									<msg:message code='system.common.sign.colon'/>
								</td>
								<td>
									<input id="contractNumber" name="contractNumber" <c:if test="${order.tradeType==3}">value="${order.contractNumber}" readonly</c:if> <c:if test="${order.tradeType!=3}">class="valid-control"</c:if>/>
								</td>
							</tr>
							<tr>
								<td align="right">
									<msg:message code='order.contractNumber.comfirm' />
									<msg:message code='system.common.sign.colon'/>
								</td>
								<td>
									<input type='text' name="confirmContractNumber" <c:if test="${order.tradeType==3}">value="${order.contractNumber}" readonly</c:if>  id='confirmContractNumber' <c:if test="${order.tradeType!=3}">class="valid-control"</c:if>/>
								</td>
							</tr>
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
<div class="toolbar">
	<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" id="btnok" value="<msg:message code="button.save"/>" />
			<input type="button" class="btn2" onclick="closeWin();" name="btnback" value="<msg:message code="order.fail.button"/>" id="btnback"/>
		</div>
	<div class="right"></div>
</div>
</form>
</body>
</html>
