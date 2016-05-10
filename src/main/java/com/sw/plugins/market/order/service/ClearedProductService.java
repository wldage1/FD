package com.sw.plugins.market.order.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.ClearedProduct;
import com.sw.plugins.market.order.entity.HoldingProduct;

/**
 * 税Service
 * 
 * @author liushuai
 * 
 */
@Service
public class ClearedProductService extends CommonService<ClearedProduct> {

	public void save(HoldingProduct entity) throws Exception {
		this.getRelationDao().insert("clearedProduct.insert", entity);
	}

	@Override
	public void save(ClearedProduct entity) throws Exception {
		this.getRelationDao().insert("clearedProduct.insert", entity);
	}
	
	/**
	 * 由赎回存续产品 产生的清算产品
	 * @param clearedProduct
	 * @throws Exception
	 */
	public void saveByRedeem(ClearedProduct clearedProduct) throws Exception {
		this.getRelationDao().insert("clearedProduct.insert_by_redeem", clearedProduct);		
	}

	@Override
	public void update(ClearedProduct entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getRecordCount(ClearedProduct entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClearedProduct> getList(ClearedProduct entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClearedProduct> getPaginatedList(ClearedProduct entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ClearedProduct entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByArr(ClearedProduct entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ClearedProduct getOne(ClearedProduct entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(ClearedProduct entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(ClearedProduct entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ClearedProduct entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}
}
