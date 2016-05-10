package com.sw.plugins.product.manage.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.ProductCommissionRatio;

public class ProductCommissionRatioService extends CommonService<ProductCommissionRatio> {

	@Resource
	private ProductService productService;

	public void saveProductCommissionRatio(ProductCommissionRatio entity) throws Exception {
		Long id = entity.getId();
		if (id != null) {
			this.update(entity);
		} else {
			this.save(entity);
		}
		Short alterCloseType = entity.getAlterCloseType();
		Long productId = entity.getProductId();
		if (alterCloseType != null && productId != null && alterCloseType.equals((short) 1)) {
			Product pt = new Product();
			pt.setId(productId);
			pt.setCloseType(1);
			productService.update(pt);
		}
	}

	@Override
	public void save(ProductCommissionRatio entity) throws Exception {
		getRelationDao().insert("commissionRatio.insert", entity);
	}

	@Override
	public void update(ProductCommissionRatio entity) throws Exception {
		getRelationDao().update("commissionRatio.update", entity);
	}

	@Override
	public Long getRecordCount(ProductCommissionRatio entity) throws Exception {
		return (Long) getRelationDao().selectOne("commissionRatio.count", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductCommissionRatio> getList(ProductCommissionRatio entity) throws Exception {
		return (List<ProductCommissionRatio>) getRelationDao().selectList("commissionRatio.select", entity);
	}
	
	public ProductCommissionRatio getOneByHoldlingPhase(ProductCommissionRatio entity) throws Exception {
		return (ProductCommissionRatio) getRelationDao().selectOne("commissionRatio.select_by_id", entity);
	}
	
	
	/**
	 * 查询认购订单佣金比例数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public ProductCommissionRatio getOneBySubproductID(ProductCommissionRatio entity) throws Exception {
		return (ProductCommissionRatio) getRelationDao().selectOne("commissionRatio.select_by_subproductid", entity);
	}
	
	/**
	 * 查询申购订单佣金比例数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public ProductCommissionRatio getOneSubscribe(ProductCommissionRatio entity) throws Exception {
		return (ProductCommissionRatio) getRelationDao().selectOne("commissionRatio.select_subscribe", entity);
	}

	@Override
	public List<ProductCommissionRatio> getPaginatedList(ProductCommissionRatio entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ProductCommissionRatio entity) throws Exception {
		getRelationDao().delete("commissionRatio.delete", entity);
	}

	public void deleteBySubProductId(ProductCommissionRatio entity) throws Exception {
		getRelationDao().delete("commissionRatio.delete_by_subproductid", entity);
	}

	@Override
	public void deleteByArr(ProductCommissionRatio entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ProductCommissionRatio getOne(ProductCommissionRatio entity) throws Exception {
		return (ProductCommissionRatio) getRelationDao().selectOne("commissionRatio.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(ProductCommissionRatio entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<ProductCommissionRatio> resultList = this.getList(entity);
		map.put("rows", resultList);
		return map;
	}

	@Override
	public String upload(ProductCommissionRatio entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductCommissionRatio entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

}
