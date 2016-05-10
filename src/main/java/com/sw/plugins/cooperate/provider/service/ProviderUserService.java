package com.sw.plugins.cooperate.provider.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.cooperate.provider.entity.ProviderUser;

/**
 * 供应商用户-Service实现类
 * 
 * @author liuvaomin
 */
public class ProviderUserService extends CommonService<ProviderUser>{

	@Override
	public Map<String, Object> getGrid(ProviderUser entity) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new Hashtable<String, Object>();
		List<ProviderUser> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int)Math.ceil(record/(double)entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	@Override
	public ProviderUser getOne(ProviderUser entity) throws Exception {
		// TODO Auto-generated method stub
		return (ProviderUser) getRelationDao().selectOne("providerUser.selectOne", entity);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderUser> getPaginatedList(ProviderUser entity) throws Exception {
		return (List<ProviderUser>)getRelationDao().selectList("providerUser.selectPagintedList", entity);
	}


	@Override
	public Long getRecordCount(ProviderUser entity) throws Exception {
		// TODO Auto-generated method stub
		return getRelationDao().getCount("providerUser.count", entity);
	}
	public Long getCountForAccount(ProviderUser entity) throws Exception {
		// TODO Auto-generated method stub
		return getRelationDao().getCount("providerUser.countforaccount", entity);
	}


	@Override
	public void save(ProviderUser entity) throws Exception {
		getRelationDao().update("providerUser.insert", entity);	
		
	}


	@Override
	public void update(ProviderUser entity) throws Exception {
		super.getRelationDao().update("providerUser.update", entity);	
		
	}


	@Override
	public String upload(ProviderUser entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delete(ProviderUser entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().delete("providerUser.delete", entity);	
	}
	@Override
	public void deleteByArr(ProviderUser entity) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object download(ProviderUser entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<ProviderUser> getList(ProviderUser entity) throws Exception {
		return (List<ProviderUser>)getRelationDao().selectList("providerUser.selectList", entity);
	}

}
