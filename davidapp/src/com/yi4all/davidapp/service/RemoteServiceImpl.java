package com.yi4all.davidapp.service;

import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yi4all.appmarketapp.ApplicationController;
import com.yi4all.appmarketapp.db.UserModel;
import com.yi4all.appmarketapp.util.Constants;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
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
//			 service = new RemoteServiceImpl("http://10.52.5.20:9000");
			 service = new RemoteServiceImpl("http://market.yi4all.com");
		}
		return service;
	}

	public boolean isLogin() {
		return tokenId != null && tokenId.length() > 0 && tokenExpirationTime > new Date().getTime();
	}
	
	private void sureLogin() throws ServiceException{
		if(!isLogin()){
				loginDirect();
		}
	}

	@Override
	public UserModel loginDirect() throws ServiceException {
		String url = base_url
				+ "/quicklogin?name=" + sid
				+ "&desc=mobile";
		RequestFuture<JSONObject> future = RequestFuture.newFuture();
		JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(), future, future);
		ApplicationController.getInstance().addToRequestQueue(request);

		try {
		  JSONObject response = future.get(); // this will block
		  MessageModel<UserModel> msg = new Gson().fromJson(response.toString(),
                  new TypeToken<MessageModel<UserModel>>() {
                  }.getType());
		  if (!msg.isFlag()) {
				throw new ServiceException(
						ServiceException.ERROR_CODE_LOGIN_ERROR,
						msg.getMessage());
			} else {
				UserModel login = msg.getData();
				tokenId = login.getTokenid();
				tokenExpirationTime = new Date().getTime() + 25*60*1000;//set expiration time shorter than 30 minutes
				
				return login;
			}
		} catch (InterruptedException e) {
		  // exception handling
			throw new ServiceException(e);
		} catch (ExecutionException e) {
		  // exception handling
			throw new ServiceException(e);
		}
	}

	@Override
	public UserModel login(String name, String password)
			throws ServiceException {
		String url = base_url
				+ "/login";
		// Post params to be sent to the server
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", name);
		params.put("password", password);
		params.put("desc", Constants.DESC_MOBILE);
		
		RequestFuture<JSONObject> future = RequestFuture.newFuture();
		JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, new JSONObject(params), future, future);
		ApplicationController.getInstance().addToRequestQueue(request);

		try {
		  JSONObject response = future.get(); // this will block
		  MessageModel<UserModel> msg = new Gson().fromJson(response.toString(),
                  new TypeToken<MessageModel<UserModel>>() {
                  }.getType());
		  if (!msg.isFlag()) {
				throw new ServiceException(
						ServiceException.ERROR_CODE_LOGIN_ERROR,
						msg.getMessage());
			} else {
				UserModel login = msg.getData();
				tokenId = login.getTokenid();
				tokenExpirationTime = new Date().getTime() + 25*60*1000;//set expiration time shorter than 30 minutes
				
				return login;
			}
		} catch (InterruptedException e) {
		  // exception handling
			throw new ServiceException(e);
		} catch (ExecutionException e) {
		  // exception handling
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean register(UserModel user)
			throws ServiceException {
		String url = base_url
				+ "/login";
		// Post params to be sent to the server
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", user.getName());
		params.put("email", user.getEmail());
		params.put("password", user.getPassword());
		params.put("twicePassword", user.getPassword());
		params.put("deviceId", user.getDeviceId());
		
		
		RequestFuture<JSONObject> future = RequestFuture.newFuture();
		JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, new JSONObject(params), future, future);
		ApplicationController.getInstance().addToRequestQueue(request);

		try {
		  JSONObject response = future.get(); // this will block
		  MessageModel<UserModel> msg = new Gson().fromJson(response.toString(),
                  new TypeToken<MessageModel<UserModel>>() {
                  }.getType());
		  if (!msg.isFlag()) {
				throw new ServiceException(
						ServiceException.ERROR_CODE_LOGIN_ERROR,
						msg.getMessage());
			} else {
				UserModel login = msg.getData();
				tokenId = login.getTokenid();
				tokenExpirationTime = new Date().getTime() + 25*60*1000;//set expiration time shorter than 30 minutes
				
				return true;
			}
		} catch (InterruptedException e) {
		  // exception handling
			throw new ServiceException(e);
		} catch (ExecutionException e) {
		  // exception handling
			throw new ServiceException(e);
		}
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
	public MessageModel<String> forgetPassword(String phone, String email)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
