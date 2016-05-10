package com.sw.plugins.product.promotion.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.promotion.entity.ProductPositionMapping;
import com.sw.plugins.product.promotion.entity.Promotion;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

public class PromotionService extends CommonService<Promotion> {

	@Resource
	private ProductPositionMappingService positionMappingService;
	@Resource
	private UserLoginService userLoginService;

	@Override
	public void save(Promotion entity) throws Exception {
		getRelationDao().insert("promotion.insert", entity);
	}

	@Override
	public void update(Promotion entity) throws Exception {
		getRelationDao().update("promotion.update", entity);
	}

	@Override
	public Long getRecordCount(Promotion entity) throws Exception {
		return (long)getRelationDao().getCount("promotion.count_product_by_positionid", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Promotion> getList(Promotion entity) throws Exception {
		return (List<Promotion>) getRelationDao().selectList("promotion.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Promotion> getPaginatedList(Promotion entity) throws Exception {
		return (List<Promotion>) getRelationDao().selectList("promotion.select_product_by_positionid_paginated", entity);
	}

	@Override
	public void delete(Promotion entity) throws Exception {
		getRelationDao().delete("promotion.delete", entity);
	}

	@Override
	public void deleteByArr(Promotion entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}

	@Override
	public Promotion getOne(Promotion entity) throws Exception {

		return (Promotion) getRelationDao().selectOne("promotion.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(Promotion entity) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = userLoginService.getCurrLoginUser();
		Organization organization = user.getSelfOrg();
		if (organization != null) {
			int isCommission = organization.getIsCommission();
			Long id = organization.getId();
			if (isCommission == 1) {
				entity.setOrgID(id);
			} else {
				entity.setOrgID(null);
			}
		}
		List<Promotion> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	@Override
	public String upload(Promotion entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(Promotion entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
	}

	public void updatePositionOrder(Promotion promotion) throws Exception {
		Long positionMappingId = promotion.getPositionMappingId();
		List<ProductPositionMapping> positionMappingList = promotion.getPositionMappingList();
		if (positionMappingList.size() > 0) {
			for (int i = 0; i < positionMappingList.size(); i++) {
				ProductPositionMapping mapping = positionMappingList.get(i);
				mapping.setId(positionMappingId);
				positionMappingService.update(mapping);
			}
		}
	}

	public void deletePositionMapping(Promotion entity) throws Exception {
		ProductPositionMapping pom = new ProductPositionMapping();
		pom.setIds(entity.getIds());
		positionMappingService.deleteByArr(pom);
	}

}
