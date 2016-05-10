package com.sw.core.data.entity;

import java.io.Serializable;


/**
 * 系统级参数配置存储类
 * @author Administrator
 *
 */
public class Setting implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 系统部署绝对路径
     */
    private String realPath;
    
    /**
     * 系统工程绝对路径
     */
    private String workspacePath;
    
    /**
     * 系统是否为调试模式
     */
    private boolean isDebug;
    
    /** 系统目录 */
    private String base;
    /** 默认样式目录 */
    private String skin ;
    
    

    public Setting(){  
    }


	public String getRealPath() {
		return realPath;
	}


	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}


	public String getWorkspacePath() {
		return workspacePath;
	}


	public void setWorkspacePath(String workspacePath) {
		this.workspacePath = workspacePath;
	}


	public boolean isDebug() {
		return isDebug;
	}


	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}


	public String getBase() {
		return base;
	}


	public void setBase(String base) {
		this.base = base;
	}


	public String getSkin() {
		return skin;
	}


	public void setSkin(String skin) {
		this.skin = skin;
	}  
    
}
