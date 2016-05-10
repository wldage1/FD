package com.sw.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.sw.core.cache.PluginsConfigCache;
import com.sw.core.common.Constant;
import com.sw.core.data.entity.Authorization;

public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	
	/**url和全面代码映射map*/
	private static Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();;
	public MySecurityMetadataSource() {
		loadResourceDefine();
	}
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return new ArrayList<ConfigAttribute>();
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	// 加载所有资源与权限的关系
	private void loadResourceDefine() {
		Collection<Authorization> authorizations = PluginsConfigCache.getAllAuthCache();
		for (Authorization authorization : authorizations) {
			if (authorization.getSetAuthorityOrNot() != null 
				&& authorization.getSetAuthorityOrNot().equals("true")){
				// 以权限名封装为Spring的security Object
				Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
				ConfigAttribute configAttribute = new SecurityConfig(authorization.getCode());
				configAttributes.add(configAttribute);
				resourceMap.put(authorization.getController(), configAttributes);				
			}
		}		
		
		Collection<ConfigAttribute> configAttributesCommon = new ArrayList<ConfigAttribute>();
		ConfigAttribute configAttributeCommon = new SecurityConfig(Constant.PERMIT_All);
		configAttributesCommon.add(configAttributeCommon);
		resourceMap.put("/"+Constant.PERMITALL, configAttributesCommon);
	}

	// 返回所请求资源所需要的权限
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		if (requestUrl != null){
			int indw = requestUrl.indexOf("?");
			if (indw > -1){
				requestUrl = requestUrl.substring(0,indw);
			}
			int idxd = requestUrl.indexOf(".");
			if (idxd > -1){
				requestUrl = requestUrl.substring(0,idxd);
			}
		}
		Collection<ConfigAttribute> configAttributes = resourceMap.get(requestUrl);
		if (configAttributes == null){
			configAttributes = resourceMap.get("/"+Constant.PERMITALL);
		}
		return configAttributes;
	}
}