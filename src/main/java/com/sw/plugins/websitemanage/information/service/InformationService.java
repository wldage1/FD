package com.sw.plugins.websitemanage.information.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.websitemanage.information.entity.Information;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.ProductInformation;
import com.sw.plugins.product.manage.service.ProductInformationService;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

/**
 * 资讯中心-Service实现类
 * 
 * @author runchao
 */
public class InformationService extends CommonService<Information>{

	@Resource
	private ProductService productService;
	@Resource
	private ProductInformationService productInformationService;
	@Resource
	private UserLoginService userLoginService;
	
	/**
	 * 获取类别列表集合
	 * 
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getTypeGrid(Information information) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Map<String, Object>> resultList = this.getInitData().getInformationType();
		map.put("rows", resultList);
		return map;
	}
	
	/**
	 * 获取资讯列表集合
	 * 
	 * @param information
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getGrid(Information information) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Information> resultList = getPaginatedList(information);
		Long record = getRecordCount(information);
		int pageCount = (int)Math.ceil(record/(double)information.getRows());
		map.put("rows", resultList);
		map.put("page", information.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 资讯分页查询
	 * 
	 * @param information
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Information> getPaginatedList(Information information) throws Exception {
		List<Information> list = (List<Information>)getRelationDao().selectList("information.selectPagintedList", information);
		return list;
	}
	
	/**
	 * 资讯统计查询
	 * 
	 * @param information
	 * @return
	 * @throws Exception
	 */
	@Override
	public Long getRecordCount(Information information) throws Exception {
		return getRelationDao().getCount("information.count", information);
	}
	
	/**
	 * 新增资讯
	 * 
	 * @param information
	 * @throws
	 */
	@Override
	public void save(Information information) throws Exception {
		information = changeContent(information);
		User user = userLoginService.getCurrLoginUser();
		information.setOrgId(user.getOrgId());
		getRelationDao().insert("information.insert", information);
		information.setId(information.getGeneratedKey());
		information.setUrl("/html/" + information.getType() + "/" + information.getId() + ".html");
		getRelationDao().update("information.update", information);
	}
	
	/**
	 * 产品中心创建并关联资讯
	 * 
	 * @param information
	 * @throws Exception
	 */
	public void saveAndRelate(Information information) throws Exception {
		information = changeContent(information);
		User user = userLoginService.getCurrLoginUser();
		information.setOrgId(user.getOrgId());
		getRelationDao().insert("information.insert", information);
		information.setId(information.getGeneratedKey());
		information.setUrl("/html/" + information.getType() + "/" + information.getId() + ".html");
		getRelationDao().update("information.update", information);
		ProductInformation productInformation = new ProductInformation();
		productInformation.setProductID(information.getProductId());
		information = (Information) getRelationDao().selectOne("information.selectOneByTitle", information);
		productInformation.setInformationID(information.getId());
		productInformationService.save(productInformation);
		
	}

	/**
	 * 修改资讯
	 * 
	 * @param information
	 * @throws
	 */
	@Override
	public void update(Information information) throws Exception {
		information = changeContent(information);
		getRelationDao().update("information.update", information);
		if(information.getType()!=8){
			ProductInformation productInformation = new ProductInformation();
			productInformation.setInformationID(information.getId());
			productInformationService.deleteByInformationId(productInformation);
		}
	}
	
