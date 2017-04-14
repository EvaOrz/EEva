package cn.com.modernmedia.exhibitioncalendar.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.AddActivity;
import cn.com.modernmedia.exhibitioncalendar.activity.UserCenterActivity;

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
    private TextView cancle, save;


    public AddPopView(Context context, int type, String time) {
        this.mContext = context;
        this.type = type;// 1：time 2：date 3:birthday
        String tt = Tools.format(Long.valueOf(time) * 1000, "yyyy-MM-dd-HH-mm");
        if (!TextUtils.isEmpty(tt)) {
            String[] array = tt.split("-");
            if (array.length > 0) aa = Integer.valueOf(array[0]);
            if (array.length > 1) bb = Integer.valueOf(array[1]);
            if (array.length > 2) cc = Integer.valueOf(array[2]);
            if (array.length > 3) dd = Integer.valueOf(array[3]);
            if (array.length > 4) ee = Integer.valueOf(array[4]);


        }

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
        timePicker.setIs24HourView(true);
        cancle = (TextView) view.findViewById(R.id.add_cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        save = (TextView) view.findViewById(R.id.add_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof AddActivity && (type == 1 || type == 2))
                    ((AddActivity) mContext).changePop(aa, bb, cc, dd, ee);
                else if (mContext instanceof UserCenterActivity && type == 3)
                    ((UserCenterActivity) mContext).setBirth(aa, bb, cc);
                window.dismiss();
            }
        });
        if (type == 1) {
            datePicker.setVisibility(View.GONE);
            timePicker.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            timePicker.setVisibility(View.GONE);
            datePicker.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            timePicker.setVisibility(View.GONE);
            datePicker.setVisibility(View.VISIBLE);
        }


        // 初始化DatePicker组件，初始化时指定监听器
        datePicker.init(aa, bb, cc, new DatePicker.OnDateChangedListener() {

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
