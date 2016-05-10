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
	$("#informationList").setGridWidth($(window).width()*0.999);　
});
	$(document).ready(function(){
		var type = '${information.type}';
		jQuery("#typeList").jqGrid({
			url: 'type/grid.json',
			datatype: 'json',
			colNames: ["<msg:message code='type.name'/>"],
			colModel: [{name:'name', index:'t.name', align:'center', hidden:false, sortable:false}],
			mtype: "POST",
			postData: {c:"${information.c}"},
			rowNum: -1,
			height: '270',
			autowidth: true,
			jsonReader: {
				repeatitems: false
			},
			onSelectRow:function(rowid,status){
				type = rowid;
				jQuery("#informationList").jqGrid("setGridParam",{postData:{c:"${information.c}",type:rowid,title:""},page:1});
				jQuery("#informationList").trigger("reloadGrid");
	    	},
			caption: '<msg:message code="type.list"/>',
			toolbar: [true,"top"]
		});
		
		jQuery("#informationList").jqGrid({
			url: 'information/grid.json',
			datatype: 'json',
			colNames: ["<msg:message code='information.type'/>",
			           "<msg:message code='information.title'/>",
			           "<msg:message code='information.product'/>",
			           "<msg:message code='information.source'/>",
			           "<msg:message code='information.released'/>",
			           "<msg:message code='information.createTime'/>",
			           "<msg:message code='info.operate'/>"],
			colModel: [{name:'type', index:'i.type', align:'center', width:'15%', hidden:false, sortable:false,
							formatter:function(cellvalue, options, rowdata){
			             	   var content="";
			             	   	<c:forEach var="item" items="${informationType}">
									   if(cellvalue=='${item.id}'){
										   content="${item.name}";
									   }
								</c:forEach>
								return content;   
						   }
					   },
			           {name:'title', index:'i.title', align:'center', width:'15%', hidden:false, sortable:false},
			           {name:'productId', index:'i.productId', align:'center', width:'0%', hidden:true, sortable:false},
			           {name:'source', index:'i.source', align:'center', width:'15%', hidden:false, sortable:false},
			           {name:'released', index:'i.released', align:'center', width:'15%', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			             	   var content="";
			             	   	<c:forEach var="item" items="${isOrNot}">
									   if(cellvalue=='${item.key}'){
										   content="${item.value}";
									   }
								</c:forEach>
								return content;   
						   }
			           },
			           {name:'createTime', index:'i.createTime', align:'center', width:'15%', hidden:false, sortable:false},
			           {name:'act', index:'i.act', align:'left', width:'25%', sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   var id = rowdata.id;
			        		   var content = "";
			        		   <c:if test="${!empty websitemanage_information_detail}">
			        		   <security:authorize ifAnyGranted="${websitemanage_information_detail.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${websitemanage_information_detail.indexCode}'";
			        		   		content += " title='${websitemanage_information_detail.name}'>";
			        		   		content += "<img src='${base}/${skin}/${websitemanage_information_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${websitemanage_information_detail.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty websitemanage_information_modify}">
			        		   <security:authorize ifAnyGranted="${websitemanage_information_modify.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${websitemanage_information_modify.indexCode}'";
			        		   		content += " title='${websitemanage_information_modify.name}'>";
			        		   		content += "<img src='${base}/${skin}/${websitemanage_information_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${websitemanage_information_modify.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty websitemanage_information_relate}">
			        		   <security:authorize ifAnyGranted="${websitemanage_information_relate.code}">
				        		   if(rowdata.type=='8'){
				        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${websitemanage_information_relate.indexCode}'";
				        		   		content += " title='${websitemanage_information_relate.name}'>";
				        		   		content += "<img src='${base}/${skin}/${websitemanage_information_relate.icon}' weight='18' height='18' border='0' align='absmiddle'/>${websitemanage_information_relate.name}";
				        		   		content += "</a>";
					       		    }
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty websitemanage_information_delete}">
			        		   <security:authorize ifAnyGranted="${websitemanage_information_delete.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${websitemanage_information_delete.indexCode}'";
			        		   		content += " title='${websitemanage_information_delete.name}'>";
			        		   		content += "<img src='${base}/${skin}/${websitemanage_information_delete.icon}' weight='18' height='18' border='0' align='absmiddle'/>${websitemanage_information_delete.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   return content;
			        	   }
			           }],
			mtype: "POST",
			postData: {c:"${information.c}", type:"${information.type}", title:"${information.title}"},
			rowNum: 10,
			page: "${information.page}",
			rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#pagered',
			height: '245',
			autowidth: true,
			viewrecords: true,
			multiselect: true,
			jsonReader: {
				repeatitems: false
			},
			caption: '<msg:message code="information.list"/>',
			toolbar: [true,"top"]
		});
		
		$(".shortcut_${websitemanage_information_detail.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			window.location.href = "${base}${websitemanage_information_detail.controller}?c=${websitemanage_information_detail.code}&id=" + rowid;
		});
		
		$(".shortcut_${websitemanage_information_modify.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			window.location.href = "${base}${websitemanage_information_modify.controller}?c=${websitemanage_information_modify.code}&id=" + rowid;
		});
		
		$(".shortcut_${websitemanage_information_relate.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			window.location.href = "${base}${websitemanage_information_relate.controller}?c=${websitemanage_information_relate.code}&id=" + rowid;
		});
		
		$(".shortcut_${websitemanage_information_delete.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var rowdata = jQuery("#informationList").jqGrid('getRowData',rowid);
			var prompt = "<msg:message code='info.prompt.delete.confirm'/>";
			if(rowdata.productId!='' && rowdata.productId!=null){
				prompt = "<msg:message code='info.prompt.deleteRelate.confirm'/>";
			}
			var url = "${base}${websitemanage_information_delete.controller}.json?c=${websitemanage_information_delete.code}&id=" + rowid;
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
										$("#informationList").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
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
		
		<c:if test="${!empty websitemanage_information_create}">
		<security:authorize ifAnyGranted="${websitemanage_information_create.code}">
			var $content = $("<a></a>").attr("href","javascript:void(0)")
						   .attr("id","${websitemanage_information_create.indexCode}")
						   .append($("<img/>").attr("src","${base}/${skin}/${websitemanage_information_create.icon}")
						   .attr("width","18").attr("height","18").attr("border",0)
						   .attr("border","0").attr("align","absmiddle"))
						   .append("${websitemanage_information_create.name}");
			$("#t_informationList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
			$("#${websitemanage_information_create.indexCode}","#t_informationList").click(function(){
				window.location.href = "${base}${websitemanage_information_create.controller}?c=${websitemanage_information_create.code}&type=" + type;
			});
		</security:authorize>
		</c:if>
		<c:if test="${!empty websitemanage_information_delete}">
		<security:authorize ifAnyGranted="${websitemanage_information_delete.code}">
			var $content = $("<a></a>").attr("href","javascript:void(0)")
						   .attr("id","${websitemanage_information_delete.indexCode}")
						   .append($("<img/>").attr("src","${base}/${skin}/${websitemanage_information_delete.icon}")
						   .attr("width","18").attr("height","18").attr("border",0)
						   .attr("border","0").attr("align","absmiddle"))
						   .append("${websitemanage_information_delete.name}");
			$("#t_informationList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
			$("#${websitemanage_information_delete.indexCode}","#t_informationList").click(function(){
				var selrow = jQuery("#informationList").jqGrid("getGridParam","selarrrow");
				if (selrow != null && selrow != ""){
					var rowdata;
					var prompt = "<msg:message code='info.prompt.delete.confirm'/>";
					for ( var i = 0; i < selrow.length; i++) {
						rowdata = jQuery("#informationList").jqGrid('getRowData',selrow[i]);
						if(rowdata.productId!='' && rowdata.productId!=null){
							prompt = "<msg:message code='info.prompt.deleteRelate.confirm'/>";
						}
					}
					var url = "${base}${websitemanage_information_delete.controller}.json?c=${websitemanage_information_delete.code}&ids=" + selrow;
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
											$("#informationList").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
											//$("#informationList").trigger("reloadGrid");
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
		<form:form action="information" method="post" commandName="information">
			<input type="hidden" name="c" value="${information.c}"/>
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
				<tr>
					<td align="right" width="10%" class="search_info">
						<msg:message code="information.type"/>
					</td>
					<td align="left" width="30%" class="search_lable">
						<select name="type" id="type">
							<option value=""><msg:message code="informationType.pleaseSelect"/></option>
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
					<td align="right" width="10%" class="search_info">
						<msg:message code="information.title"/>
					</td>
					<td align="left" width="30%" class="search_lable">
						<form:input path="title" htmlEscape="true"/>
					</td>
					<td width="20%" class="button_area">
						<input type="submit" class="btn2" id="select" value="<msg:message code='button.search' />"/>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td width="20%" valign="top">
						<table id="typeList" ><tr><td>&nbsp;</td></tr></table>
					</td>
					<td width="80%" valign="top">
						<table id="informationList" ><tr><td>&nbsp;</td></tr></table>
						<div id="pagered"></div>
					</td>
				</tr>
			</table>
		</form:form>
	</body>
</html>