package com.yi4all.davidapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.yi4all.davidapp.db.ContentModel;
import com.yi4all.davidapp.db.ContentType;
import com.yi4all.davidapp.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HallListFragment extends PullToRefreshListFragment{
	
	private final static String LOGTAG = "HallListFragment";
	
	private AppAdapter mAdapter;

	private ArrayList<ContentModel> contents = new ArrayList<ContentModel>();
	
	private Date lastUpdateTime;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Set a listener to be invoked when the list should be refreshed.
		getPullToRefreshListView()
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						
						String label = getActivity().getString(R.string.last_update_time) + DateUtils.formatDateTime(getActivity(),lastUpdateTime.getTime(),
							    DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

							    // Update the LastUpdatedLabel
							    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
							    
							loadListByPage(ContentType.HALL);

					}
				});

		// You can also just use mPullRefreshListFragment.getListView()
		ListView actualListView = getPullToRefreshListView().getRefreshableView();

		mAdapter = new AppAdapter(getActivity());

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		actualListView.setAdapter(mAdapter);

//		actualListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO download and install apk
//				((BaseActivity)getActivity()).downloadApk(contents.get(position).getDownurl());
//			}
//		});

	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		setListShown(true);
		// TODO:async to fetch contents from service and complete refresh of PTR
				loadListByPage(ContentType.HALL);
				
	}
	
	/**
	 * 统一刷新列表数据及加载更多数据两种模式： 1.根据页数查询本地数据 2.如果有本地数据则刷新列表（加载数据到最后并跳转至新数据的第一条记录）
	 * 及获取第一条数据的创建/更新时间（假设取数据时以时间倒序排列） 3.根据页数及第一条数据的创建/更新时间（如果没有数据则NULL）获取远程数据列表
	 * 4.如果有返回数据，则在本地库新增或修改列表记录， 然后将返回数据插入到本地数据之前（如果没有本地数据，则是当前列表之末）并刷新列表
	 * 
	 */
	private void loadListByPage(final ContentType type) {
		lastUpdateTime = new Date();
		
			Date lastUpdateDate = null;
			if(contents.size() > 0){
				lastUpdateDate = contents.get(0).getCreatedAt();
			}

				List<ContentModel> moreApps = ((BaseActivity)getActivity()).getService().getContentsByType(type);
				if (moreApps != null && moreApps.size() > 0) {
					Log.d(LOGTAG, "getContentsByType size:" + moreApps.size());
	
					lastUpdateDate = moreApps.get(0).getCreatedAt();
					
					contents.clear();
					
					// update
					contents.addAll(moreApps);
	
				}

			// TODO:notify updating local db
			((BaseActivity)getActivity()).getService().getContentsByTypeRemote(
                    new Handler() {
				@Override
				public void handleMessage(Message msg) {

					// msg construction:
					// what: tab value, arg1: success flag(0 - success, 1 -
					// fail), arg2: page, obj: data of list
					if (msg.arg1 == 0) {
						// load updated app into list
						List<ContentModel> remoteMoreIssues = (List<ContentModel>) msg.obj;
						if (remoteMoreIssues != null
								&& remoteMoreIssues.size() > 0) {
							Log.d(LOGTAG, "getAppsByTabRemote size:"
									+ remoteMoreIssues.size());

							contents.clear();
							
							contents.addAll(remoteMoreIssues);

							mAdapter.notifyDataSetChanged();
						} else {
								// TODO:notify no updated data
								if (getPullToRefreshListView().isRefreshing()) {
								Utils.toastMsg(getActivity(), R.string.refresh_no_data);
								}
						}
					}else{
						Utils.toastMsg(getActivity(), (String)msg.obj);
					}

					//complete refreshing
					if (getPullToRefreshListView().isRefreshing()) {
						getPullToRefreshListView().onRefreshComplete();
					}
                }
			}, type, lastUpdateDate);


		mAdapter.notifyDataSetChanged();

	}
	
	private class AppAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public AppAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return contents.size();
		}

		public ContentModel getItem(int i) {
			return contents.get(i);
		}

		public long getItemId(int i) {
			return i;
		}

		public View getView(final int position, View convertView, ViewGroup vg) {
			if (contents == null || position < 0 || position > contents.size())
				return null;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.hall_list_item, null);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}

			// other normal row
			final ContentModel am = contents.get(position);

			// set triangle for the add
			holder.icon.setImageUrl(((BaseActivity)getActivity()).getService().getURL() + "/showImage/" + am.getSmallPic(), ApplicationController
                    .getInstance().getmImageLoader());
			holder.name.setText(am.getName());
			holder.description.setText(String.valueOf(am.getDescription()));
			holder.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO:call am.getPhoneNumber()
                	Intent callIntent = new Intent(Intent.ACTION_CALL);
                	callIntent.setData(Uri.parse("tel:" + am.getPhoneNumber()));
                	startActivity(callIntent);
                }
            });
            holder.sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO:text am.getPhoneNumber()
                	Intent callIntent = new Intent(Intent.ACTION_VIEW);
                	callIntent.setData(Uri.parse("sms:" + am.getPhoneNumber()));
                	startActivity(callIntent);
                }
            });

			return (convertView);
		}

	}

	class ViewHolder {
		NetworkImageView icon = null;
		TextView name = null;
		TextView description = null;
		TextView phone = null;
        TextView sms = null;

		ViewHolder(View base) {
			this.icon = (NetworkImageView) base.findViewById(R.id.service_row_iconImg);
			this.name = (TextView) base.findViewById(R.id.service_row_name);
			this.description = (TextView) base.findViewById(R.id.service_row_description);
			this.phone = (TextView) base.findViewById(R.id.service_row_phone);
			this.sms = (TextView) base.findViewById(R.id.service_row_sms);
		}
	}
}
