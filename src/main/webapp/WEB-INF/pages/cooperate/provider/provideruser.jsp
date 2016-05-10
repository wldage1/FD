<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${base}/common/js/layer/layer.js"></script>
<title></title>
<script type="text/javascript">
$(document).ready(function(){
    jQuery("#userList").jqGrid({
        url: 'usermanage/grid.json',
        datatype: 'json',
        colNames: [ "<msg:message code='provideruser.name'/>",
                   "<msg:message code='provideruser.account'/>", 
                   "<msg:message code='provideruser.mobilePhone'/>",
                   "<msg:message code='provideruser.status'/>",
                   "",
                   "<msg:message code='info.operate'/>"
                   ],
        colModel: [
				   {name: 'name',index:'name',width:'20%',align:'center',hidden: false,sortable:false},
				   {name: 'account',index: 'account',width:'20%',align:'center',hidden: false,sortable:false}, 
				   {name: 'mobilePhone',index:'mobilePhone',width:'20%',align:'center',hidden: false,sortable:false},
				   {name: 'status',index:'status',width:'20%',align:'center',hidden: false,sortable:false,
					   formatter:function(cellvalue, options, rowObject){
                	   var content="";
                	   	<c:forEach var="item" items="${providerUserStatus}">
                	   	<c:set var="isDoing" value="0"/>
                	   	   <c:if test="${isDoing!='1'}">
							   if(cellvalue==${item.key}){
								   content="${item.value}";
								   <c:set var="isDoing" value="1"/> 
							   }
						   </c:if>
						</c:forEach>
						return content;    
					 }
				   },
				   {name: 'id',index:'id',width:'20%',align:'center',hidden: true,sortable:false},
                   {name:'act',index:'act', width:'40%',align:'center',sortable:false,title:false, 
					   formatter:function(cellvalue, options, rowdata){
                	   		   var id = rowdata.id;
			        		   var content = "";
			        		   <c:if test="${!empty cooperate_provider_provideruser_modify}">
			        		   <security:authorize ifAnyGranted="${cooperate_provider_provideruser_modify.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_provider_provideruser_modify.indexCode}'";
			        		   		content += " title='${cooperate_provider_provideruser_modify.name}' >";
			        		   		content += "<img src='${base}/${skin}/${cooperate_provider_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_provider_provideruser_modify.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty cooperate_provider_provideruser_detail}">
			        		   <security:authorize ifAnyGranted="${cooperate_provider_provideruser_detail.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_provider_provideruser_detail.indexCode}'";
			        		   		content += " title='${cooperate_provider_provideruser_detail.name}' >";
			        		   		content += "<img src='${base}/${skin}/${cooperate_provider_detail.icon}' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_provider_provideruser_detail.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
			        		   <c:if test="${!empty cooperate_provider_provideruser_stop}">
				    		    <security:authorize ifAnyGranted="${cooperate_provider_provideruser_stop.code}">
				    		    if (rowdata.status == 1){
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_provider_provideruser_stop.indexCode}'";
				        		    content += " title='${cooperate_provider_provideruser_stop.name}' >";
				        		    content += "<img src='${base}/${skin}/images/icon/recover-business.png' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_provider_provideruser_stop.name}";
				        		    content += "</a>";
				    		    }
				    		    else if (rowdata.status == 0){
				    		    	content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_provider_provideruser_stop.indexCode}'";
				        		    content += " title='<msg:message code='provideruser.start'/>' >";
				        		    content += "<img src='${base}/${skin}/images/icon/pause-business.png' weight='18' height='18' border='0' align='absmiddle'/><msg:message code='provideruser.start'/>";
				        		    content += "</a>";
				    		    }
				    		    </security:authorize>
				    		    </c:if>
			        		   <c:if test="${!empty cooperate_provider_provideruser_resetpassword}">
			        		   <security:authorize ifAnyGranted="${cooperate_provider_provideruser_resetpassword.code}">
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${cooperate_provider_provideruser_resetpassword.indexCode}'";
			        		   		content += " title='${cooperate_provider_provideruser_resetpassword.name}'>";
			        		   		content += "<img src='${base}/${skin}/images/icon/password.png' weight='18' height='18' border='0' align='absmiddle'/>${cooperate_provider_provideruser_resetpassword.name}";
			        		   		content += "</a>";
			        		   </security:authorize>
			        		   </c:if>
	       			   return content;
                   }}],
        scroll: false,
        mtype:"POST",
        postData: {c:"${provider.c}", id:"${provider.id}"},
        viewrecords: true,
        multiselect: false,
        rowNum:10,
	    page:"1",
	    rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
	    pager: '#pagered',
	    height:'<msg:message code='jqgrid.height.250'/>',
	    autowidth: true,
	    rownumbers:true,
        jsonReader: {
        	repeatitems: false
        },
        caption:'<msg:message code="provideruser.list"/>',
        toolbar: [true,"top"]
    });     		
	$(".shortcut_${cooperate_provider_provideruser_modify.indexCode}").live("click",function(){
		var rowid = $(this).attr("id");
		window.location.href = "${base}${cooperate_provider_provideruser_modify.controller}?c=${cooperate_provider_provideruser_modify.code}&id="+ rowid;
	});
	$(".shortcut_${cooperate_provider_provideruser_detail.indexCode}").live("click",function(){
		var rowid = $(this).attr("id");
		window.location.href = "${base}${cooperate_provider_provideruser_detail.controller}?c=${cooperate_provider_provideruser_detail.code}&id="+ rowid;
	});
	$(".shortcut_${cooperate_provider_provideruser_resetpassword.indexCode}").live("click",function(){
		var rowid = $(this).attr("id");
		var data = jQuery("#userList").jqGrid("getRowData",rowid);
		window.location.href = "${base}${cooperate_provider_provideruser_resetpassword.controller}?c=${cooperate_provider_provideruser_resetpassword.code}&id="+ rowid;
	});
        $(".shortcut_${cooperate_provider_provideruser_stop.indexCode}").live("click",function(){
	   	var rowid = $(this).attr("id");
	   	var title=$(this).attr("title");
	   	var data = jQuery("#userList").jqGrid("getRowData",rowid);
	   	var url = "${base}/cooperate/provider/provideruser/stop.json?prompt=name&id="+rowid+"&name="+encodeURIComponent(encodeURIComponent(data.name));
	   	if (title=='<msg:message code="provideruser.start"/>'){
	   		url = "${base}/cooperate/provider/provideruser/start.json?prompt=name&id="+rowid+"&name="+encodeURIComponent(encodeURIComponent(data.name));
	   	}
	   	///
	   	var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['50px',''],
	  	    dialog : {
	  	        msg:"<msg:message code='info.prompt.changeprovideruser.confirm'/>",
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
								//window.parent.Boxy.alert("<msg:message code='team.changesuccess'/>", null, {title: "<msg:message code='info.prompt'/>"});
								$("#userList").trigger("reloadGrid"); 
							}else if(jsonData.mark=="false")
							{
								top.$.layer({
								shade : [0.5 , '#000' , true],
						  		offset : ['50px',''],
						  	 	title : "<msg:message code='info.prompt'/>",
							    dialog : {msg:"<msg:message code='provideruser.operationerror'/>",type : 3}	
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
    jQuery("#userList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
    <c:if test="${!empty cooperate_provider_provideruser_create}">
		<security:authorize ifAnyGranted="${cooperate_provider_provideruser_create.code}">
			var $content = $("<a></a>").attr("href","javascript:void(0)")
						   .attr("id","${cooperate_provider_provideruser_create.indexCode}")
						   .append($("<img/>").attr("src","${base}/${skin}/${cooperate_provider_provideruser_create.icon}")
						   .attr("width","18").attr("height","18").attr("border",0)
						   .attr("border","0").attr("align","absmiddle"))
						   .append("${cooperate_provider_provideruser_create.name}");
			$("#t_userList").append("&nbsp;&nbsp;").append($("<span></span>").attr("class","jqgridContainer").append($content));
			$("#${cooperate_provider_provideruser_create.indexCode}","#t_userList").click(function(){
				window.location.href = "${base}${cooperate_provider_provideruser_create.controller}?c=${cooperate_provider_provideruser_create.code}&providersID=${provider.id}";
			});
	</security:authorize>
	</c:if>
});

</script>
</head>
<body class="skinMain">
<input type="hidden" name="c" value="${provider.c}"/>
<input type="hidden" name="turnCode" value="${cooperate_provider.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="100%">
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td>
						<table id="userList" ><tr><td>&nbsp;</td></tr></table>	
						<div id="pagered" ></div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tableMargin"></td>
	</tr>
	<tr>
		<td></td>
	</tr>
	  <div class="toolbar">
		<div class="left"></div>
			<div class="center">
			<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
			</div>
			<div class="right"></div>
		</div>
		<tr>
			<td></td>
		</tr>
</table>
</body>
</html>