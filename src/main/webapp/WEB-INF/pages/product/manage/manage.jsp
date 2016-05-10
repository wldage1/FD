﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#productList").setGridWidth($(window).width()*0.999);　
});　
var zTree = ""; 
$(document).ready(function(){
    jQuery("#productList").jqGrid({
        url: 'manage/grid.json',
        datatype: 'json',
        colNames: ["",
            "<msg:message code='product.shortName'/>",
	        "<msg:message code='product.code'/>",
	        "<msg:message code='product.type'/>", 
	        "<msg:message code='product.checkstatus'/>",
	        "<msg:message code='product.sellStatus'/>",
	        "<msg:message code='product.provider' />",
	        "<msg:message code='product.released'/>",
	        "<msg:message code='product.organization.name' />",
	        "<msg:message code='product.createUserName' />",
	        "<msg:message code='product.operate'/>",
	        "","","","","","",""
	        ],
        colModel: [
            {name: 'id',index: 'id',align:'center',hidden: true,sortable: false},
            {name: 'shortName',index: 'shortName',width:'15%',align:'center',hidden: false,sortable: false,formatter:preview}, 
			{name: 'code',index: 'code',width:'10%',align:'center',hidden: false,sortable: false},
			{name: 'type',index: 'type',width:'10%',align:'center',hidden: false,sortable:false,formatter:changeType},
			{name: 'status',index: 'status',width:'10%',align:'center',hidden: false,sortable:false,formatter:changeStatus},
			{name: 'sellStatus',index: 'sellStatus',width:'10%',align:'center',hidden: false,sortable:false,formatter:changeSellStatus},
			{name: 'providerShortName',index: 'providerShortName',width:'10%',align:'center',hidden: false,sortable:false},
			{name: 'released',index: 'released',width:'10%',align:'center',hidden: false,sortable: false,formatter:changeData},
			{name: 'orgName',index: 'orgName',width:'15%',align:'center',hidden: false,sortable: false},
			{name: 'createUserName',index: 'createUserName',width:'7%',align:'center',hidden: false,sortable: false},
			{name:'act',index:'act', width:'56%',align:'center',hidden:false,sortable:false,formatter:initAct},
			{name: 'status',index: 'statusTwo',align:'center',hidden: true,sortable:false},
			{name: 'sellStatus',index: 'sellStatusTwo',align:'center',hidden: true,sortable:false},
			{name: 'released',index: 'releasedTwo',align:'center',hidden: true,sortable: false},
			{name: 'orgId',index: 'orgId',align:'center',hidden: true,sortable: false},
			{name: 'type',index:'type',hidden: true},
			{name: 'providerId',index:'providerId',hidden: true},
			{name: 'shortName',index:'shortNameTwo',hidden: true}],
        mtype:"POST",
        postData:{c:"${product.c}",shortName:"${product.shortName}",type:"${product.type}",status:"${product.status}",sellStatus:"${product.sellStatus}",released:"${product.released}",code:"${product.code}"},
        rowNum:10,     
        page:"${product.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:'auto',
        autowidth: true,
        shrinkToFit:true,
        viewrecords: true,
        rownumbers: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        
    	loadComplete:function(){
    		//设置居间公司
    		$(".shortcut_${product_manage_setBetweenCompany.indexCode}").click(function(){
    			var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
    			//页面层例二
    			var i = top.$.layer({
    				type: 1,
    				title: '<msg:message code="product.organization.set"/>',
    				shadeClose: true, 
    				closeBtn: [0,true],
    				border : [5, 0.5, '#666', true],
    				offset: ['50px',''],
    				move: ['.xubox_title', true],
    				area: ['420px','190px'],
    				page: {
    					html: 
    						'<table width="420" border="0" cellspacing="0" cellpadding="0">'+
    						'<tr>'+
    						'<td width="100%" class="skinLayout_info" colspan="2">'+
    							'<table width="99%" align="center" border="0" height="115" cellspacing="1" cellpadding="0" class="skinLayout">'+
    							'<tr>'+
    							'<td align="right" width="35%">'+
    								'<msg:message code="product.shortName" /><msg:message code="system.common.sign.colon"/>'+
    							'</td>'+
    							'<td align="left" width="65%">'+rowdata.shortName+'</td>'+
    							'</tr><tr>'+
    							'<td align="right" width="35%">'+
    								'<msg:message code="product.organization.name" /><msg:message code="system.common.sign.colon"/>'+
    							'</td>'+
    							'<td align="left" width="65%">'+
	    							'<select id="commissionID" style="width: 170px;">'+
	    								'<option value="">--请选择--</option>'+
			    						<c:forEach items="${commissionList}" var="it">
			    							'<option value="${it.id}">${it.name}</option>'+
						    			</c:forEach>
    								'</select>'+
    							'</td>'+
    							'</tr>'+
    							'</table>'+
    							'</td></tr>'+
    							'<tr height="40" bgcolor="#CCDCFF">'+
    							'<td align="right" width="50%"><input type="button" class="btn2" id="updateCommissionID" value="<msg:message code="product.confirm.button" />" />&nbsp;&nbsp;</td>'+
    							'<td width="50%" >&nbsp;&nbsp;<input type="button" class="btn2" id="closeSta" value="<msg:message code="order.fail.button" />"/></td></tr>'+
   							'</tr>'+
   							'</table>'
    				}
    			});
    			//设置居间公司连带效果
    			$("#commissionID",window.parent.document).val(rowdata.orgId)
    			//设置居间公司确认按钮
        		$("#updateCommissionID",window.parent.document).click(function(){
        			var commissionID=$("#commissionID",window.parent.document).val()
        			if(commissionID==null || commissionID==''){
        				//信息
    					top.$.layer({
    						area : ['auto','auto'],
    						dialog : {msg:'<msg:message code="product.organization.set.notnull"/>',type : 8}	
    					});
        			}else{
        				//判断居间公司是否有居间协议  如果有是否处于正常状态 2正常
        				$.ajax({
        					url: '${base}/product/manage/checkContract.json',
        					type:'post',
        					timeout:'60000',
        					dataType:'json',
        					data: {orgID:commissionID}, 
        					async:false,
        					cache:false,
        					success:function(jsonData){
        						if(jsonData.status=='success'){
        							if(jsonData.jsonObject.status==0){//0代表无居间协议
            							top.$.layer({
            								area : ['auto','auto'],
            								dialog : {msg:'<msg:message code="product.organization.deal.notnull"/>',type : 8}	
            							});
        							}else if(jsonData.jsonObject.status==1){//1代表正常
        								$.ajax({
        		        					url: '${base}/product/manage/updateCommissionOrgID.json',
        		        					type:'post',
        		        					timeout:'60000',
        		        					dataType:'json',
        		        					data: {id:rowdata.id,orgId:commissionID}, 
        		        					async:false,
        		        					cache:false,
        		        					success:function(jsonData){
        		        						if(jsonData.zt=='success'){
        		        							top.layer.close(i);
        		        							//信息
        		        							top.$.layer({
        		        								area : ['auto','auto'],
        		        								dialog : {msg:'<msg:message code="product.organization.set.success"/>',type : 9}	
        		        							});
        		        							jQuery("#productList").jqGrid().trigger("reloadGrid");
        		        						}else{
        		        							top.layer.close(i);
        		        							top.$.layer({
        		        								area : ['auto','auto'],
        		        								dialog : {msg:'<msg:message code="product.organization.set.error"/>',type : 8}	
        		        							});
        		        						}
        		        					}
        		        				});
        							}else{//2代表居间协议有  但是不可用
        								top.$.layer({
            								area : ['auto','auto'],
            								dialog : {msg:'<msg:message code="product.organization.deal.stop"/>',type : 8}	
            							});
        							}
        						}else{
        							top.layer.close(i);
        							top.$.layer({
        								area : ['auto','auto'],
        								dialog : {msg:'<msg:message code="product.organization.set.error"/>',type : 8}	
        							});
        						}
        					}
        				});
        			}
        		});
    			
    			//关闭按钮事件 
    			$("#closeSta",window.parent.document).live("click",function(){
    				top.layer.close(i);
    			});
    			
    		});
    		
    		//查看按钮 连接绑定事件
    		$(".shortcut_${product_manage_detail.indexCode}").click(function(){
    			var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
    			window.location.href='${base}${product_manage_detail.controller}?c=${product_manage_detail.code}&id='+this.id+'&type='+rowdata.type;
    		});
    		//开启按钮 功能事件
    		$(".shortcut_${product_manage_release.indexCode}").click(function(){
    			var id=this.id;
    			var rowdata = jQuery("#productList").jqGrid('getRowData',id);
    			jQuery("#productList").jqGrid("setSelection",id,false); 
    			var k=$.layer({
    			    shade : false,
    			    area : ['auto','auto'],
    			    dialog : {
    			        msg:'<msg:message code="product.status.release.isornot"/>',
    			        btns : 2, 
    			        type : 4,
    			        btn : ['<msg:message code="product.confirm.button"/>','<msg:message code="product.cancel.button"/>'],
    			        yes : function(){
    			        	$.ajax({
    	    					url: '${base}${product_manage_release.controller}.json',
    	    					type:'post',
    	    					timeout:'60000',
    	    					dataType:'json',
    	    					data: {id:id,released:1,c:'${product_manage_release.code}',prompt:'name',name:rowdata.shortName}, 
    	    					async:false,
    	    					cache:false,
    	    					success:function(jsonData){
    	    						layer.close(k);
    	    						if(jsonData.status=='success'){
    	    							top.$.layer({
    	    								area : ['auto','auto'],
    	    								dialog : {msg:'<msg:message code="product.status.release.success"/>',type : 9}	
    	    							});
    	    							jQuery("#productList").jqGrid().trigger("reloadGrid");
    	    						}else{
    	    							top.$.layer({
    	    								area : ['auto','auto'],
    	    								dialog : {msg:'<msg:message code="product.status.release.fail"/>',type : 8}	
    	    							});
    	    						}
    	    					}
    	    				});
    			        },
    			        no : function(){
    			            layer.close(k);
    			        }
    			    }
    			});
    		});
    		//关闭按钮 功能事件
    		$(".shortcut_${product_manage_notrelease.indexCode}").click(function(){
    			var id=this.id;
    			var rowdata = jQuery("#productList").jqGrid('getRowData',id);
    			jQuery("#productList").jqGrid("setSelection",id,false); 
    			var k=$.layer({
    			    shade : false,
    			    area : ['auto','auto'],
    			    dialog : {
    			        msg:'<msg:message code="product.status.notrelease.isornot"/>',
    			        btns : 2, 
    			        type : 4,
    			        btn : ['确定','取消'],
    			        yes : function(){
    			        	$.ajax({
    	    					url: '${base}${product_manage_notrelease.controller}.json',
    	    					type:'post',
    	    					timeout:'60000',
    	    					dataType:'json',
    	    					data: {id:id,released:0,c:'${product_manage_notrelease.code}',prompt:'name',name:rowdata.shortName}, 
    	    					async:false,
    	    					cache:false,
    	    					success:function(jsonData){
    	    						layer.close(k);
    	    						if(jsonData.status=='success'){
    	    							top.$.layer({
    	    								area : ['auto','auto'],
    	    								dialog : {msg:'<msg:message code="product.status.notrelease.success"/>',type : 9}	
    	    							});
    	    							jQuery("#productList").jqGrid().trigger("reloadGrid");
    	    						}else{
    	    							top.$.layer({
    	    								area : ['auto','auto'],
    	    								dialog : {msg:'<msg:message code="product.status.notrelease.fail"/>',type : 8}	
    	    							});
    	    						}
    	    					}
    	    				});
    			        },
    			        no : function(){
    			            layer.close(k);
    			        }
    			    }
    			});
    		});
    		//审核按钮 连接绑定事件
    		$(".shortcut_${product_manage_examine.indexCode}").click(function(){
				window.location.href='${base}${product_manage_examine.controller}?c=${product_manage_examine.code}&id='+this.id;				  
    		});
			//修改按钮 连接绑定事件
    		$(".shortcut_${product_manage_modify.indexCode}").click(function(){
				var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
				window.location.href='${base}${product_manage_modify.controller}?c=${product_manage_modify.code}&id='+this.id+"&type="+rowdata.type+"";				  
    		});
			//高级修改按钮 连接绑定事件
    		$(".shortcut_${product_manage_modifyall.indexCode}").click(function(){
				var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
				window.location.href='${base}${product_manage_modifyall.controller}?c=${product_manage_modifyall.code}&id='+this.id+"&type="+rowdata.type+"";				  
    		});
			//资料上传按钮 连接绑定事件
			$(".shortcut_${product_manage_attachment.indexCode}").live("click",function(){
				var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
				window.location.href='${base}${product_manage_attachment.controller}?c=${product_manage_attachment.code}&id='+this.id;
			});
			//销售配置按钮 连接绑定事件
    		/**$(".shortcut_${product_manage_saleconfig.indexCode}").click(function(){
				var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
				window.location.href='${base}${product_manage_saleconfig.controller}?c=${product_manage_saleconfig.code}&id='+this.id+"&type="+rowdata.type+"";				  
    		});*/
    		//绑定资讯 连接绑定事件
    		$(".shortcut_${product_manage_bindinformation.indexCode}").click(function(){
    			window.location.href='${base}${product_manage_bindinformation.controller}?c=${product_manage_bindinformation.code}&productId='+this.id;
    		});
    		//指定销售机构
    		$(".shortcut_${product_manage_bindorganization.indexCode}").click(function(){
    			var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
    			window.location.href='${base}${product_manage_bindorganization.controller}?c=${product_manage_bindorganization.code}&productID='+this.id+"&name="+rowdata.shortName+"";
    		})
    		//状态变更按钮  连接绑定事件
    		$(".shortcut_${product_manage_updatestatus.indexCode}").click(function(){
    			var rowdata = jQuery("#productList").jqGrid('getRowData',this.id);
    			jQuery("#productList").jqGrid("setSelection",rowdata.id,false);    
    			//页面层例二
    			var i = top.$.layer({
    				type: 1,
    				title: '<msg:message code="product.title.status"/>',
    				shadeClose: true, 
    				closeBtn: [0,true],
    				border : [5, 0.5, '#666', true],
    				offset: ['50px',''],
    				move: ['.xubox_title', true],
    				area: ['520px','auto'],
    				page: {
    					html: '<table width="100%" border="0" height="200" cellspacing="1" cellpadding="0" class="skinMain">'+
    						'<tr>'+
    					'<td align="right" width="40%">'+
    						'<msg:message code="product.name" /><msg:message code="system.common.sign.colon"/>'+
    					'</td>'+
    					'<td align="left" width="60%">'+rowdata.name+'</td>'+
    				'</tr>'+
    				'<tr>'+
    					'<td align="right" width="40%">'+
    						'<msg:message code="product.sellStatus" /><msg:message code="system.common.sign.colon"/>'+
    					'</td>'+
    					'<td align="left" width="60%">'+
    						'<select id="productSellStatus" style="width: 170px;">'+
	    						<c:forEach items="${sellStatusMap}" var="it">
	    							'<option value="${it.key}">${it.value}</option>'+
				    			</c:forEach>
    						'</select>'+
    					'</td>'+
    				'</tr>'+
    				'<tr>'+
	    				'<td align="right" width="40%"><input type="button" id="updateStatus" value="<msg:message code="product.status.button" />"/>&nbsp;&nbsp;</td>'+
	    				'<td width="60%">&nbsp;&nbsp;<input type="button" id="closeSta" value="<msg:message code="product.close.button" />"/></td></tr>'+
    			'</table>'
    				}
    			});
    			//产品状态重现
    			$("#productSellStatus",window.parent.document).val(rowdata.sellStatus);
    			//关闭按钮事件 
    			$("#closeSta",window.parent.document).click(function(){
    				top.layer.close(i);
    			});
    			//变更按钮事件 
    			$("#updateStatus",window.parent.document).click(function(){
    				$.ajax({
    					url: '${base}${product_manage_updatestatus.controller}.json',
    					type:'post',
    					timeout:'60000',
    					dataType:'json',
    					data: {id:rowdata.id,sellStatus:parseInt($("#productSellStatus",window.parent.document).val())}, 
    					async:false,
    					cache:false,
    					success:function(jsonData){
    						if(jsonData.zt=='success'){
    							top.layer.close(i);
    							//信息
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code="product.sellStatus.update.success"/>',type : 9}	
    							});
    							jQuery("#productList").jqGrid().trigger("reloadGrid");
    						}else{
    							top.layer.close(i);
    							top.$.layer({
    								area : ['auto','auto'],
    								dialog : {msg:'<msg:message code="product.sellStatus.update.error"/>',type : 8}	
    							});
    						}
    					}
    				});
    			});
    		});
    		//推荐按钮 连接绑定事件
    		$(".shortcut_${product_manage_recommend.indexCode}").click(function(){
    			window.location.href='${base}${product_manage_recommend.controller}?c=${product_manage_recommend.code}&id='+this.id;
    		});
			
    	},
        caption:'<msg:message code="product.list"/>',
		toolbar: [true,"top"]
    });
	
	<c:if test="${!empty product_manage_create}">
		<security:authorize ifAnyGranted="${product_manage_create.code}">
		var $ea = $("<a></a>").attr("href","javascript:void(0)")
				  .attr("id","${product_manage_create.indexCode}")
				  .append($("<img/>").attr("src","${base}/${skin}/images/icon/create.png")
				  .attr("width","18").attr("height","18").attr("border","0")
				  .attr("border","0").attr("align","absmiddle"))
				  .append("${product_manage_create.name}");
		$("#t_productList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($ea)); 
		$("#${product_manage_create.indexCode}","#t_productList").click(function(){
			window.location.href = "${base}${product_manage_create.controller}?c=${product_manage_create.code}";
		});    
		</security:authorize>
    </c:if>
	
   // jQuery("#productList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	//JQgrid 添加导航工具
/* 	jQuery("#productList").jqGrid('navGrid','#pagered',
			{edit:false,add:false,del:false},
			{},
			{},
			{},
			{multipleSearch:true, multipleGroup:true, showQuery: true});
	jQuery("#productList").jqGrid('navButtonAdd','#pagered',{
	    caption: "Columns",
	    title: "Reorder Columns",
	    onClickButton : function (){
	        jQuery("#productList").jqGrid('columnChooser');
	    }
	}); */
    
    //jQuery("#productList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	
	if('${product.released}'==null || '${product.released}'==''){
		$("input[name='released']:last").attr("checked","checked");
	}
    
});

