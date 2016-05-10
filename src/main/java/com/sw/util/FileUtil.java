package com.sw.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.sw.core.common.Constant;

public class FileUtil {
	static boolean init = false;

	public static void deleteFile(String filename) throws IOException {
		File file = new File(filename);

		if (file.isDirectory()) {
			throw new IOException("IOException -> BadInputException: not a file.");
		}
		if (file.exists() == false) {
			throw new IOException("IOException -> BadInputException: file is not exist.");
		}
		if (file.delete() == false) {
			throw new IOException("Cannot delete file. filename = " + filename);
		}
	}

	public static void deleteDir(File dir) throws IOException {
		if (dir.isFile())
			throw new IOException("IOException -> BadInputException: not a directory.");
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					file.delete();
				} else {
					deleteDir(file);
				}
			}
		}// if
		dir.delete();
	}

	public static void lowCaseFile(String fileName) {
		File f = new File(fileName);
		String lowCaseFileName = fileName.toLowerCase();
		File lowCaseF = new File(lowCaseFileName);

		if (!fileName.equals(fileName.toUpperCase())) {
			f.renameTo(lowCaseF);
		}

		if (lowCaseF.isDirectory()) {
			File[] files = lowCaseF.listFiles();

			for (int i = 0; i < files.length; i++) {
				lowCaseFile(files[i].getPath());
			}

		}
	}

	public static void main(String arg[])

	{
//		String str = "";
		// String str = "abc32.45 <> 1.42**.11222YY0.Xaab12.999.+0.9&bbc ";
		// List numbers = new ArrayList();
		//
		// StringBuilder sb = new StringBuilder(str);
		//
		// while(sb.indexOf( ".") >= 0) {
		// StringBuilder sbFront = getFront(sb);
		// StringBuilder sbEnd = getEnd(sb);
		// if (sbFront.length() > 0 && sbEnd.length() > 0) {
		// numbers.add(sbFront.append( ".").append(sbEnd));
		// }
		// }
		//
		// for(Object obj : numbers) {
		// System.out.println(obj);
		// }
		// try{
		// String str1=
		// "d:\\Documents and Settings\\deng_shuanghua\\����\\CustomerVisitManageAction.java";
		// String str2= "d:\\CustomerVisitManageAction.java";
		// File file = new File(str1);
		// file.renameTo(new
		// File("d:\\Documents and Settings\\deng_shuanghua\\����\\CustomerVisitManageAction_bak.java"));
		// fileToFile(str1,str2);
		// System.out.println("�ϴ��ɹ���");
		// }catch(IOException e){
		// e.printStackTrace();
		// }

	}

	private static StringBuilder getFront(StringBuilder sb) {

		// handle the front
		StringBuilder strb = new StringBuilder();
		for (int i = sb.indexOf(". ") - 1; i > 0; i--) {

			if (Character.isDigit(sb.charAt(i))) {
				strb.insert(0, sb.charAt(i));
			} else {
				break;
			}
		}
		return strb;
	}

	private static StringBuilder getEnd(StringBuilder sb) {

		// handle the end
		StringBuilder strb = new StringBuilder();
		int i = 0;
		for (i = sb.indexOf(". ") + 1; i > 0; i++) {

			if (Character.isDigit(sb.charAt(i))) {
				strb.append(sb.charAt(i));
			} else {
				break;
			}
		}
		// delete the string
		sb.delete(0, i);
		return strb;
	}

	public static String normalizePath(String s) {
		return normalizePath(s, File.separator);
	} // normalize(String):String

	public static String fileToStr(String fileName) throws IOException {
		return fileToStr(new File(fileName));
	}

	// public static byte[] fileToBytes(String fileName) throws IOException {
	// return fileToBytes(new File(fileName));
	// }

	// public static byte[] fileToBytes(File file) throws IOException {
	// BufferedInputStream in = null;
	// try {
	//
	// in = new BufferedInputStream(new FileInputStream(file));
	// ByteBuffer bf = new ByteBuffer();
	// int len = -1;
	// byte[] buffer = new byte[1024];
	// while ((len = in.read(buffer)) != -1) {
	// bf.append(buffer, 0, len);
	// }
	//
	// return bf.getContent();
	// } finally {
	// if (in != null)
	// in.close();
	// }
	// }

	public static void bytesToFile(File file, byte[] content) throws IOException {
		BufferedOutputStream out = null;
		try {

			File parent = file.getParentFile();
			if (parent != null)
				parent.mkdirs();

			out = new BufferedOutputStream(new FileOutputStream(file));
			out.write(content);
			out.flush();

		} finally {
			if (out != null)
				out.close();
		}

	}

	public static void bytesToFile(String strFileName, byte[] content) throws IOException {
		bytesToFile(new File(strFileName), content);

	}

	public static String fileToStr(File file) throws IOException {
		BufferedReader reader = null;
		try {
			// BufferedInputStream in = new BufferedInputStream(new
			// FileInputStream(file));
			// reader = new BufferedReader(new FileReader(file));
			// StringBuffer sb = new StringBuffer();
			// char[] buffer = new char[1024];
			// int len = -1;
			// while ((len = reader.read(buffer)) != -1) {
			// sb.append(buffer, 0, len);
			// }
			// InputStreamReader read = new InputStreamReader (new
			// FileInputStream(file),"GBK");
			// reader = new BufferedReader(read);
			// BufferedInputStream in = new BufferedInputStream(new
			// FileInputStream(file));
			StringBuffer sb = new StringBuffer();
			// while (reader.readLine() != null) {
			// sb.append(reader.readLine()+"\\r\\n") ;
			// }
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), Constant.ENCODING);
			reader = new BufferedReader(isr);
			int ch;
			while ((ch = reader.read()) > -1) {
				sb.append((char) ch);
			}
			reader.close();
			return sb.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
	
	/**
	 * 读取文件到List中
	 * @param file
	 * @param delimiter 分隔符
	 * @param isFirstColName 首行是否为列名
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> fileToList(String strFileName, String delimiter, boolean isFirstColName) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>() ;
		BufferedReader reader = null;
		try {
			File file = new File(strFileName) ;
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), Constant.ENCODING);
			reader = new BufferedReader(isr);
			String str;
			int count = 0 ;
			while ((str = reader.readLine()) != null) {
				if(isFirstColName && count == 0){
					
				}else {
					String[] lineStr = str.trim().split(delimiter);
					Map<String, String> map = new HashMap<String, String>() ;
					for(int i = 0; i < lineStr.length; i ++){
						map.put(ExcelUtil.customColENName + (i + 1), lineStr[i]) ;
					}
					list.add(map) ;
				}
				count ++ ;
			}
			return list;
		} finally {
			if (reader != null)
				reader.close();
		}
	}
	
	/**
	 * 读取文件到List中
	 * @param file
	 * @param delimiter 分隔符
	 * @param isFirstColName 首行是否为列名
	 * @return
	 * @throws Exception
	 */
	public static List<String> fileToListStr(String strFileName, String delimiter, boolean isFirstColName) throws Exception {
		List<String> list = new ArrayList<String>() ;
		BufferedReader reader = null;
		try {
			File file = new File(strFileName) ;
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), Constant.ENCODING);
			reader = new BufferedReader(isr);
			String str;
			int count = 0 ;
			while ((str = reader.readLine()) != null) {
				if(isFirstColName && count == 0){
					
				}else {
					String[] lineStr = str.trim().split(delimiter);
					for(int i = 0; i < lineStr.length; i ++){
						list.add(lineStr[i]) ;
					}
				}
				count ++ ;
			}
			return list;
		} finally {
			if (reader != null)
				reader.close();
		}
	}
	
	/**
	 * 读取文件首行为列名
	 * @param strFileName
	 * @param delimiter
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> fileToCol(String strFileName, String delimiter, boolean isFirstColName) throws Exception {
		BufferedReader reader = null;
		try {
			Map<String, String> map = new HashMap<String, String>() ;
			File file = new File(strFileName) ;
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), Constant.ENCODING);
			reader = new BufferedReader(isr);
			String str;
			
			if ((str = reader.readLine()) != null) {
				String[] lineStr = str.trim().split(delimiter);
				if(isFirstColName) {
					for(int i = 0; i < lineStr.length; i ++){
						map.put(ExcelUtil.customColENName + (i + 1), lineStr[i]) ;
					}
				}else {
					for(int i = 0; i < lineStr.length; i ++){
						map.put(ExcelUtil.customColENName + (i + 1), ExcelUtil.customColCNName + (i + 1)) ;
					}
				}
			}
			return map;
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	public static int strToFile(String strContent, String strFileName) throws IOException {

		BufferedWriter writer = null;

		try {

			File file = new File(strFileName);
			File parent = file.getParentFile();
			if (parent != null)
				parent.mkdirs();

			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(strFileName), Constant.ENCODING));
			writer.write(strContent);
			writer.flush();

			return 0;

		} finally {
			if (writer != null)
				writer.close();
		}

	}
	
	/**
	 * 以追加方式写入文件
	 * @param strContent
	 * @param strFileName
	 * @param append 为true时，写如文件
	 * @return
	 * @throws IOException
	 */
	public static int strToFile(String strContent, String strFileName, boolean append) throws IOException {
		BufferedWriter writer = null;
		try {
			File file = new File(strFileName);
			File parent = file.getParentFile();
			if (parent != null)
				parent.mkdirs();

			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(strFileName, append), Constant.ENCODING));
			writer.write(strContent);
			writer.flush();
			return 0;
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	public static void fileToFile(String src, String des) throws IOException {
		File file1 = new File(src);
		File file2 = new File(des);
		fileToFile(file1, file2);
	}

	public static void fileToFile(File src, File des) throws IOException {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(src));
			StringBuffer sb = new StringBuffer();
			char[] buffer = new char[1024];
			int len = -1;
			while ((len = reader.read(buffer)) != -1) {
				sb.append(buffer, 0, len);
			}
			File parent = des.getParentFile();
			if (parent != null)
				parent.mkdirs();
			writer = new BufferedWriter(new FileWriter(des));
			writer.write(sb.toString());
			writer.flush();
		} finally {
			if (reader != null)
				reader.close();
			if (writer != null)
				writer.close();
		}
	}

	public static int strToFile(String str_strIn, File fileOut) throws IOException {
		return strToFile(str_strIn, fileOut.getAbsolutePath());
	}
	
	

	public static String normalizePath(String s, String file_seperator) {
		if (s == null)
			return s;
		StringBuffer str = new StringBuffer();
		boolean is_spe = false;
		int len = (s != null) ? s.length() : 0;
		char ch = '\0';
		if (len > 0) {
			ch = s.charAt(0);
			if (ch == '/' || ch == '\\') {
				str.append(file_seperator);
			} else {
				str.append(ch);

			}

			for (int i = 1; i < len; i++) {
				ch = s.charAt(i);
				switch (ch) {
				case '/':
				case '\\':
					if (is_spe) {

					} else {
						str.append(file_seperator);
						is_spe = true;
					}
					break;
				default: {
					is_spe = false;
					str.append(ch);
				}
				}
			}
		}

		return str.toString();

	} // normalize(String):String

	/**
	 * Insert the method's description here. Creation date: (2003-3-7 13:41:15)
	 * 
	 * @return java.util.Date
	 * @param filePath
	 *            java.lang.String
	 */
	public static java.util.Date getFileCreateTime(String filePath) throws java.text.ParseException {

		if (!init) {
			System.loadLibrary("fileUtil");
			init = true;
		}

		if (filePath == null)
			return null;
		String t = getFileCreateTime0(filePath);
		// log.log("file:" + filePath + " createTime:" + t);
		if (t != null) {

			// System.out.println(filePath+"createTime:"+t);
			return (new java.text.SimpleDateFormat("yyyy/MM/dd/HH:mm/ss")).parse(t);

		}
		return null;
	}

	public static String url2Filename(URL u) {
		StringBuffer sb = new StringBuffer();

		sb.append(u.getHost());
		sb.append(u.getPath());
		if (u.getPath().equals(""))
			sb.append("/");

		// is there a query part ?
		// that is something after the file name seperated by ?
		String query = u.getQuery();

		// if(u.getFile().startsWith("ThemeFmore.asp"))
		// {
		// log.info("");
		// }
		//
		if ((query != null) && (!"".equals(query))) {
			sb.append("?");
			sb.append(query);
		}

		// filename that ends with /
		// are directories, we will name the file "index.html"
		if (sb.charAt(sb.length() - 1) == '/') {
			sb.append("index.html");
		}

		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			char newc = (char) 0;

			if (c == '\\' || c == '/') {
				newc = File.separatorChar;
			}
			// replace :*?"<>| Ϊ'_'

			if (c == ':' || c == '*' || c == '?' || c == '<' || c == '>' || c == '|') {
				newc = '_';
			}

			if ((newc != (char) 0) && (newc != c)) {
				sb.setCharAt(i, newc);
			}
		}

		return sb.toString();
	}

	/**
	 * Insert the method's description here. Creation date: (2003-3-7 13:41:15)
	 * 
	 * @return java.util.Date
	 * @param filePath
	 *            java.lang.String
	 */
	public static native String getFileCreateTime0(String filePath);

	public static native boolean mapDriver(String userName, String password, String remoteName, String localName);

	public static native boolean unMapDriver(String localName);

	public static String[] fileToStringArray(File file) throws IOException {
		BufferedReader reader = null;
		try {

			reader = new BufferedReader(new FileReader(file));
			Vector<String> v = new Vector<String>();
			String str = null;
			while ((str = reader.readLine()) != null) {
				v.add(str.trim());
			}

			if (v.size() > 0) {
				String[] arr = new String[v.size()];
				for (int i = 0; i < v.size(); i++) {
					arr[i] = (String) v.get(i);
				}
				return arr;
			}
			return null;
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	public static String[] fileToStringArray(String fileName) throws IOException {
		return fileToStringArray(new File(fileName));
	}

	/**
	 * 文件复制方法
	 * @param oldPath  原文件
	 * @param targetFilePath 复制的目标文件
	 */
	public static void copySwfFile(String oldPath,String targetFilePath){
	       try  {  
	           int  byteread  =  0;  
	           File  oldfile  =  new  File(oldPath);  
	           if  (oldfile.exists())  {  //文件存在时  
	               InputStream  inStream  =  new  FileInputStream(oldPath);  //读入原文件  
	               FileOutputStream  fs  =  new  FileOutputStream(targetFilePath);  
	               byte[]  buffer  =  new  byte[1444];  
	               while  (  (byteread  =  inStream.read(buffer))  !=  -1)  {  
	                   fs.write(buffer,  0,  byteread);  
	               }  
	               inStream.close();  
	           }  
	       }  
	       catch  (Exception  e)  {  
	           System.out.println("复制单个文件操作出错");  
	           e.printStackTrace();  
	 
	       } 
	}

	public static void uploadPic(String src, String dst) {
		try {
			File file = new File(src);
			InputStream inputStream = new FileInputStream(file);
			OutputStream outputStream = new FileOutputStream(dst);
			try {
				inputStream = new BufferedInputStream(inputStream);
				outputStream = new BufferedOutputStream(outputStream);
				byte[] buffer = new byte[(int) file.length()];
				while (inputStream.read(buffer) > 0) {
					outputStream.write(buffer);
				}
			} finally {
				if (null != inputStream) {
					inputStream.close();
				}
				if (null != outputStream) {
					outputStream.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以行的方式进行文件的读取
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readFileByLines(String fileName) {
		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		if (!file.exists())
			return "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString + "\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 指定行数的文件读取，当行数为-1时，读取全部
	 * 
	 * @param fileName
	 * @param readNum
	 *            指定行数
	 * @return
	 */
	public static String readFileByLines(String fileName, int readNum) {
		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		if (!file.exists())
			return "";
		BufferedReader reader = null;
		try {
			int num = 0;
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				num++;
				sb.append(tempString + "\r\n");
				if (num == readNum) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 删除指定目录下的所有文件
	 * 
	 * @param path
	 * @return
	 * @author zhaofeng
	 * @version 1.0 </pre> Created on :2012-6-25 下午3:36:53 LastModified:
	 *          History: </pre>
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 * @author zhaofeng
	 * @version 1.0 </pre> Created on :2012-6-25 下午3:40:11 LastModified:
	 *          History: </pre>
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据文件名称，获取文件类型
	 * 
	 * @param fileName
	 * @return
	 */
	public static String findFileType(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}
	
	/**
	 * 验证是否存在相同文件
	 * @param filePath 文件路径
	 * @param fileName 验证文件名 
	 * @return 文件名存在 true , otherwhise false;
	 */
	public static boolean validSwfFileName(String filePath,String fileName){
		File testPath = new File(filePath);
		File[] fileArr = testPath.listFiles();
		boolean returnFlag = false;
		for(int i = 0 ; i < fileArr.length ;i++){
			File tempFile = fileArr[i];
			//有相同文件
			if(tempFile.getName().equals(fileName)){
				returnFlag = true;
				break;
			}
		}
		return returnFlag;
	}

	/**
	 * 判断该路径是否存在，不存在创建
	 * @param filePath
	 */
	public static void createFolder(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
}