package com.sw.plugins.system.authorization.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.sw.core.cache.PluginsConfigCache;
import com.sw.core.data.entity.Authorization;
import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.system.authorization.entity.SubAuthorization;
import com.sw.plugins.system.role.entity.RoleAuthMapping;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.BeanUtil;

/**
 * Service实现类 - Service实现类基类
 */

public class AuthorizationService extends CommonService<Authorization> {

	private static Logger logger = Logger.getLogger(AuthorizationService.class);
	@Resource
	private UserLoginService userLoginService;

	/**
	 * 权限功能表格树
	 * 
	 * @param function
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAuthGrid(SubAuthorization subAuthorization) throws Exception {
		List<SubAuthorization> resultList = null;
		Map<String, Object> map = new Hashtable<String, Object>();
		User user = userLoginService.getCurrLoginUser();
		if (!user.equals("anonymousUser")) {
			if (resultList == null) {
				resultList = new ArrayList<SubAuthorization>();
			}

			List<SubAuthorization> tempResultList = this.getAuthBusinessTypeMapping(subAuthorization);
			// 系统启动默认加载的所有权限信息
			Collection<Authorization> authorizations = PluginsConfigCache.getAllAuthCache();
			HashMap<String, String> tempCodesMap = new HashMap<String, String>();
			for (SubAuthorization tempAuthorization : tempResultList) {
				if (tempAuthorization.getRelationPath() != null) {
					String code = tempAuthorization.getCode();
					Long tempBussinessTypeId = tempAuthorization.getBusinessTypeId();
					String rparr[] = tempAuthorization.getRelationPath().split(",");
					for (int i = 0; i < rparr.length; i++) {
						String tempCode = rparr[i];
						if (tempCode == null || tempCode.equals("")) {
							continue;
						}
						if (tempCodesMap.get(tempCode) != null) {
							continue;
						}
						for (Authorization cacheAuthorization : authorizations) {
							String cacheCode = cacheAuthorization.getCode();
							if (cacheCode == null || cacheCode.equals("")) {
								continue;
							}
							String level = "0";
							if (tempCode.equals(cacheCode)) {
								tempCodesMap.put(tempCode, cacheCode);
								if (code.equals(tempCode)) {
									// 设置为可配置
									tempAuthorization.setIsConfig("1");
									level = cacheAuthorization.getTreeLevel();
									if (level != null && level.equals("1")) {
										tempAuthorization.setParentCode("-" + String.valueOf(tempBussinessTypeId));
									} else {
										tempAuthorization.setParentCode(cacheAuthorization.getParentCode());
									}
									resultList.add(tempAuthorization);
								} else {
									SubAuthorization subAuthorizationTemp = new SubAuthorization();
									// bean 属性拷贝
									BeanUtil.copyProperties(cacheAuthorization, subAuthorizationTemp);
									level = cacheAuthorization.getTreeLevel();
									if (level != null && level.equals("1")) {
										subAuthorizationTemp.setParentCode("-" + String.valueOf(tempBussinessTypeId));
										subAuthorizationTemp.setTempParentCode("-" + String.valueOf(tempBussinessTypeId));
									} else {
										subAuthorizationTemp.setParentCode(cacheAuthorization.getParentCode());
										subAuthorizationTemp.setTempParentCode(String.valueOf(tempBussinessTypeId) + cacheAuthorization.getParentCode());
									}
									subAuthorizationTemp.setTempCode(String.valueOf(tempBussinessTypeId) + cacheAuthorization.getCode());
									// 设置为不可配置
									subAuthorizationTemp.setIsConfig("0");
									resultList.add(subAuthorizationTemp);
								}
								break;
							}
						}
					}
				}
			}
			// 清除临时内存数据
			if (tempCodesMap != null) {
				tempCodesMap.clear();
			}
		}
		map.put("page", 1);
		map.put("total", 1);
		map.put("records", resultList.size());
		map.put("rows", resultList);
		return map;
	}

	public List<Object> getAuthTreeList(SubAuthorization subAuthorization) throws Exception {
		List<Object> list = new ArrayList<Object>();
		Collection<Authorization> authorizations = PluginsConfigCache.getAllAuthCache();
		List<Authorization> authorizationList = (List<Authorization>) this.getListByType();
		HashMap<String, String> tempCodesMap = new HashMap<String, String>();
		for (Authorization authorization : authorizationList) {
			if (authorization.getRelationPath() != null) {
				String rparr[] = authorization.getRelationPath().split(",");
				for (int i = 0; i < rparr.length; i++) {
					String tempCode = rparr[i];
					if (tempCode.equals("")) {
						continue;
					}
					if (tempCodesMap.get(tempCode) != null) {
						continue;
					}
					for (Authorization cacheAuthorization : authorizations) {
						String cacheCode = cacheAuthorization.getCode();
						if (tempCode.equals(cacheCode)) {
							tempCodesMap.put(cacheCode, cacheCode);
							Map<String, Object> mapauth = new Hashtable<String, Object>();
							mapauth.put("id", cacheAuthorization.getCode());
							mapauth.put("pId", cacheAuthorization.getParentCode());
							mapauth.put("code", cacheAuthorization.getCode());
							mapauth.put("name", cacheAuthorization.getName());
							mapauth.put("type", cacheAuthorization.getType());
							list.add(mapauth);
						}
					}
				}
			}
		}
		// 清除临时内存数据
		if (tempCodesMap != null) {
			tempCodesMap.clear();
		}
		return list;
	}

	/**
	 * 根据权限级别进行权限信息查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Authorization> getAuthorizationByTreeLevel(String level) throws Exception {
		Authorization authorization = new Authorization();
		authorization.setTreeLevel(level);
		return getList(authorization);
	}

	/**
	 * 查询所有权限
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Authorization> getAllAuthorization() throws Exception {
		Authorization authorization = new Authorization();
		return getList(authorization);
	}

	/**
	 * 根据code查询权限信息
	 * 
	 * @return
	 * @author
	 * @throws Exception
	 */
	public Authorization getAuthorizationByCode(String code) throws Exception {
		Authorization authorization = new Authorization();
		authorization.setCode(code);
		return getOne(authorization);
	}

