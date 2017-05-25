package cn.com.modernmedia.exhibitioncalendar.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.AddActivity;

/**
 * Created by Eva. on 17/4/25.
 */

public class ChooseAlarmPopView {

    private Context mContext;
    private PopupWindow window;

    public ChooseAlarmPopView(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_alarm_pop, null);
        window = new PopupWindow(view, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setAnimationStyle(R.style.fetch_image_popup_anim);
        window.update();
        window.setBackgroundDrawable(new

                BitmapDrawable());
        window.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        view.findViewById(R.id.alarm1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AddActivity) mContext).changeAlarm(0);
                window.dismiss();

            }
        });
        view.findViewById(R.id.alarm2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AddActivity) mContext).changeAlarm(1);
                window.dismiss();
            }
        });

        view.findViewById(R.id.alarm_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });

    }


}
