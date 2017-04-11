package modernmedia.com.cn.exhibitioncalendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import modernmedia.com.cn.corelib.util.ParseUtil;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.activity.CalendarDetailActivity;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import modernmedia.com.cn.exhibitioncalendar.util.UriParse;

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
                Intent i = new Intent(mContext, CalendarDetailActivity.class);
                i.putExtra(UriParse.DETAILCALENDAR, detail.getItemId());
                mContext.startActivity(i);
            }
        });
        return view;
    }


}