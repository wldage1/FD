package com.sw.plugins.config.dictionary.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.config.dictionary.entity.DictionaryItem;

public class DictionaryItemService extends CommonService<DictionaryItem> {

	@Override
	public Map<String, Object> getGrid(DictionaryItem dictionaryItem) throws Exception {
		List<DictionaryItem> resultList = null;
		dictionaryItem.setDictionaryCode(dictionaryItem.getDictionaryCode());
		resultList = getList(dictionaryItem);
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put("rows", resultList);
		return map;
	}
	
	public Map<String,Object> getKeyValue(DictionaryItem dictionaryItem) throws Exception {
		Map<String,Object> map = new Hashtable<String,Object>();
		List<DictionaryItem> resultList = null;
		resultList = getList(dictionaryItem);
		for(int i = 0;i<resultList.size();i++){
			map.put(resultList.get(i).getItemValue(),resultList.get(i).getItemName());
		}
		return map;
	}
	
	/**
	 * 更具字典类型常量值，获取该字典类型下的字典信息，json格式
	 * 
	 * @param entity
	 * @return
	 * @author Administrator
	 * @version 1.0 </pre> Created on :2012-5-2 下午2:40:26 LastModified: History:
	 *          </pre>
	 * @throws Exception
	 */
	public String getDictJson(DictionaryItem dictionaryItem) throws Exception {

		List<Object> list = new ArrayList<Object>();
		List<DictionaryItem> resultList = getList(dictionaryItem);
		Map<String, Object> map = new Hashtable<String, Object>();
		for (DictionaryItem tempDictionary : resultList) {
			map.put("name", tempDictionary.getItemName());
			map.put("dictSortValue", tempDictionary.getDictionaryCode());
			map.put("id", tempDictionary.getId());
			list.add(map);
		}
		return JSONArray.fromObject(list).toString();
	}

	public Map<Long, DictionaryItem> getDictMap(DictionaryItem dictionaryItem) throws Exception {
		List<DictionaryItem> resultList = getList(dictionaryItem);
		Map<Long, DictionaryItem> map = new Hashtable<Long, DictionaryItem>();
		for (DictionaryItem tempDictionary : resultList) {
			map.put(Long.valueOf(tempDictionary.getItemValue()), tempDictionary);
		}
		return map;
	}
	
	
	/**
	 * 统计字典类型下的字典值数量
	 * @param dictionaryItem
	 * @return
	 * @throws Exception
	 */
	public Long getDictionaryCountForSort(DictionaryItem dictionaryItem) throws Exception{
		return super.getRelationDao().getCount("dictionaryItem.select_count_for_sort", dictionaryItem);
	}
	
	public Long checkForDupliName(DictionaryItem dictionaryItem) throws Exception{
		return super.getRelationDao().getCount("dictionaryItem.count_check_itemname", dictionaryItem);
	}
	
	public Long checkForDupliValue(DictionaryItem dictionaryItem) throws Exception{
		return super.getRelationDao().getCount("dictionaryItem.count_check_itemvalue", dictionaryItem);
	}


	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String upload(DictionaryItem entity, HttpServletRequest request) throws Exception {
		return null;

	}

	@Override
	public void save(DictionaryItem entity) throws Exception {
		super.getRelationDao().insert("dictionaryItem.insert", entity);
	}

	@Override
	public void update(DictionaryItem entity) throws Exception {
		super.getRelationDao().update("dictionaryItem.update", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DictionaryItem> getList(DictionaryItem entity) throws Exception {
		return (List<DictionaryItem>) super.getRelationDao().selectList("dictionaryItem.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DictionaryItem> getPaginatedList(DictionaryItem entity) throws Exception {
		return (List<DictionaryItem>) super.getRelationDao().selectList("dictionaryItem.select", entity);
	}

	@Override
	public void delete(DictionaryItem entity) throws Exception {
		super.getRelationDao().delete("dictionaryItem.delete", entity);
	}

	@Override
	public void deleteByArr(DictionaryItem entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				super.getRelationDao().delete("dictionaryItem.delete", entity);
			}
		}
	}

	@Override
	public DictionaryItem getOne(DictionaryItem entity) throws Exception {
		return (DictionaryItem) super.getRelationDao().selectOne("dictionaryItem.select", entity);
	}

	@Override
	public Long getRecordCount(DictionaryItem entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("dictionaryItem.count", entity);
	}

	@Override
	public Object download(DictionaryItem entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getListMap(DictionaryItem entity)throws Exception{
		return (List<Map<String,Object>>)super.getRelationDao().selectList("dictionaryItem.selectMap", entity);
	}
	
}
