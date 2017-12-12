package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.MuseumDetailActivity;
import cn.com.modernmedia.exhibitioncalendar.model.MuseumListModel.MuseumModel;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;
import cn.com.modernmedia.exhibitioncalendar.view.ViewHolder;

/**
 * Created by Eva. on 2017/12/8.
 */

public class FavMuseumAdapter extends BaseAdapter

{
    private Context mContext;
    private List<MuseumModel> mItemList = new ArrayList<>();

    public FavMuseumAdapter(Context context, List<MuseumModel> mItemList) {
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
    public MuseumModel getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MuseumModel item = mItemList.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, R.layout.item_fav_museum);

        TextView title1 = viewHolder.getView(R.id.l_title);
        TextView title2 = viewHolder.getView(R.id.l_title1);
        ImageView img = viewHolder.getView(R.id.l_img);
        TextView title3 = viewHolder.getView(R.id.l_title2);

        title1.setText(item.getTitle());
        title2.setText(item.getTitleEn());
        title3.setText(item.getAddress());

        MyApplication.finalBitmap.display(img, item.getCoverImg());
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, MuseumDetailActivity.class);
                i.putExtra(UriParse.DETAILMUSEUM, item.getMuseumId());
                mContext.startActivity(i);
            }
        });
        return viewHolder.getConvertView();
    }


}

