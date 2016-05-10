package com.sw.core.data.dbholder;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	/**
	 * 重写方法，获取数据库类型
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		return DatasourceTypeContextHolder.getDataSourceType();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
