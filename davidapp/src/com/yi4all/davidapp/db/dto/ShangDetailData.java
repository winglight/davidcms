package com.yi4all.davidapp.db.dto;

import java.io.Serializable;

public class ShangDetailData implements Serializable{

	private String Principal;
	private String ClientName;
	private String LineName;
	private String Remark;
	private String UpDown;
	private String IsOnline;
	public String getClientName() {
		return ClientName;
	}
	public void setClientName(String clientName) {
		ClientName = clientName;
	}
	public String getLineName() {
		return LineName;
	}
	public void setLineName(String lineName) {
		LineName = lineName;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getIsOnline() {
		return IsOnline;
	}
	public void setIsOnline(String isOnline) {
		IsOnline = isOnline;
	}
	public String getPrincipal() {
		return Principal;
	}
	public void setPrincipal(String principal) {
		Principal = principal;
	}
	public String getUpDown() {
		return UpDown;
	}
	public void setUpDown(String upDown) {
		UpDown = upDown;
	}
	
	
}
