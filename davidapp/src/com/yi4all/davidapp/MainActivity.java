package com.yi4all.davidapp;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import com.yi4all.davidapp.db.CompanyModel;
import com.yi4all.davidapp.db.ContentType;
import com.yi4all.davidapp.fragment.HallListFragment;
import com.yi4all.davidapp.fragment.IntroFragment;
import com.yi4all.davidapp.fragment.MarketingListFragment;
import com.yi4all.davidapp.fragment.OrderFragment;
import com.yi4all.davidapp.fragment.ServiceListFragment;
import com.yi4all.davidapp.util.Utils;
import com.yi4all.davidapp.view.InfiniteGallery;

public class MainActivity extends BaseActivity {

	private final static String LOGTAG = "MainActivity";

	private String[] pageTitle;

	private boolean isTwiceQuit;

    private TextView carouselTxt;
    
    private TextView oldTab;
    
    private int currentTabPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);

		pageTitle = getResources().getStringArray(R.array.main_tab_label);

        carouselTxt = (TextView) findViewById(R.id.carouselTxt);

		FragmentPagerAdapter adapter = new CMSTabAdapter(
				getSupportFragmentManager());

		final InfiniteGallery indicator = (InfiniteGallery) findViewById(R.id.indicator);
		
		final ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		pager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
//				currentTab = AppsTab.values()[position];
				int pos = currentTabPos - (currentTabPos%Integer.MAX_VALUE) + position;
				indicator.setSelection(pos);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		
		indicator.setTabs(pageTitle);
		indicator.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long arg3) {
				//set older tab text appearance
				if(oldTab != null){
				oldTab.setTextColor(getResources().getColor(R.color.tab_text_color));
				oldTab.setTextSize(getResources().getDimensionPixelSize(R.dimen.tab_text_dimen));
				oldTab.invalidate();
				}
				//set new tab text appearance
				currentTabPos = position;
				TextView newTab = (TextView) view; 
				newTab.setTextColor(getResources().getColor(R.color.tab_selected_text_color));
				newTab.setTextSize(getResources().getDimensionPixelSize(R.dimen.tab_selected_text_dimen));
				newTab.invalidate();
				
				indicator.invalidate();
				
				oldTab = newTab;
				
				pager.setCurrentItem(position%pageTitle.length);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		indicator.setSelection(getMiddlePos());
		
		pager.setCurrentItem(3);
		
	}
	
	private int getMiddlePos(){
		int res = Integer.MAX_VALUE/2;
		int add = res%pageTitle.length;
		res = res + (3 - add);
		
		currentTabPos = res;
		
		return res;
	}

	@Override
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (isTwiceQuit) {

				this.finish();
			} else {
				Utils.toastMsg(this, R.string.sure_quit_app);
				isTwiceQuit = true;

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						isTwiceQuit = false;

					}
				}, 2000);
			}
			return true;
		default:
			return false;
		}
	}

	class CMSTabAdapter extends FragmentPagerAdapter {
		public CMSTabAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
            Fragment f = new IntroFragment();
            switch (position){
                case 0:
                    //資料查詢
                    break;
                case 1:
                    //推廣諮訊
                	f= new MarketingListFragment();
                    break;
                case 2:
                    //聽會介紹
                	f = new HallListFragment();
                    break;
                case 3:
                    //大衛集團
                    f = new IntroFragment();
                    break;
                case 4:
                    //線上預訂
                	f = new OrderFragment();
                    break;
                case 5:
                    //服務項目
                    f = new ServiceListFragment();
                    break;
                case 6:
                    //聯繫我們
                    break;
                case 7:
                    //意見箱
                    break;

            }
			return  f;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return pageTitle[position];
		}

		@Override
		public int getCount() {
			return pageTitle.length;
		}
	}

}
