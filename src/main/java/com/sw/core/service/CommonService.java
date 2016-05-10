package com.sw.core.service;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.common.Constant;
import com.sw.core.data.dao.IDao;
import com.sw.core.data.entity.Authorization;
import com.sw.core.data.entity.BaseEntity;
import com.sw.core.data.entity.MongoEntity;
import com.sw.core.data.entity.RelationEntity;
import com.sw.core.initialize.IInitCriteria;
import com.sw.core.initialize.InitData;
import com.sw.core.initialize.InitPageConfig;
import com.sw.core.security.SecurityResourceCache;

/**
 * 逻辑实现层（具体实现），service
 * 
 * @param <T>
 * @param <PK>
 */
public abstract class CommonService<T extends BaseEntity> implements ICommonService<T>, IInitCriteria {

	@Resource
	private IDao<RelationEntity> relationDao;
	@Resource
	private IDao<MongoEntity> mongoDao;
	/**系统初始化参数配置**/
	@Resource(name = "initData")
	private InitData initData;
	/**系统默认初始化缓存配置**/
	@Resource(name = "initPageConfig")
	private InitPageConfig initPageConfig;
	

	public IDao<RelationEntity> getRelationDao() {
		return relationDao;
	}

	public void setRelationDao(IDao<RelationEntity> relationDao) {
		this.relationDao = relationDao;
	}

	public IDao<MongoEntity> getMongoDao() {
		return mongoDao;
	}

	public void setMongoDao(IDao<MongoEntity> mongoDao) {
		this.mongoDao = mongoDao;
	}

	public InitData getInitData() {
		return initData;
	}

	public InitPageConfig getInitPageConfig() {
		return initPageConfig;
	}

	public void setInitPageConfig(InitPageConfig initPageConfig) {
		this.initPageConfig = initPageConfig;
	}

	@Resource
	public void setInitData(InitData initData) {
		try {
			String datype = this.getClass().getSimpleName().toLowerCase();
			if (datype.indexOf(Constant.DEFAULT_DATABASE_TYPE) > -1) {
				init(initData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.initData = initData;
	}

	/**
	 * 获取权限信息
	 * 
	 * @param code
	 * @return
	 */
	public Authorization getPermission(String code) {
		Collection<Authorization> authorizations = SecurityResourceCache.getAllCache();
		Authorization rauthorization = null;
		for (Authorization authorization : authorizations) {
			String tempCode = authorization.getCode();
			if (tempCode != null && code.equals(tempCode)) {
				rauthorization = authorization;
				break;
			}
		}
		return rauthorization;
	}

	/**
	 * 获取控制器url
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String getController(String code) throws Exception {
		Collection<Authorization> authorizations = SecurityResourceCache.getAllCache();
		String controller = "";
		for (Authorization authorization : authorizations) {
			String tempCode = authorization.getCode();
			if (tempCode != null && code.equals(tempCode)) {
				controller = authorization.getController();
				break;
			}
		}
		return controller;
	}


	/**
	 * 创建文件
	 */
	public File createFile(String pathFileName, HttpServletRequest request) throws Exception {
		String realPath = request.getSession().getServletContext().getRealPath("/");
		if (realPath == null) {
			try {
				realPath = request.getSession().getServletContext().getResource("/").toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		File file = new File(realPath + ICommonService.SEPARTOR + pathFileName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				if (file.getParentFile().mkdirs()) {
					file.createNewFile();
				}
			} else {
				file.createNewFile();
			}
		}
		return file;
	}

	/**
	 * 创建文件 根据相对路径和文件名
	 */
	public File createFile(String filePath, String fileName, HttpServletRequest request) throws Exception {
		String realPath = request.getSession().getServletContext().getRealPath("/");
		if (realPath == null) {
			try {
				realPath = request.getSession().getServletContext().getResource("/").toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		File file = null;
		File tempFile = new File(realPath + ICommonService.SEPARTOR + filePath);
		if (!tempFile.exists()) {
			if (tempFile.mkdirs()) {
				file = new File(realPath + ICommonService.SEPARTOR + filePath + ICommonService.SEPARTOR + fileName);
			}
		} else {
			file = new File(realPath + ICommonService.SEPARTOR + filePath + ICommonService.SEPARTOR + fileName);
		}
		return file;
	}

	/**
	 * 删除文件 根据文件相对路径和文件名
	 */
	public void deleteFile(String filePath, String fileName, HttpServletRequest request) throws Exception {
		String realPath = request.getSession().getServletContext().getRealPath("/");
		if (realPath == null) {
			try {
				realPath = request.getSession().getServletContext().getResource("/").toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		File file = new File(realPath + ICommonService.SEPARTOR + filePath + ICommonService.SEPARTOR + fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 删除文件
	 */
	public void deleteFile(String pathFileName, HttpServletRequest request) throws Exception {
		String realPath = request.getSession().getServletContext().getRealPath("/");
		if (realPath == null) {
			try {
				realPath = request.getSession().getServletContext().getResource("/").toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		File file = new File(realPath + ICommonService.SEPARTOR + pathFileName);
		if (file.exists()) {
			file.delete();
		}
	}
}