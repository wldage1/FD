<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<script	type="text/javascript">
$(window).resize(function(){ 　　
	$("#websitesettingList").setGridWidth($(window).width()*0.999);　
});
var count = 0;
var currcount = 0;
var global = "";
var tagNum = 1;
$(document).ready(function(){
	//Logo1上传
	//存储路径
	$("#uploadify").uploadify({
		'swf'            : '${base}/common/flash/uploadify/uploadify.swf',//指定uploadify.swf路径   
		//后台处理的请求的servlet,同时传递参数,由于servlet只能接收一个参数，所以将两个参数合并成一个。   
		'uploader'       : '${base}/config/websitesetting/uploadfile;jsessionid=<%=session.getId()%>',
		'buttonClass'    : 'btn2',
		'queueID'        : 'fileQueue',
		//'percentage''speed''all'//队列中显示文件上传进度的方式：all-上传速度+百分比，percentage-百分比，speed-上传速度
		'progressData'	 : 'all',
		//上传成功后的文件，是否在队列中自动删除
		'removeCompleted' : true,
		'auto'           : false,//是否自动上传，即选择了文件即刻上传。  
		'multi'          : false,//是否允许同时上传多文件，默认false
		'fileTypeDesc'   : "jpg文件",               
		'fileTypeExts'   : "*.jpg;*.png;*.bmp;*.gif;", //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
		'buttonText'     : "<msg:message code='info.prompt.select'/>",   
		'width'          : 60,
		'height'         : 24,
		'queueSizeLimit' : 1,   //队列中允许的最大上传数目
		//'uploadLimit'    : 1,    //用于同时上传的数目
		'onInit'      	 : function() {
			$("#fileDel").html("<img height='100' src='${ftpUrl}${logo.value}'>&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' class='delMongoFile'><msg:message code='button.delete'/></a>");
		},
		onUploadSuccess : function (file,data,response) {
			if(response == true){
		        var fileName = file.name;
		        $("#fileDel").html('');
		        var showpath = "<div id='logo' name='" + fileName + "' ><span>";
		        showpath +=  "<img src='${ftpUrl}" + data + "' width='100px'></span>";
		        showpath += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		        showpath += "<a href='#' class='delMongoFile'><msg:message code='button.delete'/></a>";
		        $(showpath).appendTo($("#fileDel"));
		        $.ajax({
		    		url:'websitesetting/saveLogo.json',
		    		type:'post',
		    		timeout:'60000',
		    		dataType:'json',
					data:{id:${logo.id},c:${c},code:fileName}, 
		    		success:function(jsonData){
		    			if (jsonData.status == "success"){
		    				//alert("success")
		    			}
		    		}
		        })
	         }else{
	              alert("网络异常！")
	         }
	     }
	});
	$(".delMongoFile").live("click",function(){
		$("#fileDel").html('');
		$("#logo").remove();
	});
	
	//Logo2上传
	//存储路径
	$("#uploadify2").uploadify({
		'swf'            : '${base}/common/flash/uploadify/uploadify.swf',//指定uploadify.swf路径   
		//后台处理的请求的servlet,同时传递参数,由于servlet只能接收一个参数，所以将两个参数合并成一个。   
		'uploader'       : '${base}/config/websitesetting/uploadfile;jsessionid=<%=session.getId()%>',
		'buttonClass'    : 'btn2',
		'queueID'        : 'fileQueue2',
		//'percentage''speed''all'//队列中显示文件上传进度的方式：all-上传速度+百分比，percentage-百分比，speed-上传速度
		'progressData'	 : 'all',
		//上传成功后的文件，是否在队列中自动删除
		'removeCompleted' : true,
		'auto'           : false,//是否自动上传，即选择了文件即刻上传。  
		'multi'          : false,//是否允许同时上传多文件，默认false
		'fileTypeDesc'   : "jpg文件",               
		'fileTypeExts'   : "*.jpg;*.png;*.bmp;*.gif;", //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
		'buttonText'     : "<msg:message code='info.prompt.select'/>",   
		'width'          : 60,
		'height'         : 24,
		'queueSizeLimit' : 1,   //队列中允许的最大上传数目
		//'uploadLimit'    : 1,    //用于同时上传的数目
		'onInit'      	 : function() {
			$("#fileDel2").html("<img height='100' src='${ftpUrl}${Logo2.value}'>&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' class='delMongoFile2'><msg:message code='button.delete'/></a>");
		},
		onUploadSuccess : function (file,data,response) {
 			if(response == true){
 		        var fileName2 = file.name;
 		        $("#fileDel2").html('');
 		        var showpath = "<div id='logo2' name='" + fileName2 + "' ><span>";
 		        showpath +=  "<img src='${ftpUrl}" + data + "' width='100px'></span>";
 		        showpath += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
 		        showpath += "<a href='#' class='delMongoFile2'><msg:message code='button.delete'/></a>";
 		        $(showpath).appendTo($("#fileDel2"));
 		        $.ajax({
		    		url:'websitesetting/saveLogo.json',
		    		type:'post',
		    		timeout:'60000',
		    		dataType:'json',
					data:{id:${Logo2.id},c:${c},code:fileName2}, 
		    		success:function(jsonData){
		    			if (jsonData.status == "success"){
		    				//alert("success")
		    			}
		    		}
		        })
 	         }else{
 	              alert("cuowu!")
 	         }
 	     }
    });
    $(".delMongoFile2").live("click",function(){
	   	$("#fileDel2").html('');
	    $("#logo2").remove();
  	});

	jQuery("#websitesettingList").jqGrid({
		    url: 'websitesetting/grid.json',
	        datatype: 'json',
	        colNames: ["<msg:message code='websiteSetting.name'/>",
	                   "<msg:message code='websiteSetting.value'/>",
	                   "<msg:message code='info.operate'/>",
	                   ],
	        colModel: [
	                   {name:'name',index:'name',width:'20%',align:'center',hidden: false,sortable:false},
					   {name:'value',index:'value',width:'67%',align:'center',hidden: false,sortable:false,editable:true},
	                   {name:'act',index:'act', width:'13%',align:'center',sortable:false},
	                   ],
	        mtype:"POST",
	        postData:{c:"${c}"},
	        rowNum:100,     
	        pager: false,
	        height:'400',
	        autowidth: true,
	        viewrecords: true,
	        multiselect: false,
	        jsonReader: {
        		repeatitems: false
        	},
	        gridComplete: function(){
	    		var ids = jQuery("#websitesettingList").jqGrid('getDataIDs');
	    		for(var i=0;i < ids.length;i++){
	    			var id = ids[i];
	    			count = id;
	    			var rowdata = jQuery("#websitesettingList").jqGrid('getRowData',id);
	    			var content = "";
	    			<c:if test="${!empty config_websitesetting_modify}">
	    		    <security:authorize ifAnyGranted="${config_websitesetting_modify.code}">	
	    			content += "<a href='javascript:void(0);'  id='shortcut_config_websitesetting_modify_" +id+ "'sid='"+id+"' style='color:#53056A' class='shortcut_config_websitesetting_modify' ";
	    			content += " title='${config_websitesetting_modify.name}'>";
	    			content += "<img src='${base}/common/skin/images/icon/password.png' weight='18' height='18' border='0' align='absmiddle'/>${config_websitesetting_modify.name}";
	    			content += "</a>";
	    		    </security:authorize>
	    		    </c:if>
	    			<c:if test="${!empty config_websitesetting_save}">
	    		    <security:authorize ifAnyGranted="${config_websitesetting_save.code}">	
	    			content += "<a href='javascript:void(0);'  id='shortcut_config_websitesetting_save_" +id+ "'sid='"+id+"' style='color:#999;' class='' ";
	    			content += " title='${config_websitesetting_save.name}'>";
	    			content += "<img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle' method/>${config_websitesetting_save.name}";
	    			content += "</a>";
	    		    </security:authorize>
	    		    </c:if>  
	    			<c:if test="${!empty config_websitesetting_cancel}">
	    		    <security:authorize ifAnyGranted="${config_websitesetting_cancel.code}">	
	    			content += "<a href='javascript:void(0);' id='shortcut_config_websitesetting_cancel_" + id + "' sid='"+id+"' style='color:#999;' class=''";
	    			content += " title='${config_websitesetting_cancel.name}' >";
	    			content += "<img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${config_websitesetting_cancel.name}";
	    			content += "</a>";
	    		    </security:authorize>
	    		    </c:if>
	    		    
	    		    //添加富文本
	    		    if(id==${aboutUs_id}||id==${contactUs_id}||id==${declare_id}||id==${pmrs_id}){
		    		    var temp = $("#"+id).find("td").eq(1);
			    		var value = "<div id='text_" + id + "'>" + temp.html() + "</div><div id='ke_" + id + "' style='display:none;'><textarea id='keValue_" + id + "' name='kindeditor' style='height:300px;width:750px;'>" + temp.html() + "</textarea></div>"
		    		    jQuery("#websitesettingList").jqGrid('setRowData',ids[i],{value:value, act:"<div class='jqgridContainer'>" + content + "</div>"});
	    		    }else {
		    		    jQuery("#websitesettingList").jqGrid('setRowData',ids[i],{act:"<div class='jqgridContainer'>" + content + "</div>"});
	    		    }
	    		}
	    		
	    		//富文本
	    		var editor = "";
	    		KindEditor.ready(function(K) {
	    			editor = K.create('textarea[name="kindeditor"]',{
	    				uploadJson : '${base}/config/websitesetting/kindeditor/uploadimg',
	    				fileManagerJson : '${base}/config/websitesetting/kindeditor/uploadimg',
	    				allowFileManager: true,
	    				afterBlur: function(){this.sync();}
	    			});
	    			editor.sync();
	    		});
	    		
				$("#"+${status_id}).find("td").eq(0).append("<lable style='text-align:center;color:red'>(1：正常  0：关闭或系统维护)</lable>");
	    		global = jQuery("#websitesettingList").jqGrid('getRowData',${smtpPwd_id}).value;
	    		var $global_td = $("#"+${smtpPwd_id}).find("td").eq(1);
	    		if(global != ""){
					$global_td.html("●●●●●●");
	    		}
	    	},        
	    	caption:'<msg:message code="websiteSetting.list"/>',
	        toolbar: [true,"top"]
	});
	
	//修改按钮
    $(".shortcut_config_websitesetting_modify").live("click",(function(){
		if(currcount == 0){
        	var rowid = $(this).attr("sid");
			currcount = 1;
			$(this).attr("class","").css("color","#999");
			$("#shortcut_config_websitesetting_save_"+rowid).attr("class","shortcut_config_websitesetting_save").css("color","#53056A");
			$("#shortcut_config_websitesetting_cancel_"+rowid).attr("class","shortcut_config_websitesetting_cancel").css("color","#53056A");
			if(rowid==${aboutUs_id}||rowid==${contactUs_id}||rowid==${declare_id}||rowid==${pmrs_id}){
				$("#text_" + rowid).hide();
				$("#ke_" + rowid).show();
			}else if(rowid == ${smtpPwd_id}){
				var $status_td = $("#"+${smtpPwd_id}).find("td").eq(1);
		    	var password = "<input type='password' id='"+${smtpPwd_id}+"_value' class='editable' name='value' style='width:98%'  value='"+global+"'/>";
		    	$status_td.html(password);
			}else{
				jQuery("#websitesettingList").jqGrid('editRow',rowid);
			}
		}else{
			var index = top.$.layer({
		  	    shade : [0.5 , '#000' , true],
		  	    area : ['auto','auto'],
		  	  	offset : ['50px',''],
		  	    dialog : {
		  	        msg:"请先保存当前编辑的条目",
		  	        btns : 1, 
		  	        type : 4,
		  	        btn : ["确定"],
		  	        yes : function(){
		  	        	top.layer.close(index);
		  	        },
		  	        no : function(){
		  	        	top.layer.close(index);
		  	        }
		  	    }});
		}
		var rowdata = jQuery("#websitesettingList").jqGrid('getRowData',rowid);
		
    }));
    
	//重置按钮
    $(".shortcut_config_websitesetting_cancel").live("click",( function() {
		currcount = 0;
		var rowid = $(this).attr("sid");
		if(rowid==${aboutUs_id}||rowid==${contactUs_id}||rowid==${declare_id}||rowid==${pmrs_id}){
			$("#ke_" + rowid).hide();
			$("#text_" + rowid).show();
		}else if(rowid==${smtpPwd_id}){
			var $status_td = $("#"+${smtpPwd_id}).find("td").eq(1);
	    	if(global != ""){
				$status_td.html("●●●●●●");
    		}else{
				$status_td.html("");
    		}
		}else{
			jQuery("#websitesettingList").jqGrid('restoreRow',rowid);
		}
		$(this).attr("class","").css("color","#999");	
		$("#shortcut_config_websitesetting_save_"+rowid).attr("class","shortcut_config_websitesetting_save").css("color","#999");	
		$("#shortcut_config_websitesetting_modify_"+rowid).attr("class","shortcut_config_websitesetting_modify").css("color","#53056A");
	}));
	
	//保存按钮
    $(".shortcut_config_websitesetting_save").live("click",(function(){
		var data="";
		var celcalue = "";
		var method = $(this).attr("method");
		var rowid = $(this).attr("sid");
		var zm = $("<div></div>").text($("#"+rowid+"_value").val()).html();
		if(rowid==${aboutUs_id}||rowid==${contactUs_id}||rowid==${declare_id}||rowid==${pmrs_id}){
			zm=$("#keValue_"+rowid).val();
		}
		data = "c=${c}&value="+encodeURIComponent(zm)+"&id="+rowid;
		$.ajax({
    		url:'websitesetting/save.json',
    		type:'post',
    		timeout:'60000',
    		dataType:'json',
			data: data, 
    		success:function(jsonData){
    			if (jsonData.status == "success"){
    				if(tagNum==2){
    					currcount = 0;
    					jQuery("#websitesettingList").jqGrid('setRowData',rowid,{value:$("#"+rowid+"_value").val()});
    					$("#"+rowid).attr("editable","0");
    					$("#shortcut_config_websitesetting_save_"+rowid).attr("class","").css("color","#999");
    					$("#shortcut_config_websitesetting_cancel_"+rowid).attr("class","").css("color","#999");
    					$("#shortcut_config_websitesetting_modify_"+rowid).attr("class","shortcut_config_websitesetting_modify").css("color","#53056A");
    					jQuery("#websitesettingList").trigger("reloadGrid");
    				}else{
						window.location.reload();
    				}
    			}else{
    				var index = $.layer({
    			  	    shade : [0.5 , '#000' , true],
    			  	    area : ['auto','auto'],
    			  	  	offset : ['50px',''],
    			  	    dialog : {
    			  	        msg:"参数值异常",
    			  	        btns : 1, 
    			  	        type : 4,
    			  	        btn : ["确定"],
    			  	        yes : function(){
    			  	        	layer.close(index);
    			  	        },
    			  	        no : function(){
    			  	        	layer.close(index);
    			  	        }
    			  	    }});
    				}
				}
				
    	});
	}));
    
})

