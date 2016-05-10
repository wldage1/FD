package com.sw.core.initialize;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.sw.core.common.Constant;
import com.sw.plugins.config.position.entity.Position;

public class InitData {

	public List<Map<String, Object>> organizations;
	public Map<String, Object> taskFactory;
	public Map<String, Object> user;
	public Map<String, Object> pageShowType;
	public Map<String, Object> modelFieldShowType;
	public Map<String, Object> charsetType;
	public Map<String, Object> payProgress;
	public List<Map<String, Object>> tradeStatus;
	public Map<String, Object> docStatus;
	public Map<String, Object> documentStatus;
	public Map<String, Object> payStatus;
	public Map<String, Object> exportStatus;
	public Map<String, Object> clientType;
	public Map<String, Object> clientStatus;
	public List<Map<String, Object>> tradeType;
	public Map<String, Object> pushStatus;
	public Map<String, Object> proofStatus;// 打款凭证处理状态
	public Map<String, Object> proofType;// 资金确认处理类型
	public Map<String, Object> operateType;// 资金确认操作类型

	// 理财师机构与理财师状态类型等初始化
	public Map<String, Object> teamOrOrgStatus;
	public Map<String, Object> teamOrOrgDelStatus;
	public Map<String, Object> teamOrOrgApplicationType;
	public Map<String, Object> teamOrOrgApplicationStatus;
	public Map<String, Object> memberApplicationType;
	public Map<String, Object> memberApplicationStatus;
	public Map<String, Object> memberStatus;
	public Map<String, Object> memberDelStatus;
	public Map<String, Object> memberGender;
	public Map<String, Object> memberChangeApplicationType;
	public Map<String, Object> memberChangeApplicationStatus;
	public Map<String, Object> memberIDCardType;
	public Map<String, Object> memberType;

	public Map<String, Object> providerUserAuthorization;
	public Map<String, Object> providerUserGender;
	public Map<String, Object> providerUserStatus;
	public Map<String, Object> agreementStatus;
	public Map<String, Object> contractStatus;
	public Map<String, Object> contractType;
	//全局系统参数
	public Map<String,Object> globalParameter;
	public Map<String,Object> stageParameter;
	public Map<String,Object> stageParameterName;
	
	public Map<String,Object> raiseArea;// 募集方所在区域
	

	public Map<String, Object> getExportStatus() {
		return exportStatus;
	}
	public void setExportStatus(Map<String, Object> exportStatus) {
		this.exportStatus = exportStatus;
	}
	public Map<String, Object> getContractStatus() {
		return contractStatus;
	}
	public void setContractStatus(Map<String, Object> contractStatus) {
		this.contractStatus = contractStatus;
	}
	public Map<String, Object> getContractType() {
		return contractType;
	}
	public void setContractType(Map<String, Object> contractType) {
		this.contractType = contractType;
	}
	public Map<String, Object> getAgreementStatus() {
		return agreementStatus;
	}
	public void setAgreementStatus(Map<String, Object> agreementStatus) {
		this.agreementStatus = agreementStatus;
	}

	/** 产品中心 **/
	/** 产品类型 */
	public Map<String, Object> productType;
	/** 预期收益类型 */
	public Map<String, Object> profitType;
	/** 期限类型 */
	public Map<String, Object> deadlineType;
	/** 销售类型 */
	public Map<String, Object> sellType;
	/** 产品状态 */
	public Map<String, Object> productStatus;
	/** 产品销售状态 */
	public Map<String, Object> sellStatus;
	/** 产品封闭类型 (私募) */
	public Map<String, Object> closeType;
	/** 产品发布状态 */
	public Map<String, Object> released;
	/** 佣金支付方式 */
	public Map<String, Object> payWay;
	/** 产品资料下载权限 */
	public Map<String, Object> downloadPermission;
	/** 小额度投资人限制条件 */
	public Map<String, Object> investLimit;

	/** 网站 设置 **/
	public List<Position> sitePosition; // 网站位置
	public Map<String,Object> websiteSetting; //网站设置

	/** 资讯中心参数初始化 */
	private List<Map<String, Object>> informationType; // 资讯类别
	private Map<String, Object> informationReleased; // 资讯是否发布
	/** 活动中心参数初始化 */
	private List<Map<String, Object>> activityType; // 活动类别
	private Map<String, Object> activityStatus; // 活动状态
	/** 产品评价初始化 */
	private Map<String, Object> productEvaluate;//产品评价指标
	/** 产品问卷推送状态 */
	private Map<String, Object> productPushStatus;
	/** 银行流水初始化*/
	private Map<String, Object> matchingStatus;//匹配状态
	public InitData(){
		
		String configFile = "openOffice";
		ResourceBundle rb = ResourceBundle.getBundle(configFile); 
		//通过哪一种方式进行转换  1:openoffice  2:libreoffice
		Constant.TRANSFORTYPEFLAG_KEY = rb.getString("orm.transforTypeFlag");
		//获得pdf转换jar包路径
		Constant.PDFJARPATH_KEY = rb.getString("orm.pdfJarPath");
		//获得swf服务地址
		Constant.SWFTOOLSPATH_KEY = rb.getString("orm.swfToolsPath");
		//获得能够转换的文件类型
		Constant.ONLINEREADFILETYPE_KEY = rb.getString("orm.onlineReadFileType");
		
		System.out.println(organizations);
	}
	public void setProviderUserAuthorization(Map<String, Object> providerUserAuthorization) {
		this.providerUserAuthorization = providerUserAuthorization;
	}

