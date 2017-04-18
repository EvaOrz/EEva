package cn.com.modernmedia.exhibitioncalendar.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.AddActivity;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.api.HandleFavApi;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;

/**
 * Created by Eva. on 17/4/12.
 */

public class ListItemMenuView {


    private Context mContext;
    private PopupWindow window;
    private CalendarModel calendarModel;
    private View parent;

    public ListItemMenuView(Context context, CalendarModel calendarModel, View parent) {
        this.mContext = context;
        this.calendarModel = calendarModel;
        this.parent = parent;
        init();
    }

    private void init() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_mylist_menu, null);
        window = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        window.setBackgroundDrawable(new

                BitmapDrawable());
        window.showAsDropDown(parent, 180, 0);
        view.findViewById(R.id.menu_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareDialog(mContext, calendarModel);
            }
        });
        view.findViewById(R.id.menu_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, AddActivity.class);
                i.putExtra("add_detail", calendarModel);
                mContext.startActivity(i);
            }
        });
        view.findViewById(R.id.menu_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiController.getInstance(mContext).handleFav(mContext, HandleFavApi.HANDLE_DELETE, calendarModel.getItemId(), calendarModel.getCoverImg(), calendarModel.getStartTime(), new FetchEntryListener() {
                    @Override
                    public void setData(Entry entry) {

                    }
                });
            }
        });
        view.findViewById(R.id.menu_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });


    }
}
