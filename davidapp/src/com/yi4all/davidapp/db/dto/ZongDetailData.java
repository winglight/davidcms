package com.yi4all.davidapp.db.dto;

import java.io.Serializable;

public class ZongDetailData implements Serializable{

	private int Amount;
	private String ClientName;
	
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
	
	
}
