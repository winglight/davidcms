package com.yi4all.davidapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yi4all.davidapp.BaseActivity;
import com.yi4all.davidapp.R;
import com.yi4all.davidapp.util.Utils;

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
				
				if(content == null || content.length() == 0 ){
					Utils.toastMsg(getActivity(), R.string.feed_memo_hint);
					
					return;
				}
				
				saveFeedback(content, phone);
				
			}
		});
        
        Button reset = (Button) v.findViewById(R.id.resetBtn);
        reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				contentTxt.setText("");
				phoneTxt.setText("");
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
