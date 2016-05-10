package com.sw.plugins.incentivefee.incentive.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.data.entity.BaseEntity;
import com.sw.core.exception.DetailException;
import com.sw.plugins.incentivefee.incentive.entity.RankRewardScore;
import com.sw.plugins.incentivefee.incentive.entity.RebateParameter;
import com.sw.plugins.incentivefee.incentive.entity.StageParameter;
import com.sw.plugins.incentivefee.incentive.service.RankRewardScoreService;
import com.sw.plugins.incentivefee.incentive.service.RebateParameterService;
import com.sw.plugins.incentivefee.incentive.service.StageParameterService;
import com.sw.util.DateUtil;

@Controller
public class IncentiveController extends BaseController<BaseEntity>{
	
	private static Logger logger = Logger.getLogger(IncentiveController.class);
	
	@Resource
	private StageParameterService stageParameterService;
	@Resource
	private RebateParameterService rebateParameterService;
	@Resource
	private RankRewardScoreService rankRewardScoreService;
	
	@RequestMapping("/incentivefee/incentive")
	public CommonModelAndView initIncentive(StageParameter stageParameter,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,stageParameter, messageSource);
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
			commonModelAndView.addObject("stageTimeMapSize", 0);
		}else{
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
			commonModelAndView.addObject("stageTimeMapSize", list.size());
			commonModelAndView.addObject("stageTime", list.get(0).get("stageKey"));
		}
		commonModelAndView.addObject("stageParameterName",this.initData.getStageParameterName());
		return commonModelAndView;
	}
	
	/**
	 * 阶段参数集合查询
	 * 
	 * @param stageParameter
	 * @param request
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/stageGrid")
	public CommonModelAndView stageParameterJson(StageParameter stageParameter,HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			map = stageParameterService.getGrid(stageParameter);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, stageParameter, request);
	}
	
	/**
	 * 修改阶段参数
	 * 
	 * @param stageParameter
	 * @param request
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/saveStage")
	public CommonModelAndView saveStage(StageParameter stageParameter,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
			try {
				stageParameterService.update(stageParameter);
				map.put(this.STATUS, this.STATUS_SUCCESS);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
				map.put(this.STATUS, this.STATUS_FALSE);
			}
		return new CommonModelAndView("jsonView",map);
	}
	
	/**
	 * 返利参数配置列表数据
	 * @param rebate
	 * @param request
	 * @param reponse
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/rebateGrid")
	public CommonModelAndView getRebateList(RebateParameter rebate,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = null;
		try {
			map = rebateParameterService.getGrid(rebate);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, rebate, request);
	} 
	
	/**
	 * 返利参数配置气泡验证
	 * 
	 * @param rebateParameter
	 * @param result
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/rebateValid")
	public CommonModelAndView rebateValid(@Valid RebateParameter rebateParameter,
			BindingResult result, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		if(!Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?").matcher(rebateParameter.getServiceFeeRat().toString()).matches()){
			result.rejectValue("serviceFeeRat", "regex");
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}				
		return commonModelAndView;
	}
	
	/**
	 * 返利参数配置表保存操作
	 * @param rebate
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/saveRebate")
	public CommonModelAndView saveRebates(RebateParameter rebate,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
			try {
				rebateParameterService.save(rebate);
				map.put(this.STATUS, this.STATUS_SUCCESS);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
				map.put(this.STATUS, this.STATUS_FALSE);
			}
		return new CommonModelAndView("jsonView",map);
	}
	
	/**
	 * 返利参数配置列表删除操作
	 * @param rebate
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/deleteRebate")
	public CommonModelAndView deleteRebate(RebateParameter rebate,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		if(rebate.getId()!=null){
			try {
				rebateParameterService.delete(rebate);
				map.put(this.STATUS, this.STATUS_SUCCESS);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
				map.put(this.STATUS, this.STATUS_FALSE);
			}
		}
		return new CommonModelAndView("jsonView",map);
	}
	
	/**
	 * 返利参数配置列表修改操作
	 * @param rebate
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/modifyRebate")
	public CommonModelAndView modifyRebate(RebateParameter rebate,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		if(rebate.getId()!=null){
			try {
				rebateParameterService.update(rebate);
				map.put(this.STATUS, this.STATUS_SUCCESS);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
				map.put(this.STATUS, this.STATUS_FALSE);
			}
		}
		return new CommonModelAndView("jsonView",map);
	}
	
	/**
	 * 阶段排名奖励分值配置列表数据
	 * @param rank
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/rankGrid")
	public CommonModelAndView getRankRewardScoreList(RankRewardScore rank,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = null;
		try {
			map = rankRewardScoreService.getGrid(rank);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, rank, request);
	}
	
	/**
	 * 阶段排名奖励分值配置气泡验证
	 * 
	 * @param rankRewardScore
	 * @param result
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/rankValid")
	public CommonModelAndView rankValid(@Valid RankRewardScore rankRewardScore,
			BindingResult result, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}				
		return commonModelAndView;
	}
	
	/**
	 * 阶段排名奖励分值配置列表保存操作
	 * @param rank
	 * @param request
	 * @param reponse
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/saveRank")
	public CommonModelAndView saveRank(RankRewardScore rank,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			rankRewardScoreService.save(rank);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			map.put(this.STATUS, this.STATUS_FALSE);
		}
		return new CommonModelAndView("jsonView",map);
	}
	
	/**
	 * 阶段排名奖励分值配置列表删除操作
	 * @param rank
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/deleteRank")
	public CommonModelAndView deleteRank(RankRewardScore rank,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		if(rank.getId()!=null){
			try {
				rankRewardScoreService.delete(rank);
				map.put(this.STATUS, this.STATUS_SUCCESS);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
				map.put(this.STATUS, this.STATUS_FALSE);
			}
		}
		return new CommonModelAndView("jsonView",map);
	}
	
	/**
	 * 阶段排名奖励分值配置列表修改操作
	 * @param rank
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/modifyRank")
	public CommonModelAndView modifyRank(RankRewardScore rank,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		if(rank.getId()!=null){
			try {
				rankRewardScoreService.update(rank);
				map.put(this.STATUS, this.STATUS_SUCCESS);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
				map.put(this.STATUS, this.STATUS_FALSE);
			}
		}
		return new CommonModelAndView("jsonView",map);
	}
	
	
	/**
	 * 更新阶段参数配置值  传code 和value
	 * @param stage
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/incentivefee/incentive/updateStage")
	public CommonModelAndView modifyStage(StageParameter stage,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		int i = 0;
		if(stage.getCode()!=null){
			try {
				String[] codes = stage.getCode().split(",");
				String[] values = stage.getValue().split(",");
				for(String s : codes){
					stage.setCode(s);
					stage.setValue(values[i]);
					stageParameterService.update(stage);
					i++;
				}
				map.put(this.STATUS, this.STATUS_SUCCESS);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
				map.put(this.STATUS, this.STATUS_FALSE);
			}
		}
		return new CommonModelAndView("jsonView",map);
	}
	
	
	
	@Override
	public String uploadfile(BaseEntity entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(BaseEntity entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(BaseEntity entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(BaseEntity entity, BindingResult result,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
