package com.sw.plugins.commission.auditing.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jxl.write.WritableWorkbook;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.adjust.entity.Commission;
import com.sw.plugins.commission.adjust.service.AdjustService;
import com.sw.plugins.commission.fagrant.entity.MemberPayDetail;
import com.sw.plugins.commission.fagrant.entity.MemberPayRecord;
import com.sw.plugins.commission.fagrant.service.MemberPayDetailService;
import com.sw.plugins.commission.fagrant.service.MemberPayRecordService;
import com.sw.plugins.commission.organizationgrant.service.CommissionFeeService;
import com.sw.plugins.commission.paygrantparameter.entity.PayGrantParameter;
import com.sw.plugins.commission.paygrantparameter.service.PayGrantParameterService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.util.ExportExcel;

public class AuditingService extends CommonService<Commission> {


	@Resource
	private MemberPayRecordService memberPayRecordService;

	@Resource
	private MemberPayDetailService memberPayDetailService;
	
	@Resource
	private CommissionFeeService commissionFeeService;
	
	@Resource
	private MemberService memberService;
	
	@Resource
	private PayGrantParameterService payGrantParameterService;
	@Resource
	private AdjustService adjustService;
	

	@Override
	public void save(Commission entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Commission entity) throws Exception {
		super.getRelationDao().update("auditingCommission.update", entity);
	}
	public void updateExportStatus(Commission entity) throws Exception {
		super.getRelationDao().update("auditingCommission.updateExportStatus", entity);
	}


