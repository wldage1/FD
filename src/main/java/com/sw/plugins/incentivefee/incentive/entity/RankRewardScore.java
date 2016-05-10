package com.sw.plugins.incentivefee.incentive.entity;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

//阶段排名奖励分值配置表
public class RankRewardScore extends RelationEntity{

	private static final long serialVersionUID = 1L;
	//名次名称
	@NotEmpty
	private String rankName;
	//名次标识
	@NotEmpty
	private String rankTag;
	//奖励分值
	@Pattern(regexp = "^\\d{1,10}$")
	private String rewardScore;
	//1: 个人  2：机构或团队
	private Short type;
	
	private String stageTime;
	
	public String getStageTime() {
		return stageTime;
	}
	public void setStageTime(String stageTime) {
		this.stageTime = stageTime;
	}
	private List<RankRewardScore> scoreList;
	
	public List<RankRewardScore> getScoreList() {
		return scoreList;
	}
	public void setScoreList(List<RankRewardScore> scoreList) {
		this.scoreList = scoreList;
	}
	public String getRankName() {
		return rankName;
	}
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	public String getRankTag() {
		return rankTag;
	}
	public void setRankTag(String rankTag) {
		this.rankTag = rankTag;
	}
	public String getRewardScore() {
		return rewardScore;
	}
	public void setRewardScore(String rewardScore) {
		this.rewardScore = rewardScore;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
}
