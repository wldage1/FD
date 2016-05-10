// 查询工具条
var searchTB = new SearchToolbar();
function SearchToolbar()
{
	// 工具条要显示的行数
	this.rowCount = 0;
	// 工具条要显示的容器ID
	this.elementID = "";
	// 工具条基本数据
	this.searchData = "";

	this.setRowCount = function(rowCount)
	{
		if (!rowCount)
		{
			this.rowCount = 3;
		}
		this.rowCount = rowCount;
	};

	this.setElementID = function(elementID)
	{
		if (!elementID)
		{
			alert(elementIDError);
			return false;
		}
		this.elementID = elementID;
		return true;
	};

	this.setSearchData = function(searchData)
	{
		if (!searchData)
		{
			alert(searchDataError);
			return false;
		}
		this.searchData = searchData;
		return true;
	};

	// ajax回调函数，并生成工具条信息
	this.execute = function()
	{
		var group1 = "", group2 = "";
		var index = 0;
		for ( var t = 0; t < 2; t++)
		{
			var temp = "<table align=\"center\" border=\"0\" cellpadding=\"1\" cellspacing=\"0\" class=\"rowtable\" width=\"100%\">";
			for ( var r = 0; r < this.rowCount; r++)
			{
				temp += "<tr class=\"searchRow\">";
				if (r == 0)
				{
					if (t == 0)
						temp += "<td align=\"center\">1</td>";
					else
						temp += "<td align=\"center\">2</td>";
				}
				if (r > 0)
				{
					temp += "<td align=\"center\" width=\"15%\" valign=\"middle\">";
					temp += "<select  class='searchOrAnd' style=\"height:26px;\" name='andor" + index + "' id='andor" + index + "'>";
					temp += "<option value='and'>" + and + "</option>";
					temp += "<option value='or'>" + or + "</option>";
					temp += "</select>";
					temp += "</td>";
				}
				temp += "<td align=\"center\" valign=\"middle\" width=\"25%\">";
				var columns = "<select class=\"searchColumn\" style=\"height:26px;\"  name=\"column" + index + "\" id=\"column"
						+ index + "\">";
				columns += "<option value=\"\"></option>";
				columns += "</select>";
				var columnObj = this.searchData.columns;
				if (!columnObj)
					temp += columns;
				var dindex = 0, sindex = 0, dateTypes = "", selectTypes = "", columnNames = "";
				for ( var c = 0; c < columnObj.length; c++)
				{
					if (columnObj[c].search)
					{
						if (columnObj[c].search.iscol && columnObj[c].search.iscol == "1")
						{
							if (columnObj[c].sqlfunc)
							{
								columnNames += "<option value=\"" + columnObj[c].sqlfunc + "\">" + columnObj[c].desc + "</option>";
							}
							else
							{
								columnNames += "<option value=\"" + columnObj[c].name + "\">" + columnObj[c].desc + "</option>";
							}
							if (columnObj[c].search.type && columnObj[c].search.type.toLowerCase() == "date")
							{
								if (dindex == 0)
									dateTypes = columnObj[c].name;
								else
									dateTypes += "," + columnObj[c].name;
								dindex++;
							}
							else if (columnObj[c].search.type && columnObj[c].search.type.toLowerCase() == "select")
							{
								if (sindex == 0)
									selectTypes = columnObj[c].name;
								else
									selectTypes += "," + columnObj[c].name;
								sindex++;
							}
						}
					}
				}
				temp += "<select class=\"searchColumn\" style=\"height:26px;\"  name=\"column" + index;
				temp += "\" id=\"column" + index + "\"";
				temp += " onChange=\"columnHandler('" + selectTypes + "','" + dateTypes + "'," + index + ");";
				temp += " conditionHandler('" + selectTypes + "','" + dateTypes + "'," + index + ");";
				if (this.searchData.searchFuncName)
				{
					temp += this.searchData.searchFuncName + "('columnValue" + index + "');\">";
				}
				else
				{
					temp += "\">";
				}
				temp += "<option value=''>--" + pleaseSelect + "--</option>";
				temp += columnNames;
				temp += "</select>";
				temp += "</td>";
				temp += "<td width=\"25%\" valign=\"middle\">";
				temp += "<span id='conditionSpan" + index + "'>";
				temp += "<select class='searchCondition' style=\"height:26px;\"  name='condition" + index + "' id='condition" + index + "'>";
				temp += "<option value=''>--" + pleaseSelect + "--</option>";
				temp += "</select>";
				temp += "</span>";
				temp += "</td>";
				temp += "<td width=\"25%\" valign=\"middle\">";
				temp += "<span id='continer" + index + "'><input type='text' name='columnValue";
				temp += index + "' id='columnValue" + index + "' size='7' style=\"height:18px;\" class='searchInput'></span>";
				temp += "</td>";
				temp += "</tr>";
				index++;
			}
			temp += "</table>";
			if (t == 0)
				group1 = temp;
			else
				group2 = temp;

		}
		var toolBar = "<table width=\"100%\" height=\"80\" align=\"center\" border=\"0\" class=\"searchForm\"";
		toolBar += " cellpadding=\"0\" cellspacing=\"0\"  id=\"queryTable\">";
		toolBar += "<tr class=\"searchRow\" align=\"center\">";
		toolBar += "<td class=\"group\" valign=\"middle\">";
		toolBar += group1;
		toolBar += "</td>";
		toolBar += "<td align=\"center\"  class=\"orandTD\">";
		toolBar += "<input type=\"radio\" name=\"_andor\" checked id=\"_andor\" value='and' style=\"vertical-align:middle;\"><span style=\"vertical-align:middle;\">" + and + "</span>";
		toolBar += "<br>";
		toolBar += "<br>";
		toolBar += "<input type=\"radio\" name=\"_andor\" id=\"_andor\" value='or' style=\"vertical-align:middle;\"><span style=\"vertical-align:middle;\">" + or + "</span>";
		toolBar += "</td>";
		toolBar += "<td class=\"group\">";
		toolBar += group2;
		toolBar += "</td>";
		toolBar += "</tr>";
		toolBar += "</table>";
		//alert(toolBar);
		// document.writeln(toolBar);
		document.getElementById(this.elementID).innerHTML = toolBar;
	};
}
//查询字段对应的条件信息
function conditionHandler(selectTypes, dateTypes, index)
{
	var obj = document.getElementById("column" + index);
	var selectName = obj.options[obj.selectedIndex].value;
	var sflag = 0, dflag = 0;
	var dateCondition;
	var selectCondition;
	var inputCondition;
	var expressions =jsonData.expressions;
	for ( var i = 0; i < expressions.length; i++)
	{
		if (expressions[i].type && expressions[i].type=='date')
		{
			dateCondition = expressions[i].option;
		}
		if (expressions[i].type && expressions[i].type=='select')
		{
			selectCondition = expressions[i].option;
		}
		if (expressions[i].type && expressions[i].type=='input')
		{
			inputCondition = expressions[i].option;
		}
	}
	var dateNameArray = dateTypes.split(",");
	for ( var i = 0; i < dateNameArray.length; i++)
	{
		if (!dateNameArray[i])
			continue;
		if (selectName && (selectName == dateNameArray[i] || selectName.indexOf(dateNameArray[i]) != -1))
		{
			sflag = 1;
			var htmlInfo = "<select class='searchCondition' style=\"height:26px;\" name='condition" + index + "' id='condition" + index + "'>";
			if(!dateCondition)
				htmlInfo += "<option value=''>--" + pleaseSelect + "--</option>";
			for(var j = 0; j < dateCondition.length; j++)
			{
				if (!dateCondition[j])
					continue;
				if (dateCondition[j].isValid && dateCondition[j].isValid == "1")
				{
					htmlInfo += "<option value='" + dateCondition[j].value + "'>" + dateCondition[j].desc + "</option>";
				}
			}
			htmlInfo += "</select>";
			document.getElementById("conditionSpan" + index).innerHTML = htmlInfo;
			break;
		}
	}
	var selectNameArray = selectTypes.split(",");
	for ( var k = 0; k < selectNameArray.length; k++)
	{
		if (!selectNameArray[k])
			continue;
		if (selectName && (selectName == selectNameArray[k] || selectName.indexOf(selectNameArray[k]) != -1))
		{
			dflag = 1;
			var htmlInfo = "<select class='searchCondition' style=\"height:26px;\" name='condition" + index + "' id='condition" + index + "'>";
			if(!selectCondition)
				htmlInfo += "<option value=''>--" + pleaseSelect + "--</option>";
			for(var j = 0; j < selectCondition.length; j++)
			{
				if (!selectCondition[j])
					continue;
				if (selectCondition[j].isValid && selectCondition[j].isValid == "1")
				{
					htmlInfo += "<option value='" + selectCondition[j].value + "'>" + selectCondition[j].desc + "</option>";
				}
			}
			htmlInfo += "</select>";
			document.getElementById("conditionSpan" + index).innerHTML = htmlInfo;
			break;
		}
	}

	if (dflag == 0 && sflag == 0)
	{
		var htmlInfo = "<select class='searchCondition' style=\"height:26px;\" name='condition" + index + "' id='condition" + index + "'>";
		if(selectName != "")
		{
			if(!inputCondition)
				htmlInfo += "<option value=''>--" + pleaseSelect + "--</option>";
			for(var j = 0; j < inputCondition.length; j++)
			{
				if (!inputCondition[j])
					continue;
				if (inputCondition[j].isValid && inputCondition[j].isValid == "1")
				{
					htmlInfo += "<option value='" + inputCondition[j].value + "'>" + inputCondition[j].desc + "</option>";
				}
			}
		}
		else
		{
			htmlInfo += "<option value=''>--" + pleaseSelect + "--</option>";
		}
		htmlInfo += "</select>";
		document.getElementById("conditionSpan" + index).innerHTML = htmlInfo;
		
	}
}
// 查询字段对应的值信息
function columnHandler(selectTypes, dateTypes, index)
{
	var obj = document.getElementById("column" + index);
	var selectName = obj.options[obj.selectedIndex].value;
	var dateNameArray = dateTypes.split(",");
	var sflag = 0, dflag = 0;
	for ( var i = 0; i < dateNameArray.length; i++)
	{
		if (!dateNameArray[i])
			continue;
		if (selectName && (selectName == dateNameArray[i] || selectName.indexOf(dateNameArray[i]) != -1))
		{
			sflag = 1;
			var htmlInfo = "<input type=\"text\" name=\"columnValue" + index + "\" id=\"columnValue" + index;
			htmlInfo += "\" readonly=\"readonly\" class=\"Wdate\" size=\"7\" style=\"height:20px;\" onfocus=\"WdatePicker({isShowClear:false,readOnly:true})\">";
			document.getElementById("continer" + index).innerHTML = htmlInfo;
			break;
		}
	}
	var selectNameArray = selectTypes.split(",");
	for ( var k = 0; k < selectNameArray.length; k++)
	{
		if (!selectNameArray[k])
			continue;
		if (selectName && (selectName == selectNameArray[k] || selectName.indexOf(selectNameArray[k]) != -1))
		{
			dflag = 1;
			var htmlInfo = "<select class=\"searchSelect\" style=\"height:26px;\" name=\"columnValue" + index + "\" id=\"columnValue" + index + "\">";
			htmlInfo += "<option></option>";
			var selectValues =jsonData.selectValues;
			if(!selectValues)
				continue;
			for(var m = 0; m < selectValues.length; m++)
			{
				if(selectValues[m].name && selectValues[m].name == selectName)
				{
					var value = selectValues[m].value;
					if(!value)
						continue;
					for(var n = 0; n < value.length; n++)
					{
						htmlInfo += "<option value=\"" + value[n].value + "\">" + value[n].name + "</option>";
					}
				}
			}
			htmlInfo += "</select>";
			document.getElementById("continer" + index).innerHTML = htmlInfo;
			break;
		}
	}

	if (dflag == 0 && sflag == 0)
	{
		if (selectName != "input")
		{
			var htmlInfo = "<input type=\"text\" name=\"columnValue";
			htmlInfo += index + "\" id=\"columnValue" + index + "\" size=\"7\" style=\"height:18px;\" class=\"searchInput\">";
			document.getElementById("continer" + index).innerHTML = htmlInfo;
		}
	}
}
function getSearchParams()
{
	var row = searchTB.rowCount ? parseInt(searchTB.rowCount) : 0;
	var params = "";
	var index = 0;
	var expgrp1 = "", expgrp2 = "";
	for ( var t = 0; t < 2; t++)
	{
		var temp = "";
		for ( var i = 0; i < row; i++)
		{
			var colName = document.getElementById("column" + index).value;
			var exprValue = document.getElementById("condition" + index).value;
			var colValue = document.getElementById("columnValue" + index).value;
			if(colValue != null){
				colValue = colValue.replaceAll("%","\\\\%").replaceAll("_", "\\\\_");
			}
			if (colName && exprValue && colValue)
			{
				if (!temp)
				{
					if (exprValue.toLowerCase() == "like" || exprValue.toLowerCase() == "not like")
						colValue = "\\%" + colValue + "\\%";
					temp = colName + " " + exprValue + " \\'" + colValue + "\\'";
				}
				else
				{
					var orandValue = document.getElementById("andor" + index).value;
					if (exprValue.toLowerCase() == "like" || exprValue.toLowerCase() == "not like")
						colValue = "\\%" + colValue + "\\%";
					temp += " " + orandValue + " " + colName + " " + exprValue + " \\'" + colValue + "\\'";
				}
			}
			index++;
		}
		if (t == 0)
			expgrp1 = temp;
		else
			expgrp2 = temp;
	}
	if (expgrp1 && expgrp2)
	{
		var orand = "and";
		var tagName = document.getElementsByTagName("input");
		for ( var j = 0; j < tagName.length; j++)
		{
			if (tagName[j].type == "radio" && tagName[j].checked)
			{
				orand = tagName[j].value;
				break;
			}
		}
		params = "( " + expgrp1 + " ) " + orand + " ( " + expgrp2 + " )";
	}
	else if (!expgrp1 && expgrp2)
	{
		params = "(" + expgrp2 + ")";
	}
	else if (expgrp1 && !expgrp2)
	{
		params = "(" + expgrp1 + ")";
	}
	else
	{
		params = "";
	}
	return params;
}
function getSelectValues()
{
	var row = searchTB.rowCount ? parseInt(searchTB.rowCount) : 0;
	var selectValueJSON = "[";
	var orand = "and"; 
	var tagName = document.getElementsByTagName("input");
	for ( var j = 0; j < tagName.length; j++)
	{
		if (tagName[j].type == "radio" && tagName[j].checked)
		{
			orand = tagName[j].value;
			break;
		}
	}
	selectValueJSON += "{\\\"id\\\":\\\"orand\\\",\\\"value\\\":\\\"" + orand + "\\\",\\\"type\\\":\\\"radio\\\"}";
	var index = 0;
	for ( var t = 0; t < 2; t++)
	{
		for ( var i = 0; i < row; i++)
		{
			if(i != 0)
			{
				orandValue = document.getElementById("andor" + index).value;
				selectValueJSON += ",{\\\"id\\\":\\\"andor" + index + "\\\",\\\"value\\\":\\\"" + orandValue + "\\\",\\\"type\\\":\\\"\\\"}";
				
			}
			var colName = document.getElementById("column" + index).value;
			var exprValue = document.getElementById("condition" + index).value;
			var colValue = document.getElementById("columnValue" + index).value;
			if(colName && exprValue && colValue)
			{
				selectValueJSON += ",{\\\"id\\\":\\\"column" + index + "\\\",\\\"value\\\":\\\"" + colName + "\\\",\\\"type\\\":\\\"\\\"}";
				selectValueJSON += ",{\\\"id\\\":\\\"condition" + index + "\\\",\\\"value\\\":\\\"" + exprValue + "\\\",\\\"type\\\":\\\"\\\"}";
				selectValueJSON += ",{\\\"id\\\":\\\"columnValue" + index + "\\\",\\\"value\\\":\\\"" + colValue + "\\\",\\\"type\\\":\\\"\\\"}";
			}
			index++;
		}
	}
	selectValueJSON += "]";
	return selectValueJSON;
}
String.prototype.replaceAll  = function(s1,s2){   
	return this.replace(new RegExp(s1,"gm"),s2);   
}  