	/**
	 * 查询控制器信息
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String getControllerURL(String code) throws Exception {
		Authorization authorization = new Authorization();
		authorization.setCode(code);
		authorization = getOne(authorization);
		return authorization.getController();
	}

	/**
	 * 根据权限代码查询子权限
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<Authorization> getSubAuthorizationByCode(String code) throws Exception {
		Authorization authorization = new Authorization();
		authorization.setParentCode(code);
		return getList(authorization);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(InitData initData) throws Exception {
		try {
			logger.debug("authorization info initializing , please waiting...");
			List<String> codes = new ArrayList<String>();
			// 系统所有插件配置的权限信息
			Collection<Authorization> parrayList = PluginsConfigCache.getAllAuthCache();
			if (parrayList != null) {
				for (Iterator<Authorization> localIterator = parrayList.iterator(); localIterator.hasNext();) {
					Authorization handlerAuthorization = localIterator.next();
					try {
						if (handlerAuthorization != null) {
							// 权限bean的拷贝
							String controller = handlerAuthorization.getController();
							if (controller != null && !controller.startsWith("/")) {
								handlerAuthorization.setController("/" + controller);
							}
							if (handlerAuthorization.getCode() == null || handlerAuthorization.getCode().equals("")) {
								throw new Exception("permission code is null");
							}
							if (handlerAuthorization.getParentCode() == null || handlerAuthorization.getParentCode().equals("")) {
								throw new Exception("permission parentcode is null");
							}
							/*** 如果数据库中存在，则只进行修改操作 ****/
							String setAuthority = handlerAuthorization.getSetAuthorityOrNot();
							handlerAuthorization.setSetAuthorityOrNot(null);
							List<?> list = super.getRelationDao().selectList("authorization.select_by_code", handlerAuthorization);
							handlerAuthorization.setSetAuthorityOrNot(setAuthority);
							if (list != null && list.size() == 0) {
								save(handlerAuthorization);
							} else {
								update(handlerAuthorization);
							}
							codes.add(handlerAuthorization.getCode());
						}
					} catch (Exception e) {
						logger.debug(e.getMessage() + " cause '" + handlerAuthorization.getName() + "' authorization info initialize fail , please check it.");
						throw new Exception(e);
					}
				}
			}
			if (codes.size() > 0) {
				Authorization auth = new Authorization();
				auth.setCodes(codes);
				List<Authorization> authList = (List<Authorization>) super.getRelationDao().selectList("authorization.select_by_notcodes", auth);
				for (int i = 0; i < authList.size(); i++) {
					if (authList.get(i) != null) {
						// 查询是否有角色权限绑定
						RoleAuthMapping roleAuthMapper = new RoleAuthMapping();
						roleAuthMapper.setAuthCode(authList.get(i).getCode());
						long len = super.getRelationDao().getCount("roleAuthMapping.count", roleAuthMapper);
						if (len > 0) {
							super.getRelationDao().getCount("roleAuthMapping.delete", roleAuthMapper);
						}
						super.getRelationDao().delete("authorization.delete_by_code", authList.get(i));
					}
				}
			}
			logger.debug("authorization info initialize finish.");
		} catch (Exception e) {
			logger.debug("authorization info initialize fail！");
			String debug = DetailException.expDetail(e, AuthorizationService.class);
			logger.debug(debug);
			/** 异常退出系统 */
			System.exit(0);
		}
	}

	@Override
	public Map<String, Object> getGrid(Authorization entity) throws Exception {
		return null;
	}

	@Override
	public String upload(Authorization entity, HttpServletRequest request) throws Exception {
		return null;

	}

	@Override
	public Object download(Authorization entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void save(Authorization entity) throws Exception {
		super.getRelationDao().insert("authorization.insert", entity);
	}

	@Override
	public void update(Authorization entity) throws Exception {
		super.getRelationDao().update("authorization.update", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Authorization> getList(Authorization entity) throws Exception {
		return (List<Authorization>) super.getRelationDao().selectList("authorization.select", entity);
	}

	@SuppressWarnings("unchecked")
	public List<Authorization> getListByType() throws Exception {
		return (List<Authorization>) super.getRelationDao().selectList("authorization.select_by_type", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Authorization> getPaginatedList(Authorization entity) throws Exception {
		return (List<Authorization>) super.getRelationDao().selectList("authorization.select", entity);
	}

	@Override
	public void delete(Authorization entity) throws Exception {
		super.getRelationDao().delete("authorization.delete_by_code", entity);
	}

	@Override
	public void deleteByArr(Authorization entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				super.getRelationDao().delete("authorization.delete_by_code", entity);
			}
		}
	}

	/**
	 * 查询系统配置功能和业务种类关系数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SubAuthorization> getAuthBusinessTypeMapping(SubAuthorization subAuthorization) throws Exception {
		return (List<SubAuthorization>) super.getRelationDao().selectList("authorization.select_auth_business_type_mapping", subAuthorization);
	}

	@Override
	public Authorization getOne(Authorization entity) throws Exception {
		return (Authorization) super.getRelationDao().selectOne("authorization.select", entity);
	}

	@Override
	public Long getRecordCount(Authorization entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("authorization.count", entity);
	}

	/**
	 * 获取系统所有功能
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Object> getAuthTreeList() throws Exception {
		List<Object> list = new ArrayList<Object>();
		List<Authorization> authorizationList = (List<Authorization>) this.getListByType();
		HashMap<String, String> tempCodesMap = new HashMap<String, String>();
		for (Authorization authorization : authorizationList) {
			if (authorization.getRelationPath() != null) {
				String rparr[] = authorization.getRelationPath().split(",");
				for (int i = 0; i < rparr.length; i++) {
					String tempCode = rparr[i];
					if (tempCode.equals("")) {
						continue;
					}
					if (tempCodesMap.get(tempCode) != null) {
						continue;
					}
					Collection<Authorization> authorizations = PluginsConfigCache.getAllAuthCache();
					for (Authorization cacheAuthorization : authorizations) {
						String cacheCode = cacheAuthorization.getCode();
						if (tempCode.equals(cacheCode)) {
							tempCodesMap.put(cacheCode, cacheCode);
							Map<String, Object> mapauth = new Hashtable<String, Object>();
							mapauth.put("id", cacheAuthorization.getCode());
							mapauth.put("pId", cacheAuthorization.getParentCode());
							mapauth.put("code", cacheAuthorization.getCode());
							mapauth.put("name", cacheAuthorization.getName());
							mapauth.put("type", cacheAuthorization.getType());
							list.add(mapauth);
						}
					}
				}
			}
		}
		// 清除临时内存数据
		if (tempCodesMap != null) {
			tempCodesMap.clear();
		}
		return list;
	}

}