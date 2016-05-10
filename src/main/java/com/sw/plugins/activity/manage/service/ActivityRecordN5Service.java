package com.sw.plugins.activity.manage.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.activity.manage.entity.ActivityRecord;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

/**
 * 活动统计Service
 * 
 * @author Erun
 */
public class ActivityRecordN5Service extends CommonService<ActivityRecord>{
	
	@Resource
	private CommonMessageService commonMessageService;
	@Resource
	private UserLoginService userLoginService;
	
	/**
	 * 获取N5活动统计集合
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> grid(ActivityRecord entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		entity.setActivityEndTime(dateHandle(entity.getActivityEndTime(), 1));
		List<ActivityRecord> resultList = getPaginatedList(entity);
		for (ActivityRecord activityRecord : resultList) {
			if(activityRecord.getReserved44()==1 && activityRecord.getReserved38()==0){
				activityRecord.setActivityStartTime(entity.getActivityStartTime());
				activityRecord.setActivityEndTime(entity.getActivityEndTime());
				activityRecord.setReserved33(dateHandle(activityRecord.getActivityEndTime(), 7));
				String orderTime = getOrderTime(activityRecord);
				if(orderTime!=null){
					activityRecord.setReserved38(1L);
					activityRecord.setReserved3(String.valueOf((int)Math.pow(activityRecord.getReserved42(), 3)));
					activityRecord.setReserved28(orderTime);
				}
				if(activityRecord.getReserved37()==0){
					activityRecord.setReserved37(1L);
					activityRecord.setReserved2(String.valueOf((int)Math.pow(activityRecord.getReserved42(), 2)));
					activityRecord.setReserved27(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				}
				update(activityRecord);
			}
		}
		Long record = getRecordCount(entity);
		int pageCount = (int)Math.ceil(record/(double)entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 查询理财师第一笔成功订单时间
	 * 
	 * @param activityRecord
	 * @return
	 * @throws Exception
	 */
	public String getOrderTime(ActivityRecord activityRecord) throws Exception{
		String orderTime = (String) getRelationDao().selectOne("activityRecordN5.select_order_time", activityRecord);
		if(orderTime == null){
			orderTime = (String) getRelationDao().selectOne("activityRecordN5.select_order_history_time", activityRecord);
		}
		return orderTime;
	}
	
	public void update(ActivityRecord entity) throws Exception {
		getRelationDao().update("activityRecordN5.update", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivityRecord> getPaginatedList(ActivityRecord entity)
			throws Exception {
		return (List<ActivityRecord>) getRelationDao().selectList("activityRecordN5.select_paginated_list", entity);
	}
	
	public Long getRecordCount(ActivityRecord entity) throws Exception {
		return getRelationDao().getCount("activityRecordN5.select_paginated_count", entity);
	}
	
	/**
	 * 验证当前时间是否超过活动截止日期
	 * 
	 * @return
	 * @throws ParseException
	 */
	public int checkTime(ActivityRecord activityRecord) throws ParseException {
		String endTime = dateHandle(activityRecord.getActivityEndTime(), 7);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = formatter.parse(formatter.format(new Date()));
		Date date2 =formatter.parse(endTime);
		return date.compareTo(date2);
	}
	
	/**
	 * 统计N4N5奖项
	 * 
	 * @param activity
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void countN4N5(ActivityRecord entity) throws Exception {
		entity.setActivityEndTime(dateHandle(entity.getActivityEndTime(), 1));
		entity.setReserved33(dateHandle(entity.getActivityEndTime(), 7));
		List<ActivityRecord> listN4 = (List<ActivityRecord>) getRelationDao().selectList("activityRecordN5.select_n4", entity);
		for (ActivityRecord activityRecord : listN4) {
			activityRecord.setReserved39(1L);
			activityRecord.setReserved4(String.valueOf((int)Math.pow(activityRecord.getReserved42(), 4)));
			activityRecord.setReserved29(entity.getReserved33());
			updateN4N5(activityRecord);
		}
		List<ActivityRecord> listN5 = (List<ActivityRecord>) getRelationDao().selectList("activityRecordN5.select_n5", entity);
		for (ActivityRecord activityRecord : listN5) {
			activityRecord.setReserved40(1L);
			activityRecord.setReserved5(String.valueOf((int)Math.pow(activityRecord.getReserved42(), 5)));
			activityRecord.setReserved30(entity.getReserved33());
			updateN4N5(activityRecord);
		}
		entity.setActivityEndTime(dateHandle(entity.getActivityEndTime(), -1));
	}
	
	public void updateN4N5(ActivityRecord entity) throws Exception {
		getRelationDao().update("activityRecordN5.update_n4_n5", entity);
	}
	
	public String dateHandle(String sdate, int count) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(sdate);
		Calendar c = Calendar.getInstance(); // 初始化一个Calendar
		c.setTime(date); // 设置基准日期
		c.add(Calendar.DATE, count); //你要加减的日期
		date = c.getTime(); // 从Calendar对象获得基准日期增减后的日期
		return sdf.format(date);
	}
	
	public void sendMessage(ActivityRecord activityRecord) {
		SendMessage sm = new SendMessage();
		//接收者LIST
		List<UserMessage> receiver = new ArrayList<UserMessage>();
		UserMessage us = new UserMessage();
		us.setUserID(activityRecord.getJoinId());
		us.setUserType((short)3);
		us.setUserName(activityRecord.getMemberName());
		receiver.add(us);
		sm.setUserList(receiver);
		//设置发送方式
		sm.setSendWayStr("1");
		//设置模板参数
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		if(activityRecord.getReserved36() != null){
			parameterMap.put("parameter_n1", "注册奖，");
		}
		if(activityRecord.getReserved37() != null){
			parameterMap.put("parameter_n2", "认证奖，");
		}
		if(activityRecord.getReserved38() != null){
			parameterMap.put("parameter_n3", "认购奖，");
		}
		if(activityRecord.getReserved39() != null){
			parameterMap.put("parameter_n4", "单笔交易额最高奖，");
		}
		if(activityRecord.getReserved40() != null){
			parameterMap.put("parameter_n5", "总交易额最高奖，");
		}
		sm.setTemplateParameters(parameterMap);
		//设置模板code
		sm.setTemplateCode("sys_manage_activity_issue");
		//设置发送者ID
		User user = userLoginService.getCurrLoginUser();
		sm.setSendUserID(Long.valueOf(user.getId()));
		//设置发送者类型
		sm.setSendUserType((short)1);
		//设置关联对象类型
		sm.setRelationType((short)1);
		
		commonMessageService.send(sm);
	}

	public void save(ActivityRecord entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public List<ActivityRecord> getList(ActivityRecord entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(ActivityRecord entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void deleteByArr(ActivityRecord entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public ActivityRecord getOne(ActivityRecord entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String upload(ActivityRecord entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object download(ActivityRecord entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public Map<String, Object> getGrid(ActivityRecord entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
