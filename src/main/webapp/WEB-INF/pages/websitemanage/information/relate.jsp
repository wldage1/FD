<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
<script type="text/javascript">
	$(document).ready(function(){
		jQuery("#productList").jqGrid({
			url: 'product/grid.json',
			datatype: 'json',
			colNames: ["<msg:message code='product.code'/>",
			           "<msg:message code='product.shortName'/>",
			           "<msg:message code='product.type'/>",
			           "<msg:message code='provider.shortName'/>",
			           "<msg:message code='info.operate'/>"],
			colModel: [{name:'code', index:'p.code', align:'center',width:'20%', hidden:false, sortable:false},
			           {name:'shortName', index:'p.shortName', align:'center',width:'30%', hidden:false, sortable:false},
			           {name:'type', index:'p.type', align:'center',width:'20%', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
		                	   var content="";
		                	   	<c:forEach var="item" items="${productType}">
									   if(cellvalue==${item.key}){
										   content="${item.value}";
									   }
								</c:forEach>
								return content;   
						   }
			           },
			           {name:'provider.shortName', index:'p.provider.shortName', align:'center',width:'20%', hidden:false, sortable:false},
			           {name:'act', index:'p.act', align:'left', width:'10%', sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   var id = rowdata.id;
			        		   var content = "";
			        		   <c:if test="${!empty websitemanage_information_setrelate}">
			        		   <security:authorize ifAnyGranted="${websitemanage_information_setrelate.code}">
			        		   		if(id == '${productInformation.productID}'){
				        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${websitemanage_information_setrelate.indexCode}'";
				        		   		content += " title='<msg:message code='information.removeRelate'/>'>";
				        		   		content += "<img src='${base}/${skin}/${websitemanage_information_setrelate.icon}' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='information.removeRelate'/>";
				        		   		content += "</a>";
			        		   		}else{
				        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${websitemanage_information_setrelate.indexCode}'";
				        		   		content += " title='${websitemanage_information_setrelate.name}'>";
				        		   		content += "<img src='${base}/${skin}/${websitemanage_information_setrelate.icon}' weight='18' height='18' border='0' align='absmiddle'/>${websitemanage_information_setrelate.name}";
				        		   		content += "</a>";
			        		   		}
			        		   </security:authorize>
			        		   </c:if>
			        		   return content;
			        	   }
			           }],
			mtype: "POST",
			postData: {c:"${information.c}", productType:"${information.productType}", productName:"${information.productName}"},
			rowNum: 15,
			pager: '#pagered',
			height: '<msg:message code='jqgrid.height.350'/>',
			autowidth: true,
			viewrecords: true,
			scroll: true,
			jsonReader: {
				repeatitems: false
			},
			gridComplete: function(){
				if('${productInformation.productID}' != ''){
					$("#${productInformation.productID}").attr("class","ui-widget-content jqgrow ui-row-ltr ui-state-highlight");
				}
			},
			onSelectRow:function(rowid,status){
				if('${productInformation.productID}' != ''){
					$("#productList").setSelection('${productInformation.productID}');
				}
	    	},
			caption: '<msg:message code="product.list"/>',
			toolbar: [true,"bottom"]
		});
		
		$(".shortcut_${websitemanage_information_setrelate.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			if(rowid != '${productInformation.productID}'){
				var url = "${base}${websitemanage_information_setrelate.controller}.json?c=${websitemanage_information_setrelate.code}&id=${information.id}&productId=" + rowid;
				var index = $.layer({
					shade    : false,
					dialog   : {
						msg  : "<msg:message code='relate.sure'/>",
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
										}
									}
								}
							});
						},
						no   : function(){
							layer.close(index);
						}
					},
					end 	 : function(){window.location.href='${base}${websitemanage_information_relate.controller}?c=${websitemanage_information_relate.code}&id=${information.id}';}
				});
			}else{
				var url = "${base}${websitemanage_information_setrelate.controller}.json?c=${websitemanage_information_setrelate.code}&id=${information.id}";
				var index = $.layer({
					shade    : false,
					dialog   : {
						msg  : "<msg:message code='cancelRelate.sure'/>",
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
										}
									}
								}
							});
						},
						no   : function(){
							layer.close(index);
						}
					},
					end 	 : function(){window.location.href='${base}${websitemanage_information_relate.controller}?c=${websitemanage_information_relate.code}&id=${information.id}';}
				});
			}
		});
		//返回按钮
		var $back = $("<a></a>").attr("href","javascript:void();")
		  .attr("id","back")
		  .append($("<img/>").attr("src","${base}/${skin}/images/icon/back.png")
		  .attr("width","18").attr("height","18").attr("border","0")
		  .attr("border","0").attr("align","absmiddle"))
		  .append("<msg:message code="button.back"/>");    
		$("#t_productList").append("&nbsp;&nbsp;").append(
			$("<span></span>")
			.attr("class","jqgridContainer")
			.append($back)
		);
		$("#t_productList #back").click(function(){
			window.location.href='${base}${websitemanage_information.controller}?c=${websitemanage_information.code}&back=1';
		});
	});
</script>
	</head>
	<body class="skinMain">
		<form:form action="relate" method="post" commandName="information">
			<input type="hidden" name="c" value="${information.c}">
			<input type="hidden" name="id" value="${information.id}">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td align="center">
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
							<tr>
								<td align="right" width="10%" class="search_info">
									<msg:message code="product.type"/>
								</td>
								<td align="left" width="30%" class="search_lable">
									<select name="productType">
										<option value="">--请选择--</option>
										<c:forEach items="${productType}" var="item">
											<c:if test="${productType != null}">
												<c:choose>
													<c:when test="${item.key == information.productType}">
														<option value="${item.key}" selected="selected">${item.value}</option>
													</c:when>
													<c:otherwise>
														<option value="${item.key}">${item.value}</option>
													</c:otherwise>
												</c:choose>
											</c:if>
										</c:forEach>
									</select>
								</td>
								<td align="right" width="10%" class="search_info">
									<msg:message code="product.shortName"/>
								</td>
								<td align="left" width="30%" class="search_lable">
									<form:input type="text" path="productName"/>
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
						<table id="productList"><tr><td>&nbsp;</td></tr></table>
						<div id="pagered"></div>
					</td>
				</tr>
			</table>
		</form:form>
	</body>
</html>