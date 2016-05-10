<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#productPendingList").setGridWidth($(window).width()*0.999);　
});　
$(window).resize(function(){ 　　
	$("#productQuestionList").setGridWidth($(window).width()*0.999);　
});　
$(window).resize(function(){ 　　
	$("#productDisposedQuestionList").setGridWidth($(window).width()*0.999);　
});　
$(document).ready(function(){
	   
	   jQuery("#productPendingList").jqGrid({
			url:"question/pendinggrid.json",
			datatype:"json",
			colNames:["",
			          "<msg:message code='member.name.nickname' />",
			          "<msg:message code='product.shortName' />",
			          "<msg:message code='question.question' />",
			          "<msg:message code='question.askTime' />",
			          "<msg:message code='user.operate'/>"],
			colModel:[{name:'nickName',index:'nickName',width:'1%',sortable:false,hidden:true},
			          {name: 'memberName',index:'memberName',width:'33%',align:'center',sortable:false,formatter:changeMember},
			          {name: 'productName',index:'productName',width:'33%',align:'center',sortable:false},
			          {name: 'question',index:'question',width:'33%',align:'center',sortable:false},
					   {name: 'askTime',index:'askTime',width:'33%',align:'center',sortable:false},
					   {name:'act',index:'act', width:'30%',align:'center',sortable:false,sortable:false,
						  formatter:function(cellvalue,options,rowdata){
				    			 var id = rowdata.id;
				    			 var content = "";
				 				<c:if test="${!empty product_question_reply}">
				 				<security:authorize ifAnyGranted="${product_question_reply.code}">	
				 				content += "<a href='javascript:void(0);' id='shortcut_product_question_reply_"+id+"' sid="+id+" class='shortcut_product_question_reply' title='${product_question_reply.name}'>";
				 				content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${product_question_reply.name}";
				 				content += "</a>";
				 				</security:authorize>
				 				</c:if> 
				   				<c:if test="${!empty product_question_delete}">
				 				<security:authorize ifAnyGranted="${product_question_delete.code}">	
				 				content += "<a href='javascript:void(0);' id='shortcut_product_question_reply_"+id+"' sid="+id+" class='shortcut_product_question_delete' title='${product_question_delete.name}'>";
				 				content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${product_question_delete.name}";
				 				content += "</a>";
				 				</security:authorize>
				 				</c:if> 
								return content;
				    			}
						}],
	       postData:{c:"${question.c}"},
	       mtype:"POST",
	       height:<msg:message code='jqgrid.height.250'/>,
	       rowNum:<msg:message code='jqgrid.row.num.10'/>,
	       rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
	       page: "${question.page}",
	       pager:"#threepagered",
	       autowidth: true,
	       viewrecords: true,
	       multiselect: false,
	       rownumbers: true,
	       jsonReader: {
	       	repeatitems: false
	       },
	       caption:'<msg:message code="question.pending.list"/>',
	       toolbar: [true,"top"]
	  });
	
   jQuery("#productQuestionList").jqGrid({
		url:"question/undisposedgrid.json",
		datatype:"json",
		colNames:["",
		          "<msg:message code='member.name.nickname' />",
		          "<msg:message code='product.shortName' />",
		          "<msg:message code='question.question' />",
		          "<msg:message code='question.askTime' />",
		          "<msg:message code='question.reply' />",
		          "<msg:message code='question.replyTime' />",
		          "<msg:message code='user.operate'/>"],
		colModel:[{name:'nickName',index:'nickName',width:'1%',sortable:false,hidden:true},
		          {name: 'memberName',index:'memberName',width:'33%',align:'center',sortable:false,formatter:changeMember},
				  {name: 'productName',index:'productName',width:'33%',align:'center',sortable:false},
		          {name: 'question',index:'question',width:'33%',align:'center',sortable:false},
 				   {name: 'askTime',index:'askTime',width:'33%',align:'center',sortable:false},
 				   {name: 'reply',index:'reply',width:'33%',align:'center',sortable:false},
 				   {name: 'replyTime',index:'replyTime',width:'33%',align:'center',sortable:false},
 				   {name:'act',index:'act', width:'30%',align:'center',sortable:false,sortable:false,
 					  formatter:function(cellvalue,options,rowdata){
			    			 var id = rowdata.id;
			    			 var content = "";
		    				<c:if test="${!empty product_question_modify}">
			 				<security:authorize ifAnyGranted="${product_question_modify.code}">	
			 				content += "<a href='javascript:void(0);' id='shortcut_product_question_modify_"+id+"' sid="+id+" class='shortcut_product_question_modify' title='${product_question_modify.name}'>";
			 				content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${product_question_modify.name}";
			 				content += "</a>";
			 				</security:authorize>
			 				</c:if> 
			 				<c:if test="${!empty product_question_released}">
			 				<security:authorize ifAnyGranted="${product_question_released.code}">	
			 				content += "<a href='javascript:void(0);' id='shortcut_product_question_released_"+id+"' sid="+id+" class='shortcut_product_question_released'>";
			 				content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${product_question_released.name}";
			 				content += "</a>";
			 				</security:authorize>
			 				</c:if> 
							return content;
			    			}
					}],
        postData:{c:"${question.c}"},
        mtype:"POST",
        height:<msg:message code='jqgrid.height.250'/>,
        rowNum:<msg:message code='jqgrid.row.num.10'/>,
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        page: "${question.page}",
        pager:"#onepagered",
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        caption:'<msg:message code="productquestion.list"/>',
        toolbar: [true,"top"]
        

   });
	$(".shortcut_${product_question_reply.indexCode}").live("click",function(){
		var rowid = $(this).attr("sid");
		window.location.href = "${base}${product_question_reply.controller}?c=${product_question_reply.code}&id="+ rowid;
	});
	
	$(".shortcut_${product_question_modify.indexCode}").live("click",function(){
		var rowid = $(this).attr("sid");
		window.location.href = "${base}${product_question_modify.controller}?c=${product_question_modify.code}&id="+ rowid;
	});
	
	
	$(".shortcut_${product_question_repeal.indexCode}").live("click",function(){
		var rowid = $(this).attr("sid");
		var url = "${base}/product/question/repeal.json?id="+rowid;
	   	var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['150px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.repealquestion.confirm' />",
	  	        btns : 2, 
	  	        type : 4,
	  	     	btn : ["确定","取消"],
	  	        yes : function(){
		  	        $.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData){
						if (jsonData.status == "success"){
								top.layer.close(index);
								$("#productDisposedQuestionList").trigger("reloadGrid"); 
							}else{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='question.repealerror' />",type : 3}	
								});
							}
						}
				    });
	  	        },
	  	        no : function(){
	  	        	top.layer.close(index);
	  	        }
	  	    }});
		
	});
	
	$(".shortcut_${product_question_released.indexCode}").live("click",function(){
		var rowid = $(this).attr("sid");
		var url = "${base}/product/question/isreleased.json?id="+rowid;
	   	var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['150px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.publishquestion.confirm'/>",
	  	        btns : 2, 
	  	        type : 4,
	  	     	btn : ["确定","取消"],
	  	        yes : function(){
		  	        $.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData){
						if (jsonData.status == "success"){
								top.layer.close(index);
								$("#productQuestionList").trigger("reloadGrid"); 
							}else{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='question.deleteerror' />",type : 3}	
								});
							}
						}
				    });
	  	        },
	  	        no : function(){
	  	        	top.layer.close(index);
	  	        }
	  	    }});
	});
	
	
	$(".shortcut_${product_question_delete.indexCode}").live("click",function(){
		var rowid = $(this).attr("sid");
		var url="${base}/product/question/delete.json?id="+rowid;
	   	var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['150px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.deletequestion.confirm'/>",
	  	        btns : 2, 
	  	        type : 4,
	  	     	btn : ["确定","取消"],
	  	        yes : function(){
		  	        $.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData){
						if (jsonData.status == "success"){
								top.layer.close(index);
								$("#productPendingList").trigger("reloadGrid"); 
							}else{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='question.deleteerror'/>",type : 3}	
								});
							}
						}
				    });
	  	        },
	  	        no : function(){
	  	        	top.layer.close(index);
	  	        }
	  	    }});
	});
   
   
   jQuery("#productDisposedQuestionList").jqGrid({
		url:"question/disposedgrid.json",
		datatype:"json",
		colNames:["",
		          "<msg:message code='member.name.nickname' />","<msg:message code='product.shortName' />",
		          "<msg:message code='question.question' />","<msg:message code='question.askTime' />",
		          "<msg:message code='question.reply' />","<msg:message code='question.replyTime' />",
		          "<msg:message code='user.operate' />"],
		colModel:[{name:'nickName',index:'nickName',width:'1%',sortable:false,hidden:true},
		          {name: 'memberName',index:'memberName',width:'33%',align:'center',sortable:false,formatter:changeMember},
		           {name: 'productName',index:'productName',width:'33%',align:'center',sortable:false},
		           {name: 'question',index:'q.question',width:'33%',align:'center',sortable:false},
				   {name: 'askTime',index:'q.askTime',width:'33%',align:'center',sortable:false},
				   {name: 'reply',index:'qr.reply',width:'33%',align:'center',sortable:false},
				   {name: 'replyTime',index:'qr.replyTime',width:'33%',align:'center',sortable:false},
				   {name:'act',index:'act', width:'30%',align:'center',sortable:false,sortable:false,
						  formatter:function(cellvalue,options,rowdata){
				    			 var id = rowdata.id;
				    			 var content = "";
				 				<c:if test="${!empty product_question_repeal}">
				 				<security:authorize ifAnyGranted="${product_question_repeal.code}">	
				 				content += "<a href='javascript:void(0);' id='shortcut_product_question_repeal_"+id+"' sid="+id+" class='shortcut_product_question_repeal'>";
				 				content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${product_question_repeal.name}";
				 				content += "</a>";
				 				</security:authorize>
				 				</c:if> 
								return content;
				    			}
						}],
       postData:{c:"${question.c}"},
       mtype:"POST",
       height:<msg:message code='jqgrid.height.250'/>,
       rowNum:<msg:message code='jqgrid.row.num.10'/>,
       rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
       page: "${question.page}",
       pager:"#pagered",
       autowidth: true,
       viewrecords: true,
       multiselect: false,
       jsonReader: {
       	repeatitems: false
       },
       caption:'<msg:message code="question.disposed.list"/>',
       toolbar: [true,"top"]
  });
});

