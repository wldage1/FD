package com.sw.plugins.product.manage.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.HoldingProduct;
import com.sw.plugins.market.order.service.ClearedProductService;
import com.sw.plugins.market.order.service.OrderHoldingProductService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.ProductAttachment;
import com.sw.plugins.product.manage.entity.ProductAttributeValue;
import com.sw.plugins.product.manage.entity.SubProduct;
import com.sw.plugins.product.promotion.entity.ProductPositionMapping;
import com.sw.plugins.product.promotion.service.ProductPositionMappingService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

public class ProductService extends CommonService<Product> {

	@Resource
	private ProductAttributeValueService productAttributeValueService;
	@Resource
	private ProductAttachmentService productAttachmentService;
	@Resource
	private SubProductService subProductService;
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private OrderHoldingProductService holdingProductService;
	@Resource
	private ClearedProductService clearedProductService;
	@Resource
	private ProductPositionMappingService positionMappingService;

	/**
	 * 获取产品分类信息json
	 * 
	 * @author baokai Created on :2013-2-28 上午11:03:26
	 */
	@Override
	public Map<String, Object> getGrid(Product entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Product> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 查询产品附带评价集合
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGridComment(Product entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Product> resultList = getPaginatedListComment(entity);
		Long record = getRecordCountComment(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<Product> getPaginatedListComment(Product entity) throws Exception {
		return (List<Product>) getRelationDao().selectList("product.select_paginated_comment", entity);
	}

	public Long getRecordCountComment(Product entity) throws Exception {
		return (Long) getRelationDao().getCount("product.count_comment", entity);
	}

	/**
	 * 获得完整的产品信息，包括属性、属性值
	 * 
	 * @param product
	 * @return
	 * @throws Exception
	 */
	public Product getFullProduct(Product product) throws Exception {
		product = (Product) this.getOne(product);
		// product.setFoundTime(product.getCreateTime());
		ProductAttributeValue productAttributeValue = new ProductAttributeValue();
		productAttributeValue.setProductId(product.getId());
		// 产品属性列表
		List<ProductAttributeValue> productAttributeList = (List<ProductAttributeValue>) productAttributeValueService.getList(productAttributeValue);
		ProductAttachment productAttachment = new ProductAttachment();
		productAttachment.setProductId(product.getId());
		// 产品附件列表
		List<ProductAttachment> proAttachmentList = (List<ProductAttachment>) productAttachmentService.getList(productAttachment);
		StringBuffer strValue = new StringBuffer();
		StringBuffer fileNames = new StringBuffer();
		for (int i = 0; i < proAttachmentList.size(); i++) {
			if (proAttachmentList.get(i) != null) {
				// nosqlid
				String idInNoSql = proAttachmentList.get(i).getiDInNoSql() == null ? "" : proAttachmentList.get(i).getiDInNoSql();
				// 文件名
				String fileName = proAttachmentList.get(i).getFileName() == null ? "" : proAttachmentList.get(i).getFileName();
				if (i == 0) {
					strValue.append(idInNoSql);
					fileNames.append(fileName);
				} else {
					strValue.append(",").append(idInNoSql);
					fileNames.append(",").append(fileName);
				}
			}
		}
		// product.setIdInNoSql(strValue.toString());
		// product.setFileName(fileNames.toString());
		// 属性值字符串，属性间用'*'分隔
		StringBuffer defaultValue = new StringBuffer();
		for (int m = 0; m < productAttributeList.size(); m++) {
			if (productAttributeList.get(m) != null) {
				ProductAttributeValue pav = productAttributeList.get(m);
				String attrId = pav.getAttributeId() == null ? "" : pav.getAttributeId().toString();
				String value = pav.getValue() == null ? "" : pav.getValue();
				if (m == 0) {
					defaultValue.append(attrId);
					defaultValue.append(":");
					defaultValue.append(value);
				} else {
					defaultValue.append("*");
					defaultValue.append(attrId);
					defaultValue.append(":");
					defaultValue.append(value);
				}
			}
		}
		product.setAttributeValue(defaultValue.toString());
		return product;
	}

	public Map<String, Object> getCategoryGrid(Product entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Product> productList = getProductCategoryPaginatedList(entity);
		map.put("rows", productList);
		map.put("page", 1);
		map.put("total", 1);
		map.put("records", 1);
		return map;
	}

	/**
	 * 保存或更新产品
	 * 
	 * @author baokai Created on :2013-3-5 上午11:22:22
	 */
	public Long saveOrUpdate(Product entity) throws Exception {
		entity.setDescription(HtmlUtils.htmlEscape(entity.getDescription()));
		entity.setProfile(HtmlUtils.htmlEscape(entity.getProfile()));
		Long productId;
		if (entity.getId() == null) {
			save(entity);
			productId = entity.getGeneratedKey();
			entity.setId(productId);
		} else {
			productId = entity.getId();
			update(entity);
		}
		if (productId != null) {
			// 保存产品属性信息
			List<ProductAttributeValue> productAttributeValueList = entity.getProductAttributeValueList();
			if (productAttributeValueList != null) {
				// 删除产品关联属性值
				ProductAttributeValue pa = new ProductAttributeValue();
				pa.setProductId(productId);
				productAttributeValueService.delete(pa);
				for (ProductAttributeValue tpa : productAttributeValueList) {
					Long attributeId = tpa.getAttributeId();
					String value = tpa.getValue();
					if (attributeId == null || value == null || ("").equals(value)) {
						continue;
					} else {
						ProductAttributeValue fpa = new ProductAttributeValue();
						fpa.setProductId(productId);
						fpa.setAttributeId(attributeId);
						fpa.setValue(value);
						productAttributeValueService.save(fpa);
					}
				}
			}
		}
		return productId;
	}

	/**
	 * 保存信托产品
	 * 
	 * @param product
	 * @throws Exception
	 */
	public void saveProductTrust(Product product) throws Exception {
		getRelationDao().insert("product.insert_trust", product);
	}

	/**
	 * 保存阳光私募产品
	 * 
	 * @param product
	 * @throws Exception
	 */
	public void saveProductPrivate(Product product) throws Exception {
		getRelationDao().insert("product.insert_private", product);
	}

	/**
	 * 更新信托产品
	 * 
	 * @param product
	 * @throws Exception
	 */
	public void updateProductTrust(Product product) throws Exception {
		getRelationDao().update("product.update_trust", product);
	}

	/**
	 * 更新阳光私募产品
	 * 
	 * @param product
	 * @throws Exception
	 */
	public void updateProductPrivate(Product product) throws Exception {
		getRelationDao().update("product.update_private", product);
	}

	/**
	 * 统计信托产品
	 * 
	 * @param product
	 * @return
	 * @throws Exception
	 */
	public Long countProductTrust(Product product) throws Exception {
		return (Long) getRelationDao().getCount("product.count_trust", product);
	}

	/**
	 * 统计阳光私募产品
	 * 
	 * @param product
	 * @throws Exception
	 */
	public Long countProductPrivate(Product product) throws Exception {
		return (Long) getRelationDao().getCount("product.count_private", product);
	}

	@Override
	public String upload(Product entity, HttpServletRequest request) throws Exception {
		MultipartFile file = ((MultipartHttpServletRequest) request).getFile("Filedata");
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(date);
		String path = SystemProperty.getInstance("config").getProperty("product_picture_path") + dateStr;
		String tempFileName = request.getParameter("Filename");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));
		String newFileName = FTPUtil.getRandomString();
		String realFileName = newFileName + fileExtensionName;
		return FTPUtil.uploadFile(file, path, realFileName);
	}

	@Override
	public void init(InitData initData) throws Exception {

	}

	/**
	 * 产品保存方法
	 */
	@Override
	public void save(Product entity) throws Exception {
		getRelationDao().insert("product.insert", entity);
	}

	public long getProductByAttribute(Product entity) throws Exception {
		return (Long) getRelationDao().getCount("product.select_product_by_attr", entity);
	}

	@Override
	public Long getRecordCount(Product entity) throws Exception {
		return (Long) getRelationDao().getCount("product.count", entity);
	}

	public Long getRecordCountFromAllProduct(Product entity) throws Exception {
		long obj = (Long) getRelationDao().getCount("product.count_from_all_product", entity);
		return obj;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Product> getPaginatedList(Product entity) throws Exception {
		return (List<Product>) getRelationDao().selectList("product.select_paginated", entity);
	}

	@SuppressWarnings("unchecked")
	public List<Product> getProductCategoryPaginatedList(Product entity) throws Exception {
		return (List<Product>) getRelationDao().selectList("product.select_byCategory_paginated", entity);
	}

	/**
	 * 递归删除
	 */
	@Override
	public void deleteByArr(Product entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}

	/**
	 * 删除产品
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void deleteProduct(Product product) throws Exception {
		ProductAttributeValue productAttributeValue = new ProductAttributeValue();
		productAttributeValue.setProductId(product.getId());
		ProductAttachment productAttachment = new ProductAttachment();
		productAttachment.setProductId(product.getId());
		productAttributeValueService.delete(productAttributeValue);
		productAttachmentService.delete(productAttachment);
		delete(product);
	}

	@Override
	public Object download(Product entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void update(Product entity) throws Exception {
		entity.setAccountInfo(HtmlUtils.htmlEscape(entity.getAccountInfo()));
		getRelationDao().update("product.update", entity);
	}
	
	public void updateIsSpecial(Product entity) throws Exception{
		super.getRelationDao().update("product.update_isSpecial", entity);
	}
	
	/**
	 * 开启、关闭产品功能
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void updateByStatus(Product entity) throws Exception {
		Short released = entity.getReleased();
		// 清除位置推荐
		if(released != null && released == (short)0){
			ProductPositionMapping pm = new ProductPositionMapping();
			pm.setProductId(entity.getId());
			positionMappingService.deleteByProductId(pm);
		}
		getRelationDao().update("product.update_by_status", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getList(Product entity) throws Exception {
		return (List<Product>) getRelationDao().selectList("product.select", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getUploadProductList(Product entity) throws Exception{
		return (List<Product>) getRelationDao().selectList("product.selectUpload", entity);
	}
	
	@Override
	public Product getOne(Product entity) throws Exception {
		return (Product) getRelationDao().selectOne("product.getOne_byModify", entity);
	}

	public Long checkCode(Product entity) throws Exception {
		return (Long) getRelationDao().getCount("product.checkCategoryId", entity);
	}

	@Override
	public void delete(Product entity) throws Exception {
		getRelationDao().delete("product.delete", entity);
	}

	/**
	 * 修改产品销售状态
	 * 
	 * @param product
	 * @throws Exception
	 */
	public void updateSellStatus(Product product) throws Exception {
		Product product2 = new Product();
		product2.setId(product.getId());
		product2.setSellStatus(product.getSellStatus());
		this.update(product2);
	}

	/**
	 * 订单处所用
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGridTwo(Product entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Product> resultList = (List<Product>) super.getRelationDao().selectList("product.select_paginated_byorder", entity);
		for (Product product : resultList) {
			SubProduct subProduct = new SubProduct();
			subProduct.setProductId(product.getId());
			List<SubProduct> subList = subProductService.getSubProductList(subProduct);
			product.setSubProductList(subList);
		}
		Long record = super.getRelationDao().getCount("product.select_count_byorder", entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	public Product getProductNameById(Product product) throws Exception {
		return (Product) super.getRelationDao().selectOne("product.select_product_name", product);
	}

	/**
	 * 获取存续产品列表数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCunxuProductGrid(Product entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		User user = userLoginService.getCurrLoginUser();
		Integer type = user.getType();
		if (type.equals(1)) {
			Organization organization = user.getSelfOrg();
			entity.setOrgId(organization.getId());
		}
		List<Product> resultList = new ArrayList<Product>();
		Long record = (long) super.getRelationDao().getCount("product.count_cunxu_product_paginated", entity);
		if (record > 0) {
			resultList = (List<Product>) super.getRelationDao().selectList("product.select_cunxu_product_paginated", entity);
		}
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 获取清算产品列表数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getClearedProductGrid(Product entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		User user = userLoginService.getCurrLoginUser();
		Integer type = user.getType();
		if (type.equals(1)) {
			Organization organization = user.getSelfOrg();
			entity.setOrgId(organization.getId());
		}

		List<Product> resultList = new ArrayList<Product>();
		Long record = (long) super.getRelationDao().getCount("product.count_cleared_product_paginated", entity);
		if (record > 0) {
			resultList = (List<Product>) super.getRelationDao().selectList("product.select_cleared_product_paginated", entity);
		}
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	public void saveProductAttachment(Product product) throws Exception {
		Long productId = product.getId();
		List<ProductAttachment> productAttachmentList = product.getProductAttachmentList();
		if (productAttachmentList != null) {
			for (ProductAttachment prat : productAttachmentList) {
				Long id = prat.getId();
				if (id == null) {
					String fileName = prat.getFileName();
					if (fileName == null || "".equals(fileName)) {
						continue;
					}
					// 预约文件路径的设置
					prat.setProductId(productId);
					// 删除实体文件
					prat.setAudited((short) 1);
					prat.setFileUrl("test");
					productAttachmentService.save(prat);
				} else {
					productAttachmentService.update(prat);
				}
			}
		}
	}

	/* ------------------------ 销售管控相关方法 -------------------------- */

	/**
	 * 获取销售中产品列表数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSalingProductGrid(Product entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Product> resultList = getSalingProductPaginatedList(entity);
		Long record = countSalingProductPaginatedList(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 获取销售中的产品
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Product> getSalingProductList(Product entity) throws Exception {
		return (List<Product>) getRelationDao().selectList("product.select_saling_product", entity);
	}

	/**
	 * 获取销售中的产品
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Product> getSalingProductPaginatedList(Product entity) throws Exception {
		return (List<Product>) getRelationDao().selectList("product.select_saling_product_paginated", entity);
	}

	/**
	 * 获取销售中的产品统计量
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Long countSalingProductPaginatedList(Product entity) throws Exception {
		return (long) getRelationDao().getCount("product.count_saling_product_paginated", entity);
	}

	/*----------------------------------------// 销售管控相关方法  --------------------------------------------- */
	@SuppressWarnings("unchecked")
	public List<Product> getSallingProduct(Product entity) throws Exception {
		return (List<Product>) getRelationDao().selectList("product.select_sellstatus", entity);
	}

	/**
	 * 清算产品
	 * 
	 * @param product
	 * @throws Exception
	 */
	public void saveClearProduct(Product product) throws Exception {
		Long productId = product.getId();
		if (productId != null) {
			// 转移存续产品信息到清算表
			HoldingProduct holdingProduct = new HoldingProduct();
			holdingProduct.setProductID(productId);
			List<HoldingProduct> holdingProductList = holdingProductService.getHoldingProductByProductId(holdingProduct);
			if (holdingProductList != null) {
				for (HoldingProduct hp : holdingProductList) {
					// 保存清算产品
					clearedProductService.save(hp);
					// 清除存续产品
					holdingProductService.deleteByProductId(hp);
				}
			}
			// 设置产品销售状态
			product.setSellStatus((short) 7);
			this.updateSellStatus(product);
		}
	}

	/**
	 * 查询 认购类型的产品
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Product> getOfferPayList(Product product) throws Exception {
		return (List<Product>) super.getRelationDao().selectList("product.select_offerPay_list", product);
	}

	/**
	 * 查询申购类型的产品
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Product> getSubPayList(Product product) throws Exception {
		return (List<Product>) super.getRelationDao().selectList("product.select_subPay_list", product);
	}

	/**
	 * 核算产品列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Product> getCommissionProduct(Product entity) throws Exception {
		return (List<Product>) super.getRelationDao().selectList("product.select_commission_product", entity);
	}
}
