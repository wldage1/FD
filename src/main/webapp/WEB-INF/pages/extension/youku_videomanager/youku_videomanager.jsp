<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#videoList").setGridWidth($(window).width()*0.999);　
});
var categoryID; 
var type = 0;
var currcount = 0;
var video;
var oldSelectValue ;
//#tab_1
$(document).ready(function(){
	//var x = document.getElementById("id");
	//x.selectedIndex = 0;
    $('#id').get(0).selectedIndex = 0;
    categoryID = $("#id").val();
    var select  = "all:全部公开;friend:好友可见;password:加密";
    
    jQuery("#videoList").jqGrid({
        url: 'youku_videomanager/grid.json',
        datatype: 'json',
        colNames: ["标题", 
                   "公开类型",
                   "",
                   "版权",
                   "",
                   "操作"
                   ],
        colModel: [{name: 'title',index:'title',width:'30%',align:'center',sortable:false,editable:false},
                   {name: 'publicType',index:'publicType',width:'10%',align:'center',sortable:false,editable:true,edittype:'select',editoptions:{value:select},formatter:changeTo},
                   {name: 'publicType',index:'publicType',width:'10%',align:'center',sortable:false,hidden:true},
                   {name: 'copyrightType',index:'copyrightType', width:'10%',align:'center',sortable:false,title:false,formatter:changeCo},
        		   {name: 'videoId',index:'videoId',width:'1%',hidden:true},
                   {name:'operate',index:'operate', width:'30%',align:'left',sortable:false,title:false,
                	   formatter:unPublishOperate}],
        postData:{c:"${c}",publishOrNot:type,categoryId:categoryID},
        mtype:"POST",
        height:<msg:message code='jqgrid.height.250'/>,
        pager: false,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        caption:'<msg:message code="videobasic.unpublish.list"/>',
        toolbar: [true,"top"]
	});
	
	var $content = $("<a></a>").attr("href","javascript:void(0)")
		.attr("id","youku_upload")
		.append($("<img/>").attr("src","${base}/common/skin/images/icon/create.png")
		.attr("width","18").attr("height","18").attr("border",0)
		.attr("border","0").attr("align","absmiddle"))
		.append("上传视频");
	
	$("#t_videoList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
	
	var	$content = $("<a></a>").attr("href","javascript:void(0)")
		.attr("id","youku_update_token")
		.append($("<img/>").attr("src","${base}/common/skin/images/icon/modify.png")
		.attr("width","18").attr("height","18").attr("border",0)
		.attr("border","0").attr("align","absmiddle"))
		.append("更新授权码");
	$("#t_videoList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));

	
	jQuery("#videoList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");

	$(".shortcut_extension_youku_videomanager_delete").live("click",(function(){
		var rowid = $(this).attr("sid");
		videoId = $("#videoList").jqGrid("getRowData",rowid).videoId;
		var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['150px',''],
	  	    dialog : {
	  	        msg:"您确定要删除吗？",
	  	        btns : 2, 
	  	        type : 4,
	  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
	  	        yes : function(){
	  	        	$.ajax({
	  	  			url:'youku_videomanager/delete.json',
	  	  			type:'post',
	  	  			timeout:'60000',
	  	  			dataType:'json',
	  	  			data: "id="+rowid, 
	  	  			success:function(jsonData,textStatus){
	  	  				if (textStatus == "success"){
	  	  					if (jsonData.status == "success"){
	  	  						jQuery("#videoList").trigger("reloadGrid");
	  	  						top.layer.close(index);
	  	  					}else{
	  	  						top.$.layer({
	  	  							area : ['auto','auto'],
	  	  							dialog : {msg: "删除失败" ,type : 8}	
	  	  						});
	  	  					}
	  	  				}
	  	  			}
	  	  		})
	  	        },
	  	        no : function(){
	  	        	top.layer.close(index);
	  	        }
	  	    }
	  	});
		
		$.ajax({
	  			url:'https://openapi.youku.com/v2/videos/destroy.json',  
	  			type:'post',
	  			timeout:'60000',
	  			dataType:'json',
	  			data:{client_id:'d9d6c1a24df6919c',video_id:videoId,access_token:"${accessToken}"}, 
	  			
	  		})
	}));
	
	$(".shortcut_extension_youku_videomanager_modify").live("click",(function(){
		if(currcount == 0){
			currcount = 1;
			var rowid = $(this).attr("sid");
			jQuery("#videoList").jqGrid('editRow',rowid);
			var $td = $("#"+rowid).find("td").eq(2);
			$("#"+rowid+"_publicType").val($td.html());
			$(this).removeClass();
			var $ea = $("<a></a>").attr("href","javascript:void(0)")
						.attr("id","shortcut_extension_youku_videomanager_save_"+rowid)
						.attr("sid",rowid)
						.attr("border","0").attr("class","shortcut_extension_youku_videomanager_save")
						.append($("<img/>").attr("src","${base}/${skin}/images/icon/modify.png")
					  	.attr("width","18").attr("height","18").attr("border","0")
					  	.attr("border","0").attr("align","absmiddle"))
					  	.append("${extension_youku_videomanager_save.name}");
			$(this).html($ea);
			//var select  = $("<select><select>").attr("id","publicType").append("<option value='all'>全部公开</option>").append("<option value='friend'>好友可见</option>").append("<option value='password'>加密</option>");
			//var $td = $("#"+rowid).find("td").eq(1);
			//$td.append(select);
			//$("#"+rowid+"_publicType").remove();
		}else{
			top.layer.msg('<msg:message code="dictionaryItem.notsave"/>');
		}
	}));
	
	$(".shortcut_extension_youku_videomanager_save").live("click",(function(){
		var rowid = $(this).attr("sid");
		var typeval = $("#"+rowid+"_publicType").val();
		videoId = $("#videoList").jqGrid("getRowData",rowid).videoId;
			$.ajax({
				url:'youku_videomanager/save.json',
				type:'post',
				timeout:'60000',
				dataType:'json',
				data: {id:rowid,publicType:typeval},
				success:function(jsonData,textStatus){
					if (textStatus == "success"){
					currcount = 0;
						if (jsonData.status == "success"){
							$("#videoList").trigger("reloadGrid");
						}else{
							top.$.layer({
		  							area : ['auto','auto'],
		  							dialog : {msg: "更新失败!" ,type : 8}	
		  						});
						}
					}
				}
			});	
			if(type == 1){
				$.ajax({
					url:'https://openapi.youku.com/v2/videos/update.json',
					type:'post',
					timeout:'60000',
					dataType:'json',
					data: {video_id:videoId,public_type:$("#publicType").val()}
				});
		}
	}));
	
	$(".shortcut_extension_youku_videomanager_publish").live("click",( function() {
		if(currcount == 0){
		var rowid = $(this).attr("sid");
		videoId = $("#videoList").jqGrid("getRowData",rowid).videoId;
		var videoData;
		$.ajax({
			url:'https://openapi.youku.com/v2/videos/show_basic.json',
			type:'get',
			timeout:'60000',
			dataType:'json',
			data: {video_id:videoId,client_id:'d9d6c1a24df6919c'},
			success:function(jsonData,textStatus){
				if (textStatus == "success"){
					showLayer(jsonData);
					if(jsonData.state == "normal"){
						var index = top.$.layer({
					  	    shade : [0.5 , '#000' , true],
					  	    area : ['auto','auto'],
					  	  	offset : ['150px',''],
					  	    dialog : {
					  	        msg:"视频状态为正常,您确定要发布吗？",
					  	        btns : 2, 
					  	        type : 4,
					  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
					  	        yes : function(){
			  					$.ajax({
					  	  			url:'youku_videomanager/updateStatus.json',
					  	  			type:'post',
					  	  			timeout:'60000',
					  	  			dataType:'json',
					  	  			data: {id:rowid,publishOrNot:1},
					  	  			success:function(jsonData,textStatus){
					  	  				if (textStatus == "success"){
					  	  					if (jsonData.status == "success"){
					  	  					top.layer.close(index);
											$("#videoList").trigger("reloadGrid"); 
					  	  					}else{
					  	  						top.$.layer({
					  	  							area : ['auto','auto'],
					  	  							dialog : {msg: "发布失败" ,type : 8}	
					  	  						});
					  	  					}
					  	  				}
					  	  			}
					  	  		});
					  	        },
					  	        no : function(){
					  	        	top.layer.close(index);
					  	        }
					  	    }
					  	});
					}
				}
			}
		});	
		}else{
			top.layer.msg('<msg:message code="dictionaryItem.notsave"/>');
		}
	}));
	
	$(".shortcut_extension_youku_videomanager_cancel").live("click",( function() {
		var rowid = $(this).attr("sid");
		videoId = $("#videoList").jqGrid("getRowData",rowid).videoId;
		var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['150px',''],
	  	    dialog : {
	  	        msg:"您确定要撤销吗?",
	  	        btns : 2, 
	  	        type : 4,
	  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
	  	        yes : function(){
	  	        	$.ajax({
  	  	  	  			url:'youku_videomanager/updateStatus.json',
  	  	  	  			type:'post',
  	  	  	  			timeout:'60000',
  	  	  	  			dataType:'json',
  	  	  	  			data: {id:rowid,publishOrNot:0},
  	  	  	  			success:function(jsonData,textStatus){
  	  	  	  				if (textStatus == "success"){
  	  	  	  					if (jsonData.status == "success"){
  	  	  	  					top.layer.close(index);
  	  							$("#videoList").trigger("reloadGrid"); 
  	  	  	  					}else{
  	  	  	  						top.$.layer({
  	  	  	  							area : ['auto','auto'],
  	  	  	  							dialog : {msg: "撤销失败!" ,type : 8}	
  	  	  	  						});
  	  	  	  					}
  	  	  	  				}
  	  	  	  			}
  	  	  	  		});
	  	        },
	  	        no : function(){
	  	        	top.layer.close(index);
	  	        }
	  	    }
	  	});
        	$.ajax({
	  			url:'https://openapi.youku.com/v2/videos/destroy.json',
	  			type:'post',
	  			timeout:'60000',
	  			dataType:'json',
	  			data: {client_id:'d9d6c1a24df6919c',video_id:videoId,access_token:"${accessToken}"},
  			});
	}));
	
	// 上传
	$("#youku_upload").click(function(){
		$.layer({
			type : 2,
			shade : [0],
			fix: false,
			closeBtn: [0,true],
			title : ' 视频信息',
			iframe : {src : '${base}/extension/youku_upload?c=${extension_youku_videomanager_upload.code}'},
			area : ['535px' , '485px'],
			offset : ['0px', ''],
		});
	});
	// 刷新授权码
	$("#youku_update_token").click(function(){
		$.ajax({  
			type:"POST",  
			url:"https://openapi.youku.com/v2/oauth2/token",
			data:{client_id:"d9d6c1a24df6919c",client_secret:"b2e81cf334d8914c60355d109a5bb63a",grant_type:"refresh_token",refresh_token:"${freshToken}"},
			dataType:"json",  
			success:function(data){
				updateRefreshToken(data);
			}  
		});	
	});
	
});

	//保存刷新的授权码
	function updateRefreshToken(data){
		var access_token = data.access_token;
		var fresh_token = data.refresh_token;
		var codes="accessToken,freshToken";
		var values=""+access_token+","+fresh_token;
		var cdata = {code:codes,value:values};
		jQuery.ajax({
			url:"youku_videomanager/freshToken.json",
			type: "post",
			contentType : 'application/x-www-form-urlencoded',
			data:cdata, 
			dataType : 'json',
			success:function(data){
				if(data.status == 'success'){
					layer.msg("授权码更新成功", 2, -1);
				}else{
					alert("error");
				}
			}
		});
	}

