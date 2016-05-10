<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" type="text/css" href="${base}/common/skin/css/productTab.css" />
<link rel="stylesheet" type="text/css" href="${base}/common/skin/css/flexpaper/flexpaper.css" />
<script type="text/javascript" src="${base}/common/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${base}/common/js/flexpaper/flexpaper.js"></script>
<script type="text/javascript" src="${base}/common/js/flexpaper/flexpaper_handlers.js"></script>
<script type="text/javascript">
	var isEdit = false; //  是否处于编辑状态
	var selrow = -1; // 当前页面记录条数
	var lastsel; // 最后编辑的行
	var rowicon = "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>";
	var swfAddress = "${ftpUrl}";
	var downable = "<c:forEach items="${downloadPermission}" var="dp">${dp.key}:${dp.value};</c:forEach>";
	
	$(document).ready(function() {
		if(downable.length > 0){
			downable = downable.substring(0, downable.length-1);
		}
		
		// 上传产品资料
		$("#uploadify_productdoc").uploadify({
			'swf'            : '${base}/common/flash/uploadify/uploadify.swf',
			'uploader'       : '${base}/product/manage/attachment/uploadfile;jsessionid=<%=session.getId()%>?productId=${product.id}',  
			'buttonClass'    : 'btn2',
			'queueID'        : 'fileQueue_productdoc',
			'sizeLimit'		 : 52428800,
			'simUploadLimit' :	1,
			'auto'           : false,   
			'multi'          : false,  
			'fileTypeDesc'   : "文件",               
			'fileTypeExts'   : "*.pdf;*.doc;",        		
			'buttonText'     : '<msg:message code='info.prompt.select'/>',   
			'width'          : 60,
			'height'         : 24,
			'queueSizeLimit' : 1,
			//当每个文件添加至队列后触发
			onSelect : function(file) {
				var size = file.size;
				if(size >= 52428800){
					top.layer.msg("请上传50M以内的文件");
					$('#uploadify_productdoc').uploadify('cancel', '*');
				}
			},
			onUploadSuccess: function (file,data,response) {
				 if(response==true){
					addRow(file,data);
				 }else{
					top.layer.msg("<msg:message code='uploadify.fileFail'/>");
				 }
			}
		});
		
		// 产品资料维护列表
		jQuery("#productAttachList").jqGrid({
			url:'${base}/product/manage/attachment/grid.json',
			datatype: "json",
			colNames: [
					   "",
					   "<msg:message code='product.document.title'/>", 
					   "",
					   "<msg:message code='product.document.downloadPermission'/>",
					   "<msg:message code='product.document.filename'/>",
					   "<msg:message code='product.document.size'/>",
					   "",
					   "",
					   ""
					   ],
			colModel: [
					   {name: 'id',index:'id',hidden:true},
					   {name: 'attachTitle',index:'attachTitle',width:'30%',align:'left',hidden: false,sortable:false,editable:true},
					   {name: 'downloadPermission',index:'downloadPermission',width:'10%',hidden:true},
					   {name: 're_downloadPermission',index:'re_downloadPermission',width:'10%',align:'center',hidden: false,sortable:false,formatter:changeDownablePermission,editable:true,edittype:"select",editoptions:{value:downable}},
					   {name: 'fileName',index:'fileName',width:'30%',align:'center',hidden: false,sortable:false,formatter:fileNameAct},
					   {name: 'size',index:'size',width:'12%',align:'center',hidden: false,sortable:false,formatter:initSize},
					   {name: 'fileUrl',index:'fileUrl',align:'center',hidden: true,sortable:false},
					   {name: 'status',index:'status',width:'10%',align:'center',hidden: true,sortable:false},
					   {name: 'act',index:'act',width:'35%',align:'center',hidden: false,sortable:false,formatter:initAct}
					   ],
			mtype:"POST",
			postData:{c:"${product.c}",productId:${product.id}},
			rowNum:10,     
			page:"${product.page}",
			pager: '#pagered',
			height:<msg:message code='jqgrid.height.300'/>,
			autowidth: true,
			viewrecords: true,
			rownumbers: true,
			jsonReader: {
				repeatitems: false
			},
			caption:'<msg:message code="product.tab.doc"/>'
		});
		
		// 行操作事件
		
		// 修改事件
		$(".shortcut_modify").live("click",function(){
			if(isEdit == true){
				top.layer.msg("<msg:message code="dictionaryItem.notsave"/>");
				return;
			}
			isEdit = true;
			$("#_up").css("display","none");
			var rowid = $(this).attr("sid");
			// 编辑行
			jQuery("#productAttachList").jqGrid('editRow',rowid);
			// 设置按钮
			$(this).attr("class","").css("color","#999");
			$("#shortcut_save_"+rowid).attr("class","shortcut_save").css("color","#53056A");
			$("#shortcut_reset_"+rowid).attr("class","shortcut_reset").css("color","#53056A");
			$("#shortcut_remove_"+rowid).attr("class","").css("color","#999");
			lastsel = rowid;
		});
		
		// 取消事件
		$(".shortcut_reset").live("click",function(){
			var rowid = $(this).attr("sid");
			// 编辑行
			jQuery("#productAttachList").jqGrid('restoreRow',rowid);
			// 设置按钮
			$(this).attr("class","").css("color","#999");
			$("#shortcut_modify_"+rowid).attr("class","shortcut_modify").css("color","#53056A");
			$("#shortcut_save_"+rowid).attr("class","shortcut_save").css("color","#999");
			$("#shortcut_remove_"+rowid).attr("class","shortcut_remove").css("color","#53056A");
			isEdit = false;
			$("#_up").css("display","");
		});
		
		// 保存事件
		$(".shortcut_save").live("click",function(){
			var rowid = $(this).attr("sid");
			var t_attachTitle = $("#"+rowid+"_attachTitle").val();
			if(t_attachTitle == ""){
				layer.msg("请输入附件标题！");
				return false;
			}
			
			var data = jQuery("#productAttachList").jqGrid("getRowData",rowid);
			// 设置按钮
			$(this).attr("class","").css("color","#999");
			$("#shortcut_modify_"+rowid).attr("class","shortcut_modify").css("color","#53056A");
			$("#shortcut_reset_"+rowid).attr("class","shortcut_save").css("color","#999");
			$("#shortcut_remove_"+rowid).attr("class","shortcut_remove").css("color","#53056A");
			var data="c=${product_product_attachment.code}&productId=${product.id}&attachTitle="+$("#"+rowid+"_attachTitle").val()+"&downloadPermission="+$("#"+rowid+"_re_downloadPermission").val()+"&fileUrl="+data.fileUrl;
			if(rowid >0){
				data+="&id="+rowid;
			}else{
				data+="&fileName="+$("#"+rowid+"_fileName").val()+"&size="+$("#"+rowid+"_size").val();
			}
			// 保存行数据
			jQuery.ajax({
				url:"${base}/product/manage/attachment/save.json",
				type: "post",
				contentType : 'application/x-www-form-urlencoded',
				data:data, 
				dataType : 'json',
				success:function(data){
					if(data.status == 'success'){
						isEdit = false;
						$("#_up").css("display","");
						var page = jQuery('#productAttachList').jqGrid('getGridParam','page');
						jQuery("#productAttachList").setGridParam({page:page,c:"${product.c}",productId:${product.id}}).trigger("reloadGrid");
						top.$.layer({
							area : ['auto','auto'],
							dialog : {msg:'<msg:message code='info.operate.success'/>',type : 9}	
						});
					}else{
						top.layer.msg("error");
					}
				}
			});
		});
		
		// 删除事件
		$(".shortcut_remove").live("click",function(){
			// 删除行，并且清除文件
			var rowid = $(this).attr("sid");
			if(rowid < 0){
				jQuery("#productAttachList").jqGrid('delRowData',rowid); 
				isEdit = false;
				$("#_up").css("display","");
			}else{
				if(isEdit == true){
					top.layer.msg("<msg:message code="dictionaryItem.notsave"/>");
					return;
				}
				var data="c=${product.c}&id="+rowid+"";
				var layers = top.layer.confirm("<msg:message code='product.document.remove.confirm'/>",  function(){
					jQuery.ajax({
						url:"${base}/product/manage/attachment/delete.json",
						type: "post",
						contentType : 'application/x-www-form-urlencoded',
						data:data, 
						dataType : 'json',
						success:function(jsonData){
							if (jsonData.status == "success"){
								isEdit = false;
								$("#_up").css("display","");
								var page = jQuery('#productAttachList').jqGrid('getGridParam','page');
								jQuery("#productAttachList").setGridParam({page:page,c:"${product.c}",productId:${product.id}}).trigger("reloadGrid");
								top.layer.close(layers);							
							}
						}
					});
				});
			}
		});
		
		//上架事件
		$(".shortcut_up").live("click",function(){
			var rowid = $(this).attr("sid");
			var data = jQuery("#productAttachList").jqGrid("getRowData",rowid);
			//附件id
			var productAttachID = $(this).attr("pid");
			var title = data.TITLE;
			var data="status=1&id="+productAttachID;
			// 保存行数据
			jQuery.ajax({
				url:"${base}/product/manage/updateAttachment.json",
				type: "post",
				contentType : 'application/x-www-form-urlencoded',
				data:data, 
				dataType : 'json',
				success:function(data){
					if(data.status == 'success'){
						var page = jQuery('#productAttachList').jqGrid('getGridParam','page');
						jQuery("#productAttachList").setGridParam({page:page,productId:${product.id}}).trigger("reloadGrid");
						top.$.layer({
							area : ['auto','auto'],
							dialog : {msg:'上架操作成功!',type : 1}	
						});
					}else{
						top.layer.msg("上架操作失败!");
					}
				}
			});
		});

		//下架事件
		$(".shortcut_down").live("click",function(){
		
			var rowid = $(this).attr("sid");
			var data = jQuery("#productAttachList").jqGrid("getRowData",rowid);
			//附件id
			var productAttachID = $(this).attr("pid");
			var title = data.TITLE;
			var data = "id=" + productAttachID;
				data += "&status=0";
			// 保存行数据
			jQuery.ajax({
				url:"${base}/product/manage/updateAttachment.json",
				type: "post",
				contentType : 'application/x-www-form-urlencoded',
				data:data, 
				dataType : 'json',
				success:function(data){
					if(data.status == 'success'){
						var page = jQuery('#productAttachList').jqGrid('getGridParam','page');
						jQuery("#productAttachList").setGridParam({page:page,productId:${product.id}}).trigger("reloadGrid");
						top.$.layer({
							area : ['auto','auto'],
							dialog : {msg:'下架操作成功!',type : 1}	
						});
					}else{
						top.layer.msg("下架操作失败!");
					}
				}
			});
		});
		$(".shortcut_downcheck").live("click",function(){
	    	var rowid = $(this).attr("pid");
			var url = "${base}/product/manage/downchecksave.json?prompt=name&status=0&id="+ rowid;
	    	var nurl = "${base}/product/manage/downchecksave.json?prompt=name&status=1&id="+ rowid;
			var index = top.$.layer({
			  	    shade  : [0.5 , '#000' , true],
			  	    area   : ['auto','auto'],
			  	  	offset : ['50px',''],
			  	    dialog : {
			  	    msg    : "是否同意下架？",
			  	    btns   : 2, 
			  	    type   : 4,
			  	    btn    : ["同意","不同意"],
			  	    yes    : function(){
						$.ajax({
							url:url,
							type:'post',
							timeout:'60000',
							dataType:'json',
							success:function(jsonData,textStatus){							
								if(textStatus == "success"){
									if(jsonData.status == "success"){
										top.layer.close(index);
										top.$.layer({
											shade : [0.5 , '#000' , true],
									  		offset : ['50px',''],
									  	 	title : "<msg:message code='info.prompt'/>",
										    dialog : {msg:"操作成功！",type : 1}	
										});
										jQuery("#productAttachList").trigger("reloadGrid");
									}else{
										top.layer.close(index);
										top.$.layer({
											shade : [0.5 , '#000' , true],
									  		offset : ['50px',''],
									  	 	title : "<msg:message code='info.prompt'/>",
										    dialog : {msg:"操作失败！",type : 3}	
										});
									}
								}
							}
						});
			  	    },
			  	    no     : function(){
				  	    	$.ajax({
								url:nurl,
								type:'post',
								timeout:'60000',
								dataType:'json',
								success:function(jsonData,textStatus){	
									if(textStatus == "success"){
										if(jsonData.status == "success"){
											top.layer.close(index);
											top.$.layer({
												shade : [0.5 , '#000' , true],
										  		offset : ['50px',''],
										  	 	title : "<msg:message code='info.prompt'/>",
											    dialog : {msg:"操作成功！",type : 1}	
											});
											jQuery("#productAttachList").trigger("reloadGrid");
										}else{
											top.layer.close(index);
											top.$.layer({
												shade : [0.5 , '#000' , true],
										  		offset : ['50px',''],
										  	 	title : "<msg:message code='info.prompt'/>",
											    dialog : {msg:"操作失败！",type : 3}	
											});
										}
									}
								}
							});
			  	        }
	  	    		}
				});
			
	    });   
		//-- 行操作事件
		
		// 文件预览
		$("#_preview").live("click",function(){
			var rowid = $(this).attr("sid");
			var data = jQuery("#productAttachList").jqGrid("getRowData",rowid);
			var fileName = data.fileName;
			var url = data.fileUrl;
			url = url.replace(".pdf",".swf");
			$('#documentViewer').FlexPaperViewer(
				{config : {
					SWFFile :'${ftpUrl}'+encodeURI(url),
					Scale : 0.6,
					ZoomTransition : 'easeOut',
					ZoomTime : 0.5,
					ZoomInterval : 0.2,
					FitPageOnLoad : true,
					FitWidthOnLoad : true,
					PrintEnabled: 'false',
					PrintToolsVisible: 'false',
					FullScreenAsMaxWindow : false,
					ProgressiveLoading : false,
					MinZoomSize : 0.2,
					MaxZoomSize : 5,
					SearchMatchAll : false,
					InitViewMode : 'Portrait',
					RenderingOrder : 'flash',
					StartAtPage : '',
					ViewModeToolsVisible : true,
					ZoomToolsVisible : true,
					NavToolsVisible : true,
					CursorToolsVisible : true,
					SearchToolsVisible : true,
					WMode : 'window',
					localeChain: 'zh_CN'
					}
				}
			);
			preview(fileName);
		});
	
	});
	
	// 转换下载权限中文
	function changeDownablePermission(cellvalue, options, rowdata){
		var downloadPermission = rowdata.downloadPermission;
		if(downloadPermission == 0){
			return "不能下载";
		}
		if(downloadPermission == 1){
			return "网签会员";
		}
		if(downloadPermission == 2){
			return "注册未网签";
		}else{
			return rowdata.re_downloadPermission;
		}
	}
	
	// 初始行表格标题
	function initSize(cellvalue, options, rowdata){
		if(cellvalue == null)
			cellvalue = "";
		return  cellvalue+"KB";
	}
	
	
	function initAct(cellvalue, options, rowdata){
		var id = rowdata.id;
		var content = "";
		var pl = id;
		var cl = id;
		
		//console.log("--------------"+rowdata.status);

		content += "<a href='javascript:void(0);' title='修改' id='shortcut_modify_"+cl+"' sid="+cl+" class='shortcut_modify'>"+rowicon+"修改</a>"; 
		content += "<a href='javascript:void(0);' title='保存' id='shortcut_save_"+cl+"' sid="+cl+" class='' style='color:#999;'>"+rowicon+"保存</a>"; 
		content += "<a href='javascript:void(0);' title='取消' id='shortcut_reset_"+cl+"' sid="+cl+" class='' style='color:#999;'>"+rowicon+"取消</a>"; 
		content += "<a href='javascript:void(0);'  title='删除' id='shortcut_remove_"+cl+"' sid="+cl+" class='shortcut_remove' style='color:#53056A;'>"+rowicon+"删除</a>"; 
		if(rowdata.status != null){
			if(rowdata.status == "1"){
				content += "<a href='javascript:void(0);'  title='上架' style='color:#999'>"+rowicon+"上架</a>"; 
				content += "<a href='javascript:void(0);'  title='下架' id='shortcut_down_"+cl+"'  pid="+pl+" sid="+cl+" class='shortcut_down'>"+rowicon+"下架</a>";
				content += "<a href='javascript:void(0);'  title='下架审核' style='color:#999'>"+rowicon+"下架审核</a>"; 
			}else if(rowdata.status == "0"){
				content += "<a href='javascript:void(0);'  title='上架' id='shortcut_up_"+cl+"'  pid="+pl+" sid="+cl+" class='shortcut_up'>"+rowicon+"上架</a>"; 
				content += "<a href='javascript:void(0);'  title='下架' style='color:#999'>"+rowicon+"下架</a>";
				content += "<a href='javascript:void(0);'  title='下架审核' style='color:#999'>"+rowicon+"下架审核</a>"; 
			}else if(rowdata.status == "2"){
				content += "<a href='javascript:void(0);'  title='上架' style='color:#999'>"+rowicon+"上架</a>"; 
				content += "<a href='javascript:void(0);'  title='下架' style='color:#999'>"+rowicon+"下架</a>";
				content += "<a href='javascript:void(0);'  title='下架审核' id='shortcut_downcheck_"+cl+"'  pid="+pl+" sid="+cl+" class='shortcut_downcheck'>"+rowicon+"下架审核</a>";
			}
		}else{
			content += "<a href='javascript:void(0);'  title='上架' style='color:#999'>"+rowicon+"上架</a>"; 
			content += "<a href='javascript:void(0);'  title='下架' style='color:#999'>"+rowicon+"下架</a>";
			content += "<a href='javascript:void(0);'  title='下架审核' style='color:#999'>"+rowicon+"下架审核</a>";
		}
		return content;
	}
	
	// 文件名
	function fileNameAct(cellvalue, options, rowdata){
		return "<a href='javascript:void(0);' id='_preview' sid='"+rowdata.id+"' style='text-decoration:underline'>"+cellvalue+"</a>";
	}
	
	// 增加行
	function addRow(file,path){
		var fileName = file.name;
		var size = file.size;
		addRowData(fileName,'',fileName,size,path);
		isEdit = true;
		$("#_up").css("display","none");
    };
	 
	// 行数据
	function addRowData(attachTitle,downloadPermission,fileName,size,path){
		// 还原当前编辑行
		jQuery("#productAttachList").jqGrid('restoreRow',lastsel);
		var data={id:selrow,attachTitle:attachTitle,fileName:fileName+"<input type='hidden' id='"+selrow+"_fileName' value='"+fileName+"'/>",size:size+"<input type='hidden' id='"+selrow+"_size' value='"+size+"'/>",fileUrl:path};
		jQuery("#productAttachList").jqGrid('addRowData',selrow,data);
		// 插入首行
		var current = $("tr[id='"+selrow+"']");
		var prev = $(".jqgfirstrow");
		current.insertAfter(prev);
		jQuery('#productAttachList').editRow(selrow);
		// 设置按钮
		$("#shortcut_modify_"+selrow).attr("class","shortcut_modify").css("color","#999");
		$("#shortcut_save_"+selrow).attr("class","shortcut_save").css("color","#53056A");
		$("#shortcut_reset_"+selrow).attr("class","").css("color","#999");
		$("#shortcut_remove_"+selrow).attr("class","shortcut_remove").css("color","#53056A");
		
		selrow--;
	 };

	

	//控制中文
	function checkname(evt,object,flag){
		var key = window.event?event.keyCode:evt.which;
		var re = /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/;
		if(key != 8){
			var value = $(object).val();
			if(!re.test(value)){
				if("paste" == flag){
					$(object).val("");
				}else{
					if(value != null && value != ""){
						$(object).val(value.substring(0,value.length-1));
					}
				}
			}
		}
	}

	//控制英文
	function checkcode(evt,object,flag){
		var key = window.event?event.keyCode:evt.which;
		var re = /^\w+$/;
		if(key != 8 && $(object).val() != "<msg:message code='please.input'/>" && $(object).val() != "<msg:message code='engilshname.already'/>"){
			var value = $(object).val();
			if(!re.test(value)){
				if("paste" == flag){
					$(object).val("");
				}else{
					if(value != null && value != ""){
						$(object).val(value.substring(0,value.length-1));
					}
				}
			}
		}
	}
	
	// 搜索
	function search(){
		jQuery("#productAttachList").jqGrid("setGridParam",{postData:{c:"${product.c}",productId:${product.id},attachTitle:$("#attachTitle").val()}});		
		jQuery("#productAttachList").trigger("reloadGrid");
	}
	
	// 点击上传时触发
	function uploadifyUpload(){  
        $('#uploadify_productdoc').uploadify('upload', '*');
    } 

	// 预览
	function preview(name){
		$.layer({
			type : 1,
			title : name,
			shadeClose: true,
			area : ['810px' , '550px'],
			offset : ['1px','']	,
			page:{
				html : '<div style="width:800px;height:550px;">'+$("#documentViewer").html()+'</div>'
			}
		});
	}

	
