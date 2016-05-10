package com.sw.core.data.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class CustomCommonDao {

	private JdbcTemplate jt;

	public void excute(String sql) throws Exception {
		jt.execute(sql);
	}

	public void update(String sql, Object[] args) throws Exception {
		jt.update(sql, args);
	}

	public Long getCount(String sql, Object[] args) throws Exception {
		return (long) jt.queryForLong(sql, args);
	}

	public List<?> selectList(String sql, Class<?> clazz) throws Exception {
		return (List<?>) jt.queryForList(sql, clazz);
	}

	public List<Map<String, Object>> selectList(String sql, Object[] args) throws Exception {
		return (List<Map<String, Object>>) jt.queryForList(sql, args);
	}

	public Object selectOne(String sql, Class<?> clazz) throws Exception {
		return jt.queryForObject(sql, clazz);
	}

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

}