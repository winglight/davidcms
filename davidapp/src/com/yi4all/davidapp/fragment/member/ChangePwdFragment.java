package com.yi4all.davidapp.fragment.member;

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
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChangePwdFragment extends Fragment {
	
	private final static String LOGTAG = "ChangePwdFragment";

    private EditText oldPwdTxt;
    private EditText newPwdTxt;
    private EditText verifyTxt;
    
    private SecureRandom random = new SecureRandom();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_change_pwd, container, false);

        newPwdTxt = (EditText) v.findViewById(R.id.pwd_new_txt);
        verifyTxt = (EditText) v.findViewById(R.id.pwd_verify_txt);
        oldPwdTxt = (EditText) v.findViewById(R.id.pwd_old_txt);
        
        Button submitBtn = (Button) v.findViewById(R.id.changePwdBtn);
        submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String old = oldPwdTxt.getText().toString();
				String newp = newPwdTxt.getText().toString();
				String verp = verifyTxt.getText().toString();
				
				if(old == null || old.length() == 0 || newp == null || newp.length() == 0 || verp == null  || verp.length() == 0){
					Utils.toastMsg(getActivity(), R.string.changepwd_null_error);
					
					return;
				}
				
				if(!verp.equals(newp)){
					Utils.toastMsg(getActivity(), R.string.changepwd_verify_error);
					
					return;
				}
				
				changePwd(old, newp);
			}
		});
        
        return v;
    }
    
    private void changePwd(String old, String newp) {
    	final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.waiting), true, false); 
		
			// TODO:notify updating local db
			((BaseActivity)getActivity()).getService().changePwd(
                    new Handler() {
				@Override
				public void handleMessage(Message msg) {

					progressDialog.dismiss();
					
					// msg construction:
					// what: tab value, arg1: success flag(0 - success, 1 -
					// fail), arg2: page, obj: data of list
					if (msg.arg1 == 0) {
						// load updated app into list
						Utils.toastMsg(getActivity(), R.string.changepwd_success);
						
						((MemberActivity)getActivity()).backFragment(ChangePwdFragment.this);
					}else{
						Utils.toastMsg(getActivity(), R.string.changepwd_fail);
					}

                }
			}, ((MemberActivity)getActivity()).getCurrentUser().getLineCode(), old, newp, ((MemberActivity)getActivity()).getCurrentUser().isUp());



	}
    
    private void loadVerifyTxt(){
    	new Handler().post(new Runnable() {
			
			@Override
			public void run() {
		    	newPwdTxt.setText("");
		        verifyTxt.setText("");
		        oldPwdTxt.setText("");
			}
		});
    	
    }
	
	@Override
	public void onResume() {
		super.onResume();

		loadVerifyTxt();
	}
	
}
