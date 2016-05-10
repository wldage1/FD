<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#paygrantparameterList").setGridWidth($(window).width()*0.999);　
});
var count = 0;   //用于计数
var currcount=0; //一次只允许操作一条记录
var orgID; 
var type = 1;
var count_1 = 0; //tab_1 全局变量
var currcount_1 = 0;
var globalrecord = 0;   //表格中的记录数(当选择机构居间费发放参数的时候它的最大值只能为1)
//#tab_1
$(document).ready(function(){
	//字典类型验证提示
	var options = {
		event:'focus',
		ajax:{
			url:'${base}/commission/paygrantparameter/valid.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: $("form#payGrantParameter").serialize(), 
			cache:false
		},
		poshytip:{
			alignX: 'right'
		}
	};
    var valid = new $.Valid(options);
    
    $('#orgID')[0].selectedIndex = 0;
    orgID = $("#orgID").val();
    
    jQuery("#paygrantparameterList").jqGrid({
        url: 'paygrantparameter/grid.json',
        datatype: 'json',
        colNames: ["<msg:message code='paygrantparameter.name'/>", 
                   "<msg:message code='paygrantparameter.approvedDeadline'/>",
                   "<msg:message code='paygrantparameter.payDate'/>",
                   "<msg:message code='info.operate'/>",
                   "<img id='addRow' src='${base}/${skin}/images/icon/Add.png' width='20' height='20' />"
                   ],
        colModel: [{name: 'name',index:'name',width:'33%',align:'center',sortable:false,editable:true},
                   {name: 'approvedDeadline',index:'approvedDeadline',width:'34%',align:'center',sortable:false,editable:true},
                   {name: 'payDate',index:'payDate',width:'34%',align:'center',sortable:false,editable:true},
                   {name:'operate',index:'operate', width:'30%',align:'left',sortable:false},
                   {name: 'del',index:'del', width:'5%',align:'center',sortable:false,title:false}
                   ],
        postData:{c:"${c}",type:type,orgID:$("#orgID").val()},
        mtype:"POST",
        height:<msg:message code='jqgrid.height.250'/>,
        pager: false,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete: function(){
        	globalrecord = jQuery("#paygrantparameterList").getGridParam("records");
    		var ids = jQuery("#paygrantparameterList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			count_1 = id;
    			var rowdata = jQuery("#paygrantparameterList").jqGrid('getRowData',id);
    			var content = "";
				
    			<c:if test="${!empty commission_paygrantparameter_modify}">
				<security:authorize ifAnyGranted="${commission_paygrantparameter_modify.code}">	
					content += "<a href='javascript:void(0);' id='shortcut_commission_paygrantparameter_modify_"+id+"' sid="+id+" class='shortcut_commission_paygrantparameter_modify'>";
					content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_paygrantparameter_modify.name}";
					content += "</a>";
				</security:authorize>
				</c:if> 
				
				<c:if test="${!empty commission_paygrantparameter_save}">	
				<security:authorize ifAnyGranted="${commission_paygrantparameter_save.code}">	
					content += "<a href='javascript:void(0);' id='shortcut_commission_paygrantparameter_save_"+id+"' sid="+id+" class='' style='color:#999;' method celcalue='' >";
					content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_paygrantparameter_save.name}";
					content += "</a>";
				</security:authorize>
				</c:if>
				
				<c:if test="${!empty commission_paygrantparameter_reset}">	
				<security:authorize ifAnyGranted="${commission_paygrantparameter_reset.code}">	
						content += "<a href='javascript:void(0);' id='shortcut_commission_paygrantparameter_reset_"+id+"' sid="+id+" class='' style='color:#999;'>";
						content += "<img src='${base}/${skin}/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_paygrantparameter_reset.name}";
						content += "</a>";
				</security:authorize>
				</c:if>
				
    			jQuery("#paygrantparameterList").jqGrid('setRowData',ids[i],{operate:"<div class='jqgridContainer'>" + content + "</div>"});
    			
    			<c:if test="${!empty commission_paygrantparameter_delete}">
				<security:authorize ifAnyGranted="${commission_paygrantparameter_delete.code}">
				jQuery("#paygrantparameterList").jqGrid('setRowData',ids[i],{del:"<a href='javascript:void(0);' id='shortcut_${commission_paygrantparameter_delete.indexCode}_"+id+"' sid="+id+" class='shortcut_${commission_paygrantparameter_delete.indexCode}' method><img src='${base}/${skin}/images/icon/Del.png' width='20' height='20' /></a>"});
				</security:authorize>
				</c:if>
    		}	
    	},
        caption:'<msg:message code="paygrantparameter.list"/>',
        toolbar: [true,"top"]
	});
	jQuery("#paygrantparameterList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
	
	$('#addRow').click(function(){
		count_1++;
		if(currcount_1 == 0){
			currcount_1 = 1;
			jQuery("#paygrantparameterList").jqGrid('addRowData',count_1,"{}");
			var data = {
					name:"<input name='name' id='"+count_1+"_name' maxlength='20' class='valid-control' style='width:98%;'/>",
					approvedDeadline:"<input name='approvedDeadline' id='"+count_1+"_approvedDeadline' maxlength='20' class='valid-control' style='width:98%;'/>",
					payDate:"<input name='payDate' id='"+count_1+"_payDate' maxlength='20' class='valid-control' style='width:98%;'/>",
					operate:"<c:if test='${!empty commission_paygrantparameter_modify}'><security:authorize ifAnyGranted='${commission_paygrantparameter_modify.code}'><a href='javascript:void(0);'  id='shortcut_${commission_paygrantparameter_modify.indexCode}_"
					+count_1+"' sid="+count_1+"  class=''  style='color:#999;'><img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_paygrantparameter_modify.name}</a><a href='javascript:void(0);'  id='shortcut_${commission_paygrantparameter_save.indexCode}_"
					+count_1+"' class='shortcut_${commission_paygrantparameter_save.indexCode}' style='color:#53056A;' sid="+count_1+"><img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_paygrantparameter_save.name}</a><a href='javascript:void(0);'  id='shortcut_${commission_paygrantparameter_reset.indexCode}_"+
					count_1+"' class='' style='color:#999;'><img src='${base}/common/skin/images/icon/modify.png' weight='18' height='18' border='0' align='absmiddle'/>${commission_paygrantparameter_reset.name}</a></security:authorize></c:if>",
					del:"<c:if test='${!empty commission_paygrantparameter_delete}'><security:authorize ifAnyGranted='${commission_paygrantparameter_delete.code}'><a href='javascript:void(0);' id='shortcut_${commission_paygrantparameter_delete.indexCode}_"
					+count_1+"' sid="+count_1+" class='shortcut_${commission_paygrantparameter_delete.indexCode}' method='add'><img src='${base}/${skin}/images/icon/Del.png' width='20' height='20' /></a></security:authorize></c:if>"
			};
			jQuery("#paygrantparameterList").jqGrid('setRowData',count_1,data);
		}else{
			top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "<msg:message code='info.prompt'/>",
					    dialog : {msg:"<msg:message code='dictionaryItem.notsave'/>",type : 3}	
						});
			count_1--;
		}
	});	

	$(".shortcut_${commission_paygrantparameter_delete.indexCode}").live("click",(function(){
		var rowid = $(this).attr("sid");
		var method = $(this).attr("method");
		if(method){
			jQuery("#paygrantparameterList").jqGrid('delRowData',count_1);
		}else{
			var index = top.$.layer({
		  	    shade : [0.5 , '#000' , true],
		  	    area : ['auto','auto'],
		  	  	offset : ['150px',''],
		  	    dialog : {
		  	        msg:"您确定要删除吗？",
		  	        btns : 2, 
		  	        type : 4,
		  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
		  	        yes : function(){
		  	        	$.ajax({
		  	  			url:'paygrantparameter/delete.json',
		  	  			type:'post',
		  	  			timeout:'60000',
		  	  			dataType:'json',
		  	  			data: "id="+rowid, 
		  	  			success:function(jsonData,textStatus){
		  	  				if (textStatus == "success"){
		  	  					if (jsonData.status == "success"){
		  	  						jQuery("#paygrantparameterList").jqGrid('delRowData',count_1);
		  	  						top.layer.close(index);
		  	  					}else{
		  	  						top.$.layer({
		  	  							area : ['auto','auto'],
		  	  							dialog : {msg: "删除失败" ,type : 8}	
		  	  						});
		  	  					}
		  	  				}
		  	  			}
		  	  		})
		  	        },
		  	        no : function(){
		  	        	top.layer.close(index);
		  	        }
		  	    }
		  	});
		}
		currcount_1 = 0;
	}))
		
	$(".shortcut_commission_paygrantparameter_modify").live("click",(function(){
		var rowid = $(this).attr("sid");
		if(currcount_1 == 0){
			currcount_1 = 1;
			jQuery("#paygrantparameterList").jqGrid('editRow',rowid);
			$(this).attr("class","").css("color","#999");
			$("#shortcut_commission_paygrantparameter_save_"+rowid).attr("class","shortcut_commission_paygrantparameter_save").css("color","#53056A");
			$("#shortcut_commission_paygrantparameter_reset_"+rowid).attr("class","shortcut_commission_paygrantparameter_reset").css("color","#53056A");
		}else{
  	     	 top.$.layer({
				area : ['auto','auto'],
				dialog : {msg: "请先保存当前编辑的条目" ,type : 8}	
			});
		}
	}));
		
		
	$(".shortcut_commission_paygrantparameter_reset").live("click",( function() {
		currcount_1 = 0;
		var rowid = $(this).attr("sid");
		jQuery("#paygrantparameterList").jqGrid('restoreRow',rowid);
		$(this).attr("class","").css("color","#999");	
		$("#shortcut_commission_paygrantparameter_save_"+rowid).attr("class","").css("color","#999");	
		$("#shortcut_commission_paygrantparameter_modify_"+rowid).attr("class","shortcut_commission_paygrantparameter_modify").css("color","#53056A");
	}));
		
	$(".shortcut_commission_paygrantparameter_save").live("click",(function() {
		var data="";
		var celcalue = "";
		var method = $(this).attr("method");
		var rowid = $(this).attr("sid");
		var clsn = $("#"+rowid+"_name").attr("class");
		if(method == ""){
			celcalue = $(this).attr("celcalue");
			data = "c=${c}&name="+$.trim($("#"+rowid+"_name").val())+"&id="+rowid+"&approvedDeadline="+$.trim($("#"+rowid+"_approvedDeadline").val())+"&payDate="+$.trim($("#"+rowid+"_payDate").val());
		}else{
			celcalue = $.trim($("#"+rowid+"_code").val());
			data = "c=${c}&name="+$.trim($("#"+rowid+"_name").val())+"&approvedDeadline="+$.trim($("#"+rowid+"_approvedDeadline").val())+"&payDate="+$.trim($("#"+rowid+"_payDate").val())+"&type="+type+"&orgID="+orgID;
		}
	 var tempOpts = {
	  	   classNames:[clsn],
		   validStatus:function(status){
			   if (status){
					$.ajax({
						url:'paygrantparameter/save.json',
						type:'post',
						timeout:'60000',
						dataType:'json',
						data: data, 
						success:function(jsonData){
							if (jsonData.status == "success"){
								currcount_1 = 0;
								jQuery("#paygrantparameterList").jqGrid('setRowData',rowid,{name:$.trim($("#"+rowid+"_name").val()),code:$.trim($("#"+rowid+"_code").val())});
								$("#"+rowid).attr("editable","0");
								$("#shortcut_commission_paygrantparameter_save_"+rowid).attr("class","").css("color","#999");
								$("#shortcut_commission_paygrantparameter_reset_"+rowid).attr("class","").css("color","#999");					
								$("#shortcut_commission_paygrantparameter_modify_"+rowid).attr("class","shortcut_commission_paygrantparameter_modify").css("color","#53056A");
								$("#paygrantparameterList").trigger("reloadGrid");					
								if(method != ""){
									$("#shortcut_commission_paygrantparameter_save_"+rowid).attr("method","");
									$("#shortcut_commission_paygrantparameter_save_"+rowid).attr("sid",jsonData.gid);
									$("#paygrantparameterList").trigger("reloadGrid");
								}
							}
						}
						
					});
				}
			}
		};
		$.extend(options,tempOpts);
		valid.updateSetting(options);
 		valid.reset();
  		valid.valid();
	}));
			
});

