<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
<script type="text/javascript">
	$(window).resize(function(){ 　　
		$("#providerList").setGridWidth($(window).width()*0.999);　
	});
	$(document).ready(function(){
		jQuery("#providerList").jqGrid({
			url: 'provider/grid.json',
			datatype: 'json',
			colNames: ["<msg:message code='provider.name'/>",
			           "<msg:message code='provider.short.name'/>",
			           "<msg:message code='provider.companyType'/>",
			           "<msg:message code='provider.status'/>",
			           "<msg:message code='provider.productCount'/>",
			           "<msg:message code='provider.sortNum'/>",
			           "<msg:message code='info.operate'/>"],
			colModel: [{name:'name', index:'p.name', align:'center',width:'20%', hidden:false, sortable:false},
			           {name:'shortName', index:'p.shortName',width:'20%', align:'center', hidden:false, sortable:false},
			           {name:'companyType', index:'p.companyType',width:'20%', align:'center', hidden:false, sortable:false,
				           formatter:function(cellvalue, options, rowdata){
		                	   var content="";
		                	   	<c:forEach var="item" items="${companyType}">
									   if(cellvalue==${item.key}){
										   content="${item.value}";
									   }
								</c:forEach>
								return content;   
						   }
			           },
			           {name:'status', index:'p.status',width:'10%', align:'center', hidden:false, sortable:false,
				           formatter:function(cellvalue, options, rowdata){
		                	   var content="";
		                	   	<c:forEach var="item" items="${providerStatus}">
									   if(cellvalue==${item.key}){
										   content="${item.value}";
									   }
								</c:forEach>
								return content;   
						   }
			           },
			           {name:'productCount', index:'productCount',width:'10%', align:'center', hidden:false, sortable:false},
			           {name:'sortNum', index:'sortNum',width:'10%', align:'center', hidden:false, sortable:false,editable:true,edittype:'text',editrules:{edithidden:true,required:true,number:true,minValue:0,maxValue:100000}},
			           {name:'act', index:'p.act', align:'center',width:'40%', sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   var id = rowdata.id;
			        		   var content = "";
			        		   <c:if test="${!empty cooperate_provider_detail}">
			        		   <security:authorize ifAnyGranted="${cooperate_provider_detail.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_provider_detail.indexCode}'";
			        		   		content += " title='${cooperate_provider_detail.name}'>";
			        		   		content += "<img src='${base}/${skin}/${cooperate_provider_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_provider_detail.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty cooperate_provider_modify}">
			        		   <security:authorize ifAnyGranted="${cooperate_provider_modify.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_provider_modify.indexCode}'";
			        		   		content += " title='${cooperate_provider_modify.name}'>";
			        		   		content += "<img src='${base}/${skin}/${cooperate_provider_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_provider_modify.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty cooperate_provider_status}">
			        		   <security:authorize ifAnyGranted="${cooperate_provider_status.code}">
			        		   	if(rowdata.status == 1){
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_provider_status.indexCode}'";
			        		   		content += " title='${cooperate_provider_status.name}'>";
			        		   		content += "<img src='${base}/${skin}/${cooperate_provider_status.icon}' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_provider_status.name}";
			        		   		content += "</a>";
			        		   	}else{
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_provider_status.indexCode}'";
			        		   		content += " title='<msg:message code='status.recover'/>'>";
			        		   		content += "<img src='${base}/${skin}/images/icon/Enabled.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='status.recover'/>";
			        		   		content += "</a>";
			        		   	}		
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty cooperate_provider_delete}">
			        		   <security:authorize ifAnyGranted="${cooperate_provider_delete.code}">
			        		   		if (rowdata.productCount>0){
			        		   			content += "<span style='color:#CFC09F;' title='${cooperate_provider_delete.name}'><img src='${base}/${skin}/${cooperate_provider_delete.icon}' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_provider_delete.name}</span>";
			        		   		}else{
				        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_provider_delete.indexCode}'";
				        		   		content += " title='${cooperate_provider_delete.name}'>";
				        		   		content += "<img src='${base}/${skin}/${cooperate_provider_delete.icon}' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_provider_delete.name}";
				        		   		content += "</a>";
			        		   		}
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty cooperate_provider_provideruser}">
			        		   <security:authorize ifAnyGranted="${cooperate_provider_provideruser.code}">
			        		   		if(rowdata.status == 1){
				        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_provider_provideruser.indexCode}'";
				        		   		content += " title='${cooperate_provider_provideruser.name}'>";
				        		   		content += "<img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_provider_provideruser.name}";
				        		   		content += "</a>";
			        		   		}else{
			        		   			content += "<span style='color:#CFC09F;' title='${cooperate_provider_provideruser.name}'><img src='${base}/${skin}/images/icon/data.png' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_provider_provideruser.name}</span>";
			        		   		}
			        		   </security:authorize>
			        		   </c:if>
			        		   return content;
			        	   }
			           }],
            cellEdit: true, 
            cellurl: "${base}/cooperate/provider/saveSortNum?c=${provider.c}",
            cellsubmit: 'remote',  
			mtype: "POST",
			postData: {c:"${provider.c}",name:"${provider.name}",  delStatus:1},
			rowNum: 10,
			rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#pagered',
			height: '<msg:message code='jqgrid.height.250'/>',
			autowidth: true,
			viewrecords: true,
			multiselect: true,
			jsonReader: {
				repeatitems: false
			},
			caption: '<msg:message code="provider.list"/>',
			toolbar: [true,"top"]
		});
		
		$(".shortcut_${cooperate_provider_detail.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			window.location.href = "${base}${cooperate_provider_detail.controller}?c=${cooperate_provider_detail.code}&id=" + rowid;
		});
		//用户管理
		$(".shortcut_${cooperate_provider_provideruser.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			window.location.href = "${base}${cooperate_provider_provideruser.controller}?c=${cooperate_provider_provideruser.code}&id=" + rowid;
		});
		
		$(".shortcut_${cooperate_provider_modify.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			window.location.href = "${base}${cooperate_provider_modify.controller}?c=${cooperate_provider_modify.code}&id=" + rowid;
		});
		
		$(".shortcut_${cooperate_provider_status.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var rowdata = jQuery("#providerList").jqGrid('getRowData',rowid);
			var status = rowdata.status;
			if($(this).attr("title") == '${cooperate_provider_status.name}'){
				var url = "${base}${cooperate_provider_status.controller}.json?c=${cooperate_provider_status.code}&id=" + rowid + "&status=2";
			}else{
				var url = "${base}${cooperate_provider_status.controller}.json?c=${cooperate_provider_status.code}&id=" + rowid + "&status=1";
			}
			var index = top.$.layer({
				shade    : false,
				dialog   : {
					msg  : "<msg:message code='info.prompt.status.confirm'/>",
					btns : 2,
					type : 4,
					btn  : ["<msg:message code='button.confirm'/>", "<msg:message code='button.cancel'/>"],
					yes  : function(){
						$.ajax({
							url:url,
							type:'post',
							timeout:'60000',
							dataType:'json',
							success:function(jsonData,textStatus){
								if(textStatus == "success"){
									if(jsonData.status == "success"){
										top.layer.close(index);
										$("#providerList").trigger("reloadGrid");
									}
								}
							}
						});
					},
					no   : function(){
						top.layer.close(index);
					}
				}
			});
		});
		
		$(".shortcut_${cooperate_provider_delete.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var url = "${base}${cooperate_provider_delete.controller}.json?c=${cooperate_provider_delete.code}&id=" + rowid;
			var records = $("#providerList").getGridParam("records");
	    	var page = $("#providerList").getGridParam("page");
			var index = top.$.layer({
				shade    : false,
				dialog   : {
					msg  : "<msg:message code='info.prompt.delete.confirm'/>",
					btns : 2,
					type : 4,
					btn  : ["<msg:message code='button.confirm'/>", "<msg:message code='button.cancel'/>"],
					yes  : function(){
						$.ajax({
							url:url,
							type:'post',
							timeout:'60000',
							dataType:'json',
							success:function(jsonData,textStatus){
								if(textStatus == "success"){
									if(jsonData.status == "success"){
										top.layer.close(index);
										for(var i = 1; ;i++){
		   									if(records = 10*i+1){
		   										jQuery("#providerList").setGridParam({page:page-1}).trigger("reloadGrid");
		   										break;
		   									}
		   								}
									}
								}
							}
						});
					},
					no   : function(){
						top.layer.close(index);
					}
				}
			});
		});
		
		<c:if test="${!empty cooperate_provider_create}">
		<security:authorize ifAnyGranted="${cooperate_provider_create.code}">
			var $content = $("<a></a>").attr("href","javascript:void(0)")
						   .attr("id","${cooperate_provider_create.indexCode}")
						   .append($("<img/>").attr("src","${base}/${skin}/${cooperate_provider_create.icon}")
						   .attr("width","18").attr("height","18").attr("border",0)
						   .attr("border","0").attr("align","absmiddle"))
						   .append("${cooperate_provider_create.name}");
			$("#t_providerList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
			$("#${cooperate_provider_create.indexCode}","#t_providerList").click(function(){
				window.location.href = "${base}${cooperate_provider_create.controller}?c=${cooperate_provider_create.code}";
			});
		</security:authorize>
		</c:if>
		<c:if test="${!empty cooperate_provider_delete}">
		<security:authorize ifAnyGranted="${cooperate_provider_delete.code}">
			var $content = $("<a></a>").attr("href","javascript:void(0)")
						   .attr("id","${cooperate_provider_delete.indexCode}")
						   .append($("<img/>").attr("src","${base}/${skin}/${cooperate_provider_delete.icon}")
						   .attr("width","18").attr("height","18").attr("border",0)
						   .attr("border","0").attr("align","absmiddle"))
						   .append("${cooperate_provider_delete.name}");
			$("#t_providerList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
			$("#${cooperate_provider_delete.indexCode}","#t_providerList").click(function(){
				var selrow = jQuery("#providerList").jqGrid("getGridParam","selarrrow");
				if (selrow != null && selrow != ""){
					var url = "${base}${cooperate_provider_delete.controller}.json?c=${cooperate_provider_delete.code}&ids=" + selrow;
					var index = top.$.layer({
						shade    : false,
						dialog   : {
							msg  : "<msg:message code='info.prompt.delete.confirm'/>",
							btns : 2,
							type : 4,
							btn  : ["<msg:message code='button.confirm'/>", "<msg:message code='button.cancel'/>"],
							yes  : function(){
								$.ajax({
									url:url,
									type:'post',
									timeout:'60000',
									dataType:'json',
									success:function(jsonData,textStatus){
										if(textStatus == "success"){
											if(jsonData.status == "success"){
												top.layer.close(index);
												$("#providerList").trigger("reloadGrid");
											}
										}
									}
								});
							},
							no   : function(){
								top.layer.close(index);
							}
						}
					});
					}else{
						top.layer.msg("<msg:message code='info.prompt.delete.select'/>");
					}
			});
		</security:authorize>
		</c:if>
	});
	
</script>
	</head>
	<body class="skinMain">
		<form:form action="provider" method="post" commandName="provider">
			<input type="hidden" name="c" value="${provider.c}"/>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td align="center">
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
							<tr>
								<td align="right" width="10%" class="search_info">
									<msg:message code="provider.name"/>
								</td>
								<td align="left" width="30%" class="search_lable">
									<form:input path="name" htmlEscape="true"/>
								</td>
								<td width="20%" class="button_area">
									<input type="submit" class="btn2" id="select" value="<msg:message code='button.search' />"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table id="providerList"><tr><td>&nbsp;</td></tr></table>
						<div id="pagered"></div>
					</td>
				</tr>
			</table>
		</form:form>
	</body>
</html>