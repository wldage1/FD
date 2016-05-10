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
	$("#memberList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
	
    jQuery("#memberList").jqGrid({
        treeGridModel: 'adjacency',
        url: 'memberquitorg/grid.json',
        datatype: 'json',
        colNames: [
                  '',
                   '<msg:message code="memberApplication.memberName"/>',
                   '<msg:message code="memberApplication.teamName"/>',
                   '<msg:message code="memberApplication.aplicationTime"/>',
                   '<msg:message code="memberApplication.effectTime"/>',
                   '<msg:message code="info.operate"/>'
                   ],
        colModel: [
                   {name: 'id',index: 'id',hidden: true}, 
                   {name: 'memberName',index: 'memberName',width:'20%',align:'left',hidden: false,sortable: false}, 
                   {name: 'teamName',index:'teamName',width:'25%',align:'center',hidden: false,sortable: false},
                   {name: 'applicationTime',index:'applicationTime',width:'20%',align:'center',hidden: false,sortable: false},
                   {name: 'effortTime',index: 'effortTime',width:'20%',align:'center',hidden: false,sortable: false,
                	  formatter:function(cellvalue, options, rowObject){
						var applicationTime = rowObject.applicationTime;
						if(applicationTime != null && typeof(applicationTime) != "undefined"){
							var newdate=new Date(applicationTime);
							var newtimems=newdate.getTime()+(30*24*60*60*1000);
							newdate.setTime(newtimems);
							var tDay = newdate.getDate();                                                                            
							var tMonReal = newdate.getMonth() + 1;                               
							var tYear = newdate.getFullYear();  
							var tTime = newdate.getTime();
							newdate =  tYear+"-"+tMonReal+"-"+tDay;
							
							var currTime = new Date(); //获取当前时间 
							var time = currTime.getTime() - tTime;
							var day = parseInt(time / (1000 * 60 * 60 * 24));
							if(day > 0){
								return "<font style='background-color:red;color:#FFF'>"+newdate+"</font>";
							}else if((-4 < day) && (day <= 0)){
								return "<font style='background-color:#FFFF33;'>"+newdate+"</font>";
							}else{
								return newdate;
							}
						}else{
							return '';
						}						
					 }
                  }, 
                   {name:'act',index:'act', width:'15%',align:'center',sortable:false,
                	  formatter:function(cellvalue, options, rowdata){
	                   		var content = "";
							var id = rowdata.id;
							<c:if test="${!empty customer_memberquitorg_quit}">
							<security:authorize ifAnyGranted="${customer_memberquitorg_quit.code}">
				    		    content += "<a href='javascript:void(0);' id='" + id + "'  class='shortcut_${customer_memberquitorg_quit.indexCode}'";
				        		content += " title='${customer_memberquitorg_quit.name}' >";
				        		content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${customer_memberquitorg_quit.name}";
				        		content += "</a>";
				    		</security:authorize>
				    		</c:if>
				    		return content;
	                   	}
                  }],
       	mtype: "POST",
		postData:{c:"${c}"},
        rowNum:<msg:message code='jqgrid.row.num.10'/>,   
        page:"${member.page}",
        rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
        pager: '#pagered',
        height:<msg:message code='jqgrid.height.250'/>,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        rownumbers: true,
        jsonReader: {
        	repeatitems: false
        },
        loadComplete:function(){
    	},
    	caption:'<msg:message code="memberApplication.list"/>',
        toolbar: [false,"top"]
    });
	
	jQuery("#memberList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	
	$(".shortcut_${customer_memberquitorg_quit.indexCode}").live("click",function(){
			var rowid = $(this).attr("id");
			var data = jQuery("#memberList").jqGrid("getRowData",rowid);
			var url = "${base}${customer_memberquitorg_quit.controller}.json?c=${customer_memberquitorg_quit.code}&prompt=memberName&id="+ rowid;
			var index = top.$.layer({
	  	    shade : [0.5 , '#000' , true],
	  	    area : ['auto','auto'],
	  	  	offset : ['50px',''],
	  	    dialog : {
	  	        msg:"确认"+data.memberName+"退出"+data.teamName,
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
						if(textStatus == "success"){
							if(jsonData.status == "success"){
								top.layer.close(index);
								$("#memberList").trigger("reloadGrid");
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
	
});

	// 重新加载列表数据
	function reload(){
		var name = $("#memberName").val();
		var data = {c:"${c}",memberName:name};
		jQuery("#memberList").jqGrid("setGridParam",{postData:data}).trigger("reloadGrid");
	}
	
</script>
</head>

<body class="skinMain">
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
						<input name="memberName" id="memberName"/>
					</td>
					
					<td rowspan="2" width="20%" class="button_area">
						<input type=submit class="btn2" id="select" onclick="reload();" value="<msg:message code='button.search'/>">
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
</body>
</html>