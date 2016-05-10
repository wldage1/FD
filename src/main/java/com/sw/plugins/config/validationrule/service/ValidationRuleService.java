package com.sw.plugins.config.validationrule.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.service.CommonService;
import com.sw.plugins.config.validationrule.entity.ValidationRule;


/**
 *  类简要说明
 *  @author 刘宝民
 *  @version 1.0
 *  </pre>
 *  Created on :2013-02-27
 *  LastModified:
 *  History:
 *  </pre>
 */
public class ValidationRuleService extends CommonService<ValidationRule>{

	/**
	 *获取验证规则列表信息(包括查询的)
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<ValidationRule> getList(ValidationRule validationRule) throws Exception {
		List<ValidationRule> resultList = null;
		resultList = (List<ValidationRule>) super.getRelationDao().selectList("validationRule.select", validationRule);
		return resultList;
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGrid(ValidationRule validationRule) {
		//从request对象中获取页面信息
		long record = 0;
		try {
			record = super.getRelationDao().getCount("validationRule.count_validation_rule_paginated",validationRule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<ValidationRule> resultList = null;
		try {
			resultList = (List<ValidationRule>) super.getRelationDao().selectList("validationRule.select_validation_rule_paginated", validationRule);
		} catch (Exception e1) {
		}
		//记录数
		//页数
		int pageCount = (int)Math.ceil(record/(double) validationRule.getRows());
		Map<String, Object> map = new Hashtable<String, Object>(); 
		map.put("rows", resultList);	
		map.put("page", validationRule.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	public void validationRuleDelete(ValidationRule validationRule) throws Exception {
		if (validationRule != null && validationRule.getId() != null) {
			super.getRelationDao().delete("validationRule.delete", validationRule);//单独删除
		} else if (validationRule != null && validationRule.getIds() != null) {
			for (String id : validationRule.getIds()) {
				validationRule.setId(Long.valueOf(id));
				super.getRelationDao().delete("validationRule.delete", validationRule);
			}
		}
	}
	public Long validationRuleCount(ValidationRule validationRule) throws Exception {
		return super.getRelationDao().getCount("validationRule.count", validationRule);//判断该验证过则是否已经存在 1已经存在 0不存在
	}
	public ValidationRule validationRuleSelect(ValidationRule validationRule) throws Exception {
		return (ValidationRule) super.getRelationDao().selectOne("validationRule.select", validationRule);
	}


	@Override
	public void init(com.sw.core.initialize.InitData initData) throws Exception {
		
	}


	@Override
	public Object download(ValidationRule entity, HttpServletRequest request)
			throws Exception {
		return null;
		// TODO Auto-generated method stub
		
	}
	public void saveOrUpdate(ValidationRule validationRule) throws Exception {
		if (validationRule != null && validationRule.getId() != null) {
			// 更新验证规则
			super.getRelationDao().update("validationRule.update", validationRule);
		} else {
			// 保存验证规则
			save(validationRule);
		}
	}
	@Override
	public String upload(ValidationRule entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void save(ValidationRule entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().insert("validationRule.insert", entity);
	}
	@Override
	public void update(ValidationRule entity) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Long getRecordCount(ValidationRule entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<ValidationRule> getPaginatedList(ValidationRule entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void delete(ValidationRule entity) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteByArr(ValidationRule entity) throws Exception {
		// TODO Auto-generated method stub
		
	}
	//取得一个的实体
	@Override
	public ValidationRule getOne(ValidationRule entity) throws Exception {
		// TODO Auto-generated method stub
		return (ValidationRule) super.getRelationDao().selectOne("validationRule.select", entity);
	}



}