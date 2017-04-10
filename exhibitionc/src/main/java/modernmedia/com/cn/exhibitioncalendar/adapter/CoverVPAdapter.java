package modernmedia.com.cn.exhibitioncalendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import modernmedia.com.cn.exhibitioncalendar.MyApplication;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel.CalendarModel;

/**
 * Created by Eva. on 17/4/10.
 */

public class CoverVPAdapter extends PagerAdapter {
    protected List<CalendarModel> list = new ArrayList<CalendarModel>();
    protected Context mContext;

    public CoverVPAdapter(Context context, List<CalendarModel> list) {
        this.mContext = context;
        this.list = list;

    }


    @Override
    public int getCount() {
        return list.size();
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
        ImageView view = new ImageView(mContext);
        MyApplication.finalBitmap.display(view,detail.getBackgroundImg());
        return view;
    }
}