package com.sw.plugins.system.organization.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.cooperate.organization.entity.Commission;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.role.entity.Role;
import com.sw.plugins.system.role.service.RoleService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.plugins.system.user.service.UserService;

/**
 * Service实现类 - Service实现类基类
 */

public class OrganizationService extends CommonService<Organization> {

	private static Logger logger = Logger.getLogger(OrganizationService.class);
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;

	/**
	 * 默认初始化,包括用户，机构
	 */
	@Override
	public void init(InitData initData) throws Exception {
		try {
			if (initData != null) {
				logger.debug("organization info initializing");
				List<Map<String, Object>> orgList = initData.getOrganizations();
				if (orgList != null) {
					for (Map<String, Object> orgMap : orgList) {
						String code = orgMap.get("code") == null ? "" : orgMap.get("code").toString();
						String name = orgMap.get("name") == null ? "" : orgMap.get("name").toString();
						String phone = orgMap.get("phone") == null ? "" : orgMap.get("phone").toString();
						String fax = orgMap.get("fax") == null ? "" : orgMap.get("fax").toString();
						String postCode = orgMap.get("postCode") == null ? "" : orgMap.get("postCode").toString();
						String address = orgMap.get("address") == null ? "" : orgMap.get("address").toString();
						String description = orgMap.get("description") == null ? "" : orgMap.get("description").toString();
						String status = orgMap.get("status") == null ? "1" : orgMap.get("status").toString();
						String parentId = orgMap.get("parentId") == null ? "0" : orgMap.get("parentId").toString();
						String parentName = orgMap.get("parentName") == null ? "" : orgMap.get("parentName").toString();
						String treeLevel = orgMap.get("treeLevel") == null ? "1" : orgMap.get("treeLevel").toString();
						String treePath = orgMap.get("treePath") == null ? ",0," : orgMap.get("treePath").toString();
						String isChildNode = orgMap.get("isChildNode") == null ? "1" : orgMap.get("isChildNode").toString();
						Organization organization = new Organization();
						organization.setCode(code);
						organization.setName(name);
						organization.setPhone(phone);
						organization.setFax(fax);
						organization.setPostCode(postCode);
						organization.setAddress(address);
						organization.setDescription(description);
						organization.setStatus(Integer.parseInt(status));
						organization.setParentId(Long.parseLong(parentId));
						organization.setParentName(parentName);
						organization.setTreeLevel(Integer.parseInt(treeLevel));
						organization.setTreePath(treePath);
						organization.setIsChildNode(Integer.parseInt(isChildNode));
						Organization obj = (Organization) getRelationDao().selectOne("organization.select_by_code", organization);
						if (obj == null) {
							// 插入机构数据
							getRelationDao().insert("organization.insert", organization);
							// 根据获取到的机构id，设置机构的treePath信息
							organization.setTreePath(treePath + organization.getGeneratedKey() + ",");
							getRelationDao().update("organization.update_tree_path_by_code", organization);
						} else {
							// 若存在直接修改机构信息
							getRelationDao().update("organization.update_by_code", organization);
						}
					}
				}
				logger.debug("organization info initialize finished");
			}
		} catch (Exception e) {
			String debug = DetailException.expDetail(e, this.getClass());
			logger.debug("organization info initialize fail");
			logger.debug(debug);
		}
	}

