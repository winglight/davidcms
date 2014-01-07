package com.yi4all.davidapp;


import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;

public class BaseActivity extends FragmentActivity{

	private static final String IMAGE_CACHE_DIR = "images";
	
	//Service instance for RPC or DB
	private ServiceImpl service;
	
	private Bitmap no_image;

	private Bitmap default_image;
	
	private int imgWidth;
	private int imgHeight;

	private final Object mClickLock = new Object();
	
	protected StartAppAd startAppAd = new StartAppAd(this);
	
	protected AirSDK airsdk; //Declare AirSDK here
	
	private UpdateChecker checker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        StartAppAd.init(this, "112302125", "212016065");
        StartAppSearch.init(this, "112302125", "212016065");
        
        if(airsdk==null)
        	airsdk=new AirSDK(getApplicationContext(), null, true);
        
        airsdk.startPushNotification(false);
        
        checker = new UpdateChecker(this, false);
        
        service = ServiceImpl.getInstance(this);
        
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        
        Log.d("BaseActivity", "dpi:"+metrics.density*160);
        Log.d("BaseActivity", "n Brand => "+Build.BRAND);
        Log.d("BaseActivity", "n Device => "+Build.DEVICE);
        
    }
	
	private OnBackStackChangedListener getListener()
    {
        OnBackStackChangedListener result = new OnBackStackChangedListener()
        {
            public void onBackStackChanged() 
            {                   
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null)
                {
                    Fragment currFrag = manager.
                    findFragmentById(android.R.id.content);
                    
                }                   
            }
        };

        return result;
    }
	
	public void addFragment(Fragment f, String tag,int id){
		synchronized (mClickLock) {
            final FragmentTransaction ft = getSupportFragmentManager().
            		beginTransaction();
//            ft.setCustomAnimations(R.anim.push_right_in,R.anim.push_left_out);
            ft.replace(id, f, tag).addToBackStack(tag);
            ft.commit();
            mClickLock.notifyAll();
		}            
	}
	
	public void clearFragment(){
		synchronized (mClickLock) {
		final FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
//		ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
		for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {    
		    fm.popBackStack();
		}
		ft.commit();
		mClickLock.notifyAll();
	}
	}
	
	public void backFragment(Fragment f){
		synchronized (mClickLock) {
		final FragmentTransaction ft = getSupportFragmentManager().
        		beginTransaction();
//		ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
		ft.remove(f);
		ft.commit();
		mClickLock.notifyAll();
	}
	}
	
	public void checkUpdateNDownload(AppModel app){
		checker.checkForUpdateByVersionCode(service.getURL() + "/apps/checkupdate/" + app.getId());
		if(checker.isUpdateAvailable()){
			downloadApk(app.getDownurl());
		}
	}
	
	public void downloadApk(String url){
		checker.downloadAndInstall(url);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        
        service.setContext(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        startAppAd.onPause();
        
        airsdk.startIconAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        service.close();
    }
    
    public ServiceImpl getService(){
    	if(service == null){
    		service = ServiceImpl.getInstance(this);
    	}
    	return service;
    }

	public Bitmap getNo_image() {
		return no_image;
	}

	public Bitmap getDefault_image() {
		return default_image;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public int getImgHeight() {
		return imgHeight;
	}

}
