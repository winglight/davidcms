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
import com.yi4all.davidapp.fragment.MarketingDetailFragment;
import com.yi4all.davidapp.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZongDeatilFragment extends Fragment {

	private final static String LOGTAG = "ZongDeatilFragment";

	private ListView list;
	private AppAdapter mAdapter;

	private LinePerson person;

	private int currentMode = 0; // 0 - M; 1 - CunDan ; 2 - CunJi ; 3 - ZhuanMa

	private ArrayList<ZongData> zongs = new ArrayList<ZongData>();

	private Date lastUpdateTime;

	// UI elements
	private TextView personNameTxt;
	private TextView personAmountTxt;
	private TextView listTitleTxt;
	private TextView totalTxt;
	private TextView groupAmountShowTxt;
	private TextView groupAmountTxt;

	public static ZongDeatilFragment getInstance(LinePerson person) {
		ZongDeatilFragment f = new ZongDeatilFragment();
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

		final View v = inflater.inflate(R.layout.fragment_member_zong_detail,
				container, false);

		personNameTxt = (TextView) v.findViewById(R.id.zong_person_name_txt);
		personAmountTxt = (TextView) v
				.findViewById(R.id.zong_person_amount_txt);
		listTitleTxt = (TextView) v.findViewById(R.id.zong_list_title_txt);
		totalTxt = (TextView) v.findViewById(R.id.zong_total_txt);
		groupAmountShowTxt = (TextView) v
				.findViewById(R.id.zong_group_amount_show_txt);
		groupAmountTxt = (TextView) v.findViewById(R.id.zong_group_amount_txt);
		
		list = (ListView) v.findViewById(R.id.zong_detail_list);

		mAdapter = new AppAdapter(getActivity());

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		list.setAdapter(mAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO download and install apk
				((MemberActivity) getActivity()).goinFragment(null);
			}
		});

		initRadio(v);

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();

		((MemberActivity) getActivity())
				.setTitleTxt(R.string.member_title_zong);

		personNameTxt.setText(person.getLineName());
		
		loadPersonAmount();

		loadListByPage();

	}

	private void initRadio(View v) {
		RadioButton mRadio = (RadioButton) v.findViewById(R.id.mRadio);
		mRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				if (checked) {
					currentMode = 0;
					loadUIByMode();
					loadListByPage();
				}

			}
		});

		RadioButton danRadio = (RadioButton) v.findViewById(R.id.danRadio);
		danRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				if (checked) {
					currentMode = 1;
					loadUIByMode();
					loadListByPage();
				}

			}
		});

		RadioButton jiRadio = (RadioButton) v.findViewById(R.id.jiRadio);
		jiRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				if (checked) {
					currentMode = 2;
					loadUIByMode();
					loadListByPage();
				}

			}
		});

		RadioButton maRadio = (RadioButton) v.findViewById(R.id.maRadio);
		maRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				if (checked) {
					currentMode = 3;
					loadUIByMode();
					loadListByPage();
				}

			}
		});
	}

	private void loadUIByMode() {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				switch (currentMode) {
				case 0: {
					listTitleTxt.setText(R.string.member_tab_m);
					groupAmountShowTxt.setText(R.string.member_group_total_m);
					break;
				}
				case 1: {
					listTitleTxt.setText(R.string.member_tab_cundan);
					groupAmountShowTxt
							.setText(R.string.member_group_total_cundan);
					break;
				}
				case 2: {
					listTitleTxt.setText(R.string.member_tab_cunji);
					groupAmountShowTxt
							.setText(R.string.member_group_total_cunji);
					break;
				}
				case 3: {
					listTitleTxt.setText(R.string.member_tab_zhuanma);
					groupAmountShowTxt
							.setText(R.string.member_group_total_zhuanma);
					break;
				}
				}
			}
		});

	}
	
	private int calculateTotal(List<ZongData> list){
		int total = 0;
		for(ZongData zong : list){
			total += zong.getAmount();
		}
		return total;
	}

	private void loadPersonAmount() {
		// 2. 查询下线长期额
		((BaseActivity) getActivity()).getService().getMemberLineAmount(
				new Handler() {
					@Override
					public void handleMessage(Message msg) {

						// msg construction:
						// arg1: success flag(0 - success, 1 -
						// fail),  obj: data of person
						if (msg.arg1 == 0) {
							
							// load updated app into list
							LinePerson remoteMoreIssues = (LinePerson) msg.obj;
							if (remoteMoreIssues != null) {
								person.setLineName(remoteMoreIssues.getLineName());
								person.setLongTerm(remoteMoreIssues.getLongTerm());
								
								personNameTxt.setText(person.getLineName());
								personAmountTxt.setText(person.getLongTerm());
								
							} else {
								// TODO:notify no updated data
								Utils.toastMsg(getActivity(),
										R.string.refresh_no_data);
							}
							
						} else {
							Utils.toastMsg(getActivity(), (String) msg.obj);
						}

					}
				}, person.getLineCode());
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
		((BaseActivity) getActivity()).getService().getMemberZongs(
				new Handler() {
					@Override
					public void handleMessage(Message msg) {

						// msg construction:
						// arg1: success flag(0 - success, 1 -
						// fail), arg2: total amount, obj: data of list
						if (msg.arg1 == 0) {
							
							int groupTotal = msg.arg2;
							int total = 0;
							
							groupAmountTxt.setText(String.valueOf(groupTotal));
							
							// load updated app into list
							List<ZongData> remoteMoreIssues = (List<ZongData>) msg.obj;
							if (remoteMoreIssues != null
									&& remoteMoreIssues.size() > 0) {
								
								total = calculateTotal(remoteMoreIssues);

								zongs.clear();

								zongs.addAll(remoteMoreIssues);

								mAdapter.notifyDataSetChanged();
							} else {
								// TODO:notify no updated data
								zongs.clear();
								mAdapter.notifyDataSetChanged();
								
								Utils.toastMsg(getActivity(),
										R.string.refresh_no_data);
							}
							
							totalTxt.setText(String.valueOf(total));
						} else {
							Utils.toastMsg(getActivity(), (String) msg.obj);
						}

					}
				}, person.getLineCode(), currentMode + 1);

		mAdapter.notifyDataSetChanged();

	}

	private class AppAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public AppAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return zongs.size();
		}

		public ZongData getItem(int i) {
			return zongs.get(i);
		}

		public long getItemId(int i) {
			return i;
		}

		public View getView(final int position, View convertView, ViewGroup vg) {
			if (zongs == null || position < 0 || position > zongs.size())
				return null;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.zong_list_item, null);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}

			// other normal row
			final ZongData am = zongs.get(position);

			// set triangle for the add
			holder.name.setText(am.getHallName());
			holder.amount.setText(String.valueOf(am.getAmount()));

			return (convertView);
		}

	}

	class ViewHolder {
		TextView name = null;
		TextView amount = null;

		ViewHolder(View base) {
			this.name = (TextView) base.findViewById(R.id.zong_row_name);
			this.amount = (TextView) base.findViewById(R.id.zong_row_amount);
		}
	}
}
