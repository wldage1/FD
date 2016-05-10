<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
<script type="text/javascript">
	$(document).ready(function(){
		$("#uploadify").uploadify({
			//指定uploadify.swf路径
	    	'swf'            : '${base}/common/flash/uploadify/uploadify.swf',
	    	//后台处理的请求的servlet,同时传递参数,由于servlet只能接收一个参数，所以将两个参数合并成一个
	       	'uploader'       : '${base}/market/order/uploadProof/uploadfile;jsessionid=<%=session.getId()%>',
	       	//按钮样式
	       	'buttonClass'    : 'btn2',
	       	//选择文件的容器
	       	'queueID'        : 'fileQueue',
	       	//上传表单名
	       	'file_post_name' : 'fileName',
	       	//自定义参数
	       	'formData'		 : {'orderNumber':'${orderProof.orderNumber}'},
	     	//队列中显示文件上传进度的方式：all-上传速度+百分比，percentage-百分比，speed-上传速度
	        'progressData'	 : 'all',
	      	//上传成功后的文件，是否在队列中自动删除
	        'removeCompleted' : true,
	        //是否自动上传，即选择了文件即刻上传
	        'auto'           : false,
	        //是否允许同时上传多文件，默认false
	        'multi'          : false,
	        //说明
	        'fileTypeDesc'   : "<msg:message code='orderProof.fileTypeDesc'/>",            
	        'fileTypeExts'   : "<msg:message code='orderProof.fileTypeExts'/>",
	        'buttonText'     : "<msg:message code='info.prompt.select'/>",
	        'width'          : 60,
			'height'         : 24,
			'fileSizeLimit'  : '5MB',
			'onUploadSuccess'  : function (file,data,response) {
				if(response==true){
			        $("#proofFileUrl").val(data);
			        $("#fileDel").html(file.name);
		        }else{
		        	window.parent.Boxy.alert("<msg:message code='information.fileFail'/>", null,{title:"<msg:message code='info.prompt'/>"});
		        }
			}
		});
		
		//验证
		var options = {
				event:'focus',
				ajax:{
					url:'${base}/market/order/uploadProof/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						//提交表单
						$("form#orderProof").submit();
					}
				}
			};
		$("form #btnok").valid(options);
	});
</script>

<style type="text/css">
	.skinLayout_lable textarea{
		width:250px;
		height:160px;
		border:1px solid #A6C9E2;
		font-size:14px;
	}
</style>
	</head>
	<body class="skinMain">
		<form:form action="saveProof" method="post" commandName="orderProof">
			<input name="c" type="hidden" value="${orderProof.c}">
			<input type="hidden" name="turnCode" value="${market_order_detail_fundConfirm.code}"/>
			<input type="hidden" name="coverParam" value="1" />
			<input type="hidden" name="prompt" value="orderNumber" />
			<input name="orderNumber" type="hidden" value="${orderProof.orderNumber}">
			<input name="productID" type="hidden" value="${orderProof.productID}">
			<input name="memberID" type="hidden" value="${orderProof.memberID}">
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="order.proof.amount"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="proofAmount" type="text" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="order.proof.proofTime"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<input name="proofTime" id="proofTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:250px;" readonly="readonly" class="Wdate valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td  width="15%" align="right" class="skinLayout_info">
						<msg:message code="order.proof.files"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<div id="fileQueue"></div>
						<input type="file" name="file_upload" id="uploadify" />		
						<div id="fileOperateInfo">
							<a href="javascript:$('#uploadify').uploadify('cancel', '*')">
							<msg:message code="information.cancelUpload"/></a> | 
							<a href="javascript:$('#uploadify').uploadify('upload', '*')">
							<msg:message code="information.startUpload"/></a>
							<input type="text" name="proofFileUrl" id="proofFileUrl" style="width: 0;height: 0" readonly="readonly" class="valid-control">
							<font color="red">&nbsp;*&nbsp;</font>
						</div>
						<div id="fileDel"></div>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="orderProof.remark"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:textarea path="proofRemark"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="window.location.href='${base}${market_order_detail_fundConfirm.controller}?c=${market_order_detail_fundConfirm.code}&orderNumber=${orderProof.orderNumber}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
					</td>
				</tr>
			</table>
		</form:form>
	</body>
</html>