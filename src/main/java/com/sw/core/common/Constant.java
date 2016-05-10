package com.sw.core.common;

public class Constant {
	
	/** 应用绝对路径 */
	public static String APPLICATIONPATH;
	/** 应用编码 **/
	public static final String ENCODING = "UTF-8";
	/** 系统目录 */
	public static final String BASE = "base";
	/** 默认样式目录 */
	public static final String SKIN = "skin";
	public static String STYLE = "common/skin";
	/** FREEMARKER 文件*/
	public static final String FREEMARKERSUFFIX=".ftl";
	/** JSP 文件*/
	public static final String JSPSUFFIX=".jsp";
	/** 目录分隔符号*/
	public static final String DIRSPLITER="/";
	/** 导入数据文件上传目录 **/
	public static final String DATAFILEPATH = "dataFile" ;
	
	/**
	 * 系统log4j日志类型 
	 */
	public static final String FRAMEWORK = "FRAMEWORK";

	public static String DATASOURCE_MYSQL = "mysql";
	public static String DATASOURCE_ORACLE = "oracle";
	public static String DATASOURCE_SQLSERVER = "sqlserver";
	public static String DATASOURCE_DB2 = "db2";
	public static String DATASOURCE_SYBASE = "sybase";
	public static String DATASOURCE_INFORMIX = "informix";
	public static String DATASOURCE_POSTGRES = "postgres";
	public static String DATASOURCE_MONGODB = "mongo";
	/** 保存登录会员ID的Session名称 */
	public static final String MEMBER_LOGIN_SESSION_ID = "memberLoginId";
	/** 保存登录来源URL的Session名称 */
	public static final String LOGIN_REDIRECTION_URL_SESSION_NAME = "redirectionUrl";
	/** 密码找回Key分隔符 */
	public static final String PASSWORD_RECOVER_KEY_SEPARATOR = "_";
	/** 密码找回Key有效时间（单位：分钟） */
	public static final int PASSWORD_RECOVER_KEY_PERIOD = 10080;
	/** 保存登录会员用户名的Cookie名称 */
	public static final String LOGIN_MEMBER_USERNAME_COOKIE_NAME = "loginMemberUsername";

	public static final String INDEX = "index";
	public static final String EXIT = "exit";
	public static final String FILE_SUFFIXES = ".swf";
	public static final String ERROR = "error";
	public static final String WELCOME = "welcome";
	public static final String LEFT = "left";
	public static final String MAIN = "main";
	public static final String PERMIT_All = "PERMIT_All";

	/** 无权访问 */
	public static String PERMITALL = "permitall";
	/** json返回状态 */
	public static String STATUS = "status";
	
	/** 使用数据库类型 */
	public static String DEFAULT_DATABASE_TYPE;

	/**允许转换类型*/
	public static String ONLINEREADFILETYPE_KEY = "";
	/**swftool安装路径*/
	public static String SWFTOOLSPATH_KEY = "";
	/**jodconverter jar包路径*/
	public static String PDFJARPATH_KEY = "";
	/**文档类型转换PDF方式标识，1：openoffice   2:libreoffice*/
	public static String TRANSFORTYPEFLAG_KEY = "";
	
	/**模型类型常量*/
	//模型类型-主模型
	public static final Integer DATAMODEL_TYPE_PRIMARYMODEL = 1;
	//模型类型-子模型
	public static final Integer DATAMODEL_TYPE_CHILDRENMODEL = 2;
	/**岗位常量*/
	//岗位-数据管理员
	public static final String POSITION_DM = "1";
	//岗位-TSR
	public static final String POSITION_TSR = "2";
	
	/**系统配置的功能类型*/
	//模型类型-主模型
	public static final Integer FUNCTIONTYPE_AREA = 1;
	
	/**产品属性缺省显示值来源*/
	//来自字典
	public static final Integer PRODUCT_ATTRIBUTE_SOURCE_DICTIONARY = 1;
	//来自本地
	public static final Integer PRODUCT_ATTRIBUTE_SOURCE_LOCAL = 2;
	//自定义
	public static final Integer PRODUCT_ATTRIBUTE_SOURCE_DEFINED = 3;
	/*****************录音文件服务器 FTP相关配置******************/
	public static final String AUTIAGENT_FTP_URL = "192.168.243.101";
	
	public static final int AUTIAGENT_FTP_PORT = 22;
	
	public static final String AUTIAGENT_FTP_USERNAME = "administrator";
	
	public static final String AUTIAGENT_FTP_PASSWORD = "22222";
	/******商路通FTP地址信息********/
	public static final String SYNROUTE_FTP_URL = "192.168.249.31";
	
	public static final int SYNROUTE_FTP_PORT = 22;
	
	public static final String SYNROUTE_FTP_USERNAME = "test";
	
	public static final String SYNROUTE_FTP_PASSWORD = "test";
	
}
