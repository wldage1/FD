package com.sw.plugins.storage.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.sw.core.common.Constant;
import com.sw.core.initialize.InitData;
import com.sw.plugins.storage.entity.GridFile;
import com.sw.util.CommonUtil;
import com.sw.util.FileUtil;

public class StorageFs {

	private final GridFS gridFs;

	public StorageFs(MongoDbFactory mongoDbFactory) {
		gridFs = new GridFS(mongoDbFactory.getDb());
	}

	public String upload(GridFile entity, HttpServletRequest request) throws Exception {
		MultipartFile file = entity.getFile();
		GridFSInputFile input = gridFs.createFile(file.getInputStream(), file.getOriginalFilename(), true);
		input.setContentType(file.getContentType());
		input.save();
		return input.getId().toString();
	}

	/**
	 * 保存本地文件到mongodb中
	 * 
	 * @param filePath
	 *            文件完整路径
	 * @return
	 * @throws Exception
	 */
	public String upload(String filePath) throws Exception {
		File file = new File(filePath);
		GridFSInputFile input = gridFs.createFile(file);
		input.setContentType(filePath.substring(filePath.lastIndexOf(".")));
		input.save();
		return input.getId().toString();
	}

	/**
	 * 上传文件，将文件保存至本地临时目录
	 * 
	 * @param mfile
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(MultipartFile mfile, MultipartHttpServletRequest request, InitData initData) throws Exception {
		String fileName = CommonUtil.getUUID() + mfile.getOriginalFilename().substring(mfile.getOriginalFilename().lastIndexOf("."));
		StringBuffer filePath = new StringBuffer();

		String fp = request.getParameter("fp") == null ? "" : (String) request.getParameter("fp");

		// 上传文件，如果上传路径为空，则文件保存至临时目录
		if (fp.length() > 0) {
			filePath.append(Constant.APPLICATIONPATH).append(Constant.DIRSPLITER).append(initData.getUploadDir()).append(Constant.DIRSPLITER).append(fp).append(Constant.DIRSPLITER);
		} else {
			filePath.append(Constant.APPLICATIONPATH).append(Constant.DIRSPLITER).append(initData.getUploadDir()).append(Constant.DIRSPLITER).append(initData.getUploadTmpDir()).append(Constant.DIRSPLITER);
		}

		File file = new File(filePath.toString() + fileName);
		File parent = file.getParentFile();
		if (parent != null) {
			parent.mkdirs();
		}
		FileOutputStream fs = new FileOutputStream(file);

		InputStream in = mfile.getInputStream();
		byte[] buffer = new byte[1024 * 1024];
		int byteread = 0;
		while ((byteread = in.read(buffer)) != -1) {
			fs.write(buffer, 0, byteread);
			fs.flush();
		}
		fs.close();
		in.close();
		return fileName;
	}

	public GridFSDBFile download(GridFile entity, HttpServletRequest request) throws Exception {
		return gridFs.findOne(new ObjectId(entity.getId().toString()));
	}

	public void delete(GridFile entity) throws Exception {
		gridFs.remove(new ObjectId(entity.getId().toString()));
	}

	public GridFSDBFile findOne(GridFile entity) throws Exception {
		return gridFs.findOne(new ObjectId(entity.getId().toString()));
	}

	public String uploadFromInputStream(GridFile entity, InputStream in) throws Exception {
		GridFSInputFile input = gridFs.createFile(in);
		// input.setContentType(file.getContentType());
		input.save();
		return input.getId().toString();
	}

	/**
	 * 根据文件相对路径和文件名称删除文件，只能删除initData.getUploadDir()下文件 如果文件相对路径为空，则删除临时文件夹中文件
	 * 
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	public void deleteFile(String filePath, String fileName, InitData initData) throws Exception {
		StringBuffer realPath = new StringBuffer();
		if (filePath == null || "".equals(filePath)) {
			realPath.append(Constant.APPLICATIONPATH).append(Constant.DIRSPLITER).append(initData.getUploadDir()).append(Constant.DIRSPLITER).append(initData.getUploadTmpDir()).append(Constant.DIRSPLITER);
		} else {
			realPath.append(Constant.APPLICATIONPATH).append(Constant.DIRSPLITER).append(initData.getUploadDir()).append(Constant.DIRSPLITER).append(filePath).append(Constant.DIRSPLITER);
		}
		FileUtil.deleteFile(realPath.toString() + fileName);
	}

	public File getFile(String filePath, String fileName, InitData initData) throws Exception {
		StringBuffer realPath = new StringBuffer();
		if (filePath == null || "".equals(filePath)) {
			realPath.append(Constant.APPLICATIONPATH).append(Constant.DIRSPLITER).append(initData.getUploadDir()).append(Constant.DIRSPLITER).append(initData.getUploadTmpDir()).append(Constant.DIRSPLITER);
		} else {
			realPath.append(Constant.APPLICATIONPATH).append(Constant.DIRSPLITER).append(initData.getUploadDir()).append(Constant.DIRSPLITER).append(filePath).append(Constant.DIRSPLITER);
		}
		return new File(realPath.toString() + fileName);
	}
}
