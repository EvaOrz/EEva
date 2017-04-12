package cn.com.modernmedia.exhibitioncalendar.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.AddActivity;

/**
 * Created by Eva. on 17/4/11.
 */

public class AddPopView {


    private Context mContext;
    private PopupWindow window;
    private int type;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private int aa, bb, cc, dd, ee;


    public AddPopView(Context context, int type) {
        this.mContext = context;
        this.type = type;// 1：time 2：date
        init();
    }

    private void init() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_add_pop, null);
        window = new PopupWindow(view, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setAnimationStyle(R.style.fetch_image_popup_anim);
        window.update();
        window.setBackgroundDrawable(new

                BitmapDrawable());
        window.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        view.findViewById(R.id.add_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        view.findViewById(R.id.add_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((AddActivity) mContext).changePop(aa, bb, cc, dd, ee);
                window.dismiss();
            }
        });
        if (type == 1) {
            datePicker.setVisibility(View.GONE);
            timePicker.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            timePicker.setVisibility(View.GONE);
            datePicker.setVisibility(View.VISIBLE);
        }

        // 初始化DatePicker组件，初始化时指定监听器
        datePicker.init(2017, 2, 2, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker arg0, int year, int month, int day) {
                aa = year;
                bb = month;
                cc = day;
            }
        });
        // 为TimePicker指定监听器
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                dd = hourOfDay;
                ee = minute;

            }
        });


    }

}
