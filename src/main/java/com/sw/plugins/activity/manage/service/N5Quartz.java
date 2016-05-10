package com.sw.plugins.activity.manage.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sw.core.data.dao.CustomCommonDao;
import com.sw.core.data.dao.RelationaldbCommonDao;
import com.sw.core.data.dbholder.DatasourceTypeContextHolder;
import com.sw.core.quartz.IQuartz;
import com.sw.plugins.activity.manage.entity.ActivityRecord;
import com.sw.plugins.message.task.entity.AppSender;
import com.sw.util.DateUtil;
import com.sw.util.SystemProperty;

/**
 *  类简要说明
 *  @author Administrator
 *  @version 1.0
 *  </pre>
 *  Created on :下午1:33:00
 *  LastModified:
 *  History:
 *  </pre>
 */
public class N5Quartz implements IQuartz {

	private static Logger logger = Logger.getLogger(N5Quartz.class);
	private RelationaldbCommonDao relationDao;
	private CustomCommonDao customDao;

	

	public RelationaldbCommonDao getRelationDao() {
		return relationDao;
	}

	public void setRelationDao(RelationaldbCommonDao relationDao) {
		this.relationDao = relationDao;
	}
	
	
	public CustomCommonDao getCustomDao() {
		return customDao;
	}

	public void setCustomDao(CustomCommonDao customDao) {
		this.customDao = customDao;
	}

	@Override
	public void execute() {
		this.check();
	}
	
	private void check() {
		try {
			List<Map<String, Object>> list = (List<Map<String, Object>>) relationDao.selectList("activityRecordN5.select_quartz",null);
			for (int i = 0; i < list.size(); i++) {
				Map m = list.get(i);
				AppSender appSender = new AppSender();
				appSender.setSendTarget(m.get("MobilePhone") + "");
				appSender.setSmContent("亲还在忙吗？优财富的现金大奖您还没领呢~只要认证即可获得多多的微信红包哦，并可以开放更多特权呢~快点登录活动页面进行认证吧！");
				sms(appSender);
			}
		} catch (Exception e) {
			
		}
	}
	
	private void sms( AppSender entity) throws Exception {
		try {
			String time = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
			entity.setServiceID(SystemProperty.getInstance("config")
					.getProperty("sms.serviceId"));
			entity.setRecompleteTimeBegin(time);
			entity.setRecompleteTimeEnd(time);
			entity.setRecompleteHourBegin("540");
			entity.setRecompleteHourEnd("1080");
			entity.setPriority("1");
			entity.setRodeBy("1");
			
			// super.getRelationDao().insert("sms.insert", entity);
			StringBuffer sql = new StringBuffer();
			sql.append("insert into sm_send_sm_list ");
			sql.append(" (serviceID,smContent,sendTarget,priority,RCompleteTimeBegin,RCompleteTimeEnd,RCompleteHourBegin,RCompleteHourEnd,RoadBy) ");
			sql.append(" values (?,?,?,?,?,?,?,?,?) ");
			customDao
					.update(sql.toString(),
							new Object[] { entity.getServiceID(),
									entity.getSmContent(),
									entity.getSendTarget(),
									entity.getPriority(),
									entity.getRecompleteTimeBegin(),
									entity.getRecompleteTimeEnd(),
									entity.getRecompleteHourBegin(),
									entity.getRecompleteHourEnd(),
									entity.getRodeBy() });
		} catch (Exception e) {
			logger.debug("短信发送失败！");
		} finally {
			// 清除数据源
			DatasourceTypeContextHolder.clear();
		}
	}

	

}
