package com.sw.plugins.incentivefee.organizationfeemanage.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.adjust.entity.Commission;
import com.sw.plugins.commission.adjust.service.AdjustService;
import com.sw.plugins.incentivefee.organizationfeemanage.entity.IncentiveFeeDetail;
import com.sw.plugins.incentivefee.organizationfeemanage.entity.IncentiveOrderHistory;
import com.sw.plugins.market.order.service.OrderHistoryService;

@Service
public class IncentiveOrderHistoryService extends CommonService<IncentiveOrderHistory>{
	
	@Resource
	private IncentiveFeeDetailService service;
	
	@Resource
	private OrderHistoryService orderHistoryService;
	
	@Resource
	private AdjustService adjustService;
	
	/**
	 * 激励费用发放
	 * @param entity
	 * @throws Exception
	 */
	public void pay(IncentiveOrderHistory entity) throws Exception{
		//更新发放激励费用详细表(更新发放时间)
		IncentiveFeeDetail incentiveFeeDetail = new IncentiveFeeDetail();
		incentiveFeeDetail.setId(entity.getIncentiveFeeId());
		service.update(incentiveFeeDetail);
		Commission commission = new Commission();
		commission.setTeamID(entity.getOrgId());
		commission.setAuditingTime(entity.getAuditingTime());
		commission.setOrgID(entity.getCompanyID());
		commission.setIsPay(entity.getIsPay());
		Short type = entity.getType();
		List<Commission> list = null;
		if(type == 0){
			list = adjustService.getIncentiveList(commission);
		}else if(type == 1){
			list = adjustService.getIncentiveList1(commission);
		}
		for (Commission tempCommission : list) {
			commission.setOrderID(tempCommission.getOrderID());
			//更新居间费记录表的激励费用发放状态
			adjustService.updateIsPay(commission);
			//存发放激励费用订单表(关系表)
			entity.setOrderNumber(tempCommission.getOrderNumber());
			this.save(entity);
		}
	}
	
	/**
	 * 激励费用一键发放
	 * @param entity
	 * @throws Exception
	 */
	public void oneKeyPay(IncentiveOrderHistory entity) throws Exception{
		Long[] orgIds = entity.getOrgIds();
		Long[] incentiveFeeIds = entity.getIncentiveFeeIds();
		Short type = entity.getType();
		Commission commission = new Commission();
		for (int i = 0; i < orgIds.length; i++) {
			//更新发放激励费用详细表(更新发放时间)
			IncentiveFeeDetail incentiveFeeDetail = new IncentiveFeeDetail();
			incentiveFeeDetail.setId(incentiveFeeIds[i]);
			service.update(incentiveFeeDetail);
			commission.setTeamID(orgIds[i]);
			commission.setAuditingTime(entity.getAuditingTime());
			commission.setOrgID(entity.getCompanyID());
			List<Commission> list = null;
			if(type == 1){
				list = adjustService.getIncentiveList(commission);
			}else if(type == 2){
				list = adjustService.getIncentiveList1(commission);
			}
			entity.setIncentiveFeeId(incentiveFeeIds[i]);
			for (Commission tempCommission : list) {
				commission.setOrderID(tempCommission.getOrderID());
				//更新居间费记录表的激励费用发放状态
				adjustService.updateIsPay(commission);
				//存发放激励费用订单表(关系表)
				entity.setOrderNumber(tempCommission.getOrderNumber());
				this.save(entity);
			}
		}
	}
	
	@Override
	public void save(IncentiveOrderHistory entity) throws Exception {
		super.getRelationDao().insert("incentiveOrderHistory.insert", entity);
	}
	
	@Override
	public void update(IncentiveOrderHistory entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getRecordCount(IncentiveOrderHistory entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IncentiveOrderHistory> getList(IncentiveOrderHistory entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IncentiveOrderHistory> getPaginatedList(
			IncentiveOrderHistory entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(IncentiveOrderHistory entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(IncentiveOrderHistory entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IncentiveOrderHistory getOne(IncentiveOrderHistory entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(IncentiveOrderHistory entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(IncentiveOrderHistory entity,
			HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(IncentiveOrderHistory entity,
			HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
