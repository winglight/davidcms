package com.yi4all.davidapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.yi4all.davidapp.db.OrderModel;
import com.yi4all.davidapp.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderFragment extends Fragment {
	
	private final static String LOGTAG = "IntroFragment";

    private String currentOrderItem;
    
    private LinearLayout btnPanel;
    
    private EditText memoTxt;
//    private EditText nameTxt;
    private EditText phoneTxt;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_order, container, false);

        btnPanel = (LinearLayout) v.findViewById(R.id.orderBtnPanel);
        btnPanel.removeAllViews();
        
//        nameTxt = (EditText) v.findViewById(R.id.nameTxt);
        phoneTxt = (EditText) v.findViewById(R.id.phoneTxt);
        memoTxt = (EditText) v.findViewById(R.id.memoTxt);
        
        String[] orderItems = ((BaseActivity)getActivity()).getService().getOrderItems();
        if(orderItems != null){
        	int pos = orderItems.length / 2 + (orderItems.length % 2);
        	
        	for(int i=0 ; i < orderItems.length ; i++){
        		final String title = orderItems[i];
        		final TextView txt = new TextView(getActivity());
				txt.setTextColor(Color.WHITE);
				if(i == (pos - 1)){
					txt.setTextSize(getActivity().getResources().getDimensionPixelSize(R.dimen.buttons_selected_text_dimen));
					txt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getActivity().getResources().getDrawable(R.drawable.buttons_selected_indicator));
					
					currentOrderItem = title;
				}else{
					txt.setTextSize(getActivity().getResources().getDimensionPixelSize(R.dimen.buttons_text_dimen));
				}
				txt.setGravity(Gravity.CENTER);
				txt.setPadding(5, 0, 5, 0);
				txt.setText(title);
				txt.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						currentOrderItem = title;

						for(int j=0; j < btnPanel.getChildCount() ; j++){
							TextView tv = (TextView) btnPanel.getChildAt(j);
							tv.setTextSize(getActivity().getResources().getDimensionPixelSize(R.dimen.buttons_text_dimen));
							tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
						}
						
						txt.setTextSize(getActivity().getResources().getDimensionPixelSize(R.dimen.buttons_selected_text_dimen));
						txt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getActivity().getResources().getDrawable(R.drawable.buttons_selected_indicator));
						txt.invalidate();
					}
				});
				btnPanel.addView(txt);
        	}
        	btnPanel.invalidate();
        }
        
        Button submit = (Button) v.findViewById(R.id.okBtn);
        submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				String name = nameTxt.getText().toString();
				String phone = phoneTxt.getText().toString();
				String memo = memoTxt.getText().toString();
				
				if( phone == null || phone.length() == 0){
					Utils.toastMsg(getActivity(), R.string.order_phone_hint);
					
					return;
				}
				
				if(memo == null  || memo.length() == 0){
					Utils.toastMsg(getActivity(), R.string.order_memo_hint);
					
					return;
				}
				
				submit("", phone, memo);
			}
		});
        
        Button hitory = (Button) v.findViewById(R.id.historyBtn);
        hitory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				OrderHistoryFragment newFragment = new OrderHistoryFragment();
				((BaseActivity)getActivity()).popupFragment(newFragment);
			}
		});

        return v;
    }
    
    private void submit(String name, String phone, final String memo) {
    	final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.waiting), true, false); 
		
			((BaseActivity)getActivity()).getService().saveOrder(
                    new Handler() {
				@Override
				public void handleMessage(Message msg) {

					progressDialog.dismiss();
					
					// msg construction:
					// what: tab value, arg1: success flag(0 - success, 1 -
					// fail), arg2: page, obj: data of list
					if (msg.arg1 == 0) {
						// load updated app into list
						Utils.toastMsg(getActivity(), R.string.save_order_success);
						
						OrderModel om = new OrderModel();
				    	om.setCreatedAt(new Date());
				    	om.setContent(memo);
				    	om.setService(currentOrderItem);
				    	
				    	((BaseActivity)getActivity()).getService().getDbService().createOrder(om);
					}else{
						Utils.toastMsg(getActivity(), R.string.save_order_fail);
					}

                }
			}, "", name, phone, memo, currentOrderItem);



	}
	
	@Override
	public void onResume() {
		super.onResume();

	}
	
}
