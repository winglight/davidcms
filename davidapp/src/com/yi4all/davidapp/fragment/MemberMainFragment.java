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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.yi4all.davidapp.ApplicationController;
import com.yi4all.davidapp.BaseActivity;
import com.yi4all.davidapp.MemberActivity;
import com.yi4all.davidapp.R;
import com.yi4all.davidapp.db.CompanyModel;
import com.yi4all.davidapp.db.ContentModel;
import com.yi4all.davidapp.db.ContentType;
import com.yi4all.davidapp.db.dto.LinePerson;
import com.yi4all.davidapp.fragment.member.KuanFragment;
import com.yi4all.davidapp.fragment.member.ShangFragment;
import com.yi4all.davidapp.fragment.member.ZongDeatilFragment;
import com.yi4all.davidapp.fragment.member.ZongFragment;
import com.yi4all.davidapp.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemberMainFragment extends Fragment {
	
	public final static String LOGTAG = "IntroFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_member_main, container, false);

        TextView zongTxt = (TextView) v.findViewById(R.id.zongheBtn);
        zongTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MemberActivity)getActivity()).setRoot(false);
				LinePerson person = ((MemberActivity)getActivity()).getCurrentUser();
		        if(person != null && person.isUp()){
		        	((MemberActivity)getActivity()).goinFragment(new ZongFragment());
		        }else{
		        	((MemberActivity)getActivity()).goinFragment(ZongDeatilFragment.getInstance(person));
		        }
			}
		});
        
        TextView kuanTxt = (TextView) v.findViewById(R.id.kuanBtn);
        kuanTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MemberActivity)getActivity()).setRoot(false);
				LinePerson person = ((MemberActivity)getActivity()).getCurrentUser();
		        if(person != null && person.isUp()){
		        	((MemberActivity)getActivity()).goinFragment(new KuanFragment());
		        }else{
		        	//TODO kuan detail
		        	((MemberActivity)getActivity()).goinFragment(new KuanFragment());
		        }
			}
		});
        
        TextView shangTxt = (TextView) v.findViewById(R.id.shangBtn);
        shangTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MemberActivity)getActivity()).setRoot(false);
				((MemberActivity)getActivity()).goinFragment(new ShangFragment());
			}
		});
        LinePerson person = ((MemberActivity)getActivity()).getCurrentUser();
        if(person != null && !(person.isUp())){
        	shangTxt.setVisibility(View.GONE);
        }
        
        Button changePwdBtn = (Button) v.findViewById(R.id.changePwdBtn);
        changePwdBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MemberActivity)getActivity()).setRoot(false);
				((MemberActivity)getActivity()).goinFragment(null);
				
			}
		});
        Button quitBtn = (Button) v.findViewById(R.id.quitBtn);
        quitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
				
			}
		});

        return v;
    }
	
	@Override
	public void onResume() {
		super.onResume();

		((MemberActivity)getActivity()).setRoot(true);
	}
	
}
