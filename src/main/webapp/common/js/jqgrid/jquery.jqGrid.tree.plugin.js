/***
 *	jqgrid 树形表格数据处理插件
 *	zgq
 **/
$.pluginJQGT = function(options,data){
	var PLUGIN = this;
	// 树形参数
	var settings = {	
		type: "json",						// 默认为JSON，后续可扩展XML数据
		id: "id",							// 树形结构数据id字段名称（树节点ID）
		parent: "parentId",					// 树形结构数据parentId（树节点父ID）字段名称
		root: "0",							// 树形结构数据root（树根节点默认值）
		level: "level",						// 树形结构数据level（树形级别）字段名称
		isLeaf: "isLeaf",					// 树形结构数据isLeaf（是否为子节点）字段名称
		expanded: null,						// 树形结构数据expanded （是否展开）
		loaded: null						// 树形结构数据loaded （是否异步加载）
	};	
	//待处理的树形数据
	var resultData = [];
	$.extend(settings, options);
	//递归函数
	PLUGIN.recursion = function(parentIdValue){
		var type = settings.type;
		if (type == "json"){
			if (data){
				for (var i=0;i<data.length;i++){
					if (data[i]){
						var id = eval("data[i]." + settings.id);
						var pid = eval("data[i]." + settings.parent);
						var level = eval("data[i]." + settings.level);
						var isLeaf = eval("data[i]." + settings.isLeaf) == 1 ? true : false;
						var expanded = settings.expanded;
						var loaded = settings.loaded;
						if (pid == parentIdValue){
							//tree数据处理状态合并
							var tempData = $.extend(data[i],{
								parent:pid,
		        				level:level,
		        				parent:pid,
		        				isLeaf:isLeaf,
		        				expanded:expanded,
		        				loaded:loaded
		        			});
							//存放到对象数组中
							resultData.push(tempData);
							//递归调用
		        			PLUGIN.recursion(id);
						}
					}
				}
			}
		}
	};
	//调用数据递归处理方法
	PLUGIN.recursion(settings.root);
	return resultData;
};
