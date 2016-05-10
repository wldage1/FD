package com.sw.plugins.incentivefee.organizationfeemanage.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.adjust.entity.Commission;
import com.sw.plugins.commission.adjust.service.AdjustService;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.organization.service.MemberOrganizationService;
import com.sw.plugins.customer.team.entity.Team;
import com.sw.plugins.customer.team.service.TeamService;
import com.sw.plugins.commission.organizationgrant.entity.TeamOrOrgPayRecord;
import com.sw.plugins.commission.organizationgrant.service.TeamOrOrgPayRecordService;
import com.sw.plugins.incentivefee.incentive.entity.RankRewardScore;
import com.sw.plugins.incentivefee.incentive.entity.RebateParameter;
import com.sw.plugins.incentivefee.incentive.service.RankRewardScoreService;
import com.sw.plugins.incentivefee.incentive.service.RebateParameterService;
import com.sw.plugins.incentivefee.organizationfeemanage.entity.IncentiveFeeDetail;

@Service
public class IncentiveFeeDetailService extends CommonService<IncentiveFeeDetail>{
	
	@Resource
	private RankRewardScoreService rewardScoreService;
	
	@Resource
	private RebateParameterService rebateParameterService;
	
	@Resource
	private MemberOrganizationService memberOrganizationService;
	
	@Resource
	private AdjustService adjustService;
	
	@Resource
	private TeamOrOrgPayRecordService payRecordService;
	
	@Resource
	private TeamService teamService;
	
	@Override
	public void save(IncentiveFeeDetail entity) throws Exception {
		super.getRelationDao().insert("incentiveFeeDetail.insert", entity);
	}

	@Override
	public void update(IncentiveFeeDetail entity) throws Exception {
		super.getRelationDao().update("incentiveFeeDetail.update", entity);
	}
	
