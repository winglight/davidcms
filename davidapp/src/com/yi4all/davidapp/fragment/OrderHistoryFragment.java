package com.yi4all.davidapp.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yi4all.davidapp.BaseActivity;
import com.yi4all.davidapp.R;
import com.yi4all.davidapp.db.OrderModel;
import com.yi4all.davidapp.db.dto.ZongDetailData;
import com.yi4all.davidapp.util.DateUtils;

/**
 * Created by ChenYu on 13-8-8.
 */
public class OrderHistoryFragment extends DialogFragment {

	public final static String SM_DATA = "server";

	private ListView list;
	
	private TextView tv;

	private AppAdapter mAdapter;

	private List<OrderModel> zongDetails = new ArrayList<OrderModel>();

	public OrderHistoryFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(R.string.order_history);

		final View v = inflater.inflate(R.layout.fragment_order_history,
				container, false);

		Button backBtn = (Button) v.findViewById(R.id.historyBackBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		tv = (TextView) v.findViewById(R.id.order_detail_nodata_txt);

		list = (ListView) v.findViewById(R.id.order_detail_list);

		mAdapter = new AppAdapter(getActivity());

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		list.setAdapter(mAdapter);
		
		loadListByPage();

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	private void loadListByPage() {
		zongDetails = ((BaseActivity)getActivity()).getService().getDbService().getOrders();
		
		if(zongDetails != null && zongDetails.size() > 0){
			mAdapter.notifyDataSetChanged();
			
			tv.setVisibility(View.GONE);
		}else{
			tv.setVisibility(View.VISIBLE);
		}
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

		public OrderModel getItem(int i) {
			return zongDetails.get(i);
		}

		public long getItemId(int i) {
			return i;
		}

		public View getView(final int position, View convertView, ViewGroup vg) {
			if (zongDetails == null || position < 0
					|| position > zongDetails.size())
				return null;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.order_history_list_item,
						null);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}

			// other normal row
			final OrderModel am = zongDetails.get(position);

			// set triangle for the add
			holder.content.setText(String.valueOf(am.getContent()));
			holder.type.setText(am.getService());
			holder.date.setText(DateUtils.defaultFormat(am.getCreatedAt()));

			return (convertView);
		}

	}

	class ViewHolder {
		TextView type = null;
		TextView content = null;
		TextView date = null;

		ViewHolder(View base) {
			this.type = (TextView) base.findViewById(R.id.order_row_type);
			this.content = (TextView) base
					.findViewById(R.id.order_row_content);
			this.date = (TextView) base.findViewById(R.id.order_row_date);
		}
	}
}
