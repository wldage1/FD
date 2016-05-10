<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<style>
	.skinLayout_lable input,label {vertical-align:middle;} 
</style>
<script language="JavaScript">
var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};

function filter(treeId, parentNode, childNodes) {
	if (!childNodes) return null;
	if (childNodes.stree){
		childNodes = childNodes.stree;
	}
	return childNodes;
}
function beforeAsync(treeId, treeNode) {
	return treeNode ? treeNode.level < 5 : true;
}

function onClick(e, treeId, treeNode) {
	var id = treeNode.id;
	var name = treeNode.name;
	var code = treeNode.code;
	var path = treeNode.path;
	var level = treeNode.olevel;
	var childNode = treeNode.childNode;
	$("#orgId").attr("value", id);
	$("#orgName").attr("value", name);
	$("#path").attr("value", path);
	$("#level").attr("value", level);
	$("#childNode").attr("value", childNode);
	hideMenu();
}

function showMenu() {
	var cityObj = $("#orgName");
	var cityOffset = $("#orgName").offset();
	$("#menuContent").css({
		left : cityOffset.left + "px",
		top : cityOffset.top + cityObj.outerHeight() + "px"
	}).slideDown("fast");

	$("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
	$("#menuContent").hide();
	$("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
			event.target).parents("#menuContent").length > 0)) {
		hideMenu();
	}
}
var rTree;
var uTree;
$(document).ready(function() {
	$("#type").attr("value", ${user.type});
	// 角色树
	$.fn.zTree.init($("#roleTree"), setting, ${roleList});
	rTree = $.fn.zTree.getZTreeObj("roleTree")
	//rTree.setting.check.chkboxType = {"Y":"ps", "N":"s"};

	// 设置当前已存在的绑定值
	setBindValue();
	
	// 设置岗位选中值
	var pois = "${user.position}".split(',');      
	$.each(pois, function(i,val){   
		$("#position_"+val).attr("checked", true);
	});   
});

