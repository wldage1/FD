package com.sw.plugins.message.manage.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.message.task.entity.ReceiverMessage;

public class ReceiverMessageService extends CommonService<ReceiverMessage> {

	@Override
	public void save(ReceiverMessage entity) throws Exception {
		 super.getRelationDao().insert("receiverMessage.insert", entity);
	}

	@Override
	public void update(ReceiverMessage entity) throws Exception {
		 super.getRelationDao().update("receiverMessage.update", entity);
	}

	@Override
	
	public Long getRecordCount(ReceiverMessage entity) throws Exception {
		return super.getRelationDao().getCount("receiverMessage.select_count", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReceiverMessage> getList(ReceiverMessage entity)
			throws Exception {
		return (List<ReceiverMessage>) getRelationDao().selectList("receiverMessage.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReceiverMessage> getPaginatedList(ReceiverMessage entity)
			throws Exception {
		return (List<ReceiverMessage>) getRelationDao().selectList("receiverMessage.select", entity);
	}

	@Override
	public void delete(ReceiverMessage entity) throws Exception {
		getRelationDao().delete("receiverMessage.delete", entity);
	}

	@Override
	public void deleteByArr(ReceiverMessage entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}

	@Override
	public ReceiverMessage getOne(ReceiverMessage entity) throws Exception {
		return (ReceiverMessage) getRelationDao().selectOne("receiverMessage.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(ReceiverMessage entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<ReceiverMessage> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
    
	/**
	 *  获取我的消息id集合
	 *  @param entity
	 *  @return
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-12-30 上午10:05:38
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	public List<Long> getMyMessageList(ReceiverMessage entity)
			throws Exception {
		return (List<Long>) getRelationDao().selectList("receiverMessage.select_my_message_id", entity);
	}

	@Override
	public String upload(ReceiverMessage entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ReceiverMessage entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
