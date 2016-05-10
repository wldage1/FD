package com.sw.plugins.config.websitesetting.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.config.websitesetting.entity.WebsiteSetting;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

@Service
public class WebsiteSettingService extends CommonService<WebsiteSetting>{
	
	private static Logger logger = Logger.getLogger(WebsiteSettingService.class);
	
	@Override
	public void save(WebsiteSetting entity) throws Exception {
		super.getRelationDao().insert("websiteSetting.insert", entity);
	}

	@Override
	public void update(WebsiteSetting entity) throws Exception {
		super.getRelationDao().update("websiteSetting.update", entity);
	}

	@Override
	public Long getRecordCount(WebsiteSetting entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WebsiteSetting> getList(WebsiteSetting entity) throws Exception {
		return (List<WebsiteSetting>)super.getRelationDao().selectList("websiteSetting.select_baseInfo", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebsiteSetting> getSMTPList(WebsiteSetting entity) throws Exception {
		return (List<WebsiteSetting>)super.getRelationDao().selectList("websiteSetting.select_SMPT", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebsiteSetting> getWebInfoList(WebsiteSetting entity) throws Exception {
		return (List<WebsiteSetting>)super.getRelationDao().selectList("websiteSetting.select_webInfo", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebsiteSetting> getWebLogoList(WebsiteSetting entity) throws Exception {
		return (List<WebsiteSetting>)super.getRelationDao().selectList("websiteSetting.select_webLogo", entity);
	}
	
	/** 保存上传Logo路径方法 **/
	public void saveLogo(WebsiteSetting entity)throws Exception{
		String value =SystemProperty.getInstance("config").getProperty("websitesetting.logo.path")+entity.getCode();
		entity.setValue(value);
		this.update(entity);
	}
	
	/**
	 * 上传图片至FTP
	 * @param entity
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadfile(WebsiteSetting entity, HttpServletRequest request)throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("Filedata");
		String tempFileName = request.getParameter("Filename");
		String path=SystemProperty.getInstance("config").getProperty("websitesetting.logo.path");
		return FTPUtil.uploadFile(file, path, tempFileName);
	}
	
	@Override
	public List<WebsiteSetting> getPaginatedList(WebsiteSetting entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(WebsiteSetting entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(WebsiteSetting entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WebsiteSetting getOne(WebsiteSetting entity) throws Exception {
		return (WebsiteSetting)super.getRelationDao().selectOne("websiteSetting.select_one", entity);
	}
	
	/**
	 *查询网站基本信息方法
	 * */
	@Override
	public Map<String, Object> getGrid(WebsiteSetting entity) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		List<WebsiteSetting> list = getList(entity);
		for (WebsiteSetting websiteSetting : list) {
			websiteSetting.setValue(HtmlUtils.htmlUnescape(websiteSetting.getValue()));
		}
		map.put("rows", list);
		return map;
	}
	
	/**
	 * 查询SMTP服务器基本信息方法
	 * */
	public Map<String, Object> getSMTPGrid(WebsiteSetting entity) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		List<WebsiteSetting> list = getSMTPList(entity); 
		map.put("rows", list);
		return map;
	}
	
	/**
	 * 查询网站基本信息方法
	 * */
	public Map<String, Object> getWebInfoGrid(WebsiteSetting entity) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		List<WebsiteSetting> list = getWebInfoList(entity); 
		map.put("rows", list);
		return map;
	}
	
	public Map<String,Object> getWebLogoGrid(WebsiteSetting entity) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		List<WebsiteSetting> list = getWebLogoList(entity);
		map.put("rows", list);
		return map;
	}
	
	@Override
	public String upload(WebsiteSetting entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(WebsiteSetting entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		try {
			logger.info("websiteSetting info initializing");
			WebsiteSetting website;
			Map<String, Object> parameterMap = initData.getWebsiteSetting();
				Iterator<Entry<String, Object>> ite = parameterMap.entrySet().iterator();
				while (ite.hasNext()) {
					website = new WebsiteSetting();
					Entry<String, Object> entry = (Entry<String, Object>) ite.next();
					website.setName((String) entry.getValue());
					website.setCode((String) entry.getKey());
					WebsiteSetting gp = getOne(website);
					if (gp != null) {
						continue;
					} else {
						website.setValue("");
						save(website);
					}
				}
			logger.info("websiteSetting info initialize finished");
		} catch (Exception e) {
			logger.error("websiteSetting info initialize fail");
			logger.error(DetailException.expDetail(e, this.getClass()));
		}

		
	}

}
