package com.sw.util.thread;

import java.util.List;

import org.apache.log4j.Logger;

import com.sw.core.data.dao.IDao;
import com.sw.core.data.entity.RelationEntity;
import com.sw.plugins.market.order.entity.Order;

public class OrderTimeOutTask {

	private static Logger logger = Logger.getLogger(OrderTimeOutTask.class);

	private IDao<RelationEntity> relationDao;

	public void hangUpOrder() {

		try {

			@SuppressWarnings("unchecked")
			List<Order> list = (List<Order>) relationDao.selectList( "order.select_timeout_order_count", null);

			if (list != null && list.size() > 0) {
				Order orderTemp = new Order();
				String ids[] = new String[list.size()];
				int i = 0;
				for (Order order : list) {
					ids[i] = order.getId().toString();
					i++;
				}

				orderTemp.setIds(ids);

				relationDao.update("order.update_timeout_order", orderTemp);

				relationDao.insert( "order.insert_timeout_transactiondetailhistory", orderTemp);

			}

		} catch (Exception e) {

			logger.error(e.getStackTrace());

		}
	}

	public IDao<RelationEntity> getRelationDao() {
		return relationDao;
	}

	public void setRelationDao(IDao<RelationEntity> relationDao) {
		this.relationDao = relationDao;
	}

}
