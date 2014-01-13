package com.yi4all.davidapp.view;

import com.yi4all.davidapp.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;


/**
 * This is our custom adapter for the Gallery
 * it will take a list of file paths to use to draw it's children
 * it will allow the gallery to be scrolled in a continuous loop
 * @author Blundell
 */
public class InfiniteAdapter extends BaseAdapter {
	
	/** The context your gallery is running in (usually the activity) */
	private Context mContext;
	/** The array of file paths to draw */
	private final String[] tabs;
	private final TextView[] tabTxts;

	public InfiniteAdapter(Context c, String[] tabs) {
		this.mContext = c;
		this.tabs = tabs;
		this.tabTxts = new TextView[tabs.length];
	}
	
	/**
	 * The count of how many items are in this Adapter
	 * This will return the max number as we want it to scroll as much as possible
	 */
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convertView is always null in android.widget.Gallery
		
		// If we don't have an image path - return an empty view
		if(tabs.length == 0){
			return new TextView(mContext);
		}
		
		// first we calculate the item position in your list, because we have said the adapters size is Integer.MAX_VALUE
		// the position getView gives us is not use-able in its current form, we have to use the modulus operator
		// to work out what number in our 'array of paths' this actually equals
		// Once we have retrieved the Bitmap with decodeFile we will store it in a lazily initialized array
		// This means that each image will only be retrieved once from the file system and cached locally within the adapter
		// next time getView is called with a position that is in the images array it will quickly reference it
		int itemPos = (position % tabs.length);
		TextView txt = tabTxts[itemPos];
		if(txt == null){
				String title = tabs[itemPos];
				txt = new TextView(mContext);
				txt.setTextColor(mContext.getResources().getColor(R.color.tab_text_color));
				txt.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.tab_text_dimen));
				txt.setGravity(Gravity.BOTTOM);
				txt.setPadding(15, 0, 15, 10);
				Gallery.LayoutParams params = new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.MATCH_PARENT);
				txt.setLayoutParams(params);
				txt.setText(title);
		}
		return txt;
	}
}