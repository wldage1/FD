package com.sw.plugins.market.salemanage.service;

import java.util.List;
import java.util.Map;

import com.sw.plugins.market.salemanage.entity.SaleManageMessage;
import com.sw.plugins.message.task.entity.UserMessage;

public class SaleManageMessageTask implements Runnable {

	private SaleManageMessageService messageService;
	private Methods mh;
	private SaleManageMessage ms;
	private Short opt;
	private Map<String,Object> pm;
	private List<UserMessage> rl;

	SaleManageMessageTask() {
	}

	public SaleManageMessageTask(SaleManageMessageService messageService, Methods mh, SaleManageMessage ms) {
		this.messageService = messageService;
		this.mh = mh;
		this.ms = ms;
	}
	
	public SaleManageMessageTask(SaleManageMessageService messageService, Methods mh, SaleManageMessage ms, Short opt) {
		this.messageService = messageService;
		this.mh = mh;
		this.ms = ms;
		this.opt = opt;
	}
	
	public SaleManageMessageTask(SaleManageMessageService messageService, Methods mh,SaleManageMessage ms,Map<String,Object> pm,List<UserMessage> rl,Short opt) {
		this.messageService = messageService;
		this.mh = mh;
		this.ms = ms;
		this.opt = opt;
		this.pm = pm;
		this.rl = rl;
	}

	@Override
	public void run() {
		switch (mh) {
		case HANDLEORDERSTATUS:
			messageService.handleOrderStatus(ms);
			break;
		case HANDLEREMITTANCESTATUS:
			messageService.handleRemittanceStatus(ms, opt);
			break;
		case SAVEPRODUCTSTATUS:
			messageService.saveProductStatus(ms,pm,rl,opt);
			break;
		case HANDLESHARE:
			messageService.handleShare(ms,opt);
			break;
		case HANDLEFUND:
			messageService.handleFund(ms, opt);
		}
	}
	
	

	public SaleManageMessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(SaleManageMessageService messageService) {
		this.messageService = messageService;
	}

	public Methods getMh() {
		return mh;
	}

	public void setMh(Methods mh) {
		this.mh = mh;
	}

	public SaleManageMessage getMs() {
		return ms;
	}

	public void setMs(SaleManageMessage ms) {
		this.ms = ms;
	}

	public Short getOpt() {
		return opt;
	}

	public void setOpt(Short opt) {
		this.opt = opt;
	}

	public Map<String, Object> getPm() {
		return pm;
	}

	public void setPm(Map<String, Object> pm) {
		this.pm = pm;
	}

	public List<UserMessage> getRl() {
		return rl;
	}

	public void setRl(List<UserMessage> rl) {
		this.rl = rl;
	}



	public static enum Methods {
		HANDLEORDERSTATUS, HANDLEREMITTANCESTATUS, SAVEPRODUCTSTATUS, HANDLESHARE, HANDLEFUND
	}

}