	/**
	 * 保存或更新
	 * 
	 * @param organization
	 * @throws Exception
	 */
	public void saveOrUpdate(Organization organization) throws Exception {
		// 如果id为空表示添加
		if (organization.getId() == null) {
			// 将当前机构的级别+1
			organization.setTreeLevel(organization.getTreeLevel() == null ? 1 : (organization.getTreeLevel() + 1));
			// 设置为子节点
			organization.setIsChildNode(1);
			// 插入机构数据
			save(organization);
			// 修改当前机构的id
			organization.setId(organization.getGeneratedKey());
			// 设置path
			String path = (organization.getTreePath() == null || organization.getTreePath().equals("")) ? "," + organization.getGeneratedKey() + "," : organization.getTreePath() + organization.getGeneratedKey() + ",";
			// 修改当前机构path
			organization.setTreePath(path);

			// 如果上级机构id为空，则直接设置上级机构id为0，机构级别为1
			if (organization.getParentId() == null) {
				organization.setParentId(0L);
			} else {
				// 如果IsChildNode已经为0，则不用修改
				if (organization.getIsChildNode() != 0) {
					// 如果选择了上级机构，修改上级机构的IsChildNode属性为0
					Organization tempOrganization = new Organization();
					tempOrganization.setId(organization.getParentId());
					tempOrganization.setIsChildNode(0);
					update(tempOrganization);
				}
			}
			update(organization);

		} else {
			// 新父机构id
			Long newParentId = organization.getParentId();
			// 原机构id
			Long oldParentId = organization.getOldParentId();
			// 如果不相等，则处理原机构IsChildNode属性
			if (newParentId != oldParentId) {
				Organization condOrganization = new Organization();
				// 原父机构id设置，并判断其下是否还存在其他子节点
				condOrganization.setParentId(organization.getOldParentId());
				Object countObj = super.getRelationDao().getCount("organization.select_sub_count_by_parentid", condOrganization);
				long subOrgCount = 0;
				if (countObj instanceof Long) {
					subOrgCount = (Long) countObj;
				}
				// 如果原机构没有子节点，修改此机构的IsChildNode属性为1（代表此节点为叶子节点）
				if (subOrgCount == 1) {
					condOrganization.setId(organization.getOldParentId());
					condOrganization.setParentId(null);
					condOrganization.setIsChildNode(1);
					update(condOrganization);
				}
				// 切换机构，设置当前机构的levee、path属性和当前机构父机构的IsChildNode属性
				// 设置父机构id，为查询条件
				condOrganization.setId(organization.getParentId());
				condOrganization.setParentId(null);
				condOrganization.setIsChildNode(null);
				Object orgObj = getOne(condOrganization);
				if (orgObj instanceof Organization) {
					int childNode = ((Organization) orgObj).getIsChildNode();
					// 如果父机构childNode为1，则修改此属性为0
					if (childNode == 1) {
						((Organization) orgObj).setIsChildNode(0);
						update(((Organization) orgObj));
					}
				}
				// 设置当前机构的level为父机构级别+1
				organization.setTreeLevel(((Organization) orgObj).getTreeLevel() == null ? 1 : ((Organization) orgObj).getTreeLevel() + 1);
				// 设置当前机构path
				String path = ((Organization) orgObj).getTreePath() == null ? "," + organization.getId() + "," : ((Organization) orgObj).getTreePath() + organization.getId() + ",";
				// 设置当前机构新path
				organization.setTreePath(path);
				// 修改当前机构
				update(organization);
				modifySubOrganization(organization);
			} else {
				// 不更改机构level、childnode和path属性
				organization.setIsChildNode(null);
				organization.setTreeLevel(null);
				organization.setTreePath(null);
				// 若相等，直接处理当前机构
				update(organization);
				Organization temp = new Organization();
				temp.setParentId((Long) organization.getId());
				List<Organization> organizationList = getList(temp);
				// 修改子机构
				for (int i = 0; i < organizationList.size(); i++) {
					organizationList.get(i).setParentName(organization.getName());
					update(organizationList.get(i));
				}
			}
		}
	}

	// 递归修改子机构
	public void modifySubOrganization(Organization organization) throws Exception {
		Organization temp = new Organization();
		temp.setParentId((Long) organization.getId());
		List<Organization> subList = null;
		subList = getList(temp);
		if (subList != null) {
			for (int i = 0; i < subList.size(); i++) {
				Organization o = subList.get(i);
				o.setTreeLevel(organization.getTreeLevel() + 1);
				o.setTreePath(organization.getTreePath() + o.getId() + ",");
				o.setParentName(organization.getName());
				update(o);
				modifySubOrganization(o);
			}
		}
	}

	/**
	 * 机构删除
	 * 
	 * @param organization
	 * @throws Exception
	 */
	public void delete(Organization organization) throws Exception {
		organization = getOne(organization);
		super.getRelationDao().delete("organization.delete", organization);
		Organization condOrganization = new Organization();
		condOrganization.setParentId(organization.getParentId());
		List<Organization> list = getList(condOrganization);
		if (list.size() <= 0) {
			condOrganization = new Organization();
			condOrganization.setId(organization.getParentId());
			Organization org = getOne(condOrganization);
			if (org != null) {
				org.setIsChildNode(1);
				update(org);
			}
		}
	}

