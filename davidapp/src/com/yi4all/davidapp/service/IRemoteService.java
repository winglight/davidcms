package com.yi4all.davidapp.service;

import com.yi4all.davidapp.db.CompanyModel;

public interface IRemoteService {
	
	public void setBaseUrl(String url);
	public String getBaseUrl();

	/**********User Module****************/
	
	public void setSid(String sid) ;
	public String getSid();
	
	/****************App fetch*********************/
    public CompanyModel getDefaultCompany();
}
