package com.yi4all.davidapp.service;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yi4all.appmarketapp.ApplicationController;
import com.yi4all.appmarketapp.AppsTab;
import com.yi4all.appmarketapp.R;
import com.yi4all.appmarketapp.db.AppModel;
import com.yi4all.appmarketapp.db.CategoryType;
import com.yi4all.appmarketapp.db.UserModel;
import com.yi4all.appmarketapp.util.JsonDateDeserializer;
import com.yi4all.appmarketapp.util.Utils;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class ServiceImpl {

	private static final String LOG_TAG = "ServiceImpl";

	private IDBService dbService;
	private IRemoteService remoteService;
	private UserModel currentUser;
	private Activity context;

	private Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
	
	private static ServiceImpl instance;

	public static ServiceImpl getInstance(Activity context) {
		if (instance == null) {
			// initDBFile(context);

			instance = new ServiceImpl(context);
		}
		if (instance.remoteService == null) {
			instance.remoteService = RemoteServiceImpl.getInstance();
		}
		return instance;
	}

	private ServiceImpl(Activity context) {
		this.context = context;
		this.dbService = DBServiceImpl.getInstance(context);
		this.remoteService = RemoteServiceImpl.getInstance();

		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String sid = manager.getDeviceId();

		this.remoteService.setSid(sid);
	}

	public IDBService getDbService() {
		if (dbService == null) {
			dbService = DBServiceImpl.getInstance(context);
		}
		return dbService;
	}

	private void setCurrentUser(UserModel currentUser) {
		this.currentUser = currentUser;
	}

	public boolean sureLogin() {
		if (currentUser == null || !remoteService.isLogin()) {
			MessageModel<UserModel> msg = loginDirect(remoteService.getSid());

			if (msg.isFlag()) {
				currentUser = msg.getData();
				
			}else{
				Utils.toastMsg(context, R.string.login_error);
				
				return false;
			}
		}
		return true;
	}
	
	public boolean isValid() {
		sureLogin();
		
		return new Date().before(currentUser.getExpirationDate());
	}
	
	/********** 同步方法-远程 ****************/
	public UserModel getCurrentUser() {
		if (currentUser == null) {
			currentUser = getDbService().queryDefaultUser();
		}
		return currentUser;
	}

	public boolean createUser(UserModel user) {
		return getDbService().createUser(user);
	}

	public MessageModel<UserModel> loginDirect(String sid) {
		MessageModel<UserModel> msg = new MessageModel<UserModel>();
		try {
			UserModel user = remoteService.loginDirect();

			// query user from local db
			UserModel user2 = getDbService().queryUserBySid(sid);
			if (user2 == null) {
				// save user into db
				if (!getDbService().createUser(user)) {
					msg.setFlag(false);
					msg.setErrorCode(ServiceException.ERROR_CODE_DB_EXCEPTION);
					return msg;
				}
			} else {
				getDbService().updateUser(user2);
				user = user2;
			}

			setCurrentUser(user);
			msg.setData(user);
			msg.setFlag(true);
		} catch (ServiceException e) {
			Log.e(LOG_TAG, "loginDirect error:" + e.getMessage());
			msg.setErrorCode(e.getErrorCode());
			msg.setMessage(e.getMessage());
		}
		return msg;
	}
	
	public List<AppModel> getAppsByTab(AppsTab currentTab, int page) {
		if(currentTab == AppsTab.UPLOAD){
			if(!sureLogin()){
				return null;
			}
		}
		return getDbService().getAppsByTab(currentTab, getCurrentUser(), page);
	}
	
	public void downloadApk(final Handler handler, String url){
			//sure to login the server;
			if(sureLogin()){
			
			//TODO:download apk
			}
	}
	
	public void getAppsByTabRemote(final Handler handler, final AppsTab currentTab,
			 final int page, Date lastUpdateDate) {
			String url = remoteService.getBaseUrl();
			switch(currentTab){
			case HOTS:{
				url += "/apps/hots/" + page;
				break;
			}
			case APP:{
				url += "/apps/categorytype/" + CategoryType.APP.value() + "/" + page;
				break;
			}
			case GAME:{
				url += "/apps/categorytype/" + CategoryType.GAME.value() + "/" + page;
				break;
			}
			case NEWEST:{
				url += "/apps/newest/" + page;
				break;
			}
			case ADULT:{
				url += "/apps/categorytype/" + CategoryType.ADULT.value() + "/" + page;
				break;
			}
			case UPLOAD:{
				if(sureLogin()){
					url += "/developer/apps";
					//TODO:download apk
					}else{
					return;
				}
				
				break;
			}
			}
			
			if(lastUpdateDate != null){
				url += "?lastUpdateDate=" + lastUpdateDate.getTime();
			}
			
			JsonObjectRequest req = new JsonObjectRequest(Method.GET, url, null,
				       new Response.Listener<JSONObject>() {
				           @Override
				           public void onResponse(JSONObject response) {
				        	   Message messsage = handler.obtainMessage();
				       		messsage.what = currentTab.value();
				       		messsage.arg2 = page;
				       		
				       		  MessageModel<List<AppModel>> msg = gson.fromJson(response.toString(),
				                         new TypeToken<MessageModel<List<AppModel>>>() {}.getType());
				       		  
				       		  if (!msg.isFlag()) {
				       			  
				       				messsage.arg1 = 1;//fail flag
				       				messsage.obj = msg.getMessage();
				       			} else {
				       				List<AppModel> apps = msg.getData();
				       				messsage.arg1 = 0;//success flag
				       				messsage.obj = apps;
				       				
				       				//save apps into local db
				       				getDbService().updateApps(apps);
				       			}
				       		
				       		handler.sendMessage(messsage);
				           }
				       }, new Response.ErrorListener() {
				           @Override
				           public void onErrorResponse(VolleyError error) {
				               VolleyLog.e("Error: ", error.getMessage());
				               
				               Message messsage = handler.obtainMessage();
					       		messsage.what = currentTab.value();
					       		messsage.arg2 = page;
					       		messsage.arg1 = 1;//fail flag
			       				messsage.obj = VolleyErrorHelper.getMessage(error, context);
			       				
			       				handler.sendMessage(messsage);
				           }
				       });
			
			ApplicationController.getInstance().addToRequestQueue(req);
		
	}
	
	public void close() {
		if (dbService != null) {
			dbService.close();
			dbService = null;
		}
	}

	public void setURL(String url) {
		remoteService.setBaseUrl(url);
	}

	public String getURL() {
		return remoteService.getBaseUrl();
	}

	public void setContext(Activity context) {
		this.context = context;
	}

}
