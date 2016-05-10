<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${base}/common/js/layer/layer.js"></script>
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#contractList").setGridWidth($(window).width()*0.999);　
});
$(window).resize(function(){ 　　
	$("#orgContractList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
	//////////////////////////////////////下面是协议管理的JS////////////////////////////////
    jQuery("#contractList").jqGrid({
        treeGridModel: 'adjacency',
        url: '${base}/commission/contract/grid.json',
        datatype: 'json',
        colNames: [
                  '<msg:message code="contract.orgID"/>',
                   '<msg:message code="contract.title"/>',
                   '<msg:message code="contract.status"/>',
                   '',
                   '<msg:message code="info.operate"/>'
                   ],
        colModel: [
                   {name: 'orgName',index: 'orgName',width:'30%',align:'center',hidden: false,sortable: false}, 
                   {name: 'title',index: 'title',width:'25%',align:'center',hidden: false,sortable: false}, 
                   {name: 'status',index:'status',width:'10%',align:'center',hidden: false,sortable: false,
                	  formatter:function(cellvalue, options, rowObject){
						var content="";
                	   	<c:forEach var="item" items="${agreementStatus}">
							   if(cellvalue==${item.key}){
								   content="${item.value}";
							   }
						</c:forEach>
						return content; 
					 }
                  },
                  {name: 'orgID',index: 'orgID',width:'20%',align:'center',hidden: true,sortable: false}, 
                   {name:'act',index:'act', width:'25%',align:'center',sortable:false,
                	  formatter:function(cellvalue, options, rowdata){
	                   			var content = "";
							   	var id = rowdata.id;
							   	var orgID = rowdata.orgID;
				    			var space = "&nbsp;&nbsp;";
				    			var content = "";
				    		    <c:if test="${!empty commission_contract_modify}">
				    		    <security:authorize ifAnyGranted="${commission_contract_modify.code}">
				    		    if (rowdata.status!=1){
				    		    	content += "<span style='color:#CFC09F;' title='${commission_contract_modify.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_modify.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${commission_contract_modify.name}' id='" + id + "' class='shortcut_${commission_contract_modify.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_modify.name}";
				   		    	    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty commission_contract_start}">
				    		    <security:authorize ifAnyGranted="${commission_contract_start.code}">
				    		    	if (rowdata.status==3){
				    		    		content += "<span style='color:#CFC09F;' title='${commission_contract_start.name}'><img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_start.name}</span>"; 
					    		    }
					    		    else if (rowdata.status==1){
						    		    content += "<a href='javascript:void(0);' id='" + id + "' orgID='" + orgID + "' class='shortcut_${commission_contract_start.indexCode}'";
					        		    content += " title='${commission_contract_start.name}' >";
					        		    content += "<img src='${base}/${skin}/images/icon/pause-business.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_start.name}";
					        		    content += "</a>";	
					    		    }else if (rowdata.status==2){
						    		    content += "<a href='javascript:void(0);' id='" + id + "' orgID='" + orgID + "' class='shortcut_${commission_contract_start.indexCode}'";
					        		    content += " title=<msg:message code='contract.stop'/> >";
					        		    content += "<img src='${base}/${skin}/images/icon/recover-business.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='contract.stop'/>";
					        		    content += "</a>";	
					    		    }
				    		    	
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty commission_contract_delete}">
				    		    <security:authorize ifAnyGranted="${commission_contract_delete.code}">
				    		    if (rowdata.status==2){
				    		    	content += "<span style='color:#CFC09F;' title='${commission_contract_delete.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_delete.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${commission_contract_delete.name}' id='" + id + "' class='shortcut_${commission_contract_delete.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_delete.name}";
				   		    	    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty commission_contract_detail}">
				    		    <security:authorize ifAnyGranted="${commission_contract_detail.code}">
					    		    content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${commission_contract_detail.indexCode}'";
				        		    content += " title='${commission_contract_detail.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_detail.name}";
				        		    content += "</a>";
				    		    </security:authorize>
				    		    </c:if>
				    		    return content;
	                   	}
                  }],
       	mtype: "POST",
		postData:{c:"${contract.c}",orgID:"${contract.orgID}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,   
        page:"${contract.page}",
        rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:<msg:message code='jqgrid.height.250'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	caption:'<msg:message code="sign.list"/>',
        toolbar: [true,"top"]
    });
     <c:if test="${!empty commission_contract_create}">
		<security:authorize ifAnyGranted="${commission_contract_create.code}">
			var $content = $("<a></a>").attr("href","javascript:void(0)")
						   .attr("id","${commission_contract_create.indexCode}")
						   .append($("<img/>").attr("src","${base}/${skin}/${commission_contract_create.icon}")
						   .attr("width","18").attr("height","18").attr("border",0)
						   .attr("border","0").attr("align","absmiddle"))
						   .append("${commission_contract_create.name}");
			
			$("#t_contractList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
			$("#${commission_contract_create.indexCode}","#t_contractList").click(function(){
				if ("${contract.orgID}"==""){
					top.layer.msg("<msg:message code='contract.message'/>");
					return false;
				}
				window.location.href = "${base}${commission_contract_create.controller}?c=${commission_contract_create.code}&orgID=${contract.orgID}";
			});
		</security:authorize>
	</c:if>
	
	<c:if test="${!empty commission_contract_agreement}">
		<security:authorize ifAnyGranted="${commission_contract_agreement.code}">
			$("#tab_2").show();
			$("#tab_2").click();
		</security:authorize>
	</c:if>
	<c:if test="${!empty commission_contract_contract}">
		<security:authorize ifAnyGranted="${commission_contract_contract.code}">
			$("#tab_1").show();
			$("#tab_1").click();
		</security:authorize>
	</c:if>
	jQuery("#contractList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	$(".shortcut_${commission_contract_modify.indexCode}").live("click",function(){
		if ("${contract.orgID}"==""){
			top.layer.msg("<msg:message code='contract.message'/>");
			return false;
		}
		var rowid = $(this).attr("id");
		window.location.href = "${base}${commission_contract_modify.controller}?c=${commission_contract_modify.code}&id="+ rowid;
	});
	$(".shortcut_${commission_contract_detail.indexCode}").live("click",function(){
		if ("${contract.orgID}"==""){
			top.layer.msg("<msg:message code='contract.message'/>");
			return false;
		}
		var rowid = $(this).attr("id");
		window.location.href = "${base}${commission_contract_detail.controller}?c=${commission_contract_detail.code}&id="+ rowid;
	});
	$(".shortcut_${commission_contract_start.indexCode}").live("click",function(){
		if ("${contract.orgID}"==""){
			top.layer.msg("<msg:message code='contract.message'/>");
			return false;
		}
		var rowid = $(this).attr("id");
		var orgID = $(this).attr("orgID");
		url = "${base}/commission/contract/start.json?c=${commission_contract_start.code}&status=2&id="+ rowid+"&orgID="+orgID;
		if ($(this).attr("title")=='<msg:message code="contract.stop"/>'){
			url = "${base}/commission/contract/stop.json?c=${commission_contract_start.code}&status=3&id="+ rowid+"&orgID="+orgID;
		}
		var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['50px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.agreementstart.confirm'/>",
	  	        btns : 2, 
	  	        type : 4,
	  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
	  	        yes : function(){
		  	        $.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData,textStatus){
						if (textStatus == "success"){
							if(jsonData.mark=="true"){
								top.layer.close(index);
								$("#contractList").trigger("reloadGrid"); 
							}else if(jsonData.mark=="false")
							{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='agreementchange.error'/>",type : 3}	
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
	});
	$(".shortcut_${commission_contract_delete.indexCode}").live("click",function(){
			if ("${contract.orgID}"==""){
				top.layer.msg("<msg:message code='contract.message'/>");
				return false;
			}
			var rowid = $(this).attr("id");
			var url = "${base}${commission_contract_delete.controller}.json?c=${commission_contract_delete.code}&id=" + rowid;
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
										$("#contractList").trigger("reloadGrid");
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
	//////////////////////////////////////下面是机构合同管理的JS////////////////////////////////
	jQuery("#orgContractList").jqGrid({
        treeGridModel: 'adjacency',
        url: '${base}/commission/orgcontract/grid.json',
        datatype: 'json',
        colNames: [
        		  '<msg:message code="contract.name"/>',
                  '<msg:message code="contract.orgID"/>',
                  '<msg:message code="contract.teamOrOrgName"/>',
                   '<msg:message code="contract.type"/>',
                   '<msg:message code="contract.status"/>',
                   '',
                   '<msg:message code="info.operate"/>'
                   ],
        colModel: [
        		  {name: 'contractName',index: 'contractName',width:'20%',align:'center',hidden: false,sortable: false}, 
                   {name: 'contractOrgName',index: 'contractOrgName',width:'20%',align:'center',hidden: false,sortable: false}, 
                   {name: 'teamOrOrgName',index: 'teamOrOrgName',width:'20%',align:'center',hidden: false,sortable: false}, 
                   {name: 'contractType',index: 'contractType',width:'20%',align:'center',hidden: false,sortable: false,
                	  formatter:function(cellvalue, options, rowObject){
						var content="";
                	   	<c:forEach var="item" items="${contractType}">
							   if(cellvalue==${item.key}){
								   content="${item.value}";
							   }
						</c:forEach>
						return content; 
					 }
                   }, 
                   {name: 'contractStatus',index:'contractStatus',width:'10%',align:'center',hidden: false,sortable: false,
                	  formatter:function(cellvalue, options, rowObject){
						var content="";
                	   	<c:forEach var="item" items="${contractStatus}">
							   if(cellvalue==${item.key}){
								   content="${item.value}";
							   }
						</c:forEach>
						return content; 
					 }
                  },
                  {name: 'contractOrgID',index: 'contractOrgID',width:'20%',align:'center',hidden: true,sortable: false}, 
                   {name:'act',index:'act', width:'40%',align:'center',sortable:false,
                	  formatter:function(cellvalue, options, rowdata){
	                   			var content = "";
							   	var id = rowdata.id;
							   	var orgID = rowdata.orgID;
				    			var space = "&nbsp;&nbsp;";
				    			var content = "";
				    		    <c:if test="${!empty commission_contract_contractmodify}">
				    		    <security:authorize ifAnyGranted="${commission_contract_contractmodify.code}">
				    		    if (rowdata.contractStatus!=2){
				    		    	content += "<span style='color:#CFC09F;' title='${commission_contract_contractmodify.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_contractmodify.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${commission_contract_contractmodify.name}' id='" + id + "' class='shortcut_${commission_contract_contractmodify.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_contractmodify.name}";
				   		    	    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty commission_contract_contractstart}">
				    		    <security:authorize ifAnyGranted="${commission_contract_start.code}">
				    		    	if (rowdata.contractStatus==3){
				    		    		content += "<span style='color:#CFC09F;' title='<msg:message code='contract.stop'/>' ><img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='contract.stop'/></span>"; 
					    		    //}
					    		    //else if (rowdata.contractStatus==1){
						    		//    content += "<a href='javascript:void(0);' id='" + id + "' orgID='" + orgID + "' class='shortcut_${commission_contract_contractstart.indexCode}'";
					        		//   content += " title='${commission_contract_contractstart.name}' >";
					        		//    content += "<img src='${base}/${skin}/images/icon/pause-business.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_contractstart.name}";
					        		//    content += "</a>";	
					    		    }else if (rowdata.contractStatus==2){
						    		    content += "<a href='javascript:void(0);' id='" + id + "' orgID='" + orgID + "' class='shortcut_${commission_contract_contractstart.indexCode}'";
					        		    content += " title=<msg:message code='contract.stop'/> >";
					        		    content += "<img src='${base}/${skin}/images/icon/recover-business.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='contract.stop'/>";
					        		    content += "</a>";	
					    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty commission_contract_contractupload}">
				    		    <security:authorize ifAnyGranted="${commission_contract_contractupload.code}">
				    		    	if (rowdata.contractStatus==3){
				    		    		content += "<span style='color:#CFC09F;' title='${commission_contract_contractupload.name}'><img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_contractupload.name}</span>"; 
					    		    }
					    		    else if (rowdata.contractStatus==1||rowdata.contractStatus==2){
						    		    content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${commission_contract_contractupload.indexCode}'";
					        		    content += " title='${commission_contract_contractupload.name}' >";
					        		    content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_contractupload.name}";
					        		    content += "</a>";	
					    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty commission_contract_contractdelete}">
				    		   <security:authorize ifAnyGranted="${commission_contract_contractdelete.code}">
				    		    if (rowdata.contractStatus==2){
				    		    	content += "<span style='color:#CFC09F;' title='${commission_contract_contractdelete.name}'><img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_contractdelete.name}</span>"; 
				    		    }
				    		    else{
				    		    	content += "<a href='javascript:void(0);' title='${commission_contract_contractdelete.name}' id='" + id + "' class='shortcut_${commission_contract_contractdelete.indexCode}'>";
				   		    		content += "<img src='${base}/${skin}/images/icon/audit.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_contractdelete.name}";
				   		    	    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
				    		    <c:if test="${!empty commission_contract_dowmload}">
				    		    <security:authorize ifAnyGranted="${commission_contract_dowmload.code}">
					    		    content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${commission_contract_dowmload.indexCode}'";
				        		    content += " title='${commission_contract_contractdetail.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_contract_dowmload.name}";
				        		    content += "</a>";
				    		    </security:authorize>
				    		    </c:if>
				    		    return content;
	                   	}
                  }],
       	mtype: "POST",
		postData:{c:"${contract.c}",contractOrgID:"${contract.orgID}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,   
        page:"${contract.page}",
        rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#orgpagered',
        height:<msg:message code='jqgrid.height.250'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	caption:'<msg:message code="contract.list"/>',
        toolbar: [true,"top"]
    });
	     <c:if test="${!empty commission_contract_contractcreate}">
		<security:authorize ifAnyGranted="${commission_contract_contractcreate.code}">
			var $content = $("<a></a>").attr("href","javascript:void(0)")
						   .attr("id","${commission_contract_contractcreate.indexCode}")
						   .append($("<img/>").attr("src","${base}/${skin}/${commission_contract_contractcreate.icon}")
						   .attr("width","18").attr("height","18").attr("border",0)
						   .attr("border","0").attr("align","absmiddle"))
						   .append("${commission_contract_contractcreate.name}");
			
			$("#t_orgContractList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
			$("#${commission_contract_contractcreate.indexCode}","#t_orgContractList").click(function(){
				if ("${contract.orgID}"==""){
					top.layer.msg("<msg:message code='contract.message'/>");
					return false;
				}
				window.location.href = "${base}${commission_contract_contractcreate.controller}?c=${commission_contract_contractcreate.code}&contractOrgID=${contract.orgID}";
			});
		</security:authorize>
	</c:if>
	jQuery("#orgContractList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	$(".shortcut_${commission_contract_contractmodify.indexCode}").live("click",function(){
		if ("${contract.orgID}"==""){
			top.layer.msg("<msg:message code='contract.message'/>");
			return false;
		}
		var rowid = $(this).attr("id");
		window.location.href = "${base}${commission_contract_contractmodify.controller}?c=${commission_contract_contractmodify.code}&id="+ rowid;
	});
	$(".shortcut_${commission_contract_dowmload.indexCode}").live("click",function(){
		if ("${contract.orgID}"==""){
			top.layer.msg("<msg:message code='contract.message'/>");
			return false;
		}
		var rowid = $(this).attr("id");
		 $.ajax({
				url: '${base}/commission/contract/download.json',
				type:'post',
				timeout:'60000',
				dataType:'json',
				data:{id:rowid},
				success:function(jsonData){
					if(jsonData){
						window.location.href = '${ftpUrl}'+jsonData.url;
					}else{
						top.$.layer({
							shade : [0.5 , '#000' , true],
						  	area : ['auto','auto'],
						  	offset : ['150px',''],
							dialog : {msg:'<msg:message code='sorry.contract.dowmload'/>',type : 8}	
						});
					}
				}
			});
	});
	$(".shortcut_${commission_contract_contractstart.indexCode}").live("click",function(){
		if ("${contract.orgID}"==""){
			top.layer.msg("<msg:message code='contract.message'/>");
			return false;
		}
		var rowid = $(this).attr("id");
		var orgID = $(this).attr("orgID");
		var url = "";
		if ($(this).attr("title")=='<msg:message code="contract.stop"/>'){
			url = "${base}/commission/contract/stopContractStatus.json?c=${commission_contract_contractstart.code}&status=3&id="+ rowid;
		}
		var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['50px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.contractstart.confirm'/>",
	  	        btns : 2, 
	  	        type : 4,
	  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
	  	        yes : function(){
		  	        $.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData,textStatus){
						if (textStatus == "success"){
							if(jsonData.mark=="true"){
								top.layer.close(index);
								$("#orgContractList").trigger("reloadGrid"); 
							}else if(jsonData.mark=="false"){
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='contractchange.error'/>",type : 3}	
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
	});
	$(".shortcut_${commission_contract_contractdelete.indexCode}").live("click",function(){
			if ("${contract.orgID}"==""){
				top.layer.msg("<msg:message code='contract.message'/>");
				return false;
			}
			var rowid = $(this).attr("id");
			var url = "${base}${commission_contract_contractdelete.controller}.json?c=${commission_contract_contractdelete.code}&id=" + rowid;
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
										$("#orgContractList").trigger("reloadGrid");
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
});
function tab(tag){
	for(var i = 1 ; i <= 2 ; i++){
		if(i == tag){
			$("#tab_"+i).addClass("selected");
			$("#div_"+i).css("visibility","visible");
		}else{
			$("#tab_"+i).removeClass("selected");
			$("#div_"+i).css("visibility","hidden");
		}
	}
}
</script>
</head>

<body class="skinMain">
<ul id="tabnav">
	<li class="tab_1" ><a href="#" style="display:none;" id="tab_1" onclick="tab(1)" class="selected">${commission_contract_contract.name}</a></li>
	<li class="tab_2" ><a href="#" style="display:none;" id="tab_2" onclick="tab(2)">${commission_contract_agreement.name}</a></li>
</ul>
<form:form method="post" action="contract" commandName="contract">
<input type="hidden" name="c" value="${contract.c}" />
<input type="hidden" name="turnCode" value="${commission_contract.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td>
			<div id="div_1" class="tab-div-show">
				<table id="orgContractList"><tr><td>&nbsp;</td></tr></table>
				<div id="orgpagered"></div>
			</div>
			<div id="div_2" class="tab-div-hide">
				<table id="contractList"><tr><td>&nbsp;</td></tr></table>
				<div id="pagered"></div>
			</div>
		</td>
	</tr>
	<tr>
		<td></td>
	</tr>
</table>
</form:form>
</body>
</html>