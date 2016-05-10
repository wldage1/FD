package com.sw.plugins.system.log.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.service.CommonService;
import com.sw.plugins.system.log.entity.Log;

/**
 * 类简要说明
 * 
 * @author Administrator
 * @version 1.0 </pre> Created on :下午02:21:17 LastModified: History: </pre>
 */
public class LogService extends CommonService<Log> {

	/**
	 * 获取日志MAP集合
	 */
	public Map<String, Object> getGrid(Log log) throws Exception {
		//List<Object> list = new ArrayList<Object>();
		List<Log> resultList = null;
		Map<String, Object> map = new Hashtable<String, Object>();
		resultList = getPaginatedList(log);

		// 记录数
		long record = this.getRecordCount(log);
		// 页数
		int pageCount = (int) Math.ceil(record / (double) log.getRows());

		map.put("rows", resultList);
		map.put("page", log.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	@Override
	public void init(com.sw.core.initialize.InitData initData) throws Exception {

	}

	@Override
	public String upload(Log entity, HttpServletRequest request) throws Exception {
		return null;
		// TODO Auto-generated method stub

	}

	@Override
	public Object download(Log entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void save(Log entity) throws Exception {
		super.getRelationDao().insert("log.insert", entity);
	}

	@Override
	public void update(Log entity) throws Exception {
		super.getRelationDao().update("log.update", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Log> getList(Log entity) throws Exception {
		return (List<Log>) super.getRelationDao().selectList("log.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Log> getPaginatedList(Log entity) throws Exception {
		return (List<Log>) super.getRelationDao().selectList("log.select", entity);
	}

	@Override
	public void delete(Log entity) throws Exception {
		super.getRelationDao().delete("log.delete", entity);
	}

	@Override
	public void deleteByArr(Log entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				super.getRelationDao().delete("log.delete", entity);
			}
		}
	}

	@Override
	public Log getOne(Log entity) throws Exception {
		return (Log) super.getRelationDao().selectOne("log.select", entity);
	}

	@Override
	public Long getRecordCount(Log entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("log.count", entity);
	}
}