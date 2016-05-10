<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="shortcut icon" href="">
<title></title>
<link href="http://open.youku.com/assets/lib/bootstrap2.1.0/css/bootstrap.css" rel="stylesheet">
<link href="http://open.youku.com/assets/lib/bootstrap2.1.0/css/bootstrap-responsive.css" rel="stylesheet">
<link href="${base}/common/skin/css/swfupload/swfupload.css" rel="stylesheet" type="text/css" />

<script src="http://open.youku.com/assets/lib/jquery-1.8.1.min.js"></script>
<script src="http://open.youku.com/assets/lib/uploadjs.php"></script>
<script type="text/javascript" src="${base}/common/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${base}/common/js/swfupload/jquery.swfupload.js"></script>

<script>
	var loadProduct = false;
	var isForce = false;
	var vedioTitle;

	var index = parent.layer.getFrameIndex(window.name);
	
    //document.domain = "youku.com";
    var USE_STREAM_UPLOAD = true;

    jQuery(document).ready(function(){
		var param = {client_id:"d9d6c1a24df6919c",access_token:"${accessToken}",oauth_opentype:"currentWindow",oauth_redirect_uri:"http://test.youku.com/youkuupload/upload.html",oauth_state:"",completeCallback:"uploadComplete",categoryCallback:"categoryLoaded"};
		var reg = new RegExp("(^|\\#|&)access_token=([^&]*)(\\s|&|$)", "i");
		if (reg.test(location.href)){
			var access_token = unescape(RegExp.$2.replace(/\+/g, " "));
			param.access_token = access_token;
		}
	
		youkuUploadInit(param);
		
		// 取消
		$("#btn-upload-cancel").click(function(){
			parent.layer.close(index);
		});
		
		// 上传本地
		$("#btn-upload-save").click(function(){
			var status = false;
			
			// SET-1 验证表单
			var title = $("input[name='title']").val();
			var productId = $("#product-node").val();
			
			var fileData = $("input[name='FileData']").val();
			if($.trim(fileData) == ""){
				alert("请选择上传的文件");
			}else if($.trim(title) == ""){
				alert("标题不能为空");
			}else if(loadProduct == true && $.trim(productId) == ""){
				alert("关联产品不能为空");
			}else{
				vedioTitle = title;
				var date = new Date();
				$("input[name='title']").val(date.getTime());
				status = true;
			}
			
			// SET-2 上传到优酷
			// 上传文件到优酷
			if(status){
				$("#btn-upload-start").click();
			}
			// SET-3 上传到本地
			//$("#uploadControl").swfupload('startUpload');
			
		});
		
		/**
		$('#uploadControl').swfupload({
			upload_url: "youku_upload/upload_file?c=${c}",
			file_size_limit : "-1",
			file_types : "*.rmvb;*.mp4;*.3gp,*.flv,*.wmv,*.avi,*.mpeg",
			file_types_description : "All Files",
			file_upload_limit : "0",
			flash_url : "${base}/common/flash/swfupload.swf",
			button_image_url : '${base}/common/skin/images/swfupload/XPButtonUploadText_61x22.png',
			button_width : 61,
			button_height : 22,
			button_placeholder : $('#button')[0],
			debug: false,
			custom_settings : {something : "here"}
		})
		.bind('fileQueued', function(event, file){
			$("#swfupload-box").show();
			$('#swfupload-filename').text(file.name);
			$('#swfupload-operate').show();
			$('#swfupload-info').hide();
		})
		.bind('fileQueueError', function(event, file){
			
		})
		.bind('uploadProgress', function(event, file, bytesLoaded){
			$('#swfupload-operate').hide();
			$('#swfupload-info').show();
			$('#swfupload-info').html('uploading - <font color="red">'+bytesLoaded/1000+'KB</font>');
		})
		.bind('uploadSuccess', function(event, file, serverData){
			var data = JSON.parse(serverData);
			$('#swfupload-info').text('upload success!');
			$("#fileName").val(data['url']);
			isUploaded = true;
		})
		.bind('uploadComplete', function(event, file){
			//$("#swfupload-operate").fadeOut("slow");
		})
		.bind('uploadError', function(event, file, errorCode, message){
			$("#swfupload-box").show();
			$('#swfupload-operate').hide();
			$('#swfupload-info').show();
			$('#swfupload-info').text('upload fail!');
			$("#swfupload-box").fadeOut("slow");
		});
		
		// upload
		$(".upload").click(function (){
			$("#swfupload-control").swfupload('startUpload');
		});
		// cancel
		$(".cancel").click(function(){
			$("#swfupload-box").fadeOut("slow");
		});
		*/
		
		
		
	});
	

	//分类加载后回调方法
	function categoryLoaded(data){
		if(data.categories) {
			var tpl = '';
			for (var i=0; i<data.categories.length; i++) {
				if(data.categories[i].term == 'Ads'){
					tpl += '<option value="' + data.categories[i].term + '" selected>' + data.categories[i].label + '</option>';
				}else{
					tpl += '<option value="' + data.categories[i].term + '" >' + data.categories[i].label + '</option>';
				}
            }
            $("#category-node").html(tpl);
        }
    }
	
	// 加载产品列表
	function getProductList(){
		var id = $("#categoryId").val();
		var isRelate = $("#s-"+id).attr("for");
		var isNeedPwd = $("#s-"+id).attr("class");
		
		if(isRelate == '1'){
			loadProduct = true;
			// 加载关联产品
			jQuery.ajax({
				url:"youku_upload/product.json?c=${c}",
				type: "post",
				dataType : 'json',
				success:function(data){
					productLoaded(data);
				}
			});
			$("#productList").css("display","block");
		}else{
			loadProduct = false;
			$("#product-node").val('');
			$("#productList").css("display","none");
		}
		if(isNeedPwd == '1'){
			isForce = true;
			needPwd(true);
		}else{
			isForce = false;
			needPwd(true);
		}
	}
	
	//产品加载后回调方法
	function productLoaded(data){
		if(data) {
			var tpl = '';
			for (var i=0; i<data.prdList.length; i++) {
				tpl += '<option value="' + data.prdList[i].id + '">' + data.prdList[i].name + '</option>';
            }
            $("#product-node").html(tpl);
        }
    }
	
	//上传完成时回调方法
	function uploadComplete(data){

		$("input[name='title']").val(vedioTitle);
		$("input[name='videoId']").val(data.videoid);
		jQuery.ajax({
			url:"youku_videomanager/save.json",
			type: "post",
			contentType : 'application/x-www-form-urlencoded',
			data:$("#video-upload").serializeArray(), 
			dataType : 'json',
			success:function(data){
				if(data.status == 'success'){
					window.parent.reload();
					parent.layer.msg("文件上传成功", 2, -1);
					parent.layer.close(index);
				}else{
					parent.layer.msg("文件上传失败", 2, -1);
					parent.layer.close(index);
				}
			}
		});
	}
	
	function needPwd(flug){
		if(flug == true){
			$("#pwd").attr("disabled",false);
		}else{
			$("#pwd").attr("disabled",true);
		}
		if(isForce == true){
			$("#public_type1").attr("disabled",true);
			$("#public_type2").attr("disabled",true);
			$("#public_type3").attr("checked",true);
		}else{
			$("#public_type1").attr("disabled",false);
			$("#public_type2").attr("disabled",false);
		}
	}
	
    </script>