	/**
	 * 查询机构表格树
	 * 
	 * @param organization
	 * @param nodeid
	 * @return
	 */
	public List<Organization> getOrganizationTreeGrid(Organization organization, String nodeid) throws Exception {
		Organization condOrganization = new Organization();
		List<Organization> orgList = null;
		condOrganization.setParentId(Long.valueOf(nodeid));
		orgList = getList(condOrganization);
		return orgList;
	}

	/**
	 * 根据父机构id查询子机构列表
	 * 
	 * @param parentKey
	 * @return
	 */
	public List<Organization> getSubOrganization(Long parentId) throws Exception {
		List<Organization> orgList = null;
		Organization condOrganization = new Organization();
		condOrganization.setParentId(parentId);
		orgList = getList(condOrganization);
		return orgList;
	}

	/**
	 * 机构列表树
	 * 
	 * @param nodeid
	 * @return
	 */
	@Override
	public Map<String, Object> getGrid(Organization org) throws Exception {
		// 设置是否是居间公司
		int isCommission = org.getIsCommission();
		
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Organization> orgList = new ArrayList<Organization>();
		// 获取当前登录用户信息
		User user = userLoginService.getCurrLoginUser();
		Organization usrOrg = user.getSelfOrg();
		if (user != null) {
			// 系统级超强管理员加载系统所有机构
			if (user.getType() == 0 || (usrOrg.getIsCommission() == 0 && isCommission == 1)){
				Organization sysorg = new Organization();
				sysorg.setIsCommission(isCommission);
				orgList = (List<Organization>) getList(sysorg);
			} else {
				Organization torg = new Organization();
				torg.setIsCommission(isCommission);
				// 普通使用者返回所属机构
				Organization uorg = user.getSelfOrg();
				torg.setParentId(uorg.getParentId());
				
				Integer level = uorg.getTreeLevel();
				if (level != 1) {
					torg.setParentId(uorg.getId());
				}
				orgList = (List<Organization>) this.getListByPath(torg);
			}
		}
		map.put("rows", orgList);
		return map;
	}

