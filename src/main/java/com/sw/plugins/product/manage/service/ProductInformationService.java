package com.sw.plugins.product.manage.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.manage.entity.ProductInformation;
import com.sw.plugins.websitemanage.information.entity.Information;
import com.sw.plugins.websitemanage.information.service.InformationService;

public class ProductInformationService extends CommonService<ProductInformation> {

	@Resource
	private InformationService informationService;

	/**
	 * 获取产品绑定资讯列表集合
	 * 
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGrid(Information information) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Information> resultList = informationService.selectRelateProduct(information);
		Long record = informationService.selectRelateProductCount(information);
		int pageCount = (int) Math.ceil(record / (double) information.getRows());
		map.put("rows", resultList);
		map.put("page", information.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	@Override
	public void save(ProductInformation entity) throws Exception {
		super.getRelationDao().insert("productInformation.insert", entity);
	}

	/**
	 * 修改映射
	 * 
	 * @param productInformation
	 */
	@Override
	public void update(ProductInformation productInformation) throws Exception {
		if (getOne(productInformation) == null) {
			save(productInformation);
		} else {
			getRelationDao().update("productInformation.update", productInformation);
		}
	}

	@Override
	public Long getRecordCount(ProductInformation entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductInformation> getList(ProductInformation entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductInformation> getPaginatedList(ProductInformation entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ProductInformation entity) throws Exception {
		super.getRelationDao().delete("productInformation.delete", entity);
	}

	/**
	 * 通过资讯ID删除映射
	 * 
	 * @param productInformation
	 * @throws Exception
	 */
	public void deleteByInformationId(ProductInformation productInformation) throws Exception {
		super.getRelationDao().delete("productInformation.deleteByInformationId", productInformation);
	}

	@Override
	public void deleteByArr(ProductInformation entity) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 通过资讯ID获取单个映射
	 */
	@Override
	public ProductInformation getOne(ProductInformation entity) throws Exception {
		return (ProductInformation) getRelationDao().selectOne("productInformation.selectOne", entity);
	}

	@Override
	public Map<String, Object> getGrid(ProductInformation entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(ProductInformation entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductInformation entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
	}

	/**
	 * 保存产品资讯映射关系
	 * 
	 * @param productInformation
	 * @throws Exception
	 */
	public void saveInformation(ProductInformation productInformation) throws Exception {
		ProductInformation productInformation2 = null;
		JSONArray s = JSONArray.fromObject(productInformation.getInfromatinIds());
		Object[] obj = s.toArray();
		for (Object id : obj) {
			productInformation2 = new ProductInformation();
			productInformation2.setProductID(productInformation.getId());
			productInformation2.setInformationID(Long.parseLong(id.toString()));
			List<ProductInformation> list = this.getInformationList(productInformation2);
			if (list == null || list.size() <= 0) {
				this.save(productInformation2);
			}
		}
	}

	/**
	 * 取消产品资讯映射关系
	 * 
	 * @param productInformation
	 * @throws Exception
	 */
	public void deleteInformation(ProductInformation productInformation) throws Exception {
		ProductInformation productInformation2 = null;
		JSONArray s = JSONArray.fromObject(productInformation.getInfromatinIds());
		Object[] obj = s.toArray();
		for (Object id : obj) {
			productInformation2 = new ProductInformation();
			productInformation2.setProductID(productInformation.getId());
			productInformation2.setInformationID(Long.parseLong(id.toString()));
			this.delete(productInformation2);
		}
	}

	@SuppressWarnings("unchecked")
	private List<ProductInformation> getInformationList(ProductInformation productInformation) throws Exception {
		return (List<ProductInformation>) super.getRelationDao().selectList("productInformation.select_unbind", productInformation);
	}

}
