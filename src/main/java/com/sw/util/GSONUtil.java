package com.sw.util;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 类简要说明
 * 
 * @author Administrator
 */
public class GSONUtil {

	/**
	 * 合并JSON
	 * 
	 * @param obj
	 * @return
	 * @author Administrator
	 */
	public static String combinJson(Object obj) {
		return new Gson().toJson(obj);
	}

	/**
	 * 
	 * 转换JSON为对象
	 * 
	 * @param json
	 * @param obj
	 * @return
	 * @author Administrator
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object parseJsonToObject(String json, Class clazz) {
		return new Gson().fromJson(json, clazz);
	}

	/**
	 * 转换JSON为LIST
	 * 
	 * @param json
	 * @return
	 * @author Administrator
	 */
	public static List<?> parseJsonToList(String json, Type type) {
		return new Gson().fromJson(json, type);
	}

	/**
	 * 
	 * 转换JSON为MAP
	 * 
	 * @param json
	 * @param obj
	 * @return
	 * @author Administrator
	 */
	public static Map<String, Object> parseJsonToMap(String json) {
		Type mapType = new TypeToken<Map<String, String>>() {
		}.getType();
		return new Gson().fromJson(json, mapType);
	}

	// @SuppressWarnings("unchecked")
	// public static void main(String args[]){
	// // Member member = new Member();
	// // member.setUserName("Jing");
	// // member.setCity("Beijing");
	// // String json = new JsonUtil<Member>().combinJson(member);
	// // System.out.println(json);
	// // System.out.println(new
	// JsonUtil<Member>().parseJsonToObject(json,Member.class).getUserName());
	// // String jsons =
	// "[{\"birthdayYear\":0,\"birthdayMonth\":0,\"birthdayDate\":0,\"cardType\":\"card0\",\"married\":0,\"income\":0.0,\"totalUsingCount\":0,\"userName\":\"name0\",\"delStatus\":0,\"id\":0}]";
	// // List<Member> list = new JsonUtil<Member>().parseJsonToList(jsons,new
	// TypeToken<List<Member>>(){}.getType());
	// // System.out.println(list.get(0).getBirthdayYear());
	// Map<String,String> map = new HashMap<String, String>();
	// map.put("jing", "JING");
	// map.put("vlaue", "VALUE");
	// String json = JsonUtil.combinJson(map);
	// Type mapType = new TypeToken<Map<String, String>>(){}.getType();
	// Map<String, String> son = new Gson().fromJson(json, mapType);
	// for (Map.Entry<String, String> m : son.entrySet()) {
	// System.out.println(m.getKey() + "\t" + m.getValue());
	// }
	// }

}
