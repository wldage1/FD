package com.sw.plugins.commission.fagrant.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;
import com.sw.plugins.commission.adjust.entity.Commission;
import com.sw.plugins.market.order.entity.Order;

/**
 * 理财师居间费支付明细表
 * 
 * @author Spark
 *
 */
public class MemberPayDetail extends RelationEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2686871049983651524L;

	//理财师居间费支付记录ID
	private Long memberPayID;
	//居间费ID
	private Long commissionID;
	//支付金额
	private BigDecimal payAmount;
	//产品名称
	private String productName;
	//居间费发放状态
	private short payStatus;
	//历史订单表BEAN
	private Order orderHistory;
	//居间费记录表
	private Commission commission;
	//居间费记录表
	private MemberPayRecord MemberPayRecord;
	//理财顾问ID
	private Long memberID;
	
	public Long getMemberPayID() {
		return memberPayID;
	}
	public void setMemberPayID(Long memberPayID) {
		this.memberPayID = memberPayID;
	}
	public Long getCommissionID() {
		return commissionID;
	}
	public void setCommissionID(Long commissionID) {
		this.commissionID = commissionID;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public short getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(short payStatus) {
		this.payStatus = payStatus;
	}
	public Order getOrderHistory() {
		return orderHistory;
	}
	public void setOrderHistory(Order orderHistory) {
		this.orderHistory = orderHistory;
	}
	public Commission getCommission() {
		return commission;
	}
	public void setCommission(Commission commission) {
		this.commission = commission;
	}
	public MemberPayRecord getMemberPayRecord() {
		return MemberPayRecord;
	}
	public void setMemberPayRecord(MemberPayRecord memberPayRecord) {
		MemberPayRecord = memberPayRecord;
	}
	public Long getMemberID() {
		return memberID;
	}
	public void setMemberID(Long memberID) {
		this.memberID = memberID;
	}
	
}
