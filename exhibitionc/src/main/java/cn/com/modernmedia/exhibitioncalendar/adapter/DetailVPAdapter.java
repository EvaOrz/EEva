package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.CalendarDetailActivity;
import cn.com.modernmedia.exhibitioncalendar.activity.CalendarListActivity;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;

/**
 * Created by Eva. on 17/4/10.
 */

public class DetailVPAdapter extends PagerAdapter {
    protected List<CalendarModel> list = new ArrayList<CalendarModel>();
    protected Context mContext;

    public DetailVPAdapter(Context context, List<CalendarModel> list) {
        this.mContext = context;
        this.list = list;

    }


    @Override
    public int getCount() {
        return 5;
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
        final View view = fetchView(list.get(position));
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
        date.setText(Tools.getStringToDate(detail.getStartTime()) + "-" + Tools.getStringToDate(detail.getEndTime()));
        if (ParseUtil.listNotNull(detail.getCitylist())) {
            city.setText(detail.getCitylist().get(0).getTagName());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail.getType() == 0) {// 展览详情

                    Intent i = new Intent(mContext, CalendarDetailActivity.class);
                    i.putExtra(UriParse.DETAILCALENDAR, detail.getItemId());
                    mContext.startActivity(i);
                } else if (detail.getType() == 3) {// 专题样式
                    String weburl = detail.getWeburl();
                    String[] ss = weburl.split(",");
                    if (ss.length == 2) {
                        Intent i = new Intent(mContext, CalendarListActivity.class);
                        i.putExtra("list_tagid", ss[0]);
                        i.putExtra("list_tagname", ss[1]);
                        i.putExtra("list_title", ss[1]);
                        i.putExtra(UriParse.DETAILCALENDAR, detail.getItemId());
                        mContext.startActivity(i);
                    }
                }
            }
        });
        return view;
    }


}