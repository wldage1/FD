package com.sw.core.initialize;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;

import com.sw.core.cache.PluginsConfigCache;
import com.sw.core.data.entity.Authorization;
import com.sw.util.FileUtil;

public class PluginsConfigLoader {

	private static Logger logger = Logger.getLogger(PluginsConfigLoader.class);

	/** 插件文件名称 */
	private static String PLUGIN_FILE_NAME = "plugin-config.xml";
	/** 中文语言配置文件模板文件名 */
	private static String PROPERTY_CN = "pluginsMessages_zh_CN.tmp";
	/** 中文语言配置文件名 */
	private static String PROPERTY_CN_REAL = "pluginsMessages_zh_CN.properties";
	/** 英文语言配置文件模板文件名 */
	private static String PROPERTY_US = "pluginsMessages_en_US.tmp";
	/** 英文语言配置文件名 */
	private static String PROPERTY_US_REAL = "pluginsMessages_en_US.properties";
	/** ibatis 3 sqlmap 配置文件根目录 */
	private static String CLS_PATH = "classes";
	/** ibatis 3 插件配置文件存放变量 */
	//private StringBuffer sqlMapperssb = new StringBuffer();
	/** 中文语言配置文件存放变量 */
	private StringBuffer propertiecn = new StringBuffer();
	/** 英文语言配置文件存放变量 */
	private StringBuffer propertieus = new StringBuffer();
	/** 系统权限存放列表对象 */
	private Resource configLocation;

	public void init() {
		// 获取系统绝对路劲，针对tomcat和weblogic获取路径不同
		File pluginsFile = null;
		String parentPath = null;
		String classesPath = null;
		try {
			pluginsFile = configLocation.getFile();
			parentPath = configLocation.getFile().getParent();
			if (parentPath != null) {
				int cindex = parentPath.indexOf(CLS_PATH);
				if (cindex > -1) {
					classesPath = parentPath.substring(0, cindex + CLS_PATH.length());
				}
			}
		} catch (IOException e1) {
			try {
				throw new Exception("file path not find!");
			} catch (Exception e) {
				logger.debug(e.getMessage());
				System.exit(0);
			}
		}
		// 资源文件中文语言包 配置文件验证
		String procn = parentPath + File.separator + "core" + File.separator + "template" + File.separator + PROPERTY_CN;
		File procnFile = new File(procn);
		// 如果文件不存在，抛出异常，并系统退出
		if (!procnFile.exists()) {
			try {
				throw new Exception("spring language(cn) propertie template file '" + PROPERTY_CN + "' is required");
			} catch (Exception e) {
				logger.debug(e.getMessage());
				System.exit(0);
			}
		}
		// 资源文件英文语言包 配置文件验证
		String prous = parentPath + File.separator + "core" + File.separator + "template" + File.separator + PROPERTY_US;
		File prousFile = new File(prous);
		// 如果文件不存在，抛出异常，并系统退出
		if (!prousFile.exists()) {
			try {
				throw new Exception("spring language(us) propertie template file '" + PROPERTY_US + "' is required");
			} catch (Exception e) {
				logger.debug(e.getMessage());
				System.exit(0);
			}
		}
		// 系统插件配置文件目录
		if (pluginsFile != null && pluginsFile.exists()) {
			try {
				getPluginFileRecursion(pluginsFile);
			} catch (Exception e) {
				logger.debug(e.getMessage());
				System.exit(0);
			}
		}

		// 读取本地propertie配置文件模板,并将插件里面资源文件的内容替换到中文配置文件中
		try {
			// 中文pro真实文件处理
			File realProcn = new File(classesPath + File.separator + PROPERTY_CN_REAL);
			if (realProcn.exists()) {
				realProcn.delete();
			}
			realProcn.createNewFile();
			FileUtil.strToFile(propertiecn.toString(), realProcn);
		} catch (IOException e) {
			logger.debug(e.getMessage());
		}
		// 读取本地propertie配置文件模板,并将插件里面资源文件的内容替换到英文配置文件中
		try {
			// 中文pro真实文件处理
			File realProus = new File(classesPath + File.separator + PROPERTY_US_REAL);
			if (realProus.exists()) {
				realProus.delete();
			}
			realProus.createNewFile();
			FileUtil.strToFile(propertieus.toString(), realProus);
		} catch (IOException e) {
			logger.debug(e.getMessage());
		}
	}

	/**
	 * 递归方式读取插件配置文件，并处理ibatis3 sqlmap和propertie 配置文件
	 * 
	 * @param parentFile
	 * @param sqlMapperssb
	 * @param propertiesb
	 * @throws Exception
	 */
	public void getPluginFileRecursion(File parentFile) throws Exception {
		if (parentFile != null) {
			File file[] = parentFile.listFiles();
			if (file != null) {
				for (int i = 0; i < file.length; i++) {
					if (file[i] != null) {
						if (file[i].isFile()) {
							// 插件配置文件
							if (file[i].getName().indexOf(PLUGIN_FILE_NAME) > -1) {
								this.parserXml(file[i]);
							}
						} else {
							getPluginFileRecursion(file[i]);
						}
					}
				}
			}
		}
	}