	public void saveIncentiveDetail(IncentiveFeeDetail entity)throws Exception{
		this.delete(entity);
		Short type =entity.getType();
		Commission comm = new Commission();
		comm.setStageTime(entity.getPayStageTime());
		List<IncentiveFeeDetail> list = new ArrayList<IncentiveFeeDetail>();
		RankRewardScore rankRewardScore = new RankRewardScore();
		rankRewardScore.setType((short)2);
		rankRewardScore.setStageTime(entity.getPayStageTime());
		List<RankRewardScore> listReward = rewardScoreService.getList(rankRewardScore);
		RebateParameter rebateParameter = new RebateParameter();
		rebateParameter.setType((short)2);
		rebateParameter.setStageTime(entity.getPayStageTime());
		List<RebateParameter> listRebate = rebateParameterService.getList(rebateParameter);
		if(listReward.size()!=0 && listRebate.size()!=0){
			//将对象添加到list容器
			TeamOrOrgPayRecord payrecord = new TeamOrOrgPayRecord();
			payrecord.setStageTime(entity.getPayStageTime());
			List<Long> orgIdList = null ;
			//0是机构1是团队
			if(type == 0){
				orgIdList = memberOrganizationService.getOrgIds(new MemberOrganization());
			}else if(type == 1){
				orgIdList = teamService.getTeamIds(new Team());
			}
			if(orgIdList == null){
				return ;
			}
			for(Long orgid : orgIdList){
				IncentiveFeeDetail detail = new IncentiveFeeDetail();
				comm.setTeamID(orgid);
				comm.setOrgID(entity.getCompanyID());
				payrecord.setSourceID(orgid);
				payrecord.setCompanyID(entity.getCompanyID());
				detail.setSumInvestAmount(adjustService.getSumInvestAmount(comm,type));
				detail.setSuccessRate(payRecordService.getSuccessRate(payrecord,type));
				detail.setRebateScore(detail.getSumInvestAmount().multiply(BigDecimal.valueOf(detail.getSuccessRate())).divide(BigDecimal.valueOf(1000000)));
				list.add(detail);
			}
			for(Long orgid : orgIdList){
				payrecord.setSourceID(orgid);
				payrecord.setCompanyID(entity.getCompanyID());
				comm.setTeamID(orgid);
				comm.setOrgID(entity.getCompanyID());
				entity.setOrgId(orgid);
				//成功率
				entity.setSuccessRate(payRecordService.getSuccessRate(payrecord,type));
				//投资总规模
				entity.setSumInvestAmount(adjustService.getSumInvestAmount(comm,type));
				//将list排序投资金额 逆序(从多到少)
				Collections.sort(list, new Comparator<IncentiveFeeDetail>() {
					@Override
					public int compare(IncentiveFeeDetail o1, IncentiveFeeDetail o2) {
						// TODO Auto-generated method stub
						int i = o1.getSumInvestAmount().compareTo(o2.getSumInvestAmount());
						return -i;
					}
				});
				for(int i = 0;i < (listReward.size()>list.size()?list.size():listReward.size());i++){
					if(list.get(i).getSumInvestAmount().equals(entity.getSumInvestAmount())){
						//奖励分值
						entity.setRewardScore(Integer.valueOf(listReward.get(i).getRewardScore()));
						list.get(i).setRewardScore(Integer.valueOf(listReward.get(i).getRewardScore()));
						list.get(i).setRebateScore(list.get(i).getRebateScore().add(BigDecimal.valueOf(list.get(i).getRewardScore())));
					}
				}
				if(entity.getRewardScore() == null){
					entity.setRewardScore(0);
				}
				entity.setRebateScore(entity.getSumInvestAmount().multiply(BigDecimal.valueOf(entity.getSuccessRate())).divide(BigDecimal.valueOf(1000000)).add(BigDecimal.valueOf(entity.getRewardScore())));
				//根据返利分值排序 正序
				Collections.sort(list, new Comparator<IncentiveFeeDetail>() {
					@Override
					public int compare(IncentiveFeeDetail o1, IncentiveFeeDetail o2) {
						// TODO Auto-generated method stub
						int i = o1.getRebateScore().compareTo(o2.getRebateScore());
						return i;
					}
				});
				//根据返利分值的区间 所得返利比例
				int i = 0;
				for(;i<listRebate.size();){
					if(entity.getRebateScore().compareTo(BigDecimal.valueOf(Integer.valueOf(listRebate.get(0).getStageScore()))) >= 0){
						entity.setRebateRate(Double.parseDouble(listRebate.get(0).getServiceFeeRat()));
						i++;
						break;
					}
					if(entity.getRebateScore().compareTo(BigDecimal.valueOf(Integer.valueOf(listRebate.get(listRebate.size() - 1).getStageScore())))<0){
						entity.setRebateRate(0d);
						i++;
						break;
					}
					if(entity.getRebateScore().compareTo(BigDecimal.valueOf(Integer.valueOf(listRebate.get(i+1).getStageScore()))) >= 0 && i+1 < listRebate.size() && entity.getRebateScore().compareTo(BigDecimal.valueOf(Integer.valueOf(listRebate.get(i).getStageScore()))) < 0 ){
						entity.setRebateRate(Double.parseDouble(listRebate.get(i+1).getServiceFeeRat()));
						i++;
						break;
					}
					i++;
				}
				//计算总居间费/服务费
				if(type == 0){
					entity.setSumCommissionFee(payRecordService.getSumPayAmount(payrecord));
				}else if(type == 1){
					entity.setSumCommissionFee(payRecordService.getSumPayAmount1(payrecord));
				}
				if(entity.getSumCommissionFee()!=null){
					entity.setRebateAmount(entity.getSumCommissionFee().multiply(BigDecimal.valueOf(entity.getRebateRate())));
				}else{
					entity.setRebateAmount(BigDecimal.ZERO);
				}
				if(entity.getSumInvestAmount()!=null && entity.getSumInvestAmount()!= BigDecimal.ZERO){
					this.save(entity);
				}
				
			}
		}
	}
	
	
	@Override
	public Long getRecordCount(IncentiveFeeDetail entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IncentiveFeeDetail> getList(IncentiveFeeDetail entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IncentiveFeeDetail> getPaginatedList(IncentiveFeeDetail entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(IncentiveFeeDetail entity) throws Exception {
		super.getRelationDao().delete("incentiveFeeDetail.delete", entity);
	}

	@Override
	public void deleteByArr(IncentiveFeeDetail entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IncentiveFeeDetail getOne(IncentiveFeeDetail entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(IncentiveFeeDetail entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		MemberOrganization memberOrganization = new MemberOrganization();
		memberOrganization.setType((int)entity.getType());
		memberOrganization.setIsPay(entity.getIsPay());
		memberOrganization.setPayStageTime(entity.getPayStageTime());
		memberOrganization.setCompanyID(entity.getCompanyID());
		List<MemberOrganization> resultList = memberOrganizationService.getIncentiveList(memberOrganization);
		map.put("rows", resultList);
		return map;
	}

	@Override
	public String upload(IncentiveFeeDetail entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(IncentiveFeeDetail entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
