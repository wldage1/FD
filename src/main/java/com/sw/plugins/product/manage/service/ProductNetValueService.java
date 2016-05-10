package com.sw.plugins.product.manage.service;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.manage.entity.ProductNetValue;

public class ProductNetValueService extends CommonService<ProductNetValue> {

	@Override
	public void save(ProductNetValue entity) throws Exception {
		getRelationDao().insert("productNetValue.insert_product_netvalue", entity);
	}

	@Override
	public void update(ProductNetValue entity) throws Exception {
		getRelationDao().update("productNetValue.update_product_netvalue", entity);
	}

	@Override
	public Long getRecordCount(ProductNetValue entity) throws Exception {
		return (long) super.getRelationDao().getCount("productNetValue.count", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductNetValue> getList(ProductNetValue entity) throws Exception {
		return (List<ProductNetValue>) getRelationDao().selectList("productNetValue.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductNetValue> getPaginatedList(ProductNetValue entity) throws Exception {
		return (List<ProductNetValue>) getRelationDao().selectList("productNetValue.select_product_netvalue_paginated", entity);
	}

	@Override
	public void delete(ProductNetValue entity) throws Exception {
		getRelationDao().delete("productNetValue.delete", entity);
	}

	@Override
	public void deleteByArr(ProductNetValue entity) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public ProductNetValue getOne(ProductNetValue entity) throws Exception {

		return (ProductNetValue) getRelationDao().selectOne("productNetValue.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(ProductNetValue entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<ProductNetValue> resultList = this.getProductNetValueList(entity);
		map.put("rows", resultList);
		return map;
	}

	@Override
	public String upload(ProductNetValue entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductNetValue entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 获取子产品
	 * 
	 * @param productNetValue
	 * @throws Exception
	 */
	public Long countProductNetValue(ProductNetValue productNetValue) throws Exception {
		return (long) getRelationDao().getCount("productNetValue.count_sub_product", productNetValue);
	}

	/**
	 * 获取子产品收益
	 * 
	 * @param productNetValue
	 * @throws Exception
	 */
	public ProductNetValue getProductNetValue(ProductNetValue productNetValue) throws Exception {
		return (ProductNetValue) getRelationDao().selectOne("productNetValue.select_product_netvalue", productNetValue);
	}

	/**
	 * 获取子产品收益
	 * 
	 * @param productNetValue
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ProductNetValue> getProductNetValueList(ProductNetValue productNetValue) throws Exception {
		return (List<ProductNetValue>) getRelationDao().selectList("productNetValue.select_product_netvalue", productNetValue);
	}

	/**
	 * 保存子产品收益表
	 * 
	 * @param productNetValue
	 * @throws Exception
	 */
	public void saveProductNetValue(Long subProductId, List<ProductNetValue> productNetValueList) throws Exception {
		if (productNetValueList != null) {
			for (ProductNetValue pnt : productNetValueList) {
				Long id = pnt.getId();
				if (id == null) {
					BigDecimal netValue = pnt.getNetValue();
					if (netValue != null) {
						pnt.setSubProductId(subProductId);
						save(pnt);
					}
				} else {
					update(pnt);
				}
			}
		}
	}

	/**
	 * 删除子产品收益表
	 * 
	 * @param productNetValue
	 * @throws Exception
	 */
	public void deleteProductNetValue(ProductNetValue productNetValue) throws Exception {
		getRelationDao().delete("productNetValue.delete_product_netvalue", productNetValue);
	}

	public Map<String, Object> getProductNetValueGrid(ProductNetValue productNetValue) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<ProductNetValue> resultList = (List<ProductNetValue>) this.getPaginatedList(productNetValue);
		Long record = this.getRecordCount(productNetValue);
		int pageCount = (int) Math.ceil(record / (double) productNetValue.getRows());
		map.put("rows", resultList);
		map.put("page", productNetValue.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	public void saveOrUpdate(ProductNetValue productNetValue) throws Exception {
		Long id = productNetValue.getId();
		if (id == null) {
			this.save(productNetValue);
		} else {
			this.update(productNetValue);
		}
	}
}
