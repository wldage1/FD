package com.sw.core.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Service接口 - Service适配器类
 */

public interface ICommonService<T> {
	
    public static String SEPARTOR = "/";
    
	public void save(T entity) throws Exception;

	public void update(T entity) throws Exception;

	public Long getRecordCount(T entity) throws Exception;

	public List<T> getList(T entity) throws Exception;

	public List<T> getPaginatedList(T entity) throws Exception;

	public void delete(T entity) throws Exception;

	public void deleteByArr(T entity) throws Exception;

	public T getOne(T entity) throws Exception;
	
	public Map<String, Object> getGrid(T entity) throws Exception;
	
	public String upload(T entity,HttpServletRequest request) throws Exception;
	
	public Object download(T entity,HttpServletRequest request) throws Exception;
	
	public File createFile(String pathFileName,HttpServletRequest request) throws Exception;
	
	public File createFile(String filePath,String fileName,HttpServletRequest request) throws Exception;
	
	public void deleteFile(String filePath,String fileName,HttpServletRequest request) throws Exception;
	
	public void deleteFile(String pathFileName,HttpServletRequest request) throws Exception;
	
}
