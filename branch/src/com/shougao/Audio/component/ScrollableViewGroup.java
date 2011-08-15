package com.shougao.Audio.component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

/*
 * �ο�androidԴ�룬workspace.java
 * android lanuchЧ����
 */
public class ScrollableViewGroup extends ViewGroup {

	private Scroller mScroller;
	private int mDefaultScreen = 0;
	private int mCurrentScreen;
	private int mNextScreen = -1;
	private int mTouchSlop;
	private static final int SNAP_VELOCITY = 600;
	private OnCurrentViewChangedListener mOnCurrentViewChangedListener;
	// ��¼���ϴ���갴��ʱ��XYֵ����ACTION_MOVE�и�ֵ��
	private float mLastMotionX;
	private float mLastMotionY;
	// ��¼����״̬
	private int mTouchState = TOUCH_STATE_REST;
	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;
	public int index = 0;
	private VelocityTracker mVelocityTracker;
	private static final String TAG = "ScrollLayout";
	private boolean mFirstLayout = true;

	public ScrollableViewGroup(Context paramContext) {
		super(paramContext);
		Log.e(TAG, "ScrollableViewGroup 1");
		// TODO Auto-generated constructor stub
	}

	public ScrollableViewGroup(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		initViewGroup();
		Log.e(TAG, "ScrollableViewGroup 2");
	}

	public ScrollableViewGroup(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		initViewGroup();
		Log.e(TAG, "ScrollableViewGroup 3");
	}

	private void initViewGroup() {
		Log.e(TAG, "initViewGroup");
		Context localContext = getContext();
		AccelerateDecelerateInterpolator localAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
		Scroller localScroller = new Scroller(localContext,
				localAccelerateDecelerateInterpolator);
		this.mScroller = localScroller;
		int i = this.mDefaultScreen;
		this.mCurrentScreen = i;
		int j = ViewConfiguration.get(getContext()).getScaledTouchSlop() << 1;
		this.mTouchSlop = j;// ��ʾ��Ҫ�������پ����ʱ��ŷ�����һҳ
	}


//	  ���ò��֣�������ͼ˳���������
//	  ��ʾÿһ��chlid view��layout����֮ǰ��Ҫ����measure



	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onLayout" + changed);
		// if (changed) { //changed��ʾ��Ļview�ı仯��ȥ���жϣ�ֻҪ���Ŀ�仯�����»���
		int childLeft = 0;
		final int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				// System.out.println("childLeft,childWidth,childView.getMeasuredHeight()========="+childLeft+","
				// +childWidth + ","+ childView.getMeasuredHeight());
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight());
				childLeft += childWidth;
				// System.out.println("new  childLeft========="+childLeft);
			}
		}
	}

