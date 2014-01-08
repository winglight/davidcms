package com.yi4all.davidapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class IntroFragment extends Fragment {
	
	private final static String LOGTAG = "IntroFragment";

    private TextView nameTxt;
    private TextView descTxt;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_intro, container, false);

        nameTxt = (TextView) v.findViewById(R.id.groupName);
        descTxt = (TextView) v.findViewById(R.id.groupDesc);
        CompanyModel company = ((BaseActivity)getActivity()).getService().getDefaultCompany();
        nameTxt.setText(company.getName());
        descTxt.setText(company.getDescription());

        return v;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		
	}
	
}
