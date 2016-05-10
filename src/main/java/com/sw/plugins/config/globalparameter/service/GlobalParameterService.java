package com.sw.plugins.config.globalparameter.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.contract.entity.Contract;
import com.sw.plugins.commission.contract.entity.ContractAttachment;
import com.sw.plugins.config.globalparameter.entity.GlobalParameter;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;

public class GlobalParameterService extends CommonService<GlobalParameter> {

	private static Logger logger = Logger.getLogger(GlobalParameterService.class);

	@Resource
	private OrganizationService organizationService;

	/**
	 * 获取机构树形方法
	 * 
	 * @param org
	 * @return
	 * @throws Exception
	 */

	public String getOrgTree(Organization org) throws Exception {
		String json = null;
		List<Object> list = new ArrayList<Object>();
		Organization organization = new Organization();
		List<Organization> orgList = new ArrayList<Organization>();
		// 获取机构树形只显示两级
		orgList = organizationService.getListByLevel(organization);
		for (Organization temp : orgList) {
			Map<String, Object> mapOrg = new HashMap<String, Object>();
			mapOrg.put("id", temp.getId());
			mapOrg.put("pId", temp.getParentId());
			mapOrg.put("name", temp.getName());
			list.add(mapOrg);
		}
		json = JSONArray.fromObject(list).toString();
		return json;
	}
	/**
	 * 获取居间公司树形方法
	 * 
	 * @param org
	 * @return
	 * @throws Exception
	 */

	public String getComTree(Organization org) throws Exception {
		String json = null;
		List<Object> list = new ArrayList<Object>();
		Organization organization = new Organization();
		List<Organization> orgList = new ArrayList<Organization>();
		// 获取机构树形只显示两级
		orgList = organizationService.getListByLevelCom(organization);
		for (Organization temp : orgList) {
			Map<String, Object> mapOrg = new HashMap<String, Object>();
			mapOrg.put("id", temp.getId());
			mapOrg.put("pId", temp.getParentId());
			mapOrg.put("name", temp.getName());
			list.add(mapOrg);
		}
		json = JSONArray.fromObject(list).toString();
		return json;
	}

	/**
	 * 
	 * 增加参数保存方法
	 * */
	@Override
	public void save(GlobalParameter entity) throws Exception {
		super.getRelationDao().insert("globalParameter.insert", entity);
	}

	/**
	 * 修改参数值方法
	 * 
	 * */
	@Override
	public void update(GlobalParameter entity) throws Exception {
		super.getRelationDao().update("globalParameter.update", entity);
	}
	
	public void updateByCode(GlobalParameter entity) throws Exception {
		super.getRelationDao().update("globalParameter.update_by_code", entity);
	}

	/**
	 * 查询相同机构下已存在的参数名称数目
	 * */
	@Override
	public Long getRecordCount(GlobalParameter entity) throws Exception {
		return (Long) super.getRelationDao().getCount("globalParameter.count", entity);
	}
	/**
	 * 查询相同组织下已存在的参数代码数目
	 * */
	public Long getForCode(GlobalParameter entity) throws Exception {
		return (Long) super.getRelationDao().getCount("globalParameter.count_for_code", entity);
	}
	/**
	 * 根据机构查询获取到的参数信息
	 * */
	@SuppressWarnings("unchecked")
	@Override
	public List<GlobalParameter> getList(GlobalParameter entity) throws Exception {
		return (List<GlobalParameter>) super.getRelationDao().selectList("globalParameter.select", entity);
	}
	/**
	 * 根据居间公司查询获取到的参数信息
	 * */
	@SuppressWarnings("unchecked")
	public List<GlobalParameter> getListCom(GlobalParameter entity) throws Exception {
		return (List<GlobalParameter>) super.getRelationDao().selectList("globalParameter.selectCom", entity);
	}

	@Override
	public List<GlobalParameter> getPaginatedList(GlobalParameter entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 删除参数方法
	 * 
	 * */
	@Override
	public void delete(GlobalParameter entity) throws Exception {
		super.getRelationDao().delete("globalParameter.delete", entity);
	}

	@Override
	public void deleteByArr(GlobalParameter entity) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询参数值（传orgId和参数code获得）
	 * 
	 * */
	@Override
	public GlobalParameter getOne(GlobalParameter entity) throws Exception {
		return (GlobalParameter) super.getRelationDao().selectOne("globalParameter.select_one", entity);
	}
	
	public GlobalParameter getSysOne(GlobalParameter entity) throws Exception{
		return (GlobalParameter) super.getRelationDao().selectOne("globalParameter.select_sys_one", entity);
	}
	
	/**
	 * 查询居间公司参数值
	 * 
	 * */
	public GlobalParameter getOneCom(GlobalParameter entity) throws Exception {
		return (GlobalParameter) super.getRelationDao().selectOne("globalParameter.select_one_com", entity);
	}

	/**
	 * 获取参数列表方法
	 * 
	 * */
	@Override
	public Map<String, Object> getGrid(GlobalParameter entity) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<GlobalParameter> parameterList = getList(entity);
		map.put("rows", parameterList);
		return map;
	}
	/**
	 * 获取参数列表方法
	 * 
	 * */
	public Map<String, Object> getGridCom(GlobalParameter entity) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<GlobalParameter> parameterList = getListCom(entity);
		map.put("rows", parameterList);
		return map;
	}

	@Override
	public String upload(GlobalParameter entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(GlobalParameter entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	public void saveOrUpdateCom(GlobalParameter entity) throws Exception {
		if (entity != null && entity.getId() != null) {
			// 更新参数信息
			update(entity);
		} else {
			// 保存参数信息
			save(entity);
		}
	}
	@Override
	public void init(InitData initData) throws Exception {
		try {
			logger.info("globalParameter info initializing");
//			GlobalParameter parameter;
//			Map<String, Object> parameterMap = initData.getParameterName();
//			List<Organization> orgList = new ArrayList<Organization>();
//			orgList = (List<Organization>) super.getRelationDao().selectList("organization.select_by_level", new Organization());
//			for (int i = 0; i < orgList.size(); i++) {
//				Iterator<Entry<String, Object>> ite = parameterMap.entrySet().iterator();
//				while (ite.hasNext()) {
//					parameter = new GlobalParameter();
//					Entry<String, Object> entry = (Entry<String, Object>) ite.next();
//					//parameter.setOrgId(orgList.get(i).getId());
//					parameter.setName((String) entry.getValue());
//					parameter.setCode((String) entry.getKey());
//					GlobalParameter gp = (GlobalParameter) getOne(parameter);
//					if (gp != null) {
//						continue;
//					} else {
//						parameter.setValue("");
//						save(parameter);
//					}
//				}
//			}
			Map<String, Object> globalParameter = initData.getGlobalParameter();
			for(String key : globalParameter.keySet()){
				GlobalParameter gp = new GlobalParameter();
//				gp.setCode(globalParameter.get(key).toString());
//				gp.setValue(key);
//				delete(gp);
//				save(gp);
				gp.setCode(key);
				gp = this.getSysOne(gp);
				if(gp != null){
					continue;
				}else{
					gp = new GlobalParameter();
					gp.setName(globalParameter.get(key).toString());
					gp.setCode(key);
					gp.setValue("");
					save(gp);
				}
			}
			logger.info("globalParameter info initialize finished");
		} catch (Exception e) {
			logger.error("globalParameter info initialize fail");
			logger.error(DetailException.expDetail(e, this.getClass()));
		}

	}

}
