package com.sw.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sw.core.common.Constant;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

@Component
public class FreemarkerUtil {
	// templatePath模板文件存放路径
	// templateName 模板文件名称
	// filename 生成的文件名称
	public void analysisTemplate(String templatePath, String templateName, String fileName, Map<?, ?> paramData) throws Exception {
		Configuration config = null;
		File tempFile = null;
		Template template = null;
		File file = null;
		FileOutputStream fos = null;
		Writer out = null;
		try {
			config = new Configuration();
			tempFile = new File(templatePath);
			if (!tempFile.exists()) {
				if (tempFile.isDirectory()) {
					tempFile.mkdirs();
				}
			}
			// 设置要解析的模板所在的目录，并加载模板文件
			config.setDirectoryForTemplateLoading(tempFile);
			// 设置包装器，并将对象包装为数据模型
			config.setObjectWrapper(new DefaultObjectWrapper());

			// 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
			// 否则会出现乱码
			template = config.getTemplate(templateName, Constant.ENCODING);
			// 生成文件目录
			file = new File(fileName);
			File parent = file.getParentFile();
			file = null;
			if (parent != null)
				parent.mkdirs();
			parent = null;
			// 合并数据模型与模板
			fos = new FileOutputStream(fileName);
			out = new OutputStreamWriter(fos, Constant.ENCODING);
			template.process(paramData, out);
		} finally {
			fos.close();
			out.flush();
			out.close();
			template = null;
			config.clearTemplateCache();
			config = null;
			tempFile = null;
		} 
	}
	
	/**
	 *  创建临时模板
	 *  @param templatePath
	 *  @param templateName
	 *  @param templateContent
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-15 上午9:49:16
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	public String creatTemplate(String templatePath, String templateName, String templateContent) throws Exception {
		File tempFile = null;
		FileOutputStream fos = null;
		tempFile = new File(templatePath);
		if (!tempFile.exists()) {
				tempFile.mkdirs();
		}
		File ftlFile = new File(templatePath +"/"+ templateName+".ftl");
		    fos = new FileOutputStream(ftlFile);  
			fos.write(templateContent.getBytes(Constant.ENCODING));  
			fos.close();
		return ftlFile.getName();
	}
	//更新消息模板内容
/*	public void updateTemplateContent(String templatePath, String templateName, String templateContent) throws Exception {
		File tempFile = null;
		FileOutputStream fos = null;
		tempFile = new File(templatePath);
		if (!tempFile.exists()) {
				tempFile.mkdirs();
		}
		File ftlFile = new File(templatePath +"/"+ templateName+".ftl");
		fos = new FileOutputStream(ftlFile);  
		fos.write(templateContent.getBytes(Constant.ENCODING));  
		fos.close();
	}*/
	
	
}
