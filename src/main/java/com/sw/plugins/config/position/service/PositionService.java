package com.sw.plugins.config.position.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.config.position.entity.Position;

public class PositionService extends CommonService<Position> {

	private static Logger logger = Logger.getLogger(PositionService.class);

	@Override
	public void save(Position entity) throws Exception {
		getRelationDao().insert("position.insert", entity);
	}

	@Override
	public void update(Position entity) throws Exception {
		getRelationDao().update("position.update", entity);
	}

	@Override
	public Long getRecordCount(Position entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Position> getList(Position entity) throws Exception {
		return (List<Position>) getRelationDao().selectList("position.select", entity);
	}

	@Override
	public List<Position> getPaginatedList(Position entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Position entity) throws Exception {
		getRelationDao().delete("position.delete", entity);
	}

	@Override
	public void deleteByArr(Position entity) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Position getOne(Position entity) throws Exception {

		return (Position) getRelationDao().selectOne("position.select", entity);
	}

	@Override
	public String upload(Position entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(Position entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		logger.debug(" website position info initializing , please waiting...");
		List<Position> positionList = initData.getSitePosition();
		if (positionList != null) {
			for (Position position : positionList) {
				long count = super.getRelationDao().getCount("position.count_by_code", position);
				if (count == 0) {
					super.getRelationDao().insert("position.insert", position);
				}else{
					super.getRelationDao().update("position.update_by_code", position);
				}
			}
		}
		logger.debug("website position initialize finished");
	}

	@Override
	public Map<String, Object> getGrid(Position entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
