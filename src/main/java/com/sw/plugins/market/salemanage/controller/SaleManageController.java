package com.sw.plugins.market.salemanage.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.data.dbholder.CreatorContextHolder;
import com.sw.core.exception.DetailException;
import com.sw.plugins.config.dictionary.entity.DictionaryItem;
import com.sw.plugins.config.dictionary.service.DictionaryItemService;
import com.sw.plugins.cooperate.provider.entity.Provider;
import com.sw.plugins.cooperate.provider.service.ProviderService;
import com.sw.plugins.market.salemanage.entity.SaleManage;
import com.sw.plugins.market.salemanage.entity.SaleManageMessage;
import com.sw.plugins.market.salemanage.entity.SaleManagePlot;
import com.sw.plugins.market.salemanage.service.SaleManageMessageService;
import com.sw.plugins.market.salemanage.service.SaleManageMessageTask;
import com.sw.plugins.market.salemanage.service.SaleManageService;
import com.sw.plugins.market.salemanage.service.SaleManageMessageTask.Methods;
import com.sw.plugins.product.capitalconfig.entity.ProductCategory;
import com.sw.plugins.product.capitalconfig.service.ProductCategoryService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.ProductAttributeValue;
import com.sw.plugins.product.manage.entity.ProductCommissionRatio;
import com.sw.plugins.product.manage.entity.ProductDocConfig;
import com.sw.plugins.product.manage.entity.ProductNetValue;
import com.sw.plugins.product.manage.entity.ProductProfit;
import com.sw.plugins.product.manage.entity.SubProduct;
import com.sw.plugins.product.manage.service.ProductAttributeValueService;
import com.sw.plugins.product.manage.service.ProductCommissionRatioService;
import com.sw.plugins.product.manage.service.ProductDocConfigService;
import com.sw.plugins.product.manage.service.ProductNetValueService;
import com.sw.plugins.product.manage.service.ProductProfitService;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.plugins.product.manage.service.SubProductService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.DateUtil;

@Controller
public class SaleManageController extends BaseController<Product> {

	private static Logger logger = Logger.getLogger(SaleManageController.class);

	@Resource
	private ProductService productService;
	@Resource
	private SubProductService subProductService;
	@Resource
	private SaleManageService saleManageService;
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private ProductProfitService productProfitService;
	@Resource
	private ProductNetValueService productNetValueService;
	@Resource
	private ProductCommissionRatioService commissionRatioService;
	@Resource
	private SaleManageMessageService saleManageMessageService;
	@Resource
	private OrganizationService organizationService;
	@Resource
	private ProductAttributeValueService productAttributeValueService;
	@Resource
	private ProviderService providerService;
	@Resource
	private DictionaryItemService dictionaryItemService;
	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private ProductDocConfigService productDocConfigService;
	@Resource
	private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor;

