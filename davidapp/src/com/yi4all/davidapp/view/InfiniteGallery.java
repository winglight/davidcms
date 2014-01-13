package com.yi4all.davidapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Gallery;

/**
 * This is the custom gallery view
 * To reference this from XML you need the package name followed by the class name
 * <i>ex: &lt;com.blundell.tut.ui.view.InfiniteGallery/&gt; </i>
 * 
 * @author Blundell
 */
public class InfiniteGallery extends Gallery {

	public InfiniteGallery(Context context) {
		super(context);
		init();
	}

	public InfiniteGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InfiniteGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init(){
		// These are just to make it look pretty
		setSpacing(10);
		setHorizontalFadingEdgeEnabled(false);
	}
	
	/**
	 * Set the InfiniteGallery to use images cached on the file system
	 * once this method is called the InfiniteGallery will set the adapter and draw it's images
	 * @param tabs an array of paths <i>ex: /mnt/blundell/cache/ic_launcher.png</i>
	 */
	public void setTabs(String[] tabs){
		setAdapter(new InfiniteAdapter(getContext(), tabs));
		setSelection((getCount() / 2));
	}
	
}