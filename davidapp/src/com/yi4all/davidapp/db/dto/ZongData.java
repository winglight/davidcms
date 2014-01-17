package com.yi4all.davidapp.db.dto;

import java.io.Serializable;

public class ZongData implements Serializable{

	private int Amount;
	private String HallName;
	private String HallID;
	
	public int getAmount() {
		return Amount;
	}
	public void setAmount(int amount) {
		Amount = amount;
	}
	public String getHallName() {
		return HallName;
	}
	public void setHallName(String hallName) {
		HallName = hallName;
	}
	public String getHallID() {
		return HallID;
	}
	public void setHallID(String hallID) {
		HallID = hallID;
	}
	
	
}