	public Map<String, Object> getProviderUserAuthorization() {
		return providerUserAuthorization;

	}

	public Map<String, Object> getProviderUserGender() {
		return providerUserGender;
	}

	public void setProviderUserGender(Map<String, Object> providerUserGender) {
		this.providerUserGender = providerUserGender;
	}

	public Map<String, Object> getProviderUserStatus() {
		return providerUserStatus;
	}

	public void setProviderUserStatus(Map<String, Object> providerUserStatus) {
		this.providerUserStatus = providerUserStatus;
	}

	public Map<String, Object> getMemberType() {
		return memberType;
	}

	public void setMemberType(Map<String, Object> memberType) {
		this.memberType = memberType;
	}

	public Map<String, Object> providerStatus;
	public Map<String, Object> providerDelStatus;
	public Map<String, Object> clientMarried;

	// 字典配置(初始化到字典表)
	private Map<String, String> dictionaryConfig;
	// 产品预期收益率（初始化到字典条目表）
	private Map<String, Object> profitRatio;
	// 产品期限（初始化到字典条目表）
	private Map<String, Object> deadline;
	
	// 机构类型（初始化到字典条目表）
	private Map<String, Object> orgType;

	// 理财师机构与理财师状态类型等初始化
	//订单导出上传路径
	// 上传文件目录
	private String uploadDir;
	// 上传文件临时目录
	private String uploadTmpDir;

	public String getUploadDir() {
		return uploadDir;
	}
	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
	public String getUploadTmpDir() {
		return uploadTmpDir;
	}
	public void setUploadTmpDir(String uploadTmpDir) {
		this.uploadTmpDir = uploadTmpDir;
	}
	public Map<String, Object> getMemberApplicationType() {
		return memberApplicationType;
	}

	public Map<String, Object> getTeamOrOrgStatus() {
		return teamOrOrgStatus;
	}

	public void setTeamOrOrgStatus(Map<String, Object> teamOrOrgStatus) {
		this.teamOrOrgStatus = teamOrOrgStatus;
	}

	public Map<String, Object> getTeamOrOrgDelStatus() {
		return teamOrOrgDelStatus;
	}

	public void setTeamOrOrgDelStatus(Map<String, Object> teamOrOrgDelStatus) {
		this.teamOrOrgDelStatus = teamOrOrgDelStatus;
	}

	public Map<String, Object> getTeamOrOrgApplicationType() {
		return teamOrOrgApplicationType;
	}

	public void setTeamOrOrgApplicationType(Map<String, Object> teamOrOrgApplicationType) {
		this.teamOrOrgApplicationType = teamOrOrgApplicationType;
	}

	public Map<String, Object> getTeamOrOrgApplicationStatus() {
		return teamOrOrgApplicationStatus;
	}

	public void setTeamOrOrgApplicationStatus(Map<String, Object> teamOrOrgApplicationStatus) {
		this.teamOrOrgApplicationStatus = teamOrOrgApplicationStatus;
	}

	public void setMemberApplicationType(Map<String, Object> memberApplicationType) {
		this.memberApplicationType = memberApplicationType;
	}

	public Map<String, Object> getMemberApplicationStatus() {
		return memberApplicationStatus;
	}

	public void setMemberApplicationStatus(Map<String, Object> memberApplicationStatus) {
		this.memberApplicationStatus = memberApplicationStatus;
	}

	public Map<String, Object> getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(Map<String, Object> memberStatus) {
		this.memberStatus = memberStatus;
	}

	public Map<String, Object> getMemberDelStatus() {
		return memberDelStatus;
	}

	public void setMemberDelStatus(Map<String, Object> memberDelStatus) {
		this.memberDelStatus = memberDelStatus;
	}

	public Map<String, Object> getMemberGender() {
		return memberGender;
	}

	public void setMemberGender(Map<String, Object> memberGender) {
		this.memberGender = memberGender;
	}

	public Map<String, Object> getMemberChangeApplicationType() {
		return memberChangeApplicationType;
	}

