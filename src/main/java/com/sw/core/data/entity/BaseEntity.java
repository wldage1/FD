package com.sw.core.data.entity;

import java.io.Serializable;
import java.util.Map;

import com.sw.core.common.Constant;
import com.sw.core.data.dbholder.CreatorContextHolder;
import com.sw.core.data.dbholder.DatabaseTypeContextHolder;
import com.sw.util.JqGridUtil;

public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 2093354974074612179L;

	/** 创建时间 */
	private String createTime;
	/** 修改时间 */
	private String modifyTime;

	/** 创建者 '-1' 默认超级管理员 */
	private String creatorUserId;

	/** 设置下页需要跳转的CODE */
	private String turnCode;

	/** 设置是否覆盖参数 0:不覆盖1：覆盖 */
	private String coverParam;

	/** 设置本次跳转是否回退参数 */
	private String back;

	/** 系统通用code代码 */
	private String c;
	/** 页面验证操作类别 */
	private String operate;
	/** 数据库类型 */
	private String databaseType;
	/** 系统内所有资源都在某一个机构范围内 */
	/** 适用于这对多个企业（个人）的公共系统 */
	private String orgIdPath;
	/** 提示信息替换字段配置 */
	public String prompt;
	/** 页面参数集 */
	private Map<String, String> params;
	// 列表传递参数
	/** 表示请求页码（当前页）的参数名称 */
	private int page;
	/** 表示请求记录数的参数名称 */
	private int rows;
	/** 表示分页起始数的参数名称 **/
	private int start;
	/** 表示分页偏移量 **/
	private int offset;
	/** 表示用于排序的列名的参数名称 */
	private String sidx;
	/** 表示采用的排序方式的参数名称 */
	private String sord;
	/** mongo分页时，每页最后一条记录主键值 */
	private String lastKey;
	/** 表示是否是搜索请求的参数名称 */
	private String _search;
	/** 表示已经发送请求的次数的参数名称 */
	private String nd;
	/** 列表节点id */
	private String nodeid;
	/** 异常错误信息 **/
	private String errorMsg;
	/** 数据记录数 */
	private Long record;
	/** jqgrid查询条件JSON串 **/
	private String filters;
	/** jqgrid查询条件JSON串 **/
	private String gridSearch;
	
	
	/** 是否是居间公司 */
	private int isCommission;

	public BaseEntity() {
		this.setCreatorUserId(null);
		this.setDatabaseType(null);
	}

	public String getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(String creator) {
		if (creator == null || creator.equals("")) {
			String creatorTemp = CreatorContextHolder.getCreatorContext();
			if (creatorTemp != null) {
				this.creatorUserId = creatorTemp;
			}
		} else {
			this.creatorUserId = creator;
		}
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getOrgIdPath() {
		return orgIdPath;
	}

	public void setOrgIdPath(String orgIdPath) {
		this.orgIdPath = orgIdPath;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String get_search() {
		return _search;
	}

	public void set_search(String _search) {
		this._search = _search;
	}

	public String getNd() {
		return nd;
	}

	public void setNd(String nd) {
		this.nd = nd;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		if (databaseType == null || databaseType.equals("")) {
			String datebaseType = DatabaseTypeContextHolder.getDatabaseType();
			if (datebaseType != null && !datebaseType.equals("")) {
				this.databaseType = datebaseType;
			} else {
				this.databaseType = Constant.DEFAULT_DATABASE_TYPE;
			}
		} else {
			this.databaseType = databaseType;
		}
	}

	public int getStart() {
		if (this.databaseType != null) {
			// 根据数据库类型计算分页起始值
			if (this.databaseType.equals(Constant.DATASOURCE_MYSQL)) {
				this.start = (this.getPage() - 1) * this.getRows();
			} else if (this.databaseType.equals(Constant.DATASOURCE_ORACLE)) {
				this.start = (this.getPage() - 1) * this.getRows() + 1;
			} else if (this.databaseType.equals(Constant.DATASOURCE_SQLSERVER)) {
				this.start = (this.getPage() - 1) * this.getRows();
			} else {
				this.start = (this.getPage() - 1) * this.getRows();
			}
		}
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getOffset() {
		if (this.databaseType != null) {
			// 根据数据库类型计算分页偏移量（跳跃记录数）
			if (this.databaseType.equals(Constant.DATASOURCE_MYSQL)) {
				this.offset = this.getRows();
			} else if (this.databaseType.equals(Constant.DATASOURCE_ORACLE)) {
				this.offset = this.getPage() * this.getRows();
			} else if (this.databaseType.equals(Constant.DATASOURCE_SQLSERVER)) {
				this.offset = this.getPage() * this.getRows() + 1;
			} else {
				this.offset = this.getRows();
			}
		}
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getLastKey() {
		return lastKey;
	}

	public void setLastKey(String lastKey) {
		this.lastKey = lastKey;
	}

	public Long getRecord() {
		return record;
	}

	public void setRecord(Long record) {
		this.record = record;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getGridSearch() {
		if (null != filters && !"".equals(filters)) {
			gridSearch = JqGridUtil.constructWhere(filters).toString();
		}
		return gridSearch;
	}

	public void setGridSearch(String gridSearch) {
		this.gridSearch = gridSearch;
	}

	public String getCoverParam() {
		return coverParam;
	}

	public void setCoverParam(String coverParam) {
		this.coverParam = coverParam;
	}

	public String getTurnCode() {
		return turnCode;
	}

	public void setTurnCode(String turnCode) {
		this.turnCode = turnCode;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public int getIsCommission() {
		return isCommission;
	}

	public void setIsCommission(int isCommission) {
		this.isCommission = isCommission;
	}
	
	

}