function tab(tag){
	currcount = 0;
	if(tag == 1){
		window.location.reload();
	}else if(tag == 2){
		tagNum = 2;
		$("#tab_2").addClass("selected");
		$("#tab_1").removeClass("selected");
		document.getElementById("logoShow").style.display="none";
		$("#logoShow").css("display","none");
		jQuery("#websitesettingList").jqGrid("setGridParam",{url:"websitesetting/SMTPgrid.json"});
		jQuery("#websitesettingList").trigger("reloadGrid");
	}
}
</script>
</head>
<body>
<input type="hidden" name="c" id="c" value="${c}" />
<ul id="tabnav">
	<li class="tab_1" ><a href="#" id="tab_1" onclick="tab(1)" class="selected"><msg:message code="websiteSetting.baseInfo"/></a></li>
	<li class="tab_2" ><a href="#" id="tab_2" onclick="tab(2)" ><msg:message code="websiteSetting.SMTP" /></a></li>
</ul>
<div id="div_1" class="tab-div-show">
	<table style="width:98%" border="0">
		<tr>
			<td valign="top" width="80%" colspan ="4">
				<table id="websitesettingList"><tr><td>&nbsp;</td></tr></table>	
				<div id="pagered"></div>
			</td>
		</tr>
		<tr>
			<td class="tableMargin" colspan="4"></td>
		</tr>
		<tr>
			<table align="center" width="98%" border="" cellspacing="1" cellpadding="0" class="skinLayout"  id="logoShow">
				<tr>
					<td width="10%" align="right" class="skinLayout_info">${logo.name}</td>
						<td width="40%" class="skinLayout_lable">
							<div id="fileQueue"></div>
								<input type="file" name="file_Path" id="uploadify"/>
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
					<td width="10%" align="right" class="skinLayout_info">${Logo2.name}</td>
					<td width="40%" class="skinLayout_lable">
						<div id="fileQueue2"></div>
								<input type="file" name="file_Path" id="uploadify2"/>
								<p>
									<div id="fileInfo">
										<a href="javascript:$('#uploadify2').uploadify('cancel', '*')">
										<msg:message code='uploadify.cancelUpload'/></a> | 
										<a href="javascript:$('#uploadify2').uploadify('upload', '*')">
										<msg:message code='uploadify.startUpload'/></a>
									</div>
								</p>
							<div id="fileDel2" ></div>
					</td>
				</tr>
			</table>
		</tr>
		
	</table>
</div>
</body>
</html>