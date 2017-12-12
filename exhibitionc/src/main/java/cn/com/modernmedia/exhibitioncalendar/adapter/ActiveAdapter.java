package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.ActiveActivity;
import cn.com.modernmedia.exhibitioncalendar.model.ActiveListModel.ActiveModel;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;
import cn.com.modernmedia.exhibitioncalendar.view.ViewHolder;

/**
 * Created by Eva. on 2017/12/6.
 */

public class ActiveAdapter extends BaseAdapter {
    private Context mContext;
    private List<ActiveModel> mItemList = new ArrayList<>();

    public ActiveAdapter(Context context, List<ActiveModel> mItemList) {
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
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ActiveModel item = mItemList.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, R.layout.item_list);

        TextView title = viewHolder.getView(R.id.l_title);
        TextView date = viewHolder.getView(R.id.l_date);
        ImageView img = viewHolder.getView(R.id.l_img);
        TextView acButton = viewHolder.getView(R.id.l_active);
        TextView status = viewHolder.getView(R.id.l_status);
        acButton.setVisibility(View.VISIBLE);
        status.setText(R.string.my_active);
        int width = MyApplication.width - 20;
        int height = width * 5 / 12;
        img.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        title.setText(item.getTitle());

        date.setText(Tools.getStringToDate(item.getStartTime()) + "-" + Tools.getStringToDate(item.getEndTime()));
        acButton.setText(item.getState_desc());

        MyApplication.finalBitmap.display(img, item.getCoverImg());
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, ActiveActivity.class);
                i.putExtra(UriParse.DETAILACTIVE, item);
                mContext.startActivity(i);
            }
        });
        return viewHolder.getConvertView();
    }


}
