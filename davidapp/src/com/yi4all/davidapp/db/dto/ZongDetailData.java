package com.yi4all.davidapp.db.dto;

import java.io.Serializable;

public class ZongDetailData implements Serializable{

	private int Amount;
	private String ClientName;
	private String PostTime;
	private String Remark;
	
	private String DDATE;
	private int BTotal;
	private int WTotal;
	private int Total;
	
	public int getAmount() {
		return Amount;
	}
	public void setAmount(int amount) {
		Amount = amount;
	}
	public String getClientName() {
		return ClientName;
	}
	public void setClientName(String clientName) {
		ClientName = clientName;
	}
	public String getPostTime() {
		return PostTime;
	}
	public void setPostTime(String postTime) {
		PostTime = postTime;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getDDATE() {
		return DDATE;
	}
	public void setDDATE(String dDATE) {
		DDATE = dDATE;
	}
	public int getBTotal() {
		return BTotal;
	}
	public void setBTotal(int bTotal) {
		BTotal = bTotal;
	}
	public int getWTotal() {
		return WTotal;
	}
	public void setWTotal(int wTotal) {
		WTotal = wTotal;
	}
	public int getTotal() {
		return Total;
	}
	public void setTotal(int total) {
		Total = total;
	}
	
	
}
