package com.sw.plugins.product.manage.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.manage.entity.ProductProfit;

public class ProductProfitService extends CommonService<ProductProfit> {

	public ProductProfit countExpectProfitRatio(ProductProfit productProfit) throws Exception{
		return (ProductProfit) getRelationDao().selectOne("productProfit.countExpectProfitRatio", productProfit);
	}
	
	public void deleteProfitBySubProductId(ProductProfit profit)throws Exception{
		getRelationDao().delete("productProfit.delete_product_profit_by_subproductid", profit);
	}
	
	@Override
	public void save(ProductProfit entity) throws Exception {
		getRelationDao().insert("productProfit.insert_product_profit", entity);
	}

	@Override
	public void update(ProductProfit entity) throws Exception {
		getRelationDao().update("productProfit.update_product_profit", entity);
	}

	@Override
	public Long getRecordCount(ProductProfit entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductProfit> getList(ProductProfit entity) throws Exception {
		return (List<ProductProfit>) getRelationDao().selectList("productProfit.select", entity);
	}

	@Override
	public List<ProductProfit> getPaginatedList(ProductProfit entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ProductProfit entity) throws Exception {
		getRelationDao().delete("productProfit.delete_product_profit", entity);
	}

	@Override
	public void deleteByArr(ProductProfit entity) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public ProductProfit getOne(ProductProfit entity) throws Exception {

		return (ProductProfit) getRelationDao().selectOne("productProfit.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(ProductProfit entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<ProductProfit> resultList = this.getProductProfitList(entity);
		map.put("rows", resultList);
		return map;
	}

	@Override
	public String upload(ProductProfit entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductProfit entity, HttpServletRequest request) throws Exception {
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
	 * @param productProfit
	 * @throws Exception
	 */
	public Long countProductProfit(ProductProfit productProfit) throws Exception {
		return (long) getRelationDao().getCount("productProfit.count_sub_product", productProfit);
	}

	/**
	 * 获取子产品收益
	 * 
	 * @param productProfit
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ProductProfit> getProductProfitList(ProductProfit productProfit) throws Exception {
		return (List<ProductProfit>) getRelationDao().selectList("productProfit.select_product_profit", productProfit);
	}

	/**
	 * 保存子产品收益表
	 * 
	 * @param productProfit
	 * @throws Exception
	 */
	public void saveProductProfit(ProductProfit profit) throws Exception {
		Long id = profit.getId();
		ProductProfit productProfit=(ProductProfit)super.getRelationDao().selectOne("productProfit.select_ProductType_bysubid", profit);
		if(id != null){
			//私募/货币基金 有生效日期
			if(!productProfit.getProductType().equals((short)2) && !productProfit.getProductType().equals((short)5)){
				profit.setEffectiveDate("");
			}
			this.update(profit);
		}else{
			//私募/货币基金 有生效日期
			if(!productProfit.getProductType().equals((short)2) && !productProfit.getProductType().equals((short)5)){
				DateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
				profit.setEffectiveDate(sf.format(new Date()));
			}
			this.save(profit);
		}
	}

	/**
	 * 删除子产品收益表
	 * 
	 * @param productProfit
	 * @throws Exception
	 */
	public void deleteProductProfit(ProductProfit productProfit) throws Exception {
		getRelationDao().delete("productProfit.delete_product_profit", productProfit);
	}
}