function tab(tag){
	if(tag == 1){
		jQuery("#productPendingList").trigger("reloadGrid");
	}else if(tag == 2){
		jQuery("#productQuestionList").trigger("reloadGrid");
	}else if(tag = 3){
		jQuery("#productDisposedQuestionList").trigger("reloadGrid");
	}
	for(var i = 1 ; i <= 3 ; i++){
		if(i == tag){
			$("#tab_"+i).addClass("selected");
			$("#div_"+i).css("visibility","visible");
		}else{
			$("#tab_"+i).removeClass("selected");
			$("#div_"+i).css("visibility","hidden");
		}
	}}

function changeMember(cellvalue,options,rowObject){
	if(rowObject.memberName!=null){
		if(rowObject.nickName!=null){
			return rowObject.memberName+"("+rowObject.nickName+")";
		}else{
			return rowObject.memberName;
		}
	}else{
		return " <br>";
	}
}
</script>
</head>


<body class="skinMain">
<ul id="tabnav">
	<li class="tab_1" ><a href="#" id="tab_1" onclick="tab(1)" class="selected"><msg:message code="question.pending" /></a></li>
	<li class="tab_2" ><a href="#" id="tab_2" onclick="tab(2)" ><msg:message code="question.undisposedcheck"/></a></li>
	<li class="tab_3" ><a href="#" id="tab_3" onclick="tab(3)" ><msg:message code="question.disposedcheck" /></a></li>
</ul>
<form:form method="post" action="list" commandName="question">
<input type="hidden" name="c" value="${question.c}" />
<div id="div_1" class="tab-div-show">
	<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
		<tr>
			<td>
			<table id="productPendingList"><tr><td>&nbsp;</td></tr></table>
				<div id="threepagered"></div>
			</td>
		</tr>
	</table>
</div>
<div id="div_2" class="tab-div-hide">
	<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
		<tr>
			<td>
			<table id="productQuestionList"><tr><td>&nbsp;</td></tr></table>
				<div id="onepagered"></div>
			</td>
		</tr>
	</table>
</div>
<div id="div_3" class="tab-div-hide">
	<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
		<tr>
			<td>
				<table id="productDisposedQuestionList"><tr><td>&nbsp;</td></tr></table>
				<div id="pagered"></div>
			</td>
		</tr>
	</table>
</div>
</form:form>
</body>
</html>