package com.yi4all.davidapp.db.dto;

import java.io.Serializable;

public class Hall implements Serializable{

	private String HallNo;
	private String HallName;
	public String getHallNo() {
		return HallNo;
	}
	public void setHallNo(String hallNo) {
		HallNo = hallNo;
	}
	public String getHallName() {
		return HallName;
	}
	public void setHallName(String hallName) {
		HallName = hallName;
	}
	
	
}
