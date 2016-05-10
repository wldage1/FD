package com.sw.plugins.product.manage.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.manage.entity.ProductDocConfig;

public class ProductDocConfigService extends CommonService<ProductDocConfig> {
	
	public void saveProductDocConfig(ProductDocConfig entity) throws Exception {
		Long co = this.getRecordCount(entity);
		if(co == 0){
			this.save(entity);
		}else{
			this.update(entity);
		}
	}
	

	@Override
	public void save(ProductDocConfig entity) throws Exception {
		getRelationDao().insert("productDocConfig.insert_product_docconfig", entity);
	}

	@Override
	public void update(ProductDocConfig entity) throws Exception {
		getRelationDao().update("productDocConfig.update_product_docconfig", entity);
	}

	@Override
	public Long getRecordCount(ProductDocConfig entity) throws Exception {
		return (long) getRelationDao().getCount("productDocConfig.count_product_docconfig", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDocConfig> getList(ProductDocConfig entity) throws Exception {
		return (List<ProductDocConfig>) getRelationDao().selectList("productDocConfig.select", entity);
	}

	@Override
	public List<ProductDocConfig> getPaginatedList(ProductDocConfig entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ProductDocConfig entity) throws Exception {
		getRelationDao().delete("productDocConfig.delete_product_docconfig", entity);
	}

	@Override
	public void deleteByArr(ProductDocConfig entity) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public ProductDocConfig getOne(ProductDocConfig entity) throws Exception {
		return (ProductDocConfig) getRelationDao().selectOne("productDocConfig.select_product_docconfig", entity);
	}

	@Override
	public Map<String, Object> getGrid(ProductDocConfig entity) throws Exception {
		return null;
	}

	@Override
	public String upload(ProductDocConfig entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductDocConfig entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
	}

}