function tab(tag){
	currcount_1 = 0;
	type = tag;
	if(tag == 2){
		$("#tab_2").addClass("selected");
		$("#tab_1").removeClass("selected");
		jQuery("#paygrantparameterList").focus();
		jQuery(".tip-violet").hide();
		jQuery("#paygrantparameterList").jqGrid("setCaption","<msg:message code='paygrantparameter.orglist' />");
		jQuery("#paygrantparameterList").jqGrid("setGridParam",{postData:{c:"${c}",type:type,orgID:orgID}});
		jQuery("#paygrantparameterList").trigger("reloadGrid");
	}else{
		$("#tab_1").addClass("selected");
		$("#tab_2").removeClass("selected");
		jQuery("#paygrantparameterList").focus();
		jQuery(".tip-violet").hide();
		jQuery("#paygrantparameterList").jqGrid("setCaption","<msg:message code='paygrantparameter.list' />");
		jQuery("#paygrantparameterList").jqGrid("setGridParam",{postData:{c:"${c}",type:type,orgID:orgID}});
		jQuery("#paygrantparameterList").trigger("reloadGrid");
	}
}

function reload(){
	orgID = $('#orgID').val();
	jQuery("#paygrantparameterList").focus();
	jQuery(".tip-violet").hide();
	jQuery("#paygrantparameterList").jqGrid("setGridParam",{postData:{c:"${c}",type:type,orgID:orgID}});
	jQuery("#paygrantparameterList").trigger("reloadGrid");
	currcount_1 = 0;
}
</script>
</head >
<body class="skinMain">
<ul id="tabnav">
	<li class="tab_1" ><a href="#" id="tab_1" onclick="tab(1)" class="selected"><msg:message code="paygrantparameter.type.1"/></a></li>
	<li class="tab_2" ><a href="#" id="tab_2" onclick="tab(2)" ><msg:message code="paygrantparameter.type.2" /></a></li>
</ul>
<div id="div_1" class="tab-div-show">
<form:form method="post" action="list" commandName="payGrantParameter">
<input type="hidden" name="c" value="${c}" />
<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
	<tr>
		<td width="15%" valign="top">
			<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width: 208px; ">
				<div class="ui-jqgrid-view" id="gview_dictionaryItemList" style="width: 208px; ">
					<div class="ui-jqgrid-view" id="gview_dictionaryItemList" style="width: 208px; ">
						<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix"><msg:message code="contract.orgID"/></div>
						<div style="width: 208px;" class="ui-state-default ui-jqgrid-hdiv">
							<div class="ui-jqgrid-hbox">
								<form:select path="orgID" id="orgID" items="${orgList}" itemValue="id" itemLabel="name" onChange="reload();" size="${orgListSize}" style="width:208px;border:0px;font-size:10pt;"/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</td>
		<td>
			<table id="paygrantparameterList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
</table>
</form:form>
</div>
</body>
</html>