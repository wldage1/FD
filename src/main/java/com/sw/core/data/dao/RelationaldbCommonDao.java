package com.sw.core.data.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis3.SqlSessionTemplate;

import com.sw.core.cache.PageCache;
import com.sw.core.common.Constant;
import com.sw.core.data.entity.RelationEntity;
import com.sw.core.initialize.InitPageConfig;

/**
 * Dao实现类 - Dao实现类基类
 */

public class RelationaldbCommonDao implements IDao<RelationEntity> {

	private static Logger logger = Logger.getLogger(RelationaldbCommonDao.class);
	private SqlSessionTemplate sqlSessionTemplate;
	private InitPageConfig initPageConfig;
	
	public RelationaldbCommonDao(SqlSessionTemplate sqlSessionTemplate) throws SQLException {
		if (sqlSessionTemplate == null) {
			logger.debug("SqlSessionTemplate is not null!");
			System.exit(0);
		}
		this.sqlSessionTemplate = sqlSessionTemplate;
		SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession();
		if (sqlSession != null) {
			try {
				String databaseName = sqlSession.getConnection().getMetaData().getDatabaseProductName();
				if (databaseName != null) {
					databaseName = databaseName.toLowerCase();
					String databaseNameTemp = "";
					if (databaseName != null) {
						databaseNameTemp = databaseName.replaceAll(" ", "");
					}
					if (databaseNameTemp.indexOf(Constant.DATASOURCE_MYSQL) > -1) {
						Constant.DEFAULT_DATABASE_TYPE = Constant.DATASOURCE_MYSQL;
					} else if (databaseNameTemp.indexOf(Constant.DATASOURCE_SQLSERVER) > -1) {
						Constant.DEFAULT_DATABASE_TYPE = Constant.DATASOURCE_SQLSERVER;
					} else if (databaseNameTemp.indexOf(Constant.DATASOURCE_ORACLE) > -1) {
						Constant.DEFAULT_DATABASE_TYPE = Constant.DATASOURCE_ORACLE;
					} else {
						Constant.DEFAULT_DATABASE_TYPE = Constant.DATASOURCE_MYSQL;
					}
				}
				logger.debug("database name is " + databaseName + "!");
			} catch (Exception e) {
				logger.debug("SqlSessionTemplate is not null!");
				System.exit(0);
			} finally {
				if (sqlSession != null) {
					sqlSession.commit();
					sqlSession.close();
				}
			}
		}
	}

