package com.sw.plugins.commission.fagrant.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jxl.write.WritableWorkbook;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.fagrant.entity.MemberPayRecord;
import com.sw.plugins.commission.organizationgrant.entity.TeamOrOrgPayRecord;
import com.sw.plugins.market.order.entity.TaxRate;
import com.sw.plugins.market.order.service.TaxRateService;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.system.user.entity.User;
import com.sw.util.ExportExcel;

public class MemberPayRecordService extends CommonService<MemberPayRecord> {

	private static Logger logger = Logger.getLogger(MemberPayRecordService.class);
	
	@Resource
	private TaxRateService taxRateService;
	
	@Resource
	private CommonMessageService commonMessageService;
	
	public void saveOrupdate(MemberPayRecord entity) throws Exception {
		if(entity.getId()==null){
			this.save(entity);
		}else{
			this.update(entity);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSubGrid(MemberPayRecord entity) throws Exception{
		Map<String, Object> map = new Hashtable<String, Object>();
		List<MemberPayRecord> resultList = (List<MemberPayRecord>) super.getRelationDao().selectList("memberPayRecord.select_subgrid", entity);
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		return map;
	}
	
	@Override
	public void save(MemberPayRecord entity) throws Exception {
		super.getRelationDao().update("memberPayRecord.insert", entity);
	}

	@Override
	public void update(MemberPayRecord entity) throws Exception {
		short status = entity.getStatus();
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
			userMessage.setUserID(entity.getMemberID());
			userMessage.setUserType((short)3);
			userList.add(userMessage);
			sendMessage.setUserList(userList);
			String templateCode = status == 1 ? "sys_manage_commission_fagrant_ffcg" 
												 : "sys_manage_commission_fagrant_ffsb";
			sendMessage.setTemplateCode(templateCode);
			try{
				commonMessageService.send(sendMessage);
			}catch(Exception e){
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
		super.getRelationDao().update("memberPayRecord.update", entity);
	}
	//更新理财师居间费支付记录表中税值
	public void updateTax(MemberPayRecord entity) throws Exception {
		super.getRelationDao().update("memberPayRecord.updateTax", entity);
	}
	@Override
	public Long getRecordCount(MemberPayRecord entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemberPayRecord> getList(MemberPayRecord entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MemberPayRecord> getPaginatedList(MemberPayRecord entity)
			throws Exception {
		return (List<MemberPayRecord>) super.getRelationDao().selectList("memberPayRecord.select_paginated", entity);
	}

	@Override
	public void delete(MemberPayRecord entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(MemberPayRecord entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MemberPayRecord getOne(MemberPayRecord entity) throws Exception {
		return (MemberPayRecord) super.getRelationDao().selectOne("memberPayRecord.select_one", entity);
	}

	@Override
	public Map<String, Object> getGrid(MemberPayRecord entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<MemberPayRecord> resultList = getPaginatedList(entity);
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		return map;
	}
	
	@Override
	public String upload(MemberPayRecord entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(MemberPayRecord entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public MemberPayRecord personalIncomeTaxTax(MemberPayRecord entity) throws Exception{
		//居间费
		BigDecimal commission = entity.getAmount();
		//营业税(分)
		BigDecimal salesTax = commission.multiply(BigDecimal.valueOf(0.05));
		//城建税(分)
		BigDecimal constructionTax = salesTax.multiply(BigDecimal.valueOf(0.07));
		//教育费附加(分)
		BigDecimal educationalSurtax = salesTax.multiply(BigDecimal.valueOf(0.03));
		//地方教育费附加(分)
		BigDecimal localEducationalCost = salesTax.multiply(BigDecimal.valueOf(0.02));
		//个人所得税
		BigDecimal personalIncomeTax = BigDecimal.valueOf(0);
		//减除费用
		BigDecimal deductionTemp = commission.subtract(salesTax)
											.subtract(constructionTax)
											.subtract(educationalSurtax)
											.subtract(localEducationalCost);
		BigDecimal deduction = 
				deductionTemp.compareTo(BigDecimal.valueOf(4000)) == 0 || deductionTemp.compareTo(BigDecimal.valueOf(4000)) == -1
				? BigDecimal.valueOf(800) : deductionTemp.multiply(BigDecimal.valueOf(0.2));
		
		//应纳税所得额
		BigDecimal taxableIncome = deductionTemp.subtract(deduction);
		List<TaxRate> taxRateList = (List<TaxRate>) taxRateService.getList(new TaxRate());
		for(TaxRate temp : taxRateList){
			//最大应纳所得税额度阀值
			BigDecimal maxPersonalIncomeTax = temp.getMaxPersonalIncomeTax();
			//最小应纳所得税额度阀值
			BigDecimal minPersonalIncomeTax = temp.getMinPersonalIncomeTax();
//			if(maxPersonalIncomeTax.compareTo(BigDecimal.valueOf(0)) != 0){
			if(maxPersonalIncomeTax != null){
				//taxableIncome > minPersonalIncomeTax && taxableIncome <= maxPersonalIncomeTax
				if(taxableIncome.compareTo(minPersonalIncomeTax) == 1&& 
						(taxableIncome.compareTo(maxPersonalIncomeTax) == 0 || taxableIncome.compareTo(maxPersonalIncomeTax) == -1)){
					personalIncomeTax = taxableIncome.multiply(temp.getTaxRate()).subtract(temp.getDeductCount());
					break;
				}
			}else{
				if(taxableIncome.compareTo(minPersonalIncomeTax) == 1){
					personalIncomeTax = taxableIncome.multiply(temp.getTaxRate()).subtract(temp.getDeductCount());
					break;
				}
			}
		}
		entity.setSalesTax(salesTax);
		entity.setConstructionTax(constructionTax);
		entity.setEducationalSurtax(educationalSurtax);
		entity.setLocalEducationalCost(localEducationalCost);
		entity.setPersonalIncomeTax(personalIncomeTax);
		return entity;
		
	}
	
	@SuppressWarnings({ "unchecked"})
	public void exportExcel(String colName, String colModel,
			WritableWorkbook excel, MemberPayRecord entity) throws Exception {
		List<Map<String, Object>> objList = (List<Map<String, Object>>) super.getRelationDao().selectList("memberPayRecord.export_excel",entity);
		if (objList != null) {
			for (Map<String, Object> one : objList) {
				// TSR姓名
				one.put("memberName", one.get("MEMBERNAME"));
				one.put("bankName",one.get("BANKNAME"));
				one.put("accountName", one.get("ACCOUNTNAME"));
				one.put("accountID", one.get("ACCOUNTID"));
				one.put("iDCard", one.get("IDCARD"));
				one.put("payDate", one.get("PAYDATE"));
				one.put("payAmount", one.get("PAYAMOUNT"));
				one.put("amount", one.get("AMOUNT"));
				one.put("salesTax", one.get("SALESTAX"));
				one.put("constructionTax", one.get("CONSTRUCTIONTAX"));
				one.put("educationalSurtax", one.get("EDUCATIONALSURTAX"));
				one.put("localEducationalCost", one.get("LOCALEDUCATIONALCOST"));
				one.put("personalIncomeTax", one.get("PERSONALINCOMETAX"));
			}
		}
		String[] colNames = colName.split(",");
		String[] colModels = colModel.split(",");

		String excelName = "";
		String status=String.valueOf(entity.getStatus());
		if (status.equals("0")) {	
			excelName+="待发放居间费数据";
		}if(status.equals("3")) {	
			excelName+="居间费发放失败数据";
		}if(status.equals("1")) {	
			excelName+="已发放居间费数据";
		}
		int colNameNum = colNames.length;
		int colModelNum = colModels.length;
		ExportExcel exportExcel = new ExportExcel();
		excel = exportExcel.createExcel(excelName, colNames, colNameNum,
				colModels, colModelNum, objList, excel);

	}
	
	
	/**
	 * 获取当前月的支付记录
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MemberPayRecord> getCurrMonthRecord(MemberPayRecord entity) throws Exception {
		return (List<MemberPayRecord>) super.getRelationDao().selectList("memberPayRecord.select_between_curr_month", entity);
	}
	

}
