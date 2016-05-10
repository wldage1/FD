package com.sw.plugins.message.send.service;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.message.manage.service.ReceiverMessageService;
import com.sw.plugins.message.messageinterface.service.SendEmailMessageService;
import com.sw.plugins.message.messageinterface.service.SmsMessageService;
import com.sw.plugins.message.task.entity.AppSender;
import com.sw.plugins.message.task.entity.ReceiverMessage;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

public class SendMessageService extends CommonService<SendMessage> {
	private static Logger logger = Logger.getLogger(SendMessageService.class);
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private MemberService memberService;
	@Resource
	private ReceiverMessageService receiverMessageService;
	@Resource
	private SendEmailMessageService sendEmailMessageService;
	@Resource
	private SmsMessageService smsMessageService;
	
	/**
	 *  根据选择的用户发送消息
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-8 上午10:44:59
	 *  LastModified:
	 *  History:
	 *  </pre>
	 * @throws Exception 
	 */
	public void saveSendMessage(Map map) throws Exception{
		String messageTaskID = (String) map.get("messageTaskID");
		String ids = (String)map.get("ids");
		String content = (String)map.get("content");
		 JSONObject jsonObject = JSONObject.fromObject(content);
		JSONArray jsonArray = JSONArray.fromObject(ids); 
		User user = userLoginService.getCurrLoginUser();
		 for (int i = 0; i < jsonArray.size(); i++) {
			 SendMessage sendMessage = new SendMessage();
			 String memberID = jsonArray.getString(i);
			 sendMessage.setMessageTaskID(Long.valueOf(messageTaskID));
			 sendMessage.setSendUserID(user.getId());
			 //状态，已发送
			 sendMessage.setStatus((short) 1);
			 sendMessage.setReceiverUserID(Long.valueOf(memberID));
			 //接收者类型，理财师
			 sendMessage.setReceiverUserType((short) 2);
			 //设置发送内容
			 Iterator<String> it = jsonObject.keys(); 
			 while (it.hasNext()) {  
				String key = (String) it.next();  
	            String value = jsonObject.getString(key); 
	            //发送邮件
	            Map<String, Object> userMap = getUserInfo(Long.valueOf(memberID),(short)3);
	            if("3".equals(key)){
	            	//邮件主题使用消息任务名称
	            	Map emailMap = new HashMap<String, Object>();
	            	
	            	if(userMap != null){
	            		//emailMap.put("receiveUserEmail",userMap.get("RECEIVEUSEREMAIL") + "");
	            	}
	            	emailMap.put("receiveUserEmail","84666658@qq.com");
	            	emailMap.put("subject", value);
	            	emailMap.put("content", value);
	            	sendEmailMessageService.sendEmail(emailMap);
	            }
	            if("2".equals(key)){
					 AppSender appSender = new AppSender();
					 appSender.setSendTarget(userMap.get("MOBILEPHONE") + "");
					 appSender.setSmContent(value);
					 smsMessageService.sms(appSender);
					
				}
	            sendMessage.setContent(value);
	            sendMessage.setSendWay(Short.parseShort(key));
	            //保存到发送表
	           save(sendMessage);
	           //保存到接收表
	       	   ReceiverMessage receiverMessage = new ReceiverMessage();
	       	   //理财师
	       	   receiverMessage.setReceiverUserType((short)3);
	       	   //消息内容
	       	   receiverMessage.setTitle(value);
	       	   //状态，未读
	       	   receiverMessage.setIsReaded((short)0);
	       	   receiverMessage.setSendUserType(Short.valueOf(user.getType() + ""));
	       	   receiverMessage.setReceiverUserID(Long.valueOf(memberID));
	       	   receiverMessage.setSendUserID(user.getId());
	       	   receiverMessage.setSendMessageID(sendMessage.getGeneratedKey());
	       	   receiverMessage.setSendWay(sendMessage.getSendWay());
	       	   receiverMessageService.save(receiverMessage);
			 }
		 }
	}
	/**
	 *  根据查询条件发送
	 *  @param map
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-8 下午10:54:52
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	public void saveSendMessageBySearch(Map map) throws Exception{
		String messageTaskID = (String) map.get("messageTaskID");
		String content = (String)map.get("content");
		String searchCondition = (String)map.get("searchCondition");
		Member member = new Member();
		JSONObject jsonObject = JSONObject.fromObject(searchCondition);
		if(jsonObject.getString("name") != null && !jsonObject.getString("name").equals("")){
			member.setName(jsonObject.getString("name"));
		}
		if(jsonObject.getString("status") != null && !jsonObject.getString("status").equals("")){
			member.setStatus(Integer.valueOf(jsonObject.getString("status")));
		}
		if(jsonObject.getString("orgID") != null && !jsonObject.getString("orgID").equals("0")){
			member.setOrgID(Long.valueOf(jsonObject.getString("orgID")));
		}
		if(jsonObject.getString("teamID") != null && !jsonObject.getString("teamID").equals("")){
			member.setTeamID(Integer.valueOf(jsonObject.getString("teamID")));
		}
		if(jsonObject.getString("province") != null && !jsonObject.getString("province").equals("")){
			member.setProvince(Integer.valueOf(jsonObject.getString("province")));
		}
		if(jsonObject.getString("city") != null && !jsonObject.getString("city").equals("")){
			member.setCity(Integer.valueOf(jsonObject.getString("city")));
		}
		if(jsonObject.getString("gender") != null && !jsonObject.getString("gender").equals("")){
			member.setGender(Integer.valueOf(jsonObject.getString("gender")));
		}
		List<Member> memberList = memberService.selectSendMessageMemberBySearch(member);
		User user = userLoginService.getCurrLoginUser();
		JSONObject contentjsonObject = JSONObject.fromObject(content);

		for (Member m:memberList) {
			SendMessage sendMessage = new SendMessage();
			String memberID = m.getId()+"";
			sendMessage.setMessageTaskID(Long.valueOf(messageTaskID));
			sendMessage.setSendUserID(user.getId());
			sendMessage.setStatus((short) 0);
			sendMessage.setReceiverUserID(Long.valueOf(memberID));
			sendMessage.setReceiverUserType((short) 2);
			 Iterator<String> it = contentjsonObject.keys(); 
			 while (it.hasNext()) {  
				String key = (String) it.next();  
	            String value = contentjsonObject.getString(key); 
	            //发送邮件
	            Map<String, Object> userMap = getUserInfo(Long.valueOf(memberID),(short)3);
	            if("3".equals(key)){
	            	//邮件主题使用消息任务名称
	            	Map emailMap = new HashMap<String, Object>();
	            	
	            	if(userMap != null){
	            		emailMap.put("receiveUserEmail",userMap.get("RECEIVEUSEREMAIL") + "");
	            	}
	            	emailMap.put("subject", value);
	            	emailMap.put("content", value);
	            	sendEmailMessageService.sendEmail(emailMap);
	            }
	            if("2".equals(key)){
					 AppSender appSender = new AppSender();
					 appSender.setSendTarget(userMap.get("MOBILEPHONE") + "");
					 appSender.setSmContent(value);
					 smsMessageService.sms(appSender);
					
				}
	            sendMessage.setContent(value);
	            sendMessage.setSendWay(Short.parseShort(key));
	            //保存到发送表
	           save(sendMessage);
	           //保存到接收表
	       	   ReceiverMessage receiverMessage = new ReceiverMessage();
	       	   //理财师
	       	   receiverMessage.setReceiverUserType((short)3);
	       	   //消息内容
	       	   receiverMessage.setTitle(value);
	       	   //状态，未读
	       	   receiverMessage.setIsReaded((short)0);
	       	   receiverMessage.setSendUserType(Short.valueOf(user.getType() + ""));
	       	   receiverMessage.setReceiverUserID(Long.valueOf(memberID));
	       	   receiverMessage.setSendUserID(user.getId());
	       	   receiverMessage.setSendMessageID(sendMessage.getGeneratedKey());
	       	   receiverMessage.setSendWay(sendMessage.getSendWay());
	       	   receiverMessageService.save(receiverMessage);
			 }
		}
	}
	
	/**
	 *  发送给所有理财师
	 *  @param map
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-8 下午11:14:31
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	public void saveSendMessageAllMemer(Map map) throws Exception{
		String messageTaskID = (String) map.get("messageTaskID");
		String content = (String)map.get("content");
		Member member = new Member();
		//获取所有理财师
		List<Member> memberList = memberService.selectSendMessageAllMember(member);
		User user = userLoginService.getCurrLoginUser();
		JSONObject contentjsonObject = JSONObject.fromObject(content);
		for (Member m:memberList) {
			SendMessage sendMessage = new SendMessage();
			String memberID = m.getId()+"";
			sendMessage.setMessageTaskID(Long.valueOf(messageTaskID));
			sendMessage.setSendUserID(user.getId());
			sendMessage.setStatus((short) 0);
			sendMessage.setReceiverUserID(Long.valueOf(memberID));
			sendMessage.setReceiverUserType((short) 2);
			//获取发送内容json
			 Iterator<String> it = contentjsonObject.keys(); 
			 while (it.hasNext()) {  
				String key = (String) it.next();  
	            String value = contentjsonObject.getString(key); 
	            //发送邮件
	            Map<String, Object> userMap = getUserInfo(Long.valueOf(memberID),(short)3);
	            if("3".equals(key)){
	            	//邮件主题使用消息任务名称
	            	Map emailMap = new HashMap<String, Object>();
	            	
	            	if(userMap != null){
	            		emailMap.put("receiveUserEmail",userMap.get("RECEIVEUSEREMAIL") + "");
	            	}
	            	emailMap.put("subject", value);
	            	emailMap.put("content", value);
	            	sendEmailMessageService.sendEmail(emailMap);
	            }
	            //短信
	            if("2".equals(key)){
					 AppSender appSender = new AppSender();
					 appSender.setSendTarget(userMap.get("MOBILEPHONE") + "");
					 appSender.setSmContent(value);
					 smsMessageService.sms(appSender);
					
				}
	            sendMessage.setContent(value);
	            sendMessage.setSendWay(Short.parseShort(key));
	            //保存到发送表
	           save(sendMessage);
	           //保存到接收表
	       	   ReceiverMessage receiverMessage = new ReceiverMessage();
	       	   //理财师
	       	   receiverMessage.setReceiverUserType((short)3);
	       	   //消息内容
	       	   receiverMessage.setTitle(value);
	       	   //状态，未读
	       	   receiverMessage.setIsReaded((short)0);
	       	   receiverMessage.setSendUserType(Short.valueOf(user.getType() + ""));
	       	   receiverMessage.setReceiverUserID(Long.valueOf(memberID));
	       	   receiverMessage.setSendUserID(user.getId());
	       	   receiverMessage.setSendMessageID(sendMessage.getGeneratedKey());
	       	   receiverMessage.setSendWay(sendMessage.getSendWay());
	       	   receiverMessageService.save(receiverMessage);
			 }
		}
	}
	/**
	 *消息明细
	 */
	public Map<String, Object> getGrid(SendMessage entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<SendMessage> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	@Override
	public void save(SendMessage entity) throws Exception {
		this.getRelationDao().insert("sendMessage.insert", entity);
		
	}
	//会员信息
	public Map<String, Object> selectMemberInfo(Object entity) throws Exception {	
		return (Map<String, Object>)super.getRelationDao().selectOne("sendMessage.selectMemberInfo", entity);	    
	}
	//后台用户信息
	public Map<String, Object> selectUserInfo(Object entity) throws Exception {	
		return (Map<String, Object>)super.getRelationDao().selectOne("sendMessage.selectUserInfo", entity);	    
	}
	//供应商用户信息
	public Map<String, Object> selectProviderUserInfo(Object entity) throws Exception {	
		return (Map<String, Object>)super.getRelationDao().selectOne("sendMessage.selectUserInfo", entity);	    
	}
	@Override
	public void update(SendMessage entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getRecordCount(SendMessage entity) throws Exception {

		return super.getRelationDao().getCount("sendMessage.count_task_message_detail", entity);
	}

	@Override
	public List<SendMessage> getList(SendMessage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SendMessage> getPaginatedList(SendMessage entity)
			throws Exception {
		return (List<SendMessage>) getRelationDao().selectList("sendMessage.select_task_message_detail", entity);
	}

	@Override
	public void delete(SendMessage entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(SendMessage entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SendMessage getOne(SendMessage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String upload(SendMessage entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(SendMessage entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}
    public Map<String, Object> getUserInfo (Long userID,Short userType){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userID);
		try {
			if (userType == 1 || userType == 4) {
				map = selectUserInfo(map);
			} else if (userType == 2) {
				map = selectProviderUserInfo(map);
			} else if (userType == 3) {
				map = selectMemberInfo(map);
			  
			}
		} catch (Exception e) {
			logger.error("获取用户信息失败！");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return map;
	}

}
