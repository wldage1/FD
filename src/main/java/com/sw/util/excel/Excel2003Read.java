package com.sw.util.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sw.util.ExcelUtil;

public class Excel2003Read extends Excel2003ReadAbstract {

	private List<Map<String, String>> excelList ;

	//导入的第一行是否是列名 true-第一行列名
	private boolean isRowColName = true;
	Map<String, String> columnMap = new HashMap<String, String>();
	
	public Excel2003Read(String filename, boolean isRowColName) throws IOException, FileNotFoundException {
		super(filename);
		excelList = new ArrayList<Map<String, String>>();
	}

	@Override
	public void optRows(int sheetIndex, int curRow, List<String> rowlist) {
		Map<String, String> map = new HashMap<String, String>() ;
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
