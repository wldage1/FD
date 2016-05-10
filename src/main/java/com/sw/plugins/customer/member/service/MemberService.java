package com.sw.plugins.customer.member.service;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jxl.write.WritableWorkbook;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.util.ExportExcel;

/**
 * Service实现类 -
 */

public class MemberService extends CommonService<Member> {
	
	/**
	 * 获取需要发送消息的理财师
	 */
	public Map<String, Object> sendMessageMember(Member entity) throws Exception {
		List<Member> resultList = null;
		Map<String, Object> map = new Hashtable<String, Object>();
		//分页列表
		resultList = getSendMessageMemberList(entity);
		// 记录数
		long record = this.getSendMessageMemberRecordCount(entity);
		// 页数
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());

		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<Member> getSendMessageMemberList(Member entity) throws Exception {
		return (List<Member>) super.getRelationDao().selectList("member.select_send_message_member", entity);
	}
	
	public Long getSendMessageMemberRecordCount(Member entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("member.select_send_message_member_count", entity);
	}
	
	/**
	 * 获取理财师的MAP集合
	 */
	public Map<String, Object> getGrid(Member entity) throws Exception {
		List<Member> resultList = null;
		Map<String, Object> map = new Hashtable<String, Object>();
		//分页列表
		resultList = getPaginatedList(entity);
		// 记录数
		long record = this.getRecordCount(entity);
		// 页数
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());

		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	/**
	 * 获取机构理财师的MAP集合
	 */
	public Map<String, Object> getGridForTeamOrOrgmember(Member entity) throws Exception {
		List<Member> resultList = null;
		Map<String, Object> map = new Hashtable<String, Object>();
		//分页列表
		resultList = getPaginatedListForOrgmember(entity);
		// 记录数
		long record = this.getRecordCountForTeamOrOrgmember(entity);
		// 页数
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());

		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	/**
	 * 获取团队理财师的MAP集合
	 */
	public Map<String, Object> getGridForTeammember(Member entity) throws Exception {
		List<Member> resultList = null;
		Map<String, Object> map = new Hashtable<String, Object>();
		//分页列表
		resultList = getPaginatedListForTeammember(entity);
		// 记录数
		long record = this.getRecordCountForTeamOrOrgmember(entity);
		// 页数
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());

		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	/**
	 * 获取订单理财师的MAP集合
	 */
	public Map<String, Object> getGridForOrdermember(Member entity) throws Exception {
		List<Member> resultList = null;
		Map<String, Object> map = new Hashtable<String, Object>();
		//分页列表
		resultList = getPaginatedListForOrdermember(entity);
		// 记录数
		long record = super.getRelationDao().getCount("member.select_count_for_order_member", entity);
		// 页数
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());

		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
    //理财师的分页查询
	@SuppressWarnings("unchecked")
	@Override
	public List<Member> getPaginatedList(Member entity) throws Exception {
		return (List<Member>) super.getRelationDao().selectList("member.select_paginated", entity);
	}
	//理财师的统计
	@Override
	public Long getRecordCount(Member entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("member.count", entity);
	}
	  //团队理财师的分页查询
	@SuppressWarnings("unchecked")
	public List<Member> getPaginatedListForTeammember(Member entity) throws Exception {
		return (List<Member>) super.getRelationDao().selectList("member.select_paginated_for_team_member", entity);
	}
	  //机构理财师的分页查询
	@SuppressWarnings("unchecked")
	public List<Member> getPaginatedListForOrgmember(Member entity) throws Exception {
		return (List<Member>) super.getRelationDao().selectList("member.select_paginated_for_org_member", entity);
	}
	  //订单理财师的分页查询
	@SuppressWarnings("unchecked")
	public List<Member> getPaginatedListForOrdermember(Member entity) throws Exception {
		return (List<Member>) super.getRelationDao().selectList("member.select_paginated_for_order_member", entity);
	}
	//机构理财师的统计
	public Long getRecordCountForTeamOrOrgmember(Member entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("member.count_for_team_or_org_member", entity);
	}



	@Override
	public void init(InitData initData) throws Exception {
		
	}




