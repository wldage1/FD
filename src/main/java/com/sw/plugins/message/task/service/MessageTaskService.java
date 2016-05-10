package com.sw.plugins.message.task.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.message.task.entity.MessageReceiverConfig;
import com.sw.plugins.message.task.entity.MessageTask;
import com.sw.plugins.message.task.entity.MessageTemplate;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

public class MessageTaskService extends CommonService<MessageTask> {

	@Resource
	private MessageTemplateService messageTemplateService;
	@Resource
    private UserLoginService userLoginService	;
	
	public void saveOrUpdate(MessageTask entity) throws Exception{
		if(entity.getId() == null){
			save(entity);
		}else{
			update(entity);
		}
		
	}
	
	@Override
	public void save(MessageTask entity) throws Exception {
		getRelationDao().insert("messageTask.insert", entity);
	}

	@Override
	public void update(MessageTask entity) throws Exception {
		getRelationDao().update("messageTask.update", entity);
		
		if(entity.getTemplateType() != null){
			String[] templateType = (entity.getTemplateType()).split(",");
			List<MessageTemplate> templateList = new ArrayList<MessageTemplate>();
			MessageTemplate messageTemplate;
			for(String type : templateType){
				String templatecode = "";
				String content = "";
				messageTemplate = new MessageTemplate();
				messageTemplate.setMessageTaskID(entity.getId());
				messageTemplate.setName(entity.getTemplateName());
				switch (Integer.parseInt(type)) {
				case 1:
					templatecode = entity.getSystemTemplateCode();
					content = entity.getSystemTemplateContent();
					messageTemplate.setId(entity.getSystemTemplateId());
					messageTemplate.setType(Short.parseShort(type));
					messageTemplate.setStatus(entity.getSystemTemplateStatus());
					messageTemplate.setName(entity.getTemplateName());
					messageTemplate.setContent(entity.getSystemTemplateContent());
					messageTemplate.setCode(entity.getSystemTemplateCode());
					templateList.add(messageTemplate);
					break;
				case 2:
					templatecode = entity.getNoteTemplateCode();
					content = entity.getNoteTemplateContent();
					messageTemplate.setId(entity.getNoteTemplateId());
					messageTemplate.setType(Short.parseShort(type));
					messageTemplate.setStatus(entity.getNoteTemplateStatus());
					messageTemplate.setName(entity.getTemplateName());
					messageTemplate.setContent(entity.getNoteTemplateContent());
					messageTemplate.setCode(entity.getNoteTemplateCode());
					templateList.add(messageTemplate);
					break;
				case 3:
					templatecode = entity.getEmailTemplateCode();
					content = entity.getEmailTemplateContent();
					messageTemplate.setId(entity.getEmailTemplateId());
					messageTemplate.setType(Short.parseShort(type));
					messageTemplate.setStatus(entity.getEmailTemplateStatus());
					messageTemplate.setName(entity.getTemplateName());
					messageTemplate.setContent(entity.getEmailTemplateContent());
					messageTemplate.setCode(entity.getEmailTemplateCode());
					templateList.add(messageTemplate);
					break;
				default:
					templatecode = entity.getImTemplateCode();
					content = entity.getImTemplateContent();
					messageTemplate.setId(entity.getImTemplateId());
					messageTemplate.setType(Short.parseShort(type));
					messageTemplate.setStatus(entity.getImTemplateStatus());
					messageTemplate.setName(entity.getTemplateName());
					messageTemplate.setContent(entity.getImTemplateContent());
					messageTemplate.setCode(entity.getImTemplateCode());
					templateList.add(messageTemplate);
					break;
				}
				//保存或更新消息模板
				messageTemplateService.saveOrUpdate(messageTemplate);
				//更新ftl模板文件(改为发送时生成)
				//freemarkerUtil.updateTemplateContent(Constant.APPLICATIONPATH + "template/message", 
				//		templatecode, content);
			}
		}
	}

	@Override
	public Long getRecordCount(MessageTask entity) throws Exception {
		return super.getRelationDao().getCount("messageTask.select_count", entity);
	}

