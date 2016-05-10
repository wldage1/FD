package com.sw.core.data.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.CursorPreparer;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.sw.core.common.Constant;
import com.sw.core.data.entity.MongoEntity;

/**
 * mongodb 与ibatis xml解析引擎处理类
 * 
 * @param <T>
 */

public class MongodbCommonDao<T> extends MongoTemplate implements IDao<MongoEntity> {

	private static Logger logger = Logger.getLogger(MongodbCommonDao.class);

	private SqlSessionFactory sqlSessionFactory;

	public MongodbCommonDao(MongoDbFactory mongoDbFactory) {
		super(mongoDbFactory);
		Constant.DEFAULT_DATABASE_TYPE = Constant.DATASOURCE_MONGODB;
	}

	@Override
	public void insert(String sqlid, MongoEntity entity) throws Exception {
		if (entity != null) {
			DBAttribute dba = new DBAttribute().getDBAttribute(sqlid, entity);
			if (dba != null && dba.getDbField() != null) {
				DBObject dbField = (DBObject) JSON.parse(dba.getDbField());
				ObjectId idKey = new ObjectId();
				dbField.put("_id", idKey); 
				dbField.put(entity.getClass().getSimpleName().toLowerCase(), idKey);
				entity.setGeneratedKey(idKey.toString());
				super.saveDBObject(entity.getClass().getSimpleName(), dbField);
				logger.debug("insert entity success");
			}
		}
	}

	@Override
	public void update(String sqlid, MongoEntity entity) throws Exception {
		if (entity != null) {
			DBAttribute dba = new DBAttribute().getDBAttribute(sqlid, entity);
			if (dba != null && dba.getDbQuery() != null && dba.getDbField() != null) {
				DBObject dbQuery = (DBObject) JSON.parse(dba.getDbQuery());
				DBObject dbField = (DBObject) JSON.parse(dba.getDbField());
				WriteResult wResult = super.getCollection(entity.getClass().getSimpleName()).update(dbQuery, dbField, false, false);
				logger.debug(wResult);
			}
		}
	}

	@Override
	public void delete(String sqlid, MongoEntity entity) throws Exception {
		if (entity != null) {
			DBAttribute dba = new DBAttribute().getDBAttribute(sqlid, entity);
			if (dba != null && dba.getDbQuery() != null) {
				DBObject dbQuery = (DBObject) JSON.parse(dba.getDbQuery());
				WriteResult wResult = super.getCollection(entity.getClass().getSimpleName()).remove(dbQuery);
				logger.debug(wResult);
			}
		}
	}

