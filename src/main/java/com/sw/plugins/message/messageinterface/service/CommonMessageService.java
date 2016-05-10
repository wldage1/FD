package com.sw.plugins.message.messageinterface.service;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.sw.core.common.Constant;
import com.sw.core.exception.DetailException;
import com.sw.plugins.message.manage.service.ReceiverMessageService;
import com.sw.plugins.message.send.service.SendMessageService;
import com.sw.plugins.message.task.entity.AppSender;
import com.sw.plugins.message.task.entity.MessageTemplate;
import com.sw.plugins.message.task.entity.ReceiverMessage;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.message.task.service.MessageTemplateService;
import com.sw.util.FreemarkerUtil;
import com.sw.util.SystemProperty;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 *  站内消息实现
 *  @author sean
 *  @version 1.0
 *  </pre>
 *  Created on :下午1:10:15
 *  LastModified:
 *  History:
 *  </pre>
 */
public class CommonMessageService {
	private static Logger logger = Logger.getLogger(CommonMessageService.class);
	@Resource
	private FreemarkerUtil freemarkerUtil;
	@Resource
	private SendMessageService sendMessageService;
	@Resource
	private ReceiverMessageService receiverMessageService;
	@Resource
	private MessageTemplateService messageTemplateService;
	@Resource
	private SendEmailMessageService sendEmailMessageService;
	@Resource
	private SmsMessageService smsMessageService;
	
