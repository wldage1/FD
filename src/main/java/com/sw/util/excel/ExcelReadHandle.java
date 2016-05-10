package com.sw.util.excel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ExcelReadHandle {

	/**
	 * 读取excel文件所有内容
	 * @param filePath 文件完整路径
	 * @param columnNames
	 * @param isRowColName
	 * @return
	 */
	public static List<Map<String, String>> read(String filePath, boolean isRowColName) throws Exception  {
		if (filePath.endsWith(".xls")) {
			Excel2003Read read = new Excel2003Read(filePath, isRowColName);
			read.process() ;
			return read.getExcelList() ;
		} else {
			Excel2007Read read = new Excel2007Read(isRowColName);
			read.process(filePath);
			return read.getExcelList();
		}
	}
	
	public static void main(String[] args) {
		try {
			Excel2007Read read = new Excel2007Read(true) ;
			try {
				read.process("d:/temp/ccbm/test.xlsx") ;
				List<Map<String, String>> list = read.getExcelList()	 ;
				
				int index = 0 ;
				for(Map<String, String> map : list) {
					Iterator<Entry<String, String>> iter = map.entrySet().iterator();
					System.out.print(++index + ": ");
					while (iter.hasNext()) {
						Entry<String, String> entry = iter.next();
						Object key = entry.getKey();
						Object val = entry.getValue();
						System.out.print(val + "," + key);
					}
					System.out.println();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
