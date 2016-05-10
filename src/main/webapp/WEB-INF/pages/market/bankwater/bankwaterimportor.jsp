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
			'uploader'       : '${base}/market/bankwater/bankwaterimport/uploadfile;jsessionid=<%=session.getId()%>',  
			'buttonClass'    : 'btn2',
			'queueID'        : 'fileQueue_productdoc',
			'auto'           : false,//是否允许同时上传多文件，默认false   
			'multi'          : false,//是否自动上传，即选择了文件即刻上传。  
			'fileTypeDesc'   : "<msg:message code='document.type' />",               
			'fileTypeExts'   : "*.xls;*.xlsx", //控制可上传文件的扩展名，启用本项时需同时声明fileDesc           		
			'buttonText'     : "<msg:message code='info.prompt.select'/>",   
			'width'          : 60,
			'height'         : 24,
			'queueSizeLimit' : 1,
			'onSelectError'  : function(file, errorCode, errorMsg) {
				var settings = this.settings;
				if ($.inArray('onSelectError', settings.overrideEvents) < 0) {
					if(errorCode=SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
						if (settings.queueSizeLimit > errorMsg) {
							this.queueData.errorMsg = '<msg:message code='document.importOne'/>';
						}
					}
				}
	        },
			'onUploadSuccess': function (file,data,response) {
				 if(response==true){
					 var obj = JSON.parse(data);
					 if(obj[0].fileError==undefined){
						 $("#fileUrl").val(obj[0].fileUrl);
						 $("#fileName").val(file.name);
						 $("#fileDel").html(file.name);
					 }else{
						 top.$.layer({
		    	    			area : ['auto','auto'],
		    	    			dialog : {msg:''+obj[0].fileError+'',type : 8}	
	    	    		 });
					 }
				 }else{
					 top.layer.msg("<msg:message code='uploadify.fileFail'/>");
				 }
			},
			'onUploadStart': function (file) {
                $("#uploadify_productdoc").uploadify("settings", "formData", { 'productId': $("#productId").val() });  
                //在onUploadStart事件中，也就是上传之前，把参数写好传递到后台。  
            }  
		});
		$("#startUpload").click(function(){
			$("#productID_show").html("");
			if($("#productId").val()==""){
				$("#productID_show").html("&nbsp;&nbsp;&nbsp;<msg:message code='productId.notnull'/>");
			}else{
				$('#uploadify_productdoc').uploadify('upload', '*');
			}
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
			top.$.layer({
    			area : ['auto','auto'],
    			dialog : {msg:'<msg:message code='bankwater.file.notnull'/>',type : 9}	
    		});
		}
		else{
			jQuery.ajax({
				url: "${base}/market/bankwater/bankwaterImport.json",
				type: "post",
				contentType: 'application/x-www-form-urlencoded',
				data: $("#payConfirmFromProvider").serialize(), 
				dataType : 'json',
			    success: function(data){
					if(data.zt == 'success'){
						top.$.layer({
	    	    			area : ['auto','auto'],
	    	    			dialog : {msg:'<msg:message code='info.operate.success'/>',type : 9}	
	    	    		});
						top.document.getElementById(iframeID).contentWindow.reloadGridTwo("productList");
					}else{
						top.layer.msg("error");
					}
				}
			});
		}
	}
	function closeWin(){
	 	top.document.getElementById(iframeID).contentWindow.closeWin();
	}
	
	function getProviderId(){
		$("#providerId").val($("#productId").find("option:selected").attr("id"));
		$("#fileName").val("");
		$("#fileDel").html("");
	}
</script>

</head>
<body class="skinMain">
<form id="payConfirmFromProvider" name="orderAttachment" method="post">
<input type="hidden" name="fileUrl" id="fileUrl" />
<input type="hidden" name="fileName" id="fileName" />
<input type="hidden" name="providerId" id="providerId" value="${payConfirmFromProvider.providerId}"/>
<input type="hidden" name="dataType" value="${payConfirmFromProvider.dataType}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="100%" class="skinLayout_info">
						<table border="0" cellspacing="1" cellpadding="0" width="100%" class="skinLayout">
							<tr>
								<td align="right">
									<msg:message code='product.shortname' />
									<msg:message code='system.common.sign.colon'/>
								</td>
								<td>
									<select name="productId" id="productId" onchange="getProviderId()">
										<option value="">--<msg:message code='info.select'/>--</option>
										<c:forEach items="${productList}" var="item">
											<c:if test="${productList != null}">
												<c:choose>
													<c:when test="${item.id == payConfirmFromProvider.productId}">
														<option value="${item.id}" id="${item.providerId}" selected="selected">${item.shortName}</option>
													</c:when>
													<c:otherwise>
														<option value="${item.id}" id="${item.providerId}">${item.shortName}</option>
													</c:otherwise>
												</c:choose>
											</c:if>
										</c:forEach>
									</select>
									<span id="productID_show" style="color: red;font-size: 12px;"></span>
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
									<msg:message code='bankwater.upload' />
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
			<input type="button" class="btn2" onclick="save()" id="btnok" value="<msg:message code="button.save"/>" />
			<input type="button" class="btn2" onclick="closeWin();" name="btnback" value="<msg:message code="order.fail.button"/>" id="btnback"/>
		</div>
	<div class="right"></div>
</div>
</form>
</body>
</html>
