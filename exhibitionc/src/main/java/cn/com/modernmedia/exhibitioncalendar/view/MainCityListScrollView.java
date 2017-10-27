package cn.com.modernmedia.exhibitioncalendar.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.CalendarListActivity;
import cn.com.modernmedia.exhibitioncalendar.activity.CityPickActivity;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel.TagInfo;

/**
 * top_menu view
 *
 * @author lusiyuan
 */
public class MainCityListScrollView extends LinearLayout {

    /**
     * 由于可能是独立栏目，并且绑定在列表上，导致每次切换的时候都还原，所以设置成静态变量
     */
    public static int selectPosition = -1;
    private Context mContext;
    private LinearLayout layout;


    public MainCityListScrollView(Context context) {
        super(context);
        this.mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public MainCityListScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
    }


    public void setData(List<TagInfo> tagInfos) {
        if (!ParseUtil.listNotNull(tagInfos)) return;
        layout = new LinearLayout(mContext);
        int width = (CommonApplication.width - 120) / 5;

        for (final TagInfo tagInfo : tagInfos) {
            ViewHolder viewHolder = ViewHolder.get(mContext, null, R.layout.item_city);
            View icon = viewHolder.getView(R.id.city_icon);
            TextView name = viewHolder.getView(R.id.city_name);
            name.setText(tagInfo.getTagName());

            MyApplication.finalBitmap.display(icon, UrlMaker.HOST + tagInfo.getCityiconWhite().getSourceV6PlusImg());

            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, CalendarListActivity.class);
                    i.putExtra("list_tagid", tagInfo.getTagId());
                    i.putExtra("list_tagname", tagInfo.getTagName());
                    mContext.startActivity(i);

                }
            });

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
            layout.addView(viewHolder.getConvertView(), layoutParams);
        }
        removeAllViews();
        LinearLayout.LayoutParams lll = new LinearLayout.LayoutParams(CommonApplication.width - 120,LayoutParams.WRAP_CONTENT);
        addView(layout,lll);
        ImageView icon = new ImageView(mContext);

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(80, width - 100);
        ll.gravity = Gravity.CENTER_VERTICAL;
        icon.setImageResource(R.mipmap.city_more);
        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, CityPickActivity.class));
            }
        });
        addView(icon, ll);
    }

}

