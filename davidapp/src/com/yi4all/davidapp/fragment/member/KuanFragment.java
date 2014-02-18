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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.yi4all.davidapp.db.dto.LinePerson;
import com.yi4all.davidapp.fragment.MarketingDetailFragment;
import com.yi4all.davidapp.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KuanFragment extends Fragment{
	
	private final static String LOGTAG = "KuanFragment";
	
	private PullToRefreshListView list;
	private AppAdapter mAdapter;

	private ArrayList<LinePerson> persons = new ArrayList<LinePerson>();
	
	private Date lastUpdateTime = new Date();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

		final View v = inflater.inflate(R.layout.fragment_member_zong, container, false);

		list = (PullToRefreshListView) v.findViewById(R.id.zong_line_list);
        
				list.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						
						String label = getActivity().getString(R.string.last_update_time) + DateUtils.formatDateTime(getActivity(),lastUpdateTime.getTime(),
							    DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

							    // Update the LastUpdatedLabel
							    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
							    
							loadListByPage();

					}
				});

		// You can also just use mPullRefreshListFragment.getListView()
		ListView actualListView = list.getRefreshableView();

		mAdapter = new AppAdapter(getActivity());

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		actualListView.setAdapter(mAdapter);

		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO download and install apk
				((MemberActivity)getActivity()).goinFragment(KuanDetailFragment.getInstance(persons.get(position-1)));
			}
		});

        return v;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		
		((MemberActivity)getActivity()).setTitleTxt(R.string.member_title_daikuan);

		list.setRefreshing();
				
	}
	
	/**
	 * 统一刷新列表数据及加载更多数据两种模式： 1.根据页数查询本地数据 2.如果有本地数据则刷新列表（加载数据到最后并跳转至新数据的第一条记录）
	 * 及获取第一条数据的创建/更新时间（假设取数据时以时间倒序排列） 3.根据页数及第一条数据的创建/更新时间（如果没有数据则NULL）获取远程数据列表
	 * 4.如果有返回数据，则在本地库新增或修改列表记录， 然后将返回数据插入到本地数据之前（如果没有本地数据，则是当前列表之末）并刷新列表
	 * 
	 */
	private void loadListByPage() {
		lastUpdateTime = new Date();
		
			// TODO:notify updating local db
			((BaseActivity)getActivity()).getService().getMemberLinePersons(
                    new Handler() {
				@Override
				public void handleMessage(Message msg) {

					// msg construction:
					// what: tab value, arg1: success flag(0 - success, 1 -
					// fail), arg2: page, obj: data of list
					if (msg.arg1 == 0) {
						// load updated app into list
						List<LinePerson> remoteMoreIssues = (List<LinePerson>) msg.obj;
						if (remoteMoreIssues != null
								&& remoteMoreIssues.size() > 0) {

							persons.clear();
							
							persons.addAll(remoteMoreIssues);

							mAdapter.notifyDataSetChanged();
						} else {
								// TODO:notify no updated data
								if (list.isRefreshing()) {
								Utils.toastMsg(getActivity(), R.string.refresh_no_data);
								}
						}
					}else{
						Utils.toastMsg(getActivity(), (String)msg.obj);
					}

					//complete refreshing
					if (list.isRefreshing()) {
						list.onRefreshComplete();
					}
                }
			}, ((MemberActivity)getActivity()).getCurrentUser().getLineCode());


		mAdapter.notifyDataSetChanged();

	}
	
	private class AppAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public AppAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return persons.size();
		}

		public LinePerson getItem(int i) {
			return persons.get(i);
		}

		public long getItemId(int i) {
			return i;
		}

		public View getView(final int position, View convertView, ViewGroup vg) {
			if (persons == null || position < 0 || position > persons.size())
				return null;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.lineperson_list_item, null);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}

			// other normal row
			final LinePerson am = persons.get(position);

			// set triangle for the add
			holder.name.setText(am.getLineName());
			if(!am.isHasData()){
				holder.name.setTextColor(getResources().getColor(R.color.member_line_data_text_color));
			}else{
				holder.name.setTextColor(getResources().getColor(R.color.member_menu_text_color));
			}
			return (convertView);
		}

	}

	class ViewHolder {
		TextView name = null;

		ViewHolder(View base) {
			this.name = (TextView) base.findViewById(R.id.line_row_name);
		}
	}
}
