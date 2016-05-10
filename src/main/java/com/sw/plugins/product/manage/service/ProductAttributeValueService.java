package com.sw.plugins.product.manage.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.manage.entity.ProductAttributeValue;

public class ProductAttributeValueService extends CommonService<ProductAttributeValue> {

	@Override
	public void save(ProductAttributeValue entity) throws Exception {
		getRelationDao().insert("productAttributeValue.insert", entity);
	}

	@Override
	public void update(ProductAttributeValue entity) throws Exception {
		getRelationDao().update("productAttributeValue.update", entity);
	}

	@Override
	public Long getRecordCount(ProductAttributeValue entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeValue> getList(ProductAttributeValue entity) throws Exception {
		return (List<ProductAttributeValue>) getRelationDao().selectList("productAttributeValue.select", entity);
	}

	@Override
	public List<ProductAttributeValue> getPaginatedList(ProductAttributeValue entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ProductAttributeValue entity) throws Exception {
		getRelationDao().delete("productAttributeValue.delete", entity);
	}

	@Override
	public void deleteByArr(ProductAttributeValue entity) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public ProductAttributeValue getOne(ProductAttributeValue entity) throws Exception {

		return (ProductAttributeValue) getRelationDao().selectOne("productAttributeValue.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(ProductAttributeValue entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(ProductAttributeValue entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductAttributeValue entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
	}
}
