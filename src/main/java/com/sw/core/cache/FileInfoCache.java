package com.sw.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.dao.DataRetrievalFailureException;

import com.sw.core.data.entity.FileInfo;

/**
 * 文件缓存
 * @author Spark
 *
 */
public class FileInfoCache {
	
private static final String FILE_INFO = "file_info";
	
	private static Cache cache = CacheManager.getInstance().getCache(FILE_INFO);

	public static synchronized void putAuthCache(FileInfo fileInfo) {
		if (fileInfo != null){
			Element element = new Element("fileInfo", fileInfo);
			cache.put(element);
		}
	}
	
	/**
	 * 获取一个任务ID
	 * 
	 * @return
	 */
	public static synchronized Element getCache() {
		if(cache.getSize() == 0)
			return null;
		String cacheKey = (String) cache.getKeys().get(0);
		Element element = cache.get(cacheKey);
		cache.remove(cacheKey);
		return element;
	}
	
	public static synchronized FileInfo getCache(FileInfo fileInfo) {
		Element element = null;
		try {
			element = cache.get(fileInfo.getName());
		} catch (CacheException cacheException) {
			throw new DataRetrievalFailureException("ResourceCache failure: "
					+ cacheException.getMessage(), cacheException);
		}
		if (element == null)
			return null;

		return ((FileInfo) element.getValue());
	}
	
	public static synchronized FileInfo getCache(String key) {
		Element element = null;
		try {
			element = cache.get(key);
		} catch (CacheException cacheException) {
			throw new DataRetrievalFailureException("ResourceCache failure: "
					+ cacheException.getMessage(), cacheException);
		}
		if (element == null)
			return null;

		return ((FileInfo) element.getValue());
	}

	public static synchronized void removeCache(String fileInfo) {
		cache.remove(fileInfo);
	}

	public static synchronized void removeAllCache() {
		cache.removeAll();
		cache.clearStatistics();
		cache.flush();
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized Collection<FileInfo> getAllCache() {
		Collection<String> resources;
		List<FileInfo> resclist = new ArrayList<FileInfo>();
		try {
			resources = cache.getKeys();
			if (resources != null){
				for (Iterator<String> localIterator = resources.iterator(); localIterator.hasNext();) {
					String key = localIterator.next();
					FileInfo fileInfo = null;
					try {
						fileInfo = getCache(key);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (fileInfo != null){
						resclist.add(fileInfo);
					}
				}			
			}
		} catch (IllegalStateException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (CacheException e) {
			throw new UnsupportedOperationException(e.getMessage(), e);
		}

		return resclist;
	}
}
