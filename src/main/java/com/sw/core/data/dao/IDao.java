package com.sw.core.data.dao;

import java.util.List;

import com.sw.core.data.entity.BaseEntity;

public interface IDao<T extends BaseEntity> {
	
	/**
	 * 数据库插入
	 * 
	 * @param sqlid
	 * @param t
	 */
	public void insert(String sqlid, T entity) throws Exception;

	/**
	 * 数据库修改
	 * 
	 * @param sqlid
	 * @param t
	 */
	public void update(String sqlid, T entity) throws Exception;

	/**
	 * 数据库删除
	 * 
	 * @param sqlid
	 * @param t
	 */
	public void delete(String sqlid, T entity) throws Exception;

	/**
	 * 计算满足条件条数
	 * 
	 * @param sqlid
	 * @param t
	 */
	public Long getCount(String sqlid, T entity) throws Exception;

	/**
	 * 查询列表
	 * 
	 * @param sqlid
	 * @param t
	 * @return
	 */
	public List<?> selectList(String sqlid, T entity) throws Exception;

	/**
	 * 查询单条记录
	 * 
	 * @param sqlid
	 * @param t
	 * @return
	 */
	public Object selectOne(String sqlid, T entity) throws Exception;

	/**
	 * 查询分页列表
	 * 
	 * @param sqlid
	 * @param entity
	 * @param skipResults
	 * @param maxResults
	 * @return
	 */
	public List<?> selectPaginatedList(String sqlid, T entity) throws Exception;
	
	/**
	 * 数据库插入
	 * 
	 * @param sqlid
	 * @param t
	 */
	public void insert(String sqlid, Object entity) throws Exception;

	/**
	 * 数据库修改
	 * 
	 * @param sqlid
	 * @param t
	 */
	public void update(String sqlid, Object entity) throws Exception;

	/**
	 * 数据库删除
	 * 
	 * @param sqlid
	 * @param t
	 */
	public void delete(String sqlid, Object entity) throws Exception;
	
	public Object selectOne(String sqlid, Object entity) throws Exception;

}
