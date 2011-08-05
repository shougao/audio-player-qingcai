package com.shougao.Audio.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

public class ScrollableViewGroup extends ViewGroup {

	private Scroller mScroller;
	private int mDefaultScreen;
	private int mCurrentScreen;
	private int mTouchSlop;

	public ScrollableViewGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/*
	  public ScrollableViewGroup(Context paramContext)
	  {
	    super(paramContext);
	    initViewGroup();
	  }
	  public ScrollableViewGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	  {
	    super(paramContext, paramAttributeSet, paramInt);
	    initViewGroup();
	  }	  */
	  public ScrollableViewGroup(Context paramContext, AttributeSet paramAttributeSet)
	  {
	    super(paramContext, paramAttributeSet);
	    initViewGroup();
	  }
	  
	  private void initViewGroup()
	  {
	    Context localContext = getContext();
	    AccelerateDecelerateInterpolator localAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
	    Scroller localScroller = new Scroller(localContext, localAccelerateDecelerateInterpolator);
	    this.mScroller = localScroller;
	    int i = this.mDefaultScreen;
	    this.mCurrentScreen = i;
	    int j = ViewConfiguration.get(getContext()).getScaledTouchSlop() << 1;
	    this.mTouchSlop = j;
	  }

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}

}