</head>
<style>
.btn2{
	width:59px;
	height:24px;
	background: url(../images/main/button_2_4.gif) no-repeat;
	border:0px;
	text-align:center;
	font-size: 12px;
}
</style>
<body>
<div id="youku_upload_box">
	<div id="error"></div>
    <div class="container">
        <form class="well form-horizontal" name="video-upload" id="video-upload" method="post">
			<input type="hidden" class="input-xlarge" name="tags" value="FD">
			<input type="hidden" name="category" value="Others">
			<input type="hidden" name="fileName" id="fileName">
			<input type="hidden" name="videoId" id="videoId">
			<input type="hidden" name="publishOrNot" value="0">
			<fieldset>
				<div class="control-group">
					<label class="control-label" for="spanSWFUploadButton"><font style="font-size:12px;">选择文件：</font></label>
                    <div id="uploadControl" class="controls"></div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="input01"><font style="font-size:12px;">标题：</font></label>
					<div class="controls">
						<input type="text" class="input-xlarge" id="title" name="title" style="font-size:12px;">
					</div>
                </div>
				
				<div class="control-group">
					<label class="control-label"><font style="font-size:12px;">视频分类：</font></label>
					<div class="controls">
						<select name="categoryId" id="categoryId" style="font-size:12px;" onChange="getProductList();">
						<option value="">---请选择---</option>
						<c:forEach items = "${categoryList}" var="category"><option value="${category.id}" id="s-${category.id}" for="${category.isRelate}" class="${category.isPwdControl}">${category.name}</option></c:forEach>
						</select>
                    </div>
				</div>
				
				<div class="control-group" style="display:none" id="productList">
					<label class="control-label"><font style="font-size:12px;">关联产品：</font></label>
					<div class="controls">
						 <select id="product-node" name="productId" style="font-size:12px;"></select>
					</div>
				</div>
				
                <div class="control-group">
					<label class="control-label"><font style="font-size:12px;">版权所有：</font></label>
					<div class="controls">
						<label class="radio inline">
							<input type="radio" name="copyright_type" id="copyright_type2" value="original" checked=""><font style="font-size:12px;">原创</font>
						</label>
						<label class="radio inline">
							<input type="radio" name="copyright_type" id="copyright_type1" value="reproduced"><font style="font-size:12px;">转载</font>
						</label>
					</div>
				</div>
				<div class="control-group">
				   <label class="control-label"><font style="font-size:12px;">视频权限：</font></label>
					  <div class="controls">
							 <label class="radio inline">
							   <input type="radio" name="public_type" id="public_type1" value="all" checked="" onclick="javascript:needPwd(false);"><font style="font-size:12px;">公开</font>
							 </label>
							 <label class="radio inline">
							   <input type="radio" name="public_type" id="public_type2" value="friend" onclick="javascript:needPwd(false);"><font style="font-size:12px;">仅好友</font>
							 </label>
							 <label class="radio inline">
								<input type="radio" name="public_type" id="public_type3" value="password" onclick="javascript:needPwd(true);"><span id="passwrod"></span><font style="font-size:12px;">使用密码观看</font>
								<input type="text" class="input" name="watch_password" id="pwd" style="display:none" disabled="disabled" value="${password}">
							 </label>
							
					 </div>
				</div>

				<!--
				<div class="control-group">
                    <div id="swfupload-box" style="display:none;" class="controls">
						<span id="swfupload-filename"></span>
						&nbsp;&nbsp;
						<span id="swfupload-operate">
							<a href="javascript:void(0);" class="cancel">取消</a>|
							<a href="javascript:void(0);" class="upload">上传</a>
						</span>
						<span id="swfupload-info"></span>
					</div>
				</div>
				-->
				<div class="form-actions">
					<button type="button" class="btn2" id="btn-upload-start" style="display:none"/>
					<button type="button" class="btn2" id="btn-upload-save">
						<span>上传</span>
					</button>
					<button type="button" class="btn2" id="btn-upload-cancel">
						<span>取消</span>
					</button>
				</div>
			</fieldset>
		</form>
		<div class="row" >
			<div class="span5" id="upload-status-wraper" ></div>
		</div>
    </div>
    <!--完成上传的DOM和登录DOM 开始-->
    <div id="complete"></div>
    <div id="login" style="width:100%;height:100%;position:fixed;z-index:999;left:0px;top:0px;overflow:hidden;display:none;">
    </div>
    <!--完成上传的DOM和登录DOM 结束-->
</div>
</body>
</html>