//修改发布状态
function changeData(cellvalue, options, rowObject){
	<c:forEach items="${releasedMap}" var="it">
	if(cellvalue==${it.key}){
		return '${it.value}';		
	}
</c:forEach>
}
//修改销售状态
function changeSellStatus(cellvalue, options, rowObject){
	<c:forEach items="${sellStatusMap}" var="it">
		if(cellvalue==${it.key}){
			return '${it.value}';		
		}
	</c:forEach>
}
//修改产品状态
function changeStatus(cellvalue, options, rowObject){
	<c:forEach items="${productStatusMap}" var="it">
		if(cellvalue==${it.key}){
			return '${it.value}';		
		}
	</c:forEach>
}
//修改产品类型
function changeType(cellvalue, options, rowObject){
	<c:forEach items="${productTypeMap}" var="it">
		if(cellvalue==${it.key}){
			return '${it.value}';		
		}
	</c:forEach>
}
//产品预览
function preview(cellvalue, options, rowObject){
	var id = rowObject.id;
	var providerId = rowObject.providerId;
	return "<a target='_blank' style='text-decoration:underline;' href='${previewUrl}"+"-"+id+"-"+providerId+"'>"+cellvalue+"</a>";
}

//初始化列表后的操作
function initAct(cellvalue, options, rowObject){
		var id = rowObject.id;
		var rowdata = rowObject;
		var content = "";
		var isCommission='${isCommission}';
		<!-- 查看 -->
		<c:if test="${!empty product_manage_detail}">
	    <security:authorize ifAnyGranted="${product_manage_detail.code}">
	    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_detail.indexCode}' style='color:black;' title='${product_manage_detail.name}'>";
	    	content += "<img src='${base}/${skin}/${product_manage_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_detail.name}";
	    	content += "</a>";
	    </security:authorize>
    	</c:if>
		
		<!-- 高级修改  -->
		<c:if test="${!empty product_manage_modifyall}">
		<security:authorize ifAnyGranted="${product_manage_modifyall.code}">
			content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_modifyall.indexCode}' style='color:black;' title='${product_manage_modifyall.name}'>";
			content += "<img src='${base}/${skin}/${product_manage_modifyall.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_modifyall.name}";
			content += "</a>";
		</security:authorize>
		</c:if>
		
		<!-- 资料上传 -->
		<c:if test="${!empty product_manage_attachment}">
		<security:authorize ifAnyGranted="${product_manage_attachment.code}">
			content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_attachment.indexCode}' style='color:black;' title='${product_manage_attachment.name}'>";
			content += "<img src='${base}/${skin}/${product_manage_attachment.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_attachment.name}";
			content += "</a>";
		</security:authorize>
		</c:if>
		
		<!-- 指定产品的销售机构 -->
    	<c:if test="${!empty product_manage_bindorganization}">
		    <security:authorize ifAnyGranted="${product_manage_bindorganization.code}">
		    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_bindorganization.indexCode}' style='color:black;' title='${product_manage_bindorganization.name}'>";
		    	content += "<img src='${base}/${skin}/${product_manage_bindorganization.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_bindorganization.name}";
		    	content += "</a>";
		    </security:authorize>
    	</c:if>
		if(rowdata.saleStatus != 7 ){
		//判断是否为居间公司
		if(isCommission==0 && isCommission!=1){
			<!-- 设置居间公司 -->
			//判断是否审核
			if(rowdata.status==1){
				<c:if test="${!empty product_manage_setBetweenCompany}">
			    <security:authorize ifAnyGranted="${product_manage_setBetweenCompany.code}">
			    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_setBetweenCompany.indexCode}' style='color:black;' title='${product_manage_setBetweenCompany.name}'>";
			    	content += "<img src='${base}/${skin}/${product_manage_setBetweenCompany.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_setBetweenCompany.name}";
			    	content += "</a>";
			    </security:authorize>
		    	</c:if>
			}
		}
		
    	if(rowdata.status==1 && rowdata.orgId!=null){//判断是否已经审核  不等于3代表未审核
    		<!-- 审核 -->
  				<c:if test="${!empty product_manage_examine}">
    		    <security:authorize ifAnyGranted="${product_manage_examine.code}">
	   		    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_examine.indexCode}' style='color:black;' title='${product_manage_examine.name}'>";
	   		    	content += "<img src='${base}/${skin}/${product_manage_examine.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_examine.name}";
	   		    	content += "</a>";
    		    </security:authorize>
  		    	</c:if>
    	}else if(rowdata.status==2){
    		if(rowdata.released==0){
    			<!-- 开启 -->
    			<c:if test="${!empty product_manage_release}">
    		    <security:authorize ifAnyGranted="${product_manage_release.code}">
	   		    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_release.indexCode}' style='color:black;' title='${product_manage_release.name}'>";
	   		    	content += "<img src='${base}/${skin}/${product_manage_release.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_release.name}";
	   		    	content += "</a>";
    		    </security:authorize>
		    	</c:if>
    		}
    		else{
    			<!-- 关闭 -->
    			<c:if test="${!empty product_manage_notrelease}">
    		    <security:authorize ifAnyGranted="${product_manage_notrelease.code}">
	   		    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_notrelease.indexCode}' style='color:black;' title='${product_manage_notrelease.name}'>";
	   		    	content += "<img src='${base}/${skin}/${product_manage_notrelease.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_notrelease.name}";
	   		    	content += "</a>";
    		    </security:authorize>
		    	</c:if>
    		} 
    		/** 二次改动  属于需求变更导致 三次改动 14.3.31	**/
	    	<!-- 修改  -->
	    	<c:if test="${!empty product_manage_modify} && ${userType == 1}">
   		    <security:authorize ifAnyGranted="${product_manage_modify.code}">
   		    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_modify.indexCode}' style='color:black;' title='${product_manage_modify.name}'>";
   		    	content += "<img src='${base}/${skin}/${product_manage_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_modify.name}";
   		    	content += "</a>";
   		    </security:authorize>
	    	</c:if>
	    	/**
	    	<!-- 参数配置  -->
	    	<c:if test="${!empty product_manage_saleconfig}">
   		    <security:authorize ifAnyGranted="${product_manage_saleconfig.code}">
   		    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_saleconfig.indexCode}' style='color:black;' title='${product_manage_saleconfig.name}'>";
   		    	content += "<img src='${base}/${skin}/${product_manage_saleconfig.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_saleconfig.name}";
   		    	content += "</a>";
   		    </security:authorize>
	    	</c:if>
	    	*/
			
			/**
	    	<!-- 状态变更  -->
	    	<c:if test="${!empty product_manage_updatestatus}">
   		    <security:authorize ifAnyGranted="${product_manage_updatestatus.code}">
   		    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_updatestatus.indexCode}' style='color:black;' title='${product_manage_updatestatus.name}'>";
   		    	content += "<img src='${base}/${skin}/${product_manage_updatestatus.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_updatestatus.name}";
   		    	content += "</a>";
   		    </security:authorize>
	    	</c:if>
	    	*/
	    	<!-- 绑定资讯  -->
	    	<c:if test="${!empty product_manage_bindinformation}">
   		    <security:authorize ifAnyGranted="${product_manage_bindinformation.code}">
   		    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_bindinformation.indexCode}' style='color:black;' title='${product_manage_bindinformation.name}'>";
   		    	content += "<img src='${base}/${skin}/${product_manage_bindinformation.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_bindinformation.name}";
   		    	content += "</a>";
   		    </security:authorize>
	    	</c:if>
			/**
	    	<!-- 秒杀  -->
	    	<c:if test="${!empty product_manage_recommend}">
   		    <security:authorize ifAnyGranted="${product_manage_recommend.code}">
   		    	content += "<a href='#' id='" + id + "' class='shortcut_${product_manage_recommend.indexCode}' style='color:black;' title='${product_manage_recommend.name}'>";
   		    	content += "<img src='${base}/${skin}/${product_manage_recommend.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_recommend.name}";
   		    	content += "</a>";
   		    </security:authorize>
	    	</c:if>
			*/
			}
    	}
   		return content;
} 