function publishOperate(cellvalue,options,rowdata){
	var id = rowdata.id;
	 var content = "";
	<c:if test="${!empty extension_youku_videomanager_modify}">	
	<security:authorize ifAnyGranted="${extension_youku_videomanager_modify.code}">	
		content += "<a href='javascript:void(0);' id='shortcut_extension_youku_videomanager_modify_"+id+"' sid="+id+" class='shortcut_extension_youku_videomanager_modify'>";
		content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${extension_youku_videomanager_modify.name}";
		content += "</a>";
	</security:authorize>
	</c:if>
	
	<c:if test="${!empty extension_youku_videomanager_cancel}">	
	<security:authorize ifAnyGranted="${extension_youku_videomanager_cancel.code}">	
			content += "<a href='javascript:void(0);' id='shortcut_extension_youku_videomanager_cancel_"+id+"' sid="+id+" class='shortcut_extension_youku_videomanager_cancel' >";
			content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${extension_youku_videomanager_cancel.name}";
			content += "</a>";
	</security:authorize>
	</c:if>
	return content;
}

function unPublishOperate(cellvalue,options,rowdata){
	var id = rowdata.id;
	 var content = "";
	<c:if test="${!empty extension_youku_videomanager_modify}">	
	<security:authorize ifAnyGranted="${extension_youku_videomanager_modify.code}">	
		content += "<a href='javascript:void(0);' id='shortcut_extension_youku_videomanager_modify_"+id+"' sid="+id+" class='shortcut_extension_youku_videomanager_modify' >";
		content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${extension_youku_videomanager_modify.name}";
		content += "</a>";
	</security:authorize>
	</c:if>
	
	<c:if test="${!empty extension_youku_videomanager_publish}">	
	<security:authorize ifAnyGranted="${extension_youku_videomanager_publish.code}">	
			content += "<a href='javascript:void(0);' id='shortcut_extension_youku_videomanager_publish_"+id+"' sid="+id+" class='shortcut_extension_youku_videomanager_publish'>";
			content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${extension_youku_videomanager_publish.name}";
			content += "</a>";
	</security:authorize>
	</c:if>
	
	<c:if test="${!empty extension_youku_videomanager_delete}">	
	<security:authorize ifAnyGranted="${extension_youku_videomanager_delete.code}">	
			content += "<a href='javascript:void(0);' id='shortcut_extension_youku_videomanager_delete_"+id+"' sid="+id+" class='shortcut_extension_youku_videomanager_delete'>";
			content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${extension_youku_videomanager_delete.name}";
			content += "</a>";
	</security:authorize>
	</c:if>
	return content;
}

