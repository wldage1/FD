package com.sw.plugins.extension.youku.category.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.extension.youku.category.entity.Category;

@Service
public class CategoryService extends CommonService<Category>{

	@Override
	public void save(Category entity) throws Exception {
		super.getRelationDao().insert("category.insert", entity);
	}

	@Override
	public void update(Category entity) throws Exception {
		super.getRelationDao().update("category.update", entity);
	}
	
	public void saveorupdate(Category entity)throws Exception{
		if(entity.getId()!=null){
			this.update(entity);
		}else{
			this.save(entity);
		}
	}
	
	@Override
	public Long getRecordCount(Category entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Long getNameCount(Category entity) throws Exception{
		return super.getRelationDao().getCount("category.selectNameCount", entity);
	}
	
	public Long getCodeCount(Category entity) throws Exception{
		return super.getRelationDao().getCount("category.selectCodeCount", entity);
	}
	
	public Long getCountNameByID(Category entity) throws Exception{
		return super.getRelationDao().getCount("category.count_id_Name", entity);
	}
	
	public Long getCountCodeByID(Category entity) throws Exception{
		return super.getRelationDao().getCount("category.count_id_Code", entity);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getList(Category entity) throws Exception {
		return (List<Category>)super.getRelationDao().selectList("category.select", entity);
	}

	@Override
	public List<Category> getPaginatedList(Category entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Category entity) throws Exception {
		super.getRelationDao().delete("category.delete", entity);
	}

	@Override
	public void deleteByArr(Category entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Category getOne(Category entity) throws Exception {
		return (Category)super.getRelationDao().selectOne("category.select", entity);
	}

	@Override
	public Map<String, Object> getGrid(Category entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Category> resultList = getList(entity);
		map.put("rows", resultList);
		return map;
	}

	@Override
	public String upload(Category entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(Category entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
