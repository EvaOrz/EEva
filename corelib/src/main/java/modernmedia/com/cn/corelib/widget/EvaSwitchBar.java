package modernmedia.com.cn.corelib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import modernmedia.com.cn.corelib.R;


/**
 * EvaSwitchBar 仿iphone滑动开关组件，仿百度魔图滑动开关组件 组件分为三种状态：打开、关闭、正在滑动<br/>
 * 使用方法：
 * <p>
 * <pre>
 * EvaSwitchBar EvaSwitchBar = new EvaSwitchBar(this);
 * EvaSwitchBar.setOnSwitchChangedListener(onSwitchChangedListener);
 * linearLayout.addView(EvaSwitchBar);
 * </pre>
 * <p>
 * 注：也可以加载在xml里面使用
 *
 * @author scott
 */
public class EvaSwitchBar extends View implements View.OnClickListener {
    private Bitmap mSwitchBottom, mSwitchThumb, mSwitchFrame, mSwitchMask;
    private float mCurrentX = 0;
    private boolean mSwitchOn;// 开关默认是开着的
    private int mMoveLength;// 最大移动距离
    private float mLastX = 0;// 第一次按下的有效区域

    private Rect mDest = null;// 绘制的目标区域大小
    private Rect mSrc = null;// 截取源图片的大小
    private int mDeltX = 0;// 移动的偏移量
    private Paint mPaint = null;
    private OnChangeListener mListener = null;
    private boolean mFlag = false;

    public EvaSwitchBar(Context context) {
        this(context, null);
        init();
    }

    public EvaSwitchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public EvaSwitchBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化相关资源
     */
    public void init() {
        mSwitchBottom = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_bottom);
        mSwitchThumb = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_btn);
        mSwitchFrame = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_frame);
        mSwitchMask = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_mask);
        setOnClickListener(this);
        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        mMoveLength = mSwitchBottom.getWidth() - mSwitchFrame.getWidth();
        mDest = new Rect(0, 0, mSwitchFrame.getWidth(), mSwitchFrame.getHeight());
        mSrc = new Rect();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(255);
        mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSwitchFrame.getWidth(), mSwitchFrame.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDeltX > 0 || mDeltX == 0 && mSwitchOn) {
            if (mSrc != null) {
                mSrc.set(-mDeltX, 0, mSwitchFrame.getWidth() - mDeltX, mSwitchFrame.getHeight());
            }
        } else if (mDeltX < 0 || mDeltX == 0 && !mSwitchOn) {
            if (mSrc != null) {
                mSrc.set(mMoveLength - mDeltX, 0, mSwitchBottom.getWidth() - mDeltX, mSwitchFrame.getHeight());
            }

        }
        int count = canvas.saveLayer(new RectF(mDest), null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        canvas.drawBitmap(mSwitchBottom, mSrc, mDest, null);
        canvas.drawBitmap(mSwitchThumb, mSrc, mDest, null);
        canvas.drawBitmap(mSwitchFrame, 0, 0, null);
        canvas.drawBitmap(mSwitchMask, 0, 0, mPaint);
        canvas.restoreToCount(count);
    }

    // [java] view plaincopy在CODE上查看代码片派生到我的代码片
    // <span style="white-space:pre"> </span>//这儿是离屏缓冲，自己感觉类似双缓冲机制吧

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                mDeltX = (int) (mCurrentX - mLastX);
                // 如果开关开着向左滑动，或者开关关着向右滑动（这时候是不需要处理的）
                if ((mSwitchOn && mDeltX < 0) || (!mSwitchOn && mDeltX > 0)) {
                    mFlag = true;
                    mDeltX = 0;
                }

                if (Math.abs(mDeltX) > mMoveLength) {
                    mDeltX = mDeltX > 0 ? mMoveLength : -mMoveLength;
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                if (Math.abs(mDeltX) > 0 && Math.abs(mDeltX) < mMoveLength / 2) {
                    mDeltX = 0;
                    invalidate();
                    return true;
                } else if (Math.abs(mDeltX) > mMoveLength / 2 && Math.abs(mDeltX) <= mMoveLength) {
                    mDeltX = mDeltX > 0 ? mMoveLength : -mMoveLength;
                    mSwitchOn = !mSwitchOn;
                    if (mListener != null) {
                        mListener.onChange(this, mSwitchOn);
                    }
                    invalidate();
                    mDeltX = 0;
                    return true;
                } else if (mDeltX == 0 && mFlag) {
                    // 这时候得到的是不需要进行处理的，因为已经move过了
                    mDeltX = 0;
                    mFlag = false;
                    return true;
                }
                return super.onTouchEvent(event);
            default:
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mListener = listener;
    }

    public interface OnChangeListener {
        public void onChange(EvaSwitchBar sb, boolean state);
    }

    @Override
    public void onClick(View v) {
        mDeltX = mSwitchOn ? mMoveLength : -mMoveLength;
        mSwitchOn = !mSwitchOn;
        if (mListener != null) {
            mListener.onChange(this, mSwitchOn);
        }
        invalidate();
        mDeltX = 0;
    }

    public void setChecked(boolean ifPush) {
        mSwitchOn = ifPush;
        invalidate();
    }
}