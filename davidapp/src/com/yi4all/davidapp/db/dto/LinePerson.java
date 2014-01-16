package com.yi4all.davidapp.db.dto;

import java.io.Serializable;

public class LinePerson implements Serializable{

	private String LineCode;
	private String LineName;
	private String IsData;
	private String LongTerm;
	private boolean isUp;
	
	public String getLineCode() {
		return LineCode;
	}
	public void setLineCode(String lineCode) {
		LineCode = lineCode;
	}
	public String getLineName() {
		return LineName;
	}
	public void setLineName(String lineName) {
		LineName = lineName;
	}
	
	public String getIsData() {
		return IsData;
	}
	public void setIsData(String isData) {
		IsData = isData;
	}
	public boolean isHasData() {
		return "Y".equals(IsData);
	}
	public void setHasData(boolean isData) {
		IsData = isData?"Y":"N";
	}
	public boolean isUp() {
		return isUp;
	}
	public void setUp(boolean isUp) {
		this.isUp = isUp;
	}
	public String getLongTerm() {
		return LongTerm;
	}
	public void setLongTerm(String longTerm) {
		LongTerm = longTerm;
	}
	
}
