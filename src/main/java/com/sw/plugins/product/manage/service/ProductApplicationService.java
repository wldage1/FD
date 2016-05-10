package com.sw.plugins.product.manage.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.ProductApplication;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

public class ProductApplicationService extends CommonService<ProductApplication> {

	@Resource
	private ProductService productService;
	@Resource
	private UserLoginService userLoginService;

	@Override
	public Map<String, Object> getGrid(ProductApplication entity) throws Exception {
		return null;
	}

	/**
	 * 审核功能
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void saveAndUpdateProduct(ProductApplication entity) throws Exception {
		User user = userLoginService.getCurrLoginUser();
		entity.setUserID(user.getId());
		this.save(entity);
		Product product = new Product();
		product.setId(entity.getProductID());
		if (entity.getStatus() == 0) {
			product.setStatus((short) 3);
		} else {
			product.setStatus((short) 2);
			//产品成立  审核状态 销售状态不变
			if(!entity.getSellStatusTemp().equals((short)5)){
				product.setSellStatus((short) 1);
			}
		}
		productService.update(product);
	}

	@Override
	public void save(ProductApplication entity) throws Exception {
		super.getRelationDao().insert("productApplication.save", entity);
	}

	@Override
	public void update(ProductApplication entity) throws Exception {
		super.getRelationDao().update("productApplication.update", entity);
	}

	@Override
	public Long getRecordCount(ProductApplication entity) throws Exception {
		Object obj = getRelationDao().getCount("productApplication.count", entity);
		if (obj instanceof Integer) {
			return (Long) obj;
		} else if (obj instanceof Long) {
			return (Long) obj;
		}
		return 0l;
	}

	@Override
	public List<ProductApplication> getList(ProductApplication entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductApplication> getPaginatedList(ProductApplication entity) throws Exception {
		return (List<ProductApplication>) getRelationDao().selectList("productApplication.select_paginated", entity);
	}

	@Override
	public void delete(ProductApplication entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByArr(ProductApplication entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ProductApplication getOne(ProductApplication entity) throws Exception {
		return (ProductApplication) super.getRelationDao().selectOne("productApplication.select_one", entity);
	}

	@Override
	public String upload(ProductApplication entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductApplication entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
	}
}
