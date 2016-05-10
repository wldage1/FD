package com.sw.core.data.entity;

import java.io.Serializable;


/**
 *页面缓存存储类
 * @author Administrator
 *
 */
public class Page implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 页面url
     */
    private String url;
    
    /**
     * 页面内容
     */
    private String content;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
}
