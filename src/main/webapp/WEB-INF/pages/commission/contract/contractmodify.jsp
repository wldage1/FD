<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<title></title>

<script type="text/javascript">

$(document).ready(function() {
			$("#uploadify").uploadify({
			//指定uploadify.swf路径
	    	'swf'            : '${base}/common/flash/uploadify/uploadify.swf',
	    	//后台处理的请求的servlet,同时传递参数,由于servlet只能接收一个参数，所以将两个参数合并成一个
	       	'uploader'       : '${base}/commission/orgcontract/uploadfile;jsessionid=<%=session.getId()%>',
	       	//按钮样式
	       	'buttonClass'    : 'btn2',
	       	//选择文件的容器
	       	'queueID'        : 'fileQueue',
	     	//队列中显示文件上传进度的方式：all-上传速度+百分比，percentage-百分比，speed-上传速度
	        'progressData'	 : 'all',
	      	//上传成功后的文件，是否在队列中自动删除
	        'removeCompleted' : true,
	        //是否自动上传，即选择了文件即刻上传
	        'auto'           : false,
	        //是否允许同时上传多文件，默认false
	        'multi'          : false,
	        //说明
	        'fileTypeDesc'   : "<msg:message code='provider.fileTypeDesc'/>",            
	        'fileTypeExts'   : "<msg:message code='contract.fileTypeExts'/>",
	        'buttonText'     : "<msg:message code='info.prompt.select'/>",
	        'width'          : 60,
			'height'         : 24,
			'fileSizeLimit'  : '10MB',
			//上传数量限制
	        'queueSizeLimit' : 1,
	        'onSelectError'  : function(file, errorCode, errorMsg) {
				var settings = this.settings;
				if ($.inArray('onSelectError', settings.overrideEvents) < 0) {
					if(errorCode=SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
						if (settings.queueSizeLimit > errorMsg) {
							this.queueData.errorMsg = '<msg:message code='provider.uploadOne'/>';
						}
					}
				}
	        },
			'onUploadSuccess'  : function (file,data,response) {
				if(response==true){
					var i=data.lastIndexOf('/');
					var fileName=data.substring(i+1);
			        $("#fileUrl").val(data);
					$("#fileName").val(fileName);
					$("#fileDel").text(fileName);
					top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"<msg:message code='contract.uploadSuccess'/>",type :1}	
					});
		        }else{
		        	window.parent.Boxy.alert("<msg:message code='provider.fileFail'/>", null,{title:"<msg:message code='info.prompt'/>"});
		        }
			}
		});
	//表单提交s
	var options = {
			event:'focus',
			ajax:{
				url:'${base}/commission/orgcontract/valid.json'
			},
			validStatus:function(status){
				if (status){
					//var content = $("#content").val();
					//if(content == '' || content == '<br />'){
					//	top.layer.msg("<msg:message code='NotEmpty.contract.content'/>");
					//}else{
						$("form#contract").submit();
					//}
				}
			}
		};
	$("form #btnok").valid(options);
	$('#download').click(function(){
		var rowid = ${contract.id};
		 $.ajax({
				url: '${base}/commission/contract/download.json',
				type:'post',
				timeout:'60000',
				dataType:'json',
				data:{id:rowid},
				success:function(jsonData){
					if(jsonData){
						window.location.href = '${ftpUrl}'+jsonData.url;
					}else{
						top.$.layer({
							shade : [0.5 , '#000' , true],
						  	area : ['auto','auto'],
						  	offset : ['150px',''],
							dialog : {msg:'<msg:message code='sorry.contract.dowmload'/>',type : 8}	
						});
					}
				}
		});
	})
});
function imposeMaxLength(Object, MaxLen)
{
  return (Object.value.length <MaxLen);
}
</script>
</head>
<body class="skinMain">
<form:form  method="post" action="saveContract" commandName="contract" >
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<input type="hidden" name="turnCode" value="${commission_contract.code}"/>
			<input type="hidden" name="c" value="${contract.c}"/>
			<input type="hidden" name="coverParam" value="1"/>
			<input type="hidden" name="prompt" value="contractName" />
			<input type="hidden" name="id" value="${contract.id}"/>
			<input type="hidden" name="operate" value="1" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="contract.name"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<form:input path="contractName" maxlength="20" cssClass="valid-control"/>
							<font color="red">&nbsp;*&nbsp;</font>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="contract.type"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<form:select path="contractType" >
									<form:options items="${contractType}" />
							</form:select>
							<font color="red">&nbsp;*&nbsp;</font>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="contract.teamOrOrgName"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<!-- <form:select path="teamOrOrgID" cssClass="valid-control" >
							<form:option value="">--<msg:message code='info.select'/>--</form:option>
							<form:options items="${orgList}" itemValue="id" itemLabel="name" />
							</form:select> -->
							<input type="text" name="teamOrOrgName" id="teamOrOrgName" readonly="readonly" value="${contract.teamOrOrgName}" >
							<font color="red">&nbsp;*&nbsp;</font>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="contract.upload"/><msg:message code="system.common.sign.colon"/></td>
						<td width="70%" class="skinLayout_lable">
							<div id="fileQueue"></div>
							<input type="file" name="file_upload" id="uploadify" />
							<div id="fileOperateInfo">
								<input type="hidden" name="fileName" id="fileName" value="${contract.fileName}" readonly="readonly">
								<a href="javascript:$('#uploadify').uploadify('cancel', '*')">
								<msg:message code="provider.cancelUpload"/></a> | 
								<a href="javascript:$('#uploadify').uploadify('upload', '*')">
								<msg:message code="provider.startUpload"/></a>
								<input type="text" name="fileUrl" id="fileUrl" style="width: 0;height:0" readonly="readonly" value="${contract.fileUrl}" class="valid-control"><font color="red">&nbsp;*&nbsp;</font>
							</div>
							<a href='javascript:void(0);' id="download"><div id="fileDel">${contract.fileName}</div></a>
						</td>
					</tr>
					<tr>
						<td width="15%" align="right" class="skinLayout_info">
							<msg:message code="contract.description"/><msg:message code="system.common.sign.colon"/>
						</td>
						<td height="40%" width="70%" style="padding-left:6px;padding-right:6px;" >
							<form:textarea path="contractDescription" cssClass="valid-control" cols="30" maxlength="100" rows="3" onkeypress="return imposeMaxLength(this, 100)"  cssStyle="width: 250px; height: 60px;"/>
						</td>
					</tr>
					
				</table>
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		 <div class="toolbar">
		<div class="left"></div>
			<div class="center">
			<input type="button" class="btn2" name="btnok" 
			value="<msg:message code="button.save"/>" id="btnok"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
			</div>
			<div class="right"></div>
		</div>
		<tr>
			<td></td>
		</tr>
	</table>
	</form:form>
</body>
</html>
