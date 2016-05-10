package com.sw.plugins.websitemanage.cooperative.service;

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
import com.sw.plugins.websitemanage.cooperative.entity.Partners;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

public class PartnersService extends CommonService<Partners> {

	@Override
	public void delete(Partners entity) throws Exception {
		getRelationDao().delete("partners.delete_partners", entity);
	}

	@Override
	public void deleteByArr(Partners entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Object download(Partners entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(Partners entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Partners> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	@Override
	public List<Partners> getList(Partners entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Partners getOne(Partners entity) throws Exception {
		return (Partners) getRelationDao().selectOne("partners.select_one", entity);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Partners> getPaginatedList(Partners entity) throws Exception {
		return (List<Partners>) getRelationDao().selectList("partners.select_partners_list", entity);
	}

	@Override
	public Long getRecordCount(Partners entity) throws Exception {
		return getRelationDao().getCount("partners.select_partners_count", entity);
	}

	@Override
	public void save(Partners entity) throws Exception {
		getRelationDao().insert("partners.insert_partners", entity);
	}

	@Override
	public void update(Partners entity) throws Exception {
		getRelationDao().update("partners.update_partners", entity);
	}

	public void saveOrUpdate(Partners entity) throws Exception {
//		if (entity.getPicName() != null && !("").equals(entity.getPicName())) {
//			String logo = SystemProperty.getInstance("config").getProperty("partner.logo.path") + entity.getPicName();
//			entity.setLogo(logo);
//		}
		if (entity.getId() == null) {// 新增
			save(entity);
		} else {// 修改
			update(entity);
		}
	}

	@Override
	public String upload(Partners entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

	public String uploadfile(Partners entity, HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("Filedata");
		String filename = request.getParameter("Filename");
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS"); 
		String time = sdFormat.format(new Date());
		String tempFileName = time+filename.substring(filename.length()-4,filename.length());
		String path = SystemProperty.getInstance("config").getProperty("partner.logo.path");
		return FTPUtil.uploadFile(file, path, tempFileName);
	}

}
