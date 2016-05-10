package com.sw.plugins.market.order.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.HoldingProduct;
/**
 * 存续产品Service
 * @author liushuai
 *
 */
@Service
public class OrderHoldingProductService extends CommonService<HoldingProduct>{
	
	/**
	 * 理财顾问客户存续产品统计查询
	 * 
	 * @param holdingProduct
	 * @return
	 */
	public Long getCountClientHolding(HoldingProduct holdingProduct) throws Exception {
		return getRelationDao().getCount("holdingProduct.countClientHolding", holdingProduct);
	}
	
	/**
	 * 理财顾问客户存续产品分页查询
	 * 
	 * @param holdingProduct
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HoldingProduct> getPaginatedListClientHolding(HoldingProduct holdingProduct) throws Exception {
		List<HoldingProduct> list = (List<HoldingProduct>) getRelationDao().selectList("holdingProduct.selectPaginatedListClientHolding", holdingProduct);
		return list;
	}

	@Override
	public void save(HoldingProduct entity) throws Exception {
		super.getRelationDao().insert("holdingProduct.insert", entity);
	}

	@Override
	public void update(HoldingProduct entity) throws Exception {
		super.getRelationDao().update("holdingProduct.update_money", entity);
	}

	@Override
	public Long getRecordCount(HoldingProduct entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HoldingProduct> getList(HoldingProduct entity) throws Exception {
		return (List<HoldingProduct>) super.getRelationDao().selectList("holdingProduct.select_list", entity);
	}

	/**
	 * 推送评价问卷时按理财师分组查询存续产品
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HoldingProduct> getListComment(HoldingProduct entity) throws Exception {
		return (List<HoldingProduct>) super.getRelationDao().selectList("holdingProduct.select_list_comment", entity);
	}
	
	@Override
	public List<HoldingProduct> getPaginatedList(HoldingProduct entity) throws Exception {
		return null;
	}

	@Override
	public void delete(HoldingProduct entity) throws Exception {
		this.getRelationDao().delete("holdingProduct.delete", entity);
	}

	@Override
	public void deleteByArr(HoldingProduct entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HoldingProduct getOne(HoldingProduct entity) throws Exception {
		return (HoldingProduct)super.getRelationDao().selectOne("holdingProduct.select_one", entity);
	}
	//查询存续产品信息
	@SuppressWarnings("unchecked")
	public List<HoldingProduct> getHoldingProductList(HoldingProduct entity)throws Exception{
		return (List<HoldingProduct>)super.getRelationDao().selectList("holdingProduct.select_holdingproduct", entity);
	}
	public Long getHoldingProductCount(HoldingProduct entity)throws Exception{
		return super.getRelationDao().getCount("holdingProduct.select_count_holdingproduct", entity);
	}
	@Override
	public Map<String, Object> getGrid(HoldingProduct entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<HoldingProduct> successList = this.getHoldingProductList(entity);
		Long record = getHoldingProductCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", successList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	@Override
	public String upload(HoldingProduct entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object download(HoldingProduct entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 产品ID/客户证件类型/客户证件号码/客户类别/理财顾问ID  获取存续产品的唯一数据
	 * @param holdingProduct
	 * @return
	 * @throws Exception 
	 */
	public HoldingProduct getOneByMidProId(HoldingProduct holdingProduct) throws Exception {
		List<HoldingProduct>  hList=this.getList(holdingProduct);
		HoldingProduct returnHolding=null;
		if(hList==null || hList.size()<=0){
			returnHolding= new HoldingProduct();
		}else{
			returnHolding= hList.get(0);
		}
		return returnHolding;
	}
	
	/**
	 * 根据产口ID获取存续产品
	 * 
	 * @param holdingProduct
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HoldingProduct> getHoldingProductByProductId(HoldingProduct holdingProduct)throws Exception{
		return (List<HoldingProduct>)getRelationDao().selectList("holdingProduct.select_all_fields_by_productid", holdingProduct);
	}
	
	public void deleteByProductId(HoldingProduct holdingProduct) throws Exception{
		this.getRelationDao().delete("holdingProduct.delete_by_productid", holdingProduct);
	}

}