</script>

</head>
<body class="skinMain">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
				<input type="hidden" name="c" value="${product.c}"/>
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
					<tr>
						<td align="center">
							<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
								<tr>
									<td align="right" width="10%" class="search_info" height="25"><msg:message code='product.document.title'/><msg:message code="system.common.sign.colon"/></td>
									<td align="left" width="30%"  class="search_lable">
										<input name="attachTitle" id="attachTitle"/>
									</td>
									<td rowspan="2" width="20%" class="button_area" align="center">
										<input type="button" class="btn2" id="select" onclick="search();" value="<msg:message code='button.search'/>">
									</td>
								</tr>	
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<form id="productAttachment" name="productAttachment">
							<input type="hidden" name="id" value="${product.id}"/>
							<table id="productAttachList" width="100%">
								<tr><td>&nbsp;</td></tr>
							</table>
							<div id="pagered"></div>
							</form>
						</td>
					</tr>
				</table>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinLayout">
					<tr>
						<td width="100%" class="skinLayout_info">
						     <div>如果您上传的文件格式为docx，请使用word另存为doc格式之后再上传</div>
							<div id="fileQueue_productdoc"></div>
							<input type="file" name="uploadify_productdoc" id="uploadify_productdoc" />
							<p>
								<div id="fileOperateInfo">
									<a href="javascript:$('#uploadify_productdoc').uploadify('cancel', '*')">
									<msg:message code='uploadify.cancelUpload'/></a> | 
									<a href="javascript:uploadifyUpload();" id="_up">
									<msg:message code='uploadify.startUpload'/></a>
								</div>
							</p>
						</td>
					</tr>
					<tr height="35">
						<td width="100%"style="border:0px;"></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<div id="documentViewer" class="flexpaper_viewer" style="display:none;"></div>
	<div class="toolbar">
		<div class="left"></div>
			<div class="center">
				<input type="button" class="btn2" onclick="history.go(-1);" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
			</div>
		<div class="right"></div>
	</div>
</body>
</html>
