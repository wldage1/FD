package com.sw.util;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
/**
 * 关于文件转化的Util类，其中包含word、txt、ppt、xls转化为pdf
 * 和pdf转化为swf
 * 先后通过 openoffice 和  swftools 完成
 * openoffice： 需要安装 并启动服务，同时有相应jar支持
 * swftools：      需要安装
 * 
 * @author wangchengsheng
 * @date   2011-10-13
 */
public class FileConvert {
	/**
	 * pdf  to  swf
	 * @param inputFile 待转pdf文件完整路径
	 * @param outputFile 转化完swf文件完整路径
	 */
	@SuppressWarnings("finally")
	public static boolean PdfToSwf(File inputFile,File outputFile,String swfToolsPath){
		 boolean rtnFlag = false;
		 Process p = null;
		 InputStreamReader inputStr = null;
		 BufferedReader br = null;
		 String windowsStr = "";
		 //eg: String command= "C:/Program Files/SWFTools/pdf2swf.exe  -t \""+inputFile+" -o  \"D:/convert/after/test2.swf" -s flashversion=9 ";
		 String command= swfToolsPath+" -t "+inputFile+" -o "+outputFile+" -s flashversion=9 ";
		 System.out.println(inputFile.getName()+"开始进行swf转换 ");
		 try {
			 /**
			  * Runtime 进程
			  * 需要判断当天服务器系统
			  * */
//			 if(!OSUtil.isLinux){
//				 windowsStr = "cmd /c ";
//			 }
			 System.out.println(windowsStr+command);
			 p = Runtime.getRuntime().exec(windowsStr+command);
			 inputStr = new InputStreamReader(p.getInputStream()); 
			 br = new BufferedReader(inputStr); 
			 String temp = ""; 
			 while(br.readLine()!= null){ 
				 temp = br.readLine();
			 } 
			 rtnFlag = true;
			 System.out.println(inputFile.getName()+"完成swf转换");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			rtnFlag = false;
		}finally{
			try {//关闭流
				inputStr.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			p.destroy();
			return rtnFlag;
		}
	}
	/**
	 * inputFile 待转化文档文件完整路径
	 * outputFile 转化完成文件完整路径
	 * pdfJarPath 转换pdf的路径
	 * transforTypeFlag 通过哪一种方式进行转换  1:openoffice  2:libreoffice
	 * @param inputFile
	 * @param outputFile
	 */
	@SuppressWarnings("finally")
	public static boolean docToPdf(File inputFile, File outputFile,String pdfJarPath,String transforTypeFlag){
		 System.out.println(inputFile.getName()+"开始进行pdf转换");
		 boolean rtnFlag = false;
		 Process p = null;
		 InputStreamReader inputStr = null;
		 BufferedReader br = null;
		 //通过openoffice的方式转换
		 if(transforTypeFlag != null && transforTypeFlag.trim().equals("1")){
			    //连接 8100 端口..
			    OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
			    try{
			    	connection.connect();
				    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
				    converter.convert(inputFile, outputFile);
				    rtnFlag = true;
			    }catch(Exception cex){
			    	cex.printStackTrace();
			    	rtnFlag = false;
			    }finally{
				    // 关闭连接
			    	if(connection!=null){
			    		connection.disconnect();
			    		connection = null;
			    	}
			    }
		 //通过libreoffice的方式转换
		 }else if(transforTypeFlag != null && transforTypeFlag.trim().equals("2")){
			    try{
				 	 String windowsStr = "";
					 //java -jar lib/jodconverter-cli-2.2.0.jar -f pdf *.doc
					 String command= "java -jar "+pdfJarPath+" "+inputFile+" "+outputFile;
					 System.out.println(command);
					 p = Runtime.getRuntime().exec(windowsStr+command);
					 inputStr = new InputStreamReader(p.getInputStream()); 
					 br = new BufferedReader(inputStr); 
					 String temp = ""; 
					 while(br.readLine()!= null){ 
						 temp = br.readLine();
					 } 
					 rtnFlag = true;
			    }catch(Exception cex){
			    	cex.printStackTrace();
			    	rtnFlag = false;
			    }finally{
				    // 关闭连接
		    		try {
						inputStr.close();
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			    }
		 }else{
			 rtnFlag = false;
		 }
		 return rtnFlag;
	}
	/**
	 * 以下为测试 方法和类
	 * @author wangchengsheng
	 *
	 */
	class TestThread extends java.lang.Thread{
		public File inputFile;
		public File outputFile;
		public File outputToSwfFile;
		public void run(){
			//文档转化为pdf
			FileConvert.docToPdf(inputFile, outputFile,"","");
			//pdf 转化为swf
			//t.PdfToSwf(outputFile);
			System.out.println(outputFile.getName()+"文件已生成");
		}
	}
	public void test(){
		TestThread t1 = new TestThread();
		t1.inputFile = new File("D:/init.txt");
		t1.outputFile = new File("D:/test3.pdf");
		//t1.outputToSwfFile = new File("D:/convert/after/test2.swf");
		t1.start();
	}
	
	public static void main(String[] args) throws Exception{
		FileConvert p = new FileConvert();
		p.test();
	  }
}
