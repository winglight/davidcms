package com.yi4all.davidapp.fragment.member;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.yi4all.davidapp.db.dto.Hall;
import com.yi4all.davidapp.db.dto.LinePerson;
import com.yi4all.davidapp.db.dto.ShangDetailData;
import com.yi4all.davidapp.db.dto.ZongData;
import com.yi4all.davidapp.db.dto.ZongDetailData;
import com.yi4all.davidapp.fragment.MarketingDetailFragment;
import com.yi4all.davidapp.util.DateUtils;
import com.yi4all.davidapp.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShangDetailFragment extends Fragment {

	private final static String LOGTAG = "ShangDetailFragment";

	private ListView list;
	private AppAdapter mAdapter;

	private LinePerson person;

	private Hall hall;

	private String currentDate;

	private ArrayList<ShangDetailData> shangDetails = new ArrayList<ShangDetailData>();

	// UI elements
	private TextView dateTxt;

	public static ShangDetailFragment getInstance(LinePerson person, Hall hall) {
		ShangDetailFragment f = new ShangDetailFragment();
		f.person = person;
		f.hall = hall;
		f.currentDate = DateUtils.formatDate(new Date());

		return f;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View v = inflater.inflate(R.layout.fragment_member_shang_detail,
				container, false);

		dateTxt = (TextView) v.findViewById(R.id.current_date_txt);

		list = (ListView) v.findViewById(R.id.shang_detail_list);

		mAdapter = new AppAdapter(getActivity());

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		list.setAdapter(mAdapter);
		
		initDateUI(v);

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();

		((MemberActivity) getActivity()).setTitleTxt(hall.getHallName());

		loadListByPage();

	}
	
	private void initDateUI(View v){
		final Date now = new Date();
		final Date yes = DateUtils.addDateDays(now, -1);
		currentDate = DateUtils.defaultFormat(yes);
		currentDate = "2013/04/30";
		dateTxt.setText(currentDate);
			
		RadioButton yeRadio = (RadioButton) v.findViewById(R.id.yesterdayRadio);
		yeRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				if (checked) {
					currentDate = DateUtils.defaultFormat(yes);
				}else{
					currentDate = DateUtils.defaultFormat(now);
				}
				
				dateTxt.setText(currentDate);
				
				loadListByPage();

			}
		});
		
		ImageView prevBtn = (ImageView) v.findViewById(R.id.prevBtn);
		prevBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date old = DateUtils.defaultParse(currentDate);
				currentDate = DateUtils.defaultFormat(DateUtils.addDateDays(old, -1));
				
				dateTxt.setText(currentDate);
				
				loadListByPage();
			}
		});
		
		ImageView nextBtn = (ImageView) v.findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date old = DateUtils.defaultParse(currentDate);
				currentDate = DateUtils.defaultFormat(DateUtils.addDateDays(old, 1));
				
				dateTxt.setText(currentDate);
				
				loadListByPage();
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

		// 根据当前模式：currentMode,查询列表数据
		((BaseActivity) getActivity()).getService().getMemberShangDetails(
				new Handler() {
					@Override
					public void handleMessage(Message msg) {

						// msg construction:
						// arg1: success flag(0 - success, 1 -
						// fail), obj: data of list
						if (msg.arg1 == 0) {

							// load updated app into list
							List<ShangDetailData> remoteMoreIssues = (List<ShangDetailData>) msg.obj;
							if (remoteMoreIssues != null
									&& remoteMoreIssues.size() > 0) {

								shangDetails.clear();

								shangDetails.addAll(remoteMoreIssues);

								mAdapter.notifyDataSetChanged();
							} else {
								// TODO:notify no updated data
								shangDetails.clear();
								mAdapter.notifyDataSetChanged();

								Utils.toastMsg(getActivity(),
										R.string.refresh_no_data);
							}

						} else {
							Utils.toastMsg(getActivity(), (String) msg.obj);
						}

					}
				}, person.getLineCode(), hall.getHallNo(), currentDate);

		mAdapter.notifyDataSetChanged();

	}

	private class AppAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public AppAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return shangDetails.size();
		}

		public ShangDetailData getItem(int i) {
			return shangDetails.get(i);
		}

		public long getItemId(int i) {
			return i;
		}

		public View getView(final int position, View convertView, ViewGroup vg) {
			if (shangDetails == null || position < 0
					|| position > shangDetails.size())
				return null;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.shang_detail_list_item, null);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}

			// other normal row
			final ShangDetailData am = shangDetails.get(position);

			// set triangle for the add
			holder.principle.setText(am.getPrincipal());
			holder.name.setText(am.getLineName());
			holder.client.setText(am.getClientName());
			holder.memo.setText(am.getRemark());
			holder.updown.setText(am.getUpDown());

			if ("Y".equals(am.getIsOnline())) {
				holder.updown.setVisibility(View.VISIBLE);
			} else {
				holder.updown.setVisibility(View.GONE);
			}

			return (convertView);
		}

	}

	class ViewHolder {
		TextView name = null;
		TextView principle = null;
		TextView updown = null;
		TextView client = null;
		TextView memo = null;
		ImageView onlineImg = null;

		ViewHolder(View base) {
			this.name = (TextView) base.findViewById(R.id.shang_row_name_txt);
			this.principle = (TextView) base
					.findViewById(R.id.shang_row_principle_txt);
			this.updown = (TextView) base
					.findViewById(R.id.shang_row_updown_txt);
			this.client = (TextView) base
					.findViewById(R.id.shang_row_client_txt);
			this.memo = (TextView) base.findViewById(R.id.shang_row_memo_txt);
			this.onlineImg = (ImageView) base
					.findViewById(R.id.shang_row_online_img);
		}
	}
}
