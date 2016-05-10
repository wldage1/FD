package com.sw.plugins.message.task.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.message.task.entity.MessageTemplate;
import com.sw.plugins.message.task.entity.UserMessage;

public class MessageTemplateService extends CommonService<MessageTemplate> {

	public void saveOrUpdate(MessageTemplate entity) throws Exception{
		if(entity.getId() == null){
			save(entity);
		}else{
			update(entity);
		}
	}
	
	@Override
	public void save(MessageTemplate entity) throws Exception { 
		getRelationDao().insert("messageTemplate.insert", entity);
	}

	@Override
	public void update(MessageTemplate entity) throws Exception {
		getRelationDao().update("messageTemplate.update", entity);
	}

	@Override
	public Long getRecordCount(MessageTemplate entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageTemplate> getList(MessageTemplate entity)
			throws Exception {
		return (List<MessageTemplate>)  getRelationDao().selectList("messageTemplate.select", entity);
	}

	@Override
	public List<MessageTemplate> getPaginatedList(MessageTemplate entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(MessageTemplate entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(MessageTemplate entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MessageTemplate getOne(MessageTemplate entity) throws Exception {
		return (MessageTemplate)getRelationDao(). selectOne("messageTemplate.selectByOne", entity);
	}
     
	/**
	 *  查询消息任务设置的接收者 
	 *  @param entity
	 *  @return
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-28 下午3:50:12
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	public List<UserMessage> getConfigReceivers(MessageTemplate entity)throws Exception {
		List list =getRelationDao().selectList("messageTask.select_config_receivers", entity);
		return list;
	}
	
	@Override
	public Map<String, Object> getGrid(MessageTemplate entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(MessageTemplate entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(MessageTemplate entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
