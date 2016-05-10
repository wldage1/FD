package com.sw.plugins.product.manage.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.manage.entity.ProductSeckill;

public class ProductSeckillService extends CommonService<ProductSeckill> {

	@Override
	public void save(ProductSeckill entity) throws Exception {
		super.getRelationDao().insert("productSeckill.insert", entity);
	}

	@Override
	public void update(ProductSeckill entity) throws Exception {
		super.getRelationDao().update("productSeckill.update", entity);
	}

	@Override
	public Long getRecordCount(ProductSeckill entity) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductSeckill> getList(ProductSeckill entity) throws Exception {
		return (List<ProductSeckill>) super.getRelationDao().selectList("productSeckill.select_list", entity);
	}

	@Override
	public List<ProductSeckill> getPaginatedList(ProductSeckill entity) throws Exception {
		return null;
	}

	@Override
	public void delete(ProductSeckill entity) throws Exception {
	}

	@Override
	public void deleteByArr(ProductSeckill entity) throws Exception {
	}

	@Override
	public ProductSeckill getOne(ProductSeckill entity) throws Exception {
		return (ProductSeckill) super.getRelationDao().selectOne("productSeckill.select_list", entity);
	}

	@Override
	public Map<String, Object> getGrid(ProductSeckill entity) throws Exception {
		return null;
	}

	@Override
	public String upload(ProductSeckill entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public Object download(ProductSeckill entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
	}

	public void saveorupdate(ProductSeckill productSeckill) throws Exception {
		Long secKillId = productSeckill.getId();
		if (secKillId != null) {
			this.update(productSeckill);
		} else {
			this.save(productSeckill);
		}
	}

}
