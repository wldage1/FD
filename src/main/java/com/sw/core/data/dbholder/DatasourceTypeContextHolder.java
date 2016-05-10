package com.sw.core.data.dbholder;

/**
 * 数据源类型上下文处理类
 * @author  
 *
 */
public class DatasourceTypeContextHolder {
	
	/**
	 * 线程安全的上下文对象
	 */
	private static final ThreadLocal<String> context = new ThreadLocal<String>();
	
	/**
	 * 设置数据源类型
	 * @param dsType
	 */
	public static void setDataSourceType(String dsType) {    
		context.set(dsType);   
    }   
  
	/**
	 * 获取数据源类型
	 * @return
	 */
    public static String getDataSourceType() {    
        return context.get();    
    }   
 
    /**
     *清除上下文信息
     */
    public static void clear() {    
    	context.remove();   
    } 
}
