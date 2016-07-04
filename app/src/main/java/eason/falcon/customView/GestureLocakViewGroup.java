package eason.falcon.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import eason.falcon.studydemo.R;

/**
 * Created by falcon on 2016/7/4.
 */
public class GestureLocakViewGroup extends RelativeLayout {

    private GestureLockView[] mGestureLockViews;

    private int mCount=3;

    private ArrayList<Integer> mAnswer=null;

    private List<Integer> mChoose=new ArrayList<Integer>();

    private Paint mPaint;

    private int mMarginBetweenLockView=30;

    private int mGestureLockViewWidth;

    private int mNoFingerInnerCircleColor=0xFF939090;

    private int mNoFingerOuterCircleColr=0xFFE0DBDB;

    private int mFingerOnColor=0xFF378FC9;

    private int mFingerUpColor=0xFFFF0000;

    private int mWidth;

    private int mHeight;

    private Path mPath;

    private int mLastPathX;

    private int mLastPathY;

    private Point mTmpTarget=new Point();//结束位置

    private int mTryTimes=4;//最大尝试次数

    private OnGestureLockViewListener mOnGestureLockViewListener;

    public GestureLocakViewGroup(Context context,AttributeSet attributeSet)
    {
        super(context,attributeSet);
        initResources(context,attributeSet);
    }

    public GestureLocakViewGroup(Context context,AttributeSet attrs,int defStyle)
    {
        super(context,attrs,defStyle);
        initResources(context,attrs);
    }

    private void initResources(Context context,AttributeSet attrs)
    {
        TypedArray a= context.getTheme().obtainStyledAttributes(attrs, R.styleable.GestureLockViewGroup,0,0);
        int n=a.getIndexCount();
        for(int i=0;i<n;++i)
        {
            int attr=a.getIndex(i);
            switch(attr)
            {
                case R.styleable.GestureLockViewGroup_color_no_finger_inner_circle:
                    mNoFingerInnerCircleColor=a.getColor(attr,mNoFingerInnerCircleColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_no_finger_outer_circle:
                    mNoFingerOuterCircleColr=a.getColor(attr,mNoFingerOuterCircleColr);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_on:
                    mFingerOnColor=a.getColor(attr,mFingerOnColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_up:
                    mFingerUpColor=a.getColor(attr,mFingerUpColor);
                    break;
                case R.styleable.GestureLockViewGroup_count:
                    mCount=a.getInt(attr,3);
                    break;
                case R.styleable.GestureLockViewGroup_tryTimes:
                    mTryTimes=a.getInt(attr,5);
                    break;
                default:
                    break;

            }
        }
        a.recycle();

        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPath=new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        mWidth=MeasureSpec.getSize(widthMeasureSpec);
        mHeight=MeasureSpec.getSize(heightMeasureSpec);

        mHeight=mWidth=mWidth<mHeight?mWidth:mHeight;

        if(mGestureLockViews==null)
        {
            mGestureLockViews=new GestureLockView[mCount*mCount];
            mGestureLockViewWidth=(int)(4*mWidth*1.0f/(5*mCount+1));
            mMarginBetweenLockView=(int)(mGestureLockViewWidth*0.25);
            mPaint.setStrokeWidth(mGestureLockViewWidth*029f);
            for(int i=0;i<mGestureLockViews.length;++i)
            {
                mGestureLockViews[i]=new GestureLockView(getContext(),mNoFingerInnerCircleColor,mNoFingerOuterCircleColr,mFingerOnColor,mFingerUpColor);
                mGestureLockViews[i].setId(i+1);
                RelativeLayout.LayoutParams lockerParasm=new RelativeLayout.LayoutParams(mGestureLockViewWidth,mGestureLockViewWidth);

                if(i%mCount!=0)
                {
                    lockerParasm.addRule(RelativeLayout.RIGHT_OF,mGestureLockViews[i-1].getId());
                }
                if(i>mCount-1)
                {
                    lockerParasm.addRule(RelativeLayout.BELOW,mGestureLockViews[i-mCount].getId());
                }

                int rightMargin=mMarginBetweenLockView;
                int bottomMargin=mMarginBetweenLockView;
                int leftMargin=0;
                int topMargin=0;

                if(i>=0&&i<mCount)
                {
                    topMargin=mMarginBetweenLockView;
                }
                if(i%mCount==0)
                {
                    leftMargin=mMarginBetweenLockView;
                }
                lockerParasm.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
                mGestureLockViews[i].setMode(GestureLockView.Mode.STATUS_NO_FINGER);
                addView(mGestureLockViews[i],lockerParasm);
            }
        }


    }

    public interface OnGestureLockViewListener{
        public void onBlockSelected(int cId);
        public void onGestureEvent(boolean matched);
        public void onUnmatchedExceedBoundary();
    }
}
