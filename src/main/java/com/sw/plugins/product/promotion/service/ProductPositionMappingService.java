package com.sw.plugins.product.promotion.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.promotion.entity.ProductPositionMapping;

public class ProductPositionMappingService extends CommonService<ProductPositionMapping> {

	
	public void saveOrUpdate(ProductPositionMapping entity) throws Exception {
		Long id = entity.getId();
		if(id == null){
			save(entity);
		}else{
			update(entity);
		}
	}
	
	@Override
	public void save(ProductPositionMapping entity) throws Exception {
		getRelationDao().insert("productPositionMapping.insert", entity);
	}

	@Override
	public void update(ProductPositionMapping entity) throws Exception {
		getRelationDao().update("productPositionMapping.update", entity);
	}

	@Override
	public Long getRecordCount(ProductPositionMapping entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductPositionMapping> getList(ProductPositionMapping entity) throws Exception {
		return (List<ProductPositionMapping>) getRelationDao().selectList("productPositionMapping.select", entity);
	}

	@Override
	public List<ProductPositionMapping> getPaginatedList(ProductPositionMapping entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ProductPositionMapping entity) throws Exception {
		getRelationDao().delete("productPositionMapping.delete", entity);
	}
	
	public void deleteByProductId(ProductPositionMapping entity) throws Exception {
		getRelationDao().delete("productPositionMapping.delete_by_productid", entity);
	}

	@Override
	public void deleteByArr(ProductPositionMapping entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}

	@Override
	public ProductPositionMapping getOne(ProductPositionMapping entity) throws Exception {

		return (ProductPositionMapping) getRelationDao().selectOne("productPositionMapping.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(ProductPositionMapping entity) throws Exception {
		return null;
	}

	@Override
	public String upload(ProductPositionMapping entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductPositionMapping entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
	}

}
