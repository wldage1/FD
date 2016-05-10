package  com.sw.plugins.commission.organizationgrant.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.fagrant.entity.MemberPayRecord;
import com.sw.plugins.commission.organizationgrant.entity.TeamOrOrgPayRecord;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.market.order.entity.Order;
import com.sw.plugins.market.order.service.OrderHistoryService;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.system.user.entity.User;

@Service
public class TeamOrOrgPayRecordService extends CommonService<TeamOrOrgPayRecord>{
	
	private static Logger logger = Logger.getLogger(TeamOrOrgPayRecordService.class);
	
	@Resource
	private CommonMessageService commonMessageService;
	
	@Resource
	private MemberService memberService;
	
	@Resource
	private OrderHistoryService orderHistoryService;
	
	@Override
	public void save(TeamOrOrgPayRecord entity) throws Exception {
		super.getRelationDao().insert("teamororgpayrecord.insert", entity);
	}

	@Override
	public void update(TeamOrOrgPayRecord entity) throws Exception {
		if(entity.getStatus() == 1){
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateStr = sdf.format(date);
    		entity.setPayTime(dateStr);
		}
		super.getRelationDao().update("teamororgpayrecord.update", entity);
		short status = entity.getStatus();
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Member member = new Member();
		member.setTeamID(entity.getTeamID().intValue());
		member = memberService.memberOrgType(member);
		//支付成功或支付失败
		if(status == 1 || status == 3){
			SendMessage sendMessage = new SendMessage();
			//发送方式 (站内)
			sendMessage.setSendWayStr("1,2");
			//发送者类型(运营方)
			sendMessage.setSendUserType((short)1);
			//发送者ID
			sendMessage.setSendUserID(currentUser.getId());
			//模板替换参数
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("name", "");
			sendMessage.setTemplateParameters(userMap);
			//用户信息
			List<UserMessage> userList = new ArrayList<UserMessage>();
			UserMessage userMessage = new UserMessage();
			userMessage.setUserID(member.getId());
			userMessage.setUserType((short)3);
			userList.add(userMessage);
			sendMessage.setUserList(userList);
			String templateCode = status == 1 ? "sys_manage_commission_organizationgrant_ffcg" 
												 : "sys_manage_commission_organizationgrant_ffsb";
			sendMessage.setTemplateCode(templateCode);
			try{
				commonMessageService.send(sendMessage);
			}catch(Exception e){
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
	}

	//更新到帐金额
	public void updatePayAmount(TeamOrOrgPayRecord entity) throws Exception {
		super.getRelationDao().update("teamororgpayrecord.update_payAmount", entity);
	}
	
	//获取某阶段某机构的佣金总额
	public BigDecimal getSumPayAmount(TeamOrOrgPayRecord entity) throws Exception{
		return (BigDecimal)super.getRelationDao().selectOne("teamororgpayrecord.select_sum_commission", entity);
	}
	
	public BigDecimal getSumPayAmount1(TeamOrOrgPayRecord entity) throws Exception{
		return (BigDecimal)super.getRelationDao().selectOne("teamororgpayrecord.select_sum_commission1", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<TeamOrOrgPayRecord> getSubProduct(TeamOrOrgPayRecord entity) throws Exception{
		return (List<TeamOrOrgPayRecord>)super.getRelationDao().selectList("teamororgpayrecord.select_subproduct", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<TeamOrOrgPayRecord> getSubProduct1(TeamOrOrgPayRecord entity) throws Exception{
		return (List<TeamOrOrgPayRecord>)super.getRelationDao().selectList("teamororgpayrecord.select_subproduct1", entity);
	}
	
	//计算成功率 传机构id 和 stageTime companyID
	public Double getSuccessRate(TeamOrOrgPayRecord entity,Short type) throws Exception{
		List<TeamOrOrgPayRecord> subProductList = null ;
		//0查询机构的  1查询团队的
		if(type == 0){
			subProductList = getSubProduct(entity);
		}else if(type == 1){
			subProductList = getSubProduct1(entity);
		}
		Long subproductid;
		Order order = new Order();
		order.setTeamID(entity.getSourceID());
		order.setStageTime(entity.getStageTime());
		order.setOrgID(entity.getCompanyID());
		Long success = 0L ,failed= 0L;
		for(int i = 0;i<subProductList.size();i++){
			subproductid = subProductList.get(i).getSubProductID();
			order.setSubProductID(subproductid);
			success += orderHistoryService.getSuccessOrder(order);
			failed += orderHistoryService.getFailedOrder(order);
		}
		Double successRate = 0d;
		if(success!=0 || failed !=0){
			if(type == 0){
				successRate = ((double)this.getSuccessRecord(entity) / (success + failed));
			}else if(type == 1){
				successRate = ((double)this.getSuccessRecord1(entity) / (success + failed));
			}
		}
		return successRate;
	}
	
	// 已支付的成功订单数
	public Long getSuccessRecord(TeamOrOrgPayRecord entity) throws Exception{
		return (Long)super.getRelationDao().selectOne("teamororgpayrecord.select_success_count", entity);
	}
	
	public Long getSuccessRecord1(TeamOrOrgPayRecord entity) throws Exception{
		return (Long)super.getRelationDao().selectOne("teamororgpayrecord.select_success_count1", entity);
	}
	
	@Override
	public Long getRecordCount(TeamOrOrgPayRecord entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TeamOrOrgPayRecord> getList(TeamOrOrgPayRecord entity)
			throws Exception {
		return (List<TeamOrOrgPayRecord>)super.getRelationDao().selectList("teamororgpayrecord.select_ffsb", entity);
	}
	
	@Override
	public List<TeamOrOrgPayRecord> getPaginatedList(TeamOrOrgPayRecord entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(TeamOrOrgPayRecord entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(TeamOrOrgPayRecord entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TeamOrOrgPayRecord getOne(TeamOrOrgPayRecord entity)
			throws Exception {
		return (TeamOrOrgPayRecord)super.getRelationDao().selectOne("teamororgpayrecord.select_one", entity);
	}

	@Override
	public Map<String, Object> getGrid(TeamOrOrgPayRecord entity)
			throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		List<TeamOrOrgPayRecord> list = getList(entity);
		map.put("rows", list);
		return map;
	}
	
	@Override
	public String upload(TeamOrOrgPayRecord entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(TeamOrOrgPayRecord entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
