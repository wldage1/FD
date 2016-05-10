<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${base}/common/js/layer/layer.js"></script>
<link href="${base}/${skin}/css/layer.css" rel="stylesheet" type="text/css"/>
<title></title>
<script type="text/javascript">
var iframeID = window.frameElement.id;
var parentIframeID='${iframeID}';
$(document).ready(function(){
    jQuery("#memberList").jqGrid({
        url: '${base}/market/order/memberGrid.json',
        datatype: 'json',
        colNames: [
                   '',
                  '<msg:message code="member.name"/>',
                   '<msg:message code="member.teamID"/>', 
                   '<msg:message code="member.iDCard"/>',
                   '<msg:message code="member.teamName"/>',
                   '<msg:message code="info.operate"/>'
                   ],
        colModel: [{name: 'id',index: 'id',width:'10%',align:'center',hidden: true,sortable: false},
                   {name: 'name',index: 'name',width:'20%',align:'center',hidden: false,sortable: false,formatter:function(cellvalue, options, rowObject){
                	  return cellvalue+"("+rowObject.nickName+")";
                   }}, 
                   {name: 'teamID',index: 'teamID',width:'10%',align:'center',hidden: true,sortable: false}, 
                   {name: 'iDCard',index:'iDCard',width:'20%',align:'center',hidden: false,sortable: false},
                   {name: 'teamName',index:'teamName',width:'20%',align:'center',hidden: false,sortable: false},
                   {name:'act',index:'act', width:'10%',align:'center',sortable:false,
                	   formatter:function(cellvalue, options, rowObject){
                    	   var content="<a href='javascript:void(0);' onclick=\"window.parent.document.getElementById('"+ iframeID +"').contentWindow.selectMember("+rowObject.id+",'"+rowObject.name+"',"+rowObject.teamID+","+rowObject.orgType+","+rowObject.orgID+",'"+rowObject.teamName+"')\" class='memberSelect'><font color='#0000FF' title='${product_manage_detail.name}'><msg:message code='select.button'/></fond></a>";
    						return content;    
   					   }
                  	}
                  ],
       	mtype: "POST",
		postData:{c:"${member.c}",name:"${member.name}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,   
        page:"${member.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:	200,
        autowidth: true,
        viewrecords: true,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
    	caption:'<msg:message code="member.list"/>',
        toolbar: [true,"top"]
    });
	jQuery("#memberList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	
	//查询按钮
	$("#select").click(function(){
		jQuery("#memberList").jqGrid("setGridParam",{page:0,postData:{name:$("#name").val()}}).trigger("reloadGrid");  
	});
	
});

function selectMember(mID,mName,teamID,orgType,orgID,tName){
    top.document.getElementById(parentIframeID).contentWindow.getMembers(mID,mName,teamID,orgType,orgID,tName);
    top.document.getElementById(parentIframeID).contentWindow.closeTopLayer();
}
</script>
</head>

<body class="skinMain">
<form:form method="post" action="member" commandName="member">
<input type="hidden" name="c" value="${member.c}" />
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td align="center">
			<table cellpadding="0" cellspacing="1" border="0" width="100%"
				class="search">
				<tr>
					<td align="right" width="10%" class="search_info">
						<msg:message code='member.name'/>
					</td>
					<td align="left" width="30%" class="search_lable">
						<form:input path="name" htmlEscape="true"/>
					</td>
					
					<td rowspan="2" width="20%" class="button_area">
						<input type=button class="btn2" id="select"
							value="<msg:message code='button.search'/>">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="memberList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</form:form>
</body>
</html>