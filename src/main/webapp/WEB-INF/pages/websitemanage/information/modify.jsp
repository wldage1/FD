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
		if("${information.titleImage}" != ""){
			var showpath = "<div id='inforTitleImage'><img height='100' weight='100' src='${ftpHttpUrl}${information.titleImage}'>";
			showpath += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			showpath += "<a href='#' class='uploadAct' onclick=\"del('inforTitleImage')\"><msg:message code='titleImage.delete'/></a></div>";
	        $("#fileDel").html(showpath);
		}
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
					var showpath = "<div id='inforTitleImage'><img height='100' weight='100' src='${ftpHttpUrl}" + data + "'>";
					showpath += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					showpath += "<a href='#' class='uploadAct' onclick=\"del('inforTitleImage')\"><msg:message code='titleImage.delete'/></a></div>";
			        $("#fileDel").html(showpath);
			        $("#titleImage").val(data);
		        }else{
		        	window.parent.Boxy.alert("<msg:message code='information.fileFail'/>", null,{title:"<msg:message code='info.prompt'/>"});
		        }
			}
		});
		
		/* if($("#type").val()!='4'&&$("#type").val()!='5'){
			$("#tr_freed").hide();
			//$("input[name='freed']").attr("disabled","disabled");
		} */
		
		//验证
		var options = {
				event:'focus',
				ajax:{
					url:'${base}/websitemanage/information/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						var content = $("#content").val();
						if(content){
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
	
	//删除上传文件
	function del(inforTitleImage){
		$('#' + inforTitleImage).remove();
		$.post("deleteTitleImage", {id : '${information.id}'});
		$("#titleImage").val('');
	}
	
	/* function freedV(){
		if($("#type").val()!='4'&&$("#type").val()!='5'){
			$("#tr_freed").hide();
			//$("input[name='freed']").attr("disabled","disabled");
		}else{
			$("#tr_freed").show();
			//$("input[name='freed']").removeAttr("disabled");
		}
	} */
	
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

<style type="text/css">
	a.uploadAct:link {color: #000000;}
	a.uploadAct:visited {color: #000000;}
	a.uploadAct:active {color: #000000;}
	a.uploadAct:hover {color: #000000;}
</style>

	</head>
	<body class="skinMain">
		<form:form action="save" method="post" commandName="information">
			<input type="hidden" name="turnCode" value="${websitemanage_information.code}" />
			<input type="hidden" name="coverParam" value="1" />
			<input name="c" type="hidden" value="${information.c}">
			<input type="hidden" name="prompt" value="title" />
			<input name="id" type="hidden" value="${information.id}">
			<input type="hidden" name="titleImage" id="titleImage">
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.type"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<select name="type" id="type">
							<c:forEach items="${informationType}" var="item">
								<c:if test="${informationType != null}">
									<c:choose>
										<c:when test="${item.id == information.type}">
											<option value="${item.id}" selected="selected">${item.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${item.id}">${item.name}</option>
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
						</select>
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
						<form:textarea path="summary" cssStyle="width:500px;height:150px;border:1px solid #A6C9E2;font-size:14px;"/>
					</td>
				</tr>
				<%-- <tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.freed"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%">
						<form:radiobutton path="freed" value="1" label="是"/>
						<form:radiobutton path="freed" value="0" label="否"/>
					</td>
				</tr> --%>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="information.released"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%">
						<form:radiobutton path="released" value="1" label="是"/>
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
						<form:textarea path="content" cssStyle="height:850px;width:100%;" />
					</td>
				</tr>
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="window.location.href='${base}${websitemanage_information.controller}?c=${websitemanage_information.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
					</td>
				</tr>
			</table>
		</form:form>
	</body>
</html>