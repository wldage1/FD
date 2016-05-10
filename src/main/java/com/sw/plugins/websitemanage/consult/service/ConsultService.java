package com.sw.plugins.websitemanage.consult.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.question.entity.Question;

public class ConsultService extends CommonService<Question>{

	@Override
	public void save(Question entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Question entity) throws Exception {
		super.getRelationDao().update("question.isReleased", entity);
	}
	
	@Override
	public Long getRecordCount(Question entity) throws Exception {
		return super.getRelationDao().getCount("question.web_undisposed_count", entity);
	}
	
	public Long getDisposedRecordCount(Question entity) throws Exception{
		return super.getRelationDao().getCount("question.web_disposed_count", entity);
	}
	
	@Override
	public List<Question> getList(Question entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	//获取未处理网站咨询列表数据
	@SuppressWarnings("unchecked")
	@Override
	public List<Question> getPaginatedList(Question entity) throws Exception {
		return (List<Question>)super.getRelationDao().selectList("question.select_web_undisposed", entity);
	}
	//获取已处理网站咨询列表数据
	@SuppressWarnings("unchecked")
	public List<Question> getDisposedPaginatedList(Question entity) throws Exception{
		return (List<Question>)super.getRelationDao().selectList("question.select_web_disposed", entity);
	}
	
	@Override
	public void delete(Question entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(Question entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Question getOne(Question entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(Question entity) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		List<Question> questionList = new ArrayList<Question>();
		questionList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", questionList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	public Map<String,Object> getDisposedGrid(Question entity) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		List<Question> questionList = new ArrayList<Question>();
		questionList = getDisposedPaginatedList(entity);
		Long record = getDisposedRecordCount(entity);
		int pageCount = (int)Math.ceil(record / (double) entity.getRows());
		map.put("rows", questionList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	@Override
	public String upload(Question entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(Question entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
