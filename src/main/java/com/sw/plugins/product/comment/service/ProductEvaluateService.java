package com.sw.plugins.product.comment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.HoldingProduct;
import com.sw.plugins.market.order.service.OrderHoldingProductService;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.product.comment.entity.ProductEvaluate;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.SystemProperty;

public class ProductEvaluateService extends CommonService<ProductEvaluate>{

	@Resource
	private ProductService productService;
	@Resource
	private OrderHoldingProductService orderHoldingProductService;
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private CommonMessageService commonMessageService;
	
	/**
	 * 查询产品附带评价集合，返回json
	 * 
	 * @param productEvaluate
	 * @return
	 */
	@Override
	public Map<String, Object> getGrid(ProductEvaluate productEvaluate) throws Exception {
		Product product = new Product();
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
		product.setPage(productEvaluate.getPage());
		product.setRows(productEvaluate.getRows());
		product.setShortName(productEvaluate.getProductShortName());
		product.setType(productEvaluate.getProductType());
		return productService.getGridComment(product);
	}
	
	/**
	 * 查询产品评价记录集合，返回json
	 * 
	 * @param productEvaluate
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGridEvaluate(ProductEvaluate productEvaluate) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<ProductEvaluate> resultList = getPaginatedList(productEvaluate);
		Long record = getRecordCount(productEvaluate);
		int pageCount = (int)Math.ceil(record/(double)productEvaluate.getRows());
		map.put("rows", resultList);
		map.put("page", productEvaluate.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 推送问卷
	 * 
	 * @param productEvaluate
	 * @throws Exception 
	 */
	public void push(ProductEvaluate productEvaluate) throws Exception{
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("parameter_productShortName", productEvaluate.getProductShortName());
		String url =SystemProperty.getInstance("config").getProperty("websiteDomainAddress");
		parameterMap.put("websiteDomainAddress", url);
		parameterMap.put("productID", productEvaluate.getProductId());
		SendMessage sendMessage = new SendMessage();
		List<UserMessage> messageList = new ArrayList<UserMessage>();
		HoldingProduct holdingProduct = new HoldingProduct();
		holdingProduct.setProductID(productEvaluate.getProductId());
		List<HoldingProduct> list = orderHoldingProductService.getListComment(holdingProduct);
		for (HoldingProduct holdingProduct2 : list) {
			UserMessage us = new UserMessage();
			us.setUserID(holdingProduct2.getMemberID());
			us.setUserType((short)3);
			messageList.add(us);
		}
		sendMessage.setUserList(messageList);
		//设置发送方式
		sendMessage.setSendWayStr("1");
		//设置模板参数
		sendMessage.setTemplateParameters(parameterMap);
		//设置模板code
		sendMessage.setTemplateCode("sys_manage_product_push");
		User user = userLoginService.getCurrLoginUser();
		//设置发送者ID
		sendMessage.setSendUserID(user.getId());
		//设置发送者类型
		sendMessage.setSendUserType((short)1);
		//设置关联对象类型
		sendMessage.setRelationType((short)3);
		//发送消息
		commonMessageService.send(sendMessage);
		//更新产品问卷推送状态
		Product product = new Product();
		product.setId(productEvaluate.getProductId());
		product.setPushStatus((short)1);
		productService.update(product);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductEvaluate> getPaginatedList(ProductEvaluate productEvaluate) throws Exception {
		return (List<ProductEvaluate>) getRelationDao().selectList("productEvaluate.selectPaginated", productEvaluate);
	}
	
	@Override
	public Long getRecordCount(ProductEvaluate productEvaluate) throws Exception {
		return getRelationDao().getCount("productEvaluate.count", productEvaluate);
	}
	
	@Override
	public void save(ProductEvaluate entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ProductEvaluate entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ProductEvaluate> getList(ProductEvaluate entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ProductEvaluate entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(ProductEvaluate entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProductEvaluate getOne(ProductEvaluate entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(ProductEvaluate entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductEvaluate entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
