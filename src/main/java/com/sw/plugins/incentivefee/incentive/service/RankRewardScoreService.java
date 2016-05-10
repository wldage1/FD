package com.sw.plugins.incentivefee.incentive.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.incentivefee.incentive.entity.RankRewardScore;

@Service
public class RankRewardScoreService extends CommonService<RankRewardScore>{

	@Override
	public void save(RankRewardScore entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().insert("rankRewardScore.insert", entity);
	}

	@Override
	public void update(RankRewardScore entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("rankRewardScore.update", entity);
	}
	
	public void saveAll(RankRewardScore entity) throws Exception {
		List<RankRewardScore> scoreList = entity.getScoreList();
		this.deleteAll(entity);
		if(scoreList!=null){
			for(RankRewardScore rs : scoreList){
				if(rs.getRankTag()!=null){
					this.save(rs);
				}
			}
		}
	}
	
	@Override
	public Long getRecordCount(RankRewardScore entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RankRewardScore> getList(RankRewardScore entity)
			throws Exception {
		// TODO Auto-generated method stub
		return (List<RankRewardScore>)super.getRelationDao().selectList("rankRewardScore.select", entity);
	}

	@Override
	public List<RankRewardScore> getPaginatedList(RankRewardScore entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(RankRewardScore entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().delete("rankRewardScore.delete", entity);
	}
	
	public void deleteAll(RankRewardScore entity) throws Exception{
		super.getRelationDao().delete("rankRewardScore.deleteAll", entity);
	}
	
	@Override
	public void deleteByArr(RankRewardScore entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RankRewardScore getOne(RankRewardScore entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(RankRewardScore entity) throws Exception {
		Map<String,Object> map = new Hashtable<String,Object>();
		List<RankRewardScore> list = getList(entity);
		map.put("rows", list);
		return map;
	}

	@Override
	public String upload(RankRewardScore entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(RankRewardScore entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