//	  onMeasure ������Ļ�Ĵ�С��ÿ��ֵ��ÿ��childView�Լ��趨,��Ҫ����MeasureSpec��
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		System.out.println("=======onMeasure");
		Log.e(TAG, "onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ScrollLayout only canmCurScreen run at EXACTLY mode!");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ScrollLayout only can run at EXACTLY mode!");
		}

		// The children are given the same width and height as the scrollLayout
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		// Log.e(TAG, "moving to screen "+mCurrentScreen);
		scrollTo(mCurrentScreen * width, 0);
	}

	/*
	 * ÿ�ι���һ��㶼��Ҫ����ƫ����
	 * (non-Javadoc)
	 * @see android.view.View#computeScroll()
	 */
	@Override
	public void computeScroll() {
//		Log.e(TAG, "computeScroll");
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	/*
	 * android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 * ��д�˸����onInterceptTouchEvent()����Ҫ��������onTouchEvent()����֮ǰ����
	 * touch�¼���������down��up��move�¼���
	 * ��onInterceptTouchEvent()����trueʱ����onTouchEvent()��
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.e(TAG, "onInterceptTouchEvent");
//		System.out.println(">>>>>>>onInterceptTouchEvent index:" + ++index);
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}
		final float x = ev.getX();
		final float y = ev.getY();
		// System.out.println("~~~~~~~~~debug x, y, action:" + x +","+ y + ","
		// +action);
		switch (action) {
		case MotionEvent.ACTION_MOVE:
//			System.out.println("===========onInterceptTouchEvent MOVE.");
			/*
			 * ��¼xy��mLastMotionX��mLastMotionY��ֵ�ľ���ֵ��xDiff��yDiff����
			 * touchSlopʱ����Ϊ�����϶����㹻��ľ��룬��Ļ�Ϳ����ƶ��ˡ�
			 */
			final int xDiff = (int) Math.abs(x - mLastMotionX);
			final int yDiff = (int) Math.abs(y - mLastMotionY);
//			System.out.println("--------------mTouchSlop :" + mTouchSlop);
			final int touchSlop = mTouchSlop;
			boolean xMoved = (xDiff > touchSlop) && (xDiff >= 2 * yDiff);
			boolean yMoved = yDiff > touchSlop;
			if (xMoved || yMoved) {
				if (xMoved) {
					// Scroll if the user moved far enough along the X axis
//					System.out.println("========mTouchState 2:"+ mTouchState);
//					System.out.println("++++++++mTouchState 1:"+ mTouchState);
					mTouchState = TOUCH_STATE_SCROLLING;
				}
			}
			break;
		case MotionEvent.ACTION_DOWN:
//			System.out.println("===========onInterceptTouchEvent DOWN.");
			// Remember location of down touch
			mLastMotionX = x;
			mLastMotionY = y;
//			System.out.println("===============mTouchState 3:" + mTouchState);
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;
		case MotionEvent.ACTION_CANCEL:
//			System.out.println("===========onInterceptTouchEvent CANCEL.");
		case MotionEvent.ACTION_UP:
//			System.out.println("===========onInterceptTouchEvent UP.");
			// Release the drag
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e(TAG, "onTouchEvent");
		// TODO Auto-generated method stub

		if (mVelocityTracker == null) {
			// velocityTracker�������ٴ����ٶȵ��࣬
			// ������Ҫ���ٵ�ʱ��ʹ��obtain()�����������VelocityTracker���һ��ʵ������
			// ʹ��addMovement(MotionEvent)��������ǰ���ƶ��¼����ݸ�VelocityTracker����
			// ʹ��computeCurrentVelocity (int units)���������㵱ǰ���ٶ�
			// ʹ��getXVelocity ()��getYVelocity ()��������õ�ǰ���ٶ�
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.e(TAG, "event down!");
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;

		case MotionEvent.ACTION_MOVE:
			Log.e(TAG, "event move!");
			int deltaX = (int) (mLastMotionX - x);
			mLastMotionX = x;

			scrollBy(deltaX, 0);
			break;

		case MotionEvent.ACTION_UP:
			Log.e(TAG, "event : up");
			// if (mTouchState == TOUCH_STATE_SCROLLING) {
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();

			Log.e(TAG, "velocityX:" + velocityX);
//			System.out.println("mCurScreen, mTouchState" + mCurrentScreen + ","
//					+ mTouchState);
//			System.out.println("velocityX, SNAP_VELOCITY" + velocityX + ","
//					+ SNAP_VELOCITY);
			if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
				// Fling enough to move left
				Log.e(TAG, "snap left");
				snapToScreen(mCurrentScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& mCurrentScreen < getChildCount() - 1) {
				// Fling enough to move right
				Log.e(TAG, "snap right");
				snapToScreen(mCurrentScreen + 1);
			} else {
				snapToDestination();
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			// }
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.e(TAG, "event cancel!");
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		return true;
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page.
	 */
	public void snapToDestination() {
		Log.e(TAG, "snapToDestination");
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	public void snapToScreen(int whichScreen) {
		Log.e(TAG, "snapToScreen");
		// get the valid layout page
//		System.out.println("input whichScreen:" + whichScreen);
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
//		System.out.println("new whichScreen:" + whichScreen);
//		System.out.println("getScrollX():whichScreen*getWidth():"
//				+ getScrollX() + "," + (whichScreen * getWidth()));
		if (getScrollX() != (whichScreen * getWidth())) {
			final int delta = whichScreen * getWidth() - getScrollX();
//			System.out.println("getScrollX():delta:" + getScrollX() + ","
//					+ delta);
			mScroller.startScroll(getScrollX(), 0, delta, 0,
					Math.abs(delta) * 2);
			mCurrentScreen = whichScreen;
			invalidate(); // Redraw the layout
		}
	}

	public void setOnCurrentViewChangedListener(
			OnCurrentViewChangedListener paramOnCurrentViewChangedListener) {
		this.mOnCurrentViewChangedListener = paramOnCurrentViewChangedListener;
	}

	public abstract interface OnCurrentViewChangedListener {
		public abstract void onCurrentViewChanged(View paramView, int paramInt);
	}

}