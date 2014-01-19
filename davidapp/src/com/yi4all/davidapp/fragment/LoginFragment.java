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

public class LoginFragment extends Fragment {
	
	private final static String LOGTAG = "LoginFragment";

    private EditText pwdTxt;
    private EditText nameTxt;
    private EditText verifyTxt;
    private TextView verifyShowTxt;
    
    private int lineType = 1; //上线=1;下线=0
    
    private SecureRandom random = new SecureRandom();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_login, container, false);

        nameTxt = (EditText) v.findViewById(R.id.login_user_txt);
        verifyTxt = (EditText) v.findViewById(R.id.login_verify_txt);
        pwdTxt = (EditText) v.findViewById(R.id.login_pwd_txt);
        verifyShowTxt = (TextView) v.findViewById(R.id.login_verify_show_txt);
        
        TextView changeVerifyTxt = (TextView) v.findViewById(R.id.login_change_btn);
        changeVerifyTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadVerifyTxt();
			}
		});
        
        Button loginBtn = (Button) v.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = nameTxt.getText().toString();
				String pwd = pwdTxt.getText().toString();
				String verp = verifyTxt.getText().toString();
				String verpShow = verifyShowTxt.getText().toString();
				
				if(name == null || name.length() == 0 || pwd == null || pwd.length() == 0 || verp == null  || verp.length() == 0){
					Utils.toastMsg(getActivity(), R.string.changepwd_null_error);
					
					return;
				}
				
				if(!verp.toLowerCase().equals(verpShow)){
					Utils.toastMsg(getActivity(), R.string.login_verify_error);
					
					return;
				}
				
				changePwd(name, pwd);
				
				
			}
		});
        
        RadioButton upRadio = (RadioButton) v.findViewById(R.id.upRadio);
        upRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				if(checked){
					lineType = 1;
				}else{
					lineType = 0;
				}
				
			}
		});
        
        return v;
    }
    
    private void changePwd(final String name, String pwd) {
    	final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.waiting), true, false); 
		
			// TODO:notify updating local db
			((BaseActivity)getActivity()).getService().memberLogin(
                    new Handler() {
				@Override
				public void handleMessage(Message msg) {
					
					progressDialog.dismiss();
					
					// msg construction:
					// what: tab value, arg1: success flag(0 - success, 1 -
					// fail), arg2: page, obj: data of list
					if (msg.arg1 == 0) {
						
						Intent intent = new Intent();
		                intent.setClass(getActivity(), MemberActivity.class);
		                LinePerson person = new LinePerson();
		                person.setLineName(name);
		                person.setLineCode(name);
		                person.setIsData("N");
		                person.setUp(lineType == 1);
		                intent.putExtra(MemberActivity.EXTRA_DATA, person);
						getActivity().startActivity(intent);
					}else{
						Utils.toastMsg(getActivity(), R.string.login_userpwd_error);
					}

                }
			}, name, pwd, lineType);



	}
    
    private void loadVerifyTxt(){
    	new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				String str = new BigInteger(130, random).toString(32).substring(0,4);
		    	verifyShowTxt.setText(str);
		    	nameTxt.setText("");
		        verifyTxt.setText("");
		        pwdTxt.setText("");
			}
		});
    	
    }
	
	@Override
	public void onResume() {
		super.onResume();

		loadVerifyTxt();
	}
	
}
