package com.sw.plugins.extension.youku.videomanager.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.extension.youku.videomanager.entity.ProductVideoMapping;
import com.sw.plugins.extension.youku.videomanager.entity.VideoBasic;
import com.sw.util.FileUtil;
import com.sw.util.SystemProperty;

@Service
public class VideoBasicService extends CommonService<VideoBasic> {
	
	@Resource
	private ProductVideoMappingService productVideoMappingService;
	
	@Override
	public void save(VideoBasic entity) throws Exception {
		super.getRelationDao().insert("video.insert", entity);
		if(entity.getProductId()!=null){
			ProductVideoMapping pvm = new ProductVideoMapping();
			pvm.setProductId(entity.getProductId());
			pvm.setVideoBasicId(entity.getGeneratedKey());
			productVideoMappingService.save(pvm);
		}
	}

	@Override
	public void update(VideoBasic entity) throws Exception {
		super.getRelationDao().update("video.update", entity);
	}

	@Override
	public Long getRecordCount(VideoBasic entity) throws Exception {
		return super.getRelationDao().getCount("video.count", entity);
	}

	@Override
	public List<VideoBasic> getList(VideoBasic entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VideoBasic> getPaginatedList(VideoBasic entity) throws Exception {
		return (List<VideoBasic>) super.getRelationDao().selectPaginatedList("video.select", entity);
	}

	@Override
	public void delete(VideoBasic entity) throws Exception {
		ProductVideoMapping pvm = new ProductVideoMapping();
		pvm.setVideoBasicId(entity.getId());
		productVideoMappingService.delete(pvm);
		super.getRelationDao().delete("video.delete", entity);
	}

	@Override
	public void deleteByArr(VideoBasic entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public VideoBasic getOne(VideoBasic entity) throws Exception {
		return (VideoBasic) super.getRelationDao().selectOne("video.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(VideoBasic entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<VideoBasic> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	@Override
	public String upload(VideoBasic entity, HttpServletRequest request) throws Exception {
		String newfilename = "";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String realPath = request.getSession().getServletContext().getRealPath("");
		if (realPath == null) {
			realPath = request.getSession().getServletContext().getResource("/").toString();
		}
		String tempPath = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		// 存储路径
		String videoPath = SystemProperty.getInstance("parameterConfig").getProperty("videoPath");
		// 判断文件夹是否存在如果不存在就创建
		String filePath = realPath + File.separator + videoPath + File.separator + tempPath;
		FileUtil.createFolder(filePath);
		Iterator<String> iterator = multipartRequest.getFileNames();
		while (iterator.hasNext()) {
			String fileName = iterator.next().toString();
			// 文件全名
			List<MultipartFile> flist = multipartRequest.getFiles(fileName);
			for (MultipartFile mfile : flist) {
				byte[] bytes;
				try {
					bytes = mfile.getBytes();
					if (bytes.length != 0) {
						String filename = mfile.getOriginalFilename();
						mfile.transferTo(new File(filePath, filename));
						newfilename = filename;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return newfilename;
	}

	private String getNewFilename(String filename) {
		String ex = filename.substring(filename.lastIndexOf(".") + 1);
		StringBuffer bf = new StringBuffer();
		bf.append((new Date()).getTime());
		bf.append(".");
		bf.append(ex);
		return bf.toString();
	}

	@Override
	public Object download(VideoBasic entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

}
