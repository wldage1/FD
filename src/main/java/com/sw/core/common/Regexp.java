package com.sw.core.common;

public class Regexp {

	/**
	 * 英文字母
	 */
	public static final String ALPHABETIC = "^[A-Za-z]+$";

	/**
	 * 中文汉字
	 */
	public static final String CHINESE = "^[\u4e00-\u9fa5]+$";

	/**
	 * 字母加数字加下划线
	 */
	public static final String CHARACTER = "^[a-zA-Z0-9_]+$";
	
	
	/**
	 * 英文或中文
	 */
	public static final String ENORCH = "^[a-zA-Z\\u4e00-\\u9fa5]+$";

	/**
	 * 数字
	 */
	public static final String NUMBERAL = "^[0-9]*$";

	/**
	 * 电子邮件
	 */
	public static final String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	/**
	 * 邮政编码
	 */
	public static final String POSTCODE = "^$|^[\\s\\S]{6}";

	/**
	 * 移动电话
	 */
	public static final String MOBILEPHONE = "^((13[0-9])|(15[^4, \\D])|(18[0,5-9]))\\d{8}$";

	/**
	 * 固定电话
	 */
	public static final String TELEPHONE = "^$|(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)(\\d{8}|\\d{7})";

}
