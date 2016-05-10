package com.sw.plugins.customer.client.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.client.entity.Client;
import com.sw.plugins.market.order.entity.HoldingProduct;
import com.sw.plugins.market.order.service.OrderHoldingProductService;

/**
 * 理财顾问客户-Service实现类
 *
 * @author runchao
 */
public class ClientService extends CommonService<Client>{
	
	@Resource
	private OrderHoldingProductService orderHoldingProductService;

	/**
	 * 获取理财顾问客户列表集合
	 * 
	 * @param client
	 * @return
	 */
	@Override
	public Map<String, Object> getGrid(Client client) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Client> resultList = getPaginatedList(client);
		Long record = getRecordCount(client);
		int pageCount = (int)Math.ceil(record/(double)client.getRows());
		map.put("rows", resultList);
		map.put("page", client.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 理财顾问客户统计查询
	 * 
	 * @param client
	 * @return
	 */
	@Override
	public Long getRecordCount(Client client) throws Exception {
		return getRelationDao().getCount("client.count", client);
	}
	
	/**
	 * 理财顾问客户分页查询
	 * 
	 * @param client
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Client> getPaginatedList(Client client) throws Exception {
		List<Client> list = (List<Client>) getRelationDao().selectList("client.selectPaginatedList", client);
		return list;
	}
	
	/**
	 * 查询某个理财顾问客户
	 * 
	 * @param client
	 * @return
	 */
	@Override
	public Client getOne(Client client) throws Exception {
		return (Client) getRelationDao().selectOne("client.selectOne", client);
	}
	
	/**
	 * 获取理财顾问客户存续产品列表集合
	 * 
	 * @param client
	 * @return
	 */
	public Map<String, Object> getGridProduct(Client client) throws Exception {
		HoldingProduct holdingProduct = new HoldingProduct();
		holdingProduct.setMemberID(client.getMemberId());
		holdingProduct.setiDCardType(client.getIdCardType());
		holdingProduct.setiDCard(client.getIdCard());
		holdingProduct.setRows(client.getRows());
		holdingProduct.setPage(client.getPage());
		Map<String, Object> map = new Hashtable<String, Object>();
		List<HoldingProduct> resultList = orderHoldingProductService.getPaginatedListClientHolding(holdingProduct);
		Long record = orderHoldingProductService.getCountClientHolding(holdingProduct);
		int pageCount = (int)Math.ceil(record/(double)client.getRows());
		map.put("rows", resultList);
		map.put("page", client.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 验证客户是否为 该理财师下
	 * @param client
	 * @return
	 * @throws Exception 
	 */
	public Client checkMemberClient(Client client) throws Exception {
		return (super.getRelationDao().selectList("client.check_client_one", client)==null || super.getRelationDao().selectList("client.check_client_one", client).size()<=0) 
				? null : (Client) super.getRelationDao().selectList("client.check_client_one", client).get(0);
	}
	
	@Override
	public void save(Client entity) throws Exception {
		super.getRelationDao().insert("client.insert", entity);
	}

	@Override
	public void update(Client entity) throws Exception {
		super.getRelationDao().update("client.update", entity);
	}

	@Override
	public List<Client> getList(Client entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Client entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(Client entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String upload(Client entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(Client entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
