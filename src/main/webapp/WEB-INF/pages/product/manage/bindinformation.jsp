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
        url: '${base}/product/manage/bindinformationGrid.json',
        datatype: 'json',
        colNames: ["","",
            "<msg:message code='information.title'/>",
            "<msg:message code='information.bindingStatus'/>",
            "<msg:message code='info.operate'/>"
	        ],
        colModel: [
            {name: 'productId',index: 'productId',width:'0%',align:'center',hidden: true,sortable: false},
            {name: 'id',index: 'id',width:'0%',align:'center',hidden: true,sortable: false},
            {name: 'title',index: 'title',width:'60%',align:'center',hidden: false,sortable: false},
            {name: 'bindingStatus',index: 'bindingStatus',width:'20%',align:'center',hidden: false,sortable: false,
            	formatter:function(cellvalue, options, rowdata){
	        		   var bindingStatus = rowdata.productId;
	        		   var content = "";
	        		   if(bindingStatus!=null && bindingStatus!=''){
	        			   content = "<msg:message code='binding.already'/>";
	        		   }else{
	        			   content = "<msg:message code='binding.notyet'/>";
	        		   }
	        		   return content;
	        	   }},
            {name:'act', index:'i.act', align:'left', width:'20%', sortable:false,
	        	   formatter:function(cellvalue, options, rowdata){
	        		   var id = rowdata.id;
	        		   var content = "";
	        		   <c:if test="${!empty product_manage_bindinformation_detail}">
	        		   <security:authorize ifAnyGranted="${product_manage_bindinformation_detail.code}">
	        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${product_manage_bindinformation_detail.indexCode}'";
	        		   		content += " title='${product_manage_bindinformation_detail.name}'>";
	        		   		content += "<img src='${base}/${skin}/${product_manage_bindinformation_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${product_manage_bindinformation_detail.name}";
	        		   		content += "</a>";
	        		   </security:authorize>
	        		   </c:if>
	        		   return content;
	        	   }
	           }],
        mtype:"POST",
        postData:{c:"${information.c}",productId:'${information.productId}',title:'${information.title}'},
        rowNum:10,     
        page:"${information.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:'auto',
        autowidth: true,
        viewrecords: true,
        multiselect: true,
        jsonReader: {
        	repeatitems: false
        },
        caption:'<msg:message code="information.list"/>',
        toolbar: [true,"top"]
    });
    
    $(".shortcut_${product_manage_bindinformation_detail.indexCode}").live("click",function(){
		var rowid = $(this).attr("id");
		window.location.href = "${base}${product_manage_bindinformation_detail.controller}?c=${product_manage_bindinformation_detail.code}&id=" + rowid;
	});
    
    jQuery("#productList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	//JQgrid 添加导航工具
	/* jQuery("#productList").jqGrid('navGrid','#pagered',
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
    
    jQuery("#productList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    
    //绑定按钮
    var $bi= $("<a></a>").attr("href","javascript:void(0)")
    .attr("id","${product_manage_bind.indexCode}")
    .attr("border","0")
    .append($("<img/>").attr("src","${base}/${skin}/${product_manage_bind.icon}")
 	.attr("width","18").attr("height","18").attr("border","0")
 	.attr("border","0").attr("align","absmiddle"))
 	.append("${product_manage_bind.name}").append("&nbsp;&nbsp;");
 
 	//取消绑定按钮
    var $unbi= $("<a></a>").attr("href","javascript:void(0)")
    .attr("id","${product_manage_unbind.indexCode}")
    .attr("border","0")
    .append($("<img/>").attr("src","${base}/${skin}/${product_manage_unbind.icon}")
 	.attr("width","18").attr("height","18").attr("border","0")
 	.attr("border","0").attr("align","absmiddle"))
 	.append("${product_manage_unbind.name}").append("&nbsp;&nbsp;");
    
  	//创建按钮
    var $ea = $("<a></a>").attr("href","javascript:void(0)")
	.attr("id","${product_manage_bindinformation_create.indexCode}")
	.attr("border","0").attr("class","${product_manage_bindinformation_create.indexCode}")
	.append($("<img/>").attr("src","${base}/${skin}/${product_manage_bindinformation_create.icon}")
  	.attr("width","18").attr("height","18").attr("border","0")
  	.attr("border","0").attr("align","absmiddle"))
  	.append("${product_manage_bindinformation_create.name}");
	$("#t_productList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($bi).append($unbi).append($ea));
	
	//绑定按钮 事件
	$("#${product_manage_bind.indexCode}").click(function(){
		//获取资讯的ids 数组
		var infromatinIds=jQuery("#productList").jqGrid("getGridParam","selarrrow");
		if(infromatinIds==null || infromatinIds.length<=0){
			top.$.layer({
				area : ['auto','auto'],
				dialog : {msg:'<msg:message code='NotEmpty.product.bind.information'/>',type : 8}	
			});
		}else{
		//询问框
		var k=$.layer({
			    shade : false,
			    area : ['auto','auto'],
			    dialog : {
			        msg:'<msg:message code='product.information.bind.isornot'/>',
			        btns : 2, 
			        type : 4,
			        btn : ['确定','取消'],
			        yes : function(){
			        	$.ajax({
							url: '${base}${product_manage_bind.controller}.json',
							type:'post',
							timeout:'60000',
							dataType:'json',
							data: {id:'${information.productId}',infromatinIds:$.toJSON(infromatinIds)}, 
							async:false,
							cache:false,
							success:function(jsonData){
								if(jsonData.zt=='success'){
									layer.close(k);
									top.$.layer({
	    								area : ['auto','auto'],
	    								dialog : {msg:'<msg:message code='product.information.bind.success'/>',type : 9}	
	    							});
									jQuery("#productList").jqGrid().trigger("reloadGrid");
								}else{
									layer.close(k);
									top.$.layer({
	    								area : ['auto','auto'],
	    								dialog : {msg:'<msg:message code='product.information.bind.error'/>',type : 8}	
	    							});
								}
							}
						});
			        },
			        no : function(){
			        	jQuery("#productList").jqGrid().trigger("reloadGrid");
			            layer.close(k);
			        }
			    }
			});
		}
	});
	//取消绑定按钮 事件
	$("#${product_manage_unbind.indexCode}").click(function(){
		//获取资讯的ids 数组
		var infromatinIds=jQuery("#productList").jqGrid("getGridParam","selarrrow");
		if(infromatinIds==null || infromatinIds.length<=0){
			top.$.layer({
				area : ['auto','auto'],
				dialog : {msg:'<msg:message code='NotEmpty.product.unbind.information'/>',type : 8}	
			});
		}else{
			var k=$.layer({
			    shade : false,
			    area : ['auto','auto'],
			    dialog : {
			        msg:'<msg:message code='product.information.unbind.isornot'/>',
			        btns : 2, 
			        type : 4,
			        btn : ['确定','取消'],
			        yes : function(){
			        	$.ajax({
							url: '${base}${product_manage_unbind.controller}.json',
							type:'post',
							timeout:'60000',
							dataType:'json',
							data: {id:'${information.productId}',infromatinIds:$.toJSON(infromatinIds)}, 
							async:false,
							cache:false,
							success:function(jsonData){
								if(jsonData.zt=='success'){
									layer.close(k);
									top.$.layer({
	    								area : ['auto','auto'],
	    								dialog : {msg:'<msg:message code='product.information.unbind.success'/>',type : 9}	
	    							});
									jQuery("#productList").jqGrid().trigger("reloadGrid");
								}else{
									layer.close(k);
									top.$.layer({
	    								area : ['auto','auto'],
	    								dialog : {msg:'<msg:message code='product.information.unbind.error'/>',type : 8}	
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
		}
	});
	
    //创建按钮绑定事件
	$(".${product_manage_bindinformation_create.indexCode}").click(function(){
		//访问创建资讯的接口连接地址
		window.location.href='${base}${product_manage_bindinformation_create.controller}?c=${product_manage_bindinformation_create.code}&productId=${information.productId}';
	});
	
    //查询按钮
    $("#select").click(function(){
    	jQuery("#productList").jqGrid("setGridParam",{postData:{title:$("#title").val()}}).trigger("reloadGrid");    
    });
});

</script>
	</head>
	<body class="skinMain">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
			<tr>
				<td width="100%">
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td align="center">
							<form:form method="post" action="bindinformation" commandName="information" name="f">
							<input type="hidden" name="c" value="${product_manage_bindinformation.code}"/>
							<input type="hidden" name="productId" value="${information.productId}"/>
								<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
									<tr>
										<td align="right" width="10%" class="search_info"><msg:message code='information.title'/><msg:message code="system.common.sign.colon"/></td>
										<td align="left" width="30%"  class="search_lable">
											<form:input path="title" id="title" htmlEscape="true" />
										</td>
										<td width="20%" class="button_area" align="center">
											<input type="button" class="btn2" id="select" value="<msg:message code='button.search'/>">
										</td>
									</tr>	
								</table>
							</form:form>
							</td>
						</tr>
						<tr>
							<td>
								<table id="productList"><tr><td>&nbsp;</td></tr></table>
								<div id="pagered"></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div class="toolbar">
	<div class="left"></div>
	<div class="center">
		<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
	</div>
	<div class="right"></div>
</div>
	</body>
</html>