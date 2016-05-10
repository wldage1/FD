(function($){
	//默认参数
	var settings = {
		//按钮点击前执行事件
		beforeClick:null,
		//按钮点击后执行事件
		afterClick:null,
		//form id
		form:'',
		//页面控件需要验证提示的className
		classNames:['valid-control'],
		//控件事件是否有独立提示,只支持 click,change
		event:null,
		//页面控件名称存储字段
		pageControlId:'field',
		//提示信息存储字段名称
		messageName:'msg',
		//ajax成功返回自定义函数
		ajaxSuccess:null,
		//验证状态返回函数
		validStatus:null,
		//验证失败返回函数
		validError:null,
		//绑定页面控件事件，click，focus等鼠标事件
		//提交页面控件元素默认值click，默认进行鼠标事件
		submitEvent:'click',
		//气泡提示信息poshytip参数
		poshytip:{
			className: 'tip-violet',	
			showOn: 'none',
			alignTo: 'target',
			alignX: 'right',
			alignY: 'center',
			offsetX: 10
		},
		//ajax请求参数
		ajax:{
			url:'',
			type:'post',
			timeout:'60000',
			dataType:'json',
			data:'', 
			cache:false
		}			
	};	
	
	//函数
	$.Valid = function(options) {
		//外围参数覆盖
		$.extend(true,settings,options);
		//调用初始化方法
		this.init();
		//页面点击处理事件
		this.pageClick();
	};
	//属性方法
	$.Valid.prototype = {
		//初始化函数
		init: function() {
			var $this = this;
			var classNames = settings.classNames;
			//设置气泡提示
			for(var i=0;i<classNames.length;i++){
				//为空不处理
				if (!classNames[i]){
					continue;
				}
				$("." + classNames[i]).poshytip(settings.poshytip);
			}
			//设置验证控件元素触发事件
			if (settings.event){
				for(var i=0;i<classNames.length;i++){
					$("." + classNames[i]).unbind(settings.event).bind(settings.event,function(){
						$this.valid($(this));
					});
//					$("." + classNames[i]).die(settings.event).live(settings.event,function(){
//						alert("dddd")
//						$this.valid($(this));
//					});
				}
			}
		},
		//重置函数
		reset:function(){
			this.init();
		},
		//页面点击事件
		pageClick:function(){
			var $this = this;
			//点击页面其他地方起泡提示隐藏
			$(document).unbind().bind("click",function(event) {
				var targetType = $(event.target).attr("type");
				//button不隐藏
				if (targetType != 'button' || targetType != 'submit'){
					$this.hide();
				}
			});	
		},
		//验证前执行函数
		beforeClick:function($obj){
			//鼠标点击前执行事件
			if (typeof settings.beforeClick == 'function'){
				settings.beforeClick($obj);
			}
		},
		//验证前执行函数
		afterClick:function($obj){
			//鼠标点击前执行事件
			if (typeof settings.afterClick == 'function'){
				settings.afterClick($obj);
			}
		},
		//验证函数
		valid:function(id){
			//每次验证都隐已经提示的页面控件元素
			this.hide();
			//调用ajax验证
			this.ajaxSuccess(id);
			//获取form 页面控件值
			this.formParams();	
			//ajax 提交验证
			$.ajax(settings.ajax);	
		},
		//从写ajax成功函数
		ajaxSuccess:function(obj){
			//自定义成功函数覆盖ajax成功 函数
			if (typeof settings.ajaxSuccess == 'function'){
				//覆盖ajax成功事件
				$.extend(settings.ajax,{success:settings.ajaxSuccess});
			}
			else{
				$.extend(settings.ajax,{success:function(jsonData){
					//是否存在验证控件元素
					var validStatus = true;
					//提示控件对象
					var $tempObj;
					//后台有错误提示返回结果
					if (jsonData && jsonData.result && jsonData.result.length>0){
						//class名称
						var classNames = settings.classNames;
						for(var i=0;i<classNames.length;i++){
							//为空不处理
							if (!classNames[i]){
								continue;
							}
							var className = classNames[i];
							//如果id值有效，说明是单个页面控件元素触发验证
							if (obj){
								var	id = $(obj).attr("id");
								var	name = $(obj).attr("name");
								//比对返回错误结果
								for (var r=0;r<jsonData.result.length;r++){
									//页面控件要求id和name一直
									var field = eval("jsonData.result["+r+"]." + settings.pageControlId);
									var msg = eval("jsonData.result["+r+"]." + settings.messageName);
									if (name == field){
										//找到为true
										validStatus = false;
										$tempObj = $("#" + id);
										if (!$tempObj){
											//如果有多个同名的控件，提示信息在最后一个控件上显示
											$tempObj = $("." + classNames + "[name='" + id + "']");
											//pcnames长度大于1 ，一般为radio 或checkbox 控件，提示信息在最后一个页面控件上提示
											if ($tempObj.length > 1){
												$tempObj = $("." + classNames + "[name='" + id + "']:last");
											}
										}	
										//控件高度
										var height = $tempObj.height();
										//计算偏移量
										var targetOffset = $tempObj.offset().top - height;
										//动画滚动位置
						                $('html,body').animate({
						                    scrollTop: targetOffset
						                },100,function(){
						                	$tempObj.css("border","1px solid #FFC83D");
						                	$tempObj.poshytip("reset");
						                	$tempObj.poshytip("update", msg);
						                	$tempObj.poshytip("show");
						                });	
						                break;
									}
								}
								//找到后结束循环
								if (!validStatus){
									break;
								}
							}
							else{
								//将验证页面控件元素转成数组
								var validArr = $.makeArray($("." + className));
								for (var v=0;v<validArr.length;v++){
									var $obj = $(validArr[v]);
									var tempId = $obj.attr("id");
									var tempName = $obj.attr("name");
									//页面控件id存在
									if (tempId){
										//比对返回错误结果
										for (var r=0;r<jsonData.result.length;r++){
											//默认只对第一页面控件进行提示
											var field = eval("jsonData.result["+r+"]." + settings.pageControlId);
											var msg = eval("jsonData.result["+r+"]." + settings.messageName);
											if (tempName == field){
												//找到为true
												validStatus = false;
												$tempObj = $("#" + tempId);
												if (!$tempObj){
													//如果有多个同名的控件，提示信息在最后一个控件上显示
													$tempObj = $("." + classNames + "[name='" + tempId + "']");
													//pcnames长度大于1 ，一般为radio 或checkbox 控件，提示信息在最后一个页面控件上提示
													if ($tempObj.length > 1){
														$tempObj = $("." + classNames + "[name='" + tempId + "']:last");
													}
												}	
												//控件高度
												var height = $tempObj.height();
												//计算偏移量
												var targetOffset = $tempObj.offset().top - height;
												//动画滚动位置
								                $('html,body').animate({
								                    scrollTop: targetOffset
								                },100,function(){
								                	$tempObj.css("border","1px solid #FFC83D");
								                	$tempObj.poshytip("reset");
								                	$tempObj.poshytip("update", msg);
								                	$tempObj.poshytip("show");
								                });	
								                break;
											}
										}
										//找到后结束循环
										if (!validStatus){
											break;
										}
									}
								}	
								//找到后结束大循环
								if (!validStatus){
									break;
								}
							}
						}
					}
					//调用验证通过函数 ，只有点击提交按钮时，才执行通过函数
					if (validStatus && obj == undefined){
						if(typeof settings.validStatus == 'function'){
							settings.validStatus(validStatus,$tempObj);
						}
					}
					if(typeof settings.validError == 'function'){
						settings.validError(validStatus,$tempObj);
					}
					
				}});
			}
		},
		//隐藏验证函数
		hide:function(){
			var classNames = settings.classNames;
			for (var j=0; j<classNames.length;j++){
				$("."+classNames[j]).each(function(){
					$(this).poshytip("hide");
					$(this).css("border","1px solid #A6C9E2");
				});
			}
		},
		//移除验证函数
		remove:function($obj){
			//取消气泡提示
			$obj.poshytip("destroy");
			//清除绑定事件
			$obj.unbind();
		},
		updateSetting:function(options){
			$.extend(true,settings,options);
		},
		//获取form 页面控件值
		formParams:function(){
			if (settings.form){
				$.extend(settings.ajax,{data:$("form#"+settings.form).serialize()});
			}
			else{
				$.extend(settings.ajax,{data:$("form").serialize()});
			}
		}
	};
	$.fn.valid = function(options) {
		var valid = new $.Valid(options);
		var ent = settings.submitEvent;
		$(this).unbind(ent).bind(ent,function(){
			//调用前执行函数
			valid.beforeClick($(this));
			valid.valid();
			//调用后执行函数
			valid.afterClick($(this));
		});
	};
	
})(jQuery);