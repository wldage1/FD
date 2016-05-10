package com.sw.plugins.commission.organizationgrant.service;

import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jxl.write.WritableWorkbook;

import com.sw.core.common.Constant;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.organizationgrant.entity.TeamOrOrgPayDetail;
import com.sw.util.ExportExcel;


public class TeamOrOrgPayDetailService extends CommonService<TeamOrOrgPayDetail>{

	@Override
	public void save(TeamOrOrgPayDetail entity) throws Exception {
		super.getRelationDao().insert("teamororgpaydetail.insert", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TeamOrOrgPayDetail> getPaginatedList(TeamOrOrgPayDetail entity) throws Exception {
		return (List<TeamOrOrgPayDetail>) super.getRelationDao().selectList("teamororgpaydetail.select_paginated", entity);
	}
	
	@Override
	public Map<String, Object> getGrid(TeamOrOrgPayDetail entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<TeamOrOrgPayDetail> resultList = getPaginatedList(entity);
		map.put("rows", resultList);
		return map;
	}
	
	/**
	 * 查询支付机构居间费列表集合
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOrgGrid(TeamOrOrgPayDetail entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<TeamOrOrgPayDetail> resultList = getOrgPaginatedList(entity);
		map.put("rows", resultList);
		return map;
	}

	@SuppressWarnings({ "unchecked"})
	public void exportExcel(String colName, String colModel,
			WritableWorkbook excel, TeamOrOrgPayDetail entity) throws Exception {
		List<Map<String, Object>> objList = (List<Map<String, Object>>) super.getRelationDao().selectList("teamororgpaydetail.export_excel",entity);
		if (objList != null) {
			for (Map<String, Object> one : objList) {
				// TSR姓名
				one.put("title1", one.get("TITLE1"));
				one.put("title2",one.get("TITLE2"));
				one.put("title3", one.get("TITLE3"));
				one.put("title4", one.get("TITLE4"));
				one.put("payAmount", one.get("PAYAMOUNT"));
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
	@SuppressWarnings("unchecked")
	public List<TeamOrOrgPayDetail> getOrgPaginatedList(TeamOrOrgPayDetail entity) throws Exception {
		return (List<TeamOrOrgPayDetail>) super.getRelationDao().selectList("teamororgpaydetail.select_org_paginated", entity);
	}
	
	@Override
	public void update(TeamOrOrgPayDetail entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getRecordCount(TeamOrOrgPayDetail entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TeamOrOrgPayDetail> getList(TeamOrOrgPayDetail entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(TeamOrOrgPayDetail entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(TeamOrOrgPayDetail entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TeamOrOrgPayDetail getOne(TeamOrOrgPayDetail entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(TeamOrOrgPayDetail entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(TeamOrOrgPayDetail entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
