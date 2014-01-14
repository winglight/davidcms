package com.yi4all.davidapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.yi4all.davidapp.ApplicationController;
import com.yi4all.davidapp.BaseActivity;
import com.yi4all.davidapp.R;
import com.yi4all.davidapp.db.CompanyModel;
import com.yi4all.davidapp.db.ContentModel;
import com.yi4all.davidapp.db.ContentType;
import com.yi4all.davidapp.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MarketingDetailFragment extends Fragment {
	
	public final static String LOGTAG = "MarketingDetailFragment";

	private NetworkImageView iconImg;
    private TextView nameTxt;
    private TextView descTxt;
    
    private ContentModel cm;
    
    public static MarketingDetailFragment getInstance(ContentModel cm){
    	MarketingDetailFragment f = new MarketingDetailFragment();
    	f.cm = cm;
    	
    	return f;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_marketing_detail, container, false);

        iconImg = (NetworkImageView) v.findViewById(R.id.market_img);
        nameTxt = (TextView) v.findViewById(R.id.market_name);
        descTxt = (TextView) v.findViewById(R.id.market_desc);
        
        if(cm != null){
        	iconImg.setImageUrl(((BaseActivity)getActivity()).getService().getURL() + "/showImage/" + cm.getBigPic(), ApplicationController
                    .getInstance().getmImageLoader());
        nameTxt.setText(cm.getName());
        descTxt.setText(cm.getDescription());
        }
        
        ImageView iv = (ImageView) v.findViewById(R.id.market_back);
        iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MarketingListFragment f = new MarketingListFragment();
				
				((BaseActivity)getActivity()).addFragment(f, MarketingDetailFragment.LOGTAG, true);
				
			}
		});

        return v;
    }
	
	@Override
	public void onResume() {
		super.onResume();

	}
	
}
