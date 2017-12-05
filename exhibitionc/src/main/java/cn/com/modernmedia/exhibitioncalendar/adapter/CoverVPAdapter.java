package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.model.RecommandModel;

/**
 * Created by Eva. on 17/4/10.
 */

public class CoverVPAdapter extends PagerAdapter {
    protected List<CalendarModel> recos = new ArrayList<>();
    protected Context mContext;
    private int type;// 0:cover ,1:touming

    public CoverVPAdapter(Context context, RecommandModel recommandModel, int type) {
        this.mContext = context;
        this.recos.addAll(recommandModel.getRecommandModels());
        this.type = type;
    }


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
        View view = null;
        if (type == 0) {
            view = fetchView(recos.get(position));
            container.addView(view);
        } else {
            view = new View(mContext);
        }
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
        MyApplication.finalBitmap.display(view, detail.getBackgroundImg());
        return view;
    }
}