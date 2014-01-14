package com.yi4all.davidapp.service;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yi4all.davidapp.ApplicationController;
import com.yi4all.davidapp.db.CompanyModel;
import com.yi4all.davidapp.db.ContentModel;
import com.yi4all.davidapp.db.ContentType;
import com.yi4all.davidapp.util.JsonDateDeserializer;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ServiceImpl {

	private static final String LOG_TAG = "ServiceImpl";

	private IDBService dbService;
	private IRemoteService remoteService;
	private Activity context;

	private CompanyModel company;
	
	private String[] orderitems;

	private Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
			new JsonDateDeserializer()).create();

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

	public CompanyModel getDefaultCompany() {
		if (company == null) {
			company = getDbService().getDefaultCompany();
			if (company == null) {
//				company = remoteService.getDefaultCompany();
//				if (company != null) {
//					getDbService().updateCompany(company);
//				}
			}
			// TODO:async update company info
			refreshDefaultCompany();
		}
		return company;
	}
	
	public String[] getOrderItems() {
		if (orderitems == null) {
						refreshOrderItems();
						
						//TODO:change type to orderitems
			List<ContentModel> list = getDbService().getContentsByType(ContentType.ORDERITEM);
			if (list != null) {
				String[] res = new String[list.size()];
				int i = 0;
				for(ContentModel cm : list){
					res[i] = cm.getName();
					i++;
				}
				return res;
			}
		}
		return orderitems;
	}

	public void refreshDefaultCompany() {
		String url = remoteService.getBaseUrl() + "/company";

		JsonObjectRequest req = new JsonObjectRequest(Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						MessageModel<CompanyModel> msg = gson.fromJson(
								response.toString(),
								new TypeToken<MessageModel<CompanyModel>>() {
								}.getType());

						if (!msg.isFlag()) {

						} else {
							CompanyModel cm = msg.getData();
							getDbService().updateCompany(cm);
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());

					}
				});

		ApplicationController.getInstance().addToRequestQueue(req);
	}
	
	public void refreshOrderItems() {
		String url = remoteService.getBaseUrl() + "/contents/" + ContentType.ORDERITEM.value();

		JsonObjectRequest req = new JsonObjectRequest(Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						MessageModel<List<ContentModel>> msg = gson.fromJson(
								response.toString(),
								new TypeToken<MessageModel<List<ContentModel>>>() {
								}.getType());

						if (!msg.isFlag()) {

						} else {
							List<ContentModel> apps = msg.getData();

							// save apps into local db
							getDbService().updateContents(apps);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());

					}
				});

		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public List<ContentModel> getContentsByType(ContentType type) {
		return getDbService().getContentsByType(type);
	}

	public void getContentsByTypeRemote(final Handler handler,
			final ContentType type, Date lastUpdateDate) {
		String url = remoteService.getBaseUrl();
		url += "/contents/" + type.value();

		if (lastUpdateDate != null) {
			url += "?lastUpdateDate=" + lastUpdateDate.getTime();
		}

		JsonObjectRequest req = new JsonObjectRequest(Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Message messsage = handler.obtainMessage();
						messsage.what = type.value();

						MessageModel<List<ContentModel>> msg = gson.fromJson(
								response.toString(),
								new TypeToken<MessageModel<List<ContentModel>>>() {
								}.getType());

						if (!msg.isFlag()) {

							messsage.arg1 = 1;// fail flag
							messsage.obj = msg.getMessage();
						} else {
							List<ContentModel> apps = msg.getData();
							messsage.arg1 = 0;// success flag
							messsage.obj = apps;

							// save apps into local db
							getDbService().updateContents(apps);
						}

						handler.sendMessage(messsage);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());

						Message messsage = handler.obtainMessage();
						messsage.what = type.value();
						messsage.arg1 = 1;// fail flag
						messsage.obj = VolleyErrorHelper.getMessage(error,
								context);

						handler.sendMessage(messsage);
					}
				});

		ApplicationController.getInstance().addToRequestQueue(req);

	}
	
	public void memberLogin(final Handler handler,
			final String name, final String password, final Integer type) {
		String url = remoteService.getMemberUrl();
		url += "/Member/LoginValidate";
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("UserID", name);
		params.put("Passwd", password);
		params.put("UserType", type);

		JsonObjectRequest req = new JsonObjectRequest(Method.POST, url, new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Message messsage = handler.obtainMessage();

						String res = response.toString();

						if ("false".equals(res)) {

							messsage.arg1 = 1;// fail flag
						} else {
							messsage.arg1 = 0;// success flag

						}

						handler.sendMessage(messsage);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());

						Message messsage = handler.obtainMessage();
						messsage.arg1 = 1;// fail flag
						messsage.obj = VolleyErrorHelper.getMessage(error,
								context);

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