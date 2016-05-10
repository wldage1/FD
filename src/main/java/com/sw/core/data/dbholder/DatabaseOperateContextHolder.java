package com.sw.core.data.dbholder;

/**
 * 数据库操作标识上下文处理类
 * @author  
 *
 */
public class DatabaseOperateContextHolder {
	
	/**
	 * 线程安全的上下文对象
	 */
	private static final ThreadLocal<String> context = new ThreadLocal<String>();
	
 
    /**
	 * 设置数据操作类型
	 * @param op
	 */
	public static void setOperateContext(String op) {    
		context.set(op);   
    }   
  
	/**获取数据操作类型
	 * @return
	 */
    public static String getOperateContext() {    
        return context.get();    
    }   
 
    /**
     *清除数据操作类型上下文信息
     */
    public static void clear() {    
    	context.remove();   
    } 
}
