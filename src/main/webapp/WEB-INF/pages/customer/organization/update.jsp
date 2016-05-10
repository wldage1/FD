<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript">
    $(document).ready(function(){
		if("${memberOrganization.licenceImage}" != ""){
			$("#fileDel").html("<img height='100' width='100' src='${ftpHttpUrl}${memberOrganization.licenceImage}'>");
		}
		$("#uploadify").uploadify({
			//指定uploadify.swf路径
	    	'swf'            : '${base}/common/flash/uploadify/uploadify.swf',
	    	//后台处理的请求的servlet,同时传递参数,由于servlet只能接收一个参数，所以将两个参数合并成一个
	       	'uploader'       : '${base}/customer/organization/uploadfile;jsessionid=<%=session.getId()%>',
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
			        $("#licenceImage").val(data);
		        }else{
		        	window.parent.Boxy.alert("<msg:message code='provider.fileFail'/>", null,{title:"<msg:message code='info.prompt'/>"});
		        }
			}
		});
		
		//验证
		var options = {
				event:'focus',
				ajax:{
					url:'${base}/customer/organization/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						//提交表单
						$("form#memberOrganization").submit();
					}
				}
			};
		$("form #btnok").valid(options);
	});
</script>
</head>
<body class="skinMain">
<form:form method="post" action="save" commandName="memberOrganization">
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" class="skinMain">
	<tr>
		<td>
					<input type="hidden" name="turnCode" value="${customer_organization.code}"/>
					<input type="hidden" name="coverParam" value="1"/>
					<input type="hidden" name="id" id="id" value="${memberOrganization.id}"/>
					<input name="c" type="hidden" value="${memberOrganization.c}">
					<input name="id" type="hidden" value="${memberOrganization.id}">
					<input type="hidden" name="prompt" value="name" />
					<table width="99%" border="0" cellspacing="1" cellpadding="0" >
						<tr>
							<td width="99%">
								<table width="99%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.name"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="name" maxlength='20' class="valid-control"/>
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.shortName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="shortName" maxlength='20' class="valid-control"/>
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.licenceCode"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="licenceCode" maxlength='20' class="valid-control"/>
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.licenceImage"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<div id="fileQueue"></div>
											<input type="file" name="file_upload" id="uploadify" />		
											<div id="fileOperateInfo">
												<a href="javascript:$('#uploadify').uploadify('cancel', '*')">
												<msg:message code="provider.cancelUpload"/></a> | 
												<a href="javascript:$('#uploadify').uploadify('upload', '*')">
												<msg:message code="provider.startUpload"/></a>
												<input type="hidden" name="licenceImage" id="licenceImage" style="width: 0;height:0" readonly="readonly">
												<font color="red">&nbsp;*&nbsp;</font>
											</div>
											<div id="fileDel"></div>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.licenceExpireTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="licenceExpireTime" maxlength='20' readonly='true'  class="valid-control"/>
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.postCode"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="postCode" maxlength='20' class="valid-control"/>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.bankName"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="bankName" maxlength='20' class="valid-control"/>
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.account"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="account" maxlength='20' class="valid-control"/>
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.accountHolder"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="accountHolder" maxlength='20' class="valid-control"/>
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.companyAddress"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="companyAddress" maxlength='30' class="valid-control"/>
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.representative"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="representative" maxlength='20' class="valid-control"/>
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.iDCardNumber"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="iDCardNumber" maxlength='20' class="valid-control"/>
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.iDCardExpireTime"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<input type="text" id="iDCardExpireTime" name="iDCardExpireTime" readonly="readonly" value="${memberOrganization.iDCardExpireTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" class="Wdate">
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									<tr>
										<td width="15%" align="right" class="skinLayout_info">
											<msg:message code="memberOrganization.representativePhone"/><msg:message code="system.common.sign.colon"/>
										</td>
										<td width="85%" class="skinLayout_lable">
											<form:input path="representativePhone" maxlength='20' class="valid-control"/>
											<font color="red">&nbsp;*&nbsp;</font>
										</td>
									</tr>
									
									<tr>
										<td width="15%" align="right">&nbsp;</td>
										<td width="85%" colspan="2" align="left">
											<input type="button" class="btn2" name="btnok" onclick="saveOrganization()" value="保存" id="btnok"/>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="tableMargin"></td>
						</tr>
						<tr>
							<td></td>
						</tr>
					</table>	
		</td>
	</tr>
</table>
</form:form>
</body>
</html>


