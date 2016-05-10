<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
//阶段参数编辑状态
var stageStatus = 0;
//返利参数编辑状态
var rebateStatus = 0;
//阶段排名奖励分值编辑状态
var rankStatus = 0;
//返利参数ID计数
var rebateCount = 0;
//阶段排名奖励分值ID计数
var rankCount = 0;

$(document).ready(function(){
	
	$("#stageTime").children().first().attr("selected","selected");
	
	//返利参数验证
	var rebateOptions = {
		event:'focus',
		ajax:{
			url:'${base}/incentivefee/incentive/rebateValid.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: $("form#rebateParameter").serialize(),
			cache:false
		},
		poshytip:{
			alignX: 'right'
		}
	};
    var rebateValid = new $.Valid(rebateOptions);
    
    //阶段排名奖励分值验证
	var rankOptions = {
		event:'focus',
		ajax:{
			url:'${base}/incentivefee/incentive/rankValid.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: $("form#rankRewardScore").serialize(),
			cache:false
		},
		poshytip:{
			alignX: 'right'
		}
	};
    var rankValid = new $.Valid(rankOptions);

	jQuery("#stageParameterList").jqGrid({
        url: 'incentive/stageGrid.json',
        datatype: 'json',
        colNames: ["<msg:message code='stage.code'/>",
                   "<msg:message code='stage.codeName'/>",
                   "<msg:message code='stage.value'/>",
                   "<msg:message code='info.operate'/>"],
       	colModel: [{name: 'code',index:'code',align:'center',sortable:false,hidden:true},
        		   {name: 'codeName',index:'codeName',align:'center',sortable:false,width:'30%',formatter:function(cellvalue, options, rowObject){
			        	var content="";
			     	   	<c:forEach var="item" items="${stageParameterName}">
							   if(rowObject.code=="${item.key}"){
								   content="${item.value}";
							   }
						</c:forEach>
						return content;
		   			}},
                   {name: 'value',index:'value',align:'center',sortable:false,editable:true,width:'30%'},
                   {name:'operate',index:'operate',align:'left',sortable:false,width:'40%'}],
        postData:{c:"${stageParameter.c}"},
        mtype:"POST",
        height:"<msg:message code='jqgrid.height.150'/>",
        pager: false,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete: function(){
        	globalrecord = jQuery("#stageParameterList").getGridParam("records");
    		var ids = jQuery("#stageParameterList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			var content = "";
				
    			<c:if test="${!empty incentivefee_incentive_modify}">
				<security:authorize ifAnyGranted="${incentivefee_incentive_modify.code}">	
					content += "<a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_modify.indexCode}_stage_"+id+"' sid="+id+" class='shortcut_${incentivefee_incentive_modify.indexCode}_stage'>";
					content += "<img src='${base}/${skin}/${incentivefee_incentive_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_modify.name}";
					content += "</a>";
				</security:authorize>
				</c:if> 
				
				<c:if test="${!empty incentivefee_incentive_save}">	
				<security:authorize ifAnyGranted="${incentivefee_incentive_save.code}">	
					content += "<a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_save.indexCode}_stage_"+id+"' sid="+id+" style='color:#999;'>";
					content += "<img src='${base}/${skin}/${incentivefee_incentive_save.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_save.name}";
					content += "</a>";
				</security:authorize>
				</c:if>
				
				<c:if test="${!empty incentivefee_incentive_reset}">	
				<security:authorize ifAnyGranted="${incentivefee_incentive_reset.code}">	
						content += "<a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_reset.indexCode}_stage_"+id+"' sid="+id+" style='color:#999;'>";
						content += "<img src='${base}/${skin}/${incentivefee_incentive_reset.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_reset.name}";
						content += "</a>";
				</security:authorize>
				</c:if>
				
    			jQuery("#stageParameterList").jqGrid('setRowData',ids[i],{operate:"<div class='jqgridContainer'>" + content + "</div>"});
    		}
    	},
        caption:"<msg:message code='stage.list'/>",
        toolbar: [true,"top"]
	});
	
	$(".shortcut_${incentivefee_incentive_modify.indexCode}_stage").live("click",(function(){
		var rowid = $(this).attr("sid");
		if(stageStatus == 0){
			stageStatus = 1;
			jQuery("#stageParameterList").jqGrid('editRow',rowid);
			if(rowid==2){
				$("#"+rowid+"_value").addClass("Wdate");
				$("#"+rowid+"_value").attr("readOnly",true);
				$("#"+rowid+"_value").bind("click",function(){
					WdatePicker({dateFmt:'yyyy-MM'});
				});
				$("#"+rowid+"_value").bind("focus",function(){
					WdatePicker({dateFmt:'yyyy-MM'});
				});
			}
			$(this).attr("class","").css("color","#999");
			$("#shortcut_${incentivefee_incentive_save.indexCode}_stage_"+rowid).attr("class","shortcut_${incentivefee_incentive_save.indexCode}_stage").css("color","#53056A");
			$("#shortcut_${incentivefee_incentive_reset.indexCode}_stage_"+rowid).attr("class","shortcut_${incentivefee_incentive_reset.indexCode}_stage").css("color","#53056A");
		}else{
			top.$.layer({
				shade : [0.5 , '#000' , true],
		  		offset : ['50px',''],
		  	 	title : "<msg:message code='info.prompt'/>",
			    dialog : {msg:"<msg:message code='editing.save'/>", type:3}
			});
		}
	}));
		
	$(".shortcut_${incentivefee_incentive_reset.indexCode}_stage").live("click",( function() {
		stageStatus = 0;
		var rowid = $(this).attr("sid");
		jQuery("#stageParameterList").jqGrid('restoreRow',rowid);
		$(this).attr("class","").css("color","#999");	
		$("#shortcut_${incentivefee_incentive_save.indexCode}_stage_"+rowid).attr("class","").css("color","#999");	
		$("#shortcut_${incentivefee_incentive_modify.indexCode}_stage_"+rowid).attr("class","shortcut_${incentivefee_incentive_modify.indexCode}_stage").css("color","#53056A");
	}));
		
	$(".shortcut_${incentivefee_incentive_save.indexCode}_stage").live("click",(function() {
		var rowid = $(this).attr("sid");
		var rowdata = jQuery("#stageParameterList").jqGrid('getRowData',rowid);
		var code = rowdata.code;
		var value = $("#"+rowid+"_value").val();
		$.ajax({
			url:'incentive/saveStage.json',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data: {c:"${stageParameter.c}",id:rowid,code:code,value:value},
			success:function(jsonData){
				if (jsonData.status == "success"){
					location.reload();
					stageStatus = 0;
					$("#stageParameterList").trigger("reloadGrid");
				}else{
					top.$.layer({
						area : ['auto','auto'],
						dialog : {msg:"<msg:message code='save.fail'/>", type:8}
					});
				}
			}
		});
	}));
	
	jQuery("#rebateParameterList").jqGrid({
        url: 'incentive/rebateGrid.json',
        datatype: 'json',
        colNames: ["<msg:message code='rebate.levelName'/>", 
                   "<msg:message code='rebate.levelTag'/>",
                   "<msg:message code='rebate.stageScore'/>",
                   "<msg:message code='rebate.serviceFeeRat'/>",
                   "<msg:message code='info.operate'/>",
                   "<img id='rebateAddRow' src='${base}/${skin}/images/icon/Add.png' width='20' height='20'/>"
                   ],
        colModel: [{name: 'levelName',index:'levelName',align:'center',sortable:false,editable:true,width:'16%'},
                   {name: 'levelTag',index:'levelTag',align:'center',sortable:false,editable:true,width:'16%'},
                   {name: 'stageScore',index:'stageScore',align:'center',sortable:false,editable:true,width:'16%'},
                   {name: 'serviceFeeRat',index:'serviceFeeRat',align:'center',sortable:false,editable:true,width:'16%'},
                   {name:'operate',index:'operate',align:'left',sortable:false,width:'17%'},
                   {name: 'del',index:'del',align:'center',sortable:false,title:false,width:'3%'}
                   ],
        postData:{c:"${stageParameter.c}",type:2,stageTime:$("#stageTime").val()},
        mtype:"POST",
        height:"<msg:message code='jqgrid.height.150'/>",
        pager: false,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete: function(){
        	globalrecord = jQuery("#rebateParameterList").getGridParam("records");
    		var ids = jQuery("#rebateParameterList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			if(rebateCount<id){
	    			rebateCount = id;
    			}
    			var content = "";
				
    			<c:if test="${!empty incentivefee_incentive_modify}">
				<security:authorize ifAnyGranted="${incentivefee_incentive_modify.code}">	
					content += "<a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_modify.indexCode}_rebate_"+id+"' sid="+id+" class='shortcut_${incentivefee_incentive_modify.indexCode}_rebate'>";
					content += "<img src='${base}/${skin}/${incentivefee_incentive_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_modify.name}";
					content += "</a>";
				</security:authorize>
				</c:if> 
				
				<c:if test="${!empty incentivefee_incentive_save}">	
				<security:authorize ifAnyGranted="${incentivefee_incentive_save.code}">	
					content += "<a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_save.indexCode}_rebate_"+id+"' sid="+id+" style='color:#999;'>";
					content += "<img src='${base}/${skin}/${incentivefee_incentive_save.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_save.name}";
					content += "</a>";
				</security:authorize>
				</c:if>
				
				<c:if test="${!empty incentivefee_incentive_reset}">	
				<security:authorize ifAnyGranted="${incentivefee_incentive_reset.code}">	
						content += "<a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_reset.indexCode}_rebate_"+id+"' sid="+id+" style='color:#999;'>";
						content += "<img src='${base}/${skin}/${incentivefee_incentive_reset.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_reset.name}";
						content += "</a>";
				</security:authorize>
				</c:if>
				
    			jQuery("#rebateParameterList").jqGrid('setRowData',ids[i],{operate:"<div class='jqgridContainer'>" + content + "</div>"});
    			
    			<c:if test="${!empty incentivefee_incentive_delete}">
				<security:authorize ifAnyGranted="${incentivefee_incentive_delete.code}">
				jQuery("#rebateParameterList").jqGrid('setRowData',ids[i],{del:"<a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_delete.indexCode}_rebate_"+id+"' sid="+id+" class='shortcut_${incentivefee_incentive_delete.indexCode}_rebate' method><img src='${base}/${skin}/${incentivefee_incentive_delete.icon}' width='20' height='20' /></a>"});
				</security:authorize>
				</c:if>
    		}
    	},
        caption:"<msg:message code='rebate.list'/>",
        toolbar: [true,"top"]
	});
	
	$('#rebateAddRow').click(function(){
		if(rebateStatus == 0){
			rebateStatus = 1;
			rebateCount++;
			jQuery("#rebateParameterList").jqGrid('addRowData',rebateCount,"{}");
			var data = {
				levelName:"<input name='levelName' id='"+rebateCount+"_levelName' class='valid-control'/>",
				levelTag:"<input name='levelTag' id='"+rebateCount+"_levelTag' class='valid-control'/>",
				stageScore:"<input name='stageScore' id='"+rebateCount+"_stageScore' class='valid-control' maxlength='10'/>",
				serviceFeeRat:"<input name='serviceFeeRat' id='"+rebateCount+"_serviceFeeRat' class='valid-control'/>",
				operate:"<c:if test='${!empty incentivefee_incentive_modify}'><security:authorize ifAnyGranted='${incentivefee_incentive_modify.code}'><a href='javascript:void(0);'  id='shortcut_${incentivefee_incentive_modify.indexCode}_rebate_"
				+rebateCount+"' sid="+rebateCount+" style='color:#999;'><img src='${base}/${skin}/${incentivefee_incentive_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_modify.name}</a><a href='javascript:void(0);'  id='shortcut_${incentivefee_incentive_save.indexCode}_rebate_"
				+rebateCount+"' class='shortcut_${incentivefee_incentive_save.indexCode}_rebate' style='color:#53056A;' sid="+rebateCount+" method='add'><img src='${base}/${skin}/${incentivefee_incentive_save.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_save.name}</a><a href='javascript:void(0);'  id='shortcut_${incentivefee_incentive_reset.indexCode}_"+
				rebateCount+"' style='color:#999;'><img src='${base}/${skin}/${incentivefee_incentive_reset.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_reset.name}</a></security:authorize></c:if>",
				del:"<c:if test='${!empty incentivefee_incentive_delete}'><security:authorize ifAnyGranted='${incentivefee_incentive_delete.code}'><a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_delete.indexCode}_rebate_"
				+rebateCount+"' sid="+rebateCount+" class='shortcut_${incentivefee_incentive_delete.indexCode}_rebate' method='add'><img src='${base}/${skin}/${incentivefee_incentive_delete.icon}' width='20' height='20' /></a></security:authorize></c:if>"
			};
			jQuery("#rebateParameterList").jqGrid('setRowData',rebateCount,data);
		}else{
			top.$.layer({
				shade : [0.5 , '#000' , true],
		  		offset : ['50px',''],
		  	 	title : "<msg:message code='info.prompt'/>",
			    dialog : {msg:"<msg:message code='editing.save'/>", type:3}
			});
		}
	});	

	$(".shortcut_${incentivefee_incentive_delete.indexCode}_rebate").live("click",(function(){
		var rowid = $(this).attr("sid");
		var method = $(this).attr("method");
		if(method){
			jQuery("#rebateParameterList").jqGrid('delRowData',rebateCount);
		}else{
			var index = top.$.layer({
		  	    shade : [0.5 , '#000' , true],
		  	    area : ['auto','auto'],
		  	  	offset : ['150px',''],
		  	    dialog : {
		  	        msg:"<msg:message code='delete.sure'/>",
		  	        btns : 2, 
		  	        type : 4,
		  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
		  	        yes : function(){
		  	        	$.ajax({
			  	  			url:'incentive/deleteRebate.json',
			  	  			type:'post',
			  	  			timeout:'60000',
			  	  			dataType:'json',
			  	  			data: "id="+rowid, 
			  	  			success:function(jsonData,textStatus){
			  	  				if (textStatus == "success"){
			  	  					if (jsonData.status == "success"){
			  	  						jQuery("#rebateParameterList").jqGrid('delRowData',rowid);
			  	  						top.layer.close(index);
			  	  					}else{
			  	  						top.$.layer({
			  	  							area : ['auto','auto'],
			  	  							dialog : {msg:"<msg:message code='delete.fail'/>", type:8}
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
		rebateStatus = 0;
	}))
		
	$(".shortcut_${incentivefee_incentive_modify.indexCode}_rebate").live("click",(function(){
		var rowid = $(this).attr("sid");
		if(rebateStatus == 0){
			rebateStatus = 1;
			jQuery("#rebateParameterList").jqGrid('editRow',rowid);
			$(this).attr("class","").css("color","#999");
			$("#shortcut_${incentivefee_incentive_save.indexCode}_rebate_"+rowid).attr("class","shortcut_${incentivefee_incentive_save.indexCode}_rebate").css("color","#53056A");
			$("#shortcut_${incentivefee_incentive_reset.indexCode}_rebate_"+rowid).attr("class","shortcut_${incentivefee_incentive_reset.indexCode}_rebate").css("color","#53056A");
		}else{
			top.$.layer({
				shade : [0.5 , '#000' , true],
		  		offset : ['50px',''],
		  	 	title : "<msg:message code='info.prompt'/>",
			    dialog : {msg:"<msg:message code='editing.save'/>", type:3}
			});
		}
	}));
		
	$(".shortcut_${incentivefee_incentive_reset.indexCode}_rebate").live("click",( function() {
		rebateStatus = 0;
		var rowid = $(this).attr("sid");
		jQuery("#rebateParameterList").jqGrid('restoreRow',rowid);
		$(this).attr("class","").css("color","#999");	
		$("#shortcut_${incentivefee_incentive_save.indexCode}_rebate_"+rowid).attr("class","").css("color","#999");	
		$("#shortcut_${incentivefee_incentive_modify.indexCode}_rebate_"+rowid).attr("class","shortcut_${incentivefee_incentive_modify.indexCode}_rebate").css("color","#53056A");
	}));
		
	$(".shortcut_${incentivefee_incentive_save.indexCode}_rebate").live("click",(function() {
		var data="";
		var url="";
		var method = $(this).attr("method");
		var rowid = $(this).attr("sid");
		var clsn = $("#"+rowid+"_levelName").attr("class");
		var stageTime = $("#stageTime").val();
		if(method){
			data = "c=${stageParameter.c}&stageTime="+stageTime+"&levelName="+$.trim($("#"+rowid+"_levelName").val())+"&levelTag="+$.trim($("#"+rowid+"_levelTag").val())+"&type=2&stageScore="+$.trim($("#"+rowid+"_stageScore").val())+"&serviceFeeRat="+$.trim($("#"+rowid+"_serviceFeeRat").val());
			url = "incentive/saveRebate.json";
		}else{
			data = "c=${stageParameter.c}&id="+rowid+"&stageTime="+stageTime+"&levelName="+$.trim($("#"+rowid+"_levelName").val())+"&levelTag="+$.trim($("#"+rowid+"_levelTag").val())+"&stageScore="+$.trim($("#"+rowid+"_stageScore").val())+"&serviceFeeRat="+$.trim($("#"+rowid+"_serviceFeeRat").val());
			url = "incentive/modifyRebate.json";
		}
	 	var tempOpts = {
	  	   classNames:[clsn],
		   validStatus:function(status){
			   if (status){
					$.ajax({
						url:url,
						type:'post',
						timeout:'60000',
						dataType:'json',
						data: data,
						success:function(jsonData){
							if (jsonData.status == "success"){
								rebateStatus = 0;
								$("#rebateParameterList").trigger("reloadGrid");
							}else{
								top.$.layer({
	  	  							area : ['auto','auto'],
	  	  							dialog : {msg:"<msg:message code='save.fail'/>", type:8}
	  	  						});
							}
						}
					});
				}
			}
		};
		$.extend(rebateOptions,tempOpts);
		rebateValid.updateSetting(rebateOptions);
		rebateValid.reset();
		rebateValid.valid();
	}));
	
	jQuery("#rankRewardScoreList").jqGrid({
        url: 'incentive/rankGrid.json',
        datatype: 'json',
        colNames: ["<msg:message code='rank.rankName'/>", 
                   "<msg:message code='rank.rankTag'/>",
                   "<msg:message code='rank.rewardScore'/>",
                   "<msg:message code='info.operate'/>",
                   "<img id='rankAddRow' src='${base}/${skin}/images/icon/Add.png' width='20' height='20' />"
                   ],
        colModel: [{name: 'rankName',index:'rankName',align:'center',sortable:false,editable:true,width:'20%'},
                   {name: 'rankTag',index:'rankTag',align:'center',sortable:false,editable:true,width:'20%'},
                   {name: 'rewardScore',index:'rewardScore',align:'center',sortable:false,editable:true,width:'20%'},
                   {name:'operate',index:'operate',align:'left',sortable:false,width:'17%'},
                   {name: 'del',index:'del',align:'center',sortable:false,title:false,width:'3%'}
                   ],
        postData:{c:"${stageParameter.c}",type:2,stageTime:$("#stageTime").val()},
        mtype:"POST",
        height:"<msg:message code='jqgrid.height.150'/>",
        pager: false,
        autowidth: true,
        viewrecords: true,
        multiselect: false,
        jsonReader: {
        	repeatitems: false
        },
        gridComplete: function(){
        	globalrecord = jQuery("#rankRewardScoreList").getGridParam("records");
    		var ids = jQuery("#rankRewardScoreList").jqGrid('getDataIDs');
    		for(var i=0;i < ids.length;i++){
    			var id = ids[i];
    			if(rankCount<id){
	    			rankCount = id;
    			}
    			var content = "";
				
    			<c:if test="${!empty incentivefee_incentive_modify}">
				<security:authorize ifAnyGranted="${incentivefee_incentive_modify.code}">	
					content += "<a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_modify.indexCode}_rank_"+id+"' sid="+id+" class='shortcut_${incentivefee_incentive_modify.indexCode}_rank'>";
					content += "<img src='${base}/${skin}/${incentivefee_incentive_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_modify.name}";
					content += "</a>";
				</security:authorize>
				</c:if> 
				
				<c:if test="${!empty incentivefee_incentive_save}">	
				<security:authorize ifAnyGranted="${incentivefee_incentive_save.code}">	
					content += "<a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_save.indexCode}_rank_"+id+"' sid="+id+" style='color:#999;'>";
					content += "<img src='${base}/${skin}/${incentivefee_incentive_save.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_save.name}";
					content += "</a>";
				</security:authorize>
				</c:if>
				
				<c:if test="${!empty incentivefee_incentive_reset}">	
				<security:authorize ifAnyGranted="${incentivefee_incentive_reset.code}">	
						content += "<a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_reset.indexCode}_rank_"+id+"' sid="+id+" style='color:#999;'>";
						content += "<img src='${base}/${skin}/${incentivefee_incentive_reset.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_reset.name}";
						content += "</a>";
				</security:authorize>
				</c:if>
				
    			jQuery("#rankRewardScoreList").jqGrid('setRowData',ids[i],{operate:"<div class='jqgridContainer'>" + content + "</div>"});
    			
    			<c:if test="${!empty incentivefee_incentive_delete}">
				<security:authorize ifAnyGranted="${incentivefee_incentive_delete.code}">
				jQuery("#rankRewardScoreList").jqGrid('setRowData',ids[i],{del:"<a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_delete.indexCode}_rank_"+id+"' sid="+id+" class='shortcut_${incentivefee_incentive_delete.indexCode}_rank' method><img src='${base}/${skin}/${incentivefee_incentive_delete.icon}' width='20' height='20' /></a>"});
				</security:authorize>
				</c:if>
    		}
    	},
        caption:"<msg:message code='rank.list'/>",
        toolbar: [true,"top"]
	});
	
	$('#rankAddRow').click(function(){
		if(rankStatus == 0){
			rankCount++;
			rankStatus = 1;
			jQuery("#rankRewardScoreList").jqGrid('addRowData',rankCount,"{}");
			var data = {
					rankName:"<input name='rankName' id='"+rankCount+"_rankName' class='valid-control'/>",
					rankTag:"<input name='rankTag' id='"+rankCount+"_rankTag' class='valid-control'/>",
					rewardScore:"<input name='rewardScore' id='"+rankCount+"_rewardScore' class='valid-control'/>",
					operate:"<c:if test='${!empty incentivefee_incentive_modify}'><security:authorize ifAnyGranted='${incentivefee_incentive_modify.code}'><a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_modify.indexCode}_rank_"
					+rankCount+"' sid="+rankCount+" style='color:#999;'><img src='${base}/${skin}/${incentivefee_incentive_modify.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_modify.name}</a><a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_save.indexCode}_rank_"
					+rankCount+"' class='shortcut_${incentivefee_incentive_save.indexCode}_rank' style='color:#53056A;' sid="+rankCount+" method='add'><img src='${base}/${skin}/${incentivefee_incentive_save.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_save.name}</a><a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_reset.indexCode}_rank_"+
					rankCount+"' style='color:#999;'><img src='${base}/${skin}/${incentivefee_incentive_reset.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_incentive_reset.name}</a></security:authorize></c:if>",
					del:"<c:if test='${!empty incentivefee_incentive_delete}'><security:authorize ifAnyGranted='${incentivefee_incentive_delete.code}'><a href='javascript:void(0);' id='shortcut_${incentivefee_incentive_delete.indexCode}_rank_"
					+rankCount+"' sid="+rankCount+" class='shortcut_${incentivefee_incentive_delete.indexCode}_rank' method='add'><img src='${base}/${skin}/${incentivefee_incentive_delete.icon}' width='20' height='20' /></a></security:authorize></c:if>"
			};
			jQuery("#rankRewardScoreList").jqGrid('setRowData',rankCount,data);
		}else{
			top.$.layer({
				shade : [0.5 , '#000' , true],
		  		offset : ['50px',''],
		  	 	title : "<msg:message code='info.prompt'/>",
			    dialog : {msg:"<msg:message code='editing.save'/>", type:3}
			});
		}
	});

	$(".shortcut_${incentivefee_incentive_delete.indexCode}_rank").live("click",(function(){
		var rowid = $(this).attr("sid");
		var method = $(this).attr("method");
		if(method){
			jQuery("#rankRewardScoreList").jqGrid('delRowData',rankCount);
		}else{
			var index = top.$.layer({
		  	    shade : [0.5 , '#000' , true],
		  	    area : ['auto','auto'],
		  	  	offset : ['150px',''],
		  	    dialog : {
		  	        msg:"<msg:message code='delete.sure'/>",
		  	        btns : 2, 
		  	        type : 4,
		  	        btn : ["<msg:message code='button.confirm'/>","<msg:message code='button.cancel'/>"],
		  	        yes : function(){
		  	        	$.ajax({
		  	  			url:'incentive/deleteRank.json',
		  	  			type:'post',
		  	  			timeout:'60000',
		  	  			dataType:'json',
		  	  			data: "id="+rowid, 
		  	  			success:function(jsonData,textStatus){
		  	  				if (textStatus == "success"){
		  	  					if (jsonData.status == "success"){
		  	  						jQuery("#rankRewardScoreList").jqGrid('delRowData',rowid);
		  	  						top.layer.close(index);
		  	  					}else{
		  	  						top.$.layer({
		  	  							area : ['auto','auto'],
		  	  							dialog : {msg: "<msg:message code='delete.fail'/>" ,type : 8}	
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
		rankStatus = 0;
	}))
		
	$(".shortcut_${incentivefee_incentive_modify.indexCode}_rank").live("click",(function(){
		var rowid = $(this).attr("sid");
		if(rankStatus == 0){
			rankStatus = 1;
			jQuery("#rankRewardScoreList").jqGrid('editRow',rowid);
			$(this).attr("class","").css("color","#999");
			$("#shortcut_${incentivefee_incentive_save.indexCode}_rank_"+rowid).attr("class","shortcut_${incentivefee_incentive_save.indexCode}_rank").css("color","#53056A");
			$("#shortcut_${incentivefee_incentive_reset.indexCode}_rank_"+rowid).attr("class","shortcut_${incentivefee_incentive_reset.indexCode}_rank").css("color","#53056A");
		}else{
			top.$.layer({
				shade : [0.5 , '#000' , true],
		  		offset : ['50px',''],
		  	 	title : "<msg:message code='info.prompt'/>",
			    dialog : {msg:"<msg:message code='editing.save'/>", type:3}
			});
		}
	}));
		
	$(".shortcut_${incentivefee_incentive_reset.indexCode}_rank").live("click",( function() {
		rankStatus = 0;
		var rowid = $(this).attr("sid");
		jQuery("#rankRewardScoreList").jqGrid('restoreRow',rowid);
		$(this).attr("class","").css("color","#999");	
		$("#shortcut_${incentivefee_incentive_save.indexCode}_rank_"+rowid).attr("class","").css("color","#999");	
		$("#shortcut_${incentivefee_incentive_modify.indexCode}_rank_"+rowid).attr("class","shortcut_${incentivefee_incentive_modify.indexCode}_rank").css("color","#53056A");
	}));
		
	$(".shortcut_${incentivefee_incentive_save.indexCode}_rank").live("click",(function() {
		var data="";
		var url = "";
		var method = $(this).attr("method");
		var rowid = $(this).attr("sid");
		var clsn = $("#"+rowid+"_rankName").attr("class");
		var stageTime = $("#stageTime").val();
		if(method){
			data = "c=${stageParameter.c}&stageTime="+stageTime+"&rankName="+$.trim($("#"+rowid+"_rankName").val())+"&rankTag="+$.trim($("#"+rowid+"_rankTag").val())+"&rewardScore="+$.trim($("#"+rowid+"_rewardScore").val())+"&type=2";
			url = "incentive/saveRank.json";
		}else{
			data = "c=${stageParameter.c}&id="+rowid+"&stageTime="+stageTime+"&rankName="+$.trim($("#"+rowid+"_rankName").val())+"&rankTag="+$.trim($("#"+rowid+"_rankTag").val())+"&rewardScore="+$.trim($("#"+rowid+"_rewardScore").val())+"&type=2";
			url = "incentive/modifyRank.json";
		}
	 	var tempOpts = {
	  	   classNames:[clsn],
		   validStatus:function(status){
			   if (status){
					$.ajax({
						url:url,
						type:'post',
						timeout:'60000',
						dataType:'json',
						data: data, 
						success:function(jsonData){
							if (jsonData.status == "success"){
								rankStatus = 0;
								$("#rankRewardScoreList").trigger("reloadGrid");					
							}else{
								top.$.layer({
	  	  							area : ['auto','auto'],
	  	  							dialog : {msg: "<msg:message code='save.fail'/>" ,type : 8}	
	  	  						});
							}
						}
						
					});
				}
			}
		};
		$.extend(rankOptions,tempOpts);
		rankValid.updateSetting(rankOptions);
		rankValid.reset();
		rankValid.valid();
	}));
});

function reloadList(){
	jQuery("#rebateParameterList").jqGrid("setGridParam",{postData:{c:"${stageParameter.c}",type:2,stageTime:$("#stageTime").val()}}).trigger("reloadGrid");
	jQuery("#rankRewardScoreList").jqGrid("setGridParam",{postData:{c:"${stageParameter.c}",type:2,stageTime:$("#stageTime").val()}}).trigger("reloadGrid");
}


</script>
</head>
<body class="skinMain">
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
	<tr>
		<td width="350px" valign="top">
			<table id="stageParameterList" ><tr><td>&nbsp;</td></tr></table>
			<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all">
				<div class="ui-jqgrid-view">
					<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">计划发放周期</div>
					<div class="ui-state-default ui-jqgrid-hdiv">
						<div class="ui-jqgrid-hbox">
							<select id="stageTime" size="${stageTimeMapSize}" style="width: 350px;" onchange="reloadList()">
								<c:forEach items="${stageTimeMap}" var="item">
									<option value="${item.stageKey}">${item.stageDate}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
			</div>
		</td>
		<td>
			<form:form commandName="rebateParameter">
				<table id="rebateParameterList" ><tr><td>&nbsp;</td></tr></table>
			</form:form>
			<form:form commandName="rankRewardScore">
				<table id="rankRewardScoreList" ><tr><td>&nbsp;</td></tr></table>
			</form:form>
		</td>
	</tr>
</table>
</body>
</html>