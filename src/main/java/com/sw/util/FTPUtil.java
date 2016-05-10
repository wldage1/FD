package com.sw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import com.sw.core.common.Constant;

public class FTPUtil {

	/**
	 * 连接FTP
	 * 
	 * @param ip
	 * @param username
	 * @param password
	 * @param port
	 */
	public static FTPClient connctServer(String ip, String username, String password, int port, String clientEnCode) throws Exception {
		FTPClient ftp = new FTPClient();
		try {
			if (port == 0) {
				ftp.connect(ip);

			} else {
				ftp.connect(ip, port);
			}
		} catch (SocketException e) {
			// 当出现SocketException异常时，多为网络问题。如：连接超时或者网络不通
			return null;
		} catch (IOException e) {
			throw new Exception(e);
		}

		// 登录
		boolean isLogin = ftp.login(username, password);

		if (!isLogin) {
			return null;
		}

		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			return null;
		} else {
			ftp.setFileType(FTP.BINARY_FILE_TYPE);// 设置为二进制传输模式
			ftp.enterLocalPassiveMode();// 本地客户端需要主动连接活动的ftpServer
			ftp.setControlEncoding(clientEnCode);
			FTPClientConfig conf = new FTPClientConfig(getSystemKey(ftp.getSystemName()));
			conf.setServerLanguageCode("zh");
			ftp.configure(conf);
		}
		return ftp;
	}

	/**
	 * 测试是否连接成功
	 * 
	 * @param ip
	 * @param username
	 * @param password
	 * @param port
	 * @param folder
	 *            ftp目录
	 * @param ftpEnCode
	 *            ftp服务端编码
	 * @param clientEnCode
	 *            客户端服务器编码
	 * @return
	 * @throws Exception
	 */
	public static boolean isConnect(String ip, String username, String password, int port, String folder, String ftpEnCode, String clientEnCode) throws Exception {
		boolean b = true;
		FTPClient ftp = connctServer(ip, username, password, port, clientEnCode);
		if (ftp == null) {
			b = false;
		} else {
			if (folder != null && !"".equals(folder)) {
				// 将文件目录转换为ftp服务端编码
				folder = CharsetUtil.changeCharset(folder, clientEnCode, ftpEnCode);
				b = ftp.changeWorkingDirectory(folder);
			}
		}

		return b;
	}

	/**
	 * 查看文件夹文件列表
	 * 
	 * @param ip
	 * @param username
	 * @param password
	 * @param port
	 * @param clientEnCode
	 * @throws Exception
	 */
	public static List<Map<String, Object>> viewFiles(String ip, String username, String password, int port, String sourceCharset, String folder) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		FTPClient ftp = new FTPClient();
		try {
			ftp = connctServer(ip, username, password, port, sourceCharset);
			if (ftp != null) {
				if (folder != null && !"".equals(folder)) {
					ftp.changeWorkingDirectory(folder);
				}

				for (FTPFile ff : ftp.listFiles()) {
					if (ff.isFile()) {
						Map<String, Object> map = new Hashtable<String, Object>();
						map.put("name", ff.getName());
						map.put("size", ff.getSize());
						map.put("createTime", ff.getTimestamp().getTime());
						list.add(map);
					}
				}
			}
		} finally {
			try {
				if (ftp != null) {
					ftp.logout();
					ftp.disconnect();
				}
			} catch (Exception e) {
			}
		}

		return list;
	}

	/**
	 * 下载FTP文件
	 * 
	 * @param ip
	 * @param username
	 * @param password
	 * @param port
	 * @param clientEnCode
	 * @param folder
	 *            ftp文件目录
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<InputStream> downFile(String ip, String username, String password, int port, String clientEnCode, String folder, String fileName) throws Exception {
		List<InputStream> list = new ArrayList<InputStream>();
		FTPClient ftp = new FTPClient();
		try {
			ftp = connctServer(ip, username, password, port, clientEnCode);
			if (ftp != null) {
				if (folder != null && !"".equals(folder)) {
					ftp.changeWorkingDirectory(folder);
				}

				for (FTPFile ff : ftp.listFiles()) {
					if (ff.isFile() && ff.getName().equals(fileName)) {
						InputStream is = ftp.retrieveFileStream(fileName);
						list.add(is);
					}
				}

			}
		} finally {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (Exception e) {
			}
		}
		return list;
	}

	/**
	 * 下载FTP文件
	 * 
	 * @param ip
	 * @param username
	 * @param password
	 * @param port
	 * @param clientEnCode
	 * @param folder
	 *            ftp下载目录
	 * @param fileName
	 * @param localPath
	 *            本地保存路径
	 * @param localFileName
	 *            本地保存文件名
	 * @return
	 * @throws Exception
	 */
	public static boolean downFile(String ip, String username, String password, int port, String clientEnCode, String ftpEnCode, String folder, String fileName, String localPath, String localFileName) throws Exception {
		boolean b = false;
		FTPClient ftp = new FTPClient();
		try {
			ftp = connctServer(ip, username, password, port, clientEnCode);
			if (ftp != null) {
				if (folder != null && !"".equals(folder)) {
					// 将文件目录转换为ftp服务端编码
					folder = CharsetUtil.changeCharset(folder, clientEnCode, ftpEnCode);
					ftp.changeWorkingDirectory(folder);
				}

				File file = new File(localPath);
				if (file != null) {
					file.mkdirs();
				}

				file = new File(localPath + Constant.DIRSPLITER + localFileName);
				for (FTPFile ff : ftp.listFiles()) {
					if (ff.isFile() && ff.getName().equals(fileName)) {
						String ftpFileName = CharsetUtil.changeCharset(ff.getName(), clientEnCode, ftpEnCode);
						OutputStream is = new FileOutputStream(file);
						b = ftp.retrieveFile(ftpFileName, is);
						is.close();
					}
				}

			}
		} finally {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (Exception e) {
			}
		}
		return b;
	}

	public static String getSystemKey(String systemName) {
		String[] values = systemName.split(" ");
		if (values != null && values.length > 0) {
			return values[0];
		} else {
			return null;
		}

	}

	/**
	 * 获取随机字符串
	 * 
	 * @return
	 */
	public static String getRandomString() {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 建立FTPClient
	 * 
	 * @return
	 * @throws Exception
	 * @author sean
	 * @version 1.0 </pre> Created on :2013-9-3 下午8:15:03 LastModified: History:
	 *          </pre>
	 */
	private static FTPClient creatFTPClient() throws Exception {
		// 获取ftp参数
		String ip = SystemProperty.getInstance("config").getProperty("ftp.ip");
		int port = Integer.parseInt(SystemProperty.getInstance("config").getProperty("ftp.port"));
		String username = SystemProperty.getInstance("config").getProperty("ftp.username");
		String password = SystemProperty.getInstance("config").getProperty("ftp.password");
		// 获取FTPClient
		FTPClient ftpClient = connctServer(ip, username, password, port, "UTF-8");
		return ftpClient;
	}

	/**
	 * 上传文件到FTP服务器并返回FTP文件名与文件路径
	 * 
	 * @param file
	 * @param path
	 * @return
	 * @throws Exception
	 * @author sean
	 * @version 1.0 </pre> Created on :2013-9-3 下午8:17:22 LastModified: History:
	 *          </pre>
	 */
	public static String uploadFile(MultipartFile file, String path, String fileName) {
		FTPClient ftpClient;
		try {
			ftpClient = creatFTPClient();
			// 设置访问目录
			ftpCreateDirectoryTree(ftpClient, path);
			// FTP上传文件
			byte[] bytes = file.getBytes();
			if (bytes.length != 0) {
				ftpClient.storeFile(CharsetUtil.changeCharset(fileName, CharsetUtil.GBK, CharsetUtil.ISO_8859_1), file.getInputStream());
			}
			ftpClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pathAndName = "";
		String pathLastStr = path.substring(path.length() - 1, path.length());
		if (pathLastStr.equals("/")) {
			pathAndName = path + fileName;
		} else {
			pathAndName = path + "/" + fileName;
		}
		return pathAndName;
	}

	public static String uploadFile(File file, String path, String fileName) throws Exception {
		FTPClient ftpClient = creatFTPClient();
		// 设置访问目录
		ftpCreateDirectoryTree(ftpClient, path);
		// FTP上传文件
		// byte[] bytes = file.getBytes();
		if (file.length() != 0) {
			InputStream is = new FileInputStream(file);
			ftpClient.storeFile(fileName, is);
		}
		ftpClient.disconnect();
		String pathAndName = "";
		String pathLastStr = path.substring(path.length() - 1, path.length());
		if (pathLastStr.equals("/")) {
			pathAndName = path.substring(0, path.length() - 1) + fileName;
		} else {
			pathAndName = path + "/" + fileName;
		}
		return pathAndName;
	}

	public static String uploadFile(InputStream is, String path, String fileName) throws Exception {
		FTPClient ftpClient = creatFTPClient();
		// 设置访问目录
		ftpCreateDirectoryTree(ftpClient, path);
		// FTP上传文件
		ftpClient.storeFile(fileName, is);
		ftpClient.disconnect();
		String pathAndName = "";
		String pathLastStr = path.substring(path.length() - 1, path.length());
		if (pathLastStr.equals("/")) {
			pathAndName = path.substring(0, path.length() - 1) + fileName;
		} else {
			pathAndName = path + "/" + fileName;
		}
		return pathAndName;
	}
	
	public static void main(String[] args) {
		try {
			// List<InputStream> list =FTPUtil.findFiles("192.168.12.199", "c4",
			// "123456", 20000, "GBK", "/cuijiaochuli", "1.xls") ;
			// for(InputStream in : list) {
			//
			// }

			// List<Map<String, Object>> list1 =
			// FTPUtil.viewFiles("192.168.12.199", "c4", "123456", 20000,
			// CharsetUtil.GBK, CharsetUtil.changeCharset("/帐单分期",
			// CharsetUtil.GBK, CharsetUtil.ISO_8859_1)) ;
			// for(Map<String, Object> map : list1) {
			// System.out.println(map.get("name"));
			// }

			FTPUtil.downFile("192.168.1.44", "cfqresource", "_CFQ_resource_2013_", 21, CharsetUtil.GBK, CharsetUtil.ISO_8859_1, CharsetUtil.changeCharset("/resources/website/organization/contract/", CharsetUtil.GBK, CharsetUtil.ISO_8859_1), "理财顾问协议_20140606110850.docx", "d:/temp/download", "理财顾问协议_20140606110850.docx");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private static void ftpCreateDirectoryTree(FTPClient client, String dirTree) throws IOException {
		boolean dirExists = true;
		// tokenize the string and attempt to change into each directory level.
		// If you cannot, then start creating.
		String[] directories = dirTree.split("/");
		for (String dir : directories) {
			if (!dir.isEmpty()) {
				if (dirExists) {
					dirExists = client.changeWorkingDirectory(dir);
				}
				if (!dirExists) {
					if (!client.makeDirectory(dir)) {
						throw new IOException("Unable to create remote directory '" + dir + "'.  error='" + client.getReplyString() + "'");
					}
					if (!client.changeWorkingDirectory(dir)) {
						throw new IOException("Unable to change into newly created remote directory '" + dir + "'.  error='" + client.getReplyString() + "'");
					}
				}
			}
		}
	}
}
