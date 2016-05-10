package com.sw.util.thread;

import java.io.File;

import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.sw.core.cache.FileInfoCache;
import com.sw.core.common.Constant;
import com.sw.core.data.dao.IDao;
import com.sw.core.data.entity.FileInfo;
import com.sw.core.data.entity.RelationEntity;
import com.sw.plugins.product.manage.entity.ProductAttachment;
import com.sw.util.FTPUtil;
import com.sw.util.FileConvert;
import com.sw.util.FileUtil;
import com.sw.util.WatermarkUtil;

public class ScheduelTask implements Runnable {

	private static Logger logger = Logger.getLogger(ScheduelTask.class);
	
	private IDao<RelationEntity> relationDao;


	public ScheduelTask() {
	}

	public ScheduelTask(IDao<RelationEntity> relationDao) {
		this.relationDao = relationDao;
	}

	@Override
	public void run() {
		try {
			logger.info("FILE-CONVERT-THREAD:"+Thread.currentThread().getName());
			//业务处理
			Element element = FileInfoCache.getCache();
			Object obj = element.getValue();
			if(obj instanceof FileInfo){
				FileInfo fileInfo = (FileInfo)obj;
				fileConvert(fileInfo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param filePath
	 *            文件实际路径
	 * @param realPath
	 *            工程实际路径
	 * @return rtnMap error: 0 正常 1 该文件无法转化 2 没有该文件 3 pdf转化异常 4 swf转化异常
	 * @throws Exception
	 */
	public void fileConvert(FileInfo fileInfo) throws Exception {
	
		String fileName = fileInfo.getName();
		String ftpFilePath = fileInfo.getFtpFilePath();
		String convertFilePath = fileInfo.getConvertFilePath();
		String localPath = fileInfo.getLocalPath();
		String watermark = fileInfo.getOrgWatermark();
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		// 如果文件为pdf文件，直接转成swf文件。否则先转成pdf文件再转成swf文件
		String name = fileName.substring(0, fileName.lastIndexOf("."));
		String convertPDFName = name + ".pdf";
		if(!(fileType.toLowerCase().equals(".pdf"))){
			FileConvert.docToPdf(
					new File(convertFilePath + File.separator + fileName), 
					new File(convertFilePath + File.separator + convertPDFName),
					Constant.PDFJARPATH_KEY, Constant.TRANSFORTYPEFLAG_KEY);
			if(watermark != null){
				String waterPDFName = name + "water.pdf";
				WatermarkUtil.watermark(localPath,convertFilePath + File.separator + convertPDFName,convertFilePath + File.separator + waterPDFName,watermark);
				convertPDFName = waterPDFName;
			}
		}
		String convertSWFName = name + ".swf";
		FileConvert.PdfToSwf(
				new File(convertFilePath + File.separator + convertPDFName),
				new File(convertFilePath + File.separator + convertSWFName),
				Constant.SWFTOOLSPATH_KEY);
//		Date date = new Date(System.currentTimeMillis());
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//	    String dateStr = sdf.format(date);
//		String path = SystemProperty.getInstance("config").getProperty("product_document_path")+dateStr;
		File localFile = new File(convertFilePath + "/" + convertSWFName);
		FTPUtil.uploadFile(localFile, ftpFilePath, convertSWFName);
		//删除临时转换文件
		FileUtil.delAllFile(convertFilePath);
		//更新产品附件为已转化状态
		ProductAttachment productAttachment = new ProductAttachment();
		productAttachment.setFileUrl(ftpFilePath + "/" + fileName);
		productAttachment.setIsConvert((short)1);
		relationDao.update("productAttachment.update_isConvert", productAttachment);
	}
	
}
