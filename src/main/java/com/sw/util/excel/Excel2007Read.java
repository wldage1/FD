package com.sw.util.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sw.util.ExcelUtil;

public class Excel2007Read extends Excel2007ReadAbstract {
	private List<Map<String, String>> excelList ;

	//导入的第一行是否是列名 true-第一行列名
	private boolean isRowColName = true;
	//列集合
	Map<String, String> columnMap = new HashMap<String, String>();
	
	public Excel2007Read(boolean isRowColName) {
		this.isRowColName = isRowColName ;
		excelList = new ArrayList<Map<String,String>>() ;
	}
	
	@Override
	public void optRows(int sheetIndex,int curRow, List<String> rowlist) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>() ;
		for (int i = 0; i < rowlist.size(); i++) {
			if(!isRowColName || i > 0){
				map.put(columnMap.get(ExcelUtil.customColENName + (i + 1)), rowlist.get(i));
			}
		}
		excelList.add(map) ;
	}
	
	public List<Map<String, String>> getExcelList() {
		return excelList;
	}

	public void setExcelList(List<Map<String, String>> excelList) {
		this.excelList = excelList;
	}
	
}
