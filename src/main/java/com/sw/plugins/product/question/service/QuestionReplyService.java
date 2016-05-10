package com.sw.plugins.product.question.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.question.entity.QuestionReply;

public class QuestionReplyService extends CommonService<QuestionReply>{
	
	@Override
	public void save(QuestionReply entity) throws Exception {
		super.getRelationDao().insert("questionReply.insert", entity);
	}
	
	@Override
	public void update(QuestionReply entity) throws Exception {
		super.getRelationDao().update("questionReply.update_reply", entity);
		
	}

	@Override
	public Long getRecordCount(QuestionReply entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuestionReply> getList(QuestionReply entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuestionReply> getPaginatedList(QuestionReply entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(QuestionReply entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(QuestionReply entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QuestionReply getOne(QuestionReply entity) throws Exception {
		return (QuestionReply)super.getRelationDao().selectOne("questionReply.select_one", entity);
	}

	@Override
	public Map<String, Object> getGrid(QuestionReply entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(QuestionReply entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(QuestionReply entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
