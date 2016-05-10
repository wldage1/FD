package com.sw.plugins.incentivefee.organizationfeemanage.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.commission.adjust.entity.Commission;
import com.sw.plugins.commission.adjust.service.AdjustService;
import com.sw.plugins.incentivefee.organizationfeemanage.entity.IncentiveFeeDetail;
import com.sw.plugins.incentivefee.organizationfeemanage.entity.IncentiveOrderHistory;
import com.sw.plugins.incentivefee.organizationfeemanage.service.IncentiveFeeDetailService;
import com.sw.plugins.incentivefee.organizationfeemanage.service.IncentiveOrderHistoryService;
import com.sw.plugins.incentivefee.incentive.entity.StageParameter;
import com.sw.plugins.incentivefee.incentive.service.StageParameterService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.DateUtil;

@Controller
public class OrganizationFeeManageController extends BaseController<IncentiveFeeDetail>{
	
	private static Logger logger = Logger.getLogger(OrganizationFeeManageController.class);
	
	@Resource
	private IncentiveFeeDetailService incentiveFeeDetailService;
	
	@Resource
	private IncentiveOrderHistoryService incentiveOrderHistoryService;
	
	@Resource
	private StageParameterService stageParameterService;
	
	@Resource
	private AdjustService adjustService;
	
	@Resource
	private OrganizationService organizationService;
	
	@Resource
	private UserLoginService userLoginService;
	
	@RequestMapping("incentivefee/organizationfeemanage")
	public CommonModelAndView list(IncentiveFeeDetail entity,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		StageParameter stage = new StageParameter();
		String stageTimeCode = initData.getStageParameter().get("1").toString();
		String beginTimeCode = initData.getStageParameter().get("2").toString();
		stage.setCode(stageTimeCode);
		String stageValue = null;
		String beginTimeValue = null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			stage = stageParameterService.getOne(stage);
			stageValue = stage.getValue();
			stage.setCode(beginTimeCode);
			stage = stageParameterService.getOne(stage);
			beginTimeValue = stage.getValue();
			
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		if(stageValue == null || beginTimeValue == null){
			commonModelAndView.addObject("stageTimeMap", null);
			return commonModelAndView;
		}
		Calendar date = Calendar.getInstance();
		int year = date.get(Calendar.YEAR);
		int mouth = date.get(Calendar.MONTH);
		String currentMouth = year+"-"+ (mouth > 9?mouth:"0" + mouth);
		//当前日期转换成date格式
		Date beginDate = DateUtil.stringToDate(beginTimeValue + "-01", DateUtil.ISO_EXPANDED_DATE_FORMAT);
		String beginDateStr = DateUtil.dateToString(beginDate);
		while(true){
			String stageDate = "";
			String stageKey = "";
			//当期月份增加阶梯月份后的日期
			Date increasenDate = DateUtil.dateIncreaseByMonth(beginDate, Integer.parseInt(stageValue));
			String increasenDateStr = DateUtil.dateToString(increasenDate);
			stageKey = "'" + beginDateStr + "' and '" + increasenDateStr + "'";
			Map<String, Object> map = new HashMap<String, Object>();
			stageDate = beginDateStr + " -- " + increasenDateStr;
			//存储阶段时间到map对象
			map.put("stageKey", stageKey);
			map.put("stageDate", stageDate);
			list.add(map);
			beginDate = increasenDate;
			beginDateStr = DateUtil.dateToString(beginDate);
			//如果阶段增加后的月份大于等于当前月份 就结束循环
			if (increasenDateStr.compareTo(currentMouth) > 0 ){
				break;
			}
		}
		//根据拿到阶段统计时长参数,在页面通过js函数做成 计划发放周期列表,分3个阶段,从当前时间向前推，精确到月
		commonModelAndView.addObject("stageTimeMap", list);
		
		User user = userLoginService.getCurrLoginUser();
		Organization organization = user.getSelfOrg();
		Organization org = new Organization();
		if (organization != null) {
			int isCommission = organization.getIsCommission();
			if (isCommission == 1) {
				org.setId(organization.getId());
			} else {
				org.setId(null);
			}
		}
		List<Organization> orgList = new ArrayList<Organization>();
		//只展示二级居间公司列表
		org.setTreeLevel(2);
		org.setIsCommission(1);
		try {
			orgList = organizationService.getListOnLevel2(org);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("orgList", orgList);
		commonModelAndView.addObject("orgListSize",orgList.size());
		commonModelAndView.addObject("companyID", org.getId());
		return commonModelAndView;
	}
	
	/**
	 * 传 type团队2或机构1和 isPay是否发放   
	 * 主表格显示数据
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("incentivefee/organizationfeemanage/grid")
	public CommonModelAndView json(IncentiveFeeDetail entity,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//从激励费用历史表查询，刚开始是没数据的，通过核算之后才会存在数据
			map = incentiveFeeDetailService.getGrid(entity);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, entity, request);
	}
	
	/**
	 * 子表格显示数据    传teamid stageTime beginTime orgid(居间公司ID)
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("incentivefee/organizationfeemanage/subgrid")
	public CommonModelAndView subJson(Commission commission,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		Short type = commission.getType();
		if(commission.getTeamID()!= null){
			try {
				map = adjustService.getIncentiveGrid(commission,type);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
		return new CommonModelAndView("jsonView", map, commission, request);
	}
	
	/**
	 * 激励费用发放
	 * @param entity  居间公司ID 
	 * @param request
	 * @return
	 */
	@RequestMapping("incentivefee/organizationfeemanage/pay")
	public CommonModelAndView pay(IncentiveOrderHistory entity,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			incentiveOrderHistoryService.pay(entity);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			map.put(this.STATUS, this.STATUS_FALSE);
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 激励费用一键发放
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("incentivefee/organizationfeemanage/onekeypay")
	public CommonModelAndView oneKeyPay(IncentiveOrderHistory entity,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			incentiveOrderHistoryService.oneKeyPay(entity);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			map.put(this.STATUS, this.STATUS_FALSE);
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 核算按钮
	 * @param entity 传companyID type(机构0或团队1)
	 * @param request
	 * @return
	 */
	@RequestMapping("incentivefee/organizationfeemanage/check")
	public CommonModelAndView clickCheck(IncentiveFeeDetail entity,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();

		try {
			//存激励费用历史表
			incentiveFeeDetailService.saveIncentiveDetail(entity);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			map.put(this.STATUS, this.STATUS_FALSE);
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	
	
	@Override
	public String uploadfile(IncentiveFeeDetail entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(IncentiveFeeDetail entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(IncentiveFeeDetail entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(IncentiveFeeDetail entity,
			BindingResult result, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
