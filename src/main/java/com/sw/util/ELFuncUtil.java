package com.sw.util;

import java.util.Map;


public class ELFuncUtil {  
    /** 
     * EL方法用于获取继续操作连接 
     *  
     * @param requestMap 
     * @param sessionMap 
     * @return 
     */  
    public static String controller(Map<String,String> requestMap,Map<String,String> sessionMap) {  
    	return (String) sessionMap.get("controller_"+requestMap.get("statusParam"));
    } 
    /** 
     * EL方法用于获取返回街垒连接 
     *  
     * @param requestMap 
     * @param sessionMap 
     * @return 
     */
    public static String parentcontroller(Map<String,String> requestMap,Map<String,String> sessionMap) {  
    	return (String) sessionMap.get("parent_controller_"+requestMap.get("statusParam"));
    } 
}  