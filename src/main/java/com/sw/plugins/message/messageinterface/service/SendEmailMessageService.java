package com.sw.plugins.message.messageinterface.service;


import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
/**
 *  发送邮件接口
 *  @author wanghui
 *  @version 1.0
 *  </pre>
 *  Created on :下午15:10:15
 *  LastModified:
 *  History:
 *  </pre>
 */
public class SendEmailMessageService {
	
	@Resource
    public JavaMailSenderImpl mailSender;
	
	/**
	 *  发送邮件接口  返回json格式
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void  sendEmail(Object entity){  
		 JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();  
	     MimeMessage mailMessage = senderImpl.createMimeMessage();
		 MimeMessageHelper mail;
		try {
			 Map<String, Object> map =(Map<String, Object>)entity;	
			 mail = new MimeMessageHelper(mailMessage,true,"utf-8");	
			 String receiveUserEmail=map.get("receiveUserEmail")==null?"":map.get("receiveUserEmail").toString();//接受者  
			 String subject=map.get("subject")==null?"":map.get("subject").toString();//主题  
			 String content=map.get("content")==null?"":map.get("content").toString();//内容
			 mail.setTo(receiveUserEmail);  
			 mail.setFrom(mailSender.getUsername());
			 mail.setSubject(subject);
			 mail.setText(content,true);//邮件内容 
			 if(!receiveUserEmail.equals(""))
				 mailSender.send(mailMessage);  
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
}
