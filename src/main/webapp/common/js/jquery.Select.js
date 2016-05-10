/***
 *	lSelect(Link Select) 无限级联动下拉菜单插件
 *
 *  JSON数据格式示例：[{"title": "北京", "value": "beijing"},{"title": "湖南", "value": "hunan"},{"title": "湖北", "value": "hubei"}]
 *
 *
 **/

(function($){
$.extend($.fn, {
	lSelect: function(options){
	
		// 默认参数
		var settings = {
			url: "",							// 数据获取url
			parameter: "path",					// 数据获取参数名称
			title: "title",						// 定义JSON数据格式：选择名称
			value: "value",						// 定义JSON数据格式：选择值
			path: "",							//定义默认值,默认值为所有父节点ID包括当前节点ID
			emptyOption: "--请选择--",			// 选择提示,null表示无提示
			cssClass: "lSelect",				// 下拉框css名称
			cssStyle: {"margin-right": "10px"},	// 下拉框左右css样式
			isFadeIn: true,						// 是否渐显
			onChange: null,
			selectClass:"",						// 当前显示select所有class值
			count: 1
		};
		
		$.extend(settings, options);
		
		return this.each(function(){
			var $this = $(this);
			$this.hide();
			var selectGroupClass = "lSelect" + Math.round(Math.random() * 1000000);
			var items = {};
			
			if (settings.path == "") {
				addSelect($this);
			} else {
				var $select = $this;
				var defaultSelectedValueArray = settings.path.split(",");
				for (var i = 0; i < defaultSelectedValueArray.length; i++) {
					var $nextSelect = addSelect($select, defaultSelectedValueArray[i]);
					if($nextSelect) {
						$select = $nextSelect;
					}
				}
			}
			
			// 绑定Select元素
			function bindSelect(element) {
				element.bind("change", function(){
					addSelect(element);
					var this_val = changeValue(element) ;
					$this.val(this_val);
					
					if(settings.onChange != null) {
						settings.onChange.call(this, this_val);
					}
				});
			}
			
			//当select更换时
			function changeValue(element){
				if(element.val() == "") {
					for(var i = settings.count; i > 0; i --) {
						var s_value = $("#" + selectGroupClass + i).val() ;
						if(s_value != "" && typeof(s_value) != "undefined") {
							return s_value ;
						}
					}
				}else {
					return element.val() ;
				}
			}
			
			// 获取Json数据
			function getJson(key) {
				if(typeof(items[key]) == "undefined") {
					var url = settings.url;
					if (key != "lSelectRoot") { 
						var parameter = settings.parameter;
						if (parameter != null) {
							if(url.indexOf("?") > 0) {
								url = url + "&" + parameter + "=" + key;
							} else {
								url = url + "?" + parameter + "=" + key;
							}
						}
					} 
					
					$.ajaxSetup({
						async: false,
						type: "get" 
					});
					
					//加入随机参数，防止ajax不能提交
					var number = Math.random();
					url = url + "&number=" + number ;
					
					$.getJSON(url, function(json) {
						items[key] = json;
					});
				}
				
				return items[key];
			}
			
			// 填充option
			function fill(element, key, selected) {
				var json = getJson(key);
				if (!json) {
					return false;
				}
				var length = 0;
				for (j in json){
					length ++;
				}
				if (length == 0) {
					return false;
				} else {
					element.empty();
					if(settings.emptyOption != null) {
						element.append('<option value="">' + settings.emptyOption + '</option>');
					}
					$.each(json, function(id, object) {
						var optionValue = "";
						/**
						if (object[settings.value].indexOf(",") >= 0) {
							var optionValueArray = object[settings.value].split(",");
							optionValue =  optionValueArray[optionValueArray.length - 1];
						} else {
							optionValue = object[settings.value];
						}
						**/
						optionValue = object[settings.value];
						var option;
						if(selected && optionValue == selected) {
							option	= $('<option value="' + object[settings.value] + '" selected>' + object[settings.title] + '</option>');
						} else {
							option	= $('<option value="' + object[settings.value] + '">' + object[settings.title] + '</option>');
						}
						element.append(option);
					});
					return true;
				}
			}
			
			// 增加select
			function addSelect(element, selected) {
				var $nextSelect;
				var isFill;
				if(element.is("select")) {
					element.nextAll("." + selectGroupClass).remove();
					if(element.val() == "") {
						return;
					}
					element.after('<select id="' + selectGroupClass + settings.count + '" class="' + settings.cssClass + ' ' + selectGroupClass + '" style="display: none;"></select>');
					$nextSelect = element.next("." + selectGroupClass);
					isFill = fill($nextSelect, element.val(), selected);
				} else {
					element.after('<select id="' + selectGroupClass + settings.count + '" class="' + settings.cssClass + ' ' + selectGroupClass + '" style="display: none;"></select>');
					$nextSelect = element.next("." + selectGroupClass);
					isFill = fill($nextSelect, "lSelectRoot", selected);
				}
				if (isFill) {
					settings.count = settings.count + 1 ;
					element.css(settings.cssStyle);
					if(settings.isFadeIn) {
						$nextSelect.fadeIn();
						bindSelect($nextSelect);
					} else {
						$nextSelect.show();
					}
					return $nextSelect;
				} else {
					settings.count = settings.count - 1 ;
					$nextSelect.remove();
				}
			}
		});

	}
});
})(jQuery);