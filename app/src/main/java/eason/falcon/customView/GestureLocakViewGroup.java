package eason.falcon.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eason.falcon.studydemo.R;

/**
 * Created by falcon on 2016/7/4.
 */
public class GestureLocakViewGroup extends RelativeLayout {

    private GestureLockView[] mGestureLockViews;

    private int mCount = 3;

    private ArrayList<Integer> mAnswer = new ArrayList<Integer>();

    private List<Integer> mChoose = new ArrayList<Integer>();

    private Paint mPaint;

    private int mMarginBetweenLockView = 30;

    private int mGestureLockViewWidth;

    private int mNoFingerInnerCircleColor = 0xFF939090;

    private int mNoFingerOuterCircleColr = 0xFFE0DBDB;

    private int mFingerOnColor = 0xFF378FC9;

    private int mFingerUpColor = 0xFFFF0000;

    private int mWidth;

    private int mHeight;

    private Path mPath;

    private int mLastPathX;

    private int mLastPathY;

    private Point mTmpTarget = new Point();//结束位置

    private int mTryTimes = 4;//最大尝试次数

    private OnGestureLockViewListener mOnGestureLockViewListener;

    public GestureLocakViewGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet,0);
//        initResources(context, attributeSet);
    }

    public GestureLocakViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initResources(context, attrs,defStyle);
    }

    private void initResources(Context context, AttributeSet attrs,int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GestureLockViewGroup, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.GestureLockViewGroup_color_no_finger_inner_circle:
                    mNoFingerInnerCircleColor = a.getColor(attr, mNoFingerInnerCircleColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_no_finger_outer_circle:
                    mNoFingerOuterCircleColr = a.getColor(attr, mNoFingerOuterCircleColr);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_on:
                    mFingerOnColor = a.getColor(attr, mFingerOnColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_up:
                    mFingerUpColor = a.getColor(attr, mFingerUpColor);
                    break;
                case R.styleable.GestureLockViewGroup_count:
                    mCount = a.getInt(attr, 3);
                    break;
                case R.styleable.GestureLockViewGroup_tryTimes:
                    mTryTimes = a.getInt(attr, 5);
                    break;
                default:
                    break;

            }
        }
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        mHeight = mWidth = mWidth < mHeight ? mWidth : mHeight;

        if (mGestureLockViews == null) {
            mGestureLockViews = new GestureLockView[mCount * mCount];
            mGestureLockViewWidth = (int) (4 * mWidth * 1.0f / (5 * mCount + 1));
            mMarginBetweenLockView = (int) (mGestureLockViewWidth * 0.25);
            mPaint.setStrokeWidth(mGestureLockViewWidth * 0.29f);
            for (int i = 0; i < mGestureLockViews.length; ++i) {
                mGestureLockViews[i] = new GestureLockView(getContext(), mNoFingerInnerCircleColor, mNoFingerOuterCircleColr, mFingerOnColor, mFingerUpColor);
                mGestureLockViews[i].setId(i + 1);
                RelativeLayout.LayoutParams lockerParasm = new RelativeLayout.LayoutParams(mGestureLockViewWidth, mGestureLockViewWidth);

                if (i % mCount != 0) {
                    lockerParasm.addRule(RelativeLayout.RIGHT_OF, mGestureLockViews[i - 1].getId());
                }
                if (i > mCount - 1) {
                    lockerParasm.addRule(RelativeLayout.BELOW, mGestureLockViews[i - mCount].getId());
                }

                int rightMargin = mMarginBetweenLockView;
                int bottomMargin = mMarginBetweenLockView;
                int leftMargin = 0;
                int topMargin = 0;

                if (i >= 0 && i < mCount) {
                    topMargin = mMarginBetweenLockView;
                }
                if (i % mCount == 0) {
                    leftMargin = mMarginBetweenLockView;
                }
                lockerParasm.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                mGestureLockViews[i].setMode(GestureLockView.Mode.STATUS_NO_FINGER);
                addView(mGestureLockViews[i], lockerParasm);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                mPaint.setColor(mFingerOnColor);
                mPaint.setAlpha(50);
                GestureLockView child = getChildByPos(x, y);
                if (null != child) {
                    int cId = child.getId();
                    if (!mChoose.contains(cId)) {
                        mChoose.add(cId);
                        child.setMode(GestureLockView.Mode.STATUS_FINGER_ON);
                        if (mOnGestureLockViewListener != null) {
                            mOnGestureLockViewListener.onBlockSelected(cId);
                        }
                            mLastPathX = child.getLeft() / 2 + child.getRight() / 2;
                            mLastPathY = child.getTop() / 2 + child.getBottom() / 2;
                            if (mChoose.size() == 1) {
                                mPath.moveTo(mLastPathX, mLastPathY);
                            } else {
                                mPath.lineTo(mLastPathX, mLastPathY);
                            }
                    }
                }
                mTmpTarget.x = x;
                mTmpTarget.y = y;
                break;
            case MotionEvent.ACTION_UP:
                mPaint.setColor(mFingerUpColor);
                mPaint.setAlpha(50);
                --mTryTimes;

                boolean result = checkAnswers();

                if (null != mOnGestureLockViewListener && mChoose.size() > 0) {
                    mOnGestureLockViewListener.onGestureEvent(result);
                    if (mTryTimes == 0) {
                        mOnGestureLockViewListener.onUnmatchedExceedBoundary();
                    }
                }
                mTmpTarget.x = mLastPathX;
                mTmpTarget.y = mLastPathY;
                changeItemMode();
                for (int i = 0; i + 1 < mChoose.size(); i++) {
                    int childId = mChoose.get(i);
                    int nextChildId = mChoose.get(i + 1);
                    GestureLockView startChild = (GestureLockView) findViewById(childId);
                    GestureLockView nextChild = (GestureLockView) findViewById(nextChildId);

                    int dx = nextChild.getLeft() - startChild.getLeft();
                    int dy = nextChild.getTop() - startChild.getTop();

                    int angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
                    startChild.setArrowDegree(angle);
                }
                if (mAnswer.isEmpty()) {
                    mAnswer.clear();
                    mAnswer.addAll(mChoose);

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "手势设置成功!", Toast.LENGTH_SHORT).show();
                            reset();
                        }
                    }, 2000);
                } else {
                    mAnswer.clear();
                }
                break;

        }
        invalidate();
        return true;
    }

    public void setAnswer(List<Integer> answers)
    {
        this.mAnswer.addAll(answers);
    }

    public void setUnMatchExceedBoundary(int boundary)
    {
        this.mTryTimes=boundary;
    }

    private void changeItemMode() {
        for (GestureLockView gestureLockView : mGestureLockViews) {
            if (mChoose.contains(gestureLockView.getId())) {
                gestureLockView.setMode(GestureLockView.Mode.STATUS_FINGER_UP);
            }
        }
    }

    private void reset() {
        mChoose.clear();
        mPath.reset();
        for (GestureLockView cestureLockView : mGestureLockViews) {
            cestureLockView.setMode(GestureLockView.Mode.STATUS_NO_FINGER);
            cestureLockView.setArrowDegree(-1);
        }
    }

    private boolean checkAnswers() {
        if (mAnswer == null) {
            return true;
        }

        if (mAnswer.size() != mChoose.size()) {
            return false;
        }

        for (int i = 0, len = mAnswer.size(); i < len; ++i) {
            if (mAnswer.get(i) != mChoose.get(i)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkPositionInChild(View child, int x, int y) {
        int padding = (int) (mGestureLockViewWidth * 0.15);
        if (x > child.getLeft() + padding && y > child.getTop() + padding && x < child.getRight() - padding && y < child.getBottom() - padding) {
            return true;
        }
        return false;
    }

    private GestureLockView getChildByPos(int x, int y) {
        for (GestureLockView gestureLockView : mGestureLockViews) {
            if (checkPositionInChild(gestureLockView, x, y)) {
                return gestureLockView;
            }
        }
        return null;
    }

    public void setGestureLockViewListener(OnGestureLockViewListener listener) {
        this.mOnGestureLockViewListener = listener;
    }


    public interface OnGestureLockViewListener {
        public void onBlockSelected(int cId);

        public void onGestureEvent(boolean matched);

        public void onUnmatchedExceedBoundary();
    }
}
