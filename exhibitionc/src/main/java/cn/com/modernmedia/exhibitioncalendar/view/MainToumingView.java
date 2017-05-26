package cn.com.modernmedia.exhibitioncalendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 首页监听左右滑动的透明遮盖层
 * Created by Eva. on 2017/5/26.
 */

public class MainToumingView extends RelativeLayout implements GestureDetector.OnGestureListener {
    private int verticalMinDistance = 80;

    private int minVelocity = 0;

    private MyLayoutCallBack myLayoutCallBack;
    private GestureDetector gestureDetector;
    Context context;

    public void setCallBack(MyLayoutCallBack myLayoutCallBack) {
        this.myLayoutCallBack = myLayoutCallBack;
    }

    public MainToumingView(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context, this);
        this.context = context;
    }

    public MainToumingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("pingan", "onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        //        if (distanceX < -verticalMinDistance) {
        //            Log.d("pingan", "向右手势");
        //            myLayoutCallBack.scrollByX(false);
        //            return true;
        //        } else if (distanceX > verticalMinDistance) {
        //
        //            Log.d("pingan", "向左手势");
        //            myLayoutCallBack.scrollByX(true);
        //            return true;
        //        }
        return false;


        //        else if (distanceY < -verticalMinDistance) {
        //            Log.d("pingan", "向下手势");
        //            myLayoutCallBack.scrollByY(20);
        //
        //        } else if (distanceY > verticalMinDistance) {
        //
        //            Log.d("pingan", "向上手势");
        //            myLayoutCallBack.scrollByY(-20);
        //
        //        }

    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            Log.d("pingan", "向右手势");
            myLayoutCallBack.scrollByX(false);
            return true;

        } else if ((e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity)) {
            Log.d("pingan", "向左手势");

            myLayoutCallBack.scrollByX(true);
            return true;
        }

        //        else if (e1.getY() - e2.getY() > verticalMinDistance && Math.abs(velocityY) > minVelocity) {
        //            Log.d("pingan", "向上手势");
        //            myLayoutCallBack.scrollByY(-20);
        //
        //        } else if ((e2.getY() - e1.getY() > verticalMinDistance && Math.abs(velocityY) > minVelocity)) {
        //
        //            Log.d("pingan", "向下手势");
        //            myLayoutCallBack.scrollByY(20);
        //
        //        }

        return false;
    }


    public interface MyLayoutCallBack {
        public void scrollByX(boolean ifLeft);

        public void scrollByY(int i);
    }
}
