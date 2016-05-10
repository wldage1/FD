package com.sw.plugins.extension.youku.videomanager.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.extension.youku.videomanager.entity.ProductVideoMapping;

@Service
public class ProductVideoMappingService extends CommonService<ProductVideoMapping>{

	@Override
	public void save(ProductVideoMapping entity) throws Exception {
		super.getRelationDao().insert("productVideoMapping.insert", entity);
	}

	@Override
	public void update(ProductVideoMapping entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getRecordCount(ProductVideoMapping entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductVideoMapping> getList(ProductVideoMapping entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductVideoMapping> getPaginatedList(ProductVideoMapping entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ProductVideoMapping entity) throws Exception {
		super.getRelationDao().delete("productVideoMapping.delete", entity);
	}

	@Override
	public void deleteByArr(ProductVideoMapping entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProductVideoMapping getOne(ProductVideoMapping entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(ProductVideoMapping entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(ProductVideoMapping entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductVideoMapping entity,
			HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
