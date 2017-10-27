package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.CalendarDetailActivity;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;
import cn.com.modernmedia.exhibitioncalendar.view.ListItemMenuView;
import cn.com.modernmedia.exhibitioncalendar.view.ViewHolder;

/**
 * Created by Eva. on 17/4/8.
 */

public class ExhibitionAdapter extends CheckScrollAdapter<CalendarModel> {
    protected Context mContext;
    protected List<CalendarModel> mItemList = new ArrayList<>();

    public ExhibitionAdapter(Context context) {
        super(context);
        mContext = context;
    }

    public void setData(List<CalendarModel> mItemList) {
        isScroll = false;
        synchronized (mItemList) {
            for (CalendarModel item : mItemList) {
                add(item);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CalendarModel item = getItem(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, R.layout.item_list);

        TextView title = viewHolder.getView(R.id.l_title);
        TextView city = viewHolder.getView(R.id.l_city);
        TextView date = viewHolder.getView(R.id.l_date);
        ImageView img = viewHolder.getView(R.id.l_img);

        int width = MyApplication.width - 20;
        int height = width * 9 / 16;
        img.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        title.setText(item.getTitle());
        // 显示用户自己设置的时间
        if (!TextUtils.isEmpty(item.getTime())) {
            date.setText(item.getTime());
        } else
            date.setText(Tools.getStringToDate(item.getStartTime()) + "-" + Tools.getStringToDate(item.getEndTime()));

        if (ParseUtil.listNotNull(item.getCitylist())) {
            city.setText(item.getCitylist().get(0).getTagName());
        }
        MyApplication.finalBitmap.display(img, item.getCoverImg());
        final ImageView sandian = viewHolder.getView(R.id.sandian);
        sandian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ListItemMenuView(mContext, item, sandian);
            }
        });
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, CalendarDetailActivity.class);
                i.putExtra(UriParse.DETAILCALENDAR, item.getItemId());
                mContext.startActivity(i);
            }
        });
        return viewHolder.getConvertView();
    }

    public void clearData() {
        mItemList.clear();
    }

    public boolean isScroll() {
        return isScroll;
    }


}