function setBindValue(){
	var roles = "";
	var sval = rTree.getCheckedNodes();
	if (!sval || sval.length <= 0){
		
	}else{	
		for(var i=0; i<sval.length; i++){
			if (i ==0){ 
				roles = sval[i].id;
			}else{ 
				roles += "," + sval[i].id;
			}
		} 
	}
	$("#roles").val(roles);
}
$(document).ready(function(){

	//上传文件
	//存储路径
	$("#uploadify").uploadify({
  	'swf'            : '${base}/common/flash/uploadify/uploadify.swf',//指定uploadify.swf路径   
  	//后台处理的请求的servlet,同时传递参数,由于servlet只能接收一个参数，所以将两个参数合并成一个。   
     	'uploader'       : '${base}/storage/storeFromUploadify;jsessionid=<%=session.getId()%>',
      'buttonClass'    : 'btn2',
      'queueID'        : 'fileQueue',
      // 'percentage''speed''all'//队列中显示文件上传进度的方式：all-上传速度+百分比，percentage-百分比，speed-上传速度
      'progressData'	 : 'all',
    	//上传成功后的文件，是否在队列中自动删除
      'removeCompleted' : true,
      'auto'           : false,//是否自动上传，即选择了文件即刻上传。  
      'multi'          : false,//是否允许同时上传多文件，默认false
      'fileTypeDesc'   : "jpg、gif、png、swf文件",               
      'fileTypeExts'   : "*.jpg;*.gif;*.png;*.swf;", //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
      'buttonText'     : "<msg:message code='info.prompt.select'/>",   
		'width'          : 60,
		'height'         : 24,
		'queueSizeLimit' : 1,
		//'uploadLimit'    : 1,
		'fileSizeLimit' : '5MB',
		'onInit': function() {
			var data = $("#sqlid").val();
			var id = $("#id").val();
			var c = $("#c").val();
			if(data.length>0){
			 var showpath = "<div id="+data+"><span>";
		        showpath += "<img src='${base}/storage/get?id="+data+"' width='100px'></span>";
		        showpath += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		        showpath += "<a href='#' id='"+data+"' class='delMongoFile'><msg:message code='button.delete'/></a> | ";
		        showpath += "<a href='#' id='"+data+"' class='downloadMongoFile'><msg:message code='button.view'/></a><br></div>";
		        $(showpath).appendTo($("#fileDel"));

		    	var temp = document.getElementById("uploadify");
		    	var temp1 = document.getElementById("fileOperateInfo");
				temp.style.display="none";
				temp1.style.display="none";
		        $(".delMongoFile").click(function(){
		        	$.ajax({
	    				url:"${base}/system/user/updateHeadByid.json?id="+id+"&c="+c+"",
	    				type:'post',
	    				dataType:'json',
	    				success:function(jsonData,textStatus){
	    					if (textStatus == "success"){
					    		$.delMongoFile("${base}", this.id) ;
					    		$("#"+data).remove();	    							
	    						}
	    					}
	    				}
	    			);
		    		temp.style.display="block";
		    		temp1.style.display="block";
		    	});
		        $(".downloadMongoFile").click(function(){
		    		$.downloadMongoFile("${base}", this.id) ;
		    	}) ;
		    }else{
		    	return false;
		    }
		},
		onUploadSuccess : function (file,data,response) {
	     	if(response==true){
	     		$("#sqlid").val(data);
	     		var fileName = file.name;
		        var showpath = "<div id="+data+" name='" + fileName + "' ><span>";
		        showpath += "<img src='${base}/storage/get?id="+data+"' width='100px'></span>";
		        showpath += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		        showpath += "<a href='#' id='"+data+"' class='delMongoFile'><msg:message code='button.delete'/></a> | ";
		        showpath += "<a href='#' id='"+data+"' class='downloadMongoFile'><msg:message code='button.view'/></a><br></div>";
		        $(showpath).appendTo($("#fileDel"));
		    	var temp = document.getElementById("uploadify");
		    	var temp1 = document.getElementById("fileOperateInfo");
		        temp.style.display ="none";
		        temp1.style.display ="none";
		        $(".delMongoFile").click(function(){
		    		$.delMongoFile("${base}", this.id);
		    		$("#"+data).remove();
		    		$("#sqlid").val(null);
		    		temp.style.display ="block";
		    		temp1.style.display ="block";
		    	}) ;
		        $(".downloadMongoFile").click(function(){
		    		$.downloadMongoFile("${base}", this.id) ;
		    	}) ;
	         }else{
	               window.parent.Boxy.alert("<msg:message code='uploadify.fileFail'/>", null,{title:"<msg:message code='info.prompt'/>"});
	         }
	     }
 	});
	
	var options = {
		event:'focus',	
		ajax:{
			url:'${base}/system/user/valid.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: $("form#user").serialize(), 
			async:false,
			cache:false
		},
		validStatus:function(status){
			//status 返回结果为true 代表验证全部通过
			if (status){
				setBindValue();
				$("form#user").submit();
			}
		}
	};
	 $("form #btnok").valid(options);	
	 var valid = new $.Valid(options);	
});
	
   
</script>
</head>
<body class="skinMain">
<form:form method="post" action="save" commandName="user" name="f">
<input type="hidden" name="turnCode" value="${system_user.code}" />
<input type="hidden" name="coverParam" value="1" />
<input type="hidden" name="id" value="${user.id}" id="id"/>
<input type="hidden" name="c" value="${user.c}" id="c"/>
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="bind" id="roles"/>
<input type="hidden" name="orgstr" id="uorgs"/>
<input type="hidden" name="type" id="type" value="1"/>
<input type="hidden" name="operate" value="2" />
<input type="hidden" name="head" id="sqlid" value="${user.head}"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.act"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="account" value="${user.account}" maxlength="20" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="name" value="${user.name}" maxlength="20" class="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.mobilePhone"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="mobilePhone" maxlength="11" value="${user.mobilePhone }" onkeyup="value=value.replace(/[^\d]/g,'')"/>
						<font color="red">&nbsp;若需要收到短信，此项为必填&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.position"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<input type="checkbox" name="position" value="1" id="position_1" style="width:15px;"/>&nbsp;<label>产品</label>&nbsp;&nbsp;
						<input type="checkbox" name="position" value="2" id="position_2" style="width:15px;"/>&nbsp;<label>销控</label>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>				
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.select.bind"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%">
						<div>
							<ul id="roleTree" class="ztree" style="margin-top:0px;width:40%;height:100px;"></ul>
						</div>
					</td>
				</tr>
				<!--
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.head"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<div id="fileQueue"></div>
						<form:input path="head" type="file" name="file_upload" id="uploadify"/> 
						<p>
							<div id="fileOperateInfo">
								<a href="javascript:$('#uploadify').uploadify('cancel', '*')">
								<msg:message code='uploadify.cancelUpload'/></a> | 
								<a href="javascript:$('#uploadify').uploadify('upload', '*')">
								<msg:message code='uploadify.startUpload'/></a>
							</div>
						</p>
						<div id="fileDel" ></div>
					</td>
				</tr>
				-->
				<tr>
					<td width="15%" align="right">&nbsp;</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.confirm"/>" id="btnok"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn2" onclick="location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
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
</form:form>
</body>
</html>
