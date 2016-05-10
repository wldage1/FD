package com.sw.plugins.message.messageinterface.service;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.sw.core.data.dao.CustomCommonDao;
import com.sw.core.data.dbholder.DatasourceTypeContextHolder;
import com.sw.plugins.message.task.entity.AppSender;
import com.sw.util.DateUtil;
import com.sw.util.SystemProperty;

/**
 *  发送短信类
 *  @author sean
 *  @version 1.0
 *  </pre>
 *  Created on :下午1:20:09
 *  LastModified:
 *  History:
 *  </pre>
 */
public class SmsMessageService {
	private static Logger logger = Logger.getLogger(SmsMessageService.class);
	@Resource
	private CustomCommonDao customCommonDao;
	/**
	 *  发送短信
	 *  @param entity
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-12-11 上午9:06:23
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	
	            
	public void sms( AppSender entity) throws Exception {
		/*// 设置数据源
		final IDao<RelationEntity> rdao = super.getRelationDao();
		DatasourceTypeContextHolder.setDataSourceType("sqlserver_dataSource3");
		TransactionTemplate transactionTemplate = new TransactionTemplate();
		transactionTemplate.setTransactionManager(transactionManager);
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override 
			public void doInTransactionWithoutResult(TransactionStatus status) {
					// construct engine data
					String time = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
					entity.setServiceID(SystemProperty.getInstance("config").getProperty("sms.serviceId"));
					entity.setRecompleteTimeBegin(time);
					entity.setRecompleteTimeEnd(time);
					entity.setRecompleteHourBegin("0");
					entity.setRecompleteHourEnd("1439");
					entity.setPriority("1");
					entity.setRodeBy("1");
				try {
					//sqlSessionTemplate.insert("sms.insert");
                         
					rdao.insert("sms.insert", entity);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					// 清除数据源
					DatasourceTypeContextHolder.clear();
				}
			}});*/
		try {
			String time = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
			entity.setServiceID(SystemProperty.getInstance("config")
					.getProperty("sms.serviceId"));
			entity.setRecompleteTimeBegin(time);
			entity.setRecompleteTimeEnd(time);
			entity.setRecompleteHourBegin("0");
			entity.setRecompleteHourEnd("1439");
			entity.setPriority("1");
			entity.setRodeBy("1");
			
			// super.getRelationDao().insert("sms.insert", entity);
			StringBuffer sql = new StringBuffer();
			sql.append("insert into sm_send_sm_list ");
			sql.append(" (serviceID,smContent,sendTarget,priority,RCompleteTimeBegin,RCompleteTimeEnd,RCompleteHourBegin,RCompleteHourEnd,RoadBy) ");
			sql.append(" values (?,?,?,?,?,?,?,?,?) ");
			customCommonDao
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