	//判断Code是否重复
	public boolean uniquenessCode(MessageTask entity) throws Exception{
		Long size = super.getRelationDao().getCount("messageTask.select_code_conunt", entity);
		return size == 0 ? false : true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MessageTask> getList(MessageTask entity) throws Exception {
		return (List<MessageTask>) getRelationDao().selectList("messageTask.selectList", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageTask> getPaginatedList(MessageTask entity)
			throws Exception {
		return (List<MessageTask>) getRelationDao().selectList("messageTask.select", entity);
	}
	/**
	 *  获取配置的所有用户
	 *  @param entity
	 *  @return
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-30 上午10:25:41
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	public List<MessageReceiverConfig> getMessageReceiverConfigResult(MessageTask entity)
			throws Exception {
		return (List<MessageReceiverConfig>) getRelationDao().selectList("messageTask.select_message_receiver_config", entity);
	}

	@Override
	public void delete(MessageTask entity) throws Exception {
		if(countMessageTaskSend(entity) <=0){
			super.getRelationDao().delete("messageTask.delete", entity);
		}
		
	}
	public String deleteTask(MessageTask entity) throws Exception {
		String nodeleteId = "";
		if(countMessageTaskSend(entity) <=0){
			super.getRelationDao().delete("messageTask.delete", entity);
		}else{
			nodeleteId = entity.getId() + "";
		}
		return nodeleteId;
		
	}
	/**
	 *  删除任务对应的接收者配置
	 *  @param entity
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-30 下午3:19:29
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	public void deleteConfigReceivers(MessageTask entity) throws Exception {
			super.getRelationDao().delete("messageTask.delete_config_receivers", entity);
	}
	@Override
	public void deleteByArr(MessageTask entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}
	
	/**
	 *  删除任务，并返回已发送消息不能删除的任务id
	 *  @param entity
	 *  @return
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2014-1-17 上午11:00:52
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	public String deleteTaskByArr(MessageTask entity) throws Exception {
		String nodeleteIds = "";
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				String nodeleteid = deleteTask(entity);
				//拼装测试不成功人物id
				if(!nodeleteid.equals("")){
					if(nodeleteIds.equals("")){
						nodeleteIds = nodeleteid;
					}else{
						nodeleteIds += "," + nodeleteid;
					}
				}
			}
		}
		return nodeleteIds;
	}
	
	//获取任务已发送消息数量
	public Long countMessageTaskSend(MessageTask entity) throws Exception{
		return super.getRelationDao().getCount("messageTask.count_message_task_send", entity);
	}

	@Override
	public MessageTask getOne(MessageTask entity) throws Exception {
		entity = (MessageTask) getRelationDao().selectOne("messageTask.select_messageTask", entity);
		MessageTemplate messageTemplate = new MessageTemplate();
		messageTemplate.setMessageTaskID(entity.getId());
		List<MessageTemplate> messageTemplateList = messageTemplateService.getList(messageTemplate);
		String templateType = "";
		//根据任务设置所属模板数据
		for(MessageTemplate mt : messageTemplateList){
			if(templateType == ""){
				entity.setTemplateName(mt.getName());
				templateType += mt.getType();
			}else{
				templateType += ",";
				templateType += mt.getType();
			}
			Long id = mt.getId();
			Short status = mt.getStatus();
			String content = mt.getContent();
			String code = mt.getCode();
			switch (mt.getType()) {
			case 1:
				entity.setSystemTemplateId(id);
				entity.setSystemTemplateStatus(status);
				entity.setSystemTemplateContent(content);
				entity.setSystemTemplateCode(code);
				break;
			case 2:
				entity.setNoteTemplateId(id);
				entity.setNoteTemplateStatus(status);
				entity.setNoteTemplateContent(content);
				entity.setNoteTemplateCode(code);
				break;
			case 3:
				entity.setEmailTemplateId(id);
				entity.setEmailTemplateStatus(status);
				entity.setEmailTemplateContent(content);
				entity.setEmailTemplateCode(code);
				break;
			default:
				entity.setImTemplateId(id);
				entity.setImTemplateStatus(status);
				entity.setImTemplateContent(content);
				entity.setImTemplateCode(code);
				break;
			}
		}
		if(templateType != null)
			entity.setTemplateType(templateType.toString());
		return entity;
	}

	@Override
	public Map<String, Object> getGrid(MessageTask entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<MessageTask> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	@Override
	public String upload(MessageTask entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(MessageTask entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *初始化内置消息任务和模板
	 */
	@Override
	public void init(InitData initData) throws Exception {
		String path =Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("%20", " ") + "messageTemplate.xml";
		File templateXML = new File(path);
		SAXReader saxReader = new SAXReader();
		// 把文件读入到文档
		Document document = saxReader.read(templateXML);
		// 获取文档根节点
		Element root = document.getRootElement();//获得根节点
		//循环消息任务
		for(Iterator iter = root.elementIterator();iter.hasNext();){  
            //获得具体的row元素    
            Element taskElement = (Element)iter.next();  
            //任务code
            String taskCode = taskElement.attributeValue("code");
            //任务名称
            String taskName =taskElement.attributeValue("name");
            //任务发送类型1-立即发送2-定时发送
            Short taskSendType = Short.valueOf(taskElement.attributeValue("sendType"));
            //任务状态1-未启用2-启用3-暂停
            Short taskStatus = Short.valueOf(taskElement.attributeValue("status"));
            //任务类型 ，1内置任务
            Short type =1;
            //保存到消息任务表
            MessageTask messageTask = new MessageTask();
            if(taskElement.attributeValue("isConfigReceiver") != null && "1".equals(taskElement.attributeValue("isConfigReceiver"))){
            	messageTask.setIsConfigReceiver((short)1);
            }else{
            	messageTask.setIsConfigReceiver((short)0);
            }
            //code
            messageTask.setCode(taskCode);
            //任务类型，内置任务
            messageTask.setType((short)1);
            //判断模板是否已经存在
            if(!uniquenessCode(messageTask)){
            	messageTask.setName(taskName);
                messageTask.setSendType(taskSendType);
                messageTask.setStatus(taskStatus);
                messageTask.setType(type);
                //保存到数据库
                save(messageTask);
                List<Element> list =  taskElement.elements("template");
                for(Element templateElement :list ){
                    //获取任务对应的模板
                  //  Element templateElement = taskElement.element("template");
                    //模板名称
                    String templateName = templateElement.attributeValue("name");
                    //模板code
                    String templateCode = templateElement.attributeValue("code");
                    //模板状态0-无效1-有效
                    Short templateStatus = Short.valueOf(templateElement.attributeValue("status"));
                    //模板类型1-站内2-短信3-邮件4-IM
                    Short templateType = Short.valueOf(templateElement.attributeValue("type"));
                    //保存消息模板
                    MessageTemplate  messageTemplate = new MessageTemplate();
                    messageTemplate.setMessageTaskID(messageTask.getGeneratedKey());
                    messageTemplate.setName(templateName);
                    messageTemplate.setCode(templateCode);
                    messageTemplate.setStatus(templateStatus);
                    messageTemplate.setType(templateType);
                    messageTemplate.setContent(templateElement.getTextTrim());
                    getRelationDao().insert("messageTemplate.insert", messageTemplate);
                }
        
            }
        }  
	}
	/**
	 *  根据设置的角色查询角色对应的用户
	 *  @param entity
	 *  @return
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-30 下午3:02:49
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	public List<User> selectMessageConfigUserByOrgidRoleid(User entity)
			throws Exception {
		return (List<User>) getRelationDao().selectList("messageTask.select_message_config_user_by_orgid_roleid", entity);
	}
	/**
	 *  保存消息配置
	 *  @param entity
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-30 下午3:22:55
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	public void saveReceiverConfig(MessageReceiverConfig entity) throws Exception {
		getRelationDao().insert("messageTask.insertReceiverConfig", entity);
	}
	public void saveMessageTaskAndConfig(MessageTask entity ,Map map) throws Exception{
		List<MessageReceiverConfig> configList = new ArrayList();
		String userIDs = "";
		//用户id集合json数组
		if(map.get("receiverUserIDsJSON") != null && !"".equals((String)map.get("receiverUserIDsJSON"))){
			String receiverUserIDsJSON =  (String) map.get("receiverUserIDsJSON");
			JSONArray jsonArray = JSONArray.fromObject(receiverUserIDsJSON);   
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject joo=jsonArray.getJSONObject(i);    //转为json对象
				MessageReceiverConfig messageReceiverConfig = new MessageReceiverConfig();
				messageReceiverConfig.setReceiverID(joo.getLong("receiverID"));
				messageReceiverConfig.setReceiverType(joo.getLong("receiverType"));
				messageReceiverConfig.setMessageTaskID(entity.getId());
				configList.add(messageReceiverConfig);
				if(i==0){
					userIDs += joo.getString("receiverID");
				}else{
					userIDs += "," + joo.getString("receiverID");
				}
			}

		}
		//角色id集合
		if( map.get("receiverRoles") != null && !"".equals((String)map.get("receiverRoles"))){
			String rolesStr = (String) map.get("receiverRoles");
			//设置消息任务配置的角色接收者
			entity.setReceiverRoles(rolesStr);
			String[] roleids = rolesStr.split(",");
			User user = userLoginService.getCurrLoginUser();
			//设置用户角色
			user.setIds(roleids);
			//if(!userIDs.equals("") ){
			   user.setReceiverIDs(userIDs);
			//}
			//查询角色对应用户
			List<User> userListByRole = selectMessageConfigUserByOrgidRoleid(user);
			for(User roleuser : userListByRole){
				MessageReceiverConfig messageReceiverConfig = new MessageReceiverConfig();
				messageReceiverConfig.setReceiverID(roleuser.getId());
				int iscommission = roleuser.getIsCommission();
				//判断是否为居间公司管理员 1为居间公司
				if(iscommission == 0){
					messageReceiverConfig.setReceiverType(1L);
				}else if (iscommission == 1){
					messageReceiverConfig.setReceiverType(4L);
				}
				messageReceiverConfig.setMessageTaskID(entity.getId());
				configList.add(messageReceiverConfig);
			}
		}
		//更新消息任务
		  update(entity);
		//删除消息设置的接收者信息
		  deleteConfigReceivers(entity);
		//保存消息设置信息
		for(MessageReceiverConfig mrc :configList ){
			this.saveReceiverConfig(mrc);
		}
	}
}
