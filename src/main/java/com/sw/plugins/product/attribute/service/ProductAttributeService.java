package com.sw.plugins.product.attribute.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.common.Constant;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.config.area.entity.Area;
import com.sw.plugins.config.area.service.AreaService;
import com.sw.plugins.config.dictionary.entity.DictionaryItem;
import com.sw.plugins.config.dictionary.service.DictionaryItemService;
import com.sw.plugins.config.validationrule.entity.ValidationRule;
import com.sw.plugins.config.validationrule.service.ValidationRuleService;
import com.sw.plugins.product.attribute.entity.ProductAttribute;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.ProductAttributeValue;
import com.sw.plugins.product.manage.service.ProductAttributeValueService;

public class ProductAttributeService extends CommonService<ProductAttribute> {

	@Resource
	private DictionaryItemService dictionaryItemService;

	@Resource
	private ValidationRuleService validationRuleService;

	@Resource
	private ProductAttributeValueService productAttributeValueService;

	@Resource
	private AreaService areaService;

	/**
	 * 产品属性保存功能
	 */
	@Override
	public void save(ProductAttribute entity) throws Exception {
		getRelationDao().insert("productAttribute.insert", entity);
	}

	/**
	 * 产品属性更新功能
	 */
	@Override
	public void update(ProductAttribute entity) throws Exception {
		getRelationDao().update("productAttribute.update", entity);
	}

	/**
	 * 计算记录数
	 */
	@Override
	public Long getRecordCount(ProductAttribute entity) throws Exception {
		return (Long) getRelationDao().getCount("productAttribute.count", entity);
	}

	/**
	 * 获得产品属性列表
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProductAttribute> getList(ProductAttribute entity) throws Exception {
		return (List<ProductAttribute>) getRelationDao().selectList("productAttribute.select", entity);
	}

	/**
	 * 根据产品查询所有属性信息
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ProductAttribute> getListByUserProduct(Product entity) throws Exception {
		return (List<ProductAttribute>) getRelationDao().selectList("productAttribute.select_by_bussiness_type_product", entity);
	}

	@Override
	public List<ProductAttribute> getPaginatedList(ProductAttribute entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 产品属性删除功能
	 */
	@Override
	public void delete(ProductAttribute entity) throws Exception {
		getRelationDao().delete("productAttribute.delete", entity);
	}

	@Override
	public void deleteByArr(ProductAttribute entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}

	}

	/**
	 * 获得当前产品属性
	 */
	@Override
	public ProductAttribute getOne(ProductAttribute entity) throws Exception {
		return (ProductAttribute) getRelationDao().selectOne("productAttribute.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(ProductAttribute entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(ProductAttribute entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductAttribute entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 取得产品属性相关信息，如果是下拉，多选、单选，怎么从对应的表中取得数据，值之间用“*”分隔，key和value之间用“：”分隔
	 * 
	 * @author liu shiliang
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMap(ProductAttribute entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Object> list = new ArrayList<Object>();
		Map<String, Object> mapList = null;
		List<ProductAttribute> attributeList = getList(entity);
		if (attributeList != null && attributeList.size() > 0) {
			for (int i = 0; i < attributeList.size(); i++) {
				ProductAttribute pa = attributeList.get(i);
				if (pa != null) {
					mapList = new Hashtable<String, Object>();
					mapList.put("id", pa.getId());
					// code
					mapList.put("code", pa.getCode());
					// 名称
					mapList.put("name", pa.getName());
					if (pa.getValidationRuleId() != null && pa.getValidationRuleId() != 0) {
						ValidationRule validationRule = new ValidationRule();
						validationRule.setId(pa.getValidationRuleId());
						validationRule = validationRuleService.getOne(validationRule);
						if (validationRule != null) {
							// 验证规则内容
							mapList.put("validationRuleValue", validationRule.getRuleContent());
							// 验证失败提示信息
							mapList.put("promptMessage", validationRule.getPromptMessage());
						}
					}
					// 是否允许为空
					mapList.put("isNotNull", pa.getIsNotNull());
					// 是否可用
					mapList.put("isEnabled", pa.getIsEnabled());
					// 是否作为查询条件
					mapList.put("isForQuery", pa.getIsForQuery());
					// 元素类型
					mapList.put("pageShowType", pa.getPageShowType());
					// 排序
					mapList.put("sortNumber", pa.getSortNumber());
					String valueStr = "";
					if (pa.getDefaultValue() != null && !pa.getDefaultValue().equals("")) {
						String type = pa.getDefaultValue().split("_")[0];
						valueStr = type + "_";
						// 来自字典
						if (type.equals("" + Constant.PRODUCT_ATTRIBUTE_SOURCE_DICTIONARY)) {
							DictionaryItem dictionaryItem = new DictionaryItem();
							dictionaryItem.setDictionaryCode(pa.getDefaultValue().split("_")[1]);
							List<DictionaryItem> dictionaryItemList = (List<DictionaryItem>) dictionaryItemService.getList(dictionaryItem);
							if (dictionaryItemList != null) {
								for (int j = 0; j < dictionaryItemList.size(); j++) {
									DictionaryItem dd = dictionaryItemList.get(j);
									valueStr += dd.getItemName() + ":" + dd.getItemValue();
									if (j != dictionaryItemList.size() - 1) {
										valueStr += "*";
									}
								}
							}
						} else if (type.equals("" + Constant.PRODUCT_ATTRIBUTE_SOURCE_LOCAL)) {
							List<Area> areaList = areaService.getList(null);
							for (Area area : areaList) {
								valueStr += area.getId() + ":" + area.getName() + "*";
							}
							if (areaList.size() > 0) {
								valueStr = valueStr.substring(0, valueStr.length() - 1);
							}
						} else if (type.equals("" + Constant.PRODUCT_ATTRIBUTE_SOURCE_DEFINED)) {
							valueStr = pa.getDefaultValue();
						}
					}
					mapList.put("defaultValue", valueStr);
					String productId = entity.getProductId();
					ProductAttributeValue newProductAttrValue = null;
					if ("".equals(productId) || productId != null) {
						// 查询对应的值
						ProductAttributeValue productAttributeValue = new ProductAttributeValue();
						productAttributeValue.setProductId(Long.valueOf(entity.getProductId()));
						productAttributeValue.setAttributeId(pa.getId());
						newProductAttrValue = productAttributeValueService.getOne(productAttributeValue);
					}
					String value = "";
					if (newProductAttrValue != null) {
						String tValue = newProductAttrValue.getValue();
						if(tValue != null){
							value = tValue;
						}
					}
					mapList.put("attributeValue", value);
					list.add(mapList);
				}
			}
		}
		map.put("attributeList", list);
		map.put("listSize", list.size());
		return map;
	}

}