    //发送消息
	public void send(SendMessage sendMessage)  {
		try {
		/*	MessageTemplate messageTemplate = new MessageTemplate();
			messageTemplate.setCode(sendMessage.getTemplateCode());
			//messageTemplateService.getOne(messageTemplate);
			String templateCode = sendMessage.getTemplateCode();
			//查询模板内容
			String templateContent;
			templateContent = messageTemplateService.getOne(messageTemplate).getContent();
			//模板替换参数
			Map parametersMap = sendMessage.getTemplateParameters();
			//替换模板内容
			String messageContent = replaceTemplate(parametersMap, templateCode, templateContent);
			//获取发送方式id的集合
			String[] sendWays = sendMessage.getSendWayStr().split(",");
			List<UserMessage> userList = sendMessage.getUserList();
			SendMessage send = new SendMessage();*/
			MessageTemplate messageTemplate = new MessageTemplate();
			messageTemplate.setCode(sendMessage.getTemplateCode());
			//messageTemplateService.getOne(messageTemplate);
			String templateCode = sendMessage.getTemplateCode();
			messageTemplate =  messageTemplateService.getOne(messageTemplate);
			String templateContent = "" ;
			if(messageTemplate != null && messageTemplate.getContent() != null){
				//查询模板内容
				templateContent = messageTemplate.getContent();
			}
		
			//模板替换参数
			Map parametersMap = sendMessage.getTemplateParameters();
			//获取发送方式id的集合
			String[] sendWays = sendMessage.getSendWayStr().split(",");
			List<UserMessage> userList = sendMessage.getUserList();
			SendMessage send = new SendMessage();
			for(UserMessage userMessage:userList){
				//发送者ID
				send.setSendUserID(sendMessage.getSendUserID());
				//状态已发送
				send.setStatus((short) 1);
				//接收者ID
				send.setReceiverUserID(userMessage.getUserID());
				//接收者类型
				send.setReceiverUserType(userMessage.getUserType());
				//按照不同的发送方式循环
				for(String way:sendWays){
					//获取用户信息
					Map<String, Object> userMap = sendMessageService.getUserInfo(userMessage.getUserID(),userMessage.getUserType());
					//替换模板内容
					if(userMap != null){
						parametersMap.put("userName", userMap.get("name") == null ? "" :  userMap.get("name"));
					}
					String messageContent = replaceTemplate(parametersMap, templateCode, templateContent);
					//发送邮件
					if("3".equals(way)){
						//邮件主题使用消息任务名称
						Map emailMap = new HashMap<String, Object>();
						if(userMessage.getEmail()!=null && !"".equals(userMessage.getEmail())){
							emailMap.put("receiveUserEmail",userMessage.getEmail());
						}else if(userMap != null){
							emailMap.put("receiveUserEmail",userMap.get("RECEIVEUSEREMAIL") + "");
						}
						emailMap.put("subject", messageTemplate.getTaskName());
						emailMap.put("content", messageContent);
						sendEmailMessageService.sendEmail(emailMap);
					}
					//发送短信
					if("2".equals(way)){
						 AppSender appSender = new AppSender();
						 appSender.setSendTarget(userMap.get("MOBILEPHONE") + "");
						 appSender.setSmContent(messageContent);
						 //不发短信
						 //smsMessageService.sms(appSender);
						
					}
					//设置发送方式
					send.setSendWay(Short.valueOf(way));
				    //保存到发送表
			        sendMessageService.save(send);
			        //保存到接收表
					ReceiverMessage receiverMessage = new ReceiverMessage();
					receiverMessage.setTitle(messageContent);
					//未读
					receiverMessage.setIsReaded((short)0);
					//接收者类型
					receiverMessage.setReceiverUserType(userMessage.getUserType());
					//接收者ID
					receiverMessage.setReceiverUserID(userMessage.getUserID());
					//关联对象类型
					receiverMessage.setType(sendMessage.getRelationType());
					//消息内容
					receiverMessage.setTitle(messageContent);
					//接收者ID
					receiverMessage.setSendUserID(sendMessage.getSendUserID());
					//发送者类型
					receiverMessage.setSendUserType(sendMessage.getSendUserType());
					//消息内容
					receiverMessage.setTitle(messageContent);
					//发送消息ID
					receiverMessage.setSendMessageID(send.getGeneratedKey());
					//发送方式
					receiverMessage.setSendWay(Short.valueOf(way));
					receiverMessageService.save(receiverMessage);
				}
			}
		} catch (Exception e) {
			logger.error("消息发送失败！");
			logger.error(DetailException.expDetail(e, getClass()));
		}
	}
    //替换模板内容
    private String replaceTemplate(Map parameter,String templateCode,String templateContent) throws Exception{
    	/*//创建ftl文件
    	String ftlName = freemarkerUtil.creatTemplate(Constant.APPLICATIONPATH + "template/message", 
    			templateCode, templateContent);
    	Configuration configuration = new Configuration();
    	//获取消息内容
    	configuration.setDirectoryForTemplateLoading(new File(Constant.APPLICATIONPATH + "template/message"));
    	Template template = configuration.getTemplate(ftlName, Constant.ENCODING);
    	StringWriter writer = new StringWriter();
    	//前台地址
    	parameter.put("websiteDomainAddress",SystemProperty.getInstance("config").getProperty("websiteDomainAddress"));
		//后台地址
    	parameter.put("adminDomainAddress", SystemProperty.getInstance("config").getProperty("adminDomainAddress"));
    	template.process(parameter, writer);
    	String content = writer.toString();*/
    	
    	//替换模板内容，不建立ftl文件
    	StringTemplateLoader stringLoader = new StringTemplateLoader();
    	stringLoader.putTemplate(templateCode,templateContent);
    	Configuration configuration = new Configuration(); 
    	configuration.setTemplateLoader(stringLoader);
    	Template template = configuration.getTemplate(templateCode, Constant.ENCODING);
    	StringWriter writer = new StringWriter();
        //前台地址
        parameter.put("websiteDomainAddress",SystemProperty.getInstance("config").getProperty("websiteDomainAddress"));
    	//后台地址
        parameter.put("adminDomainAddress", SystemProperty.getInstance("config").getProperty("adminDomainAddress"));
        template.process(parameter, writer);
        String content = writer.toString();
    	  
        return content;
    }
    /**
     *  根据后台配置的接收者发送
     *  @param sendMessage
     *  @throws Exception
     *  @author sean
     *  @version 1.0
     *  </pre>
     *  Created on :2013-10-29 上午9:44:30
     *  LastModified:
     *  History:
     *  </pre>
     */
    public void sendByConfigReceiver(SendMessage sendMessage)  {
    	MessageTemplate  messageTemplate = new MessageTemplate();
    	messageTemplate.setCode(sendMessage.getTemplateCode());
    	List<UserMessage> userList;
		try {
			userList = messageTemplateService.getConfigReceivers(messageTemplate);
			sendMessage.setUserList(userList);
		} catch (Exception e) {
			logger.error("消息发送失败！");
			logger.error(DetailException.expDetail(e, getClass()));
		}
    	this.send(sendMessage);
    }

}
