package com.yi4all.davidapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import com.viewpagerindicator.TabPageIndicator;
import com.yi4all.davidapp.db.CompanyModel;
import com.yi4all.davidapp.db.ContentType;
import com.yi4all.davidapp.fragment.IntroFragment;
import com.yi4all.davidapp.fragment.ServiceListFragment;
import com.yi4all.davidapp.util.Utils;

public class MainActivity extends BaseActivity {

	private final static String LOGTAG = "MainActivity";

	private String[] pageTitle;

	private boolean isTwiceQuit;

    private TextView carouselTxt;
    
    private int currentTabPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		
		setContentView(R.layout.activity_main);
		
		pageTitle = getResources().getStringArray(R.array.main_tab_label);

//        carouselTxt = (TextView) findViewById(R.id.carouselTxt);

		FragmentPagerAdapter adapter = new CMSTabAdapter(
				getSupportFragmentManager());

		final TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
//				currentTab = AppsTab.values()[position];
				//set older tab text appearance
				TextView oldTab = (TextView) indicator.getChildAt(currentTabPos); 
				oldTab.setTextColor(R.color.tab_text_color);
				oldTab.setTextSize(R.dimen.tab_text_dimen);
				//set new tab text appearance
				currentTabPos = position;
				TextView newTab = (TextView) indicator.getChildAt(currentTabPos); 
				newTab.setTextColor(R.color.tab_selected_text_color);
				newTab.setTextSize(R.dimen.tab_selected_text_dimen);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		indicator.setViewPager(pager);
		
		pager.setCurrentItem(3);
		
		currentTabPos = 3;
	}

    @Override
    protected void onResume() {
        super.onResume();

        CompanyModel company = getService().getDefaultCompany();

        if(company != null){
        carouselTxt.setText(company.getMarquee());
        }
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
                    break;
                case 2:
                    //聽會介紹
                    break;
                case 3:
                    //大衛集團
                    f = new IntroFragment();
                    break;
                case 4:
                    //線上預訂
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