function changeTo(data){
	if(data == "all"){
		return '全部公开';
	}else if(data == "friend"){
		return '朋友可见';
	}else if(data == "password"){
		return '加密';
	}else{
		return '';
	}
}

function changeCo(data){
	if(data == 'original'){
		return '原创';
	}else if(data == 'reproduced'){
		return '转载';
	}else{
		return '';
	}
}

function showLayer(data){
	var showState;
	if(data.state == 'encoding'){
		showState = '转码中';
	}else if(data.state == 'fail'){
		showState = '转码失败';
	}else if(data.state == 'in_review'){
		showState = '审核中';
	}else if(data.state == 'blocked'){
		showState = '已屏蔽';
	}
	top.layer.msg('视频状态为:'+showState,2,-1);

}

function tab(tag){
	currcount = 0;
	type = tag;
	if(tag == 1){
		$("#youku_upload").hide();
		$("#youku_update_token").hide();
		$("#tab_2").addClass("selected");
		$("#tab_1").removeClass("selected");
		jQuery("#videoList").focus();
		jQuery(".tip-violet").hide();
		jQuery("#videoList").jqGrid("setCaption","<msg:message code='videobasic.publish.list' />");
		jQuery("#videoList").jqGrid("setGridParam",{postData:{c:"${c}",publishOrNot:type,categoryID:categoryID}});
		jQuery("#videoList").setColProp('operate',{formatter:publishOperate});
		jQuery("#videoList").trigger("reloadGrid");
	}else{
		$("#youku_upload").show();
		$("#youku_update_token").show();
		$("#tab_1").addClass("selected");
		$("#tab_2").removeClass("selected");
		jQuery("#videoList").focus();
		jQuery(".tip-violet").hide();
		jQuery("#videoList").jqGrid("setCaption","<msg:message code='videobasic.unpublish.list' />");
		jQuery("#videoList").jqGrid("setGridParam",{postData:{c:"${c}",publishOrNot:type,categoryID:categoryID}});
		jQuery("#videoList").setColProp('operate',{formatter:unPublishOperate});
		jQuery("#videoList").trigger("reloadGrid");
	}
}

