package com.sw.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ActivitiesDom {

	@SuppressWarnings("unchecked")
	public Activities getActivities(String xmlFile) {
		Map<String, String> functionMap = new HashMap<String, String>();
		Map<String, String> fieldMap = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(new File(xmlFile));
			Element node = document.getRootElement();
			Element tnode = node.element("functions");
			if (tnode != null) {
				Iterator<Element> it = tnode.elementIterator();
				while (it.hasNext()) {
					Element e = it.next();
					Map<String, String> m = setFunction(e);
					functionMap.putAll(m);
				}
			}
			tnode = node.element("fields");
			if (tnode != null) {
				Iterator<Element> it = tnode.elementIterator();
				while (it.hasNext()) {
					Element e = it.next();
					Map<String, String> m = setField(e);
					fieldMap.putAll(m);
				}
			}
		} catch (DocumentException e) {
		}
		Activities acti = new Activities();
		acti.setFieldMap(fieldMap);
		acti.setFunctionMap(functionMap);
		return acti;
	}

	private Map<String, String> setFunction(Element node) {
		if(node == null){
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		Element ecode = node.element("code");
		Element eurl = node.element("url");
		String code = ecode.getTextTrim();
		String url = eurl.getTextTrim();
		map.put(code, url);
		return map;
	}

	private Map<String, String> setField(Element node) {
		if(node == null){
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		Element ename = node.element("name");
		Element ecode = node.element("code");
		String name = ename.getTextTrim();
		String code = ecode.getTextTrim();
		map.put(name, code);
		return map;
	}

//	public static void main(String[] args) {
//		ActivitiesDom a = new ActivitiesDom();
//		a.getActivities("D:/t.xml");
//	}
}

class Activities {
	private Map<String, String> functionMap;
	private Map<String, String> fieldMap;

	Activities() {
	}

	public Map<String, String> getFunctionMap() {
		return functionMap;
	}

	public void setFunctionMap(Map<String, String> functionMap) {
		this.functionMap = functionMap;
	}

	public Map<String, String> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}
}
