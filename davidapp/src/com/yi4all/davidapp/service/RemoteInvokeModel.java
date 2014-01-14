package com.yi4all.davidapp.service;

import java.io.Serializable;

import org.json.JSONObject;


public class RemoteInvokeModel implements Serializable {

	private int action;// POST or GET
	private String url;// complete request url
	private JSONObject requestBody;// request body
	private String message;// error message
	
	public RemoteInvokeModel(){
		
	}
	


	public int getAction() {
		return action;
	}



	public void setAction(int action) {
		this.action = action;
	}



	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public JSONObject getRequestBody() {
		return requestBody;
	}



	public void setRequestBody(JSONObject requestBody) {
		this.requestBody = requestBody;
	}



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
