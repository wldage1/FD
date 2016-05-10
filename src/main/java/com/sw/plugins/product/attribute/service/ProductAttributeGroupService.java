package com.sw.plugins.product.attribute.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.attribute.entity.ProductAttribute;
import com.sw.plugins.product.attribute.entity.ProductAttributeGroup;

public class ProductAttributeGroupService extends CommonService<ProductAttributeGroup> {

	@Resource
	private ProductAttributeService attributeService;

	/**
	 * 保存或更新产品属性组
	 * 
	 * @author baokai Created on :2013-3-5 上午11:22:22
	 */
	public void saveOrUpdate(ProductAttributeGroup entity) throws Exception {
		try {
			String attributeString = entity.getAttributeString();
			ProductAttribute pa = new ProductAttribute();
			pa.setInvestCategoryId(entity.getId());
			List<ProductAttribute> productAttributeList = attributeService.getList(pa);
			List<String> delIds = new ArrayList<String>();
			for (int i = 0; i < productAttributeList.size(); i++) {
				ProductAttribute temp = productAttributeList.get(i);
				delIds.add(temp.getId().toString());
			}
			String[] attributeStr = attributeString.split("@");
			for (int i = 0; i < attributeStr.length; i++) {
				String[] attributes = attributeStr[i].split("#");
				ProductAttribute attribute = new ProductAttribute();
				// 属性所属类型
				attribute.setInvestCategoryId(entity.getId());
				// 属性名称
				attribute.setName(attributes[0]);
				// 属性code
				attribute.setCode(attributes[1]);
				// 是否为空
				attribute.setIsNotNull(Integer.parseInt(attributes[2]));
				// 是否可用
				attribute.setIsEnabled(Integer.parseInt(attributes[3]));
				// 是否作为查询条件
				attribute.setIsForQuery(Integer.parseInt(attributes[4]));
				// 类型
				attribute.setPageShowType(Long.parseLong(attributes[5]));
				// 默认值
				attribute.setDefaultValue(attributes[6]);
				if (attributes[7] != null && !attributes[7].equals("")) {
					attribute.setValidationRuleId(Long.parseLong(attributes[7]));
				} else {
					attribute.setValidationRuleId(0l);
				}
				attribute.setSortNumber(Integer.parseInt(attributes[8]));
				if (attributes.length > 9 && attributes[9] != null && !attributes[9].equals("") && !attributes[9].equals("0")) {
					attribute.setId(Long.parseLong(attributes[9]));
					attributeService.update(attribute);
					delIds.remove(attributes[9]);
				} else {
					attributeService.save(attribute);
				}
			}

			ProductAttribute temp = new ProductAttribute();
			temp.setIds(delIds.toArray(new String[delIds.size()]));
			attributeService.deleteByArr(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(ProductAttributeGroup entity) throws Exception {
		getRelationDao().insert("productAttributeGroup.insert", entity);

	}

	/**
	 * 把字符串转换为ProductAttribute对象
	 * 
	 * @param str
	 * @return List<ProductAttribute>对象
	 * @throws Exception
	 */
	public List<ProductAttribute> formatData(ProductAttributeGroup entity) throws Exception {
		String[] attributeStr = null;
		List<ProductAttribute> productAttributeList = new ArrayList<ProductAttribute>();
		if (entity != null && entity.getAttributeString() != null && !"".equals(entity.getAttributeString())) {
			attributeStr = entity.getAttributeString().split("@");
		}
		if (attributeStr!= null && attributeStr.length > 0) {

			for (int i = 0; i < attributeStr.length; i++) {
				String[] attributes = attributeStr[i].split("#");
				ProductAttribute attribute = new ProductAttribute();
				// 名称
				attribute.setName(attributes[0]);
				// 代码
				attribute.setCode(attributes[1]);
				// 是否为空
				attribute.setIsNotNull(Integer.parseInt(attributes[2]));
				// 是否可用
				attribute.setIsEnabled(Integer.parseInt(attributes[3]));
				// 是否作为查询条件
				attribute.setIsForQuery(Integer.parseInt(attributes[4]));
				// 元素类型
				attribute.setPageShowType(Long.parseLong(attributes[5]));
				// 默认值
				attribute.setDefaultValue(attributes[6]);
				if (attributes[7] != null && !attributes[7].equals(""))
					attribute.setValidationRuleId(Long.parseLong(attributes[7]));
				attribute.setSortNumber(Integer.parseInt(attributes[8]));
				if (attributes.length > 9) {
					attribute.setId(Long.valueOf(attributes[9]));
				}
				productAttributeList.add(attribute);
			}
		}
		return productAttributeList;
	}

	/**
	 * 更新产品数组
	 */
	@Override
	public void update(ProductAttributeGroup entity) throws Exception {
		getRelationDao().update("productAttributeGroup.update", entity);
	}
	/**
	 * 检查产品属性组名称和code是否唯一
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Long checkProductAttribute(ProductAttributeGroup entity)throws Exception{
		return (Long)getRelationDao().getCount("productAttribute.checkAttribute", entity);
	}

	/**
	 * 记录总数
	 */
	@Override
	public Long getRecordCount(ProductAttributeGroup entity) throws Exception {
		return (Long)getRelationDao().getCount("productAttributeGroup.count", entity);
	}

	/**
	 * 通用查询
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProductAttributeGroup> getList(ProductAttributeGroup entity) throws Exception {
		return (List<ProductAttributeGroup>) getRelationDao().selectList("productAttributeGroup.select", entity);
	}

	/**
	 * 分页查询
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProductAttributeGroup> getPaginatedList(ProductAttributeGroup entity) throws Exception {
		return (List<ProductAttributeGroup>) getRelationDao().selectList("productAttributeGroup.select_paginated", entity);
	}

	/**
	 * 删除操作
	 */
	@Override
	public void delete(ProductAttributeGroup entity) throws Exception {
		getRelationDao().delete("productAttributeGroup.delete", entity);
	}

	@Override
	public void deleteByArr(ProductAttributeGroup entity) throws Exception {

	}

	/**
	 * 根据ID获得当前产品属性组
	 */
	@Override
	public ProductAttributeGroup getOne(ProductAttributeGroup entity) throws Exception {
		return (ProductAttributeGroup) getRelationDao().selectOne("productAttributeGroup.select", entity);
	}

	/**
	 * 获得产品属性组列表
	 */
	@Override
	public Map<String, Object> getGrid(ProductAttributeGroup entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<ProductAttributeGroup> resultList = getPaginatedList(entity);
		/*
		for (ProductAttributeGroup productAttributeGroup : resultList) {
			Map<String, Object> mapProductAttributeGroup = new Hashtable<String, Object>();
			List<Object> cellList = new ArrayList<Object>();
			cellList.add(productAttributeGroup.getName());
			cellList.add(productAttributeGroup.getCode());
			mapProductAttributeGroup.put("id", productAttributeGroup.getId());
			mapProductAttributeGroup.put("cell", cellList);
			list.add(mapProductAttributeGroup);
		}
		*/
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	@Override
	public String upload(ProductAttributeGroup entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductAttributeGroup entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 删除产品属性组
	 * 
	 * @author baokai Created on :2013-3-1 下午3:11:13
	 */
	public boolean deleteAttributeGroup(ProductAttributeGroup entity) throws Exception {
		// 判断该分类下是否存在产品
		delete(entity);
		return true;
	}

}
