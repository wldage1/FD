<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#uploadify").uploadify({
			//指定uploadify.swf路径
	    	'swf'            : '${base}/common/flash/uploadify/uploadify.swf',
	    	//后台处理的请求的servlet,同时传递参数,由于servlet只能接收一个参数，所以将两个参数合并成一个
	       	'uploader'       : '${base}/websitemanage/information/uploadfile;jsessionid=<%=session.getId()%>',
	       	//按钮样式
	       	'buttonClass'    : 'btn2',
	       	//选择文件的容器
	       	'queueID'        : 'fileQueue',
	      	//上传表单名
	       	'file_post_name' : 'fileName',
	     	//队列中显示文件上传进度的方式：all-上传速度+百分比，percentage-百分比，speed-上传速度
	        'progressData'	 : 'all',
	      	//上传成功后的文件，是否在队列中自动删除
	        'removeCompleted' : true,
	        //是否自动上传，即选择了文件即刻上传
	        'auto'           : false,
	        //是否允许同时上传多文件，默认false
	        'multi'          : false,
	        //说明
	        'fileTypeDesc'   : "<msg:message code='information.fileTypeDesc'/>",            
	        'fileTypeExts'   : "<msg:message code='information.fileTypeExts'/>",
	        'buttonText'     : "<msg:message code='info.prompt.select'/>",
	        'width'          : 60,
			'height'         : 24,
			'fileSizeLimit'  : '5MB',
			//上传数量限制
	        'queueSizeLimit' : 1,
	        'onSelectError'  : function(file, errorCode, errorMsg) {
				var settings = this.settings;
				if ($.inArray('onSelectError', settings.overrideEvents) < 0) {
					if(errorCode=SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
						if (settings.queueSizeLimit > errorMsg) {
							this.queueData.errorMsg = '<msg:message code='information.uploadOne'/>';
						}
					}
				}
	        },
			'onUploadSuccess'  : function (file,data,response) {
				if(response==true){
			        $("#fileDel").html("<img height='100' width='100' src='${ftpHttpUrl}" + data + "'>");
			        $("#titleImage").val(data);
		        }else{
		        	window.parent.Boxy.alert("<msg:message code='information.fileFail'/>", null,{title:"<msg:message code='info.prompt'/>"});
		        }
			}
		});
		
		//验证
		var options = {
				event:'focus',
				ajax:{
					url:'${base}/websitemanage/information/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						var textarea = $("<div></div>").text($("#content").val()).html();
						if($("#content").val()!=null && $("#content").val()!=''){
							$("#content").val(textarea);
							//提交表单
							$("form#information").submit();
						}else{
							top.$.layer({
								area : ['auto','auto'],
								dialog : {msg:'资讯内容不能为空',type : 8}	
							});
							}
					}
				}
			};
		$("form #btnok").valid(options);
	});
	
	//编辑器
	var editor = "";
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="content"]',{
			uploadJson : '${base}/websitemanage/information/kindeditor/uploadimg',
			fileManagerJson : '${base}/websitemanage/information/kindeditor/uploadimg',
			allowFileManager: true,
			afterBlur: function(){this.sync();}
		});
		editor.sync();
	});
	
</script>
	</head>
	<body>
		<form:form action="save" method="post" commandName="information">
			<input type="hidden" name="turnCode" value="${product_manage_bindinformation.code}" />
			<input type="hidden" name="coverParam" value="1" />
			<input name="c" type="hidden" value="${information.c}"/>
			<input type="hidden" name="prompt" value="title" />
			<input type="hidden" name="titleImage" id="titleImage">
			<input type="hidden" name="type" value="8">
			<input type="hidden" name="productId" value="${information.productId}">
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.type"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<msg:message code="information.type.product"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.title"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="title" type="text" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.shorterTitle"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="shorterTitle" type="text"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.shortestTitle"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="shortestTitle" type="text"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.keyword"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="keyword" type="text"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.source"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="source" type="text"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.author"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="author" type="text"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.summary"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:textarea path="summary" cssStyle="width:250px;height:160px;border:1px solid #A6C9E2;font-size:14px;"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.released"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%">
						<form:radiobutton path="released" value="1" label="是" checked="checked"/>
						<form:radiobutton path="released" value="0" label="否"/>
					</td>
				</tr>
				<tr>
					<td  width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.titleImage"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<div id="fileQueue"></div>
						<input type="file" name="file_upload" id="uploadify" />		
						<div id="fileOperateInfo">
							<a href="javascript:$('#uploadify').uploadify('cancel', '*')">
							<msg:message code="information.cancelUpload"/></a> | 
							<a href="javascript:$('#uploadify').uploadify('upload', '*')">
							<msg:message code="information.startUpload"/></a>
						</div>
						<div id="fileDel"></div>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.content"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:textarea path="content" cssStyle="height:450px;width:100%;" />
					</td>
				</tr>
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
					</td>
				</tr>
			</table>
		</form:form>
	</body>
</html>