package cn.ryando.ui2_5;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zhongkun on 2017/4/5.
 */

public final class FlingBehavior extends AppBarLayout.Behavior {
    private static final int TOP_CHILD_FLING_THRESHOLD = 3;
    private boolean isPositive;

    public FlingBehavior() {
    }

    public FlingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean mIsRvFling = false;
    private boolean mIsFlingConsumed = false;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private View mTarget;
    private float mVelocityX;
    private float mVelocityY;
    private boolean mConsumed;
    private float velocityX;
    private float velocityY;
    private RecyclerView.OnScrollListener mRvScrollListener;

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        mCoordinatorLayout = coordinatorLayout;
        mAppBarLayout = child;
//        mTarget = target;
        mVelocityX = velocityX;
        mVelocityY = velocityY;
        mConsumed = consumed;

        System.out.println("velocity" + consumed + velocityY + isPositive + (velocityY > 0 && !isPositive || velocityY < 0 && isPositive));
        if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
            velocityY = velocityY * -1;
        }
        if (target instanceof RecyclerView && velocityY < 0) {
            final RecyclerView recyclerView = (RecyclerView) target;
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            System.out.println("childAdapterPosition" + childAdapterPosition);
            consumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD;
            mIsRvFling = true;
            mIsFlingConsumed = consumed;
            if (mRvScrollListener == null) {
                mRvScrollListener = new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && mIsRvFling && mIsFlingConsumed) {
                            final View firstView = recyclerView.getChildAt(0);
                            final int viewPosition = recyclerView.getChildAdapterPosition(firstView);
                            if (viewPosition == 0) {
                                try {
                                    System.out.println("onScrollStateChanged");
                                    FlingBehavior.this.invokeSuperOnNestedFling(mCoordinatorLayout, mAppBarLayout, recyclerView, mVelocityX, mVelocityY, false);
                                    mCoordinatorLayout = null;
                                    mAppBarLayout = null;
                                } catch (Exception e) {
                                    Log.e("FlingBehavior", "exception inside onScrollStateChanged", e);
                                }
                            }
                        }
                    }
                };
                recyclerView.addOnScrollListener(mRvScrollListener);
            }

        } else {
            mIsRvFling = false;
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    private void invokeSuperOnNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }


    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);

        isPositive = dy > 0;

        System.out.println("onNestedPreScroll " + isPositive);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
        super.onStopNestedScroll(coordinatorLayout, abl, target);
        System.out.println("childAdapterPosition stopScroll");
//        if (mIsRvFling && mIsFlingConsumed && target instanceof RecyclerView) {
//            System.out.println("childAdapterPosition stopScroll recyclerview");
//
//            final RecyclerView recyclerView = (RecyclerView) target;
//            final View firstChild = recyclerView.getChildAt(0);
//            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
//            if (childAdapterPosition <= TOP_CHILD_FLING_THRESHOLD) {
//                super.onNestedFling(coordinatorLayout, abl, recyclerView, velocityX, velocityY, false);
//            }
//        }
    }
}