	/**
	 * 删除理财师(假删除)
	 * 
	 * @param provider
	 */
	@Override
	public void delete(Member entity) throws Exception {
		entity.setDelStatus(2);
		getRelationDao().delete("member.update", entity);
	}




	/**
	 * 删除多个理财师(假删除)
	 * 
	 * @param provider
	 */
	@Override
	public void deleteByArr(Member entity) throws Exception {
		if(entity.getIds() != null){
			for(String id : entity.getIds()){
				entity.setId(Long.valueOf(id));
				entity.setDelStatus(2);
				getRelationDao().delete("member.update", entity);
			}
		}
	}
	public void updateTeamToOrg(Member entity) throws Exception {
		getRelationDao().delete("member.updateTeamToOrg", entity);
	}



	@Override
	public Object download(Member entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<Member> getList(Member entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



	
	@Override
	public Member getOne(Member entity) throws Exception {
		// TODO Auto-generated method stub
		return (Member) super.getRelationDao().selectOne("member.select_for_check", entity);
	}
	
	public Member getCardInfo(Member entity) throws Exception {
		// TODO Auto-generated method stub
		return (Member) super.getRelationDao().selectOne("member.select_cardInfo", entity);
	}
	
	public Member getOneForDetail(Member entity) throws Exception {
		// TODO Auto-generated method stub
		return (Member) super.getRelationDao().selectOne("member.select", entity);
	}




	@Override
	public void save(Member entity) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void update(Member entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("member.update", entity);	
	}
	public void updateStatusForOrg(Member entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("member.updateStatusForOrg", entity);	
	}
	public void updateTeamId(Member entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("member.updateTeamId", entity);	
	}
	public void updateAnnualBack(Member entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("member.updateAnnualBack", entity);	
	}
	public void updateStatus(Member entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("member.updateStatus", entity);	
	}
	public void updateForOrgOrTeam(Member entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("member.updateForOrgOrTeam", entity);	
	}
	//查询所有理财师
    @SuppressWarnings("rawtypes")
	public List selectSendMessageAllMember (Member entity) throws Exception{
    	return super.getRelationDao().selectList("member.select_send_message_all_member",entity);
    }
    //查询组织下的所有理财师
    @SuppressWarnings("rawtypes")
	public List selectteamOrOrgmember (Member entity) throws Exception{
    	return  super.getRelationDao().selectList("member.select_teamOrOrg_member",entity);
    }
    
  //根据条件检索理财师
    @SuppressWarnings("rawtypes")
	public List selectSendMessageMemberBySearch (Member entity) throws Exception{
    	return super.getRelationDao().selectList("member.select_send_message_member_by_search",entity);
    }


	@Override
	public String upload(Member entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	//判断理财师是否属于机构
	public Member memberIsOrg(Member entity) throws Exception {
		return (Member) super.getRelationDao().selectOne("member.select_member_is_org", entity);
	}
	
	public Member memberOrgForCommission(Member entity) throws Exception {
		return (Member) super.getRelationDao().selectOne("member.select_member_org_for_commission", entity);
	}
	
	//根据机构ID查询机构管理者
	public Member memberOrgType(Member entity) throws Exception {
		return (Member) super.getRelationDao().selectOne("member.select_member_org", entity);
	}
	
	public Member getMemberContact(Member member) throws Exception {
		return (Member) getRelationDao().selectOne("member.select_member_contact", member);
	}
	
	@SuppressWarnings("unchecked")
	public void exportMember(Member member,String colName, String colModel, WritableWorkbook excel) throws Exception {
		List<Map<String, Object>> list = (List<Map<String, Object>>)super.getRelationDao().selectList("member.select_export", member) ;
		String[] colNames = colName.split(",");
		String[] colModels = colModel.split(",");
		int colNameNum = colNames.length;
		int colModelNum = colModels.length;
		
		Map<String, Map<String, Object>> paramMap = new HashMap<String, Map<String,Object>>() ;
		paramMap.put("status", getInitData().getMemberStatus()) ;
		ExportExcel exportExcel = new ExportExcel();
		excel = exportExcel.createExcel("理财师顾问", colNames, colNameNum, colModels, colModelNum, list, excel, paramMap);
	}
}