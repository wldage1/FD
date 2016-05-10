<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<style>
	.skinLayout_lable input,label {vertical-align:middle;} 
</style>
<script type="text/javascript">
var pleaseSelect = "<msg:message code='please.select'/>";
var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	view: {
		fontCss: setFontCss
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
	return treeNode ? treeNode.level < 10 : true;
}

function onClick(e, treeId, treeNode) {
	var nocheck = treeNode.nocheck;
	if(nocheck == false){
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
		showUorgs(id);
		hideMenu();
	}
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

function setFontCss(treeId, treeNode) {
	return treeNode.nocheck == true ? {color:"gray"} : {};
};

var oTree;
var rTree;
var uTree;
$(document).ready(function() {
	
	if($("#orgId").val() == 'null'){
		$("#orgId").val(null);
		$("#roletreetr").hide();
	}else{
		showUorgs($("#orgId").val());
	}
	
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
		'queueSizeLimit' : 1,   //队列中允许的最大上传数目
	//	'uploadLimit'    : 1,    //用于同时上传的数目
		'onInit'      	 : function() {
			
		},
		onUploadSuccess : function (file,data,response) {
			if(response == true){
		        var fileName = file.name;
		        $("#sqlid").val(data);
		        var showpath = "<div id="+data+" name='" + fileName + "' ><span>";
		        showpath +=  "<img src='${base}/storage/get?id="+data+"' width='100px'></span>";
		        showpath += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		        showpath += "<a href='#' id='"+data+"' class='delMongoFile'><msg:message code='button.delete'/></a> | ";
		        showpath += "<a href='#' id='"+data+"' class='downloadMongoFile'><msg:message code='button.view'/></a><br></div>";
		        $(showpath).appendTo($("#fileDel"));
		    	var temp = document.getElementById("uploadify");
		    	var temp1 = document.getElementById("fileOperateInfo");
		        temp.style.display="none";
		        temp1.style.display="none";
		        $(".delMongoFile").click(function(){
		    		$.delMongoFile("${base}", this.id) ;
		    		$("#" + data).remove();
		    		temp.style.display="block";
		    		temp1.style.display="block";
		    	}) ;
		        $(".downloadMongoFile").click(function(){
		    		$.downloadMongoFile("${base}", this.id);
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

				$("form#user").submit();
			}
		}
	};
	$("form #btnok").valid(options);	
	var valid = new $.Valid(options);
		
});
function showUorgs(orgId){
	// 角色树
	var rurl = "roletree.json?orgId="+orgId+"&isCommission=1";
	var rst = $.extend( false,{},setting,{async: {enable: true,url:rurl,dataType:"json",dataFilter: filter}});
	$.fn.zTree.init($("#roleTree"), rst);
	rTree = $.fn.zTree.getZTreeObj("roleTree");
	rTree.setting.check.chkboxType = {"Y":"ps", "N":"s"};
	$("#roletreetr").show();
		
}
</script>
</head>
<body class="skinMain">
<div id="menuContent" class="menuContent" style="display: none; position: absolute;">
	<ul id="orgTree" class="ztree" style="margin-top: 0; width: 240px;"></ul>
</div>
<form:form method="post" action="save" commandName="user">
<form:hidden path="c"/>
<form:hidden path="orgId" value="${user.orgId }"/>
<input type="hidden" name="turnCode" value="${cooperate_user.code}" />
<input type="hidden" name="coverParam" value="1" />
<input type="hidden" name="bind" id="roles"/>
<input type="hidden" name="type" id="type" value="1"/>
<input type="hidden" name="prompt" value="name" />
<input type="hidden" name="head" value="" id="sqlid" />
<input type="hidden" name="operate" value="1" />
<input type="hidden" name="gender" value="1" />
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.act"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="account" maxlength="20" cssClass="valid-control"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.name"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="name" maxlength="20" cssClass="valid-control" />
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.pwd"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:password path="newPassword" maxlength="25" cssClass="valid-control" />
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.confirmpwd"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:password path="confirmPwd" maxlength="25" cssClass="valid-control" />
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.mobilePhone"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<form:input path="mobilePhone" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')"/>
						<font color="red">&nbsp;若需要收到短信，此项为必填&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.position"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<input type="checkbox" name="position" value="1" style="width:15px;"/>&nbsp;<label>产品</label>&nbsp;&nbsp;
						<input type="checkbox" name="position" value="2" style="width:15px;"/>&nbsp;<label>销控</label>
						<font color="red">&nbsp;&nbsp;</font>
					</td>
				</tr>					
				<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="commission.info" /><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
					${orgName}
					</td>
				</tr>
					
				<tr id="roletreetr">
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.select.bind"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="70%">
						<div>
							<ul id="roleTree" class="ztree" style="margin-top:0px;width:40%;height:100px;"></ul>
						</div>
					</td>
				</tr>							
				<!--<tr>
					<td width="15%" align="right" class="skinLayout_info">
						<msg:message code="user.head"/><msg:message code="system.common.sign.colon"/>
					</td>
					<td width="85%" class="skinLayout_lable">
						<div id="fileQueue"></div>
						<form:input path="head" type="file" name="file_Path" id="uploadify"/>
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
				</tr>-->					
				<tr>
					<td width="15%" align="right">&nbsp;${errors}</td>
					<td width="85%" colspan="2" align="left">
						<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok" />
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
</body>
</html>