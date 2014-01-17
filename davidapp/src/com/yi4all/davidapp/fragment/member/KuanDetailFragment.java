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

public class KuanDetailFragment extends Fragment {

	private final static String LOGTAG = "KuanDetailFragment";

	private ListView list;
	private AppAdapter mAdapter;

	private LinePerson person;
	
	private ArrayList<ZongDetailData> zongDetails = new ArrayList<ZongDetailData>();

	private Date lastUpdateTime;

	// UI elements
	private TextView downlineNameTxt;
	private TextView totalTxt;

	public static KuanDetailFragment getInstance(LinePerson person) {
		KuanDetailFragment f = new KuanDetailFragment();
		f.person = person;

		return f;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View v = inflater.inflate(R.layout.fragment_member_kuan_detail,
				container, false);

		downlineNameTxt = (TextView) v.findViewById(R.id.kuan_downline_name_txt);
		totalTxt = (TextView) v.findViewById(R.id.kuan_detail_total_txt);
		
		list = (ListView) v.findViewById(R.id.kuan_detail_list);

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
		
		loadListByPage();

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
		((BaseActivity) getActivity()).getService().getMemberKuanDetails(
				new Handler() {
					@Override
					public void handleMessage(Message msg) {

						// msg construction: 
						// arg1: success flag(0 - success, 1 -
						// fail), obj: data of list
						if (msg.arg1 == 0) {
							
							// load updated app into list
							List<ZongDetailData> remoteMoreIssues = (List<ZongDetailData>) msg.obj;
							if (remoteMoreIssues != null
									&& remoteMoreIssues.size() > 0) {
								
								zongDetails.clear();

									zongDetails.addAll(remoteMoreIssues);
									
									int total = 0;
									for(ZongDetailData detail : remoteMoreIssues){
										total += detail.getAmount();
									}
									
									totalTxt.setText(String.valueOf(total));

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
				}, person.getLineCode());

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
			holder.amount.setText(String.valueOf(am.getAmount()));
				holder.name.setText(am.getClientName());
				holder.memo.setVisibility(View.GONE);

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
