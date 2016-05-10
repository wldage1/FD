package com.sw.plugins.product.manage.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.manage.entity.ProductCommissionRatio;
import com.sw.plugins.product.manage.entity.ProductProfit;
import com.sw.plugins.product.manage.entity.SubProduct;

public class SubProductService extends CommonService<SubProduct> {

	@Resource
	private ProductProfitService profitService;
	@Resource
	private ProductCommissionRatioService commissionRatioService;

	@Override
	public void save(SubProduct entity) throws Exception {
		getRelationDao().insert("subProduct.insert_sub_product", entity);
	}

	@Override
	public void update(SubProduct entity) throws Exception {
		getRelationDao().update("subProduct.update_sub_product", entity);
	}

	@Override
	public Long getRecordCount(SubProduct entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubProduct> getList(SubProduct entity) throws Exception {
		return (List<SubProduct>) getRelationDao().selectList("subProduct.select", entity);
	}

	@Override
	public List<SubProduct> getPaginatedList(SubProduct entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(SubProduct entity) throws Exception {
		getRelationDao().delete("subProduct.delete_sub_product", entity);
	}

	@Override
	public void deleteByArr(SubProduct entity) throws Exception {
		// if (entity != null && entity.getIds() != null) {
		// for (String id : entity.getIds()) {
		// entity.setId(Long.valueOf(id));
		// delete(entity);
		// }
		// }
	}

	@Override
	public SubProduct getOne(SubProduct entity) throws Exception {

		return (SubProduct) getRelationDao().selectOne("subProduct.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(SubProduct entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<SubProduct> resultList = this.getSubProductList(entity);
		map.put("rows", resultList);
		return map;
	}

	@Override
	public String upload(SubProduct entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(SubProduct entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 获取子产品
	 * 
	 * @param subProduct
	 * @throws Exception
	 */
	public SubProduct getSubProduct(SubProduct subProduct) throws Exception {
		return (SubProduct) getRelationDao().selectOne("subProduct.select_sub_product", subProduct);
	}

	/**
	 * 获取子产品
	 * 
	 * @param subProduct
	 * @throws Exception
	 */
	public Long countSubProduct(SubProduct subProduct) throws Exception {
		return (long) getRelationDao().getCount("subProduct.count_sub_product", subProduct);
	}

	/**
	 * 获取子产品
	 * 
	 * @param subProduct
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SubProduct> getSubProductList(SubProduct subProduct) throws Exception {
		return (List<SubProduct>) getRelationDao().selectList("subProduct.select_sub_product", subProduct);
	}
	
	/**
	 * 获取未停止打款的子产品
	 * @param subProduct
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SubProduct> getSubProductList1(SubProduct subProduct) throws Exception {
		return (List<SubProduct>) getRelationDao().selectList("subProduct.select_sub_product_isRemittance", subProduct);
	}
	
	
	/**
	 * 保存子产品表
	 * 
	 * @param subProduct
	 * @throws Exception
	 */
	public void saveSubProduct(SubProduct subProduct) throws Exception {
		Long tsubProductId = subProduct.getId();
		if(subProduct.getDeadline() == null){
			subProduct.setDeadline(0);
		}
		// 新增
		if (tsubProductId == null) {
			Short type = subProduct.getProductType();
			getRelationDao().insert("subProduct.insert_sub_product", subProduct);
			Long subProductId = subProduct.getGeneratedKey();
			// 保存私募基金及其它产品子产品
			if (type != 1) {
				ProductProfit prt = subProduct.getProductProfit();
				if (prt != null) {
					prt.setSubProductId(subProductId);
					profitService.save(prt);
				}
			}
		}
		// 更新
		else {
			Short type = subProduct.getProductType();
			getRelationDao().update("subProduct.update_sub_product", subProduct);
			// 保存私募基金及其它产品子产品
			if (type != 1) {
				ProductProfit prt = subProduct.getProductProfit();
				if (prt != null) {
					prt.setSubProductId(tsubProductId);
					profitService.deleteProductProfit(prt);
					profitService.save(prt);
				}
			}
		}
	}

	/**
	 * 删除子产品表
	 * 
	 * @param subProduct
	 * @throws Exception
	 */
	public void deleteSubProduct(SubProduct subProduct) throws Exception {
		Long subProductId = subProduct.getId();
		// 清空子产品收益率
		ProductProfit profit = new ProductProfit();
		profit.setSubProductId(subProductId);
		profitService.deleteProfitBySubProductId(profit);
		// 清空佣金率
		ProductCommissionRatio commissionRatio  = new ProductCommissionRatio();
		commissionRatio.setSubProductId(subProductId);
		commissionRatioService.deleteBySubProductId(commissionRatio);
		// 删除子产品
		this.delete(subProduct);
	}

}