	@Override
	public Long getRecordCount(Commission entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Commission> getList(Commission entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Commission> getPaginatedList(Commission entity)
			throws Exception {
		return (List<Commission>) super.getRelationDao().selectList(
				"auditingCommission.select_paginated", entity);
	}

	@Override
	public void delete(Commission entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByArr(Commission entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Commission getOne(Commission entity) throws Exception {
		return (Commission) super.getRelationDao().selectOne("auditingCommission.select_one", entity);
	}

	@Override
	public Map<String, Object> getGrid(Commission entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
//		Map<String, Object> sqlMap = new HashMap<String, Object>();
//		Map<String, Object> commissionMap = new HashMap<String, Object>();
		List<Commission> resultList = getPaginatedList(entity);
//		Commission commission = commissionSum(entity);
//		commissionMap.put("commission", commission);
		map.put("rows", resultList);
//		sqlMap.put("commission", commission);
		return map;
	}
	
	//统计居间费
	public Commission commissionSum(Commission entity) throws Exception{
		return (Commission) super.getRelationDao().selectOne("auditingCommission.select_commissionSum", entity);
	}

	@SuppressWarnings({ "unchecked"})
	public void exportExcel(String colName, String colModel,
			WritableWorkbook excel, Commission entity,
			Map<String, Object> payStatusMap,Map<String, Object> exportStatus) throws Exception {
		List<Map<String, Object>> objList = (List<Map<String, Object>>) super
				.getRelationDao().selectList("auditingCommission.export_excel",
						entity);
		if (objList != null) {
			for (Map<String, Object> one : objList) {
				// TSR姓名
				one.put("orderHistory.orderNumber", one.get("ORDERNUMBER"));
				one.put("memberName",one.get("MEMBERNAME"));
				one.put("orgShortName", one.get("ORGSHORTNAME"));
				one.put("productName", one.get("PRODUCTNAME"));
				one.put("subProductName", one.get("SUBPRODUCTNAME"));
				one.put("clientName", one.get("CLIENTNAME"));
				one.put("commissionProportion", new StringBuffer().append(one.get("COMMISSIONPROPORTION")).append("%"));
				one.put("payAmount", one.get("PAYAMOUNT"));
				one.put("commission", one.get("COMMISSION"));
				one.put("checkRatifyTime", one.get("CHECKRATIFYTIME"));
				if(one.get("PAYSTATUS")!=null){
					one.put("payStatus", payStatusMap.get(one.get("PAYSTATUS").toString()));
					one.put("payStatus", payStatusMap.get(one.get("PAYSTATUS").toString()));
				}else{
					one.put("payStatus","");
					one.put("payStatus", "");
				}
				/*if(one.get("EXPORTSTATUS")!=null){
					one.put("exportStatus", exportStatus.get(one.get("EXPORTSTATUS").toString()));
					one.put("exportStatus", exportStatus.get(one.get("EXPORTSTATUS").toString()));
				}else{
					one.put("exportStatus","");
					one.put("exportStatus", "");
				}*/
				Commission commission = new Commission();
				commission.setExportStatus("1");
				commission.setId(new Long(one.get("ID").toString()));
				updateExportStatus(commission);
			}
		}
		String[] colNames = colName.split(",");
		String[] colModels = colModel.split(",");

		String excelName = "";
		String flag=entity.getFlag();
		short paystatus=entity.getPayStatus();
		if (flag.equals("1")) {			
			excelName = "个人居间费";
			if(paystatus==(short)1){
				excelName+="未审核数据";
			}else{
				excelName+="审核未通过数据";
			}
		}else{
			excelName = "机构居间费";
			if(paystatus==(short)1){
				excelName+="未审核数据";
			}else{
				excelName+="审核未通过数据";
			}
		}
		int colNameNum = colNames.length;
		int colModelNum = colModels.length;
		ExportExcel exportExcel = new ExportExcel();
		excel = exportExcel.createExcel(excelName, colNames, colNameNum,
				colModels, colModelNum, objList, excel);
		
	}

	public void saveAuditingInfo(Commission entity) throws Exception {
		Commission commission=new Commission();
		commission.setId(entity.getId());
		commission.setIds(entity.getIds());
		commission.setPayStatus(entity.getPayStatus());
		Long id = entity.getId();
		String ids[] = entity.getIds();
		if (id != null) {
			saveAuditing(entity);
		} else if (ids != null) {
			for (String str : ids) {
				if(str == null || "".equals(str)){
					continue;
				}
				entity.setId(Long.parseLong(str));
				saveAuditing(entity);
			}
		}
		update(commission);
		adjustService.updateAuditingTime(commission);

	}
	
	/**
	 * 保存居间费数据到理财师和机构发放表
	 * 
	 * @param commission
	 */
	public void saveAuditing(Commission commission) throws Exception {
		List<PayGrantParameter> shoudPayDateList = null;   //个人发放时间
		List<PayGrantParameter> orgShoudPayDateList = null; //机构发放时间
		Commission orgCommission =commission;	
		String flag=commission.getFlag();
		String status=commission.getStatus();
		String checkRatifyTime="";	//核定日期
		String shoudPayStatrDate=""; //应发开始日期
		String shouldPayEndDate=""; //应发结束日期
		String year="";
		String month="";
		String date="";
		if(status.equals("1")){//未审核
			commission = this.getOne(commission);
			checkRatifyTime=commission.getCheckRatifyTime();
			year=checkRatifyTime.substring(0,checkRatifyTime.indexOf("-"));
			month=checkRatifyTime.substring(checkRatifyTime.indexOf("-")+1,checkRatifyTime.lastIndexOf("-"));
			date=checkRatifyTime.substring(checkRatifyTime.lastIndexOf("-")+1);	
			orgCommission.setTaxAssessmentTime(checkRatifyTime);
		}else{//审核未通过
			Calendar  now  =  Calendar.getInstance();
	    	year = String.valueOf(now.get(Calendar.YEAR));
	    	month =String.valueOf(now.get(Calendar.MONTH)  +  1);
	    	date = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
	    	checkRatifyTime=year+"-"+month+"-"+date;
			adjustService.updateTax(orgCommission);		
			orgCommission.setTaxAssessmentTime("");
			commission = this.getOne(commission);
		}
		
		// 理财顾问为个人
		if(flag.equals("1")){
			PayGrantParameter payGrantParameter = new PayGrantParameter();
			Organization organization = adjustService.isCommission();
			if (organization != null && organization.getIsCommission() == 1){
				payGrantParameter.setOrgID(organization.getId());
			}
			payGrantParameter.setType((short)1);
			shoudPayDateList = payGrantParameterService.getList(payGrantParameter);		
			String firstDate=""; //上旬
			String middleDate=""; //中旬
			String endDate=""; //下旬
			int i=0;
			for(PayGrantParameter param : shoudPayDateList){
				if(i==0){
					firstDate=String.valueOf(param.getPayDate());
				}if(i==1){
					middleDate=String.valueOf(param.getPayDate());
				}else{
					endDate=String.valueOf(param.getPayDate());
				}
				i++;
			}
			if(Integer.parseInt(date)<=Integer.parseInt(firstDate)){
				shoudPayStatrDate=year+"-"+month+"-01";
				shouldPayEndDate=year+"-"+month+"-"+firstDate;
			}if(Integer.parseInt(date)>=(Integer.parseInt(firstDate)+1)&&Integer.parseInt(date)<=Integer.parseInt(middleDate)){
				shoudPayStatrDate=year+"-"+month+"-"+(Integer.parseInt(firstDate)+1);
				shouldPayEndDate=year+"-"+month+"-"+middleDate;
			}if(Integer.parseInt(date)>=(Integer.parseInt(middleDate)+1)&&Integer.parseInt(date)<=Integer.parseInt(endDate)){
				shoudPayStatrDate=year+"-"+month+"-"+(Integer.parseInt(middleDate)+1);
				shouldPayEndDate=year+"-"+month+"-"+endDate;
			}
			MemberPayRecord payRecord=new MemberPayRecord();
			payRecord.setShoudPayStatrDate(shoudPayStatrDate);
			payRecord.setShouldPayEndDate(shouldPayEndDate);
			payRecord.setMemberID(commission.getMemberID());
			MemberPayRecord record=memberPayRecordService.getOne(payRecord);
			MemberPayRecord memberPayRecord = new MemberPayRecord();
			Long id=0L;
			
			// 查询当前月支付记录
			if(record!=null){
				payRecord.setId(record.getId());
			}
			payRecord.setPayDate(commission.getCheckRatifyTime());
			BigDecimal payedPersonalTax = BigDecimal.ZERO;
			BigDecimal tAmount = BigDecimal.ZERO;
			List<MemberPayRecord> currMonthPayedList=memberPayRecordService.getCurrMonthRecord(payRecord);
			if(currMonthPayedList != null){
				for(MemberPayRecord pd:currMonthPayedList){
					BigDecimal tp = pd.getPersonalIncomeTax();
					BigDecimal ta = pd.getAmount();
					payedPersonalTax = payedPersonalTax.add(tp);
					tAmount = tAmount.add(ta);
				}
			}
			
			if(record==null){//插入
				memberPayRecord.setMemberID(commission.getMemberID());
				BigDecimal amount = commission.getCommission();
				memberPayRecord.setAmount(amount);
				memberPayRecord.setPayAmount(commission.getCommission());
				memberPayRecord.setAccountID(commission.getAccountID());
				memberPayRecord.setAccountName(commission.getMemberName());
				memberPayRecord.setBankName(commission.getBankName());
				memberPayRecord.setStatus((short) 0);
				memberPayRecord.setPayDate(checkRatifyTime);
				MemberPayRecord mpr = memberPayRecordService.personalIncomeTaxTax(memberPayRecord);
				memberPayRecord.setActualPayAmount(commission.getPayAmount());
				memberPayRecord.setSalesTax(mpr.getSalesTax());
				memberPayRecord.setConstructionTax(mpr.getConstructionTax());
				memberPayRecord.setEducationalSurtax(mpr.getEducationalSurtax());
				memberPayRecord.setLocalEducationalCost(mpr.getLocalEducationalCost());
				
				//重新计算个人所得税
				MemberPayRecord tmpr = new  MemberPayRecord();
				amount = amount.add(tAmount);
				tmpr.setAmount(amount);
				tmpr=memberPayRecordService.personalIncomeTaxTax(tmpr);
				BigDecimal personalIncomeTaxTax = tmpr.getPersonalIncomeTax();
				personalIncomeTaxTax = personalIncomeTaxTax.subtract(payedPersonalTax);
				memberPayRecord.setPersonalIncomeTax(personalIncomeTaxTax);
				memberPayRecord.setShoudPayStatrDate(shoudPayStatrDate);
				memberPayRecord.setShouldPayEndDate(shouldPayEndDate);
				memberPayRecord.setPayAmount(commission.getCommission().subtract(mpr.getSalesTax()).subtract(mpr.getConstructionTax()).subtract(mpr.getEducationalSurtax()).subtract(mpr.getLocalEducationalCost()).subtract(mpr.getPersonalIncomeTax()));
				memberPayRecord.setTaxAssessmentTime(orgCommission.getTaxAssessmentTime());
				memberPayRecordService.save(memberPayRecord);
				id=memberPayRecord.getGeneratedKey();
			}else{//更新
				
				id=record.getId();
				BigDecimal amount = record.getAmount();
				BigDecimal actualPayAmount = record.getActualPayAmount();
				memberPayRecord.setAmount(amount.add(commission.getCommission()));
				
				MemberPayRecord mpr = new  MemberPayRecord();
				mpr=memberPayRecordService.personalIncomeTaxTax(memberPayRecord);
				
				memberPayRecord.setActualPayAmount(actualPayAmount.add(commission.getPayAmount()));
				memberPayRecord.setSalesTax(mpr.getSalesTax());
				memberPayRecord.setConstructionTax(mpr.getConstructionTax());
				memberPayRecord.setEducationalSurtax(mpr.getEducationalSurtax());
				memberPayRecord.setLocalEducationalCost(mpr.getLocalEducationalCost());
				//重新计算个人所得税
				MemberPayRecord tmpr = new  MemberPayRecord();
				amount = amount.add(tAmount);
				amount = amount.add(commission.getCommission());
				tmpr.setAmount(amount);
				tmpr=memberPayRecordService.personalIncomeTaxTax(tmpr);
				BigDecimal personalIncomeTaxTax = tmpr.getPersonalIncomeTax();
				personalIncomeTaxTax = personalIncomeTaxTax.subtract(payedPersonalTax);
				memberPayRecord.setPersonalIncomeTax(personalIncomeTaxTax);
				memberPayRecord.setPayAmount((memberPayRecord.getAmount()).subtract(memberPayRecord.getSalesTax()).subtract(memberPayRecord.getConstructionTax()).subtract(memberPayRecord.getEducationalSurtax()).subtract(memberPayRecord.getLocalEducationalCost()).subtract(memberPayRecord.getPersonalIncomeTax()));
				memberPayRecord.setShoudPayStatrDate(shoudPayStatrDate);
				memberPayRecord.setShouldPayEndDate(shouldPayEndDate);
				memberPayRecord.setMemberID(commission.getMemberID());
				memberPayRecord.setId(id);
				memberPayRecordService.updateTax(memberPayRecord);
			}
			
			MemberPayDetail memberPayDetail = new MemberPayDetail();
			memberPayDetail.setMemberPayID(id);
			memberPayDetail.setCommissionID(commission.getId());
			memberPayDetail.setPayAmount(commission.getCommission());
			memberPayDetailService.save(memberPayDetail);
		// 理财顾问在机构中
		} else {
			//机构ID		
			Long teamID = commission.getTeamID();
			Member member = new Member();
			member.setId(teamID);
			member = memberService.memberOrgForCommission(member);	
			commission.setTeamID(member.getTeamID().longValue());
			commissionFeeService.save(commission);
		}
	}

	@Override
	public String upload(Commission entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(Commission entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

}
