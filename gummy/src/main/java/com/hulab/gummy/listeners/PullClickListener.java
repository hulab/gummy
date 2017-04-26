package com.hulab.gummy.listeners;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Nebneb on 23/03/2017.
 */

public abstract class PullClickListener implements View.OnTouchListener {

    private View mView = null;
    private float mInitialPosition;
    private float mStartPoint;
    private float dY;

    // Settings
    private boolean mTriggerPull = false;
    private float mFactor = 0.6f;
    private int mAmplitude = 100;
    private int mAnimationDuration = 100;
    private int mTriggerAt = 100;
    private int mPullThreshold = 0;

    private GestureDetectorCompat gestureDetector;
    private Interpolator mInterpolator = new AccelerateInterpolator();

    /**
     * This method will be triggered when a pull event is detected and validated.
     */
    protected abstract void onPull();

    /**
     * This method will be triggered when a click event is detected.
     */
    protected abstract void onClick();

    /**
     * Set the interpolator of the back animation.
     *
     * @param interpolator the interpolator of the back animation
     * @return this to allow chaining.
     */
    public PullClickListener setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
        return this;
    }

    /**
     * Set the resistance factor of the pull action. 1f full resistance (mView will not move)
     * and 0f means no resistance (mView will follow the touch event.
     *
     * @param factor is the resistance factor, if not set, the factor is 0.5f by default.
     * @return this to allow chaining.
     */
    public PullClickListener setResistance(float factor) {
        this.mFactor = 1 - factor;
        return this;
    }

    /**
     * Set the amplitude of the drag animation in pixels.
     *
     * @param px is the number of pixel the mView can move. If this amplitude is not set, the
     *           default value is 80px.
     * @return this to allow chaining.
     */
    public PullClickListener setAmplitude(int px) {
        this.mAmplitude = px;
        return this;
    }

    /**
     * Set the number of px the user needs to pull to start animating the view.
     *
     * @param px the amount of px to start animating the view.
     * @return this to allow chaining.
     */
    public PullClickListener setPullThreshold(int px) {
        this.mPullThreshold = px;
        return this;
    }

    /**
     * Set the duration of the back-to-normal animation.
     *
     * @param duration is the duration of the animation in milli-secs.
     *                 If this duration is not set, the default value is 150ms.
     * @return this to allow chaining.
     */
    public PullClickListener setAnimationDuration(int duration) {
        this.mAnimationDuration = duration;
        return this;
    }

    /**
     * Set the minimum distance you move to activate the pull action.
     *
     * @param triggerAt is the minimum distance you have to pull to trigger the
     *                  pull action.
     * @return this to allow chaining.
     */
    public PullClickListener setTriggerAt(int triggerAt) {
        this.mTriggerAt = triggerAt;
        return this;
    }

    public PullClickListener(Context context) {

        gestureDetector = new GestureDetectorCompat(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        onClick();
                        return super.onSingleTapUp(e);
                    }
                });
        mTriggerAt = mAmplitude;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mView == null) {
            mView = v;
            mInitialPosition = v.getY();
        }

        if (!gestureDetector.onTouchEvent(event))
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mInitialPosition = mView.getY();
                    mStartPoint = event.getRawY();
                    dY = mInitialPosition;
                    break;
                case MotionEvent.ACTION_MOVE:
                    dY = (event.getRawY() - mStartPoint) * mFactor;
                    mTriggerPull = dY >= mTriggerAt;

                    if (dY <= 0)
                        break;
                    if (dY < mPullThreshold)
                        break;
                    if (dY <= mAmplitude)
                        v.setY(mInitialPosition - mPullThreshold + dY);
                    break;
                case MotionEvent.ACTION_UP:
                    if (mTriggerPull) {
                        onPull();
                        mTriggerPull = false;
                    }
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(mView.getY(), mInitialPosition);
                    valueAnimator.setDuration(mAnimationDuration);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            mView.setY(value);
                            mView.requestLayout();
                        }
                    });

                    valueAnimator.start();
                    valueAnimator.setInterpolator(mInterpolator);
                    return false;
            }
        return true;
    }
}
