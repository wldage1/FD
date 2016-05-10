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
		if("${provider.logo}" != ""){
			$("#fileDel").html("<img height='100' width='100' src='${ftpHttpUrl}${provider.logo}'>");
			$("#logo").val("${provider.logo}");
		}
		$("#uploadify").uploadify({
			//指定uploadify.swf路径
	    	'swf'            : '${base}/common/flash/uploadify/uploadify.swf',
	    	//后台处理的请求的servlet,同时传递参数,由于servlet只能接收一个参数，所以将两个参数合并成一个
	       	'uploader'       : '${base}/cooperate/provider/uploadfile;jsessionid=<%=session.getId()%>',
	       	'formData'		 : {'id':'${provider.id}'},
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
	        'fileTypeExts'   : "<msg:message code='provider.fileTypeExts'/>",
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
							this.queueData.errorMsg = '<msg:message code='provider.uploadOne'/>';
						}
					}
				}
	        },
			'onUploadSuccess'  : function (file,data,response) {
				if(response==true){
			        $("#fileDel").html("<img height='100' width='100' src='${ftpHttpUrl}" + data + "'>");
			        $("#logo").val(data);
		        }else{
		        	window.parent.Boxy.alert("<msg:message code='provider.fileFail'/>", null,{title:"<msg:message code='info.prompt'/>"});
		        }
			}
		});
		
		//验证
		var options = {
				event:'focus',
				ajax:{
					url:'${base}/cooperate/provider/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						//提交表单
						$("form#provider").submit();
					}
				}
			};
		$("form #btnok").valid(options);
	});
	
	//编辑器
	var editor = "";
	KindEditor.ready(function(K) {
		editor = K.create('.kindEditor',{
			uploadJson : '${base}/cooperate/provider/kindeditor/uploadimg',
			afterBlur: function(){this.sync();}
		});
		editor.sync();
	});
</script>
	</head>
	<body class="skinMain">
		<form:form action="save" method="post" commandName="provider">
			<input type="hidden" name="turnCode" value="${cooperate_provider.code}" />
			<input type="hidden" name="coverParam" value="1" />
			<input name="c" type="hidden" value="${provider.c}">
			<input name="id" type="hidden" value="${provider.id}">
			<input name="status" type="hidden" value="${provider.status}">
			<input type="hidden" name="prompt" value="name" />
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provider.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="name" type="text" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provider.short.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="shortName" type="text" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provider.companyType"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:select path="companyType">
							<c:forEach items="${companyType}" var="item">
								<c:if test="${companyType != null}">
									<c:choose>
										<c:when test="${provider.companyType == item.key}">
											<option value="${item.key}" selected="selected">${item.value}</option>
										</c:when>
										<c:otherwise>
											<option value="${item.key}">${item.value}</option>
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provider.createDate"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<input type="text" id="createDate" name="createDate" value="${provider.createDate}">
					</td>
				</tr>
				<tr>
					<td  width="15%" align="right" class="skinLayout_info">
						<msg:message code="provider.logo"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<div id="fileQueue"></div>
						<input type="file" name="file_upload" id="uploadify" />		
						<div id="fileOperateInfo">
							<a href="javascript:$('#uploadify').uploadify('cancel', '*')">
							<msg:message code="provider.cancelUpload"/></a> | 
							<a href="javascript:$('#uploadify').uploadify('upload', '*')">
							<msg:message code="provider.startUpload"/></a>
							<input type="text" name="logo" id="logo" style="width: 0;height: 0" readonly="readonly" class="valid-control">
							<font color="red">&nbsp;*&nbsp;<msg:message code="provider.pic.size"/></font>
						</div>
						<div id="fileDel"></div>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provider.substantialShareholder"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:textarea path="substantialShareholder" maxlength="100" cols="30" rows="3" onchange="this.value=$.trim(this.value)" cssStyle="width: 250px; height: 60px;"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provider.registeredCapital"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:textarea path="registeredCapital" maxlength="50" cols="30" rows="3" onchange="this.value=$.trim(this.value)" cssStyle="width: 250px; height: 60px;"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provider.latestAssetSize"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:textarea path="latestAssetSize" maxlength="150" cols="30" rows="3" onchange="this.value=$.trim(this.value)" cssStyle="width: 250px; height: 60px;"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provider.companiesRegistryAddress"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:textarea path="companiesRegistryAddress" maxlength="150" cols="30" rows="3" onchange="this.value=$.trim(this.value)" cssStyle="width: 250px; height: 60px;"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provider.awards"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:textarea path="awards" cssClass="kindEditor" cssStyle="width: 100%; height: 300px;"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="provider.discription"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:textarea path="discription" cssClass="kindEditor" cssStyle="width: 100%; height: 300px;"/>
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