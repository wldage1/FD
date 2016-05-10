package com.sw.plugins.product.manage.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.commission.contract.entity.Contract;
import com.sw.plugins.commission.contract.service.ContractService;
import com.sw.plugins.config.dictionary.entity.DictionaryItem;
import com.sw.plugins.config.dictionary.service.DictionaryItemService;
import com.sw.plugins.cooperate.organization.entity.Commission;
import com.sw.plugins.cooperate.provider.entity.Provider;
import com.sw.plugins.cooperate.provider.service.ProviderService;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.organization.service.MemberOrganizationService;
import com.sw.plugins.product.attribute.entity.ProductAttribute;
import com.sw.plugins.product.attribute.service.ProductAttributeService;
import com.sw.plugins.product.capitalconfig.entity.ProductCategory;
import com.sw.plugins.product.capitalconfig.service.ProductCategoryService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.ProductApplication;
import com.sw.plugins.product.manage.entity.ProductAttachment;
import com.sw.plugins.product.manage.entity.ProductAttributeValue;
import com.sw.plugins.product.manage.entity.ProductInformation;
import com.sw.plugins.product.manage.entity.ProductOrgMapping;
import com.sw.plugins.product.manage.entity.ProductSeckill;
import com.sw.plugins.product.manage.entity.SubProduct;
import com.sw.plugins.product.manage.service.ProductApplicationService;
import com.sw.plugins.product.manage.service.ProductAttachmentService;
import com.sw.plugins.product.manage.service.ProductAttributeValueService;
import com.sw.plugins.product.manage.service.ProductInformationService;
import com.sw.plugins.product.manage.service.ProductOrgMappingService;
import com.sw.plugins.product.manage.service.ProductSeckillService;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.plugins.storage.entity.GridFile;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.plugins.websitemanage.information.entity.Information;
import com.sw.plugins.websitemanage.information.service.InformationService;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

@Controller
public class ProductController extends BaseController<Product> {

	private static Logger logger = Logger.getLogger(ProductController.class);
	@Resource
	private ProductService service;
	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private ProductAttributeService productAttributeService;
	@Resource
	private ProductAttributeValueService productAttributeValueService;
	@Resource
	private ProductAttachmentService productAttachmentService;
	@Resource
	private ProductApplicationService productApplicationService;
	@Resource
	private OrganizationService organizationService;
	@Resource
	private ProductSeckillService productSeckillService;
	@Resource
	private DictionaryItemService dictionaryItemService;
	@Resource
	private InformationService informationService;
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private ContractService contractService;
	@Resource
	private ProviderService providerService;
	@Resource
	private MemberOrganizationService memOrgService;
	@Resource
	private ProductOrgMappingService proMappingService;

	/**
	 * 查询产品
	 * 
	 * 
	 */
	@RequestMapping("/product/manage")
	public CommonModelAndView list(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		// 产品类型
		commonModelAndView.addObject("productTypeMap", this.initData.getProductType());
		// 审核状态
		commonModelAndView.addObject("productStatusMap", this.initData.getProductStatus());
		// 销售状态
		commonModelAndView.addObject("sellStatusMap", this.initData.getSellStatus());
		// 发布状态
		commonModelAndView.addObject("releasedMap", this.initData.getReleased());
		// 前台预览地址
		commonModelAndView.addObject("previewUrl", SystemProperty.getInstance("config").getProperty("websiteDomainAddress") + "/preview/product/detail");

		// 判断用户是否为居间公司
		User user = userLoginService.getCurrLoginUser();
		Organization organization = user.getSelfOrg();
		Integer userType = user.getType();
		if (organization != null) {
			int isCommission = organization.getIsCommission();
			if (isCommission == 1) {
				commonModelAndView.addObject("isCommission", 1);
			} else {
				commonModelAndView.addObject("isCommission", 0);
				// 居间公司
				List<Commission> list = new ArrayList<Commission>();
				try {
					list = organizationService.getListCommission();
				} catch (Exception e) {
					e.printStackTrace();
				}
				commonModelAndView.addObject("commissionList", list);
			}
		}
		commonModelAndView.addObject("product", product);
		commonModelAndView.addObject("userType", userType);
		commonModelAndView.addObject("c", product.getC());
		return commonModelAndView;
	}

