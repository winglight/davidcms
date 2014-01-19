package com.yi4all.davidapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.LinearLayout;
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
import com.yi4all.davidapp.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedbackFragment extends Fragment {
	
	private final static String LOGTAG = "IntroFragment";
	
	EditText phoneTxt;
	EditText contentTxt;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_feedback, container, false);

        phoneTxt = (EditText) v.findViewById(R.id.f_phoneTxt);
        contentTxt = (EditText) v.findViewById(R.id.feedbackTxt);
        
        Button submit = (Button) v.findViewById(R.id.f_okBtn);
        submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String content = contentTxt.getText().toString();
				String phone = phoneTxt.getText().toString();
				
				if(content == null || content.length() == 0 || phone == null || phone.length() == 0 ){
					Utils.toastMsg(getActivity(), R.string.changepwd_null_error);
					
					return;
				}
				
				saveFeedback(content, phone);
				
			}
		});
        
        return v;
    }
	
	@Override
	public void onResume() {
		super.onResume();

	}
	
	private void saveFeedback(final String content, String phone) {
    	final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.waiting), true, false); 
		
    	String userid = "";
    	
			// TODO:notify updating local db
			((BaseActivity)getActivity()).getService().saveFeedback(
                    new Handler() {
				@Override
				public void handleMessage(Message msg) {
					
					progressDialog.dismiss();
					
					// msg construction:
					// what: tab value, arg1: success flag(0 - success, 1 -
					// fail), arg2: page, obj: data of list
					if (msg.arg1 == 0) {
						Utils.toastMsg(getActivity(), R.string.save_feedback_success);
						phoneTxt.setText("");
						phoneTxt.invalidate();
						contentTxt.setText("");
						contentTxt.invalidate();
					}else{
						Utils.toastMsg(getActivity(), R.string.save_feedback_error);
					}

                }
			},userid , content, phone, "1");



	}
	
}
