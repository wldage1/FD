package com.sw.plugins.customer.member.service;



import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.member.entity.MemberTeamOrOrgHistory;



/**
 * Service实现类 -
 */

public class MemberTeamOrOrgHistoryService extends CommonService<MemberTeamOrOrgHistory> {
	@Override
	public void delete(MemberTeamOrOrgHistory entity) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteByArr(MemberTeamOrOrgHistory entity) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object download(MemberTeamOrOrgHistory entity,
			HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Map<String, Object> getGrid(MemberTeamOrOrgHistory entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<MemberTeamOrOrgHistory> getList(MemberTeamOrOrgHistory entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MemberTeamOrOrgHistory getOne(MemberTeamOrOrgHistory entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<MemberTeamOrOrgHistory> getPaginatedList(
			MemberTeamOrOrgHistory entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Long getRecordCount(MemberTeamOrOrgHistory entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void save(MemberTeamOrOrgHistory entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().insert("memberTeamOrOrgHistory.insertHistory", entity);
	}
	@Override
	public void update(MemberTeamOrOrgHistory entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("memberTeamOrOrgHistory.update", entity);	
	}
	public void updateForStatus(MemberTeamOrOrgHistory entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("memberTeamOrOrgHistory.updateForStatus", entity);	
	}
	@Override
	public String upload(MemberTeamOrOrgHistory entity,
			HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

		
}