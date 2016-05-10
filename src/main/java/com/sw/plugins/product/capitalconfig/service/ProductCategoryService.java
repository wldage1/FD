package com.sw.plugins.product.capitalconfig.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.support.DelegatingMessageSource;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.product.capitalconfig.entity.ProductCategory;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.service.ProductService;

public class ProductCategoryService extends CommonService<ProductCategory> {

	@Resource
	private ProductService productService;
	@Resource
	public DelegatingMessageSource messageSource;

	/**
	 * 获取产品分类信息json
	 * 
	 * @author baokai Created on :2013-2-28 上午11:03:26
	 */
	@Override
	public Map<String, Object> getGrid(ProductCategory entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<ProductCategory> resultList = null;
		resultList = getList(entity);
		map.put("rows", resultList);
		return map;
	}

	/**
	 * 获取下拉树列表
	 * 
	 * @author baokai Created on :2013-2-28 下午3:35:42
	 */
	public Map<String, Object> getSelectTree(ProductCategory productCategory) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Object> list = new ArrayList<Object>();
		if (null == productCategory.getParentId()) {
			Map<String, Object> pcmap = new Hashtable<String, Object>();
			String topMessage = messageSource.getMessage("product.category.topmessage", null, Locale.CHINA);
			pcmap.put("id", "0");
			pcmap.put("name", topMessage);
			pcmap.put("pId", "0");
			pcmap.put("isParent", "true");
			list.add(pcmap);
		}
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		ProductCategory prdCategory;
		if (productCategory.getParentId() == null) {
			prdCategory = new ProductCategory();
			prdCategory.setParentId(0L);
			productCategoryList = getList(prdCategory);
		} else {
			prdCategory = new ProductCategory();
			prdCategory.setParentId(productCategory.getParentId());
			productCategoryList = getList(prdCategory);
		}

		if (productCategoryList != null && productCategoryList.size() > 0) {
			for (ProductCategory pc : productCategoryList) {
				Map<String, Object> mappc = new Hashtable<String, Object>();
				mappc.put("id", pc.getId());
				mappc.put("name", pc.getName());
				mappc.put("pId", pc.getParentId());
				mappc.put("path", pc.getTreePath());
				mappc.put("olevel", pc.getTreeLevel());
				mappc.put("childNode", pc.getIsChildNode());
				if (pc.getIsChildNode() == 1) {
					mappc.put("isParent", "false");
				} else {
					mappc.put("isParent", "true");
				}
				list.add(mappc);
			}
		}
		map.put("stree", list);
		return map;
	}

	/**
	 * 保存或 修改产品分类
	 * 
	 * @author baokai Created on :2013-2-28 下午2:22:14
	 */
	public void saveOrUpdate(ProductCategory productCategory) throws Exception {
		if (productCategory.getId() == null) {
			// 将当前机构的级别+1
			productCategory.setTreeLevel(productCategory.getTreeLevel() == null ? 1 : (productCategory.getTreeLevel() + 1));
			// 设置为子节点
			productCategory.setIsChildNode(1);
			// 插入机构数据
			save(productCategory);
			// 修改当前机构的id
			productCategory.setId(productCategory.getGeneratedKey());
			// 设置path
			String path = (productCategory.getTreePath() == null || productCategory.getTreePath().equals("")) ? "," + productCategory.getGeneratedKey() + "," : productCategory.getTreePath() + productCategory.getGeneratedKey() + ",";
			// 修改当前机构path
			productCategory.setTreePath(path);

			// 如果上级机构id为空，则直接设置上级机构id为0，机构级别为1
			if (productCategory.getParentId() == null) {
				productCategory.setParentId(0L);
			} else {
				// 如果ChildNode已经为0，则不用修改
				if (productCategory.getIsChildNode() != 0) {
					// 如果选择了上级机构，修改上级机构的ChildNode属性为0
					ProductCategory tempProductCategory = new ProductCategory();
					tempProductCategory.setId(productCategory.getParentId());
					tempProductCategory.setIsChildNode(0);
					update(tempProductCategory);
				}
			}
			update(productCategory);
		} else {
			update(productCategory);
		}
	}

	/**
	 * 删除产品分类
	 * 
	 * @author baokai Created on :2013-3-1 下午3:11:13
	 */
	public boolean deleteCategory(ProductCategory productCategory) throws Exception {
		// 判断该分类下是否存在产品
		Product p = new Product();
		p.setInvestCategoryId(productCategory.getId());
		long count = productService.getRecordCountFromAllProduct(p);
		if (count > 0) {
			return false;
		} else {
			if (productCategory != null && productCategory.getId() != null) {
				productCategory = getOne(productCategory);
				if (productCategory != null) {
					delete(productCategory);
					ProductCategory condProductCategory = new ProductCategory();
					condProductCategory.setParentId(productCategory.getParentId());
					List<ProductCategory> list = getList(condProductCategory);
					if (list.size() <= 0) {
						condProductCategory = new ProductCategory();
						condProductCategory.setId(productCategory.getParentId());
						ProductCategory pc = getOne(condProductCategory);
						if (pc != null) {
							pc.setIsChildNode(1);
							update(pc);
						}
					}
				}
			}
			return true;
		}
	}

	/**
	 * 对产品分类标识的效验
	 * 
	 * @param entity
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public long checkCode(ProductCategory entity, HttpServletRequest request) throws Exception {
		return (long) super.getRelationDao().getCount("productCategory.check_code", entity);
	}

	@Override
	public String upload(ProductCategory entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(ProductCategory entity) throws Exception {
		getRelationDao().insert("productCategory.insert", entity);
	}

	@Override
	public Long getRecordCount(ProductCategory entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductCategory> getPaginatedList(ProductCategory entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByArr(ProductCategory entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Object download(ProductCategory entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(ProductCategory entity) throws Exception {
		getRelationDao().update("productCategory.update", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductCategory> getList(ProductCategory entity) throws Exception {
		return (List<ProductCategory>) getRelationDao().selectList("productCategory.select", entity);
	}

	@Override
	public ProductCategory getOne(ProductCategory entity) throws Exception {
		return (ProductCategory) getRelationDao().selectOne("productCategory.select", entity);
	}

	@Override
	public void delete(ProductCategory entity) throws Exception {
		getRelationDao().delete("productCategory.delete", entity);
	}

}