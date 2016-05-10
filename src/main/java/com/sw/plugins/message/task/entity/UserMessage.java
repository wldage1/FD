package com.sw.plugins.message.task.entity;
//发送消息用户相关信息
public class UserMessage {
	//用户ID
   private Long userID;
   //用户姓名
   private String userName;
   //用户类型1-运营方2-供应商3-理财师4-居间公司
   private Short userType;
   //邮件地址
   private String email;
   
   public Long getUserID() {
	return userID;
}
public void setUserID(Long userID) {
	this.userID = userID;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public Short getUserType() {
	return userType;
}
public void setUserType(Short userType) {
	this.userType = userType;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}

}