	@Override
	public void insert(String sqlid, RelationEntity entity) throws Exception {
		try{
			if (entity != null) {
				sqlSessionTemplate.insert(sqlid, entity);
			} else {
				sqlSessionTemplate.insert(sqlid);
			}
			try{
				//清除页面缓存
				this.removePageCache(sqlid);
			}
			catch(Exception e){
				System.out.println("remove page cache error:"+ e.getMessage());
			}
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public void update(String sqlid, RelationEntity entity) throws Exception {
		try{
			if (entity != null) {
				sqlSessionTemplate.update(sqlid, entity);
			} else {
				sqlSessionTemplate.update(sqlid);
			}
			try{
				//清除页面缓存
				this.removePageCache(sqlid);
			}
			catch(Exception e){
				System.out.println("remove page cache error:"+ e.getMessage());
			}
		}
		catch(Exception e){
			throw e;
		}		
	}

	@Override
	public void delete(String sqlid, RelationEntity entity) throws Exception {
		try{
			if (entity != null) {
				sqlSessionTemplate.delete(sqlid, entity);
			} else {
				sqlSessionTemplate.delete(sqlid);
			}
			try{
				//清除页面缓存
				this.removePageCache(sqlid);
			}
			catch(Exception e){
				System.out.println("remove page cache error:"+ e.getMessage());
			}
		}
		catch(Exception e){
			throw e;
		}			
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> selectList(String sqlid, RelationEntity entity) throws Exception {
		List<RelationEntity> list = null;
		if (entity != null) {
			list = sqlSessionTemplate.selectList(sqlid, entity);
		} else {
			list = sqlSessionTemplate.selectList(sqlid);
		}
		return list;
	}

	@Override
	public Object selectOne(String sqlid, RelationEntity entity) throws Exception {
		Object object = null;
		if (entity != null) {
			object = sqlSessionTemplate.selectOne(sqlid, entity);
		} else {
			object = sqlSessionTemplate.selectOne(sqlid);
		}
		return object;
	}

	/**
	 * 移动结果集分页查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<?> selectPaginatedList(String sqlid, RelationEntity entity) throws Exception {
		List<RelationEntity> list = null;
		if (entity != null) {
			RowBounds rowBounds = new RowBounds(entity.getStart(), entity.getOffset());
			list = sqlSessionTemplate.selectList(sqlid, entity, rowBounds);
		}
		return list;
	}

	@Override
	public Long getCount(String sqlid, RelationEntity entity) throws Exception {
		long cnt = 0;
		Object obj = null;
		if (entity == null) {
			obj = sqlSessionTemplate.selectOne(sqlid);
		} else {
			obj = sqlSessionTemplate.selectOne(sqlid, entity);
		}
		if (obj != null) {
			if (obj instanceof Integer) {
				try {
					cnt = Long.parseLong(obj.toString());
				} catch (Exception e) {
				}
			} else if (obj instanceof Long) {
				try {
					cnt = Long.parseLong(obj.toString());
				} catch (Exception e) {
				}
			}
			return cnt;
		}
		return cnt;
	}

	public SqlSessionTemplate getSqlSessionTemplate() throws Exception {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) throws Exception {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Override
	public void insert(String sqlid, Object entity) throws Exception {
		try{
			if (entity != null) {
				sqlSessionTemplate.insert(sqlid, entity);
			}
			try{
				//清除页面缓存
				this.removePageCache(sqlid);
			}
			catch(Exception e){
				System.out.println("remove page cache error:"+ e.getMessage());
			}
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public void update(String sqlid, Object entity) throws Exception {
		try{
			if (entity != null) {
				sqlSessionTemplate.update(sqlid, entity);
			}
			try{
				//清除页面缓存
				this.removePageCache(sqlid);
			}
			catch(Exception e){
				System.out.println("remove page cache error:"+ e.getMessage());
			}
		}
		catch(Exception e){
			throw e;
		}		
	}

	@Override
	public void delete(String sqlid, Object entity) throws Exception {
		try{
			if (entity != null) {
				sqlSessionTemplate.delete(sqlid, entity);
			}
			try{
				//清除页面缓存
				this.removePageCache(sqlid);
			}
			catch(Exception e){
				System.out.println("remove page cache error:"+ e.getMessage());
			}
		}
		catch(Exception e){
			throw e;
		}		
	}

	public InitPageConfig getInitPageConfig() {
		return initPageConfig;
	}

	public void setInitPageConfig(InitPageConfig initPageConfig) {
		this.initPageConfig = initPageConfig;
	}

	/**
	 * 清除页面配置缓存通用方法
	 * @param sqlid
	 */
	private void removePageCache(String sqlid){
		if (sqlid != null){
			if (initPageConfig != null){
				Map<String,List<String>> mapCache = initPageConfig.getPageCache();
				if (mapCache != null){
					List<String> pageList = mapCache.get(sqlid);
					if (pageList != null && pageList.size() > 0){
						for (String pageKey:pageList){
							try{
								PageCache.removeCache(pageKey);
								System.out.println("success clear [" + pageKey + "] cache." );
							}
							catch(Exception e){
								System.out.println("clear [" + pageKey + "] cache fail." );
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Object selectOne(String sqlid, Object entity) throws Exception {
		Object object = null;
		if (entity != null) {
			object = sqlSessionTemplate.selectOne(sqlid, entity);
		} else {
			object = sqlSessionTemplate.selectOne(sqlid);
		}
		return object;
	}
}