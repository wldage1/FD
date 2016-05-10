<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#webQuestionList").setGridWidth($(window).width()*0.999);　
});
$(window).resize(function(){ 　　
	$("#webDisposedList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
   jQuery("#webQuestionList").jqGrid({
		url:"consult/grid.json",
		datatype:"json",
		colNames:["<msg:message code='question.ip' />",
		          "<msg:message code='question.name' />",
		          "<msg:message code='question.email' />",
		          "<msg:message code='question.phone' />",
		          "<msg:message code='question.branch' />",
		          "<msg:message code='question.webquestion' />",
		          "<msg:message code='question.webaskTime' />",
		          "<msg:message code='user.operate'/>"],
		colModel:[{name:'ip',index:'ip',width:'33%',align:'center',sortable:false},
		          {name:'name',index:'name',width:'33%',align:'center',sortable:false},
		          {name:'email',index:'email',width:'33%',align:'center',sortable:false},
		          {name:'phone',index:'phone',width:'33%',align:'center',sortable:false},
		          {name:'branch',index:'branch',width:'33%',align:'center',sortable:false},
		          {name:'question',index:'question',width:'66%',align:'center',sortable:false},
		          {name:'askTime',index:'askTime',width:'33%',align:'center',sortable:false},
 				  {name:'act',index:'act', width:'30%',align:'center',sortable:false,
						formatter:function(cellvalue,options,rowdata){
				    			 var id = rowdata.id;
				    			 var content = "";
				    			<c:if test="${!empty websitemanage_consult_handle}">
					    		<security:authorize ifAnyGranted="${websitemanage_consult_handle.code}">
								content += "<a href='javascript:void(0);' id='shortcut_websitemanage_consult_handle_"+id+"' sid="+id+" class='shortcut_websitemanage_consult_handle'>";
								content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${websitemanage_consult_handle.name}";
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
        caption:'<msg:message code="webquestion.list"/>',
        toolbar: [true,"top"]
        
   });


	$(".shortcut_${websitemanage_consult_handle.indexCode}").live("click",function(){
		var rowid = $(this).attr("sid");
		var url="${base}/websitemanage/consult/handle.json?id="+rowid;
	   	var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['150px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.handlequestion.confirm'/>",
	  	        btns : 2, 
	  	        type : 4,
	  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
	  	        yes : function(){
		  	        $.ajax({
					url:url,
					type:'post',
					timeout:'60000',
					dataType:'json',
					success:function(jsonData){
						if (jsonData.status == "success"){
								top.layer.close(index);
								$("#webQuestionList").trigger("reloadGrid"); 
							}else{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='question.checkerror'/>",type : 3}	
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
	
	 jQuery("#webDisposedList").jqGrid({
			url:"consult/disposedgrid.json",
			datatype:"json",
			colNames:["<msg:message code='question.ip' />",
			          "<msg:message code='question.name' />",
			          "<msg:message code='question.email' />",
			          "<msg:message code='question.phone' />",
			          "<msg:message code='question.branch' />",
			          "<msg:message code='question.webquestion' />",
			          "<msg:message code='question.webaskTime' />"],
			colModel:[{name:'ip',index:'ip',width:'33%',align:'center',sortable:false},
			          {name:'name',index:'name',width:'33%',align:'center',sortable:false},
			          {name:'email',index:'email',width:'33%',align:'center',sortable:false},
			          {name:'phone',index:'phone',width:'33%',align:'center',sortable:false},
			          {name:'branch',index:'branch',width:'33%',align:'center',sortable:false},
			          {name:'question',index:'question',width:'66%',align:'center',sortable:false},
			          {name:'askTime',index:'askTime',width:'33%',align:'center',sortable:false}],
	        postData:{c:"${question.c}"},
	        mtype:"POST",
	        height:<msg:message code='jqgrid.height.250'/>,
	        rowNum:<msg:message code='jqgrid.row.num.10'/>,
	        rowList:[<msg:message code='jqgrid.row.list.s10.10'/>],
	        page: "${question.page}",
	        pager:"#pagered",
	        jsonReader: {
	        	repeatitems: false
	        },
	        autowidth: true,
	        viewrecords: true,
	        multiselect: false,
	        caption:'<msg:message code="webquestion.disposedlist"/>',
	        toolbar: [true,"top"]
	 });
	
});

function tab(tag){
	if(tag == "1"){
		jQuery("#webQuestionList").trigger("reloadGrid");
	}else{
		jQuery("#webDisposedList").trigger("reloadGrid");
	}
	for(var i = 1 ; i <= 2 ; i++){
		if(i == tag){
			$("#tab_"+i).addClass("selected");
			$("#div_"+i).css("visibility","visible");
		}else{
			$("#tab_"+i).removeClass("selected");
			$("#div_"+i).css("visibility","hidden");
		}
	}}
</script>
</head>
<body class="skinMain">
<ul id="tabnav">
	<li class="tab_1" ><a href="#" id="tab_1" onclick="tab(1)" class="selected"><msg:message code="question.undisposed"/></a></li>
	<li class="tab_2" ><a href="#" id="tab_2" onclick="tab(2)" ><msg:message code="question.disposed" /></a></li>
</ul>
<form:form method="post" action="list" commandName="question">
<input type="hidden" name="c" value="${question.c}" />
<div id="div_1" class="tab-div-show">
	<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
		<tr>
			<td>
				<table id="webQuestionList"><tr><td>&nbsp;</td></tr></table>
				<div id="onepagered"></div>
			</td>
		</tr>
	</table>
</div>
<div id="div_2" class="tab-div-hide">
	<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
		<tr>
			<td>
				<table id="webDisposedList"><tr><td>&nbsp;</td></tr></table>
				<div id="pagered"></div>
			</td>
		</tr>
	</table>
</div>
</form:form>
</body>
</html>