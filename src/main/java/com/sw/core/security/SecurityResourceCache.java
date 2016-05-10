package com.sw.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import com.sw.core.data.entity.Authorization;

public class SecurityResourceCache {
	
	private static final String SECURITY_CACHE_NAME = "security_resource";
	
	private static Cache cache = CacheManager.getInstance().getCache(SECURITY_CACHE_NAME);

	public static synchronized void putCache(Authorization authorization) {
		Element element = new Element(authorization.getController(), authorization);
		cache.put(element);
	}

	public static synchronized Authorization getCache(String path) {
		Element element = null;
		try {
			element = cache.get(path);
		} catch (CacheException cacheException) {
			throw new DataRetrievalFailureException("ResourceCache failure: "
					+ cacheException.getMessage(), cacheException);
		}
		if (element == null)
			return null;

		return ((Authorization) element.getValue());
	}

	public static synchronized void removeCache(String path) {
		cache.remove(path);
	}

	public static synchronized void removeAllCache() {
		cache.removeAll();
		cache.clearStatistics();
		cache.flush();
	}

	@SuppressWarnings("unchecked")
	public static synchronized List<Authorization> getAllCache() {
		Collection<String> resources;
		List<Authorization> resclist = new ArrayList<Authorization>();
		try {
			resources = cache.getKeys();
		} catch (IllegalStateException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (CacheException e) {
			throw new UnsupportedOperationException(e.getMessage(), e);
		}
		for (Iterator<String> localIterator = resources.iterator(); localIterator.hasNext();) {
			String controller = localIterator.next();
			Authorization authorization = getCache(controller);
			resclist.add(authorization);
		}
		return resclist;
	}

	public static synchronized GrantedAuthority[] getAuthoritysInCache(
			String path) {
		GrantedAuthority[] gas = { new GrantedAuthorityImpl(getCache(path).getCode()) };
		return gas;
	}
}