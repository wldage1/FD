package com.sw.plugins.storage.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mongodb.gridfs.GridFSDBFile;
import com.sw.core.common.Constant;
import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.storage.entity.GridFile;
import com.sw.plugins.storage.service.StorageFs;

/**
 * 
 * 文件存储控制器
 *
 */
@Controller
@RequestMapping(value = "/storage")
public class StorageController extends BaseController<GridFile> {

	private static Logger logger = Logger.getLogger(StorageController.class);

	private StorageFs storageService;
	
	/**
	 * 以弹出方式下载MongoDB中保存文件
	 * ${base}/storage/download?id=5135c15024f1fd8d3a116210
	 * @param gridFile
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public HttpEntity<byte[]> getDownloadById(GridFile gridFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpHeaders header = null;
		byte[] data = null;
		try {
			if (gridFile.getId() != null) {
				GridFSDBFile file;
				file = storageService.download(gridFile, request);
				data = IOUtils.toByteArray(file.getInputStream());
				
				//设置response元素,对文件名进行encode，防止中文名称乱码
				response.setHeader("Content-Disposition", "attachment;filename=\"" + java.net.URLEncoder.encode(file.getFilename(), "UTF-8") + "\"") ;
				response.setContentType(file.getContentType());
				response.setContentLength((int) file.getLength());
				response.getOutputStream().write(data);
				response.getOutputStream().flush();
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new HttpEntity<byte[]>(data, header);
	}
	
	/**
	 * 以弹出方式下载文件
	 * @param p 文件相对路径
	 * @param n 文件名称
	 * @param request 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public HttpEntity<byte[]> getDowenloadFile(String p, String n, HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpHeaders header = null;
		InputStream in = null ;
		OutputStream out = null ;
		byte[] data = null;
		try {
			if (n != null && !"".equals(n)) {
				File file = storageService.getFile(p, n, super.initData) ;
				in = new FileInputStream(file);
				data = new byte[in.available()];
				response.setHeader("Content-disposition", "attachment;filename=\"" + java.net.URLEncoder.encode(file.getName(), "UTF-8") + "\"") ;
				out = new BufferedOutputStream(response.getOutputStream());
				out.write(data);
				out.flush();
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}finally {
			if(header != null) header.clear() ;
			if(in != null) in.close() ;
			if(out != null) out.close();
		}
		return new HttpEntity<byte[]>(data, header);
	}

	@Override
	public String uploadfile(GridFile entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 上传文件，以form方式上传文件，保存至MongoDB中
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/store", method = RequestMethod.POST)
	public CommonModelAndView store(HttpServletRequest request, Map<String, Object> model) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		GridFile gf = new GridFile();
		model.put(Constant.STATUS, "false");
		try {
			Iterator<String> iterator = multipartRequest.getFileNames();
			String ids = "" ;
			//遍历上传文件
			while(iterator.hasNext()){
				String fileName = iterator.next().toString();
				List<MultipartFile> flist = multipartRequest.getFiles(fileName);
				for (MultipartFile mfile : flist) {
					gf.setFile(mfile);
					ids += storageService.upload(gf, request) + ",";
				}
			}
			
			model.put("ids", ids.length() == 0 ? "" : ids.substring(0, ids.length() -1)) ;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		model.put(Constant.STATUS, "success");
		return new CommonModelAndView("jsonView", model);
	}
	
	/**
	 * 上传文件，以form方式上传文件，保存至临时目录中
	 * 如果保存到固定目录，fp 文件目录
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String upload(HttpServletRequest request, Map<String, Object> model) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		request.getContextPath();
		String names = "" ;
		try {
			Iterator<String> iterator = multipartRequest.getFileNames();
			//遍历上传文件
			while(iterator.hasNext()){
				String fileName = iterator.next().toString();
				List<MultipartFile> flist = multipartRequest.getFiles(fileName);
				for (MultipartFile mfile : flist) {
					
					names += storageService.uploadFile(mfile, multipartRequest, super.initData) + "," ;
				}
			}
			
			names = names.length() == 0 ? "" : names.substring(0, names.length() -1) ;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return names;
	}
	
	/**
	 * 使用uploadify插件，多文件上传
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/storeFromUploadify", method = RequestMethod.POST)
	public @ResponseBody String storeFromUploadify(HttpServletRequest request, Map<String, Object> model) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		GridFile gf = new GridFile();
		String ids = "" ;
		try {
			Iterator<String> iterator = multipartRequest.getFileNames();
			//遍历上传文件
			while(iterator.hasNext()){
				String fileName = iterator.next().toString();
				List<MultipartFile> flist = multipartRequest.getFiles(fileName);
				for (MultipartFile mfile : flist) {
					gf.setFile(mfile);
					ids += storageService.upload(gf, request) + ",";
				}
			}
			
			ids = ids.length() == 0 ? "" : ids.substring(0, ids.length() -1) ;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return ids;
	}
	
	/**
	 * 删除MongoDB中存储文件
	 * @param gridFile
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(GridFile gridFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			if(gridFile.getId() != null && !"".equals(gridFile.getId())) {
				//删除mongodb中的文件
				storageService.delete(gridFile);
			}
		}catch(Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
	}
	
	/**
	 * 删除文件，如果没有目录，则默认删除临时文件夹中文件
	 * @param p	文件的相对路径
	 * @param n 文件名称
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	public void deleteFile(String p, String n, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			storageService.deleteFile(p, n, super.initData) ;
		}catch(Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
	}

	/**
	 * 下载文件，将文件直接显示在页面中，一般为图片
	 * @param gridFile
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public void getFileById(GridFile gridFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte[] data = null;
		try {
			if (gridFile.getId() != null) {
				GridFSDBFile file;
				file = storageService.download(gridFile, request);
				data = IOUtils.toByteArray(file.getInputStream());
				response.setContentType(file.getContentType());
				response.setContentLength((int) file.getLength());
				response.getOutputStream().write(data);
				response.getOutputStream().flush();
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
	}

	@Override
	public String downloadfile(GridFile entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(GridFile entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(GridFile entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
}
