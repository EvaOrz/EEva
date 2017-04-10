package modernmedia.com.cn.exhibitioncalendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import modernmedia.com.cn.exhibitioncalendar.MyApplication;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.activity.CalendarDetailActivity;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import modernmedia.com.cn.exhibitioncalendar.util.UriParse;
import modernmedia.com.cn.exhibitioncalendar.view.ViewHolder;

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

        TextView time = viewHolder.getView(R.id.l_time);
        TextView title = viewHolder.getView(R.id.l_title);
        TextView city = viewHolder.getView(R.id.l_city);
        TextView date = viewHolder.getView(R.id.l_date);
        ImageView img = viewHolder.getView(R.id.l_img);

        title.setText(item.getTitle());
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