	@Override
	public String uploadfile(Product entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
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
	public CommonModelAndView valid(Product entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping("/market/salemanage/saleconfig/valid")
	public CommonModelAndView validSaleConfig(Product product, BindingResult result, Map<String, Object> model, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		Short sellStatus = product.getSellStatus();
		Short type = product.getType();

		// 判断是否共享募集额度
		Short isTotalShare = product.getIsTotalShare();
		if (isTotalShare == null) {
			result.rejectValue("isTotalShare", "NotNull");
		}

		// 货币基金,阳光私蓦产品成立后不受条件约束
		if (type != (short) 2 && type != (short) 5 || ((type == (short) 2 || type == (short) 5) && sellStatus != (short) 5 && sellStatus != (short) 6)) {

			// 有共享条件
			if (isTotalShare != null && isTotalShare.equals((short) 1)) {
				if (product.getMinTotalShare() == null) {
					result.rejectValue("minTotalShare", "NotNull");
				}
				if (product.getMaxTotalShare() == null) {
					result.rejectValue("maxTotalShare", "NotNull");
				}
			}

			// 判断募集时间是否合法
			String raiseStartTime = product.getRaiseStartTime();
			String raiseFinishTime = product.getRaiseFinishTime();
			// 募集开始时间
			if (raiseStartTime == null || ("").equals(raiseStartTime)) {
				result.rejectValue("raiseStartTime", "NotEmpty");
			}
			// 募集结束时间
			else if (raiseFinishTime == null || ("").equals(raiseFinishTime)) {
				result.rejectValue("raiseFinishTime", "NotEmpty");
			}
			// 开始时间不能少于结束时间
			else if (!raiseStartTime.equals(raiseFinishTime)) {
				Date st = DateUtil.stringToDate(raiseStartTime);
				Date ft = DateUtil.stringToDate(raiseFinishTime);
				if (st.after(ft)) {
					result.rejectValue("raiseFinishTime", "NotIllegal");
				}
			}

			// 判断期限类型是否为固定
			Short deadLineType = product.getDeadlineType();
			if (deadLineType != null && deadLineType.equals((short) 1)) {
				if (product.getMinDeadline() == null) {
					result.rejectValue("minDeadline", "NotNull");
				}
				if (product.getMaxDeadline() == null) {
					result.rejectValue("maxDeadline", "NotNull");
				}
			}
			// 判断预期收益类型是否为固定
			Short profitType = product.getProfitType();
			if (profitType != null && profitType.equals((short) 2)) {
				if (product.getMinProfitRatio() == null) {
					result.rejectValue("minProfitRatio", "NotNull");
				}
				if (product.getMaxProfitRatio() == null) {
					result.rejectValue("maxProfitRatio", "NotNull");
				}
			}

		}

		// 阳光私蓦产品成立后不受条件约束
		if (type != (short) 2 || (type == (short) 2 && sellStatus != (short) 5 && sellStatus != (short) 6)) {
			// 认购起点不能为空
			String beginShare = product.getBeginningShare();
			if (beginShare == null || ("").equals(beginShare)) {
				result.rejectValue("beginningShare", "NotEmpty");
			}

			// 认购递增规模不能为空
			BigDecimal incrementalShare = product.getIncrementalShare();
			if (incrementalShare == null) {
				result.rejectValue("incrementalShare", "NotNull");
			}
		}

		// 成立时间是否合法
		String foundDate = product.getFoundDate();
		String stopDate = product.getStopDate();
		if (!"".equals(foundDate) && !"".equals(stopDate) && !foundDate.equals(stopDate)) {
			Date st = DateUtil.stringToDate(foundDate);
			Date ft = DateUtil.stringToDate(stopDate);
			if (st.after(ft)) {
				result.rejectValue("stopDate", "NotIllegal");
			}
		}
		// 判断是否共享小额投资人数上限
		Short isTotalLowAmountClientCount = product.getIsTotalLowAmountClientCount();
		if (isTotalLowAmountClientCount != null && isTotalLowAmountClientCount.equals((short) 1)) {
			if (product.getMaxLowAmountClientCount() == null) {
				result.rejectValue("maxLowAmountClientCount", "NotNull");
			}
		}
		// 小额度投资金额上限不能为空
		BigDecimal lowAmountThreshold = product.getLowAmountThreshold();
		if (lowAmountThreshold == null) {
			result.rejectValue("lowAmountThreshold", "NotNull");
		}

		// 合同前缀是否为空
//		String contractPrefix = product.getContractPrefix();
//		if (contractPrefix == null || ("").equals(contractPrefix)) {
//			result.rejectValue("contractPrefix", "NotEmpty");
//		}

		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
		}
		return commonModelAndView;
	}

	@RequestMapping("/market/salemanage/saleconfig/split/valid")
	public CommonModelAndView validSaleConfig(SubProduct subProduct, BindingResult result, Map<String, Object> model, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		Short type = subProduct.getProductType();
		Short sellStatus = subProduct.getProductSaleStatus();

		// 判断是否共享募集额度
		if ((subProduct.getIsTotalShare().equals((short) 0))) {
			// 货币基金,阳光私蓦产品成立后不受条件约束
			if (type != (short) 2 && type != (short) 5 || ((type == (short) 2 || type == (short) 5) && sellStatus != (short) 5 && sellStatus != (short) 6)) {
				if (subProduct.getMinTotalShare() == null) {
					result.rejectValue("minTotalShare", "NotNull");
				}
				if (subProduct.getMaxTotalShare() == null) {
					result.rejectValue("maxTotalShare", "NotNull");
				}
			}
		}

		// 阳光私蓦产品成立后不受条件约束
		if (type != (short) 2 || (type == (short) 2 && sellStatus != (short) 5 && sellStatus != (short) 6)) {
			// 认购起点不能为空
			BigDecimal beginShare = subProduct.getBeginningShare();
			if (beginShare == null) {
				result.rejectValue("beginningShare", "NotNull");
			}
		}

		// 判断是否共享小额投资人数上限
		Short isTotalLowAmountClientCount = subProduct.getIsTotalLowAmountClientCount();
		if ((isTotalLowAmountClientCount != null && isTotalLowAmountClientCount.equals((short) 0))) {
			if (subProduct.getMaxLowAmountClientCount() == null) {
				result.rejectValue("maxLowAmountClientCount", "NotNull");
			}
		}

		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
		}
		return commonModelAndView;
	}