	/**
	 * 资讯内容处理（抓图，转码）
	 * 
	 * @param information
	 * @return
	 * @throws Exception 
	 */
	public Information changeContent(Information information) throws Exception{
		String content = information.getContent();
		//抓图上传FTP路径
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String date = format.format(new Date());
		String path = SystemProperty.getInstance("config").getProperty("information.image.path") + date;
		//获取网络图片相关参数
		String imageUrl;
		URL url;
		HttpURLConnection conn;
		InputStream inStream;
		String fileName;
		String ftpImageUrl;
		//正则匹配img中src
		String img = "";
		Pattern p_image;
		Matcher m_image;
		String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
		p_image = Pattern.compile(regEx_img,Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(content);
		while(m_image.find()){
			img = img + "," + m_image.group();
			Matcher m  = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src
			while(m.find()){
				imageUrl = m.group(1);
				url = new URL(imageUrl);  
	            conn = (HttpURLConnection)url.openConnection();  
	            conn.setRequestMethod("GET");  
	            conn.setConnectTimeout(5 * 1000);
	            inStream = conn.getInputStream();//通过输入流获取图片数据
				fileName = FTPUtil.getRandomString() + imageUrl.substring(imageUrl.lastIndexOf("."));
				ftpImageUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url") + FTPUtil.uploadFile(inStream, path, fileName);
				content = content.replace(imageUrl, ftpImageUrl);
			}
		}
		information.setContent(HtmlUtils.htmlEscape(content));
		return information;
	}

	/**
	 * 删除单个资讯
	 * 
	 * @param information
	 * @return
	 * @throws
	 */
	@Override
	public void delete(Information information) throws Exception {
		ProductInformation productInformation = new ProductInformation();
		productInformation.setInformationID(information.getId());
		productInformation.setProductID(information.getId());
		productInformationService.deleteByInformationId(productInformation);
		getRelationDao().delete("information.delete", information);
	}

	/**
	 * 删除多个资讯
	 * 
	 * @param information
	 * @return
	 * @throws
	 */
	@Override
	public void deleteByArr(Information information) throws Exception {
		if(information.getIds() != null){
			for(String id : information.getIds()){
				information.setId(Long.valueOf(id));
				delete(information);
			}
		}
	}

	/**
	 * 资讯单个查询
	 * 
	 * @param information
	 * @return
	 * @throws
	 */
	@Override
	public Information getOne(Information information) throws Exception {
		return (Information) getRelationDao().selectOne("information.selectOne", information);
	}

	/**
	 * 查询产品列表集合
	 * 
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getProductGrid(Information information) throws Exception{
		Product product = new Product();
		product.setType(information.getProductType());
		product.setShortName(information.getProductName());
		product.setRows(information.getRows());
		product.setPage(information.getPage());
		product.setStart(information.getStart());
		product.setOffset(information.getOffset());
		return productService.getGrid(product);
	}
	
	/**
	 * 通过资讯ID查询关联产品
	 * 
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public ProductInformation selectRelate(Information information) throws Exception{
		ProductInformation productInformation = new ProductInformation();
		productInformation.setInformationID(information.getId());
		return productInformationService.getOne(productInformation);
	}
	
	/**
	 * 设置关联产品
	 * 
	 * @param information
	 * @throws Exception
	 */
	public void setRelate(Information information) throws Exception{
		ProductInformation productInformation = new ProductInformation();
		productInformation.setInformationID(information.getId());
		if(information.getProductId() == null){
			productInformationService.deleteByInformationId(productInformation);
		}else{
			productInformation.setProductID(information.getProductId());
			productInformationService.update(productInformation);
		}
	}
	
	
	
	/**
	 * 上传资讯标题图片
	 */
	@Override
	public String upload(Information information, HttpServletRequest request) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String date = format.format(new Date());
		String path = SystemProperty.getInstance("config").getProperty("information.image.path") + date;
		//重置文件名
		String tempFileName = request.getParameter("Filename");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));
		String newFileName = FTPUtil.getRandomString();	
		String realFileName = newFileName + fileExtensionName;
		//FTP上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("fileName");
		return FTPUtil.uploadFile(file, path, realFileName);
	}
	
	public void deleteTitleImage(Information information) throws Exception{
		getRelationDao().update("information.deleteTitleImage", information);
	}
	
	/**
	 * 资讯标题同名验证
	 * 
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public Long checkForTitle(Information information) throws Exception{
		return getRelationDao().getCount("information.checkForTitle", information);
	}
	
	/**
	 * 产品关联资讯查询
	 * 
	 * @param information
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Information> selectRelateProduct(Information information) throws Exception {
		information.setType(8);
		return (List<Information>) getRelationDao().selectList("information.selectRelateProduct", information);
	}
	
	/**
	 * 产品关联资讯统计查询
	 * 
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public Long selectRelateProductCount(Information information) throws Exception {
		information.setType(8);
		return getRelationDao().getCount("information.selectRelateProductCount", information);
	}
	
	@Override
	public List<Information> getList(Information entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object download(Information entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
