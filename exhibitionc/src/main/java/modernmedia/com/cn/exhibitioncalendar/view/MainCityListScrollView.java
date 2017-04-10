package modernmedia.com.cn.exhibitioncalendar.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import modernmedia.com.cn.corelib.util.ParseUtil;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.activity.CalendarDetailActivity;
import modernmedia.com.cn.exhibitioncalendar.api.UrlMaker;
import modernmedia.com.cn.exhibitioncalendar.model.TagListModel.HouseOrCity;
import modernmedia.com.cn.exhibitioncalendar.util.UriParse;

/**
 * top_menu view
 *
 * @author lusiyuan
 */
public class MainCityListScrollView extends RelativeLayout {

    private Context mContext;
    private HorizontalScrollView scrollView;
    private LinearLayout layout;
    /**
     * 由于可能是独立栏目，并且绑定在列表上，导致每次切换的时候都还原，所以设置成静态变量
     */
    public static int selectPosition = -1;


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

            name.setText(tagInfo.getTagName());
            time.setText(Tools.getTimeFromZone(tagInfo.getTimeZone()));
            MyApplication.finalBitmap.display(icon, UrlMaker.HOST + tagInfo.getBackImg().getSourceV6PlusImg());
            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, CalendarDetailActivity.class);
                    i.putExtra(UriParse.DETAILCALENDAR, tagInfo.getTagId());
                    mContext.startActivity(i);
                }
            });


            layout.addView(viewHolder.getConvertView());
        }
        scrollView.addView(layout);

    }

}