	/**
	 * dom4j 解析xml 文件
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public void parserXml(File pfile) throws Exception {
		try {
			String fileName = pfile.getName();
			int pindex = fileName.indexOf("(");
			int eindex = fileName.indexOf(")");
			String region = fileName.substring(pindex + 1, eindex);
			if (region != null) {
				String arr[] = region.split("-");
				if (arr != null && arr.length == 2) {
					String parentCode = arr[0];
					String subCode = arr[1];
					if (subCode.length() == 1) {
						subCode = parentCode + "0" + subCode;
					} else {
						subCode = parentCode + subCode;
					}
					String codeRelation = ",0,";
					String fpath = pfile.getParent();
					SAXReader saxReader = new SAXReader();
					// 把文件读入到文档
					Document document = saxReader.read(pfile);
					// 获取文档根节点
					Element ele = document.getRootElement();
					// 递归计数器
					int index = 0;
					this.recursionParser(null, null, parentCode, subCode, codeRelation, index+1, ele, fpath);
				}
			}
		} catch (DocumentException e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 递归方式解析
	 * 
	 * @param ele
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private int recursionParser(String controller, String parentNodeName, String parentCode, String subCode, String codeRelation, int index, Element ele, String proPath) {
		int idx = 0;
		int childCount = 0 ;
		for (Iterator<Element> iterator = ele.elementIterator(); iterator.hasNext();) {
			Element node = iterator.next();
			String nodeName = node.getName();
			// 读取到资源文件
			if (nodeName != null && nodeName.equals("language")) {
				for (Iterator<Element> language = node.elementIterator(); language.hasNext();) {
					Element languageNode = language.next();
					// 资源文件名称
					String pcn = languageNode.getText();
					try {
						if (pcn != null) {
							// 插件内资源文件路径
							String rfile = proPath + pcn;
							// 读取资源文件内容
							String pcon = FileUtil.readFileByLines(rfile);
							// 追加到全局变量里,CN 中文 US 英文
							if (pcn.indexOf("CN") > -1) {
								propertiecn.append(pcon);
							} else {
								propertiecn.append(pcon);
							}
						}
					} catch (Exception e) {
						logger.debug(e.getMessage());
					}
				}
			} else {
				String tempCode = null;
				String tempCodeRelation = null;
				String tempParentNodeName = null;
				String tempController = null;
				if (parentNodeName == null || parentNodeName.equals("")) {
					tempParentNodeName = nodeName;
				} else {
					tempParentNodeName = parentNodeName + "_" + nodeName;
				}
				if (controller == null || controller.equals("")) {
					tempController = "/" + nodeName;
				} else {
					tempController = controller + "/" + nodeName;
				}
				// 内部循环计数,子标签个数 除了language标签
				idx++;
				// 系统权限信息
				Authorization authorization = new Authorization();
				if (index == 1) {
					tempCode = subCode;
					tempCodeRelation = codeRelation + parentCode + ",";
					authorization.setRelationPath(tempCodeRelation);
					authorization.setParentCode("0");
					authorization.setCode(parentCode);
					// 设置功能首页文件名称
					authorization.setPageName(nodeName);
				} else if (index == 2) {
					tempCode = subCode;
					tempCodeRelation = codeRelation + subCode + ",";
					authorization.setRelationPath(tempCodeRelation);
					authorization.setParentCode(parentCode);
					authorization.setCode(subCode);
					// 设置功能首页文件名称
					authorization.setPageName(nodeName);
				} else {
					parentCode = subCode;
					if (idx < 10) {
						tempCode = subCode + "0" + idx;
					} else {
						tempCode = subCode + idx;

					}
					tempCodeRelation = codeRelation + tempCode + ",";
					authorization.setRelationPath(tempCodeRelation);
					authorization.setParentCode(parentCode);
					authorization.setCode(tempCode);
				}
				// 设置index
				authorization.setIndexCode(tempParentNodeName);
				// 设置级别
				authorization.setTreeLevel(String.valueOf(index));
				// 设置控制器url
				authorization.setController(tempController);
				// 默认设置为非动态功能
				authorization.setType(0);
				// 默认是授权功能
				authorization.setSetAuthorityOrNot("true");
				
				int childSize = 0 ;
				if (node.elementIterator().hasNext()) {
					childSize = this.recursionParser(tempController, tempParentNodeName, parentCode, tempCode, tempCodeRelation, ++index, node, proPath);
				}
				
				if (node.attributes() != null && node.attributes().size() > 0) {
					for (Iterator subiterator = node.attributeIterator(); subiterator.hasNext();) {
						Attribute item = (Attribute) subiterator.next();
						String attrName = item.getName();
						String attrValue = item.getValue();
						
						if (attrName != null && attrName.equals("name")) {
							authorization.setName(attrValue);
						}
						if (attrName != null && attrName.equals("isDynamic")) {
							if("true".contains(attrValue)){
								authorization.setType(1);
							}
						}
						if (attrName != null && attrName.equals("isAuthority")) {
							authorization.setSetAuthorityOrNot(attrValue);
						}
						if (attrName != null && attrName.equals("isLog")) {
							authorization.setLogOrNot(attrValue);
						}
						if (attrName != null && attrName.equals("isNav")) {
							authorization.setNavigateOrNot(attrValue);
						}
						if (attrName != null && attrName.equals("isHelp")) {
							authorization.setHelpOrNot(attrValue);
						}
						if (attrName != null && attrName.equals("isSetAuth")) {
							authorization.setSetAuthOrNot(attrValue);
						}
						if (attrName != null && attrName.equals("isStatus")) {
							authorization.setSetStatusOrNot(attrValue);
						}
						if (attrName != null && attrName.equals("icon")) {
							authorization.setIcon(attrValue);
						}
						if (attrName != null && attrName.equals("controller")) {
							authorization.setController(attrValue);
						}						
					}
					if (node.elementIterator().hasNext() && childSize > 0) {
						authorization.setIsChildNode(0) ;
					}else {
						authorization.setIsChildNode(1) ;
					}
					// 放到缓存当中
					PluginsConfigCache.putAuthCache(authorization);
					
					childCount ++ ;
				}
			}
		}
		return childCount ;
	}

	public Resource getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

}
