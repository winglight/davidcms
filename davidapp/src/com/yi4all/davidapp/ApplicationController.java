package com.yi4all.davidapp;

import android.app.Application;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.yi4all.appmarketapp.util.BitmapCache;

public class ApplicationController extends Application {

    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static ApplicationController sInstance;
    
 // http client instance
//    private DefaultHttpClient mHttpClient;
    
    private  ImageLoader mImageLoader;
    private  ImageCache mImageCache;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        sInstance = this;
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized ApplicationController getInstance() {
        return sInstance;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
    	// lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            // Create an instance of the Http client. 
            // We need this in order to access the cookie store
//        	if(mHttpClient == null){
//        		mHttpClient = new DefaultHttpClient();
//        	}
            // create the request queue
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }
    

    public ImageLoader getmImageLoader() {
    	if(mImageLoader == null){
    		mImageCache = new BitmapCache();
            mImageLoader = new ImageLoader(getRequestQueue(),mImageCache);
    	}
		return mImageLoader;
	}

	/**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     * 
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     * 
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     * 
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    
    /**
     * Method to set a cookie
     */
//    public void setCookie(String key, String value) {
//        CookieStore cs = mHttpClient.getCookieStore();
//        // create a cookie
//        cs.addCookie(new BasicClientCookie2(key, value));
//    }
}