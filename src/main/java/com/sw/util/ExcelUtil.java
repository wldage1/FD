package com.sw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.incesoft.tools.excel.ExcelRowIterator;
import com.incesoft.tools.excel.ReaderSupport;
import com.sw.util.excel.ExcelReadHandle;

public class ExcelUtil {
	
	//自定义列中文名称
	public static String customColCNName = "列" ;
	//自定义列英文名称
	public static String customColENName = "col" ;
	
	/**
	 * 读取超大Excel文件
	 * @param filePath
	 * @param columnNames 对应列名
	 * @param isFirstColName 首行是否是列名
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> read(String filePath, boolean isFirstColName) throws Exception{
		return ExcelReadHandle.read(filePath,  isFirstColName) ;
	}
	
	/**
	 * 读取超大Excel文件
	 * @param filePath
	 * @param isFirstColName
	 * @param readNum 读取行数,如果小于0时无限制
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> readExcel(String filePath, boolean isFirstColName, int readNum) throws Exception{
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
					if(readNum > 0 && list.size() == readNum){
						break;
					}
				}
			}finally{
				readerSupport.close() ;
			}
		}
		return list ;
	}
	
	/**
	 * 读取Excel文件，返回文件中列名
	 * @param filePath
	 * @param isFirstColName 首行是否是列名
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> read(String filePath, boolean isFirstColName, int readNum) throws Exception{
		if (filePath.endsWith(".xls")) {
			return read2003(filePath, isFirstColName, readNum) ;
		} else if(filePath.endsWith(".xlsx")) {
			return read2007(filePath, isFirstColName, readNum) ;
		}else {
			return null ;
		}
	}
	
	/**
	 * 读取Excel2003文件
	 * jxl方式读取文件
	 * @param filePath
	 * @param isFirstColName 首行是否是列名
	 * @param readNum 读取行数，为0时表示读取全部
	 * @return
	 */
	public static List<Map<String, String>> read2003(String filePath, boolean isFirstColName, int readNum) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>() ;
		InputStream is = null;
		try{
			is = new FileInputStream(filePath);
			jxl.Workbook rwb = Workbook.getWorkbook(is);
			int sheetNum = rwb.getNumberOfSheets();
			
			//循环excel页签
			for (int i = 0; i < sheetNum; i++) {
				jxl.Sheet sheet = rwb.getSheet(i);
				//每页行数
				int num = sheet.getRows();
				//读取数据，如果首行是列名，则从第2行开始
				int beginNum = isFirstColName ? 1 : 0 ;
				
				//循环行
				for (int j = beginNum; j < num; j++) {
					jxl.Cell[] cell = sheet.getRow(j);
					HashMap<String, String> hm = new HashMap<String, String>();
					for (int k = 0; k < cell.length; k++) {
						//得到单元格的值
						if(cell[k].getContents() != null && !cell[k].getContents().equals("")){
							String mapValue = cell[k].getContents().trim();
							hm.put(ExcelUtil.customColENName + (k + 1), mapValue);
						}
					}
					//HashMap对象数组
					if(!hm.isEmpty()) {
						list.add(hm);
					}
					
					//当记录数等于读取行数时，跳出循环
					if(list.size() == readNum){
						break;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace() ;
		}finally {
			try{
				if(is != null)
					is.close() ;
			}catch(Exception e){}
		}
		return list ;
	}
	
	/**
	 * 读取Excel2007文件
	 * poi方式读取文件
	 * @param filePath
	 * @param isFirstColName
	 * @param readNum 读取行数，为0时表示读取全部
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> read2007(String filePath, boolean isFirstColName, int readNum) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>() ;
		InputStream is = null;
		try{
			is = new FileInputStream(filePath);
			org.apache.poi.ss.usermodel.Workbook wb = WorkbookFactory.create(is);
			int  sheetNum = wb.getNumberOfSheets();
			
			for (int i = 0; i < sheetNum; i++) {
				//读取sheet内容
				Sheet sheet = wb.getSheetAt(i);
				int rowNum = sheet.getLastRowNum();
				//读取数据，如果首行是列名，则从第2行开始
				int beginNum = isFirstColName ? 1 : 0 ;
				
				for (int j = beginNum; j <= rowNum; j++) {
					//读取行内容
					org.apache.poi.ss.usermodel.Row row = sheet.getRow(j);
					int  cellNum = sheet.getRow(j).getLastCellNum();
					HashMap<String, String> hm = new HashMap<String, String>();
					for (int k = 0; k < cellNum; k++) {
						//读取列内容
						org.apache.poi.ss.usermodel.Cell cell = row.getCell(k);
						String cellValue="";
						if(cell != null) {
							int cellType = cell.getCellType();
							if(cellType == Cell.CELL_TYPE_STRING){
								cellValue = cell.getStringCellValue().trim();
							} else if(cellType == Cell.CELL_TYPE_NUMERIC){
								cellValue = String.valueOf((long)cell.getNumericCellValue());
							} else if(cellType == Cell.CELL_TYPE_FORMULA){
								cellValue = cell.getCellFormula();
							}
						}
						hm.put(ExcelUtil.customColENName + (k + 1), cellValue);
					}
					list.add(hm);
					
					//当记录数等于读取行数时，跳出循环
					if(list.size() == readNum){
						break;
					}
				}
			}
		} finally {
			try{
				is.close() ;
			}catch(Exception e){}
		}
		return list ;
	}
	
	/**
	 * 读取Excel文件，返回文件中列名
	 * @param filePath
	 * @param isFirstColName 首行是否是列名
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> readColName(String filePath, boolean isFirstColName) throws Exception{
		if (filePath.endsWith(".xls")) {
			return read2003ColName(filePath, isFirstColName) ;
		} else if(filePath.endsWith(".xlsx")) {
			return read2007ColName(filePath, isFirstColName) ;
		}else {
			return null ;
		}
	}
	
	/**
	 * 读取Excel2003文件，返回文件中列名
	 * jxl方式读取文件
	 * @param filePath
	 * @param isFirstColName 首行是否是列名
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> read2003ColName(String filePath, boolean isFirstColName) throws Exception{
		Map<String, String> map = new HashMap<String, String>() ;
		
		InputStream is = null;
		try{
			is = new FileInputStream(filePath);
			jxl.Workbook rwb = Workbook.getWorkbook(is);
			int sheetNum = rwb.getNumberOfSheets();
			
			//循环excel页签,将参数中的字段名称添加到字段集合中
			for (int i = 0; i < sheetNum; i++) {
				jxl.Sheet sheet = rwb.getSheet(i);
				//只读取第一行内容
				jxl.Cell[] cell = sheet.getRow(0);
				//列数
				int  cellNum = cell.length;
				if(isFirstColName){	//第一行为返回字段名称，将第一行数据导入到列名集合中
					for (int k = 0; k < cellNum; k++) {
						if(cell[k].getContents() != null && !cell[k].getContents().equals("")){
							String mapValue = cell[k].getContents().trim();
							map.put(ExcelUtil.customColENName + (k + 1), mapValue) ;
						}
						
					}
				}else {	//如果首行为数据，则根据数据列数，将相应中文描述导入到列名集合中
					for (int k = 0; k < cellNum; k++) {
						map.put(ExcelUtil.customColENName + (k + 1), ExcelUtil.customColCNName + (k + 1)) ;
					}
				}
			}
		}finally {
			try{
				if(is != null)
					is.close() ;
			}catch(Exception e){}
		}
		
		return map;
	}
	
	/**
	 * 读取Excel2003文件，返回文件中列名
	 * jxl方式读取文件
	 * @param filePath
	 * @param isFirstColName 首行是否是列名
	 * @return
	 * @throws Exception
	 */
	public static List<String> read2003ColNameToList(String filePath, boolean isFirstColName) throws Exception{
		List<String> list = new ArrayList<String>() ;
		
		InputStream is = null;
		try{
			is = new FileInputStream(filePath);
			jxl.Workbook rwb = Workbook.getWorkbook(is);
			int sheetNum = rwb.getNumberOfSheets();
			
			//循环excel页签,将参数中的字段名称添加到字段集合中
			for (int i = 0; i < sheetNum; i++) {
				jxl.Sheet sheet = rwb.getSheet(i);
				//只读取第一行内容
				jxl.Cell[] cell = sheet.getRow(0);
				//列数
				int  cellNum = cell.length;
				if(isFirstColName){	//第一行为返回字段名称，将第一行数据导入到列名集合中
					for (int k = 0; k < cellNum; k++) {
						if(cell[k].getContents() != null && !cell[k].getContents().equals("")){
							String mapValue = cell[k].getContents().trim();
							list.add(mapValue) ;
						}
						
					}
				}else {	//如果首行为数据，则根据数据列数，将相应中文描述导入到列名集合中
					for (int k = 0; k < cellNum; k++) {
						list.add(ExcelUtil.customColCNName + (k + 1)) ;
					}
				}
			}
		}finally {
			try{
				if(is != null)
					is.close() ;
			}catch(Exception e){}
		}
		
		return list;
	}
	
	/**
	 * 读取Excel2007文件，返回文件中列名
	 * poi方式读取文件
	 * @param filePath
	 * @param isFirstColName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> read2007ColName(String filePath, boolean isFirstColName) throws Exception {
		Map<String, String> map = new HashMap<String, String>() ;
		InputStream is = null;
		try{
			is = new FileInputStream(filePath);
			org.apache.poi.ss.usermodel.Workbook wb = WorkbookFactory.create(is);
			int  sheetNum = wb.getNumberOfSheets();
			
			for (int i = 0; i < sheetNum; i++) {
				Sheet sheet = wb.getSheetAt(i);
				
				//读取第一行数据到row
				org.apache.poi.ss.usermodel.Row row = sheet.getRow(0);
				if(row!=null){
					//第一行列数
					int  cellNum = sheet.getRow(0).getLastCellNum();
					
					if(isFirstColName){	//第一行为返回字段名称，将第一行数据导入到列名集合中
						for (int k = 0; k < cellNum; k++) {
							org.apache.poi.ss.usermodel.Cell cell = row.getCell(k);
							String cellValue="";
							int cellType = cell.getCellType();
							if(cellType==Cell.CELL_TYPE_STRING){
								cellValue = cell.getStringCellValue().trim();
							}if(cellType==Cell.CELL_TYPE_NUMERIC){
								cellValue = String.valueOf((long)cell.getNumericCellValue());
							}if(cellType==Cell.CELL_TYPE_FORMULA){
								cellValue = cell.getCellFormula();
							}
							map.put(ExcelUtil.customColENName + (k + 1), cellValue) ;
						}
					}else {	//将参数中的字段名称添加到字段集合中
						for (int k = 0; k < cellNum; k++) {
							map.put(ExcelUtil.customColENName + (k + 1), ExcelUtil.customColCNName + (k + 1)) ;
						}
					}
				}
			}
		} finally {
			try{
				is.close() ;
			}catch(Exception e){}
		}
		return map;
	}
	
	/**
	 * 读取Excel2007文件，返回文件中列名
	 * poi方式读取文件
	 * @param filePath
	 * @param isFirstColName
	 * @return
	 * @throws Exception
	 */
	public static List<String> read2007ColNameToList(String filePath, boolean isFirstColName) throws Exception {
		List<String> list = new ArrayList<String>() ;
		InputStream is = null;
		try{
			is = new FileInputStream(filePath);
			org.apache.poi.ss.usermodel.Workbook wb = WorkbookFactory.create(is);
			int  sheetNum = wb.getNumberOfSheets();
			
			for (int i = 0; i < sheetNum; i++) {
				Sheet sheet = wb.getSheetAt(i);
				
				//读取第一行数据到row
				org.apache.poi.ss.usermodel.Row row = sheet.getRow(0);
				if(row!=null){
					//第一行列数
					int  cellNum = row.getLastCellNum();
					if(isFirstColName){	//第一行为返回字段名称，将第一行数据导入到列名集合中
						for (int k = 0; k < cellNum; k++) {
							org.apache.poi.ss.usermodel.Cell cell = row.getCell(k);
							String cellValue="";
							int cellType = cell.getCellType();
							if(cellType==Cell.CELL_TYPE_STRING){
								cellValue = cell.getStringCellValue().trim();
							}if(cellType==Cell.CELL_TYPE_NUMERIC){
								cellValue = String.valueOf((long)cell.getNumericCellValue());
							}if(cellType==Cell.CELL_TYPE_FORMULA){
								cellValue = cell.getCellFormula();
							}
							list.add(cellValue) ;
						}
					}else {	//将参数中的字段名称添加到字段集合中
						for (int k = 0; k < cellNum; k++) {
							list.add(ExcelUtil.customColCNName + (k + 1)) ;
						}
					}
				}
			}
		}finally {
			try{
				is.close() ;
			}catch(Exception e){}
		}
		return list;
	}
	
	/**
	 * 读取Excel文件，返回文件中列名
	 * @param filePath
	 * @param isFirstColName
	 * @return
	 * @throws Exception
	 */
	public static List<String> readColNameToList(String filePath, boolean isFirstColName) throws Exception {
		List<String> list = new ArrayList<String>() ;
		ReaderSupport readerSupport = null ;
		if (filePath.endsWith(".xls")) {
			readerSupport =  ReaderSupport.newInstance(ReaderSupport.TYPE_XLS, new File(filePath));
		} else if(filePath.endsWith(".xlsx")) {
			readerSupport =  ReaderSupport.newInstance(ReaderSupport.TYPE_XLSX, new File(filePath));
		}
		
		if(readerSupport != null) {
			try{
				readerSupport.open();
				int beginNum = isFirstColName ? 1 : 0 ;
				ExcelRowIterator it = readerSupport.rowIterator();
				while (it.nextRow()) {
					if(isFirstColName){	//第一行为返回字段名称，将第一行数据导入到列名集合中
						for(int i = beginNum; i <= it.getCellCount(); i ++) {
							list.add(it.getCellValue(i-1)) ;
						}
					}else {
						for(int i = beginNum; i < it.getCellCount(); i ++) {
							list.add(ExcelUtil.customColCNName + (i+1)) ;
						}
					}
					break ;
				}
			}finally{
				readerSupport.close() ;
			}
		}
		return list ;
	}
}
