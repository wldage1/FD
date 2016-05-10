package com.sw.plugins.product.manage.service;

import java.io.File;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sw.core.cache.FileInfoCache;
import com.sw.core.data.entity.FileInfo;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.manage.entity.ProductAttachment;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;
import com.sw.util.WatermarkUtil;
import com.sw.util.thread.FileConvertService;

public class ProductAttachmentService extends CommonService<ProductAttachment> {

	private static Logger logger = Logger.getLogger(ProductAttachmentService.class);

	@Resource
	private FileConvertService fileConvertService;

	@Override
	public void delete(ProductAttachment entity) throws Exception {

		getRelationDao().delete("productAttachment.delete", entity);

	}

	public void deleteByProductId(ProductAttachment entity) throws Exception {

		getRelationDao().delete("productAttachment.delete_byProductId", entity);

	}

	@Override
	public void deleteByArr(ProductAttachment entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}

	@Override
	public Object download(ProductAttachment entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public Map<String, Object> getGrid(ProductAttachment entity) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttachment> getList(ProductAttachment entity) throws Exception {
		return (List<ProductAttachment>) getRelationDao().selectList("productAttachment.select", entity);
	}

	@Override
	public ProductAttachment getOne(ProductAttachment entity) throws Exception {
		return (ProductAttachment) getRelationDao().selectOne("productAttachment.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttachment> getPaginatedList(ProductAttachment entity) throws Exception {
		return (List<ProductAttachment>) getRelationDao().selectList("productAttachment.select_paginated", entity);
	}

	@Override
	public Long getRecordCount(ProductAttachment entity) throws Exception {
		return (long) getRelationDao().getCount("productAttachment.count", entity);
	}

	@Override
	public void save(ProductAttachment entity) throws Exception {
		getRelationDao().insert("productAttachment.insert", entity);
		entity.setId(entity.getGeneratedKey());
	}

	@Override
	public void update(ProductAttachment entity) throws Exception {
		getRelationDao().update("productAttachment.update", entity);
	}

	public void updateAttachment(ProductAttachment entity) throws Exception {
		getRelationDao().update("productAttachment.updateAttachment", entity);
	}

	@Override
	public String upload(ProductAttachment entity, HttpServletRequest request) throws Exception {
		String realPath = request.getSession().getServletContext().getRealPath("");
		if (realPath == null) {
			try {
				realPath = request.getSession().getServletContext().getResource("/").toString();
			} catch (MalformedURLException e) {
				logger.error(e.getMessage());
			}
		}

		MultipartFile file = ((MultipartHttpServletRequest) request).getFile("Filedata");
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(date);
		// 上传到FTP文件的路径
		String path = SystemProperty.getInstance("config").getProperty("product_document_path") + dateStr;
		String tempFileName = request.getParameter("Filename");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));

		String newFileName = FTPUtil.getRandomString();
		String realFileName = newFileName + fileExtensionName;
		// 本地待转换文件路径
		String filePath = realPath + SystemProperty.getInstance("config").getProperty("file_convert_path");
		String separator = (File.separator).equals("/") ? "\\" : "/";
		filePath = filePath.replace(separator, File.separator);
		File nfile = new File(filePath, realFileName);
		byte[] bytes;
		try {
			File myFilePath = new File(filePath);
			if (myFilePath != null) {
				myFilePath.mkdirs();
			}
			bytes = file.getBytes();
			if (bytes.length != 0) {
				file.transferTo(nfile);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		String pdfPath = filePath + "/" + realFileName;
		String waterPdfPath = filePath + "/water" + realFileName;
		String watermark = getOrgWatermark(entity);
		if(fileExtensionName.equals(".pdf") && watermark!=null){
			WatermarkUtil.watermark(realPath, pdfPath, waterPdfPath, watermark);
			realFileName = "water" + realFileName;
		}
		
		// 文件信息放入缓存
		String name = "";
		FileInfo fileInfo = new FileInfo();
		fileInfo.setName(realFileName);
		fileInfo.setFtpFilePath(path);
		fileInfo.setConvertFilePath(filePath);
		fileInfo.setLocalPath(realPath);
		fileInfo.setOrgWatermark(watermark);
		FileInfoCache.putAuthCache(fileInfo);
		fileConvertService.runTask(this.getRelationDao());
		File localFile = new File(filePath + "/" + realFileName);
		name = FTPUtil.uploadFile(localFile, path, realFileName);

		// 保存文件附件信息
		ProductAttachment productAttachment = new ProductAttachment();
		productAttachment.setProductId(entity.getProductId());
		productAttachment.setFileName(tempFileName);
		productAttachment.setSize((int) file.getSize());
		productAttachment.setFileUrl(path + "/" + realFileName);
		getRelationDao().insert("productAttachment.insert", productAttachment);
		return name;
	}

	@Override
	public void init(InitData initData) throws Exception {

	}

	/**
	 * 获取产品资料列表数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getProductAttachmentGrid(ProductAttachment entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<ProductAttachment> resultList = (List<ProductAttachment>) this.getPaginatedList(entity);
		Long record = this.getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	public void saveOrUpdate(ProductAttachment entity) throws Exception {
		// 预留URL设置以及实体文件上传
		Long id = entity.getId();
		if (id != null) {
			this.update(entity);
		} else {
			this.save(entity);
		}
	}
	
	/**
	 * 查询产品的居间公司水印
	 * 
	 * @param product
	 * @return
	 * @throws Exception
	 */
	public String getOrgWatermark(ProductAttachment entity) throws Exception {
		return (String) super.getRelationDao().selectOne("productAttachment.select_org_watermark", entity);
	}

}
