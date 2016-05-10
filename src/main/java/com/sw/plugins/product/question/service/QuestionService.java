package com.sw.plugins.product.question.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.question.entity.Question;
import com.sw.plugins.product.question.entity.QuestionReply;

public class QuestionService extends CommonService<Question>{
	
	@Resource
	private QuestionReplyService replyService;
	
	@Override
	public void save(Question entity) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void update(Question entity) throws Exception {
		super.getRelationDao().update("question.update", entity);
	}
	
	public void updatestatus(Question entity)throws Exception{
		super.getRelationDao().update("question.update_status", entity);
	}
	
	public void updateIsReleased(Question entity) throws Exception {
		super.getRelationDao().update("question.update_repeal", entity);
	}
	
	public void updatePublish(Question entity)throws Exception{
		super.getRelationDao().update("question.isReleased", entity);
	}
	
	@Override
	public Long getRecordCount(Question entity) throws Exception {
		return super.getRelationDao().getCount("question.product_undisposed_count", entity);
	}
	
	public Long getPendingRecordCount(Question entity) throws Exception{
		return super.getRelationDao().getCount("question.product_pending_count", entity);
	}
	
	//已处理产品
	public Long getDisposedRecordCount(Question entity) throws Exception {
		return super.getRelationDao().getCount("question.product_disposed_count", entity);
	}
	
	
	
	@Override
	public List<Question> getList(Question entity) throws Exception {
		return null;
	}
	
	
	//未处理产品查询分页方法
	@SuppressWarnings("unchecked")
	@Override
	public List<Question> getPaginatedList(Question entity) throws Exception {
		return (List<Question>)super.getRelationDao().selectList("question.select_product_undisposed", entity);
	}
	//已处理产品查询分页方法
	@SuppressWarnings("unchecked")
	public List<Question> getDisposedList(Question entity) throws Exception {
		return (List<Question>)super.getRelationDao().selectList("question.select_product_disposed", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<Question> getPendingList(Question entity) throws Exception {
		return (List<Question>)super.getRelationDao().selectList("question.select_product_pending",entity);
	}
	
	@Override
	public void delete(Question entity) throws Exception {
		super.getRelationDao().delete("question.delete", entity);
	}

	@Override
	public void deleteByArr(Question entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Question getOne(Question entity) throws Exception {
		return (Question)super.getRelationDao().selectOne("question.select_one_product", entity);
	}
	
	public void saveReply(Question entity) throws Exception{
		entity.setSolved((short)1);
		this.updatestatus(entity);
		QuestionReply reply = new QuestionReply();
		reply.setQuestionID(entity.getId());
		reply.setReply(entity.getReply());
		replyService.save(reply);
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
	
	public Map<String, Object> getDisposedGrid(Question entity) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		List<Question> questionList = new ArrayList<Question>();
		questionList = getDisposedList(entity);
		Long record = getDisposedRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", questionList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	public Map<String, Object> getPendingGrid(Question entity) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		List<Question> questionList = new ArrayList<Question>();
		questionList = getPendingList(entity);
		Long record = getPendingRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
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
