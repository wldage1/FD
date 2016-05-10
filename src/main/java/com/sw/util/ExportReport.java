package com.sw.util;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.log4j.Logger;

import com.sw.core.data.dao.RelationaldbCommonDao;



/**
 * @author liushiliang
 * 利用jarsper生成Excel报表
 * @time 2013-3-22
 * */
public class ExportReport{
	private static Logger logger = Logger.getLogger(ExportReport.class);
	
	 private RelationaldbCommonDao relationDao;
	
	public ExportReport(RelationaldbCommonDao relationDao) {
		this.relationDao = relationDao;
	}
	private Connection getCon()throws Exception{
		
		return relationDao.getSqlSessionTemplate().getDataSource().getConnection();
	}


	/**
	 * @param 模版文件路径
	 * 输入文件路径例子："E:/ireport300/jasper_template.jasper"
	 * 输出文件路径例子：H:\\report.csv
	 * @return 反回成功或者失败
	 * 
	 * */
	public String CSVExport(HttpServletRequest request,HttpServletResponse response,String filePath, String outFilePath) {
		JasperPrint jasperPrint = null;
		try {
			// 导出CSV
			File reportFile = new File(filePath);
			Map<String, Object> parameters = new HashMap<String, Object>();
			jasperPrint = JasperFillManager.fillReport(reportFile.getPath(),
					parameters, this.getCon());
			
			JRCsvExporter exporter = new JRCsvExporter();
			response.setContentType("application/x-msdownload");
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFilePath);
			exporter.setParameter(
					JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.TRUE); // 删除记录最下面的空行
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.FALSE);// 删除多余的ColumnHeader
			exporter.setParameter(
					JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.FALSE);// 显示边框
			exporter.exportReport();
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			return "failed";
		}
		return "success";
	}
	
	
	/**
	 * @param 模版文件路径
	 *  输入文件路径例子："E:/ireport300/jasper_template.jasper"
	 *  输出文件路径例子：H:\\report.csv
	 *  parameters 为模板中SQL所需参数
	 * @return 反回成功或者失败
	 * @author liushiliang
	 * */
	public String CSVExport(HttpServletRequest request,HttpServletResponse response,String filePath, String outFilePath,Map<String, Object> parameters) {
		JasperPrint jasperPrint = null;
		try {
			// 导出CSV
			File reportFile = new File(filePath);
			File outFile = new File(outFilePath);
			if(outFile.exists()){
				outFile.delete();
			}
			jasperPrint = JasperFillManager.fillReport(reportFile.getPath(),
					parameters, this.getCon());
			JRExporter exporter = null;
			if (outFilePath.toLowerCase().endsWith(".xls")) {
				exporter = new JRXlsExporter();
			}else if (outFilePath.toLowerCase().endsWith(".csv")) {
				exporter = new JRCsvExporter();
			}else if (outFilePath.toLowerCase().endsWith(".pdf")) {
				exporter = new JRPdfExporter();
			}
			else {
				exporter = new JRCsvExporter();
			}
			response.setContentType("application/x-msdownload");
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFilePath);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE); // 删除记录最下面的空行
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);// 删除多余的ColumnHeader
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);// 显示边框
			exporter.exportReport();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();

			return "failed";
		}
		return "success";
	}
	 
	
	
}