</script>
	</head>
	<body class="skinMain">
		<table cellpadding="0" cellspacing="0" border="0" width="100%" class="search">
			<tr>
				<td align="center">
				<form:form method="post" action="manage" commandName="product" name="f">
				<input type="hidden" name="c" value="${product.c}"/>
					<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
						<tr>
							<td align="right" width="10%" class="search_info" height="25"><msg:message code='product.shortName'/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" width="30%"  class="search_lable">
								<form:input path="shortName" id="shortName" htmlEscape="true" />
							</td>
							<td align="right" width="10%" class="search_info"><msg:message code='product.type'/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" width="30%"  class="search_lable">
								<form:select path="type">
									<form:option value="">全部</form:option>
									<c:forEach items="${productTypeMap}" var="it">
										<form:option value="${it.key}">${it.value}</form:option>
									</c:forEach>
								</form:select>
							</td>
							<td rowspan="3" width="20%" class="button_area" align="center">
								<input type="submit" class="btn2" id="select" value="<msg:message code='button.search'/>">
							</td>
						</tr>	
						<tr>
							<td align="right" width="10%" class="search_info" height="25"><msg:message code='product.status'/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" width="30%"  class="search_lable">
								<form:select path="status">
									<form:option value="">全部</form:option>
									<c:forEach items="${productStatusMap}" var="it">
										<form:option value="${it.key}">${it.value}</form:option>
									</c:forEach>
								</form:select>
							</td>
							<td align="right" width="10%" class="search_info"><msg:message code='product.sellStatus'/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" width="30%"  class="search_lable">
								<form:select path="sellStatus">
									<form:option value="">全部</form:option>
									<c:forEach items="${sellStatusMap}" var="it">
										<c:if test="${it.key != 0}">
											<form:option value="${it.key}">${it.value}</form:option>
										</c:if>
									</c:forEach>
								</form:select>
							</td>
						</tr>	
						<tr>
							<td align="right" width="10%" class="search_info" height="25"><msg:message code='product.released'/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" width="30%"  class="button_area">
								<c:forEach items="${releasedMap}" var="it">
				&nbsp;&nbsp;&nbsp;&nbsp;<form:radiobutton  path="released"  value="${it.key}" />${it.value}
								</c:forEach>
								&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="released" value="" /><msg:message code="product.released.all"/>
							</td>
							<td align="right" width="10%" class="search_info" height="25"><msg:message code='product.code'/><msg:message code="system.common.sign.colon"/></td>
							<td align="left" width="30%"  class="search_lable">
								<form:input path="code" id="code" htmlEscape="true" />
							</td>
						</tr>	
					</table>
				</form:form>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td>
					<table id="productList"><tr><td>&nbsp;</td></tr></table>
					<div id="pagered"></div>
				</td>
			</tr>
		</table>
	</body>
</html>