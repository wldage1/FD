package com.sw.core.initialize;

import java.util.List;
import java.util.Map;


public class InitPageConfig {
	
	/**系统缓存配置**/
    public Map<String,List<String>> pageCache;
    
    public InitPageConfig(){
    }

	public Map<String, List<String>> getPageCache() {
		return pageCache;
	}

	public void setPageCache(Map<String, List<String>> pageCache) {
		this.pageCache = pageCache;
	}

}
