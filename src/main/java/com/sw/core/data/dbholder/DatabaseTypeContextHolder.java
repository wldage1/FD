package com.sw.core.data.dbholder;

/**
 * 数据库类型上下文处理类
 * @author  
 *
 */
public class DatabaseTypeContextHolder {
	
	/**
	 * 线程安全的上下文对象
	 */
	private static final ThreadLocal<String> context = new ThreadLocal<String>();
	
	/**
	 * 获取数据源类型
	 * @return
	 */
    public static String getDatabaseType() {    
        return context.get();    
    }   
 
	/**
	 * 设置数据库类型
	 * @param dbType
	 */
	public static void setDatabaseType(String dbType) {    
		context.set(dbType);   
    }   
 
    /**
     *清除上下文信息
     */
    public static void clear() {    
    	context.remove();   
    } 
}
