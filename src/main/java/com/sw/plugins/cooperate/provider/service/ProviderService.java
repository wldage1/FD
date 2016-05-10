package com.sw.plugins.cooperate.provider.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.cooperate.provider.entity.Provider;
import com.sw.plugins.cooperate.provider.entity.ProviderUser;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

/**
 * 供应商-Service实现类
 * 
 * @author runchao
 */
public class ProviderService extends CommonService<Provider>{
	
	@Resource
	ProviderUserService providerUserService;
	
	/**
	 * 获取供应商列表集合
	 * 
	 * @param provider
	 * @return
	 */
	@Override
	public Map<String, Object> getGrid(Provider provider) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Provider> resultList = getPaginatedList(provider);
		Long record = getRecordCount(provider);
		int pageCount = (int)Math.ceil(record/(double)provider.getRows());
		map.put("rows", resultList);
		map.put("page", provider.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 供应商分页查询
	 * 
	 * @param provider
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getPaginatedList(Provider provider) throws Exception {
		List<Provider> list = (List<Provider>)getRelationDao().selectList("provider.selectPagintedList", provider);
		return list;
	}
	
	/**
	 * 供应商统计查询
	 * 
	 * @param provider
	 * @return
	 */
	@Override
	public Long getRecordCount(Provider provider) throws Exception {
		return getRelationDao().getCount("provider.count", provider);
	}
	
	/**
	 * 获取某个供应商
	 * 
	 * @param provider
	 * @return
	 */
	@Override
	public Provider getOne(Provider provider) throws Exception {
		provider = (Provider) getRelationDao().selectOne("provider.selectOne", provider);
		provider.setAwards(HtmlUtils.htmlUnescape(provider.getAwards()));
		provider.setDiscription(HtmlUtils.htmlUnescape(provider.getDiscription()));
		return provider;
	}

	/**
	 * 新增供应商
	 * 
	 * @param provider
	 */
	@Override
	public void save(Provider provider) throws Exception {
		provider.setAwards(HtmlUtils.htmlEscape(provider.getAwards()));
		provider.setDiscription(HtmlUtils.htmlEscape(provider.getDiscription()));
		getRelationDao().insert("provider.insert", provider);
	}

	/**
	 * 供应商修改
	 * 
	 * @param provider
	 */
	@Override
	public void update(Provider provider) throws Exception {
		provider.setAwards(HtmlUtils.htmlEscape(provider.getAwards()));
		provider.setDiscription(HtmlUtils.htmlEscape(provider.getDiscription()));
		getRelationDao().update("provider.update", provider);
	}

	/**
	 * 删除供应商
	 * 
	 * @param provider
	 */
	@Override
	public void delete(Provider provider) throws Exception {
		provider.setDelStatus(2);
		getRelationDao().update("provider.delete", provider);
		ProviderUser providerUser = new ProviderUser();
		providerUser.setProvidersID(provider.getId());
		providerUserService.delete(providerUser);
	}
	
	/**
	 * 删除多个供应商
	 * 
	 * @param provider
	 */
	@Override
	public void deleteByArr(Provider provider) throws Exception {
		if(provider.getIds() != null){
			ProviderUser providerUser = new ProviderUser();
			for(String id : provider.getIds()){
				provider.setId(Long.valueOf(id));
				getRelationDao().update("provider.delete", provider);
				providerUser.setProvidersID(provider.getId());
				providerUserService.delete(providerUser);
			}
		}
	}
	
	/**
	 * 供应商Logo上传
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@Override
	public String upload(Provider provider, HttpServletRequest request) throws Exception {
		MultipartFile file = ((MultipartHttpServletRequest)request).getFile("Filedata");
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
		String time = sdFormat.format(new Date());
		String path = SystemProperty.getInstance("config").getProperty("provider.image.path") + time;
		//重置文件名
		String tempFileName = request.getParameter("Filename");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));
		String newFileName = FTPUtil.getRandomString();	
		String realFileName = newFileName + fileExtensionName;
		return FTPUtil.uploadFile(file, path, realFileName);
	}
	
	@Override
	public List<Provider> getList(Provider entity) throws Exception {
		@SuppressWarnings("unchecked")
		List<Provider> list = (List<Provider>)getRelationDao().selectList("provider.selectList", entity);
		return list;
	}

	@Override
	public Object download(Provider entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