	@Override
	public Long getCount(String sqlid, MongoEntity entity) throws Exception {
		Long object = null;
		if (entity != null) {
			DBAttribute dba = new DBAttribute().getDBAttribute(sqlid, entity);
			if (dba != null) {
				DBObject dbQuery = dba.getDbQuery() == null ? new BasicDBObject() : (DBObject) JSON.parse(dba.getDbQuery());
				object = super.getCollection(entity.getClass().getSimpleName()).getCount(dbQuery);
			}
		}
		return object;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public T selectOne(String sqlid, MongoEntity entity) throws Exception {
		T object = null;
		if (entity != null) {
			DBAttribute dba = new DBAttribute().getDBAttribute(sqlid, entity);
			if (dba != null) {
				DBObject dbQuery = dba.getDbQuery() == null ? new BasicDBObject() : (DBObject) JSON.parse(dba.getDbQuery());
				DBObject dbField = dba.getDbField() == null ? new BasicDBObject() : (DBObject) JSON.parse(dba.getDbField());
				Class returnType = dba.getReturnType() == null ? entity.getClass() : dba.getReturnType();
				object = (T) super.doFindOne(entity.getClass().getSimpleName(), dbQuery, dbField, returnType);
			}
		}
		return object;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> selectList(String sqlid, MongoEntity entity) throws Exception {
		List<T> list = null;
		if (entity != null) {
			DBAttribute dba = new DBAttribute().getDBAttribute(sqlid, entity);
			if (dba != null) {
				DBObject dbQuery = dba.getDbQuery() == null ? new BasicDBObject() : (DBObject) JSON.parse(dba.getDbQuery());
				DBObject dbField = dba.getDbField() == null ? new BasicDBObject() : (DBObject) JSON.parse(dba.getDbField());
				Class returnType = dba.getReturnType() == null ? entity.getClass() : dba.getReturnType();
				list = (List<T>) super.doFind(entity.getClass().getSimpleName(), dbQuery, dbField, returnType);
			}
		}
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> selectPaginatedList(String sqlid,MongoEntity entity) throws Exception {
		List<T> list = null;
		if (entity != null) {
			DBAttribute dba = new DBAttribute().getDBAttribute(sqlid, entity);
			if (dba != null) {
				//排序字段名
				String sidx = entity.getSidx();
				//排序字段值
				String sord = entity.getSord();
				CursorPreparer cursorPreparer = null;
				if (sidx != null){
					//设置排序字段
					DBObject order = new BasicDBObject();
					order.put(sidx, sord == null ? "1":sord);
					cursorPreparer = new MongoCursorPreparer(entity.getStart(),entity.getOffset(),order);	
				}
				else{
					cursorPreparer = new MongoCursorPreparer(entity.getStart(),entity.getOffset());	
				}				
				DBObject dbQuery = dba.getDbQuery() == null ? new BasicDBObject() : (DBObject) JSON.parse(dba.getDbQuery());
				DBObject dbField = dba.getDbField() == null ? new BasicDBObject() : (DBObject) JSON.parse(dba.getDbField());
				Class returnType = dba.getReturnType() == null ? entity.getClass() : dba.getReturnType();
				list = (List<T>) super.doFind(entity.getClass().getSimpleName(), dbQuery, dbField, returnType,cursorPreparer);

			}
		}
		return list;
	}
	
	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	
	class MongoCursorPreparer implements CursorPreparer{
		//跳跃记录数
		private int skip = 0;
		//每页记录数
		private int limit = 0;
		//排序字段
		private DBObject order;
		public MongoCursorPreparer(int skip, int limit,DBObject order){
			this.skip = skip;
			this.limit = limit;
			this.order = order;
		}
		public MongoCursorPreparer(int skip, int limit){
			this.skip = skip;
			this.limit = limit;
		}
		public MongoCursorPreparer(int limit,DBObject order){
			this.limit = limit;
			this.order = order;
		}
		public MongoCursorPreparer(int limit){
			this.limit = limit;
		}
		public MongoCursorPreparer(DBObject order){
			this.order = order;
		}
		@Override
		public DBCursor prepare(DBCursor paramDBCursor) {
			 DBCursor cursorToUse = paramDBCursor;
			 if (skip > 0){
				 cursorToUse.skip(skip);
			 }
			 if (limit > 0){
				 cursorToUse.limit(limit);
			 }
			 cursorToUse.sort(order);
			 return cursorToUse;
		}
	}
	
	class DBAttribute {

		// 数据库条件域
		private String dbQuery;
		// 数据库属性域
		private String dbField;
		// 数据库操作语句
		private String dbOrderby;
		// 数据库排序语句
		private String cusSQL;
		// 数据库返回类型
		private Class<T> returnType;

		DBAttribute() {
		}

		@SuppressWarnings("unchecked")
		private DBAttribute getDBAttribute(String sqlid, Object paramObj) throws Exception {
			String sql = null;
			DBAttribute dba = new DBAttribute();
			sql = sqlSessionFactory.getConfiguration().getMappedStatement(sqlid).getBoundSql(paramObj).getSql();
			dba.setReturnType(sqlSessionFactory.getConfiguration().getMappedStatement(sqlid).getResultMaps().size() == 0 ? null : sqlSessionFactory.getConfiguration().getMappedStatement(sqlid).getResultMaps().get(0).getType());
			dba.setCusSQL(sql);
			if (sql != null) {
				int i = sql.toLowerCase().indexOf("where");
				if (i > -1) {
					if(i != 0){
						//int orderIndex = sql.toLowerCase().indexOf("order");
						dba.setDbField(sql.substring(0, i));
					}
					dba.setDbQuery(sql.substring(i + 6, sql.length()));
				} else {
					dba.setDbField(sql);
				}
			}
			logger.debug("sql:" + sql);
			return dba;
		}

		public String getDbQuery() {
			return dbQuery;
		}

		public void setDbQuery(String dbQuery) {
			this.dbQuery = dbQuery;
		}

		public String getDbField() {
			return dbField;
		}

		public void setDbField(String dbField) {
			this.dbField = dbField;
		}

		public String getCusSQL() {
			return cusSQL;
		}

		public void setCusSQL(String cusSQL) {
			this.cusSQL = cusSQL;
		}

		public Class<T> getReturnType() {
			return returnType;
		}

		public void setReturnType(Class<T> returnType) {
			this.returnType = returnType;
		}

		public String getDbOrderby() {
			return dbOrderby;
		}

		public void setDbOrderby(String dbOrderby) {
			this.dbOrderby = dbOrderby;
		}

	}

	@Override
	public void insert(String sqlid, Object entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String sqlid, Object entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String sqlid, Object entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object selectOne(String sqlid, Object entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}