package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.ActiveActivity;
import cn.com.modernmedia.exhibitioncalendar.activity.CalendarDetailActivity;
import cn.com.modernmedia.exhibitioncalendar.activity.MuseumDetailActivity;
import cn.com.modernmedia.exhibitioncalendar.model.ActiveListModel;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.model.RecommandModel;
import cn.com.modernmedia.exhibitioncalendar.util.FlurryEvent;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;

/**
 * Created by Eva. on 17/4/10.
 */

public class DetailVPAdapter extends PagerAdapter {
    protected List<CalendarModel> recos = new ArrayList<>();
    private List<ActiveListModel.ActiveModel> acts = new ArrayList<>();
    protected Context mContext;

    public DetailVPAdapter(Context context, RecommandModel recommandModel) {
        this.mContext = context;
        this.recos.addAll(recommandModel.getRecommandModels());
        acts.clear();
        acts.addAll(recommandModel.getActiveModels());

    }

    /**
     * 目前支持展览、展馆、活动
     *
     * @return
     */
    @Override
    public int getCount() {
        return recos.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View view = fetchView(recos.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
    }

    /**
     * 获取view.默认只有只有imageview，如果是特殊view,请重载此方法
     *
     * @param detail
     * @return
     */
    public View fetchView(final CalendarModel detail) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recommond, null);
        TextView title = (TextView) view.findViewById(R.id.recommond_title);
        TextView date = (TextView) view.findViewById(R.id.recommond_date);
        TextView city = (TextView) view.findViewById(R.id.recommond_address);

        title.setText(detail.getTitle());
        if (!TextUtils.isEmpty(detail.getStartTime()))
            date.setText(Tools.getStringToDate(detail.getStartTime()) + "-" + Tools.getStringToDate(detail.getEndTime()));
        if (detail.getMuseumModel() != null) {
            city.setText(detail.getMuseumModel().getTitle());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // flurry log
                FlurryEvent.logACClickHomeCarousel(mContext);
                if (detail.getType() == 1) {// 展览详情
                    Intent i = new Intent(mContext, CalendarDetailActivity.class);
                    i.putExtra(UriParse.DETAILCALENDAR, detail.getItemId());
                    mContext.startActivity(i);
                } else if (detail.getType() == 2) {// 展馆详情
                    Intent i = new Intent(mContext, MuseumDetailActivity.class);
                    i.putExtra(UriParse.DETAILMUSEUM, detail.getItemId());
                    mContext.startActivity(i);
                } else if (detail.getType() == 3) {// 活动
                    for (ActiveListModel.ActiveModel a : acts) {
                        if (detail.getItemId().equals(a.getActiveId() + "")) {
                            a.setBackgroundImg(detail.getBackgroundImg());
                            Intent i = new Intent(mContext, ActiveActivity.class);
                            i.putExtra(UriParse.DETAILACTIVE, a);
                            mContext.startActivity(i);
                        }
                    }

                }
            }
        });
        return view;
    }


}