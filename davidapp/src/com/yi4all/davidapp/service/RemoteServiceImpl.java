package com.yi4all.davidapp.service;


import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yi4all.davidapp.ApplicationController;
import com.yi4all.davidapp.db.CompanyModel;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class RemoteServiceImpl implements IRemoteService {

	private static final String LOG_TAG = "ServiceImpl";
	public static final String TOKEN_TAG = "$token$";
	
	public static int RETURN_CODE_SUCCESS = 0;

	private static IRemoteService service;

	private String base_url;
	private String tokenId = "";
	private long tokenExpirationTime;
	private String sid = "";

	private RemoteServiceImpl(String url) {
		this.base_url = url;
	}

	public static IRemoteService getInstance() {
		if (service == null) {

//			 service = new RemoteServiceImpl("http://192.168.1.9:9000/service");
//			 service = new RemoteServiceImpl("http://10.31.58.74:9000");
			 service = new RemoteServiceImpl("http://market.yi4all.com");
		}
		return service;
	}

	public boolean isLogin() {
		return tokenId != null && tokenId.length() > 0 && tokenExpirationTime > new Date().getTime();
	}
	
	@Override
	public void setBaseUrl(String url) {
		this.base_url = url;

	}

	@Override
	public String getBaseUrl() {
		return this.base_url;
	}

	@Override
	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSid() {
		return sid;
	}

    @Override
    public CompanyModel getDefaultCompany() {
        String url = base_url
                + "/company";
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(url, null, future, future);
        ApplicationController.getInstance().addToRequestQueue(request);

        try {
            JSONObject response = future.get(); // this will block
            MessageModel<CompanyModel> msg = new Gson().fromJson(response.toString(),
                    new TypeToken<MessageModel<CompanyModel>>() {
                    }.getType());
            if (msg.isFlag()){
                return msg.getData();
            }
        } catch (InterruptedException e) {
            // exception handling
            e.printStackTrace();
        } catch (ExecutionException e) {
            // exception handling
            e.printStackTrace();
        }
        return null;
    }

}