	public void setMemberChangeApplicationType(Map<String, Object> memberChangeApplicationType) {
		this.memberChangeApplicationType = memberChangeApplicationType;
	}

	public Map<String, Object> getMemberChangeApplicationStatus() {
		return memberChangeApplicationStatus;
	}

	public void setMemberChangeApplicationStatus(Map<String, Object> memberChangeApplicationStatus) {
		this.memberChangeApplicationStatus = memberChangeApplicationStatus;
	}

	public Map<String, Object> getMemberIDCardType() {
		return memberIDCardType;
	}

	public void setMemberIDCardType(Map<String, Object> memberIDCardType) {
		this.memberIDCardType = memberIDCardType;
	}

	public List<Map<String, Object>> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Map<String, Object>> organizations) {
		this.organizations = organizations;
	}

	public Map<String, Object> getTaskFactory() {
		return taskFactory;
	}

	public void setTaskFactory(Map<String, Object> taskFactory) {
		this.taskFactory = taskFactory;
	}

	public Map<String, Object> getUser() {
		return user;
	}

	public void setUser(Map<String, Object> user) {
		this.user = user;
	}

	public Map<String, Object> getPageShowType() {
		return pageShowType;
	}

	public void setPageShowType(Map<String, Object> pageShowType) {
		this.pageShowType = pageShowType;
	}

	public Map<String, Object> getModelFieldShowType() {
		return modelFieldShowType;
	}

	public void setModelFieldShowType(Map<String, Object> modelFieldShowType) {
		this.modelFieldShowType = modelFieldShowType;
	}

	public Map<String, Object> getCharsetType() {
		return charsetType;
	}

	public void setCharsetType(Map<String, Object> charsetType) {
		this.charsetType = charsetType;
	}

	public Map<String, Object> getProductType() {
		return productType;
	}

	public void setProductType(Map<String, Object> productType) {
		this.productType = productType;
	}

	public Map<String, Object> getPayProgress() {
		return payProgress;
	}

	public void setPayProgress(Map<String, Object> payProgress) {
		this.payProgress = payProgress;
	}

	public List<Map<String, Object>> getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(List<Map<String, Object>> tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Map<String, Object> getDocStatus() {
		return docStatus;
	}
	
	public void setDocStatus(Map<String, Object> docStatus) {
		this.docStatus = docStatus;
	}
	
	public Map<String, Object> getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(Map<String, Object> documentStatus) {
		this.documentStatus = documentStatus;
	}

	public Map<String, Object> getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Map<String, Object> payStatus) {
		this.payStatus = payStatus;
	}

	public Map<String, Object> getClientType() {
		return clientType;
	}

	public void setClientType(Map<String, Object> clientType) {
		this.clientType = clientType;
	}

	public Map<String, Object> getClientStatus() {
		return clientStatus;
	}
	public void setClientStatus(Map<String, Object> clientStatus) {
		this.clientStatus = clientStatus;
	}

	public List<Map<String, Object>> getTradeType() {
		return tradeType;
	}

	public void setTradeType(List<Map<String, Object>> tradeType) {
		this.tradeType = tradeType;
	}

	public Map<String, Object> getProviderStatus() {
		return providerStatus;
	}

	public void setProviderStatus(Map<String, Object> providerStatus) {
		this.providerStatus = providerStatus;
	}

	public Map<String, Object> getProviderDelStatus() {
		return providerDelStatus;
	}

	public void setProviderDelStatus(Map<String, Object> providerDelStatus) {
		this.providerDelStatus = providerDelStatus;
	}

	public Map<String, Object> getClientMarried() {
		return clientMarried;
	}

	public void setClientMarried(Map<String, Object> clientMarried) {
		this.clientMarried = clientMarried;
	}

	public Map<String, Object> getProfitType() {
		return profitType;
	}

	public void setProfitType(Map<String, Object> profitType) {
		this.profitType = profitType;
	}

	public Map<String, Object> getDeadlineType() {
		return deadlineType;
	}

	public void setDeadlineType(Map<String, Object> deadlineType) {
		this.deadlineType = deadlineType;
	}

	public Map<String, Object> getSellType() {
		return sellType;
	}

	public void setSellType(Map<String, Object> sellType) {
		this.sellType = sellType;
	}

	public Map<String, Object> getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(Map<String, Object> productStatus) {
		this.productStatus = productStatus;
	}

	public Map<String, Object> getSellStatus() {
		return sellStatus;
	}

	public void setSellStatus(Map<String, Object> sellStatus) {
		this.sellStatus = sellStatus;
	}

	public Map<String, Object> getCloseType() {
		return closeType;
	}

	public void setCloseType(Map<String, Object> closeType) {
		this.closeType = closeType;
	}

	public Map<String, Object> getReleased() {
		return released;
	}

	public void setReleased(Map<String, Object> released) {
		this.released = released;
	}

	public Map<String, Object> getPayWay() {
		return payWay;
	}

	public void setPayWay(Map<String, Object> payWay) {
		this.payWay = payWay;
	}

	public Map<String, Object> getDownloadPermission() {
		return downloadPermission;
	}

	public void setDownloadPermission(Map<String, Object> downloadPermission) {
		this.downloadPermission = downloadPermission;
	}

	public Map<String, Object> getProfitRatio() {
		return profitRatio;
	}

	public void setProfitRatio(Map<String, Object> profitRatio) {
		this.profitRatio = profitRatio;
	}

	public Map<String, Object> getDeadline() {
		return deadline;
	}

	public void setDeadline(Map<String, Object> deadline) {
		this.deadline = deadline;
	}

	public Map<String, Object> getOrgType() {
		return orgType;
	}

	public void setOrgType(Map<String, Object> orgType) {
		this.orgType = orgType;
	}

	public List<Position> getSitePosition() {
		return sitePosition;
	}

	public void setSitePosition(List<Position> sitePosition) {
		this.sitePosition = sitePosition;
	}
	
	public Map<String, Object> getWebsiteSetting() {
		return websiteSetting;
	}
	public void setWebsiteSetting(Map<String, Object> websiteSetting) {
		this.websiteSetting = websiteSetting;
	}
	
	public Map<String, Object> getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Map<String, Object> pushStatus) {
		this.pushStatus = pushStatus;
	}

	public Map<String, String> getDictionaryConfig() {
		return dictionaryConfig;
	}

	public void setDictionaryConfig(Map<String, String> dictionaryConfig) {
		this.dictionaryConfig = dictionaryConfig;
	}

	/** 资讯中心参数初始化 */
	public List<Map<String, Object>> getInformationType() {
		return informationType;
	}
	public void setInformationType(List<Map<String, Object>> informationType) {
		this.informationType = informationType;
	}
	public Map<String, Object> getInformationReleased() {
		return informationReleased;
	}
	public void setInformationReleased(Map<String, Object> informationReleased) {
		this.informationReleased = informationReleased;
	}
	/** 活动中心参数初始化 */
	public List<Map<String, Object>> getActivityType() {
		return activityType;
	}
	public void setActivityType(List<Map<String, Object>> activityType) {
		this.activityType = activityType;
	}
	public Map<String, Object> getActivityStatus() {
		return activityStatus;
	}
	public void setActivityStatus(Map<String, Object> activityStatus) {
		this.activityStatus = activityStatus;
	}
	/** 产品评价参数初始化 */
	public Map<String, Object> getProductEvaluate() {
		return productEvaluate;
	}
	public void setProductEvaluate(Map<String, Object> productEvaluate) {
		this.productEvaluate = productEvaluate;
	}
	/** 产品问卷推送状态 */
	public Map<String, Object> getProductPushStatus() {
		return productPushStatus;
	}
	public void setProductPushStatus(Map<String, Object> productPushStatus) {
		this.productPushStatus = productPushStatus;
	}
	/** 银行流水匹配状态初始化*/
	public Map<String, Object> getMatchingStatus() {
		return matchingStatus;
	}
	public void setMatchingStatus(Map<String, Object> matchingStatus) {
		this.matchingStatus = matchingStatus;
	}

	public Map<String, Object> getInvestLimit() {
		return investLimit;
	}

	public void setInvestLimit(Map<String, Object> investLimit) {
		this.investLimit = investLimit;
	}

	public Map<String, Object> getProofStatus() {
		return proofStatus;
	}

	public void setProofStatus(Map<String, Object> proofStatus) {
		this.proofStatus = proofStatus;
	}

	public Map<String, Object> getProofType() {
		return proofType;
	}

	public void setProofType(Map<String, Object> proofType) {
		this.proofType = proofType;
	}

	public Map<String, Object> getOperateType() {
		return operateType;
	}

	public void setOperateType(Map<String, Object> operateType) {
		this.operateType = operateType;
	}
	public Map<String, Object> getGlobalParameter() {
		return globalParameter;
	}
	public void setGlobalParameter(Map<String, Object> globalParameter) {
		this.globalParameter = globalParameter;
	}
	
	public Map<String,Object> getStageParameter(){
		return stageParameter;
	}
	public void setStageParameter(Map<String, Object> stageParameter){
		this.stageParameter = stageParameter;
	}
	public Map<String, Object> getStageParameterName() {
		return stageParameterName;
	}
	public void setStageParameterName(Map<String, Object> stageParameterName) {
		this.stageParameterName = stageParameterName;
	}
	public Map<String, Object> getRaiseArea() {
		return raiseArea;
	}
	public void setRaiseArea(Map<String, Object> raiseArea) {
		this.raiseArea = raiseArea;
	}
	
}