function reload(){
	categoryID = $('#id').val();
	jQuery("#videoList").focus();
	jQuery(".tip-violet").hide();
	jQuery("#videoList").jqGrid("setGridParam",{postData:{c:"${c}",publishOrNot:type,publishOrNot:type,categoryId:categoryID}});
	jQuery("#videoList").trigger("reloadGrid");
}


</script>
</head >
<body class="skinMain">
<ul id="tabnav">
	<li class="tab_1" ><a href="#" id="tab_1" onclick="tab(0)" class="selected"><msg:message code="videobasic.unpublish"/></a></li>
	<li class="tab_2" ><a href="#" id="tab_2" onclick="tab(1)" ><msg:message code="videobasic.publish" /></a></li>
</ul>
<div id="div_1" class="tab-div-show" >
<form:form method="post" action="list" commandName="category">
<input type="hidden" name="c" value="${videoBasic.c}" />
<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
	<tr>
		<td width="15%" valign="top">
			<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width: 208px; ">
				<div class="ui-jqgrid-view" id="gview_dictionaryItemList" style="width: 208px; ">
					<div class="ui-jqgrid-view" id="gview_dictionaryItemList" style="width: 208px; ">
						<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix"><msg:message code="video.category"/></div>
						<div style="width: 208px;" class="ui-state-default ui-jqgrid-hdiv">
							<div class="ui-jqgrid-hbox">
								<form:select path="id" id="id" items="${categoryList}" itemValue="id" itemLabel="name" onChange="reload();" size="${categoryListSize}" style="width:208px;border:0px;font-size:10pt;"/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</td>
		<td valign="top">
			<table id="videoList" ><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
			<lable style="color:red;">授权码说明：授权码是优酷视频授权的一种方式,通过有效授权码才能上传视频。授权码期限为一个月,当过期后需点击【更新授权码】按钮获取最新授权码</lable>
		</td>
	</tr>
</table>
</form:form>
</div>
</body>
</html>