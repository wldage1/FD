package com.sw.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExportExcel {
	/**
	 * 生成 Excel 文件
	 * 
	 * @param excelName
	 *            导出excel sheet 显示名
	 * @param colNames
	 *            显示中文名
	 * @param colNameNum
	 *            中文名 数组长度
	 * @param colModels
	 *            查询字段名
	 * @param colModelNum
	 *            查询字段名 数组长度
	 * @param objList
	 *            查询的数据集合
	 * @param excel
	 *            excel对象文件
	 * @return
	 * @throws Exception
	 * @author jay
	 */
	public WritableWorkbook createExcel(String excelName, String[] colNames, int colNameNum, String[] colModels, int colModelNum,
			List<Map<String, Object>> objList, WritableWorkbook excel) throws Exception {

		// 左下角的名称 sheet
		WritableSheet sheet = excel.createSheet(excelName, 0);
		// 设置字体样式
		WritableFont boldFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
		WritableCellFormat excelTitle = new WritableCellFormat(boldFont);
		WritableCellFormat contentColumn = new WritableCellFormat();
		try {
			excelTitle.setVerticalAlignment(VerticalAlignment.CENTRE);
			excelTitle.setAlignment(Alignment.CENTRE);
			excelTitle.setBorder(Border.ALL, BorderLineStyle.THIN);
			excelTitle.setWrap(true);
			contentColumn.setBorder(Border.ALL, BorderLineStyle.THIN);
			contentColumn.setWrap(true);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		// 列、行
		List<Label> listLabel = new ArrayList<Label>();
		// 生成第一行信息
		listLabel.add(new Label(0, 0, excelName, excelTitle));
		// 循环显示中文字段名
		for (int i = 0; i < colNameNum; i++) {
			String fieldName = colNames[i];
			listLabel.add(new Label(i, 1, fieldName, contentColumn));
		}
		// 遍历开始的序号
		int startColumn = 2;
		// 循环取值
		for (Map<String, Object> obj : objList) {
			for (int i = 0; i < colModelNum; i++) {
				String fileCode = colModels[i];
				listLabel.add(new Label(i, startColumn, obj.get(fileCode) == null ? "" : obj.get(fileCode).toString(), contentColumn));
			}
			startColumn++;
		}
		try {
			sheet = setMySheet(sheet, colNameNum - 1);
			for (Label label : listLabel) {
				sheet.addCell((Label) label);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return excel;
	}

	/**
	 * 生成 Excel 文件
	 * 
	 * @param excelName
	 *            导出excel sheet 显示名
	 * @param colNames
	 *            显示中文名
	 * @param colNameNum
	 *            中文名 数组长度
	 * @param colModels
	 *            查询字段名
	 * @param colModelNum
	 *            查询字段名 数组长度
	 * @param objList
	 *            查询的数据集合
	 * @param excel
	 *            excel对象文件
	 * @return
	 * @throws Exception
	 * @author jay
	 */
	public WritableWorkbook createExcel(String excelName, String[] colNames, int colNameNum, String[] colModels, int colModelNum,
			List<Map<String, Object>> objList, WritableWorkbook excel, Map<String, Map<String, Object>> paramMap) throws Exception {

		// 左下角的名称 sheet
		WritableSheet sheet = excel.createSheet(excelName, 0);
		// 设置字体样式
		WritableFont boldFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
		WritableCellFormat excelTitle = new WritableCellFormat(boldFont);
		WritableCellFormat contentColumn = new WritableCellFormat();
		try {
			excelTitle.setVerticalAlignment(VerticalAlignment.CENTRE);
			excelTitle.setAlignment(Alignment.CENTRE);
			excelTitle.setBorder(Border.ALL, BorderLineStyle.THIN);
			excelTitle.setWrap(true);
			contentColumn.setBorder(Border.ALL, BorderLineStyle.THIN);
			contentColumn.setWrap(true);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		// 列、行
		List<Label> listLabel = new ArrayList<Label>();
		// 生成第一行信息
		listLabel.add(new Label(0, 0, excelName, excelTitle));
		// 循环显示中文字段名
		for (int i = 0; i < colNameNum; i++) {
			String fieldName = colNames[i];
			listLabel.add(new Label(i, 1, fieldName, contentColumn));
		}
		// 遍历开始的序号
		int startColumn = 2;
		// 循环取值
		for (Map<String, Object> obj : objList) {
			for (int i = 0; i < colModelNum; i++) {
				String fileCode = colModels[i];
				if(paramMap.get(fileCode) == null) {
					listLabel.add(new Label(i, startColumn, obj.get(fileCode) == null ? "" : obj.get(fileCode).toString(), contentColumn));
				}else {
					Object val = obj.get(fileCode) == null ? "" : paramMap.get(fileCode).get(obj.get(fileCode).toString()) ;
					
					listLabel.add(new Label(i, startColumn, val == null ? "" : val.toString(), contentColumn));
				}
			}
			startColumn++;
		}
		try {
			sheet = setMySheet(sheet, colNameNum - 1);
			for (Label label : listLabel) {
				sheet.addCell((Label) label);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return excel;
	}
	
	/**
	 * 合并、设置高度等
	 * 
	 * @param sheet
	 * @param num
	 * @return
	 */
	private WritableSheet setMySheet(WritableSheet sheet, int num) {

		try {
			sheet.mergeCells(0, 0, num, 0);// 合并 第一行
			sheet.setRowView(0, 500); // 设置行高 第一行
			for (int i = 0; i < num; i++) {
				sheet.setColumnView(i, 20); // 设置字段行宽
				sheet.setRowView(i, 350); // 设置行高
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sheet;
	}
}
