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
		$("#activityList").setGridWidth($(window).width()*0.999);　
	});
	$(document).ready(function(){
		jQuery("#activityList").jqGrid({
			url: 'manage/grid.json',
			datatype: 'json',
			colNames: ["活动名称",
			           "活动类型",
			           "活动状态",
			           "开始时间",
			           "结束时间",
			           "网页地址",
			           "模板名称",
			           "<msg:message code='info.operate'/>"],
			colModel: [{name:'name', index:'a.name', align:'center', sortable:false, width:'15%'},
			           {name:'type', index:'a.type', align:'center', sortable:false, width:'13%',
							formatter:function(cellvalue, options, rowdata){
			             	   var content="";
			             	   	<c:forEach var="item" items="${activityType}">
									   if(cellvalue=='${item.id}'){
										   content="${item.name}";
									   }
								</c:forEach>
								return content;   
						   }},
			           {name:'status', index:'a.status', align:'center', sortable:false, width:'13%',
				        	   formatter:function(cellvalue, options, rowdata){
				             	   var content="";
				             	   	<c:forEach var="item" items="${activityStatus}">
										   if(cellvalue=='${item.key}'){
											   content="${item.value}";
										   }
									</c:forEach>
									return content;
							   }},
			           {name:'startTime', index:'a.startTime', align:'center', sortable:false, width:'13%'},
			           {name:'endTime', index:'a.endTime', align:'center', sortable:false, width:'13%'},
			           {name:'url', index:'a.url', align:'center', sortable:false, width:'15%'},
			           {name:'templetName', index:'a.templetName', align:'center', sortable:false, hidden:true},
			           {name:'act', index:'i.act', align:'left', sortable:false, width:'18%',
			        	   formatter:function(cellvalue, options, rowdata){
			        		   var id = rowdata.id;
			        		   var content = "";
			        		   <c:if test="${!empty activity_manage_modify}">
			        		   <security:authorize ifAnyGranted="${activity_manage_modify.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${activity_manage_modify.indexCode}'";
			        		   		content += " title='${activity_manage_modify.name}'>";
			        		   		content += "<img src='${base}/${skin}/${activity_manage_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${activity_manage_modify.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty activity_manage_delete}">
			        		   <security:authorize ifAnyGranted="${activity_manage_delete.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${activity_manage_delete.indexCode}'";
			        		   		content += " title='${activity_manage_delete.name}'>";
			        		   		content += "<img src='${base}/${skin}/${activity_manage_delete.icon}' weight='18' height='18' border='0' align='absmiddle'/>${activity_manage_delete.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty activity_manage_statistics}">
			        		   <security:authorize ifAnyGranted="${activity_manage_statistics.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${activity_manage_statistics.indexCode}'";
			        		   		content += " title='${activity_manage_statistics.name}'>";
			        		   		content += "<img src='${base}/${skin}/${activity_manage_statistics.icon}' weight='18' height='18' border='0' align='absmiddle'/>${activity_manage_statistics.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty activity_manage_switch}">
			        		   <security:authorize ifAnyGranted="${activity_manage_switch.code}">
			        		   		if(rowdata.status==1){
				        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${activity_manage_switch.indexCode}'";
				        		   		content += " title='${activity_manage_switch.name}'>";
				        		   		content += "<img src='${base}/${skin}/${activity_manage_switch.icon}' weight='18' height='18' border='0' align='absmiddle'/>${activity_manage_switch.name}";
				        		   		content += "</a>";
			        		   		}else if(rowdata.status==2){
				        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${activity_manage_switch.indexCode}'";
				        		   		content += " title='活动结束'>";
				        		   		content += "<img src='${base}/${skin}/${activity_manage_switch.icon}' weight='18' height='18' border='0' align='absmiddle'/>活动结束";
				        		   		content += "</a>";
			        		   		}
			        		   </security:authorize>
			        		   </c:if>
			        		   return content;
			        	   }
			           }],
			mtype: "POST",
			postData: {c:"${activity.c}",type:"${activity.type}",name:"${activity.name}"},
			rowNum: 10,
			page: "${activity.page}",
			rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#pagered',
			height: '245',
			autowidth: true,
			viewrecords: true,
			multiselect: true,
			jsonReader: {
				repeatitems: false
			},
			caption: '活动列表',
			toolbar: [true,"top"]
		});
		
		//修改
		$(".shortcut_${activity_manage_modify.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			window.location.href = "${base}${activity_manage_modify.controller}?c=${activity_manage_modify.code}&id=" + rowid;
		});
		
		//删除
		$(".shortcut_${activity_manage_delete.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var rowdata = jQuery("#activityList").jqGrid('getRowData',rowid);
			var prompt = "<msg:message code='info.prompt.delete.confirm'/>";
			var url = "${base}${activity_manage_delete.controller}.json?c=${activity_manage_delete.code}&id=" + rowid;
			var index = $.layer({
				shade    : false,
				dialog   : {
					msg  : prompt,
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
										layer.close(index);
										$("#activityList").trigger("reloadGrid");
									}
								}
							}
						});
					},
					no   : function(){
						layer.close(index);
					}
				}
			});
		});
		
		//统计
		$(".shortcut_${activity_manage_statistics.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var rowdata = jQuery("#activityList").jqGrid('getRowData',rowid);
			var templetName = rowdata.templetName;
			var name = rowdata.name;
			var startTime = rowdata.startTime;
			var endTime = rowdata.endTime;
			window.location.href = "${base}${activity_manage_statistics.controller}?c=${activity_manage_statistics.code}&activityId="
									+ rowid+"&activityTempletName="+templetName+"&activityName="+name+"&activityStartTime="+startTime+"&activityEndTime="+endTime;
		});
		
		//活动开始或结束
		$(".shortcut_${activity_manage_switch.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var rowdata = jQuery("#activityList").jqGrid('getRowData',rowid);
			var prompt = "确认要开始活动吗？";
			var status = 2;
			if(rowdata.status=='已开始'){
				prompt = "确认要结束活动吗？";
				status = 3;
			}
			var index = $.layer({
				shade    : false,
				dialog   : {
					msg  : prompt,
					btns : 2,
					type : 4,
					btn  : ["<msg:message code='button.confirm'/>", "<msg:message code='button.cancel'/>"],
					yes  : function(){
						$.ajax({
							url:'${base}${activity_manage_switch.controller}.json',
							type:'post',
							timeout:'60000',
							dataType:'json',
							data:{c:'${activity_manage_switch.code}',id:rowid,status:status},
							success:function(jsonData,textStatus){
								if(textStatus == "success"){
									if(jsonData.status == "success"){
										layer.close(index);
										$("#activityList").trigger("reloadGrid");
									}
								}
							}
						});
					},
					no   : function(){
						layer.close(index);
					}
				}
			});
		});
		
		<c:if test="${!empty activity_manage_create}">
		<security:authorize ifAnyGranted="${activity_manage_create.code}">
			var $content = $("<a></a>").attr("href","javascript:void(0)")
						   .attr("id","${activity_manage_create.indexCode}")
						   .append($("<img/>").attr("src","${base}/${skin}/${activity_manage_create.icon}")
						   .attr("width","18").attr("height","18").attr("border",0)
						   .attr("border","0").attr("align","absmiddle"))
						   .append("${activity_manage_create.name}");
			$("#t_activityList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
			$("#${activity_manage_create.indexCode}","#t_activityList").click(function(){
				window.location.href = "${base}${activity_manage_create.controller}?c=${activity_manage_create.code}";
			});
		</security:authorize>
		</c:if>
		<c:if test="${!empty activity_manage_delete}">
		<security:authorize ifAnyGranted="${activity_manage_delete.code}">
			var $content = $("<a></a>").attr("href","javascript:void(0)")
						   .attr("id","${activity_manage_delete.indexCode}")
						   .append($("<img/>").attr("src","${base}/${skin}/${activity_manage_delete.icon}")
						   .attr("width","18").attr("height","18").attr("border",0)
						   .attr("border","0").attr("align","absmiddle"))
						   .append("${activity_manage_delete.name}");
			$("#t_activityList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
			$("#${activity_manage_delete.indexCode}","#t_activityList").click(function(){
				var selrow = jQuery("#activityList").jqGrid("getGridParam","selarrrow");
				if (selrow != null && selrow != ""){
					var rowdata;
					var prompt = "<msg:message code='info.prompt.delete.confirm'/>";
					for ( var i = 0; i < selrow.length; i++) {
						rowdata = jQuery("#activityList").jqGrid('getRowData',selrow[i]);
					}
					var url = "${base}${activity_manage_delete.controller}.json?c=${activity_manage_delete.code}&ids=" + selrow;
					var index = top.$.layer({
				  	    shade  : [0.5 , '#000' , true],
				  	    area   : ['auto','auto'],
				  	  	offset : ['50px',''],
				  	    dialog : {
				  	    msg    : prompt,
				  	    btns   : 2, 
				  	    type   : 4,
				  	    btn    : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
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
											$("#activityList").trigger("reloadGrid");
										}
									}
								}
							});
				  	    },
				  	    no     : function(){
				  	        	top.layer.close(index);
				  	        }
		  	    		}
					});
				} else{
					top.$.layer({
						shade  : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title  : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"<msg:message code='info.prompt.delete'/>",type : 3}	
					});
				}
			});
		</security:authorize>
		</c:if>
	});
</script>
	</head>
	<body class="skinMain">
		<form:form action="manage" method="post" commandName="activity">
			<input type="hidden" name="c" value="${activity.c}"/>
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
				<tr>
					<td align="right" width="10%" class="search_info">
						活动类型：
					</td>
					<td align="left" width="30%" class="search_lable">
						<select name="type" id="type">
							<option value="">请选择</option>
							<c:forEach items="${activityType}" var="item">
								<c:if test="${activityType != null}">
									<c:choose>
										<c:when test="${item.id == activity.type}">
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
					<td align="right" width="10%" class="search_info">
						活动名称：
					</td>
					<td align="left" width="30%" class="search_lable">
						<form:input path="name" htmlEscape="true"/>
					</td>
					<td width="20%" class="button_area">
						<input type="submit" class="btn2" id="select" value="<msg:message code='button.search' />"/>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td>
						<table id="activityList" ><tr><td>&nbsp;</td></tr></table>
						<div id="pagered"></div>
					</td>
				</tr>
			</table>
		</form:form>
	</body>
</html>