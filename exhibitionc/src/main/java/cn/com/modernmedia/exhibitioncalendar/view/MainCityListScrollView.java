package cn.com.modernmedia.exhibitioncalendar.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.CalendarListActivity;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel.HouseOrCity;

/**
 * top_menu view
 *
 * @author lusiyuan
 */
public class MainCityListScrollView extends RelativeLayout {

    /**
     * 由于可能是独立栏目，并且绑定在列表上，导致每次切换的时候都还原，所以设置成静态变量
     */
    public static int selectPosition = -1;
    private Context mContext;
    private HorizontalScrollView scrollView;
    private LinearLayout layout;


    public MainCityListScrollView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public MainCityListScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        scrollView = new HorizontalScrollView(mContext);
        layout = new LinearLayout(mContext);

        scrollView.addView(layout);
        addView(scrollView);


    }

    public void setData(List<HouseOrCity> tagInfos) {
        if (!ParseUtil.listNotNull(tagInfos)) return;
        layout.removeAllViews();
        scrollView.removeAllViews();

        for (int i = 0; i < tagInfos.size(); i++) {
            final int position = i;
            final HouseOrCity tagInfo = tagInfos.get(i);
            ViewHolder viewHolder = ViewHolder.get(mContext, null, R.layout.item_city);

            View icon = viewHolder.getView(R.id.city_icon);
            TextView name = viewHolder.getView(R.id.city_name);
            TextView time = viewHolder.getView(R.id.city_time);
            ImageView tian = viewHolder.getView(R.id.city_tian);
            name.setText(tagInfo.getTagName());
            String ttt = Tools.getTimeFromZone(tagInfo.getTimeZone());
            time.setText(ttt);
            int hhh = Integer.valueOf(ttt.substring(0, 2));
            if (hhh < 8 || hhh > 20) {
                tian.setImageResource(R.mipmap.wanshang);
            } else {
                tian.setImageResource(R.mipmap.baitian);
            }
            MyApplication.finalBitmap.display(icon, UrlMaker.HOST + tagInfo.getBackImg().getSourceV6PlusImg());
            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, CalendarListActivity.class);
                    i.putExtra("list_tagid", tagInfo.getTagId());
                    i.putExtra("list_tagname", tagInfo.getTagName());
                    mContext.startActivity(i);
                }
            });


            layout.addView(viewHolder.getConvertView());
        }
        scrollView.addView(layout);

    }

}

