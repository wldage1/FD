package com.sw.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JqGridUtil {
	public static Map<String, String> oper = null ;
	
	public static StringBuffer constructWhere(JsonObject jsonObject){
		StringBuffer query = new StringBuffer() ;  
    	if (jsonObject.isJsonObject()) {
    		oper = oper == null ? oper() : oper ;
    		String groupOp = jsonObject.get("groupOp").getAsString() ;
    		JsonElement rules = jsonObject.get("rules");
    		int index = 0;
            for (JsonElement e : rules.getAsJsonArray()) {
            	String field = e.getAsJsonObject().get("field").getAsString();
                String op = e.getAsJsonObject().get("op").getAsString();
                String data = e.getAsJsonObject().get("data").getAsString();
                if(!op.isEmpty() && !data.isEmpty()) {
                	if(index ++ > 0) {
                    	query.append(" ").append(groupOp).append(" ") ;
                    }
                	//in not in 条件
                	if("in".equals(op) || "ni".equals(op)) {
                		query.append(field).append(oper.get(op)).append("(").append(data).append(") ") ;
                	}
                	//包含||不包含
                	else if("cn".equals(op)||"nc".equals(op)){
                		query.append(field).append(oper.get(op)).append("'%").append(data).append("%'");
                		//结束||不结束于
                	}else if("ew".equals(op)||"en".equals(op)){
                		query.append(field).append(oper.get(op)).append("'%").append(data).append("'");
                		//开始||不开始于
                	}else if("bw".equals(op)||"bn".equals(op)){
                		query.append(field).append(oper.get(op)).append("'").append(data).append("%'");
                	}
                	else {
                		query.append(field).append(oper.get(op)).append("'").append(data).append("'") ;
                	}
                }
            }
            
            if(jsonObject.get("groups") != null) {
	            JsonArray jsonArray = jsonObject.get("groups").getAsJsonArray() ;
	            for(JsonElement e : jsonArray) {
	            	JsonObject subJsonObject = (JsonObject)e;
	            	if(subJsonObject != null) {
	            		if(index ++ > 0) {
	                    	query.append(" ").append(groupOp).append(" ") ;
	                    }
	                	query.append("(").append(constructWhere(subJsonObject)).append(") ") ;
	                }
	            }
            }
    	}
		return query ;
	}
	/**
	 * 构造where条件语句字符串
	 * filter 过滤的条件
	 * @param filter
	 * @return
	 */
	public static String constructWhere(String filter) {  
		StringBuffer query = new StringBuffer();  
        if (!filter.isEmpty()) {  
        	JsonObject jsonObject = new JsonParser().parse(filter).getAsJsonObject();
        	query = constructWhere(jsonObject) ;
        } 
        //return transactSQLInjection(query.toString());
        return query.toString();  
    }  
	
	public static Map<String, String> oper(){
		oper = new HashMap<String, String>() ;
		oper.put("eq", " = ");  
        oper.put("ne", " <> ");  
        oper.put("lt", " < ");  
        oper.put("le", " <= ");  
        oper.put("gt", " > ");  
        oper.put("ge", " >= ");  
        oper.put("bw", " LIKE ");  
        oper.put("bn", " NOT LIKE ");  
        oper.put("in", " IN ");  
        oper.put("ni", " NOT IN ");  
        oper.put("ew", " LIKE ");  
        oper.put("en", " NOT LIKE ");  
        oper.put("cn", " LIKE ");  
        oper.put("nc", " NOT LIKE ");
        return oper;
	}
	
	/**
	 * 防止sql注入
	 * 
	 * @param sql
	 * @return
	 */
	public static String transactSQLInjection(String sql) {
		return sql.replaceAll(".*([';]+|(--)+).*", " ");
	}
	
}
