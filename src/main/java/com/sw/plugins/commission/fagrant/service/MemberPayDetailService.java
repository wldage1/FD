package com.sw.plugins.commission.fagrant.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.fagrant.entity.MemberPayDetail;

public class MemberPayDetailService extends CommonService<MemberPayDetail> {

	public void saveOrupdate(MemberPayDetail entity) throws Exception {
		if(entity.getId()==null){
			this.save(entity);
		}else{
			this.update(entity);
		}
	}
	
	@Override
	public void save(MemberPayDetail entity) throws Exception {
		super.getRelationDao().update("memberPayDetail.insert", entity);
	}

	@Override
	public void update(MemberPayDetail entity) throws Exception {
		super.getRelationDao().update("memberPayDetail.update", entity);
	}

	@Override
	public Long getRecordCount(MemberPayDetail entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemberPayDetail> getList(MemberPayDetail entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemberPayDetail> getPaginatedList(MemberPayDetail entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(MemberPayDetail entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(MemberPayDetail entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MemberPayDetail getOne(MemberPayDetail entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(MemberPayDetail entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(MemberPayDetail entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(MemberPayDetail entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
