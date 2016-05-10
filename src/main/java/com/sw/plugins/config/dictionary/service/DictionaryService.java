package com.sw.plugins.config.dictionary.service;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.config.dictionary.entity.Dictionary;
import com.sw.plugins.config.dictionary.entity.DictionaryItem;

/**
 * Service实现类 -
 */

public class DictionaryService extends CommonService<Dictionary> {

    
	/** 初始化数据字典 */
	@Override
	public void init(InitData initData) throws Exception {
		Dictionary dictionary = new Dictionary();
		//获取产品的字典配置
		Map<String, String> productDictionaryConfig = initData.getDictionaryConfig();
		for(String key : productDictionaryConfig.keySet()) {
			dictionary.setName(productDictionaryConfig.get(key));
			dictionary.setCode(key);
			//删除字典code对应的字典条目
			DictionaryItem dictionaryItem = new DictionaryItem();
			dictionaryItem.setDictionaryCode(key);
			deleteDictionaryItemsByCode(dictionaryItem);
			//级联删除字典和字典条目表
			this.delete(dictionary);
			this.save(dictionary);
			//动态拼装获取字典设置的方法
			String methodNameStr = "get" + key.substring(0,1).toUpperCase() + key.substring(1);
			Method method = initData.getClass().getMethod(methodNameStr);
			//执行方法，获取配置
			@SuppressWarnings("unchecked")
			Map<String, Object> config = (Map<String, Object>) method.invoke(initData);
			saveDictionaryItems(config,dictionary);
		}
	}
	/**
	 *  保存字典条目
	 *  @param map
	 *  @param dictionary
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-8-20 下午4:44:31
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	private void saveDictionaryItems(Map<String, Object> map,Dictionary dictionary) throws Exception{
		DictionaryItem dictionaryItem = new DictionaryItem();
		dictionaryItem.setDictionaryCode(dictionary.getCode());
		for(String key : map.keySet()) {
			dictionaryItem.setDictionaryId(dictionary.getGeneratedKey());
			dictionaryItem.setDictionaryCode(dictionary.getCode());
			//字典条目名称
			dictionaryItem.setItemName(map.get(key) + "");
			dictionaryItem.setItemValue(key);
			//保存条目
			this.insertDictionaryItem(dictionaryItem);
		}
	}

	@Override
	public Map<String, Object> getGrid(Dictionary entity) throws Exception {
		Dictionary dictionary = new Dictionary();
		// 取所有字典类型
		List<Dictionary> dictionaryList = (List<Dictionary>) getList(dictionary);
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put("rows", dictionaryList);
		return map;
	}

	public void saveOrUpdate(Dictionary dictionatysort) throws Exception {
		dictionatysort.setName(dictionatysort.getName().trim());
		dictionatysort.setCode(dictionatysort.getCode().trim());
		if (dictionatysort.getId() != null) {
			update(dictionatysort);
		} else {
			save(dictionatysort);
		}
	}

	public Long checkDupicate(Dictionary dictionary) throws Exception {
		return (Long) super.getRelationDao().getCount("dictionary.count_for_duplicate", dictionary);
	}
	

	@Override
	public String upload(Dictionary entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void save(Dictionary entity) throws Exception {
		super.getRelationDao().insert("dictionary.insert", entity);
	}
	public void insertDictionaryItem(DictionaryItem entity) throws Exception {
		super.getRelationDao().insert("dictionary.insertDictionaryItem", entity);
	}

	@Override
	public void update(Dictionary entity) throws Exception {
		super.getRelationDao().update("dictionary.update", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dictionary> getList(Dictionary entity) throws Exception {
		return (List<Dictionary>) super.getRelationDao().selectList("dictionary.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dictionary> getPaginatedList(Dictionary entity) throws Exception {
		return (List<Dictionary>) super.getRelationDao().selectList("dictionary.select", entity);
	}

	@Override
	public void delete(Dictionary entity) throws Exception {
		super.getRelationDao().delete("dictionary.delete", entity);
	}
	@Override
	public void deleteByArr(Dictionary entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				super.getRelationDao().delete("dictionary.delete", entity);
			}
		}
	}

	@Override
	public Dictionary getOne(Dictionary entity) throws Exception {
		return (Dictionary) super.getRelationDao().selectOne("dictionary.select", entity);
	}

	@Override
	public Long getRecordCount(Dictionary entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("dictionary.count", entity);
	}
	/**
	 *  根据code删除字典条目（系统启动时调用）
	 *  @param dictionaryItem
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-8-27 下午1:32:22
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	public void deleteDictionaryItemsByCode(DictionaryItem dictionaryItem) throws Exception {
		super.getRelationDao().delete("dictionary.delete_dictionary_items_by_code", dictionaryItem);
	}

	@Override
	public Object download(Dictionary entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}