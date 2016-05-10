package com.sw.plugins.commission.adjust.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.adjust.entity.Commission;
import com.sw.plugins.commission.fagrant.entity.MemberPayRecord;
import com.sw.plugins.commission.fagrant.service.MemberPayRecordService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.product.manage.entity.ProductCommissionRatio;
import com.sw.plugins.product.manage.service.ProductCommissionRatioService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;

public class AdjustService extends CommonService<Commission> {

	@Resource
	private ProductCommissionRatioService productCommissionRatioService;
	
	@Resource
	private MemberService memberService;
	
	@Resource
	private MemberPayRecordService memberPayRecordService;
	
	
	@Override
	public void save(Commission entity) throws Exception {
		super.getRelationDao().update("commission.insert", entity);
	}

	@Override
	public void update(Commission entity) throws Exception {
		super.getRelationDao().update("commission.update", entity);
	}

	@Override
	public Long getRecordCount(Commission entity) throws Exception {
		return  super.getRelationDao().getCount("commission.select_count", entity);
	}
	public Long getRecordNotDo(Commission entity) throws Exception {
		return  super.getRelationDao().getCount("commission.select_order_notdo", entity);
	}

	@Override
	public List<Commission> getList(Commission entity) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Commission> getPaginatedList(Commission entity) throws Exception {
		return (List<Commission>) super.getRelationDao().selectList("commission.select_paginated", entity);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> holdlingPhase(Commission entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Commission> resultList = (List<Commission>) super.getRelationDao().selectList("commission.select_subProduct_holdlingPhase", entity);
		for(Commission commission : resultList){
			entity.setSubProductID(commission.getSubProductID());
			BigDecimal minShareThreshold = commission.getMinShareThreshold();
			BigDecimal maxShareThreshold = commission.getMaxShareThreshold();
			String effectiveDate = commission.getEffectiveDate();
			if(minShareThreshold != null){
				entity.setMinShareThreshold(minShareThreshold);
			}
			entity.setMaxShareThreshold(maxShareThreshold);
			if(effectiveDate != null){
				entity.setEffectiveDate(effectiveDate);
			}
			String payStatusCollection[] = {"0"};
			//未核定订单数量
			entity.setPayStatusCollection(payStatusCollection);
			Long notCheckOrderNumber =  super.getRelationDao().getCount("commission.select_adjust_order_count", entity);
			//已核定通过订单数量
			String payStatusCollection1[] = {"1"};
			entity.setPayStatusCollection(payStatusCollection1);
			Long notAgreeOrderNumber =  super.getRelationDao().getCount("commission.select_adjust_order_count", entity);
			//已核定不通过订单数量
			String payStatusCollection2[] = {"2"};
			entity.setPayStatusCollection(payStatusCollection2);
			Long notDisagreeOrderNumber =  super.getRelationDao().getCount("commission.select_adjust_order_count", entity);
			//拒发订单数量
			String payStatusCollection3[] = {"6"};
			entity.setPayStatusCollection(payStatusCollection3);
			Long refuseOrderNumber =  super.getRelationDao().getCount("commission.select_adjust_order_count", entity);
			//订单总数
			String payStatusCollection4[] = {"0", "1", "2" , "6"};
			entity.setPayStatusCollection(payStatusCollection4);
			Long orderNumber =  super.getRelationDao().getCount("commission.select_adjust_order_count", entity);			
			//订单总数
			//Long orderNumber =  super.getRelationDao().getCount("commission.select_all_order", entity);
//			commission.setAdjustOrderNumber(adjustOrderNumber);
//			commission.setNotAdjustOrderNumber(notAdjustOrderNumber);
			commission.setNotCheckOrderNumber(notCheckOrderNumber);
			commission.setNotAgreeOrderNumber(notAgreeOrderNumber);
			commission.setNotDisagreeOrderNumber(notDisagreeOrderNumber);
			commission.setRefuseOrderNumber(refuseOrderNumber);
			commission.setOrderCount(orderNumber);
		}
		map.put("rows", resultList);
		return map;
	}

	/**
	 * 核算居间费
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void adjustCommission(Commission entity) throws Exception {
		super.getRelationDao().update("commission.adjust_commission", entity);
	}
	/**
	 * 未核定订单数
	 * 
	 * @param entity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Commission> getOrderIdList(Commission entity) throws Exception {
		return (List<Commission>) super.getRelationDao().selectList("commission.select_orderId_list", entity);
	}
	/**
	 * 批量重新核算
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void againAdjustCommission(Commission entity) throws Exception {
		Long id = entity.getId();
		String[] ids = entity.getIds();
		
		if(id != null){
			entity.setId(id);
			Commission commission = getCommissio(entity);
			updateCommissio(commission);
		}else if(ids != null){
			for(String temp : ids){
				entity.setId(Long.parseLong(temp));
				Commission commission = getCommissio(entity);
				updateCommissio(commission);
			}
		}
	}
	
	public void updateCommissio(Commission commission) throws Exception{
		//查询需要更新的产品佣金率
		ProductCommissionRatio productCommissionRatio= new ProductCommissionRatio();
		productCommissionRatio.setSubProductId(commission.getSubProductID());
		String holdlingPhase = commission.getHoldlingPhase();
		if(holdlingPhase != null){
			//按照子产品查询有效的阶段
			//有效的阶段
			productCommissionRatio.setIsAvailable((short)1);
			productCommissionRatio.setMaxShareThreshold(commission.getPayAmount());
			productCommissionRatio = productCommissionRatioService.getOneBySubproductID(productCommissionRatio);
		}else{
			//申购和新申购订单（按照子产品查询生效日期最近的记录）
			productCommissionRatio.setEffectiveDate(commission.getOrderTime());
			productCommissionRatio.setMaxShareThreshold(commission.getPayAmount());
			productCommissionRatio = productCommissionRatioService.getOneSubscribe(productCommissionRatio);
		}
		
		//productCommissionRatio = productCommissionRatioService.getOneBySubproductID(productCommissionRatio);
		//判断理财师是否属于机构
		Long teamid = commission.getTeamID();
		if(teamid != null){
			Member member = new Member();
			member.setId(commission.getTeamID());
			member = memberService.memberIsOrg(member);
			//如果理财师属于机构则设置机构服务费系数，否则设置个人服务费系数
			if(member != null){
				commission.setServiceChargeCoefficient(productCommissionRatio.getOrgServiceChargeCoefficient());
			}else{
				commission.setServiceChargeCoefficient(productCommissionRatio.getServiceChargeCoefficient());
			}
		}else{
			commission.setServiceChargeCoefficient(productCommissionRatio.getServiceChargeCoefficient());
		}
//		commission.setHoldlingPhase(productCommissionRatio.getHoldlingPhase());
		commission.setIssuanceCostRate(productCommissionRatio.getIssuanceCostRate());
		//计算最终佣金比例
		commission.setTimeCoefficient(productCommissionRatio.getTimeCoefficient());
		BigDecimal commissionProportion = productCommissionRatio.getIssuanceCostRate().multiply(commission.getServiceChargeCoefficient())
				.multiply(productCommissionRatio.getTimeCoefficient()).add(productCommissionRatio.getIncentiveFeeRate());
		commission.setCommissionProportion(commissionProportion);
		BigDecimal commissionAmount = commissionProportion.multiply(commission.getOrderHistory().getPayAmount()).divide(BigDecimal.valueOf(100));
		commission.setCommission(commissionAmount);
		commission.setIncentiveFeeRate(productCommissionRatio.getIncentiveFeeRate());
//		commission.setPayStatus(null);
		super.getRelationDao().update("commission.againAdjust_commission", commission);
	}
	
	public Commission getCommissio(Commission entity) throws Exception {
		return (Commission) super.getRelationDao().selectOne("commission.select_commission", entity);
	}
	
	@Override
	public void delete(Commission entity) throws Exception {
	}

	@Override
	public void deleteByArr(Commission entity) throws Exception {
	}

	@Override
	public Commission getOne(Commission entity) throws Exception {
		Commission com = (Commission) super.getRelationDao().selectOne("commission.select_one", entity);
		MemberPayRecord record = new MemberPayRecord();
		record.setAmount(com.getCommission());
		MemberPayRecord payrecord=memberPayRecordService.personalIncomeTaxTax(record);
		/*com.setSalesTax(payrecord.getSalesTax());
		com.setConstructionTax(payrecord.getConstructionTax());
		com.setEducationalSurtax(payrecord.getEducationalSurtax());
		com.setLocalEducationalCost(payrecord.getLocalEducationalCost());
		com.setPersonalIncomeTax(payrecord.getPersonalIncomeTax());*/
		if (payrecord.getSalesTax()!=null){
			com.setSalesTax(payrecord.getSalesTax().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		if (payrecord.getConstructionTax()!=null){
			com.setConstructionTax(payrecord.getConstructionTax().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		if (payrecord.getEducationalSurtax()!=null){
			com.setEducationalSurtax(payrecord.getEducationalSurtax().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		if (payrecord.getLocalEducationalCost()!=null){
			com.setLocalEducationalCost(payrecord.getLocalEducationalCost().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		if (payrecord.getPersonalIncomeTax()!=null){
			com.setPersonalIncomeTax(payrecord.getPersonalIncomeTax().setScale(2, BigDecimal.ROUND_HALF_UP));	
		}
		return com;
	}

	@Override
	public Map<String, Object> getGrid(Commission entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Commission> resultList = getPaginatedList(entity);
//		Long record = getRecordCount(entity);
//		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
//		map.put("total", pageCount);
//		map.put("records", record);
		return map;
	}

	@Override
	public String upload(Commission entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public Object download(Commission entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
	}

	public void saveOrupdate(Commission Commission) throws Exception {
		if(Commission.getId()==null){
			this.save(Commission);
		}else{
			this.update(Commission);
		}
	}
	//更新理财师居间费表中税值
	public void updateTax(Commission entity) throws Exception {
		super.getRelationDao().update("commission.updateTax", entity);
	}
	/**
	 * 判断当前登录用户是否属于居间公司
	 * 
	 * @return
	 */
	public Organization isCommission(){
		//如果登录用户属于居间公司，则查询该居间公司的数据
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Organization organization = new Organization();
		organization = currentUser.getSelfOrg();
		if(organization.getIsCommission() == 1){
			return organization;
		}else{
			return null;
		}
		
	}
	//更新佣金表中审核时间
	public void updateAuditingTime(Commission entity) throws Exception {
		super.getRelationDao().update("commission.updateAuditingTime", entity);
	}
	
	final BigDecimal ZERO = BigDecimal.ZERO;
	
	@SuppressWarnings("unchecked")
	public List<Commission> getSuccessOrder(Commission entity) throws Exception{
		return (List<Commission>)super.getRelationDao().selectList("commission.select_commission_product", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<Commission> getSuccessOrder1(Commission entity) throws Exception{
		return (List<Commission>)super.getRelationDao().selectList("commission.select_commission_product1", entity);
	}
	
	/**
	 * 计算统计规模 传 teamID stageTime
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getSumInvestAmount(Commission entity,Short type) throws Exception{
		BigDecimal investAmount = ZERO;
		List<Commission> orderList = null;
		if(type == 0){
			orderList = this.getSuccessOrder(entity);
		}else if(type == 1){
			orderList = this.getSuccessOrder1(entity);
		}
		Commission order = null;
		for(int i = 0;i< orderList.size();i++){
			order =orderList.get(i);
			if(order.getDeadline() == null){
				order.setDeadline(0);
			}
			//如果是阳光私募 投资金额就是 认购规模
				if(Integer.valueOf(order.getProductType()) == 2){
					investAmount = investAmount.add(order.getPayAmount());
				}else{
					//期限类型不固定
					if(order.getDeadlineDataType() == null){
						investAmount = investAmount.add(order.getPayAmount());
					}else if(order.getDeadlineDataType() == 1){
							//按月计算
							if(order.getDeadline() >= 12){
								investAmount = investAmount.add(order.getPayAmount());
							}else{
								investAmount = investAmount.add(order.getPayAmount().multiply(BigDecimal.valueOf(order.getDeadline())).divide(BigDecimal.valueOf(12),2,BigDecimal.ROUND_HALF_EVEN));
							}
					}else{
						if(order.getDeadline() >= 365){
							investAmount = investAmount.add(order.getPayAmount());
						}else{
							//按天计算
							investAmount = investAmount.add(order.getPayAmount().multiply(BigDecimal.valueOf(order.getDeadline())).divide(BigDecimal.valueOf(365),2,BigDecimal.ROUND_HALF_EVEN));
						}
					}
					
				}
		}
		return investAmount;
	}
	
	/**
	 * 获取激励费用订单集合（jqgrid）
	 * 
	 * @param commission
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getIncentiveGrid(Commission commission,Short type) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Commission> resultList = null;
		if(type == 0){
			resultList = getIncentiveList(commission);
		}else if(type == 1){
			resultList = getIncentiveList1(commission);
		}
		map.put("rows", resultList);
		return map;
	}
	
	/**
	 * 获取激励费用订单集合
	 * 
	 * @param commission
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Commission> getIncentiveList(Commission commission) throws Exception {
		List<Commission> list = (ArrayList<Commission>) super.getRelationDao().selectList("commission.select_incentiveorder", commission);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Commission> getIncentiveList1(Commission commission) throws Exception {
		List<Commission> list = (ArrayList<Commission>) super.getRelationDao().selectList("commission.select_incentiveorder1", commission);
		return list;
	}
	
	public void updateIsPay(Commission commission)throws Exception{
		super.getRelationDao().update("commission.update_isPay", commission);
	}
}
