package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.corelib.widget.RoundAngleImageView;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.CalendarDetailActivity;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;
import cn.com.modernmedia.exhibitioncalendar.view.ViewHolder;

/**
 * 展览、活动的adapter
 * Created by Eva. on 17/4/8.
 */

public class ExhibitionAdapter extends BaseAdapter {
    private Context mContext;
    private List<CalendarModel> mItemList = new ArrayList<>();

    public ExhibitionAdapter(Context context, List<CalendarModel> mItemList) {
        this.mItemList = mItemList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CalendarModel getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CalendarModel item = mItemList.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, R.layout.item_list);

        TextView title = viewHolder.getView(R.id.l_title);
        TextView date = viewHolder.getView(R.id.l_date);
        RoundAngleImageView img = viewHolder.getView(R.id.l_img);
        img.setTag(R.id.scale_type , "CENTER_CROP");
        TextView status = viewHolder.getView(R.id.l_status);

        int width = MyApplication.width - 20;
        int height = width * 5 / 12;
        img.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        title.setText(item.getTitle());

        date.setText(Tools.getStringToDate(item.getStartTime()) + "-" + Tools.getStringToDate(item.getEndTime()));

        switch (Tools.getCalendarStatus(item.getStartTime(),item.getEndTime())){
            case 1:
                status.setText(R.string.calen_1);
                break;
            case 2:
                status.setText(R.string.calen_2);
                break;
            case 3:
                status.setText(R.string.calen_3);
                break;

        }
        MyApplication.finalBitmap.display(img, item.getCoverImg());
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


}

