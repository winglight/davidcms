package com.yi4all.davidapp.fragment.member;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yi4all.davidapp.ApplicationController;
import com.yi4all.davidapp.BaseActivity;
import com.yi4all.davidapp.MemberActivity;
import com.yi4all.davidapp.R;
import com.yi4all.davidapp.db.ContentModel;
import com.yi4all.davidapp.db.ContentType;
import com.yi4all.davidapp.db.dto.LinePerson;
import com.yi4all.davidapp.db.dto.ZongData;
import com.yi4all.davidapp.db.dto.ZongDetailData;
import com.yi4all.davidapp.fragment.MarketingDetailFragment;
import com.yi4all.davidapp.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZongDeatilDetailFragment extends Fragment {

	private final static String LOGTAG = "ZongDeatilDetailFragment";

	private ListView list;
	private AppAdapter mAdapter;

	private LinePerson person;
	
	private ZongData zong;

	private int currentMode = 0; // 0 - M; 1 - CunDan ; 2 - CunJi ; 3 - ZhuanMa

	private ArrayList<ZongDetailData> zongDetails = new ArrayList<ZongDetailData>();

	private Date lastUpdateTime;

	// UI elements
	private TextView downlineNameTxt;
	private TextView hallNametTxt;
	private TextView title1Txt;
	private TextView title2Txt;
	private TextView title3Txt;
	private TextView totalTxt;
	private TextView maBlueTxt;
	private TextView maWhiteTxt;
	private TextView maTotalTxt;
	private LinearLayout totalPanel;
	private LinearLayout maPanel;

	public static ZongDeatilDetailFragment getInstance(LinePerson person, ZongData zong, int mode) {
		ZongDeatilDetailFragment f = new ZongDeatilDetailFragment();
		f.person = person;
		f.zong = zong;
		f.currentMode = mode;

		return f;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View v = inflater.inflate(R.layout.fragment_member_zong_detail_detail,
				container, false);

		downlineNameTxt = (TextView) v.findViewById(R.id.zong_downline_name_txt);
		hallNametTxt = (TextView) v
				.findViewById(R.id.zong_detail_hall_txt);
		title1Txt = (TextView) v.findViewById(R.id.zong_detail_title1_txt);
		title2Txt = (TextView) v.findViewById(R.id.zong_detail_title2_txt);
		title3Txt = (TextView) v.findViewById(R.id.zong_detail_title3_txt);
		totalTxt = (TextView) v.findViewById(R.id.zong_detail_total_txt);
		maBlueTxt = (TextView) v
				.findViewById(R.id.zong_zhuanma_blue_txt);
		maWhiteTxt = (TextView) v
				.findViewById(R.id.zong_zhuanma_white_txt);
		maTotalTxt = (TextView) v
				.findViewById(R.id.zong_zhuanma_total_txt);
		totalPanel = (LinearLayout) v.findViewById(R.id.zong_total_panel);
		maPanel = (LinearLayout) v.findViewById(R.id.zong_zhuanma_total_panel);
		
		list = (ListView) v.findViewById(R.id.zong_detail_list);

		mAdapter = new AppAdapter(getActivity());

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		list.setAdapter(mAdapter);

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();

		downlineNameTxt.setText(person.getLineName());
		
		hallNametTxt.setText(zong.getHallName());
		
		loadUIByMode();

		loadListByPage();

	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		((MemberActivity)getActivity()).setTitleTxt(R.string.member_title_zong);
	}

	private void loadUIByMode() {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				title1Txt.setVisibility(View.VISIBLE);
				title2Txt.setVisibility(View.VISIBLE);
				list.setVisibility(View.VISIBLE);
				
				switch (currentMode) {
				case 0: {
					title1Txt.setText(R.string.member_client_name);
					title2Txt.setText(R.string.member_detail_qianm);
//					title3Txt.setText(R.string.member_detail_memo);
					title3Txt.setVisibility(View.GONE);
					totalPanel.setVisibility(View.VISIBLE);
					maPanel.setVisibility(View.GONE);
					
					((MemberActivity) getActivity())
					.setTitleTxt(R.string.member_title_m);
					break;
				}
				case 1: {
					title1Txt.setText(R.string.member_client_name);
					title2Txt.setText(R.string.member_tab_cundan);
//					title3Txt.setText(R.string.member_detail_memo);
					title3Txt.setVisibility(View.GONE);
					totalPanel.setVisibility(View.VISIBLE);
					maPanel.setVisibility(View.GONE);
					
					((MemberActivity) getActivity())
					.setTitleTxt(R.string.member_title_cundan);
					break;
				}
				case 2: {
					title1Txt.setText(R.string.member_detail_time);
					title2Txt.setText(R.string.member_detail_money);
					title3Txt.setText(R.string.member_detail_memo);
					title3Txt.setVisibility(View.GONE);
					totalPanel.setVisibility(View.VISIBLE);
					maPanel.setVisibility(View.GONE);
					
					((MemberActivity) getActivity())
					.setTitleTxt(R.string.member_title_cunji);
					break;
				}
				case 3: {
					title1Txt.setVisibility(View.GONE);
					title2Txt.setVisibility(View.GONE);
					title3Txt.setVisibility(View.GONE);
					totalPanel.setVisibility(View.GONE);
					maPanel.setVisibility(View.VISIBLE);
					list.setVisibility(View.GONE);
					
					((MemberActivity) getActivity())
					.setTitleTxt(R.string.member_title_zhuanma);
					break;
				}
				}
			}
		});

	}
	

	/**
	 * 统一刷新列表数据及加载更多数据两种模式： 1.根据页数查询本地数据 2.如果有本地数据则刷新列表（加载数据到最后并跳转至新数据的第一条记录）
	 * 及获取第一条数据的创建/更新时间（假设取数据时以时间倒序排列） 3.根据页数及第一条数据的创建/更新时间（如果没有数据则NULL）获取远程数据列表
	 * 4.如果有返回数据，则在本地库新增或修改列表记录， 然后将返回数据插入到本地数据之前（如果没有本地数据，则是当前列表之末）并刷新列表
	 * 
	 */
	private void loadListByPage() {
		lastUpdateTime = new Date();

		// 根据当前模式：currentMode,查询列表数据
		((BaseActivity) getActivity()).getService().getMemberZongDetails(
				new Handler() {
					@Override
					public void handleMessage(Message msg) {

						if(msg.what != currentMode) return;
						
						// msg construction: what: current mode
						// arg1: success flag(0 - success, 1 -
						// fail), arg2: total amount, obj: data of list
						if (msg.arg1 == 0) {
							
							// load updated app into list
							List<ZongDetailData> remoteMoreIssues = (List<ZongDetailData>) msg.obj;
							if (remoteMoreIssues != null
									&& remoteMoreIssues.size() > 0) {
								
								zongDetails.clear();

								if(msg.what != 3){
									
									zongDetails.addAll(remoteMoreIssues);
									
									int total = 0;
									for(ZongDetailData detail : remoteMoreIssues){
										total += detail.getAmount();
									}
									
									totalTxt.setText(Utils.formatNumber(total));
								}else{
									list.setVisibility(View.GONE);
									
									//calculate totals
									int blueTotal = 0;
									int whiteTotal = 0;
									int total = 0;
									for(ZongDetailData detail : remoteMoreIssues){
										blueTotal += detail.getBTotal();
										whiteTotal += detail.getWTotal();
										total += detail.getTotal();
									}
									//show zhuanma totals
									maBlueTxt.setText(Utils.formatNumber(String.valueOf(blueTotal)));
									maWhiteTxt.setText(Utils.formatNumber(String.valueOf(whiteTotal)));
									maTotalTxt.setText(Utils.formatNumber(String.valueOf(total)));
								}

								mAdapter.notifyDataSetChanged();
							} else {
								// TODO:notify no updated data
								zongDetails.clear();
								mAdapter.notifyDataSetChanged();
								
								Utils.toastMsg(getActivity(),
										R.string.refresh_no_data);
							}
							
						} else {
							Utils.toastMsg(getActivity(), (String) msg.obj);
						}

					}
				}, person.getLineCode(), zong.getHallID(), currentMode + 1);

		mAdapter.notifyDataSetChanged();

	}

	private class AppAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public AppAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return zongDetails.size();
		}

		public ZongDetailData getItem(int i) {
			return zongDetails.get(i);
		}

		public long getItemId(int i) {
			return i;
		}

		public View getView(final int position, View convertView, ViewGroup vg) {
			if (zongDetails == null || position < 0 || position > zongDetails.size())
				return null;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.zong_detail_list_item, null);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}

			// other normal row
			final ZongDetailData am = zongDetails.get(position);

			// set triangle for the add
			holder.amount.setText(Utils.formatNumber(am.getAmount()));
			if(currentMode == 2){
				holder.memo.setVisibility(View.GONE);
//				holder.memo.setText(am.getRemark());
				
				holder.name.setText(am.getPostTime());
			}else{
				holder.name.setText(am.getClientName());
				holder.memo.setVisibility(View.GONE);
			}

			return (convertView);
		}

	}

	class ViewHolder {
		TextView name = null;
		TextView amount = null;
		TextView memo = null;

		ViewHolder(View base) {
			this.name = (TextView) base.findViewById(R.id.zong_detail_row_name);
			this.amount = (TextView) base.findViewById(R.id.zong_detail_row_amount);
			this.memo = (TextView) base.findViewById(R.id.zong_detail_row_memo);
		}
	}
}
