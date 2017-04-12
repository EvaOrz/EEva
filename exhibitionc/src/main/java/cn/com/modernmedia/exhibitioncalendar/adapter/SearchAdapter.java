package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.CalendarDetailActivity;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;
import cn.com.modernmedia.exhibitioncalendar.view.ViewHolder;

/**
 * Created by Eva. on 16/9/18.
 */
public class SearchAdapter extends CheckScrollAdapter<CalendarModel> {
    protected Context mContext;
    protected List<CalendarModel> mItemList = new ArrayList<>();

    public SearchAdapter(Context context) {
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
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, R.layout.item_search);

        TextView title = viewHolder.getView(R.id.s_title);
        TextView desc = viewHolder.getView(R.id.s_desc);
        ImageView img = viewHolder.getView(R.id.s_img);

        title.setText(item.getTitle());
        desc.setText(item.getContent());
        MyApplication.finalBitmap.display(img, item.getImg());
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