	/**
	 * 机构下拉树
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getSelectTree(Organization org) throws Exception {
		// 设置是否是居间公司
		int isCommission = org.getIsCommission();
		
		List<Object> list = new ArrayList<Object>();
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Organization> orgList = new ArrayList<Organization>();
		Organization organization;

		// 获取当前登录用户信息
		User user = userLoginService.getCurrLoginUser();
		if (user == null) {
			return map;
		}

		if (org.getParentId() == null) {
			// 系统级超强管理员 (Type = 0 && OrgId = '') 加载系统所有机构 并设置顶级机构信息
			if (user.getType() == 0) {
				organization = new Organization();
				organization.setParentId(0L);
				organization.setIsCommission(isCommission);
				orgList = (List<Organization>)this.getList(organization);
			} else {
				// 一般使用者返回所属机构
				Organization uorg = user.getSelfOrg();
				if(uorg != null){
					Integer isCo = uorg.getIsCommission();
					// 判断用户是否是居间公司用户
					if(isCo.equals(0) && isCommission == 1){
						organization = new Organization();
						organization.setParentId(0L);
						organization.setIsCommission(isCommission);
						orgList = (List<Organization>)this.getList(organization);
					}else{
						organization = new Organization();
						organization.setIsCommission(isCommission);
						organization.setId(user.getOrgId());
						Organization tOrg = getOneWithCoCondition(organization);
						orgList.add(tOrg);
					}
				}
			}
		} else {
			organization = new Organization();
			organization.setIsCommission(isCommission);
			organization.setParentId(org.getParentId());
			orgList = (List<Organization>) getList(organization);
		}

		if (orgList != null && orgList.size() > 0) {
			for (Organization condOrg : orgList) {
				Map<String, Object> maporg = new Hashtable<String, Object>();
				maporg.put("id", condOrg.getId());
				maporg.put("name", condOrg.getName());
				maporg.put("pId", condOrg.getParentId());
				maporg.put("path", condOrg.getTreePath());
				maporg.put("olevel", condOrg.getTreeLevel());
				maporg.put("childNode", condOrg.getIsChildNode());
				if (condOrg.getIsChildNode() == 1) {
					maporg.put("isParent", "false");
				} else {
					maporg.put("isParent", "true");
				}
				list.add(maporg);
			}
		}
		map.put("stree", list);
		return map;
	}

	/**
	 * 获取用户机构（数据权限）下拉列表树
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getUserOwnsOrgTreeMap(Organization org) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Map<String, Object>> orgList = null;
		orgList = userService.getUserOrgAndSubOrgTreeList(new User());
		if (orgList != null) {
			map.put("stree", orgList);
		} else {
			map.put("stree", new ArrayList<Map<String, Object>>());
		}
		orgList = null;
		return map;
	}

	/**
	 * 获取用户二级机构（数据权限）下拉列表树
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getSecendLevelOrgTreeMap(Organization org) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Map<String, Object>> orgList = null;
		User LoginUser = userLoginService.getCurrLoginUser();
		orgList = userService.getUserSecendOrgTreeList(LoginUser);
		if (orgList != null) {
			map.put("stree", orgList);
		} else {
			map.put("stree", new ArrayList<Map<String, Object>>());
		}
		orgList = null;
		return map;
	}

	/**
	 * 获取某个机构所属的二级机构
	 * 
	 * @param id
	 * @return
	 */
	public Organization getSecendLevelOrg(Organization organization) throws Exception {
		Organization rorg = null;
		Organization org = this.getOne(organization);
		if (org != null && org.getTreeLevel() == 2) {
			return org;
		}
		String treeIds;
		if (org != null) {
			String treePath = org.getTreePath();
			treeIds = org.getTreePath().substring(1, treePath.length() - 1);
			organization.setIds(treeIds.split(","));
			List<Organization> orgList = this.get2ndOrgByIds(organization);
			if (orgList != null) {
				for (Organization torg : orgList) {
					if (torg.getTreeLevel() == 2) {
						return torg;
					}
				}
			}
		}
		return rorg;
	}

