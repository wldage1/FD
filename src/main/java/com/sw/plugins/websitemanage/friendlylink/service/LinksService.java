package com.sw.plugins.websitemanage.friendlylink.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.websitemanage.friendlylink.entity.Links;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

public class LinksService extends CommonService<Links> {

	@Override
	public void save(Links entity) throws Exception {
		getRelationDao().insert("links.insert_links", entity);
	}

	@Override
	public void update(Links entity) throws Exception {
		getRelationDao().update("links.update_links", entity);
	}

	public void saveOrUpdate(Links entity) throws Exception {
	/*	if (entity.getPicName() != null && !("").equals(entity.getPicName())) {
			String logo = SystemProperty.getInstance("config").getProperty("link.logo.path") + entity.getPicName();
			entity.setLogo(logo);
		}*/
		if (entity.getId() == null) {// 新增
			save(entity);
		} else {// 修改
			update(entity);
		}
	}

	@Override
	public Long getRecordCount(Links entity) throws Exception {
		return getRelationDao().getCount("links.select_links_count", entity);
	}

	@Override
	public List<Links> getList(Links entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Links> getPaginatedList(Links entity) throws Exception {
		return (List<Links>) getRelationDao().selectList("links.select_links_list", entity);
	}

	@Override
	public void delete(Links entity) throws Exception {
		getRelationDao().delete("links.delete_links", entity);
	}

	@Override
	public void deleteByArr(Links entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Links getOne(Links entity) throws Exception {
		return (Links) getRelationDao().selectOne("links.select_one", entity);
	}

	@Override
	public Map<String, Object> getGrid(Links entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Links> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	public String uploadfile(Links entity, HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("Filedata");
		String filename = request.getParameter("Filename");
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS"); 
		String time = sdFormat.format(new Date());
		String tempFileName = time+filename.substring(filename.length()-4,filename.length());
		String path = SystemProperty.getInstance("config").getProperty("link.logo.path");
		return FTPUtil.uploadFile(file, path, tempFileName);
	}

	@Override
	public String upload(Links entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(Links entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

}
