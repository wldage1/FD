package com.sw.core.data.dbholder;

/**
 * 创建者上下文处理类
 * @author  
 *
 */
public class CreatorContextHolder {
	
	/**
	 *  线程安全的上下文对象
	 */
	private static final ThreadLocal<String> context = new ThreadLocal<String>();
 
    
    /**
	 * 设置数据操作类型
	 * @param creator
	 */
	public static void setCreatorContext(String creator) {    
		context.set(creator);   
    }   
  
	/**获取数据操作类型
	 * @return
	 */
    public static String getCreatorContext() {    
        return context.get();    
    }   
    
    /**
     *清除型上下文信息
     */
    public static void clear() {    
    	context.remove();   
    }    
}
