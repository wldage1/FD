package com.sw.plugins.incentivefee.incentive.service;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.config.websitesetting.service.WebsiteSettingService;
import com.sw.plugins.incentivefee.incentive.entity.StageParameter;

@Service
public class StageParameterService extends CommonService<StageParameter>{
	
	private static Logger logger = Logger.getLogger(WebsiteSettingService.class);
	
	@Override
	public void save(StageParameter entity) throws Exception {
		super.getRelationDao().insert("stageParameter.insert", entity);
	}

	@Override
	public void update(StageParameter entity) throws Exception {
		super.getRelationDao().update("stageParameter.update", entity);
	}

	@Override
	public Long getRecordCount(StageParameter entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StageParameter> getList(StageParameter entity) throws Exception {
		return (List<StageParameter>)super.getRelationDao().selectList("stageParameter.select_list", entity);
	}

	@Override
	public List<StageParameter> getPaginatedList(StageParameter entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(StageParameter entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(StageParameter entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StageParameter getOne(StageParameter entity) throws Exception {
		// TODO Auto-generated method stub
		return (StageParameter)super.getRelationDao().selectOne("stageParameter.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(StageParameter entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<StageParameter> resultList = getList(entity);
		map.put("rows", resultList);
		return map;
	}

	@Override
	public String upload(StageParameter entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(StageParameter entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		logger.info("stageParameter info initializing");
		StageParameter stage;
		Map<String, Object> stageMap = initData.getStageParameter();
		Iterator<Entry<String, Object>> ite = stageMap.entrySet().iterator();
		while(ite.hasNext()){
			stage = new StageParameter();
			Entry<String, Object> entry = (Entry<String, Object>) ite.next();
			stage.setCode((String) entry.getValue());
			StageParameter sp = getOne(stage);
			if (sp != null) {
				continue;
			} else {
				stage.setValue("");
				save(stage);
			}
		}
		logger.info("stageParameter info initialize finished");
	}

}
