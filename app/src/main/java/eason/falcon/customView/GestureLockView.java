package eason.falcon.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by falcon on 2016/7/4.
 */
public class GestureLockView extends View {

    enum Mode {
        STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP;
    }

    private Mode mCurrentStatus = Mode.STATUS_NO_FINGER;

    private int mWidth;
    private int mHeight;
    private int mRadius;
    private int mStrokeWidth;
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;

    private float mArrowRate = 0.333f;
    private int mArrowDegree = -1;
    private Path mArrowPath;
    private float mInnerCircleRadiusRate = 0.3f;
    private int mColorNoFingerInner;
    private int mColorNoFingerOutter;
    private int mColorFingerOn;
    private int mColorFingerUp;


    public GestureLockView(Context context, int colorNoFingerInner, int colorNoFingerOutter, int colorFingerOn, int colorFingerUp) {
        super(context);
        this.mColorNoFingerInner = colorNoFingerInner;
        this.mColorNoFingerOutter = colorNoFingerOutter;
        this.mColorFingerOn = colorFingerOn;
        this.mColorFingerUp = colorFingerUp;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPath = new Path();
    }

    protected void onDraw(Canvas canvas) {
        switch (mCurrentStatus) {
            case STATUS_FINGER_ON:
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mColorFingerOn);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerCircleRadiusRate, mPaint);
                break;
            case STATUS_FINGER_UP:
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mColorFingerUp);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX,mCenterY,mRadius,mPaint);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(mCenterX,mCenterY,mRadius*mInnerCircleRadiusRate,mPaint);
                drawArrow(canvas);
                break;
            case STATUS_NO_FINGER:
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mColorNoFingerOutter);
                canvas.drawCircle(mCenterX,mCenterY,mRadius,mPaint);
                mPaint.setColor(mColorNoFingerInner);
                canvas.drawCircle(mCenterX,mCenterY,mRadius*mInnerCircleRadiusRate,mPaint);
                break;
        }
    }

    private void drawArrow(Canvas canvas)
    {
        if(mArrowDegree!=-1)
        {
            mPaint.setStyle(Paint.Style.FILL);
            canvas.save();
            canvas.rotate(mArrowDegree,mCenterX,mCenterY);
            canvas.drawPath(mArrowPath,mPaint);
            canvas.restore();
        }
    }

    public void setMode(Mode mode)
    {
        this.mCurrentStatus=mode;
        invalidate();
    }
    public void setArrowDegree(int degree)
    {
        this.mArrowDegree=degree;
    }

    public int getArrowDegree()
    {
        return this.mArrowDegree;
    }
}
