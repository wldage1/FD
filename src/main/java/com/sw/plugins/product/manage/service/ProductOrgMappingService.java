package com.sw.plugins.product.manage.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.ProductOrgMapping;

@Service
public class ProductOrgMappingService extends CommonService<ProductOrgMapping> {

	@Resource
	private ProductService productService;

	public void saveMapping(ProductOrgMapping entity) throws Exception {
		Product product = new Product();
		Long pid = entity.getProductID();
		boolean flag = false;// 是否有指定的机构
		if (pid != null) {
			this.delete(entity);
			List<ProductOrgMapping> mappList = entity.getMappingList();
			if (mappList != null) {
				for (ProductOrgMapping pm : mappList) {
					if (pm != null) {
						Long orgId = pm.getOrgID();
						Short type = pm.getType();
						if (orgId != null) {
							ProductOrgMapping tpm = new ProductOrgMapping();
							tpm.setProductID(pid);
							tpm.setType(type);
							if(type == (short)2){
								tpm.setOrgID(null);
							}else{
								tpm.setOrgID(orgId);
							}
							this.save(tpm);
							flag = true;
						}
					}
				}
				if(flag){
					product.setIsSpecialProvisionProduct((short) 1);
				}else{
					product.setIsSpecialProvisionProduct((short) 0);
				}
				product.setId(pid);
				productService.updateIsSpecial(product);
			}
		}
	}

	@Override
	public void save(ProductOrgMapping entity) throws Exception {
		super.getRelationDao().insert("productOrgMapping.insert_product_orgmapping", entity);
	}

	@Override
	public void update(ProductOrgMapping entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getRecordCount(ProductOrgMapping entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductOrgMapping> getList(ProductOrgMapping entity) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getListOrg(ProductOrgMapping entity) throws Exception {
		return (List<Long>) super.getRelationDao().selectList("productOrgMapping.select_orgids_byproduct", entity);
	}

	@Override
	public List<ProductOrgMapping> getPaginatedList(ProductOrgMapping entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ProductOrgMapping entity) throws Exception {
		super.getRelationDao().delete("productOrgMapping.delete_product_orgmapping", entity);
	}

	@Override
	public void deleteByArr(ProductOrgMapping entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ProductOrgMapping getOne(ProductOrgMapping entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(ProductOrgMapping entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(ProductOrgMapping entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(ProductOrgMapping entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

}
