package com.yi4all.davidapp.service;

import com.yi4all.davidapp.db.CompanyModel;
import com.yi4all.davidapp.db.ContentModel;
import com.yi4all.davidapp.db.ContentType;

import java.util.List;

public interface IDBService {

	public void close();
	
	//apps
	public List<ContentModel> getContentsByType(ContentType type);
	
	public void updateContents(List<ContentModel> list);

    public CompanyModel getDefaultCompany();

    public void updateCompany(CompanyModel cm);

}
