package com.sw.plugins.incentivefee.incentive.entity;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

//返利参数配置表
public class RebateParameter extends RelationEntity{

	private static final long serialVersionUID = 1L;
	
	//等级名称
	@NotEmpty
	private String levelName;
	//等级标识
	@NotEmpty
	private String levelTag;
	//1:个人，2：机构或团队
	private Short type;
	//阶段分值（万）
	@Pattern(regexp = "^\\d{1,10}$")
	private String stageScore;
	//服务费用比例
	@NotNull
	private String serviceFeeRat;
	
	private String stageTime;
	
	private List<RebateParameter> stageList;
	
	public List<RebateParameter> getStageList() {
		return stageList;
	}
	public void setStageList(List<RebateParameter> stageList) {
		this.stageList = stageList;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getLevelTag() {
		return levelTag;
	}
	public void setLevelTag(String levelTag) {
		this.levelTag = levelTag;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public String getStageScore() {
		return stageScore;
	}
	public void setStageScore(String stageScore) {
		this.stageScore = stageScore;
	}
	public String getServiceFeeRat() {
		return serviceFeeRat;
	}
	public void setServiceFeeRat(String serviceFeeRat) {
		this.serviceFeeRat = serviceFeeRat;
	}
	public String getStageTime() {
		return stageTime;
	}
	public void setStageTime(String stageTime) {
		this.stageTime = stageTime;
	}
}