	/**
	 * 产品审核列表页面，返回json
	 * 
	 * @author liushuai Created on :2013-8-8 下午1:30:59
	 */
	@RequestMapping("/product/manage/grid")
	public CommonModelAndView json(Product product, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Integer type = user.getType();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					product.setOrgId(organization.getId());
				} else {
					product.setOrgId(null);
				}
			}
			String position = user.getPosition();
			if (type == 0 || (position != null && position.contains(",1,"))) {
				product.setIsPoManager((short)1);
			}
			map = service.getGrid(product);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, product, request);
	}

	/**
	 * 设置产品居间公司
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/updateCommissionOrgID")
	public CommonModelAndView updateCommissionOrgID(Product product, HttpServletRequest request) {
		String view = "";
		if (product != null && product.getId() != null && product.getOrgId() != null) {
			try {
				service.update(product);
				view = "success";
			} catch (Exception e) {
				view = "error";
				logger.error(DetailException.expDetail(e, getClass()));
			}
		} else {
			view = "error";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zt", view);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 修改上架下架功能
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/updateAttachment")
	public CommonModelAndView updateAttachment(ProductAttachment product, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String view = "";
		try {
			productAttachmentService.updateAttachment(product);
			view = "success";
			map.put("status", "success");
		} catch (Exception e) {
			view = "error";
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		map.put("zt", view);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 订单查询产品，返回json
	 * 
	 * @author liushuai Created on :2013-8-8 下午1:30:59
	 */
	@RequestMapping("/product/manage/gridTwo")
	public CommonModelAndView jsonTwo(Product product, HttpServletRequest request) {
		Map<String, Object> map = null;
		if (product != null && product.getShortName() != null && !product.getShortName().equals("")) {
			try {
				User user = userLoginService.getCurrLoginUser();
				Organization organization = user.getSelfOrg();
				if (organization != null) {
					int isCommission = organization.getIsCommission();
					if (isCommission == 1) {
						product.setOrgId(organization.getId());
					} else {
						product.setOrgId(null);
					}
				}
				map = service.getGridTwo(product);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 进入审批页面
	 * 
	 * @param productApplication
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/examine")
	public CommonModelAndView examine(Product product, HttpServletRequest request) {
		Product product2 = product;
		try {
			product = service.getOne(product);
			product.setC(product2.getC());
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		commonModelAndView.addObject("product", product);
		return commonModelAndView;
	}

	/**
	 * 产品审核功能
	 * 
	 * @param productApplication
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/saveProductApplication")
	public CommonModelAndView examineratify(ProductApplication productApplication, HttpServletRequest request) {
		String view = "";
		try {
			productApplicationService.saveAndUpdateProduct(productApplication);
			view = this.SUCCESS;
		} catch (Exception e) {
			view = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(view, productApplication, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 改变状态为开启
	 * 
	 * @param product
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/product/manage/release.json")
	public CommonModelAndView release(Product product, BindingResult result, HttpServletRequest request, Map<String, Object> model) {
		String viewName = "";
		try {
			service.updateByStatus(product);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, product, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 改变状态为关闭
	 * 
	 * @param product
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/product/manage/notrelease.json")
	public CommonModelAndView notrelease(Product product, BindingResult result, HttpServletRequest request, Map<String, Object> model) {
		String viewName = "";
		try {
			service.updateByStatus(product);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, product, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 删除产品
	 * 
	 * @author baokai Created on :2013-1-30 下午3:18:26
	 */
	@RequestMapping("/product/manage/delete")
	public CommonModelAndView delete(Product product, HttpServletRequest request) {
		String viewName = "";
		if (product.getIds() != null) {
			for (String id : product.getIds()) {
				Product p = new Product();
				p.setId(Long.valueOf(id));
				try {
					service.deleteProduct(p);
					viewName = this.SUCCESS;
				} catch (Exception e) {
					viewName = this.ERROR;
					logger.error(e.getMessage());
				}
			}
		} else if (product.getId() != null) {
			try {
				service.deleteProduct(product);
				viewName = this.SUCCESS;
			} catch (Exception e) {
				viewName = this.ERROR;
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}

		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, product, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 跳转到创建页面
	 * 
	 * @author baokai Created on :2013-1-22 下午1:43:51
	 * @throws Exception
	 */
	@RequestMapping("/product/manage/create")
	public CommonModelAndView create(Product product, BindingResult result, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);

		String c = product.getC();
		List<ProductCategory> investCategoryList = null;
		List<Provider> providerList = null;
		Integer isCommission = null;
		Long selfOrgId = null;
		try {
			// 供应商
			Provider pr = new Provider();
			pr.setDelStatus(1);
			pr.setStatus(1);
			providerList = providerService.getList(pr);
			// 资金投向
			ProductCategory pt = new ProductCategory();
			pt.setTreeLevel(2);
			investCategoryList = productCategoryService.getList(pt);
			// 如果是居间公司用户加载居间公司ID
			User user = userLoginService.getCurrLoginUser();
			if (user != null) {
				Organization selfOrg = user.getSelfOrg();
				isCommission = selfOrg.getIsCommission();
				selfOrgId = selfOrg.getId();
			}
		} catch (Exception e) {
			logger.error(e);
		}
		commonModelAndView.addObject("product", product);
		commonModelAndView.addObject("providerList", providerList);
		commonModelAndView.addObject("productType", initData.getProductType());
		commonModelAndView.addObject("investCategoryList", investCategoryList);
		commonModelAndView.addObject("closeType", initData.getCloseType());
		commonModelAndView.addObject("raiseArea", initData.getRaiseArea());
		commonModelAndView.addObject("isCommission", isCommission);
		commonModelAndView.addObject("selfOrgId", selfOrgId);
		commonModelAndView.addObject("c", c);
		commonModelAndView.addObject("ftpUrl", SystemProperty.getInstance("config").getProperty("ftp.http.url"));
		return commonModelAndView;
	}

	/**
	 * 跳转到修改页面
	 * 
	 * @author baokai Created on :2013-1-22 下午1:43:51
	 */
	@RequestMapping("/product/manage/modify")
	public CommonModelAndView modify(Product product, HttpServletRequest request, Map<String, Object> model) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);

		String c = product.getC();
		List<Commission> commissionList = null;
		List<ProductCategory> investCategoryList = null;
		try {
			product = (Product) service.getOne(product);
			if (product != null) {

				// 资金投向
				ProductCategory pt = new ProductCategory();
				pt.setTreeLevel(2);
				investCategoryList = productCategoryService.getList(pt);
				// 居间公司
				commissionList = organizationService.getListCommission();
			}

		} catch (Exception e) {
			logger.error(e);
		}
		if (product == null) {
			product = new Product();
		}
		model.put("product", product);
		commonModelAndView.addAllObjects(model);
		commonModelAndView.addObject("investCategoryList", investCategoryList);
		commonModelAndView.addObject("commissionList", commissionList);
		commonModelAndView.addObject("productType", initData.getProductType());
		commonModelAndView.addObject("raiseArea", initData.getRaiseArea());
		commonModelAndView.addObject("c", c);
		commonModelAndView.addObject("ftpUrl", SystemProperty.getInstance("config").getProperty("ftp.http.url"));
		return commonModelAndView;
	}

	/**
	 * 跳转到修改页面
	 * 
	 * @author baokai Created on :2013-1-22 下午1:43:51
	 */
	@RequestMapping("/product/manage/modifyall")
	public CommonModelAndView modifyAll(Product product, HttpServletRequest request, Map<String, Object> model) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);

		String c = product.getC();
		List<Commission> commissionList = null;
		List<ProductCategory> investCategoryList = null;
		List<Provider> providerList = null;
		try {
			product = (Product) service.getOne(product);
			if (product != null) {
				// 供应商
				Provider pr = new Provider();
				pr.setDelStatus(1);
				pr.setStatus(1);
				providerList = providerService.getList(pr);
				// 资金投向
				ProductCategory pt = new ProductCategory();
				pt.setTreeLevel(2);
				investCategoryList = productCategoryService.getList(pt);
				// 居间公司
				commissionList = organizationService.getListCommission();
			}

		} catch (Exception e) {
			logger.error(e);
		}
		if (product == null) {
			product = new Product();
		}
		model.put("product", product);
		commonModelAndView.addAllObjects(model);
		commonModelAndView.addObject("providerList", providerList);
		commonModelAndView.addObject("investCategoryList", investCategoryList);
		commonModelAndView.addObject("commissionList", commissionList);
		commonModelAndView.addObject("productType", initData.getProductType());
		commonModelAndView.addObject("closeType", initData.getCloseType());
		commonModelAndView.addObject("raiseAreaList", initData.getRaiseArea());
		commonModelAndView.addObject("c", c);
		commonModelAndView.addObject("ftpUrl", SystemProperty.getInstance("config").getProperty("ftp.http.url"));
		return commonModelAndView;
	}

	/**
	 * 跳转到详细页面
	 * 
	 * @author liushuai Created on :2013-8-8 下午1:43:51
	 */
	@RequestMapping("/product/manage/detail")
	public CommonModelAndView detail(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		String c = product.getC();
		Product product2 = null;
		try {
			// 获取产品基本属性
			product2 = service.getOne(product);
			product2.setC(c);
			// 获取产品动态属性
			ProductAttributeValue productAttributeValue = new ProductAttributeValue();
			productAttributeValue.setProductId(product2.getId());
			List<ProductAttributeValue> productAttributeList = (List<ProductAttributeValue>) productAttributeValueService.getList(productAttributeValue);
			// 产品类型
			commonModelAndView.addObject("productTypeMap", this.initData.getProductType());
			// 居间公司
			if (product2.getOrgId() != null) {
				Organization organization = new Organization();
				organization.setId(product2.getOrgId());
				organization = organizationService.getOne(organization);
				commonModelAndView.addObject("organization", organization);
			}
			// 机构类型
			if (product2.getOrgTypeId() != null) {
				Provider provider = new Provider();
				provider.setId(product2.getProviderId());
				provider = providerService.getOne(provider);
				DictionaryItem dictionaryItem = new DictionaryItem();
				dictionaryItem.setDictionaryCode("orgType");
				dictionaryItem.setItemValue(provider.getCompanyType().toString());
				dictionaryItem = dictionaryItemService.getOne(dictionaryItem);
				commonModelAndView.addObject("dictionaryItem", dictionaryItem);
			}
			// 供应商
			if (product2.getProviderId() != null) {
				Provider provider = new Provider();
				provider.setId(product2.getProviderId());
				provider = providerService.getOne(provider);
				commonModelAndView.addObject("provider", provider);
			}
			// 期限类型
			commonModelAndView.addObject("deadlineTypeMap", this.initData.getDeadlineType());
			// 销售类型
			commonModelAndView.addObject("sellTypeMap", this.initData.getSellType());
			// 产品状态
			commonModelAndView.addObject("statusMap", this.initData.getProductStatus());
			// 产品销售状态
			commonModelAndView.addObject("sellStatusMap", this.initData.getSellStatus());
			// 产品发布状态
			commonModelAndView.addObject("releasedMap", this.initData.getReleased());
			// 预期收益率类型
			commonModelAndView.addObject("profitTypeMap", this.initData.getProfitType());
			// 募集方所在区域
			commonModelAndView.addObject("raiseArea", initData.getRaiseArea());
			// 查询资金投向表数据
			if (product2.getInvestCategoryId() != null) {
				ProductCategory productCategory = new ProductCategory();
				productCategory.setId(product2.getInvestCategoryId());
				productCategory = productCategoryService.getOne(productCategory);
				commonModelAndView.addObject("productCategory", productCategory);
			}
			// 封闭期类型
			commonModelAndView.addObject("closeTypeMap", this.initData.getCloseType());

			commonModelAndView.addObject("product", product2);
			commonModelAndView.addObject("productAttributeList", productAttributeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commonModelAndView;
	}

	/**
	 * 获取下拉列表树信息
	 * 
	 * @author baokai Created on :2013-1-22 下午2:17:31
	 */
	@RequestMapping("/product/manage/stree")
	public CommonModelAndView stree(String id, HttpServletRequest request) {
		ProductCategory productCategory = new ProductCategory();
		Map<String, Object> map = null;
		if (id != null) {
			if (id.indexOf(",") > 0) {
				id = id.substring(id.lastIndexOf(",") + 1);
			}
			productCategory.setParentId(Long.valueOf(id));
		}
		try {
			map = productCategoryService.getSelectTree(productCategory);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	@RequestMapping("/product/manage/productType")
	public CommonModelAndView productType(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("/product/manage/productType");
		commonModelAndView.addObject("product", product);
		// commonModelAndView.addObject("c", product.getC());
		return commonModelAndView;
	}

	@RequestMapping("/product/manage/productcategory.json")
	public CommonModelAndView getProductByCategoryId(Product product, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = service.getCategoryGrid(product);
			if (map == null) {
				map = new HashMap<String, Object>();
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}

		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 取某一产品类型下的属性组
	 * 
	 * @author baokai Created on :2013-1-31 下午1:22:20
	 * @throws Exception
	 */
	@RequestMapping("/product/manage/getAttributeList")
	public CommonModelAndView getAttributeJson(Product product, HttpServletRequest request) throws Exception {
		ProductAttribute pa = new ProductAttribute();
		Map<String, Object> map = (Map<String, Object>) productAttributeService.getMap(pa);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 取某一产品类型下的属性组
	 * 
	 * @author baokai Created on :2013-1-31 下午1:22:20
	 */
	@RequestMapping("/product/manage/getAttributeListValue.json")
	public CommonModelAndView getAttributeValueJson(Product product, HttpServletRequest request) {
		ProductAttribute pa = new ProductAttribute();
		pa.setInvestCategoryId(product.getInvestCategoryId());
		Long productId = product.getId();
		if (productId != null) {
			pa.setProductId(product.getId().toString());
		}
		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>) productAttributeService.getMap(pa);
			// List<ProductAttribute> paList = (List<ProductAttribute>)
			// productAttributeService.getList(pa);
			// map.put("attributeValueList", paList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 保存产品
	 * 
	 * @author baokai Created on :2013-3-19 上午10:29:05
	 */
	@RequestMapping(value = "/product/manage/save", method = RequestMethod.POST)
	public CommonModelAndView save(Product product, Map<String, Object> model, HttpServletRequest request) {
		String viewName = this.ERROR;
		Map<String, Object> map = new HashMap<String, Object>(1);
		try {
			Long productId = service.saveOrUpdate(product);
			map.put("productId", productId);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(e);
		}
		CommonModelAndView mv = new CommonModelAndView(viewName, product, request, messageSource);
		mv.addAllObjects(map);
		return mv;
	}

	/**
	 * 资讯绑定功能列表
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/bindinformation")
	public CommonModelAndView bindMessage(Information information, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, information, messageSource);
		commonModelAndView.addObject("information", information);
		return commonModelAndView;
	}

	@RequestMapping("/product/manage/bindorganization")
	public CommonModelAndView bindOrganization(ProductOrgMapping productOrgMapping, HttpServletRequest request) {
		List<MemberOrganization> orgList = new ArrayList<MemberOrganization>();
		List<Long> orgIds = new ArrayList<Long>();
		Product product = new Product();
		product.setId(productOrgMapping.getProductID());
		try {
			orgList = memOrgService.getAllMemberOrganization(new MemberOrganization());
			orgIds = proMappingService.getListOrg(productOrgMapping);
			product = service.getProductNameById(product);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, productOrgMapping, messageSource);
		commonModelAndView.addObject("orgList", orgList);
		commonModelAndView.addObject("orgIds", orgIds);
		commonModelAndView.addObject("productID", productOrgMapping.getProductID());
		commonModelAndView.addObject("c", productOrgMapping.getC());
		commonModelAndView.addObject("name", product.getName());
		return commonModelAndView;
	}

	/**
	 * 传机构串
	 * 
	 * @param productOrgMapping
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/bindorganization/save")
	public CommonModelAndView saveOrgPro(ProductOrgMapping productOrgMapping, HttpServletRequest request) {
		String view = "";
		try {
			proMappingService.saveMapping(productOrgMapping);
			view = this.SUCCESS;
		} catch (Exception e) {
			view = this.ERROR;
			logger.error(e.getMessage());
		}
		return new CommonModelAndView(view, productOrgMapping, request, messageSource);
	}

	/**
	 * 资讯绑定功能列表 json
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/product/manage/bindinformationGrid")
	public CommonModelAndView bindMessageGrid(Information information, HttpServletRequest request) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			gridJson = productInformationService.getGrid(information);
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, information, request);
	}

	@Resource
	private ProductInformationService productInformationService;

	/**
	 * 资讯绑定实现
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/bind")
	public CommonModelAndView bindMessageRealize(ProductInformation productInformation, HttpServletRequest request) {
		String view = "";
		if (productInformation != null && productInformation.getId() != null && productInformation.getInfromatinIds() != null) {
			try {
				productInformationService.saveInformation(productInformation);
				view = "success";
			} catch (Exception e) {
				view = "error";
				logger.error(DetailException.expDetail(e, getClass()));
			}
		} else {
			view = "error";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zt", view);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 资讯取消绑定实现
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/unbind")
	public CommonModelAndView unBindMessageRealize(ProductInformation productInformation, HttpServletRequest request) {
		String view = "";
		if (productInformation != null && productInformation.getId() != null && productInformation.getInfromatinIds() != null) {
			try {
				productInformationService.deleteInformation(productInformation);
				view = "success";
			} catch (Exception e) {
				view = "error";
				logger.error(DetailException.expDetail(e, getClass()));
			}
		} else {
			view = "error";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zt", view);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 初始化 产品秒杀
	 * 
	 * @param productInformation
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/recommend")
	public CommonModelAndView recommend(Product product, HttpServletRequest request, Map<String, Object> model) {
		String code = product.getC();
		ProductSeckill secKill = null;
		try {
			Long productId = product.getId();
			if (productId != null) {
				Product pt = new Product();
				pt.setId(productId);
				product = service.getProductNameById(product);
				if (product == null) {
					product = new Product();
				}
				ProductSeckill psecKill = new ProductSeckill();
				psecKill.setProductId(productId);
				secKill = productSeckillService.getOne(psecKill);
				if (secKill == null) {
					secKill = new ProductSeckill();
				}
			}
			if (code != null) {
				product.setC(code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		model.put("productSecKill", secKill);
		commonModelAndView.addAllObjects(model);
		commonModelAndView.addObject("product", product);
		return commonModelAndView;
	}

	/**
	 * 保存秒杀信息
	 * 
	 * @param productSeckill
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/saveProductSeckill")
	public CommonModelAndView saveProductSeckill(ProductSeckill productSeckill, HttpServletRequest request) {
		String view = "";
		try {
			productSeckillService.saveorupdate(productSeckill);
			view = this.SUCCESS;
		} catch (Exception e) {
			view = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView(view, productSeckill, request, messageSource);
	}

	/**
	 * 产品销售状态变更
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/updatestatus")
	public CommonModelAndView updateStatus(Product product, HttpServletRequest request) {
		String view = "";
		if (product != null && product.getId() != null && product.getSellStatus() != null) {
			try {
				service.updateSellStatus(product);
				view = "success";
			} catch (Exception e) {
				view = "error";
				logger.error(DetailException.expDetail(e, getClass()));
			}
		} else {
			view = "error";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zt", view);
		return new CommonModelAndView(view, map);
	}

	@RequestMapping("/product/manage/uploadfile")
	@Override
	public String uploadfile(Product entity, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(service.upload(entity, request));
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	@Override
	public String downloadfile(Product entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Product entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/product/manage/valid")
	public CommonModelAndView valid(@Valid Product entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		long l;
		try {
			l = (Long) service.checkCode(entity);
			if (l > 0)
				result.rejectValue("code", "check");
			if (result.hasErrors()) {
				commonModelAndView.addBindingErrors(model, result, messageSource);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return commonModelAndView;
	}

	@RequestMapping("/product/manage/subproductvalid")
	public CommonModelAndView valid(@Valid SubProduct entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try {
			if (result.hasErrors()) {
				commonModelAndView.addBindingErrors(model, result, messageSource);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return commonModelAndView;
	}

	/**
	 * 存续产品页面
	 * 
	 * @param product
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/product/cxproduct")
	public CommonModelAndView cxProductList(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		// 产品类型
		commonModelAndView.addObject("productTypeMap", this.initData.getProductType());
		commonModelAndView.addObject("product", product);
		commonModelAndView.addObject("c", product.getC());
		return commonModelAndView;
	}

	/**
	 * 存续产品列表数据
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/cxproduct/grid")
	public CommonModelAndView cxProductJSON(Product product, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = service.getCunxuProductGrid(product);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, product, request);
	}

	/**
	 * 清算产品页面
	 * 
	 * @param product
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/product/clearedproduct")
	public CommonModelAndView clearedProductList(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		// 产品类型
		commonModelAndView.addObject("productTypeMap", this.initData.getProductType());
		commonModelAndView.addObject("product", product);
		commonModelAndView.addObject("c", product.getC());
		return commonModelAndView;
	}

	/**
	 * 清算产品列表数据
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/clearedproduct/grid")
	public CommonModelAndView cleardProductJSON(Product product, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = service.getClearedProductGrid(product);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, product, request);
	}

	/**
	 * 产品清算
	 * 
	 * @param product
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/product/manage/clearproduct.json")
	public CommonModelAndView clearProduct(Product product, BindingResult result, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			service.saveClearProduct(product);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, product, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 产品资料维护页面
	 * 
	 * @param product
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/product/manage/attachment")
	public CommonModelAndView productAttachmentList(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		commonModelAndView.addObject("product", product);
		commonModelAndView.addObject("downloadPermission", initData.getDownloadPermission());
		String ftpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		String docPath = SystemProperty.getInstance("config").getProperty("product_document_path");
		commonModelAndView.addObject("ftpUrl", ftpUrl);
		commonModelAndView.addObject("docPath", docPath);
		commonModelAndView.addObject("c", product.getC());
		return commonModelAndView;
	}

	@RequestMapping("/product/manage/attachment/save")
	public CommonModelAndView saveProductAttachment(ProductAttachment productAttachment, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(this.STATUS, STATUS_FALSE);
		try {
			productAttachmentService.update(productAttachment);
			map.put("attachmentId", productAttachment.getId());
			map.put(this.STATUS, STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map);
	}

	@RequestMapping("/product/manage/attachment/delete")
	public CommonModelAndView delProductAttachment(ProductAttachment pa, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(this.STATUS, STATUS_FALSE);
		try {
			productAttachmentService.delete(pa);
			map.put(this.STATUS, STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 产品资料维护列表数据
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/attachment/grid")
	public CommonModelAndView productAttachmentJSON(ProductAttachment productAttachment, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = productAttachmentService.getProductAttachmentGrid(productAttachment);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, productAttachment, request);
	}

	@RequestMapping("/product/manage/attachment/uploadfile")
	public String attachmentUploadfile(ProductAttachment productAttachment, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(productAttachmentService.upload(productAttachment, request));
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	/**
	 * 创建资讯
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/manage/bindinformation/create")
	public CommonModelAndView createInformation(Information information, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, information, messageSource);
		commonModelAndView.addObject("information", information);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl", ftpHttpUrl);
		return commonModelAndView;
	}

	@RequestMapping("/product/manage/bindinformation/detail")
	public CommonModelAndView detailInformation(Information information, HttpServletRequest request) {
		String code = information.getC();
		try {
			information = informationService.getOne(information);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		information.setC(code);
		information.setContent(HtmlUtils.htmlUnescape(information.getContent()));
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, information, messageSource);
		List<Map<String, Object>> informationType = initData.getInformationType();
		commonModelAndView.addObject("informationType", informationType);
		commonModelAndView.addObject("information", information);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl", ftpHttpUrl);
		return commonModelAndView;
	}

	/**
	 * 保存并关联资讯
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/product/manage/bindinformation/save", method = RequestMethod.POST)
	public CommonModelAndView save(Information information, HttpServletRequest request) {
		String viewName = null;
		try {
			informationService.saveAndRelate(information);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			viewName = this.ERROR;
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, information, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 存续产品跳转到详细页面
	 * 
	 * @author liushuai Created on :2013-8-8 下午1:43:51
	 */
	@RequestMapping("/product/cxproduct/cxdetail")
	public CommonModelAndView cxdetail(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		String c = product.getC();
		Product product2 = null;
		try {
			// 获取产品基本属性
			product2 = service.getOne(product);
			product2.setC(c);
			// 获取产品动态属性
			ProductAttributeValue productAttributeValue = new ProductAttributeValue();
			productAttributeValue.setProductId(product2.getId());
			List<ProductAttributeValue> productAttributeList = (List<ProductAttributeValue>) productAttributeValueService.getList(productAttributeValue);
			// 产品类型
			commonModelAndView.addObject("productTypeMap", this.initData.getProductType());
			// 居间公司
			if (product2.getOrgId() != null) {
				Organization organization = new Organization();
				organization.setId(product2.getOrgId());
				organization = organizationService.getOne(organization);
				commonModelAndView.addObject("organization", organization);
			}
			// 机构类型
			if (product2.getOrgTypeId() != null) {
				Provider provider = new Provider();
				provider.setId(product2.getProviderId());
				provider = providerService.getOne(provider);
				DictionaryItem dictionaryItem = new DictionaryItem();
				dictionaryItem.setDictionaryCode("orgType");
				dictionaryItem.setItemValue(provider.getCompanyType().toString());
				dictionaryItem = dictionaryItemService.getOne(dictionaryItem);
				commonModelAndView.addObject("dictionaryItem", dictionaryItem);
			}
			// 供应商
			if (product2.getProviderId() != null) {
				Provider provider = new Provider();
				provider.setId(product2.getProviderId());
				provider = providerService.getOne(provider);
				commonModelAndView.addObject("provider", provider);
			}
			// 期限类型
			commonModelAndView.addObject("deadlineTypeMap", this.initData.getDeadlineType());
			// 销售类型
			commonModelAndView.addObject("sellTypeMap", this.initData.getSellType());
			// 产品状态
			commonModelAndView.addObject("statusMap", this.initData.getProductStatus());
			// 产品销售状态
			commonModelAndView.addObject("sellStatusMap", this.initData.getSellStatus());
			// 产品发布状态
			commonModelAndView.addObject("releasedMap", this.initData.getReleased());
			// 预期收益率类型
			commonModelAndView.addObject("profitTypeMap", this.initData.getProfitType());
			// 查询资金投向表数据
			if (product2.getInvestCategoryId() != null) {
				ProductCategory productCategory = new ProductCategory();
				productCategory.setId(product2.getInvestCategoryId());
				productCategory = productCategoryService.getOne(productCategory);
				commonModelAndView.addObject("productCategory", productCategory);
			}
			// 封闭期类型
			commonModelAndView.addObject("closeTypeMap", this.initData.getCloseType());

			commonModelAndView.addObject("product", product2);
			commonModelAndView.addObject("productAttributeList", productAttributeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commonModelAndView;
	}

	/**
	 * 清算产品跳转到详细页面
	 * 
	 */
	@RequestMapping("/product/clearedproduct/cleareddetail")
	public CommonModelAndView clearedetail(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		String c = product.getC();
		Product product2 = null;
		try {
			// 获取产品基本属性
			product2 = service.getOne(product);
			product2.setC(c);
			// 获取产品动态属性
			ProductAttributeValue productAttributeValue = new ProductAttributeValue();
			productAttributeValue.setProductId(product2.getId());
			List<ProductAttributeValue> productAttributeList = (List<ProductAttributeValue>) productAttributeValueService.getList(productAttributeValue);
			// 产品类型
			commonModelAndView.addObject("productTypeMap", this.initData.getProductType());
			// 居间公司
			if (product2.getOrgId() != null) {
				Organization organization = new Organization();
				organization.setId(product2.getOrgId());
				organization = organizationService.getOne(organization);
				commonModelAndView.addObject("organization", organization);
			}
			// 机构类型
			if (product2.getOrgTypeId() != null) {
				Provider provider = new Provider();
				provider.setId(product2.getProviderId());
				provider = providerService.getOne(provider);
				DictionaryItem dictionaryItem = new DictionaryItem();
				dictionaryItem.setDictionaryCode("orgType");
				dictionaryItem.setItemValue(provider.getCompanyType().toString());
				dictionaryItem = dictionaryItemService.getOne(dictionaryItem);
				commonModelAndView.addObject("dictionaryItem", dictionaryItem);
			}
			// 供应商
			if (product2.getProviderId() != null) {
				Provider provider = new Provider();
				provider.setId(product2.getProviderId());
				provider = providerService.getOne(provider);
				commonModelAndView.addObject("provider", provider);
			}
			// 期限类型
			commonModelAndView.addObject("deadlineTypeMap", this.initData.getDeadlineType());
			// 销售类型
			commonModelAndView.addObject("sellTypeMap", this.initData.getSellType());
			// 产品状态
			commonModelAndView.addObject("statusMap", this.initData.getProductStatus());
			// 产品销售状态
			commonModelAndView.addObject("sellStatusMap", this.initData.getSellStatus());
			// 产品发布状态
			commonModelAndView.addObject("releasedMap", this.initData.getReleased());
			// 预期收益率类型
			commonModelAndView.addObject("profitTypeMap", this.initData.getProfitType());
			// 查询资金投向表数据
			if (product2.getInvestCategoryId() != null) {
				ProductCategory productCategory = new ProductCategory();
				productCategory.setId(product2.getInvestCategoryId());
				productCategory = productCategoryService.getOne(productCategory);
				commonModelAndView.addObject("productCategory", productCategory);
			}
			// 封闭期类型
			commonModelAndView.addObject("closeTypeMap", this.initData.getCloseType());

			commonModelAndView.addObject("product", product2);
			commonModelAndView.addObject("productAttributeList", productAttributeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commonModelAndView;
	}

	/**
	 * 富文本编辑器上传图片
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @return
	 * @author sean
	 * @version 1.0 </pre> Created on :2013-9-25 下午6:55:20 LastModified:
	 *          History: </pre>
	 * @throws IOException
	 */
	@RequestMapping(value = "/product/manage/kindeditor/uploadimg")
	public String kindEditorUpload(GridFile entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		String tempFileName = request.getParameter("localUrl");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", ".gif,.jpg,.jpeg,.png,.bmp");
		if (!Arrays.<String> asList(extMap.get("image").split(",")).contains(fileExtensionName)) {
			response.getWriter().println("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get("image") + "格式。");
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String date = format.format(new Date());
		String path = SystemProperty.getInstance("config").getProperty("product_picture_path") + date;
		// 重置文件名
		String newFileName = FTPUtil.getRandomString();
		String realFileName = newFileName + fileExtensionName;
		// FTP上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("imgFile");
		try {
			FTPUtil.uploadFile(file, path, realFileName);
			response.getWriter().print("{\"error\":0,\"url\":\"" + SystemProperty.getInstance("config").getProperty("ftp.http.url") + path + "/" + realFileName + "\"}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/product/manage/downcheck")
	public CommonModelAndView downcheck(ProductAttachment productAttachment, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, productAttachment, messageSource);
		commonModelAndView.addObject("productAttachment", productAttachment);
		return commonModelAndView;
	}

	/**
	 * 下架审核操作
	 * 
	 * @param role
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping("/product/manage/downchecksave")
	public CommonModelAndView downchecksave(ProductAttachment entity, HttpServletRequest request) {
		String status;
		try {
			productAttachmentService.updateAttachment(entity);
			status = "success";
		} catch (Exception e) {
			status = "error";
			;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 验证居间公司是否有居间协议
	 * 
	 * @param contract
	 * @return
	 */
	@RequestMapping("/product/manage/checkContract")
	public CommonModelAndView checkContract(Contract contract) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> mapTemp = contractService.checkContractList(contract);
			map.put("jsonObject", mapTemp);
			map.put("status", "success");
		} catch (Exception e) {
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 验证 反馈信息长度
	 * 
	 * @param entity
	 * @param result
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/product/manage/validExamine")
	public CommonModelAndView validExamine(@Valid ProductApplication entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try {
			if (result.hasErrors()) {
				commonModelAndView.addBindingErrors(model, result, messageSource);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return commonModelAndView;
	}

	@RequestMapping("/product/manage/create_valid")
	public CommonModelAndView createValid(@Valid Product product, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		long l;
		try {
			if ("".equals(product.getShortName())) {
				result.rejectValue("shortName", "NotEmpty");
			} else if (null == product.getType()) {
				result.rejectValue("type", "NotNull");
			} else if (null == product.getCloseType()) {
				result.rejectValue("closeType", "NotNull");
			} else if (null == product.getOrgId()) {
				result.rejectValue("orgId", "NotNull");
			} else if (null == product.getProviderId()) {
				result.rejectValue("providerId", "NotNull");
			} else if (null == product.getOrgTypeId()) {
				result.rejectValue("orgTypeId", "NotNull");
			} else if ("".equals(product.getRaiseStartTime())) {
				result.rejectValue("raiseStartTime", "NotEmpty");
			} else if ("".equals(product.getRaiseFinishTime())) {
				result.rejectValue("raiseFinishTime", "NotEmpty");
			} else if (null == product.getMinTotalShare()) {
				result.rejectValue("minTotalShare", "NotNull");
			} else if (null == product.getMaxTotalShare()) {
				result.rejectValue("maxTotalShare", "NotNull");
			} else if (2 == product.getProfitType() && product.getMinProfitRatio() == null) {
				result.rejectValue("minProfitRatio", "NotNull");
			} else if (2 == product.getProfitType() && product.getMaxProfitRatio() == null) {
				result.rejectValue("maxProfitRatio", "NotNull");
			} else if (1 == product.getDeadlineType() && product.getMinDeadline() == null) {
				result.rejectValue("minDeadline", "NotNull");
			} else if (1 == product.getDeadlineType() && product.getMaxDeadline() == null) {
				result.rejectValue("maxDeadline", "NotNull");
			} else {
				l = (Long) service.checkCode(product);
				if (l > 0) {
					result.rejectValue("code", "check");
				}
			}
			if (result.hasErrors()) {
				commonModelAndView.addBindingErrors(model, result, messageSource);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return commonModelAndView;
	}
}
