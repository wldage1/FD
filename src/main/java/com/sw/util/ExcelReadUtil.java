package com.sw.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.incesoft.tools.excel.ExcelRowIterator;
import com.incesoft.tools.excel.ReaderSupport;

public class ExcelReadUtil {
	public static String customColENName = "col" ;
	public static void main(String[] args) {
		try {
			//List<Map<String,String>> list=ExcelReadUtil.readExcel("E:\\book1.xlsx",true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<Map<String, String>> readExcel(String filePath,Boolean isFirstColName) throws Exception{
		List<Map<String, String>> list = new ArrayList<Map<String,String>>() ;
		ReaderSupport readerSupport = null ;
		if (filePath.endsWith(".xls")) {
			readerSupport =  ReaderSupport.newInstance(ReaderSupport.TYPE_XLS, new File(filePath));
		} else if(filePath.endsWith(".xlsx")) {
			readerSupport =  ReaderSupport.newInstance(ReaderSupport.TYPE_XLSX, new File(filePath));
		}
		
		if(readerSupport != null) {
			try{
				readerSupport.open() ;
				ExcelRowIterator it = readerSupport.rowIterator();
				int index = 0 ;
				while (it.nextRow()) {
					index ++ ;
					//如果首行为字段名称,则不读取
					if(index == 1 && isFirstColName) {
						continue ;
					}
					Map<String,String> map = new HashMap<String, String>() ;
					for(int i = 0; i < it.getCellCount(); i ++) {
						map.put(customColENName + (i + 1), it.getCellValue(i)) ;
					}
					list.add(map) ;
				}
			}finally{
				readerSupport.close() ;
			}
		}
		return list ;
	}
}