	@RequestMapping("/market/salemanage/saleconfig/docconfig/valid")
	public CommonModelAndView validDocConfig(@Valid ProductDocConfig config, BindingResult result, Map<String, Object> model, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		String telephone = config.getTelephone();
		String cellphone = config.getCellphone();
		// 判断电话有一个不为空
		if ("".equals(telephone) && "".equals(cellphone)) {
			result.rejectValue("telephone", "NotEmpty");
			result.rejectValue("cellphone", "NotEmpty");
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
		}
		return commonModelAndView;
	}

	/**
	 * 销售管控页面
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage")
	public CommonModelAndView list(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		// 产品类型
		commonModelAndView.addObject("productTypeMap", this.initData.getProductType());
		// 销售状态
		commonModelAndView.addObject("sellStatusMap", this.initData.getSellStatus());
		commonModelAndView.addObject("product", product);
		commonModelAndView.addObject("c", product.getC());
		return commonModelAndView;
	}

	@RequestMapping("/market/salemanage/grid")
	public CommonModelAndView json(Product product, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Integer type = user.getType();
			String position = user.getPosition();
			Organization uorg = user.getSelfOrg();
			if (uorg != null) {
				if (position != null && position.contains(",2,")) {
					Integer isCo = uorg.getIsCommission();
					if (isCo.equals(1)) {
						product.setOrgId(uorg.getId());
						product.setSalerType((short)2);
					}else{
						product.setSalerType((short)1);
					}
				}
			}
			if(type == 0 || (position != null && position.contains(",2,"))){
				product.setIsScManager((short)1);
			}
			map = productService.getSalingProductGrid(product);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, product, request);
	}

	/**
	 * 销售控制页面
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/manage")
	public CommonModelAndView salemanage(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		Product prd = null;
		Long productId = product.getId();
		String c = product.getC();
		Product tprd = new Product();
		tprd.setId(productId);
		try {
			prd = productService.getOne(tprd);
			if (prd == null) {
				prd = product;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		commonModelAndView.addObject("product", prd);
		commonModelAndView.addObject("c", c);
		return commonModelAndView;
	}

	/**
	 * 详细页面跳转
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/detail")
	public CommonModelAndView detail(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		String c = product.getC();
		Product product2 = null;
		try {
			// 获取产品基本属性
			product2 = productService.getOne(product);
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
	 * 销售控制页面（总览）
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/manage/overview")
	public CommonModelAndView overview(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		String c = product.getC();
		Long productId = product.getId();

		Product prd = null;
		SubProduct activeSubProduct = new SubProduct();
		List<SubProduct> subProductList = null;
		SaleManagePlot saleManagePlot = new SaleManagePlot();

		try {
			// 获取产品数据
			Product tprd = new Product();
			tprd.setId(productId);
			prd = productService.getOne(tprd);
			if (prd != null) {
				// 获取该产品下的子产品
				SubProduct tspd = new SubProduct();
				tspd.setProductId(productId);
				subProductList = subProductService.getSubProductList(tspd);
				if (subProductList != null) {
					// 前台是否传递特定的子产品
					Long subProductId = product.getSubProductId();
					if (subProductId != null) {
						for (SubProduct tsp : subProductList) {
							Long spId = tsp.getId();
							if (spId.equals(subProductId)) {
								activeSubProduct = tsp;
							}
						}
					}
					// 否则默认第一个子产品
					else {
						activeSubProduct = subProductList.get(0);
					}
					// 获取产品概览统计数据
					saleManagePlot = saleManageService.getOverviewPlot(prd, activeSubProduct);
				}
			} else {
				prd = product;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("product", prd);
		commonModelAndView.addObject("subProduct", activeSubProduct);
		commonModelAndView.addObject("subProductList", subProductList);
		commonModelAndView.addObject("plot", saleManagePlot);
		commonModelAndView.addObject("c", c);
		return commonModelAndView;
	}

	/**
	 * 额度分配页面
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/manage/neworder")
	public CommonModelAndView distribute(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		String c = product.getC();
		Long productId = product.getId();

		Product prd = null;
		SubProduct activeSubProduct = null;
		List<SubProduct> subProductList = null;
		SaleManagePlot saleManagePlot = new SaleManagePlot();

		try {
			// 获取产品数据
			Product tprd = new Product();
			tprd.setId(productId);
			prd = productService.getOne(tprd);
			if (prd != null) {
				// 获取该产品下的子产品
				SubProduct tspd = new SubProduct();
				tspd.setProductId(productId);
				subProductList = subProductService.getSubProductList(tspd);
				if (subProductList != null) {
					// 前台是否传递特定的子产品
					Long subProductId = product.getSubProductId();
					if (subProductId != null) {
						for (SubProduct tsp : subProductList) {
							Long spId = tsp.getId();
							if (spId.equals(subProductId)) {
								activeSubProduct = tsp;
							}
						}
					}
					// 否则默认第一个子产品
					else {
						activeSubProduct = subProductList.get(0);
					}
					// 获取产品额度分配统计数据
					saleManagePlot = saleManageService.getDistributePlot(prd, activeSubProduct);
				}
			} else {
				prd = product;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		commonModelAndView.addObject("product", prd);
		commonModelAndView.addObject("subProduct", activeSubProduct);
		commonModelAndView.addObject("subProductList", subProductList);
		commonModelAndView.addObject("plot", saleManagePlot);
		commonModelAndView.addObject("c", c);
		return commonModelAndView;
	}

	/**
	 * 已配额订单页面
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/manage/shared")
	public CommonModelAndView sharedOrder(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		Product prd = null;
		Long productId = product.getId();
		String c = product.getC();
		Product tprd = new Product();
		tprd.setId(productId);
		try {
			prd = productService.getOne(tprd);
			if (prd == null) {
				prd = product;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("product", prd);
		commonModelAndView.addObject("c", c);
		return commonModelAndView;
	}

	/**
	 * 已到账订单页面
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/manage/payed")
	public CommonModelAndView payedOrder(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		Product prd = null;
		Long productId = product.getId();
		String c = product.getC();
		Product tprd = new Product();
		tprd.setId(productId);
		try {
			prd = productService.getOne(tprd);
			if (prd == null) {
				prd = product;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("product", prd);
		commonModelAndView.addObject("c", c);
		return commonModelAndView;
	}

	/**
	 * 小额订单页面
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/manage/limit")
	public CommonModelAndView limitOrder(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);

		String c = product.getC();
		Long productId = product.getId();

		Product prd = null;
		SubProduct activeSubProduct = null;
		List<SubProduct> subProductList = null;
		SaleManagePlot saleManagePlot = new SaleManagePlot();

		try {
			// 获取产品数据
			Product tprd = new Product();
			tprd.setId(productId);
			prd = productService.getOne(tprd);
			if (prd != null) {
				// 获取该产品下的子产品
				SubProduct tspd = new SubProduct();
				tspd.setProductId(productId);
				subProductList = subProductService.getSubProductList(tspd);
				if (subProductList != null) {
					// 前台是否传递特定的子产品
					Long subProductId = product.getSubProductId();
					if (subProductId != null) {
						for (SubProduct tsp : subProductList) {
							Long spId = tsp.getId();
							if (spId.equals(subProductId)) {
								activeSubProduct = tsp;
							}
						}
					}
					// 否则默认第一个子产品
					else {
						activeSubProduct = subProductList.get(0);
					}
					// 获取产品额度分配统计数据
					saleManagePlot = saleManageService.getDistributePlot(prd, activeSubProduct);
				}
			} else {
				prd = product;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		commonModelAndView.addObject("product", prd);
		commonModelAndView.addObject("subProduct", activeSubProduct);
		commonModelAndView.addObject("subProductList", subProductList);
		commonModelAndView.addObject("plot", saleManagePlot);
		commonModelAndView.addObject("c", c);
		return commonModelAndView;
	}

	/**
	 * 获取在途订单列表数据
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/manage/distribute/newordersgrid")
	public CommonModelAndView newOrdersJSON(SaleManage order, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = saleManageService.getNewOrdersGrid(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, order, request);
	}

	@RequestMapping("/market/salemanage/manage/distribute/limitnewordersgrid")
	public CommonModelAndView limitNewOrdersJSON(SaleManage order, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			Long productId = order.getProductId();
			// 获取产品数据
			Product tprd = new Product();
			tprd.setId(productId);
			Product prd = productService.getOne(tprd);
			if (prd != null) {
				order.setAmountLimit(prd.getLowAmountThreshold());
				map = saleManageService.getLimitNewOrdersGrid(order);
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, order, request);
	}

	/**
	 * 获取已配额订单列表数据
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/manage/distribute/sharedordersgrid")
	public CommonModelAndView sharedOrdersJSON(SaleManage order, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = saleManageService.getSharedOrdersGrid(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, order, request);
	}

	/**
	 * 获取已到账订单列表数据
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/manage/distribute/payedordersgrid")
	public CommonModelAndView payedOrdersJSON(SaleManage order, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = saleManageService.getPayedOrdersGrid(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, order, request);
	}

	/**
	 * 批量保存额度分配
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/market/salemanage/manage/distribute/distributeall", method = RequestMethod.POST)
	public CommonModelAndView distributeAll(SaleManage saleManage, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			List<SaleManage> orderList = saleManage.getSaleManageList();
			if (orderList != null) {
				saleManageService.distributeAllShare(saleManage);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return new CommonModelAndView(viewName, saleManage, request, messageSource);
	}

	/**
	 * 保存修改分配额度
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/market/salemanage/manage/distribute/updatedistribute", method = RequestMethod.POST)
	public CommonModelAndView updateDistribute(SaleManage saleManage, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			BigDecimal share = saleManage.getShare();
			if (share != null) {
				saleManageService.updateShare(saleManage, (short) 2);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return new CommonModelAndView(viewName, saleManage, request, messageSource);
	}

	/**
	 * 撤销分配额度
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/market/salemanage/manage/distribute/cancel", method = RequestMethod.POST)
	public CommonModelAndView cancelDistribute(SaleManage saleManage, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			// 清除分配的额度和分配时间，更改额度分配状态为未分配
			saleManage.setShare(null);
			saleManage.setShareTime(null);
			saleManageService.updateShareAndTradeStatus(saleManage, (short) 1);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(e);
		}
		return new CommonModelAndView(viewName, saleManage, request, messageSource);
	}

	/**
	 * 催款\退款通知
	 * 
	 * @param saleManage
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/market/salemanage/manage/distribute/fundnotice", method = RequestMethod.POST)
	public CommonModelAndView fundNotice(SaleManage saleManage, HttpServletRequest request) {
		String viewName = this.ERROR;
		short type = (short) 1;
		BigDecimal reFund = saleManage.getReFund();
		try {
			Long orderId = saleManage.getOrderId();
			if (orderId != null) {
				if (reFund != null) {
					type = (short) 0;
				}
				SaleManageMessage saleManageMessage = new SaleManageMessage();
				saleManageMessage.setOrderId(orderId);
				saleManageMessage.setCreatorUserId(CreatorContextHolder.getCreatorContext());
				simpleAsyncTaskExecutor.submit(new SaleManageMessageTask(saleManageMessageService, Methods.HANDLEFUND, saleManageMessage, type));
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return new CommonModelAndView(viewName, saleManage, request, messageSource);
	}

	/**
	 * 变更产品状态
	 * 
	 * @return
	 */
	@RequestMapping(value = "/market/salemanage/manage/overview/alterstatus", method = RequestMethod.POST)
	public CommonModelAndView alterProductStatus(SaleManage saleManage, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			// 预约状态
			Short isOrder = saleManage.getIsOrder();
			// 打款状态
			Short isRemittance = saleManage.getIsRemittance();
			// 收藏状态
			Short isFavorites = saleManage.getIsFavorites();
			// 产品销售状态
			Short sellStatus = saleManage.getSellStatus();
			// 变更产品预约状态
			if (isOrder != null) {
				saleManageService.updateProductIsOrder(saleManage, isOrder);
				viewName = this.SUCCESS;
			}
			// 变更打款状态
			else if (isRemittance != null) {
				saleManageService.updateProductIsRemittance(saleManage, isRemittance);
				viewName = this.SUCCESS;
			}
			// 变更收藏状态
			else if (isFavorites != null) {
				saleManageService.updateProductIsFavorites(saleManage, isFavorites);
				viewName = this.SUCCESS;
			}
			// 变更主产品销售状态
			else if (sellStatus != null) {
				saleManageService.updateProductSellStatus(saleManage, sellStatus);
				viewName = this.SUCCESS;
			}
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(e);
		}
		return new CommonModelAndView(viewName, saleManage, request, messageSource);
	}

	/**
	 * 检查产品成立条件 所有全部资金到账的订单合同编号是否齐全
	 * 
	 * @param saleManage
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/market/salemanage/manage/overview/checkproductfundcondition", method = RequestMethod.GET)
	public CommonModelAndView checkProductFundCondition(SaleManage saleManage, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			long co = saleManageService.getProductFundCondition(saleManage);
			if (co == 0) {
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return new CommonModelAndView(viewName, saleManage, request, messageSource);
	}

	/**
	 * 变更预约和打款状态
	 * 
	 * @return
	 */
	@RequestMapping(value = "/market/salemanage/manage/overview/alterorderandremittancestatus", method = RequestMethod.POST)
	public CommonModelAndView alterOrderAndRemittanceStatus(SaleManage saleManage, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			// 状态
			Short isOrderAndRemittance = saleManage.getIsOrderAndRemittance();
			if (isOrderAndRemittance != null) {
				saleManageService.updateOrderAndRemittance(saleManage, isOrderAndRemittance);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return new CommonModelAndView(viewName, saleManage, request, messageSource);
	}

	/**
	 * 销售配置
	 * 
	 * @param product
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig")
	public CommonModelAndView saleConfig(Product product, HttpServletRequest request) {
		CommonModelAndView cv = new CommonModelAndView(request, product, messageSource);
		Long productId = null;
		String code = null;
		try {
			productId = product.getId();
			code = product.getC();
			// 获取产品信息
			Product pt = new Product();
			pt.setId(productId);
			pt = productService.getOne(pt);
			if (pt != null) {
				product = pt;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		cv.addObject("product", product);
		cv.addObject("c", code);
		return cv;
	}

	@RequestMapping("/market/salemanage/saleconfig/saleconfigjson")
	public CommonModelAndView saleconfigJson(SubProduct subProduct, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			Long productId = subProduct.getProductId();
			SubProduct sp = new SubProduct();
			sp.setProductId(productId);
			map = subProductService.getGrid(sp);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, subProduct, request);
	}

	/**
	 * 计算子产品数量，用于控制销售控制按钮
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/countsubproduct")
	public CommonModelAndView subproductcountJson(SubProduct subProduct, HttpServletRequest request) {
		Long count = 0L;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			count = subProductService.countSubProduct(subProduct);
			map = new HashMap<String, Object>();
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		map.put("count", count);
		return new CommonModelAndView("jsonView", map, subProduct, request);
	}

	/**
	 * 设置销售参数
	 * 
	 * @param subProduct
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/config")
	public CommonModelAndView saleParamConfig(Product product, HttpServletRequest request, Map<String, Object> model) {
		CommonModelAndView cv = new CommonModelAndView(request, product, messageSource);
		String c = product.getC();
		Product spt = null;
		try {
			Long productId = product.getId();
			// 获取产品信息
			Product pt = new Product();
			pt.setId(productId);
			spt = productService.getOne(pt);
			// 有子产品ID则加载修改项数据
			if (spt == null) {
				spt = product;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		model.put("product", spt);
		cv.addAllObjects(model);
		// 销售类型
		cv.addObject("sellTypeMap", this.initData.getSellType());
		cv.addObject("closeType", initData.getCloseType());
		cv.addObject("c", c);
		return cv;
	}

	/**
	 * 保存销售参数
	 * 
	 * @param subProduct
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/config/save")
	public CommonModelAndView saveSaleConfig(Product product, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			Long id = product.getId();
			if (id != null) {
				productService.update(product);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView(viewName, product, request, messageSource);
	}

	/**
	 * 拆分产品
	 * 
	 * @param subProductId
	 *            productId
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/split")
	public CommonModelAndView split(SubProduct subProduct, HttpServletRequest request, Map<String, Object> model) {
		CommonModelAndView cv = new CommonModelAndView(request, subProduct, messageSource);
		String c = subProduct.getC();
		Product spt = null;
		SubProduct spr = null;
		try {
			Long productId = subProduct.getProductId();
			Long subProductId = subProduct.getId();
			// 获取产品信息
			Product pt = new Product();
			pt.setId(productId);
			spt = productService.getOne(pt);
			// 有子产品ID则加载修改项数据
			if (spt != null && subProductId != null) {
				SubProduct sp = new SubProduct();
				sp.setId(subProductId);
				spr = subProductService.getSubProduct(sp);
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		if (spr == null) {
			spr = new SubProduct();
		}
		model.put("subProduct", spr);
		cv.addAllObjects(model);
		cv.addObject("product", spt);
		cv.addObject("payWay", initData.getPayWay());
		cv.addObject("c", c);
		return cv;
	}

	/**
	 * 保存拆分产品
	 * 
	 * @param subProduct
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/split/save")
	public CommonModelAndView saveProductSplit(SubProduct subProduct, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			subProductService.saveSubProduct(subProduct);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView(viewName, subProduct, request, messageSource);
	}

	/**
	 * 删除信托拆分信息
	 * 
	 * @param subProduct
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/delete")
	public CommonModelAndView deleteSaleConfig(SubProduct subProduct, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			if (subProduct.getId() != null) {
				subProductService.deleteSubProduct(subProduct);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView(viewName, subProduct, request, messageSource);
	}

	/**
	 * 单证配置
	 * 
	 * @param config
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/docconfig")
	public CommonModelAndView docConfig(ProductDocConfig config, HttpServletRequest request, Map<String, Object> model) {
		CommonModelAndView cv = new CommonModelAndView(request, config, messageSource);
		String c = config.getC();
		ProductDocConfig docConfig = null;
		try {
			Long productId = config.getProductId();
			if (productId != null) {
				docConfig = productDocConfigService.getOne(config);
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		if (docConfig == null) {
			docConfig = config;
		}
		model.put("docConfig", docConfig);
		cv.addAllObjects(model);
		cv.addObject("c", c);
		return cv;
	}

	/**
	 * 保存单证配置
	 * 
	 * @param config
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/docconfig/save")
	public CommonModelAndView saveProductDocConfig(ProductDocConfig config, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			Long productId = config.getProductId();
			if (productId != null) {
				productDocConfigService.saveProductDocConfig(config);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView(viewName, config, request, messageSource);
	}

	/**
	 * 收益率设置
	 * 
	 * @param subProduct
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/profit")
	public CommonModelAndView profitConfig(SubProduct subProduct, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, subProduct, messageSource);
		try {
			commonModelAndView.addObject("subProduct", subProduct);
			commonModelAndView.addObject("c", subProduct.getC());
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}

	/**
	 * 获取产品收益率异步数据
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/profitdata")
	public CommonModelAndView profitJSON(ProductProfit profit, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ProductProfit> profitList = null;
		try {
			Long subProductId = profit.getSubProductId();
			if (subProductId != null) {
				profitList = productProfitService.getProductProfitList(profit);
			}
			map.put("rows", profitList);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, profit, request);
	}

	@RequestMapping("/market/salemanage/saleconfig/profit/save")
	public CommonModelAndView saveProductProfit(ProductProfit profit, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(this.STATUS, STATUS_FALSE);
		try {
			productProfitService.saveProductProfit(profit);
			map.put(this.STATUS, STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map);
	}

	@RequestMapping("/market/salemanage/saleconfig/profit/delete")
	public CommonModelAndView delProductProfit(ProductProfit pt, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(this.STATUS, STATUS_FALSE);
		try {
			productProfitService.delete(pt);
			map.put(this.STATUS, STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 净值
	 * 
	 * @param product
	 *            id type
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/netvalue")
	public CommonModelAndView netValueConfig(SubProduct subProduct, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, subProduct, messageSource);
		try {
			String code = subProduct.getC();
			commonModelAndView.addObject("subProduct", subProduct);
			commonModelAndView.addObject("c", code);
		} catch (Exception e) {
			logger.error(e);
		}
		return commonModelAndView;
	}

	/**
	 * 产品净值维护列表数据
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/netvalue/grid")
	public CommonModelAndView productNetValueJSON(ProductNetValue productNetValue, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = productNetValueService.getProductNetValueGrid(productNetValue);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, productNetValue, request);
	}

	@RequestMapping("/market/salemanage/saleconfig/netvalue/save")
	public CommonModelAndView saveProductNetValue(ProductNetValue productNetValue, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(this.STATUS, STATUS_FALSE);
		try {
			productNetValueService.saveOrUpdate(productNetValue);
			map.put(this.STATUS, STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map);
	}

	@RequestMapping("/market/salemanage/saleconfig/netvalue/delete")
	public CommonModelAndView delProductNetValue(ProductNetValue pt, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(this.STATUS, STATUS_FALSE);
		try {
			productNetValueService.delete(pt);
			map.put(this.STATUS, STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 设置佣金率
	 * 
	 * @param commissionRatio
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/commissionratio")
	public CommonModelAndView commissionRatioConfig(SubProduct subProduct, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, subProduct, messageSource);
		try {
			commonModelAndView.addObject("subProduct", subProduct);
			commonModelAndView.addObject("c", subProduct.getC());
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}

	/**
	 * 获取佣金率异步数据
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/salemanage/saleconfig/commissionratiodata")
	public CommonModelAndView commissionRatioJSON(ProductCommissionRatio commissionRatio, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ProductCommissionRatio> commissionRatioList = null;
		try {
			Long subProductId = commissionRatio.getSubProductId();
			if (subProductId != null) {
				commissionRatioList = commissionRatioService.getList(commissionRatio);
			}
			map.put("rows", commissionRatioList);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, commissionRatio, request);
	}

	@RequestMapping("/market/salemanage/saleconfig/commissionratio/save")
	public CommonModelAndView saveProductCommissiobRatio(ProductCommissionRatio commissionRatio, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(this.STATUS, STATUS_FALSE);
		if (commissionRatio.getIncentiveFeeRate() == null) {
			commissionRatio.setIncentiveFeeRate(BigDecimal.valueOf(0));
		}
		try {
			commissionRatioService.saveProductCommissionRatio(commissionRatio);
			map.put(this.STATUS, STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map);
	}

	@RequestMapping("/market/salemanage/saleconfig/commissionratio/delete")
	public CommonModelAndView delcommissionRatioService(ProductCommissionRatio commissionRatio, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(this.STATUS, STATUS_FALSE);
		try {
			commissionRatioService.delete(commissionRatio);
			map.put(this.STATUS, STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map);
	}

	@RequestMapping("/market/salemanage/manage/overview/check_docconfig")
	public CommonModelAndView checkProductDocConfig(ProductDocConfig config, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Long productId = config.getProductId();
			if (productId != null) {
				Long co = productDocConfigService.getRecordCount(config);
				map.put(this.STATUS, co);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map);
	}

}