	/**
	 * 获取业务种类中使用的一二级机构树
	 * 
	 * @param org
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getTreeForBusiness(Organization org) throws Exception {
		List<Object> list = new ArrayList<Object>();
		List<Organization> orgList = new ArrayList<Organization>();

		orgList = (List<Organization>) super.getRelationDao().selectList("organization.select_org_for_business", org);

		if (orgList != null && orgList.size() > 0) {
			for (Organization condOrg : orgList) {
				Map<String, Object> maporg = new Hashtable<String, Object>();
				maporg.put("id", condOrg.getId());
				maporg.put("pId", condOrg.getParentId());
				maporg.put("name", condOrg.getName());
				list.add(maporg);
			}
		}
		return JSONArray.fromObject(list).toString();
	}

	/**
	 * 获取相匹配的二级机构信息
	 * 
	 * @param org
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Organization> get2ndOrgByIds(Organization org) throws Exception {
		return (List<Organization>) super.getRelationDao().selectList("organization.select_2ndorg_by_ids", org);
	}

	public String upload(Organization baseEntity, HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String realTreePath = request.getSession().getServletContext().getRealPath("/upload/");
		if (realTreePath == null) {
			realTreePath = request.getSession().getServletContext().getResource("/").toString();
		}
		String fileTreePathList = null;
		String realFileName = "";
		String tempTreePath = "";
		Iterator<String> iterator = multipartRequest.getFileNames();
		while (iterator.hasNext()) {
			String fileName = iterator.next().toString();
			File nfile = new File(realTreePath + File.separator + tempTreePath, realFileName);
			// 文件全名
			fileTreePathList = File.separator + tempTreePath + File.separator + realFileName;
			List<MultipartFile> flist = multipartRequest.getFiles(fileName);
			for (MultipartFile mfile : flist) {
				byte[] bytes;
				bytes = mfile.getBytes();
				if (bytes.length != 0) {
					mfile.transferTo(nfile);
				}
			}
		}
		return fileTreePathList;
	}

	public Long checkConstraints(Organization entity) throws Exception {
		Long count;

		// 检查其下是否有用户
		count = 0L;
		User user = new User();
		user.setOrgId((Long) entity.getId());
		count = (Long) userService.getRecordCount(user);
		if (count > 0L) {
			return 1L;
		}

		// 检查其下是否有角色
		count = 0L;
		Role role = new Role();
		role.setOrgId((Long) entity.getId());
		count = roleService.getRecordCount(role);

		if (count > 0L) {
			return 1L;
		}

		return 0l;
	}
	
	/**
	 * 获取所在居间公司水印
	 * 
	 * @param organization
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGridWatermark(Organization organization) throws Exception{
		Map<String, Object> map = new Hashtable<String, Object>();
		User user = userLoginService.getCurrLoginUser();
		if(user.getType() == 0) {
			organization = new Organization();
		}else{
			organization = user.getSelfOrg();
		}
		List<Organization> resultList = (List<Organization>) super.getRelationDao().selectList("organization.select_org_watermark", organization);
		map.put("rows", resultList);
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<Organization> getOrgWithUserList(Organization entity) throws Exception {
		return (List<Organization>) super.getRelationDao().selectList("organization.select_org_with_user", entity);
	}

	@Override
	public Object download(Organization entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void save(Organization entity) throws Exception {
		entity.setDescription(HtmlUtils.htmlEscape(entity.getDescription()));
		super.getRelationDao().insert("organization.insert", entity);
	}

	@Override
	public void update(Organization entity) throws Exception {
		entity.setDescription(HtmlUtils.htmlEscape(entity.getDescription()));
		super.getRelationDao().update("organization.update", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Organization> getList(Organization entity) throws Exception {
		return (List<Organization>) super.getRelationDao().selectList("organization.select", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<Organization> getListWithoutTopOrg(Organization entity) throws Exception {
		return (List<Organization>) super.getRelationDao().selectList("organization.select_without_toporg", entity);
	}

	@SuppressWarnings("unchecked")
	public List<Organization> getAll() throws Exception {
		return (List<Organization>) super.getRelationDao().selectList("organization.select", null);
	}

	// 根据等级查询
	@SuppressWarnings("unchecked")
	public List<Organization> getListByLevel(Organization entity) throws Exception {
		return (List<Organization>) super.getRelationDao().selectList("organization.select_by_level", entity);
	}
	// 根据等级查询
	@SuppressWarnings("unchecked")
	public List<Organization> getListByLevelCom(Organization entity) throws Exception {
		return (List<Organization>) super.getRelationDao().selectList("organization.select_by_level_com", entity);
	}

	@SuppressWarnings("unchecked")
	public List<Organization> getListByPath(Organization entity) throws Exception {
		return (List<Organization>) super.getRelationDao().selectList("organization.select_by_path", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Organization> getPaginatedList(Organization entity) throws Exception {
		return (List<Organization>) super.getRelationDao().selectList("organization.select", entity);
	}

	@Override
	public void deleteByArr(Organization entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				super.getRelationDao().delete("organization.delete", entity);
			}
		}
	}

	@Override
	public Organization getOne(Organization entity) throws Exception {
		return (Organization) super.getRelationDao().selectOne("organization.select_one", entity);
	}
	
	public Organization getOneWithCoCondition(Organization entity) throws Exception {
		return (Organization) super.getRelationDao().selectOne("organization.select", entity);
	}

	@Override
	public Long getRecordCount(Organization entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("organization.count", entity);
	}

	public Long getCountForOrgDuplication(Organization entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("organization.count_for_org_duplication", entity);
	}

	@SuppressWarnings("unchecked")
	public List<Commission> getListCommission() throws Exception {
		return (List<Commission>) super.getRelationDao().selectList("organization.select_commission", null);
	}
	
	//获取第二季居间公司集合
	@SuppressWarnings("unchecked")
	public List<Organization> getListOnLevel2(Organization entity)throws Exception{
		return (List<Organization>)super.getRelationDao().selectList("organization.select_one_com", entity);
	}
//	
//	@SuppressWarnings("unchecked")
//	public List<Organization> getListWithoutCommission(Organization org) throws Exception {
//		return (List<Organization>) super.getRelationDao().selectList("organization.select_without_commission", org);
//	}

}