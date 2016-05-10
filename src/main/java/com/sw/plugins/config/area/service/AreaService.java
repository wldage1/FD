package com.sw.plugins.config.area.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.config.area.entity.Area;

/**
 * Service实现类 - Service实现类基类
 */

public class AreaService extends CommonService<Area> {
	@Override
	public void init(InitData initData) throws Exception {
	}

	//查询所有省份或根据省份查询城市
	@SuppressWarnings("unchecked")
	public List<Area> getAllArea(Area entity) throws Exception {
		return (List<Area>) super.getRelationDao().selectList("area.select_area_treeLevel_parentId", entity);
	}
	
	/**
	 * 地区列表树
	 * 
	 * @param nodeid
	 * @return
	 */

	public List<Area> getTree(Area area) throws Exception {
		List<Area> areaList = new ArrayList<Area>();
		areaList = (List<Area>) getList(area);
		return areaList;
	}

	/**
	 * 查看上级地区
	 * 
	 * @param parentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Area> getListByParentId(Area entity) throws Exception {
		return (List<Area>) super.getRelationDao().selectList("area.select_area_parent", entity);
	}

	/**
	 * 地区下拉列表树
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getSelectTree(Area area) throws Exception {
		List<Object> list = new ArrayList<Object>();
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Area> areaList = new ArrayList<Area>();
		Area newArea;
		if (area.getParentId() == null) {
			newArea = new Area();
			newArea.setParentId(0L);
			areaList = (List<Area>) getList(newArea);
			Map<String, Object> maparea = new Hashtable<String, Object>();
			maparea.put("id", "");
			maparea.put("name", area.getParentName());
			maparea.put("pId", 0);
			maparea.put("path", "");
			maparea.put("olevel", 0);
			maparea.put("childNode", 1);
			maparea.put("isParent", "false");
			list.add(maparea);
		} else {
			newArea = new Area();
			newArea.setParentId(area.getParentId());
			areaList = (List<Area>) getList(newArea);
		}
		if (areaList != null && areaList.size() > 0) {
			for (Area condArea : areaList) {
				Map<String, Object> maparea = new Hashtable<String, Object>();
				maparea.put("id", condArea.getId());
				maparea.put("name", condArea.getName());
				maparea.put("pId", condArea.getParentId());
				maparea.put("path", condArea.getTreePath());
				maparea.put("olevel", condArea.getTreeLevel());
				maparea.put("childNode", condArea.getIsChildNode());
				if (condArea.getIsChildNode() == 1) {
					maparea.put("isParent", "false");
				} else {
					maparea.put("isParent", "true");
				}
				list.add(maparea);
			}
		}
		map.put("stree", list);
		return map;
	}

	/**
	 * 保存或更新
	 * 
	 * @param area
	 * @throws Exception
	 */
	public void saveOrUpdate(Area area) throws Exception {
		// 如果id为空表示添加
		if (area.getId() == null) {
			// 将当前地区的级别+1
			area.setTreeLevel(area.getTreeLevel() == null ? 1 : (area.getTreeLevel() + 1));
			// 设置为子节点
			area.setIsChildNode(1l);
			// 插入地区数据
			save(area);
			// 修改当前地区的id
			area.setId(area.getGeneratedKey());
			// 设置path
			String path = (area.getTreePath() == null || area.getTreePath().equals("")) ? "," + area.getGeneratedKey() + "," : area.getTreePath() + area.getGeneratedKey() + ",";
			// 修改当前地区path
			area.setTreePath(path);
			// 如果上级地区id为空，则直接设置上级地区id为0，地区级别为1
			if (area.getParentId() == null) {
				area.setParentId(0L);
			} else {
				// 如果IsChildNode已经为0，则不用修改
				if (area.getIsChildNode() != 0) {
					// 如果选择了上级地区，修改上级地区的IsChildNode属性为0
					Area temparea = new Area();
					temparea.setId(area.getParentId());
					temparea.setIsChildNode(0l);
					update(temparea);
				}
			}
			update(area);

		} else {
			// 修改地区配置
			update(area);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getList(Area entity) throws Exception {
		return (List<Area>) super.getRelationDao().selectList("area.select_area", entity);
	}

	public Long getCount(Area entity) throws Exception {
		return super.getRelationDao().getCount("area.count", entity);
	}

	public Long getCountByParentId(Area entity) throws Exception {
		return super.getRelationDao().getCount("area.count_by_parentId", entity);
	}

	@Override
	public void save(Area entity) throws Exception {
		super.getRelationDao().insert("area.insert", entity);

	}

	@Override
	public void update(Area entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("area.update", entity);
	}

	@Override
	public Long getRecordCount(Area entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Area> getPaginatedList(Area entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByArr(Area entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Area getOne(Area entity) throws Exception {
		// TODO Auto-generated method stub
		return (Area) super.getRelationDao().selectOne("area.select", entity);
	}

	public Area getArea(Area entity) throws Exception {
		// TODO Auto-generated method stub
		return (Area) super.getRelationDao().selectOne("area.select_area", entity);
	}

	@Override
	public Map<String, Object> getGrid(Area entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(Area entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(Area entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 判断是否有下级地区
	 * 
	 * @param area
	 * @throws Exception
	 */
	public Long getParentAreaCount(Area entity) throws Exception {
		return super.getRelationDao().getCount("area.count_area_parent", entity);
	}

	/**
	 * 地区删除
	 * 
	 * @param area
	 * @throws Exception
	 */
	public void delete(Area area) throws Exception {
		area = getOne(area);
		super.getRelationDao().delete("area.delete", area);
		Area condArea = new Area();
		condArea.setParentId(area.getParentId());
		List<Area> list = getList(condArea);
		if (list.size() <= 0) {
			condArea = new Area();
			condArea.setId(area.getParentId());
			Area updateArea = getOne(condArea);
			if (updateArea != null) {
				updateArea.setIsChildNode(1l);
				update(updateArea);
			}
		}
	}

}